/*$Id: DynamicFactoryManagerTest.java,v 1.1 2003/08/21 12:29:18 nw Exp $
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

import junit.framework.*;
import java.util.Map;
import org.astrogrid.datacenter.config.*;
import org.astrogrid.datacenter.impl.empty.*;
import org.astrogrid.datacenter.FactoryProvider;
import org.astrogrid.datacenter.DatacenterException;
import org.astrogrid.datacenter.FactoryProviderTest;
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
         } catch (DatacenterException e) {
             fail("failed to create a populated facotry provider");
         }
         return fac;
     }
     
    protected void populateMap() {
        internalMap.put(JOB_KEY,NullJobFactory.class.getName());
        internalMap.put(VOTABLE_KEY,NullVOTableFactory.class.getName());
        internalMap.put(QUERY_KEY,NullQueryFactory.class.getName());
        internalMap.put(MYSPACE_KEY,NullMySpaceFactory.class.getName());
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
    public final static String CATALOG_QUERY_KEY = MapConfiguration.mkKey(FactoryProviderTest.TEST_QUERY_FACTORY_KEY +ConfigurationKeys.CATALOG_DEFAULT_QUERYFACTORY,ConfigurationKeys.CATALOG_CATEGORY);
    public void testLoadJobFactory() throws DatacenterException {
        try {
        dynFacMan.loadJobFactory();
        fail("expected to barf");
        } catch (DatacenterException e) {
        }

        internalMap.put(JOB_KEY,"unknown.class");
        try {
            dynFacMan.loadJobFactory();
            fail("expected to barf");
        } catch (DatacenterException e) {
        }
        internalMap.put(JOB_KEY,NullJobFactory.class.getName());
        dynFacMan.loadJobFactory();
    }

    public void testLoadMySpaceFactory() throws DatacenterException {
         try {
         dynFacMan.loadMySpaceFactory();
         fail("expected to barf");
         } catch (DatacenterException e) {
         }

         internalMap.put(MYSPACE_KEY,"unknown.class");
         try {
             dynFacMan.loadMySpaceFactory();
             fail("expected to barf");
         } catch (DatacenterException e) {
         }
         internalMap.put(MYSPACE_KEY,NullMySpaceFactory.class.getName());
         dynFacMan.loadMySpaceFactory();
     }
    public void testLoadDefaultQueryFactory() throws DatacenterException {
         try {
         dynFacMan.loadDefaultQueryFactory();
         fail("expected to barf");
         } catch (DatacenterException e) {
         }

         internalMap.put(QUERY_KEY,"unknown.class");
         try {
             dynFacMan.loadDefaultQueryFactory();
             fail("expected to barf");
         } catch (DatacenterException e) {
         }
         internalMap.put(QUERY_KEY,NullQueryFactory.class.getName());
         dynFacMan.loadDefaultQueryFactory();
     }
    public void testLoadVOTableFactory() throws DatacenterException {
         try {
         dynFacMan.loadVOTableFactory();
         fail("expected to barf");
         } catch (DatacenterException e) {
         }

         internalMap.put(VOTABLE_KEY,"unknown.class");
         try {
             dynFacMan.loadVOTableFactory();
             fail("expected to barf");
         } catch (DatacenterException e) {
         }
         internalMap.put(VOTABLE_KEY,NullVOTableFactory.class.getName());
         dynFacMan.loadVOTableFactory();
     }
 
    public void testGetMissingQueryFactory() throws DatacenterException{
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
    
    public void testGetPresentQueryFactory() throws DatacenterException{
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

    public void testVerify() throws DatacenterException{
        try {
            // verifying an empty / uninitialized factory manager should barf.
            dynFacMan.verify();
            fail("Should raise DatacenterException");
        } catch (DatacenterException e) {
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
Revision 1.1  2003/08/21 12:29:18  nw
added unit testing for factory manager hierarchy.
added 'AllTests' suite classes to draw unit tests together - single
place to run all.
 
*/