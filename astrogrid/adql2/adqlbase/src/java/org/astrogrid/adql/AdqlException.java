/*$Id: AdqlException.java,v 1.3 2009/06/29 21:12:58 jl99 Exp $
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.adql;

import java.util.ArrayList;
import java.util.ListIterator;
import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

/**
 * The <code>AdqlException</code> class wraps the <code>Tracker</code> object in error
 * situations. The <code>Tracker</code> object is a container of messages and tracked
 * positions.
 *
 * @see org.astrogrid.adql.Tracker
 * 
 * @author Jeff Lusted jl99@star.le.ac.uk
 * Apr 17, 2007
 */
public class AdqlException extends Exception {
    
    private static Log log = LogFactory.getLog( AdqlException.class ) ;
    
    private Tracker tracker ;
    
    /**
     * The only permitted constructor, as <code>AdqlException</code> must contain the
     * <code>Tracker</code> object.
     * 
     * @param tracker
     * @see org.astrogrid.adql.Tracker
     */
    public AdqlException( Tracker tracker ) {
        this.tracker = tracker ;
    }
    
    private AdqlException() {}
    
    /**
     * Returns the enclosed <code>Tracker</code>.
     * 
     * @return the enclosed <code>Tracker</code> object.
     */
    public Tracker getTracker() {
        return this.tracker ;
    }
    

    /**
     * Returns the error messages contained within the <code>Tracker</code>
     * 
     * @return the error messages contained within the <code>Tracker</code> object.
     */
    public String[] getErrorMessages() {  
        if( log.isTraceEnabled() ) { log.trace( "enter: getMessages()" ) ; } 
        ArrayList errors = tracker.getErrors() ;
        log.debug( "errors total: " + errors.size() ) ;
        ArrayList filteredErrors = new ArrayList() ;
        ListIterator eit = errors.listIterator() ;
        Tracker.Error e = null ;
        while( eit.hasNext() ) {
            e = (Tracker.Error)eit.next() ;
            if( checkForDuplicatePosition( e, filteredErrors ) == false ) {
                filteredErrors.add( e ) ;
            }
        }
        
        String[] messages = new String[ filteredErrors.size() ] ;
        int i = 0 ;
        ListIterator fit = filteredErrors.listIterator() ;
        while( fit.hasNext() ) {
            e = (Tracker.Error)fit.next() ;
            messages[i] = e.getShortMessage() ;
            if( !messages[i].endsWith( " " ) ) {
                messages[i]+= " " ;
            }
            String position = e.toPosition().trim() ;
            if( position.length() > 0 ) {
                messages[i]+= "at position " + position ;
            }           
            i++ ;
        }        
        if( log.isTraceEnabled() ) { log.trace( "exit: getMessages()" ) ; } 
        return messages ;
    }
    
    
    private boolean checkForDuplicatePosition( Tracker.Error e, ArrayList filteredErrors ) {
        ListIterator it = filteredErrors.listIterator() ;
        Tracker.Error fe = null ;
        while( it.hasNext() ) {
            fe = (Tracker.Error)it.next() ;
            if( e.comparePosition( fe ) == true ) {
                return true ;
            }
        }      
        return false ;
    }
    
    /**
     * Returns true if a warning message is present, false otherwise.
     * 
     * @return true if a warning message is present, false otherwise.
     */
    public boolean isWarningMessage() {
        return tracker.isWarningMessage() ;
    }
    
    /**
     * Returns the warning messages contained within the <code>Tracker</code>
     * 
     * @return the warning messages contained within the <code>Tracker</code> object.
     */
    public String[] getWarningMessages() {
        return tracker.getWarningMessages() ;
    }
    
}


/*
$Log: AdqlException.java,v $
Revision 1.3  2009/06/29 21:12:58  jl99
Updated javadocs

Revision 1.2  2008/09/08 15:37:03  jl99
Merge of branch adql_jl_2575_mark2 into HEAD

Revision 1.1.2.1  2008/08/29 14:49:02  jl99
First mass commit for the new project adql2

Revision 1.2.2.1  2008/07/12 21:47:54  jl99
(1) Refinement of warning messages.
(2) Refinement of reserved words diagnostics.

Revision 1.2  2007/09/07 10:59:16  jl99
A back peddle on position tracking. Needs a rethink.
The experiment showed up a potential for recursion.

Revision 1.1  2007/06/28 09:07:44  jl99
Creation of temporary project adql2 to explore complexities of moving
ADQL to conform to the draft spec of April 2007.

Revision 1.2  2007/06/06 18:19:27  jl99
Merge of branch adql-jl-2135

Revision 1.1.2.3  2007/04/19 11:49:50  jl99
Changes to public methods to accommodate multiple error recording.

Revision 1.1.2.2  2007/04/18 09:10:29  jl99
Rationalizing multiple error reporting

Revision 1.1.2.1  2007/04/17 15:45:29  jl99
Rationalizing multiple error reporting

*/