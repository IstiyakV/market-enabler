package ch.racic.android.marketenabler;

import com.androidiani.MarketEnabler.view.StartUpView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

public class ExecReceiver extends BroadcastReceiver{
	private Context ctx;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i("MarketEnabler", "remote exec got triggered");
		ctx = context;
		if (isPackageAvailable("ch.racic.android.marketenabler.donatekey")) {
			// get the extras from the intent
			Bundle b = intent.getExtras();
		    String code = b.getString("code");
		    Log.i("MarketEnabler", "remote triggered set with value ["+code+"]");
			// execute the commands
			new Executor(code);
		} else {
			ctx.startActivity(new Intent("android.intent.action.VIEW",Uri.parse(StartUpView.marketDonateUrl)));
		}
	}
	
	public boolean isPackageAvailable(String packageName) {
		int sigMatch = ctx.getPackageManager().checkSignatures(ctx.getPackageName(), packageName);
		return sigMatch == PackageManager.SIGNATURE_MATCH;
	}
}   
