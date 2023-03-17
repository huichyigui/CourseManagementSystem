package assignment;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StaffService {

    Connection con = Assignment.getConnection();
    ResultSet rs = null;
    PreparedStatement pst = null;

    public void print(Assignment mainApp) {
        int choice;
        do {
            try {
                Helper.printHeader("Admin Main > Staff");
                System.out.println("What would you like to do?");
                System.out.println("[1] View Staff Details");
                System.out.println("[2] Add New Staff Details");
                System.out.println("[3] Update Staff Details");
                System.out.println("[4] Delete Staff ");
                System.out.println("[5] Generate Staff Reports");
                System.out.println("[6] Return to Previous Menu");
                System.out.printf("%-50s: ", "Choice");
                choice = Integer.parseInt(Helper.readLine());
                switch (choice) {
                    case 1:
                        viewStaffDetails(mainApp);
                        break;
                    case 2:
                        addStaff(mainApp);
                        break;
                    case 3:
                        updateStaff(mainApp);
                        break;
                    case 4:
                        deleteStaff(mainApp);
                        break;
                    case 5:
                        generateStaffReportMenu(mainApp);
                        break;
                    case 6:
                        return;
                    default:
                        System.out.println("Please enter valid option.");
                        Helper.systemSleep(1);
                }
            } catch (Exception e) {
                System.out.println(e.getLocalizedMessage());
            }
        } while (true);
    }

    private void generateStaffReportMenu(Assignment mainApp) {
        Helper.printHeader("Admin > Staff: Generate Staff List Report");
        Helper.printLine(59);
        System.out.println("\nSelect type of report to be generated: ");
        System.out.println("[1] Generate ALL");
        System.out.println("[2] Generate Fulltime Staff List Only");
        System.out.println("[3] Generate Parttime Staff List Only");
        System.out.println("[0] Return to Previous Menu");

        do {
            System.out.printf("%-50s: ", "Choice");
            int choice = Integer.parseInt(Helper.readLine());
            switch (choice) {
                case 1:
                    generateTheStaffReport();
                    break;
                case 2:
                    generateTheStaffReport(1);
                    break;
                case 3:
                    generateTheStaffReport(1.0);
                    break;
                case 0:
                    mainApp.printAdminMainView();
                    break;
            }
        } while (true);
    }

    private void generateTheStaffReport() {
        try {
            int count = 0;
            FileOutputStream fileOutput = new FileOutputStream("Staff List Report - ALL.txt");
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fileOutput));
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String line = String.format("%" + 178 + "s", "").replace(" ", "═");
            String line2 = String.format("%" + 199 + "s", "").replace(" ", "═");
            bw.write(String.format("\n%92s\n", "ANAVRIN COLLEGE ENROLLMENT SYSTEM"));
            bw.write(String.format("%85s\n", "STAFF LIST REPORT"));
            bw.write(String.format("\nGenerated On: %s\n", now.format(formatter)));
            bw.newLine();
            bw.write("\nFullTime Staff\n");
            bw.write(line);
            bw.newLine();
            bw.write(String.format("║%10s ║ %-30s ║ %-15s ║ %-7s ║ %-14s ║ %-30s ║ %-10s ║ %-9s ║ %-15s ", "Staff ID", "Staff Name", "IC", "Gender", "Contact Number", "Email Address", "DOB", "Account Status", "Staff Type"));
            bw.newLine();
            bw.write(line);
            bw.newLine();
            String query = "SELECT fulltimeStaffID, name, IC, gender, contactNumber, emailAddress, DOB, accountStatus,staffType FROM fulltimestaff order by accountStatus;";
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();
            while (rs.next()) {
                bw.write(String.format("║%9s ║ %-30s ║ %-15s ║ %-7s ║ %-14s ║ %-30s ║ %-9s ║ %-9s ║ %-15s║\n",
                        rs.getString("fulltimeStaffID"), rs.getString("name"), rs.getString("IC"), rs.getString("gender"),
                        rs.getString("contactNumber"), rs.getString("emailAddress"), rs.getString("DOB"), rs.getString("accountStatus"),
                        rs.getString("staffType")));
                count++;
            }
            bw.write(line);

            bw.newLine();
            bw.write("\nPartTime Staff\n");
            bw.write(line2);
            bw.newLine();
            bw.write(String.format("║%11s ║ %-28s ║ %-15s ║ %-7s ║ %-14s ║ %-30s ║ %-10s ║ %-9s ║ %-15s║", "Staff ID", "Staff Name", "IC", "Gender", "Contact Number", "Email Address", "DOB", "Account Status", "Staff Type"));
            bw.newLine();
            bw.write(line2);
            bw.newLine();
            query = "SELECT parttimeStaffID, name, IC, gender, contactNumber, emailAddress, DOB, accountStatus, staffType FROM parttimestaff order by accountStatus;";
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();
            while (rs.next()) {
                bw.write(String.format("║%11s ║ %-28s ║ %-15s ║ %-7s ║ %-14s ║ %-30s ║ %-10s ║ %-9s ║ %-15s║\n",
                        rs.getString("parttimeStaffID"), rs.getString("name"), rs.getString("IC"), rs.getString("gender"),
                        rs.getString("contactNumber"), rs.getString("emailAddress"), rs.getString("DOB"), rs.getString("accountStatus"),
                        rs.getString("staffType")));
                count++;
            }
            bw.write(line2);
            bw.newLine();
            bw.newLine();
            bw.write("Total: " + count + " staff");
            bw.close();
        } catch (Exception e) {
            System.out.println("Failed to write file with exception " + e.getLocalizedMessage());
        }
        Helper.systemSleep(1);
        System.out.println("\nStaff Report generated successfully!\n");
    }

    private void generateTheStaffReport(int selection) {
        try {
            int count = 0;
            FileOutputStream fileOutput = new FileOutputStream("Staff List Report - FullTime.txt");
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fileOutput));
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String line = String.format("%" + 178 + "s", "").replace(" ", "═");
            String line2 = String.format("%" + 199 + "s", "").replace(" ", "═");
            bw.write(String.format("\n%92s\n", "ANAVRIN COLLEGE ENROLLMENT SYSTEM"));
            bw.write(String.format("%85s\n", "STAFF LIST REPORT"));
            bw.write(String.format("\nGenerated On: %s\n", now.format(formatter)));
            bw.newLine();
            bw.write("\nFullTime Staff\n");
            bw.write(line);
            bw.newLine();
            bw.write(String.format("║%9s ║ %-30s ║ %-15s ║ %-7s ║ %-14s ║ %-30s ║ %-10s ║ %-9s ║ %-15s║", "Staff ID", "Staff Name", "Staff IC", "Gender", "Contact Number", "Email Address", "DOB", "Account Status", "Staff Type"));
            bw.newLine();
            bw.write(line);
            bw.newLine();
            String query = "SELECT fulltimeStaffID, name, IC, gender, contactNumber, emailAddress, DOB, accountStatus,staffType FROM fulltimestaff order by accountStatus;";
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();
            while (rs.next()) {
                bw.write(String.format("║%10s ║ %-30s ║ %-15s ║ %-7s ║ %-14s ║ %-30s ║ %-10s ║ %-9s ║ %-15s║\n",
                        rs.getString("fulltimeStaffID"), rs.getString("name"), rs.getString("IC"), rs.getString("gender"),
                        rs.getString("contactNumber"), rs.getString("emailAddress"), rs.getString("DOB"), rs.getString("accountStatus"),
                        rs.getString("staffType")));
                count++;
            }
            bw.write(line);

            bw.newLine();
            bw.newLine();
            bw.write("Total: " + count + " staff");
            bw.close();
        } catch (Exception e) {
            System.out.println("Failed to write file with exception " + e.getLocalizedMessage());
        }
        Helper.systemSleep(1);
        System.out.println("\nStaff Report generated successfully!\n");
    }

    private void generateTheStaffReport(double selection) {
        try {
            int count = 0;
            FileOutputStream fileOutput = new FileOutputStream("Staff List Report - PARTTIME.txt");
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fileOutput));
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String line = String.format("%" + 178 + "s", "").replace(" ", "═");
            String line2 = String.format("%" + 199 + "s", "").replace(" ", "═");
            bw.write(String.format("\n%92s\n", "ANAVRIN COLLEGE ENROLLMENT SYSTEM"));
            bw.write(String.format("%85s\n", "STAFF LIST REPORT"));
            bw.write(String.format("\nGenerated On: %s\n", now.format(formatter)));
            bw.newLine();
            bw.write("\nPartTime Staff\n");
            bw.write(line);
            bw.newLine();
            bw.write(String.format("║%10s ║ %-30s ║ %-15s ║ %-7s ║ %-14s ║ %-30s ║ %-10s ║ %-9s ║ %-15s║", "Staff ID", "Staff Name", "Staff IC", "Gender", "Contact Number", "Email Address", "DOB", "Account Status", "Staff Type"));
            bw.newLine();
            bw.write(line);
            bw.newLine();
            String query = "SELECT parttimeStaffID, name, IC, gender, contactNumber, emailAddress, DOB, accountStatus,staffType FROM parttimestaff order by accountStatus;";
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();
            while (rs.next()) {
                bw.write(String.format("║%10s ║ %-30s ║ %-15s ║ %-7s ║ %-14s ║ %-30s ║ %-10s ║ %-9s ║ %-15s║\n",
                        rs.getString("parttimeStaffID"), rs.getString("name"), rs.getString("IC"), rs.getString("gender"),
                        rs.getString("contactNumber"), rs.getString("emailAddress"), rs.getString("DOB"), rs.getString("accountStatus"),
                        rs.getString("staffType")));
                count++;
            }
            bw.write(line);
            bw.newLine();
            bw.newLine();
            bw.write("Total: " + count + " staff");
            bw.close();
        } catch (Exception e) {
            System.out.println("Failed to write file with exception " + e.getLocalizedMessage());
        }
        Helper.systemSleep(1);
        System.out.println("\nStaff Report generated successfully!\n");
    }

    private void deleteStaff(Assignment mainApp) {
        String choice;
        Helper.printHeader("Admin > Staff: Delete Staff Details");
        while (true) {
            System.out.println("Enter Staff Type");
            System.out.println("[1] FullTime");
            System.out.println("[2] PartTime");
            System.out.printf("%-50s: ", "Enter choice (Q to quit)");
            choice = Helper.readLine().toUpperCase().trim();
            switch (choice) {
                case "1":
                    deleteFullTime(mainApp);
                    break;
                case "2":
                    deletePartTime(mainApp);
                    break;
                case "Q":
                    return;
                default:
                    System.out.println("Please enter valid option.");
            }
        }
    }

    private void deleteFullTime(Assignment mainApp) {
        Helper.printHeader("Admin > Staff: Delete FullTime Staff Details");
        viewStaff("Fulltime");
        while (true) {
            System.out.println();
            System.out.println("\t\t\t\t\t\tTips: ");
            System.out.println("The delete process is INRREVERSIBLE, please proceed with caution. ");
            System.out.println("You can update the staff account status to inactive instead of deleting it.");
            System.out.print("\n\nEnter the Staff ID in order to delete the account (Q to Quit): ");
            String staffIDInput = Helper.readLine().trim().toUpperCase();
            if (staffIDInput.equals("Q")) {
                break;
            }
            if (FulltimeStaff.checkFulltimeStaffIDFormat(staffIDInput)) {
                if (FulltimeStaff.checkDuplicateFulltimeStaffID(staffIDInput)) {
                    FulltimeStaff fulltimeStaff = FulltimeStaff.getFulltStaff(staffIDInput);

                    System.out.print("Staff ID number: " + fulltimeStaff.getStaffID() + " , " + fulltimeStaff.getName() + " will be deleted. " + "\n\nConfirm to proceed? y/n: ");
                    String confirm = Helper.readLine().toUpperCase();
                    if (confirm.equals("Y")) {
                        FulltimeStaff.deleteFulltimeStaff(staffIDInput);
                        System.out.println("Fulltime Staff's account successfully deleted!");
                    } else {
                        System.out.println("Aborting");
                        Helper.pause();
                    }
                } else {
                    System.out.printf("StaffID (%s) does not exist!\n", staffIDInput);
                    Helper.pause();
                }
            } else {
                System.out.println("Invalid StaffID format\n");
            }
        }
    }

    private void deletePartTime(Assignment mainApp) {
        Helper.printHeader("Admin > Staff: Delete PartTime Staff Details");
        viewStaff("parttimestaff");
        while (true) {
            System.out.println();
            System.out.println("\t\t\t\t\t\tTips: ");
            System.out.println("The delete process is INRREVERSIBLE, please proceed with caution. ");
            System.out.println("You can update the staff account status to inactive instead of deleting it.");
            System.out.print("\n\nEnter the Staff ID in order to delete the account (Q to Quit): ");
            String staffIDInput = Helper.readLine().trim().toUpperCase();
            if (staffIDInput.equals("Q")) {
                break;
            }
            if (ParttimeStaff.checkParttimeStaffIDFormat(staffIDInput)) {
                if (ParttimeStaff.checkDuplicateParttimeStaffID(staffIDInput)) {
                    ParttimeStaff parttimeStaff = ParttimeStaff.getParttStaff(staffIDInput);

                    System.out.print("Staff's ID number: " + parttimeStaff.getStaffID() + " , " + parttimeStaff.getName() + " will be deleted. " + "\n\nConfirm to proceed? y/n: ");
                    String confirm = Helper.readLine().toUpperCase();
                    if (confirm.equals("Y")) {
                        ParttimeStaff.deleteParttimeStaff(staffIDInput);
                        System.out.println("Parttime Staff account successfully deleted!");
                    } else {
                        System.out.println("Aborting");
                        Helper.pause();
                    }
                } else {
                    System.out.printf("StaffID (%s) does not exist!\n", staffIDInput);
                    Helper.pause();
                }
            } else {
                System.out.println("Invalid StaffID format\n");
            }
        }
    }

    private void updateStaff(Assignment mainApp) {
        String choice;
        Helper.printHeader("Admin > Staff: Update Staff Details");
        while (true) {
            System.out.println("Enter Staff Type");
            System.out.println("[1] FullTime");
            System.out.println("[2] PartTime");
            System.out.printf("%-50s: ", "Enter choice (Q to quit)");
            choice = Helper.readLine().toUpperCase().trim();
            switch (choice) {
                case "1":
                    updateFulltimeStaff(mainApp);
                    break;
                case "2":
                    updateParttimeStaff(mainApp);
                    break;
                case "Q":
                    return;
                default:
                    System.out.println("Please enter valid option.");
            }
        }
    }

    private void updateFulltimeStaff(Assignment mainApp) {
        int choice;
        do {
            Helper.printHeader("Admin > Staff: Update Staff Details > FullTime Staff");
            System.out.println("What would you like to do?");
            System.out.println("[1] Update Staff Name");
            System.out.println("[2] Update Staff IC");
            System.out.println("[3] Update Staff Gender");
            System.out.println("[4] Update Staff Contact Number");
            System.out.println("[5] Update Staff Email Address");
            System.out.println("[6] Update Staff DOB");
            System.out.println("[7] Update Staff Account Status");
            System.out.println("[8] Update Staff Account Password");
            System.out.println("[9] Update Staff Type");
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
                    changeStaffName();
                    break;
                case 2:
                    changeStaffIC();
                    break;
                case 3:
                    changeStaffGender();
                    break;
                case 4:
                    changeStaffContactNumber();
                    break;
                case 5:
                    changeStaffEmailAddress();
                    break;
                case 6:
                    changeStaffDOB();
                    break;
                case 7:
                    changeStaffAccountStatus();
                    break;
                case 8:
                    changeStaffAccountPassword();
                    break;
                case 9:
                    changeStaffType();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Please enter valid option.");
                    Helper.pause();
            }
        } while (true);
    }

    private void updateParttimeStaff(Assignment mainApp) {
        int choice;
        do {
            Helper.printHeader("Admin > Staff: Update Staff Details > PartTime");
            System.out.println("What would you like to do?");
            System.out.println("[1] Update Staff Name");
            System.out.println("[2] Update Staff IC");
            System.out.println("[3] Update Staff Gender");
            System.out.println("[4] Update Staff Contact Number");
            System.out.println("[5] Update Staff Email Address");
            System.out.println("[6] Update Staff DOB");
            System.out.println("[7] Update Staff Account Status");
            System.out.println("[8] Update Staff Account Password");
            System.out.println("[9] Update Staff Type");
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
                    changeStaffName("Parttime");
                    break;
                case 2:
                    changeStaffIC("Parttime");
                    break;
                case 3:
                    changeStaffGender("Parttime");
                    break;
                case 4:
                    changeStaffContactNumber("Parttime");
                    break;
                case 5:
                    changeStaffEmailAddress("Parttime");
                    break;
                case 6:
                    changeStaffDOB("Parttime");
                    break;
                case 7:
                    changeStaffAccountStatus("Parttime");
                    break;
                case 8:
                    changeStaffAccountPassword("Parttime");
                    break;
                case 9:
                    changeStaffType("Parttime");
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Please enter valid option.");
                    Helper.pause();
            }
        } while (true);
    }

    private void changeStaffType() {
        Helper.printHeader("Admin > Staff: Update Staff Details > Update Staff Type");
        viewStaff("FullTime");
        while (true) {
            System.out.print("Enter the Staff ID in order to change the Account Staff Type (Q to Quit): ");
            String staffIDInput = Helper.readLine().trim().toUpperCase();
            if (staffIDInput.equals("Q")) {
                break;
            }
            if (FulltimeStaff.checkFulltimeStaffIDFormat(staffIDInput)) {
                if (FulltimeStaff.checkDuplicateFulltimeStaffID(staffIDInput)) {
                    FulltimeStaff fulltimeStaff = FulltimeStaff.getFulltStaff(staffIDInput);

                    System.out.println("\nExisting staff :" + fulltimeStaff.getName() + " 's staff type is " + fulltimeStaff.getStaffType() + "\n");

                    String newInput = FulltimeStaff.selectStaffType();

                    System.out.print("Staff Type: " + fulltimeStaff.getStaffType() + " will be updated to " + newInput + "\n\nConfirm? y/n: ");
                    String confirm = Helper.readLine().toUpperCase();
                    if (confirm.equals("Y")) {
                        fulltimeStaff.setStaffType(newInput);
                        FulltimeStaff.updateStaff(fulltimeStaff);
                        System.out.println("Fulltime Staff's Staff Type changed successfully!");
                    } else {
                        System.out.println("Aborting");
                        Helper.pause();
                    }
                } else {
                    System.out.printf("StaffID (%s) does not exist!\n", staffIDInput);
                    Helper.pause();
                }
            } else {
                System.out.println("Invalid StaffID format\n");
            }
        }
    }

    private void changeStaffType(String type) {
        Helper.printHeader("Admin > Staff: Update Staff Details > Update Parttime Staff Type");
        viewStaff("Parttime");
        while (true) {
            System.out.print("Enter the Staff ID in order to change the Account Staff Type (Q to Quit): ");
            String staffIDInput = Helper.readLine().trim().toUpperCase();
            if (staffIDInput.equals("Q")) {
                break;
            }

            if (ParttimeStaff.checkParttimeStaffIDFormat(staffIDInput)) {
                if (ParttimeStaff.checkDuplicateParttimeStaffID(staffIDInput)) {
                    ParttimeStaff parttimeStaff = ParttimeStaff.getParttStaff(staffIDInput);

                    System.out.println("\nExisting staff :" + parttimeStaff.getName() + " 's staff type is " + parttimeStaff.getStaffType() + "\n");

                    String newInput = ParttimeStaff.selectStaffType();

                    System.out.print("Staff's Study Mode: " + parttimeStaff.getStaffType() + " will be updated to " + newInput + "\n\nConfirm? y/n: ");
                    String confirm = Helper.readLine().toUpperCase();
                    if (confirm.equals("Y")) {
                        parttimeStaff.setStaffType(newInput);
                        ParttimeStaff.updateStaff(parttimeStaff);
                        System.out.println("Parttime Staff's Staff Type changed successfully!");
                    } else {
                        System.out.println("Aborting");
                        Helper.pause();
                    }
                } else {
                    System.out.printf("StaffID (%s) does not exist!\n", staffIDInput);
                    Helper.pause();
                }
            } else {
                System.out.println("Invalid Stafformat\n");
            }
        }
    }

    private void changeStaffAccountStatus() {
        Helper.printHeader("Admin > Staff: Update Staff details > Update Staff Account Status");
        viewStaff("Fulltime");
        while (true) {
            System.out.print("Enter the Staff ID in order to change the Account Status (Q to Quit): ");
            String staffIDInput = Helper.readLine().trim().toUpperCase();
            if (staffIDInput.equals("Q")) {
                break;
            }
            if (FulltimeStaff.checkFulltimeStaffIDFormat(staffIDInput)) {
                if (FulltimeStaff.checkDuplicateFulltimeStaffID(staffIDInput)) {
                    FulltimeStaff fulltimeStaff = FulltimeStaff.getFulltStaff(staffIDInput);

                    System.out.println("\nExisting staff :" + fulltimeStaff.getName() + " 's account status is " + fulltimeStaff.getAccountStatus() + "\n");

                    String newInput = FulltimeStaff.selectAccountStatus();

                    System.out.print("Staff's Account Status: " + fulltimeStaff.getAccountStatus() + " will be updated to " + newInput + "\n\nConfirm? y/n: ");
                    String confirm = Helper.readLine().toUpperCase();
                    if (confirm.equals("Y")) {
                        fulltimeStaff.setAccountStatus(newInput);
                        FulltimeStaff.updateStaff(fulltimeStaff);
                        System.out.println("Fulltime Staff's Account Status changed successfully!");
                    } else {
                        System.out.println("Aborting");
                        Helper.pause();
                    }
                } else {
                    System.out.printf("StaffID (%s) does not exist!\n", staffIDInput);
                    Helper.pause();
                }
            } else {
                System.out.println("Invalid StaffID format\n");
            }
        }
    }

    private void changeStaffAccountStatus(String type) {
        Helper.printHeader("Admin > Staff: Update Staff Details > Update Parttime Staff Account Status");
        viewStaff("Parttime");
        while (true) {
            System.out.print("Enter the Staff ID in order to change the Account Status (Q to Quit): ");
            String staffIDInput = Helper.readLine().trim().toUpperCase();
            if (staffIDInput.equals("Q")) {
                break;
            }
            if (ParttimeStaff.checkParttimeStaffIDFormat(staffIDInput)) {
                if (ParttimeStaff.checkDuplicateParttimeStaffID(staffIDInput)) {
                    ParttimeStaff parttimeStaff = ParttimeStaff.getParttStaff(staffIDInput);

                    System.out.println("\nExisting staff :" + parttimeStaff.getName() + " 's account status is " + parttimeStaff.getAccountStatus() + "\n");

                    String newInput = ParttimeStaff.selectAccountStatus();

                    System.out.print("Staff's Account Status: " + parttimeStaff.getAccountStatus() + " will be updated to " + newInput + "\n\nConfirm? y/n: ");
                    String confirm = Helper.readLine().toUpperCase();
                    if (confirm.equals("Y")) {
                        parttimeStaff.setAccountStatus(newInput);
                        ParttimeStaff.updateStaff(parttimeStaff);
                        System.out.println("Parttime Staff's Account Status changed successfully!");
                    } else {
                        System.out.println("Aborting");
                        Helper.pause();
                    }
                } else {
                    System.out.printf("StaffID (%s) does not exist!\n", staffIDInput);
                    Helper.pause();
                }
            } else {
                System.out.println("Invalid StaffID format\n");
            }
        }
    }

    private void changeStaffDOB() {
        Helper.printHeader("Admin > Staff: Update Staff Details > Update Staff DOB");
        viewStaff("Fulltime");
        while (true) {
            System.out.print("Enter the Staff ID in order to change the DOB (Q to Quit): ");
            String staffIDInput = Helper.readLine().trim().toUpperCase();
            if (staffIDInput.equals("Q")) {
                break;
            }
            if (FulltimeStaff.checkFulltimeStaffIDFormat(staffIDInput)) {
                if (FulltimeStaff.checkDuplicateFulltimeStaffID(staffIDInput)) {
                    FulltimeStaff fulltimeStaff = FulltimeStaff.getFulltStaff(staffIDInput);

                    System.out.println("\nExisting staff :" + fulltimeStaff.getName() + " 's DOB is " + fulltimeStaff.getDOB() + "\n");

                    System.out.print("Enter new staff DOB: ");
                    String newInput = Helper.readLine().trim();

                    while (!FulltimeStaff.checkDOB(newInput)) {
                        System.out.print("Please re-enter the staff DOB: ");
                        newInput = Helper.readLine().trim();
                    }

                    System.out.print("Staff's DOB: " + fulltimeStaff.getDOB() + " will be updated to " + newInput + "\n\nConfirm? y/n: ");
                    String confirm = Helper.readLine().toUpperCase();
                    if (confirm.equals("Y")) {
                        fulltimeStaff.setDOB(newInput);
                        FulltimeStaff.updateStaff(fulltimeStaff);
                        System.out.println("Fulltime Staff's DOB changed successfully!");
                    } else {
                        System.out.println("Aborting");
                        Helper.pause();
                    }
                } else {
                    System.out.printf("StaffID (%s) does not exist!\n", staffIDInput);
                    Helper.pause();
                }
            } else {
                System.out.println("Invalid StaffID format\n");
            }
        }
    }

    private void changeStaffDOB(String type) {
        Helper.printHeader("Admin > Staff: Update Staff Details > Update ParttimeStaff DOB");
        viewStaff("Parttime");
        while (true) {
            System.out.print("Enter the Staff ID in order to change the DOB (Q to Quit): ");
            String staffIDInput = Helper.readLine().trim().toUpperCase();
            if (staffIDInput.equals("Q")) {
                break;
            }

            if (ParttimeStaff.checkParttimeStaffIDFormat(staffIDInput)) {
                if (ParttimeStaff.checkDuplicateParttimeStaffID(staffIDInput)) {
                    ParttimeStaff parttimeStaff = ParttimeStaff.getParttStaff(staffIDInput);

                    System.out.println("\nExisting staff :" + parttimeStaff.getName() + " 's DOB is " + parttimeStaff.getDOB() + "\n");

                    System.out.print("Enter new staff DOB: ");
                    String newInput = Helper.readLine().trim();

                    while (!ParttimeStaff.checkDOB(newInput)) {
                        System.out.print("Please re-enter the staff DOB: ");
                        newInput = Helper.readLine().trim();
                    }

                    System.out.print("Staff's DOB: " + parttimeStaff.getDOB() + " will be updated to " + newInput + "\n\nConfirm? y/n: ");
                    String confirm = Helper.readLine().toUpperCase();
                    if (confirm.equals("Y")) {
                        parttimeStaff.setDOB(newInput);
                        ParttimeStaff.updateStaff(parttimeStaff);
                        System.out.println("Parttime Staff's DOB changed successfully!");
                    } else {
                        System.out.println("Aborting");
                        Helper.pause();
                    }
                } else {
                    System.out.printf("StaffID (%s) does not exist!\n", staffIDInput);
                    Helper.pause();
                }
            } else {
                System.out.println("Invalid StaffID format\n");
            }
        }
    }

    private void changeStaffEmailAddress() {
        Helper.printHeader("Admin > Staff: Update Staff Details > Update Staff Email Address");
        viewStaff("Fulltime");
        while (true) {
            System.out.print("Enter the Staff ID in order to change the email address (Q to Quit): ");
            String staffIDInput = Helper.readLine().trim().toUpperCase();
            if (staffIDInput.equals("Q")) {
                break;
            }
            if (FulltimeStaff.checkFulltimeStaffIDFormat(staffIDInput)) {
                if (FulltimeStaff.checkDuplicateFulltimeStaffID(staffIDInput)) {
                    FulltimeStaff fulltimeStaff = FulltimeStaff.getFulltStaff(staffIDInput);

                    System.out.println("\nExisting staff :" + fulltimeStaff.getName() + " 's email address is " + fulltimeStaff.getEmailAddress() + "\n");

                    System.out.print("Enter new staff email address: ");
                    String newInput = Helper.readLine().trim();

                    while (!FulltimeStaff.checkEmailAddress(newInput)) {
                        System.out.print("Please re-enter the staff email address: ");
                        newInput = Helper.readLine().trim();
                    }

                    System.out.print("Staff's email address: " + fulltimeStaff.getEmailAddress() + " will be updated to " + newInput + "\n\nConfirm? y/n: ");
                    String confirm = Helper.readLine().toUpperCase();
                    if (confirm.equals("Y")) {
                        fulltimeStaff.setEmailAddress(newInput);
                        FulltimeStaff.updateStaff(fulltimeStaff);
                        System.out.println("Fulltime Staff's email address changed successfully!");
                    } else {
                        System.out.println("Aborting");
                        Helper.pause();
                    }
                } else {
                    System.out.printf("StaffID (%s) does not exist!\n", staffIDInput);
                    Helper.pause();
                }
            } else {
                System.out.println("Invalid StaffID format\n");
            }
        }
    }

    private void changeStaffEmailAddress(String type) {
        Helper.printHeader("Admin > Staff: Update Staff Details > Update ParttimeStaff Email Address");
        viewStaff("Parttime");
        while (true) {
            System.out.print("Enter the Staff ID in order to change the email address (Q to Quit): ");
            String staffIDInput = Helper.readLine().trim().toUpperCase();
            if (staffIDInput.equals("Q")) {
                break;
            }
            if (ParttimeStaff.checkParttimeStaffIDFormat(staffIDInput)) {
                if (ParttimeStaff.checkDuplicateParttimeStaffID(staffIDInput)) {
                    ParttimeStaff parttimeStaff = ParttimeStaff.getParttStaff(staffIDInput);

                    System.out.println("\nExisting staff :" + parttimeStaff.getName() + " 's email address is " + parttimeStaff.getEmailAddress() + "\n");

                    System.out.print("Enter new staff email address: ");
                    String newInput = Helper.readLine().trim();

                    while (!ParttimeStaff.checkEmailAddress(newInput)) {
                        System.out.print("Please re-enter the staff email address: ");
                        newInput = Helper.readLine().trim();
                    }

                    System.out.print("Staff's email address: " + parttimeStaff.getEmailAddress() + " will be updated to " + newInput + "\n\nConfirm? y/n: ");
                    String confirm = Helper.readLine().toUpperCase();
                    if (confirm.equals("Y")) {
                        parttimeStaff.setEmailAddress(newInput);
                        ParttimeStaff.updateStaff(parttimeStaff);
                        System.out.println("Parttime Staff's email address changed successfully!");
                    } else {
                        System.out.println("Aborting");
                        Helper.pause();
                    }
                } else {
                    System.out.printf("StaffID (%s) does not exist!\n", staffIDInput);
                    Helper.pause();
                }
            } else {
                System.out.println("Invalid StaffID format\n");
            }
        }
    }

    private void changeStaffContactNumber() {
        Helper.printHeader("Admin > Staff: Update Staff Details > Update Staff Contact Number");
        viewStaff("Fulltime");
        while (true) {
            System.out.print("Enter the Staff ID in order to change the contact number (Q to Quit): ");
            String staffIDInput = Helper.readLine().trim().toUpperCase();
            if (staffIDInput.equals("Q")) {
                break;
            }
            if (FulltimeStaff.checkFulltimeStaffIDFormat(staffIDInput)) {
                if (FulltimeStaff.checkDuplicateFulltimeStaffID(staffIDInput)) {
                    FulltimeStaff fulltimeStaff = FulltimeStaff.getFulltStaff(staffIDInput);

                    System.out.println("\nExisting staff :" + fulltimeStaff.getName() + " 's contact number is " + fulltimeStaff.getContactNumber() + "\n");

                    System.out.print("Enter new staff contact number: ");
                    String newInput = Helper.readLine().trim();

                    while (!FulltimeStaff.checkContactNumber(newInput)) {
                        System.out.print("Please re-enter the staff contact number: ");
                        newInput = Helper.readLine().trim();
                    }

                    System.out.print("Staff's contact number: " + fulltimeStaff.getContactNumber() + " will be updated to " + newInput + "\n\nConfirm? y/n: ");
                    String confirm = Helper.readLine().toUpperCase();
                    if (confirm.equals("Y")) {
                        fulltimeStaff.setContactNumber(newInput);
                        FulltimeStaff.updateStaff(fulltimeStaff);
                        System.out.println("Fulltime Staff's contact number changed successfully!");
                    } else {
                        System.out.println("Aborting");
                        Helper.pause();
                    }
                } else {
                    System.out.printf("StaffID (%s) does not exist!\n", staffIDInput);
                    Helper.pause();
                }
            } else {
                System.out.println("Invalid StaffID format\n");
            }
        }
    }

    private void changeStaffContactNumber(String type) {
        Helper.printHeader("Admin > Staff: Update Staff Details > Update Parttime Staff Contact Number");
        viewStaff("Parttime");
        while (true) {
            System.out.print("Enter the Staff ID in order to change the contact number (Q to Quit): ");
            String staffIDInput = Helper.readLine().trim().toUpperCase();
            if (staffIDInput.equals("Q")) {
                break;
            }
            if (ParttimeStaff.checkParttimeStaffIDFormat(staffIDInput)) {
                if (ParttimeStaff.checkDuplicateParttimeStaffID(staffIDInput)) {
                    ParttimeStaff parttimeStaff = ParttimeStaff.getParttStaff(staffIDInput);

                    System.out.println("\nExisting staff :" + parttimeStaff.getName() + " 's contact number is " + parttimeStaff.getContactNumber() + "\n");

                    System.out.print("Enter new staff contact number: ");
                    String newInput = Helper.readLine().trim();

                    while (!ParttimeStaff.checkContactNumber(newInput)) {
                        System.out.print("Please re-enter the staff contact number: ");
                        newInput = Helper.readLine().trim();
                    }

                    System.out.print("Staff's contact number: " + parttimeStaff.getContactNumber() + " will be updated to " + newInput + "\n\nConfirm? y/n: ");
                    String confirm = Helper.readLine().toUpperCase();
                    if (confirm.equals("Y")) {
                        parttimeStaff.setContactNumber(newInput);
                        ParttimeStaff.updateStaff(parttimeStaff);
                        System.out.println("ParttimeStaff's contact number changed successfully!");
                    } else {
                        System.out.println("Aborting");
                        Helper.pause();
                    }
                } else {
                    System.out.printf("StaffID (%s) does not exist!\n", staffIDInput);
                    Helper.pause();
                }
            } else {
                System.out.println("Invalid StaffID format\n");
            }
        }
    }

    private void changeStaffGender() {
        Helper.printHeader("Admin > Staff: Update Staff Details > Update Staff Gender");
        viewStaff("Fulltime");
        while (true) {
            System.out.print("Enter the Staff ID in order to change the gender (Q to Quit): ");
            String staffIDInput = Helper.readLine().trim().toUpperCase();
            if (staffIDInput.equals("Q")) {
                break;
            }
            if (FulltimeStaff.checkFulltimeStaffIDFormat(staffIDInput)) {
                if (FulltimeStaff.checkDuplicateFulltimeStaffID(staffIDInput)) {
                    FulltimeStaff fulltimeStaff = FulltimeStaff.getFulltStaff(staffIDInput);

                    System.out.println("\nExisting staff :" + fulltimeStaff.getName() + " 's gender is " + fulltimeStaff.getGender() + "\n");

                    String newInput = FulltimeStaff.selectGender();

                    System.out.print("Staff's gender: " + fulltimeStaff.getGender() + " will be updated to " + newInput + "\n\nConfirm? y/n: ");
                    String confirm = Helper.readLine().toUpperCase();
                    if (confirm.equals("Y")) {
                        fulltimeStaff.setIC(newInput);
                        FulltimeStaff.updateStaff(fulltimeStaff);
                        System.out.println("Fulltime Staff's gender changed successfully!");
                    } else {
                        System.out.println("Aborting");
                        Helper.pause();
                    }
                } else {
                    System.out.printf("StaffID (%s) does not exist!\n", staffIDInput);
                    Helper.pause();
                }
            } else {
                System.out.println("Invalid StaffID format\n");
            }
        }
    }

    private void changeStaffGender(String type) {
        Helper.printHeader("Admin > Staff: Update Staff Details > Update Parttime Staff Gender");
        viewStaff("Parttime");
        while (true) {
            System.out.print("Enter the Staff ID in order to change the gender (Q to Quit): ");
            String staffIDInput = Helper.readLine().trim().toUpperCase();
            if (staffIDInput.equals("Q")) {
                break;
            }
            if (ParttimeStaff.checkParttimeStaffIDFormat(staffIDInput)) {
                if (ParttimeStaff.checkDuplicateParttimeStaffID(staffIDInput)) {
                    ParttimeStaff parttimeStaff = ParttimeStaff.getParttStaff(staffIDInput);

                    System.out.println("\nExisting staff :" + parttimeStaff.getName() + " 's gender is " + parttimeStaff.getGender() + "\n");

                    String newInput = ParttimeStaff.selectGender();

                    System.out.print("Staff's gender: " + parttimeStaff.getGender() + " will be updated to " + newInput + "\n\nConfirm? y/n: ");
                    String confirm = Helper.readLine().toUpperCase();
                    if (confirm.equals("Y")) {
                        parttimeStaff.setIC(newInput);
                        ParttimeStaff.updateStaff(parttimeStaff);
                        System.out.println("Parttime Staff's gender changed successfully!");
                    } else {
                        System.out.println("Aborting");
                        Helper.pause();
                    }
                } else {
                    System.out.printf("StaffID (%s) does not exist!\n", staffIDInput);
                    Helper.pause();
                }
            } else {
                System.out.println("Invalid StaffID format\n");
            }
        }
    }

    private void changeStaffIC(String type) {
        Helper.printHeader("Admin > Staff: Update Staff Details > Update Parttime Staff IC Number");
        viewStaff("parttime");
        while (true) {
            System.out.print("Enter the Parttime Staff ID in order to change the account IC Number (Q to Quit): ");
            String staffIDInput = Helper.readLine().trim().toUpperCase();
            if (staffIDInput.equals("Q")) {
                break;
            }
            if (ParttimeStaff.checkParttimeStaffIDFormat(staffIDInput)) {
                if (ParttimeStaff.checkDuplicateParttimeStaffID(staffIDInput)) {
                    ParttimeStaff parttimeStaff = ParttimeStaff.getParttStaff(staffIDInput);

                    System.out.println("\nExisting staff " + parttimeStaff.getName() + " 's passport number is " + parttimeStaff.getIC() + "\n");

                    System.out.print("Enter new ic number: ");
                    String newInput = Helper.readLine().trim();

                    while (!ParttimeStaff.checkIC(newInput)) {
                        System.out.print("Please re-enter new ic number: ");
                        newInput = Helper.readLine().trim();
                    }

                    System.out.print("Staff's ic number: " + parttimeStaff.getIC() + " will be updated to " + newInput + "\n\nConfirm? y/n: ");
                    String confirm = Helper.readLine().toUpperCase();
                    if (confirm.equals("Y")) {
                        parttimeStaff.setIC(newInput);
                        ParttimeStaff.updateStaff(parttimeStaff);
                        System.out.println("Parttime Staff's ic number changed successfully!");
                    } else {
                        System.out.println("Aborting");
                        Helper.pause();
                    }
                } else {
                    System.out.printf("StaffID (%s) does not exist!\n", staffIDInput);
                    Helper.pause();
                }
            } else {
                System.out.println("Invalid StaffID format\n");
            }
        }
    }

    private void changeStaffIC() {
        Helper.printHeader("Admin > Staff: Update Staff Details > Update Staff IC Number");
        viewStaff("Fulltime");
        while (true) {
            System.out.print("Enter the Staff ID in order to change the account password (Q to Quit): ");
            String staffIDInput = Helper.readLine().trim().toUpperCase();
            if (staffIDInput.equals("Q")) {
                break;
            }
            if (FulltimeStaff.checkFulltimeStaffIDFormat(staffIDInput)) {
                if (FulltimeStaff.checkDuplicateFulltimeStaffID(staffIDInput)) {
                    FulltimeStaff fulltimeStaff = FulltimeStaff.getFulltStaff(staffIDInput);

                    System.out.println("\nExisting staff " + fulltimeStaff.getName() + " 's IC number is " + fulltimeStaff.getIC() + "\n");

                    System.out.print("Enter new staff IC number: ");
                    String newInput = Helper.readLine().trim();

                    while (!FulltimeStaff.checkIC(newInput)) {
                        System.out.print("Please re-enter new staff IC number: ");
                        newInput = Helper.readLine().trim();
                    }

                    System.out.print("Staff's IC number: " + fulltimeStaff.getIC() + " will be updated to " + newInput + "\n\nConfirm? y/n: ");
                    String confirm = Helper.readLine().toUpperCase();
                    if (confirm.equals("Y")) {
                        fulltimeStaff.setIC(newInput);
                        FulltimeStaff.updateStaff(fulltimeStaff);
                        System.out.println("Fulltime Staff's IC changed successfully!");
                        Helper.pause();
                    } else {
                        System.out.println("Aborting");
                        Helper.pause();
                    }
                } else {
                    System.out.printf("StaffID (%s) does not exist!\n", staffIDInput);
                    Helper.pause();
                }
            } else {
                System.out.println("Invalid StaffID format\n");
            }
        }
    }

    private void changeStaffAccountPassword() {
        Helper.printHeader("Admin > Staff: Update Staff Details > Update Staff Account Password");
        viewStaff("Fulltime");
        while (true) {
            System.out.print("Enter the Staff ID in order to change the account password (Q to Quit): ");
            String staffIDInput = Helper.readLine().trim().toUpperCase();
            if (staffIDInput.equals("Q")) {
                break;
            }
            if (FulltimeStaff.checkFulltimeStaffIDFormat(staffIDInput)) {
                if (FulltimeStaff.checkDuplicateFulltimeStaffID(staffIDInput)) {
                    FulltimeStaff fulltimeStaff = FulltimeStaff.getFulltStaff(staffIDInput);
                    String actualPW = fulltimeStaff.getStaffPW();

                    System.out.println("\nExisting " + fulltimeStaff.getName() + " 's password is " + actualPW + "\n");

                    System.out.println("\n\tA password must have at least 7 characters.");
                    System.out.println("\tA password consists of only letters and digits.");
                    System.out.println("\tA password must contain at least one letter and at least one digit." + "\n");

                    System.out.print("Enter the new staff account password: ");
                    String newInput = Helper.readLine().trim();

                    while (!FulltimeStaff.checkStaffPassword(newInput)) {
                        System.out.print("\nPlease re-enter staff password: ");
                        newInput = Helper.readLine().trim();
                    }

                    System.out.print(fulltimeStaff.getName() + "'s old password will be updated to " + newInput + "\n\nConfirm? y/n: ");
                    String confirm = Helper.readLine().toUpperCase();
                    if (confirm.equals("Y")) {

                        fulltimeStaff.setStaffPW(newInput);
                        FulltimeStaff.updateStaff(fulltimeStaff);
                        System.out.println("Staff account password changed successfully!");
                    } else {
                        System.out.println("Aborting");
                        Helper.pause();
                    }
                } else {
                    System.out.printf("StaffID (%s) does not exist!\n", staffIDInput);
                    Helper.pause();
                }
            } else {
                System.out.println("Invalid StaffID format\n");
            }
        }
    }

    private void changeStaffAccountPassword(String type) {
        Helper.printHeader("Admin > Staff: Update Staff Details > Update Staff Account Password");
        viewStaff("Parttime");
        while (true) {
            System.out.print("Enter the Parttime Staff ID in order to change the account password (Q to Quit): ");
            String staffIDInput = Helper.readLine().trim().toUpperCase();
            if (staffIDInput.equals("Q")) {
                break;
            }
            if (ParttimeStaff.checkParttimeStaffIDFormat(staffIDInput)) {
                if (ParttimeStaff.checkDuplicateParttimeStaffID(staffIDInput)) {
                    ParttimeStaff parttimeStaff = ParttimeStaff.getParttStaff(staffIDInput);
                    String actualPW = parttimeStaff.getStaffPW();

                    System.out.println("\nExisting " + parttimeStaff.getName() + " 's password is " + actualPW + "\n");

                    System.out.println("\n\tA password must have at least 7 characters.");
                    System.out.println("\tA password consists of only letters and digits.");
                    System.out.println("\tA password must contain at least one letter and at least one digit." + "\n");

                    System.out.print("Enter the new parttime Staff account password: ");
                    String newInput = Helper.readLine().trim();

                    while (!ParttimeStaff.checkStaffPassword(newInput)) {
                        System.out.print("\nPlease re-enter staff password: ");
                        newInput = Helper.readLine().trim();
                    }

                    System.out.print(parttimeStaff.getName() + "'s old password will be updated to " + newInput + "\n\nConfirm? y/n: ");
                    String confirm = Helper.readLine().toUpperCase();
                    if (confirm.equals("Y")) {
                        parttimeStaff.setStaffPW(newInput);
                        ParttimeStaff.updateStaff(parttimeStaff);
                        System.out.println("Staff account password changed successfully!");
                    } else {
                        System.out.println("Aborting");
                        Helper.pause();
                    }
                } else {
                    System.out.printf("StaffID (%s) does not exist!\n", staffIDInput);
                    Helper.pause();
                }
            } else {
                System.out.println("Invalid StaffID format\n");
            }
        }
    }

    private void changeStaffName() {
        Helper.printHeader("Admin > Staff: Update Staff Details > Update Fulltime Staff Name");
        String newName, confirm;
        while (true) {
            viewStaff("Fulltime");
            System.out.print("Enter the Fulltime Staff ID in order to change the name (Q to Quit): ");
            String staffIDInput = Helper.readLine().toUpperCase().trim();
            if (staffIDInput.equals("Q")) {
                break;
            }
            if (FulltimeStaff.checkFulltimeStaffIDFormat(staffIDInput)) {
                if (FulltimeStaff.checkDuplicateFulltimeStaffID(staffIDInput)) {
                    FulltimeStaff fulltimeStaff = FulltimeStaff.getFulltStaff(staffIDInput);
                    System.out.println("Existing Staff Name is :" + fulltimeStaff.getName());
                    System.out.println("\n");
                    System.out.print("Enter New Staff Name: ");
                    newName = Helper.readLine().toUpperCase();
                    System.out.print("Existing staff name " + fulltimeStaff.getName() + " will be updated to " + newName + "\n\nConfirm? y/n: ");
                    confirm = Helper.readLine().toUpperCase();
                    if (confirm.equals("Y")) {
                        fulltimeStaff.setName(newName);
                        FulltimeStaff.updateStaff(fulltimeStaff);
                        System.out.println("Fulltime Staff Name Changed Successfully!\n");
                        Helper.systemSleep(1);
                    } else {
                        System.out.println("Aborting");
                        Helper.pause();
                    }
                } else {
                    System.out.printf("StaffID (%s) does not exist!\n", staffIDInput);
                    Helper.pause();
                }
            } else {
                System.out.println("Invalid StaffID format\n");
            }
        }
    }

    private void changeStaffName(String type) {
        Helper.printHeader("Admin > Staff: Update Staff Details > Update Parttime Staff Name");
        String newName, confirm;
        while (true) {
            viewStaff("Parttime");
            System.out.print("Enter the Parttime Staff ID in order to change the name (Q to Quit): ");
            String staffIDInput = Helper.readLine().toUpperCase().trim();
            if (staffIDInput.equals("Q")) {
                break;
            }
            if (ParttimeStaff.checkParttimeStaffIDFormat(staffIDInput)) {
                if (ParttimeStaff.checkDuplicateParttimeStaffID(staffIDInput)) {
                    ParttimeStaff parttimeStaff = ParttimeStaff.getParttStaff(staffIDInput);
                    System.out.println("Existing Staff Name is :" + parttimeStaff.getName());
                    System.out.println("\n");
                    System.out.print("Enter New Staff Name: ");
                    newName = Helper.readLine().toUpperCase();
                    System.out.print("Existing staff name " + parttimeStaff.getName() + " will be updated to " + newName + "\n\nConfirm? y/n: ");
                    confirm = Helper.readLine().toUpperCase();
                    if (confirm.equals("Y")) {
                        parttimeStaff.setName(newName);
                        ParttimeStaff.updateStaff(parttimeStaff);
                        System.out.println("ParttimeStaff Name Changed Successfully!\n");
                        Helper.systemSleep(1);
                    } else {
                        System.out.println("Aborting");
                        Helper.pause();
                    }
                } else {
                    System.out.printf("StaffID (%s) does not exist!\n", staffIDInput);
                    Helper.pause();
                }
            } else {
                System.out.println("Invalid StaffID format\n");
            }
        }
    }

    private void viewStaffDetails(Assignment mainApp) {
        Helper.printHeader("Admin > Staff: View StaffDetails ");
        while (true) {
            System.out.println();
            viewStaff();
            Helper.printLine(59);
            System.out.print("\nEnter the Staff ID in order to view the stffdetails (Q to Quit): ");
            String staffIDInput = Helper.readLine().toUpperCase().trim();
            if (staffIDInput.equals("Q")) {
                break;
            }
            try {
                if (staffIDInput.length() == 5 && FulltimeStaff.checkFulltimeStaffIDFormat(staffIDInput) && FulltimeStaff.checkDuplicateFulltimeStaffID(staffIDInput)) {
                    System.out.println("\nFulltime Staff Details:");

                    String query = "SELECT * FROM fulltimestaff WHERE fulltimeStaffID= ?";
                    pst = con.prepareStatement(query);
                    pst.setString(1, staffIDInput);
                    rs = pst.executeQuery();

                    while (rs.next()) {
                        System.out.printf("%-40s: %s\n", "Staff ID", rs.getString("FulltimeStaffID"));
                        System.out.printf("%-40s: %s\n", "Staff  Password", rs.getString("password"));
                        System.out.printf("%-40s: %s\n", "Staff Name", rs.getString("name"));
                        System.out.printf("%-40s: %s\n", "Staff IC", rs.getString("IC"));
                        System.out.printf("%-40s: %s\n", "Staff Gender", rs.getString("gender"));
                        System.out.printf("%-40s: %s\n", "Staff Contact Number", rs.getString("contactNumber"));
                        System.out.printf("%-40s: %s\n", "Staff Email Address", rs.getString("emailAddress"));
                        System.out.printf("%-40s: %s\n", "Staff DOB", rs.getString("DOB"));
                        System.out.printf("%-40s: %s\n", "Staff Account Status", rs.getString("accountStatus"));
                        System.out.printf("%-40s: %s\n", "Staff Staff Type", rs.getString("staffType"));
                    }
                    Helper.printLine(59);
                    Helper.pause();
                    Helper.printSmallSpace();
                } else if (staffIDInput.length() == 6 && ParttimeStaff.checkParttimeStaffIDFormat(staffIDInput) && ParttimeStaff.checkDuplicateParttimeStaffID(staffIDInput)) {
                    System.out.println("\nParttime Staff Details:");

                    String query = "SELECT * FROM parttimestaff WHERE parttimeStaffID=?";
                    pst = con.prepareStatement(query);
                    pst.setString(1, staffIDInput);
                    rs = pst.executeQuery();

                    while (rs.next()) {
                        System.out.printf("%-40s: %s\n", "Staff ID", rs.getString("parttimeStaffID"));
                        System.out.printf("%-40s: %s\n", "Staff Password", rs.getString("password"));
                        System.out.printf("%-40s: %s\n", "Staff Name", rs.getString("name"));
                        System.out.printf("%-40s: %s\n", "Staff IC", rs.getString("ICNumber"));
                        System.out.printf("%-40s: %s\n", "Staff Gender", rs.getString("gender"));
                        System.out.printf("%-40s: %s\n", "Staff Contact Number", rs.getString("contactNumber"));
                        System.out.printf("%-40s: %s\n", "Staff Email Address", rs.getString("emailAddress"));
                        System.out.printf("%-40s: %s\n", "Staff DOB", rs.getString("DOB"));
                        System.out.printf("%-40s: %s\n", "Staff Account Status", rs.getString("accountStatus"));
                        System.out.printf("%-40s: %s\n", "Staff Type", rs.getString("staffType"));
                    }
                    Helper.printLine(59);
                    Helper.pause();
                    Helper.printSmallSpace();
                } else {
                    System.out.println("Staff Record not found. Please double check the input.");
                }
            } catch (SQLException e) {
                e.getStackTrace();
            }
        }
    }

    private void viewStaff() {
        try {
            int count = 0;
            Helper.printLine(59);
            System.out.println(String.format("║%-20s ║ %-50s ║ %-12s", "Staff ID", "Staff Name", "Staff Status"));
            Helper.printLine(59);

            String query = "SELECT fulltimeStaffID, name, accountStatus FROM fulltimestaff order by accountStatus;";
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();

            Helper.printLine(59);
            System.out.println("Fulltime Staff List");
            Helper.printLine(59);

            while (rs.next()) {
                System.out.printf("║%-20s ║ %-50s ║ %-12s\n",
                        rs.getString("fulltimeStaffID"), rs.getString("name"), rs.getString("accountStatus"));
                count++;
                Helper.printLine(59);
            }

            Helper.printLine(59);
            System.out.println("Parttime Staff List");
            Helper.printLine(59);

            query = "SELECT parttimeStaffID, name, accountStatus FROM parttimestaff order by accountStatus;;";
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();

            while (rs.next()) {
                System.out.printf("║%-20s ║ %-50s ║ %-12s\n",
                        rs.getString("parttimeStaffID"), rs.getString("name"), rs.getString("accountStatus"));
                count++;
                Helper.printLine(59);
            }
            System.out.println("\nTotal Staff Count: " + count);
        } catch (SQLException e) {
            e.getStackTrace();
        }
    }

    private void viewStaff(String type) {
        int count = 0;
        try {
            if (type.equals("Fulltime")) {
                Helper.printLine(59);
                System.out.println(String.format("║%-20s ║ %-50s ║ %-12s", "Staff ID", "Staff Name", "Staff Status"));
                Helper.printLine(59);

                String query = "SELECT fulltimeStaffID, name, accountStatus FROM fulltimestaff order by accountStatus;";
                pst = con.prepareStatement(query);
                rs = pst.executeQuery();

                while (rs.next()) {
                    System.out.printf("║%-20s ║ %-50s ║ %-12s\n",
                            rs.getString("fulltimeStaffID"), rs.getString("name"), rs.getString("accountStatus"));
                    count++;
                    Helper.printLine(59);
                }
            } else {
                Helper.printLine(59);
                System.out.println(String.format("║%-20s ║ %-50s ║ %-12s", "Staff ID", "Staff Name", "Staff Status"));
                Helper.printLine(59);

                String query = "SELECT parttimeStaffID, name, accountStatus FROM parttimestaff order by accountStatus;";
                pst = con.prepareStatement(query);
                rs = pst.executeQuery();

                while (rs.next()) {
                    System.out.printf("║%-20s ║ %-50s ║ %-12s\n",
                            rs.getString("parttimeStaffID"), rs.getString("name"), rs.getString("accountStatus"));
                    count++;
                    Helper.printLine(59);
                }
            }
            System.out.println("\nTotal Staff Count: " + count);
        } catch (SQLException e) {
            e.getStackTrace();
        }
    }

    private void addStaff(Assignment mainApp) {
        String choice;
        Helper.printHeader("Admin > Staff: Type of Staff");
        viewStaff();
        while (true) {
            System.out.println("\n\nEnter staff type: ");
            System.out.println("[1] Fulltime Staff");
            System.out.println("[2] Parttime Staff");
            System.out.printf("%-50s: ", "Enter choice (Q to quit)");
            choice = Helper.readLine().toUpperCase().trim();
            switch (choice) {
                case "1":
                    addFulltimeStaff(mainApp);
                    break;
                case "2":
                    addParttimeStaff(mainApp);
                    break;
                case "Q":
                    return;
                default:
                    System.out.println("Please enter valid option.\n");
            }
        }
    }

    private void addFulltimeStaff(Assignment mainApp) {
        String staffName;
        String staffIC;
        String staffGender;
        String staffContactNumber;
        String staffEmailAddress;
        String staffDOB;
        String staffStatus = "Active";
        String staffPW;
        String staffType;
        String[] type = {"FullTime", "PartTime"};

        Helper.printHeader("Admin > Staff: Add Fulltime Staff Details");
        while (true) {
            viewStaff("Fulltime");
            Helper.printLine(59);
            System.out.printf("%-50s: ", "\nEnter new staff ID Eg:FT001 (Q to quit)");
            String staffIDInput = Helper.readLine().toUpperCase().trim();
            if (staffIDInput.equals("Q")) {
                break;
            }
            if (FulltimeStaff.checkFulltimeStaffIDFormat(staffIDInput)) {
                if (!FulltimeStaff.checkDuplicateFulltimeStaffID(staffIDInput)) {
                    try {
                        while (true) {
                            System.out.printf("%-50s: ", "Enter Staff name");
                            staffName = Helper.readLine().toUpperCase();
                            if (staffName.length() <= 50 && FulltimeStaff.checkNameFormat(staffName)) {
                                break;
                            }
                        }

                        while (true) {
                            System.out.printf("%-50s: ", "Enter Staff IC     eg:999999-99-9999");
                            staffIC = Helper.readLine().trim();
                            if (FulltimeStaff.checkIC(staffIC)) {
                                break;
                            }
                        }

                        staffGender = FulltimeStaff.selectGender();

                        while (true) {
                            try {
                                System.out.printf("%-50s: ", "Enter staff contact number eg: 011-9999999");
                                staffContactNumber = Helper.readLine().trim();
                                if (FulltimeStaff.checkContactNumber(staffContactNumber)) {
                                    break;
                                }
                            } catch (Exception e) {
                                System.out.println("Invalid phone number length. Do try again.");
                            }
                        }

                        while (true) {
                            System.out.printf("%-50s: ", "Enter Staff date of birth   eg:dd/mm/yyyy");
                            staffDOB = Helper.readLine().trim();
                            if (FulltimeStaff.checkDOB(staffDOB)) {
                                break;
                            }
                        }

                        while (true) {
                            System.out.printf("%-50s: ", "Enter Staff email address");
                            staffEmailAddress = Helper.readLine().trim();
                            if (staffEmailAddress.length() <= 50 && FulltimeStaff.checkEmailAddress(staffEmailAddress)) {
                                break;
                            }
                        }

                        while (true) {
                            try {
                                System.out.println("Type list option: ");
                                for (int i = 0; i < type.length; i++) {
                                    System.out.print("\t\t\t\t\t\t" + (i + 1) + ". " + type[i] + "\n");
                                }
                                System.out.printf("%-50s: ", "Enter staff type option");
                                int selection = Integer.parseInt(Helper.readLine()) - 1;

                                if (selection < 0 || selection > type.length) {
                                    throw new Exception();
                                }
                                staffType = type[selection];
                                break;
                            } catch (Exception e) {
                                System.out.println("Please enter digit only.");
                            }
                        }

                        Helper.printSmallSpace();

                        System.out.printf("%-50s ", "Staff Password will be same the as staff ID for the first time registration: " + staffIDInput);
                        staffPW = staffIDInput;
                        System.out.println();
                        Helper.systemSleep(1);

                        System.out.println("\n\nNew Staff Details: ");
                        System.out.printf("%-40s: %s\n", "Staff ID: ", staffIDInput);
                        System.out.printf("%-40s: %s\n", "Staff Password: ", staffPW);
                        System.out.printf("%-40s: %s\n", "StaffName: ", staffName);
                        System.out.printf("%-40s: %s\n", "Staff IC: ", staffIC);
                        System.out.printf("%-40s: %s\n", "Staff Gender: ", staffGender);
                        System.out.printf("%-40s: %s\n", "Staff Contact Number: ", staffContactNumber);
                        System.out.printf("%-40s: %s\n", "Staff Email Address: ", staffEmailAddress);
                        System.out.printf("%-40s: %s\n", "Staff DOB: ", staffDOB);
                        System.out.printf("%-40s: %s\n", "Staff Type: ", staffType);
                        System.out.printf("%-40s: %s\n", "Staff Account Status: ", staffStatus);
                        Helper.printLine(59);
                        System.out.printf("%-40s: ", "Are you confirm to Save? y/n");
                        String confirm = Helper.readLine().toUpperCase();

                        if (confirm.equals("Y")) {
                            FulltimeStaff fulltimeStaff = new FulltimeStaff(staffIDInput, staffPW, staffName, staffIC, staffGender, staffContactNumber,
                                    staffEmailAddress, staffDOB, staffStatus, staffType);

                            FulltimeStaff.addFulltimeStaff(fulltimeStaff);
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
                    System.out.printf("StaffID %s already exist!\n", staffIDInput);
                }
            }
        }
    }

    private void addParttimeStaff(Assignment mainApp) {
        String staffName;
        String staffIC;
        String staffGender;
        String staffContactNumber;
        String staffEmailAddress;
        String staffDOB;
        String staffStatus = "Active";
        String staffPW;
        String staffType;
        String[] type = {"FullTime", "PartTime"};

        Helper.printHeader("Admin > Staff: Add Parttime Staff Details");
        while (true) {
            viewStaff("parttimestaff");
            Helper.printLine(59);
            System.out.printf("%-50s: ", "\nEnter new staff ID Eg:PT0001 (Q to quit)");
            String staffIDInput = Helper.readLine().toUpperCase().trim();
            if (staffIDInput.equals("Q")) {
                break;
            }
            if (ParttimeStaff.checkParttimeStaffIDFormat(staffIDInput)) {
                if (!ParttimeStaff.checkDuplicateParttimeStaffID(staffIDInput)) {
                    try {
                        while (true) {
                            System.out.printf("%-50s: ", "Enter Staff name");
                            staffName = Helper.readLine().toUpperCase();
                            if (staffName.length() <= 50 && ParttimeStaff.checkNameFormat(staffName)) {
                                break;
                            }
                        }

                        while (true) {
                            System.out.printf("%-50s: ", "Enter Staff IC Number     eg:010402-99-1211");
                            staffIC = Helper.readLine().trim();
                            if (ParttimeStaff.checkIC(staffIC)) {
                                break;
                            }
                        }

                        staffGender = ParttimeStaff.selectGender();

                        while (true) {
                            try {
                                System.out.printf("%-50s: ", "Enter staff contact number eg: 011-9999999");
                                staffContactNumber = Helper.readLine().trim();
                                if (ParttimeStaff.checkContactNumber(staffContactNumber)) {
                                    break;
                                }
                            } catch (Exception e) {
                                System.out.println("Invalid phone number length. Do try again.");
                            }
                        }

                        while (true) {
                            System.out.printf("%-50s: ", "Enter Staff date of birth   eg:dd-mm-yyyy");
                            staffDOB = Helper.readLine().trim();
                            if (ParttimeStaff.checkDOB(staffDOB)) {
                                break;
                            }
                        }

                        while (true) {
                            System.out.printf("%-50s: ", "Enter Staff email address");
                            staffEmailAddress = Helper.readLine().trim();
                            if (staffEmailAddress.length() <= 50 && ParttimeStaff.checkEmailAddress(staffEmailAddress)) {
                                break;
                            }
                        }

                        while (true) {
                            try {
                                System.out.println("Type list option: ");
                                for (int i = 0; i < type.length; i++) {
                                    System.out.print("\t\t\t\t\t\t" + (i + 1) + ". " + type[i] + "\n");
                                }
                                System.out.printf("%-50s: ", "Enter staff type option");
                                int selection = Integer.parseInt(Helper.readLine()) - 1;

                                if (selection < 0 || selection > type.length) {
                                    throw new Exception();
                                }
                                staffType = type[selection];
                                break;
                            } catch (Exception e) {
                                System.out.println("Please enter digit only.");
                            }
                        }

                        Helper.printSmallSpace();

                        System.out.printf("%-50s ", "Student Password will be the same as student ID for the first time registration: " + staffIDInput);
                        staffPW = staffIDInput;
                        System.out.println();
                        Helper.systemSleep(1);

                        System.out.println("\n\nNew Staff Details: ");
                        System.out.printf("%-40s: %s\n", "Staff ID: ", staffIDInput);
                        System.out.printf("%-40s: %s\n", "Staff Password: ", staffPW);
                        System.out.printf("%-40s: %s\n", "Staff Name: ", staffName);
                        System.out.printf("%-40s: %s\n", "Staff IC: ", staffIC);
                        System.out.printf("%-40s: %s\n", "Staff Gender: ", staffGender);
                        System.out.printf("%-40s: %s\n", "Staff Contact Number: ", staffContactNumber);
                        System.out.printf("%-40s: %s\n", "Staff Email Address: ", staffEmailAddress);
                        System.out.printf("%-40s: %s\n", "Staff DOB: ", staffDOB);
                        System.out.printf("%-40s: %s\n", "Staff Account Status: ", staffStatus);
                        System.out.printf("%-40s: %s\n", "Staff Type: ", staffType);
                        Helper.printLine(59);
                        System.out.printf("%-40s: ", "Are you confirm to Save? y/n");
                        String confirm = Helper.readLine().toUpperCase();

                        if (confirm.equals("Y")) {
                            ParttimeStaff parttimeStaff = new ParttimeStaff(staffIDInput, staffPW, staffName, staffIC, staffGender, staffContactNumber,
                                    staffEmailAddress, staffDOB, staffStatus, staffType);

                            ParttimeStaff.addParttimeStaff(parttimeStaff);
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
                    System.out.printf("StaffID %s already exist!\n", staffIDInput);
                }
            }
        }
    }
}
