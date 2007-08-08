/*$Id: AdqlCompilerSV.java,v 1.5 2007/08/08 09:31:04 jl99 Exp $
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.adql;

import java.io.Reader;
import java.io.StringWriter;
import java.util.Stack;
import java.io.StringReader;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException ;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import org.apache.xmlbeans.XmlObject ;
import org.astrogrid.adql.beans.SelectDocument;
import org.astrogrid.adql.metadata.Container;

/**
 * AdqlCompilerSV
 *
 *
 * @author Jeff Lusted jl99@star.le.ac.uk
 * Aug 6, 2007
 */
public class AdqlCompilerSV {
    
    private static Log log = LogFactory.getLog( AdqlCompilerSV.class ) ;
    
    public static final int ABSOLUTE_MAX_COMPILERS = 16 ;
    public static final int DEFAULT_MAX_COMPILERS = 2 ;
    
    public interface CallBack {       
        public XmlObject exec( XmlObject xo ) ;       
    }
    
    public class InvalidStateException extends Exception {

        /**
         * @param message
         * @param cause
         */
        public InvalidStateException(String message, Throwable cause) {
            super(message, cause);
        }

        /**
         * @param message
         */
        public InvalidStateException(String message) {
            super(message);
        }

    }
    
    private class CompilerUnit {      
        AdqlCompiler compiler ;
        Transformer sqlTransformer ;
        
        CompilerUnit( AdqlCompiler compiler, Transformer transformer ) {
            this.compiler = compiler ;
            this.sqlTransformer = transformer ;
        }
    }
    
    private int maxCompilers = DEFAULT_MAX_COMPILERS ; 
    private Stack inCompilers = new Stack() ;
    private Stack outCompilers = new Stack() ;
    private boolean sealed = false ;

    private TransformerFactory transformerFactory = TransformerFactory.newInstance() ;
    private String styleSheet ;
    private Transformer firstCandidate ;
    private String userDefinedFunctionPrefix ;
    private Container metadata ; 
    private CallBack midpointCallBack ;
    
    /**
     * @param stream
     */
    public AdqlCompilerSV() {}
    
    public String compileToSQL( String adqls ) throws AdqlException
                                                    , InvalidStateException
                                                    , TransformerException { 
        CompilerUnit cu = null ;
        try {
            //
            // First compile from ADQL/s to ADQL/x, returning an XML object...
            StringReader reader = new StringReader( adqls ) ;
            cu = getCompiler( reader ) ;  
            SelectDocument selectDoc = cu.compiler.compileToXmlBeans() ;
            //
            // If a callback has been registered, invoke it here
            // so the select document bean can be used to manipulate
            // whatever the user wants to manipulate...
            if( midpointCallBack != null ) {
                selectDoc = (SelectDocument)midpointCallBack.exec( selectDoc ) ;
            }
            //
            // Then get the resulting XML and transform it to SQL variant...
            String adqlx = selectDoc.xmlText() ;       
            StreamSource source = new StreamSource( new StringReader( adqlx ) ) ;
            StreamResult result = new StreamResult( new StringWriter() ) ;      
            cu.sqlTransformer.transform( source, result ) ;      
            return ((StringWriter)result.getWriter()).toString() ;
        }
        finally {
            if( cu != null ) {
                synchronized( this ) {
                    outCompilers.remove( cu ) ;
                    inCompilers.push( cu ) ;
                    this.notify() ;
                }   
            }                               
        }       
    }
    
    protected synchronized boolean setuserDefinedFunctionPrefix( String prefix ) {
        if( this.sealed == true )
            return false ;
        this.userDefinedFunctionPrefix = prefix ;
        return true ;
    }
    
    protected synchronized boolean setMetadata( Container metadata ) {
        if( this.sealed == true )
            return false ;
        this.metadata = metadata ;
        return true ;
    }
    
    protected synchronized boolean setStyleSheet( String styleSheet ) 
                                throws TransformerConfigurationException {
        if( this.sealed == true )
            return false ;
        StreamSource source = new StreamSource( new StringReader( styleSheet )  ) ;
        this.firstCandidate = this.transformerFactory.newTransformer( source ) ;       
        this.styleSheet = styleSheet ;
        return true ;
    }
    
    protected synchronized boolean setMaxCompilers( int max ) {
        if( this.sealed == true )
            return false ;
        if( max > ABSOLUTE_MAX_COMPILERS ) {
            this.maxCompilers = ABSOLUTE_MAX_COMPILERS ;
        }
        else if( max < 1 ){
            this.maxCompilers = 1 ;           
        }
        else {
            this.maxCompilers = max ;
        }
        return true ;
    }
    
    protected synchronized boolean registerMidPointCallBack( CallBack mpcb ) {
        if( this.sealed == true )
            return false ;
        this.midpointCallBack = mpcb ;
        return true ;
    }
  
    private synchronized CompilerUnit getCompiler( Reader reader ) throws InvalidStateException {
        if( this.sealed == false ) {
            this.sealed = true ;
        }
        CompilerUnit cu ;
        //
        // If the pool is empty, we need to create a new compiler
        // or wait until one is released...
        if( inCompilers.isEmpty() ) {
            //
            // Slack to create a new one? ...
            if( outCompilers.size() < maxCompilers ) {
                //
                // Yes, indeed!...
                AdqlCompiler c = new AdqlCompiler( reader ) ;
                initCompilerSettings( c ) ;               
                if( this.firstCandidate != null ) {
                    cu = new CompilerUnit( c, firstCandidate ) ;
                    firstCandidate = null ;
                }
                else {
                    try {
                        Source s = new StreamSource( new StringReader( this.styleSheet ) ) ;
                        Transformer t = transformerFactory.newTransformer( s ) ;
                        cu = new CompilerUnit( c, t ) ;
                    }
                    catch ( Throwable th ) {
                        log.error( "Failed to instantiate Transformer", th ) ;
                        throw new InvalidStateException( "Failed to instantiate Transformer", th ) ;
                    }
                }               
            }
            else {
                //
                // No! We better wait for one to be released...
                while( inCompilers.isEmpty() ) {
                    try {
                        log.debug( Thread.currentThread().getName() + " waiting for compiler..." ) ;
                        this.wait() ;
                    }
                    catch( Exception ex ) {
                        ;
                    }
                }
                //
                // One at last...
                cu = (CompilerUnit)inCompilers.pop() ;
                cu.compiler.ReInit( reader ) ;
            }           
        }
        else {
            //
            // No problems, get one from the pool...
            cu = (CompilerUnit)inCompilers.pop() ;
            cu.compiler.ReInit( reader ) ;
        }
        // Make sure we register with the outCompilers...
        outCompilers.push( cu ) ;
        this.sealed = true ;
        if( log.isDebugEnabled() ) {
            log.debug( "outCompilers: [" + outCompilers.size() 
                     + "] inCompilers: [" + inCompilers.size() + "]" ) ;
        }
        return cu ;
    }
    
    private void initCompilerSettings( AdqlCompiler compiler ) {
        if( this.userDefinedFunctionPrefix != null ) {
            compiler.setDefaultUserDefinedFunctionPrefix( this.userDefinedFunctionPrefix ) ;
        }
        if( this.metadata != null ) {   
            compiler.setMetadata( this.metadata ) ;
        }
        
    }

}


/*
$Log: AdqlCompilerSV.java,v $
Revision 1.5  2007/08/08 09:31:04  jl99
Debugging test environment for AdqlCompilerSV

Revision 1.4  2007/08/07 17:37:05  jl99
Initial multi-threaded test environment for AdqlCompilerSV

Revision 1.3  2007/08/07 11:10:05  jl99
Ensured compiler unit is returned to pool under all circumstances.

Revision 1.2  2007/08/07 11:04:50  jl99
Reduced to the one class AdqlCompilerSV with some internal classes of its own.

Revision 1.1  2007/08/06 21:29:35  jl99
First attempt at pooling the AdqlCompiler

*/