/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

import java.util.LinkedHashMap;

/**
 * @author Doug Tody 
 */


/** Map SIA V1.0 UCDs to SIA interface attributes.  This is where

 * we map the SIAP-defined UCDs in the rows of the VOTable to the

 * protocol-independent SIA dataset descriptor data model.  This

 * isolates the client code from changes to details of the underlying

 * protocol, including versioning (except for changes to the data model

 * itself).

 */

class Sia10Map {

    static final LinkedHashMap<String, String> m = new LinkedHashMap<String, String>(32);



    static {

        // SIA dataset attribute	// SIA V1.0 UCD in VOTable

        enter("Title", "VOX:Image_Title");

        enter("RA", "POS_EQ_RA_MAIN");

        enter("DEC", "POS_EQ_DEC_MAIN");

        enter("Instrument", "INST_ID");

        enter("MJDateObs", "VOX:Image_MJDateObs");

        enter("Naxes", "VOX:Image_Naxes");

        enter("Naxis", "VOX:Image_Naxis");

        enter("Scale", "VOX:Image_Scale");

        enter("Format", "VOX:Image_Format");



        enter("CoordRefFrame", "VOX:STC_CoordRefFrame");

        enter("CoordEquinox", "VOX:STC_CoordEquinox");

        enter("CoordProjection", "VOX:STC_CoordProjection");

        enter("CoordRefPixel", "VOX:STC_CoordRefPixel");

        enter("CoordRefValue", "VOX:STC_CoordRefValue");

        enter("CDMatrix", "VOX:STC_CDMatrix");



        enter("BandPass_ID", "VOX:BandPass_ID");

        enter("BandPass_Unit", "VOX:BandPass_Unit");

        enter("BandPass_RefValue", "VOX:BandPass_RefValue");

        enter("BandPass_HiLimit", "VOX:BandPass_HiLimit");

        enter("BandPass_LoLimit", "VOX:BandPass_LoLimit");



        enter("PixFlags", "VOX:Image_PixFlags");

        enter("AccessReference", "VOX:Image_AccessReference");

        enter("AccessRefTTL", "VOX:Image_AccessRefTTL");

        enter("Filesize", "VOX:Image_FileSize");

    }



    // Enter an attribute-UCD pair into the hashmap (makes UCD the key).

    static void enter(final String attribute, final String ucd) {

        m.put(ucd, attribute);

    }



    // Map a SIA V1.0 UCD to the corresponding data model attribute name.

    static String mapUCD(final String ucd) {

        return m.get(ucd);

    }



    // Get the hashmap.

    static LinkedHashMap<String, String> getMap() {

        return (m);

    }

}
