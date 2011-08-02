package org.codeswarm.timerfactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.List;
import java.util.Set;

/**
 * A {@link TimerFactory} designed for use in testing.
 * This factory constructs {@link Timer}s which do not run on
 * ordinary time, but instead depend on an artificial timeline
 * which progresses only as directed by this time controller.
 */
public class TimeController implements TimerFactory {

  // Timers, constructed by this class, that are currently running.
  private final Set<ControlledTimer> timers = Sets.newHashSet();

  @Override
  public Timer createTimer(TimerTask task) {
    Preconditions.checkNotNull(task);
    final List<ControlledTimer> timer = Lists.newArrayList();
    timer.add(new ControlledTimer(task, new Activation() {
      @Override
      public void setActive(boolean active) {
        if (active) timers.add(timer.get(0));
        else timers.remove(timer.get(0));
      }
    }));
    return timer.get(0);
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
    Preconditions.checkArgument(progressMillis >= 0);
    if (progressMillis == 0 || timers.size() == 0) return;
    int elapsed = minDelayTimer().getDelay();
    for (ControlledTimer timer : timers) timer.advanceTime(elapsed);
    advanceTime(progressMillis - elapsed);
  }

  /**
   * Searches the set of active timers to find the one
   * (resolving ties arbitrarily) with the shortest time
   * remaining until execution.
   */
  private ControlledTimer minDelayTimer() {
    int minDelay = Integer.MAX_VALUE;
    ControlledTimer soonestExpiringTimer = null;
    for (ControlledTimer timer : timers) {
      if (timer.getDelay() != 0 && timer.getDelay() < minDelay) {
        minDelay = timer.getDelay();
        soonestExpiringTimer = timer;
      }
    }
    return soonestExpiringTimer;
  }

}
