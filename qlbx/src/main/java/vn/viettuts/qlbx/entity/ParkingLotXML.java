/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.viettuts.qlbx.entity;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ParkingLots")
@XmlAccessorType(XmlAccessType.FIELD)
/**
 *
 * @author kilcud
 */
public class ParkingLotXML {
    private List<ParkingLot> parkingLot;

    public List<ParkingLot> getParkingLot() {
        return parkingLot;
    }

    public void setParkingLot(List<ParkingLot> _parkingLot) {
        this.parkingLot = _parkingLot;
    }
}


