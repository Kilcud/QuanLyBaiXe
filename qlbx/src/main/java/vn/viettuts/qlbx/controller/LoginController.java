package vn.viettuts.qlbx.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import vn.viettuts.qlbx.dao.UserDao;
import vn.viettuts.qlbx.entity.User;
import vn.viettuts.qlbx.view.Home;
import vn.viettuts.qlbx.view.LoginView;

public class LoginController {
    private final UserDao userDao;
    private final LoginView loginView;
    private Home home;
    
    public LoginController(LoginView view) {
        this.loginView = view;
        this.userDao = new UserDao();
        view.addLoginListener(new LoginListener());
    }
    
    public void showLoginView() {
        loginView.setVisible(true);
    }
    
    /**
     * Lớp LoginListener 
     * chứa cài đặt cho sự kiện click button "Login"
     * 
     * @author viettuts.vn
     */
    class LoginListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            User user = loginView.getUser();
            if (userDao.checkUser(user)) {
                // nếu đăng nhập thành công, mở màn hình HOME 
                home = new Home();
                HomeController homeController = new HomeController(home);
                homeController.showHome();
                
                loginView.setVisible(false);
            } else {
                loginView.showMessage("UserName hoặc Password không đúng.");
            }
        }
    }
}
