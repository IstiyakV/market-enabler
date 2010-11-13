package com.androidiani.MarketEnabler.view;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import ch.racic.android.marketenabler.R;

import com.androidiani.MarketEnabler.model.ProviderConfig;
import com.androidiani.MarketEnabler.presenter.CustomPresenter;
import com.androidiani.MarketEnabler.presenter.ICustomView;
import com.google.android.apps.analytics.GoogleAnalyticsTracker;

public class CustomView implements ICustomView {
	
	/** UI elements **/
	private TextView simNumeric, operatorNumeric, simISO, operatorISO,
			simAlpha, operatorAlpha;
	private Button setValues;
	private Button setForFakeOnBoot;

	private StartUpView startup;
	private CustomPresenter presenter;
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			// This Means that we've not finished the work ..
			Log.d("MarketEnabler", "progress msg["+ msg.arg1 + ", "+ msg.arg2 + "] getprogress["+presenter.getPd().getProgress()+"]");
			
			if (msg.arg2 != 0) {
				Log.d("MarketEnabler", "increment progress msg["+ msg.arg1 + ", "+ msg.arg2 + "] getprogress["+presenter.getPd().getProgress()+"]");
				//presenter.getPd().incrementProgressBy(msg.arg1 - pd.getProgress());
				presenter.getPd().setProgress(msg.arg1);
			} else {
				Log.d("MarketEnabler", "dismiss progress msg["+ msg.arg1 + ", "+ msg.arg2 + "] getprogress["+presenter.getPd().getProgress()+"]");
				//pd.setProgress(8);
				presenter.getPd().dismiss();
				//Toast.makeText(startup, "Done ;)", Toast.LENGTH_LONG).show();
				if (msg.arg1 == 0) 
					Toast.makeText(startup, " Done and all set", Toast.LENGTH_LONG)
							.show();
				else
					Toast.makeText(startup, "We Got a Problem Houston :(",
							Toast.LENGTH_LONG).show();
				startup.getTabHost().setCurrentTabByTag("actual");
			}

		}
	};
	
	public Handler getHandler() {
		return handler;
	}

	public CustomView(StartUpView startup) {
		this.startup = startup;
		
		Log.i("MarketEnabler", "Start getting UI elements");
		/** get UI elements **/
		simNumeric = (TextView) startup
				.findViewById(R.id.customsimNumericValue);
		// operatorNumeric = (TextView) startup
		// .findViewById(R.id.customoperatorNumericValue);
		// simISO = (TextView) startup.findViewById(R.id.customsimISOValue);
		// operatorISO = (TextView) startup
		// .findViewById(R.id.customoperatorISOValue);
		// simAlpha = (TextView) startup.findViewById(R.id.customsimAlphaValue);
		// operatorAlpha = (TextView) startup
		// .findViewById(R.id.customoperatorAlphaValue);
		
		/** setting actual values as default text **/
		simNumeric.setText(startup.getTelephonyManager().getSimOperator());
		// operatorNumeric.setText(startup.getTelephonyManager()
		// .getNetworkOperator());
		// simISO.setText(startup.getTelephonyManager().getSimCountryIso());
		// operatorISO.setText(startup.getTelephonyManager()
		// .getNetworkCountryIso());
		// operatorAlpha.setText(startup.getTelephonyManager()
		// .getNetworkOperatorName());
		// simAlpha.setText(startup.getTelephonyManager().getSimOperatorName());
		
		presenter = new CustomPresenter(this);
		
		
		setValues = (Button) startup.findViewById(R.id.customsetValues);
		setValues.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Log.i("MarketEnabler", "calling CustomPresenter.setValues()");
				presenter.setValues();
			}
		});
		
		setForFakeOnBoot = (Button) startup.findViewById(R.id.customsetOnBoot);
		setForFakeOnBoot.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				setFakeOnBoot(getSimNumeric());
			}
		});
		 
	}
	
	public void setFakeOnBoot(String providerCode) {
		if(isPackageAvailable("ch.racic.android.marketenabler.donatekey")) {
			Log.d("MarketEnabler", "save for fakeOnBoot with code [" + providerCode + "]");
			setFakeOnBootRecord(providerCode);

			GoogleAnalyticsTracker tracker = GoogleAnalyticsTracker.getInstance();
			tracker.trackPageView("/fakeOnBoot/"+providerCode);
			// send intent
			Intent i2 = new Intent();
			i2.setAction("ch.racic.android.marketenabler.plugin.fakeonboot.SET_CODE");
			Bundle c = new Bundle();            
			c.putString("code", providerCode);
			i2.putExtras(c);
			startup.getApplicationContext().sendBroadcast(i2);
		} else {
			 Toast.makeText(startup, "fakeOnBoot needs the donate key, you can get it from Market or I will send it to every paypal donator.\n\nThe donate key app has a boot receiver and will initiate MarketEnabler with the set fakeOnBoot provider code after the boot broadcast has been received.", 2*Toast.LENGTH_LONG).show();
			 startup.startActivity(new Intent("android.intent.action.VIEW",Uri.parse(StartUpView.marketDonateUrl)));
		}
	}

	private void setFakeOnBootRecord(String providerCode) {
	    Editor prefsEditor = PreferenceManager.getDefaultSharedPreferences(
	    		startup.getApplicationContext()).edit();
		prefsEditor.putString("FakeOnBootName", "Custom");
		prefsEditor.putString("FakeOnBootCode", providerCode);
		prefsEditor.commit();
		prefsEditor = null;
	}
	
	public boolean isPackageAvailable(String packageName) {
		int sigMatch = startup.getPackageManager().checkSignatures(startup.getPackageName(), packageName);
		return sigMatch == PackageManager.SIGNATURE_MATCH;
	}


	public void displayError(String error) {
		// TODO Auto-generated method stub

	}

	public String getOperatorAlpha() {
		return operatorAlpha.getText().toString();
	}

	public String getOperatorISO() {
		return operatorISO.getText().toString();
	}

	public String getOperatorNumeric() {
		return operatorNumeric.getText().toString();
	}

	public String getSimAlpha() {
		return simAlpha.getText().toString();
	}

	public String getSimISO() {
		return simISO.getText().toString();
	}

	public String getSimNumeric() {
		return simNumeric.getText().toString();
	}

	public void displayResult(boolean result) {
		// TODO Auto-generated method stub

	}

	public StartUpView getStartup() {
		return startup;
	}

}
