package org.astrogrid.community.delegate.policy;

import org.astrogrid.community.policy.data.GroupData;
import org.astrogrid.community.policy.data.ResourceData;
import org.astrogrid.community.policy.data.CommunityData;
import org.astrogrid.community.policy.data.AccountData;

import org.astrogrid.community.policy.server.PolicyManager ;
import org.astrogrid.community.policy.server.PolicyManagerService ;
import org.astrogrid.community.policy.server.PolicyManagerServiceLocator ;

import java.net.URL;

import java.util.ArrayList;

public class AdministrationDelegate {
   
   /**
    * Switch for our debug statements.
    *
    */
   private static final boolean DEBUG_FLAG = true ;
   
   private String localUrl = "http://localhost:8080/axis/services/PolicyManager";
   
   private String localSecureUrl = "https://localhost:8080/axis/services/PolicyManager";
      
   private String localName = "localname";
   
  
   
   PolicyManager service = null;
   
   public AdministrationDelegate() {
      try {
         service = getService(localUrl);
      }catch(Exception e) {
         e.printStackTrace();
         service = null;
      }      
   }
   
   
   public boolean addGroupMember(String account,String group) throws Exception {
      return (null != service.addGroupMember(account,group)) ;  
   }
   
   public boolean delGroupMember(String account,String group) throws Exception {
      return service.delGroupMember(account,group);  
   }


   public AccountData addAccount(String name) throws Exception {
      return service.addAccount(name);      
   }

   public boolean delAccount(String name) throws Exception {
      return (null != service.delAccount(name));      
   }
   
   public AccountData getAccount(String name) throws Exception {
      return service.getAccount(name);  
   }
   
   
   public ArrayList getAccountList() throws Exception {
      Object []list = service.getLocalAccounts();
      ArrayList al = null;
      if(list != null && list.length > 0) {
         al = new ArrayList(list.length);
         for(int i = 0;i < list.length;i++) {
            al.add(list[i]);
         }
      }
      return al;   
   }
   
   public AccountData setAccount(AccountData ad) throws Exception {
      return service.setAccount(ad);  
   }
   
   
   public ResourceData addResource(String name) throws Exception {
      return service.addResource(name);      
   }

   public boolean delResource(String name) throws Exception {
      return service.delResource(name);      
   }
   
   public ResourceData getResource(String name) throws Exception {
      return service.getResource(name);  
   }
   
   
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
   
   public ResourceData setResource(ResourceData ad) throws Exception {
      return service.setResource(ad);  
   }
   
   
   
   
   
   
   
   public ArrayList getAccountGroupList(String account) throws Exception {
      Object []list = getAccountGroupList(account,localUrl);
      ArrayList al = new ArrayList(list.length);
      for(int i = 0;i < list.length;i++) {
         al.add(list[i]);
      }
      return al;
      
   }
   
   public Object[] getAccountGroupList(String account,String url) throws Exception  {
      service = getService(url);
      return service.getAccountGroupList(account);
   }
   
   public ArrayList getGroupList() throws Exception  {
      Object []list = service.getGroupList();
      ArrayList al = new ArrayList(list.length);
      for(int i = 0;i < list.length;i++) {
         al.add(list[i]);
      }
      return al;
   }
   
   public boolean delGroup(String ident) throws Exception  {
      return service.delGroup(ident);
   }
   
   public GroupData addGroup(String ident) throws Exception  {
      return service.addGroup(ident);
   }
   
   public GroupData getGroup(String ident) throws Exception  {
      return service.getGroup(ident);
   }
   
   public GroupData setGroup(GroupData groupData) throws Exception  {
      return service.setGroup(groupData);
   }
   
   public CommunityData addCommunity(String ident) throws Exception  {
      return service.addCommunity(ident);
   }

   public CommunityData getCommunity(String ident) throws Exception  {
      return service.getCommunity(ident);
   }

   public CommunityData getCommunity(CommunityData commData) throws Exception  {
      return service.setCommunity(commData);
   }
   
   public boolean delCommunity(String ident) throws Exception  {
      return service.delCommunity(ident);
   }
   
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
    * Setup our tests.
    *
    */
   private PolicyManager getService(String url)
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
      service = locator.getPolicyManager(new URL(url));

      if (DEBUG_FLAG) System.out.println("----\"----") ;
      if (DEBUG_FLAG) System.out.println("") ;
      return service;
   }     
}