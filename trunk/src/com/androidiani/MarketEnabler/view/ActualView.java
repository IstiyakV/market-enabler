package com.androidiani.MarketEnabler.view;

import android.util.Log;
import android.widget.TextView;

import com.androidiani.MarketEnabler.R;
import com.androidiani.MarketEnabler.presenter.ActualPresenter;
import com.androidiani.MarketEnabler.presenter.IActualView;

public class ActualView implements IActualView {
	
	/** UI elements **/
	private TextView simNumeric, operatorNumeric, simISO, operatorISO,
			simAlpha, operatorAlpha;
	
	private StartUpView startup;
	
	private ActualPresenter presenter;

	public ActualView(StartUpView startup) {
		this.startup = startup;
		Log.i("MarketEnabler", "Start getting UI elements");
		/** get UI elements **/
		simNumeric = (TextView) startup
				.findViewById(R.id.actualsimNumericValue);
		operatorNumeric = (TextView) startup
				.findViewById(R.id.actualoperatorNumericValue);
		simISO = (TextView) startup.findViewById(R.id.actualsimISOValue);
		operatorISO = (TextView) startup
				.findViewById(R.id.actualoperatorISOValue);
		simAlpha = (TextView) startup.findViewById(R.id.actualsimAlphaValue);
		operatorAlpha = (TextView) startup
				.findViewById(R.id.actualoperatorAlphaValue);
		presenter = new ActualPresenter(this);
	}

	public void setSimNumeric(String simNumeric) {
		this.simNumeric.setText(simNumeric);
	}

	public void setOperatorNumeric(String operatorNumeric) {
		this.operatorNumeric.setText(operatorNumeric);
	}

	public void setSimISO(String simISO) {
		this.simISO.setText(simISO);
	}

	public void setOperatorISO(String operatorISO) {
		this.operatorISO.setText(operatorISO);
	}

	public void setSimAlpha(String simAlpha) {
		this.simAlpha.setText(simAlpha);
	}

	public void setOperatorAlpha(String operatorAlpha) {
		this.operatorAlpha.setText(operatorAlpha);
	}

	public void displayError(String error) {
		// TODO Auto-generated method stub

	}

	public void updateView() {
		presenter.updateView();
	}
	
	public StartUpView getStartup() {
		return startup;
	}

}
