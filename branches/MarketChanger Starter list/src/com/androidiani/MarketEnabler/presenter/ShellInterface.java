package com.androidiani.MarketEnabler.presenter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class ShellInterface {

	public static void doExec(String[] commands, boolean suNeeded,
			Handler handler) {
		List<String> res = new ArrayList<String>();
		Process process = null;
		DataOutputStream os = null;
		DataInputStream osRes = null;
		Message msg = null;
		int i = 1;

		try {
			
			if (suNeeded) {
				// Getting Root ;)
				Log.i("MarketEnabler", "Starting exec of su");
				process = Runtime.getRuntime().exec("su");
			} else {
				Log.i("MarketEnabler", "Starting exec of sh");
				process = Runtime.getRuntime().exec("sh");
			}

			os = new DataOutputStream(process.getOutputStream());
			
			// Doing Stuff ;)
			Log.i("MarketEnabler", "Starting command loop");
			for (String single : commands) {
				Log.i("MarketEnabler", "Executing [" + single + "]");
				os.writeBytes(single + "\n");
				Log.i("MarketEnabler", "Executing [" + single + "] os.flush()");
				os.flush();
				
				if(handler != null) {
					msg = Message.obtain();
					msg.arg1 = i++;
					msg.arg2 = -1;// This because when 0 i will dismiss the
									// progressbar.
					handler.sendMessage(msg);
				}
				
				Thread.sleep(200);
			}
			
			os.writeBytes("exit\n");
			os.flush();

			process.waitFor();
			if(handler != null) {
				msg = Message.obtain();
				msg.arg1 = 0;
				msg.arg2 = 0;
				handler.sendMessage(msg);
			}

        } catch (Exception e) {
			Log.d("MarketEnabler", "Unexpected error - Here is what I know: "
					+ e.getMessage());
			e.printStackTrace();
			if(handler != null) {
				msg = Message.obtain();
				msg.arg1 = 1;
				msg.arg2 = 0;
				handler.sendMessage(msg);
			}
			res.add(e.getMessage());
		} finally {
			try {
				if (os != null) {
					os.close();
				}
				if (osRes != null) {
					osRes.close();
				}
				process.destroy();
			} catch (Exception e) {
				// nothing
			}
		}
	}
}
