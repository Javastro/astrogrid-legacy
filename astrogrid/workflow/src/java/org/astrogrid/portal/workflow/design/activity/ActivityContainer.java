/*
 * @(#)ActivityContainer.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.3, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.portal.workflow.design.activity;

import java.util.LinkedList;

import org.astrogrid.portal.workflow.design.Flow;
import org.astrogrid.portal.workflow.design.Sequence;
import org.astrogrid.portal.workflow.design.Step;

/**
 * The <code>ActivityContainer</code> class represents... 
 * <p>
 *
 * <p>
 * The class... 
 * 
 *
 * @author  Jeff Lusted
 * @version 1.0 21-Aug-2003
 * @see     
 * @see     
 * @since   AstroGrid 1.3
 */
public abstract class ActivityContainer extends Activity {
    
    private LinkedList
        children = new LinkedList() ;
      
    public ActivityContainer() {
        super() ;
    }
    
    
    public synchronized Sequence createSequence() {
        return this.createSequence( children.size() ) ;
    }
    
    
    public synchronized Sequence createSequence( int index ) {
        return (Sequence)this.create( index, new Sequence() ) ;
    }
    
    
    public synchronized Flow createFlow() {
        return this.createFlow( children.size() ) ;
    }
    
    
    public synchronized Flow createFlow( int index ) {
        return (Flow)this.create( index, new Flow() ) ;
    }
    
    
    public synchronized Step createStep( int index ) {
        return (Step)this.create( index, new Step() ) ;        
    }
   
    
    private Activity create( int index, Activity activity ) {
        children.add( index, activity ) ;
        activity.setParent( this ) ;
        this.getWorkflow().putActivity( activity ) ;
        return activity ;                    
    }
    
    
    public boolean removeChild( Activity activity ) { 
        this.remove() ;     
        return children.remove( activity ) ;
    }


    public ActivityIterator getChildren() { return new ActivityIterator( children.listIterator() ) ; }
    
    
    public int getIndex( Activity activity ) {
        return children.indexOf( activity ) ; 
    }
    
    public void setIndex( int index, Activity activity ) {
        this.children.remove( activity ) ;
        this.children.add( index, activity ) ;
    }
    
    public String toXMLString() {
        return null ;
    }
    
} // end of class ActivityContainer