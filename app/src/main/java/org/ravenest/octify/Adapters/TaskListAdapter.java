package org.ravenest.octify.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import org.ravenest.octify.OctifyDataSet;
import org.ravenest.octify.R;
import org.ravenest.octify.TaskObject;
import org.ravenest.octify.Utility;

import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ViewHolderDefault>{

    private List<TaskObject.Model> list;
    private int layout;
    Utility util;

    public TaskListAdapter(List<TaskObject.Model> dataSet, int layout) {
        this.list   = dataSet;
        this.layout = layout;
        util = new Utility();
    }
    @Override
    public TaskListAdapter.ViewHolderDefault onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(layout,viewGroup,false);
        TaskListAdapter.ViewHolderDefault viewHolder = new ViewHolderDefault(inflate);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TaskListAdapter.ViewHolderDefault viewHolder, int pos) {
        viewHolder.task_name.setText(list.get(pos).task.name);
        viewHolder.task_time.setText(util.dateToString(new Date(list.get(pos).task.time), "HH:mm", Locale.ROOT));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    //　ViewHolder(デフォルト)
    class ViewHolderDefault extends RecyclerView.ViewHolder{
        public TextView task_name;
        public TextView task_time;
        public TextView task_desc;
        public ViewHolderDefault(View itemView) {
            super(itemView);
            task_time = itemView.findViewById(R.id.task_time_text);
            task_name = itemView.findViewById(R.id.task_content_name);
            //task_desc = itemView.findViewById(R.id.item_type);
        }
    }
}
