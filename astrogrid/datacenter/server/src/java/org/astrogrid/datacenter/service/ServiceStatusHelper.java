/*$Id: ServiceStatusHelper.java,v 1.2 2003/11/17 15:41:48 mch Exp $
 * Created on 14-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.datacenter.service;

import java.util.Date;

import org.astrogrid.datacenter.snippet.StatusHelper;
import org.astrogrid.datacenter.queriers.DatabaseQuerier;

/** methods moved from common.StatusHelper that weren't truly common.
 * @author Noel Winstanley nw@jb.man.ac.uk 14-Nov-2003
 *
 */
public class ServiceStatusHelper extends StatusHelper {
    /**
      * Returns an Iteration 02 job notification tag with status included.  The
      * It02 template was this:
      * <pre>
  <?xml version="1.0" encoding="UTF8"?>
  <!-- Template for making SOAP requests to the JobMonitor -->
  <job name="{0}"
       userid="{1}"
       community="{2}"
       jobURN="{3}"
       time="{4}" >
     <jobstep name="{5}" stepNumber="{6}" status="{7}"/>
  </job>
     </pre>
      */
     public static String makeJobNotificationTag(DatabaseQuerier querier)
     {
        return
              "<job name='"+querier.getHandle()+"'  time="+new Date()+"' >"+
                 "<jobstep name='"+querier.getHandle()+"' status='"+querier.getStatus()+"'/>"+
              "</job>";
     }

}


/*
$Log: ServiceStatusHelper.java,v $
Revision 1.2  2003/11/17 15:41:48  mch
Package movements

Revision 1.1  2003/11/17 12:16:33  nw
first stab at mavenizing the subprojects.
 
*/
