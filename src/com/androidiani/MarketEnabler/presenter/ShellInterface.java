package com.androidiani.MarketEnabler.presenter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

public class ShellInterface {

	public static List<String> doExec(String[] commands, boolean suNeeded) {
		List<String> res = new ArrayList<String>();
		Process process = null;
		DataOutputStream os = null;
		DataInputStream osRes = null;

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
			osRes = new DataInputStream(process.getInputStream());
			
			// Doing Stuff ;)
			Log.i("MarketEnabler", "Starting command loop");
			for (String single : commands) {
				Log.i("MarketEnabler", "Executing [" + single + "]");
				os.writeBytes(single + "\n");
				Log.i("MarketEnabler", "Executing [" + single + "] os.flush()");
				os.flush();
				// res.add(osRes.readLine());
			}

			os.writeBytes("exit\n");
			os.flush();

			process.waitFor();

        } catch (Exception e) {
			Log.d("MarketEnabler", "Unexpected error - Here is what I know: "
					+ e.getMessage());
			res.add(e.getMessage());
			return res;
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
		return res;
	}
}
