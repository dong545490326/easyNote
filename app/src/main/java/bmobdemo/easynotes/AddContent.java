package bmobdemo.easynotes;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/1/17.
 */
public class AddContent extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private FloatingActionButton savebtn;
    private MaterialEditText ettext;
    private MaterialEditText ettext_title;
    private NotesNB notesNB;
    private SQLiteDatabase dbWriter;
    private Toolbar sl_toolbar;
    String titlesrc = " ";
    String contentsrc = " ";
    private static final String ACTIVITY_TAG = "LogDemo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addcontent);
        initView();
        initListener();
        initData();
    }


    public void initView() {
        savebtn = (FloatingActionButton) findViewById(R.id.save);
        ettext = (MaterialEditText) findViewById(R.id.ettext);
        ettext_title = (MaterialEditText) findViewById(R.id.ettext_title);
        sl_toolbar = (Toolbar) findViewById(R.id.tl_custom);
        notesNB = new NotesNB(this);
        dbWriter = notesNB.getWritableDatabase();
    }

    public void initListener() {
        savebtn.setOnClickListener(this);
    }

    public void initData() {
        sl_toolbar.setTitle("添加便签");
        sl_toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(sl_toolbar);
        ettext.addTextChangedListener(this);
        ettext_title.addTextChangedListener(this);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save:
                addDB();
                break;
        }
    }


    private void addDB() {
        ContentValues cv = new ContentValues();
        String time = getTime();
        cv.put(NotesNB.CONTENT, ettext.getText().toString());
        cv.put(NotesNB.TITLE, ettext_title.getText().toString());
        cv.put(NotesNB.TIME, time);
        dbWriter.insert(NotesNB.TABLE_NAME, null, cv);
        note note = new note();
        note.setTitle(ettext_title.getText().toString());
        note.setContent(ettext.getText().toString());
        note.setTime(time);
        Intent intent = new Intent();
        intent.setClass(AddContent.this, MainActivity.class);
        intent.putExtra(Config.NOTE_DATA, note);
        startActivityForResult(intent, 123);
        finish();
    }

    public String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
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
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        titlesrc = ettext_title.getText().toString();
        contentsrc = ettext.getText().toString();
        if (!TextUtils.isEmpty(titlesrc) && !TextUtils.isEmpty(contentsrc)) {
            savebtn.setVisibility(View.VISIBLE);
        } else {
            savebtn.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}

