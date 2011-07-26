package org.codeswarm.timerfactory;

/**
 * The type of {@link org.codeswarm.timerfactory.Timer} constructed by
 * {@link org.codeswarm.timerfactory.TimeController}.
 * Is not affected whatsoever by the real flow of time.
 */
class ControlledTimer implements Timer {

  private final TimerTask task;
  private int remainingMillis;
  private int repeatMillis;
  private TimeController timeController;

  public ControlledTimer(TimeController timeController, TimerTask task) {
    this.timeController = timeController;
    this.task = task;
  }

  @Override
  public void run() {
    task.run();
  }

  @Override
  public void schedule(int delayMillis) {
    repeatMillis = 0;
    remainingMillis = delayMillis;
    addOrRemove();
  }

  @Override
  public void scheduleRepeating(int periodMillis) {
    repeatMillis = periodMillis;
    remainingMillis = periodMillis;
    addOrRemove();
  }

  @Override
  public void cancel() {
    remainingMillis = 0;
    remove();
  }

  /**
   * Causes the timer to behave as if the given number of milliseconds have passed.
   *
   * @param progressMillis
   *  The number of milliseconds to advance. Can not be greater than the amount of
   *  time remaining until this timer will execute. The only way to run a timer is
   *  to advance time exactly to the point of its execution.
   */
  void advanceTime(int progressMillis) {
    assert progressMillis <= remainingMillis;
    if (progressMillis == remainingMillis) {
      run();
      remainingMillis = repeatMillis;
      if (remainingMillis == 0) {
        remove();
      }
    } else {
      remainingMillis -= progressMillis;
    }
  }

  public int getRemainingMillis() {
    return remainingMillis;
  }

  private void addOrRemove() {
    if (remainingMillis == 0) {
      remove();
    } else {
      add();
    }
  }

  /**
   * Adds this timer to the time controller, indicating that the timer is active.
   */
  private void add() {
    timeController.add(this);
  }

  /**
   * Removes this timer from the time controller, indicating that the timer is inactive.
   */
  private void remove() {
    timeController.remove(this);
  }

}
