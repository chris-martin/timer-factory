package org.codeswarm.timerfactory;

import java.util.Set;

import com.google.common.collect.Sets;

/**
 * A {@link TimerFactory} designed for use in testing.
 * This factory constructs {@link Timer}s which do not run on ordinary time,
 * but instead depend on an artificial timeline which progresses only
 * as directly by this time controller.
 */
public class TimeController implements TimerFactory {

  /**
   * Timers, constructed by this class, that are currently running.
   */
  private final Set<ControlledTimer> timers;

  /**
   * Constructs a new {@linkplain TimeController}.
   */
  public TimeController() {
    timers = Sets.newHashSet();
  }

  @Override
  public Timer createTimer(final TimerTask task) {
      return new ControlledTimer(this, task);
  }

  /**
   * Causes time to move forward by the specified number of milliseconds.
   * Any running timers will be executed, in order (resolving ties arbitrarily),
   * as real timers would if this amount of time had actually passed.
   *
   * @param progressMillis
   *  The number of milliseconds by which to advance the fake timeline.
   */
  public void advanceTime(final int progressMillis) {
    if (progressMillis == 0 || timers.size() == 0) {
      return;
    }
    int timeElapsedThisIteration = soonestExpiringTimer().getRemainingMillis();
    for (ControlledTimer timer : timers) {
      timer.advanceTime(timeElapsedThisIteration);
    }
    advanceTime(progressMillis - timeElapsedThisIteration);
  }

  /**
   * Searches the set of active timers to find the one (resolving ties arbitrarily)
   * with the shortest time remaining until execution.
   */
  private ControlledTimer soonestExpiringTimer() {
    int lowestRemainingTime = Integer.MAX_VALUE;
    ControlledTimer soonestExpiringTimer = null;
    for (ControlledTimer timer : timers) {
      if (timer.getRemainingMillis() != 0 && timer.getRemainingMillis() < lowestRemainingTime) {
        lowestRemainingTime = timer.getRemainingMillis();
        soonestExpiringTimer = timer;
      }
    }
    return soonestExpiringTimer;
  }

  void add(final ControlledTimer timer) {
    timers.add(timer);
  }

  void remove(final ControlledTimer timer) {
    timers.remove(timer);
  }

}
