/*$Id: DynamicFactoryManager.java,v 1.2 2003/08/21 12:26:26 nw Exp $
 * Created on 19-Aug-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
package org.astrogrid.datacenter.datasetagent;

import org.astrogrid.datacenter.DatacenterException;
import org.astrogrid.datacenter.Util;
import org.astrogrid.datacenter.config.Configuration;
import org.astrogrid.datacenter.config.ConfigurationKeys;
import org.astrogrid.datacenter.config.FactoryManager;
import org.astrogrid.datacenter.job.JobException;
import org.astrogrid.datacenter.job.JobFactory;
import org.astrogrid.datacenter.myspace.AllocationException;
import org.astrogrid.datacenter.myspace.MySpaceFactory;
import org.astrogrid.datacenter.query.QueryException;
import org.astrogrid.datacenter.query.QueryFactory;
import org.astrogrid.datacenter.votable.VOTableException;
import org.astrogrid.datacenter.votable.VOTableFactory;
import org.astrogrid.i18n.AstroGridMessage;

/**
 * Extended factory manager that loads factories dynamically from classfiles specified in a configuration object
 * <p>
 * Loading of factory classes is initiated by the {@link #verify} method - ensure this is called before attempting to access any of the factories within
 * the manager
 * <p>
 * <b>NW</b> - refactored the factory loading methods from each package into this central class. Factories shouldn't need to know how they are
 * loaded -- restricts their flexibility. Using this DynamicFactoryManager provides another level of indirection, and allows for factories to be specified / provided
 * in other ways if needed.
 * FUTURE - replace disparate exception types with a single 'loadException' or something.
 * @author Noel Winstanley nw@jb.man.ac.uk 20-Aug-2003
 *@see org.astrogrid.config.FactoryManager
 *@see org.astrogrid.config.Configuration
 */
public class DynamicFactoryManager extends FactoryManager {
    private static final String ASTROGRIDERROR_COULD_NOT_CREATE_JOBFACTORY_IMPL =
        "AGDTCE00140";

    private static final String ASTROGRIDERROR_COULD_NOT_CREATE_MYSPACEFACTORY_IMPL =
        "AGDTCE00090";

    private final static String ASTROGRIDERROR_COULD_NOT_CREATE_QUERYFACTORY_IMPL =
        "AGDTCE00050";
    private static String ASTROGRIDERROR_COULD_NOT_CREATE_VOTABLEFACTORY_IMPL =
        "AGDTCE00120";

    private static final String SUBCOMPONENT_NAME = Util.getComponentName(DynamicFactoryManager.class);
    
    public DynamicFactoryManager(Configuration conf) {
        super(conf);
    }

   
/**
 *  Extended method that, if query factory for this catalog is not already loaded, will check the configuration for an appropriate mapping between catalog and
 * query factory, and load a factory if specified. Otherwise will return the default query factory
 * @see org.astrogrid.datacenter.FactoryProvider#getQueryFactory
 */
    public QueryFactory getQueryFactory(String catalogName)
        throws QueryException {
        if (super.isQueryFactoryAvailable(catalogName)) { // use the original implementation, as just looks in the hash map.
            return super.getQueryFactory(catalogName);
        }
        String implementationFactoryName = conf.getProperty(
                    catalogName + ConfigurationKeys.CATALOG_DEFAULT_QUERYFACTORY,
                    ConfigurationKeys.CATALOG_CATEGORY);

        // If we couldn't find a specific factory in the properties file,
        // Then look for a default factory...
        if (implementationFactoryName == null) {
            return getDefaultQueryFactory();
        }
        try {
            QueryFactory obj = (QueryFactory) Class.forName(implementationFactoryName).newInstance();
            this.setQueryFactory(catalogName, obj);
            return obj;
        } catch (Exception ex) {
            AstroGridMessage message =
                new AstroGridMessage(ASTROGRIDERROR_COULD_NOT_CREATE_QUERYFACTORY_IMPL,
                    SUBCOMPONENT_NAME,
                    implementationFactoryName);
            throw new QueryException(message, ex);
        }
    }
    /** overridden implementation - checks  the configuration file, to see if there is 
     * an entry for this catalog in it, and the internal cache.
     */
    public boolean isQueryFactoryAvailable(String catalogName) {
        String className = conf.getProperty(catalogName + ConfigurationKeys.CATALOG_DEFAULT_QUERYFACTORY
                                        , ConfigurationKeys.CATALOG_CATEGORY) ;
        return className != null || super.isQueryFactoryAvailable(catalogName);            
    }
    
    /** implementation method to load the default query factory into the factory manager, 
     * based on settings in the configuration.
     * @throws QueryException if an error occurs while loading
     * @see #verify
     */
    protected void loadDefaultQueryFactory() throws QueryException {

        String implementationFactoryName =  conf.getProperty(
                ConfigurationKeys.CATALOG_DEFAULT_QUERYFACTORY,
                ConfigurationKeys.CATALOG_CATEGORY);

        try {
            Object obj = Class.forName(implementationFactoryName).newInstance();
            this.setDefaultQueryFactory((QueryFactory) obj);
        } catch (Exception ex) {
            AstroGridMessage message =   new AstroGridMessage(
                    ASTROGRIDERROR_COULD_NOT_CREATE_QUERYFACTORY_IMPL,
                    SUBCOMPONENT_NAME,
                    implementationFactoryName);
            throw new QueryException(message, ex);
        }
        
    }
/**
 * implementation method to load the job factory into the facotry manager,
 * based on settings in the configuration
 * @throws JobException if an error occurs while loading the factory
 * @see #verify
 */
    protected void loadJobFactory() throws JobException {

        String implementationFactoryName = conf.getProperty(
                        ConfigurationKeys.JOB_FACTORY,
                        ConfigurationKeys.JOB_CATEGORY);
        try {
            Object obj = Class.forName(implementationFactoryName).newInstance();
            this.setJobFactory((JobFactory) obj);

        } catch (Exception ex) {
            AstroGridMessage message =   new AstroGridMessage( ASTROGRIDERROR_COULD_NOT_CREATE_JOBFACTORY_IMPL,
                             SUBCOMPONENT_NAME, implementationFactoryName);
            throw new JobException(message, ex);
        }
    }

    /** implemetation method to load a my space factory into the manager, based on properties in the configuration
     * 
     * @throws AllocationException if the myspacemanager cannot be loaded.
     * @see #verify
     */
    protected void loadMySpaceFactory() throws AllocationException {

        String implementationFactoryName =   conf.getProperty(  ConfigurationKeys.MYSPACE_FACTORY,  ConfigurationKeys.MYSPACE_CATEGORY);

        try {
            Object obj = Class.forName(implementationFactoryName).newInstance();
            this.setMySpaceFactory((MySpaceFactory) obj);
        } catch (Exception ex) {
            AstroGridMessage message =  new AstroGridMessage(ASTROGRIDERROR_COULD_NOT_CREATE_MYSPACEFACTORY_IMPL,
                                SUBCOMPONENT_NAME,  implementationFactoryName);
            throw new AllocationException(message, ex);
        }
    }

    /** implmenetation method to load a votable factory into the manager, based on properties in the cnfiguration
     * 
     * @throws VOTableException if the otable manager cannot be loaded
     * @see #verify
     */
    protected void loadVOTableFactory() throws VOTableException {

        String implementationFactoryName = conf.getProperty(  ConfigurationKeys.VOTABLE_FACTORY,ConfigurationKeys.VOTABLE_CATEGORY);
        try {
            Object obj = Class.forName(implementationFactoryName).newInstance();
            this.setVOTableFactory((VOTableFactory) obj);
        } catch (Exception ex) {
            AstroGridMessage message = new AstroGridMessage( ASTROGRIDERROR_COULD_NOT_CREATE_VOTABLEFACTORY_IMPL,
                             SUBCOMPONENT_NAME, implementationFactoryName);
            throw new VOTableException(message, ex);
        }
    }

    /**Loads all factory classes specified in the configuration file, and then checks all is well with the manager. 
     * @see org.astrogrid.datacenter.FactoryManager#verify()
     */
    public void verify() throws DatacenterException {
        loadJobFactory();
        loadMySpaceFactory();
        loadVOTableFactory();
        loadDefaultQueryFactory();
        super.verify();
    }

}
/*
$Log: DynamicFactoryManager.java,v $
Revision 1.2  2003/08/21 12:26:26  nw
fixed a but raised by testing - behaviour of isQueryFactoryAvailable
should depend on config object, rather than internal cache

Revision 1.1  2003/08/20 14:45:39  nw
altered the datasetAgent to use the new configuration sysem
in the config package. Provided a factory manager that
loads factory classes based on entries in configuration file.

*/