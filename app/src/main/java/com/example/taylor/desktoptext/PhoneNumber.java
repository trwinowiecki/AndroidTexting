package com.example.taylor.desktoptext;

public class PhoneNumber {
    private String number;
    private int count;

    public PhoneNumber(String number, int count) {
        this.number = number;
        this.count = count;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
