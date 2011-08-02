package org.codeswarm.timerfactory;

import com.google.common.base.Preconditions;

/**
 * The type of {@link org.codeswarm.timerfactory.Timer} constructed by
 * {@link org.codeswarm.timerfactory.TimeController}.
 * Is not affected whatsoever by the real flow of time.
 */
class ControlledTimer implements Timer {

  private final TimerTask task;
  private final Activation activation;

  private int delay;
  private int period;

  ControlledTimer(TimerTask task, Activation activation) {
    this.task = task;
    this.activation = activation;
  }

  @Override
  public void run() {
    task.run();
  }

  @Override
  public void schedule(int delay) {
    set(delay, 0);
  }

  @Override
  public void scheduleRepeating(int period) {
    set(period, period);
  }

  private void set(int delay, int period) {
    this.delay = delay;
    this.period = period;
    activation.setActive(delay != 0);
  }

  @Override
  public void cancel() {
    delay = 0;
    activation.setActive(false);
  }

  /**
   * Causes the timer to behave as if the given quantity of
   * time has passed.
   *
   * @param progress The number of milliseconds to advance.
   *  Can not be greater than the amount of time remaining
   *  until this timer will execute. The only way to run a
   *  timer is to advance time exactly to the point of its
   *  execution.
   */
  void advanceTime(int progress) {
    Preconditions.checkArgument(progress <= delay);
    if (progress == delay) {
      run();
      delay = period;
      if (delay == 0) {
        activation.setActive(false);
      }
    } else {
      delay -= progress;
    }
  }

  int getDelay() {
    return delay;
  }

}
