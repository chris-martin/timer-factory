package org.codeswarm.timerfactory;

import com.google.common.collect.Maps;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.PriorityQueue;

class TimerQueue implements TimerUpdater {

  private final PriorityQueue<QueuedTimer> priorityQueue =
    new PriorityQueue<QueuedTimer>();

  private final Map<ControlledTimer, QueuedTimer> map =
    Maps.newHashMap();

  @Override
  public void update(ControlledTimer timer) {
    if (!timer.isActive()) return;
    QueuedTimer queuedTimer = map.get(timer);
    if (queuedTimer != null) priorityQueue.remove(queuedTimer);
    queuedTimer = new QueuedTimer(timer, timer.getNextRun());
    map.put(timer, queuedTimer);
    priorityQueue.add(queuedTimer);
  }

  @Nullable ControlledTimer peek() {
    while (true) {
      QueuedTimer queuedTimer = priorityQueue.peek();
      if (queuedTimer == null) return null;
      ControlledTimer timer = queuedTimer.timer;
      if (timer.isActive()) return timer;
      priorityQueue.remove();
    }
  }

  private static class QueuedTimer implements Comparable<QueuedTimer> {

    final ControlledTimer timer;
    final Time nextRun;

    public QueuedTimer(ControlledTimer timer, Time nextRun) {
      this.timer = timer;
      this.nextRun = nextRun;
    }

    @Override
    public int compareTo(QueuedTimer that) {
      int timeComparison = nextRun.compareTo(that.nextRun);
      if (timeComparison == 0) return timer.hashCode() - that.timer.hashCode();
      return timeComparison;
    }

  }

}
