package com.androidiani.MarketEnabler.model;

public class ProviderConfig {
	private int gsmSimOperatorNumeric;
	private int gsmOperatorNumeric;
	private String gsmSimOperatorIsoCountry;
	private String gsmOperatorIsoCountry;
	private String gsmSimOperatorAlpha;
	private String gsmOperatorAlpha;
	private String SettingsHash;

	public ProviderConfig(int gsmSimOperatorNumeric, int gsmOperatorNumeric,
			String gsmSimOperatorIsoCountry, String gsmOperatorIsoCountry,
			String gsmSimOperatorAlpha, String gsmOperatorAlpha,
			String settingsHash) {
		super();
		this.gsmSimOperatorNumeric = gsmSimOperatorNumeric;
		this.gsmOperatorNumeric = gsmOperatorNumeric;
		this.gsmSimOperatorIsoCountry = gsmSimOperatorIsoCountry;
		this.gsmOperatorIsoCountry = gsmOperatorIsoCountry;
		this.gsmSimOperatorAlpha = gsmSimOperatorAlpha;
		this.gsmOperatorAlpha = gsmOperatorAlpha;
		SettingsHash = settingsHash;
	}

	public int getGsmSimOperatorNumeric() {
		return gsmSimOperatorNumeric;
	}

	public void setGsmSimOperatorNumeric(int gsmSimOperatorNumeric) {
		this.gsmSimOperatorNumeric = gsmSimOperatorNumeric;
	}

	public int getGsmOperatorNumeric() {
		return gsmOperatorNumeric;
	}

	public void setGsmOperatorNumeric(int gsmOperatorNumeric) {
		this.gsmOperatorNumeric = gsmOperatorNumeric;
	}

	public String getGsmSimOperatorIsoCountry() {
		return gsmSimOperatorIsoCountry;
	}

	public void setGsmSimOperatorIsoCountry(String gsmSimOperatorIsoCountry) {
		this.gsmSimOperatorIsoCountry = gsmSimOperatorIsoCountry;
	}

	public String getGsmOperatorIsoCountry() {
		return gsmOperatorIsoCountry;
	}

	public void setGsmOperatorIsoCountry(String gsmOperatorIsoCountry) {
		this.gsmOperatorIsoCountry = gsmOperatorIsoCountry;
	}

	public String getGsmSimOperatorAlpha() {
		return gsmSimOperatorAlpha;
	}

	public void setGsmSimOperatorAlpha(String gsmSimOperatorAlpha) {
		this.gsmSimOperatorAlpha = gsmSimOperatorAlpha;
	}

	public String getGsmOperatorAlpha() {
		return gsmOperatorAlpha;
	}

	public void setGsmOperatorAlpha(String gsmOperatorAlpha) {
		this.gsmOperatorAlpha = gsmOperatorAlpha;
	}

	public String getSettingsHash() {
		return SettingsHash;
	}

}
