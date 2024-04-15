/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.viettuts.qlbx.entity;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "vehicleBack")
@XmlAccessorType(XmlAccessType.FIELD)
/**
 *
 * @author kilcud
 */
public class VehicleBack extends Vehicle{
    private Date timeExit;
    private double fee;

    public Date getTimeExit() {
        return timeExit;
    }

    public void setTimeExit(Date timeExit) {
        this.timeExit = timeExit;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public VehicleBack() {
    }

    public VehicleBack(Date timeExit, double fee, int id, String parkingLot, String type, String bienSoXe, String brand, Date timeEnter) {
        super(id, parkingLot, type, bienSoXe, brand, timeEnter);
        this.timeExit = timeExit;
        this.fee = fee;
    }    
}
