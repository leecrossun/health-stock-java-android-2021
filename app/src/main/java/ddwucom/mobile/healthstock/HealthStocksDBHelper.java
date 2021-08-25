package ddwucom.mobile.healthstock;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class HealthStocksDBHelper extends SQLiteOpenHelper {

    private final static String DB_NAME = "health_stocks_db";

    //commom
    public final static String COL_ID = "_id";
    public final static String COL_USERNAME = "userName";

    public final static String TABLE_USERINFO = "UserInfo";
    public final static String COL_POINT = "point";
    public final static String COL_HEIGHT = "height";
    public final static String COL_WEIGHT = "weight";
    public final static String COL_AGE = "age";

    public final static String TABLE_STOCKS = "Stocks";
    public final static String COL_DATE = "date"; //yyddmm
    public final static String COL_SHAREPRICE = "sharePrice";

    public final static String TABLE_HEALTH = "Health";
    public final static String COL_TYPE = "type";
    public final static String COL_MINUTE = "minute";
    public final static String COL_RESULT = "result";
    public final static String COL_STOCKSID = "stocksId";

    public HealthStocksDBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    public HealthStocksDBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_USERINFO + " ( " + COL_USERNAME + " TEXT primary key,"
                + COL_POINT + " integer, " + COL_HEIGHT + " real, " + COL_WEIGHT + " real, " + COL_AGE + " integer);");

        db.execSQL("create table " + TABLE_STOCKS + " ( " + COL_ID + " integer primary key autoincrement,"
                + COL_USERNAME + " TEXT, " + COL_DATE + " integer, " + COL_SHAREPRICE + " integer, "
                + "foreign key(" + COL_USERNAME + ") REFERENCES " + TABLE_USERINFO + "(" + COL_USERNAME + "));");

        db.execSQL("create table " + TABLE_HEALTH + " ( " + COL_ID + " integer primary key autoincrement,"
                + COL_TYPE + " TEXT, " + COL_MINUTE + " integer, " + COL_RESULT + " integer, " + COL_STOCKSID + " integer, "
                + "foreign key(" + COL_STOCKSID + ") REFERENCES " + TABLE_STOCKS + "(" + COL_ID + "));");

        //샘플 데이터
        //db.execSQL("INSERT INTO " + TABLE_USERINFO + " VALUES ('name1', 5000, 160, 55, 20);");
        db.execSQL("INSERT INTO " + TABLE_USERINFO + " VALUES ('name2', 6000, 155, 50, 25);");
       // db.execSQL("INSERT INTO " + TABLE_USERINFO + " VALUES ('name3', 7000, 165, 65, 23);");

        /*db.execSQL("INSERT INTO " + TABLE_STOCKS + " VALUES (null, 'name1', 210819, 7800);");
        db.execSQL("INSERT INTO " + TABLE_STOCKS + " VALUES (null, 'name1', 210820, 8000);");
        db.execSQL("INSERT INTO " + TABLE_STOCKS + " VALUES (null, 'name1', 210821, 8500);");
        db.execSQL("INSERT INTO " + TABLE_STOCKS + " VALUES (null, 'name1', 210822, 9500);");
        db.execSQL("INSERT INTO " + TABLE_STOCKS + " VALUES (null, 'name1', 210823, 9800);");*/

        db.execSQL("INSERT INTO " + TABLE_STOCKS + " VALUES (null, 'name2', 210815, 2000);");
        db.execSQL("INSERT INTO " + TABLE_STOCKS + " VALUES (null, 'name2', 210816, 2300);");
        db.execSQL("INSERT INTO " + TABLE_STOCKS + " VALUES (null, 'name2', 210820, 3000);");
        db.execSQL("INSERT INTO " + TABLE_STOCKS + " VALUES (null, 'name2', 210822, 4700);");
        db.execSQL("INSERT INTO " + TABLE_STOCKS + " VALUES (null, 'name2', 210823, 6000);");

        db.execSQL("INSERT INTO " + TABLE_HEALTH + " VALUES (null, 'position', 4, 20, 1);");
        db.execSQL("INSERT INTO " + TABLE_HEALTH + " VALUES (null, 'exercise', 5, 30, 1);");
        db.execSQL("INSERT INTO " + TABLE_HEALTH + " VALUES (null, 'position', 22, 130, 2);");
        db.execSQL("INSERT INTO " + TABLE_HEALTH + " VALUES (null, 'exercise', 10, 70, 2);");
        db.execSQL("INSERT INTO " + TABLE_HEALTH + " VALUES (null, 'exercise', 40, 500, 3);");
        db.execSQL("INSERT INTO " + TABLE_HEALTH + " VALUES (null, 'position', 60, 1000, 4);");
        db.execSQL("INSERT INTO " + TABLE_HEALTH + " VALUES (null, 'position', 10, 100, 5);");
        db.execSQL("INSERT INTO " + TABLE_HEALTH + " VALUES (null, 'exercise', 35, 200, 5);");

       /*db.execSQL("INSERT INTO " + TABLE_HEALTH + " VALUES (null, 'position', 40, 800, 6);");
        db.execSQL("INSERT INTO " + TABLE_HEALTH + " VALUES (null, 'exercise', 40, 500, 6);");
        db.execSQL("INSERT INTO " + TABLE_HEALTH + " VALUES (null, 'position', 35, 400, 7);");
        db.execSQL("INSERT INTO " + TABLE_HEALTH + " VALUES (null, 'exercise', 45, 300, 7);");
        db.execSQL("INSERT INTO " + TABLE_HEALTH + " VALUES (null, 'position', 30, 200, 8);");
        db.execSQL("INSERT INTO " + TABLE_HEALTH + " VALUES (null, 'exercise', 45, 300, 8);");
        db.execSQL("INSERT INTO " + TABLE_HEALTH + " VALUES (null, 'position', 40, 1200, 9);");
        db.execSQL("INSERT INTO " + TABLE_HEALTH + " VALUES (null, 'exercise', 10, 500, 9);");
        db.execSQL("INSERT INTO " + TABLE_HEALTH + " VALUES (null, 'position', 20, 300, 10);");
        db.execSQL("INSERT INTO " + TABLE_HEALTH + " VALUES (null, 'exercise', 40, 1000, 10);");*/

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STOCKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERINFO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HEALTH);

        onCreate(db);
    }
}
