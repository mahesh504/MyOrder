package in.arula.myorder.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import in.arula.myorder.MainActivity;
import in.arula.myorder.R;

public class SplashActivity extends AppCompatActivity {
    private SharedPreferences appPrefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        appPrefs = this.getSharedPreferences("mor_prefs", MODE_PRIVATE);
        new Thread(){
            @Override
            public void run(){

                try {
                    sleep(3000);
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }finally{
                    if(appPrefs.getBoolean("isLogin", false)){
                        startActivity(new Intent(SplashActivity.this, MainHomeActivity.class));
                        finish();
                    }else{
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        finish();
                    }
                }
            }
        }.start();

    }
}
