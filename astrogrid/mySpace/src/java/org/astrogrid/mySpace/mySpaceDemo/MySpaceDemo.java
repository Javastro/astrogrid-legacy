package org.astrogrid.mySpace.mySpaceDemo;

import javax.swing.*;          //This is the final package name.
//import com.sun.java.swing.*; //Used by JDK 1.2 Beta 4 and all
                               //Swing releases before Swing 1.1 Beta 3.
import java.awt.*;
import java.awt.event.*;

import java.io.*;
import java.util.*;

import org.astrogrid.mySpace.mySpaceStatus.*;
import org.astrogrid.mySpace.mySpaceManager.*;

/**
 * Simple demonstration program for the MySpace registry.
 *
 * @author A C Davenhall (Edinburgh)
 * @version Iteration 2.
 */


public class MySpaceDemo
{  private Logger logger = new Logger(false, true, true, "./myspace.log");
   private Configuration config = new Configuration(true, false,
     Configuration.INTERNALSERVERS);

   private static MySpaceActions myspace = new MySpaceActions();
   private static MySpaceStatus status = new MySpaceStatus();

//
//      Set up for logging.

    private static Logger demoLog = new
      Logger(false, true, true, "./myspace.log");


   public Component createDemoComponents()
   {

//
//   Create the Menubar.

      JMenuBar menuBar = new JMenuBar();
      menuBar.setAlignmentX(Component.RIGHT_ALIGNMENT);
      menuBar.setBorder(BorderFactory.createEmptyBorder(
        30, //top
        30, //left
        10, //bottom
        30) //right
      );

//
//   Create the Options menu.

      JMenu fileMenu = new JMenu("Options");
      menuBar.add(fileMenu);

//
//   Create the Options menu items.

      JMenuItem menuItem = new JMenuItem("Enter query");
      menuItem.addActionListener(new ActionListener()
         {  public void actionPerformed(ActionEvent e)
            {  BufferedReader console = new BufferedReader(
                 new InputStreamReader(System.in));

               System.out.println("Enter query (eg. '*'):");
               String query = null;
               try
               {  query = console.readLine();
               }
               catch (IOException ioerror)
               {  System.out.println("Ooops");
                  query ="*";
               }

               Vector itemRecVector = new Vector();
               itemRecVector = myspace.lookupDataHoldersDetails(
                 "acd", "roe", "job27", query);

               System.out.println("Registry entries satisfying query: "
                 + query + "\n");

               int numFound;
               if (itemRecVector != null)
               {  numFound = itemRecVector.size();
               }
               else
               {   numFound = 0;
                   System.out.println("  No entries matched the query");
               }

               DataItemRecord itemRec = new DataItemRecord();

               for (int loop=0; loop<numFound; loop++)
               {  itemRec = (DataItemRecord)itemRecVector.elementAt(loop);
                  System.out.println("[" + itemRec.getDataItemID()
                    + "]: " + itemRec.toString() );
               }

               System.out.println("");
               if (!status.getSuccessStatus())
               {  System.out.println("Operation failed.");
               }
               status.outputCodes();
               status.reset();
               System.out.println("");
            }
         }
      );
      fileMenu.add(menuItem);

      menuItem = new JMenuItem("List expired dataHolders");
      menuItem.addActionListener(new ActionListener()
         {  public void actionPerformed(ActionEvent e)
            {  BufferedReader console = new BufferedReader(
                 new InputStreamReader(System.in));

               System.out.println("Enter query (eg. '*'):");
               String query = null;
               try
               {  query = console.readLine();
               }
               catch (IOException ioerror)
               {  System.out.println("Ooops");
                  query ="*";
               }

               Vector expiredRecVector = new Vector();
               expiredRecVector = myspace.listExpiredDataHolders(
                 "acd", "roe", "job27", query);

               System.out.println("Registry entries satisfying query: "
                 + query + "\n");

               int numFound;
               if (expiredRecVector != null)
               {  numFound = expiredRecVector.size();
               }
               else
               {   numFound = 0;
                   System.out.println("  No entries have expired.");
               }

               DataItemRecord itemRec = new DataItemRecord();

               for (int loop=0; loop<numFound; loop++)
               {  itemRec = (DataItemRecord)expiredRecVector.elementAt(loop);
                  System.out.println("[" + itemRec.getDataItemID()
                    + "]: " + itemRec.getDataItemName()
                    + " expired on: " + itemRec.getExpiryDate() + ".");
               }

               System.out.println("");
               if (!status.getSuccessStatus())
               {  System.out.println("Operation failed.");
               }
               status.outputCodes();
               status.reset();
               System.out.println("");
            }
         }
      );
      fileMenu.add(menuItem);

      menuItem = new JMenuItem("Show details of DataHolder");
      menuItem.addActionListener(new ActionListener()
         {  public void actionPerformed(ActionEvent e)
            {  BufferedReader console = new BufferedReader(
                 new InputStreamReader(System.in));

               System.out.println("Enter DataHolder ID:");
               int itemID = 0;
               try
               {  String buffer = console.readLine();
                  Integer itemBuffer = new Integer(buffer);
                  itemID = itemBuffer.intValue();

                  System.out.println("value decoded: " + itemID);
               }
               catch (IOException ioerror)
               {  System.out.println("Ooops");
                  itemID = 0;
               }

               DataItemRecord itemRec = new DataItemRecord();

               itemRec = myspace.lookupDataHolderDetails(
                 "acd", "roe", "job27", itemID);

               if (itemRec != null)
               {  System.out.println("Details of DataHolder with ID " + 
                    itemID + ":-" );

                  System.out.println("  name:" + itemRec.getDataItemName() );
                  System.out.println("  ID: " + itemRec.getDataItemID() );
                  System.out.println("  owner: " + itemRec.getOwnerID() );
                  System.out.println("  creation date: " + itemRec.getCreationDate() );
                  System.out.println("  expiry date: " + itemRec.getExpiryDate() );

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

               System.out.println("");
               if (!status.getSuccessStatus())
               {  System.out.println("Operation failed.");
               }
               status.outputCodes();
               status.reset();
               System.out.println("");
            }
         }
      );
      fileMenu.add(menuItem);

      menuItem = new JMenuItem("Export DataHolder");
      menuItem.addActionListener(new ActionListener()
         {  public void actionPerformed(ActionEvent e)
            {  BufferedReader console = new BufferedReader(
                 new InputStreamReader(System.in));

               System.out.println("Enter DataHolder ID:");
               int itemID = 0;
               try
               {  String buffer = console.readLine();
                  Integer itemBuffer = new Integer(buffer);
                  itemID = itemBuffer.intValue();

                  System.out.println("value decoded: " + itemID);
               }
               catch (IOException ioerror)
               {  System.out.println("Ooops");
                  itemID = 0;
               }

               String exportURI = myspace.exportDataHolder(
                 "acd", "roe", "job27", itemID);

               if (exportURI != null)
               {  System.out.println("Export URI of DataHolder with ID [" 
                    + itemID + "]: " + exportURI);
               }
               else
               {  System.out.println("Item not found.");
               }

               System.out.println("");
               if (!status.getSuccessStatus())
               {  System.out.println("Operation failed.");
               }
               status.outputCodes();
               status.reset();
               System.out.println("");
            }
         }
      );
      fileMenu.add(menuItem);

      menuItem = new JMenuItem("Copy DataHolder");
      menuItem.addActionListener(new ActionListener()
         {  public void actionPerformed(ActionEvent e)
            {  BufferedReader console = new BufferedReader(
                 new InputStreamReader(System.in));

               System.out.println("Enter ID of DataHolder to be copied:");
               int itemID = 0;
               try
               {  String buffer = console.readLine();
                  Integer itemBuffer = new Integer(buffer);
                  itemID = itemBuffer.intValue();
               }
               catch (IOException ioerror)
               {  System.out.println("Ooops");
                  itemID = 0;
               }

               String newDataHolderName;
               System.out.println("Enter name of new DataHolder:");
               try
               {  newDataHolderName = console.readLine();
               }
               catch (IOException ioerror)
               {  System.out.println("Ooops");
                  newDataHolderName = "";
               }

               DataItemRecord itemRec = new DataItemRecord();

               itemRec = myspace.copyDataHolder("acd", "roe", "job27",
                 itemID, newDataHolderName);

               Vector itemRecVector = new Vector();
               itemRecVector = myspace.lookupDataHoldersDetails(
                 "acd", "roe", "job27", "*");

               System.out.println("New state of the registry: ");

               int numFound;
               if (itemRecVector != null)
               {  numFound = itemRecVector.size();
               }
               else
               {   numFound = 0;
                   System.out.println("  No entries matched the query");
               }

               itemRec = new DataItemRecord();

               for (int loop=0; loop<numFound; loop++)
               {  itemRec = (DataItemRecord)itemRecVector.elementAt(loop);
                  System.out.println("[" + itemRec.getDataItemID()
                    + "]: " + itemRec.toString() );
               }

               System.out.println("");
               if (!status.getSuccessStatus())
               {  System.out.println("Operation failed.");
               }
               status.outputCodes();
               status.reset();
               System.out.println("");
            }
         }
      );
      fileMenu.add(menuItem);

      menuItem = new JMenuItem("Move DataHolder");
      menuItem.addActionListener(new ActionListener()
         {  public void actionPerformed(ActionEvent e)
            {  BufferedReader console = new BufferedReader(
                 new InputStreamReader(System.in));

               System.out.println("Enter ID of DataHolder to be moved:");
               int itemID = 0;
               try
               {  String buffer = console.readLine();
                  Integer itemBuffer = new Integer(buffer);
                  itemID = itemBuffer.intValue();
               }
               catch (IOException ioerror)
               {  System.out.println("Ooops");
                  itemID = 0;
               }

               String newDataHolderName;
               System.out.println("Enter new name of DataHolder:");
               try
               {  newDataHolderName = console.readLine();
               }
               catch (IOException ioerror)
               {  System.out.println("Ooops");
                  newDataHolderName = "";
               }

               DataItemRecord itemRec = new DataItemRecord();

               itemRec = myspace.moveDataHolder("acd", "roe", "job27",
                 itemID, newDataHolderName);

               Vector itemRecVector = new Vector();
               itemRecVector = myspace.lookupDataHoldersDetails(
                 "acd", "roe", "job27", "*");

               System.out.println("New state of the registry: ");

               int numFound;
               if (itemRecVector != null)
               {  numFound = itemRecVector.size();
               }
               else
               {   numFound = 0;
                   System.out.println("  No entries matched the query");
               }

               itemRec = new DataItemRecord();

               for (int loop=0; loop<numFound; loop++)
               {  itemRec = (DataItemRecord)itemRecVector.elementAt(loop);
                  System.out.println("[" + itemRec.getDataItemID()
                    + "]: " + itemRec.toString() );
               }

               System.out.println("");
               if (!status.getSuccessStatus())
               {  System.out.println("Operation failed.");
               }
               status.outputCodes();
               status.reset();
               System.out.println("");
            }
         }
      );
      fileMenu.add(menuItem);

      menuItem = new JMenuItem("Create new container");
      menuItem.addActionListener(new ActionListener()
         {  public void actionPerformed(ActionEvent e)
            {  BufferedReader console = new BufferedReader(
                 new InputStreamReader(System.in));

               String newContainerName;
               System.out.println("Enter name of new Container:");
               try
               {  newContainerName = console.readLine();
               }
               catch (IOException ioerror)
               {  System.out.println("Ooops");
                  newContainerName = "";
               }

               DataItemRecord newCont = myspace.createContainer(
                 "acd", "roe", "job27", newContainerName);


               Vector itemRecVector = new Vector();
               itemRecVector = myspace.lookupDataHoldersDetails(
                 "acd", "roe", "job27", "*");

               System.out.println("New state of the registry: ");

               int numFound;
               if (itemRecVector != null)
               {  numFound = itemRecVector.size();
               }
               else
               {   numFound = 0;
                   System.out.println("  No entries matched the query");
               }

               DataItemRecord itemRec = new DataItemRecord();

               for (int loop=0; loop<numFound; loop++)
               {  itemRec = (DataItemRecord)itemRecVector.elementAt(loop);
                  System.out.println("[" + itemRec.getDataItemID()
                    + "]: " + itemRec.toString() );
               }

               System.out.println("");
               if (!status.getSuccessStatus())
               {  System.out.println("Operation failed.");
               }
               status.outputCodes();
               status.reset();
               System.out.println("");
            }
         }
      );
      fileMenu.add(menuItem);

      menuItem = new JMenuItem("Import DataHolder");
      menuItem.addActionListener(new ActionListener()
         {  public void actionPerformed(ActionEvent e)
            {  BufferedReader console = new BufferedReader(
                 new InputStreamReader(System.in));

               String newDataHolderName;
               System.out.println("Enter name of new DataHolder:");
               try
               {  newDataHolderName = console.readLine();
               }
               catch (IOException ioerror)
               {  System.out.println("Ooops");
                  newDataHolderName = "";
               }

               String remoteURI;
               System.out.println("Enter corresponding remote URI:");
               try
               {  remoteURI = console.readLine();
               }
               catch (IOException ioerror)
               {  System.out.println("Ooops");
                  remoteURI = "";
               }

               String overwrite;
               System.out.println(
                 "Overwrite any existing DataHolder of the same name (y/n)?");
               try
               {  overwrite = console.readLine();
               }
               catch (IOException ioerror)
               {  System.out.println("Ooops");
                  overwrite = "";
               }

               int dispatchExisting = myspace.LEAVE;

               overwrite = overwrite.toLowerCase();
               int overwritePos = overwrite.indexOf('y');
               if (overwritePos > -1)
               {  dispatchExisting = myspace.OVERWRITE;
               }

               System.out.println("dispatchExisting: " + dispatchExisting);

               DataItemRecord newDataHolder = myspace.importDataHolder(
                 "acd", "roe", "job27", remoteURI, newDataHolderName,
                 "quERY", dispatchExisting);

               Vector itemRecVector = new Vector();
               itemRecVector = myspace.lookupDataHoldersDetails(
                 "acd", "roe", "job27", "*");

               System.out.println("New state of the registry: ");

               int numFound;
               if (itemRecVector != null)
               {  numFound = itemRecVector.size();
               }
               else
               {   numFound = 0;
                   System.out.println("  No entries matched the query");
               }

               DataItemRecord itemRec = new DataItemRecord();

               for (int loop=0; loop<numFound; loop++)
               {  itemRec = (DataItemRecord)itemRecVector.elementAt(loop);
                  System.out.println("[" + itemRec.getDataItemID()
                    + "]: " + itemRec.toString() );
               }

               System.out.println("");
               if (!status.getSuccessStatus())
               {  System.out.println("Operation failed.");
               }
               status.outputCodes();
               status.reset();
               System.out.println("");
            }
         }
      );
      fileMenu.add(menuItem);

      menuItem = new JMenuItem("UpLoad DataHolder");
      menuItem.addActionListener(new ActionListener()
         {  public void actionPerformed(ActionEvent e)
            {  BufferedReader console = new BufferedReader(
                 new InputStreamReader(System.in));

               String newDataHolderName;
               System.out.println("Enter name of new DataHolder:");
               try
               {  newDataHolderName = console.readLine();
               }
               catch (IOException ioerror)
               {  System.out.println("Ooops");
                  newDataHolderName = "";
               }

               String contents;
               System.out.println("Enter the contents of the DataHolder:");
               try
               {  contents = console.readLine();
               }
               catch (IOException ioerror)
               {  System.out.println("Ooops");
                  contents = "";
               }

               String overwrite;
               System.out.println(
                 "Overwrite any existing DataHolder of the same name (y/n)?");
               try
               {  overwrite = console.readLine();
               }
               catch (IOException ioerror)
               {  System.out.println("Ooops");
                  overwrite = "";
               }

               int dispatchExisting = myspace.LEAVE;

               overwrite = overwrite.toLowerCase();
               int overwritePos = overwrite.indexOf('y');
               if (overwritePos > -1)
               {  dispatchExisting = myspace.OVERWRITE;
               }

               System.out.println("dispatchExisting: " + dispatchExisting);

               DataItemRecord newDataHolder = myspace.upLoadDataHolder(
                 "acd", "roe", "job27", newDataHolderName, contents,
                 "wF", dispatchExisting);

               Vector itemRecVector = new Vector();
               itemRecVector = myspace.lookupDataHoldersDetails(
                 "acd", "roe", "job27", "*");

               System.out.println("New state of the registry: ");

               int numFound;
               if (itemRecVector != null)
               {  numFound = itemRecVector.size();
               }
               else
               {   numFound = 0;
                   System.out.println("  No entries matched the query");
               }

               DataItemRecord itemRec = new DataItemRecord();

               for (int loop=0; loop<numFound; loop++)
               {  itemRec = (DataItemRecord)itemRecVector.elementAt(loop);
                  System.out.println("[" + itemRec.getDataItemID()
                    + "]: " + itemRec.toString() );
               }

               System.out.println("");
               if (!status.getSuccessStatus())
               {  System.out.println("Operation failed.");
               }
               status.outputCodes();
               status.reset();
               System.out.println("");
            }
         }
      );
      fileMenu.add(menuItem);






      menuItem = new JMenuItem("Delete DataHolder");
      menuItem.addActionListener(new ActionListener()
         {  public void actionPerformed(ActionEvent e)
            {  BufferedReader console = new BufferedReader(
                 new InputStreamReader(System.in));

               System.out.println("Enter ID of DataHolder to be deleted:");
               int itemID = 0;
               try
               {  String buffer = console.readLine();
                  Integer itemBuffer = new Integer(buffer);
                  itemID = itemBuffer.intValue();
               }
               catch (IOException ioerror)
               {  System.out.println("Ooops");
                  itemID = 0;
               }

               boolean deletedOk = myspace.deleteDataHolder(
                 "acd", "roe", "job27", itemID);

               if (!deletedOk)
               {  System.out.println("Delete failed.");
               }

               Vector itemRecVector = new Vector();
               itemRecVector = myspace.lookupDataHoldersDetails(
                 "acd", "roe", "job27", "*");

               System.out.println("New state of the registry: ");

               int numFound;
               if (itemRecVector != null)
               {  numFound = itemRecVector.size();
               }
               else
               {   numFound = 0;
                   System.out.println("  No entries matched the query");
               }

               DataItemRecord itemRec = new DataItemRecord();

               for (int loop=0; loop<numFound; loop++)
               {  itemRec = (DataItemRecord)itemRecVector.elementAt(loop);
                  System.out.println("[" + itemRec.getDataItemID()
                    + "]: " + itemRec.toString() );
               }

               System.out.println("");
               if (!status.getSuccessStatus())
               {  System.out.println("Operation failed.");
               }
               status.outputCodes();
               status.reset();
               System.out.println("");
            }
         }
      );
      fileMenu.add(menuItem);

      menuItem = new JMenuItem("Change owner of DataHolder");
      menuItem.addActionListener(new ActionListener()
         {  public void actionPerformed(ActionEvent e)
            {  BufferedReader console = new BufferedReader(
                 new InputStreamReader(System.in));

               System.out.println("Enter ID of DataHolder to be changed:");
               int itemID = 0;
               try
               {  String buffer = console.readLine();
                  Integer itemBuffer = new Integer(buffer);
                  itemID = itemBuffer.intValue();
               }
               catch (IOException ioerror)
               {  System.out.println("Ooops");
                  itemID = 0;
               }

               String newOwner;
               System.out.println("Enter ID of new owner:");
               try
               {  newOwner = console.readLine();
               }
               catch (IOException ioerror)
               {  System.out.println("Ooops");
                  newOwner = "";
               }

               DataItemRecord itemRec = new DataItemRecord();

               itemRec = myspace.changeOwnerDataHolder("acd", "roe", "job27",
                 itemID, newOwner);

               System.out.println(itemRec.getDataItemName() +
                 " is now owned by " + itemRec.getOwnerID() + ".");

               System.out.println("");
               if (!status.getSuccessStatus())
               {  System.out.println("Operation failed.");
               }
               status.outputCodes();
               status.reset();
               System.out.println("");
            }
         }
      );
      fileMenu.add(menuItem);

      menuItem = new JMenuItem("Advance expiry date of DataHolder");
      menuItem.addActionListener(new ActionListener()
         {  public void actionPerformed(ActionEvent e)
            {  BufferedReader console = new BufferedReader(
                 new InputStreamReader(System.in));

               System.out.println("Enter ID of DataHolder to be changed:");
               int itemID = 0;
               try
               {  String buffer = console.readLine();
                  Integer itemBuffer = new Integer(buffer);
                  itemID = itemBuffer.intValue();
               }
               catch (IOException ioerror)
               {  System.out.println("Ooops");
                  itemID = 0;
               }

               int advance;
               System.out.println("Enter no. of days to advance expiry date:");
               try
               {  String buffer = console.readLine();
                  Integer itemBuffer = new Integer(buffer);
                  advance = itemBuffer.intValue();
               }
               catch (IOException ioerror)
               {  System.out.println("Ooops");
                  advance = 0;
               }

               DataItemRecord itemRec = new DataItemRecord();

               itemRec = myspace.advanceExpiryDataHolder("acd", "roe",
                 "job27", itemID, advance);

               System.out.println(itemRec.getDataItemName() +
                 " now expires on " + itemRec.getExpiryDate() + ".");

               System.out.println("");
               if (!status.getSuccessStatus())
               {  System.out.println("Operation failed.");
               }
               status.outputCodes();
               status.reset();
               System.out.println("");
            }
         }
      );
      fileMenu.add(menuItem);

      menuItem = new JMenuItem("Create new user");
      menuItem.addActionListener(new ActionListener()
         {  public void actionPerformed(ActionEvent e)
            {  BufferedReader console = new BufferedReader(
                 new InputStreamReader(System.in));

               System.out.println("Enter user ID of the new user:");
               String userID;
               try
               {  userID = console.readLine();
               }
               catch (IOException ioerror)
               {  System.out.println("Ooops");
                  userID = "";
               }

               System.out.println("Enter community ID of the new user:");
               String communityID;
               try
               {  communityID = console.readLine();
               }
               catch (IOException ioerror)
               {  System.out.println("Ooops");
                  communityID = "";
               }

               String secondServer;
               System.out.println(
                 "MySpaceDemo will automatically add serv1 as the " + 
                 "first server");
               System.out.println(
                "If required, enter the name of a second server, " +
                "otherwise hit return:");
               try
               {  secondServer = console.readLine();
               }
               catch (IOException ioerror)
               {  System.out.println("Ooops");
                  secondServer = "";
               }

               Vector servers = new Vector();
               servers.add("serv1");

               secondServer = secondServer.trim();
               if (!secondServer.equals("") && secondServer != null)
               {  servers.add(secondServer);
               }
               else
               {  System.out.println("no second server.");
               }

               boolean addOk = myspace.createUser(userID, communityID,
                 "job27", servers);

               if (addOk)
               {  System.out.println("User added ok.");
               }
               else
               {  System.out.println("Failed to add user.");
               }

               System.out.println("");
               if (!status.getSuccessStatus())
               {  System.out.println("Operation failed.");
               }
               status.outputCodes();
               status.reset();
               System.out.println("");
            }
         }
      );
      fileMenu.add(menuItem);

      menuItem = new JMenuItem("Delete user");
      menuItem.addActionListener(new ActionListener()
         {  public void actionPerformed(ActionEvent e)
            {  BufferedReader console = new BufferedReader(
                 new InputStreamReader(System.in));

               System.out.println("Enter ID of the user to delete:");
               String userID;
               try
               {  userID = console.readLine();
               }
               catch (IOException ioerror)
               {  System.out.println("Ooops");
                  userID = "";
               }

               System.out.println("Enter community ID of the user:");
               String communityID;
               try
               {  communityID = console.readLine();
               }
               catch (IOException ioerror)
               {  System.out.println("Ooops");
                  communityID = "";
               }

               boolean deleteOk = myspace.deleteUser(userID, communityID,
                 "job27");

               if (deleteOk)
               {  System.out.println("User deleted ok.");
               }
               else
               {  System.out.println("Failed to delete user.");
               }

               System.out.println("");
               if (!status.getSuccessStatus())
               {  System.out.println("Operation failed.");
               }
               status.outputCodes();
               status.reset();
               System.out.println("");
            }
         }
      );
      fileMenu.add(menuItem);


      menuItem = new JMenuItem("Quit");
      menuItem.addActionListener(new ActionListener()
         {  public void actionPerformed(ActionEvent e)
            {  demoLog.close();
               System.out.println("MySpace Demo. exiting normally.");
               System.exit(0);
            }
         }
      );
      fileMenu.add(menuItem);

//
//   Create the browser panel and add the individual components to it.

      JPanel browserPanel = new JPanel();
      browserPanel.setLayout(new BoxLayout(browserPanel, BoxLayout.Y_AXIS));
      browserPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

      browserPanel.add(menuBar);

      return browserPanel;
   }

   public static void main(String argv[])
   {  if (argv.length == 1)
      {  String registryName = argv[0];

//
//      Set the registry file name.

         myspace.setRegistryName(registryName);

//
//      Set the look and feel.

         try
         {  UIManager.setLookAndFeel(
              UIManager.getCrossPlatformLookAndFeelClassName());
         }
         catch (Exception e)
         {  System.out.println("Ooops.");
         }

//
//      Create the top-level container and add contents to it.

         JFrame demo = new JFrame("MySpaceDemo");
         MySpaceDemo app = new MySpaceDemo();
         Component demoContents = app.createDemoComponents();
         demo.getContentPane().add(demoContents, BorderLayout.CENTER);

//
//      Finish setting up the frame, and show it.

         demo.addWindowListener(new WindowAdapter()
            {  public void windowClosing(WindowEvent e)
               {  System.out.println("MySpaceDemo window closed.");
                  System.exit(0);
               }
            }
         );

         demo.pack();
         demo.setVisible(true);
      }
      else
      {  System.out.println("Usage:-");
         System.out.println("  java MySpaceDemo registryName");
      }
   }
}
