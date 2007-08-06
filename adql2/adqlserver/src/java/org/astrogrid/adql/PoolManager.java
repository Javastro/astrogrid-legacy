/*$Id: PoolManager.java,v 1.1 2007/08/06 21:29:35 jl99 Exp $
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.adql;

import java.io.InputStream;
import java.io.Reader;
import java.util.Stack ;


/**
 * PoolManager
 *
 *
 * @author Jeff Lusted jl99@star.le.ac.uk
 * Aug 6, 2007
 */
public class PoolManager {
    
    public final int MAX_COMPILERS = 16 ;
    
    public class PoolManagerException extends java.lang.Exception {

        /**
         * @param message
         */
        public PoolManagerException(String message) {
            super(message);
        }
      
    }
    
    private int maxCompilers ; 
    private Stack inCompilers ;
    private Stack outCompilers ;
    private boolean sealed = false ;
    
    public PoolManager() {
        this.inCompilers = new Stack() ;
        this.outCompilers = new Stack() ;
    }
    
    public synchronized void setMaxCompilers( int maxSetting ) throws PoolManagerException  {
        if( sealed ) {
            throw new PoolManagerException( "PoolManager is already in use." ) ;
        }
        else if( maxSetting > MAX_COMPILERS ) {
            throw new PoolManagerException( " Max compilers should lie between 1 and " + MAX_COMPILERS ) ;
        }
        else {
            this.maxCompilers = maxSetting ;
        }
    }
    
    public synchronized void setCompiler( AdqlCompilerSV compiler ) throws PoolManagerException {
        if( sealed ) {
            throw new PoolManagerException( "PoolManager is already in use." ) ;
        }
        else  {
            compiler.setPoolManager( this ) ;
            inCompilers.push( compiler ) ;
        }
    }
    
    public synchronized AdqlCompilerSV getCompiler( InputStream stream ) {
        AdqlCompilerSV c ;
        //
        // If the pool is empty, we need to create a new compiler
        // or wait until one is released...
        if( inCompilers.isEmpty() ) {
            //
            // OK here. Slack to create a new one...
            if( outCompilers.size() < maxCompilers ) {
                c = new AdqlCompilerSV( stream ) ;
                initCompilerSettings( c ) ;
            }
            else {
                //
                // We better wait for one to be released...
                while( outCompilers.size() == maxCompilers ) {
                    try {
                        this.wait() ;
                    }
                    catch( Exception ex ) {
                        ;
                    }
                }
                //
                // One at last...
                c = (AdqlCompilerSV)inCompilers.pop() ;
                c.ReInit( stream ) ;
            }           
        }
        else {
            //
            // No problems, get one from the pool...
            c = (AdqlCompilerSV)inCompilers.pop() ;
            c.ReInit( stream ) ;
        }
        // Make sure we register with the outCompilers...
        outCompilers.push( c ) ;
        this.sealed = true ;
        return c ;
    }
    
    public synchronized AdqlCompilerSV getCompiler( Reader reader  ) {
        AdqlCompilerSV c ;
        //
        // If the pool is empty, we need to create a new compiler
        // or wait until one is released...
        if( inCompilers.isEmpty() ) {
            //
            // OK here. Slack to create a new one...
            if( outCompilers.size() < maxCompilers ) {
                c = new AdqlCompilerSV( reader ) ;
                c.setPoolManager( this ) ;
                initCompilerSettings( c ) ;
            }
            else {
                //
                // We better wait for one to be released...
                while( outCompilers.size() == maxCompilers ) {
                    try {
                        this.wait() ;
                    }
                    catch( Exception ex ) {
                        ;
                    }
                }
                c = (AdqlCompilerSV)inCompilers.pop() ;
                c.ReInit( reader ) ;
            }           
        }
        else {
            //
            // No problems, get one from the pool...
            c = (AdqlCompilerSV)inCompilers.pop() ;
            c.ReInit( reader ) ;
        }
        outCompilers.push( c ) ;
        this.sealed = true ;
        return c ;
    }
    
    protected synchronized void release( AdqlCompilerSV compiler ) {
        outCompilers.remove( compiler ) ;
        inCompilers.push( compiler ) ;
        this.notify() ;
    }
    
    private void initCompilerSettings( AdqlCompilerSV compiler ) {
        compiler.setPoolManager( this ) ;
        compiler.setSettings( ((AdqlCompilerSV)outCompilers.peek()).getSettings() ) ;
    }
    
}


/*
$Log: PoolManager.java,v $
Revision 1.1  2007/08/06 21:29:35  jl99
First attempt at pooling the AdqlCompiler

*/