package zeromqSamples;

import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;

public class PacmanDealer {
	
	ZContext context;
	Socket publisher;
	
   
	private static PacmanDealer instance;
	
	public static void initialize() {
		instance = new PacmanDealer();
		
	}
	
	public static void destroy(){
		instance.publisher.close();
		instance.context.destroy();
		instance = null;
	}
	
	private PacmanDealer() {
		this.context = new ZContext();
		this.publisher= context.createSocket(ZMQ.XPUB);
		//election value to stored and added her
		this.publisher.connect("url val");
		
	}
	
	public static void sendList(String subscription,Object tosend){
		System.out.println("------- sending vector list");
		//PointVector pntVector = SerializationUtil.serializeData(update);
		//System.out.println(subscription + " ---- ");
		instance.publisher.sendMore(subscription);
		//instance.publisher.send( );
	}

}
