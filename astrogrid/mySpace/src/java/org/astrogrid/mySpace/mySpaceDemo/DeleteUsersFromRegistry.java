package org.astrogrid.mySpace.mySpaceDemo;

import java.io.*;
import java.util.*;

import org.astrogrid.mySpace.mySpaceManager.*;

/**
 * Minimal program to delete users from a MySpace registry.
 * For each user the program checks that the only remaining entries for
 * the user in the registry are containers (ie. there are no VOTables
 * or other files).  If this condition is satisfied it deletes all the
 * entries for the user from the registry.
 *
 * @author A C Davenhall (Edinburgh)
 * @version Iteration 3.
 */

class DeleteUsersFromRegistry
{  public static void main (String argv[])
   {  if (argv.length == 1)
      {  String registryName = argv[0]; //argv[0] is "example"

         String deadUsersFileName = registryName + ".deadusers";

//
//      Attempt to open the input file from which the list of users to
//      be deleted is to be read.

         try
         {  File deadUsersFile = new File(deadUsersFileName);
            FileReader in = new FileReader(deadUsersFile);
            char c[] = new char[(int)deadUsersFile.length()];
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

//
//         Loop through the lines read from the input file.

            for (int loop = 0; loop < inputLines.size(); loop++)
            {  String user = (String)inputLines.get(loop);

//
//            Lookup all the entries for this user in the registry and
//            proceed if some entries were found.

               String query = user + "*";

               Vector entries = reg.lookupDataItemRecords(query);
               if (entries.size() > 0)
               {  boolean deleteOk = true;

//
//               Examine every entry and check that it is a container.

                  for (int currentEntry = 0; currentEntry < entries.size();
                    currentEntry++)
                  {  itemRec =
                       (DataItemRecord)entries.elementAt(currentEntry);

                     if (itemRec.getType() != DataItemRecord.CON)
                     {  deleteOk = false;

                        System.out.println("      Extant dataHolder: " +
                          itemRec.getDataItemName() +
                          " belonging to user " + user + ".");
                     }
                  }

//
//               Proceed to delete the user if all his entries were
//               containers.

                  if (deleteOk)
                  {  boolean allRemoved = true;

                     for (int currentEntry = 0; currentEntry < entries.size();
                       currentEntry++)
                     {  itemRec =
                          (DataItemRecord)entries.elementAt(currentEntry);

                        int itemRecID = itemRec.getDataItemID();
                        boolean deleteStatus =
                          reg.deleteDataItemRecord(itemRecID);

                        if (!deleteStatus)
                        {  allRemoved = false;

                           System.out.println(
                             "      Failed to delete container " +
                             itemRec.getDataItemName() +
                             " belonging to user " + user + ".");
                        }
                     }

                     if (allRemoved)
                     {  System.out.println("  Deleted user " + user + ".");
                     }
                     else
                     {  System.out.println("  *** Failed to delete " +
                          user + " cleanly.");
                     }
                  }
                  else
                  {  System.out.println("  *** Could not delete user " +
                       user + "; dataHolders still exist.");
                  }
               }
               else
               {  System.out.println("  *** No entries found for user "
                    + user + ".");
               }
            }

//
//         Write the registry file to disk.

            reg.rewriteRegistryFile();

//
//         Close the input file.

            in.close();
         }
         catch (java.io.IOException ex)
         {  System.out.println(
              "Failed to read the file of dead users " +
              deadUsersFileName);

            ex.printStackTrace();
         }
      }
      else
      {  System.out.println("Usage:-");
         System.out.println("  java DeleteUsersFromRegistry registryName");
      }
   }
}
