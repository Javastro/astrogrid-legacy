package org.astrogrid.community;
import java.sql.*;

public class CommunityDb {

   private Connection myConnection = null;

   public Connection getConnection() {
      return myConnection;
   }
}