package wikdd.kasapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Thread thread = new Thread(){
            public void run(){
                try{
                    sleep(2000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                } finally {
//                    JIKA MAU LANGSUNG MASUK KE HOME ACTIVITY LANGSUNG YANG DI BAWAH INI:
                    startActivity(new Intent(SplashScreen.this, MainActivity.class));


                    finish(); //menutup activity splashScreen
                }
            }
        };
        thread.start();
    }
}
