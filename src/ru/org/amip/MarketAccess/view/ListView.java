package ru.org.amip.MarketAccess.view;

import android.R;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import ru.org.amip.MarketAccess.model.ProviderConfig;
import ru.org.amip.MarketAccess.utils.CompleteListener;
import ru.org.amip.MarketAccess.utils.RunWithProgress;

import java.util.ArrayList;

import static ru.org.amip.MarketAccess.R.string;

public class ListView extends ListActivity {
  protected ArrayList<ProviderConfig> providers;
  private Context ctx;
  public StartUpView startup;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    startup = (StartUpView) getParent();
    ctx = this;
    providers = createDefaultList();
    setListAdapter(new ArrayAdapter<ProviderConfig>(this, R.layout.simple_list_item_1, providers));
    registerForContextMenu(getListView());
  }

  @Override
  public boolean onContextItemSelected(MenuItem item) {
    AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
    setValues(info.position);
    return true;
  }

  @Override
  public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
    menu.setHeaderTitle(getResources().getString(string.confirmation));
    menu.add(string.emulate);
  }

  public static ArrayList<ProviderConfig> createDefaultList() {
    ArrayList<ProviderConfig> list = new ArrayList<ProviderConfig>();
    list.add(new ProviderConfig(310260, "us", "T-Mobile"));
    list.add(new ProviderConfig(22802, "ch", "sunrise"));
    list.add(new ProviderConfig(26207, "de", "o2 - de"));
    list.add(new ProviderConfig(26203, "de", "simyo"));
    list.add(new ProviderConfig(22201, "it", "TIM"));
    list.add(new ProviderConfig(23203, "au", "T-Mobile"));
    list.add(new ProviderConfig(20416, "nl", "T-Mobile"));
    list.add(new ProviderConfig(27203, "ie", "Meteor"));
    list.add(new ProviderConfig(25001, "ru", "MTS"));
    list.add(new ProviderConfig(25002, "ru", "MegaFon"));
    list.add(new ProviderConfig(25028, "ru", "Beeline"));
    return list;
  }

  public void setValues(int i) {
    ProviderConfig cfg = providers.get(i);
    String value = String.valueOf(cfg.getGsmSimOperatorNumeric());
    final RunWithProgress rwp =
      new RunWithProgress(ctx, value, getString(string.emulating_name) + ' ' + cfg.getGsmSimOperatorAlpha());
    rwp.setCompleteListener(new CompleteListener() {
      @Override
      public void onComplete() {
        startup.updateActualView();
        startup.getTabHost().setCurrentTabByTag(StartUpView.ACTUAL);
      }
    });
    rwp.doRun();
  }
}
