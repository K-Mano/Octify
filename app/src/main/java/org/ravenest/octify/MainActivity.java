package org.ravenest.octify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.net.URL;

import jp.wasabeef.blurry.Blurry;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {

    private BottomSheetBehavior sheetBehavior;
    private CardView bottom_sheet;
    private TextView sheet_state;

    private ConstraintLayout rootView;
    private ImageView blur;

    private Resources r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.main_toolbar);

        rootView = findViewById(R.id.base);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        bottom_sheet = findViewById(R.id.sheet_base);
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        sheet_state = findViewById(R.id.text_sheet_state);

        r = getResources();

        blur=findViewById(R.id.imageView);

        sheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(View view, int newState) {
                boolean flag=false;

                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        sheet_state.setText("今日の予定を隠す");
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        sheet_state.setText("今日の予定を表示");
                        Blurry.delete(rootView);
                        flag=false;
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        if(!flag){
                            flag=true;
                            Blurry.with(getApplicationContext()).async().radius(25).capture(rootView).into(blur);
                        }
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(View view, float offset) {
                blur.setAlpha(offset);
                //bottom_sheet.setAlpha(1-(offset*0.05f));
                if(offset==0){
                    blur.setVisibility(View.GONE);
                }else{
                    if(blur.getVisibility()==View.GONE)blur.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_button) {
            Intent intent = new Intent(this, AddActivity.class);
            startActivity(intent);
        }
        return true;
    }
}