package org.codeswarm.timerfactory;

public class StandardTimerFactory implements TimerFactory {

  @Override
  public Timer createTimer(TimerTask task) {
    return new StandardTimer(task);
  }

}
