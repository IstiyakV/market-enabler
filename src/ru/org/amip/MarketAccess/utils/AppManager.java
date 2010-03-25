package ru.org.amip.MarketAccess.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;
import ru.org.amip.MarketAccess.view.StartUpView;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: Mar 25, 2010
 * Time: 9:55:54 PM
 *
 * @author serge
 */
public class AppManager {
  private static AppManager instance;
  @SuppressWarnings({"FieldMayBeFinal"})
  private ActivityManager activityManager;
  private List<ActivityManager.RunningAppProcessInfo> processes = new ArrayList<ActivityManager.RunningAppProcessInfo>();

  public static synchronized AppManager getInstance(Context ctx) {
    if (instance == null) instance = new AppManager(ctx);
    return instance;
  }

  private AppManager(Context ctx) {
    if (ctx == null) return;
    activityManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
  }

  public void refreshList() {
    if (activityManager == null) return;
    processes = activityManager.getRunningAppProcesses();
  }

  public boolean isRunning(String app) {
    if (activityManager == null) return false;
    refreshList();
    for (ActivityManager.RunningAppProcessInfo process : processes) {
      if (process.processName.startsWith(app)) return true;
    }
    return false;
  }

  public boolean kill(String app) {
    if (activityManager == null) return false;
    refreshList();
    for (ActivityManager.RunningAppProcessInfo process : processes) {
      if (process.processName.startsWith(app)) {
        activityManager.restartPackage(process.processName);
        Log.i(StartUpView.MARKET_ACCESS, "killed: " + process.processName);
        return true;
      }
    }
    return false;
  }
}
