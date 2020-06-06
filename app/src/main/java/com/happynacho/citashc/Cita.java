package com.happynacho.citashc;

public class Cita {
    private String id;
    private String date;
    private String description;
    public Cita(){

    }
    public Cita(String id,String date,String description){
        this.id=id;
        this.date=date;
        this.description=description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
