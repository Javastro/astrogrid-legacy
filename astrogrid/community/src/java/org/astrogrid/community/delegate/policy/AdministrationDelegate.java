package org.astrogrid.community.delegate.policy;

import org.astrogrid.community.policy.data.GroupData;
import org.astrogrid.community.policy.data.CommunityData;

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
   
   private String localUrl = "localhost";
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