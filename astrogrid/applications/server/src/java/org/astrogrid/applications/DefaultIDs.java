/*$Id: DefaultIDs.java,v 1.4 2008/09/03 14:18:33 pah Exp $
 * Created on 08-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications;

import org.astrogrid.community.User;


/** Manages all the identity info for an application execution
 * <p> its a container that pairs together the user-assigned id and system-assigned id for an application 
 * @deprecated
 * assignes default userid for now - dummy value.*/
public class DefaultIDs implements AbstractApplication.IDs {
    
    public DefaultIDs(String a,String b) {
        this(a,b,new User());
    }

      public DefaultIDs(String jobStepId,String id,User user) {
          this.user = user;
          this.id = id;
          this.jobStepId = jobStepId;
      } 
      protected final String id;
      protected final String jobStepId;
      protected final User user;

     public String getId() {
         return id;
     }

     public String getJobStepId() {
         return jobStepId;
     }
     
     public User getUser() {
         return user;
     }
   }

/* 
$Log: DefaultIDs.java,v $
Revision 1.4  2008/09/03 14:18:33  pah
result of merge of pah_cea_1611 branch

Revision 1.3.272.1  2008/06/11 14:31:42  pah
merged the ids into the application execution environment

Revision 1.3  2004/07/23 10:37:28  nw
Javadocs

Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.2  2004/07/01 01:42:47  nw
final version, before merge

Revision 1.1.2.1  2004/06/14 08:56:58  nw
factored applications into sub-projects,
got packaging of wars to work again
 
*/