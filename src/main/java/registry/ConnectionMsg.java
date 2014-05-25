package registry;

import java.io.Serializable;
import java.util.UUID;

public class ConnectionMsg implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -415937655914005639L;
	String tcpAddress;
    UUID uniqueID;
	public ConnectionMsg(UUID uid,String tcpAddress) {
		super();
		this.tcpAddress = tcpAddress;
		this.uniqueID = uid;
	}

	public ConnectionMsg(Identity identity) {
		// TODO Auto-generated constructor stub
		super();
		this.tcpAddress = identity.address;
		this.uniqueID = identity.tokenID;
	}

	public UUID getUniqueID() {
		return uniqueID;
	}

	public void setUniqueID(UUID uniqueID) {
		this.uniqueID = uniqueID;
	}

	public String getTcpAddress() {
		return tcpAddress;
	}

	public void setTcpAddress(String tcpAddress) {
		this.tcpAddress = tcpAddress;
	}

}
