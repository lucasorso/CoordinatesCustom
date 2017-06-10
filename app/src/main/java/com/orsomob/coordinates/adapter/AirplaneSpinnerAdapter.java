package com.orsomob.coordinates.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.orsomob.coordinates.R;
import com.orsomob.coordinates.module.Airplane;

import java.util.List;

/**
 * Created by LucasOrso on 6/9/17.
 */

public class AirplaneSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

    private Context mContext;
    private List<Airplane> mAirplaneList;

    public AirplaneSpinnerAdapter(Context aContext, List<Airplane> aAirplaneList){
        this.mContext = aContext;
        this.mAirplaneList = aAirplaneList;
    }

    public int getCount(){
        return mAirplaneList.size();
    }

    public Airplane getItem(int position){
        return mAirplaneList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return (long) position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.row_airplane_select, parent, false);

        ImageView lImageView = (ImageView) convertView.findViewById(R.id.iv_airplane);
        TextView lId = (TextView) convertView.findViewById(R.id.tx_id_sp);
        TextView lName = (TextView) convertView.findViewById(R.id.tx_name);
        Airplane lAirplane = getItem(position);
        if (lAirplane != null) {
            lImageView.setRotation(lAirplane.getDirection());
            lId.setText("ID: " + lAirplane.getId());
            lName.setText(lAirplane.getName());
        }
        return convertView;
    }

}
