/*$Id: CommandlineApplication.java,v 1.1 2005/01/14 00:51:19 nw Exp $
 * Created on 13-Jan-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.ui.script.commandline;

import org.astrogrid.community.common.exception.CommunityIdentifierException;
import org.astrogrid.community.common.exception.CommunitySecurityException;
import org.astrogrid.community.common.exception.CommunityServiceException;
import org.astrogrid.community.resolver.exception.CommunityResolverException;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.store.Ivorn;
import org.astrogrid.ui.script.AbstractAstrogridApplication;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.PosixParser;

import java.net.URISyntaxException;
import java.util.StringTokenizer;

/** Base class for all groovy commandline scripts.
 * sets up infrastructure for parsing of commandline options, logging into astrogrid, and accessing system objects.
 * @author Noel Winstanley nw@jb.man.ac.uk 13-Jan-2005
 *
 */
public abstract class CommandlineApplication extends AbstractAstrogridApplication {

    /** to be implemented by extension script - contains main body of script.
     * all member variables will be initialized at the point this method is called 
     * @throws Exception
     */
    public abstract void doIt() throws Exception;
    
    /** 'main' execution method - parses commandline, logs in, and hten executes {@link CommandlineApplication#doIt()},
     * trapping and reporting any exceptions 
     * @param args
     */
    public final void run(String[] args) {
        try {
        line = parser.parse(o,args);
        login();
        doIt();
        } catch (Exception e) {
            System.out.println("An Error occurred :" + e.getClass().getName());
            System.out.println(e.getMessage());
            displayHelp();
            System.exit(-1);
    }
    }
    /** display standard help on commandline options. should be overridden by extenders to provide more information */
    public void displayHelp() {
        (new HelpFormatter()).printHelp("",o);        
    }
    
    /** Construct a new CommandlineApplication
     * sets up the options objects, registers the standard -u, -p, -c and -h options.
     */
    public CommandlineApplication() {
        super();
        o.addOption("u","user",true,"username (optional)")
        .addOption("p","password",true,"password (optional)")
        .addOption("c","community",true,"community (optional)")
        .addOption("h","help",false,"Display this help.");        
    }

    /** set of commandline options that are acceptable */
    public Options o = new Options();
    /** a group of mutually exclusive options */
    public OptionGroup og = new OptionGroup();
    /** the commandline parser that will be used */
    public Parser parser = new PosixParser();
    /** results of parsing the commandline */
    public CommandLine line ;
    
    /** authenticate user in the astrogrid
     * on success, the fields of this class that contain astogrid scripting objects are initialized.
     * @throws CommunityResolverException
     * @throws CommunityServiceException
     * @throws CommunitySecurityException
     * @throws CommunityIdentifierException
     * @throws RegistryException
     */
   public final void login() throws CommunityResolverException, CommunityServiceException, CommunitySecurityException, CommunityIdentifierException, RegistryException {
        String u=null;
        if (line.hasOption("u")) {
                u = line.getOptionValue("user");
        }

        String p=null;
        if (line.hasOption("p")) {
                p = line.getOptionValue("password");
        }
        String comm=null;
        if (line.hasOption("c")) {
                comm = line.getOptionValue("community");
        }
          super.login(u,comm,p);

        }    
   
   /** parse an ivorn (in full or abridged form) into an Ivorn object, and check it belongs to the current user */
   public Ivorn mkFull(String s) throws URISyntaxException {
      Ivorn result =  _mkFull(s);
      if (!result.getPath().equals(homeIvorn.getPath())) {
          throw new IllegalArgumentException("Cannot access another user's myspace");
      }      
      return result;
   }
   
   private Ivorn _mkFull(String s) throws URISyntaxException { // make this the full form of myspace reference.
      String u = null;
      if (s.startsWith("ivo://")) {
           return astrogrid.getObjectBuilder().newIvorn(s);
      }      
      StringTokenizer tok = new StringTokenizer(s,"/");
      if (s.startsWith("#/")) {
          tok.nextElement();// skip junk
           u = tok.nextToken();
           return astrogrid.getObjectBuilder().newIvorn(account.getCommunity(),u,s.substring(1));
      } else  if (s.startsWith( "#")) { // error tolerant, of not qute correct syntax
           u = tok.nextToken().substring(1);
           return astrogrid.getObjectBuilder().newIvorn(account.getCommunity(),u,"/" + s.substring(1));
      } else { // assume to be something else, and pass thru
        throw new IllegalArgumentException("Didn't recognize " + s);
      }
   }

    
}


/* 
$Log: CommandlineApplication.java,v $
Revision 1.1  2005/01/14 00:51:19  nw
introduced java baseclass for all scripting apps -
captures the common code and hides some of the dirty washing.
 
*/