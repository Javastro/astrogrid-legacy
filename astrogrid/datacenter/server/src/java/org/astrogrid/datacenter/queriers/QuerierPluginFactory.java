/*$Id: QuerierPluginFactory.java,v 1.1 2004/03/12 04:45:26 mch Exp $
 * Created on 24-Sep-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 **/
package org.astrogrid.datacenter.queriers;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.axisdataserver.types.Query;
import org.astrogrid.datacenter.query.QueryState;
import org.astrogrid.store.Agsl;

/** Manages a single query; this is the root for a chain of possible chain
 * of plugins, translators, etc. It makes the right kind of querier for the job,
 * and fields any things that are not directly
 * to do with the actual querying process
 *
 */
public class QuerierPluginFactory {
   
   private static final Log log = LogFactory.getLog(QuerierPluginFactory.class);
   
   /** Key to which plugin (which sublcass of Querier) to use */
   public static final String DATABASE_QUERIER_KEY = "DatabaseQuerierPlugin";
   
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
      String querierClass = SimpleConfig.getSingleton().getString(DATABASE_QUERIER_KEY);

      try {
         Class qClass = Class.forName(querierClass);

         /* NWW - interesting bug here.
          original code used class.newInstance(); this method doesn't declare it throws InvocationTargetException,
          however, this exception _is_ thrown if an exception is thrown by the constructor (as is often the case at the moment)
          worse, as InvocatioinTargetException is a checked exception, the compiler rejects code with a catch clause for
          invocationTargetExcetpion - as it thinks it cannot be thrown.
          this means the exception boils out of the code, and is unstoppable - dodgy
          work-around - use the equivalent methods on java.lang.reflect.Constructor - which do throw the correct exceptions */

         Constructor constr = qClass.getConstructor(new Class[] {Querier.class});
         return (QuerierPlugin) constr.newInstance(new Object[] {querier});
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
 Revision 1.1  2004/03/12 04:45:26  mch
 It05 MCH Refactor

 */
