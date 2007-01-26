/*$Id: Tracker.java,v 1.2 2007/01/26 09:45:56 jl99 Exp $
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.adql;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;
import java.util.* ;
import org.apache.xmlbeans.XmlOptions ;
import org.astrogrid.adql.v1_0.beans.* ;
import org.astrogrid.stc.region.v1_10.beans.* ;
import org.astrogrid.stc.coords.v1_10.beans.* ;
import org.w3c.dom.Node ;
import org.apache.xmlbeans.SchemaProperty;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlAnySimpleType;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.XmlCursor;

//import org.astrogrid.acr.astrogrid.TableBean;
//import org.astrogrid.acr.astrogrid.DatabaseBean;
//import org.astrogrid.acr.ivoa.resource.Catalog;
//import org.astrogrid.acr.ivoa.resource.DataCollection;
/**
 * Tracker
 *
 *
 * @author Jeff Lusted jl99@star.le.ac.uk
 * Dec 29, 2006
 */
public class Tracker {
    
    private static Log log = LogFactory.getLog( Tracker.class ) ;
    
    public class Part {
        /**
         * Logger for this class
         */
        private final Log log = LogFactory.getLog(Part.class);
        
        private String element ;
        private SchemaType type ;
        private int childCount = 0 ;
        private StringBuffer comment ;
        private int index = 0 ;
        
        private Part() {}
        
        private Part( String element, SchemaType type ) {
            this.element = element ;
            this.type = type ;
        }
        
        public Part Reinit( String element, SchemaType type ) {
            this.element = element ;
            this.type = type ;
            this.childCount = 0 ;
            return this ;
        }

        public SchemaType getType() {
            return type;
        }
        
        public void setType(SchemaType type) {
            this.type = type;
        }

        public String getElement() {
            return element;
        }
        
        public void setElement( String element ) {
            this.element = element ;
        }
        
        public int incrementChildCount() {
            return ++childCount ;
        }
        
        public int getChildCount() {
            return childCount ;
        }
        
        public String toString() {
            StringBuffer buffer = new StringBuffer( 32 ) ;
            buffer
               .append( element )
               .append( "[@type='" )
               .append( type.getName().getLocalPart() )
               .append( "']" )
               .append( "\nchildCount=" )
               .append( childCount )
               .append( "\nindex=" )
               .append( index ) 
               .append( "\ncomment: " )
               .append( comment ) ;            
            return buffer.toString() ;
        }

        public String getComment() {
            if( comment == null )
                return "" ;
            return comment.toString() ;
        }

        public void setComment(String commnt ) {
            if( this.comment == null ) {
                this.comment = new StringBuffer() ;
            }
            this.comment.append( commnt ) ;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }
        
    }
    
    private Stack stack ;
    private Part lastVisited ;
    private Object userDefinedObject ;
    private Stack pool ;
    private ArrayList comments ;
    
    public Tracker() {
        stack = new Stack() ;
        pool = new Stack() ;
    }
    
    public Tracker( Object userDefinedObject ) {
        this() ;
        this.userDefinedObject = userDefinedObject ;
    }
    
    private Part newPart( String element, SchemaType type ) {
        if( pool.isEmpty() )
            return new Part( element, type ) ;
        else
            return ((Part)pool.pop()).Reinit( element, type ) ;       
    }

    public boolean isEmpty() {
        return stack.isEmpty();
    }

    public ListIterator listIterator() {
        return stack.listIterator();
    }

    public Part peek() {
        return (Part)stack.peek();
    }

    public Part pop() {
        if( lastVisited != null && !comments.contains( lastVisited ) ) {            
            pool.push( lastVisited ) ;
        }
        lastVisited = (Part)stack.pop();
        return lastVisited ;
    }
    
    public Part pop( boolean yes ) {
        if( yes == true )
            return pop() ;
        return null ;
    }

    private Part push( Part part ) {
       if( isEmpty() == false ) {
           //
           // Bump up the child count of the previous parent...
           int index = peek().incrementChildCount() ;
           //
           // Maintain child index for part...
           part.setIndex( index - 1 ) ;
       }
       return (Part)stack.push( part );
    }
    
    public Part push( String element ) {
        return push( newPart( element, null ) ) ;
    }
    
    public Part push( String element, SchemaType type ) {
        return push( newPart( element, type ) ) ;
    }
    
    public void setType( SchemaType type ) {
        if( log.isDebugEnabled() )
            if( type == null ) {
                log.debug( "setType() received a null type" ) ;
        }
        peek().setType( type ) ;
    }

    public int size() {
        return stack.size();
    }

    public Tracker Reinit( Object userDefinedObject ) {
        if( lastVisited != null ) {
            pool.push( lastVisited ) ;
        }
        Iterator it = stack.iterator() ;
        while( it.hasNext() ) {
            pool.push( stack.pop() ) ;
        }
        
        if( comments != null ) {
            it = comments.iterator() ;
            while( it.hasNext() ) {
                Part p = (Part)it.next() ;
                if( pool.contains( p ) == false ) {
                    pool.push( p ) ;
                }
            }
            comments.clear() ;
        }
                
        lastVisited = null ;
        this.userDefinedObject = userDefinedObject ;
        return this ;
    }

    public Part getLastVisited() {
        return lastVisited;
    }

    public Object getUserDefinedObject() {
        return userDefinedObject;
    } 
    
    public String toString() {
        StringBuffer buffer = new StringBuffer( stack.size() * 16 ) ;
        ListIterator it = this.listIterator() ;
        while( it.hasNext() ) {
            Part part = (Part)it.next() ;
            buffer.append( part ) ;
            if( it.hasNext() )
                buffer.append( '/' ) ;
        }
        buffer.append( "     lastVisited: " + lastVisited.toString() ) ;        
        return buffer.toString() ;
    }
    
    public String commentsToString() {
        if( comments == null )
            return "" ;
        StringBuffer buffer = new StringBuffer( comments.size() * 32 ) ;
        ListIterator it = comments.listIterator() ;
        while( it.hasNext() ) {
            Part part = (Part)it.next() ;
            buffer.append( part ) ;
        }     
        return buffer.toString() ;
    }
    
    
    //
    // Notes.
    // As it stands, even an attribute could have a comment
    // entered by a user. But these could not be accommodated
    // within the xml as an xml comment. The easiest solution
    // would be to concatinate each comment and associate them
    // with the owning element.
    public void setComment( String comment ) {
//        Part part = peek() ;
//        part.setComment( comment ) ;
//        if( comments != null ) {
//            comments = new ArrayList() ;
//        }
//        comments.add( part ) ;
    }

}


/*
$Log: Tracker.java,v $
Revision 1.2  2007/01/26 09:45:56  jl99
Merge of adql-jl-2031-a into HEAD

Revision 1.1.2.4  2007/01/25 15:21:30  jl99
Removed ACR dependencies for the moment.

Revision 1.1.2.3  2007/01/25 14:13:21  jl99
Logging adjustments

Revision 1.1.2.2  2007/01/19 08:32:53  jl99
Working towards including comments.

Revision 1.1.2.1  2007/01/10 13:59:01  jl99
First commit after cvs recovery.

*/