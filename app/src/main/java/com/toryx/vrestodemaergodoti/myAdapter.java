package com.toryx.vrestodemaergodoti;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class myAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String>listid;
    private ArrayList<String> nameid;
    private ArrayList<String> ggid;




    public myAdapter(Context context, ArrayList<String> listid, ArrayList<String> nameid, ArrayList<String> ggid) {
        this.context = context;
        this.listid = listid;
        this.nameid = nameid;
        this.ggid = ggid;



    }

    @Override
    public int getCount() {
        return nameid.size();
    }

    @Override
    public Object getItem(int position) {
        return nameid.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if(convertView == null){convertView=View.inflate(context,R.layout.item_la,null);
        }

        final TextView txt = (TextView)  convertView.findViewById(R.id.textView3);
        final TextView txt2 = (TextView)  convertView.findViewById(R.id.textView2);
        final TextView txt3 = (TextView)  convertView.findViewById(R.id.textView4);

        txt.setText(listid.get(position));
        txt2.setText(nameid.get(position));
        txt3.setText(ggid.get(position));

        return convertView;

    }
}
