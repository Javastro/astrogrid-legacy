/*
 * $Id: VoSpaceResolver.java,v 1.19 2004/04/21 11:51:58 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.store.delegate;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.community.User;
import org.astrogrid.community.common.exception.CommunityException;
import org.astrogrid.community.resolver.CommunityAccountSpaceResolver;
import org.astrogrid.community.resolver.exception.CommunityResolverException;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.registry.client.RegistryDelegateFactory;
import org.astrogrid.registry.client.query.RegistryService;
import org.astrogrid.store.Agsl;
import org.astrogrid.store.Ivorn;
import org.astrogrid.store.delegate.StoreDelegateFactory;

/**
 * A VoSpaceResolver is used to resolve actual locations from IVORNs to any
 * storepoints anywhere on the Virtual Observatory.
 *
 * This class takes IVO ids and resolves these to Agsls (and can therefore
 * resolve to streams) to access the the store services or files.
 * As such it is really a factory, making appropriate delegates/etc as required
 *
 * It makes use of the registry and commnunity to look up IVORNs
 * --------------------------------------------------------------------
 *
 * -------------------------------------------------------------------
 *
 */

public class VoSpaceResolver {
   
   private static RegistryService registry = null;
   private static CommunityAccountSpaceResolver community = null;

   private static Log log = LogFactory.getLog(VoSpaceResolver.class);
   
//   private static final String VOSPACE_CLASS_RESOURCE_KEY_LOOKUP = "org.astrogrid.store.myspace.MyspaceMgr";
   private static final String VOSPACE_CLASS_RESOURCE_KEY_LOOKUP = "myspace";
   
   public static final String AUTOINT_MYSPACE_IVOKEY = "org.astrogrid.localhost/myspace";
   public static final String AUTOINT_MYSPACE_AGSL = "astrogrid:store:myspace:http://localhost:8080/astrogrid-mySpace-SNAPSHOT/services/Manager";
   
   /**
    * Given an IVO Resource Name, resolves the AGSL
    */
   public static Agsl resolveAgsl(Ivorn ivorn) throws IOException {

      //preserve for after resolving...
      String fragment = "";
      if (ivorn.getFragment() != null) {
         fragment = "#"+ivorn.getFragment();
      }
      
      //used for debug/user info - says somethign about where the ivorn has been looked for
      String lookedIn = "";
      
      Agsl agsl = null;
      
      //look up in config first
      String s = SimpleConfig.getSingleton().getString(ivorn.getPath(), null);
      lookedIn += "Config (key="+ivorn.getPath()+") ";
      if (s != null) {
         agsl = new Agsl(s+fragment);
         
      }

      try {

         //if not found, see if it's an account identifier, and if so get that account's homespace (eg that community's myspace)
         if (agsl == null) {
            Ivorn homespace = getCommunityMySpace(ivorn);
   
            lookedIn += ", Community "+community+ "(->"+homespace+")";
            if (homespace != null)  {
               ivorn = homespace;
               /*
               try {
                  //new Ivorn(homespace.toString()+fragment); //now resolve this
               }
               catch (URISyntaxException e) {
                  throw new ResolverException("community resolved "+ivorn+" to myspace "+homespace+", but "+homespace+"#"+fragment+" is not a valid IVORN");
               }
                */
            }
         }
         
         //if not found, see if the registry can resolve it
         if (agsl == null) {
            agsl = resolveUsingRegistry(ivorn);
            lookedIn += ", Registry "+registry;
         }
      }
      catch (ResolverException re) {
         //if it's the special hardcoded auto-integration resolver then we want
         //to ignore any problems and continue to hard-resolve it.  Otherwise
         //rethrow the exception
         if (!ivorn.getPath().toString().equals(AUTOINT_MYSPACE_IVOKEY)) {
            throw re;
         }
      }
         
      //if not found, use hardcoded entries for auto-integration tests
      if (agsl == null) {
         if (ivorn.getPath().toString().equals(AUTOINT_MYSPACE_IVOKEY)) {
            agsl = new Agsl(AUTOINT_MYSPACE_AGSL+fragment);
         }
      }
      
      //if not found, throw an exception
      if (agsl == null) {
         throw new FileNotFoundException("Cannot resolve "+ivorn+" from "+lookedIn);
      }else {
         log.trace("Resolved "+ivorn+" to "+agsl);
      }

      return agsl;
   }
   
   /**
    * Given an IVO Resource Name, looks it up in the community/registry, and
    * returns the appropriate StoreClient to access it. Ignores any fragment
    * given.  The user is the account attempting to access the store.
    */
   public static StoreClient resolveStore(User user, Ivorn ivorn) throws IOException {
      return StoreDelegateFactory.createDelegate(user, resolveAgsl(ivorn));
   }

   /**
    * Given an IVO Resource Name, looks it up in the community/registry, and
    * returns the appropriate StoreAdminClient to administer it. Ignores any fragment
    * given.  The user is the account attempting to access the store.
    */
   public static StoreAdminClient resolveStoreAdmin(User user, Ivorn ivorn) throws IOException {
      return StoreDelegateFactory.createAdminDelegate(user, resolveAgsl(ivorn));
   }
   
   /**
    * Given an IVO Resource Name, resolves a Stream to read from the given
    * lcoation.  The user is the account
    * attempting to access the file.
    */
   public static InputStream resolveInputStream(User user, Ivorn ivorn) throws IOException {
      return resolveAgsl(ivorn).openInputStream(user);
   }
   
   /**
    * Given an IVO Resource Name, resolves a Stream to write to the given location.  The user is the account
    * attempting to access the file.
    */
   public static OutputStream resolveOutputStream(User user, Ivorn ivorn) throws IOException {
      return resolveAgsl(ivorn).openOutputStream(user);
   }

   /**
    * Given an IVORN, does a straight resolve using the registry delegate
    */
   private static Agsl resolveUsingRegistry(Ivorn ivorn) throws IOException {

      if (ivorn == null) throw new IllegalArgumentException("Cannot resolve null ivorn");
      
      if (registry == null) {
         makeRegistryDelegate();
      }

      try {
         String endPoint = registry.getEndPointByIdentifier(ivorn.getPath());
         if (endPoint == null) {
            return null;
         }
         else {
            return new Agsl(endPoint, ivorn.getFragment());
         }
      }
      catch (RegistryException e) {
         throw new ResolverException("Failed to find agsl endpoint for '"+ivorn+"' using "+registry);
      }
      
   }
   
   /** Resolve IVORN using Registry
    *
   private static Agsl registryResolve(Ivorn ivorn) throws IOException {
      //lazy load registry delegate - also more robust in case it doesn't instantiate
      log.trace("entered registry resolve");
      if (registry == null) {
         makeRegistryDelegate();
      }
      
      if (registry != null) {
         try {
            //look in registry
            CommunityIvornParser ci = new CommunityIvornParser(ivorn);
            System.out.println("the commname = " + ci.getCommunityName());
            System.out.println("the account name = " + ci.getAccountName());
            System.out.println("the remainder = " + ci.getRemainder());
            String remainder = "";
            if(ci.getRemainder() != null && ci.getRemainder().trim().length() > 0) {
               remainder = ci.getRemainder();
               if(remainder.startsWith("#")) {
                  //cut off the first character the agsl will put it back in later.
                  remainder = remainder.substring(1);
               }
            }

            String ident = ci.getCommunityName() ;
            if (null != ci.getAccountName()) {
              ident = ident + "/" + ci.getAccountName() ;
            }

            System.out.println("Lookfor endpoint with ident = " + ident);
            String vospaceEndPoint = registry.getEndPointByIdentifier(ident);
            if(vospaceEndPoint == null) {
               //Okay got a vospaceendpoint back lets check if their was no resource key if so then
               //lets try calling the registry through our known resource key which is the myspace class.
               vospaceEndPoint = registry.getEndPointByIdentifier(ci.getCommunityName() + "/" + VOSPACE_CLASS_RESOURCE_KEY_LOOKUP);
               if(vospaceEndPoint == null) {
                  throw new ResolverException("Registry failed resolving "+ivorn);
               }
               remainder = ci.getAccountName() + remainder;
            }
            System.out.println("Quick debug: Registry found = " + vospaceEndPoint + " with remainder = " + remainder);
            return new Agsl(Msrl.SCHEME + ":" + vospaceEndPoint,remainder);
         }
         catch (Exception e) {
            throw new ResolverException("Registry failed resolving "+ivorn,e);
         }
      }
      return null;
      
   }
   
   /** Resolve using Registry
    *
   private static Agsl registryMyspaceResolve(Ivorn ivorn) throws IOException {
      //lazy load registry delegate - also more robust in case it doesn't instantiate
      System.out.println("entered registry resolve specefic for Myspace");
      if (registry == null) {
         try {
            makeRegistryDelegate();
         }
         catch (Exception e) {
            log.error("Could not create Registry Delegate: ",e);
         }
      }
      
      if (registry != null) {
         try {
            //look in registry
            CommunityIvornParser ci = new CommunityIvornParser(ivorn);
            System.out.println("the commname = " + ci.getCommunityName());
            System.out.println("the account name = " + ci.getAccountName());
            System.out.println("the remainder = " + ci.getRemainder());
            String remainder = "";
            if(ci.getRemainder() != null && ci.getRemainder().trim().length() > 0) {
               remainder = ci.getRemainder();
               if(remainder.startsWith("#")) {
                  //cut off the first character the agsl will put it back in later.
                  remainder = remainder.substring(1);
               }
            }

            String ident = ci.getCommunityName();
            if (ci.getAccountName() == null || !VOSPACE_CLASS_RESOURCE_KEY_LOOKUP.equals(ci.getAccountName()) ) {
               ident = ident + "/" + VOSPACE_CLASS_RESOURCE_KEY_LOOKUP;
            }
            System.out.println("Look for endpoint with ident = " + ident);
            String vospaceEndPoint = registry.getEndPointByIdentifier(ident);
            System.out.println("Quick debug: Registry found = " + vospaceEndPoint + " with remainder = " + remainder);
            return new Agsl(Msrl.SCHEME + ":" + vospaceEndPoint,remainder);
         }
         catch (Exception e) {
            throw new ResolverException("Registry failed resolving "+ivorn,e);
         }
      }
      return null;
      
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
    * Returns the ivorn to the myspace for the community specified by the givne ivorn,
    * preserving that ivorn's fragment
    */
   private static Ivorn getCommunityMySpace(Ivorn communityIvorn) throws IOException {
      //lazy load delegate - also more robust in case it doesn't instantiate
      if (community == null) {
         makeCommunityDelegate();
      }
      try {
         Ivorn commIvo = community.resolve(communityIvorn);
         return commIvo;
      }
      catch (CommunityResolverException cre) {
         //ignore this - rather naughtily we assume that this is just a registry returns null point error
         log.trace(cre+" resolving "+communityIvorn+" from community "+community+", ignoring...");
         return null;
      }
      catch (CommunityException e) {
         throw new ResolverException("Community "+community+" failure returning myspace for community "+communityIvorn, e);
      }
//      catch (CommunityIdentifierException e) {
//         throw new ResolverException("Community "+community+" failed to return myspace for community "+communityIvorn, e);
//      }
//      catch (CommunityPolicyException e) {
//         throw new ResolverException("Community "+community+" failed to return myspace for community "+communityIvorn, e);
//      }
   
      catch (RegistryException e) {
         throw new ResolverException("Community "+community+" failed to return myspace for community "+communityIvorn, e);
      }
   }
   
   /** Resolve using Community
    *
   private static Agsl communityResolve(Ivorn ivorn) throws IOException {

      //lazy load delegate - also more robust in case it doesn't instantiate
      log.trace("entered communityResolvoe");
      if (community == null) {
         makeCommunityDelegate();
      }
      
      Ivorn commIvo = null;
      Ivorn regIvo = null;
      if (community != null) {
         try {
            commIvo = community.resolve(ivorn);
            CommunityIvornParser ci = new CommunityIvornParser(commIvo);
            log.info("Resolving "+ivorn+" using community "+ commIvo.toString() +
                     " (comname = " + ci.getCommunityName() +
                     ", account name = " + ci.getAccountName() +
                     ", remainder = " + ci.getRemainder()+")");

            String remainder = "" ;
            if (null != ci.getAccountName()) {
                remainder += "/" + ci.getAccountName();
            }

            if(ci.getRemainder() != null && ci.getRemainder().trim().length() > 0) {
               remainder += ci.getRemainder();
            }
            
            regIvo = new Ivorn(Ivorn.SCHEME + "://" + ci.getCommunityName() + "/" + VOSPACE_CLASS_RESOURCE_KEY_LOOKUP + remainder);

            return registryMyspaceResolve(regIvo);
         }catch(ResolverException re) {
            //okay the community found an ivo, but it was not in the registry
            throw new ResolverException("Community resolved "+ivorn+" to "+commIvo+", but registry failed to resolve resulting " + regIvo,re);
         } catch(Exception e) {
            throw new ResolverException(e+" resolving Ivorn "+ivorn+" (-> commIvo "+commIvo+", regIvo"+regIvo+")",e);
         }
      }
            
      return null;
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
Revision 1.19  2004/04/21 11:51:58  mch
Fix to resolve hardcoded ivorn if registry/community exceptions thrown

Revision 1.18  2004/04/21 10:35:50  mch
Fixes to ivorn/fragment resolving

Revision 1.17  2004/04/21 09:41:38  mch
Added softwired shortcut for auto-integration tests

Revision 1.16  2004/04/21 08:54:29  mch
Added special hardcoded resolving for auto-integration tests

Revision 1.15  2004/04/20 15:26:46  mch
More complete error reporting and simplified resolving (not as featureful ? as before)

Revision 1.14  2004/04/20 12:45:55  KevinBenson
commented out the printStackTrace

Revision 1.13  2004/04/16 08:15:38  KevinBenson
small change to split out the reolving of a "myspace" resourcekey.

Revision 1.12  2004/04/15 17:55:58  dave
Fixed ivorn handling

Revision 1.11  2004/04/06 09:00:46  KevinBenson
changed it around so that it calls community first then registry to figvure out endpoints

Revision 1.10  2004/04/05 12:47:22  KevinBenson
small changes and debug printouts for now.

Revision 1.9  2004/03/31 16:43:04  KevinBenson
Okay still needs some testing, but checking in for now.  almost ready.

Revision 1.8  2004/03/25 13:13:43  KevinBenson
new vospaceresolver

Revision 1.7  2004/03/22 10:25:42  mch
Added VoSpaceClient, StoreDelegate, some minor changes to StoreClient interface

Revision 1.6  2004/03/19 12:48:03  mch
added outputStream

Revision 1.5  2004/03/19 12:39:37  mch
Added StoreAdminClient implementation to LocalFileStore

Revision 1.4  2004/03/15 18:01:39  mch
Removed build dependency on castor

Revision 1.3  2004/03/15 17:26:53  mch
Added more robust (& lazy) registry & community delegate creation

Revision 1.2  2004/03/14 03:32:14  mch
Added openOutputStream

Revision 1.1  2004/03/12 13:09:11  mch
Changed name

Revision 1.4  2004/03/09 23:18:58  mch
Added Vorl for 4.1 access

Revision 1.3  2004/03/09 11:48:19  mch
Updated design for community call

Revision 1.2  2004/03/08 22:57:17  mch
Added placeholding for registry and community delegates

Revision 1.1  2004/03/01 22:38:46  mch
Part II of copy from It4.1 datacenter + updates from myspace meetings + test fixes

 */



