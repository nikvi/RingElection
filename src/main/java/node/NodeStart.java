package node;

import java.util.concurrent.TimeUnit;

public class NodeStart {
	
	public static void main(String[] args) throws Exception{
		Thread t1 = new Thread(new SingleNode("6001"));
		Thread t2 = new Thread(new SingleNode("6002"));
		//Thread t3 = new Thread(new SingleNode("6003"));
		
		t1.start();
		//sleep();
		t2.start();
		//sleep();
		//t3.start();
		
		t1.join();
		t2.join();
		//t3.join();
	}
	
	
}
