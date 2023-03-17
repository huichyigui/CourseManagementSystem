package assignment;

//Capture user input
import java.util.Scanner;

//For delay purpose
import java.util.concurrent.*;

//For AES Encryption
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
//import java.util.Base64;
//import java.util.Base64.Decoder;
//import java.util.Base64.Encoder;
import sun.misc.BASE64Decoder; //for older Java version
import sun.misc.BASE64Encoder;

//For sending email
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

//For generate student graph
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.Rotation;
import javax.swing.*;

public class Helper extends JFrame{
    public static Scanner sc = new Scanner(System.in);

    public static void pause() {
        System.out.println("Press <Enter> to continue... ");
        sc.nextLine();
    }

    public static String readLine() {
        return sc.nextLine();
    }

    public static String readTime() throws Exception {
        String time = sc.nextLine();

        if (!time.matches("\\p{Digit}{4}$")) {
            throw new Exception();
        }
        return time;
    }

    public static void printSmallSpace() {
        for (int i = 0; i < 3; i++) {
            System.out.println();
        }
    }

    public static void printMediumSpace() {
        for (int i = 0; i < 5; i++) {
            System.out.println();
        }
    }

    public static void printLargeSpace() {
        for (int i = 0; i < 7; i++) {
            System.out.println();
        }
    }

    public static void printLine(int size) {
        String line = String.format("%" + size + "s", "").replace(" ", "═");
        System.out.println(line);
    }

    public static void printHeader(String directory) {
        Helper.printLargeSpace();
        String spaces = String.format("%" + (112 - directory.length()) + "s", "");
        System.out.println("╔════════════════════════════════════════════════════════════════════════╗");
        System.out.printf("║%113c║", ' ');
        System.out.printf("\n║%52cWelcome to%51c║", ' ', ' ');
        System.out.printf("\n║%113c║", ' ');
        System.out.printf("\n║%31c           _   _     __      _______  _____ _   _ %32c║", ' ', ' ');
        System.out.printf("\n║%31c     /\\   | \\ | |   /\\ \\    / /  __ \\|_   _| \\ | |%32c║", ' ', ' ');
        System.out.printf("\n║%31c    /  \\  |  \\| |  /  \\ \\  / /| |__) | | | |  \\| |%32c║", ' ', ' ');
        System.out.printf("\n║%31c   / /\\ \\ | . ` | / /\\ \\ \\/ / |  _  /  | | | . ` |%32c║", ' ', ' ');
        System.out.printf("\n║%31c  / ____ \\| |\\  |/ ____ \\  /  | | \\ \\ _| |_| |\\  |%32c║", ' ', ' ');
        System.out.printf("\n║%31c /_/    \\_\\_| \\_/_/    \\_\\/   |_|  \\_\\_____|_| \\_|%32c║", ' ', ' ');
        System.out.printf("\n║%113c║", ' ');
        System.out.printf("\n║%41cAnavrin College Enrollment System%39c║", ' ', ' ');
        System.out.printf("\n║%113c║", ' ');
        System.out.println("\n╚════════════════════════════════════════════════════════════════════════╝");

        System.out.println("╔════════════════════════════════════════════════════════════════════════╗");
        System.out.printf("║ %s%s║", directory, spaces);
        System.out.println("\n╚════════════════════════════════════════════════════════════════════════╝");
    }

    //**************System Delay******************
    public static void systemSleep(int sleepTime){
        TimeUnit time = TimeUnit.SECONDS;
        try {
            time.sleep(sleepTime);
        } catch (InterruptedException e) {
            System.out.println("Interrupted Exception Caught"+ e);
        }
    }
    //**************End of System Delay Method****

    //**************AESEncryption*****************
    private static final String ALGO = "AES";
    private byte[] keyValue;

    public Helper(){
        String key = "lv39eptvulhaqqsr";
        keyValue = key.getBytes();
    }

    private Key generateKey() throws Exception{
        Key key = new SecretKeySpec(keyValue, ALGO);
        return key;
    }

    public String encrypt(String Data) throws Exception{
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(Data.getBytes());
        String encryptedValue = new BASE64Encoder().encode(encVal);
        return encryptedValue;
    }

    public String decrypt(String encryptedData) throws Exception{
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decorderValue = new BASE64Decoder().decodeBuffer(encryptedData);
        byte[] decValue = c.doFinal(decorderValue);
        String decryptedValue = new String(decValue);
        return decryptedValue;
    }
    //**************End of AESEncryption Method******

    //**************Send Email***********************
    public static void sendMail(String recipient, String username, String password, String studentName) throws Exception{
        System.out.println("Sending email, please wait.");
        Properties properties = new Properties();

        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        String myAccountEmail = "dummyliciousx@gmail.com";
        String emailPassword = "Dummy123!!";

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication(myAccountEmail, emailPassword);
            }
        });

        Message message = prepareMessage(session,myAccountEmail, recipient, username, password, studentName);

        Transport.send(message);

        System.out.println("\nEmail successfully sent!");
    }

    private static Message prepareMessage(Session session, String myAccountEmail, String recipient, String username, String password, String studentName){
        try{
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myAccountEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject("Anavrin Enrollment System: Student Login Credentials");
            message.setText("Greetings, " + studentName + "\n\nAttached your login credentials" + "\n\nStudent Login ID: " + username + "\nStudent Login Password: " + password
            + "\n\nPlease don't hesitate to contact us for more enquiries." + "\n\nThank You!");
            return message;
        } catch (Exception ex){
            Logger.getLogger(Helper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    //**************End of Send Email Method********

    //**************Display Graph Method************
    public Helper(String appTitle, String chartTitle, String variable1, double variable1num, String variable2, double variable2num){

        PieDataset dataset = createDataset(variable1, (int) variable1num, variable2, (int) variable2num);
        JFreeChart chart = createPieChart(dataset, chartTitle);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 300));
        setContentPane(chartPanel);
    }


    private PieDataset createDataset(String variable1, int variable1num, String variable2, int variable2num) {

        DefaultPieDataset result = new DefaultPieDataset();
        result.setValue(variable1, variable1num);
        result.setValue(variable2, variable2num);
        return result;
    }

    private JFreeChart createPieChart(PieDataset dataset, String title){

        JFreeChart chart = ChartFactory.createPieChart3D(title, dataset, true, true, false);
        PiePlot3D plot = (PiePlot3D) chart.getPlot();
        plot.setStartAngle(0);
        plot.setDirection(Rotation.CLOCKWISE);
        plot.setForegroundAlpha(0.5f);
        return chart;
    }
    //***********End of Display Graph Method********

}
