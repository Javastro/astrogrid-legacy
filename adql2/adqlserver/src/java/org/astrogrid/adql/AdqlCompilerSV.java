/*$Id: AdqlCompilerSV.java,v 1.6 2007/08/08 16:59:48 jl99 Exp $
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
 * The <code>AdqlCompilerSV</code> class is a server side version of 
 * <code>AdqlCompiler</code>. It differs from <code>AdqlCompiler</code>
 * in a number of important respects:
 * <p><blockquote><pre>
 *   (1) It processes ADQL right the way through to SQL.
 *   (2) It is suitable for a multi-threading environment.
 * </pre></blockquote><p>
 * In starting from ADQL/s and progressing to SQL, there are a number of
 * phases. The original ADQL/s is parsed into instances of XMLBeans; if a callback
 * has been registered, the resulting SelectDocument is passed to the callback for
 * whatever processing the user thinks is fit, before emitting
 * XML and transforming that with an appropriate XSLT style sheet into a 
 * variant of SQL. The style sheet must be provided by the user.
 * <p><p>
 * The underlying compiler is not thread safe, but is serially reusable.
 * <code>AdqlCompilerSV</code> wraps a pool of compilers to provided
 * a thread safety mechanism. The minimum size of the pool is 1 and the
 * maximum size can be set to an upper limit of 16, with a default setting
 * of 2.
 *
 * @author Jeff Lusted jl99@star.le.ac.uk
 * Aug 6, 2007
 */
public class AdqlCompilerSV {
    
    private static Log log = LogFactory.getLog( AdqlCompilerSV.class ) ;
    
    /**
     * The maximum number of compiler instances allowed.
     */
    public static final int ABSOLUTE_MAX_COMPILERS = 16 ;
    /**
     * The default maximum number of compiler instances allowed.
     */
    public static final int DEFAULT_MAX_COMPILERS = 2 ;
    
    /**
     * CallBack
     *
     *
     * @author Jeff Lusted jl99@star.le.ac.uk
     * Aug 8, 2007
     */
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
     * The only constructor.
     * <p> 
     * All state is established by setters.
     * In order to guarantee thread safety, no alteration of the
     * internal state of <code>AdqlCompilerSV</code> is allowed
     * once compilation gets underway. That is: the first compilation
     * will seal the compiler against any further change of settings.
     * 
     * @see org.astrogrid.adql.AdqlCompilerSV#registerMidPointCallBack(CallBack)
     * @see org.astrogrid.adql.AdqlCompilerSV#setMaxCompilers(int)
     * @see org.astrogrid.adql.AdqlCompilerSV#setMetadata(Container)
     * @see org.astrogrid.adql.AdqlCompilerSV#setStyleSheet(String)
     * @see org.astrogrid.adql.AdqlCompilerSV#setUserDefinedFunctionPrefix(String)
     */
    public AdqlCompilerSV() {}
    
    /**
     * Compiles an ADQL query in string format to a specific
     * variant of SQL. The input is a full query, starting
     * with a SELECT statement, and not simply a fragment of a query. 
     * For example:
     * <p>
     * Select * from first as f ;
     * <p><p>
     * 
     * (1) Retrieves a compiler from the compiler pool.<p>
     * (2) Compiles the source to XmlBeans.<p>
     * (3) Invokes the user's mid-point call back (if one was registered).<p>
     * (4) Emits XML from the SelectDocument bean.<p>
     * (5) Transforms the XML into SQL variant using style sheet.<p>
     * <p>
     * @param adqls - An ADQL query in string format
     * @return A specific SQL version of the query
     * @throws AdqlException 
     *         if ADQL input fails compilation.
     * @throws InvalidStateException 
     *         if there is an invalid state, for example: an instance of
     *         the transformer corresponding to the XSLT style sheet cannot be formed.
     * @throws TransformerException
     *         if there is a problem with the XSLT style sheet.
     */
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
    
    /**
     * Sets the prefix to be used for checking for user defined functions.
     * Can be any string of characters provided the prefix begins with an
     * letter. But check ADQL syntax if in doubt. If not set, assumes "_udf".
     * The setter - if used - must be before compilations
     * begin, otherwise it will be ignored.
     * 
     * @param prefix
     * @return boolean indicating whether setting was a success.
     */
    public synchronized boolean setUserDefinedFunctionPrefix( String prefix ) {
        if( this.sealed == true )
            return false ;
        this.userDefinedFunctionPrefix = prefix ;
        return true ;
    }
    
    /**
     * Sets any metadata that the user wishes the compiler to take
     * account of when compiling. If none, is provided, none will
     * be used. 
     * The setter - if used - must be before compilations
     * begin, otherwise it will be ignored.
     * 
     * @param metadata
     * @return boolean indicating whether setting was a success.
     */
    public synchronized boolean setMetadata( Container metadata ) {
        if( this.sealed == true )
            return false ;
        this.metadata = metadata ;
        return true ;
    }
    
    /**
     * Sets the XSLT style sheet to be used in the transformation from 
     * ADQL/x to an SQL variant. All the compilations managed by
     * <code>AdqlCompilerSV</code> will use this style sheet.
     * The setter - if used - must be before compilations
     * begin, otherwise it will be ignored.
     * 
     * @param styleSheet in String format.
     * @return boolean indicating whether setting was a success.
     * @throws TransformerConfigurationException
     */
    public synchronized boolean setStyleSheet( String styleSheet ) 
                                throws TransformerConfigurationException {
        if( this.sealed == true )
            return false ;
        StreamSource source = new StreamSource( new StringReader( styleSheet )  ) ;
        this.firstCandidate = this.transformerFactory.newTransformer( source ) ;       
        this.styleSheet = styleSheet ;
        return true ;
    }
    
    /**
     * Sets the maximum number of compiler instances that
     * <code>AdqlCompilerSV</code> is allowed to instantiate.
     * The setter - if used - must be before compilations
     * begin, otherwise it will be ignored. The setting must 
     * be beween 1 and 16 inclusive. If the setter is not used, 
     * the maximum setting of 2 is assumed. 
     * 
     * @param max - Ceiling for the number of compilers allowed.
     * @return boolean indicating whether setting was a success.
     */
    public synchronized boolean setMaxCompilers( int max ) {
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
    
    /**
     * Registers a user's call back routine, which will be invoked once
     * the ADQL/s source has been compiled into XMLBeans. A call back
     * must be registered before compilations begin.
     * 
     * @param mpcb - the CallBack interface
     * @return boolean indicating whether the registration was a success.
     * 
     * @see  org.astrogrid.adql.AdqlCompilerSV#CallBack
     */
    public synchronized boolean registerMidPointCallBack( CallBack mpcb ) {
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
Revision 1.6  2007/08/08 16:59:48  jl99
Some docs

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