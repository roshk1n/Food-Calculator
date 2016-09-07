package com.example.roshk1n.foodcalculator.rest.model.ndbApi.response;

public class ItemOfSearchFood {
    private int offset;
    private String group;
    private String name;
    private String ndbno;

    public ItemOfSearchFood() {}

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public String getGroup() { return group; }

    public void setGroup(String group) { this.group = group; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNdbno() {
        return ndbno;
    }

    public void setNdbno(String ndbno) {
        this.ndbno = ndbno;
    }
}
