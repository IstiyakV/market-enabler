package ru.org.amip.MarketAccess.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import ru.org.amip.MarketAccess.R;
import ru.org.amip.MarketAccess.view.StartUpView;

import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Date: Mar 24, 2010
 * Time: 4:14:07 PM
 *
 * @author serge
 */
public class RunWithProgress implements Runnable {
  private static String[] writePropCommand;
  private ProgressDialog pd;
  private final Context ctx;
  private final String message;
  private CompleteListener completeListener;
  private boolean silent;

  private static final Pattern PATTERN = Pattern.compile(" ");

  private static final String KILL_ALL = "killall";
  private static final String SETPREF = "setpref";
  private static final String SETOWN = "setown";

  private static final String[] COMMANDS = new String[]{
    "setprop gsm.sim.operator.numeric",
    "killall com.android.vending",
    "rm -rf /data/data/com.android.vending/cache/*",
    "chmod 777 /data/data/com.android.vending/shared_prefs",
    "chmod 666 /data/data/com.android.vending/shared_prefs/vending_preferences.xml",
    "setpref com.android.vending vending_preferences boolean metadata_paid_apps_enabled true",
    "chmod 660 /data/data/com.android.vending/shared_prefs/vending_preferences.xml",
    "chmod 771 /data/data/com.android.vending/shared_prefs",
    "setown com.android.vending /data/data/com.android.vending/shared_prefs/vending_preferences.xml"
  };

  private final Handler handler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      if (!silent) showProgress(msg);
      if (completeListener != null) {
        completeListener.onComplete();
      }
    }
  };

  public void setSilent(boolean silent) {
    this.silent = silent;
  }

  public void setCompleteListener(CompleteListener completeListener) {
    this.completeListener = completeListener;
  }

  public void showProgress(Message msg) {
    if (msg.arg2 != 0) {
      pd.setProgress(msg.arg1);
    } else {
      pd.dismiss();
      if (msg.arg1 == 0) {
        Toast.makeText(ctx, R.string.applied, Toast.LENGTH_SHORT).show();
      } else if (msg.arg1 == 1) {
        Toast.makeText(ctx, R.string.error, Toast.LENGTH_LONG).show();
      } else {
        showNoRootAlert();
      }
    }
  }

  private void showNoRootAlert() {
    new AlertDialog.Builder(ctx)
      .setMessage(R.string.no_root)
      .setCancelable(false)
      .setPositiveButton(R.string.no_root_ok, new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int id) {
          dialog.cancel();
        }
      }).create().show();
  }

  public static String[] makeCommand(String numeric) {
    final String[] strings = new String[COMMANDS.length];
    System.arraycopy(COMMANDS, 0, strings, 0, COMMANDS.length);
    strings[0] = strings[0] + ' ' + numeric;
    return strings;
  }

  public RunWithProgress(Context ctx, String value, String message) {
    this.ctx = ctx;
    this.message = message;

    writePropCommand = makeCommand(value);
  }

  public void doRun() {
    if (!silent) {
      pd = new ProgressDialog(ctx);
      pd.setMax(writePropCommand.length);
      pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
      pd.setProgress(1);
      pd.setTitle(R.string.working);
      pd.setMessage(message);
      pd.show();
    }
    new Thread(this).start();
  }

  @Override
  public void run() {
    if (!ShellInterface.isSuAvailable()) {
      Message msg = Message.obtain();
      msg.arg1 = 2;
      msg.arg2 = 0;
      handler.sendMessage(msg);
      return;
    }
    doExec(writePropCommand, handler);
  }

  private static void doExec(String[] commands, Handler handler) {
    Message msg;
    int i = 0;

    try {
      for (String cmd : commands) {
        Log.i(StartUpView.MARKET_ACCESS, cmd);
        if (cmd.startsWith(KILL_ALL)) {
          // special treatment for killall command, use java to kill the process
          handleKill(cmd);
        } else if (cmd.startsWith(SETPREF)) {
          // setting preferences requires some context, get one from AppManager
          handlePref(cmd, AppManager.getInstance());
        } else if (cmd.startsWith(SETOWN)) {
          handleOwn(cmd, AppManager.getInstance());
        } else {
          if (!ShellInterface.runCommand(cmd)) throw new IOException("Shell command failed: " + cmd);
        }
        msg = Message.obtain();
        i++;
        msg.arg1 = i;
        msg.arg2 = -1; // 0 will dismiss the progress bar
        handler.sendMessage(msg);
      }
      msg = Message.obtain();
      msg.arg1 = 0;
      msg.arg2 = 0;
      handler.sendMessage(msg);
    } catch (Exception e) {
      e.printStackTrace();
      msg = Message.obtain();
      msg.arg1 = 1;
      msg.arg2 = 0;
      handler.sendMessage(msg);
    }
  }

  private static void handleOwn(String single, Context ctx) {
    final String[] parts = PATTERN.split(single, 3);
    try {
      final int uid = ctx.getPackageManager().getApplicationInfo(parts[1], 0).uid;
      Log.i(StartUpView.MARKET_ACCESS, "setting owner: " + uid);
      ShellInterface.runCommand("chown " + uid + '.' + uid + ' ' + parts[2]);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void handlePref(String single, Context ctx) {
    final String[] parts = PATTERN.split(single, 6);
    try {
      Context app = ctx.createPackageContext(parts[1], 0);
      final SharedPreferences preferences =
        app.getSharedPreferences(parts[2], Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE);
      // TODO add support for other types if you want it to be universal
      // only change if set to false, don't add new preference or overwrite if already true
      if (parts[3].equals("boolean") && !preferences.getBoolean(parts[4], true)) {
        final SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(parts[4], Boolean.parseBoolean(parts[5]));
        editor.commit();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void handleKill(String single) throws IOException {
    if (single.indexOf(' ') > 0) {
      final String app = single.substring(single.indexOf(' ') + 1);
      final AppManager am = AppManager.getInstance();
      int count = 0;
      while (am.isRunning(app)) {
        count++;
        am.kill(app);
        if (am.isRunning(app)) {
          Log.w(StartUpView.MARKET_ACCESS, "Failed to kill " + app);
          try {
            Thread.sleep(200);
          } catch (InterruptedException ignored) {
            break;
          }
        } else {
          break;
        }
        if (count >= 5) {
          Log.e(StartUpView.MARKET_ACCESS, "Failed to kill " + app + " 5 times, aborting");
          throw new IOException("Can't kill app: " + app);
        }
      }
    }
  }
}
