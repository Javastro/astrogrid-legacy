package org.astrogrid.community.client.delegate ;

import org.astrogrid.community.common.policy.data.GroupData;
import org.astrogrid.community.common.policy.data.ResourceData;
import org.astrogrid.community.common.policy.data.CommunityData;
import org.astrogrid.community.common.policy.data.AccountData;
import org.astrogrid.community.common.policy.data.PolicyPermission;
import org.astrogrid.community.common.policy.data.GroupMemberData;

import org.astrogrid.community.common.config.CommunityConfig;

import org.astrogrid.community.common.policy.manager.PolicyManager ;
import org.astrogrid.community.common.policy.manager.PolicyManagerService ;
import org.astrogrid.community.common.policy.manager.PolicyManagerServiceLocator ;

import java.net.URL;
import java.util.regex.*;

import java.util.ArrayList;
/**
 * The AdministrationDelegate is the gateway to the webservice, having mappped methods to the webservice methods. 
 * This particular class deals with administration piece of the community.  Many webservice methods are requred for 
 * administration such as inserting permissions, resources, accounts, groups and others.
 * @author Kevin Benson
 *
 * This API is going to be replaced during iter 5, and will be deprecated in iter 6.
 *
 */
public class AdministrationDelegate {
   
   /**
    * Switch for our debug statements.
    *
    */
   private static final boolean DEBUG_FLAG = true ;
         
   /**
    *  service variable to our PolicyManager.
    */
   PolicyManager service = null;

    //
    // Load our community config.
    static
        {
        CommunityConfig.loadConfig() ;
        }
   
   /**
    * Public constructor deals with getting our service (link) to the webservice.
    *
    */
   public AdministrationDelegate() {
      try {
         service = getService(CommunityConfig.getManagerUrl());
      }catch(Exception e) {
         e.printStackTrace();
         service = null;
      }      
   }
   
   private static final String REGEX_SECUREPORT = ":\\d+";
   private String getSecureURL() {
      String communitySecurity = CommunityConfig.getProperty("community.security","on");
      if(communitySecurity == null || !communitySecurity.equals("on")) {
         return CommunityConfig.getManagerUrl();
      }
      String policyURL = CommunityConfig.getProperty("policy.manager.secure.url");
      if(policyURL != null && policyURL.trim().length() > 0) {
         return policyURL;
      }
      
      policyURL = CommunityConfig.getManagerUrl();
      
      System.out.println("manager url = " + policyURL);

      
      String securePort = CommunityConfig.getProperty("community.secure.port");
      System.out.println("the secure port = " + securePort);      
      if(securePort != null && securePort.length() > 0) {
         policyURL = policyURL.replaceAll("http","https");         
         Pattern p = Pattern.compile(REGEX_SECUREPORT);
         Matcher m = p.matcher(policyURL); // get a matcher object
         policyURL = m.replaceAll((":" + securePort));         
      }else {
         return null;
      }
      System.out.println("The PolicyURL = " + policyURL);
      return policyURL;      
   }
   

  /*
   * Removed for refactoring.
   *
   public String getPassword(String name) throws Exception {
      String policyURL = getSecureURL();
      if(policyURL == null) {
         throw new Exception("This operation requires a secure ssl connection which cannot be found for retrieving the password.");
      }
      PolicyManager secureService = getService(policyURL);
      return secureService.getPassword(name);      
   }
  *
  */

  /*
   * Removed for refactoring.
   *
   public AccountData setPassword(String account,String password) throws Exception {
      String policyURL = getSecureURL();
      if(policyURL == null) {
         throw new Exception("This operation requires a secure ssl connection which cannot be found for retrieving the password.");
      }
      PolicyManager secureService = getService(policyURL);
      return secureService.setPassword(account,password);      
   }
  *
  */

   
   /**
    * Creates a new permission in the database tying a resource to a group for a specefic action.
    * @param resource 
    * @param group
    * @param action
    * @return
    * @throws Exception
    */
   public PolicyPermission addPermission(String resource, String group, String action)
      throws Exception
      {
      return service.addPermission(resource, group, action) ;
      }
      
      /**
       * Deletes a permission from teh database.
       * @param resource
       * @param group
       * @param action
       * @return
       * @throws Exception
       */
   public boolean delPermission(String resource, String group, String action)
      throws Exception
      {
      return service.delPermission(resource, group, action) ;
      }
      
   
   /**
    * Adds a  user/account to a particular MULTI group.
    * @param account
    * @param group
    * @return
    * @throws Exception
    */
   public boolean addGroupMember(String account,String group) throws Exception {
      return (null != service.addGroupMember(account,group)) ;
   }
   
   /**
    * Deletes a user from a group.
    * @param account
    * @param group
    * @return
    * @throws Exception
    */
   public boolean delGroupMember(String account,String group) throws Exception {
      return (null != service.delGroupMember(account,group)) ;
   }

/**
 * Adds an account. 
 * @param name
 * @return
 * @throws Exception
 */
   public AccountData addAccount(String name) throws Exception {
      return service.addAccount(name);
   }

/**
 * Deletes an account.
 * @param name
 * @return
 * @throws Exception
 */
   public boolean delAccount(String name) throws Exception {
      return (null != service.delAccount(name));
   }
   
   /**
    * Get an Account object which consists of a name, and description.
    * @param name
    * @return
    * @throws Exception
    * @see org.astrogrid.community.policy.data.AccountData
    */
   public AccountData getAccount(String name) throws Exception {
      return service.getAccount(name);
   }
   
   /**
    * Gets a local account list. 
    * @deprecated because getRemoteAccounts using the local url, would get the same results.
    * @return
    * @throws Exception
    */
   public ArrayList getAccountList() throws Exception {
      return createArrayList(service.getLocalAccounts());
   }
   
   /**
    * Gets an account list based off of a community name.
    * @param community
    * @return
   public ArrayList getAccountList(String community) throws Exception {
      return createArrayList(service.getRemoteAccounts(community));
   }
    */
   
   /**
    * Converts an array of AccountData objects to an ArrayList.
    * @param list
    * @return
    */
   private ArrayList createArrayList(Object []list) {
      ArrayList al = null;
      if(list != null && list.length > 0) {
         al = new ArrayList(list.length);
         for(int i = 0;i < list.length;i++) {
            al.add(list[i]);
         }
      }
      return al;         
   }

/*
 *   
   public ArrayList getAccountGroups(String account, String community) throws Exception {
      return createArrayList(service.getRemoteAccountGroups(account,community));
   } 
 */   
/*
 *   
   public boolean isAdminAccount(String account,String community) throws Exception {
      ArrayList al = getAccountGroups(account,community);
      String adminGroup = "Admin@" + community;
      adminGroup = adminGroup.toLowerCase();
      
      if(al != null && al.size() > 0) {
         for(int i = 0;i < al.size();i++) {
            GroupMemberData data = (GroupMemberData)al.get(i);
            if(adminGroup.equals(data.getGroup().toLowerCase())) {
               return true;            
            }//if
         }//for
      }//if
      return false;
   }
 *
 */   
   /**
    * Updates an account.
    * @param ad
    * @return
    * @throws Exception
    */
   public AccountData setAccount(AccountData ad) throws Exception {
      String policyURL = getSecureURL();
      if(policyURL == null) {
         throw new Exception("A Secure ssl connection is required for updating an account, and cannot be found.");
      }
      PolicyManager secureService = getService(policyURL);
      return secureService.setAccount(ad);  
   }
   
   /**
    * Adds a resource.
    * @param name
    * @return
    * @throws Exception
    */
   public ResourceData addResource(String name) throws Exception {
      return service.addResource(name);      
   }

/**
 * Deletes a resource.
 * @param name
 * @return
 * @throws Exception
 */
   public boolean delResource(String name) throws Exception {
      return service.delResource(name);      
   }
   
   /**
    * Returns a resource information.
    * @param name
    * @return
    * @throws Exception
    * @see org.astrogrid.community.policy.data.ResourceData
    */
   public ResourceData getResource(String name) throws Exception {
      return service.getResource(name);  
   }
   
   
   /**
    * Returna an array of ResourceData objects.
    * @return
    * @throws Exception
    */
   public ArrayList getResourceList() throws Exception {
      return createArrayList(service.getResourceList());
   }
   
   /**
    * Update a resource.
    * @param ad
    * @return
    * @throws Exception
    */
   public ResourceData setResource(ResourceData ad) throws Exception {
      return service.setResource(ad);  
   }
      
   /**
    * Get a group list.
    * @return
    * @throws Exception
    * @deprecated
    */
   public ArrayList getGroupList() throws Exception  {
      return createArrayList(service.getLocalGroups());
   }
   
   /**
    * Return a grou list in a particular community.  Might be the local or remote community.
    * @param community
    * @return
   public ArrayList getGroupList(String community) throws Exception {
      return createArrayList(service.getRemoteGroups(community));  
   }
    */
      
   /**
    * Delete a group.
    * @param ident
    * @return
    * @throws Exception
    */
   public boolean delGroup(String ident) throws Exception  {
      return (null != service.delGroup(ident));
   }
   
   /**
    * Add a group.
    * @param ident
    * @return
    * @throws Exception
    */
   public GroupData addGroup(String ident) throws Exception  {
      return service.addGroup(ident);
   }
   
   /**
    * Get a group.
    * @param ident
    * @return
    * @throws Exception
    */
   public GroupData getGroup(String ident) throws Exception  {
      return service.getGroup(ident);
   }
   
   /**
    * Update a group.
    * @param groupData
    * @return
    * @throws Exception
    */
   public GroupData setGroup(GroupData groupData) throws Exception  {
      return service.setGroup(groupData);
   }
   
   /** 
    * Add a community.
    * @param ident
    * @return
    * @throws Exception
    */
   public CommunityData addCommunity(String ident) throws Exception  {
      return service.addCommunity(ident);
   }

   /**
    * Get a community
    * @param ident
    * @return
    * @throws Exception
    */
   public CommunityData getCommunity(String ident) throws Exception  {
      return service.getCommunity(ident);
   }

   /**
    * Set a community.
    * @param commData
    * @return
    * @throws Exception
    */
   public CommunityData setCommunity(CommunityData commData) throws Exception  {
      return service.setCommunity(commData);
   }
   
   /**
    * Delete a community
    * @param ident
    * @return
    * @throws Exception
    */
   public boolean delCommunity(String ident) throws Exception  {
      return (null != service.delCommunity(ident));
   }
   
   /**
    * Get a community list.
    * @return
    * @throws Exception
    */
   public ArrayList getCommunityList()  throws Exception {
      return createArrayList(service.getCommunityList());
   }
   
   public ArrayList getGroupMembers(String group) throws Exception {
      return createArrayList(service.getGroupMembers(group));   
   }

   /**
    * Return our service object which is our link to the webservice.
    * @return
    * @throws Exception
    */
   private PolicyManager getService(String targetEndPoint)
      throws Exception
      {
      if (DEBUG_FLAG) System.out.println("") ;
      if (DEBUG_FLAG) System.out.println("----\"----") ;
      if (DEBUG_FLAG) System.out.println("setUp()") ;

      PolicyManagerService locator = null;
      PolicyManager service = null;
      //
      // Create our service locator.
      locator = new PolicyManagerServiceLocator();
      
      //
      // Create our service.
      service = locator.getPolicyManager(new URL(targetEndPoint));

      if (DEBUG_FLAG) System.out.println("----\"----") ;
      if (DEBUG_FLAG) System.out.println("") ;
      return service;
   }     
}