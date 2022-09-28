package com.btw.dickies.model;

public class Scan {
    int scanID;
    String rack;
    String rowScan;
    String barcode;
    int lineNo;
    int qty;
    String scanBy;
    String dateTime;

    @Override
    public String toString() {
        return "Scan{" +
                "scanID=" + scanID +
                ", rack='" + rack + '\'' +
                ", rowScan='" + rowScan + '\'' +
                ", barcode='" + barcode + '\'' +
                ", lineNo=" + lineNo +
                ", qty=" + qty +
                ", scanBy='" + scanBy + '\'' +
                ", dateTime='" + dateTime + '\'' +
                '}';
    }

    public int getScanID() {
        return scanID;
    }

    public void setScanID(int scanID) {
        this.scanID = scanID;
    }

    public String getRack() {
        return rack;
    }

    public void setRack(String rack) {
        this.rack = rack;
    }

    public String getRowScan() {
        return rowScan;
    }

    public void setRowScan(String rowScan) {
        this.rowScan = rowScan;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public int getLineNo() {
        return lineNo;
    }

    public void setLineNo(int lineNo) {
        this.lineNo = lineNo;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getScanBy() {
        return scanBy;
    }

    public void setScanBy(String scanBy) {
        this.scanBy = scanBy;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
