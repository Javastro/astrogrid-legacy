/*
 * $Id AladinPiper.java $
 *
 */

package org.astrogrid.ace.aladin;

import org.astrogrid.ace.client.AceConsumer;
import org.astrogrid.ace.client.PassbandSpecifierPanel;
import org.astrogrid.ace.client.Vot2InputPipe;
import org.w3c.dom.*;
import org.astrogrid.log.Log;

/**
 * A bit of a botch - a subclass of Vot2InputPipe, that takes the elements
 * returned by the server and turns them into a stream suitable for Aladin.
 * However it also takes as an argument the panel that specifies the passbands,
 * so that it can change the headings of the appropriate columns before
 * passing it on
 *
 * @author M Hill
 */


public class AladinPiper extends Vot2InputPipe
{
   PassbandSpecifierPanel passbandPanel;

   public AladinPiper(AceConsumer givenConsumer, PassbandSpecifierPanel aPanel)
   {
      super(givenConsumer);

      this.passbandPanel = aPanel;
   }

   /**
    * Looks for MAG_AUTO and MAGERR_AUTO if the magnitude buttons are selected, or FLUX_AUTO
    * and FLUXERR_AUTO if the physical button is selected (outputting warnings if not
    * found).  Replaces ucd bits with relevent ones from passband specifier panel
   <p>
    * If physical specified, should add OBS_FREQUENCY somewhere but don't know where yet
    */
   public void consumeAceResults(Element votResults)
   {
      String[] passbandIds = passbandPanel.getIds();
      boolean foundPhot = false;
      boolean foundErr = false;

      if (passbandPanel.isPhysicalSelected())
      {
         Log.trace("Adding physical units to table...");

         //physical units - look for FLUX_AUTO
         NodeList fieldNodes = votResults.getElementsByTagName("FIELD");

         for (int i=0;i<fieldNodes.getLength();i++)
         {
            Element tag = (Element) fieldNodes.item(i);

            //look for ID=FLUX_AUTO
            NamedNodeMap attrNodes = tag.getAttributes();

            if (getAttrFor(attrNodes, "ID").getValue().equals("FLUX_AUTO")) // it is possible that the getAttrFor will return a null, in which case the VOTable is broken.  And this is a botch anyway
            {
               //yippee, found the right one - now set find the ucd and name attributes
               //and set those.
               tag.setAttribute("name", passbandIds[0]);
               tag.setAttribute("ucd", passbandIds[1]);

               foundPhot = true;
            }

            //now look for ID=FLUXERR_AUTO, as above
            if (getAttrFor(attrNodes, "ID").getValue().equals("FLUXERR_AUTO"))
            {
               tag.setAttribute("name", "e_"+passbandIds[0]);
               tag.setAttribute("ucd", "ERROR");

               foundErr = true;

            }

         }

         if (!foundErr) Log.logError("Could not find FLUXERR_AUTO");
         if (!foundPhot) Log.logError("Could not find FLUX_AUTO");

         //now for each row we need to add OBS_FREQUENCY and FILTER_FWHM
         //first need to add a <FIELD> tag to the <table> element
         //assume only one <TABLE> tag for now
         Element tableTag = (Element) (votResults.getElementsByTagName("TABLE").item(0));
         Element dataTag = (Element) (tableTag.getElementsByTagName("DATA").item(0));

         //create the obs frequency  field element
         Element obsFreqFieldTag = tableTag.getOwnerDocument().createElement("FIELD");
         obsFreqFieldTag.setAttribute("ID","ObservedFrequency");
         obsFreqFieldTag.setAttribute("ucd","OBS_FREQUENCY");
         obsFreqFieldTag.setAttribute("name","ObservedFrequency");

         tableTag.insertBefore(obsFreqFieldTag, dataTag);

         //create the filter Fwhm field element
         Element filterFwhmFieldTag = tableTag.getOwnerDocument().createElement("FIELD");
         filterFwhmFieldTag.setAttribute("ID","FilterFWHM");
         filterFwhmFieldTag.setAttribute("ucd","INST_FILTER_FWHM");
         filterFwhmFieldTag.setAttribute("name","FilterFWHM");

         tableTag.insertBefore(filterFwhmFieldTag, dataTag);

         //now for each table row tag <TR>, add a <TD> tag with the same values in
         Element tableDataTag = (Element) (votResults.getElementsByTagName("TABLEDATA").item(0));

         NodeList rows = tableDataTag.getElementsByTagName("TR");

         //Unfortunately cannot attach the same instances across the whole lot -
         //need to actually create a new instance for each row
         Element obsFreqTag, filterFwhmTag;


         for (int n=0;n<rows.getLength();n++)
         {
            obsFreqTag = tableTag.getOwnerDocument().createElement("TD");
            obsFreqTag.appendChild(tableTag.getOwnerDocument().createTextNode(""+passbandPanel.getPassbandFreq()));

            filterFwhmTag = tableTag.getOwnerDocument().createElement("TD");
            filterFwhmTag.appendChild(tableTag.getOwnerDocument().createTextNode(""+passbandPanel.getFilterFwhm()));

            ((Element) rows.item(n)).appendChild(obsFreqTag);
            ((Element) rows.item(n)).appendChild(filterFwhmTag);
         }
      }
      else if (passbandPanel.isMagnitudeSelected())
      {
         //magnitudes
         NodeList fieldNodes = votResults.getElementsByTagName("FIELD");

         for (int i=0;i<fieldNodes.getLength();i++)
         {
            Element tag = (Element) fieldNodes.item(i);

            //look for ID=MAG_AUTO
            NamedNodeMap attrNodes = tag.getAttributes();

            if (getAttrFor(attrNodes, "ID").getValue().equals("MAG_AUTO")) // it is possible that the getAttrFor will return a null, in which case the VOTable is broken.  And this is a botch anyway
            {
               //yippee, found the right one - now set find the ucd and name attributes
               //and set those.
               tag.setAttribute("name", passbandIds[0]);
               tag.setAttribute("ucd", passbandIds[1]);

               foundPhot = true;
            }

            //now look for ID=FLUXERR_AUTO, as above
            if (getAttrFor(attrNodes, "ID").getValue().equals("MAGERR_AUTO"))
            {
               tag.setAttribute("name", "e_"+passbandIds[0]);
               tag.setAttribute("ucd", "ERROR");

               foundErr = true;

            }

         }


         if (!foundErr) Log.logError("Could not find MAGERR_AUTO");
         if (!foundPhot) Log.logError("Could not find MAG_AUTO");

      }



      super.consumeAceResults(votResults);
   }

   /**
    * returns the element with the tag name "FIELD" and attribute ID=givenId
    *
   public static Element findField(String givenId)
   {
   }
    */

   /**
    * Returns the attribute with the given name in the given list of
    * attributes
    */
   public static Attr getAttrFor(NamedNodeMap attrNodes, String name)
   {
      for (int a=0;a<attrNodes.getLength();a++)
      {
         Attr attr = (Attr) attrNodes.item(a);

         if (attr.getName().equals(name))
         {
            return attr;
         }
      }
      return null;
   }
}

/*
$Log: AladinPiper.java,v $
Revision 1.1.1.1  2003/08/25 18:36:02  mch
Reimported to fit It02 source structure

Revision 1.2  2003/07/02 19:18:15  mch
Fix to handle 'ignore' button

Revision 1.1  2003/06/26 19:13:49  mch
Extra processing on results to insert special SED UCDs

*/
