/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vn.viettuts.qlbx.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import vn.viettuts.qlbx.dao.ParkingLotDao;
import vn.viettuts.qlbx.dao.VehicleDao;
import vn.viettuts.qlbx.entity.ParkingLot;
import vn.viettuts.qlbx.entity.Vehicle;
import vn.viettuts.qlbx.entity.VehicleBack;

/**
 *
 * @author kilcud
 */
public class Home extends javax.swing.JFrame {
    private final ParkingLotDao parkingLotDao;
    private final VehicleDao vehicleDao;
    private final String[] columnsName_NhanXe = new String[]{
        "ID", "Bãi xe", "Loại xe", "Hãng xe", "Biển số xe", "Giờ nhận xe", "Ngày nhận xe"};
    private final String[] columnsName_TraXe = new String[]{
        "ID", "Bãi xe", "Loại xe", "Hãng xe", "Biển số xe", "Giờ nhận xe", "Ngày nhận xe", "Giờ trả xe", "Ngày trả xe", "Phí gửi xe (VNĐ)"};
    private final String[] columnsName_BaiXe = new String[]{
        "ID", "Tên bãi xe", "Sức chứa", "Số xe đang chứa"};

    
    /**
     * Creates new form Home
     */
    public Home() {
        parkingLotDao = new ParkingLotDao();
        vehicleDao = new VehicleDao();
        initComponents();

        // Disable Edit and Delete buttons
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        btnTraXe.setEnabled(false);
        // Enable Add button
        btnAdd.setEnabled(true);
        
        btnEditParkingLot.setEnabled(false);
        btnDeleteParkingLot.setEnabled(false);
    }
    
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
    
    public void enableAllBtn(){
        // enable Edit and Delete buttons
        btnEdit.setEnabled(true);
        btnDelete.setEnabled(true);
        // enable Add button
        btnAdd.setEnabled(true);
        btnTraXe.setEnabled(true);
    }
    
    // <editor-fold defaultstate="collapsed" desc="tab Nhận Xe">     
    /**
     * Hiển thị list vehicle vào bảng vehicleTable
     * 
     * @param list
     */
    public void showListVehicles(List<Vehicle> list) {
        int size = list.size();
        // với bảng vehicleTable có 7 cột, 
        // khởi tạo mảng 2 chiều vehicles, trong đó:
        // số hàng: là kích thước của list vehicle 
        // số cột: là 7
        Object [][] vehicles = new Object[size][7];
        for (int i = 0; i < size; i++) {
            vehicles[i][0] = list.get(i).getId();
            vehicles[i][1] = list.get(i).getParkingLot();           
            vehicles[i][2] = list.get(i).getType();
            vehicles[i][3] = list.get(i).getBrand();
            vehicles[i][4] = list.get(i).getBienSoXe();
            
            //Chuyển từ Date sang String
            Date dateNhanXe = list.get(i).getTimeEnter();

            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss" );
            String timeString = timeFormat.format(dateNhanXe);
            vehicles[i][5] = timeString;
            
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String dateString = dateFormat.format(dateNhanXe);
            vehicles[i][6] = dateString;
        }
        tbNhanXe.setModel(new DefaultTableModel(vehicles, columnsName_NhanXe));
    }
    
    
    /**
     * điền thông tin của hàng được chọn từ bảng vehicle 
     * vào các trường tương ứng của vehicle.
     */
    public void fillVehicleFromSelectedRow()  {
        // Lấy chỉ số của hàng được chọn 
        int row = tbNhanXe.getSelectedRow();
        if (row >= 0) {
            try {
                txtID.setText(tbNhanXe.getModel().getValueAt(row, 0).toString());
                cbbBaiXe.setSelectedItem(tbNhanXe.getModel().getValueAt(row, 1));                
                cbbLoaiXe.setSelectedItem(tbNhanXe.getModel().getValueAt(row, 2));
                txtHangXe.setText(tbNhanXe.getModel().getValueAt(row, 3).toString());
                txtBienSoXe.setText(tbNhanXe.getModel().getValueAt(row, 4).toString());
              
                
                //Chuyển từ String sang Time
                String timeString = tbNhanXe.getModel().getValueAt(row, 5).toString();
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                Date time = timeFormat.parse(timeString);
                    //Chuyển từ Date sang 1 ô kiểu Jspinner
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(time);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                int second = calendar.get(Calendar.SECOND);
                
                SpinnerDateModel model = new SpinnerDateModel();
                calendar.set( hour, minute, second);
                model.setValue(calendar.getTime());
                
                JSpinner.DateEditor editor = new JSpinner.DateEditor(timeNhanXe, "HH:mm:ss");
                timeNhanXe.setEditor(editor);
                timeNhanXe.setModel(model);
                
                // Chuyển từ String sang Date
                String dateString = tbNhanXe.getModel().getValueAt(row, 6).toString();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date date = dateFormat.parse(dateString);
                    //Chuyển từ Date 1 ô kiểu JDateChooser
                dateNhanXe.setDate(date);
                dateNhanXe.setDateFormatString("dd/MM/yyyy");
                
                enableAllBtn();
                
            } catch (ParseException ex) {
                Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
            }  
        }
    }
    
    /**
     * Xóa thông tin vehicle
     */
    public void clearVehicleInfo() {
        txtID.setText("");
        cbbBaiXe.setSelectedIndex(-1);
        cbbLoaiXe.setSelectedIndex(-1);
        txtHangXe.setText("");
        txtBienSoXe.setText("");
        
        // Tạo đối tượng Date từ dữ liệu đầu vào
        Date date = new Date(); 
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        
        // Thiết lập giá trị cho ô nhập thời gian (JSpinner)
        timeNhanXe.setEditor(new JSpinner.DateEditor(timeNhanXe, "HH:mm:ss"));
        timeNhanXe.setValue(calendar.getTime());
        
        // Thiết lập giá trị cho ô nhập ngày (JDateChooser)
        dateNhanXe.setDate(calendar.getTime());
        
        // disable Edit and Delete buttons
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        // enable Add button
        btnAdd.setEnabled(true);
        btnTraXe.setEnabled(false);
    }
    
    /**
     * Lấy thông tin vehicle
     * 
     * @return
     */
    public Vehicle getVehicleInfo() {
        // validate vehicle
        if (!validateParkingLot() || !validateLoaiXe()
                || !validateBienSoXe()
                || !validateEnterTime()
                || !validateDate()) {
            return null;
        }
        try {
            Vehicle vehicle = new Vehicle();
            if (txtID.getText() != null && !"".equals(txtID.getText())) {
                vehicle.setId(Integer.parseInt(txtID.getText()));
            }
            
            vehicle.setParkingLot(cbbBaiXe.getSelectedItem().toString());
            vehicle.setType(cbbLoaiXe.getSelectedItem().toString());
            vehicle.setBrand(txtHangXe.getText().trim());
            vehicle.setBienSoXe(txtBienSoXe.getText().trim());
            
            // Lấy giá trị từ ô nhập thời gian (JSpinner)
            Date timeValue = (Date) timeNhanXe.getValue();
            // Lấy giá trị từ ô nhập ngày (JDateChooser)
            Date dateValue = dateNhanXe.getDate();
            
            // Tạo một đối tượng Calendar và thiết lập giá trị ngày và giờ
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateValue);
            calendar.set(Calendar.HOUR_OF_DAY, timeValue.getHours());
            calendar.set(Calendar.MINUTE, timeValue.getMinutes());
            calendar.set(Calendar.SECOND, timeValue.getSeconds());
            
            // Lưu giá trị vào biến kiểu Date
            Date combinedValue = calendar.getTime();
            vehicle.setTimeEnter(combinedValue);
            return vehicle;
        } catch (NumberFormatException e) {
            showMessage(e.getMessage());
        }
        return null;
    }
    
    private boolean validateParkingLot(){
        if (cbbBaiXe.getSelectedIndex() == -1){
            cbbBaiXe.requestFocus();
            showMessage("Bãi xe đang trống.");
            return false;
        }
        return true;
    }
    
    private boolean validateLoaiXe(){
        if (cbbLoaiXe.getSelectedIndex() == -1){
            cbbLoaiXe.requestFocus();
            showMessage("Loại xe đang trống.");
            return false;
        }
        return true;
    }
    
    private boolean validateBienSoXe() {
        String BienSoXe = txtBienSoXe.getText();
        if (BienSoXe == null || "".equals(BienSoXe.trim())) {
            txtBienSoXe.requestFocus();
            showMessage("Biển số xe không được trống.");
            return false;
        }
        String regex = "([1-9][0-9][A-Z]){1}[A-Z1-9]?[-]{1}[0-9]{4,5}";
        Pattern pattern = Pattern.compile(regex);
        if(!pattern.matcher(BienSoXe).matches()){
            txtBienSoXe.requestFocus();
            showMessage("Biển số xe không đúng định dạng. \nVí dụ:\n 99A-12345; \n 29B-1234;\n 99G1-66666;\n 30LD-55555");
            return false;
        }
        return true;
    }
    
    // Check biển số xe
    public boolean checkDuplicate(){
        String BienSoXe = txtBienSoXe.getText().trim();
        vehicleDao.Update();
        List<Vehicle> listVehicles = vehicleDao.getListVehicles();
        
        for (Vehicle v : listVehicles){
            if (v.getBienSoXe().trim().equals(BienSoXe)){
                return true; 
            }
        }
        return false;
    }
    
    private boolean validateEnterTime() {
        // Lấy giá trị từ ô nhập thời gian (JSpinner)
        Date timeValue = (Date) timeNhanXe.getValue();
        // Lấy giá trị từ ô nhập ngày (JDateChooser)
        if(validateDate() == false){
            return false;
        }
        
        Date dateValue = dateNhanXe.getDate();
        // Tạo một đối tượng Calendar và thiết lập giá trị ngày và giờ
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateValue);
        calendar.set(Calendar.HOUR_OF_DAY, timeValue.getHours());
        calendar.set(Calendar.MINUTE, timeValue.getMinutes());
        calendar.set(Calendar.SECOND, timeValue.getSeconds());
        
        // Lưu giá trị vào biến kiểu Date
        Date enterTime = calendar.getTime();
        Date currentTime = new Date();
        int compare = enterTime.compareTo(currentTime);
        if(compare > 0) {
            showMessage("Thời gian vào bãi không được sau thời gian hiện tại");
            return false;
        } 
        return true;
    }
    
    private boolean validateDate() {
        if (dateNhanXe.getDate() == null) {
            showMessage("Ngày vào bãi không được trống.");
            return false;
        }
        return true;
    }
    
    public void actionPerformed(ActionEvent e) {
    }
    
    public void valueChanged(ListSelectionEvent e) {
    }
    
    public void addAddVehicleListener(ActionListener listener) {
        btnAdd.addActionListener(listener);
    }
    
    public void addEdiVehicleListener(ActionListener listener) {
        btnEdit.addActionListener(listener);
    }
    
    public void addDeleteVehicleListener(ActionListener listener) {
        btnDelete.addActionListener(listener);
    }
    
    public void addClearListener(ActionListener listener) {
        btnClear.addActionListener(listener);
    }
    
    public void addRefreshListener(ActionListener listener) {
        btnRefresh.addActionListener(listener);
    }
  
    public String getTypeSearch(){
        return cbbTypeSearch.getSelectedItem().toString();
    }
    
    public String getTxtSearch(){
        return txtSearch.getText().trim();
    }

    public void setTxtSearch(String txtSearch) {
        this.txtSearch.setText(txtSearch); 
    }
    
    public String getTypeSort(){
        return cbbTypeSort.getSelectedItem().toString();
    }
    public void addSearchListener(ActionListener listener) {
        btnSearch.addActionListener(listener);
    }
    
    public void addSortListener(ActionListener listener) {
        btnSort.addActionListener(listener);
    }
    
    public void addListVehicleSelectionListener(ListSelectionListener listener) {
        tbNhanXe.getSelectionModel().addListSelectionListener(listener);
    }

    public void addTraXeListener(ActionListener listener) {
        btnTraXe.addActionListener(listener);
    }
    
    public void addThongKeLoaiXeListener(ActionListener listener) {
        btnThongKeLoaiXe.addActionListener(listener);
    }
    // </editor-fold>   
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane = new javax.swing.JTabbedPane();
        ImageIcon homeIcon = new javax.swing.ImageIcon("src/main/java/vn/viettuts/qlbx/images/homeIcon.png");
        tabHome = new javax.swing.JPanel();
        ImageIcon wallpaper = new javax.swing.ImageIcon("src/main/java/vn/viettuts/qlbx/images/wallpaper.png");
        jLabel1 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbInfor = new javax.swing.JTable();
        ImageIcon parkingZoneIcon = new javax.swing.ImageIcon("src/main/java/vn/viettuts/qlbx/images/parkingZoneIcon.png");
        tabBaiXe = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        txtIDParkingLot = new javax.swing.JTextField();
        txtNameParkingLot = new javax.swing.JTextField();
        txtSucChua = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jlbSoXeDangChua = new javax.swing.JLabel();
        txtSoXeDangChua = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tbParkingLot = new javax.swing.JTable();
        jPanel10 = new javax.swing.JPanel();
        btnAddParkingLot = new javax.swing.JButton();
        btnEditParkingLot = new javax.swing.JButton();
        btnDeleteParkingLot = new javax.swing.JButton();
        btnClearParkingLot = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        cbbTypeSearchParkingLot = new javax.swing.JComboBox<>();
        txtSearchParkingLot = new javax.swing.JTextField();
        btnRefreshSearchParkingLot = new javax.swing.JButton();
        btnSearchParkingLot = new javax.swing.JButton();
        jLabel22 = new javax.swing.JLabel();
        parkingLotDao.Update();
        int SucChuaNow = 0;
        for (ParkingLot pLot : parkingLotDao.getListParkingLots()){
            SucChuaNow += pLot.getSucChua();
        }
        int SucChuaConLai = 10000 - SucChuaNow;
        labelSucChuaConLai = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        ImageIcon receiveCarIcon = new javax.swing.ImageIcon("src/main/java/vn/viettuts/qlbx/images/receiveCarIcon.png");
        tabNhanXe = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbNhanXe = new javax.swing.JTable();
        jlbTitle = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        btnSort = new javax.swing.JButton();
        cbbTypeSort = new javax.swing.JComboBox<>();
        jPanel4 = new javax.swing.JPanel();
        btnAdd = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        ljbSearch = new javax.swing.JLabel();
        cbbTypeSearch = new javax.swing.JComboBox<>();
        txtSearch = new javax.swing.JTextField();
        btnRefresh = new javax.swing.JButton();
        btnSearch = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jlbID = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtID = new javax.swing.JTextPane();
        jlbBaiXe = new javax.swing.JLabel();
        cbbBaiXe = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        cbbLoaiXe = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        txtHangXe = new javax.swing.JTextPane();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        txtBienSoXe = new javax.swing.JTextPane();
        jLabel5 = new javax.swing.JLabel();
        Date date= new Date();
        SpinnerDateModel sm = new SpinnerDateModel(date,null,null,Calendar.HOUR_OF_DAY);
        timeNhanXe = new javax.swing.JSpinner(sm);
        dateNhanXe = new com.toedter.calendar.JDateChooser();
        jLabel6 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        btnTraXe = new javax.swing.JButton();
        btnThongKeLoaiXe = new javax.swing.JButton();
        ImageIcon backCarIcon = new javax.swing.ImageIcon("src/main/java/vn/viettuts/qlbx/images/backCarIcon.png");
        tabTraXe = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tbTraXe = new javax.swing.JTable();
        jLabel13 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        dateSearchStart = new com.toedter.calendar.JDateChooser();
        dateSearchEnd = new com.toedter.calendar.JDateChooser();
        jLabel11 = new javax.swing.JLabel();
        btnRefreshSearchDate = new javax.swing.JButton();
        btnSearchDate = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        cbbTypeDate = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        txtMinPayment = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtMaxPayment = new javax.swing.JTextField();
        btnRefreshSearchPayment = new javax.swing.JButton();
        btnSearchPayment = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        cbbTypeSortTraXe = new javax.swing.JComboBox<>();
        btnSortTraXe = new javax.swing.JButton();
        btnThongKeSoTien = new javax.swing.JButton();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Cardo", 1, 36)); // NOI18N
        jLabel1.setIcon(wallpaper);
        jLabel1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel1.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel7.setText("PHẦN MỀM QUẢN LÝ BÃI ĐỖ XE ");

        tbInfor.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"D53AT.B9.12 - T01", "Hoàng Đức Linh", "B9D53"},
                {"D53AT.B9.14 - T01", "Nguyễn Tuyết Mai", "B9D53"}
            },
            new String [] {
                "Mã học viên", "Họ và tên", "Lớp"
            }
        ));
        tbInfor.setAutoscrolls(false);
        tbInfor.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tbInfor.setGridColor(new java.awt.Color(204, 255, 255));
        tbInfor.setShowGrid(true);
        jScrollPane3.setViewportView(tbInfor);
        if (tbInfor.getColumnModel().getColumnCount() > 0) {
            tbInfor.getColumnModel().getColumn(0).setHeaderValue("Mã học viên");
            tbInfor.getColumnModel().getColumn(1).setHeaderValue("Họ và tên");
            tbInfor.getColumnModel().getColumn(2).setHeaderValue("Lớp");
        }

        javax.swing.GroupLayout tabHomeLayout = new javax.swing.GroupLayout(tabHome);
        tabHome.setLayout(tabHomeLayout);
        tabHomeLayout.setHorizontalGroup(
            tabHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabHomeLayout.createSequentialGroup()
                .addGap(75, 75, 75)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 527, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 77, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 384, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(75, 75, 75))
            .addGroup(tabHomeLayout.createSequentialGroup()
                .addGap(382, 382, 382)
                .addComponent(jLabel7)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        tabHomeLayout.setVerticalGroup(
            tabHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabHomeLayout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(jLabel7)
                .addGroup(tabHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tabHomeLayout.createSequentialGroup()
                        .addGap(59, 59, 59)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(tabHomeLayout.createSequentialGroup()
                        .addGap(197, 197, 197)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(93, Short.MAX_VALUE))
        );

        jTabbedPane.addTab("HOME", homeIcon, tabHome);

        jPanel8.setBackground(new java.awt.Color(255, 102, 102));

        txtIDParkingLot.setEditable(false);
        txtIDParkingLot.setBackground(new java.awt.Color(204, 204, 204));

        txtNameParkingLot.setEditable(false);
        txtNameParkingLot.setBackground(new java.awt.Color(204, 204, 204));

        jLabel15.setText("ID");

        jLabel16.setText("Tên bãi");

        jLabel17.setText("Sức chứa");

        jlbSoXeDangChua.setText("Số xe đang chứa");

        txtSoXeDangChua.setEditable(false);
        txtSoXeDangChua.setForeground(new java.awt.Color(204, 204, 204));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15)
                            .addComponent(jLabel16)
                            .addComponent(jLabel17))
                        .addGap(66, 66, 66)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtSucChua, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE)
                            .addComponent(txtNameParkingLot, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtIDParkingLot, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(jlbSoXeDangChua, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtSoXeDangChua)))
                .addGap(59, 59, 59))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtIDParkingLot, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addGap(26, 26, 26)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNameParkingLot, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSucChua, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSoXeDangChua, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlbSoXeDangChua))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        jPanel9.setBackground(new java.awt.Color(204, 255, 204));

        tbParkingLot.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Tên bãi", "Sức chứa", "Số xe đang chứa"
            }
        ));
        jScrollPane5.setViewportView(tbParkingLot);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap(44, Short.MAX_VALUE)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 486, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 316, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(53, Short.MAX_VALUE))
        );

        jPanel10.setBackground(new java.awt.Color(255, 204, 204));

        btnAddParkingLot.setText("Thêm bãi");

        btnEditParkingLot.setText("Sửa thông tin");

        btnDeleteParkingLot.setText("Xóa bãi");

        btnClearParkingLot.setText("Clear");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnAddParkingLot, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnEditParkingLot)
                .addGap(18, 18, 18)
                .addComponent(btnDeleteParkingLot, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnClearParkingLot, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAddParkingLot)
                    .addComponent(btnEditParkingLot)
                    .addComponent(btnDeleteParkingLot)
                    .addComponent(btnClearParkingLot))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel18.setText("QUẢN LÝ BÃI XE");

        jPanel11.setBackground(new java.awt.Color(204, 255, 255));

        jLabel19.setText("Tìm kiếm thông tin:");

        cbbTypeSearchParkingLot.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Theo ID", "Theo Bãi xe", "Theo Sức chứa", "Theo Số xe đang chứa" }));

        btnRefreshSearchParkingLot.setText("X");

        btnSearchParkingLot.setText("Tìm kiếm");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel19)
                .addGap(18, 18, 18)
                .addComponent(cbbTypeSearchParkingLot, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtSearchParkingLot, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnRefreshSearchParkingLot, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSearchParkingLot)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap(16, Short.MAX_VALUE)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtSearchParkingLot, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel19)
                        .addComponent(cbbTypeSearchParkingLot, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnRefreshSearchParkingLot)
                        .addComponent(btnSearchParkingLot)))
                .addContainerGap())
        );

        jLabel22.setText("Tổng sức chứa tối đa của tất cả các bãi: 10 000 xe");

        labelSucChuaConLai.setText(Integer.toString(SucChuaConLai));

        jLabel23.setText("Tổng sức chứa tối đa còn lại: ");

        jLabel24.setText("xe");

        javax.swing.GroupLayout tabBaiXeLayout = new javax.swing.GroupLayout(tabBaiXe);
        tabBaiXe.setLayout(tabBaiXeLayout);
        tabBaiXeLayout.setHorizontalGroup(
            tabBaiXeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabBaiXeLayout.createSequentialGroup()
                .addGap(417, 417, 417)
                .addComponent(jLabel18)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabBaiXeLayout.createSequentialGroup()
                .addContainerGap(41, Short.MAX_VALUE)
                .addGroup(tabBaiXeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(tabBaiXeLayout.createSequentialGroup()
                        .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(74, 74, 74))
                    .addGroup(tabBaiXeLayout.createSequentialGroup()
                        .addGroup(tabBaiXeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(tabBaiXeLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(tabBaiXeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(tabBaiXeLayout.createSequentialGroup()
                                        .addComponent(jLabel23)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(labelSucChuaConLai)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel24))
                                    .addComponent(jLabel22)))
                            .addGroup(tabBaiXeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(66, 66, 66))))
        );
        tabBaiXeLayout.setVerticalGroup(
            tabBaiXeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabBaiXeLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel18)
                .addGap(34, 34, 34)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(tabBaiXeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tabBaiXeLayout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addComponent(jLabel22)
                        .addGap(7, 7, 7)
                        .addGroup(tabBaiXeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labelSucChuaConLai)
                            .addComponent(jLabel23)
                            .addComponent(jLabel24))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(49, Short.MAX_VALUE))
        );

        jTabbedPane.addTab("BÃI XE", parkingZoneIcon, tabBaiXe);

        tabNhanXe.setBackground(new java.awt.Color(255, 255, 255));

        tbNhanXe.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1", "A", "A", "A", null, "A", null},
                {null, null, null, null, null, "", null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Bãi xe", "Loại xe", "Hãng xe", "Biển số xe", "Giờ nhận xe", "Ngày nhận xe"
            }
        ));
        tbNhanXe.setToolTipText("");
        tbNhanXe.setFocusable(false);
        tbNhanXe.setGridColor(new java.awt.Color(204, 102, 0));
        tbNhanXe.setSelectionBackground(new java.awt.Color(232, 57, 95));
        tbNhanXe.setSelectionForeground(new java.awt.Color(102, 51, 0));
        tbNhanXe.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tbNhanXe.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tbNhanXe.setShowGrid(false);
        tbNhanXe.setShowVerticalLines(true);
        jScrollPane2.setViewportView(tbNhanXe);
        tbNhanXe.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        if (tbNhanXe.getColumnModel().getColumnCount() > 0) {
            tbNhanXe.getColumnModel().getColumn(0).setPreferredWidth(10);
            tbNhanXe.getColumnModel().getColumn(1).setPreferredWidth(50);
            tbNhanXe.getColumnModel().getColumn(2).setPreferredWidth(50);
            tbNhanXe.getColumnModel().getColumn(3).setPreferredWidth(50);
            tbNhanXe.getColumnModel().getColumn(5).setPreferredWidth(50);
            tbNhanXe.getColumnModel().getColumn(6).setPreferredWidth(50);
        }

        jlbTitle.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jlbTitle.setText("NHẬN XE VÀO BÃI");

        jPanel3.setBackground(new java.awt.Color(204, 204, 255));

        btnSort.setText("SẮP XẾP");

        cbbTypeSort.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Theo ID", "Theo Bãi xe", "Theo Loại xe", "Theo Hãng xe", "Theo Biển số xe", "Theo Thời gian nhận xe" }));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(btnSort, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(cbbTypeSort, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSort)
                    .addComponent(cbbTypeSort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(153, 255, 255));

        btnAdd.setText("Thêm xe");

        btnEdit.setText("Sửa thông tin");

        btnDelete.setText("Xóa xe");

        btnClear.setText("Clear thông tin");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnClear, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnEdit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAdd)
                    .addComponent(btnEdit))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDelete)
                    .addComponent(btnClear))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(255, 153, 153));

        ljbSearch.setText("Tìm kiếm thông tin:");

        cbbTypeSearch.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Theo ID", "Theo Bãi xe", "Theo Loại xe", "Theo Hãng xe", "Theo Biển số xe" }));

        txtSearch.setToolTipText("");

        btnRefresh.setText("X");

        btnSearch.setText("Tìm kiếm");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ljbSearch)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbbTypeSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnRefresh)
                .addGap(18, 18, 18)
                .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(54, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ljbSearch)
                    .addComponent(cbbTypeSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRefresh)
                    .addComponent(btnSearch))
                .addGap(25, 25, 25))
        );

        jPanel6.setBackground(new java.awt.Color(204, 255, 204));

        jlbID.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jlbID.setText("ID");

        txtID.setEditable(false);
        txtID.setBackground(new java.awt.Color(204, 204, 204));
        txtID.setFont(new java.awt.Font("Josefin Sans", 0, 14)); // NOI18N
        txtID.setMargin(new java.awt.Insets(5, 6, 2, 6));
        jScrollPane1.setViewportView(txtID);

        jlbBaiXe.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jlbBaiXe.setText("Bãi xe");

        cbbBaiXe.setFont(new java.awt.Font("Josefin Sans", 0, 14)); // NOI18N
        cbbBaiXe.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Bãi số 1", "Bãi số 2", "Bãi số 3" }));
        List<ParkingLot> listParkingLots= parkingLotDao.getListParkingLots();
        String[] comboBoxName=new String[listParkingLots.size()];
        for(int i=0;i<listParkingLots.size();i++){
            comboBoxName[i]=listParkingLots.get(i).getName();
        }
        cbbBaiXe.setModel(new javax.swing.DefaultComboBoxModel<>(comboBoxName));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Loại xe");

        cbbLoaiXe.setFont(new java.awt.Font("Josefin Sans", 0, 14)); // NOI18N
        cbbLoaiXe.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Xe máy", "Ô tô", "Xe đạp điện", "Xe máy điện", "Ô tô điện" }));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Hãng xe");

        txtHangXe.setFont(new java.awt.Font("Josefin Sans", 0, 14)); // NOI18N
        jScrollPane7.setViewportView(txtHangXe);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("Biển số xe");

        txtBienSoXe.setFont(new java.awt.Font("Josefin Sans", 0, 14)); // NOI18N
        jScrollPane8.setViewportView(txtBienSoXe);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setText("Giờ nhận xe");

        JSpinner.DateEditor de=new JSpinner.DateEditor(timeNhanXe, "HH:mm:ss");
        timeNhanXe.setEditor(de);

        dateNhanXe.setDateFormatString("dd/MM/yyyy");
        dateNhanXe.setInheritsPopupMenu(true);

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setText("Ngày nhận xe");

        jLabel21.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        jLabel21.setText("(Ví dụ: 99A-12345; 29B-1234; 99G1-66666; 30LD-55555...)");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel6Layout.createSequentialGroup()
                                    .addComponent(jLabel2)
                                    .addGap(90, 90, 90))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                                    .addComponent(jlbID)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(cbbLoaiXe, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jlbBaiXe)
                                .addGap(18, 18, 18)
                                .addComponent(cbbBaiXe, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(130, 130, 130))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jScrollPane7)
                                .addGap(10, 10, 10))))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel6)
                            .addComponent(jLabel5)
                            .addComponent(timeNhanXe)
                            .addComponent(dateNhanXe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel21)))
                        .addContainerGap())))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jlbID))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(cbbBaiXe, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jlbBaiXe)))))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbbLoaiXe, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jLabel4))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel21)
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(timeNhanXe, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dateNhanXe, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );

        jPanel7.setBackground(new java.awt.Color(102, 204, 255));

        btnTraXe.setText("TRẢ XE ");

        btnThongKeLoaiXe.setText("THỐNG KÊ LOẠI XE");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(15, Short.MAX_VALUE)
                .addComponent(btnThongKeLoaiXe, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnTraXe, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTraXe)
                    .addComponent(btnThongKeLoaiXe))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout tabNhanXeLayout = new javax.swing.GroupLayout(tabNhanXe);
        tabNhanXe.setLayout(tabNhanXeLayout);
        tabNhanXeLayout.setHorizontalGroup(
            tabNhanXeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabNhanXeLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(tabNhanXeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tabNhanXeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 732, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(tabNhanXeLayout.createSequentialGroup()
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(tabNhanXeLayout.createSequentialGroup()
                        .addGap(492, 492, 492)
                        .addComponent(jlbTitle)))
                .addGap(31, 31, 31)
                .addGroup(tabNhanXeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        tabNhanXeLayout.setVerticalGroup(
            tabNhanXeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabNhanXeLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(tabNhanXeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tabNhanXeLayout.createSequentialGroup()
                        .addComponent(jlbTitle)
                        .addGap(26, 26, 26)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(tabNhanXeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(tabNhanXeLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabNhanXeLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(3, 3, 3))))
                    .addGroup(tabNhanXeLayout.createSequentialGroup()
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(58, Short.MAX_VALUE))
        );

        jTabbedPane.addTab("NHẬN XE", receiveCarIcon, tabNhanXe);

        tabTraXe.setBackground(new java.awt.Color(255, 255, 255));

        tbTraXe.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1", "A", "A", "A", null, "A", null, null, null, null},
                {null, null, null, null, null, "", null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Bãi xe", "Loại xe", "Hãng xe", "Biển số xe", "Giờ nhận xe", "Ngày nhận xe", "Giờ trả xe", "Ngày trả xe", "Phí gửi xe"
            }
        ));
        tbTraXe.setToolTipText("");
        tbTraXe.setFocusable(false);
        tbTraXe.setSelectionBackground(new java.awt.Color(232, 57, 95));
        tbTraXe.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tbTraXe.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tbTraXe.setShowGrid(false);
        tbTraXe.setShowVerticalLines(true);
        jScrollPane4.setViewportView(tbTraXe);
        if (tbTraXe.getColumnModel().getColumnCount() > 0) {
            tbTraXe.getColumnModel().getColumn(0).setPreferredWidth(10);
            tbTraXe.getColumnModel().getColumn(1).setPreferredWidth(50);
            tbTraXe.getColumnModel().getColumn(2).setPreferredWidth(50);
            tbTraXe.getColumnModel().getColumn(3).setPreferredWidth(50);
            tbTraXe.getColumnModel().getColumn(5).setPreferredWidth(50);
            tbTraXe.getColumnModel().getColumn(6).setPreferredWidth(50);
        }

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel13.setText("TRẢ XE RỜI BÃI");

        jPanel1.setBackground(new java.awt.Color(255, 153, 153));

        jLabel8.setText("Tìm kiếm theo thời gian:");

        dateSearchStart.setDateFormatString("dd/MM/yyyy HH:mm:ss");

        dateSearchEnd.setDateFormatString("dd/MM/yyyy HH:mm:ss");

        jLabel11.setText("đến");

        btnRefreshSearchDate.setText("X");

        btnSearchDate.setText("Tìm kiếm");

        jLabel20.setText("Từ");

        cbbTypeDate.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Ngày nhận xe", "Ngày trả xe" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cbbTypeDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dateSearchStart, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(jLabel11)
                .addGap(33, 33, 33)
                .addComponent(dateSearchEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnRefreshSearchDate)
                .addGap(18, 18, 18)
                .addComponent(btnSearchDate)
                .addGap(34, 34, 34))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel8)
                        .addComponent(jLabel20)
                        .addComponent(cbbTypeDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel11)
                    .addComponent(dateSearchStart, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dateSearchEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnRefreshSearchDate)
                        .addComponent(btnSearchDate)))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(153, 255, 204));

        jLabel9.setText("Tìm kiếm theo số tiền:                              Từ:");

        jLabel10.setText("đến");

        btnRefreshSearchPayment.setText("X");

        btnSearchPayment.setText("Tìm kiếm");

        jLabel12.setText("(VNĐ)");

        jLabel14.setText("(VNĐ)");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtMinPayment, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel12)
                .addGap(31, 31, 31)
                .addComponent(jLabel10)
                .addGap(18, 18, 18)
                .addComponent(txtMaxPayment, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnRefreshSearchPayment)
                .addGap(18, 18, 18)
                .addComponent(btnSearchPayment)
                .addGap(33, 33, 33))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtMinPayment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(txtMaxPayment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRefreshSearchPayment)
                    .addComponent(btnSearchPayment)
                    .addComponent(jLabel12)
                    .addComponent(jLabel14))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        jPanel12.setBackground(new java.awt.Color(204, 204, 255));

        cbbTypeSortTraXe.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Theo ID", "Theo Bãi xe", "Theo Loại xe", "Theo Biển số xe", "Theo Thời gian nhận xe", "Theo Thời gian trả xe", "Theo Phí gửi xe" }));

        btnSortTraXe.setText("SẮP XẾP");

        btnThongKeSoTien.setText("THỐNG KÊ SỐ TIỀN");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(btnSortTraXe)
                .addGap(29, 29, 29)
                .addComponent(cbbTypeSortTraXe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(btnThongKeSoTien, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(46, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap(16, Short.MAX_VALUE)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbbTypeSortTraXe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSortTraXe)
                    .addComponent(btnThongKeSoTien))
                .addGap(13, 13, 13))
        );

        jLabel25.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel25.setText("ĐƠN GIÁ:");

        jLabel26.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel26.setText("Xe đạp điện, Xe máy điện, Xe máy: 300 đ/ giờ");

        jLabel27.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel27.setText("Ô tô, Ô tô điện: 10.000 đ/ giờ");

        javax.swing.GroupLayout tabTraXeLayout = new javax.swing.GroupLayout(tabTraXe);
        tabTraXe.setLayout(tabTraXeLayout);
        tabTraXeLayout.setHorizontalGroup(
            tabTraXeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(tabTraXeLayout.createSequentialGroup()
                .addGap(507, 507, 507)
                .addComponent(jLabel13)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabTraXeLayout.createSequentialGroup()
                .addContainerGap(165, Short.MAX_VALUE)
                .addGroup(tabTraXeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(100, 100, 100))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabTraXeLayout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(jLabel25)
                .addGap(18, 18, 18)
                .addGroup(tabTraXeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel26)
                    .addComponent(jLabel27))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );
        tabTraXeLayout.setVerticalGroup(
            tabTraXeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabTraXeLayout.createSequentialGroup()
                .addGroup(tabTraXeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(tabTraXeLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(tabTraXeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel25)
                            .addComponent(jLabel26))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel27)
                        .addGap(6, 6, 6))
                    .addGroup(tabTraXeLayout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(jLabel13)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(47, 47, 47))
        );

        jTabbedPane.addTab("TRẢ XE", backCarIcon, tabTraXe);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // <editor-fold defaultstate="collapsed" desc="Khai báo các biến">    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnAddParkingLot;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnClearParkingLot;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnDeleteParkingLot;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnEditParkingLot;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnRefreshSearchDate;
    private javax.swing.JButton btnRefreshSearchParkingLot;
    private javax.swing.JButton btnRefreshSearchPayment;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnSearchDate;
    private javax.swing.JButton btnSearchParkingLot;
    private javax.swing.JButton btnSearchPayment;
    private javax.swing.JButton btnSort;
    private javax.swing.JButton btnSortTraXe;
    private javax.swing.JButton btnThongKeLoaiXe;
    private javax.swing.JButton btnThongKeSoTien;
    private javax.swing.JButton btnTraXe;
    private javax.swing.JComboBox<String> cbbBaiXe;
    private javax.swing.JComboBox<String> cbbLoaiXe;
    private javax.swing.JComboBox<String> cbbTypeDate;
    private javax.swing.JComboBox<String> cbbTypeSearch;
    private javax.swing.JComboBox<String> cbbTypeSearchParkingLot;
    private javax.swing.JComboBox<String> cbbTypeSort;
    private javax.swing.JComboBox<String> cbbTypeSortTraXe;
    private com.toedter.calendar.JDateChooser dateNhanXe;
    private com.toedter.calendar.JDateChooser dateSearchEnd;
    private com.toedter.calendar.JDateChooser dateSearchStart;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JTabbedPane jTabbedPane;
    private javax.swing.JLabel jlbBaiXe;
    private javax.swing.JLabel jlbID;
    private javax.swing.JLabel jlbSoXeDangChua;
    private javax.swing.JLabel jlbTitle;
    private javax.swing.JLabel labelSucChuaConLai;
    private javax.swing.JLabel ljbSearch;
    private javax.swing.JPanel tabBaiXe;
    private javax.swing.JPanel tabHome;
    private javax.swing.JPanel tabNhanXe;
    private javax.swing.JPanel tabTraXe;
    private javax.swing.JTable tbInfor;
    private javax.swing.JTable tbNhanXe;
    private javax.swing.JTable tbParkingLot;
    private javax.swing.JTable tbTraXe;
    private javax.swing.JSpinner timeNhanXe;
    private javax.swing.JTextPane txtBienSoXe;
    private javax.swing.JTextPane txtHangXe;
    private javax.swing.JTextPane txtID;
    private javax.swing.JTextField txtIDParkingLot;
    private javax.swing.JTextField txtMaxPayment;
    private javax.swing.JTextField txtMinPayment;
    private javax.swing.JTextField txtNameParkingLot;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtSearchParkingLot;
    private javax.swing.JTextField txtSoXeDangChua;
    private javax.swing.JTextField txtSucChua;
    // End of variables declaration//GEN-END:variables
    // </editor-fold>   
    
    // <editor-fold defaultstate="collapsed" desc="tab Trả Xe">    
    public void showListVehicleBacks(List<VehicleBack> listBack) {
        int size = listBack.size();
        // với bảng vehicleBackTable có 10 cột, 
        // khởi tạo mảng 2 chiều vehicles, trong đó:
        // số hàng: là kích thước của list vehicleBack
        // số cột: là 10
        Object [][] vehicleBacks = new Object[size][10];
        for (int i = 0; i < size; i++) {
            vehicleBacks[i][0] = listBack.get(i).getId();
            vehicleBacks[i][1] = listBack.get(i).getParkingLot();           
            vehicleBacks[i][2] = listBack.get(i).getType();
            vehicleBacks[i][3] = listBack.get(i).getBrand();
            vehicleBacks[i][4] = listBack.get(i).getBienSoXe();
            
            //Chuyển từ Date sang String
            Date dateNhanXe = listBack.get(i).getTimeEnter();

            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss" );
            String timeString = timeFormat.format(dateNhanXe);
            vehicleBacks[i][5] = timeString;
            
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String dateString = dateFormat.format(dateNhanXe);
            vehicleBacks[i][6] = dateString;
            
            Date dateTraXe = listBack.get(i).getTimeExit();
            String timeString2 = timeFormat.format(dateTraXe);
            vehicleBacks[i][7] = timeString2;
            
            String dateString2 = dateFormat.format(dateTraXe);
            vehicleBacks[i][8] = dateString2;
            
            Locale locale = new Locale("vi", "VN");
            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
            vehicleBacks[i][9] = currencyFormatter.format(listBack.get(i).getFee());
        }
        tbTraXe.setModel(new DefaultTableModel(vehicleBacks, columnsName_TraXe));
    }
    
    /**
     * Lấy thông tin vehicleBack
     * 
     * @return
     */
    public VehicleBack getVehicleBackInfo() {
        // validate vehicle
        if ( !validateBienSoXe()
                || !validateEnterTime()
                || !validateDate()) {
            return null;
        }
        try {
            VehicleBack vehicleBack = new VehicleBack();
            if (txtID.getText() != null && !"".equals(txtID.getText())) {
                vehicleBack.setId(Integer.parseInt(txtID.getText()));
            }
            
            vehicleBack.setParkingLot(cbbBaiXe.getSelectedItem().toString());
            vehicleBack.setType(cbbLoaiXe.getSelectedItem().toString());
            vehicleBack.setBrand(txtHangXe.getText().trim());
            vehicleBack.setBienSoXe(txtBienSoXe.getText().trim());
            
            // Lấy giá trị từ ô nhập thời gian (JSpinner)
            Date timeValue = (Date) timeNhanXe.getValue();
            // Lấy giá trị từ ô nhập ngày (JDateChooser)
            Date dateValue = dateNhanXe.getDate();
            
            // Tạo một đối tượng Calendar và thiết lập giá trị ngày và giờ
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateValue);
            calendar.set(Calendar.HOUR_OF_DAY, timeValue.getHours());
            calendar.set(Calendar.MINUTE, timeValue.getMinutes());
            calendar.set(Calendar.SECOND, timeValue.getSeconds());
            
            // Lưu giá trị vào biến kiểu Date
            Date combinedValue = calendar.getTime();
            vehicleBack.setTimeEnter(combinedValue);
            
            Calendar exitCalendar = Calendar.getInstance();
            Date currentTime = new Date(); // Lấy thời gian hiện tại
            exitCalendar.setTime(currentTime);
            Date exitTime = exitCalendar.getTime();
            vehicleBack.setTimeExit(exitTime);
            
            // Lấy thời gian ra và vào của xe
            Date timeEnter = vehicleBack.getTimeEnter();
            Date timeExit = vehicleBack.getTimeExit();

            // Tính thời gian chênh lệch (đơn vị: milliseconds)
            long timeDifferenceMillis = timeExit.getTime() - timeEnter.getTime();

            // Chuyển thời gian chênh lệch từ milliseconds sang giờ
            double timeDifferenceHours = timeDifferenceMillis / (1000 * 60 * 60) + 1; // milliseconds to hours
            
            double pricePerHour = 300;
            // Số tiền cho mỗi giờ 
            // Loại xe đạp, xe máy thì 300đ/h (tham khảo giá trên Internet thì 7-10k/ ngày)
            // Ô tô thì 10.000 đ/ h (tham khảo Internet 30.000-50.000đ/3h)
            if (vehicleBack.getType().equals("Ô tô") 
                    || vehicleBack.getType().equals("Ô tô điện")){
                pricePerHour = 10000; // 10.000đ/ 1h
            } 
            else pricePerHour = 300;
            double fee = timeDifferenceHours * pricePerHour;
            
            vehicleBack.setFee(fee);
            
            return vehicleBack;
        } 
        catch (NumberFormatException e) 
        {
            showMessage(e.getMessage());
        }
        return null;
    }
    
    // Search khoảng Date
    public void addSearchDateListener(ActionListener listener) {
        btnSearchDate.addActionListener(listener);
    }
    
    public Date getSearchTimeStart(){
        return dateSearchStart.getDate();
    }
    
     public Date getSearchTimeEnd(){
        return dateSearchEnd.getDate();
    }

    public void setDateSearchEnd(Date dateSearchEnd) {
        this.dateSearchEnd.setDate(dateSearchEnd);
    }

    public void setDateSearchStart(Date dateSearchStart) {
        this.dateSearchStart.setDate(dateSearchStart);
    }
       
    public String getTypeDate(){
        return cbbTypeDate.getSelectedItem().toString();
    }
    
    // Refresh search khoảng Date
    public void addRefreshSearchDateListener(ActionListener listener) {
        btnRefreshSearchDate.addActionListener(listener);
    }
    
    public String getTxtMaxPayment() {
        return txtMaxPayment.getText().trim();
    }

    public String getTxtMinPayment() {
        return txtMinPayment.getText().trim();
    }

    public void setTxtMaxPayment(String txtMaxPayment) {
        this.txtMaxPayment.setText(txtMaxPayment);
    }

    public void setTxtMinPayment(String txtMinPayment) {
        this.txtMinPayment.setText(txtMinPayment);
    }
    
    // Search khoảng tiền
    public void addSearchPaymentListener(ActionListener listener) {
        btnSearchPayment.addActionListener(listener);
    }
    
    // Refresh search khoảng tiền
    public void addRefreshSearchPaymentListener(ActionListener listener) {
        btnRefreshSearchPayment.addActionListener(listener);
    }
    
    // Sort tb Trả xe
    public void addSortTraXeListener(ActionListener listener) {
        btnSortTraXe.addActionListener(listener);
    }
    
    public String getTypeSortTraXe(){
        return cbbTypeSortTraXe.getSelectedItem().toString();
    }
    
    // Thống kê số tiền
    public void addThongKeSoTienListener(ActionListener listener) {
        btnThongKeSoTien.addActionListener(listener);
    }
    // </editor-fold>   
    
    // <editor-fold defaultstate="collapsed" desc="tab Bãi Xe">   
    /**
     * hiển thị list parkingLot vào bảng parkingLotTable
     * 
     * @param list
     */
    public void showListParkingLots(List<ParkingLot> list) {
        int size = list.size();
        // với bảng parkingLotTable có 4 cột, 
        // khởi tạo mảng 2 chiều parkingLots, trong đó:
        // số hàng: là kích thước của list parkingLot 
        // số cột: là 4
        Object [][] parkingLots = new Object[size][4];
        for (int i = 0; i < size; i++) {
            parkingLots[i][0] = list.get(i).getIdParkingLot();
            parkingLots[i][1] = list.get(i).getName();
            parkingLots[i][2] = list.get(i).getSucChua();
            parkingLots[i][3] = list.get(i).getSoXeDangChua();
        }
        tbParkingLot.setModel(new DefaultTableModel(parkingLots,columnsName_BaiXe));
    }
    
     /**
     * điền thông tin của hàng được chọn từ bảng parkingLot 
     * vào các trường tương ứng của parkingLot.
     */
    public void fillParkingLotFromSelectedRow()  {
        // lấy chỉ số của hàng được chọn 
        int row = tbParkingLot.getSelectedRow();
        if (row >= 0) {
            txtIDParkingLot.setText(tbParkingLot.getModel().getValueAt(row, 0).toString());
            txtNameParkingLot.setText(tbParkingLot.getModel().getValueAt(row, 1).toString());
            txtSucChua.setText(tbParkingLot.getModel().getValueAt(row, 2).toString());
            txtSoXeDangChua.setText(tbParkingLot.getModel().getValueAt(row, 3).toString());
            
            // enable Edit and Delete buttons
            btnEditParkingLot.setEnabled(true);
            btnDeleteParkingLot.setEnabled(true);
            // disable Add button
            btnAddParkingLot.setEnabled(false);
        }
    }
    
    /**
     * xóa thông tin parkingLot
     */
    public void clearParkingLotInfo() {
        txtIDParkingLot.setText("");
        txtNameParkingLot.setText("");
        txtSucChua.setText("");
        txtSoXeDangChua.setText("");
        
        // disable Edit and Delete buttons
        btnEditParkingLot.setEnabled(false);
        btnDeleteParkingLot.setEnabled(false);
        // enable Add button
        btnAddParkingLot.setEnabled(true);
    }
    
    /**
     * Lấy thông tin parkingLot
     * 
     * @return
     */
    public ParkingLot getParkingLotInfo() {
        // validate parkingLot
        if (!validateSucChua()) {
            return null;
        }
        try {
            ParkingLot parkingLot = new ParkingLot();
            if (txtIDParkingLot.getText() != null && !"".equals(txtIDParkingLot.getText())) {
                parkingLot.setIdParkingLot(Integer.parseInt(txtIDParkingLot.getText()));
            }
            parkingLot.setName(txtNameParkingLot.getText().trim());
            parkingLot.setSucChua(Integer.parseInt(txtSucChua.getText().trim()));
            if (txtSoXeDangChua.getText() != null && !"".equals(txtSoXeDangChua.getText())) 
            {
                parkingLot.setSoXeDangChua(Integer.parseInt(txtSoXeDangChua.getText().trim()));
            }
            return parkingLot;
        } catch (NumberFormatException e) {
            showMessage(e.getMessage());
        } 
        return null;
    }
    
    private boolean validateSucChua() {
        try {
            int capacity = Integer.parseInt(txtSucChua.getText().trim());
            if (capacity <= 0) {
                txtSucChua.requestFocus();
                showMessage("Sức chứa không hợp lệ, sức chứa phải lớn hơn 0.");
                return false;
            }
        } catch (NumberFormatException e) {
            txtSucChua.requestFocus();
            showMessage("Sức chứa không hợp lệ!");
            return false;
        }
        return true;
    }
    
    public int getTxtSucChua(){
        return Integer.parseInt(txtSucChua.getText().trim());
    }
    
    public void setLabelSucChuaConLai(int sucChuaConLai){
        labelSucChuaConLai.setText(Integer.toString(sucChuaConLai));
    }
    
    public String getTypeSearchParkingLot(){
        return cbbTypeSearchParkingLot.getSelectedItem().toString();
    }
    
    public void setTxtSearchParkingLot(String newTxt){
        txtSearchParkingLot.setText(newTxt);
    }
     
    public String getTxtSearchParkingLot(){
        return txtSearchParkingLot.getText().trim();
    }
      
    public void addAddParkingLotListener(ActionListener listener) {
        btnAddParkingLot.addActionListener(listener);
    }
    
    public void addEdiParkingLotListener(ActionListener listener) {
        btnEditParkingLot.addActionListener(listener);
    }
    
    public void addDeleteParkingLotListener(ActionListener listener) {
        btnDeleteParkingLot.addActionListener(listener);
    }
    
    public void addClearParkingLotListener(ActionListener listener) {
        btnClearParkingLot.addActionListener(listener);
    }
    
    public void addSearchParkingLotListener(ActionListener listener) {
        btnSearchParkingLot.addActionListener(listener);
    }
    
    public void addRefreshSearchParkingLotListener(ActionListener listener) {
        btnRefreshSearchParkingLot.addActionListener(listener);
    }
    
    
    public void addListParkingLotSelectionListener(ListSelectionListener listener) {
        tbParkingLot.getSelectionModel().addListSelectionListener(listener);
    }
    
    public void refreshCbbBaiXe(){
        parkingLotDao.Update();

        List<ParkingLot> listParkingLots= parkingLotDao.getListParkingLots();
        Collections.sort(listParkingLots, new ParkingLotComparator());
        String[] comboBoxName=new String[listParkingLots.size()];
        for(int i=0;i<listParkingLots.size();i++){
            comboBoxName[i]=listParkingLots.get(i).getName();
        }
        cbbBaiXe.setModel(new javax.swing.DefaultComboBoxModel<>(comboBoxName));
    }
    
    // sắp xếp bãi xe.
    class ParkingLotComparator implements Comparator<ParkingLot> {
      @Override
      public int compare(ParkingLot lot1, ParkingLot lot2) {
          // So sánh theo tên hoặc thuộc tính khác của ParkingLot
          return lot1.getName().compareTo(lot2.getName());
      }
    }
}
    // </editor-fold>

    
    

