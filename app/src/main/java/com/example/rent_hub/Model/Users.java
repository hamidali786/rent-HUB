package  com.example.rent_hub.Model;

public class Users {
    // get user information
    private String mail, username, phoneNo, userAddress, password;
    //default constructor with no parameter
    public Users(){

    }
    //constructor for all parameters
    public Users(String mail, String username, String phoneNo, String userAddress, String password) {
        this.mail = mail;
        this.username = username;
        this.phoneNo = phoneNo;
        this.userAddress = userAddress;
        this.password = password;
    }
    //setter and getter for all parameters

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
