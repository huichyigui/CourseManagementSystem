package assignment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

abstract class Payment {

    protected String referenceNo;
    protected String studentID;
    protected LocalDate date;
    protected String item;
    protected double price;
    protected double paid;
    protected double balance;

    public Payment(Double price, String item, String studentID) {
        Connection con = Assignment.getConnection();
        ResultSet rs = null;
        PreparedStatement pst = null;
        int count = 0;
        try {
            String query = "SELECT COUNT(referenceNo) FROM paymentRecord";
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();
            while (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
        }
        this.referenceNo = String.format("P%06d", count + 1);
        this.studentID = studentID;
        this.price = price;
        date = LocalDate.now();
        this.item = item;
        this.studentID = studentID;
        this.paid = 0;
        this.balance = 0;

    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public String getStudentID() {
        return studentID;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getPaid() {
        return paid;
    }

    public void setPaid(double paid) {
        this.paid = paid;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    abstract void makePayment();

    abstract void getInvoice();

    public static void addPaymentRecord(Payment payment) {
        Connection con = Assignment.getConnection();

        try {
            String query = "INSERT INTO paymentRecord VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(query);

            pst.setString(1, payment.getReferenceNo());
            pst.setString(2, payment.getStudentID());
            pst.setString(3, payment.getDate().toString());
            pst.setString(4, payment.getItem());
            pst.setDouble(5, payment.getPrice());
            pst.setDouble(6, payment.getPaid());
            pst.setDouble(7, payment.getBalance());

            pst.executeUpdate();

            System.out.println("Payment record added successfully.");
            con.close();
        } catch (SQLException e) {
            e.getStackTrace();
        }
    }
}
