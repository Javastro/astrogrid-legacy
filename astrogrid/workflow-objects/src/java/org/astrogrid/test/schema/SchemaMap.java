/*$Id: SchemaMap.java,v 1.1 2004/09/02 01:31:15 nw Exp $
 * Created on 01-Sep-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.test.schema;

import java.util.HashMap;
import java.util.Map;

/** static class that provides maps of all namespace - schema locations in this project.
 * useful to pass to schema-validation methods in {@link org.astrogrid.test.AstrogridAssert}
 * @author Noel Winstanley nw@jb.man.ac.uk 01-Sep-2004
 *@ see org.astrogrid.test.AstrogridAssert
 */
public class SchemaMap {

    /** Construct a new SchemaMap
     * 
     */
    private SchemaMap() {
    }
    
    public static final Map ALL; 
    
    static {
        // populate the map.
        ALL = new HashMap();
        ALL.put("http://www.astrogrid.org/schema/CommonExecutionArchitectureBase/v1", SchemaMap.class.getResource("/schema/AGApplicationBase.xsd"));
        ALL.put("http://www.astrogrid.org/schema/AGParameterDefinition/v1",SchemaMap.class.getResource("/schema/AGParameterDefinition.xsd"));
        ALL.put("http://www.astrogrid.org/schema/CEATypes/v1",SchemaMap.class.getResource("/schema/CEATypes.xsd"));
        ALL.put("http://www.astrogrid.org/schema/Credentials/v1",SchemaMap.class.getResource("/schema/Credentials.xsd"));
        ALL.put("http://www.astrogrid.org/schema/ExecutionRecord/v1",SchemaMap.class.getResource("/schema/ExecutionRecord.xsd"));
        ALL.put("urn:jes/types/v1",SchemaMap.class.getResource("/schema/JesTypes.xsd"));
        ALL.put("http://www.ivoa.net/xml/CEAService/v0.1",SchemaMap.class.getResource("/schema/VOCEA.xsd"));
        ALL.put("http://www.astrogrid.org/schema/AGWorkflow/v1",SchemaMap.class.getResource("/schema/Workflow.xsd"));
   
        ALL.put("http://ivoa.org/Accuracy",SchemaMap.class.getResource("/schema/externalSchema/quantity/Accuracy.xsd"));
        ALL.put("http://ivoa.org/CoordinateSystems",SchemaMap.class.getResource("/schema/externalSchema/quantity/CoordinateSystems.xsd"));
        ALL.put("http://ivoa.org/Mappings",SchemaMap.class.getResource("/schema/externalSchema/quantity/Mappings.xsd"));
        ALL.put("http://ivoa.org/Quantity",SchemaMap.class.getResource("/schema/externalSchema/quantity/Quantity.xsd"));
                
        ALL.put("urn:nvo-coords",SchemaMap.class.getResource("/schema/externalSchema/stc/coords.xsd"));
        ALL.put("urn:nvo-region",SchemaMap.class.getResource("/schema/externalSchema/stc/region.xsd"));
        ALL.put("urn:nvo-stc",SchemaMap.class.getResource("/schema/externalSchema/stc/stc.xsd"));
                             
        ALL.put("http://www.ivoa.net/xml/ConeSearch/v0.2",SchemaMap.class.getResource("/schema/externalSchema/ConeSearch-v0.2.xsd"));
        ALL.put("http://www.ivoa.net/xml/SIA/v0.6",SchemaMap.class.getResource("/schema/externalSchema/SIA-v0.6.xsd"));
        ALL.put("http://www.ivoa.net/xml/VOCommunity/v0.2",SchemaMap.class.getResource("/schema/externalSchema/VOCommunity-v0.2.xsd"));
        ALL.put("http://www.ivoa.net/xml/VODataService/v0.4",SchemaMap.class.getResource("/schema/externalSchema/VODataService-v0.4.xsd"));
   // VODataServiceCoverage-* omitted - no target namespace declared. doesn't look valid as a stand-alone schema 
        ALL.put("http://www.ivoa.net/xml/VORegistry/v0.2",SchemaMap.class.getResource("/schema/externalSchema/VORegistry-v0.2.xsd"));
        ALL.put("http://www.ivoa.net/xml/VOResource/v0.9",SchemaMap.class.getResource("/schema/externalSchema/VOResource-v0.9.xsd"));
   // likewise, omitted VOResourceRelType-* 
        ALL.put("http://www.ivoa.net/xml/VOTable/v1.0",SchemaMap.class.getResource("/schema/externalSchema/VOTable.xsd"));
        // isn't there a more recent version of votable now too?
                                                                                            
    }
    

}


/* 
$Log: SchemaMap.java,v $
Revision 1.1  2004/09/02 01:31:15  nw
added in all external schemas to jar (for testing).
wrote static that lists all namesapces and system IDs for schemas.
 
*/