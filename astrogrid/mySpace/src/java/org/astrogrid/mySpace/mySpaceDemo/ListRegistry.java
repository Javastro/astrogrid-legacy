import java.io.*;
import java.util.*;

import org.astrogrid.mySpace.mySpaceManager.*;

/**
 * Program to list all the entries in a MySpace registry to standard
 * output.
 *
 * @author A C Davenhall (Edinburgh)
 * @version Iteration 2.
 */

class ListRegistry
{  public static void main (String argv[])
   {  if (argv.length == 1)
      {
//
//      argv[0] contains the name of the registry (not the name of
//      of the registry file, ie. the name does not have the ".reg"
//      file-type on the end.

         String registryName = argv[0];

         String registryFileName = registryName + ".reg";

//
//      Try to open and read the registry file.

         try
         {  File registryFile = new File(registryFileName);
            FileInputStream ris = new FileInputStream(registryFile);
            ObjectInputStream ois = new ObjectInputStream(ris);

//
//         Read and de-serialise DataItemRecord from the input file.

            boolean more = true;
            int count = 0;

            while (more == true)
            {  try
               {  DataItemRecord itemRec = (DataItemRecord)ois.readObject();
                  count = count + 1;

                  System.out.println("["
                    + itemRec.getDataItemID() + "] "
                    + itemRec.toString() );
               }
               catch (IOException e)
               {  more = false;
               }
               catch (ClassNotFoundException cnfe)
               {  more = false;
                  System.out.println("Illegal object in registry file.");
               }
            }

            System.out.println(" ");
            System.out.println("Number of entries = " + count);

//
//         Close the input file.

            ois.close();
         }
         catch (IOException e)
         {  System.out.println("Failure reading the registry file.");
         }
      }
      else
      {  System.out.println("Usage:-");
         System.out.println("  java ListRegistry registryName");
      }
   }
}
