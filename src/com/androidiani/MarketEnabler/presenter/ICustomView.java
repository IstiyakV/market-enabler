package com.androidiani.MarketEnabler.presenter;

import android.os.Handler;

import com.androidiani.MarketEnabler.view.StartUpView;

public interface ICustomView {
	String getSimNumeric();

	String getOperatorNumeric();

	String getSimISO();

	String getOperatorISO();

	String getSimAlpha();

	String getOperatorAlpha();

	void displayError(String error);
	
	void displayResult(boolean result);
	
	StartUpView getStartup();
	Handler getHandler();

}
