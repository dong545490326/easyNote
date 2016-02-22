package bmobdemo.easynotes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.leo.gesturelibray.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView mRecyclerView;
    private FloatingActionButton bt;
    private SQLiteDatabase dbReader;
    private NotesNB notesNB;
    private CheckBox checkbox_card;
    private TextView checkbox_lock;
    private SQLiteDatabase dbWriter;
    private bmobdemo.easynotes.SimpleAdapter mAdapter;
    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private SharedPreferences sp;
    private AnimationDrawable mAnimationDrawable;
    private List<note> list = new ArrayList<>();
    String[] item_list = {"删除"};
    String[] item_list_lock = {"绘制密码锁", "删除密码锁"};
    private String mPassword = null;
    private static String PASS_KEY = "PASS_KEY_MAP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        boolean isChecked = sp.getBoolean("isChecked", false);
        boolean isNoChecked = sp.getBoolean("!isChecked", true);
        checkbox_card.setChecked(isNoChecked);
        checkbox_card.setChecked(isChecked);
    }

    private void isCheck() {
        checkbox_card.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("isChecked", isChecked);
                editor.putBoolean("!isChecked", !isChecked);
                editor.commit();
                if (isChecked) {
                    mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                } else if (!isChecked) {
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                }
            }
        });

    }

    private void initData() {
        isCheck();
        checkbox_lock.setOnClickListener(this);
        bt.setOnClickListener(this);
        toolbar.setTitle("EasyNotes");
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAdapter.setmOnItemClickLitener(new bmobdemo.easynotes.SimpleAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, note note, int p) {
                Log.e("note-----", note.toString());
                actionSelectActivity(note);
            }

            @Override
            public void onItemLongClidc(View view, note note, int pos) {
                Log.e("note-----", note.toString());
                Log.e("note-----", String.valueOf(pos));
                showDialog(note, pos);
            }

            private void showDialog(final note note, final int pos) {
                Intent intent = new Intent();
                intent.putExtra(Config.NOTE_DATA, note);
                intent.putExtra(Config.NOTE_DATA, pos);
                LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                final View view = inflater.inflate(R.layout.selectdialog, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setItems(item_list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            Toast.makeText(MainActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                            Log.e("note-----", note.toString());
                            dbWriter.delete(NotesNB.TABLE_NAME, NotesNB.ID + " = ?", new String[]{note.getId()});
                            list.remove(pos);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }

        });
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                toolbar.setTitle("极简便签");
                mAnimationDrawable.stop();
                drawerView.setClickable(true);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                toolbar.setTitle("EasyNotes");
                mAnimationDrawable.start();
            }
        };
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void actionSelectActivity(note note) {
        Intent intent = new Intent(this, SelectAct.class);
        intent.putExtra(Config.NOTE_DATA, note);
        startActivityForResult(intent,321);
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview);
        sp = this.getSharedPreferences("config", MODE_PRIVATE);
        bt = (FloatingActionButton) findViewById(R.id.bt);
        checkbox_card = (CheckBox) findViewById(R.id.checkbox_card);
        checkbox_lock = (TextView) findViewById(R.id.checkbox_lock);
        toolbar = (Toolbar) findViewById(R.id.tl_custom);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_left);
        mAnimationDrawable = new AnimationDrawable();
        notesNB = new NotesNB(this);
        dbReader = notesNB.getReadableDatabase();
        dbWriter = notesNB.getWritableDatabase();
        selectDB();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt:
                Intent intent = new Intent(MainActivity.this, AddContent.class);
                startActivityForResult(intent, 123);
                break;
            case R.id.checkbox_lock:
                LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                final View view = inflater.inflate(R.layout.selectdialog, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setItems(item_list_lock, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String password = ConfigUtil.getInstance(MainActivity.this).getString(PASS_KEY);
                        if(which==0){
                            if(StringUtils.isEmpty(password)){
                            Intent intent =new Intent(MainActivity.this,LockActivity.class);
                            startActivity(intent);
                            }
                            else if (StringUtils.isNotEmpty(password)){
                                Toast.makeText(MainActivity.this,"请先删除已绘制的密码锁",Toast.LENGTH_LONG).show();
                            }
                        }
                        else if (which==1){

                            if(StringUtils.isEmpty(password)){
                                Toast.makeText(MainActivity.this,"没有绘制密码锁",Toast.LENGTH_SHORT).show();
                            }else if (StringUtils.isNotEmpty(password)){
                                ConfigUtil.clearAll();
                                Toast.makeText(MainActivity.this,"绘制的密码锁已清除",Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
        }
    }


    public void selectDB() {
        list.clear();
        Cursor cursor = dbReader.query(NotesNB.TABLE_NAME, null, null, null, null, null, null);
        mAdapter = new bmobdemo.easynotes.SimpleAdapter(this, list);
        mRecyclerView.setAdapter(mAdapter);
        int i = 0;
        while (cursor.moveToPosition(i)) {
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String content = cursor.getString(cursor.getColumnIndex("content"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            String id = cursor.getString(cursor.getColumnIndex("_id"));
            note note = new note();
            note.setTitle(title);
            note.setContent(content);
            note.setTime(time);
            note.setId(id);
            list.add(note);
            i++;
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Log.e(MainActivity.ACTIVITY_SERVICE, "数据");
        }
         if (requestCode == 321) {
         list.clear();
             Cursor cursor = dbReader.query(NotesNB.TABLE_NAME, null, null, null, null, null, null);
             int i = 0;
             while (cursor.moveToPosition(i)) {
                 String title = cursor.getString(cursor.getColumnIndex("title"));
                 String content = cursor.getString(cursor.getColumnIndex("content"));
                 String time = cursor.getString(cursor.getColumnIndex("time"));
                 String id = cursor.getString(cursor.getColumnIndex("_id"));
                 note note = new note();
                 note.setTitle(title);
                 note.setContent(content);
                 note.setTime(time);
                 note.setId(id);
                 list.add(note);
                 i++;
             }
            mAdapter.notifyDataSetChanged();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }
}
