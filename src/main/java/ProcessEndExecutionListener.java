import java.util.Date;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

import com.espertech.esper.client.EPServiceProviderManager;

public class ProcessEndExecutionListener implements ExecutionListener {

	  @Override
	  public void notify(DelegateExecution execution) throws Exception {
	    LeaveRequestProcessedEvent event = new LeaveRequestProcessedEvent (
	      execution.getId(), 
	      new Date().getTime(),
	      (Boolean) execution.getVariable("requestApproved"),
	      (Integer) execution.getVariable("leaveDay"));
	    EPServiceProviderManager.getDefaultProvider().getEPRuntime()
	      .getEventSender("LeaveRequestProcessedEvent")
	      .sendEvent(event);
	  }
	}