/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment;

/**
 *
 * @author elfhc
 */
public abstract class Course {

    private String courseCode;
    private String courseName;
    private String description;
    private int creditHours;
    private double fee;
    private String status;

    protected Course() {
    }

    protected Course(String courseName, String description, double fee) {
        this.courseName = courseName;
        this.description = description;
        this.fee = fee;
        this.creditHours = 2;
        this.status = "AVAILABLE";
    }

    protected Course(String courseCode, String courseName, String description, int creditHours, double fee) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.description = description;
        this.creditHours = creditHours;
        this.fee = fee;
        this.status = "AVAILABLE";
    }

    protected Course(String courseCode, String courseName, String description, int creditHours, double fee, String status) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.description = description;
        this.creditHours = creditHours;
        this.fee = fee;
        this.status = status;
    }

    public String getCourseCode() {
        return this.courseCode;
    }

    public String getCourseName() {
        return this.courseName;
    }

    public String getDescription() {
        return this.description;
    }

    public int getCreditHours() {
        return this.creditHours;
    }

    public double getFee() {
        return this.fee;
    }

    public String getStatus() {
        return this.status;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreditHours(int creditHours) {
        this.creditHours = creditHours;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static String chooseStatus() {
        String option;
        System.out.println("Status");
        System.out.println("[1] Activate");
        System.out.println("[2] Deactivate");
        do {
            System.out.printf("%-50s: ", "Update Status:");
            option = Helper.readLine().trim();
            switch (option) {
                case "1":
                    return "AVAILABLE";
                case "2":
                    return "UNAVAILABLE";
                default:
                    System.out.println("Please enter a valid number.");
            }
        } while (true);
    }

    @Override
    public String toString() {
        return String.format("%12.12s ║ %-30s ║ %-70s ║ %-12s ║ %-15.2f ║ %-12s", 
                courseCode, courseName, description, creditHours, fee, status);
    }
}
