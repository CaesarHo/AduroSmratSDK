package com.adurosmart.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

//import com.core.entity.AppTask;
import com.adurosmart.sdk.R;
import com.core.entity.AppTask2;

import java.util.List;

/**
 * Created by best on 2016/11/17.
 */

public class vRecyclerAdapter extends RecyclerView.Adapter<vRecyclerAdapter.MyViewHolder> {
    private List<AppTask2> mDatas;
    private Context mContext;
    private LayoutInflater inflater;
    private OnItemClickListener mOnItemClickListener;

    public vRecyclerAdapter(Context context, List<AppTask2> datas) {
        this.mContext = context;
        this.mDatas = datas;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    //填充onCreateViewHolder方法返回的holder中的控件
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final AppTask2 taskInfo = mDatas.get(position);
        holder.task_no.setText("编号:" + taskInfo.getTask_no() + "   ,  " + "是否启用:" + taskInfo.getIsEnabled());
        holder.task_name.setText("任务名称:"+taskInfo.getTask_name()+","+"时间:"+taskInfo.getTask_hour()+":"+taskInfo.getTask_minute());
        if (taskInfo.getTask_type() == 0) {
            holder.task_isenabled.setText("类型:" + "触发任务" + "," + taskInfo.getSensor_mac()
                    + "," + taskInfo.getSensor_short());
        } else if (taskInfo.getTask_type() == 1) {
            holder.task_isenabled.setText("类型:" + "定时任务" + "," + "周期:" + taskInfo.getTask_cycle());
        }

        if( mOnItemClickListener!= null){
            holder. itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(position);
                }
            });

            holder. itemView.setOnLongClickListener( new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onLongClick(position);
                    return false;
                }
            });
        }
    }

    //重写onCreateViewHolder方法，返回一个自定义的ViewHolder
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_task_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView task_no;
        private TextView task_name;
        private TextView task_isenabled;
        private ImageView control_btn;

        public MyViewHolder(View view) {
            super(view);
            task_no = (TextView) view.findViewById(R.id.task_no);
            task_name = (TextView) view.findViewById(R.id.task_name);
            task_isenabled = (TextView) view.findViewById(R.id.task_is_text);
            control_btn = (ImageView) view.findViewById(R.id.control_btn);
        }
    }

    public interface OnItemClickListener{
        void onClick(int position);
        void onLongClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener ){
        this. mOnItemClickListener=onItemClickListener;
    }
}
