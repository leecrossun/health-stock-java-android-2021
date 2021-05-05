package ddwucom.mobile.healthstock.dto;

public class Exercise {
    String kinds; // run, walk, climb
    int minute; // 운동 시간
    String stocksId; // 단위 주가 ID (하루단위)

    public String getKinds() {
        return kinds;
    }

    public void setKinds(String kinds) {
        this.kinds = kinds;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public String getStocksId() {
        return stocksId;
    }

    public void setStocksId(String stocksId) {
        this.stocksId = stocksId;
    }
}
