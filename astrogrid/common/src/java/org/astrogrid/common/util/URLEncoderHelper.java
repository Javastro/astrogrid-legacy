/*
 * $Id: URLEncoderHelper.java,v 1.2 2008/09/17 08:16:05 pah Exp $
 * 
 * Created on 29-Sep-2005 by Paul Harrison (pharriso@eso.org)
 * Copyright 2005 ESO. All rights reserved.
 *
 * This software is published under the terms of the ESO 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.common.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Encode URL strings. Here so that do not have to remember the encoding.
 * @author Paul Harrison (pharriso@eso.org) 29-Sep-2005
 * @version $Name:  $
 * @since initial Coding
 */
public class URLEncoderHelper {
   /**
    * Logger for this class
    */
   private static final Log logger = LogFactory.getLog(URLEncoderHelper.class);

   /**
    * just has static helper methods.
    */
   private URLEncoderHelper() {
      super();
    
   }

   public static String encode(String s){
      try {
         return URLEncoder.encode(s, "UTF-8");
      }
      catch (UnsupportedEncodingException e) {
        logger.fatal("internal error specifying ecoding", e);
        // perhaps we do not have to do this...
        return URLEncoder.encode(s);
      }
   }
   
   public static String decode(String s){
	   try {
		return URLDecoder.decode(s, "UTF-8");
	} catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
	       logger.fatal("internal error specifying ecoding", e);
	        // perhaps we do not have to do this...
	        return URLDecoder.decode(s);
	 
	}
   }
   
   public static Map decodeQueryParameters(URL url)
   {
	   Map retval = new HashMap();
	   String query = url.getQuery();
	   String[] pars = query.split("&");
	   for (int i = 0; i < pars.length; i++) {
		String par = pars[i];
		int i1 = par.indexOf('=');
		if(i1== -1){
			// there is no equals - but parameter alone has been specified
			retval.put(par, null);
			
		}
		else{
			retval.put(par.substring(0, i1-1), URLEncoderHelper.decode(par.substring(i1+1)));
		}
			
			
		
	}
	return retval ;
   }
   
   
}


/*
 * $Log: URLEncoderHelper.java,v $
 * Revision 1.2  2008/09/17 08:16:05  pah
 * result of merge of pah_community_1611 branch
 *
 * Revision 1.1.2.1  2008/05/17 20:55:13  pah
 * safety checkin before interop
 *
 * Revision 1.2  2006/09/15 14:35:43  pharriso
 * add decoder
 *
 * Revision 1.1  2005/09/30 11:21:56  pharriso
 * added to stop having the irritation of catching Encoding exceptions everywhere.
 *
 */
