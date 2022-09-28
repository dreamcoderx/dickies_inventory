package com.btw.dickies.model;

public class ModelScan {
    int modelID;
    String modelNo;
    String processLocation;
    String processKanban;

    public ModelScan(){

    }
    public ModelScan(String modelNo, String processLocation, String processKanban) {
        this.modelNo = modelNo;
        this.processLocation = processLocation;
        this.processKanban = processKanban;
    }

    public int getModelID() {
        return this.modelID;
    }

    public void setModelID(int modelID) {
        this.modelID = modelID;
    }

    public String getModelNo() {
        return modelNo;
    }

    public void setModelNo(String modelNo) {
        this.modelNo = modelNo;
    }

    public String getProcessLocation() {
        return processLocation;
    }

    public void setProcessLocation(String processLocation) {
        this.processLocation = processLocation;
    }

    public String getProcessKanban() {
        return processKanban;
    }

    public void setProcessKanban(String processKanban) {
        this.processKanban = processKanban;
    }


}
