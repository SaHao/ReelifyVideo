//package com.reelify.kkkkwillo.adapter;
//
//import static com.reelify.kkkkwillo.ac.CustomA.CONFIG_INFO;
//
//import android.content.Context;
//import android.os.Build;
//import android.text.Html;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.reelify.kkkkwillo.R;
//import com.reelify.kkkkwillo.bean.ListInfo;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class AdapterB extends RecyclerView.Adapter<AdapterB.ViewHolder>{
//    private List<ListInfo.Descriptions> mDataList=new ArrayList<>();;
//    private Context mContext;
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_info_b, parent, false);
//        ViewHolder holder = new ViewHolder(view);
//        return holder;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        ListInfo.Descriptions data = mDataList.get(position);
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//        );
//        params.gravity= Gravity.RIGHT;
//        params.topMargin = 20;
//        params.leftMargin = 20;
//        params.rightMargin = 20;
//        if (CONFIG_INFO.data.rtl){
//            holder.txt_content.setLayoutParams(params);
//            holder.txt_tittle.setLayoutParams(params);
//            holder.txt_content.setGravity(Gravity.RIGHT);
//            holder.txt_tittle.setGravity(Gravity.RIGHT);
//        }
//        String content=data.getContents().toString().replaceAll("<br/>","\\\n").replaceAll("]","").replaceAll("\\[","");
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            holder.txt_content.setText(Html.fromHtml(content, Html.FROM_HTML_MODE_COMPACT));
//        }
//        holder.txt_tittle.setText(data.getTitle());
//    }
//
//    @Override
//    public int getItemCount() {
//        return mDataList.size();
//    }
//    public AdapterB(Context context) {
//        this.mContext=context;
//    }
//    public void upDateDatas(List<ListInfo.Descriptions>  datas) {
//        mDataList.clear();
//
//        if (mDataList != null) {
//            mDataList.addAll(datas);
//        }
//        notifyDataSetChanged();
//    }
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        TextView txt_tittle;
//        TextView txt_content;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            txt_tittle = itemView.findViewById(R.id.txt_tittle);
//            txt_content = itemView.findViewById(R.id.txt_content);
//        }
//    }
//}
