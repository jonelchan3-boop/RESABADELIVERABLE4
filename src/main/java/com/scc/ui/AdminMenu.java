package com.scc.ui;

import com.scc.dao.UserDao;
import com.scc.model.User;
import java.util.List;
import java.util.Scanner;

public class AdminMenu {
    private Scanner sc = new Scanner(System.in);
    private UserDao userDao = new UserDao();

    public void show() {
        while (true) {
            System.out.println("\n=====================================");
            System.out.println("          ADMIN DASHBOARD            ");
            System.out.println("=====================================");
            System.out.println("1. View All Users");
            System.out.println("2. View Pending User Approvals");
            System.out.println("3. Approve User Registration");
            System.out.println("4. Add New User");
            System.out.println("5. Update User Role / Status");
            System.out.println("6. Delete User Account");
            System.out.println("7. Logout");
            System.out.print("Choose an option (1-7): ");

            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    viewAllUsers();
                    break;
                case "2":
                    viewPendingApprovals();
                    break;
                case "3":
                    approveRegistration();
                    break;
                case "4":
                    addNewUser();
                    break;
                case "5":
                    updateUserInfo();
                    break;
                case "6":
                    deleteUser();
                    break;
                case "7":
                    System.out.println("Logging out... Goodbye!");
                    return;
                default:
                    System.out.println("[!] Invalid choice! Try again.");
            }
        }
    }

    private void viewAllUsers() {
        System.out.println("\n--- ALL USERS ---");
        List<User> users = userDao.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("[!] No users found.");
            return;
        }
        System.out.printf("%-5s | %-15s | %-18s | %-15s%n", "ID", "USERNAME", "ROLE", "STATUS");
        System.out.println("-----------------------------------------------------------");
        for (User u : users) {
            System.out.printf("%-5d | %-15s | %-18s | %-15s%n",
                    u.getUserId(), u.getUsername(), u.getRole(), u.getAccountStatus());
        }
    }

    private void viewPendingApprovals() {
        System.out.println("\n--- PENDING USER APPROVALS ---");
        List<User> pending = userDao.getPendingUsers();
        if (pending.isEmpty()) {
            System.out.println("[!] No pending approvals.");
            return;
        }
        System.out.printf("%-5s | %-15s | %-18s%n", "ID", "USERNAME", "ROLE");
        System.out.println("------------------------------------------");
        for (User u : pending) {
            System.out.printf("%-5d | %-15s | %-18s%n", u.getUserId(), u.getUsername(), u.getRole());
        }
    }

    private void approveRegistration() {
        viewPendingApprovals();
        System.out.print("\nEnter User ID to approve: ");
        try {
            int id = Integer.parseInt(sc.nextLine().trim());
            if (userDao.updateStatus(id, "APPROVED")) {
                System.out.println("[✓] User APPROVED successfully!");
            } else {
                System.out.println("[✗] Failed to approve.");
            }
        } catch (NumberFormatException e) {
            System.out.println("[!] Invalid User ID.");
        }
    }

    private void addNewUser() {
        System.out.println("\n--- ADD NEW USER ---");
        System.out.print("Username: ");
        String uname = sc.nextLine().trim();
        if (uname.isEmpty()) {
            System.out.println("[!] Username cannot be empty.");
            return;
        }
        System.out.print("Password: ");
        String pwd = sc.nextLine().trim();
        if (pwd.isEmpty()) {
            System.out.println("[!] Password cannot be empty.");
            return;
        }
        System.out.print("Role (ADMIN | STAFF/CASHIER | DELIVERY RIDER | CUSTOMER): ");
        String role = sc.nextLine().trim().toUpperCase();

        // ✅ PILI: PENDING o APPROVED
        System.out.print("Account Status (PENDING | APPROVED): ");
        String statusInput = sc.nextLine().trim().toUpperCase();

        String status;
        if (statusInput.equals("APPROVED")) {
            status = "APPROVED";
        } else if (statusInput.equals("PENDING")) {
            status = "PENDING";
        } else {
            System.out.println("[!] Invalid status! Default set to PENDING.");
            status = "PENDING";
        }

        User newUser = new User(uname, pwd, role, status);
        if (userDao.addUser(newUser)) {
            System.out.println("[✓] User added! Status: " + status);
        } else {
            System.out.println("[✗] Failed — username exists or invalid role.");
        }
    }

    private void updateUserInfo() {
        viewAllUsers();
        System.out.print("\nEnter User ID to update: ");
        try {
            int id = Integer.parseInt(sc.nextLine().trim());

            System.out.print("Update Role? (Y/N): ");
            String updateRole = sc.nextLine().trim().toUpperCase();
            if (updateRole.equals("Y")) {
                System.out.print("New Role (ADMIN | STAFF/CASHIER | DELIVERY RIDER | CUSTOMER): ");
                String newRole = sc.nextLine().trim().toUpperCase();
                if (userDao.updateRole(id, newRole)) {
                    System.out.println("[✓] Role updated.");
                } else {
                    System.out.println("[✗] Invalid role.");
                }
            }

            System.out.print("Update Status? (Y/N): ");
            String updateStat = sc.nextLine().trim().toUpperCase();
            if (updateStat.equals("Y")) {
                System.out.print("New Status (PENDING | APPROVED | REJECTED | SUSPENDED): ");
                String newStat = sc.nextLine().trim().toUpperCase();
                if (newStat.equals("PENDING") || newStat.equals("APPROVED") || newStat.equals("REJECTED") || newStat.equals("SUSPENDED")) {
                    if (userDao.updateStatus(id, newStat)) {
                        System.out.println("[✓] Status updated to: " + newStat);
                    } else {
                        System.out.println("[✗] Failed to update.");
                    }
                } else {
                    System.out.println("[!] Invalid status.");
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("[!] Invalid User ID.");
        }
    }

    private void deleteUser() {
        viewAllUsers();
        System.out.print("\nEnter User ID to DELETE: ");
        try {
            int id = Integer.parseInt(sc.nextLine().trim());
            System.out.print("Are you sure? (YES/NO): ");
            String confirm = sc.nextLine().trim().toUpperCase();
            if (confirm.equals("YES")) {
                if (userDao.deleteUser(id)) {
                    System.out.println("[✓] User deleted.");
                } else {
                    System.out.println("[✗] Failed to delete.");
                }
            } else {
                System.out.println("[!] Delete cancelled.");
            }
        } catch (NumberFormatException e) {
            System.out.println("[!] Invalid User ID.");
        }
    }
}