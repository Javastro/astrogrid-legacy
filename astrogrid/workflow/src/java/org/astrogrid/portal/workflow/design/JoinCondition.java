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

package org.astrogrid.portal.workflow.design;

/**
 * The <code>JoinCondition</code> class represents... 
 * <p>
 *
 * <p>
 * The class... 
 * 
 *
 * @author  Jeff Lusted
 * @version 1.0 04-Sep-2003
 * @see     
 * @see     
 * @since   AstroGrid 1.3
 */
public class JoinCondition {
    
    public static final JoinCondition
        TRUE = new JoinCondition( "TRUE".intern() ),
        FALSE = new JoinCondition( "FALSE".intern() ),
        ANY = new JoinCondition( "ANY".intern() );
    
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

} // end of class JoinCondition
