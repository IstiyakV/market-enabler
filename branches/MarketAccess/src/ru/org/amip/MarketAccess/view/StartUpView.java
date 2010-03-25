package ru.org.amip.MarketAccess.view;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import ru.org.amip.MarketAccess.R;
import ru.org.amip.MarketAccess.utils.AppManager;
import ru.org.amip.MarketAccess.utils.CompleteListener;
import ru.org.amip.MarketAccess.utils.RunWithProgress;

public class StartUpView extends TabActivity implements OnTabChangeListener {
  private TelephonyManager tm;

  public static final String ACTUAL = "actual";
  private static final String CUSTOM = "custom";
  private static final String LIST = "list";

  private static final String SIM_NUM = "simNumeric";
  private static final String BACKUP_AVAILABLE = "backupAvailable";

  public static final String APPLY_ON_BOOT = "applyOnBoot";
  public static final String APPLY_SIM_NUM = "applySimNumeric";
  public static final String SHOW_NOTIFICATION = "showNotification";

  private TextView simNumeric;
  private Button restore;
  private CheckBox bootCheckbox;
  private CheckBox notificationCheckbox;
  private SharedPreferences preferences;
  public static final String MARKET_ACCESS = "MarketAccess";

  @Override
  public void onCreate(Bundle savedInstanceState) {
    tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.mainview);
    preferences = PreferenceManager.getDefaultSharedPreferences(this);

    setupActualTab();
    setupCustomTab();
    setupListAndTabs();

    // init process manager
    AppManager.getInstance(this);
  }

  private void setupCheckboxes() {
    bootCheckbox = (CheckBox) findViewById(R.id.bootCheckbox);
    notificationCheckbox = (CheckBox) findViewById(R.id.notificationCheckbox);

    boolean applyOnBoot = preferences.getBoolean(APPLY_ON_BOOT, false);

    bootCheckbox.setChecked(applyOnBoot);
    notificationCheckbox.setChecked(preferences.getBoolean(SHOW_NOTIFICATION, false));
    notificationCheckbox.setEnabled(applyOnBoot);

    bootCheckbox.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (((CheckBox) view).isChecked()) {
          AlertDialog.Builder builder = new AlertDialog.Builder(StartUpView.this);
          builder
            .setMessage(R.string.boot_warning)
            .setCancelable(false)
            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int id) {
                final String sim = simNumeric.getText().toString();
                final String msg = getString(R.string.boot_emulation_for) + ' ' + sim;
                saveBootSettings();
                Toast.makeText(StartUpView.this, msg, Toast.LENGTH_SHORT).show();
                notificationCheckbox.setEnabled(true);
              }
            }).
            setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int i) {
                dialog.cancel();
                bootCheckbox.setChecked(false);
              }
            });
          builder.create().show();
        } else {
          saveBootSettings();
          Toast.makeText(StartUpView.this, R.string.boot_emulation_off, Toast.LENGTH_SHORT).show();
          notificationCheckbox.setEnabled(false);
        }
      }
    });

    notificationCheckbox.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        saveNotificationSettings();
      }
    });
  }

  private void setupListAndTabs() {
    TabHost mTabHost = getTabHost();
    mTabHost.addTab(mTabHost.newTabSpec(ACTUAL).setIndicator(getString(R.string.actual)).setContent(R.id.actual));

    TabSpec tab = mTabHost.newTabSpec(LIST).setIndicator(getString(R.string.settings));
    tab.setContent(new Intent(this, ListView.class));
    mTabHost.addTab(tab);

    mTabHost.addTab(mTabHost.newTabSpec(CUSTOM).setIndicator(getString(R.string.custom)).setContent(R.id.custom));
    mTabHost.setOnTabChangedListener(this);

    mTabHost.setCurrentTabByTag(ACTUAL);
  }

  private void setupActualTab() {
    simNumeric = (TextView) findViewById(R.id.actualsimNumericValue);
    Button save = (Button) findViewById(R.id.buttonSave);
    save.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        backupSettings();
      }
    });
    restore = (Button) findViewById(R.id.buttonRestore);
    restore.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        restoreSettings();
      }
    });
    if (!preferences.getBoolean(BACKUP_AVAILABLE, false)) {
      restore.setEnabled(false);
    }
    setupCheckboxes();
    updateActualView();
  }

  private void setupCustomTab() {
    final TextView simNumeric = (TextView) findViewById(R.id.customsimNumericValue);
    simNumeric.setText(tm.getSimOperator());
    Button setValues = (Button) findViewById(R.id.customsetValues);
    setValues.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        new RunWithProgress(StartUpView.this, simNumeric.getText().toString(), getString(R.string.emulating)).doRun();
      }
    });
  }

  private void saveBootSettings() {
    SharedPreferences.Editor editor = preferences.edit();
    editor.putBoolean(APPLY_ON_BOOT, bootCheckbox.isChecked());
    editor.putString(APPLY_SIM_NUM, simNumeric.getText().toString());
    editor.commit();
  }

  private void saveNotificationSettings() {
    SharedPreferences.Editor editor = preferences.edit();
    editor.putBoolean(SHOW_NOTIFICATION, notificationCheckbox.isChecked());
    editor.commit();
  }

  private void backupSettings() {
    SharedPreferences.Editor editor = preferences.edit();
    editor.putBoolean(BACKUP_AVAILABLE, true);
    editor.putString(SIM_NUM, simNumeric.getText().toString());
    editor.commit();
    restore.setEnabled(true);
  }

  private void restoreSettings() {
    final String sim = preferences.getString(SIM_NUM, "");

    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage(getString(R.string.confirm_restore) + " '" + sim + "'?")
      .setCancelable(false)
      .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int id) {
          final RunWithProgress rwp = new RunWithProgress(StartUpView.this, sim, getString(R.string.restoring));
          rwp.setCompleteListener(new CompleteListener() {
            @Override
            public void onComplete() {
              updateActualView();
            }
          });
          rwp.doRun();
        }
      }).
      setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int i) {
          dialog.cancel();
        }
      });
    builder.create().show();
  }

  public void updateActualView() {
    simNumeric.setText(tm.getSimOperator());
  }

  @Override
  public void onTabChanged(String tabId) {
    if (tabId.equals(ACTUAL)) {
      updateActualView();
    } else if (tabId.equals(LIST)) {
      final Toast toast = Toast.makeText(this, getString(R.string.tap_hint), Toast.LENGTH_SHORT);
      toast.setGravity(Gravity.BOTTOM, 0, 0);
      toast.show();
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == 123) {
      finish();
    }
  }
}
