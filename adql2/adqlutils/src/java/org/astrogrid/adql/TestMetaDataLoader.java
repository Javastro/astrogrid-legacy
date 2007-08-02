/*$Id: TestMetaDataLoader.java,v 1.1 2007/08/02 11:20:18 jl99 Exp $
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.adql;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

import org.astrogrid.adql.metadata.Column;
import org.astrogrid.adql.metadata.Container;
import org.astrogrid.adql.metadata.Function;
import org.astrogrid.adql.metadata.Schema;
import org.astrogrid.adql.metadata.Table;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

/**
 * TestMetaDataLoader
 *
 *
 * @author Jeff Lusted jl99@star.le.ac.uk
 * Aug 2, 2007
 */
public class TestMetaDataLoader {
    
    private static Log log = LogFactory.getLog( Interactive.class ) ;
    
    public static Container getMetaData() {
        //
        // If no meta data properties file available,
        // return an empty md container...
        Container md = new Container() ;
        Properties p = getMetaDataProperties() ;
        if( p == null ) {
            log.warn( "adql-metadata.properties file not found." ) ;
            return md ;
        }
        //
        // Load what data we have...
        Enumeration e = p.keys() ;
        while( e.hasMoreElements() ) {
            String key = (String)e.nextElement() ;
            if( key.startsWith( "func" ) ) {
                loadFunction( md, p, key ) ;
            }
            else if( key.startsWith( "table" ) ) {
                loadTable( md, p, key ) ;
            }
            else if( key.startsWith( "col" )  && key.endsWith( "name" ) ) {
                loadColumn( md, p, key ) ;
            }
            else {
                
            }           
        }     
        return md ;
    }
    
    private static void loadFunction( Container md, Properties p, String key ) {
        String[]pp = ((String)p.getProperty( key )).split( "\\." ) ;
        
        Function f = new Function( pp[0], Integer.valueOf( pp[1]).intValue(), Integer.valueOf(pp[2]).intValue() ) ;
        md.pushFunction( f ) ;
    }
    
    private static void loadTable( Container md, Properties p, String key ) {
        String[]pp = ((String)p.getProperty( key )).split( "\\." ) ;
        if( pp.length == 1 ) {
            Table t = new Table( pp[0]  ) ;
            md.pushTable( t ) ;
        }
        else {
            String[] schemaStrings = new String[pp.length-1] ;
            System.arraycopy( pp, 0, schemaStrings, 0, schemaStrings.length ) ;
            Schema s = new Schema( schemaStrings ) ;
            md.pushSchema( s ) ;
            Table t = new Table( pp[ pp.length-1 ], s ) ;
            md.pushTable( t ) ;
        }
        
    }
    
    private static void loadColumn( Container md, Properties p, String key ) {
        String[]keyParts = key.split( "\\." ) ;
        String[]valueParts = ((String)p.getProperty( key )).split( "\\." ) ;
       
        String[] tableQualifiers = p.getProperty( "table" + valueParts[0] ).split( "\\." ) ;
       
        String ucd = p.getProperty( keyParts[0] + keyParts[1] + "ucd" ) ;
        String type = p.getProperty( keyParts[0] + keyParts[1] + "type" ) ;
        String desc = p.getProperty( keyParts[0] + keyParts[1] + "description" ) ; 
       
        Column c = new Column( valueParts[1], tableQualifiers, new String[] { ucd, type, desc } ) ;
        md.pushColumn( c ) ;
    }
    
    private static Properties getMetaDataProperties() {        
        InputStream pIS = AdqlStoXTest.class.getResourceAsStream( "adql-metadata.properties" ) ;
        if( pIS == null )
            return null ;
        Properties p = new Properties() ;
        try {
            p.load( pIS ) ;
        }
        catch( IOException iox ) {
            return null ;
        }
        return p ;
    }
    
}


/*
$Log: TestMetaDataLoader.java,v $
Revision 1.1  2007/08/02 11:20:18  jl99
Test meta data loader first commit.

*/