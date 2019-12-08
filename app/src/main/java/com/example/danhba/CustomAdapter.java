package com.example.danhba;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<Contact> {
    private Context context;
    private int resourse;
    private ArrayList<Contact> arrayContact;

    private ArrayList<Contact> orig = null;
    private final String TAG = getClass().getSimpleName();

    public CustomAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Contact> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resourse = resource;
        this.arrayContact = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_row_listview, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.imgAvt = convertView.findViewById(R.id.imgAvt);
            viewHolder.imgSend = convertView.findViewById(R.id.imgBtnSend);
            viewHolder.imgCall = convertView.findViewById(R.id.imgBtnCall);
            viewHolder.tv_Name = convertView.findViewById(R.id.tvName);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Contact contact = arrayContact.get(position);
        viewHolder.tv_Name.setText(contact.getName());

        viewHolder.imgCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNo = arrayContact.get(position).getPhone().toString();
                Log.d("debug1", phoneNo);
                if(!TextUtils.isEmpty(phoneNo)) {
                    String dial = "tel:" + phoneNo;
                    context.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(dial)));
                }else {
                    Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewHolder.imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNo = arrayContact.get(position).getPhone().toString();
                Log.d("debug2", phoneNo);
                if(!TextUtils.isEmpty(phoneNo)) {
                    String dial = "sms:" + phoneNo;
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(dial)));
                }else {
                    Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return convertView;
    }

    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<Contact> results = new ArrayList<Contact>();
                if (orig == null)
                    orig = arrayContact;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final Contact g : orig) {
                            if (g.getName().toLowerCase().contains(constraint.toString()))
                                results.add(g);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                arrayContact = (ArrayList<Contact>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void notifyDataSetChanged() {

        super.notifyDataSetChanged();
    }

    public class ViewHolder {
        TextView tv_Name;
        ImageView imgAvt, imgCall,imgSend;
    }
}
