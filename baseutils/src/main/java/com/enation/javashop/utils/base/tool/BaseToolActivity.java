package com.enation.javashop.utils.base.tool;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import com.enation.javashop.utils.base.R;
import com.enation.javashop.utils.base.config.BaseConfig;
import com.enation.javashop.utils.base.widget.LoadingDialog;
import com.enation.javashop.utils.base.widget.ScrollBackView;

/**
 * Created by LDD on 17/4/13.
 */

public abstract class BaseToolActivity extends AppCompatActivity implements BaseInterface {

    /**
     * acivity
     */
    protected BaseToolActivity activity = null;
    /**
     * 滑动退出View
     */
    private ScrollBackView scrollBackView;
    /**
     * 加载Dilaog
     */
    private LoadingDialog dialog = null;
    /**
     * 是否使用工具类自带的退出功能
     */
    protected boolean useToolBack = true;
    /**
     * 当前页面是否使用滑动退出
     */
    protected boolean thisScrollBack = true;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (BaseConfig.isOpenScrollBack()){
            if (!getRunningActivityName().equals(BaseConfig.getHomeName())&&thisScrollBack){
                scrollBackView.attachToActivity(this);
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        if (BaseConfig.isOpenScrollBack()){
            if (!getRunningActivityName().equals(BaseConfig.getHomeName())&&thisScrollBack){
                ScrollBackInit();
            }
        }
        initTool();
    }

    private void initTool(){
        dialog = CommonTool.createLoadingDialog(activity);
    }

    @Override
    public void showDialog(){
        dialog.show();
    }

    @Override
    public void dismissDialog(){
        if (dialog!=null){
            dialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        if (useToolBack){
            toolBack();
        }
    }

    /**
     * 工具Activity自带退出
     */
    protected void toolBack(){
        if (getRunningActivityName().equals(BaseConfig.getHomeName())){
            Intent localIntent = new Intent("android.intent.action.MAIN");
            localIntent.addCategory("android.intent.category.HOME");
            startActivity(localIntent);
            return;
        }
        finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    private String getRunningActivityName() {
        String contextString = activity.toString();
        return contextString.substring(contextString.lastIndexOf(".") + 1, contextString.indexOf("@"));
    }

    /**
     * 滑动退出配置
     */
    private void ScrollBackInit() {
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().getDecorView().setBackgroundDrawable(null);
        scrollBackView = new ScrollBackView(this);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        scrollBackView.setLayoutParams(params);
        scrollBackView.setEdgeOrientation(ScrollBackView.EDGE_LEFT); // EDGE_LEFT(默认),EDGE_ALL
        scrollBackView.setParallaxOffset(0.5f); // （类iOS）滑动退出视觉差，默认0.3
        scrollBackView.addSwipeListener(new ScrollBackView.OnSwipeListener() {
            @Override
            public void onDragStateChange(int state) {
                // Drag state
            }

            @Override
            public void onEdgeTouch(int edgeFlag) {
                // 触摸的边缘flag
            }

            @Override
            public void onDragScrolled(float scrollPercent) {
                // 滑动百分比
                if (scrollPercent>50){
                    onBackPressed();
                }
            }
        });
        /**禁止滑动退出*/
        //scrollBackView.setEnableGesture(false);
    }

    public boolean swipeBackPriority() {
        return getSupportFragmentManager().getBackStackEntryCount() <= 1;
    }
}
