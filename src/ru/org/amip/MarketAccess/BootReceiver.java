package ru.org.amip.MarketAccess;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import ru.org.amip.MarketAccess.utils.AppManager;
import ru.org.amip.MarketAccess.utils.CompleteListener;
import ru.org.amip.MarketAccess.utils.RunWithProgress;
import ru.org.amip.MarketAccess.view.StartUpView;

/**
 * Date: Mar 24, 2010
 * Time: 9:37:00 PM
 *
 * @author serge
 */
public class BootReceiver extends BroadcastReceiver {
  @Override
  public void onReceive(final Context ctx, Intent intent) {
    final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(ctx);
    final String sim = settings.getString(StartUpView.APPLY_SIM_NUM, "");
    if (settings.getBoolean(StartUpView.APPLY_ON_BOOT, false) && sim.length() > 0) {
      final RunWithProgress run = new RunWithProgress(ctx, sim, "");
      run.setSilent(true);
      run.setCompleteListener(new CompleteListener() {
        @Override
        public void onComplete() {
          if (settings.getBoolean(StartUpView.SHOW_NOTIFICATION, false)) {
            showNotification(ctx, sim);
          } else {
            AppManager.getInstance().suicide();
          }
        }
      });
      run.doRun();
    } else {
      AppManager.getInstance().suicide();
    }
  }

  private static void showNotification(Context ctx, String msg) {
    CharSequence appName = ctx.getText(R.string.app_name);
    final String fullText = String.format(ctx.getString(R.string.emulating_name), msg);
    Notification notification = new Notification(R.drawable.icon, fullText, System.currentTimeMillis());
    notification.flags = 0;
    PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0, new Intent(ctx, StartUpView.class), 0);
    notification.setLatestEventInfo(ctx, appName, fullText, contentIntent);
    NotificationManager nm = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
    nm.notify(R.string.app_name, notification);
  }
}
