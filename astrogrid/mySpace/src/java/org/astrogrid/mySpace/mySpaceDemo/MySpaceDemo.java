package org.astrogrid.mySpace.mySpaceDemo;

import javax.swing.*;
import org.astrogrid.store.delegate.myspaceItn05.*;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import org.astrogrid.mySpace.mySpaceManager.Configuration;
import org.astrogrid.mySpace.mySpaceStatus.Logger;
import org.astrogrid.mySpace.mySpaceStatus.MySpaceStatus;
import org.astrogrid.store.delegate.StoreFile;

/**
 * Simple demonstration program for the MySpace registry.
 *
 * @author A C Davenhall (Edinburgh)
 * @since Iteration 2.
 * @version Iteration 5.
 */


public class MySpaceDemo
{  private Logger logger = new Logger(false, true, true, "./myspace.log");
   private Configuration config = new Configuration(true, false,
     Configuration.INTERNALSERVERS);

   private static ManagerGenuine myspace = new ManagerGenuine();
   private static MySpaceStatus status = new MySpaceStatus();
   private KernelResults results = new KernelResults();

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

               try
               {  results = myspace.getEntriesList(query, true);

//
//               List the entries selected.

                  int numEntries = 0;
                  java.lang.Object[] entries = results.getEntries();
                  if (entries != null)
                  {  numEntries = Array.getLength(entries);
                  }

                  if (numEntries > 0)
                  {  for(int loop=0; loop<numEntries; loop++)
                     {  EntryResults entry = (EntryResults)entries[loop];

                        System.out.println(loop + ": " + entry.getEntryName());
                     }
                  }
                  else
                  {  System.out.println("No files matched the query.");
                  }
                  System.out.println("");

//
//               List the status messages.

                  Object[] statusList = results.getStatusList();
                  int numStatus = Array.getLength(statusList);

                  StatusResults status = new StatusResults();

                  for(int loop=0; loop<numStatus; loop++)
                  {  status = (StatusResults)statusList[loop];
                     StatusMessage message = new StatusMessage(status);
                     System.out.println(loop + ": " + message.toString() );
                  }
               }
               catch (Exception ex)
               {  ex.printStackTrace();
               }
            }
         }
      );
      fileMenu.add(menuItem);


      menuItem = new JMenuItem("Create file from String");
      menuItem.addActionListener(new ActionListener()
         {  public void actionPerformed(ActionEvent e)
            {  BufferedReader console = new BufferedReader(
                 new InputStreamReader(System.in));

               String newFileName;
               System.out.println("Enter name of new file:");
               try
               {  newFileName = console.readLine();
               }
               catch (IOException ioerror)
               {  System.out.println("Ooops");
                  newFileName = "";
               }

               String contents;
               System.out.println("Enter the contents of the file:");
               try
               {  contents = console.readLine();
               }
               catch (IOException ioerror)
               {  System.out.println("Ooops");
                  contents = "";
               }

               try
               {  results = myspace.putString(newFileName, contents,
                    EntryCodes.VOT, ManagerCodes.LEAVE, true);

                  Object[] statusList = results.getStatusList();
                  int numStatus = Array.getLength(statusList);

                  StatusResults status = new StatusResults();

                  for(int loop=0; loop<numStatus; loop++)
                  {  status = (StatusResults)statusList[loop];
                     StatusMessage message = new StatusMessage(status);
                     System.out.println(loop + ": " + message.toString() );
                  }
               }
               catch (Exception ex)
               {  ex.printStackTrace();
               }
            }
         }
      );
      fileMenu.add(menuItem);


      menuItem = new JMenuItem("Retrieve file contents");
      menuItem.addActionListener(new ActionListener()
         {  public void actionPerformed(ActionEvent e)
            {  BufferedReader console = new BufferedReader(
                 new InputStreamReader(System.in));

               System.out.println("File name:");
               String fileName = "";
               try
               {  fileName = console.readLine();
               }
               catch (IOException ioerror)
               {  System.out.println("Ooops");
                  fileName = "";
               }

               try
               {  results = myspace.getString(fileName, true);

                  String contents = results.getContentsString();

                  System.out.println("File contents:-");
                  int contentsLength = contents.length();
                  if (contentsLength < 70)
                  {  System.out.println(contents);
                  }
                  else
                  {  System.out.println(contents.substring(0, 69)
                       + "...");
                  }

                  Object[] statusList = results.getStatusList();
                  int numStatus = Array.getLength(statusList);

                  StatusResults status = new StatusResults();

                  for(int loop=0; loop<numStatus; loop++)
                  {  status = (StatusResults)statusList[loop];
                     StatusMessage message = new StatusMessage(status);
                     System.out.println(loop + ": " + message.toString() );
                  }
               }
               catch (Exception ex)
               {  ex.printStackTrace();
               }
            }
         }
      );
      fileMenu.add(menuItem);


      menuItem = new JMenuItem("Create a new container");
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

               try
               {  results = myspace.createContainer(newContainerName,
                    true);

                  String contents = results.getContentsString();

                  Object[] statusList = results.getStatusList();
                  int numStatus = Array.getLength(statusList);

                  StatusResults status = new StatusResults();

                  for(int loop=0; loop<numStatus; loop++)
                  {  status = (StatusResults)statusList[loop];
                     StatusMessage message = new StatusMessage(status);
                     System.out.println(loop + ": " + message.toString() );
                  }
               }
               catch (Exception ex)
               {  ex.printStackTrace();
               }
            }
         }
      );
      fileMenu.add(menuItem);

      menuItem = new JMenuItem("Copy a file");
      menuItem.addActionListener(new ActionListener()
         {  public void actionPerformed(ActionEvent e)
            {  BufferedReader console = new BufferedReader(
                 new InputStreamReader(System.in));

               System.out.println("Enter the name of the file to be copied:");
               String oldFileName = "";
               try
               {  oldFileName = console.readLine();
               }
               catch (IOException ioerror)
               {  System.out.println("Ooops");
                  oldFileName = "";
               }

               String newFileName;
               System.out.println("Enter name of the new file:");
               try
               {  newFileName = console.readLine();
               }
               catch (IOException ioerror)
               {  System.out.println("Ooops");
                  newFileName = "";
               }

               try
               {  results = myspace.copyFile(oldFileName,
                    newFileName, true);

                  String contents = results.getContentsString();

                  Object[] statusList = results.getStatusList();
                  int numStatus = Array.getLength(statusList);

                  StatusResults status = new StatusResults();

                  for(int loop=0; loop<numStatus; loop++)
                  {  status = (StatusResults)statusList[loop];
                     StatusMessage message = new StatusMessage(status);
                     System.out.println(loop + ": " + message.toString() );
                  }
               }
               catch (Exception ex)
               {  ex.printStackTrace();
               }
            }
         }
      );
      fileMenu.add(menuItem);


      menuItem = new JMenuItem("Move a file");
      menuItem.addActionListener(new ActionListener()
         {  public void actionPerformed(ActionEvent e)
            {  BufferedReader console = new BufferedReader(
                 new InputStreamReader(System.in));

               System.out.println("Enter the name of the file to be moved:");
               String oldFileName = "";
               try
               {  oldFileName = console.readLine();
               }
               catch (IOException ioerror)
               {  System.out.println("Ooops");
                  oldFileName = "";
               }

               String newFileName;
               System.out.println("Enter name of the new file:");
               try
               {  newFileName = console.readLine();
               }
               catch (IOException ioerror)
               {  System.out.println("Ooops");
                  newFileName = "";
               }

               try
               {  results = myspace.moveFile(oldFileName,
                    newFileName, true);

                  String contents = results.getContentsString();

                  Object[] statusList = results.getStatusList();
                  int numStatus = Array.getLength(statusList);

                  StatusResults status = new StatusResults();

                  for(int loop=0; loop<numStatus; loop++)
                  {  status = (StatusResults)statusList[loop];
                     StatusMessage message = new StatusMessage(status);
                     System.out.println(loop + ": " + message.toString() );
                  }
               }
               catch (Exception ex)
               {  ex.printStackTrace();
               }
            }
         }
      );
      fileMenu.add(menuItem);


      menuItem = new JMenuItem("Delete a file");
      menuItem.addActionListener(new ActionListener()
         {  public void actionPerformed(ActionEvent e)
            {  BufferedReader console = new BufferedReader(
                 new InputStreamReader(System.in));

               System.out.println("Name of the file to delete:");
               String fileName = "";
               try
               {  fileName = console.readLine();
               }
               catch (IOException ioerror)
               {  System.out.println("Ooops");
                  fileName = "";
               }

               try
               {  results = myspace.deleteFile(fileName, true);

                  String contents = results.getContentsString();

                  Object[] statusList = results.getStatusList();
                  int numStatus = Array.getLength(statusList);

                  StatusResults status = new StatusResults();

                  for(int loop=0; loop<numStatus; loop++)
                  {  status = (StatusResults)statusList[loop];
                     StatusMessage message = new StatusMessage(status);
                     System.out.println(loop + ": " + message.toString() );
                  }
               }
               catch (Exception ex)
               {  ex.printStackTrace();
               }
            }
         }
      );
      fileMenu.add(menuItem);


      menuItem = new JMenuItem("Create new account");
      menuItem.addActionListener(new ActionListener()
         {  public void actionPerformed(ActionEvent e)
            {  BufferedReader console = new BufferedReader(
                 new InputStreamReader(System.in));

               System.out.println("Enter ID of the new account:");
               String newAccount;
               try
               {  newAccount = console.readLine();
               }
               catch (IOException ioerror)
               {  System.out.println("Ooops");
                  newAccount = "";
               }

               try
               {  results = myspace.createAccount(newAccount, true);

                  Object[] statusList = results.getStatusList();
                  int numStatus = Array.getLength(statusList);

                  StatusResults status = new StatusResults();

                  for(int loop=0; loop<numStatus; loop++)
                  {  status = (StatusResults)statusList[loop];
                     StatusMessage message = new StatusMessage(status);
                     System.out.println(loop + ": " + message.toString() );
                  }
               }
               catch (Exception ex)
               {  ex.printStackTrace();
               }
            }
         }
      );
      fileMenu.add(menuItem);


      menuItem = new JMenuItem("Delete an account");
      menuItem.addActionListener(new ActionListener()
         {  public void actionPerformed(ActionEvent e)
            {  BufferedReader console = new BufferedReader(
                 new InputStreamReader(System.in));

               System.out.println("Enter ID of the account to delete:");
               String accountID;
               try
               {  accountID = console.readLine();
               }
               catch (IOException ioerror)
               {  System.out.println("Ooops");
                  accountID = "";
               }

               try
               {  results = myspace.deleteAccount(accountID, true);

                  Object[] statusList = results.getStatusList();
                  int numStatus = Array.getLength(statusList);

                  StatusResults status = new StatusResults();

                  for(int loop=0; loop<numStatus; loop++)
                  {  status = (StatusResults)statusList[loop];
                     StatusMessage message = new StatusMessage(status);
                     System.out.println(loop + ": " + message.toString() );
                  }
               }
               catch (Exception ex)
               {  ex.printStackTrace();
               }
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
   {  System.setProperty("java.util.prefs.syncInterval","2000000");

      if (argv.length == 0)
      {
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
         System.out.println("  java MySpaceDemo");
      }
   }
}
