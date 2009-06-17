/*$Id: Tracker.java,v 1.2 2009/06/17 10:13:30 jl99 Exp $
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.adql;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Stack;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlbeans.SchemaType;

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
        
        private String element ;
        private SchemaType type ;
        private int childCount = 0 ;
        private int index = -1 ;
        
        private Part() {}
        
        private Part( Part parent, String element, SchemaType type ) {
            this.element = element ;
            this.type = type ;
        }
        
        public Part Reinit( Part parent, String element, SchemaType type ) {
            this.element = element ;
            this.type = type ;
            childCount = 0 ;
            index = -1 ;
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
               .append( "\nchildCount=" )
               .append( childCount )
               .append( "\nindex=" )
               .append( index ) ;            
            return buffer.toString() ;
        }

        public String toPosition() {
            StringBuffer buffer = new StringBuffer( 32 ) ;
            buffer.append( element ) ;
            if( index != -1 ) {
                buffer.append( '[' ).append( index ) ;
                if( type != null ) {
                    buffer
                       .append( ",@type='" )
                       .append( type.getName().getLocalPart() )
                       .append( '\'' ) ;
                }
                buffer.append( ']' ) ; 
            }
            else if( type != null ) {
                buffer
                  .append( "[@type='" )
                  .append( type.getName().getLocalPart() )
                  .append( "']" ) ;
            }
            return buffer.toString() ;
        }
        
        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public int hashCode() {
            final int PRIME = 31;
            int result = super.hashCode();
            result = PRIME * result + (element == null ? 0 : element.hashCode());
            result = PRIME * result + index;
            result = PRIME * result + (type == null ? 0 : type.hashCode());
            return result;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Part other = (Part) obj;
            if (element == null) {
                if (other.element != null) {
                    return false;
                }
            } else if (!element.equals(other.element)) {
                return false;
            }
            if (index != other.index) {
                return false;
            }
            if (type == null) {
                if (other.type != null) {
                    return false;
                }
            } else if (!type.getName().equals(other.type.getName()) ) {
                return false;
            }
            return true;
        }
        
    } // end of class Part
    
    public class Error {
        
        Part[] position = null ;
        String message = null ;
        ParseException parseException = null ;
        
        Error() {}

        public String getMessage() {
            String m = null ;
            if( message != null ) {
                m = message;
            }
            else if( parseException != null ) {
                m = parseException.getLocalizedMessage() ;
            }
            return m ;
        }

        public void setMessage(String message) {
            this.message = message;
        }
        
        /**
         * Returns a short message, which is understood to be the message upto
         * the first end-of-line character.
         * 
         * @return  The shortened message
         */
        public String getShortMessage() {
            String m = getMessage() ;
            int i = m.indexOf( '\n' ) ;                 
            if( i == -1 ) {
                return m ;
            }
            return m.substring( 0, i ) ;  
        }
        
        /**
         * Returns the message description, which is understood to be the message upto
         * either:
         * <ul><li>the beginning of a textual position if one exists, or 
         *     <li>the first end-of-line character
         * 
         * @return  A message description.
         */
        public String getDescription() {
            String m = getMessage() ;
            int i = m.indexOf( "at line" );
            if( i == -1 ) {
                i = m.indexOf( '\n' ) ;                
            }    
            if( i == -1 ) {
                return m ;
            }

            String description = m.substring( 0, i ).trim() ;
            //
            // Example of sparse EOF message...
            
            // Encountered "<EOF>" at line 2, column 9.
            // Was expecting:
            //    "from" ...
            if( description.equals( "Encountered \"<EOF>\"") ) {
                i = m.indexOf( "Was expecting:" ) ;
                if( i != -1 ) {
                    description = m.substring( i ) ;
                }
            }
            else if( description.endsWith( "<EOF>\"" ) ) {
                i = description.indexOf( "<EOF>\"" ) ; 
                description = description.substring( 0, i ) + '"' ;
            }
            return description ;
        }
        
        /**
         * Returns the textual position of where an error occurred, 
         * if indeed the textual position has been recorded. 
         * For example, "at line 0 column 21"
         * 
         * @return  A textual position or the empty string.
         */
        public String getTextualPosition() {
            String m = getMessage() ;
            int i = m.indexOf( "at line" );
            if( i == -1 ) {
                return "" ;              
            }  
            int j = m.indexOf( i, '\n' ) ;                 
            if( j == -1 ) {
                return m.substring( 0, j ) ;  
            }           
            return m.substring( i, j ) ;  
        }

        public Part[] getPosition() {
            return position;
        }

        public void setPosition(Part[] position) {
            this.position = position;
        } 
        
        public String toPosition() {
            StringBuffer buffer = new StringBuffer( position.length * 48 ) ;
            for( int i=0; i<position.length; i++ ) {               
                buffer
                   .append( position[i].toPosition() ) 
                   .append( '/' ) ;
            }
            if( buffer.length() != 0 ) {
                buffer.deleteCharAt(  buffer.length()-1 ) ;
            }
            return buffer.toString() ;
        }

        public ParseException getParseException() {
            return parseException;
        }

        public void setParseException(ParseException parseException) {
            this.parseException = parseException;
        }
        
        public boolean comparePosition( Error err1 ) {
            Part err1pos[] = err1.getPosition() ;
            if( err1pos.length != position.length ) {
                return false ;
            }
            for( int i=0; i<position.length; i++ ) {
                if( !position[i].equals( err1pos[i] ) ) {
                    return false ;
                }
            }
            return true ;
        }
        
    }
    
    private Stack stack ;
    private Stack pool ;
    private ArrayList errors ;
    
    public Tracker() {
        if( log.isTraceEnabled() ) {
            log.trace( "enter: Tracker()" ) ;
        }
        stack = new Stack() ;
        pool = new Stack() ;
        errors = new ArrayList() ;
        if( log.isTraceEnabled() ) {
            log.trace( "exit: Tracker()" ) ;
        }
    }
    
    private Part newPart( String element, SchemaType type ) {
        Part parent = null ;
        if( !stack.isEmpty() ) {
            parent = peek() ;
        }
        if( pool.isEmpty() ) {
            return new Part( parent, element, type ) ;
        } else {
            return ((Part)pool.pop()).Reinit( parent, element, type ) ;
        }       
    }
    
    private Part newPart( Part part ) {
        Part p =  newPart( part.element, part.type ) ;
        p.childCount = part.childCount ;
        p.index = part.index ;
        return p ;
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
        if( stack.isEmpty() )
            return null ;
        Part p = (Part)stack.pop() ;
        if( log.isDebugEnabled() ) {
            log.debug( "pop() left position as: " + this.toPosition() ) ;
        }
        pool.push( p ) ;
        return p ;
    }
    
    public Part pop( boolean yes ) {
        if( yes == true ) {
            return pop() ;
        }
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
       Part p = (Part)stack.push( part );
       if( log.isDebugEnabled() ) {
           log.debug( "push() set up position: " + this.toPosition() ) ;
       }
       return p ;
    }
    
    public Part push( String element ) {
        return push( newPart( element, null ) ) ;
    }
    
    public Part push( String element, SchemaType type ) {
        return push( newPart( element, type ) ) ;
    }
    
    public void setType( SchemaType type ) {
        if( log.isDebugEnabled() ) {
            if( type == null ) {
                log.debug( "setType() received a null type" ) ;
        }
        }
        if( isEmpty() == false ) {
           peek().setType( type ) ;
        }
    }
    
    public void setError( String message ) {
        Error error = new Error() ;
        error.setMessage( message ) ;      
        Part[] position = new Part[ stack.size() ] ;
        position = (Part[])stack.toArray( position ) ;
        for( int i=0; i<position.length; i++ ) {
            position[i] = newPart( position[i] ) ;
        }
        error.setPosition( position ) ;
        errors.add( error ) ;
    }
    
    public void setError( ParseException pex ) {
        if( isExceptionRecorded( pex ) == false ) {       
            Error error = new Error() ;
            error.setParseException( pex ) ;        
            Part[] position = new Part[ stack.size() ] ;
            position = (Part[])stack.toArray( position ) ;
            for( int i=0; i<position.length; i++ ) {
                position[i] = newPart( position[i] ) ;
            }
            error.setPosition( position ) ;
            errors.add( error ) ;
        }
    }
    
    public ArrayList getErrors() {
        return errors ;
    }
    
    public int numberOfErrors() {
        return errors.size() ;
    }

    public int size() {
        return stack.size();
    }

    public Tracker Reinit() {
        if( log.isTraceEnabled() ) {
            log.trace( "enter: Reinit()" ) ;
        }

        Iterator it = stack.iterator() ;
        while( it.hasNext() ) {
            pool.push( stack.pop() ) ;
        }
        
        if( errors != null ) {
            it = errors.iterator() ;
            while( it.hasNext() ) {
                Error error = (Error)it.next() ;
                Part[] position = error.getPosition() ;
                for( int i=0; i<position.length; i++ ) {
                    pool.push( position[i] ) ;
                }
            }     
            errors.clear() ;
        }
                
        if( log.isTraceEnabled() ) {
            log.trace( "exit: Reinit()" ) ;
        }
        return this ;
    }
    
    public void resetPosition() {
        Iterator it = stack.iterator() ;
        while( it.hasNext() ) {
            pool.push( stack.pop() ) ;
        }
        log.debug( "pool.size(): " + pool.size() ) ;
        log.debug( "stack.size(): " + stack.size() ) ;
    }
    
    public String toString() {
        StringBuffer buffer = new StringBuffer( stack.size() * 16 ) ;
        ListIterator it = this.listIterator() ;
        while( it.hasNext() ) {
            Part part = (Part)it.next() ;
            buffer.append( part ) ;
            if( it.hasNext() ) {
                buffer.append( '/' ) ;
            }
        }
        return buffer.toString() ;
    }
    
    public String toPosition() {
        StringBuffer buffer = new StringBuffer( stack.size() * 48 ) ;
        ListIterator it = this.listIterator() ;
        while( it.hasNext() ) {
            Part part = (Part)it.next() ;
            buffer.append( part.toPosition() ) ;
            if( it.hasNext() ) {
                buffer.append( '/' ) ;
            }
        }       
        return buffer.toString() ;
    }
    
    private boolean isExceptionRecorded( ParseException pex ) {
        boolean retCode = false ;
        ListIterator it = errors.listIterator() ;
        Error error = null ;
        while( it.hasNext() ) {
            error = (Error)it.next() ;
            if( pex == error.getParseException() ) {
                retCode = true ;
                break ;
            }
        }     
        return retCode ;
    }

}


/*
$Log: Tracker.java,v $
Revision 1.2  2009/06/17 10:13:30  jl99
Merge of adql v1 parser with maven 2 build.
Due to some necessary restructuring, the maven 1 build process has been removed.

Revision 1.1.2.1  2009/06/16 07:49:37  jl99
First commit of maven 2 build

Revision 1.4  2008/02/04 17:47:29  jl99
Merge of branch adql-jl-2504

Revision 1.3.2.3  2008/02/01 09:54:12  jl99
Some changes to returning message description.
An attempt to make messages containing <EOF> more cosmetic.

Revision 1.3.2.2  2008/01/29 14:16:35  jl99
Small bunch of methods added to return different parts of an error,
eg: description, textual position etc

Revision 1.3.2.1  2008/01/25 15:49:43  jl99
Method Error.getShortMessage() removes line and column references

Revision 1.3  2007/06/06 18:19:27  jl99
Merge of branch adql-jl-2135

Revision 1.2.2.11  2007/05/11 13:59:06  jl99
Better cross validation position processing plus unit tests

Revision 1.2.2.10  2007/04/27 20:40:55  jl99
Improvements to comment processing.

Revision 1.2.2.9  2007/04/20 21:51:52  jl99
Equals method improved

Revision 1.2.2.8  2007/04/18 09:10:06  jl99
Rationalizing multiple error reporting

Revision 1.2.2.7  2007/04/17 15:45:29  jl99
Rationalizing multiple error reporting

Revision 1.2.2.6  2007/04/04 17:35:33  jl99
Improvements to compiling beyond the first error.

Revision 1.2.2.5  2007/03/09 09:21:16  jl99
Started to use position tracking for errors. At present only in debugging messages.

Revision 1.2.2.4  2007/03/08 16:28:03  jl99
Position Tracking

Revision 1.2.2.3  2007/03/04 22:54:09  jl99
More coverage of comments.

Revision 1.2.2.2  2007/03/03 00:22:20  jl99
Tracking comments: principles established.

Revision 1.2.2.1  2007/03/02 21:14:13  jl99
Tracking comments introduced. First attempt to track position.

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