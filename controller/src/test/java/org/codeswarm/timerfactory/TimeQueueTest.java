package org.codeswarm.timerfactory;

import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TimeQueueTest {

  private TimerQueue queue;
  private TimeProvider timeProvider;
  private TimerTask task;

  @BeforeMethod
  public void setUp() throws Exception {
    queue = new TimerQueue();
    timeProvider = new TimeProviderImpl();
    task = Mockito.mock(TimerTask.class);
  }

  private ControlledTimer timer(TimerTask task) {
    return new ControlledTimer(task, timeProvider, queue);
  }

  private ControlledTimer timer() {
    return timer(task);
  }

  @Test
  public void testEmptyQueue() throws Exception {
    Assert.assertEquals(queue.peek(), null);
  }

  @Test
  public void testOneScheduledItem() throws Exception {
    ControlledTimer timer = timer();
    timer.schedule(5);
    Assert.assertEquals(queue.peek(), timer);
  }

  @Test
  public void testOneCancelledItem() throws Exception {
    ControlledTimer timer = timer();
    timer.schedule(5);
    timer.cancel();
    Assert.assertEquals(queue.peek(), null);
  }

  private static class TimeProviderImpl implements TimeProvider {

    Time time = new Time(0l);

    @Override
    public Time currentTime() {
      return time;
    }

  }

}
