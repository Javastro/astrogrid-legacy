import java.io.*;
import java.util.*;

import org.astrogrid.mySpace.mySpaceStatus.*;
import org.astrogrid.mySpace.mySpaceManager.*;

/**
 * Test program to thrash the registry. 
 *
 * @author A C Davenhall (Edinburgh)
 * @version Iteration 2.
 */

class ThrashRegistry
{  public static void main (String argv[])
   {  if (argv.length == 1)
      {  String registryName = argv[0];

         MySpaceActions myspace = new MySpaceActions();
         MySpaceStatus status = new MySpaceStatus();

         DataItemRecord newCont = new DataItemRecord();
         DataItemRecord newDataHolder = new DataItemRecord();
         DataItemRecord itemRec = new DataItemRecord();

         final int numDataHolders = 1000;

//
//      Set the registry file name.

         myspace.setRegistryName(registryName);

//
//      Outer loop creates containers.

         for (int contloop = 0; contloop < 10; contloop++)
         {  System.out.println("Processing container loop " +
              contloop + "...");

//
//         Create a container.

            String containerName = "/clq/serv2/cont" + contloop;

            newCont = myspace.createContainer(
                 "acd", "roe", "job27", containerName);

            int[] dataItemIDs = new int[numDataHolders];
            int dataItemID;
            String dataHolderName = "";
            String serverFileName = "";
            boolean deletedOk = true;

//
//         Create a 100 dataHolders in the container.

            for (int holderloop = 0; holderloop <numDataHolders ;
              holderloop++)
            {
               dataHolderName = containerName + "/dh"
                 + holderloop;
               serverFileName = "f" + holderloop;

               newDataHolder = myspace.importDataHolder(
                 "acd", "roe", "job27", dataHolderName,
                 serverFileName, 100 );

               dataItemID = newDataHolder.getDataItemID();
               dataItemIDs[holderloop] = dataItemID;
            }

//
//         Move and copy a couple of the dataHolders.

            dataItemID =  dataItemIDs[0];
            dataHolderName = containerName + "/move0";
            newDataHolder = myspace.moveDataHolder("acd", "roe", "job27",
              dataItemID, dataHolderName);

            dataItemID = newDataHolder.getDataItemID();
            dataHolderName = containerName + "/copy0";
            newDataHolder = myspace.copyDataHolder("acd", "roe", "job27",
              dataItemID, dataHolderName);

//
//         Show details for the copied dataHolder.

            dataItemID = newDataHolder.getDataItemID();

            itemRec = myspace.lookupDataHolderDetails(
              "acd", "roe", "job27", dataItemID);

            if (itemRec != null)
            {  System.out.println("Details of DataHolder with ID " + 
                 dataItemID + ":-" );

               System.out.println("  name:" + itemRec.getDataItemName() );
               System.out.println("  ID: " + itemRec.getDataItemID() );
               System.out.println("  owner: " + itemRec.getOwnerID() );
               System.out.println("  creation date: " + 
                 itemRec.getCreationDate() );
               System.out.println("  expiry date: " +
                 itemRec.getExpiryDate() );

               int type = itemRec.getType();
               if (type == DataItemRecord.CON)
               {  System.out.println("  type: Container.");
               }
               else if (type == DataItemRecord.VOT)
               {  System.out.println("  type: VOTable.");
               }
               else
               {  System.out.println("  type: unknown.");
               }
            }
            else
            {  System.out.println("Item not found.");
            }

            String exportURI = myspace.exportDataHolder(
              "acd", "roe", "job27", dataItemID);

            if (exportURI != null)
            {  System.out.println("  Export URI of DataHolder with ID [" 
                 + dataItemID + "]: " + exportURI);
            }
            else
            {  System.out.println("Item not found.");
            }

//
//         Delete the remaining containers.

            for (int holderloop = 1; holderloop < numDataHolders;
              holderloop++)
            {
               dataItemID = dataItemIDs[holderloop];

               deletedOk = myspace.deleteDataHolder(
                 "acd", "roe", "job27", dataItemID);
            }
         }

         System.out.println("");
         if (!status.getSuccessStatus())
         {  System.out.println("Operation failedAn error occurred.");
         }
         status.outputCodes();
      }
      else
      {  System.out.println("Usage:-");
         System.out.println("  java ThrashRegistry registryName");
      }
   }
}
