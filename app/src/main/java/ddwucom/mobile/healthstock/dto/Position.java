package ddwucom.mobile.healthstock.dto;

public class  Position {
    private String kinds; //자세의 종류: 거북목(neck), 다리꼬기(leg), 허리(waist)
    private int price; //계산한 결과값
    private int stockId; //이 자세가 속하는 주가의 id

    public Position() {
    }

    public String getKinds() {
        return kinds;
    }

    public void setKinds(String kinds) {
        this.kinds = kinds;
    }

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
}
