/*
 * $Id: It03DatacenterDelegateFactory.java,v 1.2 2003/11/26 16:31:46 nw Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.datacenter.delegate.deprecated;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.xml.rpc.ServiceException;

import org.astrogrid.datacenter.delegate.DatacenterDelegateFactory;

/**
 * Constructs the right delegate for the given service.  Presents an It03
 * delegate java interface; this is a wrapper around the new delegates.
 * <p>
 * users of it03 delegates just need to change the one line that specifies
 * the factory, and they can keep using the old delegate interface.  As time
 * goes on, they can gradually change over to the new one (ie, using both
 * factories at once if necessary).
 * <p>
 * @author M Hill
 */

public class It03DatacenterDelegateFactory
{
   
   /**
    * Convenience? method for making delegates where there is no known
    * certification/user (ie anonymous)
    */
   public static It03DatacenterDelegate makeDelegate(String givenEndPoint)
         throws MalformedURLException, ServiceException, IOException
   {
      return new It03DatacenterDelegate(DatacenterDelegateFactory.makeAdqlQuerier(givenEndPoint));
   }
   


}

/*
$Log: It03DatacenterDelegateFactory.java,v $
Revision 1.2  2003/11/26 16:31:46  nw
altered transport to accept any query format.
moved back to axis from castor

Revision 1.1  2003/11/14 00:36:40  mch
Code restructure

Revision 1.2  2003/11/05 18:52:53  mch
Build fixes for change to SOAPy Beans and new delegates

Revision 1.1  2003/10/06 18:55:21  mch
Naughtily large set of changes converting to SOAPy bean/interface-based delegates



*/


