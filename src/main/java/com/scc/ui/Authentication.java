package com.scc.ui;

import com.scc.dao.UserDao;
import com.scc.model.User;
import java.util.Scanner;

public class Authentication {
    private Scanner sc = new Scanner(System.in);
    private UserDao userDao = new UserDao();

    public void start() {
        System.out.println("\n===== USER LOGIN =====");
        System.out.print("Username: ");
        String uname = sc.nextLine().trim();

        System.out.print("Password: ");
        String pwd = sc.nextLine().trim();

        User user = userDao.getUserByUsername(uname);

        if (user == null) {
            System.out.println("[✗] Username not found.");
            return;
        }

        if (!user.getPassword().equals(pwd)) {
            System.out.println("[✗] Incorrect password.");
            return;
        }

        if (!"ACTIVE".equals(user.getAccountStatus()) && !"APPROVED".equals(user.getAccountStatus())) {
            System.out.println("[!] Account not approved. Wait for admin approval.");
            return;
        }

        System.out.println("[✓] Login SUCCESSFUL! Welcome, " + user.getUsername() + " [" + user.getRole() + "]");

        switch (user.getRole()) {
            case "ADMIN":
                new AdminMenu().show();
                break;
            case "STAFF/CASHIER":
                System.out.println("[!] Staff/Cashier Menu — coming soon.");
                break;
            case "DELIVERY RIDER":
                System.out.println("[!] Delivery Rider Menu — coming soon.");
                break;
            case "CUSTOMER":
                System.out.println("[!] Customer Menu — coming soon.");
                break;
            default:
                System.out.println("[✗] Unknown role.");
        }
    }
}