/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.viettuts.qlbx.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import vn.viettuts.qlbx.entity.ParkingLot;
import vn.viettuts.qlbx.entity.ParkingLotXML;
import vn.viettuts.qlbx.utils.FileUtils;
/**
 *
 * @author kilcud
 */
    
public class ParkingLotDao {
    private static final String PARKINGLOT_FILE_NAME = "parkingLot.xml";
    private List<ParkingLot> listParkingLots;
    

    public ParkingLotDao() {
        this.listParkingLots = readListParkingLots();
        if (listParkingLots == null) {
            listParkingLots = new ArrayList<>();
        }
    }
    
    /**
     * Đọc các đối tượng parkingLot từ file parkingLot.xml
     * 
     * @return list parkingLot
     */
    public List<ParkingLot> readListParkingLots() {
        List<ParkingLot> list = new ArrayList<>();
        ParkingLotXML parkingLotXML = (ParkingLotXML) FileUtils.readXMLFile(
                PARKINGLOT_FILE_NAME, ParkingLotXML.class);
        if (parkingLotXML != null) {
            list = parkingLotXML.getParkingLot();
        }
        return list;
    }
    
    /**
     * Lưu các đối tượng parkingLot vào file parkingLot.xml
     * 
     * @param parkingLots
     */
    public void writeListParkingLots(List<ParkingLot> parkingLots) {
        ParkingLotXML parkingLotXML = new ParkingLotXML();
        parkingLotXML.setParkingLot(parkingLots);
        FileUtils.writeXMLtoFile(PARKINGLOT_FILE_NAME, parkingLotXML);
    }
    
     /**
     * thêm parkingLot vào listParkingLots và lưu listParkingLots vào file
     * 
     * @param parkingLot
     */
    public void add(ParkingLot parkingLot){
        int id = 1;
        List<Boolean> used = new ArrayList<>(Collections.nCopies(100005, false));
        
        for(int i=0; i < listParkingLots.size(); i++){
            int index = listParkingLots.get(i).getIdParkingLot();
            used.set(index, true);
        }
        for(int i = 1; i < used.size(); i++){
            if(!used.get(i)){
                id = i;
                break;
            }
        }

        parkingLot.setIdParkingLot(id);
        listParkingLots.add(parkingLot);
        //sắp xếp lại listParkinglot
        sortParkingLotByID();
        writeListParkingLots(listParkingLots);
    }

    /**
     * cập nhật parkingLot vào listParkingLots và lưu listParkingLots vào file
     * 
     * @param parkingLot
     */
    public void edit(ParkingLot parkingLot) {
        int size = listParkingLots.size();
        for (int i = 0; i < size; i++){
            if (listParkingLots.get(i).getIdParkingLot() == parkingLot.getIdParkingLot()) {
                listParkingLots.get(i).setName(parkingLot.getName());
                listParkingLots.get(i).setSucChua(parkingLot.getSucChua());
                listParkingLots.get(i).setSoXeDangChua(parkingLot.getSoXeDangChua());
                writeListParkingLots(listParkingLots);
                break;
            }
        }
    }

    /**
     * xóa parkingLot từ listParkingLots và lưu listParkingLots vào file
     * 
     * @param parkingLot
     * @return 
     */
    
    
    public boolean delete(ParkingLot parkingLot) {
        boolean isFound = false;
        int size = listParkingLots.size();
        for (int i = 0; i < size; i++) {
            if (listParkingLots.get(i).getIdParkingLot() == parkingLot.getIdParkingLot()) {
                parkingLot = listParkingLots.get(i);
                isFound = true;
                break;
            }
        }
        if (isFound) {
            listParkingLots.remove(parkingLot);
            writeListParkingLots(listParkingLots);
            return true;
        }
        return false;
    }

    /**
     * sắp xếp danh sách parkingLot theo name theo tứ tự tăng dần
     */
    public void sortParkingLotByName() {
        Collections.sort(listParkingLots, new Comparator<ParkingLot>() {
            @Override
            public int compare(ParkingLot parkingLot1, ParkingLot parkingLot2) {
                return parkingLot1.getName().compareTo(parkingLot2.getName());
            }
        });
    }

    /**
     * sắp xếp danh sách parkingLot theo sức chứa theo tứ tự tăng dần
     */
    public void sortParkingLotByCapacity() {
        Collections.sort(listParkingLots, new Comparator<ParkingLot>() {
            @Override
            public int compare(ParkingLot parkingLot1, ParkingLot parkingLot2) {
                if (parkingLot1.getSucChua() > parkingLot2.getSucChua()) {
                    return 1;
                }
                return -1;
            }
        });
    }
    
     /**
     *  sắp xếp danh sách vehicle theo ID xe theo tứ tự tăng dần
     */
    public void sortParkingLotByID() {
        Collections.sort(listParkingLots, new Comparator<ParkingLot>() {
            @Override
            public int compare(ParkingLot parkingLot1, ParkingLot parkingLot2) {
                return parkingLot1.getIdParkingLot() - (parkingLot2.getIdParkingLot());
            }
            
        }
        );
    }
    
    /**
     * sắp xếp danh sách parkingLot theo số vị trí có xe đỗ theo tứ tự tăng dần
     */
    public void sortParkingLotByCurrentOccupancy() {
        Collections.sort(listParkingLots, new Comparator<ParkingLot>() {
            @Override
            public int compare(ParkingLot parkingLot1, ParkingLot parkingLot2) {
                if (parkingLot1.getSoXeDangChua() > parkingLot2.getSoXeDangChua()) {
                    return 1;
                }
                return -1;
            }
        });
    }

    public List<ParkingLot> getListParkingLots() {
        return listParkingLots;
    }
    
    public void Update(){
        this.listParkingLots = readListParkingLots();
        if (listParkingLots == null) {
            listParkingLots = new ArrayList<>();
        }
    }

    public void setListParkingLots(List<ParkingLot> listParkingLots) {
        this.listParkingLots = listParkingLots;
    }
    
   
}




