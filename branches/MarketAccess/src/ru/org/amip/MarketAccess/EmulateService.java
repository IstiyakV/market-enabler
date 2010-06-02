package ru.org.amip.MarketAccess;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import ru.org.amip.MarketAccess.utils.AppManager;
import ru.org.amip.MarketAccess.utils.RunWithProgress;
import ru.org.amip.MarketAccess.view.StartUpView;

/**
 * Date: May 22, 2010
 * Time: 1:42:10 PM
 *
 * @author serge
 */
public class EmulateService extends WakefulIntentService {
  public EmulateService() {
    super(EmulateService.class.getSimpleName());
  }

  @Override
  void doWakefulWork(Intent intent) {
    final Context ctx = getApplicationContext();
    final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(ctx);
    final String sim = settings.getString(StartUpView.APPLY_SIM_NUM, "");

    final RunWithProgress run = new RunWithProgress(ctx, sim, "");
    run.setSilent(true);
    run.doRunForeground();

    if (settings.getBoolean(StartUpView.SHOW_NOTIFICATION, false)) {
      showNotification(ctx, sim);
    } else {
      AppManager.getInstance().suicide();
    }
  }

  private static void showNotification(Context ctx, String msg) {
    CharSequence appName = ctx.getText(R.string.app_name);
    final String fullText = String.format(ctx.getString(R.string.emulating_name), msg);
    Notification notification = new Notification(R.drawable.icon, fullText, System.currentTimeMillis());
    notification.flags = Notification.FLAG_AUTO_CANCEL;
    PendingIntent contentIntent =
      PendingIntent.getActivity(ctx, 0, new Intent(ctx, StartUpView.class), Intent.FLAG_ACTIVITY_NEW_TASK);
    notification.setLatestEventInfo(ctx, appName, fullText, contentIntent);
    NotificationManager nm = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
    nm.notify(R.string.app_name, notification);
  }
}
