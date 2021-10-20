package top.iotequ.svasclient;

import org.springframework.context.ApplicationEvent;

public class TemplatesChangedEvent extends ApplicationEvent {
	private static final long serialVersionUID = 1L;
	public TemplatesChangedEvent(Object source,String userNo,String mode,int fingers) {
		super(source);
		this.userNo=userNo;
		this.mode=mode;
		this.fingers=fingers;
	}
	private String userNo;
	private String mode;
	private int fingers;
	public String getUserNo() {
		return userNo;
	}
	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public int getFingers() {
		return fingers;
	}
	public void setFingers(int fingers) {
		this.fingers = fingers;
	}
}
