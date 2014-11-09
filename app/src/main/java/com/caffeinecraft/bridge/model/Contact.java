package com.caffeinecraft.bridge.model;

import java.util.List;

public class Contact {
    private long id;
    private String firstName;
    private String lastName;
    private List<String> emails;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String name) {
        this.firstName = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String name) {
        this.lastName = name;
    }

    public void addEmail(String email) {
        emails.add(email);
    }

    public String[] getEmails() {
        return (String[])emails.toArray();
    }

    @Override
    public String toString() {
        if(lastName != null)
            return firstName + " " + lastName;

        return firstName;
    }
}
