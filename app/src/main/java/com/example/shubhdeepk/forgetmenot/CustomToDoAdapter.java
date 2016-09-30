package com.example.shubhdeepk.forgetmenot;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by shkochh on 9/27/2016.
 */

public class CustomToDoAdapter extends BaseAdapter{

    ArrayList<String> toDoList;
    ToDoDbHandler db;
    Context context;

    private static LayoutInflater inflater=null;
    @Override
    public int getCount() {
        return toDoList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public CustomToDoAdapter(MainActivity mainActivity, ToDoDbHandler db){

        this.db = db;
        this.toDoList = db.getAll();
        this.context = mainActivity;
        this.inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        final View returnView;
        returnView = inflater.inflate(R.layout.list_item,null);
        LinearLayout layout = (LinearLayout) returnView.findViewById(R.id.itemLayout);
        TextView name = (TextView) returnView.findViewById(R.id.toDoName);
        name.setText(toDoList.get(position));
        TextView status = (TextView) returnView.findViewById(R.id.toDoStatus);
        status.setText(db.getStatus(toDoList.get(position)));
        TextView due = (TextView) returnView.findViewById(R.id.toDoDueDate);
        due.setText(db.getDueDate(toDoList.get(position)));
        TextView priority = (TextView) returnView.findViewById(R.id.toDoPriority);
        priority.setText(db.getPriority(toDoList.get(position)));
        if(priority.getText().toString().matches("Low"))
            layout.setBackgroundResource(R.drawable.bubble);

        if(priority.getText().toString().matches("Medium"))
            layout.setBackgroundResource(R.drawable.bubbleyellow);

        if(priority.getText().toString().matches("High"))
            layout.setBackgroundResource(R.drawable.bubblered);





        returnView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "view " + toDoList.get(position), Toast.LENGTH_LONG).show();
                Intent viewIntent = new Intent(context, ViewToDoActivity.class);
                viewIntent.putExtra("NAME", toDoList.get(position));
                viewIntent.putExtra("TYPE", "View");
                ((MainActivity) context).startActivityForResult(viewIntent, 1);
            }
        });

        returnView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(context, "delete " + toDoList.get(position), Toast.LENGTH_LONG).show();
                db.delete(toDoList.get(position));
                toDoList = db.getAll();
                notifyDataSetChanged();
                return false;
            }
        });
        return returnView;
    }
}
