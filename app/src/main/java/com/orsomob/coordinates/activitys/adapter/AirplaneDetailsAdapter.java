package com.orsomob.coordinates.activitys.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.orsomob.coordinates.R;
import com.orsomob.coordinates.data.module.AirplaneData;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

/**
 * Created by LucasOrso on 6/8/17.
 */

public class AirplaneDetailsAdapter extends RecyclerView.Adapter<AirplaneDetailsAdapter.ViewHolder> implements AdapterView.OnItemSelectedListener, View.OnCreateContextMenuListener {

    List<AirplaneData> mAirplaneDataList;

    public AirplaneDetailsAdapter() {
        mAirplaneDataList = SQLite.select().from(AirplaneData.class).queryList();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View lView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_airplane_detail, parent, false);
        return new ViewHolder(lView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AirplaneData lAirplane = mAirplaneDataList.get(position);

        holder.mTextId.setText(lAirplane.getId() != null ? lAirplane.getId().toString() : "");
        holder.mTextViewName.setText(lAirplane.getName() != null ? lAirplane.getName() : "");
        holder.mImageViewAirplane.setRotation(lAirplane.getDirection() != null ? lAirplane.getDirection().floatValue() : 0.0f);
        holder.mTextViewCartesianX.setText(lAirplane.getCoordinateX() != null ? String.valueOf(lAirplane.getCoordinateX()) : "");
        holder.mTextViewCartesianY.setText(lAirplane.getCoordinateY() != null ? String.valueOf(lAirplane.getCoordinateY()) : "");
        holder.mTextViewPolarRadius.setText(lAirplane.getRadius() != null ? String.valueOf(lAirplane.getRadius()) : "");
        holder.mTextViewPolarDegrees.setText(lAirplane.getDegree() != null ? String.valueOf(lAirplane.getDegree()) : "");
        holder.mTextViewSpeed.setText(lAirplane.getSpeed() != null ? String.valueOf(lAirplane.getSpeed()) : "");
        holder.mTextViewDirection.setText(lAirplane.getDirection() != null ? String.valueOf(lAirplane.getDirection()) : "");
    }

    @Override
    public int getItemCount() {
        return mAirplaneDataList.size();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle(R.string.selected);
        v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTextViewName;
        private TextView mTextId;
        private TextView mTextViewCartesianX;
        private TextView mTextViewCartesianY;
        private TextView mTextViewPolarDegrees;
        private TextView mTextViewPolarRadius;
        private TextView mTextViewSpeed;
        private TextView mTextViewDirection;
        private ImageView mImageViewAirplane;

        ViewHolder(View itemView) {
            super(itemView);
            mTextId = (TextView) itemView.findViewById(R.id.tx_id);
            mTextViewName = (TextView) itemView.findViewById(R.id.tx_name);
            mTextViewCartesianX = (TextView) itemView.findViewById(R.id.tx_x);
            mTextViewCartesianY = (TextView) itemView.findViewById(R.id.tx_y);
            mTextViewPolarRadius = (TextView) itemView.findViewById(R.id.tx_radius);
            mTextViewPolarDegrees = (TextView) itemView.findViewById(R.id.tx_degree);
            mTextViewSpeed = (TextView) itemView.findViewById(R.id.tx_speed);
            mTextViewDirection = (TextView) itemView.findViewById(R.id.tx_direction);
            mImageViewAirplane = (ImageView) itemView.findViewById(R.id.iv_airplane);
        }
    }
}
