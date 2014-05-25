package registry;

public class StartNetwork {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int numProc=2;
		
		RegistryServer nt = new RegistryServer(numProc);
		
		try {
			Thread ntThread = new Thread(nt);
			ntThread.start();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
