/*$Id: SchemaMap.java,v 1.3 2007/01/04 16:26:37 clq2 Exp $
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/** static class that provides maps of all namespace - schema locations in this project.
 * useful to pass to schema-validation methods in {@link org.astrogrid.test.AstrogridAssert}
 * @author Noel Winstanley nw@jb.man.ac.uk 01-Sep-2004
 * @author pharriso@eso.org May 27, 2005
 *@ see org.astrogrid.test.AstrogridAssert
 * @deprecated Use org.astrogrid.contracts.SchemaMap instead.
 */
public class SchemaMap {
   /**
    * Logger for this class
    */
   private static final Log logger = LogFactory.getLog(SchemaMap.class);

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
        ALL.put("http://www.ivoa.net/xml/CEAService/v0.1",SchemaMap.class.getResource("/schema/VOCEA.xsd"));
        ALL.put("http://www.ivoa.net/xml/CEAService/v0.2",SchemaMap.class.getResource("/schema/VOCEA-v0.2.xsd"));
        ALL.put("http://www.astrogrid.org/schema/CEAImplementation/v1", SchemaMap.class.getResource("/schema/CEAImplementation.xsd"));
        ALL.put("http://www.astrogrid.org/schema/AGWorkflow/v1",SchemaMap.class.getResource("/schema/Workflow.xsd"));
  //TODO these data model ones are almost certainly out of date...pah 
        ALL.put("http://ivoa.org/Accuracy",SchemaMap.class.getResource("/schema/externalSchema/quantity/Accuracy.xsd"));
        ALL.put("http://ivoa.org/CoordinateSystems",SchemaMap.class.getResource("/schema/externalSchema/quantity/CoordinateSystems.xsd"));
        ALL.put("http://ivoa.org/Mappings",SchemaMap.class.getResource("/schema/externalSchema/quantity/Mappings.xsd"));
        ALL.put("http://ivoa.org/Quantity",SchemaMap.class.getResource("/schema/externalSchema/quantity/Quantity.xsd"));
                
        ALL.put("urn:nvo-coords",SchemaMap.class.getResource("/schema/externalSchema/stc/coords.xsd"));
        ALL.put("urn:nvo-region",SchemaMap.class.getResource("/schema/externalSchema/stc/region.xsd"));
        ALL.put("urn:nvo-stc",SchemaMap.class.getResource("/schema/externalSchema/stc/stc.xsd"));
      //0.9 registry                       
        ALL.put("http://www.ivoa.net/xml/ConeSearch/v0.2",SchemaMap.class.getResource("/schema/externalSchema/ConeSearch-v0.2.xsd"));
        ALL.put("http://www.ivoa.net/xml/SIA/v0.6",SchemaMap.class.getResource("/schema/externalSchema/SIA-v0.6.xsd"));
        ALL.put("http://www.ivoa.net/xml/VOCommunity/v0.2",SchemaMap.class.getResource("/schema/externalSchema/VOCommunity-v0.2.xsd"));
        ALL.put("http://www.ivoa.net/xml/VODataService/v0.4",SchemaMap.class.getResource("/schema/externalSchema/VODataService-v0.4.xsd"));
   // VODataServiceCoverage-* omitted - no target namespace declared. doesn't look valid as a stand-alone schema 
        ALL.put("http://www.ivoa.net/xml/VORegistry/v0.2",SchemaMap.class.getResource("/schema/externalSchema/VORegistry-v0.2.xsd"));
        ALL.put("http://www.ivoa.net/xml/VOResource/v0.9",SchemaMap.class.getResource("/schema/externalSchema/VOResource-v0.9.xsd"));
   // likewise, omitted VOResourceRelType-* 
        //0.10 registry
        ALL.put("http://www.ivoa.net/xml/ConeSearch/v0.3",SchemaMap.class.getResource("/schema/externalSchema/v10/ConeSearch-v0.3.xsd"));
        ALL.put("http://www.ivoa.net/xml/SIA/v0.7",SchemaMap.class.getResource("/schema/externalSchema/v10/SIA-v0.7.xsd"));
        ALL.put("http://www.ivoa.net/xml/VODataService/v0.5",SchemaMap.class.getResource("/schema/externalSchema/v10/VODataService-v0.5.xsd"));
        ALL.put("http://www.ivoa.net/xml/VORegistry/v0.3",SchemaMap.class.getResource("/schema/externalSchema/v10/VORegistry-v0.3.xsd"));
        ALL.put("http://www.ivoa.net/xml/VOResource/v0.10",SchemaMap.class.getResource("/schema/externalSchema/v10/VOResource-v0.10.xsd"));
     // the registry interface
        ALL.put("http://www.ivoa.net/xml/RegistryInterface/v0.1",SchemaMap.class.getResource("/schema/externalSchema/v10/RegistryInterface-v0.1.xsd"));
        
        ALL.put("http://www.ivoa.net/xml/VOTable/v1.0",SchemaMap.class.getResource("/schema/externalSchema/VOTable.xsd"));
        ALL.put("http://www.ivoa.net/xml/VOTable/v1.1",SchemaMap.class.getResource("/schema/externalSchema/VOTable-1.1.xsd"));
        // isn't there a more recent version of votable now too?
             
        
        for (Iterator iter = SchemaMap.ALL.keySet().iterator(); iter.hasNext();) {
           String key = (String) iter.next();
           if(SchemaMap.ALL.get(key) == null)
           {
              logger.error("key "+key+ " has no associated schema");
           }
        }

    }
    

}


/* 
$Log: SchemaMap.java,v $
Revision 1.3  2007/01/04 16:26:37  clq2
wo_gtr_1942

Revision 1.2.48.1  2006/10/17 17:25:54  gtr
This class is deprecated in favour of org.astrogrid.contracts.SchemaMap.

Revision 1.2  2005/07/05 08:27:01  clq2
paul's 559b and 559c for wo/apps and jes

Revision 1.1.90.6  2005/06/09 07:28:31  pah
*** empty log message ***

Revision 1.1.90.5  2005/06/01 15:07:30  pah
added votable 1.1

Revision 1.1.90.4  2005/05/27 10:21:49  pah
add the registry interface schema

Revision 1.1.90.3  2005/05/27 10:08:40  pah
fix broken mappings for jes types and v05 data service

Revision 1.1.90.2  2005/05/25 09:01:05  pah
will create v10 registry objects in addition to the v9 objects (which have been left so as not to break existing components that use v9)
rearranged the generation so that it occurs into the gen folder - easier to see the few handwritten classes in this module

Revision 1.1.90.1  2005/05/17 11:39:38  pah
moved generated source to the gen tree - src only has hand written source

Revision 1.1.88.1  2005/05/17 09:54:35  pah
put in v10 schema and separated out the CEA implementation

Revision 1.1  2004/09/02 01:31:15  nw
added in all external schemas to jar (for testing).
wrote static that lists all namesapces and system IDs for schemas.
 
*/