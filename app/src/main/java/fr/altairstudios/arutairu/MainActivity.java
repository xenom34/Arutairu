package fr.altairstudios.arutairu;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class MainActivity extends AppCompatActivity {
    private ImageView mAltair, mBg;
    private ImageView mCurrentLanguage;
    private com.google.android.material.textview.MaterialTextView mWelcome;
    private TextView mArutairu;
    private com.google.android.material.button.MaterialButton mStart;
    private Animation fadeAltair2, fadeAltair3;
    private ShuffleBg shuffleBg = new ShuffleBg();

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fadeAltair2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade2);
        fadeAltair3 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade3);

        mAltair = findViewById(R.id.altair);
        mBg = findViewById(R.id.bg);
        mCurrentLanguage = findViewById(R.id.logo);
        mStart = findViewById(R.id.startBtn);
        mArutairu = findViewById(R.id.txtArutairu);
        mWelcome = findViewById(R.id.welcomeTxtLogo);
        mAltair.setVisibility(View.INVISIBLE);
        mArutairu.setAlpha(0f);
        mWelcome.setAlpha(0f);
        mStart.setAlpha(0f);
        mCurrentLanguage.setAlpha(0f);
        mBg.setImageResource(shuffleBg.choose());
        mBg.setAlpha(0f);

        mStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ListActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });


        Animation fadeAltair =
                AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.fade);
        mAltair.startAnimation(fadeAltair);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mArutairu.setAlpha(1f);
                mStart.setAlpha(1f);
                mWelcome.setAlpha(1f);
                mCurrentLanguage.setAlpha(1f);
                mBg.setAlpha(0.5f);
                mArutairu.startAnimation(fadeAltair2);
                mStart.startAnimation(fadeAltair2);
                mCurrentLanguage.startAnimation(fadeAltair2);
                mWelcome.startAnimation(fadeAltair2);
                mBg.startAnimation(fadeAltair3);
            }
        },3000);

    }
}
