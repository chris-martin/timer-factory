package org.codeswarm.timerfactory;

/**
 * A factory that constructs {@link Timer}s.
 */
public interface TimerFactory {

  /**
   * Constructs and returns timer which will run the given effect when it executes.
   *
   * @param task
   *  The task that will be performed when the timer executes.
   * @return
   *  The timer that was constructed.
   */
  Timer createTimer(TimerTask task);

}
