package ch.racic.android.marketenabler.ads;

import com.androidiani.MarketEnabler.view.StartUpView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Splash extends Activity {
        public void onCreate(Bundle icicle) {
                super.onCreate(icicle);
                new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                                Splash.this.startActivity(new Intent(Splash.this, StartUpView.class));
                                Splash.this.finish();
                        }
                });
        }
}