package node;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import model.Acknowledge;
import model.ElectionMessages;
import model.ElectionMessages.MessageType;
import model.Response;

import org.apache.commons.lang.SerializationUtils;
import org.zeromq.ZMQ;

import utils.SerializationUtil;
import utils.Variables;

public class SingleNode implements Runnable{
	public SingleNode(String port) {
		super();
		this.port = port;
		this.myUID = UUID.randomUUID();
		this.initialize();
	}

	String nTcpPoint;
	String tcpEndpoint;
	String port;
	UUID myUID;
	volatile Boolean electionStart;
	String leader;
	UUID elected_id = null;
	Boolean msgRec;
	Boolean elected = Boolean.FALSE;
	ElectionState state;

	public enum ElectionState {
		NON_PARTICIPANT, PARTICIPANT
	}

	public void initialize() {
		System.out.println("Initializing Proc " + this.myUID);
		this.state = ElectionState.NON_PARTICIPANT;
		this.msgRec = false;
	}

	String _hostNetTopology;

	// int _portNetTopology;
	public void startConnection() {
		ZMQ.Context context = ZMQ.context(1);
		ZMQ.Socket socket = context.socket(ZMQ.REQ);
		String localAddr = null;
		socket.connect("tcp://localhost:5555");
		// try {
		localAddr = this.port;// InetAddress.getLocalHost().getHostAddress();
		// } catch (UnknownHostException e) {

		// e.printStackTrace();
		// }
		this.tcpEndpoint = "tcp://" + localAddr;
		
		System.out.println("My UUID is :" + this.myUID);
		Response objectRes = new Response(this.myUID, localAddr,
				Variables.ALIVE);
		socket.send(SerializationUtil.fromJavaToByteArray(objectRes), 0);
		byte[] rawBytes = socket.recv(0);
		Object command = SerializationUtil.fromByteArrayToJava(rawBytes);
		if (command instanceof Acknowledge) {
			Acknowledge msg = (Acknowledge) command;
			System.out.println((msg).getPosition());
		}

		socket.close();
		context.term();

	}

	public void getNieghbour() {
		// TODO Auto-generated method stub
		ZMQ.Context context1 = ZMQ.context(1);
		ZMQ.Socket server = context1.socket(ZMQ.REP);
		System.out.println("tcp://*:" + port);
		server.bind("tcp://*:" + port);
		Object val;
		while (true) {
			System.out.println("In loop");
			byte[] nData = server.recv();
			System.out.println(nData);
			val = SerializationUtil.fromByteArrayToJava(nData);
			Acknowledge ack = new Acknowledge(Variables.ACK, 0);
			System.out.println("Got data!!!!");
			server.send(SerializationUtil.fromJavaToByteArray(ack));
			if ((val instanceof Response)
					&& (((Response) val).getRequestType()
							.equalsIgnoreCase(Variables.NNValue)))
				break;
		}
		this.nTcpPoint = ((Response) val).getTcpEndpoint();
		System.out.println(port + ": Got nieghbour value: " + nTcpPoint);
		server.close();
		context1.term();
	}
	/*
	public void processMssg(ElectionMessages em){
		ElectionMessages.MessageType type = em.getMsgType();
		boolean recvGrtr = false;
		
		if (type == MessageType.ELECTION){
			if (//myUID != null &&// !myUID.equals(em.getmID())){
				if (em.getmID() != null && myUID.compareTo(em.getmID()) == 1) {
					em.setmID(this.myUID);
				} else {
					recvGrtr = true;
				}
				
				if (state == ElectionState.NON_PARTICIPANT || (state == ElectionState.PARTICIPANT && recvGrtr)) {
					System.out.println("Forwarding message");
					sendMessage(em);
					this.state = ElectionState.PARTICIPANT;
				}
			} else {
				ElectionMessages elected = new ElectionMessages(ElectionMessages.MessageType.ELECTED, this.myUID, this.myUID);
				this.state = ElectionState.NON_PARTICIPANT;
				System.out.println(port+ ": Elected!!");
				sendMessage(elected);
			}
		} else if (type == MessageType.ELECTED){
			this.state = ElectionState.NON_PARTICIPANT;
			this.elected_id = em.getElectedUID();
			if (elected_id != this.myUID) {
				System.out.println(port + ": forwarding "+ em);
				sendMessage(em);
			}
			this.elected = Boolean.TRUE;
		}
	}
	*/
	public void processMessages(ElectionMessages em) {
		ElectionMessages.MessageType type = em.getMsgType();
		System.out.println("port:"+port+ " mssg "+ em.getMsgType());
		boolean rGreater = Boolean.FALSE;

		if (type == ElectionMessages.MessageType.ELECTION) {
			if (em.getmID() != null) {
				int compare = this.myUID.compareTo(em.getmID());
				System.out.println("port:"+port+ ": comaring values"+compare);
				if (compare != 0) {
					if (compare == 1) {
						// mID stores the largest election ID seen so far
						em.setmID(this.myUID);
					} else {
						rGreater = Boolean.TRUE;
					}
					if (state == ElectionState.NON_PARTICIPANT
							|| (state == ElectionState.PARTICIPANT && rGreater)) {
						System.out.println("Forwarding message");
						sendMessage(em);
						this.state = ElectionState.PARTICIPANT;
					}
				} else {
					ElectionMessages elected = new ElectionMessages(ElectionMessages.MessageType.ELECTED, this.myUID, this.myUID);
					this.state = ElectionState.NON_PARTICIPANT;
					System.out.println(port+ ": Elected!!");
					sendMessage(elected);
				}

			} else {
				em.setmID(this.myUID);
				System.out.println(port + "initial send");
				sendMessage(em);
				this.state = ElectionState.PARTICIPANT;
			}
		} else if (type == ElectionMessages.MessageType.ELECTED) {
			this.state = ElectionState.NON_PARTICIPANT;
			this.elected_id = em.getElectedUID();
			if (elected_id != this.myUID) {
				sendMessage(em);
			}
			this.elected = Boolean.TRUE;
		}
	}

	void sendMessage(ElectionMessages em) {
		ZMQ.Context context = ZMQ.context(1);
		ZMQ.Socket socket = context.socket(ZMQ.REQ);
		// sending data to nieghbour
		socket.connect("tcp://*:" + this.nTcpPoint);
		socket.send(SerializationUtil.fromJavaToByteArray(em),0);
		System.out.println("Send data to right nieghbour");
		byte[] ackResp = socket.recv(0);
		Object resp = SerializationUtil.fromByteArrayToJava(ackResp);
		if (resp instanceof Acknowledge) {
			System.out.println("Recieved acknowledgement");
		}
	}

	public void listenforMessages() {
		ZMQ.Context context1 = ZMQ.context(1);
		ZMQ.Socket server = context1.socket(ZMQ.REP);
		System.out.println("tcp://*:" + port);
		server.bind("tcp://*:" + port);
		do {
			byte[] nData = server.recv();
			server.send(SerializationUtil.fromJavaToByteArray(new Acknowledge("Sent",1)));
			Object msg = SerializationUtil.fromByteArrayToJava(nData);
			if (msg instanceof ElectionMessages) {
				System.out.println(port + ": listenForMssg true");
				this.processMessages((ElectionMessages) msg);
				//this.processMssg((ElectionMessages) msg);
			}
			//Acknowledge ack = ;
			//

		} while (!this.elected);

	}
	
	public void startElection(boolean startE) {

		electionStart = startE;
     }

	public void detectStart() {
		if (electionStart) {
			System.out.println("Starting election");
			ElectionMessages aMsg = new ElectionMessages(
					ElectionMessages.MessageType.ELECTION, null, this.myUID);
			sendMessage(aMsg);
			this.state = ElectionState.PARTICIPANT;
			electionStart = false;
		}
	}

	public void run() {
		startConnection();
		getNieghbour();
	
		electionStart = port.endsWith("1");//(elected_id == null);
		
		//sleep(Integer.parseInt(port));
		Thread listener = new Thread(new Runnable() {
			public void run() {
				listenforMessages();
			}
		});
		
		listener.start();
		detectStart();
		try {
			listener.join();
		} catch (Exception ex){
			ex.printStackTrace();
		}
	}
	
	private static void sleep(int i){
		try {
			System.out.println(i - 6000);
			Thread.sleep(TimeUnit.MILLISECONDS.convert(i - 6000, TimeUnit.SECONDS));
			System.out.println("Woken");
		} catch (InterruptedException ex){
			ex.printStackTrace();
		}
	}
}
