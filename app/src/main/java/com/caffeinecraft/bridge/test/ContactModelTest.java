package com.caffeinecraft.bridge.test;

import com.caffeinecraft.bridge.model.Contact;

import junit.framework.TestCase;

public class ContactModelTest extends TestCase {
    Contact contact;
    public void setUp() {
        contact = new Contact();
    }

    public void testId() {
        int id = 45;
        contact.setId(id);
        assertEquals(contact.getId(), id);
    }
}
