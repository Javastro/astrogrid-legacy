/*$Id: PortalImpl.java,v 1.5 2005/05/12 15:59:12 clq2 Exp $
 * Created on 22-Mar-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ag;

import org.astrogrid.acr.astrogrid.Community;
import org.astrogrid.acr.astrogrid.Portal;
import org.astrogrid.acr.astrogrid.UserLoginEvent;
import org.astrogrid.acr.astrogrid.UserLoginListener;
import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.ui.script.ScriptEnvironment;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

/**nasty impleemtation - needs to be quite a hack to get the session cookie.
 * @author Noel Winstanley nw@jb.man.ac.uk 22-Mar-2005
 *
 */
public class PortalImpl implements Portal, UserLoginListener {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(PortalImpl.class);

    /** Construct a new PortalImpl
     * 
     */
    public PortalImpl(Community community, Configuration conf,BrowserControl browser) {
        super();
        this.community = community;
        this.conf = conf;
        this.browser = browser;
    }
    
    public static String PORTAL_ROOT_KEY = "ag.portal.root";
    
    protected final Community community;
    protected final Configuration conf;
    protected final BrowserControl browser;
    private String session;
    private URL root;
    
    protected String getSession() throws HttpException, IOException {
        if (this.session == null) {
            root = new URL(conf.getKey(PORTAL_ROOT_KEY));
            logger.info("Portal root " + getRoot());
            HttpClient client = new HttpClient();
            URL loginURL = new URL(getRoot(),"main/mount/login/login-form");
            logger.info("login url " + loginURL);
            PostMethod meth = new PostMethod(loginURL.toString());
            ScriptEnvironment env = community.getEnv();
            meth.addParameter("user",env.getAccount().getName());
            meth.addParameter("pass",env.getPassword());
            meth.addParameter("community",env.getAccount().getCommunity());
            meth.setFollowRedirects(true);
            int code = client.executeMethod(meth);
            logger.info("Response " + code);
            if (code != 200) {
                throw new IOException("Could not connect to portal - gave HTTP response code " + code);
            }
            Cookie[] cookies = client.getState().getCookies();
            if (logger.isDebugEnabled()) {
                logger.debug(Arrays.asList(cookies));
            }

            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i].getName().equals("JSESSIONID")) {
                    session = cookies[i].getValue();

                    logger.info("Setting session to " + session);                    
                }
            }
            if (session == null) {
                throw new IOException("No cookie set from login form - can't open portal page");
            }            
            
        }
        return  this.session;
    }
    


    protected URL getRoot() throws HttpException, IOException {
        if (root == null) {
            getSession(); // initiaizes root
        }
        return root;
    }


    /** open page in the portal 
     * @throws Exception*/
    public void openPage(String path) throws Exception {
        URL page = new URL(getRoot(),path + ";jsessionid=" + getSession());
        logger.info("Opening " + page);
        browser.openURL(page);
    }



    /**
     * @see org.astrogrid.acr.astrogrid.Portal#openPageWithParams(java.lang.String, java.util.Map)
     */
    public void openPageWithParams(String path, Map args) throws Exception {        
        StringBuffer query = new StringBuffer();
        for (Iterator i = args.entrySet().iterator(); i.hasNext();) {
            Map.Entry e = (Map.Entry)i.next();
            query.append(e.getKey());
            query.append("=");
        //you'd have thought that the jobURN would be urlencoded - but this causes an error.    
       //     query.append(URLEncoder.encode(e.getValue().toString(),"UTF-8"));
            query.append(e.getValue().toString());
            if (i.hasNext()) {
                query.append("&");
            }
        }
        URL page = new URL(getRoot(),path + ";jsessionid=" + getSession() + "?" + query);
        logger.info("Opening " + page);        
        browser.openURL(page);        
    }



    /**
     * @see org.astrogrid.acr.astrogrid.UserLoginListener#userLogin(org.astrogrid.desktop.modules.ag.UserLoginEvent)
     */
    public void userLogin(UserLoginEvent e) {
    }



    /**
     * @see org.astrogrid.acr.astrogrid.UserLoginListener#userLogout(org.astrogrid.desktop.modules.ag.UserLoginEvent)
     */
    public void userLogout(UserLoginEvent e) {
        session = null;
        root = null;
    }
    
}


/* 
$Log: PortalImpl.java,v $
Revision 1.5  2005/05/12 15:59:12  clq2
nww 1111 again

Revision 1.3.8.1  2005/05/11 14:25:24  nw
javadoc, improved result transformers for xml

Revision 1.3  2005/04/27 13:42:40  clq2
1082

Revision 1.2.2.1  2005/04/25 11:18:51  nw
split component interfaces into separate package hierarchy
- improved documentation

Revision 1.2  2005/04/13 12:59:11  nw
checkin from branch desktop-nww-998

Revision 1.1.2.3  2005/04/04 08:49:27  nw
working job monitor, tied into pw launcher.

Revision 1.1.2.2  2005/03/29 16:10:59  nw
integration with the portal

Revision 1.1.2.1  2005/03/23 14:36:18  nw
got pw working
 
*/