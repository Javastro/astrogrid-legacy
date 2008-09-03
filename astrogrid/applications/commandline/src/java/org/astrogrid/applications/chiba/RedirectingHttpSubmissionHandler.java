/*
 * $Id: RedirectingHttpSubmissionHandler.java,v 1.2 2008/09/03 14:18:40 pah Exp $
 * 
 * Created on 2 May 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.chiba;

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.cookie.CookieSpecBase;
import org.apache.commons.httpclient.cookie.MalformedCookieException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.chiba.xml.xforms.connector.http.HTTPSubmissionHandler;
import org.chiba.xml.xforms.exception.XFormsException;

public class RedirectingHttpSubmissionHandler extends HTTPSubmissionHandler {
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory
	    .getLog(RedirectingHttpSubmissionHandler.class);

    @Override
    protected void execute(HttpMethod httpMethod) throws Exception {
        //		(new HttpClient()).executeMethod(httpMethod);
        HttpClient client = new HttpClient();

        if(logger.isDebugEnabled()){
            logger.debug("context params>>>");
            Map map = getContext();
            Iterator keys = map.keySet().iterator();
            while (keys.hasNext()) {
                String key =  keys.next().toString();
                String value =  map.get(key).toString();
                logger.debug(key + "=" + value);
            }
            logger.debug("<<<end params");
        }

        if (getContext().containsKey(ACCEPT_LANGUAGE)) {
            String acceptLanguage = (String) getContext().get(ACCEPT_LANGUAGE);
            if ((acceptLanguage != null) && (acceptLanguage.length() > 0)){
                httpMethod.setRequestHeader(new Header("Accept-Language", acceptLanguage));
            }
        }

        //alternative method for non-tomcat servers
        if(getContext().containsKey(REQUEST_COOKIE)){
            HttpState state = client.getState();
            state.setCookiePolicy(CookiePolicy.COMPATIBILITY);

            if(getContext().get(REQUEST_COOKIE) instanceof Cookie[]){
                Cookie[] cookiesIn = (Cookie[]) getContext().get(REQUEST_COOKIE);
                if(cookiesIn[0] != null){
                    for (int i=0;i<cookiesIn.length;i++){
                        Cookie cookie = cookiesIn[i];
                        state.addCookie(cookie);
                    }
                    Cookie[] cookies = state.getCookies();
                    Header cookieOut = new CookieSpecBase().formatCookieHeader(cookies);
                    httpMethod.setRequestHeader(cookieOut);
                    client.setState(state);
                }
            }else{
                throw new MalformedCookieException("Cookies must be passed as org.apache.commons.httpclient.Cookie objects.");
            }
        }

        client.executeMethod(httpMethod);
// this is the only change from the overridden method - allow redirects to pass through the rest of the system.
        if (httpMethod.getStatusCode() >= 400) {
// end change
            throw new XFormsException("HTTP status "
                    + httpMethod.getStatusCode()
                    + ": "
                    + httpMethod.getStatusText()
                    + ": "
                    + httpMethod.getResponseBodyAsString());
        }
        this.handleHttpMethod(httpMethod);
    }

    @Override
    protected void handleHttpMethod(HttpMethod httpMethod) throws Exception {
	super.handleHttpMethod(httpMethod);
	getResponseHeader().put("Status", new Integer(httpMethod.getStatusCode()));
    }

}


/*
 * $Log: RedirectingHttpSubmissionHandler.java,v $
 * Revision 1.2  2008/09/03 14:18:40  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.1.2.1  2008/05/08 22:44:04  pah
 * basic UWS working
 *
 */
