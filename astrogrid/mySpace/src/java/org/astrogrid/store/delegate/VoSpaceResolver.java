/*
 * $Id: VoSpaceResolver.java,v 1.9 2004/03/31 16:43:04 KevinBenson Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.store.delegate;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.community.User;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.store.Agsl;
import org.astrogrid.store.Ivorn;
import org.astrogrid.store.Msrl;
import org.astrogrid.store.delegate.StoreDelegateFactory;


import org.astrogrid.registry.client.query.RegistryService;
import org.astrogrid.registry.client.RegistryDelegateFactory;
import org.astrogrid.community.resolver.CommunityAccountSpaceResolver;
import org.astrogrid.community.common.ivorn.CommunityIvornParser;

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
   
   private static final String VOSPACE_CLASS_RESOURCE_KEY_LOOKUP = "org.astrogrid.store.myspace.MyspaceMgr";   
   
   /**
    * Given an IVO Resource Name, resolves the AGSL
    */
   public static Agsl resolveAgsl(Ivorn ivorn) throws IOException {

      //NB the implementation is not complete until we have sorted out how this is supposed to work in detail
      
      //the fragment bit is not relevent to the service lookup - preserve it
      String fragment = ivorn.getFragment();

      Agsl agsl = null;
      
      //look up in config first
      String s = SimpleConfig.getSingleton().getString(ivorn.getPath(), null);
      if (s != null) {
         agsl = new Agsl(s);
      }

      if (agsl == null) {
         try {
            agsl = registryResolve(ivorn);
         }catch(ResolverException re) {
            re.printStackTrace();
            agsl = null;
         }
         
      }
      
      if (agsl == null) {
         agsl = communityResolve(ivorn);
      }

      if (agsl == null) {
         throw new FileNotFoundException("Cannot resolve "+ivorn);
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
   
   /** Resolve using Registry
    */
   public static Agsl registryResolve(Ivorn ivorn) throws IOException {
      /** This is the deprecated nyspace one; use the one in ivor if you want
       * real registry resolving *
       */
      //lazy load registry delegate - also more robust in case it doesn't instantiate
      System.out.println("entered registry resolve");
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
            String vospaceEndPoint = registry.getEndPointByIdentifier(ci.getCommunityName()+"/"+ci.getAccountName());
            if(vospaceEndPoint == null) {
               //Okay got a vospaceendpoint back lets check if their was no resource key if so then
               //lets try calling the registry through our known resource key which is the myspace class.
               vospaceEndPoint = registry.getEndPointByIdentifier(ci.getCommunityName() + "/" + VOSPACE_CLASS_RESOURCE_KEY_LOOKUP);
               if(vospaceEndPoint == null) {
                  throw new ResolverException("Registry failed resolving "+ivorn);
               }
            }
            System.out.println("Quick debug: Registry found = " + vospaceEndPoint);
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
    *
    */
   public static synchronized void makeRegistryDelegate() throws IOException {
      if (registry == null) {
         registry = RegistryDelegateFactory.createQuery();
      }
   }

   /** Resolve using Community
    */
   public static Agsl communityResolve(Ivorn ivorn) throws IOException {
      /**
       //lazy load delegate - also more robust in case it doesn't instantiate
        */ 
      System.out.println("entered communityResolvoe");
      if (community == null) {
         try {
            makeCommunityDelegate();
         }
         catch (Exception e) {
            log.error("Could not create Community Delegate: ",e);
         }
      }
      
      Ivorn commIvo = null;
      if (community != null) {
         try {
            commIvo = community.resolve(ivorn);
            CommunityIvornParser ci = new CommunityIvornParser(commIvo);
            System.out.println("Community resolved = " + commIvo.toString());
            System.out.println("the commname = " + ci.getCommunityName());
            System.out.println("the account name = " + ci.getAccountName());
            System.out.println("the remainder = " + ci.getRemainder());
            String remainder = "";
            if(ci.getRemainder() != null && ci.getRemainder().trim().length() > 0) {
               remainder = ci.getRemainder();
            }                        
            Ivorn regIvo = new Ivorn(ci.getCommunityName() + "/" + VOSPACE_CLASS_RESOURCE_KEY_LOOKUP + remainder);                        
            return registryResolve(regIvo);
         }catch(ResolverException re) {
            //okay the community found an ivo, but it was not in the registry
            throw new ResolverException("Community seems to resolve, but not registry " + ivorn + " commIvo = " + commIvo,re);
         } catch(Exception e) {
            throw new ResolverException("Community & Registry failed resolving "+ivorn,e);
         }
      }
            
      return null;
   }
   
   /**
    * Lazy community delegate load
    *
    */
   public static synchronized void makeCommunityDelegate() throws IOException {
      
      if (community == null) {
         community = new CommunityAccountSpaceResolver();
      }
      
   }
}

/*
$Log: VoSpaceResolver.java,v $
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

