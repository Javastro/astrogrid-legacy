package org.astrogrid.registry;

/**
 * @author Elizabeth Auden
 * 
 * The RegistryAdminService is a web service that submits an XML formatted
 * administratio query to the RegistryAdmin class.  This web service allows
 * a user to add, edit, or delete a resource service entry inside the registry.
 * 
 * Elizabeth  Auden, 24 October 2003
 */

public class RegistryAdminService {

  public String adminQuery(String adminQuery) {
    String adminResponse = new String();
    adminResponse = RegistryAdmin.requestAdmin(adminQuery);
    return adminResponse;
  }
}
