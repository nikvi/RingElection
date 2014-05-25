package model;

import java.io.Serializable;
import java.util.UUID;

public class Response implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4189545298848825399L;
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	String requestType;
	UUID id;
	String tcpEndpoint;
	
	public Response(UUID uid, String tcpEndpoint,String requestType) {
		super();
		this.id = uid;
		this.tcpEndpoint = tcpEndpoint;
		this.requestType = requestType;
	}
	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	public String getTcpEndpoint() {
		return tcpEndpoint;
	}
	public void setTcpEndpoint(String tcpEndpoint) {
		this.tcpEndpoint = tcpEndpoint;
	}
	
	
	
}
