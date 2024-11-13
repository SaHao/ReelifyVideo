package com.reelify.kkkkwillo.Dialog;

import static com.reelify.kkkkwillo.media.VideoFragment.CONFIG_INFO;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.reelify.kkkkwillo.R;
import com.reelify.kkkkwillo.ac.DialogB;
import com.reelify.kkkkwillo.adapter.CommentAdapter;
import com.reelify.kkkkwillo.bean.ConfigInfo;
import com.reelify.kkkkwillo.bean.ListInfo;
import com.reelify.kkkkwillo.utils.AdIdManager;
import com.reelify.kkkkwillo.utils.MySettings;

import java.util.ArrayList;

public class CommentDialog extends Dialog {
    private ArrayList<ListInfo.comments> comments = new ArrayList<>();
    private CommentAdapter commentAdapter;
    private EditText etComment;
    RecyclerView recyclerView;
    LinearLayout rootView;
    ListInfo.Comment data;
    Context context;
    int position;
    private clickListener OnclickListener;
    public interface clickListener {
        public void onClick();
    }
    public void setOnclickListener(clickListener OnclickListener) {
        this.OnclickListener = OnclickListener;
    }
    public CommentDialog(@NonNull Context context, ListInfo.Comment data,int position) {
        super(context, R.style.CustomDialog);
        this.data = data;
        this.context = context;
        this.position = position;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_comment);
        etComment = findViewById(R.id.et_comment);
        rootView = findViewById(R.id.mainView);
        Button btnSend = findViewById(R.id.btn_send);
        recyclerView = findViewById(R.id.recycler_view_comments);
        ImageView close = findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        Gson gson = new Gson();
        String info = MySettings.getInstance().getStringSetting("comments");
        if (info != null && info.length() > 0) {
            ListInfo.Comment data = gson.fromJson(info, ListInfo.Comment.class);
            comments.addAll(data.getComments());
        } else {
            comments.addAll(data.getComments());
        }
        commentAdapter = new CommentAdapter(comments, context);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(commentAdapter);

        btnSend.setOnClickListener(view -> {
            sendComment();
        });
    }

    private void sendComment() {
        String comment = etComment.getText().toString().trim();
        if (!comment.isEmpty()) {
            Gson gson = new Gson();
            String info = MySettings.getInstance().getStringSetting("listInfo");
            ListInfo listInfo = gson.fromJson(info, ListInfo.class);
            ListInfo.comments comm = new ListInfo.comments("", "pic", MySettings.getInstance().getStringSetting("name"), comment, "recent");
            comments.add(0, comm);
            data.setComments(comments);
            data.setAmount(comments.size() + "");
            listInfo.getData().getVideos().get(position).setComment(data);
            MySettings.getInstance().saveSetting("listInfo", listInfo);
            etComment.setText("");
            commentAdapter.updateData(position);
            OnclickListener.onClick();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (getWindow() != null) {
            Window window = getWindow();
            window.setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    (int) (getContext().getResources().getDisplayMetrics().heightPixels * 0.66)
            );
            window.setGravity(Gravity.BOTTOM);
        }
    }
}
