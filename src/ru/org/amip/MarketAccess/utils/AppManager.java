package ru.org.amip.MarketAccess.utils;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.util.Log;
import ru.org.amip.MarketAccess.view.StartUpView;

import java.util.List;

/**
 * Date: Mar 25, 2010
 * Time: 9:55:54 PM
 *
 * @author serge
 */
public class AppManager extends Application {
  private static AppManager instance;
  private ActivityManager activityManager;
  private List<ActivityManager.RunningAppProcessInfo> processes;

  @Override
  public void onCreate() {
    instance = this;
    activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
  }

  public static AppManager getInstance() {
    return instance;
  }

  public void refreshList() {
    processes = activityManager.getRunningAppProcesses();
  }

  public boolean isRunning(String app) {
    refreshList();
    for (ActivityManager.RunningAppProcessInfo process : processes) {
      if (process.processName.startsWith(app)) return true;
    }
    return false;
  }

  public boolean kill(String app) {
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
