package org.astrogrid.mySpace.mySpaceManager;

import java.io.*;
import java.util.Date;
import java.util.Locale;
import java.text.*;

/**
 * The <code>ServerDetails</code> class represents the details held in the
 * MySpace registry for a single MySpace server.  The details held for
 * each server are: its name, expiry period, URI and base directory.
 *
 * <p>
 * The class has two constructors.  In one values for all the member
 * variables are passed as arguments.  The other is a dummy with no
 * arguments.  There are no <code>set</code> methods, but there is a
 * <code>get</code> method for every member variable.
 *
 * @author A C Davenhall (Edinburgh)
 * @version Iteration 4.
 */

public class ServerDetails
{  
   private String name;           // Full Name.
   private int    expiryPeriod;   // Expiry period (days).
   private String URI;            // External URI.
   private String directory;      // Base directory.

//
// -------------------------------------------------------------------------

//
// Constructors.

/**
 * Constructor in which arguments comprising the details of the server
 * are passed.
 *
 * @param serverName The name of the server.
 * @param expiryPeriod The expiry period, in days.
 * @param URI The URI to access the server.
 * @param directory The base directory of files on the server
 */

   public ServerDetails (String name, int expiryPeriod, String URI,
     String directory)
   {  this.name = name;
      this.expiryPeriod = expiryPeriod;
      this.URI = URI;
      this.directory = directory;
   }

/**
 * Constructor with no arguments.  All the member variables are set to
 * null or -1.
 */

   public ServerDetails ()
   {  this.name = null;
      this.expiryPeriod = -1;
      this.URI = null;
      this.directory = null;
   }

//
// -------------------------------------------------------------------------

//
// Get methods.
//
// The Server class has a get method for every member variable.

/**
 * Return the name of the server.
 *
 * @return The name of the server.
 */

   public String getName()
   {  return name;
   }

/**
 * Return the expiry period of the server.
 *
 * @return The expiry period, in days, of the server.
 */

   public int getExpiryPeriod()
   {  return expiryPeriod;
   }

/**
 * Return the URI to access the server.
 *
 * @return The URI of the server.
 */

   public String getURI()
   {  return URI;
   }

/**
 * Return the base directory for the server.
 *
 * @return The base directory of the server.
 */

   public String getDirectory()
   {  return directory;
   }

//
// -------------------------------------------------------------------------

//
// Other methods.

/**
 * Produce a reasonable string representation of a
 * <code>ServerDetails</code>.
 *
 * @return A String representation of the server details.
 */

   public String toString()
   {  String returnString;

      if ( (name== null) && (expiryPeriod == -1) && (URI == null) &&
           (directory == null) )
      {  returnString = "(The object is undefined.)";
      }
      else
      {  returnString = name + " (" + expiryPeriod + " days, " + URI + ").";
      }

      return returnString;
   }
}
