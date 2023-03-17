package assignment;

import java.io.FileWriter;
import java.io.IOException;

public class CreditPayment extends Payment {

    public CreditPayment(Double price, String item, String studentID) {
        super(price, item, studentID);
    }

    @Override
    public void makePayment() {
        System.out.println("        Tuition Payment        ");
        System.out.println("===============================");
        System.out.printf("%20s %10.2f\n\n\n", item, price);
        System.out.printf("Enter card Number: ");
        String cardno = "";
        while (cardno.length() != 10) {
            cardno = Helper.readLine();
            if (cardno.length() != 10) {
                System.out.println("Please Enter valid card no (10 digit)..");
            } else {
                paid = price;
                balance = paid - price;
            }
        }
        System.out.printf("Payment Successful!");
        Helper.pause();
        this.getInvoice();
    }

    @Override
    void getInvoice() {
        System.out.printf("========================\n");
        System.out.printf("█ █▄░█ █░█ █▀█ █ █▀▀ █▀▀\n");
        System.out.printf("█ █░▀█ ▀▄▀ █▄█ █ █▄▄ ██▄\n");
        System.out.printf("%24s\n", date.toString());
        System.out.printf("========================\n");
        System.out.printf("ITEM               FEEs \n");
        System.out.printf("====               ==== \n");
        System.out.printf("%-14s  %-4.2f\n", item, price);
        System.out.printf("\n\n\n");
        System.out.printf("========================\n");
        System.out.printf("%-14s  %4.2f\n", "Paid", paid);
        System.out.printf("%-14s  %4s\n", "Method", "Card");
        System.out.printf("%-14s  %4.2f\n", "Change", balance);
        Helper.pause();

        while (true) {
            System.out.println("save Invoice to file? (y/n):");
            String choice = Helper.readLine().toUpperCase();
            if (choice.equals("Y")) {
                try {
                    FileWriter fw = new FileWriter("filename.txt_" + this.referenceNo + ".txt");
                    fw.write(String.format("========================\n"));
                    fw.write(String.format("█ █▄░█ █░█ █▀█ █ █▀▀ █▀▀\n"));
                    fw.write(String.format("█ █░▀█ ▀▄▀ █▄█ █ █▄▄ ██▄\n"));
                    fw.write(String.format("%24s\n", date.toString()));
                    fw.write(String.format("========================\n"));
                    fw.write(String.format("ITEM               FEEs \n"));
                    fw.write(String.format("====               ==== \n"));
                    fw.write(String.format("%-14s  %-4.2f\n", item, price));
                    fw.write(String.format("\n\n\n"));
                    fw.write(String.format("========================\n"));
                    fw.write(String.format("%-14s  %4.2f\n", "Paid", paid));
                    fw.write(String.format("%-14s  %4s\n", "Method", "Card"));
                    fw.write(String.format("%-14s  %4.2f\n", "Change", balance));
                    fw.close();
                    System.out.println("Saved to file.");
                    return;
                } catch (IOException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }

            } else if (choice.equals("N")) {
                System.out.println("Payment Complete");
                Helper.pause();
                return;
            }
        }

    }
}
