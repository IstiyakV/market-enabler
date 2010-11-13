package com.androidiani.MarketEnabler.presenter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;


public class ActualPresenter {
	private IActualView view;

	public ActualPresenter(IActualView viewIn) {
		view = viewIn;
		updateView();
	}

	public void updateView() {
		Log.i("MarketEnabler", "Getting actual values from TelephonyManager");
		view.setSimNumeric(view.getStartup().getTelephonyManager()
				.getSimOperator());
		// view.setOperatorNumeric(view.getStartup().getTelephonyManager()
		// .getNetworkOperator());
		// view.setSimISO(view.getStartup().getTelephonyManager()
		// .getSimCountryIso());
		// view.setOperatorISO(view.getStartup().getTelephonyManager()
		// .getNetworkCountryIso());
		// view.setOperatorAlpha(view.getStartup().getTelephonyManager()
		// .getNetworkOperatorName());
		// view.setSimAlpha(view.getStartup().getTelephonyManager()
		// .getSimOperatorName());
	}
	
	
}
