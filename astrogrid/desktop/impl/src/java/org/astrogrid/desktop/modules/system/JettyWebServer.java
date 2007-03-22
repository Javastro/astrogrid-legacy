/*$Id: JettyWebServer.java,v 1.11 2007/03/22 19:03:47 nw Exp $
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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.builtin.ShutdownListener;
import org.astrogrid.acr.system.WebServer;
import org.astrogrid.desktop.modules.system.contributions.ServletContextContribution;
import org.astrogrid.desktop.modules.system.contributions.ServletsContribution;
import org.mortbay.http.HttpContext;
import org.mortbay.http.HttpException;
import org.mortbay.http.HttpRequest;
import org.mortbay.http.HttpResponse;
import org.mortbay.http.HttpServer;
import org.mortbay.http.SocketListener;
import org.mortbay.http.handler.AbstractHttpHandler;
import org.mortbay.http.handler.DumpHandler;
import org.mortbay.http.handler.ErrorPageHandler;
import org.mortbay.http.handler.ForwardHandler;
import org.mortbay.http.handler.NotFoundHandler;
import org.mortbay.http.handler.NullHandler;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.ServletHttpContext;
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
    protected final HttpServer server; 
    	// it's actually a subclass, Server, but this provides no additional useful methods
    	// but is neccessary to use this subclass to be able to deploy servlets.
    protected  SocketListener sockets;
    protected final List servlets;
    protected URL urlRoot;
    private boolean disableConnectionFile = false;
    
    private File connectionFile = new File(System.getProperty("user.home") , ".astrogrid-desktop");
    private InetAddress inetAddress;
    public JettyWebServer(List servlets,List contextObjects )  {
        super();
        this.server = new Server();
       // this.server.setStopGracefully(true); 
        this.servlets = servlets;
        this.contextObjects = contextObjects;
        
        try {
			inetAddress = InetAddress.getLocalHost(); // default, but can be overridden later using setter.
		} catch (UnknownHostException x) {
			logger.warn("Failed to resolve local ip - falling back to loopback address");
			logger.debug("Cause was",x);
			try {
				inetAddress = InetAddress.getByName("127.0.0.1");
			} catch (UnknownHostException x1) {
				logger.fatal("Loopback address considered invalid",x1); 
			} 
		} 
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
        	e.printStackTrace();
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
        populateContext(this.defaultContext);
         
        // this places something on the root of the web server.
        // otherwise jetty generates a helpful error page listing all available contexts - not what we want.
        HttpContext root = new HttpContext();
        root.setContextPath("/");
        root.addHandler(new AbstractHttpHandler() {
			public void handle(String pathInContext, String pathParams
					, HttpRequest req, HttpResponse resp) throws HttpException, IOException {
				resp.sendError(HttpResponse.__403_Forbidden,"Astro Runtime - no session specified");
			}
        });
        server.addContext(root);
        server.start();
    }

    // populate a context with servlets and objects.
	private void populateContext(ServletHttpContext cxt) {
		for (Iterator i = contextObjects.iterator(); i.hasNext(); ) {
            ServletContextContribution contrib = (ServletContextContribution)i.next();
            logger.info("Adding context object " + contrib.getName());
            cxt.getServletContext().setAttribute(contrib.getName(),contrib.getObject());
        }
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
 * @param inetAddress the inetAddress to set
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
	populateContext(cxt);
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
	}
	try {
		context.stop();
	} catch (InterruptedException x) {
		logger.info("InterruptedException",x);
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
			logger.debug(Arrays.toString(contexts));
		}
		String cxtPath = "/" + sessionId; // workaround for oddness
		for (int i = 0; i < contexts.length; i++) {			
			if (cxtPath.equals(contexts[i].getContextPath())) {
				return contexts[i];
			}
		}
		return null;
	}

}


/* 
$Log: JettyWebServer.java,v $
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

