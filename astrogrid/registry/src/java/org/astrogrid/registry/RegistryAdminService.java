package org.astrogrid.registry;

/**
 * @author Elizabeth Auden
 */

public class RegistryAdminService {

  public String adminQuery(String adminQuery) {
    String adminResponse = new String();
    adminResponse = RegistryAdmin.requestAdmin(adminQuery);
    return adminResponse;
  }
}
