package in.rushitthakker.notebook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by silversurfer on 12/26/2016.
 */

public class NoteBookDbAdapter {
    private static final String DATABASE_NAME = "notebook.db";
    private static final int DATABASE_VERSION = 1;
    private NotebookDBHelper notebookDBHelper;

    public static final String NOTE_TABLE = "note";
    public static final String COLOUMN_ID = "_id";
    public static final String COLOUMN_TITLE = "title";s
    public static final String COLOUMN_MESSAGE= "message";
    public static final String COLOUMN_CATEGORY= "category";
    public static final String COLOUMN_DATE = "date";

    private String[] allColumns = {COLOUMN_ID,COLOUMN_TITLE,COLOUMN_MESSAGE,COLOUMN_CATEGORY,COLOUMN_DATE};

    public static final String CREATE_TABLE_NOTE ="Create table " + NOTE_TABLE + " ( " + COLOUMN_ID + " integer primary key autoincrement, "
                                                + COLOUMN_TITLE +" text not null, " + COLOUMN_MESSAGE + " text not null, " + COLOUMN_CATEGORY +" text not null, "
                                                + COLOUMN_DATE +");";

    private SQLiteDatabase sqlDB;
    private Context context;

    public NoteBookDbAdapter(Context ctx){
        context = ctx;
    }

    public NoteBookDbAdapter open() throws android.database.SQLException {
        notebookDBHelper = new NotebookDBHelper(context);
        sqlDB = notebookDBHelper.getWritableDatabase();
        return  this;
    }

    public void close(){
        notebookDBHelper.close();
    }
    public Note createNote(String title, String message, Note.Category category){
        ContentValues values = new ContentValues();
        values.put(COLOUMN_TITLE, title);
        values.put(COLOUMN_MESSAGE, message);
        values.put(COLOUMN_CATEGORY, category.name());
        values.put(COLOUMN_DATE, Calendar.getInstance().getTimeInMillis() + "");

        long insertId = sqlDB.insert(NOTE_TABLE, null, values);

        Cursor cursor = sqlDB.query(NOTE_TABLE,
                allColumns, COLOUMN_ID + " = "  + insertId, null, null, null, null);

        cursor.moveToFirst();
        Note newNote = cursorToNote(cursor);
        cursor.close();
        return newNote;
    }

    public long deleteNote(long idToDelete){
        return sqlDB.delete(NOTE_TABLE, COLOUMN_ID + " = " + idToDelete, null);
    }

    public long updateNote(long idToUpdate, String newTitle, String newMessage, Note.Category newCategory){
        ContentValues values = new ContentValues();
        values.put(COLOUMN_TITLE, newTitle);
        values.put(COLOUMN_MESSAGE, newMessage);
        values.put(COLOUMN_CATEGORY, newCategory.name());
        values.put(COLOUMN_DATE, Calendar.getInstance().getTimeInMillis() + "");

        return sqlDB.update(NOTE_TABLE, values, COLOUMN_ID + " = " + idToUpdate, null);
    }

    public ArrayList<Note> getAllNotes(){
        ArrayList<Note> notes = new ArrayList<Note>();

        //grab all of the information in our database for the notes in it
        Cursor cursor = sqlDB.query(NOTE_TABLE, allColumns, null, null, null, null, null );

        //loop through all of the rows (notes) in our database and create new note objects from those
        //rows and add them to our array list
        for(cursor.moveToLast(); !cursor.isBeforeFirst(); cursor.moveToPrevious()){
            Note note = cursorToNote(cursor);
            notes.add(note);
        }

        //close our cursor required
        cursor.close();

        //return arrayList now filled with database notes or notes in our database
        return notes;
    }

    //give a cursor returns a note object
    private Note cursorToNote(Cursor cursor){
        Note newNote = new Note( cursor.getString(1), cursor.getString(2),
                Note.Category.valueOf(cursor.getString(3)), cursor.getLong(0), cursor.getLong(4));// Don't forget the "value of" in case of category

        return newNote;
    }

    private static class NotebookDBHelper extends SQLiteOpenHelper{

        NotebookDBHelper(Context ctx){
            super(ctx,DATABASE_NAME,null,DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db){
            db.execSQL(CREATE_TABLE_NOTE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
            Log.w(NotebookDBHelper.class.getName(),"Upgrading the database" + oldVersion + " to " + newVersion);
            db.execSQL("DROP TABLE IF EXISTS "+ NOTE_TABLE);
            onCreate(db);
        }
    }
}
