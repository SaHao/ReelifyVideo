package com.reelify.kkkkwillo.adapter;



import static com.reelify.kkkkwillo.media.VideoFragment.CONFIG_INFO;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.reelify.kkkkwillo.R;
import com.reelify.kkkkwillo.bean.ListInfo;

import java.util.ArrayList;
import java.util.List;

public class AdapterA extends RecyclerView.Adapter<AdapterA.ViewHolder> {
    private List<ListInfo.Data> mDataList = new ArrayList<>();
    private Context mContext;

    public interface OnItemClickListener {
        void onClick(ListInfo.Data data);

        void onClickB(ListInfo.Data data);
    }

    private OnItemClickListener mOnItemClickListener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_info_a, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListInfo.Data data = mDataList.get(position);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.gravity = Gravity.RIGHT;
        params.topMargin = 20;
        params.leftMargin = 20;
        params.rightMargin = 20;
        if (CONFIG_INFO.data.rtl) {
            holder.txt_tag.setLayoutParams(params);
            holder.txt_tittle.setLayoutParams(params);
            holder.txt_company.setLayoutParams(params);
            holder.txt_address.setLayoutParams(params);
            holder.txt_salary.setLayoutParams(params);

            holder.txt_tag.setGravity(Gravity.RIGHT);
            holder.txt_tittle.setGravity(Gravity.RIGHT);
            holder.txt_company.setGravity(Gravity.RIGHT);
            holder.txt_address.setGravity(Gravity.RIGHT);
            holder.txt_salary.setGravity(Gravity.RIGHT);
        }
//        holder.txt_tag.setText(data.getTag());
//        holder.txt_tittle.setText(data.getTitle());
//        holder.txt_company.setText(data.getCompany());
//        holder.txt_address.setText(data.getAddress());
//        holder.txt_salary.setText(data.getSalary());
//        holder.txt_button.setText(CONFIG_INFO.data.actions.apply);
        holder.txt_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    if (data.getType().equalsIgnoreCase("c")) {
                        mOnItemClickListener.onClick(data);
                    } else {
                        mOnItemClickListener.onClickB(data);
                    }

                }
            }
        });
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onClickB(data);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public AdapterA(Context context) {
        this.mContext = context;
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickLitener) {
        this.mOnItemClickListener = mOnItemClickLitener;
    }

    public void upDateDatas(List<ListInfo.Data> datas) {
        mDataList.clear();

        if (mDataList != null) {
            mDataList.addAll(datas);
        }
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        View rootView;
        TextView txt_tag;
        TextView txt_tittle;
        TextView txt_company;
        TextView txt_address;
        TextView txt_salary;
        TextView txt_button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView;
            txt_tag = itemView.findViewById(R.id.txt_tag);
            txt_tittle = itemView.findViewById(R.id.txt_tittle);
            txt_company = itemView.findViewById(R.id.txt_company);
            txt_address = itemView.findViewById(R.id.txt_address);
            txt_salary = itemView.findViewById(R.id.txt_salary);
            txt_button = itemView.findViewById(R.id.txt_button);
        }
    }
}