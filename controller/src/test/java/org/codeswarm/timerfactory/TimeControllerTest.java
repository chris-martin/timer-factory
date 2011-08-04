package org.codeswarm.timerfactory;

import org.mockito.Mockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TimeControllerTest {

  private TimeController controller;
  private TimerTask task;

  @BeforeMethod
  public void setUp() throws Exception {
    controller = new TimeController();
    task = Mockito.mock(TimerTask.class);
  }

  @Test
  public void testNotYet() throws Exception {
    Timer timer = controller.createTimer(task);
    timer.schedule(10);
    controller.advanceTime(9);
    Mockito.verify(task, Mockito.never()).run();
  }

  @Test
  public void testBarelyMadeIt() throws Exception {
    Timer timer = controller.createTimer(task);
    timer.schedule(10);
    controller.advanceTime(10);
    Mockito.verify(task).run();
  }

  @Test
  public void testExtraTime() throws Exception {
    Timer timer = controller.createTimer(task);
    timer.schedule(10);
    controller.advanceTime(11);
    Mockito.verify(task).run();
  }

}
