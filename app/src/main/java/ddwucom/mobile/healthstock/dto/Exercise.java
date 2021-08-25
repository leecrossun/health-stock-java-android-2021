package ddwucom.mobile.healthstock.dto;

public class Exercise {
//    private String kinds; // run, walk, climb
    private int minute; // 운동 시간
    private int stocksId; // 단위 주가 ID (하루단위)
    private int price;

    public Exercise(int price, int stocksId, int minute) {
        this.price = price;
        this.stocksId = stocksId;
        this.minute = minute;
    }

    //    public String getKinds() {
//        return kinds;
//    }
//
//    public void setKinds(String kinds) {
//        this.kinds = kinds;
//    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getStocksId() {
        return stocksId;
    }

    public void setStocksId(int stocksId) {
        this.stocksId = stocksId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
