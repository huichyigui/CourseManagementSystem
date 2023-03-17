/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment;

import com.google.zxing.*;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.*;
import java.io.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author elfhc
 */
public class CourseService {

    Connection con = Assignment.getConnection();
    ResultSet rs = null;
    PreparedStatement pst = null;

    public void print() {
        String choice;
        do {
            try {
                Helper.printHeader("Admin Main > Course");
                System.out.println("What would you like to do?");
                System.out.println("[1] Search Course");
                System.out.println("[2] Add Course");
                System.out.println("[3] Update Course");
                System.out.println("[4] Display Course List");
                System.out.println("[5] View Course Summary Report");
                System.out.println("[6] Return to Main Menu");
                System.out.printf("%-50s: ", "Choice");
                choice = Helper.readLine().trim();
                switch (choice) {
                    case "1":
                        search();
                        break;
                    case "2":
                        addCourse();
                        break;
                    case "3":
                        updateCourse();
                        break;
                    case "4":
                        do {
                            Helper.printHeader("Admin Main > Display Course List");
                            System.out.println("What would you like to do?");
                            System.out.println("[1] Curriculum Course List");
                            System.out.println("[2] Cocurriculum Course List");
                            System.out.printf("%-50s: ", "Enter Choice (Q to quit)");

                            String option = Helper.readLine().toUpperCase().trim();
                            if (option.equals("Q")) {
                                return;
                            }
                            switch (option) {
                                case "1":
                                    currSummary();
                                    break;
                                case "2":
                                    cocuSummary();
                                    break;
                                default:
                                    System.out.println("Please enter valid option.");
                                    Helper.pause();
                                    break;
                            }
                        } while (true);
                    case "5":
                        Helper.printHeader("Admin Main > View Course Summary Report");
                        courseReport();
                        System.out.print("Do you want to save the report? y/n: ");
                        String confirm = Helper.readLine().toUpperCase().trim();
                        if (confirm.equals("Y")) {
                            writeCourseReport();
                            System.out.println("Course Report generated successfully!\n");
                        }
                        break;
                    case "6":
                        return;
//                        mainApp.printAdminMainView();
//                        break;
                    default:
                        System.out.println("Please enter valid option.");
                }
            } catch (NumberFormatException e) {
                System.out.println(e.getLocalizedMessage());
            }
        } while (true);
    }

    private void search() {
        String choice;
        Helper.printHeader("Admin Main > Search Course");
        while (true) {
            System.out.println("Enter Course Type");
            System.out.println("[1] Curriculum");
            System.out.println("[2] Cocurriculum");
            System.out.printf("%-50s: ", "Enter choice (Q to quit)");
            choice = Helper.readLine().toUpperCase().trim();
            switch (choice) {
                case "1":
                    Helper.printHeader("Admin Main > Search Curriculum");
                    break;
                case "2":
                    Helper.printHeader("Admin Main > Search Cocurriculum");
                    break;
                case "Q":
                    return;
                default:
                    System.out.println("Please enter valid option.\n");
            }
            String keyword;
            while (true) {
                System.out.printf("%-50s: ", "Enter keyword (Q to quit)");
                keyword = Helper.readLine().toUpperCase().trim();

                if (keyword.equals("Q")) {
                    return;
//                    print(mainApp);
                }
                if (choice.equals("1")) {
                    searchCourse("Curriculum", keyword);
                } else {
                    searchCourse("Cocurriculum", keyword);
                }
            }
        }
    }

    private void searchCourse(String type, String keyword) {
        int count = 0;
        Helper.printLine(150);
        try {
            if (type.equals("Curriculum")) {
                System.out.println(String.format("%12.12s ║ %-30s ║ %-70s ║ %-11s ║ %-15s ║ %-12s", "Course Code", "Course Name", "Course Description", "Credit Hours", "Tuition Fee(RM)", "Status"));
                Helper.printLine(150);
                String query = "SELECT * FROM Curriculum WHERE courseName LIKE '%" + keyword + "%'";
                pst = con.prepareStatement(query);
                rs = pst.executeQuery();

                while (rs.next()) {
                    System.out.printf("%12.12s ║ %-30s ║ %-70s ║ %-12s ║ %-15.2f ║ %-12s\n",
                            rs.getString("courseCode"), rs.getString("courseName"), rs.getString("description"), rs.getInt("creditHours"),
                            rs.getDouble("fee"), rs.getString("status"));
                    count++;
                    Helper.printLine(150);
                }
            } else {
                System.out.println(String.format("%12.12s ║ %-30s ║ %-50s ║ %-20s ║ %-11s ║ %-15s ║ %-12s", "Course Code", "Course Name", "Course Description", "Category", "Credit Hours", "Tuition Fee(RM)", "Status"));
                Helper.printLine(150);
                String query = "SELECT * FROM Cocurriculum WHERE courseName LIKE '%" + keyword + "%'";
                pst = con.prepareStatement(query);
                rs = pst.executeQuery();

                while (rs.next()) {
                    System.out.printf("%12.12s ║ %-30s ║ %-50s ║ %-20s ║ %-12s ║ %-15.2f ║ %-12s\n",
                            rs.getString("courseCode"), rs.getString("courseName"), rs.getString("description"), rs.getString("category"), rs.getInt("creditHours"),
                            rs.getDouble("fee"), rs.getString("status"));
                    count++;
                    Helper.printLine(150);
                }
            }
            System.out.println(count + " course with \"" + keyword + "\"");
            System.out.println();
        } catch (SQLException e) {
            e.getStackTrace();
        }
    }

    private void addCourse() {
        String choice;
        Helper.printHeader("Admin Main > Add Course");
        while (true) {
            System.out.println("Enter Course Type");
            System.out.println("[1] Curriculum");
            System.out.println("[2] Cocurriculum");
            System.out.println("[3] Import Curriculum From Excel");
            System.out.println("[4] Import Cocurriculum From Excel");
            System.out.printf("%-50s: ", "Enter choice (Q to quit)");
            choice = Helper.readLine().toUpperCase().trim();
            switch (choice) {
                case "1":
                    addCurriculum();
                    break;
                case "2":
                    addCocurriculum();
                    break;
                case "3":
                    Helper.printHeader("Admin Main > Add Course > Import Curriculum From Excel");
                    while (true) {
                        System.out.printf("%-50s: ", "Enter Excel File Name (0 to quit): ");
                        String fileName = Helper.readLine().trim();
                        if (fileName.equals("0")) {
                            return;
                        }
                        readCurriculumExcel(fileName);
                        break;
                    }
                    break;
                case "4":
                    Helper.printHeader("Admin Main > Add Course > Import Cocurriculum From Excel");
                    while (true) {
                        System.out.printf("%-50s: ", "Enter Excel File Name (0 to quit): ");
                        String fileName = Helper.readLine().trim();
                        if (fileName.equals("0")) {
                            return;
                        }
                        readCocurriculumExcel(fileName);
                        break;
                    }
                    break;
                case "Q":
                    return;
                default:
                    System.out.println("Please enter valid option.\n");
            }
        }
    }

    private void addCurriculum() {
        Helper.printHeader("Admin Main > Add Curriculum");
        while (true) {
            printCourseList("Curriculum");
            System.out.printf("%-50s: ", "Enter new course code (Q to quit)");
            String courseCode = Helper.readLine().toUpperCase().trim();
            if (courseCode.equals("Q")) {
                return;
//                print(mainApp);
//                break;
            }
            if (Curriculum.checkCourseCodeFormat(courseCode)) {
                if (!Curriculum.checkDuplicateCode(courseCode)) {
                    try {
                        String courseName;
                        while (true) {
                            System.out.printf("%-50s: ", "Enter course name");
                            courseName = Helper.readLine().toUpperCase().trim();
                            if (courseName.length() <= 50) {
                                break;
                            } else {
                                System.out.println("Course Name is too long!");
                            }
                        }
                        System.out.printf("%-50s: ", "Enter course description");
                        String description = Helper.readLine().toUpperCase().trim();

                        int creditHours;
                        while (true) {
                            try {
                                System.out.printf("%-50s: ", "Enter credit hours");
                                creditHours = Integer.parseInt(Helper.readLine().trim());
                                if (creditHours < 1 || creditHours > 4) {
                                    throw new Exception();
                                }
                                break;
                            } catch (Exception e) {
                                System.out.println("Please enter valid number between 1 and 4.");
                            }
                        }
                        double fee;
                        while (true) {
                            try {
                                System.out.printf("%-50s: RM ", "Enter course tuition fee");
                                fee = Double.parseDouble(Helper.readLine().trim());
                                if (fee <= 0) {
                                    throw new Exception();
                                }
                                break;
                            } catch (Exception e) {
                                System.out.println("Please enter valid number.");
                            }
                        }
                        Helper.printLine(74);
                        System.out.println("\n\nNew Curriculum Details: ");
                        System.out.printf("%-40s: %s\n", "Course Code", courseCode);
                        System.out.printf("%-40s: %s\n", "Course Name", courseName);
                        System.out.printf("%-40s: %s\n", "Description", description);
                        System.out.printf("%-40s: %d\n", "Credit Hours", creditHours);
                        System.out.printf("%-40s: RM %.2f\n\n", "Tuition Fee", fee);
                        System.out.printf("%-40s: ", "Confirm? y/n");
                        String confirm = Helper.readLine().toUpperCase().trim();
                        if (confirm.equals("Y")) {
                            Curriculum newCurr = new Curriculum(courseCode, courseName, description, creditHours, fee);
                            Curriculum.addCurriculum(newCurr);
                            System.out.println("New curriculum added successfully.");
                            generateQR(newCurr);
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
                    System.out.printf("Course Code %s already exist!\n\n", courseCode);
                }
            } else {
                System.out.println("Invalid course code format\n");
            }
        }
    }

    private void addCocurriculum() {
        Helper.printHeader("Admin Main > Add Cocurriculum");
        while (true) {
            printCourseList("Cocurriculum");
            System.out.printf("%-50s: ", "Enter Course Name (Q to quit)");
            String courseName = Helper.readLine().toUpperCase().trim();
            if (courseName.equals("Q")) {
                return;
//                print(mainApp);
//                break;
            }
            if (!Cocurriculum.checkDuplicateCocu(courseName)) {
                try {
                    System.out.printf("%-50s: ", "Enter course description");
                    String description = Helper.readLine().toUpperCase().trim();
                    String type = Cocurriculum.chooseCocuCategory();
                    double fee;
                    while (true) {
                        try {
                            System.out.printf("%-50s: RM ", "Enter course tuition fee");
                            fee = Double.parseDouble(Helper.readLine().trim());
                            if (fee < 0) {
                                throw new Exception();
                            }
                            break;
                        } catch (Exception e) {
                            System.out.println("Please enter valid number.");
                        }
                    }
                    Helper.printLine(74);
                    System.out.println("\n\nNew Cocuriculum Details: ");
                    System.out.printf("%-40s: %s\n", "Course Name", courseName);
                    System.out.printf("%-40s: %s\n", "Description", description);
                    System.out.printf("%-40s: %s\n", "Course Category", type);
                    System.out.printf("%-40s: RM %.2f\n\n", "Cocurriculum Fee", fee);
                    System.out.printf("%-40s: ", "Confirm? y/n");
                    String confirm = Helper.readLine().toUpperCase().trim();
                    if (confirm.equals("Y")) {
                        Cocurriculum newCocu = new Cocurriculum(courseName, description, fee, type);
                        Cocurriculum.addCocurriculum(newCocu);
                        System.out.println("New cocurriculum added successfully.");
                        generateQR(newCocu);
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
                System.out.printf("Course Name: %s already exist!\n", courseName);
            }
        }
    }

    private void updateCourse() {
        String choice;
        Helper.printHeader("Admin Main > Update Course");
        while (true) {
            System.out.println("Enter Course Type");
            System.out.println("[1] Curriculum");
            System.out.println("[2] Cocurriculum");
            System.out.printf("%-50s: ", "Enter choice (Q to quit)");
            choice = Helper.readLine().toUpperCase().trim();
            switch (choice) {
                case "1":
                    updateCurriculum();
                    break;
                case "2":
                    updateCocurriculum();
                    break;
                case "Q":
                    return;
                default:
                    System.out.println("Please enter valid option.");
            }
        }
    }

    private void updateCurriculum() {
        String choice;
        do {
            Helper.printHeader("Admin Main > Update Cocurriculum");
            System.out.println("What would you like to do?");
            System.out.println("[1] Change Course Name");
            System.out.println("[2] Change Course Description");
            System.out.println("[3] Change Credit Hours");
            System.out.println("[4] Change Tuition Fee");
            System.out.println("[5] Deactivate/Activate Course");
            System.out.println("[6] Return to Previous Menu");

            System.out.printf("%-50s: ", "Enter Choice");
            choice = Helper.readLine().trim();
            switch (choice) {
                case "1":
                    changeCourseName("Curriculum");
                    break;
                case "2":
                    changeDescription("Curriculum");
                    break;
                case "3":
                    changeCreditHours("Curriculum");
                    break;
                case "4":
                    changeFee("Curriculum");
                    break;
                case "5":
                    activeDeactive("Curriculum");
                    break;
                case "6":
                    return;
//                    print(mainApp);
//                    break;
                default:
                    System.out.println("Please enter valid option.");
            }
        } while (true);
    }

    private void updateCocurriculum() {
        String choice;
        do {
            Helper.printHeader("Admin Main > Update Cocurriculum");
            System.out.println("What would you like to do?");
            System.out.println("[1] Change Course Name");
            System.out.println("[2] Change Course Description");
            System.out.println("[3] Change Credit Hours");
            System.out.println("[4] Change Tuition Fee");
            System.out.println("[5] Change Course Category");
            System.out.println("[6] Deactivate/Active Course");
            System.out.println("[7] Return to Previous Menu");

            System.out.printf("%-50s: ", "Enter Choice");
            choice = Helper.readLine().trim();
            switch (choice) {
                case "1":
                    changeCourseName("Cocurriculum");
                    break;
                case "2":
                    changeDescription("Cocurriculum");
                    break;
                case "3":
                    changeCreditHours("Cocurriculum");
                    break;
                case "4":
                    changeFee("Cocurriculum");
                    break;
                case "5":
                    changeCategory();
                    break;
                case "6":
                    activeDeactive("Cocurriculum");
                    break;
                case "7":
                    return;
//                    print(mainApp);
//                    break;
                default:
                    System.out.println("Please enter valid option.");
            }
        } while (true);
    }

    private void changeCourseName(String type) {
        Helper.printHeader("Admin Main > Update Course > Change Course Name");
        String newName, confirm;
        while (true) {
            if (type.equals("Curriculum")) {
                printCourseList(type);
                System.out.print("Enter the Course Code (Q to Quit): ");
                String courseCode = Helper.readLine().toUpperCase().trim();
                if (courseCode.equals("Q")) {
                    break;
                }
                if (Curriculum.checkDuplicateCode(courseCode)) {
                    Curriculum curr = Curriculum.getCurriculum(courseCode);
                    while (true) {
                        System.out.print("Enter new Course Name: ");
                        newName = Helper.readLine().toUpperCase().trim();
                        if (newName.length() <= 50) {
                            break;
                        } else {
                            System.out.println("Course Name is too long!");
                        }
                    }
                    System.out.print("This Course Name will be updated to Course " + curr.getCourseCode() + ": " + newName + "\n\nConfirm? y/n: ");
                    confirm = Helper.readLine().toUpperCase().trim();
                    if (confirm.equals("Y")) {
                        curr.setCourseName(newName);
                        Curriculum.updateCurriculum(curr);
                        System.out.println("Course Name Changed Successfully!\n");
                        generateQR(curr);
                    } else {
                        System.out.println("Aborting");
                        Helper.pause();
                    }
                } else {
                    System.out.printf("Course Code (%s) does not exist!\n", courseCode);
                    Helper.pause();
                }
            } else {
                printCourseList(type);
                System.out.print("Enter the Course Code (Q to Quit): ");
                String courseCode = Helper.readLine().toUpperCase().trim();
                if (courseCode.equals("Q")) {
                    break;
                }
                if (Cocurriculum.checkDuplicateCocuCode(courseCode)) {
                    Cocurriculum cocu = Cocurriculum.getCocurriculum(courseCode);
                    while (true) {
                        System.out.print("Enter new Course Name: ");
                        newName = Helper.readLine().toUpperCase().trim();
                        if (newName.length() <= 50) {
                            break;
                        } else {
                            System.out.println("Course Name is too long!");
                        }
                    }
                    System.out.print("This Course Name will be updated to Course " + cocu.getCourseCode() + ": " + newName + "\n\nConfirm? y/n: ");
                    confirm = Helper.readLine().toUpperCase().trim();
                    if (confirm.equals("Y")) {
                        cocu.setCourseName(newName);
                        Cocurriculum.updateCocurriculum(cocu);
                        System.out.println("Course Name Changed Successfully!\n");
                        generateQR(cocu);
                    } else {
                        System.out.println("Aborting");
                        Helper.pause();
                    }
                } else {
                    System.out.printf("Course Code (%s) does not exist!\n", courseCode);
                    Helper.pause();
                }
            }
        }
    }

    private void changeDescription(String type) {
        Helper.printHeader("Admin Main > Update Course > Change Course Description");
        String newDescription, confirm;
        while (true) {
            if (type.equals("Curriculum")) {
                printCourseList(type);
                System.out.print("Enter the Course Code (Q to Quit): ");
                String courseCode = Helper.readLine().toUpperCase().trim();
                if (courseCode.equals("Q")) {
                    break;
                }
                if (Curriculum.checkDuplicateCode(courseCode)) {
                    Curriculum curr = Curriculum.getCurriculum(courseCode);
                    System.out.print("Enter new Course Description: ");
                    newDescription = Helper.readLine().toUpperCase().trim();
                    System.out.print("This Course Description will be updated to Course " + curr.getCourseCode() + ": " + newDescription + "\n\nConfirm? y/n: ");
                    confirm = Helper.readLine().toUpperCase().trim();
                    if (confirm.equals("Y")) {
                        curr.setDescription(newDescription);
                        Curriculum.updateCurriculum(curr);
                        System.out.println("Course Description Changed Successfully!");
                        generateQR(curr);
                    } else {
                        System.out.println("Aborting");
                        Helper.pause();
                    }
                } else {
                    System.out.printf("Course Code (%s) does not exist!\n", courseCode);
                    Helper.pause();
                }
            } else {
                printCourseList(type);
                System.out.print("Enter the Course Code (Q to Quit): ");
                String courseCode = Helper.readLine().toUpperCase().trim();
                if (courseCode.equals("Q")) {
                    break;
                }
                if (Cocurriculum.checkDuplicateCocuCode(courseCode)) {
                    Cocurriculum cocu = Cocurriculum.getCocurriculum(courseCode);
                    System.out.print("Enter new Course Description: ");
                    newDescription = Helper.readLine().toUpperCase().trim();
                    System.out.print("This Course Description will be updated to Course " + cocu.getCourseCode() + ": " + newDescription + "\n\nConfirm? y/n: ");
                    confirm = Helper.readLine().toUpperCase().trim();
                    if (confirm.equals("Y")) {
                        cocu.setDescription(newDescription);
                        Cocurriculum.updateCocurriculum(cocu);
                        System.out.println("Course Description Changed Successfully!\n");
                        generateQR(cocu);
                    } else {
                        System.out.println("Aborting");
                        Helper.pause();
                    }
                } else {
                    System.out.printf("Course Code (%s) does not exist!\n", courseCode);
                    Helper.pause();
                }
            }
        }
    }

    private void changeCreditHours(String type) {
        Helper.printHeader("Admin Main > Update Course > Change Course Credit Hours");
        int newCreditHours;
        String confirm;
        while (true) {
            if (type.equals("Curriculum")) {
                printCourseList(type);
                System.out.print("Enter the Course Code (Q to Quit): ");
                String courseCode = Helper.readLine().toUpperCase().trim();
                if (courseCode.equals("Q")) {
                    break;
                }
                if (Curriculum.checkDuplicateCode(courseCode)) {
                    Curriculum curr = Curriculum.getCurriculum(courseCode);
                    while (true) {
                        try {
                            System.out.print("Enter new Course Credit Hours: ");
                            newCreditHours = Integer.parseInt(Helper.readLine().trim());
                            if (newCreditHours < 1 || newCreditHours > 4) {
                                throw new Exception();
                            }
                            break;
                        } catch (Exception e) {
                            System.out.println("Please enter valid number between 1 and 4.");
                        }
                    }
                    System.out.print("This Course Credit Hours will be updated to Course " + curr.getCourseCode() + ": " + newCreditHours + "\n\nConfirm? y/n: ");
                    confirm = Helper.readLine().toUpperCase().trim();
                    if (confirm.equals("Y")) {
                        curr.setCreditHours(newCreditHours);
                        Curriculum.updateCurriculum(curr);
                        System.out.println("Course Credit Hours Changed Successfully!\n");
                        generateQR(curr);
                    } else {
                        System.out.println("Aborting");
                        Helper.pause();
                    }
                } else {
                    System.out.printf("Course Code (%s) does not exist!\n", courseCode);
                    Helper.pause();
                }
            } else {
                printCourseList(type);
                System.out.print("Enter the Course Code (Q to Quit): ");
                String courseCode = Helper.readLine().toUpperCase().trim();
                if (courseCode.equals("Q")) {
                    break;
                }
                if (Cocurriculum.checkDuplicateCocuCode(courseCode)) {
                    Cocurriculum cocu = Cocurriculum.getCocurriculum(courseCode);
                    while (true) {
                        try {
                            System.out.print("Enter new Course Credit Hours: ");
                            newCreditHours = Integer.parseInt(Helper.readLine().trim());
                            if (newCreditHours < 1 || newCreditHours > 4) {
                                throw new Exception();
                            }
                            break;
                        } catch (Exception e) {
                            System.out.println("Please enter valid number between 1 and 4.");
                        }
                    }
                    System.out.print("This Course Credit Hours will be updated to Course " + cocu.getCourseCode() + ": " + newCreditHours + "\n\nConfirm? y/n: ");
                    confirm = Helper.readLine().toUpperCase().trim();
                    if (confirm.equals("Y")) {
                        cocu.setCreditHours(newCreditHours);
                        Cocurriculum.updateCocurriculum(cocu);
                        System.out.println("Course Credit Hours Changed Successfully!\n");
                        generateQR(cocu);
                    } else {
                        System.out.println("Aborting");
                        Helper.pause();
                    }
                } else {
                    System.out.printf("Course Code (%s) does not exist!\n", courseCode);
                    Helper.pause();
                }
            }
        }
    }

    private void changeFee(String type) {
        Helper.printHeader("Admin Main > Update Course > Change Course Fee");
        double newFee;
        String confirm;
        while (true) {
            if (type.equals("Curriculum")) {
                printCourseList(type);
                System.out.print("Enter the Course Code (Q to Quit): ");
                String courseCode = Helper.readLine().toUpperCase().trim();
                if (courseCode.equals("Q")) {
                    break;
                }
                if (Curriculum.checkDuplicateCode(courseCode)) {
                    Curriculum curr = Curriculum.getCurriculum(courseCode);
                    while (true) {
                        try {
                            System.out.print("Enter new Course Tuition Fee : RM ");
                            newFee = Double.parseDouble(Helper.readLine().trim());
                            if (newFee <= 0) {
                                throw new Exception();
                            }
                            break;
                        } catch (Exception e) {
                            System.out.println("Please enter valid number.");
                        }
                    }
                    System.out.printf("This Course Tuition Fee will be updated to Course %s: RM %.2f\n\nConfirm? y/n: ", curr.getCourseCode(), newFee);
                    confirm = Helper.readLine().toUpperCase().trim();
                    if (confirm.equals("Y")) {
                        curr.setFee(newFee);
                        Curriculum.updateCurriculum(curr);
                        System.out.println("Course Tuition Fee Changed Successfully!");
                        generateQR(curr);
                    } else {
                        System.out.println("Aborting");
                        Helper.pause();
                    }
                } else {
                    System.out.printf("Course Code (%s) does not exist!\n", courseCode);
                    Helper.pause();
                }
            } else {
                printCourseList(type);
                System.out.print("Enter the Course Code (Q to Quit): ");
                String courseCode = Helper.readLine().toUpperCase().trim();
                if (courseCode.equals("Q")) {
                    break;
                }
                if (Cocurriculum.checkDuplicateCocuCode(courseCode)) {
                    Cocurriculum cocu = Cocurriculum.getCocurriculum(courseCode);
                    while (true) {
                        try {
                            System.out.print("Enter new Course Fee : RM ");
                            newFee = Double.parseDouble(Helper.readLine().trim());
                            if (newFee < 0) {
                                throw new Exception();
                            }
                            break;
                        } catch (Exception e) {
                            System.out.println("Please enter valid number.");
                        }
                    }
                    System.out.printf("This Course Fee will be updated to Course %s: RM %.2f\n\nConfirm? y/n: ", cocu.getCourseCode(), newFee);
                    confirm = Helper.readLine().toUpperCase().trim();
                    if (confirm.equals("Y")) {
                        cocu.setFee(newFee);
                        Cocurriculum.updateCocurriculum(cocu);
                        System.out.println("Course Tuition Fee Changed Successfully!\n");
                        generateQR(cocu);
                    } else {
                        System.out.println("Aborting");
                        Helper.pause();
                    }
                } else {
                    System.out.printf("Course Code (%s) does not exist!\n", courseCode);
                    Helper.pause();
                }
            }
        }
    }

    private void activeDeactive(String type) {
        Helper.printHeader("Admin Main > Update Course > Deactivate/Active Course");
        while (true) {
            if (type.equals("Curriculum")) {
                printCourseList(type);
                System.out.print("Enter the Course Code (Q to Quit): ");
                String courseCode = Helper.readLine().toUpperCase().trim();
                if (courseCode.equals("Q")) {
                    break;
                }
                if (Curriculum.checkDuplicateCode(courseCode)) {
                    Curriculum curr = Curriculum.getCurriculum(courseCode);
                    String newStatus = Course.chooseStatus();
                    System.out.print("This Course Status will be updated to Course " + curr.getCourseCode() + ": " + newStatus + "\n\nConfirm? y/n: ");
                    String confirm = Helper.readLine().toUpperCase().trim();
                    if (confirm.equals("Y")) {
                        curr.setStatus(newStatus);
                        Curriculum.updateCurriculum(curr);
                        System.out.println("Course Status Changed Successfully!");
                        generateQR(curr);
                    } else {
                        System.out.println("Aborting");
                        Helper.pause();
                    }
                } else {
                    System.out.printf("Course Code (%s) does not exist!\n", courseCode);
                    Helper.pause();
                }
            } else {
                printCourseList(type);
                System.out.print("Enter the Course Code (Q to Quit): ");
                String courseCode = Helper.readLine().toUpperCase().trim();
                if (courseCode.equals("Q")) {
                    break;
                }
                if (Cocurriculum.checkDuplicateCocuCode(courseCode)) {
                    Cocurriculum cocu = Cocurriculum.getCocurriculum(courseCode);
                    String newStatus = Course.chooseStatus();
                    System.out.print("This Course Status will be updated to Course " + cocu.getCourseCode() + ": " + newStatus + "\n\nConfirm? y/n: ");
                    String confirm = Helper.readLine().toUpperCase().trim();
                    if (confirm.equals("Y")) {
                        cocu.setStatus(newStatus);
                        Cocurriculum.updateCocurriculum(cocu);
                        System.out.println("Course Status Changed Successfully!\n");
                        generateQR(cocu);
                    } else {
                        System.out.println("Aborting");
                        Helper.pause();
                    }
                } else {
                    System.out.printf("Course Code (%s) does not exist!\n", courseCode);
                    Helper.pause();
                }
            }
        }
    }

    private void changeCategory() {
        Helper.printHeader("Admin Main > Update Course > Change Cocuriculum Category");
        while (true) {
            printCourseList("Cocurriculum");
            System.out.print("Enter the Course Code (Q to Quit): ");
            String courseCode = Helper.readLine().toUpperCase().trim();
            if (courseCode.equals("Q")) {
                break;
            }
            if (Cocurriculum.checkDuplicateCocuCode(courseCode)) {
                Cocurriculum cocu = Cocurriculum.getCocurriculum(courseCode);
                String newCategory = Cocurriculum.chooseCocuCategory();
                System.out.print("This Cocu Category will be updated to Course " + cocu.getCourseCode() + ": " + newCategory + "\n\nConfirm? y/n: ");
                String confirm = Helper.readLine().toUpperCase().trim();
                if (confirm.equals("Y")) {
                    cocu.setCategory(newCategory);
                    Cocurriculum.updateCocurriculum(cocu);
                    System.out.println("Course Category Changed Successfully!");
                    generateQR(cocu);
                } else {
                    System.out.println("Aborting");
                    Helper.pause();
                }
            } else {
                System.out.printf("Course Code (%s) does not exist!\n", courseCode);
                Helper.pause();
            }
        }
    }

    private void currSummary() {
        String choice;
        Helper.printHeader("Admin Main > Curriculum Course List");
        do {
            System.out.println("View Curriculum Course List By Status");
            System.out.println("Status");
            System.out.println("[1] Shows All");
            System.out.println("[2] Shows Available Course Only");
            System.out.println("[3] Shows Unavailable Course Only");
            System.out.println("[4] Return to Previous Menu");

            System.out.printf("%-50s: ", "Enter Choice");
            choice = Helper.readLine().trim();
            String confirm;
            switch (choice) {
                case "1":
                    printCourseList("Curriculum");
                    System.out.print("Do you want to export data into Excel? y/n: ");
                    confirm = Helper.readLine().toUpperCase().trim();
                    if (confirm.equals("Y")) {
                        export("Curriculum");
                        System.out.println("Curriculum list exported successfully!\n");
                    }
                    break;
                case "2":
                    printCourseList("Curriculum", "AVAILABLE");
                    System.out.print("Do you want to export data into Excel? y/n: ");
                    confirm = Helper.readLine().toUpperCase().trim();
                    if (confirm.equals("Y")) {
                        export("Curriculum", "AVAILABLE");
                        System.out.println("Curriculum list exported successfully!\n");
                    }
                    break;
                case "3":
                    printCourseList("Curriculum", "UNAVAILABLE");
                    System.out.print("Do you want to export data into Excel? y/n: ");
                    confirm = Helper.readLine().toUpperCase().trim();
                    if (confirm.equals("Y")) {
                        export("Curriculum", "UNAVAILABLE");
                        System.out.println("Curriculum list exported successfully!\n");
                    }
                    break;
                case "4":
                    return;
//                    print(mainApp);
//                    break;
                default:
                    System.out.println("Please enter valid option.\n");
            }
        } while (true);
    }

    private void cocuSummary() {
        String choice;
        Helper.printHeader("Admin Main > Cocurriculum Course List");
        do {
            System.out.println("View Cocurriculum Course List By Filtering");
            System.out.println("Filtering");
            System.out.println("[1] Shows All");
            System.out.println("[2] Shows Available Course Only");
            System.out.println("[3] Shows Unavailable Course Only");
            System.out.println("[4] Shows Course Filtering By Category");
            System.out.println("[5] Return to Previous Menu");

            System.out.printf("%-50s: ", "Enter Choice");
            choice = Helper.readLine().trim();
            switch (choice) {
                case "1":
                    printCourseList("Cocurriculum");
                    System.out.print("Do you want to export data into Excel? y/n: ");
                    String confirm = Helper.readLine().toUpperCase().trim();
                    if (confirm.equals("Y")) {
                        export("Cocurriculum");
                        System.out.println("Cocurriculum list exported successfully!\n");
                    }
                    break;
                case "2":
                    printCourseList("Cocurriculum", "AVAILABLE");
                    System.out.print("Do you want to export data into Excel? y/n: ");
                    confirm = Helper.readLine().toUpperCase().trim();
                    if (confirm.equals("Y")) {
                        export("Cocurriculum", "AVAILABLE");
                        System.out.println("Cocurriculum list exported successfully!\n");
                    }
                    break;
                case "3":
                    printCourseList("Cocurriculum", "UNAVAILABLE");
                    System.out.print("Do you want to export data into Excel? y/n: ");
                    confirm = Helper.readLine().toUpperCase().trim();
                    if (confirm.equals("Y")) {
                        export("Cocurriculum", "UNAVAILABLE");
                        System.out.println("Curriculum list exported successfully!\n");
                    }
                    break;
                case "4":
                    Helper.printHeader("Admin Main > Cocurriculum Course Summary > Filtering By Category");
                    String category = Cocurriculum.chooseCocuCategory();
                    courseCategorySummary(category);
                    System.out.print("Do you want to export data into Excel? y/n: ");
                    confirm = Helper.readLine().toUpperCase().trim();
                    if (confirm.equals("Y")) {
                        exportCategory(category);
                        System.out.println("Cocurriculum list exported successfully!\n");
                    }
                    break;
                case "5":
                    return;
//                    print(mainApp);
//                    break;
                default:
                    System.out.println("Please enter valid option.\n");
            }
        } while (true);
    }

    private void printCourseList(String type) {
        Helper.printLine(150);
        int count = 0;
        try {
            if (type.equals("Curriculum")) {
                System.out.println(String.format("%12.12s ║ %-30s ║ %-70s ║ %-11s ║ %-15s ║ %-12s", "Course Code", "Course Name", "Course Description", "Credit Hours", "Tuition Fee(RM)", "Status"));
                Helper.printLine(150);

                String query = "SELECT courseCode FROM curriculum";
                pst = con.prepareStatement(query);
                rs = pst.executeQuery();

                while (rs.next()) {
                    Curriculum curr = Curriculum.getCurriculum(rs.getString("courseCode"));
                    System.out.println(curr.toString());
                    count++;
                    Helper.printLine(150);
                }
            } else {
                System.out.println(String.format("%12.12s ║ %-30s ║ %-50s ║ %-20s ║ %-11s ║ %-15s ║ %-12s", "Course Code", "Course Name", "Course Description", "Category", "Credit Hours", "Tuition Fee(RM)", "Status"));
                Helper.printLine(150);

                String query = "SELECT courseCode FROM cocurriculum";
                pst = con.prepareStatement(query);
                rs = pst.executeQuery();

                while (rs.next()) {
                    Cocurriculum cocu = Cocurriculum.getCocurriculum(rs.getString("courseCode"));
                    System.out.println(cocu.toString());
                    count++;
                    Helper.printLine(150);
                }
            }
            System.out.println("Total " + type + " Course: " + count);
            System.out.println();
        } catch (SQLException e) {
            e.getStackTrace();
        }
    }

    private void printCourseList(String type, String status) {
        Helper.printLine(150);
        int count = 0;
        try {
            if (type.equals("Curriculum")) {
                System.out.println(String.format("%12.12s ║ %-30s ║ %-70s ║ %-11s ║ %-15s ║ %-12s", "Course Code", "Course Name", "Course Description", "Credit Hours", "Tuition Fee(RM)", "Status"));
                Helper.printLine(150);

                String query = "SELECT courseCode FROM curriculum WHERE status=?";
                pst = con.prepareStatement(query);
                pst.setString(1, status);
                rs = pst.executeQuery();

                while (rs.next()) {
                    Curriculum curr = Curriculum.getCurriculum(rs.getString("courseCode"));
                    System.out.println(curr.toString());
                    count++;
                    Helper.printLine(150);
                }
            } else {
                System.out.println(String.format("%12.12s ║ %-30s ║ %-50s ║ %-20s ║ %-11s ║ %-15s ║ %-12s", "Course Code", "Course Name", "Course Description", "Category", "Credit Hours", "Tuition Fee(RM)", "Status"));
                Helper.printLine(150);

                String query = "SELECT courseCode FROM cocurriculum WHERE status=?";
                pst = con.prepareStatement(query);
                pst.setString(1, status);
                rs = pst.executeQuery();

                while (rs.next()) {
                    Cocurriculum cocu = Cocurriculum.getCocurriculum(rs.getString("courseCode"));
                    System.out.println(cocu.toString());
                    count++;
                    Helper.printLine(150);
                }
            }
            System.out.println("Total " + status + " Course: " + count);
            System.out.println();
        } catch (SQLException e) {
            e.getStackTrace();
        }
    }

    private void courseCategorySummary(String category) {
        Helper.printLine(150);
        int count = 0;

        try {
            System.out.println(String.format("%12.12s ║ %-30s ║ %-50s ║ %-20s ║ %-11s ║ %-15s ║ %-12s", "Course Code", "Course Name", "Course Description", "Category", "Credit Hours", "Tuition Fee(RM)", "Status"));
            Helper.printLine(150);

            String query = "SELECT courseCode FROM cocurriculum WHERE category=?";
            pst = con.prepareStatement(query);
            pst.setString(1, category);
            rs = pst.executeQuery();

            while (rs.next()) {
                Cocurriculum cocu = Cocurriculum.getCocurriculum(rs.getString("courseCode"));
                System.out.println(cocu.toString());
                count++;
                Helper.printLine(150);
            }
            System.out.println("Total Course From " + category + ": " + count);
            System.out.println();
        } catch (SQLException e) {
            e.getStackTrace();
        }
    }

    private void courseReport() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        int totalCourse = Curriculum.getActive() + Curriculum.getInactive() + Cocurriculum.getActive() + Cocurriculum.getInactive();
        Helper.printLine(50);
        System.out.printf("%55s\n", "ANAVRIN COLLEGE ENROLLMENT SYSTEM");
        System.out.printf("%50s", "COURSE SUMMARY REPORT");
        System.out.println("\n\n Generated On: " + now.format(formatter));
        Helper.printLine(50);
        System.out.println(" Curriculum Course");
        System.out.println(" By Course Availability:-");
        System.out.printf(" \u25cf %-40s: %d (%.2f%%)\n", "Available Course", Curriculum.getActive(), Curriculum.calPercentage("AVAILABLE"));
        System.out.printf(" \u25cf %-40s: %d (%.2f%%)\n", "Unavailable Course", Curriculum.getInactive(), Curriculum.calPercentage("UNAVAILABLE"));
        Helper.printLine(50);
        System.out.printf(" %-43s: %d\n", "Total", Curriculum.getActive() + Curriculum.getInactive());
        Helper.printLine(50);
        System.out.println(" Cocurriculum Course");
        System.out.println(" By Course Availability:-");
        System.out.printf(" \u25cf %-40s: %d (%.2f%%)\n", "Available Course", Cocurriculum.getActive(), Cocurriculum.calPercentage("AVAILABLE"));
        System.out.printf(" \u25cf %-40s: %d (%.2f%%)\n\n", "Unavailable Course", Cocurriculum.getInactive(), Cocurriculum.calPercentage("UNAVAILABLE"));

        System.out.println(" By Course Category:-");
        System.out.printf(" \u25cf %-40s: %d (%.2f%%)\n", "Games/Sports", Cocurriculum.getGamesSports(), Cocurriculum.calPercentage("GAMES/SPORTS"));
        System.out.printf(" \u25cf %-40s: %d (%.2f%%)\n", "Cultural Studies", Cocurriculum.getCulturalStudies(), Cocurriculum.calPercentage("CULTURAL STUDIES"));
        System.out.printf(" \u25cf %-40s: %d (%.2f%%)\n", "Community Service", Cocurriculum.getCommunityService(), Cocurriculum.calPercentage("COMMUNITY SERVICE"));
        System.out.printf(" \u25cf %-40s: %d (%.2f%%)\n", "Volunteerism", Cocurriculum.getVolunteerism(), Cocurriculum.calPercentage("VOLUNTEERISM"));
        Helper.printLine(50);
        System.out.printf(" %-43s: %d\n", "Total", Cocurriculum.getActive() + Cocurriculum.getInactive());
        Helper.printLine(50);
        System.out.printf("%-44s: %d\n", " Grand Total Course", totalCourse);
        Helper.printLine(50);
    }

    private static boolean writeCourseReport() {
        try {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter0 = DateTimeFormatter.ofPattern("yyyyMMdd");
            String dateTimeInfo = now.format(formatter0);
            String fileName = String.format("Course_Report_%s", dateTimeInfo);
            FileOutputStream fileOutput = new FileOutputStream(fileName + ".txt");
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fileOutput));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String line = String.format("%" + 50 + "s", "").replace(" ", "═");
            int totalCourse = Curriculum.getActive() + Curriculum.getInactive() + Cocurriculum.getActive() + Cocurriculum.getInactive();
            bw.write(line);
            bw.write(String.format("\n%55s\n", "ANAVRIN COLLEGE ENROLLMENT SYSTEM"));
            bw.write(String.format("%50s\n", "COURSE SUMMARY REPORT"));
            bw.write(String.format("\n Generated On: %s\n", now.format(formatter)));
            bw.write(line);
            bw.write("\n Curriculum Course\n");
            bw.write(" By Course Availability:-\n");
            bw.write(String.format(" \u25cf %-40s: %d (%.2f%%)\n", "Available Course", Curriculum.getActive(), Curriculum.calPercentage("AVAILABLE")));
            bw.write(String.format(" \u25cf %-40s: %d (%.2f%%)\n", "Unavailable Course", Curriculum.getInactive(), Curriculum.calPercentage("UNAVAILABLE")));
            bw.write(line);
            bw.write(String.format("\n %-43s: %d\n", "Total", Curriculum.getActive() + Curriculum.getInactive()));
            bw.write(line);
            bw.write("\n Cocurriculum Course\n");
            bw.write(" By Course Availability:-\n");
            bw.write(String.format(" \u25cf %-40s: %d (%.2f%%)\n", "Available Course", Cocurriculum.getActive(), Cocurriculum.calPercentage("AVAILABLE")));
            bw.write(String.format(" \u25cf %-40s: %d (%.2f%%)\n\n", "Unavailable Course", Cocurriculum.getInactive(), Cocurriculum.calPercentage("UNAVAILABLE")));
            bw.write(" By Course Category:-\n");
            bw.write(String.format(" \u25cf %-40s: %d (%.2f%%)\n", "Games/Sports", Cocurriculum.getGamesSports(), Cocurriculum.calPercentage("GAMES/SPORTS")));
            bw.write(String.format(" \u25cf %-40s: %d (%.2f%%)\n", "Cultural Studies", Cocurriculum.getCulturalStudies(), Cocurriculum.calPercentage("CULTURAL STUDIES")));
            bw.write(String.format(" \u25cf %-40s: %d (%.2f%%)\n", "Community Service", Cocurriculum.getCommunityService(), Cocurriculum.calPercentage("COMMUNITY SERVICE")));
            bw.write(String.format(" \u25cf %-40s: %d (%.2f%%)\n", "Volunteerism", Cocurriculum.getVolunteerism(), Cocurriculum.calPercentage("VOLUNTEERISM")));
            bw.write(line);
            bw.write(String.format("\n %-43s: %d\n", "Total", Cocurriculum.getActive() + Cocurriculum.getInactive()));
            bw.write(line);
            bw.write(String.format("\n%-44s: %d\n", " Grand Total Course", totalCourse));
            bw.write(line);
            bw.close();
        } catch (IOException e) {
            System.out.println("Failed to write file with exception " + e.getLocalizedMessage());
            return false;
        }
        return true;
    }

    private static void generateQR(Curriculum curr) {
        String myCodeText = String.format("Course Code: %s\nCourse Name: %s\nDescription: %s\nCredit Hours: %d\nTuition Fee: RM %.2f\nStatus: %s",
                curr.getCourseCode(), curr.getCourseName(), curr.getDescription(), curr.getCreditHours(), curr.getFee(), curr.getStatus());
        String filePath = "C:/Users/elfhc/Desktop/Assignment version/Assignment_ver1/Course-QRCode/Curriculum/" + curr.getCourseCode() + ".jpg";
        int size = 512;
        String fileType = "jpg";
        File currFile = new File(filePath);

        try {
            Map<EncodeHintType, Object> currHintType = new EnumMap<EncodeHintType, Object>(EncodeHintType.class
            );
            currHintType.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            currHintType.put(EncodeHintType.MARGIN, 1);
            Object put = currHintType.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            QRCodeWriter myQRCodeWriter = new QRCodeWriter();
            BitMatrix currBitMatrix = myQRCodeWriter.encode(myCodeText, BarcodeFormat.QR_CODE, size, size, currHintType);
            int currWidth = currBitMatrix.getWidth();
            BufferedImage currImage = new BufferedImage(currWidth, currWidth, BufferedImage.TYPE_INT_RGB);
            currImage.createGraphics();
            Graphics2D currGraphics = (Graphics2D) currImage.getGraphics();
            currGraphics.setColor(Color.WHITE);
            currGraphics.fillRect(0, 0, currWidth, currWidth);
            currGraphics.setColor(Color.BLACK);

            for (int i = 0; i < currWidth; i++) {
                for (int j = 0; j < currWidth; j++) {
                    if (currBitMatrix.get(i, j)) {
                        currGraphics.fillRect(i, j, 1, 1);
                    }
                }
            }

            ImageIO.write(currImage, fileType, currFile);
            System.out.println("QR Code for Course " + curr.getCourseCode() + " is created/updated successfully!");
        } catch (WriterException e) {
            System.out.println("\nSorry.. Something went wrong...\n");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void generateQR(Cocurriculum cocu) {
        String myCodeText = String.format("Course Name: %s\nDescription: %s\nCredit Hours: %d\nCategory: %s\nTuition Fee: RM %.2f\nStatus: %s",
                cocu.getCourseName(), cocu.getDescription(), cocu.getCreditHours(), cocu.getCategory(), cocu.getFee(), cocu.getStatus());
        String filePath = "C:/Users/elfhc/Desktop/Assignment version/Assignment_ver1/Course-QRCode/Cocurriculum/" + cocu.getCourseName() + ".jpg";
        int size = 512;
        String fileType = "jpg";
        File cocuFile = new File(filePath);

        try {
            Map<EncodeHintType, Object> cocuHintType = new EnumMap<EncodeHintType, Object>(EncodeHintType.class
            );
            cocuHintType.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            cocuHintType.put(EncodeHintType.MARGIN, 1);
            Object put = cocuHintType.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            QRCodeWriter myQRCodeWriter = new QRCodeWriter();
            BitMatrix currBitMatrix = myQRCodeWriter.encode(myCodeText, BarcodeFormat.QR_CODE, size, size, cocuHintType);
            int cocuWidth = currBitMatrix.getWidth();
            BufferedImage cocuImage = new BufferedImage(cocuWidth, cocuWidth, BufferedImage.TYPE_INT_RGB);
            cocuImage.createGraphics();
            Graphics2D cocuGraphics = (Graphics2D) cocuImage.getGraphics();
            cocuGraphics.setColor(Color.WHITE);
            cocuGraphics.fillRect(0, 0, cocuWidth, cocuWidth);
            cocuGraphics.setColor(Color.BLACK);

            for (int i = 0; i < cocuWidth; i++) {
                for (int j = 0; j < cocuWidth; j++) {
                    if (currBitMatrix.get(i, j)) {
                        cocuGraphics.fillRect(i, j, 1, 1);
                    }
                }
            }

            ImageIO.write(cocuImage, fileType, cocuFile);
            System.out.println("QR Code for Course " + cocu.getCourseCode() + " is created/updated successfully!\n");
        } catch (WriterException e) {
            System.out.println("\nSorry.. Something went wrong...\n");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readCurriculumExcel(String fileName) {
        try {
            String currExcel = "C:/Users/elfhc/Desktop/Assignment version/Assignment_ver1/Course-Excel/" + fileName + ".xlsx";
            FileInputStream inputStream = new FileInputStream(currExcel);
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet firstSheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = firstSheet.iterator();
            rowIterator.next();

            while (rowIterator.hasNext()) {
                Row nextRow = rowIterator.next();
                Iterator<Cell> cellIterator = nextRow.cellIterator();
                Curriculum newCurr = new Curriculum();
                while (cellIterator.hasNext()) {
                    Cell nextCell = cellIterator.next();
                    int columnIndex = nextCell.getColumnIndex();

                    switch (columnIndex) {
                        case 0:
                            String courseCode = nextCell.getStringCellValue().toUpperCase();
                            newCurr.setCourseCode(courseCode);
                            break;
                        case 1:
                            String courseName = nextCell.getStringCellValue().toUpperCase();
                            newCurr.setCourseName(courseName);
                            break;
                        case 2:
                            String description = nextCell.getStringCellValue().toUpperCase();
                            newCurr.setDescription(description);
                            break;
                        case 3:
                            int creditHours = (int) nextCell.getNumericCellValue();
                            newCurr.setCreditHours(creditHours);
                            break;
                        case 4:
                            double fee = nextCell.getNumericCellValue();
                            newCurr.setFee(fee);
                            break;
                        case 5:
                            String status = nextCell.getStringCellValue().toUpperCase();
                            newCurr.setStatus(status);
                            break;
                    }
                }
                Curriculum.addCurriculum(newCurr);
                generateQR(newCurr);
            }
            workbook.close();
            System.out.println("Curriculum imported successfully!");
        } catch (FileNotFoundException e) {
            System.out.println("File does not exist. Please check the name of file.");
        } catch (IOException e) {
            System.out.println("Error reading file");
            e.printStackTrace();
        }
    }

    private void readCocurriculumExcel(String fileName) {
        try {
            String cocuExcel = "C:/Users/elfhc/Desktop/Assignment version/Assignment_ver1/Course-Excel/" + fileName + ".xlsx";
            FileInputStream inputStream = new FileInputStream(cocuExcel);
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet firstSheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = firstSheet.iterator();
            rowIterator.next();

            while (rowIterator.hasNext()) {
                Row nextRow = rowIterator.next();
                Iterator<Cell> cellIterator = nextRow.cellIterator();
                Cocurriculum newCocu = new Cocurriculum();
                while (cellIterator.hasNext()) {
                    Cell nextCell = cellIterator.next();
                    int columnIndex = nextCell.getColumnIndex();

                    switch (columnIndex) {
                        case 0:
                            String courseName = nextCell.getStringCellValue().toUpperCase();
                            newCocu.setCourseName(courseName);
                            break;
                        case 1:
                            String description = nextCell.getStringCellValue().toUpperCase();
                            newCocu.setDescription(description);
                            break;
                        case 2:
                            int creditHours = (int) nextCell.getNumericCellValue();
                            newCocu.setCreditHours(creditHours);
                            break;
                        case 3:
                            String category = nextCell.getStringCellValue().toUpperCase();
                            newCocu.setCategory(category);
                            break;
                        case 4:
                            double fee = nextCell.getNumericCellValue();
                            newCocu.setFee(fee);
                            break;
                        case 5:
                            String status = nextCell.getStringCellValue();
                            newCocu.setStatus(status);
                            break;
                    }
                }
                System.out.println(newCocu.toString());
                Cocurriculum.addCocurriculum(newCocu);
            }
            workbook.close();
            System.out.println("Cocurriculum imported successfully!");
        } catch (FileNotFoundException e) {
            System.out.println("File does not exist. Please check the name of file.");
        } catch (IOException e) {
            System.out.println("Error reading file");
            e.printStackTrace();
        }
    }

    private String getFileName(String baseName) {
        LocalDateTime myDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String dateTimeInfo = myDate.format(formatter);
        return baseName.concat(String.format("_%s.xlsx", dateTimeInfo));
    }

    private void export(String table) {
        con = Assignment.getConnection();
        String excelFilePath = getFileName(table.concat("_Export"));
        try {
            String query = "SELECT * FROM ".concat(table);
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();

            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet(table);
            writeHeaderLine(rs, sheet);
            writeDataLines(rs, workbook, sheet);

            FileOutputStream outputStream = new FileOutputStream(excelFilePath);
            workbook.write(outputStream);
            workbook.close();

            pst.close();
        } catch (SQLException e) {
            System.out.println("Database error:");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("File IO error:");
            e.printStackTrace();
        }
    }

    private void export(String table, String status) {
        con = Assignment.getConnection();
        String excelFilePath = getFileName(table.concat("_" + status + "_Export"));
        try {
            String query = "SELECT * FROM ".concat(table) + " WHERE status = ?";
            pst = con.prepareStatement(query);
            pst.setString(1, status);
            rs = pst.executeQuery();

            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet(table);
            writeHeaderLine(rs, sheet);
            writeDataLines(rs, workbook, sheet);

            FileOutputStream outputStream = new FileOutputStream(excelFilePath);
            workbook.write(outputStream);
            workbook.close();

            pst.close();
        } catch (SQLException e) {
            System.out.println("Database error:");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("File IO error:");
            e.printStackTrace();
        }
    }

    private void exportCategory(String category) {
        con = Assignment.getConnection();
        String excelFilePath = getFileName(category.replace('/', '_') + "_Export");
        try {
            String query = "SELECT * FROM Cocurriculum WHERE category = ?";
            pst = con.prepareStatement(query);
            pst.setString(1, category);
            rs = pst.executeQuery();

            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Cocurriculum");
            writeHeaderLine(rs, sheet);
            writeDataLines(rs, workbook, sheet);

            FileOutputStream outputStream = new FileOutputStream(excelFilePath);
            workbook.write(outputStream);
            workbook.close();

            pst.close();
        } catch (SQLException e) {
            System.out.println("Database error:");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("File IO error:");
            e.printStackTrace();
        }
    }

    private void writeHeaderLine(ResultSet rs, XSSFSheet sheet) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int numOfColumns = metaData.getColumnCount();

        Row headerRow = sheet.createRow(0);

        for (int i = 1; i <= numOfColumns; i++) {
            String columnName = metaData.getColumnName(i);
            Cell headerCell = headerRow.createCell(i - 1);
            headerCell.setCellValue(columnName);
        }
    }

    private void writeDataLines(ResultSet rs, XSSFWorkbook workbook, XSSFSheet sheet) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int numOfColumns = metaData.getColumnCount();

        int rowCount = 1;

        while (rs.next()) {
            Row row = sheet.createRow(rowCount++);

            for (int i = 1; i <= numOfColumns; i++) {
                Object valueObject = rs.getObject(i);

                Cell cell = row.createCell(i - 1);

                if (valueObject instanceof Double) {
                    cell.setCellValue(((Double) valueObject));
                } else if (valueObject instanceof Integer) {
                    cell.setCellValue(((Integer) valueObject));
                } else {
                    cell.setCellValue(valueObject.toString());
                }
            }
        }
    }
}
