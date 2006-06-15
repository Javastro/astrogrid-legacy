/* Command.java
 * Created on 15-Mar-2006
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.adqlEditor.commands;

/**
 * @author jl99
 *
 */
public interface CommandExec {
    
    public static final Result OK = new Result( 0 ) ;
    public static final Result WARNING = new Result( 1 ) ;
    public static final Result ERROR = new Result( 2 ) ;
    public static final Result FAILED = new Result( 3 ) ;
    
    public Result execute() ;
    
    public String[] getMessages() ;
    
    public static class Result {       
        private int code ;
        private Result( int code ) {
            this.code = code ;
        } 
        public int intValue() { 
            return code ;
        }
    }

}
