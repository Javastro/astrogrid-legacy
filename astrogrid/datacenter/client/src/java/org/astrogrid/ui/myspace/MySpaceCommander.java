/*
 * $Id: MySpaceCommander.java,v 1.3 2004/03/06 19:34:21 mch Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.ui.myspace;


/**
 * A command-line interface to the MySpace delegates.
 * Using this means we can poke things at the delegate straight off the
 * command line.  See printHelp() for usage.
 *
 * At the moment only works with VoRL references...
 *
 * @author M Hill
 */


import java.io.*;

import org.astrogrid.community.User;
import org.astrogrid.io.Piper;
import org.astrogrid.store.Agsl;
import org.astrogrid.store.delegate.MySpaceFile;
import org.astrogrid.store.delegate.MySpaceFolder;
import org.astrogrid.store.delegate.StoreClient;
import org.astrogrid.store.delegate.StoreDelegateFactory;
import org.astrogrid.store.delegate.StoreFile;

public class MySpaceCommander
{

   /**
    * Prints the usage instructions
    */
   public static void printHelp()   {
      System.out.println("MySpaceCommander: Command-line access to MySpace");
      System.out.println();
      System.out.println("    org.astrogrid.ui.myspace.MySpaceCommander  <command> <VospaceRL> [<file>]");
      System.out.println();
      System.out.println("Commands:    delete - removes the specified VospaceRL");
      System.out.println("Commands:    list   - lists the files at the given VospaceRL");
      System.out.println("Commands:    get    - downloads from the given VospaceRL to the given file");
      System.out.println();
      System.out.println("AGSL is of the form: "+Agsl.FORM);
      System.out.println("eg    astrogrid:store:myspace:http://grendel12.roe.ac.uk:8080/astrogrid-mySpace#avodemo@test.astrogrid.org/serv1/votable/6dfResults");
   }

   public static StoreClient contactServer(Agsl vorl) throws IOException {
      System.out.println("Connecting to myspace...");
      return StoreDelegateFactory.createDelegate(User.ANONYMOUS, vorl);
      
   }
   
   /**
    * Deletes the file at the given VoRL
    */
   public static void delete(Agsl vorl) throws IOException
   {
      StoreClient delegate = contactServer(vorl);
      
      System.out.println("...deleting...");
      delegate.delete(vorl.getPath());

      System.out.println("...done");
   }
   
   /**
    * Lists the files at the given VoRL
    */
   public static void list(Agsl server, String filter) throws IOException
   {
      StoreClient delegate = contactServer(server);
      
      System.out.println("Getting List (filter='"+filter+"')...");
      StoreFile root = delegate.getFiles(filter);

      //recursively display tree
      System.out.println("Path, Type, Owner, Created Date, Expiry Date, Size, Permissions:");
      printListEntry(root);
      
   }

   /** Called by list to recursively print directory/entry tree */
   private static void printListEntry(StoreFile entry) {
      System.out.print(entry.toAgsl());
      
      if (entry instanceof MySpaceFile) {
         //print more file details
         MySpaceFile mse = (MySpaceFile) entry;
         System.out.print( ",   "+mse.getType()+",  "+mse.getOwner()+", "+
                             mse.getCreated()+", "+mse.getExpires()+", "+
                             mse.getSize()+", "+mse.getPermissions());
      }
      System.out.println();
      
      if (entry instanceof MySpaceFolder) {
         StoreFile[] files = ((MySpaceFolder) entry).listFiles();
         
         for (int i=0;i<files.length;i++) {
            printListEntry(files[i]);
         }
      }
   }
      
   
   /**
    * Gets the files from the given VoRL to the given File
    */
   public static void get(Agsl source, File target) throws IOException
   {
      StoreClient delegate = contactServer(source);
      
      System.out.println("Connecting to source...");
      InputStream in = new BufferedInputStream(delegate.getStream(source.getPath()));
      OutputStream out = new BufferedOutputStream(new FileOutputStream(target));
      
      System.out.println("Copying...");
      Piper.pipe(in, out);
      System.out.println("..done");
      out.close();
   }

   /**
    * Executes the commands given
    */
   public static void main(String[] args) throws IOException {
      if (args.length<2) {
         printHelp();
         return;
      }
      
      String command = args[0].toLowerCase().trim();

      if (command.equals("delete")) {
         delete(new Agsl(args[1]));
      }
      else if (command.equals("list")) {
         String filter = "*";
         if (args.length>2) filter = args[2];
         list(new Agsl(args[1]), filter);
      }
      else if (command.equals("get")) {
         if (args.length<3) {
            System.out.println("Not enough arguments");
            printHelp();
         }
         else {
            get(new Agsl(args[1]), new File(args[2]));
         }
      }
      else {
         System.out.println("Unknown command: "+command);
         printHelp();
      }
   }
}

/*
$Log: MySpaceCommander.java,v $
Revision 1.3  2004/03/06 19:34:21  mch
Merged in mostly support code (eg web query form) changes

Revision 1.1  2004/03/03 17:40:58  mch
Moved ui package

Revision 1.3  2004/03/02 01:33:24  mch
Updates from chagnes to StoreClient and Agsls

Revision 1.2  2004/02/24 16:04:02  mch
Config refactoring and moved datacenter It04.1 VoSpaceStuff to myspace StoreStuff

Revision 1.1  2004/02/17 16:04:06  mch
New Desktop GUI

Revision 1.3  2004/02/17 12:01:24  mch
Added Get command

Revision 1.2  2004/02/17 11:56:12  mch
Added List command

Revision 1.1  2004/02/17 03:47:04  mch
Naughtily large lump of various fixes for demo

 */


