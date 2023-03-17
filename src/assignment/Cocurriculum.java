/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment;

import java.sql.*;

/**
 *
 * @author elfhc
 */
public class Cocurriculum extends Course {

    private String category;

    public Cocurriculum() {
    }

    public Cocurriculum(String courseName, String description, double fee, String category) {
        super(courseName, description, fee);
        this.category = category;
    }

    public Cocurriculum(String courseCode, String courseName, String description, int creditHours, String category, double fee, String status) {
        super(courseCode, courseName, description, creditHours, fee, status);
        this.category = category;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public static boolean checkDuplicateCocu(String courseName) {
        Connection con = Assignment.getConnection();

        try {
            String query = "SELECT * FROM cocurriculum WHERE courseName = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, courseName);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.getStackTrace();
        }
        return false;
    }

    public static boolean checkDuplicateCocuCode(String courseCode) {
        Connection con = Assignment.getConnection();

        try {
            String query = "SELECT * FROM cocurriculum WHERE courseCode = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, courseCode);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.getStackTrace();
        }
        return false;
    }

    public static Cocurriculum getCocurriculum(String courseCode) {
        Connection con = Assignment.getConnection();

        try {
            String query = "SELECT * FROM cocurriculum WHERE courseCode = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, courseCode);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                Cocurriculum cocu = new Cocurriculum(rs.getString("courseCode"), rs.getString("courseName"),
                        rs.getString("description"), rs.getInt("creditHours"), rs.getString("category"), rs.getDouble("fee"), rs.getString("status"));
                return cocu;
            }
        } catch (SQLException e) {
            e.getStackTrace();
        }
        return null;
    }

    public static String chooseCocuCategory() {

        System.out.println("Enter Course Category");
        System.out.println("[1] Games/Sports");
        System.out.println("[2] Cultural Studies");
        System.out.println("[3] Community Service");
        System.out.println("[4] Volunteerism");

        String cocuCategory;
        do {
            System.out.printf("%-50s: ", "Enter Choice");
            cocuCategory = Helper.readLine().trim();
            switch (cocuCategory) {
                case "1":
                    return "GAMES/SPORTS";
                case "2":
                    return "CULTURAL STUDIES";
                case "3":
                    return "COMMUNITY SERVICE";
                case "4":
                    return "VOLUNTEERISM";
                default:
                    System.out.println("Please enter a valid number.");
            }
        } while (true);
    }

    public static void addCocurriculum(Cocurriculum newCocu) {
        Connection con = Assignment.getConnection();

        try {
            String query = "INSERT INTO cocurriculum (courseName, description, creditHours, category, fee, status) VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, newCocu.getCourseName());
            pst.setString(2, newCocu.getDescription());
            pst.setInt(3, newCocu.getCreditHours());
            pst.setString(4, newCocu.getCategory());
            pst.setDouble(5, newCocu.getFee());
            pst.setString(6, newCocu.getStatus());

            pst.executeUpdate();
            con.close();
        } catch (SQLException e) {
            e.getStackTrace();
        }
    }

    public static void updateCocurriculum(Cocurriculum cocu) {
        Connection con = Assignment.getConnection();

        try {
            String query = "UPDATE cocurriculum SET courseName=?, description=?, creditHours=?, category=?, fee=?, status=? WHERE courseCode=?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, cocu.getCourseName());
            pst.setString(2, cocu.getDescription());
            pst.setInt(3, cocu.getCreditHours());
            pst.setString(4, cocu.getCategory());
            pst.setDouble(5, cocu.getFee());
            pst.setString(6, cocu.getStatus());
            pst.setString(7, cocu.getCourseCode());

            pst.execute();
            con.close();
        } catch (SQLException e) {
            e.getStackTrace();
        }
    }

    public static int getActive() {
        Connection con = Assignment.getConnection();
        try {
            String query = "SELECT COUNT(status) FROM cocurriculum WHERE status = ? GROUP BY status";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, "AVAILABLE");
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.getStackTrace();
        }
        return 0;
    }

    public static int getInactive() {
        Connection con = Assignment.getConnection();
        try {
            String query = "SELECT COUNT(status) FROM cocurriculum WHERE status = ? GROUP BY status";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, "UNAVAILABLE");
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.getStackTrace();
        }
        return 0;
    }

    public static int getGamesSports() {
        Connection con = Assignment.getConnection();
        try {
            String query = "SELECT COUNT(category) FROM cocurriculum WHERE category = ? GROUP BY category";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, "GAMES/SPORTS");
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.getStackTrace();
        }
        return 0;
    }

    public static int getCulturalStudies() {
        Connection con = Assignment.getConnection();
        try {
            String query = "SELECT COUNT(category) FROM cocurriculum WHERE category = ? GROUP BY category";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, "CULTURAL STUDIES");
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.getStackTrace();
        }
        return 0;
    }

    public static int getCommunityService() {
        Connection con = Assignment.getConnection();
        try {
            String query = "SELECT COUNT(category) FROM cocurriculum WHERE category = ? GROUP BY category";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, "COMMUNITY SERVICE");
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.getStackTrace();
        }
        return 0;
    }

    public static int getVolunteerism() {
        Connection con = Assignment.getConnection();
        try {
            String query = "SELECT COUNT(category) FROM cocurriculum WHERE category = ? GROUP BY category";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, "VOLUNTEERISM");
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.getStackTrace();
        }
        return 0;
    }

    public static double calPercentage(String type) {
        int total = Cocurriculum.getActive() + Cocurriculum.getInactive();
        double percent;
        if (type.equals("AVAILABLE")) {
            percent = Cocurriculum.getActive() / (double) total * 100.0;
        } else if (type.equals("UNAVAILABLE")) {
            percent = Cocurriculum.getInactive() / (double) total * 100.0;
        } else if (type.equals("GAMES/SPORTS")) {
            percent = Cocurriculum.getGamesSports() / (double) total * 100.0;
        } else if (type.equals("CULTURAL STUDIES")) {
            percent = Cocurriculum.getCulturalStudies() / (double) total * 100.0;
        } else if (type.equals("COMMUNITY SERVICE")) {
            percent = Cocurriculum.getCommunityService() / (double) total * 100.0;
        } else {
            percent = Cocurriculum.getVolunteerism() / (double) total * 100.0;
        }
        return percent;
    }

    @Override
    public String toString() {
        return String.format("%12.12s ║ %-30s ║ %-50s ║ %-20s ║ %-12s ║ %-15.2f ║ %-12s", 
                getCourseCode(), getCourseName(), getDescription(), category, getCreditHours(), getFee(), getStatus());
    }
}
