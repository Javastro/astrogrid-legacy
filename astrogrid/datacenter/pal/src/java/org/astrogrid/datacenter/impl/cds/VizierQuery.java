/*$Id: VizierQuery.java,v 1.1 2004/10/05 19:19:18 mch Exp $
 * Created on 28-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 **/
package org.astrogrid.datacenter.impl.cds;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.axis.utils.XMLUtils;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.impl.cds.vizier.Target;
import org.astrogrid.datacenter.impl.cds.vizier.Unit;
import org.astrogrid.datacenter.impl.cds.vizier.VizierDelegate;
import org.astrogrid.datacenter.impl.cds.vizier.Wavelength;
import org.astrogrid.datacenter.queriers.VotableResults;
import org.astrogrid.datacenter.query.Query;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;


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
   protected String catalogue;
   
   /**
    * Constructor sets default catalogue from configuration and unit as degrees
    */
   public VizierQuery(Target aTarget, double aRadius) {
      catalogue =
         SimpleConfig.getProperty(VizierQuerierPlugin.CATALOGUE_NAME);
      unit = Unit.DEG;
   }
   
   public void setCatalogue(String catalogue) {
      this.catalogue = catalogue;
   }
   
   public String getCatalogue() {
      return catalogue;
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
   
   public String getAdditionalTerms() {
      return additionalTerms;
   }
   
   /** double-dispatch thingie -
    *   chooses which query method of the vizier delegate to call,
    *   based on which search terms are currently set.
    *
    * @param delegate - delegate to call method on
    */
   public Document askQuery(VizierDelegate delegate) throws IOException, SAXException, ParserConfigurationException {
      
      // always require a Target and radius
      if (target == null || radius == 0.0 || unit == null) {
         throw new IllegalArgumentException(
            "Must specify target, radius an unit");
      }
      
      if (wavelength == null) {
         if (additionalTerms == null) {
            
            return delegate.cataloguesData(target,radius,unit,
                                           catalogue);
            
         } else {
            return delegate.cataloguesData(target,radius,
                                           unit,additionalTerms);
         }
      } else {
         if (additionalTerms == null) {
            return delegate.cataloguesData(target,radius,
                                           unit,wavelength);
         } else {
            return delegate.cataloguesData(target,radius,
                                           unit,wavelength,additionalTerms);
         }
      }
   }
}


/*
 $Log: VizierQuery.java,v $
 Revision 1.1  2004/10/05 19:19:18  mch
 Merged CDS implementation into PAL

 Revision 1.4  2004/09/29 18:45:55  mch
 Bringing Vizier into line with new(er) metadata stuff

 Revision 1.3  2004/08/14 14:35:42  acd
 Fix the cone search in the Vizier Proxy.

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
