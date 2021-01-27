package org.ravenest.octify.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.ravenest.octify.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TodayFragment extends Fragment {

    private TextView datetext;

    public TodayFragment() {
        // Required empty public constructor
    }

    public static TodayFragment newInstance() {
        TodayFragment fragment = new TodayFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_today, container, false);

        DateFormat df = new SimpleDateFormat("yyyy年 M月 d日 EEE曜日");
        Date date = new Date(System.currentTimeMillis());

        datetext = rootView.findViewById(R.id.date_title);
        datetext.setText(df.format(date));
        return rootView;
    }

    public static String getNowDate(){
        final DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        final Date date = new Date(System.currentTimeMillis());
        return df.format(date);
    }
}