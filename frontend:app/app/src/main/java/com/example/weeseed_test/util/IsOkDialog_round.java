package com.example.weeseed_test.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.weeseed_test.R;
import com.example.weeseed_test.databinding.DialogIsOkRoundBinding;

/**
여기저기서 사용되는 커스텀 dialog
 */
public class IsOkDialog_round extends Dialog {
    DialogIsOkRoundBinding bi;
    int digMode;
    //종류
    //0: default 취소(dismiss)/확인(RightListener)
    //1: both listen 좌/우 둘다 listener
    //2: only right
    //3: recording mode

    public enum COLOR {DEF, RED,YEL
    }

    public interface OnDialogButtonClickListener {
        void onLeftButtonClick();
        void onRightButtonClick();
    }

    OnDialogButtonClickListener listener;

    public IsOkDialog_round(@NonNull Context context) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        bi = DialogIsOkRoundBinding.inflate(getLayoutInflater());
        setContentView(bi.getRoot());

        setDialogSize();
        setButtons();
    }


    ////ui setting
    private void setDialogSize(){
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();   //set dig size
        Window window = getWindow();
        if (window != null) {
            params.copyFrom(window.getAttributes());
            params.width = WindowManager.LayoutParams.WRAP_CONTENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(params);
        }
    }

    private void setButtons(){
        bi.btnRndLeft.setOnClickListener(v -> clickLeft());
        bi.btnRndRight.setOnClickListener(v -> clickRight());
        bi.getRoot().setOnClickListener(v -> dismiss());
    }

    private void clickLeft() {
        if(listener!=null)      listener.onLeftButtonClick();
        dismiss();
    }
    private void clickRight(){
        if(listener!=null)      listener.onRightButtonClick();
        dismiss();
    }

    //사용법
//    IsOkDialog_round dialog = new IsOkDialog_round(getActivity());
//        dialog.setDialogContent("제목", "설명", "취소", "확인", 2);
//        dialog.setOnDialogButtonClickListener(new IsOkDialog_round.OnDialogButtonClickListener(){...}
//    dialog.show();
    public void setDialogContent(String title, String description, String btnLeft, String btnRight, int mode, int titleColor) {
        //내용 설정. 외부에서 사용
        bi.titleRndIsOK.setText(title);
        bi.descRndIsOK.setText(description);
        bi.btnRndLeft.setText(btnLeft);
        bi.btnRndRight.setText(btnRight);
        this.digMode=mode;
        if(COLOR.DEF.equals(titleColor))
            bi.titleRndIsOK.setTextColor(Color.parseColor("#FF111111"));
        else if (COLOR.RED.equals(titleColor))
            bi.titleRndIsOK.setTextColor(Color.parseColor("#FFDC0000"));
        else if (COLOR.YEL.equals(titleColor))
            bi.titleRndIsOK.setTextColor(Color.parseColor("#FFFFCE00"));

    }
    public void setOnDialogButtonClickListener(OnDialogButtonClickListener listener){
        //리스너 설정. 외부에서 사용
        this.listener=listener;
    }
}