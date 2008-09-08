/*$Id: ContextPath.java,v 1.2 2008/09/08 15:37:02 jl99 Exp $
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.adql;

import java.util.ArrayList;

/**
 * ContextPath
 *
 *
 * @author Jeff Lusted jl99@star.le.ac.uk
 * Dec 12, 2006
 */
public class ContextPath {
    
    private ArrayList list ;
    
    public class Element {
        
        private String name ;
        private String type ;
        
        private Element( String name, String type ) {
            this.name = name ;
            this.type = type ;
        }
       
        public Element( String elementDescriptor ) {
            int iFirstBracket = elementDescriptor.indexOf( '[' ) ;
            // int iType = elementDescriptor.indexOf( "@type=" ) ;
            int iFirstSpeech = elementDescriptor.indexOf( '\'' ) ;
            int iLastSpeech = elementDescriptor.lastIndexOf( '\'' ) ;
            if( iFirstSpeech == -1 ) {
                iFirstSpeech = elementDescriptor.indexOf( '\"' ) ; 
                iLastSpeech = elementDescriptor.lastIndexOf( '\"' ) ;
            }           
            try {
                name = elementDescriptor.substring( 0, iFirstBracket ) ;
                type = elementDescriptor.substring( iFirstSpeech+1, iLastSpeech ) ;
            }
            catch( Exception ex ) {
                name = elementDescriptor;
                type = "" ;
            }
        }

        /**
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * @return the type
         */
        public String getType() {
            return type;
        }
        
    }
    
    private ContextPath() {}
    
    public ContextPath( String contextPath ) {
        
        String[] segs = contextPath.split( "/" ) ;
        list = new ArrayList( segs.length ) ;
        
        for( int i=0; i<segs.length; i++ ) {
            list.add( new Element( segs[i] ) ) ;
        }
  
    }
    
    public Element getElement( int index ) {
        Element retVal = null ;
        try {
            retVal = (Element)list.get( index ) ;
        }
        catch( IndexOutOfBoundsException ioobex ) {
            retVal = new Element( "", "" ) ;
        }
        return retVal ;
    }
    
    public Element getChild() {
        return (Element)list.get( list.size()-1 ) ;
    }
    
    public Element getParent() {
        if( list.size() == 1 )
            return new Element("","") ;
        return (Element)list.get( list.size()-2 ) ;
    }
    
    public int size() {
        return list.size() ;
    }
    
 

}


/*
$Log: ContextPath.java,v $
Revision 1.2  2008/09/08 15:37:02  jl99
Merge of branch adql_jl_2575_mark2 into HEAD

Revision 1.1.2.1  2008/08/29 14:49:03  jl99
First mass commit for the new project adql2

Revision 1.1  2007/06/28 09:07:44  jl99
Creation of temporary project adql2 to explore complexities of moving
ADQL to conform to the draft spec of April 2007.

Revision 1.3  2007/06/06 18:19:28  jl99
Merge of branch adql-jl-2135

Revision 1.2.2.1  2007/04/17 15:45:29  jl99
Rationalizing multiple error reporting

Revision 1.2  2007/01/26 09:45:54  jl99
Merge of adql-jl-2031-a into HEAD

Revision 1.1.2.1  2007/01/10 13:59:00  jl99
First commit after cvs recovery.

Revision 1.1.2.1  2006-12-19 14:22:36  jl99
More revision and refinement of fragment processing: more elements and types

*/