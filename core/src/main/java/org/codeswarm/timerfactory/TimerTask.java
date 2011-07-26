package org.codeswarm.timerfactory;

/**
 * Specifies the action that will be invoked when a {@link Timer} is executed.
 */
public interface TimerTask {

  /**
   * Runs whatever action needs to run when the timer runs out.
   */
  void run();

}
