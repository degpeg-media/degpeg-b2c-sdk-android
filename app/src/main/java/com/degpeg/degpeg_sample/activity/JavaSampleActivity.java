package com.degpeg.degpeg_sample.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.degpeg.b2csdk.DegpegSDKProvider;
import com.degpeg.b2csdk.UserRole;
import com.degpeg.degpeg_sample.LocalDataHelper;
import com.degpeg.degpeg_sample.databinding.ActivityMainBinding;
import com.degpeg.model.AppUiConfig;
import com.degpeg.model.User;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;

public class JavaSampleActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initUi();
    }

    private void initUi() {
        DegpegSDKProvider.INSTANCE.init(
                "degpegarjun_CmGG9Xmc",
                "mtduV6B6WmnnkefI",
                "",
                "632a9b98cc4f9f244ff8e24c",
                UserRole.PROVIDER,
                true,
                () -> {
                    runOnUiThread(() -> {
                        Toast.makeText(JavaSampleActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    });
                    DegpegSDKProvider.INSTANCE.startPlayer(this, "6203642ba958e382ff246d66");
                    return null;
                },
                s -> {
                    runOnUiThread(() -> {
                        Toast.makeText(JavaSampleActivity.this, "Error : " + s, Toast.LENGTH_SHORT).show();
                    });
                    return null;
                });

        AppUiConfig appUiConfig = new AppUiConfig();
        appUiConfig.setChatEnable(true);
        appUiConfig.setMuteEnable(true);
        appUiConfig.setLikeEnable(true);
        appUiConfig.setProductEnable(false);
        appUiConfig.setShareEnable(false);

        DegpegSDKProvider.INSTANCE.updateAppUiConfig(appUiConfig);

        DegpegSDKProvider.INSTANCE.updateUser(
                new User("Dhaval Patel", "6278c4556cb38a7a9c10df6e")
        );
    }

    private void startAsActivity() {
        DegpegSDKProvider.INSTANCE.startAsActivity(
                this,
                s -> {
                    runOnUiThread(() -> {
                        Toast.makeText(JavaSampleActivity.this, "Error : " + s, Toast.LENGTH_SHORT).show();
                    });
                    return null;
                });
    }

    private void useAsFragment() {
        DegpegSDKProvider.INSTANCE.useAsFragment(
                getSupportFragmentManager(),
                0, s -> {
                    runOnUiThread(() -> {
                        Toast.makeText(JavaSampleActivity.this, "Error : " + s, Toast.LENGTH_SHORT).show();
                    });
                    return null;
                });
    }
}