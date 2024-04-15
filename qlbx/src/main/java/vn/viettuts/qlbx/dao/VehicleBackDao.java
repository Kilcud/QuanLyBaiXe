/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.viettuts.qlbx.dao;

import java.util.*;

import vn.viettuts.qlbx.entity.*;
import vn.viettuts.qlbx.utils.FileUtils;

/**
 *
 * @author kilcud
 */
public class VehicleBackDao {
    private static final String VEHICLE_BACK_FILE_NAME = "vehicleBack.xml";
    private List<VehicleBack> listVehicleBacks;
    private ParkingLotDao parkingLotDao;
    //private StatisticDao statisticDao;
    
    public VehicleBackDao(){
        //statisticDao = new StatisticDao();
        this.listVehicleBacks = readListVehicleBacks();
        if (listVehicleBacks == null) {
            listVehicleBacks = new ArrayList<>();
        }
    }
    
     /**
     * Đọc các đối tượng vehicle từ file vehicleBack.xml
     * 
     * @return list vehicle
     */
    public List<VehicleBack> readListVehicleBacks() {
        List<VehicleBack> listBack = new ArrayList<>();
        VehicleBackXML vehicleBackXML = (VehicleBackXML) FileUtils.readXMLFile(
                VEHICLE_BACK_FILE_NAME, VehicleBackXML.class);
        if (vehicleBackXML != null) {
            listBack = vehicleBackXML.getVehicleBack();
        }
        return listBack;
    }
    
    /**
     * Lưu các đối tượng vehicle vào file vehicleBack.xml
     * 
     * @param vehicleBacks
     */
    public void writeListVehicleBacks(List<VehicleBack> vehicleBacks) {
        VehicleBackXML vehicleBackXML = new VehicleBackXML();
        vehicleBackXML.setVehicleBack(vehicleBacks);
        FileUtils.writeXMLtoFile(VEHICLE_BACK_FILE_NAME, vehicleBackXML);
    }

    /**
     * thêm vehicle vào listVehicles và lưu listVehicles vào file
     * 
     * @param vehicleBack
     */
    public void add(VehicleBack vehicleBack) {
        int id = 1;
        
        List<Boolean> used = new ArrayList<>(Collections.nCopies(100005, false));
        
        for(int i=0; i < listVehicleBacks.size(); i++){
            int index = listVehicleBacks.get(i).getId();
            used.set(index, true);
        }
        for(int i = 1; i < used.size(); i++){
            if(!used.get(i)){
                id = i;
                break;
            }
        }
        vehicleBack.setId(id);
        listVehicleBacks.add(vehicleBack);
        writeListVehicleBacks(listVehicleBacks);
    }

    public List<VehicleBack> getListVehicleBacks() {
        return listVehicleBacks;
    }

    public void setListVehicleBacks(List<VehicleBack> listVehicleBacks) {
        this.listVehicleBacks = listVehicleBacks;
    }
    
    /**
    *  sắp xếp danh sách vehicleBack theo ID xe theo tứ tự tăng dần
    */
    public void sortVehicleBackByID() {
        Collections.sort(listVehicleBacks, new Comparator<VehicleBack>() {
            @Override
            public int compare(VehicleBack vehicleBack1, VehicleBack vehicleBack2) {
                return vehicleBack1.getId() - (vehicleBack2.getId());
            }
        });
    }
    
    /**
     *  Sắp xếp danh sách vehicleBack theo bãi đỗ xe theo tứ tự tăng dần
     */
    public void sortVehicleBackByParkingLot() {
        Collections.sort(listVehicleBacks, new Comparator<VehicleBack>() {
            @Override
            public int compare(VehicleBack vehicleBack1, VehicleBack vehicleBack2) {
                return vehicleBack1.getParkingLot().compareTo(vehicleBack2.getParkingLot());
            }
        });
    }
    
    /**
     * sắp xếp danh sách vehicleBack theo loại xe
     */
    public void sortVehicleBackByType() {
        Collections.sort(listVehicleBacks, new Comparator<VehicleBack>() {
            @Override
            public int compare(VehicleBack vehicleBack1, VehicleBack vehicleBack2) {
                return vehicleBack1.getType().compareTo(vehicleBack2.getType());
            }
        });
    }
    
    /**
     * sắp xếp danh sách vehicleBack theo hãng xe theo tứ tự tăng dần
     */
    public void sortVehicleBackByBrand() {
        Collections.sort(listVehicleBacks, new Comparator<VehicleBack>() {
            @Override
            public int compare(VehicleBack vehicleBack1, VehicleBack vehicleBack2) {
                return vehicleBack1.getBrand().compareTo(vehicleBack2.getBrand());
            }
        });
    }
    
    /**
     *  sắp xếp danh sách vehicleBack theo biển số xe theo tứ tự tăng dần
     */
    public void sortVehicleBackByBienSoXe() {
        Collections.sort(listVehicleBacks, new Comparator<VehicleBack>() {
            @Override
            public int compare(VehicleBack vehicleBack1, VehicleBack vehicleBack2) {
                return vehicleBack1.getBienSoXe().compareTo(vehicleBack2.getBienSoXe());
            }
        });
    }
    
    /**
     *  sắp xếp danh sách vehicle theo thời gian vào bãi theo tứ tự tăng dần
     */
    public void sortVehicleByTimeEnter() {
        Collections.sort(listVehicleBacks, new Comparator<VehicleBack>() {
            @Override
            public int compare(VehicleBack vehicleBack1, VehicleBack vehicleBack2) {
                return vehicleBack1.getTimeEnter().compareTo(vehicleBack2.getTimeEnter());
            }
        });
    }
    
    /**
     *  sắp xếp danh sách vehicle theo thời gian vào bãi theo tứ tự tăng dần
     */
    public void sortVehicleByTimeExit() {
        Collections.sort(listVehicleBacks, new Comparator<VehicleBack>() {
            @Override
            public int compare(VehicleBack vehicleBack1, VehicleBack vehicleBack2) {
                return vehicleBack1.getTimeExit().compareTo(vehicleBack2.getTimeExit());
            }
        });
    }
    
    /**
     *  sắp xếp danh sách vehicleBack theo phí gửi xe theo tứ tự tăng dần
     */
    public void sortVehicleBackByFee() {
        Collections.sort(listVehicleBacks, new Comparator<VehicleBack>() {
            @Override
            public int compare(VehicleBack vehicleBack1, VehicleBack vehicleBack2) {
                return Double.compare(vehicleBack1.getFee(), vehicleBack2.getFee());
            }
        });
    }
    
}
