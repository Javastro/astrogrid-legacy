/*
 * @(#)TemplateManager.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 */
package org.astrogrid;

import java.util.Hashtable ;
import java.io.IOException ;
import java.io.BufferedInputStream ;
import org.astrogrid.log.Log;
import org.astrogrid.i18n.AstroGridMessage ;

import org.jconfig.utils.*;
import java.io.* ;


/**
 * The <code>TemplateManager</code> class represents
 *
 * @author  Jeff Lusted
 * @version 1.0 08-Jul-2003
 * @see
 * @see
 * @since   AstroGrid 1.2
 */
public class TemplateManager {

    private static TemplateManager
        singletonTemplateManager = new TemplateManager() ;

   private static String
       ASTROGRIDERROR_UNABLE_TO_LOCATE_TEMPLATE = "AG{0}Z00007:{1}: Unable to locate template file [{2}]" ;

    private Hashtable
        templates = new Hashtable() ;


   public static TemplateManager getInstance(){ return singletonTemplateManager; }


    private TemplateManager() {}


    public String getTemplate( String subsystemAcronym, String name ) {
      Log.trace( "getTemplate(): entry") ;

      String
         targetTemplate = null ;
      BufferedInputStream
         bistream = null ;

      try {

         targetTemplate = (String)templates.get( name ) ;

         if( targetTemplate == null ) {

                ResourceLocator
                    resourceLocator = new ResourceLocator( name ) ;

                BufferedReader
                    bufferedReader = new BufferedReader( new FileReader( resourceLocator.getFile() ) ) ;

            StringBuffer
                sBuffer = new StringBuffer( 1024 ) ;
                String
                    line = null ;

            line = bufferedReader.readLine() ;
             while( line != null ){
               sBuffer.append( line ) ;
                    line = bufferedReader.readLine() ;
             }

            targetTemplate = sBuffer.toString() ;
            templates.put( name, targetTemplate ) ;

         } // endif

      }
      catch ( IOException ex ) {

         AstroGridMessage
            message = new AstroGridMessage( ASTROGRIDERROR_UNABLE_TO_LOCATE_TEMPLATE
                                              , subsystemAcronym
                                              , Configurator.getClassName( TemplateManager.class )
                                              , name ) ;
         Log.logError( message.toString(), ex ) ;

      }
      finally {
         if( bistream != null ) try{ bistream.close(); } catch( IOException ioex ){ ; }
         Log.trace( "getTemplate(): exit") ;
      }

      return targetTemplate ;

    } // end of getTemplate()


} // end of class TemplateManager
