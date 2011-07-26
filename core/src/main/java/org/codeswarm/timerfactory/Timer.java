package org.codeswarm.timerfactory;

public interface Timer {

  /**
   * Cancels this timer.
   */
  void cancel();

  /**
   * Schedules a timer to elapse in the future.
   *
   * @param delayMillis
   *  How long to wait before the timer elapses, in milliseconds.
   */
  void schedule(int delayMillis);

  /**
   * Schedules a timer that elapses repeatedly.
   *
   * @param periodMillis
   *  How long to wait before the timer elapses, in milliseconds, between each repetition.
   */
  void scheduleRepeating(int periodMillis);

  /**
   * Runs the method that is be called when the timer fires.
   */
  void run();

}
