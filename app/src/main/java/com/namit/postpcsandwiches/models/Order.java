package com.namit.postpcsandwiches.models;

import com.namit.postpcsandwiches.OrderStatus;
import java.io.Serializable;
import java.util.UUID;

public class Order implements Serializable {
    String id;
    String customerName;
    int pickles;
    boolean hummus;
    boolean tahini;
    String comment;
    OrderStatus status;
    private transient OnOrderChangedListener picklesChangeListener;

    public Order() {
        this("", 0, true, true, "");
    }

    public Order(String customerName, int pickles, boolean hummus, boolean tahini, String comment) {
        this.id = UUID.randomUUID().toString();
        this.status = OrderStatus.WAITING;
        this.customerName = customerName;
        this.pickles = pickles;
        this.hummus = hummus;
        this.tahini = tahini;
        this.comment = comment;
    }

    public Order(Order copy) {
        this.id = copy.id;
        this.status = copy.status;
        this.customerName = copy.customerName;
        this.pickles = copy.pickles;
        this.hummus = copy.hummus;
        this.tahini = copy.tahini;
        this.comment = copy.comment;
    }


    public void setOnPicklesChangedListener(OnOrderChangedListener listener) {
        this.picklesChangeListener = listener;
    }

    public String getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public int getPickles() {
        return pickles;
    }

    public boolean isHummus() {
        return hummus;
    }

    public boolean isTahini() {
        return tahini;
    }

    public String getComment() {
        return comment;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void incrementPickles() {
        if (pickles == 10) return;
        pickles++;
        if (this.picklesChangeListener != null) {
            picklesChangeListener.onChange();
        }
    }

    public void decrementPickles() {
        if (pickles == 0) return;
        pickles--;
        if (this.picklesChangeListener != null) {
            picklesChangeListener.onChange();
        }
    }

    public void setHummus(boolean hummus) {
        this.hummus = hummus;
    }

    public void setTahini(boolean tahini) {
        this.tahini = tahini;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

