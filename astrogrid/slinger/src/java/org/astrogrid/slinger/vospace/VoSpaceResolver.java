/*
 * $Id: VoSpaceResolver.java,v 1.2 2004/12/07 01:33:36 jdt Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.slinger.vospace;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.Principal;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.community.common.exception.CommunityException;
import org.astrogrid.community.resolver.CommunityAccountSpaceResolver;
import org.astrogrid.community.resolver.exception.CommunityResolverException;
import org.astrogrid.config.ConfigException;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.registry.client.RegistryDelegateFactory;
import org.astrogrid.registry.client.query.RegistryService;
import org.astrogrid.slinger.StoreException;
import org.astrogrid.slinger.myspace.MSRL;
import org.astrogrid.slinger.vospace.HomespaceName;

/**
 * A VoSpaceResolver is used to resolve actual locations from IVORNs to any
 * storepoints anywhere on the Virtual Observatory.
 *
 * This class takes IVO ids and resolves these to Agsls (and can therefore
 * resolve to streams) to access the the store services or files.
 * As such it is really a factory, making appropriate delegates/etc as required
 *
 * It makes use of the registry to look up IVORNs
 *
 */

public class VoSpaceResolver {
   
  private static CommunityAccountSpaceResolver community = null;

   private static RegistryService registry = null;
 
   private static Log log = LogFactory.getLog(VoSpaceResolver.class);

   /**
    * Given an IVO Resource Name, resolves an output stream to get to it
    */
   public static OutputStream resolveOutputStream(IVOSRN ivosrn, Principal user) throws IOException {
       return resolveMsrl(ivosrn).resolveOutputStream(user);
   }

   
   public static InputStream resolveInputStream(IVOSRN ivosrn, Principal user) throws IOException {
       return resolveMsrl(ivosrn).resolveInputStream(user);
   }

   /**
    * Given an IVO Storepoint Resource Name, resolves a Locator from it.  Assumes
    * a myspace locator at the moment
    * @todo This might
    * be an MSRL or a URL or an SRB at the moment.  Unfortunately not 'typeing' this stuff well...
    */
   public static MSRL resolveMsrl(IVOSRN ivorn) throws IOException {
      if (ivorn == null) throw new IllegalArgumentException("Cannot resolve null ivorn");
      
      if (registry == null) {
         makeRegistryDelegate();
      }

      //preserve for after resolving...
      /*
      String fragment = "";
      if (ivorn.getFragment() != null) {
         fragment = ivorn.getFragment();
      }
       */
      
      //used for debug/Principal info - says somethign about where the ivorn has been looked for
      String lookedIn = "";
      
      //look up in config first
      String key = "ivorn."+ivorn.getPath();
      String ms = SimpleConfig.getSingleton().getString(key, null);
      lookedIn += "Config (key="+key+") ";
      if (ms != null) {
         MSRL msrl = new MSRL(new MSRL(ms).getDelegateEndpoint(), ivorn.getFragment());
         log.trace("Config-Resolved "+ivorn+" to "+msrl);
         return msrl;
      }
      
      //if not found, see if the registry can resolve it
      try {
         lookedIn += ", Registry "+registry;
         String resolvedEndPoint = registry.getEndPointByIdentifier(ivorn.getPath());

         if (resolvedEndPoint == null) {
            //if not found, throw an exception
            throw new FileNotFoundException("Cannot resolve "+ivorn+" from "+lookedIn);
         }
         else {
            //hmmm yes but what have we got back? a myspace endpoint or an http endpoint?
            //assume myspace for now...
            log.trace("Registry-Resolved "+ivorn+" to "+resolvedEndPoint+" (from "+registry+")");
            return new MSRL(new URL(resolvedEndPoint), ivorn.getFragment());
         }
      }
      catch (RegistryException e) {
         IOException ioe = new IOException(e+", getting endpoint for '"+ivorn+"' from "+registry);
         ioe.setStackTrace(e.getStackTrace());
         throw ioe;
      }
   }
   
   /** Convenience routine to resolve an output stream to a homespace name */
   public static OutputStream resolveOutputStream(HomespaceName homespace, Principal user) throws IOException {
      return resolveOutputStream(resolveIvosrn(homespace), user);
   }

   /**
    * Returns the ivorn to the myspace for the community specified by the givne homespace,,
    * preserving that homespaces's path to the ivorn
    */
   public static IVOSRN resolveIvosrn(HomespaceName homespace) throws IOException {

      //used for debug/user info - says somethign about where the ivorn has been looked for
      String lookedIn = "";

      //create backwards compatible ivorn-syntax homespace, as community still expects the form "ivo://community/individual#path"
      int atIdx = homespace.getName().indexOf("@");
      if (atIdx == -1) {
         throw new IllegalArgumentException("homespace "+homespace+" should be of the form homespace:<individual>@<community>[#path]");
      }
      String individualId = homespace.getName().substring(0,atIdx);
      String communityId = homespace.getName().substring(atIdx+1);
      org.astrogrid.store.Ivorn oldHomespaceId = new org.astrogrid.store.Ivorn(communityId, individualId, homespace.getPath());

      //look up in config first
      String key = "homespace."+homespace.getName();
      String value = SimpleConfig.getSingleton().getString(key, null);
      lookedIn += "Config (key=homespace."+homespace.getName()+") ";
      if (value != null) {
         if (IVOSRN.isIvorn(value)) {
            try {
               return new IVOSRN( new IVOSRN(value), homespace.getPath());
            }
            catch (URISyntaxException use) {
               throw new ConfigException(use+" config value for homespace key "+key+" is not a valid IVORN: "+value);
            }
         }
         else {
            throw new ConfigException("Config value for homespace key '"+key+"' is not an IVORN: "+value);
         }
      }

      //look up old id in config
      value = SimpleConfig.getSingleton().getString(oldHomespaceId.getPath(), null);
      lookedIn += "Config (key="+oldHomespaceId.getPath()+") ";
      if (value != null) {
         if (value.startsWith("ivo:")) {
            try {
               return new IVOSRN( new IVOSRN(value), homespace.getPath());
            }
            catch (URISyntaxException use) {
               throw new ConfigException(use+" config value for homespace key "+key+" is not a valid IVORN: "+value);
            }
         }
         else {
            throw new ConfigException("Config value for homespace key '"+key+"' is not a valid IVORN: "+value);
         }
      }
      
      
      //lazy load delegate - also more robust in case it doesn't instantiate
      if (community == null) {
         makeCommunityDelegate();
      }
      try {
//         Ivorn homespaceIvorn = community.resolve(homespace);
         lookedIn += "Community ("+community+") ";
         IVOSRN storepoint = new IVOSRN(new IVOSRN(community.resolve(oldHomespaceId).toString()), homespace.getPath());
         return storepoint;
      }
      catch (URISyntaxException use) {
         throw new StoreException(use+" from IVORN returned by community delegate for "+oldHomespaceId);
      }
      catch (CommunityResolverException cre) {
         //don't ignore this - if it is trying to be resolved, use the config to do shortcut that
         throw new StoreException(cre+" resolving "+homespace+" ("+oldHomespaceId+") from community "+community+", looked in "+lookedIn,cre);
      }
      catch (CommunityException ce) {
         throw new StoreException(ce+" resolving "+homespace+" ("+oldHomespaceId+") from community "+community+", looked in "+lookedIn,ce);
      }
      catch (RegistryException re) {
         throw new StoreException(re+" resolving "+homespace+" ("+oldHomespaceId+") from community "+community+", looked in "+lookedIn,re);
      }
   }
   
 
   /**
    * Lazy registry delegate load
    */
   private static synchronized void makeRegistryDelegate() throws IOException {
      if (registry == null) {
         registry = RegistryDelegateFactory.createQuery();
      }
   }

   
   /**
    * Lazy community delegate load
    *
    */
   private static synchronized void makeCommunityDelegate() throws IOException {
      
      if (community == null) {
         community = new CommunityAccountSpaceResolver();
      }
      
   }
}

/*
$Log: VoSpaceResolver.java,v $
Revision 1.2  2004/12/07 01:33:36  jdt
Merge from PAL_Itn07

Revision 1.1.2.5  2004/12/06 21:03:16  mch
Fixes to resolving homespace


 */




