package org.astrogrid.community.delegate.policy;

import org.astrogrid.community.policy.data.GroupData;
import org.astrogrid.community.policy.data.ResourceData;
import org.astrogrid.community.policy.data.CommunityData;
import org.astrogrid.community.policy.data.AccountData;
import org.astrogrid.community.policy.data.PolicyPermission;

import org.astrogrid.community.policy.server.PolicyManager ;
import org.astrogrid.community.policy.server.PolicyManagerService ;
import org.astrogrid.community.policy.server.PolicyManagerServiceLocator ;

import java.net.URL;

import java.util.ArrayList;
/**
 * The AdministrationDelegate is the gateway to the webservice, having mappped methods to the webservice methods. 
 * This particular class deals with administration piece of the community.  Many webservice methods are requred for 
 * administration such as inserting permissions, resources, accounts, groups and others.
 * @author Kevin Benson
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
   
   /**
    * Public constructor deals with getting our service (link) to the webservice.
    *
    */
   public AdministrationDelegate() {
      try {
         service = getService();
      }catch(Exception e) {
         e.printStackTrace();
         service = null;
      }      
   }
   
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
    * Get an Account object which consists of a name, password, and description.
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
      return getAccountList(service.getLocalAccounts());
   }
   
   /**
    * Gets an account list based off of a community name.
    * @param community
    * @return
    */
   public ArrayList getAccountList(String community) throws Exception {
      return getAccountList(service.getRemoteAccounts(community));
   }
   
   /**
    * Converts an array of AccountData objects to an ArrayList.
    * @param list
    * @return
    */
   private ArrayList getAccountList(Object []list) {
      ArrayList al = null;
      if(list != null && list.length > 0) {
         al = new ArrayList(list.length);
         for(int i = 0;i < list.length;i++) {
            al.add(list[i]);
         }
      }
      return al;         
   }
   
   /**
    * Updates an account.
    * @param ad
    * @return
    * @throws Exception
    */
   public AccountData setAccount(AccountData ad) throws Exception {
      return service.setAccount(ad);  
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
      Object []list = service.getResourceList();
      ArrayList al = null;
      if(list != null && list.length > 0) {
         al = new ArrayList(list.length);
         for(int i = 0;i < list.length;i++) {
            al.add(list[i]);
         }
      }
      return al;   
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
      return getGroupList(service.getLocalGroups());
   }
   
   /**
    * Return a grou list in a particular community.  Might be the local or remote community.
    * @param community
    * @return
    */
   public ArrayList getGroupList(String community) throws Exception {
      return getGroupList(service.getRemoteGroups(community));  
   }
   
   /**
    * Converts an object array into an araylist.
    * @param list
    * @return
    */
   private ArrayList getGroupList(Object []list) {
      ArrayList al = new ArrayList(list.length);
      for(int i = 0;i < list.length;i++) {
         al.add(list[i]);
      }
      return al;      
   }
   
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
      Object []list = service.getCommunityList();
      if(list == null) {
         throw new Exception("isn't finding my data");
      }
      ArrayList al = new ArrayList(list.length);
      for(int i = 0;i < list.length;i++) {
         al.add(list[i]);
      }
      return al;
   }   

   /**
    * Return our service object which is our link to the webservice.
    * @return
    * @throws Exception
    */
   private PolicyManager getService()
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
      service = locator.getPolicyManager();

      if (DEBUG_FLAG) System.out.println("----\"----") ;
      if (DEBUG_FLAG) System.out.println("") ;
      return service;
   }     
}