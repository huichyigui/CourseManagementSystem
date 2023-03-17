package assignment;

abstract class Student extends Person{
    protected final int MAXCREDITHOUR = 20;

    protected String studentID;
    protected String studentPW;
    protected String studentProgramme;
    protected String studyMode;

    public Student(){};

    public Student(String studentID, String studentPW, String name, String IC, String gender, String contactNumber, String emailAddress, String DOB,
                   String accountStatus, String studentProgramme, String studyMode){

        super(name, IC, gender, contactNumber,emailAddress, DOB, accountStatus);
        this.studentProgramme = studentProgramme;
        this.studentID = studentID;
        this.studentPW = studentPW;
        this.studyMode = studyMode;
    }

    //***********Getter************
    public int getMAXCREDITHOUR() {
        return MAXCREDITHOUR;
    }
    public String getStudentID() {
        return studentID;
    }
    public String getStudentPW() {
        return studentPW;
    }
    public String getStudentProgramme() {
        return studentProgramme;
    }
    public String getStudyMode() {
        return studyMode;
    }
    //***********Setter************
    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }
    public void setStudentPW(String studentPW) {
        this.studentPW = studentPW;
    }
    public void setStudentProgramme(String studentProgramme) {
        this.studentProgramme = studentProgramme;
    }
    public void setStudyMode(String studyMode) {
        this.studyMode = studyMode;
    }
    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", IC='" + IC + '\'' +
                ", gender='" + gender + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", DOB='" + DOB + '\'' +
                ", accountStatus='" + accountStatus + '\'' +
                ", studentID='" + studentID + '\'' +
                ", studentPW='" + studentPW + '\'' +
                ", studentProgramme='" + studentProgramme + '\'' +
                ", studyMode='" + studyMode + '\'' +
                '}';
    }

    //Input Validation
    protected static boolean checkLocalStudentIDFormat(String studentID) {
        if (studentID.matches("\\d{2}\\p{Upper}{3}\\d{5}") || studentID.equals("Q")) {
            return true;
        }
        System.out.println("Invalid input. Student ID has 2 digits, followed by 3 alphabets and 5 digits, eg = 21WMR00001");
        return false;
    }

    protected static boolean checkInterStudentIDFormat(String studentID) {
        if (studentID.matches("\\d{2}\\p{Upper}{4}\\d{5}") || studentID.equals("Q")) {
            return true;
        }
        System.out.println("Invalid input. Student ID has 2 digits, followed by  4 alphabets for international student and 5 digits, eg = 21IWMR00001");
        return false;
    }

    protected static boolean checkStudentPassword(String studentPW) {
        boolean valid = true;
        int countLetter = 0;
        int countDigit = 0;

        if (studentPW.length() < 7){
            valid = false;
        } else {
            for (int i = 0; i < studentPW.length() && valid; i++){
                char ch = studentPW.charAt(i);

                if(Character.isLetter(ch)){
                    countLetter++;
                } else if (Character.isDigit(ch)){
                    countDigit++;
                } else {
                    valid = false;
                }
            }

            if (countDigit == 0 || countLetter == 0){
                valid = false;
            }
        }

        if(valid){
            return true;
        } else {
            System.out.println("Invalid password input. Please check your input.");
            return false;
        }
    }

    protected static String selectProgramme() {
        int option;
        System.out.println("Programme :");
        System.out.println("[1] RMM");
        System.out.println("[2] RSD");
        System.out.println("[3] REI");
        do {
            try{
                System.out.printf("%-50s: ", "Choice: ");
                option = Integer.parseInt(Helper.readLine().trim());
                switch (option) {
                    case 1:
                        return "RMM";
                    case 2:
                        return "RSD";
                    case 3:
                        return "REI";
                    default:
                        System.out.println("Please enter a valid number.");
                }
            } catch (Exception e) {
                System.out.println("Incorrect input. Please enter digits 1 - 3 only.");
            }
        } while (true);
    }

    protected static String selectStudyMode() {
        int option;
        System.out.println("Study Mode :");
        System.out.println("\t\t[1] Full-time");
        System.out.println("\t\t[2] Part-time");
        do {
            try{
                System.out.printf("%-50s: ", "Choice: ");
                option = Integer.parseInt(Helper.readLine().trim());
                switch (option) {
                    case 1:
                        return "Full-time";
                    case 2:
                        return "Part-time";
                    default:
                        System.out.println("Please enter a valid number.");}
            }catch (Exception e) {
                System.out.println("Incorrect input. Please enter digits 1 - 3 only.");
            }
        } while (true);
    }
}
