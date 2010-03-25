package com.androidiani.MarketEnabler.presenter;

import android.content.Context;
import android.os.Handler;

import com.androidiani.MarketEnabler.view.StartUpView;

public interface IListView {


	// void displayError(String error);
	
	// void displayResult(boolean result);
	
	StartUpView getStartup();
	Handler getHandler();
	Context getCtx();

}
