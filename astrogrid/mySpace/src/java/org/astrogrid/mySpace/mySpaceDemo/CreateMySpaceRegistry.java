// package org.astrogrid.mySpace.mySpaceDemo;

import java.io.*;
import java.util.*;

import org.astrogrid.mySpace.mySpaceManager.*;
import org.astrogrid.mySpace.mySpaceStatus.MySpaceStatus;
import org.astrogrid.mySpace.mySpaceStatus.MySpaceStatusCode;

/**
 * Program to create a new MySpace registry.  The MySpace registry
 * comprises a database of two tables.  One table will hold the files
 * known to the MySpace system and is left open.  The other holds the
 * details of the servers comprising the MySpace system and is populated
 * with a set of server details read from a text file.
 *
 * @author A C Davenhall (Edinburgh)
 * @version Iteration 3.
 */

class CreateMySpaceRegistry
{  private static MySpaceStatus status = new MySpaceStatus();

/**
 * Read the file containing details of the servers known to the
 * MySpace system. 
 */

   private Vector readServersFile(String serversFileName)
   {  Vector servers = new Vector();

//
//   Try to read the file

      try
      {  File serversFile = new File(serversFileName);
         FileReader in = new FileReader(serversFile);
         char c[] = new char[(int)serversFile.length()];
         in.read(c);
         String s = new String(c);

//
//      Convert the input file into a vector of individual lines.

         Vector inputLines = new Vector();

         StringTokenizer token = new StringTokenizer(s, "\n");

         while(token.hasMoreTokens())
         {  inputLines.add(token.nextToken());
         }

//
//      Examine every line, decoding the details of the registry
//      configuration.

         String currentLine = "";
         int lineCount = 0;
         int numServers = 0;

         Iterator iter = inputLines.iterator();
         while(iter.hasNext())
         {  lineCount = lineCount + 1;

            currentLine = (String)iter.next();

            System.out.println("processing line " + lineCount 
              + ": " + currentLine);

//
//         Remove any comments.  Remember that comments start with a
//         '#' character.

            int commentPos = currentLine.indexOf("#");
            if (commentPos > 0)
            {  currentLine = currentLine.substring(0, commentPos-1);
            }

//
//         Split the remaining line into a series of elements (or
//         'tokens') separated by blanks.  Proceed if there are any
//         such elements.

            StringTokenizer currentLineToken =
              new StringTokenizer(currentLine);

            int    currentElementCount = -1;
            Vector currentElements = new Vector();

            while(currentLineToken.hasMoreTokens())
            {  currentElementCount = currentElementCount + 1;

               String currentElem = currentLineToken.nextToken();
//               System.out.println(currentElem);

               currentElements.add(
                 currentElementCount, currentElem);
            }

            int numElements = currentElements.size();

            if (numElements > 0)
            {
//
//            Check if the first element corresponds to a server.
//            Currently servers are the only type of item which can
//            appear in the file.

               if (((String)currentElements.get(0)).equals("server"))
               {  if (numElements > 4)
                  {  String currentName = (String)currentElements.get(1);

                     String stringExpiry = (String)currentElements.get(2);
                     Integer expiry = new Integer(stringExpiry);
                     int currentExpiry = expiry.intValue();

                     String currentURI = (String)currentElements.get(3);
                     String currentDirectory =
                       (String)currentElements.get(4);

                     numServers = numServers + 1;

                     ServerDetails currentServer = new ServerDetails(
                       currentName, currentExpiry, currentURI,
                       currentDirectory);

                     servers.add(currentServer);
                  }
                  else
                  {  MySpaceStatus status  =
                       new MySpaceStatus(
                         MySpaceStatusCode.AGMMCW00151,
                         MySpaceStatusCode.WARN,
                         MySpaceStatusCode.NOLOG,
                         this.getClassName() );
                  }
               }
            }
         }

//
//      Check that at least one server has been specified, and if not
//      report an error.

         if (servers.size() < 1)
         {  MySpaceStatus status  =
              new MySpaceStatus(MySpaceStatusCode.AGMMCE00104,
                MySpaceStatusCode.ERROR, MySpaceStatusCode.NOLOG,
                this.getClassName());

            servers = null;
         }

         in.close();
      }

//
//   Note: any exception caught here is likely to be an I/O exception,
//   but other types are possible, so a catch-all exception is used.

      catch (Exception all)
      {  MySpaceStatus status = new MySpaceStatus(
           MySpaceStatusCode.AGMMCE00104, MySpaceStatusCode.ERROR,
           MySpaceStatusCode.LOG, this.getClassName() );

         System.out.println("Cannot read input file.\n");
         all.printStackTrace();

         servers = null;
      }

      return servers;
   }

//
// -------------------------------------------------------------------

/**
 * Obtain the name of the current Java class.
 */

   protected String getClassName()
   { Class currentClass = this.getClass();
     String name =  currentClass.getName();
     int dotPos = name.lastIndexOf(".");
     if (dotPos > -1)
     {  name = name.substring(dotPos+1, name.length() );
     }

     return name;
   }

//
// -------------------------------------------------------------------

   public static void main (String argv[])
   {  if (argv.length == 1)
      {  
//
//      argv[0] contains the name of the MySpace registry.

         String registryName = argv[0]; 

//
//      Construct the name of the file holding the servers' details
//      from the registry name.

         String serversFileName = registryName + ".servers";

//
//      Attempt to read and parse the file of servers' details and
//      proceed of ok.

         CreateMySpaceRegistry createReg = new CreateMySpaceRegistry();
         Vector servers = createReg.readServersFile(serversFileName);
         if (servers != null)
         {
//
//         Attempt to create the MySpace registry.

            RegistryManager reg = new RegistryManager(registryName,
              servers);
            if (status.getSuccessStatus())
            {  System.out.println("MySpace registry " + registryName +
                 " created successfully.");
            }
            else
            {  System.out.println("\n*** Failed to create MySpace " +
                 "registry " + registryName + ".");
            }
         }
         else
         {  System.out.println("\n*** Failed to read file " +
              serversFileName + " containing details of the servers.");
         }
      }
      else
      {  System.out.println("Usage:-");
         System.out.println("  java CreateMySpaceRegistry registryName");
      }
   }
}
