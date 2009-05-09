package com.androidiani.MarketEnabler.model;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ProviderConfig {
	private int gsmSimOperatorNumeric;
	private int gsmOperatorNumeric;
	private String gsmSimOperatorIsoCountry;
	private String gsmOperatorIsoCountry;
	private String gsmSimOperatorAlpha;
	private String gsmOperatorAlpha;
	private String SettingsHash;
	private boolean hashValid;

	public ProviderConfig(int gsmSimOperatorNumeric, int gsmOperatorNumeric,
			String gsmSimOperatorIsoCountry, String gsmOperatorIsoCountry,
			String gsmSimOperatorAlpha, String gsmOperatorAlpha) {
		this.gsmSimOperatorNumeric = gsmSimOperatorNumeric;
		this.gsmOperatorNumeric = gsmOperatorNumeric;
		this.gsmSimOperatorIsoCountry = gsmSimOperatorIsoCountry;
		this.gsmOperatorIsoCountry = gsmOperatorIsoCountry;
		this.gsmSimOperatorAlpha = gsmSimOperatorAlpha;
		this.gsmOperatorAlpha = gsmOperatorAlpha;
		invalidateHash();
		
	}

	public int getGsmSimOperatorNumeric() {
		return gsmSimOperatorNumeric;
	}

	public void setGsmSimOperatorNumeric(int gsmSimOperatorNumeric) {
		this.gsmSimOperatorNumeric = gsmSimOperatorNumeric;
		invalidateHash();
	}

	public int getGsmOperatorNumeric() {
		return gsmOperatorNumeric;
	}

	public void setGsmOperatorNumeric(int gsmOperatorNumeric) {
		this.gsmOperatorNumeric = gsmOperatorNumeric;
		invalidateHash();
	}

	public String getGsmSimOperatorIsoCountry() {
		return gsmSimOperatorIsoCountry;
	}

	public void setGsmSimOperatorIsoCountry(String gsmSimOperatorIsoCountry) {
		this.gsmSimOperatorIsoCountry = gsmSimOperatorIsoCountry;
		invalidateHash();
	}

	public String getGsmOperatorIsoCountry() {
		return gsmOperatorIsoCountry;
	}

	public void setGsmOperatorIsoCountry(String gsmOperatorIsoCountry) {
		this.gsmOperatorIsoCountry = gsmOperatorIsoCountry;
		invalidateHash();
	}

	public String getGsmSimOperatorAlpha() {
		return gsmSimOperatorAlpha;
	
	}

	public void setGsmSimOperatorAlpha(String gsmSimOperatorAlpha) {
		this.gsmSimOperatorAlpha = gsmSimOperatorAlpha;
		invalidateHash();
	}

	public String getGsmOperatorAlpha() {
		return gsmOperatorAlpha;
		
	}

	public void setGsmOperatorAlpha(String gsmOperatorAlpha) {
		this.gsmOperatorAlpha = gsmOperatorAlpha;
		invalidateHash();
	}
	private void invalidateHash() {
		hashValid=false;
	}
	public String getSettingsHash() {
		String toEnc="";
		toEnc=this.getGsmOperatorAlpha()+this.getGsmOperatorIsoCountry()+this.getGsmOperatorNumeric()+this.getGsmSimOperatorAlpha()+this.getGsmSimOperatorIsoCountry()+this.getGsmSimOperatorNumeric();
		MessageDigest x=null;
        try {
			x=MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			
			e.printStackTrace();
			return null;
		}
		byte[] messageDigest = x.digest(toEnc.getBytes());
		BigInteger number = new BigInteger(1,messageDigest);
		String md5 = number.toString(16);
		while (md5.length()<32) {
			md5 = "0"+md5;
		}
		hashValid=true;
		return SettingsHash=md5;
    	
    	

	}

}
