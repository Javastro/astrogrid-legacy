/*
 * $Id ConeSearcher.java $
 *
 */

package org.astrogrid.datacenter.delegate;

import java.io.IOException;
import java.io.InputStream;


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
    * @param sr search radius in decimal degrees.
    * @return InputStream to results document, including votable
    */
   public InputStream coneSearch(double ra, double dec, double sr) throws IOException;

}

/*
$Log: ConeSearcher.java,v $
Revision 1.5  2003/11/26 16:31:46  nw
altered transport to accept any query format.
moved back to axis from castor

Revision 1.4  2003/11/17 20:47:57  mch
Adding Adql-like access to Nvo cone searches

Revision 1.3  2003/11/17 16:59:12  mch
ConeSearcher.coneSearch now returns stream not parsed element, throws IOException

Revision 1.2  2003/11/17 12:32:26  mch
Moved QueryStatus to query pacakge

Revision 1.1  2003/11/14 00:36:40  mch
Code restructure

Revision 1.1  2003/10/06 18:55:21  mch
Naughtily large set of changes converting to SOAPy bean/interface-based delegates

*/
