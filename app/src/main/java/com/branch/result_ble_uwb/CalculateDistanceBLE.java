package com.branch.result_ble_uwb;

import android.annotation.SuppressLint;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CalculateDistanceBLE {
    ArrayList<Pair<Integer, Integer>> machine_pos = new ArrayList<>();
    ArrayList<String> machine_dist;

    public CalculateDistanceBLE(ArrayList<String> arr) {
        machine_pos.add(new Pair<>(0, -5));
        machine_pos.add(new Pair<>(10, 10));
        machine_pos.add(new Pair<>(-10, 10)); // 절대 좌표
        machine_dist = arr;

    }

    public ArrayList<String> calculate_pos_ble() {
        double x1 = machine_pos.get(0).first;
        double y1 = machine_pos.get(0).second;
        double x2 = machine_pos.get(1).first;
        double y2 = machine_pos.get(1).second;
        double x3 = machine_pos.get(2).first;
        double y3 = machine_pos.get(2).second; // 절대 좌표 꺼내기

        double r1 = Double.parseDouble(machine_dist.get(0));
        double r2 = Double.parseDouble(machine_dist.get(1));
        double r3 = Double.parseDouble(machine_dist.get(2)); // 거리 꺼내기

        double A = x2 - x1, B = y2 - y1, D = x3 - x2, E = y3 - y2;
        double C = (Math.pow(r1, 2.) - Math.pow(r2, 2.) - Math.pow(x1, 2.) + Math.pow(x2, 2.)
                - Math.pow(y1, 2.) + Math.pow(y2, 2.));
        double F = (Math.pow(r2, 2.) - Math.pow(r3, 2.) - Math.pow(x2, 2.) + Math.pow(x3, 2.)
                - Math.pow(y2, 2.) + Math.pow(y3, 2.)) / 2.0;

        double y = ((F * A) - (D * C)) / ((A * E) - (D * B));
        double x = ((F * B) - (E * C)) / ((B * D) - (E * A));
        double dis = (Math.sqrt(Math.pow(x, 2.) + Math.pow(y, 2.))) / 100.;
        double radian = Math.atan2(y, x);
        double degree = radian * 180 / Math.PI;
        if(degree < 0) degree = 360 - degree;
        @SuppressLint("DefaultLocale") String str_dis = String.format("%.2f", dis);
        @SuppressLint("DefaultLocale") String str_x = String.format("%.2f", x);
        @SuppressLint("DefaultLocale") String str_y = String.format("%.2f", y);
        @SuppressLint("DefaultLocale") String str_angle = String.format("%.2f", degree);

        ArrayList<String> res = new ArrayList(Arrays.asList(str_x, str_y, str_dis, str_angle));
        return res;
    }
}
