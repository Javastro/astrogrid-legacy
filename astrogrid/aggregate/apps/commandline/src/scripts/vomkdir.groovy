#!/usr/bin/env groovy

/** make a directory in myspace

*/

import org.apache.axis.utils.XMLUtils
import org.apache.commons.cli.*
import org.astrogrid.community.resolver.CommunityPasswordResolver

Options o = new Options()
o.addOption("u","user",true,"username (optional)")
 .addOption("p","password",true,"password (optional)")
 .addOption("c","community",true,"community (optional)")


astrogrid = new org.astrogrid.scripting.Toolbox()
parser = new PosixParser()
try {// catch all exceptio handler.

line = parser.parse(o,this.args)

u=null
if (line.hasOption("u")) {
        u = line.getOptionValue("user")
} else {
        u = astrogrid.systemConfig.getProperty("username")
}

p=null
if (line.hasOption("p")) {
        p = line.getOptionValue("password")
        // not that we do anything with this yet..
} else {
        p = astrogrid.systemConfig.getProperty("password")
}

comm=null
if (line.hasOption("c")) {
        comm = line.getOptionValue("community")
} else {
        comm = astrogrid.systemConfig.getProperty("org.astrogrid.community.ident")
}
login(u,comm,p)
client = mkClient(u,comm)
dir =  mkFull(line.getArgs()[0])
client.newFolder(dir)



} catch (Exception e) { // catch all, for better reporting.
        println("An Error occurred :" + e.class.name)
        println(e.message)
        displayHelp()
        System.exit(-1)
}

def displayHelp() {
        (new HelpFormatter()).printHelp(
<<<DOC
vomkdir <options> resource
  make a directory in myspace.

DOC
,o)
}

def mkFull(s) { // make this the full form of whatever it is.
   user = null;
   if (s.startsWith("ivo://")) {
        return s;
   }
   tok = new java.util.StringTokenizer(s,"/");
   if (s.startsWith("#/")) {
        tok.nextToken() // skip junk
        user = tok.nextToken()
        return astrogrid.objectBuilder.newIvorn(comm,user,s.substring(1)).toString()
   } else  if (s.startsWith("#")) { // error tolerant, of not qute correct syntax
        user = tok.nextToken().substring(1)
        return astrogrid.objectBuilder.newIvorn(comm,user,"/" + s.substring(1)).toString()
   } else { // assume to be something else, and pass thru
     return s
   }
}

def mkClient(u,comm) {
        user = astrogrid.objectBuilder.newUser(u,comm,comm,null)
        return astrogrid.createVoSpaceClient(user)
}

def login(u,comm,p) {

  security = new CommunityPasswordResolver();
  token = security.checkPassword("ivo://" + comm + "/" + u,p)
  // not that we do anything with the token afterwards at the moment..
}
