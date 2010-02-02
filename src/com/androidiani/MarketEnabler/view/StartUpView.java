package com.androidiani.MarketEnabler.view;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

import com.androidiani.MarketEnabler.R;
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
	public android.widget.ListView getList() {
		return list;
	}





	@Override
	public void onCreate(Bundle savedInstanceState) {
		/** start update service **/
		startService(new Intent(this, Update.class));
		tracker = GoogleAnalyticsTracker.getInstance();
		tracker.start("UA-10016187-3", this);
		tracker.setDispatchPeriod(10);// 5 secondi

		tracker.trackPageView("/");
		/** get telephony manager **/
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

		Log.i("MarketEnabler", "Start app");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainview);
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
	public void onPause() {
		tracker.dispatch();
		super.onPause();
	}

}
