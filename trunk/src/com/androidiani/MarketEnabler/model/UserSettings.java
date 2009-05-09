package com.androidiani.MarketEnabler.model;

public class UserSettings {
	private String defaultProviderSettingsHash;
	private boolean autosetDefaultOnStart;

	public UserSettings() {

	}

	public String getDefaultProviderSettingsHash() {
		return defaultProviderSettingsHash;
	}

	public void setDefaultProviderSettingsHash(
			String defaultProviderSettingsHash) {
		this.defaultProviderSettingsHash = defaultProviderSettingsHash;
	}

	public boolean isAutosetDefaultOnStart() {
		return autosetDefaultOnStart;
	}

	public void setAutosetDefaultOnStart(boolean autosetDefaultOnStart) {
		this.autosetDefaultOnStart = autosetDefaultOnStart;
	}
	
	
}
