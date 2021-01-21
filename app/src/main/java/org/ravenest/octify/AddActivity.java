package org.ravenest.octify;

import android.app.SearchManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.util.Pair;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.Calendar;

import static androidx.fragment.app.DialogFragment.STYLE_NO_FRAME;

public class AddActivity extends AppCompatActivity {

    FloatingActionButton submit;
    ConstraintLayout date_set;
    ConstraintLayout time_set;

    FragmentManager fm;
    FragmentTransaction ft;

    MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);

        setContentView(R.layout.activity_add);
        Toolbar toolbar = findViewById(R.id.toolbar_aa);

        // ToolBarをActionBarとして設定
        setSupportActionBar(toolbar);

        // ActionBarの設定
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // コントロールの取得
        submit = findViewById(R.id.submit_button);
        date_set = findViewById(R.id.date_set);
        time_set = findViewById(R.id.time_set);

        fm = getSupportFragmentManager();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        date_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDatePicker bd = MaterialDatePicker.Builder.datePicker().build();
                bd.addOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {

                    }
                });
                bd.show(fm,"DatePicker");
            }
        });
        time_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialTimePicker bt = new MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_12H).build();
                bt.show(fm,"TimePicker");
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}