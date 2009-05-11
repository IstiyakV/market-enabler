package com.androidiani.MarketEnabler.view;

import android.app.TabActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

import com.androidiani.MarketEnabler.R;
import com.androidiani.MarketEnabler.presenter.IActualView;

public class StartUpView extends TabActivity implements OnTabChangeListener {
	private IActualView viewActual = null;
	private Object viewCustom = null; // TODO create custom view
	private Object viewList = null; // TODO create list view
	
	
	public void onCreate(Bundle savedInstanceState) {
		Log.i("MarketEnabler", "Start app");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainview);
		Log.i("MarketEnabler", "Start setting up tabs");
		/** setup tabs **/
		TabHost mTabHost = getTabHost();
		/** add tabs **/
		mTabHost.addTab(mTabHost.newTabSpec("actual").setIndicator("Actual")
				.setContent(R.id.actual));
		mTabHost.addTab(mTabHost.newTabSpec("custom")
				.setIndicator("Set custom").setContent(R.id.custom));
		mTabHost.setOnTabChangedListener(this);
		
		/** set current tab */
		mTabHost.setCurrentTabByTag("actual");
		
		viewActual = new ActualView(this); 
		
		
	}

	
	
	public View createTabContent(String tag) {
		
		return null;
	}

	@Override
	public void onTabChanged(String tabId) {
		Log.i("MarketEnabler", "Changed to tab initiated [" + tabId + "]");
		if (tabId.equals("actual")) {
			viewActual.updateView();
		} else if (tabId.equals("custom")) {
			if (viewCustom == null) {
				// creaete customview object
				// TODO
			} else {
				// is it really needed to update custom view? I don't think so!
				// viewCustom.updateView();
			}
		} else if (tabId.equals("list")) {
			// TODO viewList.updateView();
		}

	}
	
}
