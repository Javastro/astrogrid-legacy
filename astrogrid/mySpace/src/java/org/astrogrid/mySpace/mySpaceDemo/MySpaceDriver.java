package org.astrogrid.mySpace.mySpaceDemo;

import javax.swing.*;          //This is the final package name.
//import com.sun.java.swing.*; //Used by JDK 1.2 Beta 4 and all
                               //Swing releases before Swing 1.1 Beta 3.
import java.awt.*;
import java.awt.event.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.ArrayList;
import java.lang.reflect.Array;

import org.astrogrid.mySpace.mySpaceStatus.*;
import org.astrogrid.mySpace.mySpaceManager.*;

import org.astrogrid.store.Agsl;
import org.astrogrid.store.delegate.StoreFile;
import org.astrogrid.store.delegate.StoreClient;
import org.astrogrid.community.User;

import org.astrogrid.store.delegate.myspaceItn05.MySpaceIt05Delegate;
import org.astrogrid.store.delegate.myspaceItn05.EntryRecord;
import org.astrogrid.store.delegate.myspaceItn05.EntryNode;

//
// Example end point:
//
//   http://grendel12.roe.ac.uk:8080/astrogrid-mySpace/services/Manager


/**
 * Simple demonstration program for the MySpace registry.
 *
 * @author A C Davenhall (Edinburgh)
 * @since Iteration 2.
 * @version Iteration 5.
 */


public class MySpaceDriver
{  static boolean isTest;

   static User operator = new User();

   static MySpaceIt05Delegate middle = null;

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

      JMenuItem menuItem = new JMenuItem("Enter query (output as list)");
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
               {  middle.setTest(isTest);
                  middle.setThrow(false);
                  StoreFile[] files = middle.listFiles(query);

                  System.out.println("\nMatching files:-");

                  int numFiles = Array.getLength(files);

                  if (numFiles > 0)
                  {  EntryRecord file = new EntryRecord();

                     for(int loop=0; loop<numFiles; loop++)
                     {  file = (EntryRecord)files[loop];
                        System.out.println(loop + ": " + file.toString() );
                     }
                  }
                  else
                  {  System.out.println("No files satisfied query.");
                  }

                  System.out.println("\nMessages:-");
                  middle.outputStatusList();
                  middle.resetStatusList();
               }
               catch (Exception ex)
               {  ex.printStackTrace();
               }
            }
         }
      );
      fileMenu.add(menuItem);


      menuItem = new JMenuItem("Enter query (output as tree)");
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
               {  middle.setTest(isTest);
                  middle.setThrow(false);
                  EntryNode fileRoot = (EntryNode)middle.getFiles(query);

                  System.out.println("\nMatching files:-");
                  System.out.println(fileRoot.toString() );

                  System.out.println("\nMessages:-");
                  middle.outputStatusList();
                  middle.resetStatusList();
               }
               catch (Exception ex)
               {  ex.printStackTrace();
               }
            }
         }
      );
      fileMenu.add(menuItem);


      menuItem = new JMenuItem("Show details of file");
      menuItem.addActionListener(new ActionListener()
         {  public void actionPerformed(ActionEvent e)
            {  BufferedReader console = new BufferedReader(
                 new InputStreamReader(System.in));

               String fileName;
               System.out.println("Enter name of file:");
               try
               {  fileName = console.readLine();
               }
               catch (IOException ioerror)
               {  System.out.println("Ooops");
                  fileName = "";
               }

               try
               {  middle.setTest(isTest);
                  middle.setThrow(false);

                  EntryRecord file = 
                    (EntryRecord)middle.getFile(fileName);

                  System.out.println("\nFile details:-");
                  System.out.println("  " + file.toString() );
                  System.out.println("  URL: " + file.getEntryUri() );

                  middle.outputStatusList();
                  middle.resetStatusList();
               }
               catch (Exception ex)
               {  ex.printStackTrace();
               }
 

            }
         }
      );
      fileMenu.add(menuItem);


      menuItem = new JMenuItem("Show URL of file");
      menuItem.addActionListener(new ActionListener()
         {  public void actionPerformed(ActionEvent e)
            {  BufferedReader console = new BufferedReader(
                 new InputStreamReader(System.in));

               String fileName;
               System.out.println("Enter name of file:");
               try
               {  fileName = console.readLine();
               }
               catch (IOException ioerror)
               {  System.out.println("Ooops");
                  fileName = "";
               }

               try
               {  middle.setTest(isTest);
                  middle.setThrow(false);

                  URL url = middle.getUrl(fileName);

                  System.out.println("URL: " + url.toString() );

                  middle.outputStatusList();
                  middle.resetStatusList();
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
               {  middle.setTest(isTest);
                  middle.setThrow(false);
                  middle.putString(contents, newFileName, false);

                  System.out.println("\nMessages:-");
                  middle.outputStatusList();
                  middle.resetStatusList();
               }
               catch (Exception ex)
               {  ex.printStackTrace();
               }
 

            }
         }
      );
      fileMenu.add(menuItem);


      menuItem = new JMenuItem("Append a String to a file");
      menuItem.addActionListener(new ActionListener()
         {  public void actionPerformed(ActionEvent e)
            {  BufferedReader console = new BufferedReader(
                 new InputStreamReader(System.in));

               String fileName;
               System.out.println("Enter name of file:");
               try
               {  fileName = console.readLine();
               }
               catch (IOException ioerror)
               {  System.out.println("Ooops");
                  fileName = "";
               }

               String contents;
               System.out.println("Enter the new contents:");
               try
               {  contents = console.readLine();
               }
               catch (IOException ioerror)
               {  System.out.println("Ooops");
                  contents = "";
               }

               try
               {  middle.setTest(isTest);
                  middle.setThrow(false);
                  middle.putString(contents, fileName, true);

                  System.out.println("\nMessages:-");
                  middle.outputStatusList();
                  middle.resetStatusList();
               }
               catch (Exception ex)
               {  ex.printStackTrace();
               }
             }
         }
      );
      fileMenu.add(menuItem);


      menuItem = new JMenuItem("Create file from a URL");
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

               String urlString;
               System.out.println("Enter the URL:");
               try
               {  urlString = console.readLine();
               }
               catch (IOException ioerror)
               {  System.out.println("Ooops");
                  urlString = "http://blue.nowhere/";
               }

               try
               {  URL url = new URL(urlString);

                  middle.setTest(isTest);
                  middle.setThrow(false);
                  middle.putUrl(url, newFileName, false);

                  System.out.println("\nMessages:-");
                  middle.outputStatusList();
                  middle.resetStatusList();
               }
               catch (Exception ex)
               {  ex.printStackTrace();
               }
 

            }
         }
      );
      fileMenu.add(menuItem);

      menuItem = new JMenuItem("Create file from array of bytes");
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

               try
               {  byte[] someBytes = new byte[] {1, 2, 3, 4, 5, 6};

                  middle.setTest(isTest);
                  middle.setThrow(false);
                  middle.putBytes(someBytes, 0, 6, newFileName, false);

                  System.out.println("\nMessages:-");
                  middle.outputStatusList();
                  middle.resetStatusList();
               }
               catch (Exception ex)
               {  ex.printStackTrace();
               }
 

            }
         }
      );
      fileMenu.add(menuItem);


      menuItem = new JMenuItem("Retrieve file contents as String");
      menuItem.addActionListener(new ActionListener()
         {  public void actionPerformed(ActionEvent e)
            {  BufferedReader console = new BufferedReader(
                 new InputStreamReader(System.in));

               String fileName;
               System.out.println("Enter name of file:");
               try
               {  fileName = console.readLine();
               }
               catch (IOException ioerror)
               {  System.out.println("Ooops");
                  fileName = "";
               }

               try
               {  middle.setTest(isTest);
                  middle.setThrow(false);
                  String contents = middle.getString(fileName);

                  System.out.println("\nFile content:-");
                  System.out.println(contents);

                  System.out.println("\nMessages:-");
                  middle.outputStatusList();
                  middle.resetStatusList();
               }
               catch (Exception ex)
               {  ex.printStackTrace();
               }
 

            }
         }
      );
      fileMenu.add(menuItem);


      menuItem = new JMenuItem("Retrieve file contents as byte array");
      menuItem.addActionListener(new ActionListener()
         {  public void actionPerformed(ActionEvent e)
            {  BufferedReader console = new BufferedReader(
                 new InputStreamReader(System.in));

               String fileName;
               System.out.println("Enter name of file:");
               try
               {  fileName = console.readLine();
               }
               catch (IOException ioerror)
               {  System.out.println("Ooops");
                  fileName = "";
               }

               try
               {  middle.setTest(isTest);
                  middle.setThrow(false);
                  byte[] contents = middle.getBytes(fileName);

                  System.out.println("\nFile content:-");

                  int contentsLength = Array.getLength(contents);

                  int numShow = contentsLength;
                  if (numShow > 5)
                  {  numShow = 5;
                  }

                  for (int loop=0; loop<numShow; loop++)
                  {  System.out.print(" " + contents[loop]);
                  }

                  if (contentsLength > numShow)
                  {  System.out.print("...");
                  }

                  System.out.println("\n\nMessages:-");
                  middle.outputStatusList();
                  middle.resetStatusList();
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
               {  middle.setTest(isTest);
                  middle.setThrow(false);
                  middle.newFolder(newContainerName);

                  System.out.println("\nMessages:-");
                  middle.outputStatusList();
                  middle.resetStatusList();
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
               {  middle.setTest(isTest);
                  middle.setThrow(false);

                  Agsl someAgsl = new Agsl("http://www.google.com",
                    newFileName);

                  middle.copy(oldFileName, someAgsl);

                  System.out.println("\nMessages:-");
                  middle.outputStatusList();
                  middle.resetStatusList();
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
               {  middle.setTest(isTest);
                  middle.setThrow(false);

                  Agsl someAgsl = new Agsl("http://www.google.com",
                    newFileName);

                  middle.move(oldFileName, someAgsl);

                  System.out.println("\nMessages:-");
                  middle.outputStatusList();
                  middle.resetStatusList();
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
               {  middle.setTest(isTest);
                  middle.setThrow(false);
                  middle.delete(fileName);

                  System.out.println("\nMessages:-");
                  middle.outputStatusList();
                  middle.resetStatusList();
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

               User account = new User(newAccount, "", "", "");

               try
               {  middle.setTest(isTest);
                  middle.setThrow(false);
                  middle.createUser(account);

                  System.out.println("\nMessages:-");
                  middle.outputStatusList();
                  middle.resetStatusList();
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
               String deadAccount;
               try
               {  deadAccount = console.readLine();
               }
               catch (IOException ioerror)
               {  System.out.println("Ooops");
                  deadAccount = "";
               }

               User account = new User(deadAccount, "", "", "");

               try
               {  middle.setTest(isTest);
                  middle.setThrow(false);
                  middle.deleteUser(account);

                  System.out.println("\nMessages:-");
                  middle.outputStatusList();
                  middle.resetStatusList();
               }
               catch (Exception ex)
               {  ex.printStackTrace();
               }
            }
         }
      );
      fileMenu.add(menuItem);


      menuItem = new JMenuItem("Heartbeat");
      menuItem.addActionListener(new ActionListener()
         {  public void actionPerformed(ActionEvent e)
            {  try
               {  middle.setTest(isTest);
                  middle.setThrow(false);

                  if (middle.heartBeat() )
                  {  System.out.println("The Manager is responding ok.");
                  }
                  else
                  {  System.out.println("No response from the Manager.");
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
            {  System.out.println("MySpace Driver exiting normally.");
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

      if (argv.length > 0)
      {  String endPoint = argv[0];

         try
         {  middle = new MySpaceIt05Delegate(operator, endPoint);
         }
         catch (Exception all)
         {  System.out.println("Ooops.");
         }

         isTest = false;
         if (argv.length > 1)
         {  isTest = true;
         }
         

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

         JFrame demo = new JFrame("MySpaceDriver");
         MySpaceDriver app = new MySpaceDriver();
         Component demoContents = app.createDemoComponents();
         demo.getContentPane().add(demoContents, BorderLayout.CENTER);

//
//      Finish setting up the frame, and show it.

         demo.addWindowListener(new WindowAdapter()
            {  public void windowClosing(WindowEvent e)
               {  System.out.println("MySpaceDriver window closed.");
                  System.exit(0);
               }
            }
         );

         demo.pack();
         demo.setVisible(true);
      }
      else
      {  System.out.println("Usage:-");
         System.out.println("  java MySpaceDriver Manager-end-point [test]");
         System.out.println("");
         System.out.println(
           "Any value for the second argument causes the Manager to return");
         System.out.println("standard, test responses.");
      }
   }
}
