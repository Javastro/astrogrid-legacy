import java.io.*;
import java.util.*;

import org.astrogrid.mySpace.mySpaceManager.*;

/**
 * Program to create a new MySpace registry.  The registry is
 * initialised with top-level user containers and second-level
 * containers for all the servers which each user can access.
 * The full names for these containers are read from a file.
 *
 * @author A C Davenhall (Edinburgh)
 * @version Iteration 2.
 */

class CreateRegistry
{  public static void main (String argv[])
   {  if (argv.length == 1)
      {  String registryName = argv[0]; //argv[0] is "example"

         String registryFileName = registryName + ".reg";
         String registryInitialName = registryName + ".initial";

//
//      Attempt to open the input file from which the initial list
//      of top- and second-level containers are to be read.

         try
         {  File registryInitialFile = new File(registryInitialName);
            FileReader in = new FileReader(registryInitialFile);
            char c[] = new char[(int)registryInitialFile.length()];
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

            RegistryManager reg = new RegistryManager(registryName,
              "new");

            DataItemRecord itemRec = new DataItemRecord();

            Date creation = new Date();
            creation = Calendar.getInstance().getTime();

//
//         Loop through the lines read from the input file creating an
//         entry in the registry for each one.

            for (int loop = 0; loop < inputLines.size(); loop++)
            {

//
//            Create an entry for the current entry.

               String containerName = (String)inputLines.get(loop);
//               System.out.println("loop, containerName: "
//                 + loop + " " + containerName);

               itemRec = new DataItemRecord(containerName,
                 loop+1, "none", "sysadmin", creation, creation, 0,
                 DataItemRecord.CON, "permissions");

//
//            Add the entry to the registry.

               reg.addDataItemRecord(itemRec);
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
              "Failed to read registry initialisation file " +
              registryInitialName);

            ex.printStackTrace();
         }
      }
      else
      {  System.out.println("Usage:-");
         System.out.println("  java CreateRegistry registryName");
      }
   }
}
