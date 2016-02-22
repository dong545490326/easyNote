package bmobdemo.easynotes;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/1/19.
 */
public class SelectAct extends AppCompatActivity implements View.OnClickListener, android.view.View.OnFocusChangeListener, TextWatcher {
    private note note;
    private FloatingActionButton sl_save;
    private EditText s_content, s_title;
    private TextView s_time;
    private Toolbar sl_toolbar;
    private NotesNB notesNB;
    private SQLiteDatabase dbWriter;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select);
        note = (bmobdemo.easynotes.note) getIntent().getSerializableExtra(Config.NOTE_DATA);
        if (note != null) {
            Log.e("SelectAct---", note.toString());
        }
        initView();
        initData();
    }


    private void initView() {
        s_content = (EditText) findViewById(R.id.s_content);
        s_title = (EditText) findViewById(R.id.s_title);
        s_time = (TextView) findViewById(R.id.s_time);
        sl_toolbar = (Toolbar) findViewById(R.id.tl_custom);
        sl_save = (FloatingActionButton) findViewById(R.id.sl_save);
        notesNB = new NotesNB(this);
        dbWriter = notesNB.getWritableDatabase();
    }

    private void initData() {
        s_content.setText(note.getContent());
        s_title.setText(note.getTitle());
        s_time.setText(note.getTime());
        sl_toolbar.setTitle("查看便签");
        sl_toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(sl_toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        s_content.setOnFocusChangeListener(this);
        s_title.setOnFocusChangeListener(this);
        s_content.addTextChangedListener(this);
        s_title.addTextChangedListener(this);
        sl_save.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sl_save:
                ContentValues cv = new ContentValues();
                String time = getTime();
                Editable sl_title = s_title.getText();
                Editable sl_content = s_content.getText();
                cv.put(NotesNB.CONTENT, sl_content.toString());
                cv.put(NotesNB.TITLE, sl_title.toString());
                cv.put(NotesNB.TIME, time);
                Log.e("SelectAct2---", note.toString());
                dbWriter.update(NotesNB.TABLE_NAME, cv, NotesNB.ID + " = ?", new String[]{note.getId()});
                note note = new note();
                note.setTitle(sl_title.toString());
                note.setContent(sl_content.toString());
                note.setTime(time);
                Intent intent = new Intent();
                intent.setClass(SelectAct.this, MainActivity.class);
                intent.putExtra(Config.NOTE_DATA, note);
                startActivityForResult(intent, 321);
                finish();
                break;
        }
    }

    public String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        finish();
        Date curDate = new Date();
        String str = format.format(curDate);
        return str;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.s_content:
                sl_toolbar.setTitle("编辑便签");
                break;
            case R.id.s_title:
                sl_toolbar.setTitle("编辑便签");
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Editable sl_title = s_title.getText();
        Editable sl_content = s_content.getText();
        if (!TextUtils.isEmpty(sl_content) && !TextUtils.isEmpty(sl_title)) {
            if (TextUtils.equals(sl_title, note.getTitle()) && TextUtils.equals(sl_content, note.getContent())) {
                Log.e("SelectAct1---", note.toString());
                sl_save.setVisibility(View.INVISIBLE);
                return;
            }
            sl_save.setVisibility(View.VISIBLE);
        } else {
            sl_save.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
