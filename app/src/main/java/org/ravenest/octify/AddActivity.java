package org.ravenest.octify;

import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.util.Pair;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.Calendar;

import static androidx.fragment.app.DialogFragment.STYLE_NO_FRAME;

public class AddActivity extends AppCompatActivity {

    private FloatingActionButton submit;
    private ConstraintLayout date_set;
    private ConstraintLayout time_set;

    private TextInputEditText task_name;
    private TextInputEditText task_desc;
    private TextView date_text;
    private TextView time_text;

    private TaskObject result;

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

        task_name = findViewById(R.id.task_name_input);
        task_desc = findViewById(R.id.task_desc_input);

        fm = getSupportFragmentManager();

        date_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDatePicker bd = MaterialDatePicker.Builder.datePicker().build();
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

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(MainActivity.NEW_TASK_DATA, (Parcelable)result);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}