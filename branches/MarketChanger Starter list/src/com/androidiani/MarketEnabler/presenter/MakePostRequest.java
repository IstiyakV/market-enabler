package com.androidiani.MarketEnabler.presenter;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;


import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * Questa bellissima classe non far� altro che effettuare una chiamata ad internet in post e ritornare il risultato ;)
 * @author veke
 *
 */
public class MakePostRequest implements Runnable{
	private String TAG= "MarketEnabler";
	private String toUrl;
	private ArrayList<BasicHeader> headers;
	private ArrayList<BasicNameValuePair> data;
	private Handler handler;
	public MakePostRequest () {
		headers= new ArrayList<BasicHeader>();
		data=new ArrayList<BasicNameValuePair>();
		return;
	}
	/**
	 * Setta l'url dove fare la richiesta
	 * @param url l'url completo di http
	 * @return MakePostRequest per concatenare le richieste ;) Evviva GIAVA ;)
	 */
	public MakePostRequest setUrl(String url) {
		toUrl=url;
		return this;
	}
	/**
	 * Aggiunge un header da mandare.
	 * @param nome E' il nome dell'header http
	 * @param valore e' il valore dell'header http
	 * @return This
	 */
	public MakePostRequest addHeader(String nome,String valore) {
		headers.add(new BasicHeader(nome,valore));
		return this;
	}
	
	public MakePostRequest addData(String nome, String valore) {
		data.add(new BasicNameValuePair(nome,valore));
		return this;
	}
	public MakePostRequest setHandler (Handler h) {
		handler=h;
		return this;
	}
	public void doRequest () {
		Thread thread = new Thread(this);
        thread.start();
	}
	@Override
	public void run() {
		HttpPost hp=null;
		try {
			hp=new HttpPost(new URI(toUrl));
		} catch (URISyntaxException e) {
			Log.e(TAG,"Problemi con La uri di autentificati");
			handler.sendEmptyMessage(-1);
			return;
		}
		/* Aggiungo gli headers */
		for (int i=0; i<headers.size();i++)
			hp.addHeader(headers.get(i));
		
		
		UrlEncodedFormEntity entity= null;
		try {
			 entity =new UrlEncodedFormEntity(data,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			Log.e(TAG,"Problemi di codifica in UrlEncodedFormEntity");
			handler.sendEmptyMessage(-1);
			return;
		}
				
		hp.setEntity(entity);
		
		DefaultHttpClient httpClient=new DefaultHttpClient();
		HttpResponse res= null;
		try {
			res= httpClient.execute(hp);
		} catch (ClientProtocolException e) {
			Log.e(TAG,"Problemi in connessione internet");
			handler.sendEmptyMessage(-1);
			return;
		} catch (IOException e) {
			Log.e(TAG,"Problemi di IO");
			handler.sendEmptyMessage(-1);
			return;
		}
		String risultato="";
		try {
			risultato= inputStreamToString(res.getEntity().getContent());
		} catch (IllegalStateException e) {
			Log.e(TAG,"Problema con la ricezione del contenuto da internet");
			handler.sendEmptyMessage(-1);
			return;
		} catch (IOException e) {
			Log.e(TAG,"Problema con l'io della ricezione da internet");
			handler.sendEmptyMessage(-1);
			return;
		}
		
		// Finisco per mandare all'handler il messaggio richiesto ;)
		Message toSend= Message.obtain();
		toSend.obj= risultato;
		toSend.arg1=1;
		handler.sendMessage(toSend);
		

		
		
		
	}
	public static String inputStreamToString (InputStream in)  {
	    StringBuffer out = new StringBuffer();
	    byte[] b = new byte[4096];
	    try {
			for (int n; (n = in.read(b)) != -1;) {
			    out.append(new String(b, 0, n));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return out.toString();
	}
	
}
