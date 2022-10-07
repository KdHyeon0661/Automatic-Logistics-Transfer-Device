package com.branch.result_ble_uwb;

import android.animation.ValueAnimator;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseSettings;
import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.BeaconTransmitter;

import java.util.ArrayList;
import java.util.Arrays;

public class BluetoothAdapter extends RecyclerView.Adapter<BluetoothAdapter.ViewHolder> {
    private int prePosition = -1;
    private Context context;
    private ArrayList<ArrayList<String>> arr;
    private SparseBooleanArray selectedItems = new SparseBooleanArray();

    public BluetoothAdapter(Context context, ArrayList<ArrayList<String>> arr) {
        this.context = context;
        this.arr = arr;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView uuid, weight, distance;
        public Button call, stop;
        public LinearLayout linearItem, expand;
        private int position;
        String angle, My_uuid = "11-223344556677";
        public ArrayList<String> data;
        private int call_activate = 0, status_button = 0;

        BeaconParser beaconParser = new BeaconParser()
                .setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24");
        BeaconTransmitter beaconTransmitter = new BeaconTransmitter(context, beaconParser);

        ViewHolder(Context context, View itemView) {
            super(itemView);

            uuid = itemView.findViewById(R.id.uuid);
            weight = itemView.findViewById(R.id.weight);
            distance = itemView.findViewById(R.id.distance);
            call = itemView.findViewById(R.id.call);
            stop = itemView.findViewById(R.id.stop);
            linearItem = itemView.findViewById(R.id.linearItem);
            expand = itemView.findViewById(R.id.expanded);
        }

        void onBind(ArrayList<String> data, int position) {
            this.data = data;
            this.position = position;

            uuid.setText(data.get(0));
            weight.setText(data.get(1));
            distance.setText(data.get(3));
            angle = data.get(4);
            int angleD = (int)(Double.parseDouble(angle) * 100);
            angle = String.valueOf(angleD);
            String connected = data.get(7);

            changeVisibility(selectedItems.get(position));

            linearItem.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if (selectedItems.get(position)) {
                        // 펼쳐진 Item을 클릭 시
                        selectedItems.delete(position);
                    } else {
                        // 직전의 클릭됐던 Item의 클릭상태를 지움
                        selectedItems.delete(prePosition);
                        // 클릭한 Item의 position을 저장
                        selectedItems.put(position, true);
                    }
                    // 해당 포지션의 변화를 알림
                    if (prePosition != -1) notifyItemChanged(prePosition);
                    notifyItemChanged(position);
                    // 클릭된 position 저장
                    prePosition = position;
                }
            });

            call.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    if (status_button == 0){
                        Beacon beacon = new Beacon.Builder()
                                .setId1(data.get(0) + "04-01" + My_uuid)
                                .setId2(angle)
                                .setId3("0")
                                .setManufacturer(0x004c)
                                .setTxPower(-59)
                                .build();
                        beaconTransmitter.startAdvertising(beacon, new AdvertiseCallback() {

                            @Override
                            public void onStartFailure(int errorCode) {
                                Log.d("BeaconAdapter", "Error from start advertising " + errorCode);
                            }

                            @Override
                            public void onStartSuccess(AdvertiseSettings settingsInEffect) {
                                Log.d("BeaconAdapter", "Success start advertising");
                            }
                        });
                        call_activate = 1;
                        status_button = 1;
                        Toast.makeText(context, "스캔 시작", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        status_button = 0;
                        call_activate = 0;
                        beaconTransmitter.stopAdvertising();
                        Log.d("BeaconAdapter", "Success stop advertising");
                    }
                }
            });

            stop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (call_activate == 1){
                        beaconTransmitter.stopAdvertising();
                        beaconTransmitter = new BeaconTransmitter(context, beaconParser);
                        Beacon beacon = new Beacon.Builder()
                                .setId1(data.get(0) + "04-02" + My_uuid)
                                .setId2(angle)
                                .setId3("0")
                                .setManufacturer(0x004c)
                                .setTxPower(-59)
                                .build();
                        beaconTransmitter.startAdvertising(beacon);
                        Toast.makeText(context, "일시 정지", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(context, "해당 장치와 연결되어 있지 않습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        private void changeVisibility(final boolean isExpanded) {
            // height 값을 dp로 지정해서 넣고싶으면 아래 소스를 이용
            int dpValue = 50;
            float d = context.getResources().getDisplayMetrics().density;
            int height = (int) (dpValue * d);

            // ValueAnimator.ofInt(int... values)는 View가 변할 값을 지정, 인자는 int 배열
            ValueAnimator va = isExpanded ? ValueAnimator.ofInt(0, height) : ValueAnimator.ofInt(height, 0);
            // Animation이 실행되는 시간, n/1000초
            va.setDuration(600);
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    // value는 height 값
                    int value = (int) animation.getAnimatedValue();
                    // imageView의 높이 변경
                    expand.getLayoutParams().height = value;
                    expand.requestLayout();
                    // imageView가 실제로 사라지게하는 부분
                    expand.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
                }
            });
            // Animation start
            va.start();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.make_cardview, parent, false);

        ViewHolder viewholder = new ViewHolder(context, view);

        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(arr.get(position), position);
    }

    @Override
    public int getItemCount() {
        return arr.size();
    }
}