/*
 * @(#)Service.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 */
package org.astrogrid.datacenter.query;

import org.astrogrid.i18n.AstroGridMessage;
import org.astrogrid.log.Log;
import org.w3c.dom.Element;

/**
 * The <code>Service</code> class represents ...
 * <p>
 * Introductory text.... For example:
 * <p><blockquote><pre>
 *
 * </pre></blockquote>
 * <p>
 *
 * @author  Jeff Lusted
 * @author  Phill Nicolson
 * @version 1.0 27-May-2003
 * @see     org.astrogrid.datacenter.Query
 * @since   AstroGrid 1.2
 */
public class Service {

   private static final String
      ASTROGRIDERROR_COULD_NOT_CREATE_SERVICE = "AGDTCE00450" ;

   private String
      name,
      url ;

   public Service( Element serviceElement ) throws QueryException {
      Log.trace( "Service(Element): entry") ;

      try {
         setName(serviceElement.getAttribute( AdqlTags.SERVICE_NAME_ATTR )) ;
         setUrl(serviceElement.getAttribute( AdqlTags.SERVICE_URL_ATTR )) ;
      }
      catch( Exception ex ) {
         AstroGridMessage
            message = new AstroGridMessage( ASTROGRIDERROR_COULD_NOT_CREATE_SERVICE
                                              , this);
         Log.logError( message.toString(), ex ) ;
         throw new QueryException( message, ex );
      }
      finally {
         Log.trace( "Service(Element): exit") ;
      }

   } // end of Service( Element )


   public void setName(String name) {
      this.name = name;
   }

   public String getName() {
      return name;
   }

   public void setUrl(String url) {
      this.url = url;
   }

   public String getUrl() {
      return url;
   }

} // end of class Service
