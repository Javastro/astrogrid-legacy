/*
 * $Id: DatacenterDelegateFactory.java,v 1.8 2003/12/03 19:37:03 mch Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.datacenter.delegate;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.rpc.ServiceException;

import org.astrogrid.datacenter.delegate.agss.SocketDelegate;
import org.astrogrid.datacenter.delegate.agws.WebDelegate;
import org.astrogrid.datacenter.delegate.dummy.DummyDelegate;
import org.astrogrid.datacenter.delegate.nvocone.AdqlNvoConeDelegate;
import org.astrogrid.datacenter.delegate.nvocone.NvoConeSearchDelegate;

import java.lang.reflect.Constructor;
/**
 * Constructs the right delegate for the given service
 *
 * @author M Hill
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
    * @deprecated
    */
   public static ConeSearcher makeConeSearcher(String givenEndPoint)
      throws MalformedURLException, DatacenterException {

      //there are three server types -> three delegate implementations - dummies,
      // nvo cone-search servers, and astrogrid datacenter servers (agds).
      // The urls of nvo cone-search servers and adql servers may be similar.
      // only adql servers return metadata.
      
      if (givenEndPoint == null) {
         return makeConeSearcher(Certification.ANONYMOUS, givenEndPoint, DUMMY_SERVICE);
      }
      else if (givenEndPoint.toUpperCase().indexOf("?CAT=") >-1) {
         //if the url includes ?CAT it's an nvo-server
         return makeConeSearcher(Certification.ANONYMOUS, givenEndPoint, NVO_CONE_SERVICE);
      }
      else
      {
         //pants, should really now query the service...
         return makeConeSearcher(Certification.ANONYMOUS, givenEndPoint, NVO_CONE_SERVICE);
      }
   }
   
   /** Creates a ConeSearcher-implementing delegate given an endpoint
    * (a url to the service). If the endPoint
    * is null, creates a dummy delegate that can be used to test against, which
    * does not need access to any datacenter servers.
    * @todo make CatalogDelegate implement ConeSearcher and add here
    */
   public static ConeSearcher makeConeSearcher(Certification user, String givenEndPoint, String serviceType)
      throws MalformedURLException, DatacenterException {
      
      if (serviceType.equals(DUMMY_SERVICE)) {
         //easy one, return a dummy
         return new DummyDelegate();
      }
      
      if (serviceType.equals(NVO_CONE_SERVICE)) {
         return new NvoConeSearchDelegate(givenEndPoint);
      }
      
      throw new IllegalArgumentException("Service Type '"+serviceType+"' unknown");
   }
   
   
   /**
    * For backwards compatibility - call without user & type info.  It makes
    * best attempt to work out type but it's not safe
    * @deprecated
    */
   public static AdqlQuerier makeAdqlQuerier(String givenEndPoint) throws ServiceException, MalformedURLException, IOException {

      //there are three server types -> three delegate implementations - dummies,
      // nvo cone-search servers, and astrogrid datacenter servers (agds).
      // The urls of nvo cone-search servers and adql servers may be similar.
      // only adql servers return metadata.
      
      if (givenEndPoint == null) {
         return makeAdqlQuerier(Certification.ANONYMOUS, givenEndPoint, DUMMY_SERVICE);
      }
      //if the url includes ?CAT it's an nvo-server
      if (givenEndPoint.indexOf("?") >-1) {
         return makeAdqlQuerier(Certification.ANONYMOUS, givenEndPoint, NVO_CONE_SERVICE);
      }

      if (givenEndPoint.startsWith("socket")) {
         return makeAdqlQuerier(Certification.ANONYMOUS, givenEndPoint, ASTROGRID_SOCKET_SERVICE);
      }
      
      return makeAdqlQuerier(Certification.ANONYMOUS, givenEndPoint, ASTROGRID_WEB_SERVICE);
   }


   /** Creates an adql-implementing delegate given an endpoint
    * (a url to the service). If the endPoint
    * is null, creates a dummy delegate that can be used to test against, which
    * does not need access to any datacenter servers.
    */
   public static AdqlQuerier makeAdqlQuerier(Certification user, String givenEndPoint, String serviceType) throws ServiceException, MalformedURLException, IOException {

      if (serviceType.equals(DUMMY_SERVICE)) {
         //easy one, return a dummy
         return new DummyDelegate();
      }
      
      if (serviceType.equals(NVO_CONE_SERVICE)) {
         return new AdqlNvoConeDelegate(givenEndPoint);
      }

      if (serviceType.equals(ASTROGRID_SOCKET_SERVICE)) {
         return new SocketDelegate(givenEndPoint, null);
      }
      
      if (serviceType.equals(ASTROGRID_WEB_SERVICE)) {
         return new WebDelegate(user, new URL(givenEndPoint));
      }

      if (serviceType.equals(ASTROGRID_DIRECT)) {
         //lets see if we can find DirectDelegate on the class path - if so then we
         //can connect directly to the server.  This is not very nice and note quite
         //right yet - we ought to be able to check and then fallback.  Later.
         try
         {
            Class delegate = Class.forName("org.astrogrid.datacenter.delegate.agdirect.DirectDelegate");
         
            Constructor constr = delegate.getConstructor(new Class[] {Certification.class });
            return (AdqlQuerier) constr.newInstance(new Object[] {user});
         
         }
         catch (Exception e) {
            //other exceptions raised by constructor of the direct delegate
            throw new RuntimeException(e);
         
         }
      }
      
      throw new IllegalArgumentException("Service Type '"+serviceType+"' unknown");
   }
   
   /**
    * Creates a SQL-passthrough-implementing delegate given an endpoint (a
    * url to the service). and a service type.
    * <p>
    * For a test call, use:
    * <pre>
    *    querier = makeSqlQuerier(Certification.ANONYMOUS, "something random", DatacenterDelegateFactory.DUMMY_SERVICE
    * </pre>
    */
   public static SqlQuerier makeSqlQuerier(Certification user, String givenEndPoint, String serviceType) throws ServiceException, MalformedURLException, IOException {

      if (serviceType.equals(DUMMY_SERVICE)) {
         //easy one, return a dummy
         return new DummyDelegate();
      }

      if (serviceType.equals(ASTROGRID_WEB_SERVICE)) {
         return new WebDelegate(user, new URL(givenEndPoint));
      }
      
      throw new IllegalArgumentException("Service Type '"+serviceType+"' unknown for making SQL Queriers");
   }
}

/*
 $Log: DatacenterDelegateFactory.java,v $
 Revision 1.8  2003/12/03 19:37:03  mch
 Introduced DirectDelegate, fixed DummyQuerier

 Revision 1.7  2003/11/26 16:31:46  nw
 altered transport to accept any query format.
 moved back to axis from castor

 Revision 1.6  2003/11/25 15:59:55  mch
 Added doc

 Revision 1.5  2003/11/25 15:46:25  mch
 Added certification and service types; deprecated older methods

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


