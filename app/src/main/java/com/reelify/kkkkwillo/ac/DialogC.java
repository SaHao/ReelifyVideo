package com.reelify.kkkkwillo.ac;

import static com.reelify.kkkkwillo.media.VideoFragment.CONFIG_INFO;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.reelify.kkkkwillo.R;

public class DialogC extends Dialog {
    private TextView txt3,buttonTxt;
    private LinearLayout replay;
    private String wen1,wen4;
    private CnOnclickListener OnclickListener;
    View rootView;
    public interface CnOnclickListener {
        public void onClick();
        public void replay();
    }
    public void setOnclickListener(CnOnclickListener OnclickListener) {
        this.OnclickListener = OnclickListener;
    }
    public DialogC(@NonNull Context context) {
        super(context,R.style.CustomDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_c);
        initView();
        initData();

    }
    private void initView() {
        replay=findViewById(R.id.replay_layout);
        buttonTxt = (TextView) findViewById(R.id.txt_button);
        txt3 = (TextView) findViewById(R.id.txt_wen3);
        rootView = findViewById(R.id.mainView);
        if (CONFIG_INFO.data.rtl){
            rootView.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
//        if (CONFIG_INFO.data.rtl){
//            txt3.setGravity(Gravity.RIGHT);
//            txt3.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
//        }
        buttonTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (OnclickListener != null) {
                    OnclickListener.onClick();
                }
            }
        });
        replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (OnclickListener != null) {
                    OnclickListener.replay();
                }
            }
        });
    }

    private void initData() {
        if (wen1 != null) {
            buttonTxt.setText(wen1);

        }
        if (wen4 != null) {
            txt3.setText(wen4);
        }
    }
    public void setWen1(String wen1) {
        this.wen1 = wen1;
    }
    public void setWen4(String wen4) {
        this.wen4 = wen4;
    }


}
