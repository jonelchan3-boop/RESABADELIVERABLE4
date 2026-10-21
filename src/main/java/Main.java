package com.scc;

import com.scc.dao.DatabaseInitializer;
import com.scc.dao.UserDao;
import com.scc.model.User;
import com.scc.ui.Authentication;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        DatabaseInitializer.init();

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== DRINKING WATER REFILL & DELIVERY SYSTEM =====");
            System.out.println("1. LOGIN");
            System.out.println("2. REGISTER");
            System.out.println("3. EXIT");
            System.out.print("Enter choice: ");

            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    new Authentication().start();
                    break;

                case "2":
                    System.out.println("\n---------- USER REGISTRATION ----------");
                    System.out.print("Create Username: ");
                    String regUname = sc.nextLine().trim();
                    if (regUname.isEmpty()) {
                        System.out.println("[!] Username cannot be empty.");
                        break;
                    }
                    System.out.print("Create Password: ");
                    String regPwd = sc.nextLine().trim();
                    if (regPwd.isEmpty()) {
                        System.out.println("[!] Password cannot be empty.");
                        break;
                    }

                    User regUser = new User(regUname, regPwd, "CUSTOMER", "PENDING");
                    UserDao regDao = new UserDao();

                    if (regDao.addUser(regUser)) {
                        System.out.println("[SUCCESS] Registration submitted!");
                        System.out.println("Wait for ADMIN approval before logging in.");
                    } else {
                        System.out.println("[FAILED] Registration unsuccessful. Username may exist.");
                    }
                    break;

                case "3":
                    System.out.println("Exiting... Goodbye!");
                    sc.close();
                    return;

                default:
                    System.out.println("[INVALID CHOICE] Please select 1, 2, or 3 only.");
            }
        }
    }
}