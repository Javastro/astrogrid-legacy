/*$Id: RegistrySchemaMap.java,v 1.3 2005/01/07 14:14:25 jdt Exp $
 * Created on 01-Sep-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.registry.common;

import java.util.HashMap;
import java.util.Map;

/** static class that provides maps of all namespace - schema locations in this project.
 * useful to pass to schema-validation methods in {@link org.astrogrid.test.AstrogridAssert}
 * @author Noel Winstanley nw@jb.man.ac.uk 01-Sep-2004
 *@ see org.astrogrid.test.AstrogridAssert
 */
public class RegistrySchemaMap {

    /** Construct a new SchemaMap
     * 
     */
    private RegistrySchemaMap() {
    }
    
    public static final Map ALL; 
    
    static {
        // populate the map.
        ALL = new HashMap();
        ALL.put("http://www.astrogrid.org/schema/CommonExecutionArchitectureBase/v1", RegistrySchemaMap.class.getResource("/schema/cea/AGApplicationBase.xsd"));
        ALL.put("http://www.astrogrid.org/schema/AGParameterDefinition/v1",RegistrySchemaMap.class.getResource("/schema/cea/AGParameterDefinition.xsd"));
        ALL.put("http://www.astrogrid.org/schema/CEATypes/v1",RegistrySchemaMap.class.getResource("/schema/cea/CEATypes.xsd"));
        //ALL.put("http://www.astrogrid.org/schema/Credentials/v1",RegistrySchemaMap.class.getResource("/schema/Credentials.xsd"));
        //ALL.put("http://www.astrogrid.org/schema/ExecutionRecord/v1",SchemaMap.class.getResource("/schema/ExecutionRecord.xsd"));
        //ALL.put("urn:jes/types/v1",SchemaMap.class.getResource("/schema/JesTypes.xsd"));
        ALL.put("http://www.ivoa.net/xml/CEAService/v0.1",RegistrySchemaMap.class.getResource("/schema/cea/VOCEA-v0.1.xsd"));
        ALL.put("http://www.ivoa.net/xml/CEAService/v0.2",RegistrySchemaMap.class.getResource("/schema/cea/VOCEA-v0.2.xsd"));
        //ALL.put("http://www.astrogrid.org/schema/AGWorkflow/v1",RegistrySchemaMap.class.getResource("/schema/Workflow.xsd"));
   
        ALL.put("http://ivoa.org/Accuracy",RegistrySchemaMap.class.getResource("/schema/adql/Accuracy.xsd"));
        ALL.put("http://ivoa.org/CoordinateSystems",RegistrySchemaMap.class.getResource("/schema/adql/CoordinateSystems.xsd"));
        ALL.put("http://ivoa.org/Mappings",RegistrySchemaMap.class.getResource("/schema/adql/Mappings.xsd"));
        ALL.put("http://ivoa.org/Quantity",RegistrySchemaMap.class.getResource("/schema/adql/Quantity.xsd"));
                
        ALL.put("urn:nvo-coords",RegistrySchemaMap.class.getResource("/schema/adql/coords.xsd"));
        ALL.put("urn:nvo-region",RegistrySchemaMap.class.getResource("/schema/adql/region.xsd"));
        ALL.put("urn:nvo-stc",RegistrySchemaMap.class.getResource("/schema/adql/stc.xsd"));
        ALL.put("http://adql.ivoa.net/v0.73",RegistrySchemaMap.class.getResource("/schema/adql/ADQL-0.7.3.xsd"));
        ALL.put("http://www.ivoa.net/xml/ADQL/v0.7.4",RegistrySchemaMap.class.getResource("/schema/adql/ADQL-0.7.4.xsd"));
                                     
        ALL.put("http://www.ivoa.net/xml/RegistryInterface/v0.1",RegistrySchemaMap.class.getResource("/schema/registry/RegistryInterface-v0.1.xsd"));            
        ALL.put("http://www.ivoa.net/xml/ConeSearch/v0.2",RegistrySchemaMap.class.getResource("/schema/registry/ConeSearch-v0.2.xsd"));
        ALL.put("http://www.ivoa.net/xml/ConeSearch/v0.3",RegistrySchemaMap.class.getResource("/schema/registry/ConeSearch-v0.3.xsd"));
        ALL.put("http://www.ivoa.net/xml/SIA/v0.6",RegistrySchemaMap.class.getResource("/schema/registry/SIA-v0.6.xsd"));
        ALL.put("http://www.ivoa.net/xml/SIA/v0.7",RegistrySchemaMap.class.getResource("/schema/registry/SIA-v0.7.xsd"));
        ALL.put("http://www.ivoa.net/xml/VOCommunity/v0.2",RegistrySchemaMap.class.getResource("/schema/registry/VOCommunity-v0.2.xsd"));
        ALL.put("http://www.ivoa.net/xml/VODataService/v0.4",RegistrySchemaMap.class.getResource("/schema/registry/VODataService-v0.4.xsd"));
        ALL.put("http://www.ivoa.net/xml/VODataService/v0.5",RegistrySchemaMap.class.getResource("/schema/registry/VODataService-v0.5.xsd"));
   // VODataServiceCoverage-* omitted - no target namespace declared. doesn't look valid as a stand-alone schema 
        ALL.put("http://www.ivoa.net/xml/VORegistry/v0.2",RegistrySchemaMap.class.getResource("/schema/registry/VORegistry-v0.2.xsd"));
        ALL.put("http://www.ivoa.net/xml/VORegistry/v0.3",RegistrySchemaMap.class.getResource("/schema/registry/VORegistry-v0.3.xsd"));
        
        ALL.put("http://www.ivoa.net/xml/VOResource/v0.9",RegistrySchemaMap.class.getResource("/schema/registry/VOResource-v0.9.xsd"));
        ALL.put("http://www.ivoa.net/xml/VOResource/v0.10",RegistrySchemaMap.class.getResource("/schema/registry/VOResource-v0.10.xsd"));
        
   // likewise, omitted VOResourceRelType-* 
        ALL.put("http://www.ivoa.net/xml/VOTable/v1.0",RegistrySchemaMap.class.getResource("/schema/registry/VOTable.xsd"));
        ALL.put("http://www.astrogrid.org/xml/AstrogridDataService/v0.1",RegistrySchemaMap.class.getResource("/schema/registry/AstrogridDataService-0.1.xsd"));
        ALL.put("http://www.astrogrid.org/xml/AstrogridDataService/v0.2",RegistrySchemaMap.class.getResource("/schema/registry/AstrogridDataService-0.2.xsd"));
        
        ALL.put("http://www.ivoa.net/xml/OpenSkyNode/v0.1",RegistrySchemaMap.class.getResource("/schema/registry/OpenSkyNode-v0.1.xsd"));
        
                                                                                            
    }
    

}


/* 
$Log: RegistrySchemaMap.java,v $
Revision 1.3  2005/01/07 14:14:25  jdt
merged from Reg_KMB_787

Revision 1.1.4.2.2.1  2005/01/05 10:54:15  KevinBenson
added javadoc to it

Revision 1.1.4.2  2004/11/26 21:57:05  KevinBenson
adding more checks on validation for version 10

Revision 1.1.4.1  2004/11/19 10:51:26  KevinBenson
okay this is the result of a comparison wiht eclipse with brach Reg_KMB_605
and then updating this branch Reg_KMB_728.  Reason for this was some new jsp pages
in head that was not picked up on 605

Revision 1.1.2.1  2004/11/16 16:49:32  KevinBenson
new validation to the registry being added.  With some unit tests.  And a schemamap mapping all the schems to a local schema url file

Revision 1.1  2004/09/02 01:31:15  nw
added in all external schemas to jar (for testing).
wrote static that lists all namesapces and system IDs for schemas.
 
*/