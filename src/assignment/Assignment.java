package assignment;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Assignment {

    private static String username;

    private String password;

    private String domain;

    public static String studentLoggedInUserName = null;
    public static String studentUserID = null;

    public static Scanner getScanner() {
        return Helper.sc;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public static Connection getConnection() {
        Connection con = null;

        String user = "root";  //NEED change to your MySQL login ID.
        String pass = "admin";
        //String pass = "cong834";
        //String pass = "Abcd1234!"; //NEED change to your MySQL login PW.
        try {
            Class.forName("com.mysql.jdbc.Driver"); //SQL driver for Java 8
            //Class.forName("com.mysql.cj.jdbc.Driver"); //SQL driver for above Java 8
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/anavrin", user, pass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return con;
    }

    public static String printAdminMainView() {
        Helper.printHeader("Admin Main");
        System.out.println("What would you like to do?");
        System.out.println("[1] Staff");
        System.out.println("[2] Student");
        System.out.println("[3] Course");
        System.out.println("[4] Logout");
        System.out.printf("%-50s: ", "Choice");
        return Helper.readLine().trim();
    }

    public static String printStudentMainView() {
        Helper.printHeader("Student Main");
        System.out.println("\nWelcome " + studentLoggedInUserName);
        System.out.println("\nWhat would you like to do?");
        System.out.println("[1] Enroll Into A Course");
        System.out.println("[2] Drop From A Course");
        System.out.println("[3] Show Courses Enrolled");
        System.out.println("[4] Change Your Password");
        System.out.println("[5] Logout");
        System.out.printf("%-50s: ", "Choice");
        return Helper.readLine().trim();
    }

    public boolean login() {
        Helper.printHeader("Login");
        while (true) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            String decrypStudentPW = null;
            String actualStudentID = null;
            System.out.println("Current Time: " + formatter.format(now));
            System.out.println();

            System.out.println("Enter user domain");
            System.out.println("[1] Staff");
            System.out.println("[2] Student\n");
            System.out.println("Tips: Enter Q to quit the system");

            while (true) {
                System.out.printf("%-50s: ", "Choice");
                domain = Helper.readLine().trim();
                if (domain.equals("1") || domain.equals("2") || domain.equals("Q") || domain.equals("q")) {
                    break;
                } else {
                    System.out.println("Please enter 1 or 2 to choose the domain!");
                }
            }

            if (domain.equals("Q") || domain.equals("q")) {
                return false;
            }

            int tries = 3;
            while (tries >= 0) {
                System.out.printf("%-50s: ", "Enter Username");
                username = Helper.readLine().trim();
                System.out.printf("%-50s: ", "Enter password");
                password = Helper.readLine().trim();

                if (username.length() == 10) {
                    if (LocalStudent.checkLocalStudentIDFormat(username)) {
                        if (LocalStudent.checkDuplicateLocalStudentID(username)) {
                            LocalStudent localStudent = LocalStudent.getLocalStudent(username);
                            try {
                                Helper aes = new Helper();
                                decrypStudentPW = aes.decrypt(localStudent.getStudentPW());
                            } catch (Exception ex) {
                                Logger.getLogger(Helper.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            actualStudentID = localStudent.getStudentID();
                            studentLoggedInUserName = localStudent.getName();
                        } else {
                            System.out.printf("StudentID (%s) does not exist!\n", username);
                            Helper.pause();
                        }
                    }
                } else if (username.length() == 11) {
                    if (InternationalStudent.checkInterStudentIDFormat(username)) {
                        if (InternationalStudent.checkDuplicateInterStudentID(username)) {
                            InternationalStudent internationalStudent = InternationalStudent.getInterStudent(username);
                            try {
                                Helper aes = new Helper();
                                decrypStudentPW = aes.decrypt(internationalStudent.getStudentPW());
                            } catch (Exception ex) {
                                Logger.getLogger(Helper.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            actualStudentID = internationalStudent.getStudentID();
                            studentLoggedInUserName = internationalStudent.getName();
                        } else {
                            System.out.printf("StudentID (%s) does not exist!\n", username);
                            Helper.pause();
                        }
                    }
                }

                if (domain.equals("1") && username.equals("admin") && password.equals("admin")) {
                    return true;
                } else if (domain.equals("2") && username.equals(actualStudentID) && password.equals(decrypStudentPW)) {
                    studentUserID = actualStudentID;
                    return true;
                } else {
                    if (tries >= 1) {
                        System.out.printf("Wrong username or password! You can try for %d more time(s).\n", tries);
                    }
                    tries -= 1;
                }
            }
            return false;
        }
    }

    public static void main(String[] args) {
        String choice;
        do {
            getConnection();
            Assignment mainApp = new Assignment();
            if (!mainApp.login()) {
                System.out.println("Thank You for using the System.");
                Helper.systemSleep(1);
                Helper.sc.close();
                return;
            }

            if (mainApp.domain.equals("1")) {
                do {
                    choice = printAdminMainView();
                    switch (choice) {
                        case "1":
                            StaffService staffService = new StaffService();
                            staffService.print(mainApp);
                            break;
                        case "2":
                            StudentService studentService = new StudentService();
                            studentService.print(mainApp);
                            break;
                        case "3":
                            CourseService courseService = new CourseService();
                            courseService.print();
                            break;
                        case "4":
                            System.out.println("\n\nTaking you back to the log in screen");
                            Helper.systemSleep(1);
                            break;
                        default:
                            System.out.println("\nInvalid Input.");
                    }
                } while (!choice.equals("4"));
            } else {
                Enrol enrolService = new Enrol();
                do {
                    choice = printStudentMainView();
                    switch (choice) {
                        case "1":
                            enrolService.enrolCourse(mainApp, username);
                            break;
                        case "2":
                            enrolService.dropCourse(username);
                            break;
                        case "3":
                            enrolService.showCourseEnrolled(username);
                            break;
                        case "4":
                            StudentService studentService = new StudentService();
                            studentService.studentMenuChangeStudentAccountPassword(studentUserID);
                            break;
                        case "5":
                            System.out.println("\n\nTaking you back to the log in screen");
                            Helper.systemSleep(1);
                            break;
                        default:
                            System.out.println("\nInvalid Input.");
                    }
                } while (!choice.equals("5"));
            }
        } while (!choice.equals("0"));
    }
}
