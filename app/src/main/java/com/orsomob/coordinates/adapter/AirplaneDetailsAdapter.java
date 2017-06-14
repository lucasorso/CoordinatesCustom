package com.orsomob.coordinates.adapter;

import android.content.Context;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.orsomob.coordinates.R;
import com.orsomob.coordinates.data.module.AirplaneData;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LucasOrso on 6/8/17.
 */

public class AirplaneDetailsAdapter extends RecyclerView.Adapter<AirplaneDetailsAdapter.ViewHolder> {

    private static String TAG = "AIRPLANE_DETAIL_ADAPTER";
    private List<AirplaneData> mAirplaneDataList;
    private ActionBar mActionBar;
    private static RecyclerViewClickListener mClickListener;
    private SparseBooleanArray selectedItems;
    private Context mContext;

    /**
     * On create this object, this will get all airplanes in database to manipulate.
     *
     * @param aContext          Manipulate ViewHolder
     * @param aSupportActionBar Update the title
     * @param aClickListener    Create a listener interface
     */
    public AirplaneDetailsAdapter(Context aContext, ActionBar aSupportActionBar, RecyclerViewClickListener aClickListener) {
        mContext = aContext;
        mActionBar = aSupportActionBar;
        mClickListener = aClickListener;
        mAirplaneDataList = SQLite.select().from(AirplaneData.class).queryList();
        selectedItems = new SparseBooleanArray();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View lView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_airplane_detail, parent, false);
        return new ViewHolder(lView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final AirplaneData lAirplane = mAirplaneDataList.get(position);

        holder.mTextId.setText(lAirplane.getId() != null ? lAirplane.getId().toString() : "");
        holder.mTextViewName.setText(lAirplane.getName() != null ? lAirplane.getName() : "");
        holder.mImageViewAirplane.setRotation(lAirplane.getDirection() != null ? lAirplane.getDirection().floatValue() : 0.0f);
        holder.mTextViewCartesianX.setText(lAirplane.getCoordinateX() != null ? String.valueOf(lAirplane.getCoordinateX()) : "");
        holder.mTextViewCartesianY.setText(lAirplane.getCoordinateY() != null ? String.valueOf(lAirplane.getCoordinateY()) : "");
        holder.mTextViewPolarRadius.setText(lAirplane.getRadius() != null ? String.valueOf(lAirplane.getRadius()) : "");
        holder.mTextViewPolarDegrees.setText(lAirplane.getDegree() != null ? String.valueOf(lAirplane.getDegree()) : "");
        holder.mTextViewSpeed.setText(lAirplane.getSpeed() != null ? String.valueOf(lAirplane.getSpeed()) : "");
        holder.mTextViewDirection.setText(lAirplane.getDirection() != null ? String.valueOf(lAirplane.getDirection()) : "");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSelection(position, holder, lAirplane);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAirplaneDataList.size();
    }

    public interface RecyclerViewClickListener {
        void recyclerViewListClicked(AirplaneData aAirplaneData, int aPosition);

        void recyclerViewListUnClicked(AirplaneData aAirplaneData, int aPosition);
    }

    /**
     * Toggle the selection status of the item at a given position
     *
     * @param aPostion  Position of the item to toggle the selection status for
     * @param aAirplane Send to add or remove from list of activity
     */
    private void toggleSelection(int aPostion, ViewHolder aViewHolder, AirplaneData aAirplane) {
        if (selectedItems.get(aPostion, false)) {
            selectedItems.delete(aPostion);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                aViewHolder.itemView.setElevation(0f);
            }
            aViewHolder.itemView.setBackgroundColor(mContext.getResources().getColor(android.R.color.transparent));
            Log.i(TAG, " -- " + " Position: " + aPostion);
            mClickListener.recyclerViewListUnClicked(aAirplane, aPostion);
        } else {
            selectedItems.put(aPostion, true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                aViewHolder.itemView.setElevation(100f);
            }
            aViewHolder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
            Log.i(TAG, " ++ " + " Position: " + aPostion);
            mClickListener.recyclerViewListClicked(aAirplane, aPostion);
        }
        updateActionBarTitle();
    }

    private void updateActionBarTitle() {
        if (getSelectedItemCount() == 0) {
            mActionBar.setTitle("Coordinates");
        } else {
            mActionBar.setTitle("Itens Selected " + getSelectedItemCount());
        }
    }

    /**
     * Indicates if the item at position position is selected
     *
     * @param aPosition Position of the item to check
     * @return true if the item is selected, false otherwise
     */
    private boolean isSelected(int aPosition) {
        return getSelectedItems().contains(aPosition);
        // Not used yet
    }

    /**
     * Count the selected items
     *
     * @return Selected items count
     */
    private int getSelectedItemCount() {
        return selectedItems.size();
    }

    /**
     * Indicates the list of selected items
     *
     * @return List of selected items ids
     */
    private List<Integer> getSelectedItems() {
        List<Integer> items = new ArrayList<>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }

    /**
     * Inner Class ViewHolder
     */
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
