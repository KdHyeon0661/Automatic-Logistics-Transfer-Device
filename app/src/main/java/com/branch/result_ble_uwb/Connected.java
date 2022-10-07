package com.branch.result_ble_uwb;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Connected extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedinstanceState){
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment2, container, false);
        return rootView;
    }
}
