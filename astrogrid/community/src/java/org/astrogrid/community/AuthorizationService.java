package org.astrogrid.community;
import java.sql.*;
import org.astrogrid.community.CommunityDb;

public class AuthorizationService{

   private Connection conn = new CommunityDb().getConnection();

   private Statement myStmt;

   private ResultSet myRs;

   public boolean checkPermission(String who, String action, String resource) {

      try {
		  myStmt = conn.createStatement();
      }
      catch( java.sql.SQLException e ) {
	     System.out.println("SQL exception " + e);
     }

//    myRs = myStmt.executeQuery("select * from  whatever where something);

      return true;
   }
}
