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

public class DialogB extends Dialog {
    private TextView txt1,txt2,txt3,buttonTxt;
    private ImageView closeImg;
    private String wen1,wen2,wen3,wen4;
    private BnOnclickListener OnclickListener;
    public interface BnOnclickListener {
        public void onClick();
    }
    public DialogB(@NonNull Context context) {
        super(context, R.style.MyDialog);
    }
    public void setOnclickListener(BnOnclickListener OnclickListener) {
        this.OnclickListener = OnclickListener;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_b);
        setCanceledOnTouchOutside(false);
        initView();
        initEvent();
        initData();

    }
    private void initView() {
        closeImg = (ImageView) findViewById(R.id.close);
        buttonTxt = (TextView) findViewById(R.id.txt_button);
        txt1 = (TextView) findViewById(R.id.txt_wen1);
        txt2 = (TextView) findViewById(R.id.txt_wen2);
        txt3 = (TextView) findViewById(R.id.txt_wen3);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.gravity = Gravity.RIGHT;
        params.topMargin = 50;
        params.leftMargin = 40;
        params.rightMargin = 40;
        if (CONFIG_INFO.data.rtl){
            txt1.setLayoutParams(params);
            txt2.setLayoutParams(params);
            txt3.setLayoutParams(params);

            txt1.setGravity(Gravity.RIGHT);
            txt2.setGravity(Gravity.RIGHT);
            txt3.setGravity(Gravity.RIGHT);
        }
    }

    private void initEvent() {
        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        buttonTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (OnclickListener != null) {
                    OnclickListener.onClick();
                }
            }
        });
    }

    private void initData() {
        if (wen1 != null) {
            buttonTxt.setText(wen1);

        }
        if (wen2 != null) {
            txt1.setText(wen2);

        }
        if (wen3 != null) {
            txt2.setText(wen3);

        }
        if (wen4 != null) {
            txt3.setText(wen4);
        }
    }
    public void setWen1(String wen1) {
        this.wen1 = wen1;
    }
    public void setWen2(String wen2) {
        this.wen2 = wen2;
    }
    public void setWen3(String wen3) {
        this.wen3 = wen3;
    }
    public void setWen4(String wen4) {
        this.wen4 = wen4;
    }

}
