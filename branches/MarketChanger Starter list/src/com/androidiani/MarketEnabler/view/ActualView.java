package com.androidiani.MarketEnabler.view;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidiani.MarketEnabler.R;
import com.androidiani.MarketEnabler.presenter.ActualPresenter;
import com.androidiani.MarketEnabler.presenter.IActualView;
import com.androidiani.MarketEnabler.presenter.RestorePresenter;

public class ActualView implements IActualView {
	
	/** UI elements **/
	private TextView simNumeric, operatorNumeric, simISO, operatorISO,
			simAlpha, operatorAlpha;
	
	private StartUpView startup;
	
	private ActualPresenter presenter;
	private RestorePresenter restorePresenter = null;
	
	private Button save, restore;

	private static final String SIMNUM = "simNumeric",
			OPNUM = "operatorNumeric", SIMISO = "simISO",
			OPISO = "operatorISO", SIMAL = "simAlpha", OPAL = "operatorAlpha",
			BACKUPAV = "BackupAvailable", PREF_NAME = "MarketEnablerBackup";

	public ActualView(StartUpView startup) {
		this.startup = startup;
		Log.i("MarketEnabler", "Start getting UI elements");
		/** get UI elements **/
		simNumeric = (TextView) startup
				.findViewById(R.id.actualsimNumericValue);
		// operatorNumeric = (TextView) startup
		// .findViewById(R.id.actualoperatorNumericValue);
		// simISO = (TextView) startup.findViewById(R.id.actualsimISOValue);
		// operatorISO = (TextView) startup
		// .findViewById(R.id.actualoperatorISOValue);
		// simAlpha = (TextView) startup.findViewById(R.id.actualsimAlphaValue);
		// operatorAlpha = (TextView) startup
		// .findViewById(R.id.actualoperatorAlphaValue);
		presenter = new ActualPresenter(this);
		save = (Button) startup.findViewById(R.id.buttonSave);
		save.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				backupSettings();
			}
		});
		restore = (Button) startup.findViewById(R.id.buttonRestore);
		restore.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				restoreSettings();
			}
		});
		if (!backupAvailable()) {
			restore.setEnabled(false);
			SharedPreferences settings = startup.getSharedPreferences(
					PREF_NAME, 0);
			restore.setText("Restore");
		}

	}

	private boolean backupAvailable() {
		SharedPreferences settings = startup.getSharedPreferences(PREF_NAME, 0);
		return settings.getBoolean(BACKUPAV, false);
	}

	private void backupSettings() {
		SharedPreferences settings = startup.getSharedPreferences(PREF_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(BACKUPAV, true);
		editor.putString(SIMNUM, simNumeric.getText().toString());
		// editor.putString(OPNUM, operatorNumeric.getText().toString());
		// editor.putString(SIMISO, simISO.getText().toString());
		// editor.putString(OPISO, operatorISO.getText().toString());
		// editor.putString(SIMAL, simAlpha.getText().toString());
		// editor.putString(OPAL, operatorAlpha.getText().toString());
		editor.commit();
		restore.setEnabled(true);
		restore.setText("Restore");
	}

	private void restoreSettings() {
		if (restorePresenter == null) {
			restorePresenter = new RestorePresenter(this);
		}
		restorePresenter.setValues();
		
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
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			// This Means that we've not finished the work ..
			Log.d("MarketEnabler", "progress msg[" + msg.arg1 + ", " + msg.arg2
					+ "] getprogress[" + restorePresenter.getPd().getProgress()
					+ "]");

			if (msg.arg2 != 0) {
				Log.d("MarketEnabler", "increment progress msg[" + msg.arg1
						+ ", " + msg.arg2 + "] getprogress["
						+ restorePresenter.getPd().getProgress() + "]");
				// presenter.getPd().incrementProgressBy(msg.arg1 -
				// pd.getProgress());
				restorePresenter.getPd().setProgress(msg.arg1);
			} else {
				Log.d("MarketEnabler", "dismiss progress msg[" + msg.arg1
						+ ", " + msg.arg2 + "] getprogress["
						+ restorePresenter.getPd().getProgress() + "]");
				// pd.setProgress(8);
				restorePresenter.getPd().dismiss();
				// Toast.makeText(startup, "Done ;)", Toast.LENGTH_LONG).show();
				if (msg.arg1 == 0)
					Toast.makeText(startup, " Done and all set",
							Toast.LENGTH_LONG).show();
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

	

}
