package ru.org.amip.MarketAccess.utils;

import android.util.Log;
import ru.org.amip.MarketAccess.view.StartUpView;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Date: Mar 24, 2010
 * Time: 4:14:07 PM
 *
 * @author serge
 */
public class ShellInterface {
  private static String  suBinary;
  private static boolean suChecked;

  // uid=0(root) gid=0(root)
  private static final Pattern UID_PATTERN = Pattern.compile("^uid=(\\d+).*?");

  enum OUTPUT {
    STDOUT,
    STDERR,
    BOTH
  }

  private static final String XBIN_SU      = "/system/xbin/su";
  private static final String BIN_SU       = "/system/bin/su";
  private static final String TEST_COMMAND = "id";
  private static final String EXIT         = "exit\n";
  private static final String RUN_ERROR    = "___SHELL_ERROR";

  public static synchronized boolean isSuAvailable() {
    if (!suChecked) {
      checkSu();
      suChecked = true;
    }
    return suBinary != null;
  }

  public static synchronized void setSuChecked(boolean suChecked) {
    ShellInterface.suChecked = suChecked;
  }

  private static boolean checkSu() {
    suBinary = XBIN_SU;
    if (isRootUid()) return true;
    suBinary = BIN_SU;
    if (isRootUid()) return true;
    suBinary = null;
    return false;
  }

  private static boolean isRootUid() {
    String out = getProcessOutput(TEST_COMMAND);
    if (out == null) return false;
    Matcher matcher = UID_PATTERN.matcher(out);
    if (matcher.matches()) {
      if ("0".equals(matcher.group(1))) {
        return true;
      }
    }
    return false;
  }

  public static String getProcessOutput(String command) {
    String out = _runCommand(command, OUTPUT.STDERR);
    if (RUN_ERROR.equals(out)) return null;
    return out;
  }

  public static boolean runCommand(String command) {
    String out = _runCommand(command, OUTPUT.BOTH);
    return out == null;
  }

  private static String _runCommand(String command, OUTPUT o) {
    DataOutputStream os = null;
    Process process = null;
    try {
      process = Runtime.getRuntime().exec(suBinary);
      os = new DataOutputStream(process.getOutputStream());
      InputStreamHandler sh = sinkProcessOutput(process, o);
      os.writeBytes(command + '\n');
      os.flush();
      os.writeBytes(EXIT);
      os.flush();
      process.waitFor();
      if (sh != null) {
        String output = sh.getOutput();
        Log.d(StartUpView.TAG, command + " output: " + output);
        return output;
      } else {
        return null;
      }
    } catch (Exception e) {
      Log.e(StartUpView.TAG, "runCommand error: " + e.getMessage());
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
    return RUN_ERROR;
  }

  public static InputStreamHandler sinkProcessOutput(Process p, OUTPUT o) {
    InputStreamHandler output = null;
    switch (o) {
      case STDOUT:
        output = new InputStreamHandler(p.getErrorStream(), false);
        new InputStreamHandler(p.getInputStream(), true);
        break;
      case STDERR:
        output = new InputStreamHandler(p.getInputStream(), false);
        new InputStreamHandler(p.getErrorStream(), true);
        break;
      case BOTH:
        new InputStreamHandler(p.getInputStream(), true);
        new InputStreamHandler(p.getErrorStream(), true);
        break;
    }
    return output;
  }

  private static class InputStreamHandler extends Thread {
    private final InputStream stream;
    private final boolean     sink;
    StringBuffer output;

    public String getOutput() {
      return output.toString();
    }

    InputStreamHandler(InputStream stream, boolean sink) {
      this.sink = sink;
      this.stream = stream;
      start();
    }

    @Override
    public void run() {
      try {
        if (sink) {
          while (stream.read() != -1) {}
        } else {
          output = new StringBuffer();
          BufferedReader b = new BufferedReader(new InputStreamReader(stream));
          String s;
          while ((s = b.readLine()) != null) {
            output.append(s);
          }
        }
      } catch (IOException ignored) {}
    }
  }
}
