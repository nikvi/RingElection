package model;

import java.awt.TrayIcon.MessageType;
import java.io.Serializable;
import java.util.UUID;

public class ElectionMessages implements Serializable{

	private MessageType msgType;
    private UUID electedUID;
    private UUID mID;
    
    public enum MessageType {
        ELECTION, ELECTED
}
    
   public ElectionMessages(MessageType msgType, UUID electedUID, UUID mID) {
		super();
		this.msgType = msgType;
		this.electedUID = electedUID;
		this.mID = mID;
	}

	public ElectionMessages() {
		super();
	}

	public MessageType getMsgType() {
		return msgType;
	}

	public void setMsgType(MessageType msgType) {
		this.msgType = msgType;
	}

	public UUID getElectedUID() {
		return electedUID;
	}

	public void setElectedUID(UUID electedUID) {
		this.electedUID = electedUID;
	}

	public UUID getmID() {
		return mID;
	}

	public void setmID(UUID mID) {
		this.mID = mID;
	}
    
    
    
}
