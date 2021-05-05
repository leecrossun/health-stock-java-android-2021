package ddwucom.mobile.healthstock.dto;

import java.util.Date;

public class Stocks {
    private int stocksId;
    private int sharePrice;
    private Date date;
    private UserInfo userName;

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
