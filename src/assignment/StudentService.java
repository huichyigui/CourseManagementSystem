package assignment;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

public class StudentService {

    Connection con = Assignment.getConnection();
    ResultSet rs = null;
    PreparedStatement pst = null;

    public void print(Assignment mainApp) {
        int choice;
        do {
            try {
                Helper.printHeader("Admin Main > Student");
                System.out.println("What would you like to do?");
                System.out.println("[1] View Student Details");
                System.out.println("[2] Add New Student Details");
                System.out.println("[3] Update Student Details");
                System.out.println("[4] Delete Student Entry");
                System.out.println("[5] Send Student Login Credentials Through Email");
                System.out.println("[6] Generate Student Reports");
                System.out.println("[7] Generate Student Graph");
                System.out.println("[9] Return to Previous Menu");
                System.out.printf("%-50s: ", "Choice");
                choice = Integer.parseInt(Helper.readLine());
                switch (choice) {
                    case 1:
                        viewStudentDetails(mainApp);
                        break;
                    case 2:
                        addStudent(mainApp);
                        break;
                    case 3:
                        updateStudent(mainApp);
                        break;
                    case 4:
                        deleteStudent(mainApp);
                        break;
                    case 5:
                        sendStudentEmail(mainApp);
                        break;
                    case 6:
                        generateStudentReportMenu(mainApp);
                        break;
                    case 7:
                        generateStudentGraphMenu(mainApp);
                        break;
                    case 9:
                        return;
                    default:
                        System.out.println("Please enter valid option.");
                        Helper.systemSleep(1);
                }
            } catch (Exception e) {
                System.out.println("Incorrect input. Please enter digits 1 - 9 only.");
            }
        } while (true);
    }

    private void generateStudentGraphMenu(Assignment mainApp){
        Helper.printHeader("Admin > Student: Generate Student Pie Chart");
        System.out.println("\nSelect type of report to be generated: ");
        System.out.println("[1] Generate Students Accounts Status Pie Chart");
        System.out.println("[2] Generate Students Gender Pie Chart");
        System.out.println("[0] Return to Previous Menu");

        do{
            try{
                System.out.printf("%-50s: ", "Choice  (Press 0 to return to previous menu)");
                int choice = Integer.parseInt(Helper.readLine());
                switch (choice){
                    case 1:
                        generateActivePieChart();
                        break;
                    case 2:
                        generateGenderPieChart();
                        break;
                    case 0:
                        return;
                }
            }catch (Exception e) {
                System.out.println("Incorrect input. Please enter digits 1 - 3 only.");
            }

        }while (true);
    }

    private void generateActivePieChart(){
        int activeAccountCount = 0;
        int inactiveAccountCount = 0;
        int totalCount = 0;
        double activePercentage =0;
        double inactivePercentage =0;

        try {
            String query = "SELECT count(accountStatus) FROM localstudent where accountStatus = \"Active\" GROUP BY accountStatus;";
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                activeAccountCount += rs.getInt(1);
            }
            query = "SELECT count(accountStatus) FROM interstudent where accountStatus = \"Active\" GROUP BY accountStatus;";
            pst = con.prepareStatement(query);
             rs = pst.executeQuery();
            if (rs.next()) {
                activeAccountCount += rs.getInt(1);
            }

            query = "SELECT count(accountStatus) FROM localstudent where accountStatus = \"Inactive\" GROUP BY accountStatus;";
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();
            if (rs.next()) {
                inactiveAccountCount += rs.getInt(1);
            }
            query = "SELECT count(accountStatus) FROM interstudent where accountStatus = \"Inactive\" GROUP BY accountStatus;";
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();
            if (rs.next()) {
                inactiveAccountCount += rs.getInt(1);
            }
        } catch (SQLException e) {
            e.getStackTrace();
        }

        totalCount = activeAccountCount + inactiveAccountCount;

        activePercentage =  ((double) activeAccountCount / totalCount) * 100;
        inactivePercentage = ((double) inactiveAccountCount / totalCount) * 100;


        Helper createGraph = new Helper("Pie Chart", "Student Account Status Pie Chart", "Active", activePercentage, "Inactive", inactivePercentage);
        createGraph.pack();
        createGraph.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        createGraph.setVisible(true);

        System.out.println("\n\nStudent Account Status Pie Chart Generated Successfully.");
        System.out.printf("\nActive Student Account Percentage: %.2f%%", activePercentage);
        System.out.printf("\nInactive Student Account Percentage: %.2f%%\n\n", inactivePercentage);
        Helper.systemSleep(1);
    }

    private void generateGenderPieChart(){
        int maleCount = 0;
        int femaleCount = 0;
        int totalCount = 0;
        double activePercentage =0;
        double inactivePercentage =0;

        try {
            String query = "SELECT count(gender) FROM localstudent where gender = \"Male\" GROUP BY gender;";
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                maleCount += rs.getInt(1);
            }
            query = "SELECT count(gender) FROM interstudent where gender = \"Male\" GROUP BY gender;";
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();
            if (rs.next()) {
                maleCount += rs.getInt(1);
            }

            query = "SELECT count(gender) FROM localstudent where gender = \"Female\" GROUP BY gender;";
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();
            if (rs.next()) {
                femaleCount += rs.getInt(1);
            }
            query = "SELECT count(gender) FROM interstudent where gender = \"Female\" GROUP BY gender;";
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();
            if (rs.next()) {
                femaleCount += rs.getInt(1);
            }
        } catch (SQLException e) {
            e.getStackTrace();
        }

        totalCount = maleCount + femaleCount;

        activePercentage =  ((double) maleCount / totalCount) * 100;
        inactivePercentage = ((double) femaleCount / totalCount) * 100;


        Helper createPieChart = new Helper("Pie Chart", "Student Gender Pie Chart", "Male", activePercentage, "Female", inactivePercentage);
        createPieChart.pack();
        createPieChart.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        createPieChart.setVisible(true);

        System.out.println("\n\nStudent Gender Pie Chart Generated Successfully.");
        System.out.printf("\nMale Student Percentage: %.2f%%", activePercentage);
        System.out.printf("\nFemale Student Percentage: %.2f%%\n\n", inactivePercentage);
        Helper.systemSleep(1);
    }

    private void generateStudentReportMenu(Assignment mainApp){
        Helper.printHeader("Admin > Student: Generate Student List Report");
        Helper.printLine(59);
        System.out.println("\nSelect type of report to be generated: ");
        System.out.println("[1] Generate ALL");
        System.out.println("[2] Generate Active Students Account List Only");
        System.out.println("[3] Generate Inactive Students Account List Only");
        System.out.println("[0] Return to Previous Menu");

        do{
            try{
                System.out.printf("%-50s: ", "Choice");
                int choice = Integer.parseInt(Helper.readLine());
                switch (choice){
                    case 1:
                        generateTheStudentReport();
                        break;
                    case 2:
                        generateTheStudentReport(1);
                        break;
                    case 3:
                        generateTheStudentReport(1.0);
                        break;
                    case 0:
                        return;
                }
            }catch (Exception e) {
                System.out.println("Incorrect input. Please enter digits 1 - 3 only.");
            }

        }while (true);
    }

    private void generateTheStudentReport(){
        try {
            int count = 0;
            FileOutputStream fileOutput = new FileOutputStream( "Student List Report - ALL.txt");
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fileOutput));
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String line = String.format("%" + 178 + "s", "").replace(" ", "═");
            String line2 = String.format("%" + 199 + "s", "").replace(" ", "═");
            bw.write(String.format("\n%92s\n", "ANAVRIN COLLEGE ENROLLMENT SYSTEM"));
            bw.write(String.format("%85s\n", "STUDENT LIST REPORT"));
            bw.write(String.format("\nGenerated On: %s\n", now.format(formatter)));
            bw.newLine();
            bw.write("\nLocal Students\n");
            bw.write(line);
            bw.newLine();
            bw.write(String.format("║%10s ║ %-30s ║ %-15s ║ %-7s ║ %-14s ║ %-30s ║ %-10s ║ %-9s ║ %-15s║ %-10s║", "Student ID", "Student Name", "Student IC", "Gender", "Contact Number", "Email Address", "DOB", "Programme", "Account Status", "Study Mode"));
            bw.newLine();
            bw.write(line);
            bw.newLine();
            String query = "SELECT localStudentID, name, ICNumber, gender, contactNumber, emailAddress, DOB, accountStatus, programme, studyMode FROM localstudent order by accountStatus;";
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();
            while (rs.next()) {
                bw.write(String.format("║%10s ║ %-30s ║ %-15s ║ %-7s ║ %-14s ║ %-30s ║ %-10s ║ %-9s ║ %-15s║ %-10s║\n",
                        rs.getString("localStudentID"), rs.getString("name"), rs.getString("ICNumber"), rs.getString("gender"),
                        rs.getString("contactNumber"), rs.getString("emailAddress"), rs.getString("DOB"), rs.getString("accountStatus"),
                        rs.getString("programme"), rs.getString("studyMode")));
                count++;
            }
            bw.write(line);

            bw.newLine();
            bw.write("\nInternational Students\n");
            bw.write(line2);
            bw.newLine();
            bw.write(String.format("║%11s ║ %-28s ║ %-15s ║ %-7s ║ %-14s ║ %-30s ║ %-10s ║ %-9s ║ %-15s║ %-10s║ %-20s║", "Student ID", "Student Name", "Student IC", "Gender", "Contact Number", "Email Address", "DOB", "Programme", "Account Status", "Study Mode", "Country"));
            bw.newLine();
            bw.write(line2);
            bw.newLine();
            query = "SELECT interStudentID, name, ICNumber, gender, contactNumber, emailAddress, DOB, accountStatus, programme, studyMode, country FROM interstudent order by accountStatus;";
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();
            while (rs.next()) {
                bw.write(String.format("║%11s ║ %-28s ║ %-15s ║ %-7s ║ %-14s ║ %-30s ║ %-10s ║ %-9s ║ %-15s║ %-10s║ %-20s║\n",
                        rs.getString("interStudentID"), rs.getString("name"), rs.getString("ICNumber"), rs.getString("gender"),
                        rs.getString("contactNumber"), rs.getString("emailAddress"), rs.getString("DOB"), rs.getString("accountStatus"),
                        rs.getString("programme"), rs.getString("studyMode"), rs.getString("country")));
                count++;
            }
            bw.write(line2);
            bw.newLine();
            bw.newLine();
            bw.write("Total: " + count + " students");
            bw.close();
        } catch (Exception e) {
            System.out.println("Failed to write file with exception " + e.getLocalizedMessage());
        }
        Helper.systemSleep(1);
        System.out.println("\nStudent Report generated successfully!\n");
    }

    private void generateTheStudentReport(int selection){
        try {
            int count = 0;
            FileOutputStream fileOutput = new FileOutputStream( "Student List Report - ACTIVE.txt");
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fileOutput));
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String line = String.format("%" + 178 + "s", "").replace(" ", "═");
            String line2 = String.format("%" + 199 + "s", "").replace(" ", "═");
            bw.write(String.format("\n%92s\n", "ANAVRIN COLLEGE ENROLLMENT SYSTEM"));
            bw.write(String.format("%85s\n", "STUDENT LIST REPORT"));
            bw.write(String.format("\nGenerated On: %s\n", now.format(formatter)));
            bw.newLine();
            bw.write("\nLocal Students\n");
            bw.write(line);
            bw.newLine();
            bw.write(String.format("║%10s ║ %-30s ║ %-15s ║ %-7s ║ %-14s ║ %-30s ║ %-10s ║ %-9s ║ %-15s║ %-10s║", "Student ID", "Student Name", "Student IC", "Gender", "Contact Number", "Email Address", "DOB", "Programme", "Account Status", "Study Mode"));
            bw.newLine();
            bw.write(line);
            bw.newLine();
            String query = "SELECT localStudentID, name, ICNumber, gender, contactNumber, emailAddress, DOB, accountStatus, programme, studyMode FROM localstudent WHERE accountStatus = \"Active\" ORDER BY accountStatus;";
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();
            while (rs.next()) {
                bw.write(String.format("║%10s ║ %-30s ║ %-15s ║ %-7s ║ %-14s ║ %-30s ║ %-10s ║ %-9s ║ %-15s║ %-10s║\n",
                        rs.getString("localStudentID"), rs.getString("name"), rs.getString("ICNumber"), rs.getString("gender"),
                        rs.getString("contactNumber"), rs.getString("emailAddress"), rs.getString("DOB"), rs.getString("accountStatus"),
                        rs.getString("programme"), rs.getString("studyMode")));
                count++;
            }
            bw.write(line);

            bw.newLine();
            bw.write("\nInternational Students\n");
            bw.write(line2);
            bw.newLine();
            bw.write(String.format("║%11s ║ %-28s ║ %-15s ║ %-7s ║ %-14s ║ %-30s ║ %-10s ║ %-9s ║ %-15s║ %-10s║ %-20s║", "Student ID", "Student Name", "Student IC", "Gender", "Contact Number", "Email Address", "DOB", "Programme", "Account Status", "Study Mode", "Country"));
            bw.newLine();
            bw.write(line2);
            bw.newLine();
            query = "SELECT interStudentID, name, ICNumber, gender, contactNumber, emailAddress, DOB, accountStatus, programme, studyMode, country FROM interstudent WHERE accountStatus = \"Active\" ORDER BY accountStatus;";
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();
            while (rs.next()) {
                bw.write(String.format("║%11s ║ %-28s ║ %-15s ║ %-7s ║ %-14s ║ %-30s ║ %-10s ║ %-9s ║ %-15s║ %-10s║ %-20s║\n",
                        rs.getString("interStudentID"), rs.getString("name"), rs.getString("ICNumber"), rs.getString("gender"),
                        rs.getString("contactNumber"), rs.getString("emailAddress"), rs.getString("DOB"), rs.getString("accountStatus"),
                        rs.getString("programme"), rs.getString("studyMode"), rs.getString("country")));
                count++;
            }
            bw.write(line2);
            bw.newLine();
            bw.newLine();
            bw.write("Total: " + count + " students");
            bw.close();
        } catch (Exception e) {
            System.out.println("Failed to write file with exception " + e.getLocalizedMessage());
        }
        Helper.systemSleep(1);
        System.out.println("\nStudent Report generated successfully!\n");
    }

    private void generateTheStudentReport(double selection){
        try {
            int count = 0;
            FileOutputStream fileOutput = new FileOutputStream( "Student List Report - INACTIVE.txt");
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fileOutput));
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String line = String.format("%" + 178 + "s", "").replace(" ", "═");
            String line2 = String.format("%" + 199 + "s", "").replace(" ", "═");
            bw.write(String.format("\n%92s\n", "ANAVRIN COLLEGE ENROLLMENT SYSTEM"));
            bw.write(String.format("%85s\n", "STUDENT LIST REPORT"));
            bw.write(String.format("\nGenerated On: %s\n", now.format(formatter)));
            bw.newLine();
            bw.write("\nLocal Students\n");
            bw.write(line);
            bw.newLine();
            bw.write(String.format("║%10s ║ %-30s ║ %-15s ║ %-7s ║ %-14s ║ %-30s ║ %-10s ║ %-9s ║ %-15s║ %-10s║", "Student ID", "Student Name", "Student IC", "Gender", "Contact Number", "Email Address", "DOB", "Programme", "Account Status", "Study Mode"));
            bw.newLine();
            bw.write(line);
            bw.newLine();
            String query = "SELECT localStudentID, name, ICNumber, gender, contactNumber, emailAddress, DOB, accountStatus, programme, studyMode FROM localstudent WHERE accountStatus = \"Inactive\" ORDER BY accountStatus;";
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();
            while (rs.next()) {
                bw.write(String.format("║%10s ║ %-30s ║ %-15s ║ %-7s ║ %-14s ║ %-30s ║ %-10s ║ %-9s ║ %-15s║ %-10s║\n",
                        rs.getString("localStudentID"), rs.getString("name"), rs.getString("ICNumber"), rs.getString("gender"),
                        rs.getString("contactNumber"), rs.getString("emailAddress"), rs.getString("DOB"), rs.getString("accountStatus"),
                        rs.getString("programme"), rs.getString("studyMode")));
                count++;
            }
            bw.write(line);

            bw.newLine();
            bw.write("\nInternational Students\n");
            bw.write(line2);
            bw.newLine();
            bw.write(String.format("║%11s ║ %-28s ║ %-15s ║ %-7s ║ %-14s ║ %-30s ║ %-10s ║ %-9s ║ %-15s║ %-10s║ %-20s║", "Student ID", "Student Name", "Student IC", "Gender", "Contact Number", "Email Address", "DOB", "Programme", "Account Status", "Study Mode", "Country"));
            bw.newLine();
            bw.write(line2);
            bw.newLine();
            query = "SELECT interStudentID, name, ICNumber, gender, contactNumber, emailAddress, DOB, accountStatus, programme, studyMode, country FROM interstudent WHERE accountStatus = \"Inactive\" ORDER BY accountStatus;";
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();
            while (rs.next()) {
                bw.write(String.format("║%11s ║ %-28s ║ %-15s ║ %-7s ║ %-14s ║ %-30s ║ %-10s ║ %-9s ║ %-15s║ %-10s║ %-20s║\n",
                        rs.getString("interStudentID"), rs.getString("name"), rs.getString("ICNumber"), rs.getString("gender"),
                        rs.getString("contactNumber"), rs.getString("emailAddress"), rs.getString("DOB"), rs.getString("accountStatus"),
                        rs.getString("programme"), rs.getString("studyMode"), rs.getString("country")));
                count++;
            }
            bw.write(line2);
            bw.newLine();
            bw.newLine();
            bw.write("Total: " + count + " students");
            bw.close();
        } catch (Exception e) {
            System.out.println("Failed to write file with exception " + e.getLocalizedMessage());
        }
        Helper.systemSleep(1);
        System.out.println("\nStudent Report generated successfully!\n");
    }

    private void sendStudentEmail(Assignment mainApp) throws Exception{
        String choice;
        Helper.printHeader("Admin > Student: Send Student Login Credentials Through Email");
        while (true){
            viewStudent();
            System.out.print("\nEnter the Student ID to proceed sending the Student Login Credentials email (Q to Quit): ");
            String studentIDInput = Helper.readLine().toUpperCase().trim();
            if (studentIDInput.equals("Q")) {
                break;
            }
            try{
                if(studentIDInput.length() == 10 && LocalStudent.checkLocalStudentIDFormat(studentIDInput) && LocalStudent.checkDuplicateLocalStudentID(studentIDInput)){
                    LocalStudent localStudent = LocalStudent.getLocalStudent(studentIDInput);
                    System.out.println("Local Student :" + localStudent.getName() + "'s email address is "+ localStudent.getEmailAddress());
                    System.out.print("\nPress Y to proceed (Q to quit): ");

                    String confirm = Helper.readLine().toUpperCase();
                    if (confirm.equals("Y")) {
                        String email = localStudent.getEmailAddress();
                        String username = localStudent.getStudentID();
                        String decrypStudentPW = null;
                        String studentName = localStudent.getName();
                        try {
                            Helper aes = new Helper();
                            decrypStudentPW = aes.decrypt(localStudent.getStudentPW());
                        }catch (Exception ex){
                            Logger.getLogger(Helper.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        Helper.sendMail(email, username, decrypStudentPW, studentName);

                    } else {
                        System.out.println("Aborting");
                        Helper.pause();
                        break;
                    }
                }

                if(studentIDInput.length() == 11 && InternationalStudent.checkInterStudentIDFormat(studentIDInput) && InternationalStudent.checkDuplicateInterStudentID(studentIDInput)){
                    InternationalStudent internationalStudent = InternationalStudent.getInterStudent(studentIDInput);
                    System.out.println("International Student :" + internationalStudent.getName() + "'s email address is "+ internationalStudent.getEmailAddress());
                    System.out.print("\nPress Y to proceed (Q to quit): ");

                    String confirm = Helper.readLine().toUpperCase();
                    if (confirm.equals("Y")) {
                        String email = internationalStudent.getEmailAddress();
                        String username = internationalStudent.getStudentID();
                        String decrypStudentPW = null;
                        String studentName = internationalStudent.getName();
                        try {
                            Helper aes = new Helper();
                            decrypStudentPW = aes.decrypt(internationalStudent.getStudentPW());
                        }catch (Exception ex){
                            Logger.getLogger(Helper.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        Helper.sendMail(email, username, decrypStudentPW, studentName);

                    } else {
                        System.out.println("Aborting");
                        Helper.pause();
                        break;
                    }
                }else{
                    System.out.println("Student Record not found. Please double check the input.");}
                
                Helper.pause();
            }catch (SQLException e) {
                e.getStackTrace();
            }
        }
    }

    private void deleteStudent(Assignment mainApp){
        String choice;
        Helper.printHeader("Admin > Student: Delete Student Details");
        while (true) {
            System.out.println("Enter Student Type");
            System.out.println("[1] Local");
            System.out.println("[2] International");
            System.out.printf("%-50s: ", "Enter choice (Q to quit)");
            try{
                choice = Helper.readLine().toUpperCase().trim();
                switch (choice) {
                    case "1":
                        deleteLocalStudent(mainApp);
                        break;
                    case "2":
                        deleteInterStudent(mainApp);
                        break;
                    case "Q":
                        return;
                    default:
                        System.out.println("Please enter valid option.");
            }
            }catch (Exception e) {
            System.out.println("Please enter digits 1 to 2 only");
            }
        }
    }

    private void deleteLocalStudent(Assignment mainApp){
        Helper.printHeader("Admin > Student: Delete Local Student Details");
        viewStudent("Local");
        while (true) {
            System.out.println();
            System.out.println("\t\t\t\t\t\tTips: ");
            System.out.println("The delete process is INRREVERSIBLE, please proceed with caution. ");
            System.out.println("You can update the student account status to inactive instead of deleting it.");
            System.out.print("\n\nEnter the Student ID in order to delete the account (Q to Quit): ");
            String studentIDInput = Helper.readLine().trim().toUpperCase();
            if (studentIDInput.equals("Q")) {
                break;
            }
            if (LocalStudent.checkLocalStudentIDFormat(studentIDInput)){
                if (LocalStudent.checkDuplicateLocalStudentID(studentIDInput)){
                    LocalStudent localStudent = LocalStudent.getLocalStudent(studentIDInput);

                    System.out.print("Student's ID number: " + localStudent.getStudentID() + " , " + localStudent.getName() + " will be deleted. " + "\n\nConfirm to proceed? y/n: ");
                    String confirm = Helper.readLine().toUpperCase();
                    if (confirm.equals("Y")) {
                        LocalStudent.deleteLocalStudent(studentIDInput);
                        System.out.println("Local student's account successfully deleted!");
                    } else {
                        System.out.println("Aborting");
                        Helper.pause();
                    }
                } else {
                    System.out.printf("StudentID (%s) does not exist!\n", studentIDInput);
                    Helper.pause();
                }
            }else {
                System.out.println("Invalid StudentID format\n");
            }
        }
    }

    private void deleteInterStudent(Assignment mainApp){
        Helper.printHeader("Admin > Student: Delete International Student Details");
        viewStudent("Inter");
        while (true) {
            System.out.println();
            System.out.println("\t\t\t\t\t\tTips: ");
            System.out.println("The delete process is INRREVERSIBLE, please proceed with caution. ");
            System.out.println("You can update the student account status to inactive instead of deleting it.");
            System.out.print("\n\nEnter the Student ID in order to delete the account (Q to Quit): ");
            String studentIDInput = Helper.readLine().trim().toUpperCase();
            if (studentIDInput.equals("Q")) {
                break;
            }
            if (InternationalStudent.checkInterStudentIDFormat(studentIDInput)){
                if (InternationalStudent.checkDuplicateInterStudentID(studentIDInput)){
                    InternationalStudent internationalStudent = InternationalStudent.getInterStudent(studentIDInput);

                    System.out.print("Student's ID number: " + internationalStudent.getStudentID() + " , " + internationalStudent.getName() + " will be deleted. " + "\n\nConfirm to proceed? y/n: ");
                    String confirm = Helper.readLine().toUpperCase();
                    if (confirm.equals("Y")) {
                        InternationalStudent.deleteInterStudent(studentIDInput);
                        System.out.println("International student's account successfully deleted!");
                    } else {
                        System.out.println("Aborting");
                        Helper.pause();
                    }
                } else {
                    System.out.printf("StudentID (%s) does not exist!\n", studentIDInput);
                    Helper.pause();
                }
            }else {
                System.out.println("Invalid StudentID format\n");
            }
        }
    }

    private void updateStudent(Assignment mainApp){
        String choice;
        Helper.printHeader("Admin > Student: Update Student Details");
        while (true) {
            System.out.println("Enter Student Type");
            System.out.println("[1] Local");
            System.out.println("[2] International");
            System.out.printf("%-50s: ", "Enter choice (Q to quit)");
            choice = Helper.readLine().toUpperCase().trim();
            switch (choice) {
                case "1":
                    updateLocalStudent(mainApp);
                    break;
                case "2":
                    updateInterStudent(mainApp);
                    break;
                case "Q":
                    return;
                default:
                    System.out.println("Please enter valid option.");
            }
        }
    }

    private void updateInterStudent(Assignment mainApp){
        int choice;
        do {
            Helper.printHeader("Admin > Student: Update Student Details > International Student");
            System.out.println("What would you like to do?");
            System.out.println("[1] Update Student Name");
            System.out.println("[2] Update Student Passport Number");
            System.out.println("[3] Update Student Gender");
            System.out.println("[4] Update Student Contact Number");
            System.out.println("[5] Update Student Email Address");
            System.out.println("[6] Update Student DOB");
            System.out.println("[7] Update Student Programme");
            System.out.println("[8] Update Student Account Status");
            System.out.println("[9] Update Student Account Password");
            System.out.println("[10] Update Student Study Mode");
            System.out.println("[11] Update Student Origin Country");
            System.out.println("[0] Return to Previous Menu");
            System.out.printf("%-50s: ", "Choice");
            try {
                choice = Integer.parseInt(Helper.readLine());
            } catch (Exception e) {
                choice = -1;
                Helper.readLine();
            }

            switch (choice) {
                case 1:
                    changeStudentName("Inter");
                    break;
                case 2:
                    changeStudentIC("Inter");
                    break;
                case 3:
                    changeStudentGender("Inter");
                    break;
                case 4:
                    changeStudentContactNumber("Inter");
                    break;
                case 5:
                    changeStudentEmailAddress("Inter");
                    break;
                case 6:
                    changeStudentDOB("Inter");
                    break;
                case 7:
                    changeStudentProgramme("Inter");
                    break;
                case 8:
                    changeStudentAccountStatus("Inter");
                    break;
                case 9:
                    changeStudentAccountPassword("Inter");
                    break;
                case 10:
                    changeStudentStudyMode("Inter");
                    break;
                case 11:
                    changeStudentCountry();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Please enter valid option.");
                    Helper.pause();
            }
        } while (true);
    }

    private void updateLocalStudent(Assignment mainApp){
        int choice;
        do {
            Helper.printHeader("Admin > Student: Update Student Details > Local Student");
            System.out.println("What would you like to do?");
            System.out.println("[1] Update Student Name");
            System.out.println("[2] Update Student IC");
            System.out.println("[3] Update Student Gender");
            System.out.println("[4] Update Student Contact Number");
            System.out.println("[5] Update Student Email Address");
            System.out.println("[6] Update Student DOB");
            System.out.println("[7] Update Student Programme");
            System.out.println("[8] Update Student Account Status");
            System.out.println("[9] Update Student Account Password");
            System.out.println("[10] Update Student Study Mode");
            System.out.println("[0] Return to Previous Menu");
            System.out.printf("%-50s: ", "Choice");
            try {
                choice = Integer.parseInt(Helper.readLine());
            } catch (Exception e) {
                choice = -1;
                Helper.readLine();
            }

            switch (choice) {
                case 1:
                    changeStudentName();
                    break;
                case 2:
                    changeStudentIC();
                    break;
                case 3:
                    changeStudentGender();
                    break;
                case 4:
                    changeStudentContactNumber();
                    break;
                case 5:
                    changeStudentEmailAddress();
                    break;
                case 6:
                    changeStudentDOB();
                    break;
                case 7:
                    changeStudentProgramme();
                    break;
                case 8:
                    changeStudentAccountStatus();
                    break;
                case 9:
                    changeStudentAccountPassword();
                    break;
                case 10:
                    changeStudentStudyMode();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Please enter valid option.");
                    Helper.pause();
            }
        } while (true);
    }

    private void changeStudentProgramme() {
        Helper.printHeader("Admin > Student: Update Student Details > Update Student Programme");
        viewStudent("Local");
        while (true) {
            System.out.print("Enter the Student ID in order to change the Programme (Q to Quit): ");
            String studentIDInput = Helper.readLine().trim().toUpperCase();
            if (studentIDInput.equals("Q")) {
                break;
            }
            if (LocalStudent.checkLocalStudentIDFormat(studentIDInput)){
                if (LocalStudent.checkDuplicateLocalStudentID(studentIDInput)){
                    LocalStudent localStudent = LocalStudent.getLocalStudent(studentIDInput);

                    System.out.println("\nExisting student :" + localStudent.getName() + " 's programme is " + localStudent.getStudentProgramme() + "\n");

                    String newInput = LocalStudent.selectProgramme();

                    System.out.print("Student's Programme: " + localStudent.getStudentProgramme() + " will be updated to " + newInput + "\n\nConfirm? y/n: ");
                    String confirm = Helper.readLine().toUpperCase();
                    if (confirm.equals("Y")) {
                        localStudent.setStudentProgramme(newInput);
                        LocalStudent.updateStudent(localStudent);
                        System.out.println("Local student's Programme changed successfully!");
                    } else {
                        System.out.println("Aborting");
                        Helper.pause();
                    }
                } else {
                    System.out.printf("StudentID (%s) does not exist!\n", studentIDInput);
                    Helper.pause();
                }
            }else {
                System.out.println("Invalid StudentID format\n");
            }
        }
    }

    private void changeStudentProgramme(String type) {
        Helper.printHeader("Admin > Student: Update Student Details > Update International Student Programme");
        viewStudent("Inter");
        while (true) {
            System.out.print("Enter the Student ID in order to change the Programme (Q to Quit): ");
            String studentIDInput = Helper.readLine().trim().toUpperCase();
            if (studentIDInput.equals("Q")) {
                break;
            }
            if (InternationalStudent.checkInterStudentIDFormat(studentIDInput)){
                if (InternationalStudent.checkDuplicateInterStudentID(studentIDInput)){
                    InternationalStudent internationalStudent = InternationalStudent.getInterStudent(studentIDInput);

                    System.out.println("\nExisting student :" + internationalStudent.getName() + " 's programme is " + internationalStudent.getStudentProgramme() + "\n");

                    String newInput = InternationalStudent.selectProgramme();

                    System.out.print("Student's Programme: " + internationalStudent.getStudentProgramme() + " will be updated to " + newInput + "\n\nConfirm? y/n: ");
                    String confirm = Helper.readLine().toUpperCase();
                    if (confirm.equals("Y")) {
                        internationalStudent.setStudentProgramme(newInput);
                        InternationalStudent.updateStudent(internationalStudent);
                        System.out.println("International student's Programme changed successfully!");
                    } else {
                        System.out.println("Aborting");
                        Helper.pause();
                    }
                } else {
                    System.out.printf("StudentID (%s) does not exist!\n", studentIDInput);
                    Helper.pause();
                }
            }else {
                System.out.println("Invalid StudentID format\n");
            }
        }
    }

    private void changeStudentCountry() {
        Helper.printHeader("Admin > Student: Update Student Details > Update International Student Country");
        viewStudent("Inter");
        while (true) {
            System.out.print("Enter the Student ID in order to change the Country (Q to Quit): ");
            String studentIDInput = Helper.readLine().trim().toUpperCase();
            if (studentIDInput.equals("Q")) {
                break;
            }
            if (InternationalStudent.checkInterStudentIDFormat(studentIDInput)){
                if (InternationalStudent.checkDuplicateInterStudentID(studentIDInput)){
                    InternationalStudent internationalStudent = InternationalStudent.getInterStudent(studentIDInput);

                    System.out.println("\nExisting student :" + internationalStudent.getName() + " 's country is " + internationalStudent.getCountry() + "\n");

                    System.out.print("Enter new student origin country: ");
                    String newInput = Helper.readLine().trim();

                    while (!InternationalStudent.checkCountry(newInput)){
                        System.out.print("Please re-enter the student origin country: ");
                        newInput = Helper.readLine().trim();
                    }

                    System.out.print("Student's origin country: " + internationalStudent.getCountry() + " will be updated to " + newInput + "\n\nConfirm? y/n: ");
                    String confirm = Helper.readLine().toUpperCase();
                    if (confirm.equals("Y")) {
                        internationalStudent.setCountry(newInput);
                        InternationalStudent.updateStudent(internationalStudent);
                        System.out.println("International student's origin country changed successfully!");
                    } else {
                        System.out.println("Aborting");
                        Helper.pause();
                    }
                } else {
                    System.out.printf("StudentID (%s) does not exist!\n", studentIDInput);
                    Helper.pause();
                }
            }else {
                System.out.println("Invalid StudentID format\n");
            }
        }
    }

    private void changeStudentStudyMode() {
        Helper.printHeader("Admin > Student: Update Student Details > Update Student Study Mode");
        viewStudent("Local");
        while (true) {
            System.out.print("Enter the Student ID in order to change the Account Study Mode (Q to Quit): ");
            String studentIDInput = Helper.readLine().trim().toUpperCase();
            if (studentIDInput.equals("Q")) {
                break;
            }
            if (LocalStudent.checkLocalStudentIDFormat(studentIDInput)){
                if (LocalStudent.checkDuplicateLocalStudentID(studentIDInput)){
                    LocalStudent localStudent = LocalStudent.getLocalStudent(studentIDInput);

                    System.out.println("\nExisting student :" + localStudent.getName() + " 's study mode is " + localStudent.getStudyMode() + "\n");

                    String newInput = LocalStudent.selectStudyMode();

                    System.out.print("Student's Study Mode: " + localStudent.getStudyMode() + " will be updated to " + newInput + "\n\nConfirm? y/n: ");
                    String confirm = Helper.readLine().toUpperCase();
                    if (confirm.equals("Y")) {
                        localStudent.setStudyMode(newInput);
                        LocalStudent.updateStudent(localStudent);
                        System.out.println("Local student's Study Mode changed successfully!");
                    } else {
                        System.out.println("Aborting");
                        Helper.pause();
                    }
                } else {
                    System.out.printf("StudentID (%s) does not exist!\n", studentIDInput);
                    Helper.pause();
                }
            }else {
                System.out.println("Invalid StudentID format\n");
            }
        }
    }

    private void changeStudentStudyMode(String type) {
        Helper.printHeader("Admin > Student: Update Student Details > Update International Student Study Mode");
        viewStudent("Inter");
        while (true) {
            System.out.print("Enter the Student ID in order to change the Account Study Mode (Q to Quit): ");
            String studentIDInput = Helper.readLine().trim().toUpperCase();
            if (studentIDInput.equals("Q")) {
                break;
            }

            if (InternationalStudent.checkInterStudentIDFormat(studentIDInput)){
                if (InternationalStudent.checkDuplicateInterStudentID(studentIDInput)){
                    InternationalStudent internationalStudent = InternationalStudent.getInterStudent(studentIDInput);

                    System.out.println("\nExisting student :" + internationalStudent.getName() + " 's study mode is " + internationalStudent.getStudyMode() + "\n");

                    String newInput = InternationalStudent.selectStudyMode();

                    System.out.print("Student's Study Mode: " + internationalStudent.getStudyMode() + " will be updated to " + newInput + "\n\nConfirm? y/n: ");
                    String confirm = Helper.readLine().toUpperCase();
                    if (confirm.equals("Y")) {
                        internationalStudent.setStudyMode(newInput);
                        InternationalStudent.updateStudent(internationalStudent);
                        System.out.println("International student's Study Mode changed successfully!");
                    } else {
                        System.out.println("Aborting");
                        Helper.pause();
                    }
                } else {
                    System.out.printf("StudentID (%s) does not exist!\n", studentIDInput);
                    Helper.pause();
                }
            }else {
                System.out.println("Invalid StudentID format\n");
            }
        }
    }

    private void changeStudentAccountStatus() {
        Helper.printHeader("Admin > Student: Update Student Details > Update Student Account Status");
        viewStudent("Local");
        while (true) {
            System.out.print("Enter the Student ID in order to change the Account Status (Q to Quit): ");
            String studentIDInput = Helper.readLine().trim().toUpperCase();
            if (studentIDInput.equals("Q")) {
                break;
            }
            if (LocalStudent.checkLocalStudentIDFormat(studentIDInput)){
                if (LocalStudent.checkDuplicateLocalStudentID(studentIDInput)){
                    LocalStudent localStudent = LocalStudent.getLocalStudent(studentIDInput);

                    System.out.println("\nExisting student :" + localStudent.getName() + " 's account status is " + localStudent.getAccountStatus() + "\n");

                    String newInput = LocalStudent.selectAccountStatus();

                    System.out.print("Student's Account Status: " + localStudent.getAccountStatus() + " will be updated to " + newInput + "\n\nConfirm? y/n: ");
                    String confirm = Helper.readLine().toUpperCase();
                    if (confirm.equals("Y")) {
                        localStudent.setAccountStatus(newInput);
                        LocalStudent.updateStudent(localStudent);
                        System.out.println("Local student's Account Status changed successfully!");
                    } else {
                        System.out.println("Aborting");
                        Helper.pause();
                    }
                } else {
                    System.out.printf("StudentID (%s) does not exist!\n", studentIDInput);
                    Helper.pause();
                }
            }else {
                System.out.println("Invalid StudentID format\n");
            }
        }
    }

    private void changeStudentAccountStatus(String type) {
        Helper.printHeader("Admin > Student: Update Student Details > Update International Student Account Status");
        viewStudent("Inter");
        while (true) {
            System.out.print("Enter the Student ID in order to change the Account Status (Q to Quit): ");
            String studentIDInput = Helper.readLine().trim().toUpperCase();
            if (studentIDInput.equals("Q")) {
                break;
            }
            if (InternationalStudent.checkInterStudentIDFormat(studentIDInput)){
                if (InternationalStudent.checkDuplicateInterStudentID(studentIDInput)){
                    InternationalStudent internationalStudent = InternationalStudent.getInterStudent(studentIDInput);

                    System.out.println("\nExisting student :" + internationalStudent.getName() + " 's account status is " + internationalStudent.getAccountStatus() + "\n");

                    String newInput = InternationalStudent.selectAccountStatus();

                    System.out.print("Student's Account Status: " + internationalStudent.getAccountStatus() + " will be updated to " + newInput + "\n\nConfirm? y/n: ");
                    String confirm = Helper.readLine().toUpperCase();
                    if (confirm.equals("Y")) {
                        internationalStudent.setAccountStatus(newInput);
                        InternationalStudent.updateStudent(internationalStudent);
                        System.out.println("International student's Account Status changed successfully!");
                    } else {
                        System.out.println("Aborting");
                        Helper.pause();
                    }
                } else {
                    System.out.printf("StudentID (%s) does not exist!\n", studentIDInput);
                    Helper.pause();
                }
            }else {
                System.out.println("Invalid StudentID format\n");
            }
        }
    }

    private void changeStudentDOB() {
        Helper.printHeader("Admin > Student: Update Student Details > Update Student DOB");
        viewStudent("Local");
        while (true) {
            System.out.print("Enter the Student ID in order to change the DOB (Q to Quit): ");
            String studentIDInput = Helper.readLine().trim().toUpperCase();
            if (studentIDInput.equals("Q")) {
                break;
            }
            if (LocalStudent.checkLocalStudentIDFormat(studentIDInput)){
                if (LocalStudent.checkDuplicateLocalStudentID(studentIDInput)){
                    LocalStudent localStudent = LocalStudent.getLocalStudent(studentIDInput);

                    System.out.println("\nExisting student :" + localStudent.getName() + " 's DOB is " + localStudent.getDOB() + "\n");

                    System.out.print("Enter new student DOB: ");
                    String newInput = Helper.readLine().trim();

                    while (!LocalStudent.checkDOB(newInput)){
                        System.out.print("Please re-enter the student DOB: ");
                        newInput = Helper.readLine().trim();
                    }

                    System.out.print("Student's DOB: " + localStudent.getDOB() + " will be updated to " + newInput + "\n\nConfirm? y/n: ");
                    String confirm = Helper.readLine().toUpperCase();
                    if (confirm.equals("Y")) {
                        localStudent.setDOB(newInput);
                        LocalStudent.updateStudent(localStudent);
                        System.out.println("Local student's DOB changed successfully!");
                    } else {
                        System.out.println("Aborting");
                        Helper.pause();
                    }
                } else {
                    System.out.printf("StudentID (%s) does not exist!\n", studentIDInput);
                    Helper.pause();
                }
            }else {
                System.out.println("Invalid StudentID format\n");
            }
        }
    }

    private void changeStudentDOB(String type) {
        Helper.printHeader("Admin > Student: Update Student Details > Update International Student DOB");
        viewStudent("Inter");
        while (true) {
            System.out.print("Enter the Student ID in order to change the DOB (Q to Quit): ");
            String studentIDInput = Helper.readLine().trim().toUpperCase();
            if (studentIDInput.equals("Q")) {
                break;
            }

            if (InternationalStudent.checkInterStudentIDFormat(studentIDInput)){
                if (InternationalStudent.checkDuplicateInterStudentID(studentIDInput)){
                    InternationalStudent internationalStudent = InternationalStudent.getInterStudent(studentIDInput);

                    System.out.println("\nExisting student :" + internationalStudent.getName() + " 's DOB is " + internationalStudent.getDOB() + "\n");

                    System.out.print("Enter new student DOB: ");
                    String newInput = Helper.readLine().trim();

                    while (!InternationalStudent.checkDOB(newInput)){
                        System.out.print("Please re-enter the student DOB: ");
                        newInput = Helper.readLine().trim();
                    }

                    System.out.print("Student's DOB: " + internationalStudent.getDOB() + " will be updated to " + newInput + "\n\nConfirm? y/n: ");
                    String confirm = Helper.readLine().toUpperCase();
                    if (confirm.equals("Y")) {
                        internationalStudent.setDOB(newInput);
                        InternationalStudent.updateStudent(internationalStudent);
                        System.out.println("International student's DOB changed successfully!");
                    } else {
                        System.out.println("Aborting");
                        Helper.pause();
                    }
                } else {
                    System.out.printf("StudentID (%s) does not exist!\n", studentIDInput);
                    Helper.pause();
                }
            }else {
                System.out.println("Invalid StudentID format\n");
            }
        }
    }

    private void changeStudentEmailAddress() {
        Helper.printHeader("Admin > Student: Update Student Details > Update Student Email Address");
        viewStudent("Local");
        while (true) {
            System.out.print("Enter the Student ID in order to change the email address (Q to Quit): ");
            String studentIDInput = Helper.readLine().trim().toUpperCase();
            if (studentIDInput.equals("Q")) {
                break;
            }
            if (LocalStudent.checkLocalStudentIDFormat(studentIDInput)){
                if (LocalStudent.checkDuplicateLocalStudentID(studentIDInput)){
                    LocalStudent localStudent = LocalStudent.getLocalStudent(studentIDInput);

                    System.out.println("\nExisting student :" + localStudent.getName() + " 's email address is " + localStudent.getEmailAddress() + "\n");

                    System.out.print("Enter new student email address: ");
                    String newInput = Helper.readLine().trim();

                    while (!LocalStudent.checkEmailAddress(newInput)){
                        System.out.print("Please re-enter the student email address: ");
                        newInput = Helper.readLine().trim();
                    }

                    System.out.print("Student's email address: " + localStudent.getEmailAddress() + " will be updated to " + newInput + "\n\nConfirm? y/n: ");
                    String confirm = Helper.readLine().toUpperCase();
                    if (confirm.equals("Y")) {
                        localStudent.setEmailAddress(newInput);
                        LocalStudent.updateStudent(localStudent);
                        System.out.println("Local student's email address changed successfully!");
                    } else {
                        System.out.println("Aborting");
                        Helper.pause();
                    }
                } else {
                    System.out.printf("StudentID (%s) does not exist!\n", studentIDInput);
                    Helper.pause();
                }
            }else {
                System.out.println("Invalid StudentID format\n");
            }
        }
    }

    private void changeStudentEmailAddress(String type) {
        Helper.printHeader("Admin > Student: Update Student Details > Update International Student Email Address");
        viewStudent("Inter");
        while (true) {
            System.out.print("Enter the Student ID in order to change the email address (Q to Quit): ");
            String studentIDInput = Helper.readLine().trim().toUpperCase();
            if (studentIDInput.equals("Q")) {
                break;
            }
            if (InternationalStudent.checkInterStudentIDFormat(studentIDInput)){
                if (InternationalStudent.checkDuplicateInterStudentID(studentIDInput)){
                    InternationalStudent internationalStudent = InternationalStudent.getInterStudent(studentIDInput);

                    System.out.println("\nExisting student :" + internationalStudent.getName() + " 's email address is " + internationalStudent.getEmailAddress() + "\n");

                    System.out.print("Enter new student email address: ");
                    String newInput = Helper.readLine().trim();

                    while (!InternationalStudent.checkEmailAddress(newInput)){
                        System.out.print("Please re-enter the student email address: ");
                        newInput = Helper.readLine().trim();
                    }

                    System.out.print("Student's email address: " + internationalStudent.getEmailAddress() + " will be updated to " + newInput + "\n\nConfirm? y/n: ");
                    String confirm = Helper.readLine().toUpperCase();
                    if (confirm.equals("Y")) {
                        internationalStudent.setEmailAddress(newInput);
                        InternationalStudent.updateStudent(internationalStudent);
                        System.out.println("International student's email address changed successfully!");
                    } else {
                        System.out.println("Aborting");
                        Helper.pause();
                    }
                } else {
                    System.out.printf("StudentID (%s) does not exist!\n", studentIDInput);
                    Helper.pause();
                }
            }else {
                System.out.println("Invalid StudentID format\n");
            }
        }
    }

    private void changeStudentContactNumber() {
        Helper.printHeader("Admin > Student: Update Student Details > Update Student Contact Number");
        viewStudent("Local");
        while (true) {
            System.out.print("Enter the Student ID in order to change the contact number (Q to Quit): ");
            String studentIDInput = Helper.readLine().trim().toUpperCase();
            if (studentIDInput.equals("Q")) {
                break;
            }
            if (LocalStudent.checkLocalStudentIDFormat(studentIDInput)){
                if (LocalStudent.checkDuplicateLocalStudentID(studentIDInput)){
                    LocalStudent localStudent = LocalStudent.getLocalStudent(studentIDInput);

                    System.out.println("\nExisting student :" + localStudent.getName() + " 's contact number is " + localStudent.getContactNumber() + "\n");

                    System.out.print("Enter new student contact number: ");
                    String newInput = Helper.readLine().trim();

                    while (!LocalStudent.checkContactNumber(newInput)){
                        System.out.print("Please re-enter the student contact number: ");
                        newInput = Helper.readLine().trim();
                    }

                    System.out.print("Student's contact number: " + localStudent.getContactNumber() + " will be updated to " + newInput + "\n\nConfirm? y/n: ");
                    String confirm = Helper.readLine().toUpperCase();
                    if (confirm.equals("Y")) {
                        localStudent.setContactNumber(newInput);
                        LocalStudent.updateStudent(localStudent);
                        System.out.println("Local student's contact number changed successfully!");
                    } else {
                        System.out.println("Aborting");
                        Helper.pause();
                    }
                } else {
                    System.out.printf("StudentID (%s) does not exist!\n", studentIDInput);
                    Helper.pause();
                }
            }else {
                System.out.println("Invalid StudentID format\n");
            }
        }
    }

    private void changeStudentContactNumber(String type) {
        Helper.printHeader("Admin > Student: Update Student Details > Update International Student Contact Number");
        viewStudent("Inter");
        while (true) {
            System.out.print("Enter the Student ID in order to change the contact number (Q to Quit): ");
            String studentIDInput = Helper.readLine().trim().toUpperCase();
            if (studentIDInput.equals("Q")) {
                break;
            }
            if (InternationalStudent.checkInterStudentIDFormat(studentIDInput)){
                if (InternationalStudent.checkDuplicateInterStudentID(studentIDInput)){
                    InternationalStudent internationalStudent = InternationalStudent.getInterStudent(studentIDInput);

                    System.out.println("\nExisting student :" + internationalStudent.getName() + " 's contact number is " + internationalStudent.getContactNumber() + "\n");

                    System.out.print("Enter new student contact number: ");
                    String newInput = Helper.readLine().trim();

                    while (!LocalStudent.checkContactNumber(newInput)){
                        System.out.print("Please re-enter the student contact number: ");
                        newInput = Helper.readLine().trim();
                    }

                    System.out.print("Student's contact number: " + internationalStudent.getContactNumber() + " will be updated to " + newInput + "\n\nConfirm? y/n: ");
                    String confirm = Helper.readLine().toUpperCase();
                    if (confirm.equals("Y")) {
                        internationalStudent.setContactNumber(newInput);
                        InternationalStudent.updateStudent(internationalStudent);
                        System.out.println("International student's contact number changed successfully!");
                    } else {
                        System.out.println("Aborting");
                        Helper.pause();
                    }
                } else {
                    System.out.printf("StudentID (%s) does not exist!\n", studentIDInput);
                    Helper.pause();
                }
            }else {
                System.out.println("Invalid StudentID format\n");
            }
        }
    }

    private void changeStudentGender() {
        Helper.printHeader("Admin > Student: Update Student Details > Update Student Gender");
        viewStudent("Local");
        while (true) {
            System.out.print("Enter the Student ID in order to change the gender (Q to Quit): ");
            String studentIDInput = Helper.readLine().trim().toUpperCase();
            if (studentIDInput.equals("Q")) {
                break;
            }
            if (LocalStudent.checkLocalStudentIDFormat(studentIDInput)){
                if (LocalStudent.checkDuplicateLocalStudentID(studentIDInput)){
                    LocalStudent localStudent = LocalStudent.getLocalStudent(studentIDInput);

                    System.out.println("\nExisting student :" + localStudent.getName() + " 's gender is " + localStudent.getGender() + "\n");


                    String newInput = LocalStudent.selectGender();

                    System.out.print("Student's gender: " + localStudent.getGender() + " will be updated to " + newInput + "\n\nConfirm? y/n: ");
                    String confirm = Helper.readLine().toUpperCase();
                    if (confirm.equals("Y")) {
                        localStudent.setIC(newInput);
                        LocalStudent.updateStudent(localStudent);
                        System.out.println("Local student's gender changed successfully!");
                    } else {
                        System.out.println("Aborting");
                        Helper.pause();
                    }
                } else {
                    System.out.printf("StudentID (%s) does not exist!\n", studentIDInput);
                    Helper.pause();
                }
            }else {
                System.out.println("Invalid StudentID format\n");
            }
        }
    }

    private void changeStudentGender(String type) {
        Helper.printHeader("Admin > Student: Update Student Details > Update International Student Gender");
        viewStudent("Inter");
        while (true) {
            System.out.print("Enter the Student ID in order to change the gender (Q to Quit): ");
            String studentIDInput = Helper.readLine().trim().toUpperCase();
            if (studentIDInput.equals("Q")) {
                break;
            }
            if (InternationalStudent.checkInterStudentIDFormat(studentIDInput)){
                if (InternationalStudent.checkDuplicateInterStudentID(studentIDInput)){
                    InternationalStudent internationalStudent = InternationalStudent.getInterStudent(studentIDInput);

                    System.out.println("\nExisting student :" + internationalStudent.getName() + " 's gender is " + internationalStudent.getGender() + "\n");


                    String newInput = LocalStudent.selectGender();

                    System.out.print("Student's gender: " + internationalStudent.getGender() + " will be updated to " + newInput + "\n\nConfirm? y/n: ");
                    String confirm = Helper.readLine().toUpperCase();
                    if (confirm.equals("Y")) {
                        internationalStudent.setIC(newInput);
                        InternationalStudent.updateStudent(internationalStudent);
                        System.out.println("International student's gender changed successfully!");
                    } else {
                        System.out.println("Aborting");
                        Helper.pause();
                    }
                } else {
                    System.out.printf("StudentID (%s) does not exist!\n", studentIDInput);
                    Helper.pause();
                }
            }else {
                System.out.println("Invalid StudentID format\n");
            }
        }
    }

    private void changeStudentIC(String type) {
        Helper.printHeader("Admin > Student: Update Student Details > Update International Student Passport Number");
        viewStudent("Inter");
        while (true) {
            System.out.print("Enter the International Student ID in order to change the account Passport Number (Q to Quit): ");
            String studentIDInput = Helper.readLine().trim().toUpperCase();
            if (studentIDInput.equals("Q")) {
                break;
            }
            if (InternationalStudent.checkInterStudentIDFormat(studentIDInput)){
                if (InternationalStudent.checkDuplicateInterStudentID(studentIDInput)){
                    InternationalStudent internationalStudent = InternationalStudent.getInterStudent(studentIDInput);

                    System.out.println("\nExisting student " + internationalStudent.getName() + " 's passport number is " + internationalStudent.getIC() + "\n");

                    System.out.print("Enter new passport number: ");
                    String newInput = Helper.readLine().trim();

                    while (!InternationalStudent.checkIC(newInput)){
                        System.out.print("Please re-enter new passport number: ");
                        newInput = Helper.readLine().trim();
                    }

                    System.out.print("Student's passport number: " + internationalStudent.getIC() + " will be updated to " + newInput + "\n\nConfirm? y/n: ");
                    String confirm = Helper.readLine().toUpperCase();
                    if (confirm.equals("Y")) {
                        internationalStudent.setIC(newInput);
                        InternationalStudent.updateStudent(internationalStudent);
                        System.out.println("International student's passport number changed successfully!");
                    } else {
                        System.out.println("Aborting");
                        Helper.pause();
                    }
                } else {
                    System.out.printf("StudentID (%s) does not exist!\n", studentIDInput);
                    Helper.pause();
                }
            }else {
                System.out.println("Invalid StudentID format\n");
            }
        }
    }

    private void changeStudentIC() {
        Helper.printHeader("Admin > Student: Update Student Details > Update Student IC Number");
        viewStudent("Local");
        while (true) {
            System.out.print("Enter the Student ID in order to change the account password (Q to Quit): ");
            String studentIDInput = Helper.readLine().trim().toUpperCase();
            if (studentIDInput.equals("Q")) {
                break;
            }
            if (LocalStudent.checkLocalStudentIDFormat(studentIDInput)){
                if (LocalStudent.checkDuplicateLocalStudentID(studentIDInput)){
                    LocalStudent localStudent = LocalStudent.getLocalStudent(studentIDInput);

                    System.out.println("\nExisting student " + localStudent.getName() + " 's IC number is " + localStudent.getIC() + "\n");

                    System.out.print("Enter new student IC number: ");
                    String newInput = Helper.readLine().trim();

                    while (!LocalStudent.checkIC(newInput)){
                        System.out.print("Please re-enter new student IC number: ");
                        newInput = Helper.readLine().trim();
                    }

                    System.out.print("Student's IC number: " + localStudent.getIC() + " will be updated to " + newInput + "\n\nConfirm? y/n: ");
                    String confirm = Helper.readLine().toUpperCase();
                    if (confirm.equals("Y")) {
                        localStudent.setIC(newInput);
                        LocalStudent.updateStudent(localStudent);
                        System.out.println("Local student's IC changed successfully!");
                        Helper.pause();
                    } else {
                        System.out.println("Aborting");
                        Helper.pause();
                    }
                } else {
                    System.out.printf("StudentID (%s) does not exist!\n", studentIDInput);
                    Helper.pause();
                }
            }else {
                System.out.println("Invalid StudentID format\n");
            }
        }
    }

    public void studentMenuChangeStudentAccountPassword(String studentID) {
        String decrypStudentPW = null;
        while (true) {
            Helper.printHeader("Student > Update Account Password");
            System.out.print("\nPlease enter your existing password to proceed (Q to Quit): ");
            String studentPWInput = Helper.readLine().trim();
            if (studentPWInput.equals("Q")) {
                break;
            }

            if (studentID.length() ==10){
                if (LocalStudent.checkStudentPassword(studentPWInput)){
                    LocalStudent localStudent = LocalStudent.getLocalStudent(studentID);

                    //Decrypt student password from database
                    try {
                        Helper aes = new Helper();
                        decrypStudentPW = aes.decrypt(localStudent.getStudentPW());
                    }catch (Exception ex){
                        Logger.getLogger(Helper.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    while (!decrypStudentPW.equals(studentPWInput)){
                        System.out.print("Incorrect Password. Please re-enter your existing password to proceed (Q to Quit): ");
                        studentPWInput = Helper.readLine().trim();
                        if (studentPWInput.equals("Q")) {
                            return;
                        }
                    }

                    System.out.println("\nYour existing password is " + decrypStudentPW + "\n");

                    System.out.println("\n\tA password must have at least 7 characters.");
                    System.out.println("\tA password consists of only letters and digits.");
                    System.out.println("\tA password must contain at least one letter and at least one digit." + "\n");

                    System.out.print("Enter your new student account password: ");
                    String newInput = Helper.readLine().trim();

                    while (!LocalStudent.checkStudentPassword(newInput)){
                        System.out.print("\nIncorrect password sequence. Please re-enter new student password: ");
                        newInput = Helper.readLine().trim();
                    }

                    System.out.print("Your old password will be updated to " + newInput + "\n\nConfirm? y/n: ");
                    String confirm = Helper.readLine().toUpperCase();
                    if (confirm.equals("Y")) {

                        //Encrypt student password and save it to database
                        try {
                            Helper aes = new Helper();
                            newInput = aes.encrypt(newInput);
                        }catch (Exception ex){
                            Logger.getLogger(Helper.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        localStudent.setStudentPW(newInput);
                        LocalStudent.updateStudent(localStudent);
                        System.out.println("Account password changed successfully!");
                        Helper.pause();
                    } else {
                        System.out.println("Aborting");
                        Helper.pause();
                    }
                }
            } else if (studentID.length() ==11){
                if (InternationalStudent.checkStudentPassword(studentPWInput)) {
                    InternationalStudent internationalStudent = InternationalStudent.getInterStudent(studentID);

                    //Decrypt student password from database
                    try {
                        Helper aes = new Helper();
                        decrypStudentPW = aes.decrypt(internationalStudent.getStudentPW());
                    } catch (Exception ex) {
                        Logger.getLogger(Helper.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    while (!decrypStudentPW.equals(studentPWInput)) {
                        System.out.print("Incorrect Password. Please re-enter your existing password to proceed (Q to Quit): ");
                        studentPWInput = Helper.readLine().trim();
                        if (studentPWInput.equals("Q")) {
                            return;
                        }
                    }

                    System.out.println("\nYour existing password is " + decrypStudentPW + "\n");

                    System.out.println("\n\tA password must have at least 7 characters.");
                    System.out.println("\tA password consists of only letters and digits.");
                    System.out.println("\tA password must contain at least one letter and at least one digit." + "\n");

                    System.out.print("Enter your new student account password: ");
                    String newInput = Helper.readLine().trim();

                    while (!InternationalStudent.checkStudentPassword(newInput)) {
                        System.out.print("\nIncorrect password sequence. Please re-enter new student password: ");
                        newInput = Helper.readLine().trim();
                    }

                    System.out.print("Your old password will be updated to " + newInput + "\n\nConfirm? y/n: ");
                    String confirm = Helper.readLine().toUpperCase();
                    if (confirm.equals("Y")) {

                        //Encrypt student password and save it to database
                        try {
                            Helper aes = new Helper();
                            newInput = aes.encrypt(newInput);
                        } catch (Exception ex) {
                            Logger.getLogger(Helper.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        internationalStudent.setStudentPW(newInput);
                        InternationalStudent.updateStudent(internationalStudent);
                        System.out.println("Account password changed successfully!");
                        Helper.pause();
                    } else {
                        System.out.println("Aborting");
                        Helper.pause();
                    }
                }
            } else {
                System.out.println("\nIncorrect Student Password. Please try again.");
            }
        }
    }













    private void changeStudentAccountPassword() {
        Helper.printHeader("Admin > Student: Update Student Details > Update Student Account Password");
        viewStudent("Local");
        String decrypStudentPW = null;
        while (true) {
            System.out.print("Enter the Student ID in order to change the account password (Q to Quit): ");
            String studentIDInput = Helper.readLine().trim().toUpperCase();
            if (studentIDInput.equals("Q")) {
                break;
            }
            if (LocalStudent.checkLocalStudentIDFormat(studentIDInput)){
                if (LocalStudent.checkDuplicateLocalStudentID(studentIDInput)){
                    LocalStudent localStudent = LocalStudent.getLocalStudent(studentIDInput);

                    //Decrypt student password from database
                    try {
                        Helper aes = new Helper();
                        decrypStudentPW = aes.decrypt(localStudent.getStudentPW());
                    }catch (Exception ex){
                        Logger.getLogger(Helper.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    System.out.println("\nExisting " + localStudent.getName() + " 's password is " + decrypStudentPW + "\n");

                    System.out.println("\n\tA password must have at least 7 characters.");
                    System.out.println("\tA password consists of only letters and digits.");
                    System.out.println("\tA password must contain at least one letter and at least one digit." + "\n");

                    System.out.print("Enter the new student account password: ");
                    String newInput = Helper.readLine().trim();

                    while (!LocalStudent.checkStudentPassword(newInput)){
                        System.out.print("\nPlease re-enter student password: ");
                        newInput = Helper.readLine().trim();
                    }

                    System.out.print(localStudent.getName() + "'s old password will be updated to " + newInput + "\n\nConfirm? y/n: ");
                    String confirm = Helper.readLine().toUpperCase();
                    if (confirm.equals("Y")) {

                        //Encrypt student password and save it to database
                        try {
                            Helper aes = new Helper();
                            newInput = aes.encrypt(newInput);
                        }catch (Exception ex){
                            Logger.getLogger(Helper.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        localStudent.setStudentPW(newInput);
                        LocalStudent.updateStudent(localStudent);
                        System.out.println("Student account password changed successfully!");
                    } else {
                        System.out.println("Aborting");
                        Helper.pause();
                    }
                } else {
                    System.out.printf("StudentID (%s) does not exist!\n", studentIDInput);
                    Helper.pause();
                }
            }else {
                System.out.println("Invalid StudentID format\n");
            }
        }
    }

    private void changeStudentAccountPassword(String type) {
        Helper.printHeader("Admin > Student: Update Student Details > Update Student Account Password");
        viewStudent("Inter");
        String decrypStudentPW = null;
        while (true) {
            System.out.print("Enter the International Student ID in order to change the account password (Q to Quit): ");
            String studentIDInput = Helper.readLine().trim().toUpperCase();
            if (studentIDInput.equals("Q")) {
                break;
            }
            if (InternationalStudent.checkInterStudentIDFormat(studentIDInput)){
                if (InternationalStudent.checkDuplicateInterStudentID(studentIDInput)){
                    InternationalStudent internationalStudent = InternationalStudent.getInterStudent(studentIDInput);

                    //Decrypt student password from database
                    try {
                        Helper aes = new Helper();
                        decrypStudentPW = aes.decrypt(internationalStudent.getStudentPW());
                    }catch (Exception ex){
                        Logger.getLogger(Helper.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    System.out.println("\nExisting " + internationalStudent.getName() + " 's password is " + decrypStudentPW + "\n");

                    System.out.println("\n\tA password must have at least 7 characters.");
                    System.out.println("\tA password consists of only letters and digits.");
                    System.out.println("\tA password must contain at least one letter and at least one digit." + "\n");

                    System.out.print("Enter the new international account password: ");
                    String newInput = Helper.readLine().trim();

                    while (!InternationalStudent.checkStudentPassword(newInput)){
                        System.out.print("\nPlease re-enter student password: ");
                        newInput = Helper.readLine().trim();
                    }

                    System.out.print(internationalStudent.getName() + "'s old password will be updated to " + newInput + "\n\nConfirm? y/n: ");
                    String confirm = Helper.readLine().toUpperCase();
                    if (confirm.equals("Y")) {

                        //Encrypt student password and save it to database
                        try {
                            Helper aes = new Helper();
                            newInput = aes.encrypt(newInput);
                        }catch (Exception ex){
                            Logger.getLogger(Helper.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        internationalStudent.setStudentPW(newInput);
                        InternationalStudent.updateStudent(internationalStudent);
                        System.out.println("Student account password changed successfully!");
                    } else {
                        System.out.println("Aborting");
                        Helper.pause();
                    }
                } else {
                    System.out.printf("StudentID (%s) does not exist!\n", studentIDInput);
                    Helper.pause();
                }
            }else {
                System.out.println("Invalid StudentID format\n");
            }
        }
    }

    private void changeStudentName() {
        Helper.printHeader("Admin > Student: Update Student Details > Update Local Student Name");
        String newName, confirm;
        while (true) {
            viewStudent("Local");
            System.out.print("Enter the Local Student ID in order to change the name (Q to Quit): ");
            String studentIDInput = Helper.readLine().toUpperCase().trim();
            if (studentIDInput.equals("Q")) {
                break;
            }
            if (LocalStudent.checkLocalStudentIDFormat(studentIDInput)){
                if (LocalStudent.checkDuplicateLocalStudentID(studentIDInput)){
                    LocalStudent localStudent = LocalStudent.getLocalStudent(studentIDInput);
                    System.out.println("Existing Student Name is :" + localStudent.getName());
                    System.out.println("\n");
                    System.out.print("Enter New Student Name: ");
                    newName = Helper.readLine().toUpperCase();
                    System.out.print("Existing student name " + localStudent.getName() + " will be updated to " + newName + "\n\nConfirm? y/n: ");
                    confirm = Helper.readLine().toUpperCase();
                    if (confirm.equals("Y")) {
                        localStudent.setName(newName);
                        LocalStudent.updateStudent(localStudent);
                        System.out.println("Local Student Name Changed Successfully!\n");
                        Helper.systemSleep(1);
                    } else {
                        System.out.println("Aborting");
                        Helper.pause();
                    }
                } else {
                    System.out.printf("StudentID (%s) does not exist!\n", studentIDInput);
                    Helper.pause();
                }
            }else {
                System.out.println("Invalid StudentID format\n");
            }
        }
    }

    private void changeStudentName(String type) {
        Helper.printHeader("Admin > Student: Update Student Details > Update International Student Name");
        String newName, confirm;
        while (true) {
            viewStudent("Inter");
            System.out.print("Enter the International Student ID in order to change the name (Q to Quit): ");
            String studentIDInput = Helper.readLine().toUpperCase().trim();
            if (studentIDInput.equals("Q")) {
                break;
            }
            if (InternationalStudent.checkInterStudentIDFormat(studentIDInput)){
                if (InternationalStudent.checkDuplicateInterStudentID(studentIDInput)){
                    InternationalStudent internationalStudent = InternationalStudent.getInterStudent(studentIDInput);
                    System.out.println("Existing Student Name is :" + internationalStudent.getName());
                    System.out.println("\n");
                    System.out.print("Enter New Student Name: ");
                    newName = Helper.readLine().toUpperCase();
                    System.out.print("Existing student name " + internationalStudent.getName() + " will be updated to " + newName + "\n\nConfirm? y/n: ");
                    confirm = Helper.readLine().toUpperCase();
                    if (confirm.equals("Y")) {
                        internationalStudent.setName(newName);
                        InternationalStudent.updateStudent(internationalStudent);
                        System.out.println("International Student Name Changed Successfully!\n");
                        Helper.systemSleep(1);
                    } else {
                        System.out.println("Aborting");
                        Helper.pause();
                    }
                } else {
                    System.out.printf("StudentID (%s) does not exist!\n", studentIDInput);
                    Helper.pause();
                }
            }else {
                System.out.println("Invalid StudentID format\n");
            }
        }
    }

    private void viewStudentDetails(Assignment mainApp){
        Helper.printHeader("Admin > Student: View Student Details ");
        while (true) {
            System.out.println();
            viewStudent();
            Helper.printLine(59);
            System.out.print("\nEnter the Student ID in order to view the student details (Q to Quit): ");
            String studentIDInput = Helper.readLine().toUpperCase().trim();
            if (studentIDInput.equals("Q")) {
                break;
            }
            try{
                if(studentIDInput.length() == 10 && LocalStudent.checkLocalStudentIDFormat(studentIDInput) && LocalStudent.checkDuplicateLocalStudentID(studentIDInput)){
                    System.out.println("\nLocal Student Details:");

                    String query = "SELECT * FROM localstudent WHERE localStudentID= ?";
                    pst = con.prepareStatement(query);
                    pst.setString(1, studentIDInput);
                    rs = pst.executeQuery();

                    while (rs.next()) {
                        System.out.printf("%-40s: %s\n", "Student ID", rs.getString("localStudentID"));
                        System.out.printf("%-40s: %s\n", "Student Encrypted Password",rs.getString("password"));
                        System.out.printf("%-40s: %s\n", "Student Name", rs.getString("name"));
                        System.out.printf("%-40s: %s\n", "Student IC", rs.getString("ICNumber"));
                        System.out.printf("%-40s: %s\n", "Student Gender", rs.getString("gender"));
                        System.out.printf("%-40s: %s\n", "Student Contact Number", rs.getString("contactNumber"));
                        System.out.printf("%-40s: %s\n", "Student Email Address", rs.getString("emailAddress"));
                        System.out.printf("%-40s: %s\n", "Student DOB", rs.getString("DOB"));
                        System.out.printf("%-40s: %s\n", "Student Account Status", rs.getString("accountStatus"));
                        System.out.printf("%-40s: %s\n", "Student Programme", rs.getString("programme"));
                        System.out.printf("%-40s: %s\n", "Student Study Mode", rs.getString("studyMode"));
                    }
                    Helper.printLine(59);
                    Helper.pause();
                    Helper.printSmallSpace();
                } else if(studentIDInput.length() == 11 && InternationalStudent.checkInterStudentIDFormat(studentIDInput) && InternationalStudent.checkDuplicateInterStudentID(studentIDInput)){
                    System.out.println("\nInternational Student Details:");

                    String query = "SELECT * FROM interstudent WHERE interStudentID=?";
                    pst = con.prepareStatement(query);
                    pst.setString(1, studentIDInput);
                    rs = pst.executeQuery();

                    while (rs.next()) {
                        System.out.printf("%-40s: %s\n", "Student ID", rs.getString("interStudentID"));
                        System.out.printf("%-40s: %s\n", "Student Encrypted Password",rs.getString("password"));
                        System.out.printf("%-40s: %s\n", "Student Name", rs.getString("name"));
                        System.out.printf("%-40s: %s\n", "Student IC", rs.getString("ICNumber"));
                        System.out.printf("%-40s: %s\n", "Student Gender", rs.getString("gender"));
                        System.out.printf("%-40s: %s\n", "Student Contact Number", rs.getString("contactNumber"));
                        System.out.printf("%-40s: %s\n", "Student Email Address", rs.getString("emailAddress"));
                        System.out.printf("%-40s: %s\n", "Student DOB", rs.getString("DOB"));
                        System.out.printf("%-40s: %s\n", "Student Account Status", rs.getString("accountStatus"));
                        System.out.printf("%-40s: %s\n", "Student Programme", rs.getString("programme"));
                        System.out.printf("%-40s: %s\n", "Student Study Mode", rs.getString("studyMode"));
                        System.out.printf("%-40s: %s\n", "Student Origin Country", rs.getString("country"));
                    }
                    Helper.printLine(59);
                    Helper.pause();
                    Helper.printSmallSpace();
                }else
                    System.out.println("Student Record not found. Please double check the input.");
            }catch (SQLException e) {
                e.getStackTrace();
            }
        }
    }

    private void viewStudent() {
        try{
            int count = 0;
            Helper.printLine(59);
            System.out.println(String.format("║%-20s ║ %-50s ║ %-12s", "Student ID", "Student Name", "Student Status"));
            Helper.printLine(59);

            String query = "SELECT localStudentID, name, accountStatus FROM localstudent order by accountStatus;";
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();

            Helper.printLine(59);
            System.out.println("Local Student List");
            Helper.printLine(59);

            while (rs.next()) {
                System.out.printf("║%-20s ║ %-50s ║ %-12s\n",
                        rs.getString("localStudentID"), rs.getString("name"), rs.getString("accountStatus"));
                count++;
                Helper.printLine(59);
            }

            Helper.printLine(59);
            System.out.println("International Student List");
            Helper.printLine(59);

            query = "SELECT interStudentID, name, accountStatus FROM interstudent order by accountStatus;;";
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();

            while (rs.next()) {
                System.out.printf("║%-20s ║ %-50s ║ %-12s\n",
                        rs.getString("interStudentID"), rs.getString("name"), rs.getString("accountStatus"));
                count++;
                Helper.printLine(59);
            }
            System.out.println("\nTotal Student Count: " + count);
        } catch (SQLException e) {
            e.getStackTrace();
        }
    }

    private void viewStudent(String type) {
        int count = 0;
        try{
            if (type.equals("Local")){
                Helper.printLine(59);
                System.out.println(String.format("║%-20s ║ %-50s ║ %-12s", "Student ID", "Student Name", "Student Status"));
                Helper.printLine(59);

                String query = "SELECT localStudentID, name, accountStatus FROM localstudent order by accountStatus;";
                pst = con.prepareStatement(query);
                rs = pst.executeQuery();

                while (rs.next()) {
                    System.out.printf("║%-20s ║ %-50s ║ %-12s\n",
                            rs.getString("localStudentID"), rs.getString("name"), rs.getString("accountStatus"));
                    count++;
                    Helper.printLine(59);
                }
            } else {
                Helper.printLine(59);
                System.out.println(String.format("║%-20s ║ %-50s ║ %-12s", "Student ID", "Student Name", "Student Status"));
                Helper.printLine(59);

                String query = "SELECT interStudentID, name, accountStatus FROM interstudent order by accountStatus;";
                pst = con.prepareStatement(query);
                rs = pst.executeQuery();

                while (rs.next()) {
                    System.out.printf("║%-20s ║ %-50s ║ %-12s\n",
                            rs.getString("interStudentID"), rs.getString("name"), rs.getString("accountStatus"));
                    count++;
                    Helper.printLine(59);
                }
            }
            System.out.println("\nTotal Student Count: " + count);
        } catch (SQLException e) {
            e.getStackTrace();
        }
    }

    private void addStudent(Assignment mainApp) {
        String choice;
        Helper.printHeader("Admin > Student: Type of Student");
        viewStudent();
        while (true) {
            System.out.println("\n\nEnter student type: ");
            System.out.println("[1] Local Student");
            System.out.println("[2] International Student");
            System.out.printf("%-50s: ", "Enter choice (Q to quit)");
            choice = Helper.readLine().toUpperCase().trim();
            switch (choice) {
                case "1":
                    addLocalStudent(mainApp);
                    break;
                case "2":
                    addInternationalStudent(mainApp);
                    break;
                case "Q":
                    return;
                default:
                    System.out.println("Please enter valid option.\n");
            }
        }
    }

    private void addLocalStudent(Assignment mainApp) {
        String studentName;
        String studentIC;
        String studentGender;
        String studentContactNumber;
        String studentEmailAddress;
        String studentDOB;
        String studentStatus = "Active";
        String studentPW;
        String studentProgramme;
        String[] programme = {"RMM", "RSD", "REI"};
        String studyMode;

        Helper.printHeader("Admin > Student: Add Local Student Details");
        while (true) {
            viewStudent("Local");
            Helper.printLine(59);
            System.out.printf("%-50s: ", "\nEnter new student ID Eg:21WMR00001 (Q to quit)");
            String studentIDInput = Helper.readLine().toUpperCase().trim();
            if (studentIDInput.equals("Q")) {
                break;
            }
            if (LocalStudent.checkLocalStudentIDFormat(studentIDInput)) {
                if (!LocalStudent.checkDuplicateLocalStudentID(studentIDInput)) {
                    try {
                        while (true) {
                            System.out.printf("%-50s: ", "Enter Student name");
                            studentName = Helper.readLine().toUpperCase();
                            if (studentName.length() <= 50 && LocalStudent.checkNameFormat(studentName)) {
                                break;
                            }
                        }

                        while (true) {
                            System.out.printf("%-50s: ", "Enter Student IC     eg:999999-99-9999");
                            studentIC = Helper.readLine().trim();
                            if (LocalStudent.checkIC(studentIC)) {
                                break;
                            }
                        }
                        
                        while (true){
                            studentGender = LocalStudent.selectGender();
                            break;
                            
                        }

                        
                        
                        
                        

                        while (true) {
                            try {
                                System.out.printf("%-50s: ", "Enter student contact number eg: 011-9999999");
                                studentContactNumber = Helper.readLine().trim();
                                if (LocalStudent.checkContactNumber(studentContactNumber)) {
                                    break;
                                }
                            } catch (Exception e) {
                                System.out.println("Invalid phone number length. Do try again.");
                            }
                        }

                        while (true) {
                            System.out.printf("%-50s: ", "Enter Student date of birth   eg:dd/mm/yyyy");
                            studentDOB = Helper.readLine().trim();
                            if (LocalStudent.checkDOB(studentDOB)) {
                                break;
                            }
                        }

                        while (true) {
                            System.out.printf("%-50s: ", "Enter Student email address");
                            studentEmailAddress = Helper.readLine().trim();
                            if (studentEmailAddress.length() <= 50 && LocalStudent.checkEmailAddress(studentEmailAddress)) {
                                break;
                            }
                        }

                        while (true) {
                            try {
                                System.out.println("Programme list option: ");
                                for (int i = 0; i < programme.length; i++) {
                                    System.out.print("\t\t\t\t\t\t" + (i + 1) + ". " + programme[i] + "\n");
                                }
                                System.out.printf("%-50s: ", "Enter student programme option");
                                int selection = Integer.parseInt(Helper.readLine()) - 1;

                                if (selection < 0 || selection > programme.length) {
                                    throw new Exception();
                                }
                                studentProgramme = programme[selection];
                                break;
                            } catch (Exception e) {
                                System.out.println("Please enter digit only.");
                            }
                        }

                        studyMode = LocalStudent.selectStudyMode();

                        Helper.printSmallSpace();

                        System.out.printf("%-50s ", "Student Password will be same the as student ID for the first time registration: " + studentIDInput);
                        studentPW = studentIDInput;
                        System.out.println();
                        Helper.systemSleep(1);

                        System.out.println("\n\nNew Student Details: ");
                        System.out.printf("%-40s: %s\n", "Student ID: ", studentIDInput);
                        System.out.printf("%-40s: %s\n", "Student Password: ", studentPW);
                        System.out.printf("%-40s: %s\n", "Student Name: ", studentName);
                        System.out.printf("%-40s: %s\n", "Student IC: ", studentIC);
                        System.out.printf("%-40s: %s\n", "Student Gender: ", studentGender);
                        System.out.printf("%-40s: %s\n", "Student Contact Number: ", studentContactNumber);
                        System.out.printf("%-40s: %s\n", "Student Email Address: ", studentEmailAddress);
                        System.out.printf("%-40s: %s\n", "Student DOB: ", studentDOB);
                        System.out.printf("%-40s: %s\n", "Student Programme: ", studentProgramme);
                        System.out.printf("%-40s: %s\n", "Study Mode: ", studyMode);
                        System.out.printf("%-40s: %s\n", "Student Account Status: ", studentStatus);
                        Helper.printLine(59);
                        System.out.printf("%-40s: ", "Are you confirm to Save? y/n");
                        String confirm = Helper.readLine().toUpperCase();

                        //Encrypt student password and save it to database
                        try {
                            Helper aes = new Helper();
                            studentPW = aes.encrypt(studentPW);
                        }catch (Exception ex){
                            Logger.getLogger(Helper.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        if (confirm.equals("Y")) {
                            LocalStudent localStudent = new LocalStudent(studentIDInput, studentPW, studentName, studentIC, studentGender, studentContactNumber,
                                    studentEmailAddress, studentDOB, studentStatus, studentProgramme, studyMode);

                            LocalStudent.addLocalStudent(localStudent);
                            Helper.pause();
                            Helper.printSmallSpace();
                        } else {
                            System.out.println("Aborting");
                            Helper.pause();
                            Helper.printSmallSpace();
                        }
                    } catch (Exception e) {
                        System.out.println(e.getLocalizedMessage());
                        Helper.pause();
                        Helper.printSmallSpace();
                    }
                } else {
                    System.out.printf("StudentID %s already exist!\n", studentIDInput);
                }
            }
        }
    }

    private void addInternationalStudent(Assignment mainApp) {
        String studentName;
        String studentIC;
        String studentGender;
        String studentContactNumber;
        String studentEmailAddress;
        String studentDOB;
        String studentStatus = "Active";
        String studentPW;
        String studentProgramme;
        String[] programme = {"RMM", "RSD", "REI"};
        String studyMode;
        String country;

        Helper.printHeader("Admin > Student: Add International Student Details");
        while (true) {

            Helper.printLine(59);
            System.out.printf("%-50s: ", "\nEnter new student ID Eg:21IWMR00001 (Q to quit)");
            String studentIDInput = Helper.readLine().toUpperCase().trim();
            if (studentIDInput.equals("Q")) {
                break;
            }
            if (InternationalStudent.checkInterStudentIDFormat(studentIDInput)) {
                if (!InternationalStudent.checkDuplicateInterStudentID(studentIDInput)) {
                    try {
                        while (true) {
                            System.out.printf("%-50s: ", "Enter Student name");
                            studentName = Helper.readLine().toUpperCase();
                            if (studentName.length() <= 50 && InternationalStudent.checkNameFormat(studentName)) {
                                break;
                            }
                        }

                        while (true) {
                            System.out.printf("%-50s: ", "Enter Student Passport Number     eg:A99999999");
                            studentIC = Helper.readLine().trim();
                            if (InternationalStudent.checkPassport(studentIC)) {
                                break;
                            }
                        }

                        studentGender = InternationalStudent.selectGender();

                        while (true) {
                            try {
                                System.out.printf("%-50s: ", "Enter student contact number eg: 011-9999999");
                                studentContactNumber = Helper.readLine().trim();
                                if (InternationalStudent.checkContactNumber(studentContactNumber)) {
                                    break;
                                }
                            } catch (Exception e) {
                                System.out.println("Invalid phone number length. Do try again.");
                            }
                        }

                        while (true) {
                            System.out.printf("%-50s: ", "Enter Student date of birth   eg:dd/mm/yyyy");
                            studentDOB = Helper.readLine().trim();
                            if (InternationalStudent.checkDOB(studentDOB)) {
                                break;
                            }
                        }

                        while (true) {
                            System.out.printf("%-50s: ", "Enter Student email address");
                            studentEmailAddress = Helper.readLine().trim();
                            if (studentEmailAddress.length() <= 50 && InternationalStudent.checkEmailAddress(studentEmailAddress)) {
                                break;
                            }
                        }

                        while (true) {
                            try {
                                System.out.println("Programme list option: ");
                                for (int i = 0; i < programme.length; i++) {
                                    System.out.print("\t\t\t\t\t\t" + (i + 1) + ". " + programme[i] + "\n");
                                }
                                System.out.printf("%-50s: ", "Enter student programme option");
                                int selection = Integer.parseInt(Helper.readLine()) - 1;

                                if (selection < 0 || selection > programme.length) {
                                    throw new Exception();
                                }
                                studentProgramme = programme[selection];
                                break;
                            } catch (Exception e) {
                                System.out.println("Please enter digit only.");
                            }
                        }

                        studyMode = LocalStudent.selectStudyMode();

                        while (true) {
                            System.out.printf("%-50s: ", "Enter Student Origin Country ");
                            country = Helper.readLine().toUpperCase();
                            if (country.length() <= 50 && InternationalStudent.checkCountry(country)) {
                                break;
                            }
                        }

                        Helper.printSmallSpace();

                        System.out.printf("%-50s ", "Student Password will be the same as student ID for the first time registration: " + studentIDInput);
                        studentPW = studentIDInput;
                        System.out.println();
                        Helper.systemSleep(1);

                        System.out.println("\n\nNew Student Details: ");
                        System.out.printf("%-40s: %s\n", "Student ID: ", studentIDInput);
                        System.out.printf("%-40s: %s\n", "Student Password: ", studentPW);
                        System.out.printf("%-40s: %s\n", "Student Name: ", studentName);
                        System.out.printf("%-40s: %s\n", "Student IC: ", studentIC);
                        System.out.printf("%-40s: %s\n", "Student Gender: ", studentGender);
                        System.out.printf("%-40s: %s\n", "Student Contact Number: ", studentContactNumber);
                        System.out.printf("%-40s: %s\n", "Student Email Address: ", studentEmailAddress);
                        System.out.printf("%-40s: %s\n", "Student DOB: ", studentDOB);
                        System.out.printf("%-40s: %s\n", "Student Programme: ", studentProgramme);
                        System.out.printf("%-40s: %s\n", "Study Mode: ", studyMode);
                        System.out.printf("%-40s: %s\n", "Student Account Status: ", studentStatus);
                        System.out.printf("%-40s: %s\n", "Student Origin Country: ", country);
                        Helper.printLine(59);
                        System.out.printf("%-40s: ", "Are you confirm to Save? y/n");
                        String confirm = Helper.readLine().toUpperCase();

                        //Encrypt student password and save it to database
                        try {
                            Helper aes = new Helper();
                            studentPW = aes.encrypt(studentPW);
                        }catch (Exception ex){
                            Logger.getLogger(Helper.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        if (confirm.equals("Y")) {
                            InternationalStudent internationalStudent = new InternationalStudent(studentIDInput, studentPW, studentName, studentIC, studentGender, studentContactNumber,
                                    studentEmailAddress, studentDOB, studentStatus, studentProgramme, studyMode, country);

                            InternationalStudent.addInterStudent(internationalStudent);
                            Helper.pause();
                            Helper.printSmallSpace();
                        } else {
                            System.out.println("Aborting");
                            Helper.pause();
                            Helper.printSmallSpace();
                        }
                    } catch (Exception e) {
                        System.out.println(e.getLocalizedMessage());
                        Helper.pause();
                        Helper.printSmallSpace();
                    }
                } else {
                    System.out.printf("StudentID %s already exist!\n", studentIDInput);
                }
            }
        }
    }
}
