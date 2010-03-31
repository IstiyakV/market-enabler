package ru.org.amip.MarketAccess.model;

public class ProviderConfig {
  private final int gsmSimOperatorNumeric;
  private final String gsmSimOperatorIsoCountry;
  private final String gsmSimOperatorAlpha;

  public ProviderConfig(int gsmSimOperatorNumeric, String gsmSimOperatorIsoCountry, String gsmSimOperatorAlpha) {
    this.gsmSimOperatorNumeric = gsmSimOperatorNumeric;
    this.gsmSimOperatorIsoCountry = gsmSimOperatorIsoCountry;
    this.gsmSimOperatorAlpha = gsmSimOperatorAlpha;
  }

  public int getGsmSimOperatorNumeric() {
    return gsmSimOperatorNumeric;
  }

  public String getGsmSimOperatorAlpha() {
    return gsmSimOperatorAlpha;
  }

  public String getGsmSimOperatorIsoCountry() {
    return gsmSimOperatorIsoCountry;
  }
}
