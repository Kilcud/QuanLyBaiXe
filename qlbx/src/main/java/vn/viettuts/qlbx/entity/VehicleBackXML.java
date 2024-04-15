/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.viettuts.qlbx.entity;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "vehicleBacks")
@XmlAccessorType(XmlAccessType.FIELD)
/**
 *
 * @author pc
 */
public class VehicleBackXML {
    private List<VehicleBack> vehicleBack;

    public List<VehicleBack> getVehicleBack() {
        return vehicleBack;
    }

    public void setVehicleBack(List<VehicleBack> vehicleBack) {
        this.vehicleBack = vehicleBack;
    }
}
