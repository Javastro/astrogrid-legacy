/*$Id: AdqlCompilerSV.java,v 1.1 2007/08/06 21:29:35 jl99 Exp $
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
import org.astrogrid.adql.metadata.Container;

/**
 * AdqlCompilerSV
 *
 *
 * @author Jeff Lusted jl99@star.le.ac.uk
 * Aug 6, 2007
 */
public class AdqlCompilerSV extends AdqlCompiler {
    
    public class Settings {
        String userDefinedFunctionPrefix ;
        int prettyPrintIndent ;
        Container metadata ;
    }
    
    protected PoolManager poolManager ;

    /**
     * @param stream
     */
    public AdqlCompilerSV( InputStream stream ) {
        super(stream);
    }

    /**
     * @param stream
     */
    public AdqlCompilerSV( Reader stream ) {
        super(stream);
    }
    
    protected void setSettings( Settings s ) {
        this.setDefaultUserDefinedFunctionPrefix( s.userDefinedFunctionPrefix ) ;
        this.setMetadata( s.metadata ) ;
        this.setPrettyPrintIndent( s.prettyPrintIndent ) ;
    }
     
    protected Settings getSettings() {
        Settings s = new Settings() ;
        s.metadata = this.getMetadata() ;
        s.userDefinedFunctionPrefix = this.getDefaultUserDefinedFunctionPrefix() ;
        s.prettyPrintIndent = this.getPrettyPrintIndent() ;
        return s ;
    }

    
    protected void setPoolManager( PoolManager poolManager ) {
        this.poolManager = poolManager ;
    }
    
    public void release() {
        this.poolManager.release( this ) ;
    }

}


/*
$Log: AdqlCompilerSV.java,v $
Revision 1.1  2007/08/06 21:29:35  jl99
First attempt at pooling the AdqlCompiler

*/