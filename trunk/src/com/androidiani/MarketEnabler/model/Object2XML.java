package com.androidiani.MarketEnabler.model;

import java.util.List;

public class Object2XML {
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
					+ "</gsmSimOperatorNumeric>\n";
			ret += "<gsmOperatorIso-Country>" + pc.getGsmOperatorIsoCountry()
					+ "</gsmSimOperatorIso-Country>\n";
			ret += "<gsmOperatorAlpha>" + pc.getGsmOperatorAlpha()
					+ "</gsmSimOperatorAlpha>\n";
			ret += "<SettingsHash>" + pc.getSettingsHash()
					+ "</SettingsHash>\n";
			ret += "</configuration>";
		}
		return ret;
	}
}
