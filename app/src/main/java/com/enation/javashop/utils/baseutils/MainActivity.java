package com.enation.javashop.utils.baseutils;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.enation.javashop.utils.base.tool.BaseToolActivity;
import com.enation.javashop.utils.base.widget.LoadingDialog;
import com.enation.javashop.utils.base.tool.CommonTool;

public class MainActivity extends BaseToolActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final LoadingDialog dialog = CommonTool.createLoadingPage(this);
        dialog.show();
        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        },5000);
        findViewById(R.id.hello).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dialog.show();
//                new Handler(getMainLooper()).postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        dialog.dismiss();
//                    }
//                },5000);
                CommonTool.createVerifyDialog("1111", "1111", "1111", activity, new CommonTool.DialogInterface() {
                    @Override
                    public void no() {

                    }

                    @Override
                    public void yes() {

                    }
                });
            }
        });
    }
}
