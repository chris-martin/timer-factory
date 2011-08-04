package org.codeswarm.timerfactory;

import com.google.common.base.Preconditions;

/**
 * The type of {@link org.codeswarm.timerfactory.Timer} constructed by
 * {@link org.codeswarm.timerfactory.TimeController}.
 * Is not affected whatsoever by the real flow of time.
 */
class ControlledTimer implements Timer {

  private final TimeProvider timeProvider;
  private final TimerUpdater updater;
  private final TimerTask task;
  private int delay;
  private boolean repeating;
  private ActiveState active = new ActiveState();
  private Time nextRun;

  ControlledTimer(TimerTask task,
                  TimeProvider timeProvider,
                  TimerUpdater updater) {
    this.task = task;
    this.timeProvider = timeProvider;
    this.updater = updater;
  }

  @Override
  public void run() {
    task.run();
  }

  @Override
  public void schedule(int delay) {
    schedule(delay, false);
  }

  @Override
  public void scheduleRepeating(int period) {
    schedule(period, true);
  }

  private void schedule(int delay, boolean repeating) {
    active.set(true);
    this.delay = delay;
    this.repeating = repeating;
    nextRun = timeProvider.currentTime().future(delay);
    updater.update(this);
  }

  @Override
  public void cancel() {
    if (active.get()) {
      active.set(false);
    }
  }

  void _run() {
    Preconditions.checkState(active.get());
    run();
    nextRun = repeating ? timeProvider.currentTime().future(delay) : null;
    active.set(repeating);
  }

  Time getNextRun() {
    Preconditions.checkState(active.get());
    return nextRun;
  }

  public boolean isActive() {
    return active.get();
  }

  private class ActiveState {

    boolean value;

    void set(boolean value) {
      this.value = value;
      if (!value) nextRun = null;
      updater.update(ControlledTimer.this);
    }

    boolean get() {
      return value;
    }

  }

}
