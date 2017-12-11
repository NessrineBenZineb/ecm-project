import java.util.Date;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

import com.espertech.esper.client.EPServiceProviderManager;

public class ProcessStartExecutionListener implements ExecutionListener {
	  @Override
	  public void notify(DelegateExecution execution) throws Exception {
	    LeaveRequestReceivedEvent event = new LeaveRequestReceivedEvent(
	        execution.getId(), 
	        new Date().getTime(), 
	        (Integer) execution.getVariable("leaveDay"));
	        EPServiceProviderManager.getDefaultProvider().getEPRuntime()
	        .getEventSender("LeaveRequestReceivedEvent")
	        .sendEvent(event);
	  }
}
