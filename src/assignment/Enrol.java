package assignment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class Enrol {

    Connection con = Assignment.getConnection();
    ResultSet rs = null;
    PreparedStatement pst = null;

    public void enrolCourse(Assignment mainApp, String studentID) {
        String choice;
        Helper.printHeader("Student > Enrol");
        do {
            System.out.println("What would you like to Enrol in? :");
            System.out.println("[1] Curriculum");
            System.out.println("[2] Cocurriculum");
            System.out.println("[0] Exit");
            System.out.println("====================================================");
            System.out.printf("Choice                                            :");
            choice = Helper.readLine().trim();
            switch (choice) {
                case "1":
                    enrolCurriculum(mainApp, studentID);
                    break;
                case "2":
                    enrolCoCurriculum(mainApp, studentID);
                    break;
                default:
                    System.out.println("Please select a valid option");
            }
        } while (!choice.equals("0"));
        return;
    }

    private void enrolCurriculum(Assignment mainApp, String studentID) {
        ArrayList<String> courseList = new ArrayList<String>();
        String course = "Test";
        //get student currently enrolled course
        System.out.print(checkCourseNo(studentID));
        if (checkCourseNo(studentID) < 5 || getStudentCourses(studentID)[0] == "Unenrolled") {
            //display course
            Helper.printHeader("Student > Enrol > curriculum");
            try {
                String query = "SELECT * FROM curriculum";
                pst = con.prepareStatement(query);
                rs = pst.executeQuery();

                System.out.printf("Course Code ||%-30s||%-40s \n", "Course Name", "Description");
                while (rs.next()) {
                    System.out.printf("%11s ||%-30s||%-40s \n", rs.getString("courseCode"),
                            rs.getString("courseName"), rs.getString("description"));
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

            //Select Course
            do {
                System.out.printf("Select a course [Course Code] [Q to exit]: ");
                course = Helper.readLine().trim().toUpperCase();
                //check course availability
                if (course.trim().equals("Q")) {
                    return;
                } else if (checkCourseStatus(course) == 1 && checkCourseEnrolled(course, studentID) != true) {
                    System.out.printf("Course is available,");
                    Helper.pause();
                    //add course to array before updating SQL
                    paymentSelection(getFeesCourse(course), studentID, course);
                    for (int i = 0; i < getStudentCourses(studentID).length; i++) {
                        if (getStudentCourses(studentID)[i] != null) {
                            if (!getStudentCourses(studentID)[i].equals("Unenrolled")) {
                                courseList.add(getStudentCourses(studentID)[i]);
                            }
                        }
                    }
                    courseList.add(course);
                    updateCourse(courseList.toArray(), studentID);
                    return;
                } else if (checkCourseStatus(course) == 0) {
                    System.out.println("Course unavailable");
                } else if (checkCourseStatus(course) == 2) {
                    System.out.println("Please Select a valid Course");
                }

            } while (true);
        } else if (checkCourseNo(studentID) == 5) {
            System.out.println("you have exceeded the amount of Course you can enroll in");
            System.out.println("Please finish your current course or drop out of it.");
            Helper.pause();
            return;
        }
    }

    private void enrolCoCurriculum(Assignment mainApp, String studentID) {
        Helper.printHeader("Student > Enrol > Enrol Cocurriculum");
        //display course
        try {
            String query = "SELECT * FROM cocurriculum";
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();

            System.out.printf("%-10s ||%-30s||%-40s \n", "Curriculum", "Course Name", "Description");
            while (rs.next()) {
                System.out.printf("%11s ||%-30s||%-40s \n", rs.getString("courseCode"),
                        rs.getString("courseName"), rs.getString("description"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.printf("\n\n");

        if (checkCoCourseEnrolled(studentID).equals("Unenrolled")) {
            do {
                System.out.printf("Please Select a Cocuriculum activity: ");
                String cocu = Helper.readLine();
                if (checkCoCourseStatus(cocu) == 1) {
                    updateCoCourse(cocu, studentID);
                    getFeesCoCourse(cocu);
                    paymentSelection(getFeesCoCourse(cocu), studentID, cocu);
                    return;
                } else if (checkCoCourseStatus(cocu) == 1) {
                    System.out.println("Cocurriculum Unavailable;");
                } else if (checkCoCourseStatus(cocu) == 2) {
                    System.out.println("Please select a valid cocu Activity");
                }
            } while (true);
        } else {
            System.out.printf("You are already enrolled in a cocu activity ");
            Helper.pause();
        }

    }

    private int checkCourseStatus(String courseCode) {
        String status;
        try {
            String query = "SELECT * FROM curriculum WHERE courseCode = ?";
            pst = con.prepareStatement(query);
            pst.setString(1, courseCode.trim());
            rs = pst.executeQuery();

            if (rs.next()) {
                status = rs.getString("status");
                if (status.equals("AVAILABLE")) {
                    return 1;
                } else if (status.equals("UNAVAILABLE")) {
                    return 0;
                }
            } else {
                return 2;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return 2;
    }

    private int checkCoCourseStatus(String courseCode) {
        String status;
        try {
            String query = "SELECT * FROM cocurriculum WHERE courseCode = ?";
            pst = con.prepareStatement(query);
            pst.setString(1, courseCode.trim());
            rs = pst.executeQuery();

            if (rs.next()) {
                status = rs.getString("status");
                if (status.equals("AVAILABLE")) {
                    return 1;
                } else if (status.equals("UNAVAILABLE")) {
                    return 0;
                }
            } else {
                return 2;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return 2;
    }

    private void updateCourse(Object[] course, String studentID) {
        String courses = Arrays.toString(course).replace("[", "").replace("]", "").replace(" ", "");
        String studentTable, studentIDtype;
        try {
            if (studentID.trim().length() == 10) {
                studentTable = "localstudent";
                studentIDtype = "localStudentID";
            } else {
                studentTable = "interstudent";
                studentIDtype = "interStudentID";
            }
            String query = "UPDATE " + studentTable + " SET courses=? WHERE " + studentIDtype + " =?";
            pst = con.prepareStatement(query);
            pst.setString(1, courses);
            pst.setString(2, studentID);
            pst.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.printf("Done! ");
        Helper.pause();
    }

    private void updateCoCourse(String cocu, String studentID) {
        String studentTable, studentIDtype;
        try {
            if (studentID.trim().length() == 10) {
                studentTable = "localstudent";
                studentIDtype = "localStudentID";
            } else {
                studentTable = "interstudent";
                studentIDtype = "interStudentID";
            }
            String query = "UPDATE " + studentTable + " SET cocurriculum =? WHERE " + studentIDtype + " =?";
            pst = con.prepareStatement(query);
            pst.setString(1, cocu);
            pst.setString(2, studentID);
            pst.execute();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.printf("Done!");
        Helper.pause();
    }

    private int checkCourseNo(String studentID) {
        String studentTable, studentIDtype;
        try {
            if (studentID.trim().length() == 10) {
                studentTable = "localstudent";
                studentIDtype = "localStudentID";
            } else {
                studentTable = "interstudent";
                studentIDtype = "interStudentID";
            }

            String query = "SELECT * FROM " + studentTable + " WHERE " + studentIDtype + " = ?";
            pst = con.prepareStatement(query);
            pst.setString(1, studentID);
            rs = pst.executeQuery();

            if (rs.next()) {
                if (rs.getString("courses").equals("Unenrolled")) {
                    return 0;
                } else {
                    return rs.getString("courses").split(",", 0).length;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    private void paymentSelection(Double price, String studentID, String course) {
        String choice;
        while (true) {
            Helper.printHeader("Student > Enrol > Payment");
            System.out.println("Please select payment method");
            System.out.println("[1] cash");
            System.out.println("[2] card");
            System.out.printf("Choice                                            :");
            choice = Helper.readLine().trim();

            switch (choice) {
                case "1":
                    Payment newCashpayment = new CashPayment(price, course, studentID);
                    newCashpayment.makePayment();
                    Payment.addPaymentRecord(newCashpayment);
                    return;
                case "2":
                    Payment newCreditpayment = new CreditPayment(price, course, studentID);
                    newCreditpayment.makePayment();
                    Payment.addPaymentRecord(newCreditpayment);
                    return;
                default:
                    System.out.println("Invalid Input");
                    Helper.pause();
            }
        }

    }

    private double getFeesCourse(String course) {
        try {
            String query = "SELECT * FROM curriculum WHERE courseCode = ?";
            pst = con.prepareStatement(query);
            pst.setString(1, course.trim());
            rs = pst.executeQuery();

            if (rs.next()) {
                return rs.getDouble("fee");
            } else {
                return 0.00;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0.00;
    }

    private double getFeesCoCourse(String cocu) {
        try {
            String query = "SELECT * FROM cocurriculum WHERE courseCode = ?";
            pst = con.prepareStatement(query);
            pst.setString(1, cocu.trim());
            rs = pst.executeQuery();

            if (rs.next()) {
                return rs.getDouble("fee");
            } else {
                return 0.00;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0.00;
    }

    private String[] getStudentCourses(String studentID) {
        String studentTable, studentIDtype;
        String[] coursesArr = new String[5];
        try {
            if (studentID.trim().length() == 10) {
                studentTable = "localstudent";
                studentIDtype = "localStudentID";
            } else {
                studentTable = "interstudent";
                studentIDtype = "interStudentID";
            }

            String query = "SELECT * FROM " + studentTable + " WHERE " + studentIDtype + " = ? ";
            pst = con.prepareStatement(query);
            pst.setString(1, studentID);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (!rs.getString("courses").equals("Unenrolled")) {
                    int x = rs.getString("courses").split(",", 5).length;
                    for (int i = 0; i < 5; i++) {
                        if (i <= x - 1) {
                            String courses = rs.getString("courses");
                            coursesArr[i] = courses.split(",", 5)[i];
                        } else {
                            coursesArr[i] = null;
                        }
                    }
                    return coursesArr;

                } else {
                    coursesArr[0] = "Unenrolled";
                    return coursesArr;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return coursesArr;
    }

    private String getStudentCoCourse(String studentID) {
        String studentTable, studentIDtype;
        try {
            if (studentID.trim().length() == 10) {
                studentTable = "localstudent";
                studentIDtype = "localStudentID";
            } else {
                studentTable = "interstudent";
                studentIDtype = "interStudentID";
            }

            String query = "SELECT * FROM " + studentTable + " WHERE " + studentIDtype + " = ? ";
            pst = con.prepareStatement(query);
            pst.setString(1, studentID);
            rs = pst.executeQuery();

            if (rs.next()) {
                return rs.getString("cocurriculum");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private boolean checkCourseEnrolled(String courseCode, String studentID) {
        String[] currentCourse = getStudentCourses(studentID);
        int i = 0;
        while (currentCourse[i] != null) {
            if (currentCourse[i].equals(courseCode.toUpperCase())) {
                System.out.println("you are already enrolled in this course");
                return true;
            }
            i++;
        }
        return false;
    }

    private String checkCoCourseEnrolled(String studentID) {
        String studentTable, studentIDtype;
        try {
            if (studentID.trim().length() == 10) {
                studentTable = "localstudent";
                studentIDtype = "localStudentID";
            } else {
                studentTable = "interstudent";
                studentIDtype = "interStudentID";
            }

            String query = "SELECT * FROM " + studentTable + " WHERE " + studentIDtype + " = ?";
            pst = con.prepareStatement(query);
            pst.setString(1, studentID);
            rs = pst.executeQuery();

            if (rs.next()) {
                if (rs.getString("cocurriculum").equals("Unenrolled")) {
                    return "Unenrolled";
                } else {
                    return rs.getString("cocurriculum");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return "Unenrolled";
    }

    public void showCourseEnrolled(String studentID) {
        Helper.printHeader("Student > Show Course Enrolled");
        String coursesArr[] = getStudentCourses(studentID);
        String line = ("==========" + "==========" + "==========" + "==============="
                + "==========" + "==========" + "==========" + "==========" + "\n");
        System.out.printf(line);
        System.out.println("STUDENT ID : " + studentID);
        System.out.printf(line);
        System.out.println("Main Curriculum");
        System.out.printf(line);

        for (int i = 0; i < checkCourseNo(studentID); i++) {
            try {
                String query = "SELECT * FROM curriculum WHERE courseCode = ?";
                PreparedStatement pst = con.prepareStatement(query);
                pst.setString(1, coursesArr[i]);
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    System.out.printf("%-8s || %-30s || %-40s \n", rs.getString("courseCode"),
                            rs.getString("courseName"), rs.getString("description"));
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        System.out.printf(line);
        System.out.println("Cocurriculum");
        System.out.printf(line);

        try {
            String query = "SELECT * FROM cocurriculum WHERE courseCode =  ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, getStudentCoCourse(studentID).trim().toUpperCase());
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                System.out.printf("%-8s || %-30s || %-40s \n", rs.getString("courseCode"),
                        rs.getString("courseName"), rs.getString("description"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        Helper.pause();
    }

    public void dropCourse(String studentID) {
        Helper.printHeader("Student > DROP COURSE");
        do {
            System.out.println("Select courses to drop");
            System.out.println("[1] Curriculum");
            System.out.println("[2] Cocurriculum");
            System.out.println("[0] Back");
            System.out.printf("Choice                                    : ");
            String choice = Helper.readLine().trim();
            switch (choice) {
                case "1":
                    dropCurriculum(studentID);
                    break;
                case "2":
                    dropCocurriculum(studentID);
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Please enter a valid option");
            }
        } while (true);

    }

    private void dropCurriculum(String studentID) {
        System.out.println(checkCourseNo(studentID));
        if (checkCourseNo(studentID) == 0) {
            System.out.println("You are not enrolled in any course");
            System.out.println("Enroll now :D");
            Helper.pause();
            return;
        }
        ArrayList<String> courseList = new ArrayList<String>();
        Helper.printHeader("Student > Drop-A-Course> Drop curriculum");
        System.out.println("WELCOME TO DROP-A-COURSE (Curriculum)!");
        System.out.println("Enter 'i'  to  view your current enrolled courses.");
        System.out.println("Enter 'q'  to  exit.");

        while (true) {
            System.out.printf("Enter the course code of the course you wish to drop:");
            String dropCourse = Helper.readLine().toUpperCase().trim();
            if (dropCourse.equals("Q")) {
                return;
            } else if (dropCourse.equals("I")) {
                showCourseEnrolled(studentID);
            } else if (checkCourseEnrolled(dropCourse, studentID)) {
                for (int i = 0; i < getStudentCourses(studentID).length; i++) {
                    if (getStudentCourses(studentID)[i] != null) {
                        if (!getStudentCourses(studentID)[i].equals("Unenrolled")) {
                            courseList.add(getStudentCourses(studentID)[i]);
                        }
                    }
                }
                for (int i = 0; i < courseList.size(); i++) {
                    if (courseList.get(i).equals(dropCourse) && checkCourseNo(studentID) == 1) {
                        courseList.remove(dropCourse);
                        courseList.add("Unenrolled");
                        System.out.println("Course Removed");
                        updateCourse(courseList.toArray(), studentID);
                    } else if (courseList.get(i).equals(dropCourse)) {
                        courseList.remove(dropCourse);
                        System.out.println("Course Removed");
                        updateCourse(courseList.toArray(), studentID);
                        return;
                    }
                }
            } else {
                System.out.println("You are not enrolled in this course.");
            }
        }
    }

    private void dropCocurriculum(String studentID) {
        if (!checkCoCourseEnrolled(studentID).equals("Unenrolled")) {
            Helper.printHeader("Student > Drop-A-Course> Drop Cocurriculum");
            while (true) {
                System.out.println("WELCOME TO DROP-A-COURSE (Cocurriculum)!");
                System.out.println("[1] drop from your current cocu activity");
                System.out.println("[2] Exit");
                String choice = Helper.readLine();
                switch (choice) {
                    case "1":
                        updateCoCourse("Unenrolled", studentID);
                        return;
                    case "2":
                        return;
                }
            }
        } else {
            System.out.println("You are not currently enrolled in a cocu");
            Helper.pause();
            return;
        }

    }
}
