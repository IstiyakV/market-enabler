package com.androidiani.MarketEnabler.presenter;

public interface ICustomView {
	String getSimNumeric();

	String getOperatorNumeric();

	String getSimISO();

	String getOperatorISO();

	String getSimAlpha();

	String getOperatorAlpha();

	void displayError(String error);
	
	void displayResult(boolean result);
}
