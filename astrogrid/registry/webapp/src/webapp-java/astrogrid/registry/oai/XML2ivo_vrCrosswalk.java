
/**
*Copyright (c) 2000-2002 OCLC Online Computer Library Center,
*Inc. and other contributors. All rights reserved.  The contents of this file, as updated
*from time to time by the OCLC Office of Research, are subject to OCLC Research
*Public License Version 2.0 (the "License"); you may not use this file except in
*compliance with the License. You may obtain a current copy of the License at
*http://purl.oclc.org/oclc/research/ORPL/.  Software distributed under the License is
*distributed on an "AS IS" basis, WITHOUT WARRANTY OF ANY KIND, either express
*or implied. See the License for the specific language governing rights and limitations
*under the License.  This software consists of voluntary contributions made by many
*individuals on behalf of OCLC Research. For more information on OCLC Research,
*please see http://www.oclc.org/oclc/research/.
*
*The Original Code is XML2oai_dc.java.
*The Initial Developer of the Original Code is Jeff Young.
*Portions created by ______________________ are
*Copyright (C) _____ _______________________. All Rights Reserved.
*Contributor(s):______________________________________.
*/


package astrogrid.registry.oai;

import ORG.oclc.oai.server.crosswalk.*;

import java.util.Properties;
import ORG.oclc.oai.server.verb.CannotDisseminateFormatException;

/**
 * Convert native "item" to oai_dc. In this case, the native "item"
 * is assumed to already be formatted as an OAI <record> element,
 * with the possible exception that multiple metadataFormats may
 * be present in the <metadata> element. The "crosswalk", merely
 * involves pulling out the one that is requested.
 */
public class XML2ivo_vrCrosswalk extends Crosswalk {
    private static String elementName = "Resource";
    private static final String prefix_09 = "vr:";
    private static final String prefix_10 = "vor:";

    //private static final String elementStart = "<" + elementName;
    //private static final String elementEnd = elementName + ">";

    /**
     * The constructor assigns the schemaLocation associated with this crosswalk. Since
     * the crosswalk is trivial in this case, no properties are utilized.
     *
     * @param properties properties that are needed to configure the crosswalk.
     */
    public XML2ivo_vrCrosswalk(Properties properties) {
     super("http://www.ivoa.net/xml/VOResource/v" + properties.getProperty("registry_version") + " http://www.ivoa.net/xml/VOResource/VOResource-v" + properties.getProperty("registry_version") + ".xsd ");
    }

    /**
     * Can this nativeItem be represented in DC format?
     * @param nativeItem a record in native format
     * @return true if DC format is possible, false otherwise.
     */
    public boolean isAvailableFor(Object nativeItem) {
   String fullItem = (String)nativeItem;
   if ((fullItem.indexOf(("<" + prefix_09 + elementName)) >= 0) || 
       (fullItem.indexOf(("<" + prefix_10 + elementName)) >= 0))
       return true;
   return false;
    }

    /**
     * Perform the actual crosswalk.
     *
     * @param nativeItem the native "item". In this case, it is
     * already formatted as an OAI <record> element, with the
     * possible exception that multiple metadataFormats are
     * present in the <metadata> element.
     * @return a String containing the XML to be stored within the <metadata> element.
     * @exception CannotDisseminateFormatException nativeItem doesn't support this format.
     */
    public String createMetadata(Object nativeItem)
   throws CannotDisseminateFormatException {
   String fullItem = (String)nativeItem;
   String elementEnd = prefix_09 + elementName + ">";
   int startOffset = fullItem.indexOf(("<" + prefix_09 + elementName));
   if(startOffset == -1) {
       startOffset = fullItem.indexOf(("<" + prefix_10 + elementName));
       elementEnd = prefix_10 + elementName + ">";
   }
   if (startOffset == -1) {
       throw new CannotDisseminateFormatException(getSchemaLocation());
   }
      int endOffset = fullItem.indexOf(elementEnd) + elementEnd.length();
      System.out.println("STARTOFFSET = " + startOffset + " ENDOFFSET=" + endOffset + "THE FULLITEM = " + fullItem);
      return fullItem.substring(startOffset, endOffset);
    }
}
