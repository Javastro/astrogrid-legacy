/*
 * $Id: StoreCommander.java,v 1.2 2004/06/14 23:08:52 jdt Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.store.ui;


/**
 * A command-line interface to the Store delegates.
 * <p>
 * Using this means we can poke things at the delegate straight off the
 * command line.  See printHelp() for usage.
 *
 * @todo: Only a few commands implemented so far
 *
 * @author M Hill
 */


import java.io.*;

import org.astrogrid.community.User;
import org.astrogrid.io.Piper;
import org.astrogrid.store.Agsl;
import org.astrogrid.store.delegate.StoreClient;
import org.astrogrid.store.delegate.StoreDelegateFactory;
import org.astrogrid.store.delegate.StoreFile;
import org.astrogrid.store.delegate.myspace.MySpaceFile;

public class StoreCommander
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
      printListEntry(0, root);
      
   }

   /** Called by list to recursively print directory/entry tree */
   private static void printListEntry(int indent, StoreFile entry) {
      System.out.print(entry.getPath());

      String line = "";
      while (line.length() <indent*2) { line = line +"  "; }
      
      if (entry.isFile()) {
         line = line + entry.getName();
         if (entry instanceof MySpaceFile) {
           //print more file details
           MySpaceFile mse = (MySpaceFile) entry;
           System.out.print( ",   "+mse.getType()+",  "+mse.getOwner()+", "+
                               mse.getCreated()+", "+mse.getExpires()+", "+
                               mse.getSize()+", "+mse.getPermissions());
         }
        System.out.println(line);
      }
      
      if (entry.isFolder()) {
         System.out.println(line+entry.getName());
         StoreFile[] files = entry.listFiles();
         
         for (int i=0;i<files.length;i++) {
            printListEntry(indent+1, files[i]);
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
$Log: StoreCommander.java,v $
Revision 1.2  2004/06/14 23:08:52  jdt
Merge from branches
ClientServerSplit_JDT
and
MySpaceClientServerSplit_JDT

MySpace now split into a client/delegate jar
astrogrid-myspace-<version>.jar
and a server/manager war
astrogrid-myspace-server-<version>.war

Revision 1.1.2.1  2004/06/14 22:33:20  jdt
Split into delegate jar and server war.  
Delegate: astrogrid-myspace-SNAPSHOT.jar
Server/Manager: astrogrid-myspace-server-SNAPSHOT.war

Package names unchanged.
If you regenerate the axis java/wsdd/wsdl files etc you'll need
to move some files around to ensure they end up in the client
or the server as appropriate.
As of this check-in the tests/errors/failures is 162/1/22 which
matches that before the split.

Revision 1.2  2004/05/03 08:55:53  mch
Fixes to getFiles(), introduced getSize(), getOwner() etc to StoreFile

Revision 1.1  2004/04/15 12:40:54  mch
Added command line UI

Revision 1.4  2004/03/09 11:00:39  mch
Fixed for moved myspace delegates

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


