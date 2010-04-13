package ru.org.amip.MarketAccess.utils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ShellInterface {
  private static String suBinary;
  private static boolean suChecked;

  private static final String XBIN_SU = "/system/xbin/su";
  private static final String BIN_SU = "/system/bin/su";
  private static final String TEST_COMMAND = "echo";
  private static final String EXIT = "exit\n";

  public static synchronized boolean isSuAvailable() {
    if (!suChecked) {
      checkSu();
      suChecked = true;
    }
    return suBinary != null;
  }

  private static boolean checkSu() {
    suBinary = XBIN_SU;
    if (runCommand(TEST_COMMAND)) return true;
    suBinary = BIN_SU;
    if (runCommand(TEST_COMMAND)) return true;
    suBinary = null;
    return false;
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

  private static class InputStreamHandler extends Thread {
    private final InputStream stream;

    InputStreamHandler(InputStream stream) {
      this.stream = stream;
      start();
    }

    @Override
    public void run() {
      try { while (stream.read() != -1) {} } catch (IOException ignored) {}
    }
  }
}
