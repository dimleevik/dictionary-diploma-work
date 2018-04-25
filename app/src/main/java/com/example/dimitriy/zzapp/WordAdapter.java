package com.example.dimitriy.zzapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Dimitriy on 04.04.2018.
 */

public class WordAdapter extends BaseAdapter{
    Context ctx;
    LayoutInflater lf;
    ArrayList<Words> array;
    public WordAdapter(MainActivity context, ArrayList<Words> words) {
        ctx=context;
        array=words;
        lf=(LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Object getItem(int i) {
        return array.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {

        // используем созданные, но не используемые view
        View view = convertView;
        if (view == null) {
            view = lf.inflate(R.layout.item, parent, false);
        }

        Words p = getProduct(i);

        // заполняем View в пункте списка данными из товаров: наименование, цена
        // и картинка
        ((TextView) view.findViewById(R.id.iWord)).setText(p.word+"");
        ((TextView) view.findViewById(R.id.iTranslation)).setText(p.translation + "");
        ((TextView) view.findViewById(R.id.iDesription)).setText(p.description + "");

        return view;
    }
    Words getProduct(int position) {

        return ((Words) getItem(position));
    }
}
