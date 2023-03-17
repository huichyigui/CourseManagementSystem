package assignment;

import java.sql.*;

public class LocalStudent extends Student{

    public LocalStudent(){}

    public LocalStudent(String studentID, String studentPW, String name, String IC, String gender, String contactNumber, String emailAddress, String DOB,
                        String accountStatus, String studentProgramme, String studyMode){
        super(studentID,studentPW, name, IC, gender, contactNumber, emailAddress, DOB, accountStatus, studentProgramme, studyMode);
    }

    public static boolean checkDuplicateLocalStudentID(String studentID) {
        Connection con = Assignment.getConnection();

        try {
            String query = "SELECT * FROM localstudent WHERE localStudentID = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, studentID);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.getStackTrace();
        }
        return false;
    }

    public static void addLocalStudent(LocalStudent newLocalStudent) {
        Connection con = Assignment.getConnection();

        try {
            String query = "INSERT INTO localstudent VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement pst = con.prepareStatement(query);

            pst.setString(1, newLocalStudent.getStudentID());
            pst.setString(2, newLocalStudent.getStudentPW());
            pst.setString(3, newLocalStudent.getName());
            pst.setString(4, newLocalStudent.getIC());
            pst.setString(5, newLocalStudent.getGender());
            pst.setString(6, newLocalStudent.getContactNumber());
            pst.setString(7, newLocalStudent.getEmailAddress());
            pst.setString(8, newLocalStudent.getDOB());
            pst.setString(9, newLocalStudent.getAccountStatus());
            pst.setString(10, newLocalStudent.getStudentProgramme());
            pst.setString(11, newLocalStudent.getStudyMode());

            pst.executeUpdate();
            System.out.println("New local student record added successfully.");
            con.close();
        } catch (SQLException e) {
            e.getStackTrace();
        }
    }

   public static LocalStudent getLocalStudent(String studentID) {
        Connection con = Assignment.getConnection();

        try {
            String query = "SELECT * FROM localstudent WHERE localStudentID= ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, studentID);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                LocalStudent localStudent = new LocalStudent(rs.getString("localStudentID"), rs.getString("password"), rs.getString("name"),
                        rs.getString("ICNumber"),rs.getString("gender"), rs.getString("contactNumber"), rs.getString("emailAddress"),
                        rs.getString("DOB"), rs.getString("accountStatus"), rs.getString("programme"), rs.getString("studyMode"));

                return localStudent;
            }
        } catch (SQLException e) {
            e.getStackTrace();
        }
        return null;
    }

    public static void updateStudent(LocalStudent localStudent) {
        Connection con = Assignment.getConnection();

        try {
            String query = "UPDATE localstudent SET password=?, name=?, ICNumber=?, Gender=?, contactNumber=?, emailAddress=?, DOB=?, accountStatus=?, programme=?, studyMode=? WHERE localStudentID=?";
            PreparedStatement pst = con.prepareStatement(query);

            pst.setString(1, localStudent.getStudentPW());
            pst.setString(2, localStudent.getName());
            pst.setString(3, localStudent.getIC());
            pst.setString(4, localStudent.getGender());
            pst.setString(5, localStudent.getContactNumber());
            pst.setString(6, localStudent.getEmailAddress());
            pst.setString(7, localStudent.getDOB());
            pst.setString(8, localStudent.getAccountStatus());
            pst.setString(9, localStudent.getStudentProgramme());
            pst.setString(10, localStudent.getStudyMode());
            pst.setString(11, localStudent.getStudentID());

            pst.execute();
            con.close();
        } catch (SQLException e) {
            e.getStackTrace();
        }
    }

    public static void deleteLocalStudent(String studentID) {
        Connection con = Assignment.getConnection();

        try {
            String query = "DELETE FROM localstudent WHERE localStudentID=?";
            PreparedStatement pst = con.prepareStatement(query);

            pst.setString(1, studentID);

            pst.execute();
            con.close();
        } catch (SQLException e) {
            e.getStackTrace();
        }
    }
}
