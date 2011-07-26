package org.codeswarm.timerfactory;

/**
 * Default implementation of {@link TimerFactory}, using
 * {@link com.google.gwt.user.client.Timer}.
 */
public class GwtTimerFactory implements TimerFactory {

  @Override
  public Timer createTimer(final TimerTask task) {
    return new GwtTimer(task);
  }

}
