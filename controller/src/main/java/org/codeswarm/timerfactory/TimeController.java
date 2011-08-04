package org.codeswarm.timerfactory;

import com.google.common.base.Preconditions;

/**
 * A {@link TimerFactory} designed for use in testing.
 * This factory constructs {@link Timer}s which do not run on
 * ordinary time, but instead depend on an artificial timeline
 * which progresses only as directed by this time controller.
 */
public class TimeController implements TimerFactory {

  private final TimerQueue queue = new TimerQueue();

  private Time time = new Time(0l);

  private final TimeProvider timeProvider = new TimeProvider() {
    @Override
    public Time currentTime() {
      return time;
    }
  };

  @Override
  public Timer createTimer(TimerTask task) {
    Preconditions.checkNotNull(task);
    return new ControlledTimer(task, timeProvider, queue);
  }

  /**
   * Causes time to move forward by the specified number of
   * milliseconds. Any running timers will be executed, in order
   * (resolving ties arbitrarily), as real timers would if this
   * amount of time had actually passed.
   *
   * @param progressMillis The number of milliseconds by which to
   *  advance the fake timeline. Must be nonnegative.
   */
  public void advanceTime(int progressMillis) {

    // Condition check
    Preconditions.checkArgument(progressMillis >= 0);

    // Timer with the soonest future run time
    ControlledTimer timer = queue.peek();

    // If the queue was empty, just skip time ahead
    if (timer == null) {
      time = time.future(progressMillis);
      return;
    }

    // The time at which the timer will run. We will
    // skip to this point at the end of the iteration.
    Time newTime = timer.getNextRun();

    // How much time will pass from now until the timer is run
    int timerDelay = time.util(newTime);

    // Always run timers that have run out,
    // but otherwise try to terminate recursion.
    if (timerDelay != 0 && progressMillis == 0) return;

    // If we're not progressing far enough to
    // trigger this timer, just skip time ahead
    if (progressMillis < timerDelay) {
      time = time.future(progressMillis);
      return;
    }

    // Run the timer
    timer._run();

    // Skip to the time at which the timer executes
    time = newTime;

    // Recurse
    advanceTime(progressMillis - timerDelay);

  }

}
