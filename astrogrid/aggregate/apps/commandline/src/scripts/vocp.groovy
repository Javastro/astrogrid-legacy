#!/usr/bin/env groovy
import org.apache.commons.cli.*
import org.astrogrid.community.resolver.CommunityPasswordResolver

// setup commandline options
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
        p = astrogrid.systemConfig.geetProperty("password")
}


comm=null
if (line.hasOption("c")) {
        comm = line.getOptionValue("community")
} else {
        comm = astrogrid.systemConfig.getProperty("org.astrogrid.community.ident")
}
login(u,comm,p)
src = mkFull(line.getArgs()[0])
dest = mkFull(line.getArgs()[1])

if (dest.startsWith("ivo://") && src.startsWith("ivo://")) { // its a remotve move..
   client = mkClient(u,comm)
   client.copy(src,dest)
}
else if (dest.startsWith("ivo://") && isRemoteURL(src)) {//if dest is an ivorn, can use an optimal method
   client = mkClient(u,comm)
  client.putUrl(src.toURL(),dest,false)
}
else { // have to pipe it ourselves.
  is = null
  os = null
  if (src == "-") {
        is = System.in
  } else {
    is = astrogrid.ioHelper.getExternalValue(src).read()
  }

  if (dest == "-") {
        os =  System.out
  } else {
     os = astrogrid.ioHelper.getExternalValue(dest).write()
  }

  is.withStream{ iss |
        os.withStream{ oss |
                astrogrid.ioHelper.bufferedPipe(iss,oss)
                }
        }
}

} catch (Exception e) { // catch all, for better reporting.
        println("An Error occurred :" + e.class.name)
        println(e.message)
        printHelp()
        System.exit(-1)
}

def printHelp() {
        (new HelpFormatter()).printHelp(
<<<DOC
vocp <options> <src-file> <dest-file>
where src, dest may be any of
http://... a http resource
ftp://...  a ftp resource
ivo://...  a myspace resource
file:/...  a file resource
-          standard input / output.
vocp also supports an abridged notation for myspace locations, in the form
#/fred/path - equivalent to ivo://default-community/fred#/fred/path

anything else is interpreted as a local file path

VOCP will the most efficient method of moving data from src to dest - i.e.
if both are remote, no data will pass through this client.

NB: writing to http:// and ftp:// urls is likely to fail at present.
DOC
,o)
}

def mkFull(s) { // make this the full form of whatever it is.
   user = null;
   if (["ivo://","http://","file:/","ftp://","-"].any{s.startsWith(it)}) {
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
   } else { // assume to be file.
    return (new java.io.File(s)).toURL().toString()
   }
}




def isRemoteURL(src) {
  return ["http://","ftp://"].any{src.startsWith(it)}
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
