package com.branch.result_ble_uwb;

import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class BlueTooth extends Fragment {
    RelativeLayout relativeLayout;
    RecyclerView recyclerView;
    BluetoothAdapter adapter;
    Button startBtn;
    private int statusBtn = 0;
    private BeaconManager beaconManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        beaconManager = BeaconManager.getInstanceForApplication(getActivity());
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24"));
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bluetooth_frag, container, false);
        relativeLayout = v.findViewById(R.id.Relative_One);
        recyclerView = v.findViewById(R.id.recyceler_view);
        startBtn = v.findViewById(R.id.startBtn);
        return v;
    }

    public void onStart(){
        super.onStart();
    }

    public void onResume() {
        super.onResume();
        try{
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Make Relative Layout to be Gone
                    relativeLayout.setVisibility(View.GONE);

                    //Make RecyclerView to be visible
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                }
            });
        }
        catch(Exception e){

        }

        final Region region = new Region("myBeacons",null, null, null);
        ArrayList<ArrayList<String>> resultList = new ArrayList<ArrayList<String>>();

        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    try{
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Make Relative Layout to be Gone
                                relativeLayout.setVisibility(View.GONE);

                                //Make RecyclerView to be visible
                                recyclerView.setVisibility(View.VISIBLE);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            }
                        });
                    }
                    catch(Exception e){

                    }

                    ArrayList<ArrayList<String>> arrayList = new ArrayList<ArrayList<String>>();
                    ArrayList<ArrayList<String>> temparr = new ArrayList<ArrayList<String>>();

                    // Iterating through all Beacons from Collection of Beacons
                    for (Beacon b:beacons){
                        String uuid = String.valueOf(b.getId1());
                        String major = String.valueOf(b.getId2());
                        String minor = String.valueOf(b.getId3());
                        double distance1 =b.getDistance();
                        String distance = String.valueOf(Math.round(distance1*100.0)/100.0);

                        ArrayList<String> arr = new ArrayList<String>();
                        arr.add(uuid);
                        arr.add(major);
                        arr.add(minor);
                        arr.add(distance);
                        temparr.add(arr);
                    }

                    Collections.sort(temparr, new Comparator<ArrayList<String>>() {
                        @Override
                        public int compare(ArrayList<String> o1, ArrayList<String> o2) {
                            return o1.get(0).compareTo(o2.get(0));
                        }
                    });

                    int cont = 0;
                    String uuid = "";
                    for (int i = 0; i < temparr.size(); i++){
                        ArrayList<String> val = temparr.get(i);
                        String tmp = val.get(0).substring(0, 16);
                        if (tmp.equals(uuid)){
                            cont++;
                            if (cont == 2){
                                ArrayList<String> disval = new ArrayList(Arrays.asList(temparr.get(i).get(3),
                                        temparr.get(i-1).get(3), temparr.get(i-2).get(3)));
                                CalculateDistanceBLE calc = new CalculateDistanceBLE(disval);
                                ArrayList<String> temp_res = calc.calculate_pos_ble();
                                ArrayList<String> res = new ArrayList<String>();
                                String connected = val.get(0).substring(21, 36);
                                res.add(val.get(0)); // uuid
                                res.add(val.get(1)); // major
                                res.add(val.get(2)); // minor
                                res.add(temp_res.get(2));  // distance
                                res.add(temp_res.get(3));  // angle
                                res.add(temp_res.get(0));  // xpos
                                res.add(temp_res.get(1));  // ypos
                                if(connected.equals("00-0000000000")) res.add("0");
                                else res.add("1"); // is it connected?
                                arrayList.add(res);
                            }
                        }
                        else{
                            uuid = tmp;
                            cont = 0;
                        }
                    }

                    for(int i = 0; i < arrayList.size(); i++){
                        ArrayList<String> val = arrayList.get(i);
                        ArrayList<String> res = new ArrayList<String>();
                        String rasp_uuid = val.get(0).substring(0, 16);
                        String status = val.get(0).substring(19, 21);
                        String my_uuid = val.get(0).substring(21, 36);
                        int change = 0;
                        res.add(rasp_uuid);  // uuid
                        res.add(val.get(1)); // major
                        res.add(val.get(2));  // minor
                        res.add(val.get(3));  // distance
                        res.add(val.get(4));  // angle
                        res.add(val.get(5));
                        res.add(val.get(6));
                        res.add(val.get(7)); // connected
                        res.add(status);
                        res.add(my_uuid);

                        for(int j = 0;j < resultList.size(); j++){
                            if(rasp_uuid.equals(resultList.get(j).get(0))){
                                resultList.set(j, res);
                                change = 1;
                            }
                        }
                        if(change != 1) resultList.add(res);
                    }

                    Collections.sort(resultList, new Comparator<ArrayList<String>>() {
                        @Override
                        public int compare(ArrayList<String> o1, ArrayList<String> o2) {
                            return o1.get(3).compareTo(o2.get(3));
                        }
                    });
                }

                if(resultList.size() > 0){
                    try {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Setting Up the Adapter for Recycler View
                                adapter = new BluetoothAdapter(getActivity(), resultList);
                                recyclerView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }catch(Exception e){

                    }
                }
                else{
                    try {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                relativeLayout.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            }
                        });
                    } catch (Exception e) {

                    }
                }
            }
        });


        startBtn.setOnClickListener(v -> {
            if(statusBtn == 0){
                startBtn.setText("종료");
                Toast.makeText(getActivity(), "스캔 시작", Toast.LENGTH_SHORT).show();
                beaconManager.startRangingBeacons(region);
                statusBtn = 1;
            }
            else if(statusBtn == 1){
                beaconManager.stopRangingBeacons(region);
                startBtn.setText("시작");
                Toast.makeText(getActivity(), "스캔 종료", Toast.LENGTH_SHORT).show();
                statusBtn = 0;
            }
        });
    }
}