package bmobdemo.easynotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/1/17.
 */
public class NotesNB extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "notes";
    public static final String TITLE = "title";
    public static final String CONTENT = "content";
    public static final String ID = "_id";
    public static final String TIME = "time";

    public NotesNB(Context context) {
        super(context, "notes", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + TITLE + " TEXT NOT NULL,"
                + CONTENT + " TEXT NOT NULL," + TIME + " TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    private void update(int id, String title, String content) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = ID + " = ?";
        String[] whereValue = {Integer.toString(id)};
        ContentValues cv = new ContentValues();
        cv.put(TABLE_NAME, title);
        cv.put(CONTENT, content);
        db.update(TABLE_NAME, cv, where, whereValue);
    }

    public void delete(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = ID + " = ?";
        String[] whereValue = {Integer.toString(id)};
        db.delete(TABLE_NAME, where, whereValue);
    }
}
