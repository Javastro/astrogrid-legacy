package org.astrogrid.mySpace.mySpaceDemo;

import java.io.*;
import java.util.*;

import org.astrogrid.mySpace.mySpaceManager.*;

/**
 * Minimal program to create a add new users to a MySpace registry.
 * For each user entries for his top-level container (corresponding to
 * him as a user) and his  second-level containers for all(corresponding
 * each of the servers which he can access) should be added.  The full
 * names of these containers are read from a file.
 *
 * @author A C Davenhall (Edinburgh)
 * @version Iteration 3.
 */

class AddUsersToRegistry
{  public static void main (String argv[])
   {  if (argv.length == 1)
      {  String registryName = argv[0]; //argv[0] is "example"

         String newUsersFileName = registryName + ".newusers";

//
//      Attempt to open the input file from which the list
//      of top- and second-level containers for the new users are to be
//      read.

         try
         {  File newUsersFile = new File(newUsersFileName);
            FileReader in = new FileReader(newUsersFile);
            char c[] = new char[(int)newUsersFile.length()];
            in.read(c);
            String s = new String(c);

//
//         Convert the input file into a vector of individual lines.

            Vector inputLines = new Vector();

            StringTokenizer token = new StringTokenizer(s, "\n");

            while(token.hasMoreTokens())
            {  inputLines.add(token.nextToken());
            }

//
//         Create a registry manager to which the new entries will be
//         written.

            RegistryManager reg = new RegistryManager(registryName);

            DataItemRecord itemRec = new DataItemRecord();

            Date creation = new Date();
            creation = Calendar.getInstance().getTime();

//
//         Loop through the lines read from the input file creating an
//         entry in the registry for each one.

            for (int loop = 0; loop < inputLines.size(); loop++)
            {  String containerName = (String)inputLines.get(loop);

//
//            Check that this container does not already exist.

               Vector vec = reg.lookupDataItemRecords(containerName);
               if (vec.size() == 0)
               {

//
//               Create an entry for the current entry.

//                  int dataItemID = reg.getNextDataItemID();
// TODO pah - have commented out the above and entered 0 as an argument below...
                  itemRec = new DataItemRecord(containerName,
                    -1, "none", "sysadmin", creation, creation, 0,
                    DataItemRecord.CON, "permissions");

//
//               Add the entry to the registry.
//TODO pah changed this from a boolean - not sure if null is a failure condition...
                  DataItemRecord addSuccess = reg.addDataItemRecord(itemRec);
                  if (addSuccess!=null)
                  {  System.out.println("  Added: " + containerName);
                  }
                  else
                  {  System.out.println("  *** Failed to add: " +
                       containerName);
                  }
               }
               else
               {  System.out.println("  *** Container " + containerName +
                    " already exists in the registry.");
               }
            }

//
//         Write the registry file to disk.
//TODO pah - not needed any more?
//            reg.rewriteRegistryFile();

//
//         Close the input file.

            in.close();
         }
         catch (java.io.IOException ex)
         {  System.out.println(
              "Failed to read the file of new users " +
              newUsersFileName);

            ex.printStackTrace();
         }
      }
      else
      {  System.out.println("Usage:-");
         System.out.println("  java AddUsersToRegistry registryName");
      }
   }
}
