package net.wizardsoflua.lua.module.time;

import net.sandius.rembulan.runtime.ExecutionContext;
import net.sandius.rembulan.runtime.ResolvedControlThrowable;
import net.sandius.rembulan.runtime.UnresolvedControlThrowable;
import net.wizardsoflua.annotation.GenerateLuaDoc;
import net.wizardsoflua.annotation.GenerateLuaModule;
import net.wizardsoflua.annotation.LuaFunction;
import net.wizardsoflua.annotation.LuaProperty;
import net.wizardsoflua.lua.Converters;
import net.wizardsoflua.lua.function.NamedFunction1;

/**
 * The Time module provides access to time related properties of the active Spell's world.
 */
@GenerateLuaModule(name = "Time2")
@GenerateLuaDoc(subtitle = "Accessing the Time")
public class TimeApi {
  private Time delegate;
  private Converters converters;

  public Converters getConverters() {
    return converters;
  }

  /**
   * The allowance is the number of lua ticks that are left before the active spell must sleep for
   * at least one game tick.
   */
  @LuaProperty
  public long getAllowance() {
    return delegate.getAllowance();
  }

  /**
   * The autosleep value defines whether the current spell should go to sleep automatically when its
   * allowance is exceeded. If this is set to false, the spell will never go to sleep automatically,
   * but instead will be broken when its allowance falls below zero. Default is true.
   */
  @LuaProperty
  public boolean isAutosleep() {
    return delegate.isAutoSleep();
  }

  @LuaProperty
  public void setAutosleep(boolean autosleep) {
    delegate.setAutoSleep(autosleep);
  }

  /**
   * The gametime is the number of game ticks that have passed since the world has been created.
   */
  @LuaProperty
  public long getGametime() {
    return delegate.getGameTotalTime();
  }

  /**
   * The luatime is the number of lua ticks that the current spell has worked since it has been
   * casted.
   */
  @LuaProperty
  public long getLuatime() {
    return delegate.getLuaTicks();
  }

  /**
   * The realtime is the number of milliseconds that have passed since January 1st, 1970.
   */
  @LuaProperty
  public long getRealtime() {
    return delegate.getRealtime();
  }

  /**
   * Returns a string with the current real date and time. If you want you can change the format by
   * providing an optional format string.
   */
  @LuaFunction
  public String getDate(String pattern) {
    return delegate.getDate(pattern);
  }

  public void onCreateModule(TimeModule2 module) {
    module.addReadOnly(new SleepFunction());
  }

  /**
   * Forces the current spell to sleep for the given amount of game ticks.
   */
  @LuaFunction
  public class SleepFunction extends NamedFunction1 {
    @Override
    public String getName() {
      return "sleep";
    }

    @Override
    public void invoke(ExecutionContext context, Object arg1) throws ResolvedControlThrowable {
      if (arg1 == null) {
        return; // ignore call
      }
      int ticks = getConverters().toJava(int.class, arg1, 1, "ticks", getName());
      delegate.startSleep(ticks);
      execute(context);
    }

    @Override
    public void resume(ExecutionContext context, Object suspendedState)
        throws ResolvedControlThrowable {
      execute(context);
    }

    private void execute(ExecutionContext context) throws ResolvedControlThrowable {
      try {
        context.pauseIfRequested();
      } catch (UnresolvedControlThrowable ex) {
        throw ex.resolve(this, null);
      }
      context.getReturnBuffer().setTo();
    }
  }
}
