package org.astrogrid.community.webapp;

import org.astrogrid.community.common.database.manager.DatabaseManager;
import org.astrogrid.community.server.database.manager.DatabaseManagerImpl;

/**
 * A Java bean for operations on the community database as a whole.
 *
 * The bean is used for the "health-check" on the DB.
 *
 * @author Guy Rixon
 */
public class CommunityDbBean {
  
  /**
   * Constructs a CommunityDbBean.
   */
  public CommunityDbBean() {
    this.dbManager = new DatabaseManagerImpl();
  }
  
  protected DatabaseManager dbManager;
  
  /**
   * Reveals the current useability of the database.
   */
  public boolean isUseable() {
    try {
      return this.dbManager.checkDatabaseTables();
    }
    catch (Exception e) {
      return false;
    }
  }
  
  /**
   * Reveals the status of the database.
   *
   * @return - "OK" or "not useable".
   */
  public String getBriefStatus() {
    try {
      if (this.isUseable()) {
        return "OK";
      }
      else {
        return "not useable";
      }
    }
    catch (Exception e) {
      return "not useable";
    }
  }
  
}
