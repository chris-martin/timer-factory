package org.codeswarm.timerfactory;

class GwtTimer
    extends com.google.gwt.user.client.Timer
    implements Timer {

  private final TimerTask task;

  GwtTimer(TimerTask task) {
    this.task = task;
  }

  @Override
  public void run() {
    task.run();
  }

}
