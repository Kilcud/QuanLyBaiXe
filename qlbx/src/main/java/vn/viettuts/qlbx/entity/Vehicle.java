/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.viettuts.qlbx.entity;

import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "vehicle")
@XmlAccessorType(XmlAccessType.FIELD)

/**
 *
 * @author kilcud
 */
public class Vehicle {
    private int id;
    private String parkingLot;
    private String type;
    private String bienSoXe;
    private String brand;
    private Date timeEnter;

    public Vehicle() {
    }

    public Vehicle(int id, String parkingLot, String type, String bienSoXe, String brand, Date timeEnter) {
        this.id = id;
        this.parkingLot = parkingLot;
        this.type = type;
        this.bienSoXe = bienSoXe;
        this.brand = brand;
        this.timeEnter = timeEnter;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getParkingLot() {
        return parkingLot;
    }

    public void setParkingLot(String parkingLot) {
        this.parkingLot = parkingLot;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBienSoXe() {
        return bienSoXe;
    }

    public void setBienSoXe(String bienSoXe) {
        this.bienSoXe = bienSoXe;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Date getTimeEnter() {
        return timeEnter;
    }

    public void setTimeEnter(Date timeEnter) {
        this.timeEnter = timeEnter;
    }
}
