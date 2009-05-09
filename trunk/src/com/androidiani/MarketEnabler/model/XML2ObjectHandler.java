package com.androidiani.MarketEnabler.model;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;


public class XML2ObjectHandler extends DefaultHandler {
	private int gsmSimOperatorNumeric;
	private int gsmOperatorNumeric;
	private String gsmSimOperatorIsoCountry;
	private String gsmOperatorIsoCountry;
	private String gsmSimOperatorAlpha;
	private String gsmOperatorAlpha;

	private boolean inSimNum = false;
	private boolean inOpNum = false;
	private boolean inSimIso = false;
	private boolean inOpIso = false;
	private boolean inSimAlpha = false;
	private boolean inOpAlpha = false;
	private boolean inHash = false;
	private boolean inConfig = false;
	
	public XML2ObjectHandler() {
		super();
	}
	
	public void startElement(String uri, String name, String qName,
			Attributes atts) {
		if (name.trim().equals("configuration")) {
			this.inConfig = true;
		} else if (name.trim().equals("gsmSimOperatorNumeric")) {
			this.inSimNum = true;
		} else if (name.trim().equals("gsmSimOperatorIso-Country")) {
			this.inSimIso = true;
		} else if (name.trim().equals("gsmSimOperatorAlpha")) {
			this.inSimAlpha = true;
		} else if (name.trim().equals("gsmOperatorNumeric")) {
			this.inOpNum = true;
		} else if (name.trim().equals("gsmOperatorIso-Country")) {
			this.inOpIso = true;
		} else if (name.trim().equals("gsmOperatorAlpha")) {
			this.inOpAlpha = true;
		} else if (name.trim().equals("SettingsHash")) {
			this.inHash = true;
		} 
	}

	public void endElement(String uri, String name, String qName) {
		if (name.trim().equals("configuration")) {
			this.inConfig = false;
		} else if (name.trim().equals("gsmSimOperatorNumeric")) {
			this.inSimNum = false;
		} else if (name.trim().equals("gsmSimOperatorIso-Country")) {
			this.inSimIso = false;
		} else if (name.trim().equals("gsmSimOperatorAlpha")) {
			this.inSimAlpha = false;
		} else if (name.trim().equals("gsmOperatorNumeric")) {
			this.inOpNum = false;
		} else if (name.trim().equals("gsmOperatorIso-Country")) {
			this.inOpIso = false;
		} else if (name.trim().equals("gsmOperatorAlpha")) {
			this.inOpAlpha = false;
		} else if (name.trim().equals("SettingsHash")) {
			this.inHash = false;
		} 
		
	}
	
	public void characters(char ch[], int start, int length) {
		System.out.print("Characters:    \"");
		for (int i = start; i < start + length; i++) {
			switch (ch[i]) {
			case '\\':
				System.out.print("\\\\");
				break;
			case '"':
				System.out.print("\\\"");
				break;
			case '\n':
				System.out.print("\\n");
				break;
			case '\r':
				System.out.print("\\r");
				break;
			case '\t':
				System.out.print("\\t");
				break;
			default:
				System.out.print(ch[i]);
				break;
			}
		}
		System.out.print("\"\n");
	}


}
