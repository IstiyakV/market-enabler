package com.androidiani.MarketEnabler.presenter;

public interface IActualView {
	void setSimNumeric(String simNumeric);

	void setOperatorNumeric(String operatorNumeric);

	void setSimISO(String simISO);

	void setOperatorISO(String operatorISO);

	void setSimAlpha(String simAlpha);

	void setOperatorAlpha(String operatorAlpha);
	
	void displayError(String error);
	
	void updateView();
}
