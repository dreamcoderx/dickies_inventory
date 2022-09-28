package com.btw.dickies.model;

public class ScanTxn {
    int scan_id, qty;
    String barcode, date_time, scan_by;

    public ScanTxn() {
    }


    public String getScan_by() {
        return scan_by;
    }

    public void setScan_by(String scan_by) {
        this.scan_by = scan_by;
    }

    public int getScan_id() {
        return scan_id;
    }

    public void setScan_id(int scan_id) {
        this.scan_id = scan_id;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }


    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }


    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }
}
