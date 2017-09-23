package com.adurosmart.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.adurosmart.activitys.GroupDetailActivity;
import com.core.entity.AppGroup;
import com.core.gatewayinterface.SerialHandler;
import com.adurosmart.sdk.R;
import com.adurosmart.widget.CompatSeekBar;

import java.util.List;

/**
 * Created by best on 2016/8/8.
 */
public class GroupInfoAdapter extends BaseAdapter {
    private Context context;
    private List<AppGroup> datas;
    private boolean isSwitch = false;

    public GroupInfoAdapter(Context context, List<AppGroup> datas) {
        this.datas = datas;
        this.context = context;
    }

    class ViewHolder {
        private TextView group_id;
        private TextView group_name;
        private ImageView group_state;
        private CompatSeekBar seekBar;
        private RelativeLayout rl_layout;

        public void setGroup_id(TextView textView) {
            this.group_id = textView;
        }

        public void setGroup_name(TextView textView) {
            this.group_name = textView;
        }

        public TextView getGroup_id() {
            return group_id;
        }

        public TextView getGroup_name() {
            return group_name;
        }

        public ImageView getGroup_state() {
            return group_state;
        }

        public void setGroup_State(ImageView imageView) {
            this.group_state = imageView;
        }


        public void setSeekBar_brightness(CompatSeekBar seekBar) {
            this.seekBar = seekBar;
        }

        public CompatSeekBar getSeekBar() {
            return seekBar;
        }

        public void setRl_layout(RelativeLayout rl_layout) {
            this.rl_layout = rl_layout;
        }

        public RelativeLayout getRl_layout() {
            return rl_layout;
        }
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View view = convertView;
        final ViewHolder holder;
        if (null == view) {
            view = LayoutInflater.from(context).inflate(R.layout.list_groups_item, null);
            holder = new ViewHolder();

            //组ID
            TextView group_id_text = (TextView) view.findViewById(R.id.group_id);
            holder.setGroup_id(group_id_text);

            //组名称
            TextView group_name_text = (TextView) view.findViewById(R.id.group_name);
            holder.setGroup_name(group_name_text);

            ImageView group_state = (ImageView) view.findViewById(R.id.group_state);
            holder.setGroup_State(group_state);

            CompatSeekBar seekbar = (CompatSeekBar) view.findViewById(R.id.seekbar);
            holder.setSeekBar_brightness(seekbar);

            RelativeLayout rl_layout = (RelativeLayout) view.findViewById(R.id.rl_layout);
            holder.setRl_layout(rl_layout);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        final AppGroup groupInfo = datas.get(position);

        holder.getGroup_id().setText("" + groupInfo.getGroup_id());
        holder.getGroup_name().setText("" + groupInfo.getGroup_name());

        holder.getGroup_state().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSwitch) {
                    isSwitch = false;
                    SerialHandler.getInstance().setGroupState(groupInfo.getGroup_id(), 1);
                    holder.getGroup_state().setBackgroundResource(R.mipmap.btn_on);
                    holder.getRl_layout().setVisibility(View.VISIBLE);
                } else {
                    isSwitch = true;
                    SerialHandler.getInstance().setGroupState(groupInfo.getGroup_id(), 0);
                    holder.getGroup_state().setBackgroundResource(R.mipmap.btn_off);
                    holder.getRl_layout().setVisibility(View.VISIBLE);
                }
            }
        });

        holder.getSeekBar().setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                System.out.println("Level = " + 255 * progress / 100);
                SerialHandler.getInstance().setGroupLevel(groupInfo.getGroup_id(), 255 * progress / 100);
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GroupDetailActivity.class);
                intent.putExtra("GROUP_INFO", groupInfo);
                context.startActivity(intent);
            }
        });


        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.dialog_view, null);
                dialog.setView(layout);
                final EditText et_search = (EditText) layout.findViewById(R.id.searchC);
                dialog.setPositiveButton("update", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (et_search.getText().toString() == null) {
                            return;
                        }
                        SerialHandler.getInstance().ChangeGroupName(groupInfo.getGroup_id(), et_search.getText().toString());
                    }
                });

                dialog.setNegativeButton("delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        datas.remove(position);
                        notifyDataSetInvalidated();
                        SerialHandler.getInstance().DeleteGroup(groupInfo.getGroup_id());
                    }
                });
                dialog.show();

                return true;
            }
        });

        return view;
    }
}
