Timer Factory
=============

Timer Factory helps you write testable code that utilizes timer-based logic.

Core
----

The core project defines the API for timers and timer factories.

Example usage::

 class Foo {
   
   private final TimerFactory timerFactory;
   boolean x;
   
   Foo(TimerFactory timerFactory) {
     this.timerFactory = timerFactory;
   }
   
   /** Toggles the value of x after 1 second. */
   void bar() {
     Timer timer = timerFactory.createTimer(new TimerTask() {
       public void run() { x = !x; }
     });
     timer.schedule(1000);
   }
 
 }

The factory is provided via constructor injection.
This will allow us to inject a mock factory when using Foo
in a test environment.
This also lets GWT users use the Foo class in both client and server
environments by instantiating it with the appropriate factory.

Standard
--------

StandardTimerFactory
  Creates timers backed by java.util.Timer.

GWT
---

GwtTimerFactory
  Creates timers backed by com.google.gwt.user.client.Timer.

HybridTimerFactory
  Invokes either GwtTimerFactory StandardTimerFactory,
  depending on whether it is used in a client or server environment.

Controller
----------

TimeController
  A timer factory whose timers operate on an imaginary timeline.
  Intended to be used for testing.

Example usage::

  @Test
  public void testFoo {
    TimeController timeController = new TimeController();
    Foo foo = new Foo(timeController);
    foo.bar();
    timeController.advanceTime(800);
    Assert.assertFalse(foo.x); // Nothing has happened yet
    timerController.advanceTime(800);
    Assert.assertTrue(foo.x); // Now, over 1 second has passed
  }

