package com.androidiani.MarketEnabler.view;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidiani.MarketEnabler.R;
import com.androidiani.MarketEnabler.presenter.CustomPresenter;
import com.androidiani.MarketEnabler.presenter.ICustomView;

public class ListView extends ListActivity {
	
	/** UI elements **/
	private android.widget.ListView list;

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

	public ListView(StartUpView startup) {
		this.startup = startup;
		
		Log.i("MarketEnabler", "Start getting UI elements");
		/** get UI elements **/
		list = (android.widget.ListView) startup
				.findViewById(R.id.list);
		
		
		//presenter = new CustomPresenter(this);
//		startup.setListAdapter(new ArrayAdapter<String>(this,
//		          android.R.layout.simple_list_item_1, COUNTRIES));
//		  getListView().setTextFilterEnabled(true);

		 
	}

	public void displayError(String error) {
		// TODO Auto-generated method stub

	}


	public StartUpView getStartup() {
		return startup;
	}

}
