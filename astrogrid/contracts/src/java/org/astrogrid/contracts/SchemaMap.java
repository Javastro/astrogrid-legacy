/*$Id: SchemaMap.java,v 1.2 2006/02/07 14:45:52 clq2 Exp $
 * Created on 01-Sep-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.contracts;

import java.util.HashMap;
import java.util.Map;

/**
 * Class: RegistrySchemaMap
 * Description: static class that provides maps of all namespace - schema locations in this project.
 * useful to pass to schema-validation methods in {@link org.astrogrid.test.AstrogridAssert}
 *@ see org.astrogrid.test.AstrogridAssert
 */
public class SchemaMap {

    /** Construct a new SchemaMap
     * 
     */
    private SchemaMap() {
    }
    
    public static final Map ALL; 
    
    /**
     * Static area for placing all namespaces.
     */
    static {
        // populate the map.
        ALL = new HashMap();

        //ADQL schemas
        ALL.put("http://www.ivoa.net/xml/ADQL/v1.0",SchemaMap.class.getResource("/schema/adql/ADQL/v1.0/ADQL.xsd"));
        ALL.put("http://www.ivoa.net/xml/ADQL/v0.9",SchemaMap.class.getResource("/schema/adql/ADQL/v0.9/ADQL.xsd"));
        ALL.put("http://www.ivoa.net/xml/ADQL/v0.8",SchemaMap.class.getResource("/schema/adql/ADQL/v0.8/ADQL.xsd"));
        ALL.put("http://www.ivoa.net/xml/ADQL/v0.7.4",SchemaMap.class.getResource("/schema/adql/ADQL/v0.7.4/ADQL.xsd"));
        ALL.put("http://adql.ivoa.net/v0.73",SchemaMap.class.getResource("/schema/adql/ADQL/v0.7.3/ADQL.xsd"));
                
        //CEA schemas
        ALL.put("http://www.astrogrid.org/schema/CommonExecutionArchitectureBase/v1", SchemaMap.class.getResource("/schema/cea/CommonExecutionArchitectureBase/v1.0/CommonExecutionArchitectureBase.xsd"));        
        ALL.put("http://www.astrogrid.org/schema/CEATypes/v1",SchemaMap.class.getResource("/schema/cea/CEATypes/v1.0/CEATypes.xsd"));

        //Contract schemas
        ALL.put("urn:astrogrid:schema:architecture:Contract:v0.1",SchemaMap.class.getResource("/schema/architecture/Contract/v0.1/Contract.xsd"));
        
        //jes schemas
        ALL.put("http://www.astrogrid.org/schema/AGParameterDefinition/v1",SchemaMap.class.getResource("/schema/jes/AGParameterDefinition/v1.0/AGParameterDefinition.xsd"));        
        ALL.put("http://www.astrogrid.org/schema/AGWorkflow/v1",SchemaMap.class.getResource("/schema/jes/AGWorkflow/v1.0/AGWorkflow.xsd"));
        ALL.put("http://www.astrogrid.org/schema/Credentials/v1",SchemaMap.class.getResource("/schema/jes/Credentials/v1.0/Credentials.xsd"));
        ALL.put("http://www.astrogrid.org/schema/ExecutionRecord/v1",SchemaMap.class.getResource("/schema/jes/ExecutionRecord/v1.0/ExecutionRecord.xsd"));

        
        //registry schemas
        ALL.put("http://www.ivoa.net/xml/RegistryInterface/v0.1",SchemaMap.class.getResource("/schema/registry/RegistryInterface/v0.1/RegistryInterface.xsd"));
        
        //oai shemas
        ALL.put("http://www.openarchives.org/OAI/2.0/",SchemaMap.class.getResource("/schema/oai/OAI/v2.0/OAI.xsd"));
        
        
        
        //stc schemas (go with adql imports usually)
        
        ALL.put("http://www.ivoa.net/xml/STC/stc-v1.10.xsd",SchemaMap.class.getResource("/schema/stc/STC/v1.10/STC.xsd"));
        ALL.put("http://www.ivoa.net/xml/STC/stc-v1.20.xsd",SchemaMap.class.getResource("/schema/stc/STC/v1.20/STC.xsd"));
        
        ALL.put("http://www.ivoa.net/xml/STC/STCcoords/v1.10",SchemaMap.class.getResource("/schema/stc/STCcoords/v1.10/STCcoords.xsd"));
        ALL.put("http://www.ivoa.net/xml/STC/STCcoords/v1.20",SchemaMap.class.getResource("/schema/stc/STCcoords/v1.20/STCcoords.xsd"));
        ALL.put("http://www.ivoa.net/xml/STC/STCregion/v1.10",SchemaMap.class.getResource("/schema/stc/STCRegion/v1.10/STCregion.xsd"));
        ALL.put("http://www.ivoa.net/xml/STC/STCregion/v1.20",SchemaMap.class.getResource("/schema/stc/STCRegion/v1.20/STCregion.xsd"));
        
        //votable schemas
        ALL.put("http://www.ivoa.net/xml/VOTable/v1.0",SchemaMap.class.getResource("/schema/vo-formats/VOTable/v1.0/VOTable.xsd"));
        ALL.put("http://www.ivoa.net/xml/VOTable/v1.1",SchemaMap.class.getResource("/schema/vo-formats/VOTable/v1.1/VOTable.xsd"));

        //vo-resource-types
        ALL.put("http://www.ivoa.net/xml/CEAService/v0.2",SchemaMap.class.getResource("/schema/vo-resource-types/CEAService/v0.2/CEAService.xsd"));
   
        ALL.put("http://www.ivoa.net/xml/ConeSearch/v0.3",SchemaMap.class.getResource("/schema/vo-resource-types/ConeSearch/v0.3/ConeSearch.xsd"));
        ALL.put("http://www.ivoa.net/xml/SIA/v0.7",SchemaMap.class.getResource("/schema/vo-resource-types/SIA/v0.7/SIA.xsd"));
        ALL.put("http://www.ivoa.net/xml/OpenSkyNode/v0.1",SchemaMap.class.getResource("/schema/vo-resource-types/OpenSkyNode/v0.1/OpenSkyNode.xsd"));
        ALL.put("urn:astrogrid:schema:vo-resource-types:TabularDB:v0.3",SchemaMap.class.getResource("/schema/vo-resource-types/TabularDB/v0.3/TabularDB.xsd"));
        ALL.put("http://www.ivoa.net/xml/VODataService/v0.5",SchemaMap.class.getResource("/schema/vo-resource-types/VODataService/v0.5/VODataService.xsd"));
        // VODataServiceCoverage-* omitted - no target namespace declared. doesn't look valid as a stand-alone schema
        ALL.put("http://www.ivoa.net/xml/VORegistry/v0.3",SchemaMap.class.getResource("/schema/vo-resource-types/VORegistry/v0.3/VORegistry.xsd"));
        ALL.put("http://www.ivoa.net/xml/VOResource/v0.10",SchemaMap.class.getResource("/schema/vo-resource-types/VOResource/v0.10/VOResource.xsd"));
        // likewise, omitted VOResourceRelType-*        
        
        //hmmm not sure what this one is, must be used for castor possibly with cea.
        ALL.put("urn:astrogrid:schema:vo-resource-types:AllResourceTypes:v0.2",SchemaMap.class.getResource("/schema/vo-resource-types/AllResourceTypes/v0.2/AllResourceTypes.xsd"));        
    }
    

}


/* 
$Log: SchemaMap.java,v $
Revision 1.2  2006/02/07 14:45:52  clq2
Kevin's KMB_WSDL bunch

Revision 1.1.2.2  2006/01/23 17:16:09  KevinBenson
small correction on the conesearch version number

Revision 1.1.2.1  2005/11/30 11:53:00  KevinBenson
new schemamap java class that loads the schemas into a hashmap that can be used for validation

Revision 1.8  2005/03/31 17:02:55  jdt
Merges from KMB_972

Revision 1.7.6.2  2005/03/29 08:09:11  KevinBenson
forgot needed to add TabualrDB schema infor validation.

Revision 1.7.6.1  2005/03/26 15:40:08  KevinBenson
new commenting and class name change

Revision 1.7  2005/02/22 20:51:25  clq2
merge 889 again.

Revision 1.3.6.1  2005/01/19 14:37:16  KevinBenson
changing where the xsl is, and fix some of the xsl.

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