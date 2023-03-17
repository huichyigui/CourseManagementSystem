package assignment;

import java.sql.*;

public class ParttimeStaff extends Staff{

    public ParttimeStaff(){}

    public ParttimeStaff(String staffID, String staffPW, String name, String IC, String gender, String contactNumber, String emailAddress, String DOB,
                        String accountStatus, String staffType){
        super(staffID,staffPW, name, IC, gender, contactNumber, emailAddress, DOB, accountStatus, staffType);
    }

    public static boolean checkDuplicateParttimeStaffID(String staffID) {
        Connection con = Assignment.getConnection();

        try {
            String query = "SELECT * FROM parttimestaff WHERE parttimeStaffID = ?";
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

    public static void addParttimeStaff(ParttimeStaff newParttimeStaff) {
        Connection con = Assignment.getConnection();

        try {
            String query = "INSERT INTO parttimeStaff VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement pst = con.prepareStatement(query);

            pst.setString(1, newParttimeStaff.getStaffID());
            pst.setString(2, newParttimeStaff.getStaffPW());
            pst.setString(3, newParttimeStaff.getName());
            pst.setString(4, newParttimeStaff.getIC());
            pst.setString(5, newParttimeStaff.getGender());
            pst.setString(6, newParttimeStaff.getContactNumber());
            pst.setString(7, newParttimeStaff.getEmailAddress());
            pst.setString(8, newParttimeStaff.getDOB());
            pst.setString(9, newParttimeStaff.getAccountStatus());
            pst.setString(10,newParttimeStaff.getStaffType());
            

            pst.executeUpdate();
            System.out.println("New parttime staff record added successfully.");
            con.close();
        } catch (SQLException e) {
            e.getStackTrace();
        }
    }

   public static ParttimeStaff getParttStaff(String staffID) {
        Connection con = Assignment.getConnection();

        try {
            String query = "SELECT * FROM parttimestaff WHERE parttimeStaffID= ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, staffID);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                ParttimeStaff parttimeStaff = new ParttimeStaff(rs.getString("parttimeStaffID"), rs.getString("password"), rs.getString("name"),
                        rs.getString("IC"),rs.getString("gender"), rs.getString("contactNumber"), rs.getString("emailAddress"),
                        rs.getString("DOB"), rs.getString("accountStatus"), rs.getString("staffType"));
                return parttimeStaff;
                
            }
        } catch (SQLException e) {
            e.getStackTrace();
        }
        return null;
    }

    public static void updateStaff(ParttimeStaff ParttimeStaff) {
        Connection con = Assignment.getConnection();

        try {
            String query = "UPDATE parttimestaff SET password=?, name=?, IC=?, Gender=?, contactNumber=?, emailAddress=?, DOB=?, accountStatus=?, stafftype=? WHERE parttimeStaffID=?";
            PreparedStatement pst = con.prepareStatement(query);

            pst.setString(1, ParttimeStaff.getStaffPW());
            pst.setString(2, ParttimeStaff.getName());
            pst.setString(3, ParttimeStaff.getIC());
            pst.setString(4, ParttimeStaff.getGender());
            pst.setString(5, ParttimeStaff.getContactNumber());
            pst.setString(6, ParttimeStaff.getEmailAddress());
            pst.setString(7, ParttimeStaff.getDOB());
            pst.setString(8, ParttimeStaff.getAccountStatus());
            pst.setString(9, ParttimeStaff.getStaffType());
            pst.setString(10,ParttimeStaff.getStaffID());

            pst.execute();
            con.close();
        } catch (SQLException e) {
            e.getStackTrace();
        }
    }

    public static void deleteParttimeStaff(String staffID) {
        Connection con = Assignment.getConnection();

        try {
            String query = "DELETE FROM parttimestaff WHERE fulltimeStaffID=?";
            PreparedStatement pst = con.prepareStatement(query);

            pst.setString(1, staffID);

            pst.execute();
            con.close();
        } catch (SQLException e) {
            e.getStackTrace();
        }
    }
}
