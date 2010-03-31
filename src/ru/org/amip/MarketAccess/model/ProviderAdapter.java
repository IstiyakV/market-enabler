package ru.org.amip.MarketAccess.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import ru.org.amip.MarketAccess.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: Mar 31, 2010
 * Time: 1:51:45 AM
 *
 * @author serge
 */
public class ProviderAdapter extends BaseAdapter {
  private final List<ProviderConfig> elements;
  private final Context c;
  // cache
  private final List<View> views;
  private final int count;
  private String operator;

  public ProviderAdapter(List<ProviderConfig> elements, Context c, String operator) {
    count = elements.size();
    views = new ArrayList<View>(count);
    initCache();
    this.elements = elements;
    this.c = c;
    this.operator = operator;
  }

  private void initCache() {
    for (int i = 0; i < count; i++) {
      views.add(null);
    }
  }

  public synchronized void clearCache() {
    for (int i = 0; i < count; i++) {
      views.set(i, null);
    }
  }

  public synchronized void setOperator(String operator) {
    this.operator = operator;
  }

  @Override
  public int getCount() {
    return count;
  }

  @Override
  public Object getItem(int i) {
    return elements.get(i);
  }

  @Override
  public long getItemId(int i) {
    return i;
  }

  @Override
  public View getView(int i, View view, ViewGroup group) {
    View result = views.get(i);
    if (result == null) {
      ProviderConfig cfg = elements.get(i);
      result = LayoutInflater.from(c).inflate(R.layout.itemview, group, false);
      final TextView provider = (TextView) result.findViewById(R.id.item_provider);
      provider.setText(cfg.getGsmSimOperatorAlpha());
      if (isActual(i)) {
        provider.setTextColor(c.getResources().getColor(R.color.active));
      } else {
        provider.setTextColor(c.getResources().getColor(android.R.color.primary_text_dark));
      }
      ((TextView) result.findViewById(R.id.item_country)).setText(cfg.getGsmSimOperatorIsoCountry().toUpperCase());
      final TextView sim = (TextView) result.findViewById(R.id.item_sim);
      sim.setText(String.valueOf(cfg.getGsmSimOperatorNumeric()));
      if (isActual(i)) {
        sim.setTextColor(c.getResources().getColor(R.color.active));
      } else {
        sim.setTextColor(c.getResources().getColor(android.R.color.secondary_text_dark));
      }
      views.set(i, result);
    }
    return result;
  }

  @Override
  public boolean isEnabled(int position) {
    return !isActual(position);
  }

  private boolean isActual(int position) {
    return String.valueOf(elements.get(position).getGsmSimOperatorNumeric()).equals(operator);
  }
}
