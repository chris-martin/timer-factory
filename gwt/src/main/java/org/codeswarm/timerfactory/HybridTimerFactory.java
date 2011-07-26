package org.codeswarm.timerfactory;

import com.google.gwt.core.client.GWT;

public class HybridTimerFactory implements TimerFactory {

  private final TimerFactory clientTimerFactory;

  private final TimerFactory serverTimerFactory;

  public HybridTimerFactory() {
    this(new GwtTimerFactory(), new StandardTimerFactory());
  }

  public HybridTimerFactory(TimerFactory clientTimerFactory,
                            TimerFactory serverTimerFactory) {
    this.clientTimerFactory = clientTimerFactory;
    this.serverTimerFactory = serverTimerFactory;
  }

  @Override
  public Timer createTimer(TimerTask task) {
    return getTimerFactory().createTimer(task);
  }

  private TimerFactory getTimerFactory() {
    return GWT.isClient() ? clientTimerFactory : serverTimerFactory;
  }

}
