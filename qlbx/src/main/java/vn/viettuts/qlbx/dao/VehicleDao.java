/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.viettuts.qlbx.dao;

import java.util.*;

import vn.viettuts.qlbx.utils.FileUtils;
import vn.viettuts.qlbx.entity.Vehicle;
import vn.viettuts.qlbx.entity.VehicleXML;
import vn.viettuts.qlbx.entity.ParkingLot;

/**
 *
 * @author kilcud
 */
public final class VehicleDao {
    private static final String VEHICLE_FILE_NAME = "vehicle.xml";
    private List<Vehicle> listVehicles;
    private ParkingLotDao parkingLotDao;
    
    public VehicleDao(){
        this.listVehicles = readListVehicles();
        if (listVehicles == null) {
            listVehicles = new ArrayList<>();
        }
    }
    
     /**
     * Đọc các đối tượng vehicle từ file vehicle.xml
     * 
     * @return list vehicle
     */
    public List<Vehicle> readListVehicles() {
        List<Vehicle> list = new ArrayList<>();
        VehicleXML vehicleXML = (VehicleXML) FileUtils.readXMLFile(
                VEHICLE_FILE_NAME, VehicleXML.class);
        if (vehicleXML != null) {
            list = vehicleXML.getVehicle();
        }
        return list;
    }
    
    /**
     * Lưu các đối tượng vehicle vào file vehicle.xml
     * 
     * @param vehicles
     */
    public void writeListVehicles(List<Vehicle> vehicles) {
        VehicleXML vehicleXML = new VehicleXML();
        vehicleXML.setVehicle(vehicles);
        FileUtils.writeXMLtoFile(VEHICLE_FILE_NAME, vehicleXML);
    }
    
    public boolean isFull(Vehicle vehicle){
        parkingLotDao= new ParkingLotDao();
        List<ParkingLot> listParkingLot=parkingLotDao.getListParkingLots();
        for(int i = 0; i < listParkingLot.size(); i++){
            if(listParkingLot.get(i).getName().equals(vehicle.getParkingLot())){
                int soChoTrongConLai = listParkingLot.get(i).getSoXeDangChua();
                int sucChua = listParkingLot.get(i).getSucChua();
                return soChoTrongConLai >= sucChua;
            }
        }
        return false;
    }

    /**
     * thêm vehicle vào listVehicles và lưu listVehicles vào file
     * 
     * @param vehicle
     */
    public void add(Vehicle vehicle) {
        int id = 1;
        List<Boolean> used = new ArrayList<>(Collections.nCopies(100005, false));
        
        for(int i=0; i < listVehicles.size(); i++){
            int index = listVehicles.get(i).getId();
            used.set(index, true);
        }
        for(int i = 1; i < used.size(); i++){
            if(!used.get(i)){
                id = i;
                break;
            }
        }
        vehicle.setId(id);
        listVehicles.add(vehicle);
        writeListVehicles(listVehicles);
    }

    /**
     * cập nhật vehicle vào listVehicles và lưu listVehicles vào file
     * 
     * @param vehicle
     */
    public void edit(Vehicle vehicle) {
        int size = listVehicles.size();
        for (int i = 0; i < size; i++) {
            if (listVehicles.get(i).getId() == vehicle.getId()) {  
                listVehicles.get(i).setParkingLot(vehicle.getParkingLot());
                listVehicles.get(i).setType(vehicle.getType());
                listVehicles.get(i).setBrand(vehicle.getBrand());
                listVehicles.get(i).setBienSoXe(vehicle.getBienSoXe());
                listVehicles.get(i).setTimeEnter(vehicle.getTimeEnter());
                
                writeListVehicles(listVehicles);
                break;
            }
        }
    }

     /**
     * xóa vehicle từ listVehicles và lưu listVehicles vào file
     * 
     * @param vehicle
     * @return 
     */
    public boolean delete(Vehicle vehicle) {
        boolean isFound = false;
        int size = listVehicles.size();
        for (int i = 0; i < size; i++) {
            if (listVehicles.get(i).getId() == vehicle.getId() && listVehicles.get(i).getParkingLot().equals(vehicle.getParkingLot())) {
                vehicle = listVehicles.get(i);
                isFound = true;
                break;
            }
        }
        if (isFound) 
        {
            listVehicles.remove(vehicle);
            writeListVehicles(listVehicles);
            return true;
        }
        return false;
    }
    
    public List<Vehicle> getListVehicles() {
        return listVehicles;
    }

    public void setListVehicles(List<Vehicle> listVehicles) {
        this.listVehicles = listVehicles;
    }

    /**
     *  sắp xếp danh sách vehicle theo ID xe theo tứ tự tăng dần
     */
    public void sortVehicleByID() {
        Collections.sort(listVehicles, new Comparator<Vehicle>() {
            @Override
            public int compare(Vehicle vehicle1, Vehicle vehicle2) {
                return vehicle1.getId() - (vehicle2.getId());
            }
        });
    }
    
    /**
     *  Sắp xếp danh sách vehicle theo bãi đỗ xe theo tứ tự tăng dần
     */
    public void sortVehicleByParkingLot() {
        Collections.sort(listVehicles, new Comparator<Vehicle>() {
            @Override
            public int compare(Vehicle vehicle1, Vehicle vehicle2) {
                return vehicle1.getParkingLot().compareTo(vehicle2.getParkingLot());
            }
        });
    }
    
    /**
     * sắp xếp danh sách vehicle theo loại xe
     */
    public void sortVehicleByType() {
        Collections.sort(listVehicles, new Comparator<Vehicle>() {
            @Override
            public int compare(Vehicle vehicle1, Vehicle vehicle2) {
                return vehicle1.getType().compareTo(vehicle2.getType());
            }
        });
    }
    
    /**
     * sắp xếp danh sách vehicle theo hãng xe theo tứ tự tăng dần
     */
    public void sortVehicleByBrand() {
        Collections.sort(listVehicles, new Comparator<Vehicle>() {
            @Override
            public int compare(Vehicle vehicle1, Vehicle vehicle2) {
                return vehicle1.getBrand().compareTo(vehicle2.getBrand());
            }
        });
    }
    
    /**
     *  sắp xếp danh sách vehicle theo biển số xe theo tứ tự tăng dần
     */
    public void sortVehicleByBienSoXe() {
        Collections.sort(listVehicles, new Comparator<Vehicle>() {
            @Override
            public int compare(Vehicle vehicle1, Vehicle vehicle2) {
                return vehicle1.getBienSoXe().compareTo(vehicle2.getBienSoXe());
            }
        });
    }
    
    /**
     *  sắp xếp danh sách vehicle theo thời gian vào bãi theo tứ tự tăng dần
     */
    public void sortVehicleByTimeEnter() {
        Collections.sort(listVehicles, new Comparator<Vehicle>() {
            @Override
            public int compare(Vehicle vehicle1, Vehicle vehicle2) {
                return vehicle1.getTimeEnter().compareTo(vehicle2.getTimeEnter());
            }
        });
    }
    
    public void Update(){
        this.listVehicles = readListVehicles();
        if (listVehicles == null) {
            listVehicles = new ArrayList<>();
        }
    }
}

