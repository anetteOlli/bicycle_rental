package DatabaseHandler;
import java.sql.*;

/**This class manages connections to MySQL.
 *
 * Can be replaced with try-with recources?
 *This class is the modified version of "Opprydder.java" from "programmering
 * i java" by Else Lervik.
 *
 *
 */
class DatabaseCleanup {
    /**
     *
     * @param res res is the Resultset that will be closed
     * @return the method returns true if it successfully closed the Restultset,
     * or false if it enocountered a problem.
     */
    static boolean closeResult(ResultSet res) {
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
    static boolean closeSentence(Statement stm) {
        try {
            if (stm != null) {
                stm.close();
                return true;
            }
        } catch (SQLException e) {
            skrivMelding(e, "lukkSetning()");
            return false;
        }
        return false;
    }

    /**
     *
     * @param con the Connection that should be closed
     * @return return true if it succesfully closed a connection
     */
    static boolean closeConnection(Connection con) {
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
     * @param con
     * @return
     */
    static boolean rollback(Connection con) {
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
     *
     * @param con
     * @return
     */
    static boolean setAutoCommit(Connection con) {
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
     *
     * @param con
     * @return
     */
    static boolean commit(Connection con){
        try{
            con.commit();
            return true;
        }catch (SQLException ex){
            return false;
        }
    }

    /**
     *
     * @param e
     * @param melding
     */
    private static void skrivMelding(Exception e, String melding) {
        System.err.println("*** Feil oppstått: " + melding + ". ***");
        e.printStackTrace(System.err);
    }
}
