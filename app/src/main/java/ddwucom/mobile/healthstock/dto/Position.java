package ddwucom.mobile.healthstock.dto;

public class Position {
//    private String kinds; //자세의 종류: 거북목(neck), 다리꼬기(leg), 허리(waist)
    private int minute;
    private int price; //계산한 결과값
    private int stockId; //이 자세가 속하는 주가의 id

    public Position() { }

    public Position(int price, int stockId, int minute) {
        this.price = price;
        this.stockId = stockId;
        this.minute = minute;
    }

    //    public String getKinds() {
//        return kinds;
//    }
//
//    public void setKinds(String kinds) {
//        this.kinds = kinds;
//    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStockId() {
        return stockId;
    }

    public void setStockId(int stockId) {
        this.stockId = stockId;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }
}
