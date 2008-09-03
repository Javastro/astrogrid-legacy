/*
 * $Id: BuiltInApplicationDescriptionTest.java,v 1.2 2008/09/03 14:19:02 pah Exp $
 * 
 * Created on 14 Jun 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.description;


import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.contracts.MockNonSpringConfiguredConfig;
import org.astrogrid.applications.description.base.ApplicationDescriptionEnvironment;
import org.astrogrid.applications.description.base.TestAuthorityResolver;
import org.astrogrid.applications.description.jaxb.DescriptionValidator;
import org.astrogrid.applications.manager.AppAuthorityIDResolver;
import org.astrogrid.applications.manager.idgen.IdGen;
import org.astrogrid.applications.manager.idgen.InMemoryIdGen;
import org.astrogrid.applications.parameter.protocol.FileProtocol;
import org.astrogrid.applications.parameter.protocol.Protocol;
import org.astrogrid.applications.parameter.protocol.ProtocolLibrary;
import org.junit.Before;
import org.junit.Test;

public class BuiltInApplicationDescriptionTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testDescription() throws CeaException{
	IdGen idgen = new InMemoryIdGen();
	
	ProtocolLibrary lib = new org.astrogrid.applications.parameter.protocol.DefaultProtocolLibrary(new Protocol[]{new FileProtocol()});
	
	AppAuthorityIDResolver resol = new TestAuthorityResolver();
	BuiltInApplicationDescription desc = new BuiltInApplicationDescription( new MockNonSpringConfiguredConfig());
	DescriptionValidator.validate(desc.getMetadataAdapter().getResource());
	
    }
}


/*
 * $Log: BuiltInApplicationDescriptionTest.java,v $
 * Revision 1.2  2008/09/03 14:19:02  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.1.2.2  2008/08/29 07:28:28  pah
 * moved most of the commandline CEC into the main server - also new schema for CEAImplementation in preparation for DAL compatible service registration
 *
 * Revision 1.1.2.1  2008/06/16 21:58:59  pah
 * altered how the description libraries fit together  - introduced the SimpleApplicationDescriptionLibrary to just plonk app descriptions into.
 *
 */
