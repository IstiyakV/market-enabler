package ru.org.amip.MarketAccess.view;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import ru.org.amip.MarketAccess.model.ProviderAdapter;
import ru.org.amip.MarketAccess.model.ProviderConfig;
import ru.org.amip.MarketAccess.utils.CompleteListener;
import ru.org.amip.MarketAccess.utils.RunWithProgress;

import java.util.ArrayList;

import static ru.org.amip.MarketAccess.R.string;

public class ListView extends ListActivity {
  private ArrayList<ProviderConfig> providers;
  private Context                   ctx;
  public  StartUpView               startup;
  private ProviderAdapter           adapter;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    startup = (StartUpView) getParent();
    startup.setList(this);
    ctx = this;
    providers = createDefaultList();
    adapter = new ProviderAdapter(providers, ctx, startup.getSimOperator());
    setListAdapter(adapter);
  }

  public static ArrayList<ProviderConfig> createDefaultList() {
    ArrayList<ProviderConfig> list = new ArrayList<ProviderConfig>();
    list.add(new ProviderConfig(310260, "us", "T-Mobile"));
    list.add(new ProviderConfig(23203, "au", "T-Mobile"));
    list.add(new ProviderConfig(20416, "nl", "T-Mobile"));
    list.add(new ProviderConfig(26207, "de", "O2"));
    list.add(new ProviderConfig(26203, "de", "E-Plus"));
    list.add(new ProviderConfig(22802, "ch", "sunrise"));
    list.add(new ProviderConfig(22201, "it", "TIM"));
    list.add(new ProviderConfig(27203, "ie", "Meteor"));
    list.add(new ProviderConfig(25001, "ru", "MTS"));
    list.add(new ProviderConfig(25002, "ru", "MegaFon"));
    list.add(new ProviderConfig(25099, "ru", "Beeline"));
    list.add(new ProviderConfig(25020, "ru", "Tele2"));
    return list;
  }

  @Override
  protected void onListItemClick(android.widget.ListView l, View v, final int position, long id) {
    ProviderConfig cfg = providers.get(position);
    final String alpha = cfg.getGsmSimOperatorAlpha();
    final String numeric = String.valueOf(cfg.getGsmSimOperatorNumeric());
    new AlertDialog.Builder(this)
      .setMessage(String.format(getString(string.emulate), alpha, numeric))
      .setCancelable(false)
      .setPositiveButton(string.yes, new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int id) {
          setValues(position);
        }
      }).
      setNegativeButton(string.no, new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int i) {
          dialog.cancel();
        }
      }).create().show();
  }

  public void setValues(final int i) {
    final ProviderConfig cfg = providers.get(i);
    String value = String.valueOf(cfg.getGsmSimOperatorNumeric());
    final RunWithProgress rwp =
      new RunWithProgress(ctx, value, String.format(getString(string.emulating_name), cfg.getGsmSimOperatorAlpha()));
    rwp.setCompleteListener(new CompleteListener() {
      @Override
      public void onComplete() {
        startup.updateActualView();
        invalidate();
      }
    });
    rwp.doRun();
  }

  public void invalidate() {
    adapter.setOperator(String.valueOf(startup.getSimOperator()));
    adapter.clearCache();
    getListView().invalidateViews();
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    return getParent().onKeyDown(keyCode, event);
  }
}
