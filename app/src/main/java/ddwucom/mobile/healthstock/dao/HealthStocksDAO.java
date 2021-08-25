package ddwucom.mobile.healthstock.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;

import ddwucom.mobile.healthstock.HealthStocksDBHelper;
import ddwucom.mobile.healthstock.dto.Stocks;
import ddwucom.mobile.healthstock.dto.UserInfo;

public class HealthStocksDAO {

    HealthStocksDBHelper healthStocksDBHelper = null;
    Cursor cursor = null;

    public HealthStocksDAO(Context context) { healthStocksDBHelper = new HealthStocksDBHelper(context); }

    //stock list 반환
    public ArrayList<Stocks> getAllStocks(UserInfo userInfo) {
        ArrayList<Stocks> stocksList = new ArrayList<>();
        SQLiteDatabase db = healthStocksDBHelper.getReadableDatabase();
        cursor = db.rawQuery("select * from " + HealthStocksDBHelper.TABLE_STOCKS
                + " where " + HealthStocksDBHelper.COL_USERNAME + "='" + userInfo.getUserName()
                + "' order by " + HealthStocksDBHelper.COL_ID + " desc limit 5", null);

        while (cursor.moveToNext()) {
            stocksList.add(new Stocks(
                    cursor.getInt(cursor.getColumnIndex(HealthStocksDBHelper.COL_ID)),
                    userInfo,
                    cursor.getInt(cursor.getColumnIndex(HealthStocksDBHelper.COL_DATE)),
                    cursor.getInt(cursor.getColumnIndex(HealthStocksDBHelper.COL_SHAREPRICE))
            ));
        }
        Collections.reverse(stocksList);
        return stocksList;
    }

    //position list 반환


    //exercise list 반환


    //오늘 날짜에 해당하는 stock이 있는지 보고 있으면 id return, 없으면 만들고 id return


    //가장 최근 stock의 shareprice 반환


    // stock id && type에 해당하는 health가 있는지 보고 있으면 update 없으면 새로 save
}
