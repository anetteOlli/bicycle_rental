package DatabaseHandler;

import java.io.Serializable;

/**
 * This class handles communication with the database
 * It reads username and password from a file and establishes a connection
 * with the database
 * implements Serializable in order to read passwords and usernames from file
 * This method of storing password is far from okay, however it's better than storing
 * password in the code...
 */
public class DatabaseConnection implements Serializable {

}
