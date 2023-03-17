package assignment;

import java.sql.*;

public class InternationalStudent extends Student{
    private String country;

    public InternationalStudent(){};

    public InternationalStudent(String studentID, String studentPW, String name, String IC, String gender, String contactNumber, String emailAddress, String DOB,
                        String accountStatus, String studentProgramme, String studyMode, String country){
        super(studentID,studentPW, name, IC, gender, contactNumber, emailAddress, DOB, accountStatus, studentProgramme, studyMode);
        this.country = country;
    };

    //***********Getter************
    public String getCountry() {
        return country;
    }
    //***********Setter************
    public void setCountry(String country) {
        this.country = country;
    }

    public static void addInterStudent(InternationalStudent newInterStudent) {
        Connection con = Assignment.getConnection();

        try {
            String query = "INSERT INTO interstudent VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement pst = con.prepareStatement(query);

            pst.setString(1, newInterStudent.getStudentID());
            pst.setString(2, newInterStudent.getStudentPW());
            pst.setString(3, newInterStudent.getName());
            pst.setString(4, newInterStudent.getIC());
            pst.setString(5, newInterStudent.getGender());
            pst.setString(6, newInterStudent.getContactNumber());
            pst.setString(7, newInterStudent.getEmailAddress());
            pst.setString(8, newInterStudent.getDOB());
            pst.setString(9, newInterStudent.getAccountStatus());
            pst.setString(10, newInterStudent.getStudentProgramme());
            pst.setString(11, newInterStudent.getStudyMode());
            pst.setString(12, newInterStudent.getCountry());

            pst.executeUpdate();
            System.out.println("New international student record added successfully.");
            con.close();
        } catch (SQLException e) {
            e.getStackTrace();
        }
    }

    protected static boolean checkCountry(String country){
        if (country.matches("^[a-zA-Z\\s]*$") && country.length() <= 50) {
            return true;
        }
        System.out.println("Invalid input. No symbols or digits is allowed.");
        return false;
    }

    public static boolean checkDuplicateInterStudentID(String studentID) {
        Connection con = Assignment.getConnection();

        try {
            String query = "SELECT * FROM interstudent WHERE interStudentID = ?";
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

    public static InternationalStudent getInterStudent(String studentID) {
        Connection con = Assignment.getConnection();

        try {
            String query = "SELECT * FROM interstudent WHERE interStudentID= ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, studentID);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                InternationalStudent internationalStudent = new InternationalStudent(rs.getString("interStudentID"), rs.getString("password"), rs.getString("name"),
                        rs.getString("ICNumber"),rs.getString("gender"), rs.getString("contactNumber"), rs.getString("emailAddress"),
                        rs.getString("DOB"), rs.getString("accountStatus"), rs.getString("programme"), rs.getString("studyMode"), rs.getString("country"));

                return internationalStudent;
            }
        } catch (SQLException e) {
            e.getStackTrace();
        }
        return null;
    }


    public static void updateStudent(InternationalStudent internationalStudent) {
        Connection con = Assignment.getConnection();

        try {
            String query = "UPDATE interstudent SET password=?, name=?, ICNumber=?, Gender=?, contactNumber=?, emailAddress=?, DOB=?, accountStatus=?, programme=?, studyMode=?, country=? WHERE interStudentID=?";
            PreparedStatement pst = con.prepareStatement(query);

            pst.setString(1, internationalStudent.getStudentPW());
            pst.setString(2, internationalStudent.getName());
            pst.setString(3, internationalStudent.getIC());
            pst.setString(4, internationalStudent.getGender());
            pst.setString(5, internationalStudent.getContactNumber());
            pst.setString(6, internationalStudent.getEmailAddress());
            pst.setString(7, internationalStudent.getDOB());
            pst.setString(8, internationalStudent.getAccountStatus());
            pst.setString(9, internationalStudent.getStudentProgramme());
            pst.setString(10, internationalStudent.getStudyMode());
            pst.setString(11, internationalStudent.getCountry());
            pst.setString(12, internationalStudent.getStudentID());

            pst.execute();
            con.close();
        } catch (SQLException e) {
            e.getStackTrace();
        }
    }

    public static void deleteInterStudent(String studentID) {
        Connection con = Assignment.getConnection();

        try {
            String query = "DELETE FROM interstudent WHERE interStudentID=?";
            PreparedStatement pst = con.prepareStatement(query);

            pst.setString(1, studentID);

            pst.execute();
            con.close();
        } catch (SQLException e) {
            e.getStackTrace();
        }
    }
}
