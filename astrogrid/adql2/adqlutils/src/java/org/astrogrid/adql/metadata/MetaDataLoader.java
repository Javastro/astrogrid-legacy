/*
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.adql.metadata ;

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
 * MetaDataLoader
 *
 *
 * @author Jeff Lusted jl99@star.le.ac.uk
 * Aug 2, 2007
 */
public class MetaDataLoader {
    
    private static Log log = LogFactory.getLog( MetaDataLoader.class ) ;
    
    public static Container getMetaData( String propertiesFileName ) {
        //
        // If no meta data properties file available,
        // return an empty md container...
        Container md = new Container() ;
        Properties p = getMetaDataProperties( propertiesFileName ) ;
        if( p == null ) {
            log.warn( "Metadata properties file not found: " + propertiesFileName ) ;
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
        String[]pp = p.getProperty( key ).trim().split( "\\." ) ;       
        Function f = new Function( pp[0], Integer.valueOf( pp[1]).intValue(), Integer.valueOf(pp[2]).intValue() ) ;
        md.pushFunction( f ) ;
    }
    
    private static void loadTable( Container md, Properties p, String key ) {
        String[]pp = p.getProperty( key ).trim().split( "\\." ) ;
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
        String[]valueParts = p.getProperty( key ).trim().split( "\\." ) ;
       
        String[] tableQualifiers = p.getProperty( "table." + valueParts[0] ).trim().split( "\\." ) ;
       
        String ucd = p.getProperty( keyParts[0] + '.' + keyParts[1] + '.' + "ucd" ) ;
        if( ucd != null )
            ucd = ucd.trim() ;
        String unit = p.getProperty( keyParts[0] + '.' + keyParts[1] + '.' + "unit" ) ;
        if( unit != null )
            unit = unit.trim() ;
        String type = p.getProperty( keyParts[0] + '.' + keyParts[1] + '.' + "type" ).trim() ;
        if( type != null )
            type = type.trim();
        String desc = p.getProperty( keyParts[0] + '.' + keyParts[1] + '.' + "description" ).trim() ; 
        if( desc != null )
            desc = desc.trim();
       
        Column c = new Column( valueParts[1], tableQualifiers, new String[] { type, unit, ucd, desc } ) ;
        md.pushColumn( c ) ;
    }
    
    private static Properties getMetaDataProperties( String propertiesFileName ) {        
        InputStream pIS = ClassLoader.getSystemResourceAsStream( propertiesFileName ) ;
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
$Log: MetaDataLoader.java,v $
Revision 1.2  2008/09/08 15:37:22  jl99
Merge of branch adql_jl_2575_mark2 into HEAD

Revision 1.1.2.1  2008/08/29 14:49:07  jl99
First mass commit for the new project adql2

Revision 1.1.2.1  2008/08/01 18:50:36  jl99
Complete reorg for purposes of setting up a maven build

Revision 1.4  2007/08/09 10:43:18  jl99
tidy

Revision 1.3  2007/08/07 17:37:05  jl99
Initial multi-threaded test environment for AdqlCompilerSV

Revision 1.2  2007/08/02 14:13:31  jl99
Test system metadata loader with metadata modelled on a subset of SDSS.

Revision 1.1  2007/08/02 11:20:18  jl99
Test meta data loader first commit.

*/