package com.example.roshk1n.foodcalculator.rest.model.ndbApi.response;

import java.util.ArrayList;

public class List {

    private String q;
    private String sr;
    private int start;
    private int end;
    private int total;
    private String group;
    private String sort;
    private ArrayList<ItemOfSearchFood> item = new ArrayList<>();

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public String getSr() {
        return sr;
    }

    public void setSr(String sr) {
        this.sr = sr;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public ArrayList<ItemOfSearchFood> getItem() {
        return item;
    }

    public void setItem(ArrayList<ItemOfSearchFood> item) {
        this.item = item;
    }
}
