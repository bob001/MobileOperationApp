package com.swdn.model_xunjian;

import java.io.Serializable;

/**
 * Created by lenovo on 2016/10/7.
 */

public class TourInspectionTask implements Serializable{
    private int id;
    private String category;
    private String writePerson;
    private String execPerson;
    private String execDate;
    private int isFinished;
    private String dept;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getWritePerson() {
        return writePerson;
    }

    public void setWritePerson(String writePerson) {
        this.writePerson = writePerson;
    }

    public String getExecPerson() {
        return execPerson;
    }

    public void setExecPerson(String execPerson) {
        this.execPerson = execPerson;
    }

    public String getExecDate() {
        return execDate;
    }

    public void setExecDate(String execDate) {
        this.execDate = execDate;
    }

    public int getIsFinished() {
        return isFinished;
    }

    public void setIsFinished(int isFinished) {
        this.isFinished = isFinished;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }
}
