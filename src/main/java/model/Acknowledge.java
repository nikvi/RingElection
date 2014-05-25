package model;

import java.io.Serializable;

public class Acknowledge implements Serializable {
	
	String response;
	int position;

	public Acknowledge(String ack,int position) {
		this.response = ack;
		this.position = position;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

}
