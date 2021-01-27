package org.ravenest.octify.Fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.ravenest.octify.R;
import org.ravenest.octify.Views.CircleChartView;

public class ChartFragment extends Fragment {

    private CircleChartView ccv;
    private TextView percent;

    private float value = 72.8f;
    public ChartFragment() {
        // Required empty public constructor
    }

    public static ChartFragment newInstance() {
        ChartFragment fragment = new ChartFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chart, container, false);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

        float dp = displayMetrics.density;
        Log.d("DEBUG","Width->" + dp + ",Height=>" + dp);

        percent = rootView.findViewById(R.id.percent);
        percent.setText(String.format("%.1f",value));

        ccv = rootView.findViewById(R.id.chart);
        ccv.setScale(75*dp, 100*dp);
        ccv.setColor(Color.argb(255,117,121,98));
        ccv.setValue(value);

        return rootView;
    }
}