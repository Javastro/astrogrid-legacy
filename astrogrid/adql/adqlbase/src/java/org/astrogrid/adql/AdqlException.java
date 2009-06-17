/*$Id: AdqlException.java,v 1.2 2009/06/17 10:13:29 jl99 Exp $
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
 * AdqlException
 *
 *
 * @author Jeff Lusted jl99@star.le.ac.uk
 * Apr 17, 2007
 */
public class AdqlException extends Exception {
    
    private static Log log = LogFactory.getLog( AdqlException.class ) ;
    
    private Tracker tracker ;
    
    public AdqlException( Tracker tracker ) {
        this.tracker = tracker ;
    }
    
    public Tracker getTracker() {
        return this.tracker ;
    }
    

    public String[] getMessages() {  
        if( log.isTraceEnabled() ) { log.trace( "enter: getMessages()" ) ; } 
        ArrayList errors = tracker.getErrors() ;
        log.debug( "errors total: " + errors.size() ) ;
        
        //
        // JL Note: I've turned filtration off for the moment.
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
            //
            // At the moment I'm only putting out the message description.
            // No positional information at all.
            messages[i] = e.getDescription() ;
            
//            if( !messages[i].endsWith( " " ) ) {
//                messages[i]+= " " ;
//            }
//            messages[i]+= "at position " + e.toPosition() ;
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
//                return true ;
            }
        }      
        return false ;
    }
}


/*
$Log: AdqlException.java,v $
Revision 1.2  2009/06/17 10:13:29  jl99
Merge of adql v1 parser with maven 2 build.
Due to some necessary restructuring, the maven 1 build process has been removed.

Revision 1.1.2.1  2009/06/16 07:49:39  jl99
First commit of maven 2 build

Revision 1.3  2008/02/04 17:47:29  jl99
Merge of branch adql-jl-2504

Revision 1.2.2.2  2008/01/29 14:15:02  jl99
For the method getMessages(), messages are no longer (for the moment) filtered on position.
And only the message description is returned. No positional information, either textual or path-wise, and no extended details of what was expected.

Revision 1.2.2.1  2008/01/25 14:49:12  jl99
Turned off the filtering of error messages based on position, for the time being.

Revision 1.2  2007/06/06 18:19:27  jl99
Merge of branch adql-jl-2135

Revision 1.1.2.3  2007/04/19 11:49:50  jl99
Changes to public methods to accommodate multiple error recording.

Revision 1.1.2.2  2007/04/18 09:10:29  jl99
Rationalizing multiple error reporting

Revision 1.1.2.1  2007/04/17 15:45:29  jl99
Rationalizing multiple error reporting

*/