package com.hometodo.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowManager;

import com.hometodo.R;


public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(3000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{

                    SharedPreferences shared = getSharedPreferences("userInfo", MODE_PRIVATE);
                    String nombre=shared.getString("nombre","");

                    if (nombre.equals("")){
                        Intent main = new Intent(SplashScreen.this, Registro.class);
                        startActivity(main);
                        finish();
                    }else{
                        Intent main = new Intent(SplashScreen.this, MainActivity.class);
                        startActivity(main);
                        finish();
                    }

                }
            }
        };
        timerThread.start();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }
}
