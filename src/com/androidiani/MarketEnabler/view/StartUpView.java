package com.androidiani.MarketEnabler.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

import ch.racic.android.marketenabler.R;

import com.adwhirl.AdWhirlLayout;
import com.androidiani.MarketEnabler.presenter.IActualView;
import com.androidiani.MarketEnabler.update.Update;
import com.google.android.apps.analytics.GoogleAnalyticsTracker;

public class StartUpView extends TabActivity implements OnTabChangeListener {
	private IActualView viewActual = null;
	private CustomView viewCustom = null;
	private final ListView viewList = null;
	private TelephonyManager tm = null;
	private android.widget.ListView list;
	private GoogleAnalyticsTracker tracker;
	private Context ctx;
	private boolean isAppPayed;
	private final String	paypalDonateUrl	= "https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=SJDNMZ5JLZRSU&lc=CH&item_name=Android%20Market%20Enabler%20%2d%20Developement%20team&item_number=MarketEnabler&currency_code=CHF&bn=PP%2dDonationsBF%3abtn_donateCC_LG%2egif%3aNonHosted";
	public static final String	marketDonateUrl	= "market://search?q=pname:ch.racic.android.marketenabler.donatekey";
	
	public android.widget.ListView getList() {
		return list;
	}





	@Override
	public void onCreate(Bundle savedInstanceState) {
		/** start update service **/
		ctx = getApplicationContext();
		
		tracker = GoogleAnalyticsTracker.getInstance();
		tracker.start("UA-10016187-3", this);
		tracker.setDispatchPeriod(10);// 5 secondi

		tracker.trackPageView("/");
		/** get telephony manager **/
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

		Log.i("MarketEnabler", "Start app");
		super.onCreate(savedInstanceState);
		
		/** check if donate key is available **/
		if(!isAppPayed) { 
			if (isPackageAvailable("ch.racic.android.marketenabler.donatekey")) {
				setAppToPayed();
				setContentView(R.layout.mainview_donate);
				Log.i("MarketEnabler", "Loading in donate mode");
			} else {
				setContentView(R.layout.mainview);
				Log.i("MarketEnabler", "Setup ads");
				LinearLayout layout = (LinearLayout) findViewById(R.id.ads);
				AdWhirlLayout adWhirlLayout = new AdWhirlLayout(this, "950d37c5a6b54c82a2dcb752f7a91112");
				RelativeLayout.LayoutParams adWhirlLayoutParams =
					new RelativeLayout.LayoutParams(
							LayoutParams.FILL_PARENT,
							LayoutParams.WRAP_CONTENT);
				layout.addView(adWhirlLayout, adWhirlLayoutParams);
				layout.invalidate();
				/** find the paypal donate view **/
				final Button paypal = (Button) findViewById(R.id.donatePaypal);
				/** find the market donate view **/
				final Button market = (Button) findViewById(R.id.donateMarket);
				/**
				 * create an intent that can be fired if the paypal donate button gets
				 * pressed
				 **/
				final Intent paypalIntent = new Intent("android.intent.action.VIEW",
						Uri.parse(paypalDonateUrl));
				/**
				 * create an intent that can be fired if the market donate button gets
				 * pressed
				 **/
				final Intent marketIntent = new Intent("android.intent.action.VIEW",
						Uri.parse(marketDonateUrl));

				/**
				 * create an onClick listener for the logo and buttons that fires the
				 * intent
				 **/
				paypal.setOnClickListener(new View.OnClickListener() {
					public void onClick(View view) {
						startActivity(paypalIntent);
					}
				});
				market.setOnClickListener(new View.OnClickListener() {
					public void onClick(View view) {
						startActivity(marketIntent);
					}
				});
			}
		} else {
			setContentView(R.layout.mainview_donate);
			Log.i("MarketEnabler", "Loading in donate mode");
		}
		
		/** check if old package is installed that is deprecated **/
		if(isOldPackageStillInstalled()) {
			oldVersionNotifyDialog();
        }
		
		Log.i("MarketEnabler", "Start setting up tabs");
		/** setup tabs **/
		TabHost mTabHost = getTabHost();
		/** add tabs **/
		mTabHost.addTab(mTabHost.newTabSpec("actual").setIndicator("Actual")
				.setContent(R.id.actual));

		// mTabHost.addTab(mTabHost.newTabSpec("list").setIndicator("Settings list")
		// .setContent(R.id.list));

		TabSpec tab = mTabHost.newTabSpec("List").setIndicator("Settings list");
		tab.setContent(new Intent(this, ListView.class));
		mTabHost.addTab(tab);

		mTabHost.addTab(mTabHost.newTabSpec("custom")
				.setIndicator("Set custom").setContent(R.id.custom));
		mTabHost.setOnTabChangedListener(this);

		/** set current tab */
		mTabHost.setCurrentTabByTag("actual");

		viewActual = new ActualView(this);
		viewCustom = new CustomView(this);
		// viewList = new ListView(this);

		/**
		 * Sorry for this break of MVP but i couldn't manage the ListView
		 * because of exception
		 **/
		list = (android.widget.ListView) findViewById(R.id.listset);
		// list.setAdapter(new ArrayAdapter<ProviderConfig>(this,
		// android.R.layout.simple_list_item_1, viewList.getPresenter()
		// .createDefaultList()));
		// ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		// android.R.layout.simple_list_item_1, new String[] { "item4",
		// "item5", "item6" });
		// list.setAdapter(adapter);

		// list.setOnCreateContextMenuListener(new OnCreateContextMenuListener()
		// {
		// @Override
		// public void onCreateContextMenu(ContextMenu menu, View v,
		// ContextMenuInfo menuInfo) {
		// menu.setHeaderTitle("ContextMenu");
		// menu.add(0, CONTEXTMENU_SETITEM, 0, "fake it baby!");
		// // menu.add(0, CONTEXTMENU_MAKEDEFAULT, 0,
		// // "this is my default on startup");
		//
		// }
		//
		// });
		
		//Timer that will start the update thread in 500ms from Activity rendering :)
		CountDownTimer ctimer = new CountDownTimer(500, 500) {
	    	public void onTick(long muf) {
	    		
	    	}
	    	
	    	public void onFinish()  {
	    		ctx.startService(new Intent(ctx, Update.class));
	    	} 
	    };
	    ctimer.start();
	}

	private void setAppToPayed() {
		isAppPayed = true;
	}





	// public boolean onContextItemSelected(MenuItem aItem) {
	// AdapterView.AdapterContextMenuInfo menuInfo =
	// (AdapterView.AdapterContextMenuInfo) aItem
	// .getMenuInfo();
	// /* Switch on the ID of the item, to get what the user selected. */
	// switch (aItem.getItemId()) {
	// case CONTEXTMENU_SETITEM:
	// viewList.getPresenter().setValues(
	// (ProviderConfig) list.getAdapter().getItem(
	// menuInfo.position));
	// return true; /* true means: "we handled the event". */
	// case CONTEXTMENU_MAKEDEFAULT:
	// // not yet implemented
	// }
	// return false;
	// }
	//
	public View createTabContent(String tag) {

		return null;
	}

	@Override
	public void onTabChanged(String tabId) {
		tracker.trackPageView("/tab_"+tabId);
		Log.i("MarketEnabler", "Changed to tab initiated [" + tabId + "]");
		if (tabId.equals("actual")) {
			viewActual.updateView();
		} else if (tabId.equals("custom")) {
			/* is it really needed to update custom view? I don't
			 * think so!
			 * // viewCustom.updateView(); }
			 */
		} else if (tabId.equals("list")) {
			// TODO viewList.updateView();
		}

	}

	public TelephonyManager getTelephonyManager() {
		return tm;
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	public void onPause() {
		tracker.dispatch();
		super.onPause();
	}
	
 
	public boolean isPackageAvailable(String packageName) {
		int sigMatch = getPackageManager().checkSignatures(getPackageName(), packageName);
		return sigMatch == PackageManager.SIGNATURE_MATCH;
	}
	
	public boolean isOldPackageStillInstalled() {
		int sigMatch = getPackageManager().checkSignatures(getPackageName(), "com.androidiani.MarketEnabler");
		boolean ret = sigMatch == PackageManager.SIGNATURE_MATCH;
		Log.i("MarketEnabler", "check for old installed package = "+ret);
		return ret;
	}
	

	private void oldVersionNotifyDialog() {
		AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
			alt_bld.setMessage("You have still installed an old MarketEnabler version!\nI have changed the package name to publish it to the market and you can safely uninstall the old version.\n\nDo you want to uninstall the old package now?")
				.setCancelable(false)
				.setPositiveButton(getString(android.R.string.yes), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// Action for 'Yes' Button
						Uri uri = Uri.fromParts("package", "com.androidiani.MarketEnabler", null);
						Intent deleteIntent = new Intent(Intent.ACTION_DELETE, uri);
						startActivity(deleteIntent);
					}
				})
				.setNegativeButton(getString(android.R.string.no), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						//  Action for 'NO' Button
						dialog.cancel();
					}
				});
				AlertDialog alert = alt_bld.create();
				// 	Title for AlertDialog
				alert.setTitle("Title");
				alert.show();
		}





	public boolean isAppPayed() {
		return isAppPayed;
	}
	
}
