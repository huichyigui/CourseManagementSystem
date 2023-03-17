package assignment;

import java.sql.*;

public class FulltimeStaff extends Staff {

    public FulltimeStaff() {
    }

    public FulltimeStaff(String staffID, String staffPW, String name, String IC, String gender, String contactNumber, String emailAddress, String DOB,
            String accStatus, String staffType) {
        super(staffID, staffPW, name, IC, gender, contactNumber, emailAddress, DOB, accStatus, staffType);
    }

    public static boolean checkDuplicateFulltimeStaffID(String staffID) {
        Connection con = Assignment.getConnection();

        try {
            String query = "SELECT * FROM fulltimestaff WHERE fulltimeStaffID = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, staffID);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.getStackTrace();
        }
        return false;
    }

    public static void addFulltimeStaff(FulltimeStaff newFulltimeStaff) {
        Connection con = Assignment.getConnection();

        try {
            String query = "INSERT INTO fulltimestaff VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement pst = con.prepareStatement(query);

            pst.setString(1, newFulltimeStaff.getStaffID());
            pst.setString(2, newFulltimeStaff.getStaffPW());
            pst.setString(3, newFulltimeStaff.getName());
            pst.setString(4, newFulltimeStaff.getIC());
            pst.setString(5, newFulltimeStaff.getGender());
            pst.setString(6, newFulltimeStaff.getContactNumber());
            pst.setString(7, newFulltimeStaff.getEmailAddress());
            pst.setString(8, newFulltimeStaff.getDOB());
            pst.setString(9, newFulltimeStaff.getAccountStatus());
            pst.setString(10, newFulltimeStaff.getStaffType());

            pst.executeUpdate();
            System.out.println("New fulltime staff record added successfully.");
            con.close();
        } catch (SQLException e) {
            e.getStackTrace();
        }
    }

    public static FulltimeStaff getFulltStaff(String staffID) {
        Connection con = Assignment.getConnection();

        try {
            String query = "SELECT * FROM fulltimestaff WHERE fulltimeStaffID= ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, staffID);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                FulltimeStaff fulltimeStaff = new FulltimeStaff(rs.getString("fulltimeStaffID"), rs.getString("password"), rs.getString("name"),
                        rs.getString("IC"), rs.getString("gender"), rs.getString("contactNumber"), rs.getString("emailAddress"),
                        rs.getString("DOB"), rs.getString("accountStatus"), rs.getString("staffType"));

                return fulltimeStaff;

            }
        } catch (SQLException e) {
            e.getStackTrace();
        }
        return null;
    }

    public static void updateStaff(FulltimeStaff FulltimeStaff) {
        Connection con = Assignment.getConnection();

        try {
            String query = "UPDATE fulltimestaff SET password=?, name=?, IC=?, Gender=?, contactNumber=?, emailAddress=?, DOB=?, accountStatus=?, stafftype=? WHERE fulltimeStaffID=?";
            PreparedStatement pst = con.prepareStatement(query);

            pst.setString(1, FulltimeStaff.getStaffPW());
            pst.setString(2, FulltimeStaff.getName());
            pst.setString(3, FulltimeStaff.getIC());
            pst.setString(4, FulltimeStaff.getGender());
            pst.setString(5, FulltimeStaff.getContactNumber());
            pst.setString(6, FulltimeStaff.getEmailAddress());
            pst.setString(7, FulltimeStaff.getDOB());
            pst.setString(8, FulltimeStaff.getAccountStatus());
            pst.setString(9, FulltimeStaff.getStaffType());
            pst.setString(10, FulltimeStaff.getStaffID());

            pst.execute();
            con.close();
        } catch (SQLException e) {
            e.getStackTrace();
        }
    }

    public static void deleteFulltimeStaff(String staffID) {
        Connection con = Assignment.getConnection();

        try {
            String query = "DELETE FROM fulltimestaff WHERE fulltimeStaffID=?";
            PreparedStatement pst = con.prepareStatement(query);

            pst.setString(1, staffID);

            pst.execute();
            con.close();
        } catch (SQLException e) {
            e.getStackTrace();
        }
    }
}
