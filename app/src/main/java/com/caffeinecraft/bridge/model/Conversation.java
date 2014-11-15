package com.caffeinecraft.bridge.model;

import java.util.ArrayList;
import java.util.List;

public class Conversation {
    private Contact contact;
    private List<Message> messages;

    public Conversation() {
        messages = new ArrayList<Message>();
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void addMessage(Message message) {
        messages.add(message);
    }

    @Override
    public String toString() {
        return contact.getFirstName() + " " + contact.getLastName();
    }
}
