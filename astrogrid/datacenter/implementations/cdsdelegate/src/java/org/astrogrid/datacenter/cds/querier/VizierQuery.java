/*$Id: VizierQuery.java,v 1.2 2004/08/14 14:28:37 acd Exp $
 * Created on 28-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.datacenter.cds.querier;

import org.astrogrid.config.SimpleConfig;

import java.io.IOException;
import java.io.Writer;
import org.astrogrid.datacenter.cdsdelegate.vizier.Target;
import org.astrogrid.datacenter.cdsdelegate.vizier.Unit;
import org.astrogrid.datacenter.cdsdelegate.vizier.VizierDelegate;
import org.astrogrid.datacenter.cdsdelegate.vizier.Wavelength;
import org.astrogrid.datacenter.query.Query;
import org.w3c.dom.Document;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import org.apache.axis.utils.XMLUtils;


/** Dataclass that represents a vizier cone search.
 * <p>
 * Contains fields for each of the required or optional search terms
 * that can be passed to vizier.
 * @author Noel Winstanley nw@jb.man.ac.uk 28-Nov-2003
 *
 */
public class VizierQuery implements Query {

    protected Target target;
    protected double radius;
    protected Unit unit;
    protected Wavelength wavelength;
    protected String additionalTerms;
    protected boolean metaData = false;
    
    /**
     *
     */
    public VizierQuery() {

    }
    
    /** double-dispatch thingie - 
     *   chooses which query method of the vizier delegate to call,
     *   based on which search terms are currently set.
     *
     * @param delegate - delegate to call method on
     * @param out - Writer to which the results returned by the delegate
     *   are written.
     *
     * @todo This method uses the passed VizierDelegate to obtain results
     *   from Vizier.  Vizier ultimately returns a String.
     *   VizierDelegate converts this String to a Document.  The present
     *   method converts this Document back to a String and writes it
     *   to a Writer.  If VizierDelegate was modified to return Vizier's
     *   original String the conversion to and from a Document could be
     *   avoided.  I avoided doing this because I wanted to make no more
     *   changes than were strictly necessary.  ACD.
     */
    public void doDelegateQuery(VizierDelegate delegate, Writer out) 
      throws IOException {
       String catalogueName = 
          SimpleConfig.getProperty(VizierQuerierPlugin.CATALOGUE_NAME);

       System.out.println("catalogueName: " + catalogueName);

       String MetaDataFlag =
          SimpleConfig.getProperty(VizierQuerierPlugin.METADATA);

       if (MetaDataFlag != null) {
          if (MetaDataFlag.equalsIgnoreCase("TRUE") ||
              MetaDataFlag.equalsIgnoreCase("YES") ) {
             metaData = true;
          }
       }

       System.out.println("MetaDataFlag, metaData: " + MetaDataFlag
         + "  " + metaData);


       try {
         Document results = XMLUtils.newDocument();
         results = null;

        //special case for metadata=true and nothing else
        if (metaData && target == null) {
             results = delegate.metaAll();
       }
       // always require a Target and radius
       if (target == null || radius == 0.0 || unit == null) {
          throw new IllegalArgumentException(
            "Must specify target, radius an unit");
        }
        if (metaData) {
            if (wavelength == null) {
                if (additionalTerms == null) {

                    results = delegate.cataloguesMetaData(target,radius,
                      unit,catalogueName);

                } else {
                    results = 
                      delegate.cataloguesMetaData(target,radius,unit,
                        additionalTerms);
                }
            } else {
                if (additionalTerms == null) {
                    results = 
                      delegate.cataloguesMetaData(target,radius,unit,
                        wavelength);
                } else {
                    results = delegate.cataloguesMetaData(target,
                      radius,unit,wavelength,additionalTerms);
                }
            }
        } else { // non meta-data query
            if (wavelength == null) {
                if (additionalTerms == null) {

                    results = delegate.cataloguesData(target,radius,unit,
                      catalogueName);

                } else {
                    results = delegate.cataloguesData(target,radius,
                      unit,additionalTerms);
                }
            } else {
                if (additionalTerms == null) {
                    results = delegate.cataloguesData(target,radius,
                      unit,wavelength);
                } else {
                    results = delegate.cataloguesData(target,radius,
                      unit,wavelength,additionalTerms);
                }
            }
        }

        if (results != null) {
           out.write( XMLUtils.DocumentToString(results) );
        } else {
           throw new RuntimeException(
             "No results obtained from Vizier.");
        }

       }
       catch (ParserConfigurationException e) {
          throw new RuntimeException("Vizier not configured properly: "+e, e);
       }
       catch (SAXException e) {
          throw new RuntimeException("Vizier error: "+e, e);
       }

       return;
   }

   
    
    
    /** Set additional search terms for the cone search
     * @param string pattern to match. Set to null for no pattern
     */
    public void setAdditionalTerms(String string) {
        additionalTerms = string;
    }

    /** Set radius of search cone
     * @param d radius
     */
    public void setRadius(double d) {
        radius = d;
    }

    /** Set the target of the cone search
     * @param target
     */
    public void setTarget(Target target) {
        this.target = target;
    }

    /** Set the units used to express the radius
     * @param unit
     */
    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    /** Set the wavelength to restrict the search to
     * @param wavelength to search on. Set to null for all.
     */
    public void setWavelength(Wavelength wavelength) {
        this.wavelength = wavelength;
    }

    /** Set whether the search is to query meta-data or data.
     * @param b true for meta-data
     */
    public void setMetaData(boolean b) {
        metaData = b;
    }

    /**
     * @return
     */
    public String getAdditionalTerms() {
        return additionalTerms;
    }

}


/*
$Log: VizierQuery.java,v $
Revision 1.2  2004/08/14 14:28:37  acd
Fix the Vizier proxy to correctly perform cone searches.

Revision 1.3  2004/08/13 18:09:00 acd
Made changes corresponding to those below for the `metadata'
clauses.  Also made to obtain the metadata flag from the properties
file.

Revision 1.2  2004/08/12 17:40:00  acd
Fixed method doDelegateQuery to write its results to Writer out,
rather than returning them as a Document.  Also made to obtain the
name of the Vizier catalogue which is to be queried from the properties
file.

Revision 1.1  2004/03/13 23:40:59  mch
Changes to adapt to It05 refactor

Revision 1.3  2003/12/09 16:25:08  nw
wrote plugin documentation

Revision 1.2  2003/12/01 16:50:11  nw
first working tested version

Revision 1.1  2003/11/28 19:12:16  nw
getting there..
 
*/
