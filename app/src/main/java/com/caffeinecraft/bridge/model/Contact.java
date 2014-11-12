package com.caffeinecraft.bridge.model;

import java.util.ArrayList;
import java.util.List;

public class Contact {
    private long id;
    private String firstName;
    private String lastName;
    private List<String> emails;
    private List<ContactMethod> methods;

    public Contact() {
        methods = new ArrayList<ContactMethod>();
        emails = new ArrayList<String>();
    }

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

    @Deprecated
    public void addEmail(String email) {
        emails.add(email);
    }

    @Deprecated
    public String[] getEmails() {
        //return (String[])emails.toArray();
        if(emails.get(0)==null)return new String[0];
        String[] temp = new String[emails.size()];
        return emails.toArray(temp);
    }

    //TODO:
    public void setPreferredContactMethod(ContactMethod method) {

    }

    //TODO:
    public ContactMethod getPreferredContactMethod() {
        return methods.get(0);
    }

    public List<ContactMethod> getContactMethods() {
        return methods;
    }

    public void addContactMethod(ContactMethod method) {
        methods.add(method);
    }

    public void removeContactMethod(ContactMethod method)
    {
        methods.remove(methods.indexOf(method));
    }

    @Override
    public String toString() {
        if(lastName != null)
            return firstName + " " + lastName;

        return firstName;
    }
}
