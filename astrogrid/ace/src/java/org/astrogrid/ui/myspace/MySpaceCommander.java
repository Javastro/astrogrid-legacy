/*
 * $Id: MySpaceCommander.java,v 1.1 2004/02/17 03:47:04 mch Exp $
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

import java.io.File;
import java.io.IOException;
import org.astrogrid.community.Account;
import org.astrogrid.vospace.VospaceRL;
import org.astrogrid.vospace.delegate.VoSpaceClient;
import org.astrogrid.vospace.delegate.VoSpaceDelegateFactory;

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
      System.out.println("VospaceRL is of the form: vospace://protocol.authority/endpointpath#myspace/path/filename");
      System.out.println("eg    vospace://http.grendel12.roe.ac.uk:8080/astrogrid-mySpace#test.astrogrid.org/avodemo/serv1/votable/6dfResults");
   }
      
   /**
    * Deletes the file at the given VoRL
    */
   public static void delete(VospaceRL vorl) throws IOException
   {
      System.out.println("Connecting to myspace...");
      VoSpaceClient delegate = VoSpaceDelegateFactory.createDelegate(Account.ANONYMOUS, vorl.getDelegateEndpoint().toString());
      
      System.out.println("...deleting...");
      delegate.delete(vorl.getDelegateFileRef());

      System.out.println("...done");
   }
   
   /**
    * Lists the files at the given VoRL
    */
   public static void list(VospaceRL server)
   {
      throw new UnsupportedOperationException("Not written list yet");
   }

   /**
    * Gets the files from the given VoRL to the given File
    */
   public static void get(VospaceRL source, File target)
   {
      throw new UnsupportedOperationException("Not written get yet");
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
         delete(new VospaceRL(args[1]));
      }
      else if (command.equals("list")) {
         list(new VospaceRL(args[1]));
      }
      else if (command.equals("get")) {
         if (args.length<3) {
            System.out.println("Not enough arguments");
            printHelp();
         }
         else {
            get(new VospaceRL(args[1]), new File(args[2]));
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
Revision 1.1  2004/02/17 03:47:04  mch
Naughtily large lump of various fixes for demo

 */

