package com.hacks.reunite.Model;

public class Profile {
    private int id;
    private String name;
    private int age;
    private String contactNo;
    private String address;

    public Profile(int id, String name, int age, String contactNo, String address) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.contactNo = contactNo;
        this.address = address;
    }

    public int getId(){
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getContactNo() {
        return contactNo;
    }

    public String getAddress() {
        return address;
    }
}