/**
 * 
 */
package com.androidiani.MarketEnabler.update;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import ch.racic.android.marketenabler.ads.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * @author rac
 *
 */
public class Update extends Service {

	private static final String EXTRA_NOTIFY = "EXTRA_NOTIFY";
	private static final String DOWNLOAD_URL = "DOWNLOAD_URL";
	/** Constants **/
	private static String urlLatestVersion = "http://market-enabler.googlecode.com/svn/branches/MarketChanger%20Starter%20list/update/latestversion";
	private static String urlLatestUrl = "http://market-enabler.googlecode.com/svn/branches/MarketChanger%20Starter%20list/update/latesturl";
	private static String logName = "MarketEnabler update";
	private static int UPDATE_NOTIFICATION_ID = 1;

	/** Variables **/
	private int latestVersion = 0;
	private int actualVersion = 0;
	private String latestUrl = "";

	/** managers **/
	private static SharedPreferences settings;

	@Override
	public void onStart(Intent intent, int startId) {
		// check if the intent contains notification data :)
		if (intent.getBooleanExtra(EXTRA_NOTIFY, false) && intent.hasExtra(DOWNLOAD_URL)) {
			NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			
			notificationManager.cancel(UPDATE_NOTIFICATION_ID);
			
			//launching the browser
			Intent downloadIntent = new Intent(		android.content.Intent.ACTION_VIEW	, Uri.parse(intent.getStringExtra(DOWNLOAD_URL)));
			downloadIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			this.startActivity(downloadIntent);
			this.stopSelf();
			return;
		} 
		/** get me my preferences **/
		settings = PreferenceManager
		.getDefaultSharedPreferences(getBaseContext());
		/** which version am I running? **/
		try {
			PackageInfo pi = getPackageManager().getPackageInfo(
					getPackageName(), 0);
			actualVersion = pi.versionCode;
		} catch (PackageManager.NameNotFoundException e) {
		}
		/** does a update exist? **/
		if (checkForUpdate()) {
			// Update exists
			Log.i(logName, "MarketEnabler version " + latestVersion
					+ " is available!");
			updateNotify();
		} else {
			// no update needed
			Log.i(logName,
			"you have the latest version of MarketEnabler running");
		}
		this.stopSelf();
	}

	/* (non-Javadoc)
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	private boolean checkForUpdate() {
		try {
			latestVersion = Integer.parseInt(getHttpString(urlLatestVersion));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			// e1.printStackTrace();
			Log.i(logName, e1.getLocalizedMessage());
			return false;
		}
		if (actualVersion < latestVersion) {
			try {
				latestUrl = getHttpString(urlLatestUrl);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				Log.i(logName, e.getLocalizedMessage());
			}
			return true;
		} else
			return false;
	}

	private void updateNotify() {
		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		int icon = R.drawable.me348;
		long when = System.currentTimeMillis();
		Notification notification = new Notification(icon,"MarketEnabler update available", when);
		//Intent notifyIntent = new Intent(		android.content.Intent.ACTION_VIEW	, Uri.parse(latestUrl));
		Intent notifyIntent = new Intent(this, Update.class);
		notifyIntent.putExtra(Update.EXTRA_NOTIFY, true);
		notifyIntent.putExtra(Update.DOWNLOAD_URL, latestUrl);
		
		PendingIntent pendingIntent =  PendingIntent.getService(
				this, 
				0, 
				notifyIntent, 
				android.content.Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS); 
			

		notification.setLatestEventInfo(
				getApplicationContext(),
				"MarketEnabler update available","Version " + latestVersion + " is ready, click to download",
				pendingIntent);
		
		notificationManager.notify(UPDATE_NOTIFICATION_ID, notification);
		
	}

	/**
	 * taken from http://www.devx.com/wireless/Article/39810/1954
	 * 
	 * @param urlString
	 * @return InputStream
	 * @throws IOException
	 */
	private String getHttpString(String urlString) throws IOException {
		InputStream in = null;
		int response = -1;

		URL url = new URL(urlString);
		URLConnection conn = url.openConnection();

		if (!(conn instanceof HttpURLConnection))
			throw new IOException("Not an HTTP connection");

		try {
			HttpURLConnection httpConn = (HttpURLConnection) conn;
			httpConn.setAllowUserInteraction(false);
			httpConn.setInstanceFollowRedirects(true);
			httpConn.setRequestMethod("GET");
			httpConn.connect();

			response = httpConn.getResponseCode();
			if (response == HttpURLConnection.HTTP_OK) {
				in = httpConn.getInputStream();
			}
		} catch (Exception ex) {
			throw new IOException("Error connecting");
		}
		if (in != null) {
			StringBuilder sb = new StringBuilder();
			String line;

			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(in, "UTF-8"));
				while ((line = reader.readLine()) != null) {
					sb.append(line); // .append("\n");
				}
			} finally {
				in.close();
			}
			return sb.toString();
		} else
			return "";

	}


}
