package com.androidiani.MarketEnabler.presenter;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;

public class RestorePresenter implements Runnable {
	private IActualView view;
	private static Handler handler;
	private static String[] writePropCommand;
	private ProgressDialog pd;
	
	private static final String SIMNUM = "simNumeric",
			OPNUM = "operatorNumeric", SIMISO = "simISO",
			OPISO = "operatorISO", SIMAL = "simAlpha", OPAL = "operatorAlpha",
			BACKUPAV = "BackupAvailable", PREF_NAME = "MarketEnablerBackup";
	
	public ProgressDialog getPd() {
		return pd;
	}

	public RestorePresenter(IActualView viewIn) {
		view = viewIn;
		handler = view.getHandler();
	}
	
	private static void setCommand(String[] cmd) {
		writePropCommand = cmd;
	}

	public void setValues() {
		Log.d("MarketEnabler", "starting setValues");
		// getting values from view and creating shell command
		
		SharedPreferences settings = view.getStartup().getSharedPreferences(
				PREF_NAME, 0);
		
		String[] writePropCommand = {
				"setprop gsm.sim.operator.numeric "
						+ settings.getString(SIMNUM, ""),
				"setprop gsm.operator.numeric " + settings.getString(OPNUM, ""),
				"setprop gsm.sim.operator.iso-country "
						+ settings.getString(SIMISO, ""),
				"setprop gsm.operator.iso-country "
						+ settings.getString(OPISO, ""),
				"setprop gsm.operator.alpha \"" + settings.getString(OPAL, "")
						+ "\"",
				"setprop gsm.sim.operator.alpha \""
						+ settings.getString(SIMAL, "") + "\"",
				"kill $(ps | grep vending | tr -s  ' ' | cut -d ' ' -f2)",
				"rm -rf /data/data/com.android.vending/cache/*" };
		
		setCommand(writePropCommand);
		// Executing command in su mode
		Log.i("MarketEnabler", "dropping shell commands for custom values");
		// view.startProgress(10, "working", "executing setprop commands");
		pd = new ProgressDialog(view.getStartup());
		pd.setMax(writePropCommand.length);
		pd.setProgress(1);
		pd.setTitle("working");
		pd.setMessage("Restore saved settings");
		pd.show();
		
		Thread thread = new Thread(this);
		thread.start();

		// // TODO: how to check result? doing readprop again?
		// for (String tmp : shellRes) {
		// Log.d("MarketEnabler", "readprop result [" + tmp + "]");
		// }
		// view.displayResult(true);
	}

	@Override
	public void run() {
		Log.d("MarketEnabler", "Starting shell thread with ["
				+ writePropCommand.length + "] commands");
		ShellInterface.doExec(writePropCommand, true, handler);
	}
}
