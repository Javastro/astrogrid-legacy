/* $Id: FactoryManager.java,v 1.3 2003/08/25 20:51:16 mch Exp $
 * Created on 19-Aug-2003
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 */
package org.astrogrid.datacenter.config;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.astrogrid.datacenter.DatacenterException;
import org.astrogrid.datacenter.FactoryProvider;
import org.astrogrid.datacenter.job.JobFactory;
import org.astrogrid.datacenter.query.QueryException;
import org.astrogrid.datacenter.query.QueryFactory;
import org.astrogrid.datacenter.votable.VOTableFactory;
import org.astrogrid.i18n.AstroGridMessage;

/** class to manage a set of factory objects - an implementation of {@link org.astrogrid.datacenter.FactoryProvider}
 *
 *<p>
 * Maintains a reference to a {@link Configuration}, and has member fields for each factory (plus a {@link java.util.Map} of additional query factories.
 * <p>
 * Provides a  bean-style getter and setter for each member field. Setter methods also ensures that factories are correctly configured by calling
 * {@link Configurable.setConfiguration} on factories as they are being added.
 * @author Noel Winstanley nw@jb.man.ac.uk 20-Aug-2003
 *@see org.astrogrid.datacenter.FactoryProvider
 */
public class FactoryManager implements FactoryProvider {
   private JobFactory jobFactory;
   private Map queryFactoryMap = new HashMap();
   private QueryFactory defaultQueryFactory;
   private VOTableFactory voTableFactory;
   protected final Configuration conf;
   /**
     * Construct a new factory manager
    * @param conf the configuration object to propagate on to all managed factories
    */
   public FactoryManager(Configuration conf) {
      this.conf = conf;
   }
// accessors - see org.astrogrid.datacenter.FactoryProvider for documentation
   public JobFactory getJobFactory() {
      return jobFactory;
   }

   public QueryFactory getQueryFactory(String catalogName)  throws QueryException{
      QueryFactory qf =  (QueryFactory)queryFactoryMap.get(catalogName);
      return (qf != null ? qf : defaultQueryFactory);
   }

   public VOTableFactory getVOTableFactory() {
      return voTableFactory;
   }

    public QueryFactory getDefaultQueryFactory() {
        return defaultQueryFactory;
    }

    public boolean isQueryFactoryAvailable(String catalogName) {
        return queryFactoryMap.containsKey(catalogName);
    }
    /** check that all factories are present
     * TODO - produce a proper astrogrid message for this.
     * @throws DatacenterException if any factory field is set to null
     */
    public void verify() throws DatacenterException {
       if (jobFactory == null /*|| mySpaceFactory == null */||  voTableFactory == null || defaultQueryFactory == null){
            throw new DatacenterException(new AstroGridMessage("some.factories.null"));
        }
    }
// mutators

    /** set the query factory for a particular catalog
     * @param catalogName the catalog this query factory is intended for
    * @param factory the factory for this catalog.
    */
   public void setQueryFactory(String catalogName,QueryFactory factory) throws IllegalArgumentException {
        if (factory == null) throw new IllegalArgumentException("Factory cannot be null");
        if (catalogName == null) throw new IllegalArgumentException("CatalogName cannot be null");
      factory.setConfiguration(conf);
      queryFactoryMap.put(catalogName,factory);
   }

   /** set the votable factory
    * @param factory
    */
   public void setVOTableFactory(VOTableFactory factory) throws IllegalArgumentException{
        if (factory == null) throw new IllegalArgumentException("Factory cannot be null");
      factory.setConfiguration(conf);
      voTableFactory = factory;
   }
    /**
     * set the job factory
     * @param factory
     */
    public void setJobFactory(JobFactory factory) throws IllegalArgumentException {
        if (factory == null) throw new IllegalArgumentException("Factory cannot be null");
        factory.setConfiguration(conf);
        jobFactory = factory;
    }
    /** set the default query factory - this is used when no other query factory has been associated with a catalog.
     * @param factory
     */
    public void setDefaultQueryFactory(QueryFactory factory) throws IllegalArgumentException {
        if (factory == null) throw new IllegalArgumentException("Factory cannot be null");
        factory.setConfiguration(conf);
        defaultQueryFactory = factory;
    }


    /** produce a string representation of this object
     * @return a formatted string with details of the classes implementing each of the factory types.
     */
   public String toString() {
      String qFactories = "";
      for (Iterator i = queryFactoryMap.entrySet().iterator();i.hasNext();){
         Map.Entry e = (Map.Entry)i.next();
         qFactories += e.getKey() + " := " + getFactoryInfo(e.getValue()) + ",";
      }
        if (qFactories.equals("")) {
            qFactories = "<none>";
        }
      return this.getClass().getName()
         + "\njobFactory " + getFactoryInfo(jobFactory)
         + "\nvoTableFactory " + getFactoryInfo(voTableFactory)
         + "\nDefaultQueryFactory" + getFactoryInfo(defaultQueryFactory)
      + "\nqueryFactories " + qFactories;

   }
    /** helper method to get class information for a factory */
   private String getFactoryInfo(Object o){
      return (o == null ? "<null>" : o.getClass().getName());
   }

}
/*
 * $Log: FactoryManager.java,v $
 * Revision 1.3  2003/08/25 20:51:16  mch
 * Removed dummy MySpace-related classes
 *
 * Revision 1.2  2003/08/21 12:24:17  nw
 * Added some armour - if methods are passed a null, when they
 * really can't handle that, they throw an illegalArgumentException
 * (rather than a null pointer later on).
 *
 * Revision 1.1  2003/08/20 14:42:59  nw
 * added a configuration package -
 * wraps the existing DTC class, and provides somewhere to
 * manage dynamically loaded factories.
 *
 */
