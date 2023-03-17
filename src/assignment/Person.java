package assignment;

abstract class Person {
    protected String name;
    protected String IC;
    protected String gender;
    protected String contactNumber;
    protected String emailAddress;
    protected String DOB;
    protected String accountStatus;

    public Person(){
    }

    public Person (String name,String IC, String gender, String contactNumber, String emailAddress, String DOB, String accountStatus){
        this.name = name;
        this.IC = IC;
        this.gender = gender;
        this.contactNumber = contactNumber;
        this.emailAddress = emailAddress;
        this.DOB = DOB;
        this.accountStatus = accountStatus;
    }

    //***********Getter************
    public String getName() {
        return name;
    }
    public String getIC() {
        return IC;
    }
    public String getGender() {
        return gender;
    }
    public String getContactNumber() {
        return contactNumber;
    }
    public String getEmailAddress(){
        return emailAddress;
    }
    public String getDOB() {
        return DOB;
    }
    public String getAccountStatus() {
        return accountStatus;
    }
    //***********Setter************
    public void setName(String name) {
        this.name = name;
    }
    public void setIC(String IC) {
        this.IC = IC;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
    public void setDOB(String DOB) {
        this.DOB = DOB;
    }
    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }
    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", IC='" + IC + '\'' +
                ", gender='" + gender + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", DOB='" + DOB + '\'' +
                ", accountStatus='" + accountStatus + '\'' +
                '}';
    }

    //Input Validation
    protected static boolean checkNameFormat(String name){
        if (name.matches("^[a-zA-Z\\s]*$")) {
            return true;
        }
        System.out.println("Invalid input. No symbols or digits is allowed.");
        return false;
    }

    protected static boolean checkIC(String IC) {
        if (IC.matches("^\\d{6}-?\\d{2}-?\\d{4}-?$")) {
            return true;
        }else {
            System.out.println("Invalid input. Please check your input. eg:999999-99-9999.");
            return false;
        }
    }

    protected static boolean checkPassport(String IC) {
        if (IC.matches("^[A-Z0-9]+$") && IC.length() < 12 ) {
            return true;
        }else {
            System.out.println("Invalid input. Please check your input. eg:999999-99-9999.");
            return false;
        }
    }

    protected static String selectGender() {
        int option;
        System.out.println("Gender :");
        System.out.println("\t\t[1] Male");
        System.out.println("\t\t[2] Female");
        do {
            try{
                System.out.printf("%-50s: ", "Choice: ");
                option = Integer.parseInt(Helper.readLine().trim());
                switch (option) {
                case 1:
                    return "Male";
                case 2:
                    return "Female";
                default:
                    System.out.println("Please enter a valid number.");
                }
            } catch (Exception e) {
                System.out.println("Incorrect input. Please enter digits 1 - 2 only.");
            }
        } while (true);
    }

    protected static boolean checkContactNumber(String contactNumber) {
        if (contactNumber.matches("^\\d{3}-?\\d{7}-?$") || contactNumber.matches("^\\d{3}-?\\d{8}-?$")) {
            return true;
        }else {
            System.out.println("Invalid input. Please double check your number input.");
            return false;
        }
    }

    protected static boolean checkEmailAddress(String emailAddress) {
        if (emailAddress.matches("^[a-zA-Z0-9\\w.]+[@]+[a-zA-Z0-9\\w]+[.]+[a-zA-Z0-9\\w]*$")) {
            return true;
        }
        System.out.println("Invalid input. Please double check your email address input.");
        return false;
    }

    protected static boolean checkDOB(String DOB) {
        if (DOB.matches("^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[13-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$")) {
            return true;
        }
        System.out.println("Invalid input. Please double check your DOB input.");
        return false;
    }

    protected static String selectAccountStatus() {
        int option;
        System.out.println("Account Status");
        System.out.println("[1] Activate");
        System.out.println("[2] Deactivate");
        do {
            try{
                System.out.printf("%-50s: ", "Choice: ");
                option = Integer.parseInt(Helper.readLine().trim());
                switch (option) {
                    case 1:
                        return "Active";
                    case 2:
                        return "Inactive";
                    default:
                        System.out.println("Please enter a valid number.");}
            }catch (Exception e) {
                System.out.println("Incorrect input. Please enter digits 1 - 2 only.");
            }
        } while (true);
    }
}
