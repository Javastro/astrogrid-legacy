/*
 * $Id ConeSearcher.java $
 *
 */

package org.astrogrid.datacenter.delegate;

import org.w3c.dom.Element;

/**
 * Interface defining the simplest of delegates - being able to run a cone
 * search on a service.  Providing implementations of this for different
 * service types means client tool builders can access such services with
 * the minimum of learning - no adql, certifications, etc.
 *
 * @author M Hill
 */

public interface ConeSearcher
{

   /**
    * Simple cone-search call.
    * @param ra Right Ascension in decimal degrees, J2000
    * @param dec Decliniation in decimal degress, J2000
    * @param sr search radius.
    * @return VOTable
    * @todo return VOTable instance not Element
    */
   public Element coneSearch(double ra, double dec, double sr) throws DatacenterException;

}

/*
$Log: ConeSearcher.java,v $
Revision 1.1  2003/11/14 00:36:40  mch
Code restructure

Revision 1.1  2003/10/06 18:55:21  mch
Naughtily large set of changes converting to SOAPy bean/interface-based delegates

*/
