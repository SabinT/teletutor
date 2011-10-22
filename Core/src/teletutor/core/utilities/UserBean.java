/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.core.utilities;

/**
 * use this from the same package as used in the web applications
 * this class should be generated by the web-service client
 * @author Sabin Timalsena
 */
public class UserBean {
    String username;
    String realName;
    String email;

    public UserBean() {
    }

    public UserBean(String username, String realName, String email) {
        this.username = username;
        this.realName = realName;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
}
