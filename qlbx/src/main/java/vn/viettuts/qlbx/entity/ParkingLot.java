/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.viettuts.qlbx.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ParkingLot")
@XmlAccessorType(XmlAccessType.FIELD)
/**
 *
 * @author kilcud
 */
public class ParkingLot {
    private int idParkingLot;
    private int sucChua;
    private int soXeDangChua;
   
    private String name;

    public ParkingLot() {
    }

    public ParkingLot(int idParkingLot, int sucChua, int soXeDangChua, String name) {
        this.idParkingLot = idParkingLot;
        this.sucChua = sucChua;
        this.soXeDangChua = soXeDangChua;
        this.name = name;
    }

    public int getIdParkingLot() {
        return idParkingLot;
    }

    public void setIdParkingLot(int idParkingLot) {
        this.idParkingLot = idParkingLot;
    }
   
    public int getSucChua() {
        return sucChua;
    }

    public void setSucChua(int sucChua) {
        this.sucChua = sucChua;
    }

    public int getSoXeDangChua() {
        return soXeDangChua;
    }

    public void setSoXeDangChua(int soXeDangChua) {
        this.soXeDangChua = soXeDangChua;
    }

    public String getName() {
        return "Bãi " + Integer.toString(idParkingLot);
    }

    public void setName(String name) {
        this.name = name;
    }

    
}


