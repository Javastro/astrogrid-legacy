/*$Id: AdqlParserSV.java,v 1.2 2008/09/08 15:37:22 jl99 Exp $
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

/**
 * The <code>AdqlParserSV</code> class is a server side version of 
 * <code>AdqlParser</code>. It differs from <code>AdqlParser</code>
 * in a number of important respects:
 * <p><blockquote><pre>
 *   (1) It processes ADQL right the way through to SQL.
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
 *
 * @author Jeff Lusted jl99@star.le.ac.uk
 * Aug 6, 2007
 */
public class AdqlParserSV {
    
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
     * ParserInitCallBack
     *
     *
     *
     * @author Jeff Lusted jl99@star.le.ac.uk
     * Aug 8, 2007
     */
    public interface ParserInitCallBack { 
        
        
        /**
         * @param xo
         * @return
         */
        public void execInit( AdqlParser ap ) ; 
        
        
    }
    
    /**
     * PostParseCallBack
     *
     *
     *
     * @author Jeff Lusted jl99@star.le.ac.uk
     * Aug 8, 2007
     */
    public interface PostParseCallBack { 
        
        
        /**
         * @param xo
         * @return
         */
        public XmlObject execMidPoint( XmlObject xo ) ; 
        
        
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
    
    private class ParserUnit {      
        AdqlParser parser ;
        Transformer sqlTransformer ;
        
        ParserUnit( AdqlParser parser, Transformer transformer ) {
            this.parser = parser ;
            this.sqlTransformer = transformer ;
        }
    }
    
    private int maxParsers = DEFAULT_MAX_PARSERS ; 
    private Stack inParsers = new Stack() ;
    private Stack outParsers = new Stack() ;
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
     * <p><p>
     * 
     * (1) Retrieves a parser from the parser pool.<p>
     * (2) Parses the source to XmlBeans.<p>
     * (3) Invokes the user's post-parse call back (if one was registered).<p>
     * (4) Emits XML from the SelectDocument bean.<p>
     * (5) Transforms the XML into SQL variant using style sheet.<p>
     * <p>
     * @param adqls - An ADQL query in string format
     * @return A specific SQL version of the query
     * @throws AdqlException 
     *         if ADQL input fails parsing.
     * @throws InvalidStateException 
     *         if there is an invalid state, for example: an instance of
     *         the transformer corresponding to the XSLT style sheet cannot be formed.
     * @throws TransformerException
     *         if there is a problem with the XSLT style sheet.
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
                    outParsers.remove( cu ) ;
                    inParsers.push( cu ) ;
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
     * The critical method of AdqlParserSV in that it manages the controlled removal
     * of a parser instance for use from a pool of instances. It manages instantiating
     * a new parser instance when the pool is empty AND the maximum setting has not been
     * reached. If the pool is empty and the maximum setting has been reached, then
     * a wait is imposed until an instance is returned to the pool.
     * 
     * For each parser instance in the pool, an associated transformer is also instantiated.
     * Neither parsers nor transformers are thread safe, though they are serially reusable.
     */
    private synchronized ParserUnit getParser( Reader reader ) throws InvalidStateException {
        //
        // If not yet sealed, the first invocation will seal against any 
        // further changes to settings by a user...
        if( this.sealed == false ) {
            this.sealed = true ;
        }
        //
        // Some defensive programming here. 
        // If possible, fail anywhere but where it will affect the pool.
        // At some point it may be worthwhile to investigate the Reinit()
        // method on the underlying parser, to establish any potential
        // weaknesses to do with the Reader input object.
        if( reader == null )
            return null ;

        ParserUnit cu ;
        //
        // If the pool is empty, we need to create a new parser
        // or wait until one is released...
        if( inParsers.isEmpty() ) {
            //
            // Slack to create a new one? ...
            if( outParsers.size() < maxParsers ) {
                //
                // Yes, indeed!...
                AdqlParser c = new AdqlParser( reader ) ;
                initParserSettings( c ) ;               
                if( this.firstCandidate != null ) {
                    cu = new ParserUnit( c, firstCandidate ) ;
                    firstCandidate = null ;
                }
                else {
                    try {
                        Source s = new StreamSource( new StringReader( this.styleSheet ) ) ;
                        Transformer t = transformerFactory.newTransformer( s ) ;
                        cu = new ParserUnit( c, t ) ;
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
                while( inParsers.isEmpty() ) {
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
                cu = (ParserUnit)inParsers.pop() ;
                cu.parser.ReInit( reader ) ;
            }           
        }
        else {
            //
            // No problems, get one from the pool...
            cu = (ParserUnit)inParsers.pop() ;
            cu.parser.ReInit( reader ) ;
        }
        //
        // Make sure we register with the outParsers...
        outParsers.push( cu ) ;
        this.sealed = true ;
        if( log.isDebugEnabled() ) {
            log.debug( "outParsers: [" + outParsers.size() 
                     + "] outParsers: [" + inParsers.size() + "]" ) ;
        }
        return cu ;
    }
    
    private void initParserSettings( AdqlParser parser ) {
        if( this.parserInitCallBack != null ) {
            this.parserInitCallBack.execInit( parser ) ;
        }
    }

}


/*
$Log: AdqlParserSV.java,v $
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