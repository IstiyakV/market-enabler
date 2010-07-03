package ru.org.amip.MarketAccess;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

abstract public class WakefulIntentService extends IntentService {
  abstract protected void doWakefulWork(Intent intent);

  public static final String LOCK_NAME_STATIC = EmulateService.class.getName();
  private static PowerManager.WakeLock lockStatic;

  public static void acquireStaticLock(Context context) {
    getLock(context).acquire();
  }

  synchronized private static PowerManager.WakeLock getLock(Context context) {
    if (lockStatic == null) {
      PowerManager mgr = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
      lockStatic = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, LOCK_NAME_STATIC);
      lockStatic.setReferenceCounted(true);
    }

    return lockStatic;
  }

  public WakefulIntentService(String name) {
    super(name);
  }

  @Override
  public void onStart(Intent intent, int startId) {
    if (!getLock(this).isHeld()) {  // fail-safe for crash restart
      getLock(this).acquire();
    }
    super.onStart(intent, startId);
  }

  @Override
  final protected void onHandleIntent(Intent intent) {
    try {
      doWakefulWork(intent);
    } finally {
      getLock(this).release();
    }
  }
}
