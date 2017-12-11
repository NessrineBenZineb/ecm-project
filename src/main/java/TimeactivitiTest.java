import java.util.LinkedList;
import java.util.Queue;

import org.apache.ibatis.session.Configuration;
import org.junit.Before;
import org.junit.Test;

import com.espertech.esper.client.time.CurrentTimeEvent;
import com.espertech.esper.client.time.TimerControlEvent;
import com.espertech.esper.client.time.TimerControlEvent.ClockType;

import junit.framework.Assert;
import com.espertech.esper.client.*;

public class TimeactivitiTest implements UpdateListener  {
	private EPRuntime epRuntime;
	  private EPAdministrator epAdmin;

	  private Queue<Double> avgProcessDurationQueue = new LinkedList<Double>();
	  private Queue<Long> maxProcessDurationQueue = new LinkedList<Long>();
	  @Before
	  public void startEsper() {
	    Configuration configuration = new Configuration();
	  //  configuration.addEventType("",org.activiti.engine.delegate.event);
	    EPServiceProvider epService = EPServiceProviderManager.getProvider("");
	    epRuntime = epService.getEPRuntime();
	    epAdmin = epService.getEPAdministrator();
	  }
	  
	  
	  @Test
	  public void monitorProcessDuration() {
	    epRuntime.sendEvent(new TimerControlEvent(ClockType.CLOCK_EXTERNAL));

	    EPStatement epStatement = epAdmin.createEPL(new StringBuffer()
	        .append("select avg(endEvent.processedTime - beginEvent.receiveTime) as avgProcessDuration, ")
	        .append("max(endEvent.processedTime - beginEvent.receiveTime) as maxProcessDuration ")
	        .append("from pattern [every beginEvent=LeaveRequestReceivedEvent -> endEvent=LeaveRequestProcessedEvent(processInstanceId=beginEvent.processInstanceId)].win:time(5 sec)")
	        .toString());
	    
	    epStatement.addListener(new UpdateListener () {
	      public void update(EventBean[] newEvents, EventBean[] oldEvents) {
	        Assert.assertEquals(1, newEvents.length);
	        Assert.assertNull(oldEvents);
	        Double avgProcessDuration = (Double) newEvents[0].get("avgProcessDuration");
	        Long maxProcessDuration = (Long) newEvents[0].get("maxProcessDuration");
	        System.out.println
	("avgProcessDuration="+avgProcessDuration+", maxProcessDuration="+maxProcessDuration);
	        avgProcessDurationQueue.add(avgProcessDuration);
	        maxProcessDurationQueue.add(maxProcessDuration);
	      }
	    } );
	    
	    sendLeaveRequestReceivedEvent (   0, "1", 100);
	    assertMonitoredProcessDuration(null, null);
	    
	    sendLeaveRequestReceivedEvent ( 300, "2", 200);
	    assertMonitoredProcessDuration(null, null);
	    
	    epStatement.destroy();
	  }
	  
	  
	  private void assertMonitoredProcessDuration(Double avgProcessDuration, Long maxProcessDuration) {
		    Assert.assertEquals(avgProcessDuration, avgProcessDurationQueue.poll());
		    Assert.assertEquals(maxProcessDuration, maxProcessDurationQueue.poll());
		  }

		  private void sendLeaveRequestReceivedEvent(long time, String processInstanceId, int requestedAmount) {
		    sendEvent(time, new LeaveRequestReceivedEvent(processInstanceId, time, requestedAmount));
		  }

		  private void sendLeaveRequestProcessedEvent(long time, String processInstanceId, boolean requestApproved, int LeaveedAmount) {
		    sendEvent(time, new LeaveRequestProcessedEvent());
		  }

		  private void sendEvent(long time, Object event) {
		    System.out.printf(" %1$4d : %2$s\n", time, event);
		    epRuntime.sendEvent(new CurrentTimeEvent(time));
		    epRuntime.sendEvent(event);
		  }


		@Override
		public void update(EventBean[] arg0, EventBean[] arg1) {
			// TODO Auto-generated method stub
			
		}
}
