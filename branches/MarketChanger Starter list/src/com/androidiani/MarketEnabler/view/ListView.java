package com.androidiani.MarketEnabler.view;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.androidiani.MarketEnabler.model.ProviderConfig;
import com.androidiani.MarketEnabler.presenter.IListView;
import com.androidiani.MarketEnabler.presenter.ListPresenter;
import com.google.android.apps.analytics.GoogleAnalyticsTracker;

public class ListView extends ListActivity implements IListView {

	protected ArrayList<ProviderConfig> providerlist; // = new
	// ArrayList<String>();
	private Context ctx;
	private ListPresenter presenter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ctx = getApplicationContext();
		ctx = (Context) this;
		presenter = new ListPresenter(this);
		providerlist = presenter.createDefaultList();
		
		
		// Use an existing ListAdapter that will map an array
		// of strings to TextViews
		setListAdapter(new ArrayAdapter<ProviderConfig>(this,
				android.R.layout.simple_list_item_1, providerlist));
		// whats this?
	//	getListView().setTextFilterEnabled(true);

		registerForContextMenu(getListView());

	}
	
    public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		// Toast.makeText(this, "Longpress: " + info.position,
		// Toast.LENGTH_SHORT)
		// .show();
		if(item.getTitle() == "Set for fakeOnBoot") {
			
			setFakeOnBoot(presenter.getCodeFromListItem(info.position));
			return true;
		} else if(item.getTitle() == "fake this provider now") {
			presenter.setValues(info.position);
			return true;
		} else {
			return false;
		}
	}
    
    public void setFakeOnBoot(ProviderConfig providerConfig) {
		if(isPackageAvailable("ch.racic.android.marketenabler.donatekey")) {
			Log.d("MarketEnabler", "save for fakeOnBoot with item[" + providerConfig.getGsmSimOperatorAlpha()
				+ "] provider config[" + providerConfig.getGsmSimOperatorNumeric() + "]");
			setFakeOnBootRecord(providerConfig);

			GoogleAnalyticsTracker tracker = GoogleAnalyticsTracker.getInstance();
			tracker.trackPageView("/fakeOnBoot/"+providerConfig.getGsmOperatorAlpha());
			// send intent
			Intent i2 = new Intent("ch.racic.android.marketenabler.plugin.fakeonboot.SET_CODE");
			Bundle c = new Bundle();            
			c.putString("code", ""+providerConfig.getGsmSimOperatorNumeric());
			i2.putExtras(c);
			sendBroadcast(i2);
		} else {
			 Toast.makeText(this, "fakeOnBoot needs the donate key, you can get it from Market or I will send it to every paypal donator.\n\nThe donate key app has a boot receiver and will initiate MarketEnabler with the set fakeOnBoot provider code after the boot broadcast has been received.", 2*Toast.LENGTH_LONG).show();
			 startActivity(new Intent("android.intent.action.VIEW",Uri.parse(StartUpView.marketDonateUrl)));
		}
	}

	private void setFakeOnBootRecord(ProviderConfig tmp) {
	    Editor prefsEditor = PreferenceManager.getDefaultSharedPreferences(
	    		getApplicationContext()).edit();
		prefsEditor.putString("FakeOnBootName", tmp.getGsmSimOperatorAlpha());
		prefsEditor.putString("FakeOnBootCode", ""+tmp.getGsmSimOperatorNumeric());
		prefsEditor.commit();
		prefsEditor = null;
	}
	
	public boolean isPackageAvailable(String packageName) {
		int sigMatch = getPackageManager().checkSignatures(getPackageName(), packageName);
		return sigMatch == PackageManager.SIGNATURE_MATCH;
	}

	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
		int selPos = info.position;

		menu.add("fake this provider now");
		menu.add("Set for fakeOnBoot");
	}

	private StartUpView startup;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			// This Means that we've not finished the work ..
			Log.d("MarketEnabler", "progress msg[" + msg.arg1 + ", " + msg.arg2
					+ "] getprogress[" + presenter.getPd().getProgress() + "]");

			if (msg.arg2 != 0) {
				Log.d("MarketEnabler", "increment progress msg[" + msg.arg1
						+ ", " + msg.arg2 + "] getprogress["
						+ presenter.getPd().getProgress() + "]");
				// presenter.getPd().incrementProgressBy(msg.arg1 -
				// pd.getProgress());
				presenter.getPd().setProgress(msg.arg1);
			} else {
				Log.d("MarketEnabler", "dismiss progress msg[" + msg.arg1
						+ ", " + msg.arg2 + "] getprogress["
						+ presenter.getPd().getProgress() + "]");
				// pd.setProgress(8);
				presenter.getPd().dismiss();
				// Toast.makeText(startup, "Done ;)", Toast.LENGTH_LONG).show();
				if (msg.arg1 == 0)
					Toast
							.makeText(ctx, " Done and all set",
									Toast.LENGTH_LONG)
							.show();
				else
					Toast.makeText(ctx, "We Got a Problem Houston :(",
							Toast.LENGTH_LONG).show();
				// startup.getTabHost().setCurrentTabByTag("actual");

			}

		}
	};

	@Override
	public Handler getHandler() {
		return handler;
	}

	@Override
	public StartUpView getStartup() {
		return startup;
	}

	@Override
	public Context getCtx() {
		return ctx;
	}
	
}