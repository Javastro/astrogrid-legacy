/*
 * $Id: DatacenterDelegateFactory.java,v 1.13 2004/02/16 23:33:42 mch Exp $
 *
 * (C) Copyright AstroGrid...
 */
package org.astrogrid.datacenter.delegate;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.rpc.ServiceException;

import org.apache.axis.utils.XMLUtils;
import org.astrogrid.community.Account;
import org.astrogrid.datacenter.adql.ADQLException;
import org.astrogrid.datacenter.adql.ADQLUtils;
import org.astrogrid.datacenter.adql.generated.Select;
import org.astrogrid.datacenter.delegate.agws.WebDelegate;
import org.astrogrid.datacenter.delegate.dummy.DummyDelegate;
import org.astrogrid.datacenter.delegate.nvocone.AdqlNvoConeDelegate;
import org.astrogrid.datacenter.delegate.nvocone.NvoConeSearchDelegate;
import org.astrogrid.datacenter.sql.SQLUtils;
import org.w3c.dom.Element;
/**
 * Constructs the right delegate for the given service
 *
 * @author M Hill
 * @modified nww - added factory methods for FullSearcher, deprecated AdqlSearcher factory methods.
 */
public class DatacenterDelegateFactory {
   public final static String ASTROGRID_WEB_SERVICE = "AstroGrid Web Service";
   public final static String NVO_CONE_SERVICE = "NVO Cone Service";
   public final static String DUMMY_SERVICE = "Dummy";
   public final static String ASTROGRID_SOCKET_SERVICE = "AstroGrid Socket Service";
   public final static String ASTROGRID_DIRECT = "AstroGrid Direct";
   /**
    * For backwards compatibility - call without user & type info.  It makes
    * best attempt to work out type but it's not safe
    * @deprecated use method that accepts user parameter
    */
   public static ConeSearcher makeConeSearcher(String givenEndPoint)
      throws MalformedURLException, DatacenterException {
      //there are three server types -> three delegate implementations - dummies,
      // nvo cone-search servers, and astrogrid datacenter servers (agds).
      // The urls of nvo cone-search servers and adql servers may be similar.
      // only adql servers return metadata.
      if (givenEndPoint == null) {
         return makeConeSearcher(Account.ANONYMOUS, givenEndPoint, DUMMY_SERVICE);
      } else if (givenEndPoint.toUpperCase().indexOf("?CAT=") > -1) {
         //if the url includes ?CAT it's an nvo-server
         return makeConeSearcher(Account.ANONYMOUS, givenEndPoint, NVO_CONE_SERVICE);
      } else {
         //pants, should really now query the service...
         return makeConeSearcher(Account.ANONYMOUS, givenEndPoint, NVO_CONE_SERVICE);
      }
   }
   /** Creates a ConeSearcher-implementing delegate given an endpoint
    * (a url to the service). If the endPoint
    * is null, creates a dummy delegate that can be used to test against, which
    * does not need access to any datacenter servers.
    * @todo make CatalogDelegate implement ConeSearcher and add here
    */
   public static ConeSearcher makeConeSearcher(Account user, String givenEndPoint, String serviceType)
      throws MalformedURLException, DatacenterException {
      if (serviceType.equals(DUMMY_SERVICE)) {
         //easy one, return a dummy
         return new DummyDelegate();
      }
      if (serviceType.equals(NVO_CONE_SERVICE)) {
         return new NvoConeSearchDelegate(givenEndPoint);
      }
      if (serviceType.equals(ASTROGRID_WEB_SERVICE)) {
         try {
            return new WebDelegate(Account.ANONYMOUS, new URL(givenEndPoint));
         } catch (ServiceException e) {
            throw new DatacenterException("Could not connect to " + givenEndPoint, e);
         }
      }
      throw new IllegalArgumentException("Service Type '" + serviceType + "' unknown");
   }
   /**
    * For backwards compatibility - call without user & type info.  It makes
    * best attempt to work out type but it's not safe
    * @deprecated use method that accepts user
    */
   public static FullSearcher makeFullSearcher(String givenEndPoint)
      throws ServiceException, MalformedURLException, IOException {
      //there are three server types -> three delegate implementations - dummies,
      // nvo cone-search servers, and astrogrid datacenter servers (agds).
      // The urls of nvo cone-search servers and adql servers may be similar.
      // only adql servers return metadata.
      if (givenEndPoint == null) {
         return makeFullSearcher(Account.ANONYMOUS, givenEndPoint, DUMMY_SERVICE);
      }
      //if the url includes ?CAT it's an nvo-server
      if (givenEndPoint.indexOf("?") > -1) {
         return makeFullSearcher(Account.ANONYMOUS, givenEndPoint, NVO_CONE_SERVICE);
      }
      if (givenEndPoint.startsWith("socket")) {
         return makeFullSearcher(Account.ANONYMOUS, givenEndPoint, ASTROGRID_SOCKET_SERVICE);
      }
      return makeFullSearcher(Account.ANONYMOUS, givenEndPoint, ASTROGRID_WEB_SERVICE);
   }
   /** Creates an  delegate given an endpoint
    * (a url to the service). If the endPoint
    * is null, creates a dummy delegate that can be used to test against, which
    * does not need access to any datacenter servers.
    */
   public static FullSearcher makeFullSearcher(Account user, String givenEndPoint, String serviceType)
      throws ServiceException, MalformedURLException, IOException {
      if (serviceType.equals(DUMMY_SERVICE)) {
         //easy one, return a dummy
         return new DummyDelegate();
      }
      if (serviceType.equals(NVO_CONE_SERVICE)) {
         return new AdqlNvoConeDelegate(givenEndPoint);
      }
      if (serviceType.equals(ASTROGRID_WEB_SERVICE)) {
         return new WebDelegate(user, new URL(givenEndPoint));
      }
      if (serviceType.equals(ASTROGRID_DIRECT)) {
         //lets see if we can find DirectDelegate on the class path - if so then we
         //can connect directly to the server.  This is not very nice and note quite
         //right yet - we ought to be able to check and then fallback.  Later.
         try {
            Class delegate = Class.forName("org.astrogrid.datacenter.delegate.agdirect.DirectDelegate");
            Constructor constr = delegate.getConstructor(new Class[] { Account.class });
            return (FullSearcher) constr.newInstance(new Object[] { user });
         } catch (Exception e) {
            //other exceptions raised by constructor of the direct delegate
            throw new RuntimeException(e);
         }
      }
     
      throw new IllegalArgumentException("Service Type '" + serviceType + "' unknown");
   }
   
}
/*
 $Log: DatacenterDelegateFactory.java,v $
 Revision 1.13  2004/02/16 23:33:42  mch
 Changed to use Account and AttomConfig

 Revision 1.12  2004/02/15 23:09:04  mch
 Naughty Big Lump of changes: Updated myspace access, applicationcontroller interface, some tidy ups.

 Revision 1.11  2004/01/22 14:57:55  nw
 minor doc fix

 Revision 1.10  2004/01/13 00:32:47  nw
 Merged in branch providing
 * sql pass-through
 * replace Certification by User
 * Rename _query as Query

 Revision 1.9.4.4  2004/01/12 16:44:07  nw
 Reintroduced deprecated interfaces for backwards compatability

 sql passthru finished and tested

 Revision 1.9.4.3  2004/01/08 09:42:26  nw
 tidied imports

 Revision 1.9.4.2  2004/01/08 09:10:20  nw
 replaced adql front end with a generalized front end that accepts
 a range of query languages (pass-thru sql at the moment)

 Revision 1.9.4.1  2004/01/07 13:01:44  nw
 removed Community object, now using User object from common

 Revision 1.9  2003/12/15 14:29:19  mch
 Added WebDelegate to ConeSearch factory method

 Revision 1.8  2003/12/03 19:37:03  mch
 Introduced DirectDelegate, fixed DummyQuerier

 Revision 1.7  2003/11/26 16:31:46  nw
 altered transport to accept any query format.
 moved back to axis from castor

 Revision 1.6  2003/11/25 15:59:55  mch
 Added doc

 Revision 1.5  2003/11/25 15:46:25  mch
 Added User and service types; deprecated older methods

 Revision 1.4  2003/11/25 11:54:41  mch
 Added framework for SQL-passthrough queries

 Revision 1.3  2003/11/18 00:34:37  mch
 New Adql-compliant cone search

 Revision 1.2  2003/11/17 20:47:57  mch
 Adding Adql-like access to Nvo cone searches

 Revision 1.1  2003/11/14 00:36:40  mch
 Code restructure

 Revision 1.1  2003/10/06 18:55:21  mch
 Naughtily large set of changes converting to SOAPy bean/interface-based delegates



 */
