import javax.xml.bind.annotation.XmlElement;

public class LeaveRequestProcessedEvent {

	// For correlating the events.
	@XmlElement
	private final String processInstanceId;
	@XmlElement
	private final long processedTime;
	@XmlElement
	private final boolean requestApproved;
	@XmlElement
	private final int requestedDay;
	@SuppressWarnings("unused")
	public LeaveRequestProcessedEvent() {
	processInstanceId = null;
	processedTime = 0;
	requestApproved = false;
	requestedDay = 0;
	}
	public LeaveRequestProcessedEvent(String processInstanceId, long
	processedTime, boolean requestApproved, int requestedDay) {
	this.processInstanceId = processInstanceId;
	this.processedTime = processedTime;
	this.requestApproved = requestApproved;
	this.requestedDay = requestedDay;
	}
	
	public String getProcessInstanceId() {
		return processInstanceId;
		}
		public long getProcessedTime() {
		
		return processedTime;
		}
		public boolean isRequestApproved() {
		return requestApproved;
		}
		public int getRequestedDay() {
		return requestedDay;
		}
		@Override
		public String toString() {
		return "LeaveRequestProcessedEvent{processInstanceId="+processInstanceId+",processedTime="+processedTime+",requestApproved="+requestApproved+",requestedDay="+requestedDay+"}";
	
}
}
