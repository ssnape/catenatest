/**
 *  The ConfigReader class is used to load values from the environment
 *  properties file. Each property has a corresponding get method.
 *
 * @author Arielle Bonnici
 * @version 1.0
 * @since 2020-09-04
 */

package datareader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    private Properties props;
    private final String filePath = "src/test/config/askgamblers.properties";

    public ConfigReader() {
        FileInputStream fStream;
        try {

            fStream = new FileInputStream(new File(filePath));
            props = new Properties();
            try {
                props.load(fStream);
                fStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Properties file not found at " + filePath);
        }
    }

    public String getDriverPath() {
        return getPropertyValue("driverPath");
    }

    public String getLoginUrl() {
        return getPropertyValue("loginUrl");
    }

    public String getInvalidUsername() {
        return getPropertyValue("invalidUsername");
    }

    public String getInvalidPassword() {
        return getPropertyValue("invalidPassword");
    }

    public String getInvalidCredentialsError() {
        return getPropertyValue("invalidCredentialsError");
    }

    public String getForgotPasswordUrl() {
        return getPropertyValue("forgotPasswordUrl");
    }

    public String getUsername() {
        return getPropertyValue("username");
    }

    public String getPassword() {
        return getPropertyValue("password");
    }

    public String getResetPasswordSubject() {
        return getPropertyValue("resetPasswordSubject");
    }

    public String getGmailUser() {
        return getPropertyValue("gmailUser");
    }

    public String getGmailPasword() {
        return getPropertyValue("gmailPassword");
    }

    private String getPropertyValue(String property) {
        String value = props.getProperty(property);
        if (value != null)
            return value;
        else throw new RuntimeException(String.format("%s not defined in the properties file", property));
    }
}
