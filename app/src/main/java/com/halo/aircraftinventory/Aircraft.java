package com.halo.aircraftinventory;

public class Aircraft {
    public static final String TABLE_NAME = "aircraft";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_MODEL = "model";
    public static final String COLUMN_PRODUCER = "producer";
    public static final String COLUMN_STATUS = "currentStatus";
    public static final String COLUMN_PILOT = "pilot";

    private int id;
    private String model;
    private String producer;
    private String currentStatus;
    private String pilot;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public String getPilot() {
        return pilot;
    }

    public void setPilot(String pilot) {
        this.pilot = pilot;
    }
}

