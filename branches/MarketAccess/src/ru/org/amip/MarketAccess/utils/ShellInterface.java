package ru.org.amip.MarketAccess.utils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import ru.org.amip.MarketAccess.view.StartUpView;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class ShellInterface {
  private static String suBinary;
  private static boolean suAvailable;
  private static boolean suChecked;

  private static final String XBIN_SU = "/system/xbin/su";
  private static final String BIN_SU = "/system/bin/su";
  private static final String TEST_COMMAND = "echo";
  private static final String KILL_ALL = "killall";
  private static final String EXIT = "exit\n";

  public static void doExec(String[] commands, boolean suNeeded, Handler handler) {
    Process process = null;
    DataOutputStream os = null;
    DataInputStream osRes = null;
    Message msg;
    int i = 0;

    try {
      if (suNeeded && isSuAvailable()) {
        process = Runtime.getRuntime().exec(suBinary);
      } else {
        process = Runtime.getRuntime().exec("sh");
      }

      os = new DataOutputStream(process.getOutputStream());

      for (String single : commands) {
        if (single.startsWith(KILL_ALL)) {
          // special treatment for killall command, use java to kill the process
          handleKill(single);
        } else {
          os.writeBytes(single + '\n');
          os.flush();
        }
        msg = Message.obtain();
        i++;
        msg.arg1 = i;
        msg.arg2 = -1; // 0 will dismiss the progress bar
        handler.sendMessage(msg);
        Thread.sleep(50);
      }
      os.writeBytes(EXIT);
      os.flush();
      process.waitFor();
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
    } finally {
      try {
        if (os != null) {
          os.close();
        }
        if (osRes != null) {
          osRes.close();
        }
        if (process != null) {
          process.destroy();
        }
      } catch (Exception ignored) {}
    }
  }

  private static void handleKill(String single) throws Exception {
    if (single.indexOf(' ') > 0) {
      final String app = single.substring(single.indexOf(' ') + 1);
      final AppManager am = AppManager.getInstance(null);
      int count = 0;
      while (am.isRunning(app)) {
        count++;
        am.kill(app);
        if (am.isRunning(app)) {
          Log.w(StartUpView.MARKET_ACCESS, "Failed to kill " + app);
          Thread.sleep(200);
        } else {
          break;
        }
        if (count >= 5) {
          Log.e(StartUpView.MARKET_ACCESS, "Failed to kill " + app + " 5 times, aborting");
          throw new Exception("Can't kill app: " + app);
        }
      }
    }
  }

  public static synchronized boolean isSuAvailable() {
    if (!suChecked) {
      checkSu();
      suChecked = true;
    }
    return suAvailable;
  }

  private static boolean checkSu() {
    suAvailable = true;
    suBinary = XBIN_SU;
    if (runCommand(TEST_COMMAND)) return suAvailable;
    suBinary = BIN_SU;
    if (runCommand(TEST_COMMAND)) return suAvailable;
    suBinary = "";
    suAvailable = false;
    return suAvailable;
  }

  public static boolean runCommand(String command) {
    DataOutputStream os = null;
    Process process = null;
    try {
      process = Runtime.getRuntime().exec(suBinary);
      os = new DataOutputStream(process.getOutputStream());
      os.writeBytes(command + '\n');
      os.flush();
      os.writeBytes(EXIT);
      os.flush();
      process.waitFor();
      return true;
    } catch (Exception ignored) {
    } finally {
      try {
        if (os != null) {
          os.close();
        }
        if (process != null) {
          process.destroy();
        }
      } catch (Exception ignored) { }
    }
    return false;
  }
}
