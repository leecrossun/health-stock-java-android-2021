package ddwucom.mobile.healthstock.dto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.SimpleFormatter;

public class Stocks {
    private int stocksId;
    private UserInfo userName;
    private int sharePrice;
    private Date date;

    public Stocks(int stocksId, UserInfo userName, int date, int sharePrice) {
        this.stocksId = stocksId;
        this.userName = userName;
        this.sharePrice = sharePrice;

        String d = String.valueOf(date);
        SimpleDateFormat format = new SimpleDateFormat("yyMMdd");
        try {
            this.date = new Date(format.parse(d).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public int getStocksId() {
        return stocksId;
    }

    public void setStocksId(int stocksId) {
        this.stocksId = stocksId;
    }

    public int getSharePrice() {
        return sharePrice;
    }

    public void setSharePrice(int sharePrice) {
        this.sharePrice = sharePrice;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public UserInfo getUserName() {
        return userName;
    }

    public void setUserName(UserInfo userName) {
        this.userName = userName;
    }
}
