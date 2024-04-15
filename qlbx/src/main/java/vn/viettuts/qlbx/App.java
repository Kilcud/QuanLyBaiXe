package vn.viettuts.qlbx;

import java.awt.EventQueue;

import vn.viettuts.qlbx.controller.LoginController;
import vn.viettuts.qlbx.view.LoginView;


public class App {
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                LoginView view = new LoginView();
                LoginController controller = new LoginController(view);
                // Hiển thị màn hình login
                controller.showLoginView();
            }
        });
    }
}