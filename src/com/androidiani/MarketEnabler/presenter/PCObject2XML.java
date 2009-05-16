package com.androidiani.MarketEnabler.presenter;

import java.util.List;

import com.androidiani.MarketEnabler.model.ProviderConfig;

public class PCObject2XML {
	public static String getXML(ProviderConfig pc) {
		String ret = "<?xml version=\"1.0\"?>\n";
		ret += "<configuration>\n";
		ret += "<gsmSimOperatorNumeric>" + pc.getGsmSimOperatorNumeric()
				+ "</gsmSimOperatorNumeric>\n";
		ret += "<gsmSimOperatorIso-Country>" + pc.getGsmSimOperatorIsoCountry()
				+ "</gsmSimOperatorIso-Country>\n";
		ret += "<gsmSimOperatorAlpha>" + pc.getGsmSimOperatorAlpha()
				+ "</gsmSimOperatorAlpha>\n";
		ret += "<gsmOperatorNumeric>" + pc.getGsmOperatorNumeric()
				+ "</gsmSimOperatorNumeric>\n";
		ret += "<gsmOperatorIso-Country>" + pc.getGsmOperatorIsoCountry()
				+ "</gsmSimOperatorIso-Country>\n";
		ret += "<gsmOperatorAlpha>" + pc.getGsmOperatorAlpha()
				+ "</gsmSimOperatorAlpha>\n";
		ret += "<SettingsHash>" + pc.getSettingsHash() + "</SettingsHash>\n";
		ret += "</configuration>";
		return ret;
	}

	public static String getXML(List<ProviderConfig> pcl) {
		String ret = "<?xml version=\"1.0\"?>";
		for (ProviderConfig pc : pcl) {
			ret += "\n<configuration>\n";
			ret += "<gsmSimOperatorNumeric>" + pc.getGsmSimOperatorNumeric()
					+ "</gsmSimOperatorNumeric>\n";
			ret += "<gsmSimOperatorIso-Country>"
					+ pc.getGsmSimOperatorIsoCountry()
					+ "</gsmSimOperatorIso-Country>\n";
			ret += "<gsmSimOperatorAlpha>" + pc.getGsmSimOperatorAlpha()
					+ "</gsmSimOperatorAlpha>\n";
			ret += "<gsmOperatorNumeric>" + pc.getGsmOperatorNumeric()
					+ "</gsmOperatorNumeric>\n";
			ret += "<gsmOperatorIso-Country>" + pc.getGsmOperatorIsoCountry()
					+ "</gsmOperatorIso-Country>\n";
			ret += "<gsmOperatorAlpha>" + pc.getGsmOperatorAlpha()
					+ "</gsmOperatorAlpha>\n";
			ret += "<SettingsHash>" + pc.getSettingsHash()
					+ "</SettingsHash>\n";
			ret += "</configuration>";
		}
		return ret;
	}
}
