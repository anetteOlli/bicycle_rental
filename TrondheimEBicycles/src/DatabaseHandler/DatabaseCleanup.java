package DatabaseHandler;
import java.sql.*;

/**This class manages connections to MySQL.
 * Quan
 * Can be replaced with try-with recources?
 *This class is the modified version of "Opprydder.java" from "programmering
 * i java" by Else Lervik.
 */
class DatabaseCleanup {
    /**
     *
     * @param res
     * @return
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
     *
     * @param stm
     */
    static void closeSentence(Statement stm) {
        try {
            if (stm != null) {
                stm.close();
            }
        } catch (SQLException e) {
            skrivMelding(e, "lukkSetning()");
        }
    }

    /**
     *
     * @param forbindelse
     */
    static void closeConnection(Connection forbindelse) {
        try {
            if (forbindelse != null) {
                forbindelse.close();
            }
        } catch (SQLException e) {
            skrivMelding(e, "lukkForbindelse()");
        }
    }

    /**
     *
     * @param forbindelse
     * @return
     */
    static boolean rollback(Connection forbindelse) {
        try {
            if (forbindelse != null && !forbindelse.getAutoCommit()) {
                forbindelse.rollback();
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
