package org.astrogrid.datacenter.config;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.datacenter.DatacenterException;
import org.astrogrid.datacenter.FactoryProvider;
import org.astrogrid.datacenter.FactoryProviderTestSpec;
import org.astrogrid.datacenter.job.JobFactory;
import org.astrogrid.datacenter.query.QueryException;
import org.astrogrid.datacenter.query.QueryFactory;
import org.astrogrid.datacenter.votable.VOTableFactory;

/**
 * JUnit test case for FactoryManagerTest
 * extends FactoryProvider test with tests of the setters, and checks that the set factories are initialized via setConfiguration.
 */

public class FactoryManagerTest extends FactoryProviderTestSpec implements InvocationHandler {
   //declare reusable objects to be used across multiple tests
   public FactoryManagerTest(String name) {
      super(name);
   }
   public static void main(String[] args) {
      junit.textui.TestRunner.run(FactoryManagerTest.class);
   }
   public static Test suite() {
      return new TestSuite(FactoryManagerTest.class);
   }
    protected void setUp() {
        facMan = new FactoryManager(conf);
    }
    protected FactoryProvider createEmptyFactoryProvider() {
        return new FactoryManager(conf);
    }
    protected FactoryProvider createPopulatedFactoryProvider() {
        super.setUp();
        FactoryManager fac = new FactoryManager(conf);
        populateFactoryManager(fac);
        return fac;
    }
    protected void populateFactoryManager(FactoryManager fac) {
        // initialize here
        fac.setDefaultQueryFactory((QueryFactory)createFactory(QueryFactory.class));
        fac.setJobFactory((JobFactory)createFactory(JobFactory.class));
        //fac.setMySpaceFactory((MySpaceFactory)createFactory(MySpaceFactory.class)); no longer used
        fac.setVOTableFactory((VOTableFactory)createFactory(VOTableFactory.class));
        fac.setQueryFactory(FactoryProviderTestSpec.TEST_QUERY_FACTORY_KEY,(QueryFactory)createFactory(QueryFactory.class));
    }

    protected FactoryManager facMan;
    /** set to true if a setConfiguration() call is encountered */
    protected boolean setConfigurationSeen = false;

   public void testJobFactoryNull() {
    try {
       facMan.setJobFactory(null);
       fail("should have barfed");
    } catch (IllegalArgumentException e) {
    }
   }

    public void testVOTableFactoryNull() {
        try {
            facMan.setVOTableFactory(null);
            fail("should have barfed");
        } catch (IllegalArgumentException e) {
        }
    }
    public void testDefaultQueryFactoryNull() {
        try {
            facMan.setDefaultQueryFactory(null);
            fail("should have barfed");
        } catch (IllegalArgumentException e) {
        }
    }

   public void testQueryFactoryNull() {
        try {
            facMan.setQueryFactory(null,(QueryFactory)createFactory(QueryFactory.class));
            fail("should have barfed");
        } catch (IllegalArgumentException e) {
        }
      try {
           facMan.setQueryFactory("foo",null);
           fail("should have barfed");
       } catch (IllegalArgumentException e) {
       }
   }


   public void testVerify() throws DatacenterException{

      try {
         // verifying an empty / uninitialized factory manager should barf.
         facMan.verify();
         fail("Should raise DatacenterException");
      } catch (DatacenterException e) {
      }

        // verifying a full one shouldn't complain.
        populateFactoryManager(facMan);
        facMan.verify();
   }

   public void testDefaultQueryFactoryVal() {
        QueryFactory q = (QueryFactory)createFactory(QueryFactory.class);
        facMan.setDefaultQueryFactory(q);
        QueryFactory q1 = facMan.getDefaultQueryFactory();
        assertSame(q,q1);
        assertTrue(setConfigurationSeen);
   }
   public void testVOTableFactoryVal() {
        VOTableFactory v = (VOTableFactory)createFactory(VOTableFactory.class);
        facMan.setVOTableFactory(v);
        VOTableFactory v1 = facMan.getVOTableFactory();
        assertSame(v,v1);
        assertTrue(setConfigurationSeen);
   }
   public void testJobFactoryVal() {
        JobFactory j = (JobFactory)createFactory(JobFactory.class);
        facMan.setJobFactory(j);
        JobFactory j1 = facMan.getJobFactory();
        assertSame(j,j1);
        assertTrue(setConfigurationSeen);
   }

    public void testQueryFactoryVal() throws QueryException{
        QueryFactory q = (QueryFactory) createFactory(QueryFactory.class);
        assertFalse(facMan.isQueryFactoryAvailable("foo"));
        facMan.setQueryFactory("foo",q);
        assertTrue(facMan.isQueryFactoryAvailable("foo"));
        QueryFactory q1 = facMan.getQueryFactory("foo");
        assertSame(q,q1);
        assertTrue(setConfigurationSeen);
    }

    //helper methods
    /** create a dummy factory implementation */
    protected Object createFactory(Class factoryInterface) {
        return Proxy.newProxyInstance(this.getClass().getClassLoader(),new Class[] {factoryInterface},this);

    }

    /* 'callback' method from proxy classes
     if the set configuration method is called, sets a boolean flag which is then checked in test case.*/
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().equals("setConfiguration")) {
            setConfigurationSeen = true;
        }
        return null;
    }
}
