package zeromqSamples;


public class SampleClient {
	
	public static void main(String[] args) throws InterruptedException{
	String leaderUID="";
	
	PacmanReciever.initializePacmanClient(leaderUID, new PacmanReciever.Callback() {
		
		public void onMessage(byte[] data) {
			// TODO Auto-generated method stub
			
		}
	});
	}
}
