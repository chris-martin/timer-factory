package org.codeswarm.timerfactory;

import com.google.common.base.Preconditions;

/**
 * Represents TIME ITSELF.
 */
class Time implements Comparable<Time> {

  private final long value;

  public Time(long value) {
    this.value = value;
  }

  long value() {
    return value;
  }

  /**
   * How long will we have to wait to get to the future?
   */
  int util(Time future) {
    long delay = future.value - value;
    Preconditions.checkState(delay > 0);
    Preconditions.checkState(delay <= Integer.MAX_VALUE);
    return (int) delay;
  }

  /**
   * What time will it be after the delay?
   */
  Time future(int delay) {
    return new Time(value + delay);
  }

  @Override
  public int compareTo(Time other) {
    long difference = value - other.value;
    Preconditions.checkArgument(Integer.MIN_VALUE <= difference);
    Preconditions.checkState(difference <= Integer.MAX_VALUE);
    return (int) difference;
  }

}
