package starterClasses;

import zeromqSamples.Forwarder;
import zeromqSamples.PacmanDealer;

public class LeaderStart {
	
	//can make it start server code from this
	public static void main(String[] args) throws InterruptedException{
		
		Thread relay = new Thread(new Forwarder());
		relay.setDaemon(true);
		PacmanDealer.initialize();
		relay.start();
		System.out.println("Server has started");

		relay.join();
		
	}


}
