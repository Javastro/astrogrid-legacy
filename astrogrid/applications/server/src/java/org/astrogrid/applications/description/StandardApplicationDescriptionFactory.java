/*
 * $Id: StandardApplicationDescriptionFactory.java,v 1.2 2011/09/02 21:55:49 pah Exp $
 * 
 * Created on 9 Jul 2009 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2009 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.description;

import org.astrogrid.applications.commandline.CommandLineApplicationDescription;
import org.astrogrid.applications.contracts.Configuration;
import org.astrogrid.applications.dal.DALDbApplication;
import org.astrogrid.applications.dal.DALDbApplicationDescription;
import org.astrogrid.applications.db.DBApplicationDescription;
import org.astrogrid.applications.db.FromMetadataDataSourceFinder;
import org.astrogrid.applications.description.base.ApplicationBase;
import org.astrogrid.applications.description.exception.ApplicationDescriptionNotLoadedException;
import org.astrogrid.applications.description.impl.CeaCmdLineApplicationDefinition;
import org.astrogrid.applications.description.impl.CeaDBApplicationDefinition;
import org.astrogrid.applications.description.impl.CeaHttpApplicationDefinition;
import org.astrogrid.applications.http.HttpApplicationDescription;
import org.springframework.stereotype.Component;

/**
 * Standard Factory for creating application descriptions from the XML based description.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 14 Jul 2009
 * @version $Name:  $
 * @since AIDA Stage 1
 */
@Component
public class StandardApplicationDescriptionFactory implements
        BaseApplicationDescriptionFactory {

    protected final Configuration conf;

    public StandardApplicationDescriptionFactory(
            Configuration conf) {
        
        this.conf = conf;
    }

    public ApplicationDescription addApp(ApplicationBase apptyp, MetadataAdapter ma) throws ApplicationDescriptionNotLoadedException {
        if (apptyp instanceof CeaCmdLineApplicationDefinition) {
            return new CommandLineApplicationDescription(
                            conf, ma);
        } else if (apptyp instanceof CeaDBApplicationDefinition) {
           return (new DALDbApplicationDescription(ma, conf, new FromMetadataDataSourceFinder((CeaDBApplicationDefinition) apptyp)));
        } else if (apptyp instanceof CeaHttpApplicationDefinition) {
           return (new HttpApplicationDescription(conf,
                    ma));
        }
        else {
            throw new ApplicationDescriptionNotLoadedException("application has unknown type "+ ma.getResource().getIdentifier());
        }
    }

}


/*
 * $Log: StandardApplicationDescriptionFactory.java,v $
 * Revision 1.2  2011/09/02 21:55:49  pah
 * result of merging the 2931 branch
 *
 * Revision 1.1.2.1  2009/07/15 13:04:29  pah
 * update to impl v2.1
 *
 */
