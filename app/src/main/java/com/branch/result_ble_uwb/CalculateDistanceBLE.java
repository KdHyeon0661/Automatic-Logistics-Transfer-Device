package com.branch.result_ble_uwb;

import android.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CalculateDistanceBLE {
    ArrayList<Pair<Integer, Integer>> machine_pos = new ArrayList<>();
    ArrayList<String> machine_dist;

    public CalculateDistanceBLE(ArrayList<String> arr){
        machine_pos.add(new Pair<>(0, 0));
        machine_pos.add(new Pair<>(10, 10));
        machine_pos.add(new Pair<>(-10, 10));
        machine_dist = arr;

    }

    public ArrayList<String> calculate_pos_ble(){
        double x1 = machine_pos.get(0).first;
        double y1 = machine_pos.get(0).second;
        double x2 = machine_pos.get(1).first;
        double y2 = machine_pos.get(1).second;
        double x3 = machine_pos.get(2).first;
        double y3 = machine_pos.get(2).second;

        double r1 = Double.parseDouble(machine_dist.get(0));
        double r2 = Double.parseDouble(machine_dist.get(1));
        double r3 = Double.parseDouble(machine_dist.get(2));

        double S = (Math.pow(x3, 2.) - Math.pow(x2, 2.) + Math.pow(y3, 2.) - Math.pow(y2, 2.)
                    + Math.pow(r2, 2.) - Math.pow(r3, 2.)) / 2.0;
        double T = (Math.pow(x1, 2.) - Math.pow(x2, 2.) + Math.pow(y1, 2.) - Math.pow(y2, 2.)
                + Math.pow(r2, 2.) - Math.pow(r1, 2.)) / 2.0;

        double y = ((T * (x2 - x3)) - (S * (x2 - x1))) / (((y1 - y2) * (x2 - x3)) - ((y3 - y2) * (x2 - x1)));
        double x = ((y * (y1 - y2)) - T) / (x2 - x1);
        double dis = Math.sqrt(Math.pow(5 - x, 2.) + Math.pow(5 - y, 2.));
        double angle = Math.atan2(5-y, 5-x);
        ArrayList<String> res = new ArrayList(Arrays.asList(Double.toString(x), Double.toString(y), Double.toString(dis),
                    Double.toString(angle)));
        return res;
    }
}
