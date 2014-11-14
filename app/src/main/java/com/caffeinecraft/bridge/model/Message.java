package com.caffeinecraft.bridge.model;

public class Message {
    private boolean received;
    private String text;
    private long timestamp;

    public boolean wasReceived() {
        return received;
    }

    public void setReceived(boolean received) {
        this.received = received;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
