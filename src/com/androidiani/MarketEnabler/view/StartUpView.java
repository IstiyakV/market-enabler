package com.androidiani.MarketEnabler.view;

import android.app.TabActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;
import android.widget.TextView;

import com.androidiani.MarketEnabler.R;
import com.androidiani.MarketEnabler.presenter.IStartUp;
import com.androidiani.MarketEnabler.presenter.StartUp;

public class StartUpView extends TabActivity implements IStartUp {
	private StartUp presenter;
	
	/** UI elements **/
	private TextView simNumeric, operatorNumeric, simISO, operatorISO,
			simAlpha, operatorAlpha;
	
	public void onCreate(Bundle savedInstanceState) {
		Log.d("*** DEBUG ***", "Start app");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainview);
		Log.d("*** DEBUG ***", "Start setting up tabs");
		/** setup tabs **/
		TabHost mTabHost = getTabHost();
		/** add tabs **/
		mTabHost.addTab(mTabHost.newTabSpec("actual").setIndicator("Actual")
				.setContent(R.id.actual));
		mTabHost.addTab(mTabHost.newTabSpec("custom")
				.setIndicator("Set custom").setContent(R.id.custom));
		mTabHost.addTab(mTabHost.newTabSpec("old").setIndicator("Old")
				.setContent(R.id.old));
		/** set current tab */
		mTabHost.setCurrentTabByTag("actual");
		
		Log.d("*** DEBUG ***", "Start getting UI elements");
		/** get UI elements **/
		simNumeric = (TextView) findViewById(R.id.actualsimNumericValue);
		operatorNumeric = (TextView) findViewById(R.id.actualoperatorNumericValue);
		simISO = (TextView) findViewById(R.id.actualsimISOValue);
		operatorISO = (TextView) findViewById(R.id.actualoperatorISOValue);
		simAlpha = (TextView) findViewById(R.id.actualsimAlphaValue);
		operatorAlpha = (TextView) findViewById(R.id.actualoperatorAlphaValue);
		
		presenter = new StartUp(this);
		
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

	@Override
	public void displayError(String error) {
		// TODO Auto-generated method stub

	}
	
}
