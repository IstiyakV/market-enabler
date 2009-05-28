package com.androidiani.MarketEnabler;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;





import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MarketEnabler extends Activity  implements Runnable {
	private String operatorAlphaLong;
	private String operatorAlphaShort;
	private String operatorNumeric;
	private ProgressDialog pd;
	private TextView stato;
	private String[] commands;
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
			"</ul>";
	private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
        	// This Means that we've not finished the work ..
        
        	if (msg.arg2 != 0) {
        		pd.incrementProgressBy(msg.arg1-pd.getProgress());
        		 
        	} else {
        		pd.dismiss();
        		Toast.makeText(MarketEnabler.this, "Done ;)",Toast.LENGTH_LONG).show();
        		if ( msg.arg1 == 0)  
        			stato.setText("Everything Ok !! Enjoy :P\n" +
        				"Close this appplication and open the market ;) " +
        				"Click on the other side button for step back from this operation :)");
        		else
        			stato.setText("We Got a Problem Houston :(");
        		
        	}
        	
        }
    };
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

            }
        });
        
        Button bd= (Button) this.findViewById(R.id.Button02);
        bd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
       
            	restoreValue();
 
            }
        });
        
        
        
        
    }
    /**
     * This Method is going to launch all the commands and check about problems.
     * @param comandi Array containings all the bash commands to run ;)
     */
    public void doStuff(String [] comandi ) {
    	TextView state = (TextView) this.findViewById(R.id.TextView01);
    	stato=state;
    	pd= new ProgressDialog(this);
    	pd.setMax(comandi.length+2);
    	pd.setProgress(1);
    	pd.setTitle("Doing Stuff");
    	pd.setMessage("Root Stuff");
    	pd.show();
    	commands = comandi;
        Thread thread = new Thread(this);
        thread.start();
    
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

   
	@Override
	public void run() {

		Message msg=null;
		// Look at the var commands
		int i=1;

		Process process = null;
        DataOutputStream os = null;
        try {
        	// Getting Root ;)
	        process = Runtime.getRuntime().exec("su");
	       
	        
	        os = new DataOutputStream(process.getOutputStream());
	        
	        // Doing Stuff ;)
	        for ( String single : commands ) {
	          	os.writeBytes(single+"\n");
	            os.flush();
	          	msg = Message.obtain();
	          	msg.arg1=i++;
	          	msg.arg2=-1;// This because when 0 i will dismiss the progressbar.
	          	handler.sendMessage(msg);	// Not checking if it  goes ok because i trust android :P

	      	}
	        
	        os.writeBytes("exit\n");
	        os.flush();
	       
	        Thread.currentThread().sleep(3000);
	       
	       process.waitFor();
	        msg =  Message.obtain();
	        msg.arg1=0;
	        msg.arg2=0;
	        handler.sendMessage(msg);
	        
            } catch (Exception e) {
	            	msg = Message.obtain();
	    	        msg.arg1=1;
	    	        msg.arg2=0;
	    	        handler.sendMessage(msg);
                    Log.d("*** DEBUG ***", "Unexpected error - Here is what I know: "+e.getMessage());
                    return ;
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

		
	        return;
		
		
	}

}