package registry;

import java.util.UUID;

public class Identity {
	/**
	 * Data object used by sever to store the manger sessions and hosted boards.
	 * @author nikki
	 *
	 */
	    String address;
	    //this allows the registry to provide position of the node in ring, not the process id
	    UUID tokenID;
		public Identity(String address,UUID uid) {
			super();
			this.address = address;
			this.tokenID = uid;
		}
		public UUID getTokenID() {
			return tokenID;
		}
		public void setTokenID(UUID tokenID) {
			this.tokenID = tokenID;
		}
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}

	}

