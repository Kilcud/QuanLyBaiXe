/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.viettuts.qlbx.controller;

import java.awt.event.*;
import java.text.NumberFormat;
import java.util.*;
import javax.swing.event.*;
import vn.viettuts.qlbx.dao.*;
import vn.viettuts.qlbx.entity.*;
import vn.viettuts.qlbx.view.Home;

/**
 *
 * @author kilcud
 */
public class HomeController {
    private final Home home;
    private final VehicleDao vehicleDao;
    private final VehicleBackDao vehicleBackDao; 
    private ParkingLotDao parkingLotDao;
    public final int SoXeToiDa = 10000;
    
    public HomeController(Home view){
        this.home = view;
        vehicleDao = new VehicleDao();
        vehicleBackDao = new VehicleBackDao();
        parkingLotDao = new ParkingLotDao();
        
        // <editor-fold defaultstate="collapsed" desc="Sự kiện tab Nhận xe">   
        // Nút thêm
        home.addAddVehicleListener(new AddVehicleListener());
        
        // Nút sửa
        home.addEdiVehicleListener(new EditVehicleListener());
        
        // Nút xóa
        home.addDeleteVehicleListener(new DeleteVehicleListener());
        
        // Nút clear
        home.addClearListener(new ClearVehicleListener());

        // Nút search
        home.addSearchListener(new SearchListener());
        home.addRefreshListener(new RefreshListener());
        
        // Điền thông tin hàng được chọn vào các ô text tương ứng
        home.addListVehicleSelectionListener(new ListVehicleSelectionListener());
        
        // Nút sort tbNhanXe
        home.addSortListener(new SortListener());
        
        // Nút trả xe
        home.addTraXeListener(new TraXeListener());
        
        // Nút thống kê loại xe
        home.addThongKeLoaiXeListener(new ThongKeLoaiXeListener());
        // </editor-fold>
        
        // <editor-fold defaultstate="collapsed" desc="Sự kiện tab Trả Xe">   
        // Nút tìm kiếm theo thời gian 
        home.addSearchDateListener(new SearchDateListener());
        home.addRefreshSearchDateListener(new RefreshSearchDateListener());
        
        // Nút tìm kiếm theo phí gửi 
        home.addSearchPaymentListener(new SearchPaymentListener());
        home.addRefreshSearchPaymentListener(new RefreshSearchPaymentListener());

        // Nút sort tbTraXe 
        home.addSortTraXeListener(new SortTraXeListener());
        
        // Nút thống kê số tiền
        home.addThongKeSoTienListener(new ThongKeSoTienListener());
        // </editor-fold>
        
        // <editor-fold defaultstate="collapsed" desc="Sự kiện tab Bãi Xe">   
        // Nút tìm kiếm tab bãi xe
        home.addSearchParkingLotListener(new SearchParkingLotListener());
        home.addRefreshSearchParkingLotListener(new RefreshSearchParkingLotListener());

         // Nút thêm bãi xe
        home.addAddParkingLotListener(new AddParkingLotListener());
        
        // Nút sửa bãi xe
        home.addEdiParkingLotListener(new EditParkingLotListener());
        
        // Nút xóa bãi xe
        home.addDeleteParkingLotListener(new DeleteParkingLotListener());
        
        // Nút clear
        home.addClearParkingLotListener(new ClearParkingLotVehicleListener());
        
        // Điền thông tin hàng được chọn vào các ô text tương ứng
        home.addListParkingLotSelectionListener(new ListParkingLotSelectionListener());
        // </editor-fold>

    }
    
    public void showHome() {
        home.setVisible(true);
        home.showListVehicles(vehicleDao.getListVehicles());
        home.showListVehicleBacks(vehicleBackDao.getListVehicleBacks());
        home.showListParkingLots(parkingLotDao.getListParkingLots());

    }
    
    // <editor-fold defaultstate="collapsed" desc="tab Nhận xe">   
    /**
     * Lớp AddVehicleListener 
     * chứa cài đặt cho sự kiện click button "Add"
     */
    class AddVehicleListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Vehicle vehicle = home.getVehicleInfo();
            if (vehicle != null) {
                if(vehicleDao.isFull(vehicle)){
                    home.showMessage("Bãi xe đã hết chỗ trống. Vui lòng chọn bãi xe khác!");
                }
                else if (home.checkDuplicate() == true){
                    home.showMessage("Biển số xe đã tồn tại.");
                }
                else 
                {
                    vehicleDao.add(vehicle);
                    home.showListVehicles(vehicleDao.getListVehicles());
                    home.showMessage("Thêm thành công!");
                    home.clearVehicleInfo();
                
                
                // Tăng phần đã chữa của bãi đỗ xe thêm 1
                parkingLotDao = new ParkingLotDao();
                List<ParkingLot> listParkingLot = parkingLotDao.getListParkingLots();
                
                for(int i = 0; i < listParkingLot.size(); i++){
                    int temp = listParkingLot.get(i).getSoXeDangChua();
                    if(listParkingLot.get(i).getName().equals(vehicle.getParkingLot())){
                        listParkingLot.get(i).setSoXeDangChua(temp + 1);
                    }
                }
                parkingLotDao.writeListParkingLots(listParkingLot);
                home.showListParkingLots(listParkingLot);
                }
            }
        }
    }
    
     /**
     * Lớp EditVehicleListener 
     * chứa cài đặt cho sự kiện click button "Edit"
     */
    class EditVehicleListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Vehicle vehicle = home.getVehicleInfo();
            List<Vehicle> listVehicle = vehicleDao.getListVehicles();
            boolean flag = false;
            
            if (vehicle != null) {
                for (Vehicle v : listVehicle){
                    if(v.getId() == vehicle.getId() && v.getBienSoXe().equals(vehicle.getBienSoXe()))
                        flag = true;
                }
                
                if (flag){
                    vehicleDao.edit(vehicle);
                    home.showListVehicles(listVehicle);
                    home.clearVehicleInfo();
                    home.showMessage("Cập nhật thành công!");
                }
                else{
                    if (home.checkDuplicate() == true){
                        home.showMessage("Biển số xe đã tồn tại.");
                    } else {
                        vehicleDao.edit(vehicle);
                        home.showListVehicles(listVehicle);
                        home.clearVehicleInfo();
                        home.showMessage("Cập nhật thành công!");
                    }
                }
            }
        }
    }

    /**
     * Lớp DeleteVehicleListener 
     * chứa cài đặt cho sự kiện click button "Delete"
     */
    class DeleteVehicleListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Vehicle vehicle = home.getVehicleInfo();
            if (vehicle != null) {
                vehicleDao.delete(vehicle);
                home.clearVehicleInfo();
                home.showListVehicles(vehicleDao.getListVehicles());
                home.showMessage("Xóa thành công!");
                
                // Giảm phần đã chữa của bãi đỗ xe đi 1
                parkingLotDao = new ParkingLotDao();
                List<ParkingLot> listParkingLot = parkingLotDao.getListParkingLots();
                for(int i = 0; i < listParkingLot.size(); i++){
                    int temp = listParkingLot.get(i).getSoXeDangChua();
                    if(listParkingLot.get(i).getName().equals(vehicle.getParkingLot())) 
                        listParkingLot.get(i).setSoXeDangChua(temp - 1);
                }
                parkingLotDao.writeListParkingLots(listParkingLot);
                home.showListParkingLots(listParkingLot);
            }
        }
    }

    /**
     * Lớp ClearVehicleListener 
     * chứa cài đặt cho sự kiện click button "Clear"
     */
    class ClearVehicleListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            
            home.clearVehicleInfo();
            home.showListVehicles(vehicleDao.getListVehicles());
        }
    }

    /**
     * Lớp ListVehicleSelectionListener 
     * chứa cài đặt cho sự kiện chọn vehicle trong bảng vehicle
     */
    class ListVehicleSelectionListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            home.fillVehicleFromSelectedRow();
        }
    }
    
    /**
     * Lớp SearchListener 
     * chứa cài đặt cho sự kiện search
     */
    class SearchListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String typeSearch = home.getTypeSearch().trim();
            String search = home.getTxtSearch().toLowerCase().trim();
            
            List<Vehicle> listVehicle = vehicleDao.getListVehicles();
            List<Vehicle> listSearchVehicle = new ArrayList<>();
           
            switch (typeSearch) {
                case "Theo ID":
                    for(int i = 0; i < listVehicle.size(); i++){
                        if(Integer.toString(listVehicle.get(i).getId()).toLowerCase().contains(search)) {
                            listSearchVehicle.add(listVehicle.get(i));
                        }
                    }   
                    break;
                case "Theo Bãi xe":
                    for(int i = 0; i < listVehicle.size(); i++){
                        if(listVehicle.get(i).getParkingLot().toLowerCase().contains(search)) {
                            listSearchVehicle.add(listVehicle.get(i));
                        }
                    }   
                    break;
                case "Theo Loại xe":
                    String[] keys = search.split(" ");
                    for (int i = 0; i < listVehicle.size(); i++) {
                        boolean flag = false;
                        for (String key : keys) {
                            if (listVehicle.get(i).getType().toLowerCase().contains(key) 
                                    && !listSearchVehicle.contains(listVehicle.get(i))) {
                                flag = true;
                            } else{
                                flag = false;
                                break;
                            }
                        }
                        if(flag){
                            listSearchVehicle.add(listVehicle.get(i));
                        }
                    }
                    break;
                case "Theo Hãng xe":
                    for(int i = 0; i < listVehicle.size(); i++){
                        if(listVehicle.get(i).getBrand().toLowerCase().contains(search)){
                            listSearchVehicle.add(listVehicle.get(i));
                        }
                    }   
                    break;
                case "Theo Biển số xe":
                    for(int i = 0; i < listVehicle.size(); i++){
                        if(listVehicle.get(i).getBienSoXe().toLowerCase().contains(search)){
                            listSearchVehicle.add(listVehicle.get(i));
                        }
                    }   
                    break;
                default:
                    for(int i = 0; i < listVehicle.size(); i++){
                        if(listVehicle.get(i).getBrand().toLowerCase().contains(search)) {
                            listSearchVehicle.add(listVehicle.get(i));
                        }
                    }   
                    break;
            }
            home.showListVehicles(listSearchVehicle);
        }
    }
    
    /**
     * Lớp RefreshListener 
     * chứa cài đặt cho sự kiện refresh
     */
    class RefreshListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            home.setTxtSearch("");
            home.showListVehicles(vehicleDao.getListVehicles());
        }
    }
    
     /**
     * Lớp SortListener 
     * chứa cài đặt cho sự kiện sort
     */
    class SortListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String typeSort = home.getTypeSort().trim();
            
            switch (typeSort) {
                case "Theo ID":
                    vehicleDao.sortVehicleByID();
                    break;
                case "Theo Bãi xe":
                    vehicleDao.sortVehicleByParkingLot();
                    break;
                case "Theo Loại xe":
                    vehicleDao.sortVehicleByType();
                    break;
                case "Theo Hãng xe":
                    vehicleDao.sortVehicleByBrand();
                    break;
                case "Theo Biển số xe":
                    vehicleDao.sortVehicleByBienSoXe();
                    break;
                case "Theo Thời gian nhận xe":
                    vehicleDao.sortVehicleByTimeEnter();
                    break;
                default:
                    vehicleDao.sortVehicleByBrand();
                    break;
            }
            home.showListVehicles(vehicleDao.getListVehicles());
        }
    }
    
    class TraXeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            VehicleBack vehicleBack = home.getVehicleBackInfo();
            Vehicle vehicle = home.getVehicleInfo();
            if (vehicleBack != null && vehicle != null) {
                vehicleDao.delete(vehicle);
                home.clearVehicleInfo();
                vehicleBackDao.add(vehicleBack);
                
                //home.showVehicleBack(vehicleBack);
                home.showListVehicles(vehicleDao.getListVehicles());
                home.showListVehicleBacks(vehicleBackDao.getListVehicleBacks());
                home.showMessage("Trả xe thành công!");
            }
            
            // Giảm phần đã chữa của bãi đỗ xe đi 1
                parkingLotDao = new ParkingLotDao();
                List<ParkingLot> listParkingLot = parkingLotDao.getListParkingLots();
                
                for(int i = 0; i < listParkingLot.size(); i++){
                    int temp = listParkingLot.get(i).getSoXeDangChua();
                    if(listParkingLot.get(i).getName().equals(vehicle.getParkingLot())){
                        listParkingLot.get(i).setSoXeDangChua(temp - 1);
                    }
                }
                parkingLotDao.writeListParkingLots(listParkingLot);
                home.showListParkingLots(listParkingLot);
        }
    }
    
    class ThongKeLoaiXeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e){
            List<Vehicle> listVehicle = vehicleDao.getListVehicles();
            int XeMayCount = 0;
            int OToCount = 0;
            int XeDapDienCount = 0;
            int XeMayDienCount = 0;
            int OToDienCount = 0;
            for (Vehicle v : listVehicle){
                String type = v.getType().trim();
                
                switch(type){
                    case "Xe máy":
                        XeMayCount++;
                        break;
                    case "Ô tô":
                        OToCount++;
                        break;
                    case "Xe đạp điện":
                        XeDapDienCount++;
                        break;
                    case "Xe máy điện":
                        XeMayDienCount++;
                        break;
                    case "Ô tô điện":
                        OToDienCount++;
                        break;
                    default:
                        break;
                }
            }
            home.showMessage("Tổng số xe trong bãi: " + listVehicle.size()
                            + "\nTrong đó:  "
                            + "\n           Số xe máy: " + XeMayCount
                            + "\n           Số ô tô: " + OToCount
                            + "\n           Số xe đạp điện: " + XeDapDienCount
                            + "\n           Số xe máy điện: " + XeMayDienCount
                            + "\n           Số ô tô điện: " + OToDienCount);
        }
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="tab Trả Xe">   
    class SearchDateListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            List<VehicleBack> listVehicleBack = vehicleBackDao.getListVehicleBacks();
            List<VehicleBack> listSearchVehicleBack = new ArrayList<>();     
              
            String typeDate = home.getTypeDate().trim();
            
            try {
            switch (typeDate) {
                case "Ngày nhận xe":
                    //Check nếu người dùng chọn giờ bắt đầu sau giờ kết thúc
                        if (home.getSearchTimeStart().after(home.getSearchTimeEnd())){
                            home.showMessage("Bạn phải chọn khoảng thời gian hợp lệ!");
                            break;
                        }
                    
                    for(int i = 0; i < listVehicleBack.size(); i++){                       
                         //int check = listVehicleBack.get(i).getTimeEnter().compareTo(home.getSearchTimeStart());
                        
                        if(listVehicleBack.get(i).getTimeEnter().after(home.getSearchTimeStart())
                        && listVehicleBack.get(i).getTimeEnter().before(home.getSearchTimeEnd())) 
                        {
                            listSearchVehicleBack.add(listVehicleBack.get(i));
                        }
                    }  
                    break;
                case "Ngày trả xe":
                    //Check nếu người dùng chọn giờ bắt đầu sau giờ kết thúc
                        if (home.getSearchTimeStart().after(home.getSearchTimeEnd())){
                            home.showMessage("Bạn phải chọn khoảng thời gian hợp lệ!");
                            break;
                        } 
                    
                    for(int i = 0; i < listVehicleBack.size(); i++){
                        if(listVehicleBack.get(i).getTimeExit().after(home.getSearchTimeStart())
                        && listVehicleBack.get(i).getTimeExit().before(home.getSearchTimeEnd())) 
                        {
                            listSearchVehicleBack.add(listVehicleBack.get(i));
                        }
                    }  
                    break;
                default:
                    for(int i = 0; i < listVehicleBack.size(); i++){
                        if(listVehicleBack.get(i).getTimeEnter().after(home.getSearchTimeStart())
                        && listVehicleBack.get(i).getTimeEnter().before(home.getSearchTimeEnd())) 
                        {
                            listSearchVehicleBack.add(listVehicleBack.get(i));
                        }
                    }  
                    break;
            }
            
            home.showListVehicleBacks(listSearchVehicleBack);
            } catch (Exception ex){
                home.showMessage("Bạn phải nhập đúng định dạng ngày (dd/MM/yyyy HH:mm:ss)");
        }
        }
    }
    
    class RefreshSearchDateListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            home.setDateSearchStart(null);
            home.setDateSearchEnd(null);
            home.showListVehicleBacks(vehicleBackDao.getListVehicleBacks());
        }
    }
    
    class SearchPaymentListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            try {
                double paymentMin = Double.parseDouble(home.getTxtMinPayment());
                double paymentMax = Double.parseDouble(home.getTxtMaxPayment());
                
                if (paymentMin > paymentMax){home.showMessage("Bạn phải chọn khoảng tiền cần tìm hợp lệ!");}

                List<VehicleBack> listVehicleBack = vehicleBackDao.getListVehicleBacks();
                List<VehicleBack> listSearchVehicleBack = new ArrayList<>();

                for(int i = 0; i < listVehicleBack.size(); i++){
                    if (listVehicleBack.get(i).getFee() >= paymentMin 
                            && listVehicleBack.get(i).getFee() <= paymentMax){
                        listSearchVehicleBack.add(listVehicleBack.get(i));
                    }
                }

                home.showListVehicleBacks(listSearchVehicleBack);
            }
            catch (NumberFormatException ex)
            {
                home.showMessage("Bạn phải nhập 1 số");
            }
        }
    }
    
    class RefreshSearchPaymentListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            home.setTxtMinPayment("");
            home.setTxtMaxPayment("");
            home.showListVehicleBacks(vehicleBackDao.getListVehicleBacks());
        }
    }
    
    /**
     * Lớp SortTraXeListener 
     * chứa cài đặt cho sự kiện sort
     */
    class SortTraXeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String typeSortTraXe = home.getTypeSortTraXe().trim();
            
            switch (typeSortTraXe) {
                case "Theo ID":
                    vehicleBackDao.sortVehicleBackByID();
                    break;
                case "Theo Bãi xe":
                    vehicleBackDao.sortVehicleBackByParkingLot();
                    break;
                case "Theo Loại xe":
                    vehicleBackDao.sortVehicleBackByType();
                    break;
                case "Theo Hãng xe":
                    vehicleBackDao.sortVehicleBackByBrand();
                    break;
                case "Theo Biển số xe":
                    vehicleBackDao.sortVehicleBackByBienSoXe();
                    break;
                case "Theo Thời gian nhận xe":
                    vehicleBackDao.sortVehicleByTimeEnter();
                    break;
                case "Theo Thời gian trả xe":
                    vehicleBackDao.sortVehicleByTimeExit();
                    break;
                case "Theo Phí gửi xe":
                    vehicleBackDao.sortVehicleBackByFee();
                    break;
                default:
                    vehicleBackDao.sortVehicleBackByBrand();
                    break;
            }
            home.showListVehicleBacks(vehicleBackDao.getListVehicleBacks());
        }
    }
    
    class ThongKeSoTienListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            List<VehicleBack> listVehicleBack = vehicleBackDao.getListVehicleBacks();
            
            double FeeXeMayCount = 0.0;
            double FeeOToCount = 0.0;
            double FeeXeDapDienCount = 0.0;
            double FeeXeMayDienCount = 0.0;
            double FeeOToDienCount = 0.0;
            double sumFee = 0.0;
            
            for (VehicleBack v : listVehicleBack){
                String type = v.getType().trim();
                
                switch(type){
                    case "Xe máy":
                        FeeXeMayCount += v.getFee();
                        break;
                    case "Ô tô":
                        FeeOToCount += v.getFee();
                        break;
                    case "Xe đạp điện":
                        FeeXeDapDienCount += v.getFee();
                        break;
                    case "Xe máy điện":
                        FeeXeMayDienCount += v.getFee();
                        break;
                    case "Ô tô điện":
                        FeeOToDienCount += v.getFee();
                        break;
                    default:
                        break;
                }
                
                sumFee += v.getFee();
            }
            
            // Định dạng tiền
            Locale locale = new Locale("vi", "VN");
            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
            home.showMessage("Tổng số tiền thu được: " + currencyFormatter.format(sumFee)
                            + "\nTrong đó:  "
                            + "\n           Tiền xe máy: " + currencyFormatter.format(FeeXeMayCount)
                            + "\n           Tiền ô tô: " + currencyFormatter.format(FeeOToCount)
                            + "\n           Tiền xe đạp điện: " + currencyFormatter.format(FeeXeDapDienCount)
                            + "\n           Tiền xe máy điện: " + currencyFormatter.format(FeeXeMayDienCount)
                            + "\n           Tiền ô tô điện: " + currencyFormatter.format(FeeOToDienCount));
        }
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="tab Bãi xe">   
    /**
     * Lớp AddParkingLotListener 
     * chứa cài đặt cho sự kiện click button "Add"
     */
    class AddParkingLotListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ParkingLot parkingLot = home.getParkingLotInfo();
            List<ParkingLot> listParkingLots =  parkingLotDao.getListParkingLots();
            
            int SucChuaNow = 0;
            for (ParkingLot pLot : listParkingLots){
                SucChuaNow += pLot.getSucChua();
            }
            int SucChuaConLai = SoXeToiDa - SucChuaNow;
            
            if (parkingLot != null) {
                if (SucChuaConLai < home.getTxtSucChua()){
                    home.showMessage("Không thể thêm do sức chứa tối đa có thể thêm còn " + SucChuaConLai + " xe");
                    home.setLabelSucChuaConLai(SucChuaConLai);
                }
                else {
                    parkingLotDao.add(parkingLot); 

                    home.showListParkingLots(listParkingLots);

                    // Refresh cbbBaiXe
                    home.refreshCbbBaiXe();
                    home.showMessage("Thêm thành công! Còn thêm được sức chứa tối đa là " + (SucChuaConLai - home.getTxtSucChua())  + " xe");
                    home.setLabelSucChuaConLai(SucChuaConLai - home.getTxtSucChua());
                }
                home.clearParkingLotInfo();
            }
        }
    }
    
     /**
     * Lớp EditParkingLotListener 
     * chứa cài đặt cho sự kiện click button "Edit"
     */
    class EditParkingLotListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ParkingLot parkingLot = home.getParkingLotInfo();
            List<ParkingLot> listParkingLots = parkingLotDao.getListParkingLots();
            
            if (parkingLot != null) {
                int SucChuaNow = 0;
                int SucChuaBanDau = 0;
                
                for (ParkingLot pLot : listParkingLots){
                    if(pLot.getIdParkingLot() == parkingLot.getIdParkingLot())
                    {
                        SucChuaBanDau = pLot.getSucChua();
                    }
                    SucChuaNow += pLot.getSucChua();
                }
                int SucChuaConLai = SoXeToiDa - SucChuaNow;
                int SucChuaConLaiIfEdit = SucChuaConLai + SucChuaBanDau;
                
                if(parkingLot.getSoXeDangChua() > home.getTxtSucChua())
                {
                    home.showMessage("Không thể sửa vì số xe trong bãi lớn hơn sức chứa!");
                    return;
                }
                
                if (SucChuaConLaiIfEdit  < home.getTxtSucChua()){
                    home.showMessage("Không thể sửa do sức chứa tối đa có thể thêm khi sửa còn " + SucChuaConLaiIfEdit + " xe");
                }
                else 
                {
                    parkingLotDao.edit(parkingLot);
                    home.showListParkingLots(listParkingLots);
                    home.showMessage("Cập nhật thành công! Còn thêm được sức chứa tối đa là " + (SucChuaConLaiIfEdit - home.getTxtSucChua()) + " xe");
                    home.setLabelSucChuaConLai(SucChuaConLaiIfEdit - home.getTxtSucChua());
                }
            home.clearParkingLotInfo();
            }
        }
    }
    
    /**
     * Lớp DeleteParkingLotListener 
     * chứa cài đặt cho sự kiện click button "Delete"
     */
    class DeleteParkingLotListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            parkingLotDao.Update();
            
            ParkingLot parkingLot = home.getParkingLotInfo();
            List<ParkingLot> listParkingLots = parkingLotDao.getListParkingLots();
            
            int SucChuaNow = 0;
            for (ParkingLot pLot : listParkingLots){
                SucChuaNow += pLot.getSucChua();
            }
            int SucChuaConLai = SoXeToiDa - SucChuaNow + parkingLot.getSucChua();
            
            if (parkingLot != null) {
                if(parkingLot.getSoXeDangChua() > 0)
                {
                    home.showMessage("Không thể xóa vì còn xe trong bãi!");
                }
                else
                {
                    parkingLotDao.delete(parkingLot);
                    home.showListParkingLots(listParkingLots);

                    // Refresh cbbBaiXe
                    home.refreshCbbBaiXe();

                    home.showMessage("Xóa thành công! Còn thêm được sức chứa tối đa là " + SucChuaConLai + " xe");
                    home.setLabelSucChuaConLai(SucChuaConLai);
                }
            home.clearParkingLotInfo();
            }
        }
    }
    
    /**
     * Lớp ClearVehicleListener 
     * chứa cài đặt cho sự kiện click button "Clear"
     */
    class ClearParkingLotVehicleListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            
            home.clearParkingLotInfo();
            home.showListParkingLots(parkingLotDao.getListParkingLots());
        }
    }
    
     // Chứa sự kiện khi nhấn nút tìm kiếm
    class SearchParkingLotListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String typeSearchParkingLot = home.getTypeSearchParkingLot().trim(); 
            String search = home.getTxtSearchParkingLot().toLowerCase().trim();
            
            List<ParkingLot> listParkingLot = parkingLotDao.getListParkingLots();
            List<ParkingLot> listSearchParkingLot= new ArrayList<>();
            
             switch (typeSearchParkingLot) {
                case "Theo ID":
                    for(int i = 0; i < listParkingLot.size(); i++)
                    {
                        if(Integer.toString(listParkingLot.get(i).getIdParkingLot()).toLowerCase().contains(search)){
                            listSearchParkingLot.add(listParkingLot.get(i));
                        }
                    } 
                    break;
                case "Theo Bãi xe":
                    for(int i = 0; i < listParkingLot.size(); i++)
                    {
                        if(listParkingLot.get(i).getName().toLowerCase().contains(search)){
                            listSearchParkingLot.add(listParkingLot.get(i));
                        }
                    }
                    break;
                case "Theo Sức chứa":
                    for(int i = 0; i < listParkingLot.size(); i++)
                    {
                        if(Integer.toString(listParkingLot.get(i).getSucChua()).toLowerCase().contains(search)){
                            listSearchParkingLot.add(listParkingLot.get(i));
                        }
                    }
                    break;
                case "Theo Số xe đang chứa":
                    for(int i = 0; i < listParkingLot.size(); i++)
                    {
                        if(Integer.toString(listParkingLot.get(i).getSoXeDangChua()).toLowerCase().contains(search)){
                            listSearchParkingLot.add(listParkingLot.get(i));
                        }
                    }
                    break;
                default:
                    for(int i = 0; i < listParkingLot.size(); i++)
                    {
                        if(listParkingLot.get(i).getName().toLowerCase().contains(search)){
                            listSearchParkingLot.add(listParkingLot.get(i));
                        }
                    }
                    break;
            }
            home.showListParkingLots(listSearchParkingLot);
        }
    }
    
    class RefreshSearchParkingLotListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            home.setTxtSearchParkingLot("");
            home.showListParkingLots(parkingLotDao.getListParkingLots());
        }
    }
    
     /**
     * Lớp ListParkingLotSelectionListener 
     * chứa cài đặt cho sự kiện chọn parkingLot trong bảng parkingLot
     */
    class ListParkingLotSelectionListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            home.fillParkingLotFromSelectedRow();
        }
    }
    // </editor-fold>


}
