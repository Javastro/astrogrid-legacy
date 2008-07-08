/*$Id: JettyWebServer.java,v 1.19 2008/07/08 08:59:00 nw Exp $
 * Created on 31-Jan-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.system;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.SystemUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlrpc.XmlRpcClient;
import org.apache.xmlrpc.XmlRpcException;
import org.astrogrid.acr.builtin.ShutdownListener;
import org.astrogrid.desktop.SplashWindow;
import org.astrogrid.desktop.modules.system.contributions.ServletContextContribution;
import org.astrogrid.desktop.modules.system.contributions.ServletsContribution;
import org.astrogrid.desktop.modules.system.contributions.WebappContribution;
import org.mortbay.http.HttpContext;
import org.mortbay.http.HttpException;
import org.mortbay.http.HttpRequest;
import org.mortbay.http.HttpResponse;
import org.mortbay.http.SocketListener;
import org.mortbay.http.handler.AbstractHttpHandler;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.ServletHttpContext;
import org.mortbay.jetty.servlet.WebApplicationContext;
import org.mortbay.util.InetAddrPort;

/** Factory to create a webserver, listening to a random port, with a hashed key path.
  * @author Noel Winstanley noel.winstanley@manchester.ac.uk 31-Jan-2005
 *
 */
public class JettyWebServer implements WebServerInternal, ShutdownListener{
    /** end of range of ports to scan */
    public final static int SCAN_END_PORT_DEFAULT = 8880;

    /** start of range of ports to scan to find a free available one. */
    public final static int SCAN_START_PORT_DEFAULT = 8001; 
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(JettyWebServer.class);
    
    protected  ServletHttpContext defaultContext;
    protected String defaultContextName;
    protected int port = -1;
    
    protected final List contextObjects;
    protected int scanEndPort = SCAN_END_PORT_DEFAULT;
    protected int scanStartPort = SCAN_START_PORT_DEFAULT;
    protected final Server server; 
    protected  SocketListener sockets;
    protected final List servlets;
    protected URL urlRoot;
    private boolean disableConnectionFile = false;
    
    private File connectionFile = new File(System.getProperty("user.home") , ".astrogrid-desktop");
    private InetAddress inetAddress;

    private final List<WebappContribution> webapps;

    public JettyWebServer(List servlets,List contextObjects, List<WebappContribution> webapps )  {
        super();
        this.webapps = webapps;
        SplashWindow.reportProgress("Starting Astro Runtime HTTP interface...");
        this.server = new Server();
       // this.server.setStopGracefully(true); 
        this.servlets = servlets;
        this.contextObjects = contextObjects;
        inetAddress = MyInetAddress.myAddress();
       
    }
   
    public String getKey() {
        return this.defaultContextName;
    }
    
    public int getPort() {
        return this.port;
    }
    
    
    public String getUrlRoot() {
        return this.urlRoot.toString();
    }
    
    public URL getRoot() {
    	return this.urlRoot;
    }



    public void halting() {
        try {
        	/* NWW - logs a null pointer exception from the internals - so maybe not needed?
        	if (context != null) {
        		context.stop(false);
        	}
        	*/                      
        	if (sockets != null) {
        		sockets.stop();
        	}
        	/* likewise - noisy - maybe we don't need this?
        	 * yep - appears to shut down cleanly without calling these.
            server.stop();
            
            server.destroy();
            */        
        } catch (InterruptedException e) {
            logger.warn("InterruptedException",e);
        } catch (Throwable t) {
            logger.warn("throwable while halting..",t);
        }

    }

    public void init() throws Exception  {
        if (port < 1) { // not been set.
            port = AbstractRmiServerImpl.findSparePort(scanStartPort,scanEndPort,inetAddress);
        } else {
            if (!AbstractRmiServerImpl.checkPort(port,inetAddress)) {
                throw new Exception("Cannot connect on specified port " + port);
            }       
        }
        logger.info("Web Server will listen on port " + port);
        if (defaultContextName == null || defaultContextName.trim().length() == 0) {
        	logger.warn("Context/Session name not provided - will autogenerate one");
            generateContextName();
        }
        
        urlRoot = new URL("http",inetAddress.getHostAddress(),port,"/" +  defaultContextName + "/");
        logger.info("Default session root is " + urlRoot);        
        if (! disableConnectionFile) {
        	recordDetails() ;
        }
        final InetAddrPort inetAddrPort = new InetAddrPort(inetAddress,port);        
        sockets = new SocketListener(inetAddrPort);
        server.addListener(sockets);
        
        this.defaultContext = (ServletHttpContext) server.addContext(defaultContextName);
        populateContextAttributes(this.defaultContext);
        //now add servlets to this context..
        populateContextServlets(this.defaultContext);        
                
        // now work through any additional web apps listed in the configuration.
        boolean rootContextProvided = false;
        for (WebappContribution webapp : webapps) {            
            WebApplicationContext appContext = server.addWebApplication(webapp.getContext(),webapp.getLocation());
            appContext.setDefaultsDescriptor(this.getClass().getResource("webdefault.xml").toString()); // our own custom descriptor (resource in this package).
            populateContextAttributes(appContext); // all webapps get the context objects too.
            rootContextProvided = rootContextProvided || webapp.getContext().equals("/");
        }
        
        //if there's not already something at the root,  this places something on the root of the web server.
        // otherwise jetty generates a helpful error page listing all available contexts - not what we want.
        if (!rootContextProvided) {
            HttpContext root = new HttpContext();
            root.setContextPath("/");
            root.addHandler(new AbstractHttpHandler() {
                public void handle(String pathInContext, String pathParams
                        , HttpRequest req, HttpResponse resp) throws HttpException, IOException {
                    resp.sendError(HttpResponse.__403_Forbidden,"Astro Runtime - Forbidden");
                }
            });
            server.addContext(root);
        }
        server.start();
    }

    /**
     * 
     */
    private void populateContextServlets(ServletHttpContext cxt) {
        for (Iterator i = servlets.iterator(); i.hasNext(); ) {
            ServletsContribution d = (ServletsContribution)i.next();
            logger.info("Adding servlet " + d.getName());
            try {
                cxt.addServlet(d.getName(),d.getPath(),d.getServletClass().getName());
            } catch (Exception x) {
                logger.error("Failed to deploy servlet " + d.getName(),x);
            } 
        }
    }

    // populate a context with attribute objects..
	private void populateContextAttributes(ServletHttpContext cxt) {
		for (Iterator i = contextObjects.iterator(); i.hasNext(); ) {
            ServletContextContribution contrib = (ServletContextContribution)i.next();
            logger.debug("Adding context object " + contrib.getName());
            cxt.getServletContext().setAttribute(contrib.getName(),contrib.getObject());
        }
	}

// don't care - web server will never object.
public String lastChance() {
    return null;
}

public void setConnectionFile(String connectionFile) {
    this.connectionFile = new File(connectionFile);
}
public void setContextName(String contextName) {
    this.defaultContextName = contextName;
}
public void setPort(int port) {
    this.port = port;
}
public void setScanEndPort(int scanEndPort) {
    this.scanEndPort = scanEndPort;
}
public void setScanStartPort(int scanStartPort) {
    this.scanStartPort = scanStartPort;
}
/* generates a random string */
private void generateContextName() {
    Random r = new Random();
    defaultContextName = Long.toString(Math.abs(r.nextLong()),16);        
}
private void recordDetails() throws IOException {
     if (connectionFile.exists()) {
        connectionFile.delete();
    }
    connectionFile.deleteOnExit();
    PrintWriter pw = new PrintWriter(new FileWriter(connectionFile));
    pw.println(urlRoot);
    pw.close();
}

/**
 * @return the disableConnectionFile
 */
public boolean isDisableConnectionFile() {
	return this.disableConnectionFile;
}

/**
 * @param disableConnectionFile the disableConnectionFile to set
 */
public void setDisableConnectionFile(boolean disableConnectionFile) {
	this.disableConnectionFile = disableConnectionFile;
}

/** Set the internet address this server is to listen on.
 * 
 * defaults to localhost. set this to configure machines with
 * more than one network catd / internet address.
 * 
 * @param netAddress dotted ip address or server name. null or empty strings are ignored.
 * 
 * @throws UnknownHostException 
 */
public void setInetAddress(String netAddress) throws UnknownHostException {
	if (netAddress != null && netAddress.trim().length() > 0) {
		this.inetAddress = InetAddress.getByName(netAddress);
	}
}

// webServerinternal interfvace - for context management.

public URL createContext(String sessionId) {
	logger.info("Creating context for session " + sessionId);
	ServletHttpContext cxt = (ServletHttpContext)server.addContext(sessionId);
	populateContextAttributes(cxt);
	populateContextServlets(cxt);
	// previously, I'd tried a lighter-weight approach using a forwarder - to forward
	// requests on the new context to the servlets on the existing context - but couldnt get
	// it to function correctlly. SO I'll create a new set of servlets for each
	// context  - at least all the underlying hivemind services are shared.
	//ForwardHandler forw = new ForwardHandler("/" + defaultContextName);
//	forw.addForward("/*","/" + defaultContextName );
//	cxt.addHandler(forw);
	try {
		cxt.start();
	} catch (Exception e) {
		logger.fatal("Failed to start context for session " + sessionId);
	}
	return getContextBase(sessionId);
}

public void dropContext(String sessionId) {
	logger.info("Dropping context for session " + sessionId);
	HttpContext context = findContext(sessionId);
	if (context != null) {
		server.removeContext(context);
	try {
		context.stop();
	} catch (InterruptedException x) {
		logger.info("InterruptedException",x);
	}
	}
}

public URL getContextBase(String sessionId) {
	HttpContext context = findContext(sessionId);
	if (context == null) {
		return null;
	}
	try {
		return  new URL("http",inetAddress.getHostAddress(),port,"/" +  sessionId + "/");
	} catch (MalformedURLException x) {
		logger.error("MalformedURLException in getContextBase",x);
		return null; // unlikely
	}
}

	/** finds a context, if it exists, else return null
	 * necessary because Jetty's getContext will create it if it doesn't exist
	 * @param sessionId
	 * @return
	 */
	private HttpContext findContext (String sessionId) {
		HttpContext[] contexts = server.getContexts();
		if (logger.isDebugEnabled()) {
			logger.debug(ArrayUtils.toString(contexts));
		}
		String cxtPath = "/" + sessionId; // workaround for oddness
		for (int i = 0; i < contexts.length; i++) {			
			if (cxtPath.equals(contexts[i].getContextPath())) {
				return contexts[i];
			}
		}
		return null;
	}

    public Test getSelftest() {
        TestSuite ts = new TestSuite("Internal webserver");
        ts.addTest(new TestCase("AR connection file"){
            protected void runTest() {
                File f = new File(SystemUtils.getUserHome(),".astrogrid-desktop");
                assertTrue("~/.astrogrid-desktop not present",f.exists());
                FileReader fr = null;
                URL endpoint = null;
                try {
                    fr = new FileReader(f);
                    String str = new BufferedReader(fr).readLine();
                    assertNotNull("~/.astrogrid-desktop is empty",str);
                    endpoint = new URL(str);
                } catch (MalformedURLException ex) {
                    logger.info("unable to parse contents",ex);
                    fail("unable to parse contents of ~/.astrogrid-desktop");
                } catch (FileNotFoundException x) {
                    // just tested for this.
                    fail("~/.astrogrid-desktop not present");                    
                } catch (IOException x) {
                    logger.info("unable to read ~/.astrogrid-desktop");
                    fail("unable to read ~/.astrogrid-desktop");
                } finally {
                    if (fr != null) {
                        try {
                            fr.close();
                        } catch (IOException e) {
                            // ho hum
                        }
                    }
                }
                if (endpoint == null) {
                    fail("unable to parse contents of ~/.astrogrid-desktop");
                }
                assertEquals("incorrect endpoint in ~/.astrogrid-desktop",getRoot(),endpoint);
                try {
                    endpoint.openConnection().connect();
                } catch (IOException x) {
                    logger.error("unable to connect to internal webserver",x);
                    fail("unable to connect to internal webserver");
                }
            }
        });
        ts.addTest(new TestCase("HTML access to AR"){
            protected void runTest() {
                InputStream is = null;
                try {
                    URL endpoint = new URL(getRoot(),"system/webserver/getRoot/plain");
                    is = endpoint.openStream();
                    String val = new BufferedReader(new InputStreamReader(is)).readLine();
                    assertNotNull("no response from webserver",val);                    
                    assertEquals("webserver didn't repond with expected result",getRoot().toString(),val.trim());
                } catch (MalformedURLException x) {
                    logger.error("unable to construct html call url",x);
                    fail("Unable to construct html call url");
                } catch (IOException x) {
                    logger.error("unable to call html interface",x);
                    fail("Unable to call html interface");
                } finally {
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e) {
                            // don't care
                        }
                    }
                }                
            }
        });
        ts.addTest(new TestCase("XMLRPC access to AR") {
            protected void runTest() { //@todo test passing a parameter to the server too??
                XmlRpcClient client;
                try {
                    client = new XmlRpcClient(new URL(getRoot(),"xmlrpc"));
                    Object result = client.execute("system.webserver.getRoot",new Vector());
                    assertNotNull("no response from xmlrpc server",result);
                    assertTrue("unexpected response from xmlrpc server",result instanceof String);
                    assertEquals("xmlrpc server didn't respond with expected result",getRoot().toString(),result);
                } catch (MalformedURLException x) {
                    logger.error("unable to contruct xmlrpc endpoint",x);
                    fail("unable to construct xmlrpc endpoint");
                } catch (XmlRpcException x) {
                    logger.error("xmlrpc failure",x);
                    fail("xmlrpc failure");
                } catch (IOException x) {
                    logger.error("IO Failure", x);
                    fail("IO failure");
                }
            }
        });
        return ts;
    }

}


/* 
$Log: JettyWebServer.java,v $
Revision 1.19  2008/07/08 08:59:00  nw
Added ability to deploy external war in embedded jetty.

Revision 1.18  2008/03/05 10:59:22  nw
added progress reporting to splashscreen

Revision 1.17  2007/10/22 10:29:54  nw
factored common inet-address code into separate helper class.

Revision 1.16  2007/10/22 07:23:56  nw
improved logging.

Revision 1.15  2007/10/12 11:02:49  nw
added code for selftesting

Revision 1.14  2007/09/21 16:35:13  nw
improved error reporting,
various code-review tweaks.

Revision 1.13  2007/06/18 17:00:13  nw
javadoc fixes.

Revision 1.12  2007/04/18 15:47:07  nw
tidied up voexplorer, removed front pane.

Revision 1.11  2007/03/22 19:03:47  nw
added support for sessions and multi-user ar.

Revision 1.10  2007/01/29 16:45:07  nw
cleaned up imports.

Revision 1.9  2007/01/29 11:11:37  nw
updated contact details.

Revision 1.8  2006/08/31 21:32:49  nw
doc fixes.

Revision 1.7  2006/06/27 19:18:32  nw
adjusted todo tags.

Revision 1.6  2006/06/27 10:42:04  nw
minor fix.

Revision 1.5  2006/06/15 09:52:06  nw
simplified shutdown.

Revision 1.4  2006/04/26 15:56:18  nw
made servers more configurable.added standalone browser launcher

Revision 1.3  2006/04/18 23:25:44  nw
merged asr development.

Revision 1.1.68.4  2006/04/18 18:49:03  nw
version to merge back into head.

Revision 1.1.68.3  2006/04/14 02:45:02  nw
finished code.extruded plastic hub.

Revision 1.1.68.2  2006/04/04 10:31:26  nw
preparing to move to mac.

Revision 1.1.68.1  2006/03/22 18:01:30  nw
merges from head, and snapshot of development

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.6  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.

Revision 1.5  2005/05/12 15:59:10  clq2
nww 1111 again

Revision 1.3.8.2  2005/05/11 11:55:30  nw
removed unused interface.

Revision 1.3.8.1  2005/05/09 15:45:33  nw
implemented port scanning.

Revision 1.3  2005/04/27 13:42:41  clq2
1082

Revision 1.2.2.1  2005/04/25 11:18:51  nw
split component interfaces into separate package hierarchy
- improved documentation

Revision 1.2  2005/04/13 12:59:12  nw
checkin from branch desktop-nww-998

Revision 1.1.2.2  2005/03/23 14:36:18  nw
got pw working

Revision 1.1.2.1  2005/03/18 12:09:31  nw
got framework, builtin and system levels working.

Revision 1.2  2005/02/22 01:10:31  nw
enough of a prototype here to do a show-n-tell on.

Revision 1.1  2005/02/21 11:25:07  nw
first add to cvs
 
*/

