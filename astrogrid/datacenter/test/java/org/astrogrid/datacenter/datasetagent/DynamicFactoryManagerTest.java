/*$Id: DynamicFactoryManagerTest.java,v 1.5 2003/08/25 22:01:17 mch Exp $
 * Created on 21-Aug-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.datacenter.datasetagent;

import java.util.Map;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.astrogrid.AstroGridException;
import org.astrogrid.datacenter.FactoryProvider;
import org.astrogrid.datacenter.FactoryProviderTestSpec;
import org.astrogrid.datacenter.config.Configuration;
import org.astrogrid.datacenter.config.ConfigurationKeys;
import org.astrogrid.datacenter.config.FactoryManagerTest;
import org.astrogrid.datacenter.impl.empty.NullJobFactory;
import org.astrogrid.datacenter.impl.empty.NullQueryFactory;
import org.astrogrid.datacenter.query.QueryFactory;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Aug-2003
 *
 */
public class DynamicFactoryManagerTest extends FactoryManagerTest {

    /**
     * Constructor for DynamicFactoryManagerTest.
     * @param arg0
     */
    public DynamicFactoryManagerTest(String arg0) {
        super(arg0);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(DynamicFactoryManagerTest.class);
    }
    public static Test suite() {
        return new TestSuite(DynamicFactoryManagerTest.class);
    }
    protected FactoryProvider createEmptyFactoryProvider() {
        return new DynamicFactoryManager(conf);
    }
    protected FactoryProvider createPopulatedFactoryProvider() {
        populateMap();
        DynamicFactoryManager fac = new DynamicFactoryManager(conf);
        try {
             fac.verify();
         } catch (AstroGridException e) {
             fail("failed to create a populated facotry provider");
         }
         return fac;
     }

    protected void populateMap() {
        internalMap.put(JOB_KEY,NullJobFactory.class.getName());
        internalMap.put(QUERY_KEY,NullQueryFactory.class.getName());
        internalMap.put(CATALOG_QUERY_KEY,NullQueryFactory.class.getName());
    }



    /** overridden to provide a configuration with some entries present */
    protected void setUp() {
        internalMap =  new java.util.HashMap();
        conf = new MapConfiguration(internalMap);
        dynFacMan = new DynamicFactoryManager(conf);
        facMan = dynFacMan;

    }
    protected Map internalMap;
    protected DynamicFactoryManager dynFacMan;
    // statics
    public final static String JOB_KEY = MapConfiguration.mkKey(ConfigurationKeys.JOB_FACTORY,ConfigurationKeys.JOB_CATEGORY);
    public final static String MYSPACE_KEY = MapConfiguration.mkKey(ConfigurationKeys.MYSPACE_FACTORY,ConfigurationKeys.MYSPACE_CATEGORY);
    public final static String QUERY_KEY = MapConfiguration.mkKey(ConfigurationKeys.CATALOG_DEFAULT_QUERYFACTORY,ConfigurationKeys.CATALOG_CATEGORY);
    public final static String VOTABLE_KEY = MapConfiguration.mkKey(ConfigurationKeys.VOTABLE_FACTORY,ConfigurationKeys.VOTABLE_CATEGORY);
    public final static String CATALOG_QUERY_KEY = MapConfiguration.mkKey(FactoryProviderTestSpec.TEST_QUERY_FACTORY_KEY +ConfigurationKeys.CATALOG_DEFAULT_QUERYFACTORY,ConfigurationKeys.CATALOG_CATEGORY);

    public void testLoadJobFactory() throws AstroGridException
    {
        try {
        dynFacMan.loadJobFactory();
        fail("expected to barf");
        } catch (AstroGridException e) {
        }

        internalMap.put(JOB_KEY,"unknown.class");
        try {
            dynFacMan.loadJobFactory();
            fail("expected to barf");
        } catch (AstroGridException e) {
        }
        internalMap.put(JOB_KEY,NullJobFactory.class.getName());
        dynFacMan.loadJobFactory();
    }

    public void testLoadDefaultQueryFactory() throws AstroGridException
    {
         try {
         dynFacMan.loadDefaultQueryFactory();
         fail("expected to barf");
         } catch (AstroGridException e) {
         }

         internalMap.put(QUERY_KEY,"unknown.class");
         try {
             dynFacMan.loadDefaultQueryFactory();
             fail("expected to barf");
         } catch (AstroGridException e) {
         }
         internalMap.put(QUERY_KEY,NullQueryFactory.class.getName());
         dynFacMan.loadDefaultQueryFactory();
     }

    public void testGetMissingQueryFactory() throws AstroGridException
     {
        // first set up dynFacMan with a default.
        internalMap.put(QUERY_KEY,NullQueryFactory.class.getName());
        dynFacMan.loadDefaultQueryFactory();
        // attempting to load an unknown query class should return the default
        QueryFactory qf = dynFacMan.getQueryFactory("unknownCatalog");
        assertNotNull(qf);
        assertSame(qf,dynFacMan.getDefaultQueryFactory());
        // and catalog name will not be present now.
        assertFalse(dynFacMan.isQueryFactoryAvailable("unknownCatalog"));
    }

    public void testGetPresentQueryFactory() throws AstroGridException
    {
        // first set up dynFacMan with a default.
        internalMap.put(QUERY_KEY,NullQueryFactory.class.getName());
        dynFacMan.loadDefaultQueryFactory();
        // now an entry for a known catalog.
        String key = MapConfiguration.mkKey("knownCatalog" + ConfigurationKeys.CATALOG_DEFAULT_QUERYFACTORY,ConfigurationKeys.CATALOG_CATEGORY);
        internalMap.put(key,NullQueryFactory.class.getName());
        QueryFactory qf = dynFacMan.getQueryFactory("knownCatalog");
        assertNotNull(qf);
        assertNotSame(qf,dynFacMan.getDefaultQueryFactory());
        // and catalog name will be present now.
        assertTrue(dynFacMan.isQueryFactoryAvailable("knownCatalog"));

    }

    public void testVerify() throws AstroGridException
    {
        try {
            // verifying an empty / uninitialized factory manager should barf.
            dynFacMan.verify();
            fail("Should raise DatacenterException");
        } catch (AstroGridException e) {
        }
        // verifying a full one shouldn't complain.
        populateMap();
        dynFacMan.verify();
    }

    /** simple implementation of a configuration that wraps a map object. - useful for testing when we don't
      * want to load confifgurationdata from the filesystem
      * @author Noel Winstanley nw@jb.man.ac.uk 21-Aug-2003
      *
      */
     public static class MapConfiguration implements Configuration {
         public MapConfiguration(Map m) {
             this.m = m;
         }
         protected final Map m;
         /* generates composite key in form <i>category</i>.<i>key</i>,
          * and uses this to look up property in the map.
          */
         public String getProperty(String key, String category) {
             Object o = m.get(mkKey(key,category));
             //assertNotNull(category + "." + key,o);
             //return o.toString();
             return (String)o;
         }
         public static String mkKey(String key,String category) {
                return category + "." + key;
         }
     }


}


/*
$Log: DynamicFactoryManagerTest.java,v $
Revision 1.5  2003/08/25 22:01:17  mch
Removed DatacenterException

Revision 1.4  2003/08/25 21:47:57  mch
Removed VOTable-middleman classes (to replace with more general ResultSet)

Revision 1.3  2003/08/25 21:05:36  mch
Removed dummy MySpace-related classes

Revision 1.2  2003/08/22 10:37:07  nw
tidied imports

Revision 1.1  2003/08/21 12:29:18  nw
added unit testing for factory manager hierarchy.
added 'AllTests' suite classes to draw unit tests together - single
place to run all.

*/
