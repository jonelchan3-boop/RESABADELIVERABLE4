package com.scc.dao;

import com.scc.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    private static final String[] VALID_ROLES = {
            "ADMIN", "STAFF/CASHIER", "DELIVERY RIDER", "CUSTOMER"
    };

    private boolean isValidRole(String role) {
        for (String r : VALID_ROLES) {
            if (r.equals(role)) return true;
        }
        return false;
    }

    // Add User
    public boolean addUser(User user) {
        if (!isValidRole(user.getRole())) {
            System.out.println("[!] Invalid role! Choose: ADMIN | STAFF/CASHIER | DELIVERY RIDER | CUSTOMER");
            return false;
        }

        String sql = "INSERT INTO Users (Username, Password, Role, AccountStatus) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getRole());
            pstmt.setString(4, user.getAccountStatus() != null ? user.getAccountStatus() : "PENDING");

            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("[✗] Add User Error: " + e.getMessage());
            return false;
        }
    }

    // Get User by Username
    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM Users WHERE Username = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                User u = new User();
                u.setUserId(rs.getInt("User_ID"));
                u.setUsername(rs.getString("Username"));
                u.setPassword(rs.getString("Password"));
                u.setRole(rs.getString("Role"));
                u.setAccountStatus(rs.getString("AccountStatus"));
                return u;
            }
        } catch (Exception e) {
            System.out.println("[✗] Fetch User Error: " + e.getMessage());
        }
        return null;
    }

    // Get All Users
    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM Users ORDER BY User_ID";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                User u = new User();
                u.setUserId(rs.getInt("User_ID"));
                u.setUsername(rs.getString("Username"));
                u.setRole(rs.getString("Role"));
                u.setAccountStatus(rs.getString("AccountStatus"));
                list.add(u);
            }
        } catch (Exception e) {
            System.out.println("[✗] Get All Users Error: " + e.getMessage());
        }
        return list;
    }

    // Get Pending Users
    public List<User> getPendingUsers() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM Users WHERE AccountStatus = 'PENDING' ORDER BY User_ID";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                User u = new User();
                u.setUserId(rs.getInt("User_ID"));
                u.setUsername(rs.getString("Username"));
                u.setRole(rs.getString("Role"));
                u.setAccountStatus(rs.getString("AccountStatus"));
                list.add(u);
            }
        } catch (Exception e) {
            System.out.println("[✗] Get Pending Error: " + e.getMessage());
        }
        return list;
    }

    // Update Status
    public boolean updateStatus(int userId, String newStatus) {
        String sql = "UPDATE Users SET AccountStatus = ? WHERE User_ID = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newStatus);
            pstmt.setInt(2, userId);
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("[✗] Update Status Error: " + e.getMessage());
            return false;
        }
    }

    // Update Role
    public boolean updateRole(int userId, String newRole) {
        if (!isValidRole(newRole)) return false;
        String sql = "UPDATE Users SET Role = ? WHERE User_ID = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newRole);
            pstmt.setInt(2, userId);
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("[✗] Update Role Error: " + e.getMessage());
            return false;
        }
    }

    // Delete User
    public boolean deleteUser(int userId) {
        String sql = "DELETE FROM Users WHERE User_ID = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("[✗] Delete Error: " + e.getMessage());
            return false;
        }
    }
}