package com.androidiani.MarketEnabler.view;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

public class List1 extends ListActivity implements IListView {

	protected ArrayList<ProviderConfig> providerlist; // = new
														// ArrayList<String>();
	private Context ctx;
	private ListPresenter presenter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ctx = getApplicationContext();
		presenter = new ListPresenter(this);
		providerlist = presenter.createDefaultList();
		
		
		// Use an existing ListAdapter that will map an array
		// of strings to TextViews
		setListAdapter(new ArrayAdapter<ProviderConfig>(this,
				android.R.layout.simple_list_item_1, providerlist));
		
		getListView().setTextFilterEnabled(true);
		
		registerForContextMenu(getListView());
		
	}
	
    public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		// Toast.makeText(this, "Longpress: " + info.position,
		// Toast.LENGTH_SHORT)
		// .show();
		presenter.setValues(info.position);
		return true;
	}

	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
		int selPos = info.position;

		menu.add("fake this provider now");
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
					Toast.makeText(ctx, " Done and all set",
							Toast.LENGTH_LONG).show();
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
		// TODO Auto-generated method stub
		return ctx;
	}
	
}