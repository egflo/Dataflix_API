package com.moviedb_api.employee;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="EmpId")
    private int EmpID;

    @Column(name="Name")
    private String Name;

    @Column(name="UName")
    private String UName;

    @Column(name="Password")
    private String Password;

    @Column (name="UserLevel")
    private Integer UserLevel;

    public int getEmpID() {
        return EmpID;
    }

    public void setEmpID(int EmpID) {
        this.EmpID = EmpID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getUName() {
        return UName;
    }

    public void setUName(String UName) {
        this.UName = UName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    @Column (name="UserLevel")
    public Integer getUserLevel() {
        return UserLevel;
    }

    @Column (name="UserLevel")
    public void setUserLevel(Integer UserLevel) {
        this.UserLevel = UserLevel;
    }
}

