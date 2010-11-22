package com.androidiani.MarketEnabler.presenter;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.androidiani.MarketEnabler.model.ProviderConfig;
import com.androidiani.MarketEnabler.view.StartUpView;
import com.google.android.apps.analytics.GoogleAnalyticsTracker;

public class ListPresenter implements Runnable {
	private final IListView view;
	private static Handler handler;
	private static String[] writePropCommand;
	private ProgressDialog pd;
	private ArrayList<ProviderConfig> list;

	public ProgressDialog getPd() {
		return pd;
	}

	public ListPresenter(IListView viewIn) {
		view = viewIn;
		handler = view.getHandler();
		createDefaultList();
	}

	public ArrayList<ProviderConfig> createDefaultList() {
		list = new ArrayList<ProviderConfig>();
		list.add(new ProviderConfig(310260, 310260, "us", "us", "T-Mobile",
		"T-Mobile"));
		list.add(new ProviderConfig(310260, 310260, "us", "us", "Verizon",
		"Verizon"));
		list.add(new ProviderConfig(22802, 22802, "ch", "ch", "sunrise", "sunrise"));
		list.add(new ProviderConfig(22801, 22801, "ch", "ch", "swisscom", "swisscom"));
		list.add(new ProviderConfig(22803, 22803, "ch", "ch", "orange", "orange"));
		list.add(new ProviderConfig(26207, 26207, "de", "de", "o2 - de",
		"o2 - de"));
		list
		.add(new ProviderConfig(26203, 26203, "de", "de", "simyo",
		"E-Plus"));
		list.add(new ProviderConfig(22201, 22201, "it", "it", "TIM", "TIM"));
		list.add(new ProviderConfig(50501, 50501, "au", "au", "Telstra",
		"Telstra"));
		list.add(new ProviderConfig(23203, 23203, "at", "at", "T-Mobile",
		"T-Mobile"));
		list.add(new ProviderConfig(20416, 20416, "nl", "nl", "T-Mobile",
		"T-Mobile"));
		list.add(new ProviderConfig(27203, 27203, "ie", "ie", "", "Meteor"));

		return list;
	}

	private static void setCommand(String[] cmd) {
		writePropCommand = cmd;
	}
	public void setValues(int i) {


		Log.d("MarketEnabler", "starting setValues with list item[" + i + "]");
		ProviderConfig tmp = list.get(i);
		Log.d("MarketEnabler", "starting setValues with list item[" + i
				+ "] provider config[" + tmp.getGsmOperatorAlpha() + "]");
		setValues(tmp);

		GoogleAnalyticsTracker tracker = GoogleAnalyticsTracker.getInstance();
		tracker.trackPageView("/fake_provider/"+tmp.getGsmOperatorAlpha());
	}
	public void setValues(ProviderConfig settings) {
		Log.d("MarketEnabler", "starting setValues");
		// getting values from view and creating shell command
		String[] writePropCommand = {
				"setprop gsm.sim.operator.numeric "
				+ settings.getGsmSimOperatorNumeric(),
				"setprop gsm.operator.numeric "
				+ settings.getGsmOperatorNumeric(),
				// "setprop gsm.sim.operator.iso-country "
				// + settings.getGsmSimOperatorIsoCountry(),
				// "setprop gsm.operator.iso-country "
				// + settings.getGsmOperatorIsoCountry(),
				// "setprop gsm.operator.alpha \""
				// + settings.getGsmOperatorAlpha() + "\"",
				// "setprop gsm.sim.operator.alpha \""
				// + settings.getGsmSimOperatorAlpha() + "\"",
				// "kill $(ps | grep vending | tr -s  ' ' | cut -d ' ' -f2)",
				"killall com.android.vending",
		"rm -rf /data/data/com.android.vending/cache/*" };

		setCommand(writePropCommand);

		Log.i("MarketEnabler", "dropping shell commands for list values");
		// pd = new ProgressDialog(view.getStartup());
		pd = new ProgressDialog((Context) view);
		pd.setMax(writePropCommand.length);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setProgress(1);
		pd.setTitle("Working");
		pd.setMessage("We Are Faking: " + settings.getGsmSimOperatorAlpha()
				+ "(" + settings.getGsmOperatorAlpha() + ")");
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
	
	public ProviderConfig getCodeFromListItem(int i) {
		return list.get(i);
	}
	
	
}
