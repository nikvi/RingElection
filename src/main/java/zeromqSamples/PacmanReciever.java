package zeromqSamples;

import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;

public class PacmanReciever implements Runnable {
	
	private static PacmanReciever nodeInstance;
	private static Thread nodeThread;
	private boolean execute;
    ZContext context;
	Socket subscriber;
	String electedLeaderID;
	Callback callback;
	
	public PacmanReciever(String electedLeaderID, Callback callback) {
		this.context = new ZContext();
		this.subscriber = context.createSocket(ZMQ.SUB);
		this.electedLeaderID = electedLeaderID;
		this.callback = callback;
	}
	
	public static void initializePacmanClient(String subscription, Callback callback){
		nodeInstance = new PacmanReciever(subscription, callback);
		nodeInstance.execute = true;
		nodeThread = new Thread(nodeInstance);
		nodeThread.start();
		
	}
	public static void destroy(){
		nodeInstance.subscriber.close();
		nodeInstance.context.destroy();
		nodeInstance.execute =false;
		nodeThread.interrupt();
		nodeThread = null;
	}
	
	

	public void run() {
		//Settings.getSubscriberURL()
		this.subscriber.connect("Leader node value here");
		this.subscriber.subscribe(electedLeaderID.getBytes());
		while (this.execute){
			System.out.println("while true...");
			byte[] topic = this.subscriber.recv();
			
			System.out.println("----- topic = " + topic);
            if (topic == null)
                continue;
            
            byte[] data = this.subscriber.recv();
            this.callback.onMessage(data);
		}
		
		
	}

	public static interface Callback {
		public void onMessage(byte[] data);
	}
}
