package org.ravenest.octify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import org.ravenest.octify.Adapters.MainPagerAdapter;
import org.ravenest.octify.Adapters.TaskListAdapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import android.view.View;

import jp.wasabeef.blurry.Blurry;

import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED;
import static com.google.android.material.color.MaterialColors.ALPHA_FULL;
import static org.ravenest.octify.Utility.drawableToBitmap;

public class MainActivity extends AppCompatActivity {

    public List<TaskObject.Model> tasks = new ArrayList<TaskObject.Model>();

    public static String NEW_TASK_RESULT = "";
    private BottomSheetBehavior sheetBehavior;
    private CardView bottom_sheet;
    private TextView sheet_state;

    private RecyclerView tasks_list;
    private TabLayout tab;
    private ViewPager vp;

    private CoordinatorLayout rootView;
    private ConstraintLayout baseView;
    private ImageView blur;

    private TaskObject obj;

    private Resources r;
    private Drawable ICON_REMOVE;

    private TaskListAdapter tAdapter;
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.main_toolbar);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int height = displayMetrics.heightPixels;

        rootView = findViewById(R.id.root);
        baseView = findViewById(R.id.base);
        
        tab = findViewById(R.id.tab);
        vp = findViewById(R.id.pager);

        MainPagerAdapter fAdapter = new MainPagerAdapter(getSupportFragmentManager());

        vp.setOffscreenPageLimit(2);
        vp.setAdapter(fAdapter);

        tab.setupWithViewPager(vp);

        tasks_list = findViewById(R.id.tasks_list);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        bottom_sheet = findViewById(R.id.sheet_base);
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        sheet_state = findViewById(R.id.sheet_state);

        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        sheetBehavior.setPeekHeight(height/3, true);

        r = getResources();
        ICON_REMOVE = r.getDrawable(R.drawable.ic_baseline_delete_24);

        blur=findViewById(R.id.imageView);

        //RecyclerViewを取得
        tasks_list = findViewById(R.id.tasks_list);

        LinearLayoutManager lManager = new LinearLayoutManager(this);
        lManager.setOrientation(LinearLayoutManager.VERTICAL);
        tasks_list.setLayoutManager(lManager);
        tasks_list.setHasFixedSize(true);

        //Dividerの設定
        DividerItemDecoration divider = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        tasks_list.addItemDecoration(divider);

        sheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
             @Override
             public void onStateChanged(View view, int newState) {
                 boolean flag = false;

                 switch (newState) {
                     case BottomSheetBehavior.STATE_HIDDEN:
                         break;
                     case BottomSheetBehavior.STATE_EXPANDED:
                         sheet_state.setText("今日の予定を隠す");
                         break;
                     case STATE_COLLAPSED:
                         sheet_state.setText("今日の予定を表示");
                         Blurry.delete(rootView);
                         flag = false;
                         break;
                     case BottomSheetBehavior.STATE_DRAGGING:
                         if (!flag) {
                             flag = true;
                             Blurry.with(getApplicationContext()).async().radius(2).sampling(12).capture(rootView).into(blur);
                         }
                         break;
                     case BottomSheetBehavior.STATE_SETTLING:
                 }
             }

             @Override
             public void onSlide(@NonNull View bottomSheet, float offset) {
                blur.setAlpha(offset);
                if(offset>0){
                    if(blur.getVisibility()==View.GONE) blur.setVisibility(View.VISIBLE);
                }
             }
         });

        // タスク読み込み
        reload();

        ItemTouchHelper swipeTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target){
                return false;
            }

            @Override
            public void onSwiped( RecyclerView.ViewHolder viewHolder, int direction) {
                if(direction == ItemTouchHelper.LEFT){
                    deleteFile(String.format("%s.json",tasks.get(viewHolder.getAdapterPosition()).task.name));
                    tasks.remove(viewHolder.getAdapterPosition());
                    tAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                }
                else if(direction == ItemTouchHelper.RIGHT){
                    tAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                }
            }

            @Override
            public void onChildDraw(Canvas base, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    // RecyclerViewのアイテムをViewHolderから取得
                    View itemView = viewHolder.itemView;

                    // スワイプ中の背景
                    Paint swipe_background = new Paint();
                    // アイコン
                    Bitmap icon = null;
                    if(dX>0){

                    }
                    // 右にスワイプ
                    else{
                        // 背景色を設定
                        swipe_background.setARGB(255,255,50,50);

                        // スワイプした距離によって背景を描画
                        base.drawRect((float)itemView.getRight() + dX, (float)itemView.getTop(), (float)itemView.getRight(), (float)itemView.getBottom(), swipe_background);

                        // DrawableからBitmapを作成
                        if(Math.abs(dX) < itemView.getBottom()-itemView.getTop()){
                            icon = drawableToBitmap(ICON_REMOVE, 96, 96, itemView.getRight()+(int)dX+(itemView.getBottom()-itemView.getTop())/2, (itemView.getTop()+itemView.getBottom())/2, Color.WHITE);
                        }else{
                            icon = drawableToBitmap(ICON_REMOVE, 96, 96, itemView.getRight()-(itemView.getBottom()-itemView.getTop())/2, (itemView.getTop()+itemView.getBottom())/2, Color.WHITE);
                        }
                        // アイコンを描画
                        base.drawBitmap(icon, new Matrix(), swipe_background);
                    }
                    final float alpha = ALPHA_FULL - Math.abs(dX) / (float) viewHolder.itemView.getWidth();

                    viewHolder.itemView.setAlpha(alpha);
                    viewHolder.itemView.setTranslationX(dX);
                } else {
                    super.onChildDraw(base, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
            }
        });
        swipeTouchHelper.attachToRecyclerView(tasks_list);
    }

    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if(resultCode==RESULT_OK){
            Snackbar task = Snackbar.make(baseView, R.string.tasks_created, 3000);
            task.show();
            reload();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.add_button:
                Intent intent = new Intent(this, AddActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.today_button:
                sheetBehavior.setState(STATE_COLLAPSED);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void reload(){
        tasks.clear();
        try{
            File[] fileArray = getFilesDir().listFiles();
            for(int i=0; i<fileArray.length;i++){
                if(fileArray[i].getName().contains(".json")){
                    Log.d("LOAD", fileArray[i].getName());
                    FileInputStream in = openFileInput(fileArray[i].getName());
                    BufferedReader reader = new BufferedReader( new InputStreamReader( in , "UTF-8") );
                    String json = "";
                    String tmp;
                    while( (tmp = reader.readLine()) != null ){
                        json += tmp;
                    }
                    reader.close();

                    Gson gson = new Gson();
                    tasks.add(gson.fromJson(json, TaskObject.Model.class));
                }
            }
            tAdapter = new TaskListAdapter(tasks, R.layout.task_context);
            tasks_list.setAdapter(tAdapter);
        }catch(IOException e){
            e.printStackTrace();
        }catch(NullPointerException e){
            e.printStackTrace();
        }

        tAdapter.notifyDataSetChanged();
    }
}