package com.androidiani.MarketEnabler.presenter;

import android.app.ProgressDialog;
import android.os.Handler;
import android.util.Log;

public class CustomPresenter implements Runnable {
	private ICustomView view;
	private static Handler handler;
	private static String[] writePropCommand;
	private ProgressDialog pd;
	
	public ProgressDialog getPd() {
		return pd;
	}

	public CustomPresenter(ICustomView viewIn) {
		view = viewIn;
		handler = view.getHandler();
	}
	
	private static void setCommand(String[] cmd) {
		writePropCommand = cmd;
	}

	public void setValues() {
		Log.d("MarketEnabler", "starting setValues");
		// getting values from view and creating shell command
		String[] writePropCommand = {
				"setprop gsm.sim.operator.numeric " + view.getSimNumeric(),
				"setprop gsm.operator.numeric " + view.getOperatorNumeric(),
				"setprop gsm.sim.operator.iso-country " + view.getSimISO(),
				"setprop gsm.operator.iso-country " + view.getOperatorISO(),
				"setprop gsm.operator.alpha \"" + view.getOperatorAlpha()
						+ "\"",
				"setprop gsm.sim.operator.alpha \"" + view.getSimAlpha() + "\"",
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
		pd.setMessage("Setting to fake custom provider");
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
