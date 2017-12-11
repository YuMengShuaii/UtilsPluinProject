package com.enation.javashop.utils.base.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.enation.javashop.utils.base.R;

/**
 * 加载时显示的Dialog
 */

public class LoadingDialog extends Dialog {
    private Animation hyperspaceJumpAnimation;
    private ImageView view;
    public static LoadingDialog getInstance(Context paramContext, int paramInt, ImageView view){
        return new LoadingDialog(paramContext,paramInt,view);
    }
    private LoadingDialog(Context paramContext, int paramInt, ImageView view)
    {
        super(paramContext, paramInt);
        this.hyperspaceJumpAnimation = AnimationUtils.loadAnimation(paramContext, R.anim.loading_animation);
        this.view = view;
    }

    protected void onCreate(Bundle paramBundle)
    {
        super.onCreate(paramBundle);
    }

    public void show()
    {
        dismiss();
        super.show();
        if (this.view != null||!isShowing())
            this.view.startAnimation(this.hyperspaceJumpAnimation);
    }

    public void dismiss(){
        if (this.isShowing()){
            super.dismiss();
        }
    }

}
