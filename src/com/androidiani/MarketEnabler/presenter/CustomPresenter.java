package com.androidiani.MarketEnabler.presenter;

import java.util.List;

import android.util.Log;

public class CustomPresenter {
	private ICustomView view;
	List<String> shellRes;

	public CustomPresenter(ICustomView viewIn) {
		view = viewIn;
	}

	public void setValues() {
		// android.os.SystemProperties.set(String key, String val)
		
		view.displayResult(true);
	}

	public void setValuesOld() {
		// getting values from view and creating shell command
		String[] writePropCommand = {
				"setprop gsm.sim.operator.numeric " + view.getSimNumeric(),
				"setprop gsm.operator.numeric " + view.getOperatorNumeric(),
				"setprop gsm.sim.operator.iso-country " + view.getSimISO(),
				"setprop gsm.operator.iso-country " + view.getOperatorISO(),
				"setprop gsm.operator.alpha " + view.getOperatorAlpha(),
				"setprop gsm.sim.operator.alpha " + view.getSimAlpha(),
				"kill $(ps | grep vending | tr -s  ' ' | cut -d ' ' -f2)",
				"rm -rf /data/data/com.android.vending/cache/*" };
		// Executing command in su mode
		Log.i("MarketEnabler", "dropping shell commands for custom values");
		shellRes = ShellInterface.doExec(writePropCommand, true);
		// TODO: how to check result? doing readprop again?
		for (String tmp : shellRes) {
			Log.d("MarketEnabler", "readprop result [" + tmp + "]");
		}
		view.displayResult(true);
	}
}
