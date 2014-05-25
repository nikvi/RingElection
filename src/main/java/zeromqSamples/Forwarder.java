package zeromqSamples;


import java.util.HashMap;
import java.util.Map;

import org.zeromq.ZContext;
import org.zeromq.ZFrame;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.PollItem;
import org.zeromq.ZMQ.Socket;

import utils.Variables;



/*
 * author : Nikki
 * Relay server is a simple relay which stores and forwards the last value obtained for a topic
 * The queue is hosted by relay server.
 */

public class Forwarder implements Runnable {
	
	  public void run()
	    {
	        ZContext context = new ZContext();
	        Socket frontend = context.createSocket(ZMQ.SUB);
	        frontend.bind("tcp://*:5557");
	        
	        Socket backend = context.createSocket(ZMQ.XPUB);
	        backend.bind("tcp://*:5558");
	        
	        frontend.subscribe("".getBytes());

	        Map<String, byte[]> cache = new HashMap<String, byte[]>();
	        while (true) {
	            PollItem[] items = {
	                    new PollItem(frontend, ZMQ.Poller.POLLIN),
	                    new PollItem(backend, ZMQ.Poller.POLLIN),
	            };
	            
	            if (ZMQ.poll(items, 1000) == -1)
	                break;              //  Interrupted

	            //  Any new topic data we cache and then forward
	            if (items[0].isReadable()) {
	                String topic = frontend.recvStr();
	                byte[] current = frontend.recv();

	                if (topic == null)
	                    break;
	                cache.put(topic, current);
	                backend.sendMore(topic);
	                backend.send(current);
	            }
	            
	            //  .split handle subscriptions
	            //  When we get a new subscription, we pull data from the cache:
	            if (items[1].isReadable()) {
	                ZFrame frame = ZFrame.recvFrame(backend);
	                if (frame == null)
	                    break;
	                
	                byte[] event = frame.getData();
	                if (event [0] == 1) {
	                    String topic = new String(event, 1, event.length -1);
	                    System.out.printf ("Client subscribed for %s\n", topic);
	                    byte[] previous = cache.get(topic);
	                    if (previous != null) {
	                        backend.sendMore(topic);
	                        backend.send(previous);
	                }
	                }
	                frame.destroy();
	            }
	        }
	        context.destroy();
	    }

}
