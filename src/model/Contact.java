package model;

public class Contact {

    private int contactId;
    private String name;
    private String email;

    public Contact(int contactId, String name, String email) {
        this.contactId = contactId;
        this.name = name;
        this.email = email;
    }

    @Override
    public String toString() {
        return name;
    }

    public int getContactId() {
        return contactId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
