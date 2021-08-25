package ddwucom.mobile.healthstock.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

import ddwucom.mobile.healthstock.HealthStocksDBHelper;
import ddwucom.mobile.healthstock.UserActivity;
import ddwucom.mobile.healthstock.dto.Exercise;
import ddwucom.mobile.healthstock.dto.Position;
import ddwucom.mobile.healthstock.dto.Stocks;
import ddwucom.mobile.healthstock.dto.UserInfo;

public class HealthStocksDAO {

    HealthStocksDBHelper helper = null;
    Cursor cursor = null;
    private final int count = 5;

    public HealthStocksDAO(Context context) { helper = new HealthStocksDBHelper(context); }

    //userinfo 반환
    public UserInfo getUserInfo(String userName) {
        UserInfo userInfo = null;
        SQLiteDatabase db = helper.getReadableDatabase();
        cursor = db.rawQuery("select * from " + HealthStocksDBHelper.TABLE_USERINFO
                + " where " + HealthStocksDBHelper.COL_USERNAME + "=" + userName, null);

        if (cursor.moveToNext()) {
            userInfo = new UserInfo(
                    userName,
                    cursor.getInt(cursor.getColumnIndex(HealthStocksDBHelper.COL_POINT)),
                    cursor.getInt(cursor.getColumnIndex(HealthStocksDBHelper.COL_HEIGHT)),
                    cursor.getInt(cursor.getColumnIndex(HealthStocksDBHelper.COL_WEIGHT))
            );
        }
        cursor.close();
        helper.close();
        return userInfo;
    }

    //stock list 반환
    public ArrayList<Stocks> getAllStocks(String userName) {
        ArrayList<Stocks> stocksList = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        cursor = db.rawQuery("select * from " + HealthStocksDBHelper.TABLE_STOCKS
                + " where " + HealthStocksDBHelper.COL_USERNAME + "='" + userName
                + "' order by " + HealthStocksDBHelper.COL_ID + " desc limit 5", null);

        while (cursor.moveToNext()) {
            stocksList.add(new Stocks(
                    cursor.getInt(cursor.getColumnIndex(HealthStocksDBHelper.COL_ID)),
                    userName,
                    cursor.getInt(cursor.getColumnIndex(HealthStocksDBHelper.COL_DATE)),
                    cursor.getInt(cursor.getColumnIndex(HealthStocksDBHelper.COL_SHAREPRICE))
            ));
        }
        Collections.reverse(stocksList);

        cursor.close();
        helper.close();
        return stocksList;
    }

    //stock 반환
    public Stocks getStocks(int id) {
        Stocks stocks = null;
        SQLiteDatabase db = helper.getReadableDatabase();
        cursor = db.rawQuery("select * from " + HealthStocksDBHelper.TABLE_STOCKS
                + " where " + HealthStocksDBHelper.COL_ID + "=" + id, null);

        if (cursor.moveToNext()) {
            stocks = new Stocks(
                    id,
                    cursor.getString(cursor.getColumnIndex(HealthStocksDBHelper.COL_DATE)),
                    cursor.getInt(cursor.getColumnIndex(HealthStocksDBHelper.COL_DATE)),
                    cursor.getInt(cursor.getColumnIndex(HealthStocksDBHelper.COL_SHAREPRICE))
            );
        }

        cursor.close();
        helper.close();
        return stocks;
    }

    //position 반환
    public Position getPosition(int sId) {
        Position position = null;
        SQLiteDatabase db = helper.getReadableDatabase();

        cursor = db.rawQuery("select * from " + HealthStocksDBHelper.TABLE_HEALTH
                + " where " + HealthStocksDBHelper.COL_STOCKSID + "=" + sId, null);
        while (cursor.moveToNext()) {
            String type = cursor.getString(cursor.getColumnIndex(HealthStocksDBHelper.COL_TYPE));
            int stocksId = cursor.getInt(cursor.getColumnIndex(HealthStocksDBHelper.COL_STOCKSID));
            if (type.equals("position")) {
                position = new Position(
                        cursor.getInt(cursor.getColumnIndex(HealthStocksDBHelper.COL_RESULT)),
                        stocksId,
                        cursor.getInt(cursor.getColumnIndex(HealthStocksDBHelper.COL_MINUTE))
                );
            }
        }

        cursor.close();
        helper.close();
        return position;
    }

    //exercise 반환
    public Exercise getExercise(int sId) {
        Exercise exercise = null;
        SQLiteDatabase db = helper.getReadableDatabase();

        cursor = db.rawQuery("select * from " + HealthStocksDBHelper.TABLE_HEALTH
                + " where " + HealthStocksDBHelper.COL_STOCKSID + "=" + String.valueOf(sId), null);
        while (cursor.moveToNext()) {
            String type = cursor.getString(cursor.getColumnIndex(HealthStocksDBHelper.COL_TYPE));
            int stocksId = cursor.getInt(cursor.getColumnIndex(HealthStocksDBHelper.COL_STOCKSID));
            if (type.equals("exercise")) {
                exercise = new Exercise(
                        cursor.getInt(cursor.getColumnIndex(HealthStocksDBHelper.COL_RESULT)),
                        stocksId,
                        cursor.getInt(cursor.getColumnIndex(HealthStocksDBHelper.COL_MINUTE))
                );
            }
        }

        cursor.close();
        helper.close();
        return exercise;
    }

    public boolean saveOrUpdate(Position position, int point) {
        // stock id && type에 해당하는 health가 있는지 보고 있으면 update 없으면 새로 save
        SQLiteDatabase db = helper.getWritableDatabase();
        cursor = db.rawQuery("select * from " + HealthStocksDBHelper.TABLE_HEALTH
                + " where " + HealthStocksDBHelper.COL_STOCKSID + "=" + position.getStockId()
                + " and " + HealthStocksDBHelper.COL_TYPE + "='position'", null);

        ContentValues value = new ContentValues();
        value.put(HealthStocksDBHelper.COL_TYPE, "position");
        value.put(HealthStocksDBHelper.COL_MINUTE, position.getMinute());
        value.put(HealthStocksDBHelper.COL_RESULT, position.getPrice());
        value.put(HealthStocksDBHelper.COL_STOCKSID, position.getStockId());

        int result = -1; long count = -1;
        if (cursor.moveToNext()) { //있으면
            int id = cursor.getInt(cursor.getColumnIndex(HealthStocksDBHelper.COL_ID));
            String whereClause = HealthStocksDBHelper.COL_ID + "=?";
            String[] whereArgs = new String[] {String.valueOf(id)};
            result = db.update(HealthStocksDBHelper.TABLE_HEALTH, value, whereClause, whereArgs);
        } else { //없으면
            count = db.insert(HealthStocksDBHelper.TABLE_HEALTH, null, value);
        }

        helper.close();
        cursor.close();
        if (count > 0 || result > 0) {
            updateStock(getStocks(position.getStockId()), point);
            return true;
        }
        return false;
    }

    public boolean saveOrUpdate(Exercise exercise, int step_to_point) {
        // stock id && type에 해당하는 health가 있는지 보고 있으면 update 없으면 새로 save
        SQLiteDatabase db = helper.getWritableDatabase();
        cursor = db.rawQuery("select * from " + HealthStocksDBHelper.TABLE_HEALTH
                + " where " + HealthStocksDBHelper.COL_STOCKSID + "=" + exercise.getStocksId()
                + " and " + HealthStocksDBHelper.COL_TYPE + "='exercise'", null);

        ContentValues value = new ContentValues();
        value.put(HealthStocksDBHelper.COL_TYPE, "exercise");
        value.put(HealthStocksDBHelper.COL_MINUTE, exercise.getMinute());
        value.put(HealthStocksDBHelper.COL_RESULT, exercise.getPrice());
        value.put(HealthStocksDBHelper.COL_STOCKSID, exercise.getStocksId());

        int result = -1; long count = -1;
        if (cursor.moveToNext()) { //있으면
            int id = cursor.getInt(cursor.getColumnIndex(HealthStocksDBHelper.COL_ID));
            String whereClause = HealthStocksDBHelper.COL_ID + "=?";
            String[] whereArgs = new String[] {String.valueOf(id)};
            result = db.update(HealthStocksDBHelper.TABLE_HEALTH, value, whereClause, whereArgs);
        } else { //없으면
            count = db.insert(HealthStocksDBHelper.TABLE_HEALTH, null, value);
        }

        helper.close();
        cursor.close();
        if (count > 0 || result > 0) {
            updateStock(getStocks(exercise.getStocksId()), step_to_point);
            return true;
        }
        return false;
    }

    //stock을 udpate하는데 result에 exercise.getPrice()를 더해서 update
    public boolean updateStock(Stocks stocks, int point) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(HealthStocksDBHelper.COL_SHAREPRICE, stocks.getSharePrice() + point);

        String whereClause = HealthStocksDBHelper.COL_ID + "=?";
        String[] whereArgs = new String[] {String.valueOf(stocks.getStocksId())};
        int result = db.update(HealthStocksDBHelper.TABLE_STOCKS, value, whereClause, whereArgs);
        helper.close();
        if (result > 0) return true;
        return false;
    }

    public int getTodayStockId(String username, int date) {
        //오늘 날짜에 해당하는 stock이 있는지 보고 있으면 id return, 없으면 만들고 id return
        SQLiteDatabase db = helper.getWritableDatabase();
        cursor = db.rawQuery("select " + HealthStocksDBHelper.COL_ID + " from " + HealthStocksDBHelper.TABLE_STOCKS
                + " where " + HealthStocksDBHelper.COL_DATE + "=" + date, null);

        if (cursor.moveToNext()) { //있으면
            return cursor.getInt(cursor.getColumnIndex(HealthStocksDBHelper.COL_ID));
        }
        cursor.close();

        //없으면
        ContentValues value = new ContentValues();
        value.put(HealthStocksDBHelper.COL_USERNAME, username);
        value.put(HealthStocksDBHelper.COL_DATE, date);
        value.put(HealthStocksDBHelper.COL_SHAREPRICE, getRecentShareprice(db));

        long count = db.insert(HealthStocksDBHelper.TABLE_STOCKS, null, value);
        helper.close();
        return Integer.valueOf((int) count); //실패 시 -1 반환
    }


    //가장 최근 stock의 shareprice 반환
    public int getRecentShareprice(SQLiteDatabase db) {
        cursor = db.rawQuery("select " + HealthStocksDBHelper.COL_SHAREPRICE + " from " + HealthStocksDBHelper.TABLE_STOCKS
                + " order by " + HealthStocksDBHelper.COL_SHAREPRICE + " desc limit 1", null);

        if (cursor.moveToNext()) {
            return cursor.getInt(cursor.getColumnIndex(HealthStocksDBHelper.COL_SHAREPRICE));
        }
        cursor.close();

        //아예 없으면
        return 0;
    }
}
