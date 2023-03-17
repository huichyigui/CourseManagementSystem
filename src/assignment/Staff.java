/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment;

abstract class Staff extends Person {
    
    protected String staffID;
    protected String staffPW;
    protected String staffType;

    public Staff(){};
    
    public Staff(String staffID , String staffPW , String name , String IC , String gender , String contactNumber , String emailAddress , String DOB , String accountStatus , String staffType){
        
        super(name , IC , gender , contactNumber , emailAddress , DOB , accountStatus);
        this.staffID = staffID;
        this.staffPW = staffPW;
        this.staffType = staffType;
    }
    
    // Get 
    public String getStaffID(){
        return staffID;
    }
    
    public String getStaffPW(){
        return staffPW;
    }
    
    public String getStaffType(){
        return staffType;
    }
    
    
    //Set
    public void setStaffID(String staffID ){
        this.staffID = staffID;
    }
    public void setStaffPW(String staffPW){
        this.staffPW = staffPW;
    }
    
    public void setStaffType(String staffType){
        this.staffType = staffType;
    }
    
    @Override
    public String toString(){
        return "Staff{" +
                "name ='" + name +'\'' +
                ".IC ='" + IC +'\'' +
                ",gender = '" + gender + '\'' +
                ",contactNumber = '" + contactNumber + '\'' +
                ",emailAddress = '" + emailAddress + '\'' +
                ",DOB = '" + DOB +'\'' +
                ",accStatus = '" + accountStatus + '\'' +
                ",staffID = '" + staffID + '\'' +
                ",staffPW = '" + staffPW + '\'' +
                ",staffType = '" + staffType + '\'' +
                '}';
    }
    
    //Validation for INput
    protected static boolean checkFulltimeStaffIDFormat(String staffID) {
        if (staffID.matches("\\p{Upper}{2}\\d{3}") || staffID.equals("Q")) {
            return true;
        }
        System.out.println("Invalid input. Staff ID should have 2 alphabets and 3 digits , Eg = FT001");
        return false;
        }
    
    protected static boolean checkParttimeStaffIDFormat(String staffID) {
        if (staffID.matches("\\p{Upper}{2}\\d{4}") || staffID.equals("Q")) {
            return true;
        }
        System.out.println("Invalid input. Staff ID should have 2 alphabets and 4 digits , Eg = PT0001");
        return false;
        }
    
    protected static boolean checkStaffPassword(String staffPW){
        boolean valid = true ;
        int countLetter = 0;
        int countDigit = 0;
        
        if (staffPW.length() < 5){
            valid = false;
        }else {
            for (int i=0; i <staffPW.length() && valid; i++){
                char ch = staffPW.charAt(i);
                
                if(Character.isLetter(ch)){
                    countLetter++;
                } else if (Character.isDigit(ch)){
                    countDigit++;
                }else {
                    valid = false;
                }
            }
            
            if (countDigit == 0 || countLetter == 0){
                valid = false;
            }
        }
        
        if(valid){
            return true ;
        } else {
            System.out.println("Invalid password input. Please check your input.");
            return false;
        }
    
    }
    
    protected static String selectStaffType() {
        int option;
        System.out.println("Study Mode :");
        System.out.println("\t\t[1] Full Time");
        System.out.println("\t\t[2] Part Time");
        do {
            System.out.printf("%-50s: ", "Choice: ");
            option = Integer.parseInt(Helper.readLine().trim());
            switch (option) {
                case 1:
                    return "Full Time";
                case 2:
                    return "Part Time";
                default:
                    System.out.println("Please enter a valid number.");
            }
        } while (true);
    }
}

    



