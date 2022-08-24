package com.branch.result_ble_uwb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BluetoothAdapter extends RecyclerView.Adapter<BluetoothAdapter.ViewHolder> {
    private ArrayList<ArrayList<String>> arr;

    public BluetoothAdapter(ArrayList<ArrayList<String>> arr) {
        this.arr = arr;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView uuid;
        public TextView weight;
        public TextView distance;

        ViewHolder(Context context, View itemView) {
            super(itemView);

            uuid = itemView.findViewById(R.id.uuid);
            weight = itemView.findViewById(R.id.weight);
            distance = itemView.findViewById(R.id.distance);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.make_cardview, parent, false);

        ViewHolder viewholder = new ViewHolder(context, view);

        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ArrayList<String> arrayList = arr.get(position);

        if (arr.size()>0){
            holder.uuid.setText(arrayList.get(0));
            holder.weight.setText(arrayList.get(1));
            holder.distance.setText(arrayList.get(3));
        }
    }

    @Override
    public int getItemCount() {
        return arr.size();
    }
}