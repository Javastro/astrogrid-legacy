/*
 * $Id: WorkFlowUsingTestCase.java,v 1.6 2004/04/25 20:41:00 pah Exp $
 * 
 * Created on 18-Mar-2004 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.manager;

import java.io.InputStream;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.InputSource;

import org.astrogrid.applications.common.config.BaseDBTestCase;
import org.astrogrid.community.User;
import org.astrogrid.community.beans.v1.Account;
import org.astrogrid.community.beans.v1.Credentials;
import org.astrogrid.community.beans.v1.Group;
import org.astrogrid.store.Ivorn;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.Workflow;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk) 18-Mar-2004
 * @version $Name:  $
 * @since iteration5
 */
public class WorkFlowUsingTestCase extends BaseDBTestCase {


   protected Workflow workflow;
   protected Tool tool, tool2;
   /**
    * 
    */
   public WorkFlowUsingTestCase() {
      this("applications workflow");
   }
   protected void setUp() throws Exception {
       super.setUp();
       // credentials object
       acc =  new Account();
       acc.setCommunity(COMMUNITY);
       acc.setName("frog");
       
       group = new Group();
       group.setCommunity(COMMUNITY);
       group.setName("devel");
       
       creds = new Credentials();
       creds.setAccount(acc);
       creds.setGroup(group);
       creds.setSecurityToken("blah");

       //equivalent user object
       user = new User();
       user.setAccount(creds.getAccount().getName() + "@" + creds.getAccount().getCommunity());
       user.setGroup(creds.getGroup().getName() + "@" + creds.getGroup().getCommunity());
       user.setToken(creds.getSecurityToken());        

      InputStream is = this.getClass().getResourceAsStream("/testworkflow.xml");
      assertNotNull(is);
      InputSource source = new InputSource(is);
      try {
         workflow = (Workflow)Unmarshaller.unmarshal(Workflow.class, source);
         assertNotNull(workflow);
         workflow.validate();
         tool = (Tool)workflow.findXPathValue("//sequence/activity[1]/tool"); // there should only be one!
         assertNotNull(tool);
         tool2 = (Tool)workflow.findXPathValue("//sequence/activity[2]/tool"); // there should be more that one one!
         assertNotNull(tool2);
        
      }
      catch (MarshalException e) {
         logger.error("cannot unmarshall the testworkflow", e);
         fail("cannot unmarshall the testworkflow" + e);
      }
      catch (ValidationException e) {
            fail("cannot validate the test workflow");
      }
      

   }
   
   protected Account acc;
   protected Group group;   
   protected Credentials creds;
   protected User user;
   protected Workflow wf;
    
   public static final String COMMUNITY = "org.astrogrid.localhost";
   public static final String MYSPACE = COMMUNITY + "/myspace";
   public static final String TESTDSA = COMMUNITY + "/testdsa";
   public static final String TESTAPP = COMMUNITY + "/testapp";
   public static final String TESTAPP2 = COMMUNITY + "/testap2"; //note it isn't double 'p'
    
  protected Ivorn createIVORN(String path)
  {
     return new Ivorn(COMMUNITY+"/"+user.getUserId(),path);
  }

   /**
    * @param arg0
    */
   public WorkFlowUsingTestCase(String arg0) {
      super(arg0);
      
      
   }


}
