package wikdd.kasapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;

/**
 * Created by ubuntu on 9/16/2018.
 */

public class IntroActivity extends AppIntro2 {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        MACAM" ANIMASI"
//        setFadeAnimation();
//        setZoomAnimation();
//        setFlowAnimation();


        // Note here that we DO NOT use setContentView();

        // Add your slide fragments here.
        // AppIntro will automatically generate the dots indicator and buttons.

//        ADA DUA PILIHAN CARA MEMBUAT INTRO, MENGGUNAKAN FRAGMENT ATAU OTOMATIS
//        CARA PERTAMA DENGAN MENGGUNAKAN FRAGMENT :

//        addSlide(firstFragment);
//        addSlide(secondFragment);
//        addSlide(thirdFragment);
//        addSlide(fourthFragment);

        // Instead of fragments, you can also use our default slide.
        // Just create a `SliderPage` and provide title, description, background and image.
        // AppIntro will do the rest.



        SliderPage sliderPage = new SliderPage();

//        SLIDER 1
        sliderPage.setTitle("Selamat Datang");
        sliderPage.setDescription("Saya Android Developer");
        sliderPage.setImageDrawable(R.drawable.android);
        sliderPage.setBgColor(getResources().getColor(R.color.colorAccent));
        addSlide(AppIntroFragment.newInstance(sliderPage));

//        SLIDER 2
        sliderPage.setTitle("Kas App");
        sliderPage.setDescription("Pencatatan uang masuk dan keluar");
        sliderPage.setImageDrawable(R.drawable.calc);
        sliderPage.setBgColor(getResources().getColor(R.color.colorAccent));
        addSlide(AppIntroFragment.newInstance(sliderPage));

//        SLIDER 3
        sliderPage.setTitle("");
        sliderPage.setDescription("");
        sliderPage.setImageDrawable(R.drawable.lazday);
        sliderPage.setBgColor(getResources().getColor(R.color.colorAccent));
        addSlide(AppIntroFragment.newInstance(sliderPage));


        // OPTIONAL METHODS
        // Override bar/separator color.
        setBarColor(getResources().getColor(R.color.colorPrimary));/*
        setSeparatorColor(getResources().getColor(R.color.colorAccent));*/

        // Hide Skip/Done button.
        showSkipButton(true);
        setProgressButtonEnabled(true);

        // Turn vibration on and set intensity.
        // NOTE: you will probably need to ask VIBRATE permission in Manifest.
        setVibrate(true);
        setVibrateIntensity(30);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // Do something when users tap on Done button.
        finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }
}
