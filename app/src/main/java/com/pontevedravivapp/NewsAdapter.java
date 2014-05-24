package com.pontevedravivapp;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pontevedravivapp.xml.Entry;

public class NewsAdapter extends BaseAdapter {
    private List<Entry> items;
    private Context context;
    private LayoutInflater layoutInflater;

    public NewsAdapter(Context context, List<Entry> items) {
        this.context = context;
        this.items = items;
        this.layoutInflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int arg0) {
        return items.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    private class ViewHolder {
        public TextView title;
        public TextView author;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final ViewHolder holder;
        if (convertView == null) {
            view = layoutInflater.inflate(R.layout.item_view, parent, false);
            holder = new ViewHolder();
            holder.title = (TextView) view.findViewById(R.id.Title);
            holder.author = (TextView) view.findViewById(R.id.author);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.title.setText(items.get(position).getTitle());
        holder.author.setText(items.get(position).getAuthor());
        return view;
    }

}
