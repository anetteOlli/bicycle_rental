package DatabaseHandler;
import java.sql.*;

/**This class manages connections to MySQL.
 *test
 * Can be replaced with try-with recources?
 *This class is the modified version of "Opprydder.java" from "programmering
 * i java" by Else Lervik.
 * testing
 */
public class DatabaseCleanup{
    /**
     *
     * @param res res is the Resultset that will be closed
     * @return the method returns true if it successfully closed the Restultset,
     * or false if it enocountered a problem.
     */
    boolean closeResult(ResultSet res) {
        try {
            if (res != null) {
                res.close();
                return true;
            }
        } catch (SQLException e) {
            return false;
        }
        return false;
    }

    /**
     * Method closes statement, this method handles exceptions.
     * @param stm the statement that should be closed.
     * @return returns true if it succesfully closed the statement.
     */
    boolean closeSentence(Statement stm) {
        try {
            if (stm != null) {
                stm.close();
                return true;
            }
        } catch (SQLException e) {
            return false;
        }
        return false;
    }

    /**
     *
     * @param con the Connection that should be closed
     * @return return true if it successfully closed the connection
     */
    boolean closeConnection(Connection con) {
        try {
            if (con != null) {
                con.close();
                return true;

            }
        } catch (SQLException e) {
            return false;
        }
        return false;
    }

    /**
     *
     * @param con the connection that should be rolled back
     * @return return true if it successfully rolled back the database
     */
    boolean rollback(Connection con) {
        try {
            if (con != null && !con.getAutoCommit()) {
                con.rollback();
                return true;
            }
            return false;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * This method turns autoCommit back on
     * @param con the connection that should set autocommit back on
     * @return return true if it successfully turned autocommit on
     */
    boolean setAutoCommit(Connection con) {
        try {
            if (con != null && !con.getAutoCommit()) {
                con.setAutoCommit(true);
                return true;
            }
            return false;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * NON TESTED METHOD
     * @param con
     * @return
     */
    boolean setReadCommited(Connection con){
        try {
            con.setTransactionIsolation(con.TRANSACTION_READ_COMMITTED);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * NON TESTED METHOD
     * @param con
     * @return
     */
    boolean setReadUnCommited(Connection con){
        try {
            con.setTransactionIsolation(con.TRANSACTION_READ_UNCOMMITTED);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * NON TESTED METHOD
     * @param con
     * @return
     */
    boolean setRepeatableRead(Connection con){
        try {
            con.setTransactionIsolation(con.TRANSACTION_REPEATABLE_READ);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * NON TESTED METHOD
     * @param con
     * @return
     */
    boolean setSerializable(Connection con){
        try {
            con.setTransactionIsolation(con.TRANSACTION_SERIALIZABLE);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }


    /**
     * commits the changes made
     * @param con the connection that should become commited
     * @return return true if it successfully commited
     */
    boolean commit(Connection con){
        try{
            con.commit();
            return true;
        }catch (SQLException ex){
            return false;
        }
    }
}
