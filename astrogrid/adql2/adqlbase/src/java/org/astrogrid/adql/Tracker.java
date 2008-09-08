/*$Id: Tracker.java,v 1.2 2008/09/08 15:37:02 jl99 Exp $
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

import org.apache.xmlbeans.*;

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
    private static final boolean TRACE_BUFFER_ENABLED = true ;
    private static final int TRACE_BUFFER_LENGTH = 4096 ;
    
    private boolean traceBufferEnabled = false ;
    
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
        
        public void setChildCount( int count ) {
            this.childCount = count ;
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
                    if( type.isAnonymousType() == false ) {
                        buffer
                            .append( ",@type='" )
                            .append( type.getName().getLocalPart() )
                            .append( '\'' ) ;
                    }
                }
                buffer.append( ']' ) ; 
            }
            else if( type != null) {
                if( type.isAnonymousType() == false ) {
                    buffer
                        .append( "[@type='" )
                        .append( type.getName().getLocalPart() )
                        .append( "']" ) ;
                }
                
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
            if (type == null && other.type != null) {
                return false;                
            }
            if (type != null && other.type == null) {
                return false;                
            } else if (other.type != type ) {
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
        
        public String getShortMessage() {
            String m = getMessage() ;
            int i = m.indexOf( '\n' ) ;
            if( i == -1 ) {
                return m ;
            }
            return m.substring( 0, i ) ;  
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
    private ArrayList warningMessages ;
    private StringBuffer traceBuffer ;
    
    public Tracker() {
        if( log.isTraceEnabled() ) { log.trace( "enter: Tracker()" ) ; }
        stack = new Stack() ;
        pool = new Stack() ;
        errors = new ArrayList() ;
        warningMessages = new ArrayList() ;
        if( log.isTraceEnabled() ) { log.trace( "exit: Tracker()" ) ; }
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
        if( stack.isEmpty() )
            return null ;
        return (Part)stack.peek();
    }

    public Part pop() {
        if( stack.isEmpty() )
            return null ;
        Part p = (Part)stack.pop() ;
//        if( log.isDebugEnabled() ) {
//            log.debug( "pop() left position as: " + this.toPosition() ) ;
//        }
        pool.push( p ) ;
        if( TRACE_BUFFER_ENABLED ) tracePosition() ;
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
//       if( log.isDebugEnabled() ) {
//           log.debug( "push() set up position: " + this.toPosition() ) ;
//       }
       if( TRACE_BUFFER_ENABLED ) tracePosition() ;
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
           if( TRACE_BUFFER_ENABLED ) retracePosition() ;
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
    
    //
    // This does not work. Fails on the comparison.
    // However, if/when perfected, will be far superior
    // to push/pop of position on tracker object.
    private Part[] formPosition( XmlObject xoError ) {       
        Part[] parts ;
        XmlCursor cu = xoError.newCursor() ;
        try {
            Stack xos = new Stack() ;
            if( cu.isAnyAttr() ) {
                cu.toParent() ;
            }
            xos.push( cu.getObject() ) ;
            //
            // Work backwards to the select 
            // or possibly the first element in a fragment...
            while( cu.toParent() ) {
                if( cu.isStartdoc() == false ) {
                    xos.push( cu.getObject() ) ;
                }
            }
            //
            // Work through to the error element 
            // creating a part/position array on the way...
            parts = new Part[ xos.size() ] ;
            for( int i=0; i<parts.length; i++ ) {
                XmlObject xo = (XmlObject)xos.pop() ;
                int childIndex = 0 ;
                XmlObject cox = cu.getObject() ;
                //
                // Fails on the comparison.
                while( cox != xo ) {
                    cu.toNextSibling() ;
                    cox = cu.getObject();
                    childIndex++ ;
                }
                Part p = new Part() ;
                p.setElement( cu.getName().getLocalPart() ) ;
                p.setType( xo.schemaType() ) ;
                p.setIndex( childIndex ) ;
                parts[i] = p ;
                cu.toFirstChild() ;
            }
        }
        finally {
            cu.dispose() ;
        }
        return parts ;
    }
    
    public void setError( String message, XmlObject xoError ) {
        //
        // Form a position from the error object...
        Part[] parts = formPosition( xoError ) ;
        //
        // Generate the error
        Error error = new Error() ;
        error.setMessage( message ) ;   
        error.setPosition( parts ) ;
        errors.add( error ) ;       
    }
    
    public void setError( ParseException pex, XmlObject xoError ) {
        if( isExceptionRecorded( pex ) == false ) {       
            Error error = new Error() ;
            error.setParseException( pex ) ;        
            Part[] position = formPosition( xoError ) ;
            error.setPosition( position ) ;
            errors.add( error ) ;
        }
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
            warningMessages.clear() ;
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
    
    private void tracePosition() {
        if( !isTraceBufferEnabled() )
            return ;
        traceBuffer.append( '\n' ).append( this.toPosition() ) ;
    }
    
    private void retracePosition() {
        if( !isTraceBufferEnabled() )
            return ;
        int i = traceBuffer.lastIndexOf( "\n" ) ;
        traceBuffer.delete( i, traceBuffer.length() ) ;
        traceBuffer.append( '\n' ).append( this.toPosition() ) ;
    }
    
    public void writeTraceBuffer() {
        if( !isTraceBufferEnabled() )
            return ;
        log.trace( traceBuffer.toString() ) ;
        resetTraceBuffer() ;
    }
    
    public void flushTraceBuffer() {
        if( !isTraceBufferEnabled() )
            return ;
        resetTraceBuffer() ;
    }
    
    private void resetTraceBuffer() {
        traceBuffer.delete( 0, traceBuffer.length() ) ;
        traceBuffer.append( "Tracker's position trace buffer:" ) ;
    }

    /**
     * @return the traceBufferEnabled
     */
    public boolean isTraceBufferEnabled() {
        if( TRACE_BUFFER_ENABLED && log.isTraceEnabled() )
            return traceBufferEnabled;
        return false ;
    }

    /**
     * @param traceBufferEnabled the traceBufferEnabled to set
     */
    public void setTraceBufferEnabled( boolean traceBufferEnabled ) {
        this.traceBufferEnabled = traceBufferEnabled;
        if( TRACE_BUFFER_ENABLED && traceBufferEnabled ) {
            traceBuffer = new StringBuffer( TRACE_BUFFER_LENGTH ) ;
            traceBuffer.append( "Tracker's position trace buffer:" ) ;
        }
        else {
            traceBuffer = null ;
        }
    }
    
    public void setWarningMessage( String message ) {
        warningMessages.add( message ) ;
    }
    
    public boolean isWarningMessage() {
        return warningMessages.size() > 0 ;
    }
    
    public String[] getWarningMessages() {
        return (String[]) warningMessages.toArray( new String[ warningMessages.size()] ) ;
    }

}


/*
$Log: Tracker.java,v $
Revision 1.2  2008/09/08 15:37:02  jl99
Merge of branch adql_jl_2575_mark2 into HEAD

Revision 1.1.2.1  2008/08/29 14:49:03  jl99
First mass commit for the new project adql2

Revision 1.5.2.4  2008/07/12 21:47:54  jl99
(1) Refinement of warning messages.
(2) Refinement of reserved words diagnostics.

Revision 1.5.2.3  2008/07/12 09:24:46  jl99
(1) Introduced warning messages.
(2) Used warning messages where possible incorrect use of reserved words encountered.
(3) Allowing sort keys to be qualified.

Revision 1.5.2.2  2008/07/05 21:12:26  jl99
Delving into bit processing and bit/hex literals: nearly done

Revision 1.5.2.1  2008/06/18 17:51:46  jl99
In predicate and null predicate improved

Revision 1.5  2007/09/04 16:24:47  jl99
Yet another attempt at refining position tracking.

Revision 1.4  2007/08/02 14:15:28  jl99
fix to null pointer exception

Revision 1.3  2007/07/13 14:37:38  jl99
Accommodating anonymous types again.

Revision 1.2  2007/07/10 21:05:49  jl99
new ADQL schema  experiment contains an anonymous type.

Revision 1.1  2007/06/28 09:07:45  jl99
Creation of temporary project adql2 to explore complexities of moving
ADQL to conform to the draft spec of April 2007.

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