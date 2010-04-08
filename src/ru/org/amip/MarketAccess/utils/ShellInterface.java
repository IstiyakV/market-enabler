package ru.org.amip.MarketAccess.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import ru.org.amip.MarketAccess.view.StartUpView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

public class ShellInterface {
  private static String suBinary;
  private static boolean suAvailable;
  private static boolean suChecked;

  private static final String XBIN_SU = "/system/xbin/su";
  private static final String BIN_SU = "/system/bin/su";
  private static final String TEST_COMMAND = "echo";
  private static final String KILL_ALL = "killall";
  private static final String EXIT = "exit\n";
  private static final String SETPREF = "setpref";
  private static final Pattern PATTERN = Pattern.compile(" ");

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
      sinkProcessOutput(process);

      for (String single : commands) {
        Log.i(StartUpView.MARKET_ACCESS, single);
        if (single.startsWith(KILL_ALL)) {
          // special treatment for killall command, use java to kill the process
          handleKill(single);
        } else if (single.startsWith(SETPREF)) {
          // setting preferences requires some context, get one from AppManager
          handlePref(single, AppManager.getInstance());
        } else {
          os.writeBytes(single + '\n');
          os.flush();
        }
        msg = Message.obtain();
        i++;
        msg.arg1 = i;
        msg.arg2 = -1; // 0 will dismiss the progress bar
        handler.sendMessage(msg);
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

  private static void handlePref(String single, Context ctx) {
    final String[] parts = PATTERN.split(single, 6);
    try {
      Context app = ctx.createPackageContext(parts[1], Context.CONTEXT_IGNORE_SECURITY);
      final SharedPreferences.Editor editor =
        app.getSharedPreferences(parts[2], Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE).edit();
      //TODO add support for other types if you want it to be universal
      if (parts[3].equals("boolean")) {
        editor.putBoolean(parts[4], Boolean.parseBoolean(parts[5]));
      }
      editor.commit();
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
      sinkProcessOutput(process);
      os.writeBytes(command + '\n');
      os.flush();
      os.writeBytes(EXIT);
      os.flush();
      process.waitFor();
      return true;
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        if (os != null) {
          os.close();
        }
        if (process != null) {
          process.destroy();
        }
      } catch (Exception ignored) {}
    }
    return false;
  }

  public static void sinkProcessOutput(Process p) {
    new InputStreamHandler(p.getInputStream());
    new InputStreamHandler(p.getErrorStream());
  }
}

class InputStreamHandler extends Thread {
  private final InputStream stream;

  InputStreamHandler(InputStream stream) {
    setName("SinkThread");
    this.stream = stream;
    start();
  }

  @Override
  public void run() {
    try { while (stream.read() != -1) {} } catch (IOException ignored) {}
  }
}
