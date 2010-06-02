package ru.org.amip.MarketAccess;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import ru.org.amip.MarketAccess.utils.AppManager;
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
      WakefulIntentService.acquireStaticLock(ctx);
      ctx.startService(new Intent(ctx, EmulateService.class));
    } else {
      AppManager.getInstance().suicide();
    }
  }
}
