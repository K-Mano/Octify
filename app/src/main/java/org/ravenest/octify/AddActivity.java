package org.ravenest.octify;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.util.Pair;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Month;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static androidx.fragment.app.DialogFragment.STYLE_NO_FRAME;

public class AddActivity extends AppCompatActivity {

    private FloatingActionButton submit;
    private ConstraintLayout date_set;
    private ConstraintLayout time_set;

    private TextInputEditText task_name;
    private TextInputEditText task_desc;

    private TextView date_text;
    private TextView time_text;

    private RatingBar priority;

    private Date date = new Date();
    private Long day = Long.valueOf(0);
    private Long time = Long.valueOf(0);

    int year;
    int month;

    FragmentManager fm;
    FragmentTransaction ft;

    MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Calendar calendar = Calendar.getInstance();

        year  = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);

        setContentView(R.layout.activity_add);
        final Toolbar toolbar = findViewById(R.id.toolbar_aa);

        // 外部クラスのインスタンス化
        final Utility util = new Utility();

        // ToolBarをActionBarとして設定
        setSupportActionBar(toolbar);

        // ActionBarの設定
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // コントロールの取得
        submit = findViewById(R.id.submit_button);
        priority = findViewById(R.id.priorBar);
        date_set = findViewById(R.id.date_set);
        time_set = findViewById(R.id.time_set);

        date_text = findViewById(R.id.date_text);
        time_text = findViewById(R.id.time_text);

        task_name = findViewById(R.id.task_name_input);
        task_desc = findViewById(R.id.task_desc_input);

        fm = getSupportFragmentManager();

        date_set.setOnClickListener(v -> {
            CalendarConstraints.Builder cc = new CalendarConstraints.Builder();
            cc.setStart(Calendar.getInstance().getTimeInMillis());
            final MaterialDatePicker bd = MaterialDatePicker.Builder.datePicker().setCalendarConstraints(cc.build()).build();
            bd.addOnPositiveButtonClickListener(selection -> {
                day = util.regionConverter((Long)selection, "yyyy/MM/dd", Locale.ROOT);
                date_text.setText(String.format("%s",util.dateToString(new Date(day), "yyyy/MM/dd", Locale.ROOT)));
            });
            bd.show(fm,"DatePicker");
        });

        time_set.setOnClickListener(v -> {
            final MaterialTimePicker bt = new MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H).build();
            bt.addOnPositiveButtonClickListener(v1 -> {
                time = util.stringToDate(String.format("%d:%d",bt.getHour(),bt.getMinute()), "HH:mm", Locale.ROOT).getTime();
                time_text.setText(String.format("%s",util.dateToString(new Date(time), "HH:mm", Locale.ROOT)));
            });
            bt.show(fm,"TimePicker");
        });

        submit.setOnClickListener(v -> {
            TaskObjectOperations too = new TaskObjectOperations();
            String result = too.ToJsonObject(task_name.getText().toString(), task_desc.getText().toString(), priority.getNumStars(), day, time);

            try {
                FileOutputStream fos = openFileOutput(String.format("%s.json", task_name.getText()), Context.MODE_PRIVATE);
                fos.write(result.getBytes());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }

            Intent intent = new Intent();
            intent.putExtra(MainActivity.NEW_TASK_RESULT, "add");
            setResult(RESULT_OK, intent);
            finish();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}