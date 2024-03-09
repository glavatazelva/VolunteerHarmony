package com.example.pericinprojektnizadatak.model.generics;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Changes<T1 , T2> implements Serializable {
    private String role;
    private LocalDateTime dateTimeOfChange;
    T1 before;
    T2 after;

    public Changes(String role, LocalDateTime dateTimeOfChange, T1 before, T2 after) {
        this.role = role;
        this.dateTimeOfChange = dateTimeOfChange;
        this.before = before;
        this.after = after;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDateTime getDateTimeOfChange() {
        return dateTimeOfChange;
    }

    public void setDateTimeOfChange(LocalDateTime dateTimeOfChange) {
        this.dateTimeOfChange = dateTimeOfChange;
    }

    public T1 getBefore() {
        return before;
    }

    public void setBefore(T1 before) {
        this.before = before;
    }

    public T2 getAfter() {
        return after;
    }

    public void setAfter(T2 after) {
        this.after = after;
    }
}