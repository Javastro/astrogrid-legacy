/*$Id: FactoryProviderTestSpec.java,v 1.2 2003/08/25 21:04:11 mch Exp $
 * Created on 21-Aug-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.datacenter;

import junit.framework.TestCase;

import org.astrogrid.datacenter.config.Configuration;
import org.astrogrid.datacenter.query.QueryException;
/** abstract test case that specifies contract of the FactoryProvider interface.
 * extends this for test cases for classes that implement this interface.
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Aug-2003
 *
 */
public abstract class FactoryProviderTestSpec extends TestCase {

    /**
     *
     */
    public FactoryProviderTestSpec(String name) {
        super(name);
    }
    /** extenders must extend this to create a factory provider (uninitialized)*/
    protected abstract FactoryProvider createEmptyFactoryProvider();

    /** extenders must provide an implementation that creates a populated factory provider
     * - i.e. it has all factoryies filled in,
     * plus an entry for {@link #TEST_QUERY_FACTORY_KEY} in the query factory map.
     * */
    protected abstract FactoryProvider createPopulatedFactoryProvider();
    public static final String TEST_QUERY_FACTORY_KEY = "someCatalog";
    protected void setUp() {
        conf = new Configuration() {
        public String getProperty(String k,String v) {
                return null;
            }
        };
    }
    protected Configuration conf;

    public void testEmptyFactoryManager() throws QueryException {
        FactoryProvider fac = createEmptyFactoryProvider();
        assertNotNull(fac);
        assertNull(fac.getDefaultQueryFactory());
        assertNull(fac.getJobFactory());
        assertNull(fac.getVOTableFactory());
        assertNull(fac.getQueryFactory("foo"));
        assertFalse(fac.isQueryFactoryAvailable("foo"));

    }

    public void testPopulatedFactoryManager() throws QueryException {
         FactoryProvider fac = createPopulatedFactoryProvider();
         assertNotNull(fac);
         assertNotNull(fac.getDefaultQueryFactory());
         assertNotNull(fac.getJobFactory());
         assertNotNull(fac.getQueryFactory("foo"));
         assertNotNull(fac.getVOTableFactory());
         assertFalse(fac.isQueryFactoryAvailable("foo"));
        assertTrue(fac.isQueryFactoryAvailable(TEST_QUERY_FACTORY_KEY));
        assertNotNull(fac.getQueryFactory(TEST_QUERY_FACTORY_KEY));
     }

    public void testToString() {

      assertNotNull(createEmptyFactoryProvider().toString());
        assertNotNull(createPopulatedFactoryProvider().toString());

    }
}


/*
$Log: FactoryProviderTestSpec.java,v $
Revision 1.2  2003/08/25 21:04:11  mch
Removed dummy MySpace-related classes

Revision 1.1  2003/08/22 10:38:17  nw
renamed from FactoryProviderTest - prevent maven trying to run this abstract test class.

Revision 1.1  2003/08/21 12:29:18  nw
added unit testing for factory manager hierarchy.
added 'AllTests' suite classes to draw unit tests together - single
place to run all.

*/
