/*
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.adql;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.adql.AdqlParser.SyntaxOption;
import org.astrogrid.adql.beans.SelectDocument;

/**
 * The <code>AdqlParserSVNC</code> class is a server side wrapper of 
 * <code>AdqlParser</code>. It differs from <code>AdqlParser</code>
 * in a number of important respects:
 * <p><blockquote><pre>
 *   (1) It can be used to process ADQL right the way through to SQL.
 *   (2) It is suitable for a multi-threading environment and maintains
 *   a pool of artifacts to improve performance.
 * </pre></blockquote><p><p>
 * 
 * In starting from ADQL/s and progressing to SQL, there are a number of
 * phases. 
 * <p><blockquote><pre>
 *   (1) The original ADQL/s is parsed into instances of XMLBeans.
 *   (2) The XMLBeans can then be transformed with an appropriate XSLT style sheet 
 *   into a variant of SQL. The style sheet must be provided at initialization.
 *   Working style sheets are included with the jar file.
 * </pre></blockquote><p><p>
 * These two phases can be combined into one or invoked separately.
 * <p><p>
 * There is also a pooled converter that can be invoked on ADQL versions 1.0 and earlier
 * to convert queries into ADQL version 2.0
 * <p><p>
 * The underlying software artifacts are not thread safe, but are serially reusable.
 * <code>AdqlParserSVNC</code> wraps a pool of provide a thread safety mechanism. 
 * The minimum size of the pool is 1 and the maximum size can be set up to a limit of 16, 
 * with a default setting of 2. The <strong>SVNC</strong> appellation means
 * "Server Version No Callbacks". 
 * For a version using a callback strategy, see <code>AdqlParserSV</code>
 * 
 * @see org.astrogrid.adql.IAdqlParser
 * @see org.astrogrid.adql.AdqlParserSV
 * @see org.astrogrid.adql.AdqlConverter
 * 
 * @author Jeff Lusted jl99@star.le.ac.uk
 * Aug 6, 2007
 */
public class AdqlParserSVNC implements IAdqlParser {

	private static Log log = LogFactory.getLog( AdqlParserSVNC.class ) ;
        
    /**
     * The maximum number of parser instances allowed.
     */
    public static final int ABSOLUTE_MAX_PARSERS = 16 ;
    /**
     * The default maximum number of parser instances allowed.
     */
    public static final int DEFAULT_MAX_PARSERS = 2 ;
    
    private int maxParsers = DEFAULT_MAX_PARSERS ; 
    
    private ArrayList<AdqlParser> parsers = new ArrayList<AdqlParser>();
    private ArrayList<AdqlConverter> converters = new ArrayList<AdqlConverter>() ;
    private ArrayList<Transformer> transformers = new ArrayList<Transformer>();
    
    private int parserCount = 0 ;
    private int converterCount = 0 ;
    private int transformerCount = 0 ;
    
    private boolean sealed = false ;
    private AdqlParser.SyntaxOption[] options = null ;
    private String userDefinedFunctionPrefix = null ;
    private TransformerFactory transformerFactory = TransformerFactory.newInstance() ;
    private String styleSheet ;
     
    /**
     * The only constructor.
     * <p> 
     * All state is established by setters.
     * In order to guarantee thread safety, no alteration of the
     * internal state of <code>AdqlParserSVNC</code> is allowed
     * once parsing gets underway. That is: the first parse, transform or convert
     * will seal the parser against any further change of settings.
     * 
     * @see org.astrogrid.adql.AdqlParserSVNC#setMaxParsers(int)
     * @see org.astrogrid.adql.AdqlParserSVNC#setStrictSyntax(SyntaxOption[])
     * @see org.astrogrid.adql.AdqlParserSVNC#setUserDefinedFunctionPrefix(String)
     * @see org.astrogrid.adql.AdqlParserSVNC#setStyleSheet(InputStream)
     * @see org.astrogrid.adql.AdqlParserSVNC#setStyleSheet(String)
     */
    public AdqlParserSVNC() {}
    
    /**
	 * @see org.astrogrid.adql.IAdqlParser#parseToXML(java.lang.String)
	 */
    public SelectDocument parseToXML( String adqls ) throws AdqlException, InvalidStateException { 
    	AdqlParser p = null ;
    	try {
    		//
    		// Parse from ADQL/s to ADQL/x, returning an XML object...
    		p = getParser( new StringReader( adqls ) ) ;  
    		return p.parseToXmlBeans() ;
    	}
    	finally {
    		//
    		// Whatever happens, the parser unit instance must be returned
    		// to the pool in a thread-safe manner...
    		if( p != null ) {
    			synchronized( this ) {
    				parsers.add( p ) ;
    				this.notify() ;
    			}   
    		}                               
    	}       
    }
    
    /**
	 * @see org.astrogrid.adql.IAdqlParser#transformToSQL(org.astrogrid.adql.beans.SelectDocument)
	 */
    public String transformToSQL( SelectDocument selectDoc ) throws InvalidStateException, TransformerException { 
    	Transformer t = null ;
    	try {
    		t = getTransformer() ;    		
    		//
    		// Then get the resulting XML and transform it to SQL variant...
    		String adqlx = selectDoc.xmlText() ;       
    		StreamSource source = new StreamSource( new StringReader( adqlx ) ) ;
    		StreamResult result = new StreamResult( new StringWriter() ) ;      
    		t.transform( source, result ) ;  
    		//
    		// Return the final SQL variant...
    		return ((StringWriter)result.getWriter()).toString() ;
    	}
    	finally {
    		//
    		// Whatever happens, the transformer instance must be returned
    		// to the pool in a thread-safe manner...
    		if( t != null ) {
    			synchronized( this ) {				
    				transformers.add( t ) ;
    				this.notify() ;
    			}   
    		}                               
    	}       
    } 
    
    /**
     * @see org.astrogrid.adql.IAdqlParser#parseToSQL(java.lang.String)
     */
    public String parseToSQL(String adqls) 
    	throws AdqlException, InvalidStateException, TransformerException {
    	return transformToSQL( parseToXML(adqls) ) ;
    }
    
    /**
	 * @see org.astrogrid.adql.IAdqlParser#convert(java.lang.String)
	 */
    public String convert( String adqlString ) throws InvalidStateException, TransformerException {
    	AdqlConverter c = null ;
    	try {
    		c = getConverter() ;
    		return c.convertToV20( adqlString ) ;
    	}
    	finally {
    		//
    		// Whatever happens, the converter instance must be returned
    		// to the pool in a thread-safe manner...
    		if( c != null ) {
    			synchronized( this ) {				
    				converters.add( c ) ;
    				this.notify() ;
    			}   
    		}                               
    	}       
    }
    
    /**
     * Sets the XSLT style sheet to be used in the transformation from 
     * ADQL/x to an SQL variant. All the parsings managed by
     * <code>AdqlParserSVNC</code> will use this style sheet.
     * The setter - if used - must be before any parsing is undertaken,
     * otherwise it will be ignored.
     * 
     * @param styleSheet in String format.
     * @return boolean indicating whether setting was a success.
     * @throws TransformerException
     */
    public synchronized boolean setStyleSheet( String styleSheet ) throws TransformerException {
    	if( this.sealed == true )
    		return false ;
    	StreamSource source = new StreamSource( new StringReader( styleSheet )  ) ;
    	this.transformers.add( transformerFactory.newTransformer( source ) ) ;       
    	this.styleSheet = styleSheet ;
    	return true ;
    }
    
    /**
     * Sets the XSLT style sheet to be used in the transformation from 
     * ADQL/x to an SQL variant. All the parsings managed by
     * <code>AdqlParserSVNC</code> will use this style sheet.
     * The setter - if used - must be before any parsing is undertaken,
     * otherwise it will be ignored.
     * 
     * @param isStyleSheet as an InputStream.
     * @return boolean indicating whether setting was a success.
     * @throws TransformerException
     */
    public synchronized boolean setStyleSheet( InputStream isStyleSheet ) throws TransformerException {
    	if( this.sealed == true )
    		return false ;
    	try {
    		this.styleSheet = getStyleSheet( isStyleSheet ) ;
    	}
    	catch( IOException iox ) {
    		throw new TransformerException( iox ) ;
    	}
    	return true ;  	
    }
    
    private String getStyleSheet( InputStream is ) throws IOException {
    	BufferedInputStream bis = new BufferedInputStream( is, 8192 ) ;
    	StringBuilder buffer =  new StringBuilder( 20480 ) ;
    	int c = bis.read() ;
    	while( c != -1 ) {
    		buffer.append( (char)c ) ;
    		c = bis.read() ;
    	}
    	String retVal = buffer.toString() ;
    	return retVal ;
  }
    
    
    /**
     * Sets the maximum number of parser instances that
     * <code>AdqlParserSVNC</code> is allowed to instantiate.
     * The setter - if used - must be before any parsing
     * begin, otherwise it will be ignored. The setting must 
     * be beween 1 and 16 inclusive. If the setter is not used, 
     * a maximum setting of 2 is assumed. 
     * 
     * @param max - Ceiling for the number of parsers allowed.
     * @return boolean indicating whether setting was a success.
     */
    public synchronized boolean setMaxParsers( int max ) {
    	if( this.sealed == true )
    		return false ;
    	if( max > ABSOLUTE_MAX_PARSERS ) {
    		this.maxParsers = ABSOLUTE_MAX_PARSERS ;
    	}
    	else if( max < 1 ){
    		this.maxParsers = 1 ;           
    	}
    	else {
    		this.maxParsers = max ;
    	}
    	return true ;       
    }
    
    /**
     * Sets a strict ADQL syntax level for this parser to obey.
     * Strict means overwriting any other previous options that
     * may have been set individually.
     * 
     * @param opts an array of syntax options
     * @return boolean indicating whether setting was a success.
     */  
    public synchronized boolean setStrictSyntax( final AdqlParser.SyntaxOption[] opts ) {
    	if( this.sealed == true )
    		return false ;
    	this.options = new AdqlParser.SyntaxOption[ opts.length ] ;
    	System.arraycopy( opts, 0, this.options, 0, opts.length ) ;    	
    	return true ;
    }
    
    /**
     * Sets the user defined function prefix. User defined functions can be forced to begin with 
     * a known prefix, eg fPhotoFlags( String astring ), where the prefix is "f". This method 
     * sets that prefix for syntactical checking of user defined functions. If not set, the prefix
     * defaults to "udf_".
     * 
     * @param prefix the defaultUserDefinedFunctionPrefix to set
     * @return boolean indicating whether setting was a success.
     */
    public synchronized boolean setUserDefinedFunctionPrefix( String prefix ) {
    	if( this.sealed == true )
    		return false ;
        this.userDefinedFunctionPrefix = prefix.trim() ;
        return true ;
    }
    
    /*
     * A critical method of AdqlParserSVNC in that it manages the controlled removal
     * of a parser instance for use from a pool of instances. It manages instantiating
     * a new parser instance when the pool is empty AND the maximum setting has not been
     * reached. If the pool is empty and the maximum setting has been reached, then
     * a wait is imposed until an instance is returned to the pool.
     * 
     * @param reader: the query to be parsed
     * @return a primed AdqlParser instance 
     */
    private AdqlParser getParser( Reader reader ) throws InvalidStateException {  
    	AdqlParser p = null ;
    	
    	synchronized( this ) {
    		//
            // If not yet sealed, the first invocation will seal against any 
            // further changes to settings by a user...
        	if( this.sealed == false ) {
        		this.sealed = true ;
        	}
            //
            // If the pool is empty, we need to create a new parser
            // or wait until one is released...
            if( parsers.isEmpty() ) {
                //
                // Slack to create a new one? ...
                if( parserCount < maxParsers ) {
                    //
                    // Yes, indeed!...            	
                    parserCount++ ;                        
                }
                else {
                    //
                    // No! We better wait for one to be released...
                    while( parsers.isEmpty() ) {
                        try {
                            log.debug( Thread.currentThread().getName() + " waiting for parser..." ) ;
                            this.wait() ;
                        }
                        catch( Exception ex ) {
                            ;
                        }
                    }
                    //
                    // One at last...
                    p = parsers.remove( 0 ) ;
                }           
            }
            else {
                //
                // No problems, get one from the pool...
            	p = parsers.remove( 0 ) ;
            }
            
    	} // end of synchronized block
    	
    	//
    	// Any creation of new parsers takes place outside of synchronization ...
    	if( p == null) {
    		 p = new AdqlParser( reader ) ;
             initParserSettings( p ) ;      
    	}
    	else {
    	 	p.ReInit( reader ) ;
    	}
        return p ;
    }
    
    /*
     * A critical method of AdqlParserSVNC in that it manages the controlled removal
     * of a parser instance for use from a pool of instances. It manages instantiating
     * a new parser instance when the pool is empty AND the maximum setting has not been
     * reached. If the pool is empty and the maximum setting has been reached, then
     * a wait is imposed until an instance is returned to the pool.
     * 
     * For each parser instance in the pool, an associated transformer is also instantiated.
     * Neither parsers nor transformers are thread safe, though they are serially reusable.
     */
    private AdqlConverter getConverter() throws InvalidStateException {        
    	AdqlConverter c = null ;
    	
    	synchronized( this ) {
        	//
            // If not yet sealed, the first invocation will seal against any 
            // further changes to settings by a user...
        	if( this.sealed == false ) {
        		this.sealed = true ;
        	}
            //
            // If the pool is empty, we need to create a new parser
            // or wait until one is released...
            if( converters.isEmpty() ) {
                //
                // Slack to create a new one? ...
                if( converterCount < maxParsers ) {
                    //
                    // Yes, indeed!...            	
                    converterCount++ ;                          
                }
                else {
                    //
                    // No! We better wait for one to be released...
                    while( converters.isEmpty() ) {
                        try {
                            log.debug( Thread.currentThread().getName() + " waiting for converter..." ) ;
                            this.wait() ;
                        }
                        catch( Exception ex ) {
                            ;
                        }
                    }
                    //
                    // One at last...
                    c = converters.remove( 0 ) ;
                }           
            }
            else {
                //
                // No problems, get one from the pool...
            	c = converters.remove( 0 ) ;
            }
    	} // end of synchronized block
    	
    	//
    	// Any required creation of new converters takes place outside of synchronization ...
    	if( c == null ) {
            c = new AdqlConverter() ;
    	}
        return c ;
    }
    
    private Transformer getTransformer() throws InvalidStateException {
    	Transformer t = null ;
    	
    	synchronized( this ){
    		//
            // If not yet sealed, the first invocation will seal against any 
            // further changes to settings by a user...
        	if( this.sealed == false ) {
        		this.sealed = true ;
        	}
            //
            // If the pool is empty, we need to create a new transformer
            // or wait until one is released...
        	
            if( transformers.isEmpty() ) {
                //
                // Slack to create a new one? ...
                if( transformerCount < maxParsers ) {
                    //
                    // Yes, indeed!...   
                    transformerCount++ ;             
                }
                else {
                    //
                    // No! We better wait for one to be released...
                    while( transformers.isEmpty() ) {
                        try {
                            if( log.isDebugEnabled() ) 
                            	log.debug( Thread.currentThread().getName() + " waiting for transformer..." ) ;
                            this.wait() ;
                        }
                        catch( Exception ex ) {
                            ;
                        }
                    }
                    //
                    // One at last...
                    t = transformers.remove(0) ;
                }           
            }
            else {
                //
                // No problems, get one from the pool...
                t = transformers.remove(0) ;
            }
    	} // end of synchronized block
    	
    	//
    	// Any construction of new transformers takes place outside of synchronization...
    	if( t == null ) {
    		
    		try {
                t = buildTransformer() ;
            }
            catch ( Throwable th ) {
            	// Failure, so we decrement the created count...
            	synchronized( this ) {
            		transformerCount-- ;
            	}
                log.error( "Failed to instantiate Transformer", th ) ;
                throw new InvalidStateException( "Failed to instantiate Transformer", th ) ;
            } 
    
    	}
        
        return t ;
    }
    
    private Transformer buildTransformer() throws TransformerException {
    	 Source s = new StreamSource( new StringReader( this.styleSheet ) ) ;
         Transformer t = transformerFactory.newTransformer( s ) ;
         return t ;
    }
       
    private void initParserSettings( AdqlParser parser ) {
    	if( this.options != null ) {
    		if( this.options.length > 0 ) {
    			parser.setStrictSyntax( this.options ) ;
    		}
    	}
    	if( this.userDefinedFunctionPrefix != null ) {
    		parser.setUserDefinedFunctionPrefix( this.userDefinedFunctionPrefix ) ;
    	}
    } 
  
    /**
     * Pool settings can only be initialized before use.
     * 
     * @return Whether the pool has been sealed against further change.
     */
    public synchronized boolean isSealed() {
    	// Not sure the synchronization is necessary
    	return this.sealed ;
    }
    
    /**
     * @return The fully qualified class name of the Transformer factory used.
     */

    public String getTransformerUsed() {
    	return transformerFactory.getClass().getName() ;
    }

}