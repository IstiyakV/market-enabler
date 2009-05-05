package com.androidiani.MarketEnabler;

import java.io.DataOutputStream;
import java.util.Date;





import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MarketEnabler extends Activity {
	private String operatorAlphaLong;
	private String operatorAlphaShort;
	private String operatorNumeric;
	private static String disclaymer= "" +
			"<html><head>" +
			"<style> body { font-size:14px; color:#000000; background-color:#cacaca;  } a { color:#ff0000; font-size:14px;}</style></head> <body >" +
			"Andrea B from <a href=\"http://www.androidiani.com\">www.androidiani.com</a> (The italian Resource About Android) presents <big>MarketEnabler</big>!!<br/>"+
			"<big><b><u><i>Use This Application at your own risk and Have Fun ;).</i></u></b></big><br/>"+
			" Special Thanks to:"+
			"<ul>"+
			"<li>Tim Strazzere (<a href=\"http://www.strazzere.com\">Strazzere.Com</a>)</li>"+
			"<li>My Girlfriend ( She was bored waiting for me to finish to write this application )</li>"+
			"<li>Every User from <a href=\"http://www.androidiani.com\">androidiani.com</a> For supporting and thanking me</li>"+
			"<li>MySelf</li>"+
			"<li>Tube8.Com ( For Mental support and stress management :) )</li></ul>";
	private class MyPhoneStateListener extends PhoneStateListener
	   {
	      public void onCallStateChanged(int state, String incomingNumber)
	      {
	         Log.d("CIAO","listener call state: " + state + " "+ new Date());
	      }
	      public void onServiceStateChanged(ServiceState serviceState) {
	    	setLocalValue(serviceState.getOperatorAlphaLong(), serviceState.getOperatorAlphaShort(),serviceState.getOperatorNumeric());
	    	
	      }
	   }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        // Set Content in webView
        
        WebView x = (WebView) this.findViewById(R.id.webview);
        x.loadData(disclaymer, "text/html", "UTF-8");
        
        
        MyPhoneStateListener myPhoneStateListener = new MyPhoneStateListener ();
        TelephonyManager telMgr=(TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        telMgr.listen(myPhoneStateListener,PhoneStateListener.LISTEN_SERVICE_STATE);
        // Cerco il bottone 1 e ci fracco su l'onclick listener :P
        Button be= (Button) this.findViewById(R.id.Button01);
        be.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	enableMarket();
            	Toast.makeText(MarketEnabler.this, "Done", Toast.LENGTH_LONG).show();

            }
        });
        
        Button bd= (Button) this.findViewById(R.id.Button02);
        bd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	TextView stato = (TextView) findViewById(R.id.TextView01);

            	
            	restoreValue();
            	Toast.makeText(MarketEnabler.this, "Done", Toast.LENGTH_LONG).show();
            }
        });
        
        
        
        
    }
    /**
     * This Method is going to launch all the commands and check about problems.
     * @param comandi Array containings all the bash commands to run ;)
     */
    public void doStuff(String [] comandi ) {
    	TextView stato = (TextView) this.findViewById(R.id.TextView01);

  
    	boolean trouble=false;

    	for ( String x : comandi ) {
    		if (!runRootCommand(x)) {
    			Log.d("MARKETENABLER","Problem running command: "+x);
    			trouble=true;
    		}
    			
    	}
    	if (!trouble) {
    		stato.setText("Everything Ok !! Enjoy :P\n" +
    				"Close this appplication and open the market ;) " +
    				"Click on the other side button for step back from this operation :)");
    	}else
    		stato.setText("We Got a proble Houston ;(");
    	stato.setVisibility(TextView.VISIBLE);
    }
    public void restoreValue() {
    	
    	String [] comandi= {
    			"setprop gsm.sim.operator.numeric "+this.operatorNumeric,
    			"setprop gsm.operator.numeric "+this.operatorNumeric,
    			"setprop gsm.sim.operator.iso-country us",
    			"setprop gsm.operator.iso-country us",
    			"setprop gsm.operator.alpha "+this.operatorAlphaLong,
    			"setprop gsm.sim.operator.alpha "+this.operatorAlphaLong,
    			"kill $(ps | grep vending | tr -s  ' ' | cut -d ' ' -f2)",
    			"rm -rf /data/data/com.android.vending/cache/*"

    			    	};
    	doStuff(comandi);

    }
    public void enableMarket() {
    	
     	String [] comandi= {
     			"setprop gsm.sim.operator.numeric 31026",
     			"setprop gsm.operator.numeric 31026",
     			"setprop gsm.sim.operator.iso-country us",
     			"setprop gsm.operator.iso-country us",
     			"setprop gsm.operator.alpha T-Mobile",
     			"setprop gsm.sim.operator.alpha T-Mobile",
     			"kill $(ps | grep vending | tr -s  ' ' | cut -d ' ' -f2)",
     			"rm -rf /data/data/com.android.vending/cache/*"

     			    	};
    	doStuff(comandi);
    	
    }
    public void setLocalValue(String operatorelungo, String operatore, String MCCMNC){
    	this.operatorAlphaLong=operatorelungo;
    	this.operatorAlphaShort=operatore;
    	this.operatorNumeric=MCCMNC;
    }

    public static boolean runRootCommand(String command) {
        Process process = null;
        DataOutputStream os = null;
            try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(command+"\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
            } catch (Exception e) {
                    Log.d("*** DEBUG ***", "Unexpected error - Here is what I know: "+e.getMessage());
                    return false;
            }
            finally {
                    try {
                            if (os != null) {
                                    os.close();
                            }
                            process.destroy();
                    } catch (Exception e) {
                            // nothing
                    }
            }
            return true;
    }
}