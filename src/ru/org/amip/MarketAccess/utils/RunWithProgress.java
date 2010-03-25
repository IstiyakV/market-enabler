package ru.org.amip.MarketAccess.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import ru.org.amip.MarketAccess.R;
import ru.org.amip.MarketAccess.view.StartUpView;

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

  private static final String cmd_1 = "setprop gsm.sim.operator.numeric";
  private static final String cmd_2 = "killall com.android.vending";
  private static final String cmd_3 = "rm -rf /data/data/com.android.vending/cache/*";

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
      Log.i(StartUpView.MARKET_ACCESS, "progress: " + msg.arg1);
      pd.setProgress(msg.arg1);
    } else {
      pd.dismiss();
      if (msg.arg1 == 0) {
        Toast.makeText(ctx, R.string.applied, Toast.LENGTH_LONG).show();
      } else if (msg.arg1 == 1) {
        Toast.makeText(ctx, R.string.error, Toast.LENGTH_LONG).show();
      } else {
        showNoRootAlert();
      }
    }
  }

  private void showNoRootAlert() {
    AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
    builder.setMessage(R.string.no_root)
      .setCancelable(false)
      .setPositiveButton(R.string.no_root_ok, new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int id) {
          dialog.cancel();
        }
      });
    builder.create().show();
  }

  public static String[] makeCommand(String numeric) {
    return new String[]{cmd_1 + ' ' + numeric, cmd_2, cmd_3};
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

    Thread thread = new Thread(this);
    thread.start();
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
    ShellInterface.doExec(writePropCommand, true, handler);
  }
}
