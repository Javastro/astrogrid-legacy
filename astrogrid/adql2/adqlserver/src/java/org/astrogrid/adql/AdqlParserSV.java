/*$Id: AdqlParserSV.java,v 1.3 2010/08/25 11:04:43 jl99 Exp $
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
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlbeans.XmlObject;
import org.astrogrid.adql.beans.SelectDocument;

/**
 * The <code>AdqlParserSV</code> class is a server side wrapper of 
 * <code>AdqlParser</code>. It differs from <code>AdqlParser</code>
 * in a number of important respects:
 * <p><blockquote><pre>
 *   (1) It can process ADQL right the way through to SQL.
 *   (2) It is suitable for a multi-threading environment.
 * </pre></blockquote><p>
 * In starting from ADQL/s and progressing to SQL, there are a number of
 * phases. The original ADQL/s is parsed into instances of XMLBeans; if a callback
 * has been registered, the resulting SelectDocument is passed to the callback for
 * whatever processing the user thinks is fit to do, before emitting
 * XML and transforming that with an appropriate XSLT style sheet into a 
 * variant of SQL. The style sheet must be provided by the user.
 * <p><p>
 * The underlying parser is not thread safe, but is serially reusable.
 * <code>AdqlParserSV</code> wraps a pool of parsers to provided
 * a thread safety mechanism. The minimum size of the pool is 1 and the
 * maximum size can be set up to a limit of 16, with a default setting
 * of 2.
 * <p><p>
 * <code>AdqlParseSv</code> uses callback strategies (a) to initialize each
 * individual parser maintained in the parser pool and (b) to enable manipulation
 * of a query after parsing it into XML but before translation into SQL.  
 *
 * @see org.astrogrid.adql.IAdqlParser
 * @see org.astrogrid.adql.AdqlParserSVNC
 * @see org.astrogrid.adql.AdqlConverter
 *
 * @author Jeff Lusted jl99@star.le.ac.uk
 * Aug 6, 2007
 */
public class AdqlParserSV implements IAdqlParser {

	private static Log log = LogFactory.getLog( AdqlParserSV.class ) ;
    
    /**
     * The maximum number of parser instances allowed.
     */
    public static final int ABSOLUTE_MAX_PARSERS = 16 ;
    /**
     * The default maximum number of parser instances allowed.
     */
    public static final int DEFAULT_MAX_PARSERS = 2 ;
    
    /**
     * User callback interface used to initialize each individual parser created.
     *
     * @see org.astrogrid.adql.AdqlParser
     * @author Jeff Lusted jl99@star.le.ac.uk
     * Aug 8, 2007
     */
    public interface ParserInitCallBack {      
        /**
         * @param AdqlParser that requires initializing
         */
        public void execInit( AdqlParser ap ) ;        
    }
    
    /**
     * User callback interface used to manipulate an ADQL query after it has been
     * parsed into XML but before it is translated into SQL. 
     * XmlBeans are used for passing the data and it is recommended that 
     * the XmlBeans facilities are used to manipulate the query.
     * <p>
     * NB: This call back is only used if the method <code>parseToSQL</code> is invoked.
     *
     * @author Jeff Lusted jl99@star.le.ac.uk
     * Aug 8, 2007
     */
    public interface PostParseCallBack {        
        /**
         * @param An ADQL query in XML format.
         * @return The same query tranformed in some fasion.
         */
        public XmlObject execMidPoint( XmlObject xo ) ;        
    }
    
    private class ParserUnit {      
        AdqlParser parser ;
        Transformer sqlTransformer ;
        
        ParserUnit( AdqlParser parser, Transformer transformer ) {
            this.parser = parser ;
            this.sqlTransformer = transformer ;
        }
    }
    
    private int maxParsers = DEFAULT_MAX_PARSERS ; 
    private ArrayList<ParserUnit> parsers = new ArrayList<ParserUnit>();
    private ArrayList<AdqlConverter> converters = new ArrayList<AdqlConverter>() ;
    
    private int parserCount = 0 ;
    private int converterCount = 0 ;
    private boolean sealed = false ;

    private TransformerFactory transformerFactory = TransformerFactory.newInstance() ;
    private String styleSheet ;
    private Transformer firstCandidate ;
    private PostParseCallBack postParseCallBack ;
    private ParserInitCallBack parserInitCallBack ;
    
   
    /**
     * The only constructor.
     * <p> 
     * All state is established by setters.
     * In order to guarantee thread safety, no alteration of the
     * internal state of <code>AdqlParserSV</code> is allowed
     * once parsing gets underway. That is: the first parse
     * will seal the parser against any further change of settings.
     * 
     * @see org.astrogrid.adql.AdqlParserSV#registerParserInitCallBack(ParserInitCallBack)
     * @see org.astrogrid.adql.AdqlParserSV#registerPostParseCallBack(PostParseCallBack)
     * @see org.astrogrid.adql.AdqlParserSV#setMaxParsers(int)
     * @see org.astrogrid.adql.AdqlParserSV#setStyleSheet(String)
     */
    public AdqlParserSV() {}
    
    /**
     * Transforms an ADQL query in string format to a specific
     * variant of SQL. The input is a full query, starting
     * with a SELECT statement, and not simply a fragment of a query. 
     * For example:
     * <p>
     * Select * from first as f ;
     * <p>
     * <p><blockquote><pre>
     * (1) Retrieves a parser from the parser pool.<p>
     * (2) Parses the source to XmlBeans.<p>
     * (3) Invokes the user's post-parse call back (if one was registered).<p>
     * (4) Emits XML from the SelectDocument bean.<p>
     * (5) Transforms the XML into SQL variant using style sheet.<p>
     * </pre></blockquote><p>
     * 
     * @param adqls - An ADQL query in string format
     * @return A specific SQL version of the query
     * @throws AdqlException 
     *         if ADQL input fails parsing.
     * @throws InvalidStateException 
     *         if there is an invalid state, for example: an instance of
     *         the transformer corresponding to the XSLT style sheet cannot be formed.
     * @throws TransformerException
     *         if there is a problem with the XSLT style sheet.
     * @see org.astrogrid.adql.IAdqlParser
     */
    public String parseToSQL( String adqls ) throws AdqlException
                                                  , InvalidStateException
                                                  , TransformerException { 
        ParserUnit cu = null ;
        try {
            //
            // First parse from ADQL/s to ADQL/x, returning an XML object...
            StringReader reader = new StringReader( adqls ) ;
            cu = getParser( reader ) ;  
            SelectDocument selectDoc = cu.parser.parseToXmlBeans() ;
            //
            // If a callback has been registered, invoke it here
            // so the select document bean can be used to manipulate
            // whatever the user wants to manipulate...
            if( postParseCallBack != null ) {
                selectDoc = (SelectDocument)postParseCallBack.execMidPoint( selectDoc ) ;
            }
            //
            // Then get the resulting XML and transform it to SQL variant...
            String adqlx = selectDoc.xmlText() ;       
            StreamSource source = new StreamSource( new StringReader( adqlx ) ) ;
            StreamResult result = new StreamResult( new StringWriter() ) ;      
            cu.sqlTransformer.transform( source, result ) ;  
            //
            // Return the final SQL variant...
            return ((StringWriter)result.getWriter()).toString() ;
        }
        finally {
            //
            // Whatever happens, the parser unit instance must be returned
            // to the pool in a thread-safe manner...
            if( cu != null ) {
                synchronized( this ) {
                    parsers.add( cu ) ;
                    this.notify() ;
                }   
            }                               
        }       
    }
    

    /* (non-Javadoc)
     * @see org.astrogrid.adql.IAdqlParser#convert(java.lang.String)
     */
    public String convert(String adqlString) throws InvalidStateException, TransformerException {
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

	public SelectDocument parseToXML(String adqls) throws AdqlException, InvalidStateException {
		ParserUnit pu = null ;
    	try {
    		//
    		// Parse from ADQL/s to ADQL/x, returning an XML object...
    		pu = getParser( new StringReader( adqls ) ) ;  
    		return pu.parser.parseToXmlBeans() ;
    	}
    	finally {
    		//
    		// Whatever happens, the parser unit instance must be returned
    		// to the pool in a thread-safe manner...
    		if( pu != null ) {
    			synchronized( this ) {
    				parsers.add( pu ) ;
    				this.notify() ;
    			}   
    		}                               
    	}       
	}

	public String transformToSQL( SelectDocument selectDoc ) 
		throws InvalidStateException, TransformerException {
		ParserUnit pu = null ; ;
    	try {
    		pu = getParser( null ) ;  
    		//
    		// Then get the resulting XML and transform it to SQL variant...     
    		StreamSource source = new StreamSource( new StringReader( selectDoc.xmlText() ) ) ;
    		StreamResult result = new StreamResult( new StringWriter() ) ;      
    		pu.sqlTransformer.transform( source, result ) ;  
    		//
    		// Return the final SQL variant...
    		return ((StringWriter)result.getWriter()).toString() ;
    	}
    	finally {
    		//
    		// Whatever happens, the parser unit instance must be returned
    		// to the pool in a thread-safe manner...
    		if( pu != null ) {
    			synchronized( this ) {				
    				parsers.add( pu ) ;
    				this.notify() ;
    			}   
    		}                               
    	}       
	}
    
    /**
     * Sets the XSLT style sheet to be used in the transformation from 
     * ADQL/x to an SQL variant. All the parsings managed by
     * <code>AdqlParserSV</code> will use this style sheet.
     * The setter - if used - must be before any parsing is undertaken,
     * otherwise it will be ignored.
     * 
     * @param styleSheet in String format.
     * @return boolean indicating whether setting was a success.
     * @throws TransformerConfigurationException
     */
    public synchronized boolean setStyleSheet( String styleSheet ) throws TransformerConfigurationException {
    	if( this.sealed == true )
    		return false ;
    	StreamSource source = new StreamSource( new StringReader( styleSheet )  ) ;
    	this.firstCandidate = this.transformerFactory.newTransformer( source ) ;       
    	this.styleSheet = styleSheet ;
    	return true ;
    }
    
    /**
     * Sets the XSLT style sheet to be used in the transformation from 
     * ADQL/x to an SQL variant. All the parsings managed by
     * <code>AdqlParserSV</code> will use this style sheet.
     * The setter - if used - must be before any parsing is undertaken,
     * otherwise it will be ignored.
     * 
     * @param styleSheet as an InputStream.
     * @return boolean indicating whether setting was a success.
     * @throws TransformerConfigurationException
     */
    public synchronized boolean setStyleSheet( InputStream isStyleSheet ) throws TransformerConfigurationException {
    	if( this.sealed == true )
    		return false ;
    	try {
    		this.styleSheet = getStyleSheet( isStyleSheet ) ;
    	}
    	catch( IOException iox ) {
    		throw new TransformerConfigurationException( iox ) ;
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
     * <code>AdqlParserSV</code> is allowed to instantiate.
     * The setter - if used - must be before any parsing
     * begin, otherwise it will be ignored. The setting must 
     * be beween 1 and 16 inclusive. If the setter is not used, 
     * the maximum setting of 2 is assumed. 
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
     * Registers a user's post call back routine for initializing a parser instance. 
     * A call back must be registered before any parsing begins.
     * 
     * @param picb - the ParserInitCallBack interface
     * @return boolean indicating whether the registration was a success.
     */
    public synchronized boolean registerParseInitCallBack( ParserInitCallBack picb ) {
    	if( this.sealed == true )
    		return false ;
    	this.parserInitCallBack = picb ;
    	return true ;    
    }
    
    /**
     * Registers a user's post parsing call back routine, which will be invoked once
     * the ADQL/s source has been parsed into XMLBeans. A call back
     * must be registered before any parsing begins.
     * 
     * @param ppcb - the PostParseCallBack interface
     * @return boolean indicating whether the registration was a success.
     */
    public synchronized boolean registerPostParseCallBack( PostParseCallBack ppcb ) {
    	if( this.sealed == true )
    		return false ;
    	this.postParseCallBack = ppcb ;
    	return true ;     
    }
  
    /*
     * A critical method of AdqlParserSV in that it manages the controlled removal
     * of a parser instance for use from a pool of instances. It manages instantiating
     * a new parser instance when the pool is empty AND the maximum setting has not been
     * reached. If the pool is empty and the maximum setting has been reached, then
     * a wait is imposed until an instance is returned to the pool.
     * 
     * For each parser instance in the pool, an associated transformer is also instantiated.
     * Neither parsers nor transformers are thread safe, though they are serially reusable.
     */
    private ParserUnit getParser( Reader reader ) throws InvalidStateException {
        ParserUnit cu = null ;
        Transformer firstCandidate_Saved = null ;
        
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
                    if( this.firstCandidate != null ) {
                    	firstCandidate_Saved = firstCandidate ;
                        firstCandidate = null ;
                    }
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
                    cu = parsers.remove(0) ;
                }           
            }
            else {
                //
                // No problems, get one from the pool...
                cu = parsers.remove(0) ;
            }
        } // end of synchronized block
        
        //
        // Some defensive programming here. 
        // If possible, fail anywhere but where it will affect the pool.
        // At some point it may be worthwhile to investigate the Reinit()
        // method on the underlying parser, to establish any potential
        // weaknesses to do with the Reader input object.
        if( reader == null ) {
        	reader = new StringReader( "" ) ;
        }
        //
        // Any new instances created or re-initialization is done outside of synchronization ...
        if( cu == null ) {
        	  AdqlParser c = new AdqlParser( reader ) ;
              initParserSettings( c ) ;      
               
              if( firstCandidate_Saved != null ) {
                  cu = new ParserUnit( c, firstCandidate_Saved ) ;
              }
              else {
                  try {
                      Source s = new StreamSource( new StringReader( this.styleSheet ) ) ;
                      Transformer t = transformerFactory.newTransformer( s ) ;
                      cu = new ParserUnit( c, t ) ;
                  }
                  catch ( TransformerConfigurationException tce ) {
                	  
                	  synchronized( this ) {
                		  parserCount-- ;
                	  }               	  
                      log.error( "Failed to instantiate Transformer", tce ) ;
                      throw new InvalidStateException( "Failed to instantiate Transformer", tce ) ;
                  }
              }       
        }
        else {
           	cu.parser.ReInit( reader ) ;
        }       
        return cu ;
    }
    
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
    	// Any creation of new converters takes place outside of synchronization ...
    	if( c == null ) {
            c = new AdqlConverter() ;
    	}
        return c ;
    }
       
    private void initParserSettings( AdqlParser parser ) {
        if( this.parserInitCallBack != null ) {
            this.parserInitCallBack.execInit( parser ) ;
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


/*
$Log: AdqlParserSV.java,v $
Revision 1.3  2010/08/25 11:04:43  jl99
Adaptation of ADQL parser to provide suitable pooling strategy for DSA.
Version: 2010.1

Revision 1.2.4.5  2010/01/25 18:26:29  jl99
Improved JavaDocs

Revision 1.2.4.4  2010/01/20 15:33:44  jl99
Rejigged server version(s) to fit in more with possible DSA adjustments.

Revision 1.2.4.3  2010/01/19 17:22:43  jl99
Changes concomitant on accommodating DSA

Revision 1.2.4.2  2009/12/18 21:18:29  jl99
Added Server version without call backs. Amended multi-threading test.

Revision 1.2.4.1  2009/12/04 15:31:41  jl99
Additional unit tests.

Revision 1.2  2008/09/08 15:37:22  jl99
Merge of branch adql_jl_2575_mark2 into HEAD

Revision 1.1.2.2  2008/08/31 10:12:37  jl99
Now using a callback to intialize individual parsers within the pool

Revision 1.1.2.1  2008/08/29 14:49:10  jl99
First mass commit for the new project adql2

Revision 1.1.2.3  2008/08/01 18:49:59  jl99
Complete reorg for purposes of setting up a maven build

Revision 1.1.2.2  2008/07/25 18:16:14  jl99
Tidy.

Revision 1.1.2.1  2008/07/14 18:47:14  jl99
Attempt to rename AdqlCompilerSV to AdqlCompilerSV
*/