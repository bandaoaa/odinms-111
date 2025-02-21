package server;

public class MapleShopItem {

    private short buyable;
    private int itemId;
    private int price;
    private int reqItem;
    private int reqItemQ;
    private byte rank;
    private byte tab;
    private int period;
    private int potential;

    public MapleShopItem(int itemId, int price, short buyable) {
        this.buyable = buyable;
        this.itemId = itemId;
        this.price = price;
        this.reqItem = 0;
        this.reqItemQ = 0;
        this.rank = (byte) 0;
        this.tab = (byte) 0;
        this.period = 0;
        this.potential = 0;
    }

    public MapleShopItem(short buyable, int itemId, int price, int reqItem, int reqItemQ, byte rank, byte tab, int period, int potential) {
        this.buyable = buyable;
        this.itemId = itemId;
        this.price = price;
        this.reqItem = reqItem;
        this.reqItemQ = reqItemQ;
        this.rank = rank;
        this.tab = tab;
        this.period = period;
        this.potential = potential;
    }

    public short getBuyable() {
        return buyable;
    }

    public int getItemId() {
        return itemId;
    }

    public int getPrice() {
        return price;
    }

    public int getReqItem() {
        return reqItem;
    }

    public int getReqItemQ() {
        return reqItemQ;
    }

    public byte getRank() {
        return rank;
    }

    public int getPotential() {
        return potential;
    }

    public byte getTab() {
        return tab;
    }

    public int getPeriod() {
        return period;
    }
}