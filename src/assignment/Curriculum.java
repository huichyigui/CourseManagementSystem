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
public class Curriculum extends Course {

    public Curriculum() {
    }

    public Curriculum(String courseCode, String courseName, String description, int creditHours, double fee) {
        super(courseCode, courseName, description, creditHours, fee);
    }

    public Curriculum(String courseCode, String courseName, String description, int creditHours, double fee, String status) {
        super(courseCode, courseName, description, creditHours, fee, status);
    }

    public static boolean checkCourseCodeFormat(String courseCode) {
        if (courseCode.matches("\\p{Upper}{4}\\d{4}") || courseCode.equals("Q")) {
            return true;
        }
        System.out.println("Invalid input. Course code must be four letters followed by four digits");
        return false;
    }

    /**
     *
     * @param courseCode
     * @return
     */
    public static boolean checkDuplicateCode(String courseCode) {
        Connection con = Assignment.getConnection();

        try {
            String query = "SELECT * FROM curriculum WHERE courseCode = ?";
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

    public static Curriculum getCurriculum(String courseCode) {
        Connection con = Assignment.getConnection();

        try {
            String query = "SELECT * FROM curriculum WHERE courseCode = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, courseCode);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                Curriculum curr = new Curriculum(rs.getString("courseCode"), rs.getString("courseName"),
                        rs.getString("description"), rs.getInt("creditHours"), rs.getDouble("fee"), rs.getString("status"));
                return curr;
            }
        } catch (SQLException e) {
            e.getStackTrace();
        }
        return null;
    }

    public static void addCurriculum(Curriculum newCurr) {
        Connection con = Assignment.getConnection();

        try {
            String query = "INSERT INTO curriculum VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, newCurr.getCourseCode());
            pst.setString(2, newCurr.getCourseName());
            pst.setString(3, newCurr.getDescription());
            pst.setInt(4, newCurr.getCreditHours());
            pst.setDouble(5, newCurr.getFee());
            pst.setString(6, newCurr.getStatus());

            pst.executeUpdate();
            con.close();
        } catch (SQLException e) {
            e.getStackTrace();
        }
    }

    public static void updateCurriculum(Curriculum curr) {
        Connection con = Assignment.getConnection();

        try {
            String query = "UPDATE curriculum SET courseName=?, description=?, creditHours=?, fee=?, status=? WHERE courseCode=?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, curr.getCourseName());
            pst.setString(2, curr.getDescription());
            pst.setInt(3, curr.getCreditHours());
            pst.setDouble(4, curr.getFee());
            pst.setString(5, curr.getStatus());
            pst.setString(6, curr.getCourseCode());

            pst.execute();
            con.close();
        } catch (SQLException e) {
            e.getStackTrace();
        }
    }

    public static int getActive() {
        Connection con = Assignment.getConnection();
        try {
            String query = "SELECT COUNT(status) FROM curriculum WHERE status = ? GROUP BY status";
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
            String query = "SELECT COUNT(status) FROM curriculum WHERE status = ? GROUP BY status";
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

    public static double calPercentage(String type) {
        int total = Curriculum.getActive() + Curriculum.getInactive();
        double percent;
        if (type.equals("AVAILABLE")) {
            percent = Curriculum.getActive() / (double) total * 100.0;
        } else {
            percent = Curriculum.getInactive() / (double) total * 100.0;
        }
        return percent;
    }

//    @Override
//    public String toString() {
//        return String.format("%12.12s ║ %-30s ║ %-70s ║ %-12s ║ %-15.2f ║ %-12s", courseCode, courseName, description, creditHours, fee, status);
//    }

}
