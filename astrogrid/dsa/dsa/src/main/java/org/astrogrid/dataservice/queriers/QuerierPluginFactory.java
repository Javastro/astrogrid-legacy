/*$Id: QuerierPluginFactory.java,v 1.1 2009/05/13 13:20:26 gtr Exp $
 * Created on 24-Sep-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 **/
package org.astrogrid.dataservice.queriers;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.cfg.ConfigFactory;

/** Manages a single query; this is the root for a chain of possible chain
 * of plugins, translators, etc. It makes the right kind of querier for the job,
 * and fields any things that are not directly
 * to do with the actual querying process
 *
 */
public class QuerierPluginFactory {
   
   private static final Log log = LogFactory.getLog(QuerierPluginFactory.class);
   
   /** Key to which plugin (which sublcass of Querier) to use */
   public static final String QUERIER_PLUGIN_KEY = "datacenter.querier.plugin";
   
    /**
    * Creates a plugin from the configuration file details for the given querier
    */
   public static QuerierPlugin createPlugin(Querier aQuerier) throws QuerierPluginException {
      return instantiatePlugin(aQuerier);
   }
   
   
   
   /** Instantiates the class with the given name.  This is useful for things
    * such as 'plugins', where a class name might be given in a configuration file.
    * Rather messily throws Throwable because anything might have
    * gone wrong in the constructor.
    */
   public static Object instantiate(String classname) throws Throwable {
      
      try {
         Class qClass = Class.forName(classname);

         /* NWW - interesting bug here.
          original code used class.newInstance(); this method doesn't declare it throws InvocationTargetException,
          however, this exception _is_ thrown if an exception is thrown by the constructor (as is often the case at the moment)
          worse, as InvocatioinTargetException is a checked exception, the compiler rejects code with a catch clause for
          invocationTargetExcetpion - as it thinks it cannot be thrown.
          this means the exception boils out of the code, and is unstoppable - dodgy
          work-around - use the equivalent methods on java.lang.reflect.Constructor - which do throw the correct exceptions */

         Constructor constr = qClass.getConstructor(new Class[] { });
         return constr.newInstance(new Object[] { } );
         
      } catch (InvocationTargetException e) {
         // interested in the root cause here - invocation target is just a wrapper, and not meaningful in itself.
         throw e.getCause();
      }
   }
   
   /** Instantiates the querier given in the configuration file
    */
   public static QuerierPlugin instantiatePlugin(Querier querier) throws QuerierPluginException {
      
      //'default' org.astrogrid.datacenter.queriers.test.PrecannedPlugin should be given in default config, not here.
      String querierClass = ConfigFactory.getCommonConfig().getString(QUERIER_PLUGIN_KEY);

      try {
         Class qClass = Class.forName(querierClass);

         /* NWW - interesting bug here.
          original code used class.newInstance(); this method doesn't declare it throws InvocationTargetException,
          however, this exception _is_ thrown if an exception is thrown by the constructor (as is often the case at the moment)
          worse, as InvocatioinTargetException is a checked exception, the compiler rejects code with a catch clause for
          invocationTargetExcetpion - as it thinks it cannot be thrown.
          this means the exception boils out of the code, and is unstoppable - dodgy
          work-around - use the equivalent methods on java.lang.reflect.Constructor - which do throw the correct exceptions */

//         Constructor constr = qClass.getConstructor(new Class[] {Querier.class});
//         return (QuerierPlugin) constr.newInstance(new Object[] {querier});
         Constructor constr = qClass.getConstructor(new Class[] {});
         return (QuerierPlugin) constr.newInstance(new Object[] {});
      }
      catch (ClassNotFoundException cnfe) {
         throw new QuerierPluginException("Server not configured properly: plugin '"+querierClass+"' not found", cnfe);
      }
      catch (ClassCastException cce) {
         throw new QuerierPluginException("Server not configured properly: plugin '"+querierClass+"' is not a Querier subclass", cce);
      }
      catch (InvocationTargetException e) {
         throw new QuerierPluginException("Querier '"+querierClass+"' constructor failed: "+e, e.getCause());
      }
      catch (Exception e) {
         throw new QuerierPluginException("Could not load Querier '"+querierClass+"': "+e,e);
      }
   }
   
   
   
}

/*
 $Log: QuerierPluginFactory.java,v $
 Revision 1.1  2009/05/13 13:20:26  gtr
 *** empty log message ***

 Revision 1.2  2005/03/21 18:45:55  mch
 Naughty big lump of changes

 Revision 1.1.1.1  2005/02/17 18:37:35  mch
 Initial checkin

 Revision 1.1.1.1  2005/02/16 17:11:24  mch
 Initial checkin

 Revision 1.5  2004/10/18 13:11:30  mch
 Lumpy Merge

 Revision 1.4.2.1  2004/10/15 19:59:05  mch
 Lots of changes during trip to CDS to improve int test pass rate

 Revision 1.4  2004/10/08 17:14:22  mch
 Clearer separation of metadata and querier plugins, and improvements to VoResource plugin mechanisms

 Revision 1.3  2004/10/08 15:17:20  mch
 Removed unnecessary imports

 Revision 1.2  2004/09/28 16:38:05  mch
 Removed 4.1 interface

 Revision 1.1  2004/09/28 15:02:13  mch
 Merged PAL and server packages

 Revision 1.2  2004/03/13 23:38:46  mch
 Test fixes and better front-end JSP access

 Revision 1.1  2004/03/12 04:45:26  mch
 It05 MCH Refactor

 */
