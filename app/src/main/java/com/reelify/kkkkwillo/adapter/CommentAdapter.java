package com.reelify.kkkkwillo.adapter;

import static com.reelify.kkkkwillo.media.VideoFragment.CONFIG_INFO;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.reelify.kkkkwillo.R;
import com.reelify.kkkkwillo.bean.ListInfo;
import com.reelify.kkkkwillo.utils.MySettings;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private List<ListInfo.comments> comments = new ArrayList<>();
    private Context context;

    public CommentAdapter(List<ListInfo.comments> comments, Context context) {
        this.comments = comments;
        this.context = context;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Glide.with(context)
                .load(comments.get(position).getSrc())
                .placeholder(R.drawable.btn_user_off)
                .error(R.drawable.icon_avatar)
                .into(holder.imgAvatar);
        holder.tvComment.setText(comments.get(position).getDesc());
        holder.tvName.setText(comments.get(position).getName());
        holder.tvTime.setText(comments.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(int pos) {
        comments.clear();
        Gson gson = new Gson();
        String info = MySettings.getInstance().getStringSetting("listInfo");
        ListInfo data = gson.fromJson(info, ListInfo.class);
        comments.addAll(data.getData().getVideos().get(pos).getComment().getComments());
        notifyDataSetChanged();
    }


    static class CommentViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvComment;
        private final TextView tvName;
        private final TextView tvTime;
        private final ImageView imgAvatar;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvComment = itemView.findViewById(R.id.Desc);
            tvName = itemView.findViewById(R.id.Nickname);
            tvTime = itemView.findViewById(R.id.Time);
            imgAvatar = itemView.findViewById(R.id.avatar);

            if (CONFIG_INFO.data.rtl){
                tvComment.setGravity(Gravity.RIGHT);
                tvComment.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
        }

    }
}
