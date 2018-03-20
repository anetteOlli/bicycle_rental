package DatabaseHandler;

import javax.swing.JOptionPane;
import java.io.*;



/**
 *b
 * It reads username and password from a file and send it as a String to the DatabaseConnection
 * implements Serializable in order to read passwords and usernames from file
 * This method of storing password is far from okay, however it's better than storing
 * passwords in the code...
 * Author: Anette
 */
class DatabasePasswordKeep implements Serializable {
    private String filepath = "loginCredentials.ser";
    private String key = "abcdefghijklmop";

    /**
     * This method creates a loginCredentials file containing the username and password for the database
     * @param username the username credentials for logging into MySQL database
     * @param password the password credentials for logging into MySQL database
     * @return returns true if it succesfully managed to create a credential file
     */
    private boolean saveCredentials(String username, String password){
        //CrytpAES encrypts the credentials and generates a byte[] from them
        //the key can be changed, and ideally should not be stored inside the source code
        CryptAES cryptionHandler = new CryptAES();
        byte[] usernameCrypted;
        byte[] passwordCrypted;
        try {
            usernameCrypted = cryptionHandler.encrypt(username, key);
            passwordCrypted = cryptionHandler.encrypt(password, key);
        } catch (Exception e) {
            return false;
        }
        try{
            //store the encrypted username and password as two byte[]
            FileOutputStream stream = new FileOutputStream(filepath);
            ObjectOutputStream output = new ObjectOutputStream(stream);
            output.writeObject(usernameCrypted);
            output.writeObject(passwordCrypted);
            output.close();
            stream.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * This method reads the username and password for the mySQL database
     * @return returns a String[] containing the username and password for the database.
     * the first index is the username and the second index is the password
     * this method should only be accessed by the database package.
     */
    String[] getCredentials(){
        try {
            byte[] usernameCredentials;
            byte[] passwordCredentials;
            String[] credentials = new String[2];
            FileInputStream stream = new FileInputStream(filepath);
            ObjectInputStream input = new ObjectInputStream(stream);
            /* TO DO:
            * this needs to handle unexpected events such as:
            *  1. the credentials file not existing
            *  2. the credentials file containing incorrect data
            */
            //the username and password are stored as encrypted byte[]
            usernameCredentials = (byte[]) input.readObject();
            passwordCredentials = (byte[]) input.readObject();
            CryptAES cryptionHandler = new CryptAES();
            input.close();
            stream.close();
            try{
                //decrypts the username and password from the byte[]
                credentials[0] = cryptionHandler.decrypt(usernameCredentials, key);
                credentials[1] = cryptionHandler.decrypt(passwordCredentials, key);
            } catch (Exception e) {
                return null;
            }
            return credentials;
        }catch (IOException e){
            return null;
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    /**
     * This main method must be run in order to create the credential file.
     * (as a part of the installation of the system)
     * It is only needed to run the main method once during the installation
     * and in the case the file "loginCredentials.ser" goes missing
     *
     */
    public static void main(String[] args) {
        DatabasePasswordKeep setup = new DatabasePasswordKeep();
        String username = JOptionPane.showInputDialog("enter your username");
        String password = JOptionPane.showInputDialog("enter your password");
        boolean success = setup.saveCredentials(username, password);
        if(success){
            String[] savedCredentials = setup.getCredentials();
            for(int i = 0; i < savedCredentials.length; i++){
                System.out.println(savedCredentials[i]);
            }
        }
    }
}
