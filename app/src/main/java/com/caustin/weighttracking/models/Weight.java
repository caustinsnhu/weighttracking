package com.caustin.weighttracking.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Weight {

    private Integer id;
    private Date date;
    private float weight;
    SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");

    public Weight() {
    }

    public Weight(Integer id, Date date, float weight) {
        this.id = id;
        this.date = date;
        this.weight = weight;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public void setDateFromString(String date) {
        try {
            this.date = formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            this.date = Calendar.getInstance().getTime();
        }
    }

    public String getDateAsString() {
        return formatter.format(date);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
