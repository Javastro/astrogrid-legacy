/*
 * @(#)JoinCondition.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.3, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.portal.workflow.intf;

/**
 * The <code>JoinCondition</code> class represents the condition
 * under which a Step will execute according to the outcome of
 * some previous Step 
 * <p>
 * There are only three fixed instances, representing...
 * <p><blockquote><pre>
 * (1) true - previous step must execute cleanly, 
 * (2) false - previous step must fail, and
 * (3) any - previous step can succeed or fail
 * </pre></blockquote><p>
 *
 * @author  Jeff Lusted
 * @version 1.0 04-Sep-2003
 * @see     
 * @see     
 * @since   AstroGrid 1.3
 * @modified replaced constants by static methods. this allows us to extract an interface, and substitute one constants implementation by another.
 */
public final class JoinCondition {
    
    private static final JoinCondition
        TRUE = new JoinCondition( "TRUE".intern() ) ;
          
    private static final JoinCondition
        FALSE = new JoinCondition( "FALSE".intern() ) ;
        
    private static final JoinCondition
        ANY = new JoinCondition( "ANY".intern() ) ;
    
    private String
        type ;
    
    private JoinCondition() {
    }
    
    private JoinCondition( String type ) {
        this.type = type ;
    }
    
    public boolean equals( JoinCondition joinCondition ) {
        return this == joinCondition ; 
    }
    
    public String toString() {
        return type.toLowerCase() ;
    }

    public  static  JoinCondition FALSE() {
        return FALSE;
    }

    public static  JoinCondition ANY() {
        return ANY;
    }

    public static JoinCondition TRUE() {
        return TRUE;
    }

} // end of class JoinCondition
