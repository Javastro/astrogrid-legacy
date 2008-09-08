/*
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.adql;

import org.apache.xmlbeans.*;

import java.io.IOException;
import java.io.StringReader;
import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import org.astrogrid.adql.metadata.*;

/**
 * Interactive
 * 
 * Conducts a console based conversation. A query is entered in string form.
 * The query will be compiled into adql/x or an error returned.
 * <p>
 * Type an ADQL query and press the ENTER key.<br/>
 * Ensure the query ends with a semicolon.<br/>
 * Type "bye" to exit.<br/>
 * <p>
 * To parse fragments of ADQL, switch to fragment mode by typing "parse_fragment" 
 * and pressing the ENTER key. Once in fragment mode, parse a fragment of ADQL by
 * prefixing any ADQL string with the fragment name or an XPath like statement followed 
 * by a colon. Examples:
 * <p>
 * from: from catalogA as a inner join catalogB as b on a.col1 = b.col1 ;<br/>
 * Select/Column: a.ra ;<br/>
 * Select/Item[@type="aggregateFunctionType"]/Arg: * ;<br/>
 * <p>
 * To return to compiling full queries, switch mode by typing "parse_full" 
 * and pressing the ENTER key.
 * <p>
 * There are three command line options <br/>
 * -s=style_sheet_name <br/>
 * -m=properties_file_name, which will load metadata from the given properties file on the classpath.<br/>
 * -u=function_prefix, which allows the setting of a user defined prefix for function calls.<br/>
 * Enter -u= with no function prefix if you wish the function prefix to be ignored.<br/> 
 *
 * @author Jeff Lusted jl99@star.le.ac.uk Sep 18, 2006
 */
public class Interactive implements MetadataQuery {
    
    class EOFException extends Exception {
        EOFException() {} ;
        
    } 
    
    class ExitException extends Exception {
        ExitException() {} ; 
    }
    
    class ModeSwitchException extends Exception {
        
        String mode ;
        
        ModeSwitchException( String mode ) {
            this.mode = mode ;
        } ;
        
        String getMode() {
            return mode ;
        }
    }
    
    private static Log log = LogFactory.getLog( Interactive.class ) ;

    public static final String BYE_TRIGGER = "Encountered \"bye\"" ;
    public static final String BYE = "BYE" ;
    public static final String FULL_MODE = "parse_full" ;
    public static final String FRAGMENT_MODE = "parse_fragment" ;
   
    public static boolean bFullMode = true ;
 
    private static AdqlParser PARSER ;
    private static StringBuffer queryBuffer ;
    private static Interactive interactive ;
    private static String fragmentName ;
    private static Container metadata ;  
    private static String metaDataFileName = null ;
    private static String userDefinedPrefix = "udf_" ;
    private static AdqlTransformer TRANSFORMER ;
    
    private static String[] RESERVED_WORDS = { "first" , "match", "area", "next", "distance", "region", "substring" } ;
       
    public static final String WELCOME = 
        "Welcome to the command line tester for the Adql parser...\n" +
        " USAGE: Type an ADQL query and press the ENTER key.\n" +
        "        Ensure the query ends with a semicolon.\n" +
        "        Type \"bye\" to exit.\n\n" +
        "        To parse fragments of ADQL, switch to fragment mode by typing \"parse_fragment\".\n" +
        "        Then prefix any ADQL string with the fragment name. Examples:\n\n" +
        "        from: from catalogA as a inner join catalogB as b on a.col1 = b.col1;\n" +
        "        Select/Column: a.ra;\n" +
        "        Select/Item[@type=\"aggregateFunctionType\"]/Arg: * ; \n\n" +
        "        To return to parsing full queries, switch by typing \"parse_full\".\n" +
        " COMMAND LINE OPTIONS: \n" +
        "        -s=style_sheet_file_name \n" +
        "           Will attempt to load a style sheet from the classpath.\n" +
        "           The style sheet is used to echo back to the user the output XML\n" +
        "           after it has been passed through the style sheet.\n" +
        "           Useful to check the translation to some variant of SQL.\n" +
        "        -m=properties_file_name \n" +
        "           Will attempt to load metadata from the given properties file on the classpath.\n" +
        "        -u=function_prefix\n" +
        "           The prefix to be used for user defined functions.\n" +
        "           Enter -u= with no function prefix if you wish the function prefix to be ignored.\n"  ;
   
    public Interactive() {}
    
	 public static void main(String args[]) {
         interactive = new Interactive() ;   
         print( WELCOME );
         
         if( !interactive.retrieveArgs( args ) ) {
             return ;
         }
                    
         while( true ) {
             initQueryBuffer() ;
             print("\nEnter an SQL-like expression :");	

             try {
                 StringReader source = getQuerySource() ;
                 String queryXml ;
                 if( bFullMode == true ) {
                     queryXml = getParser( source ).parseToXmlText() ; 
                     print( "\nParser produced:" ) ;
                     print( "\n" + queryXml ) ;  
                     if( TRANSFORMER != null ) {
                         print( "\n\nTransformer produced:" ) ;
                         print( "\n" + TRANSFORMER.transformToAdqls( queryXml ) ) ;
                     } 
                 }
                 else {
                     if( fragmentName == null ) {
                         print( "\nPlease provide a fragment name." ) ;
                     }
                     else {
                         print( "\nfragment name: " + fragmentName ) ;
                         String[] comments = new String[2] ;
                         XmlObject xo = getParser( source ).execFragment( fragmentName, comments ) ;
                         XmlOptions opts = new XmlOptions();
                         opts.setSaveOuter() ;                    
                         opts.setSaveAggressiveNamespaces() ;
                         opts.setSavePrettyPrint() ;
                         opts.setSavePrettyPrintIndent(4) ;
                         queryXml = xo.xmlText( opts ) ;
                         print( "\nParser produced:" ) ;
                         if( comments[0] != null ) {
                             print( "header comment: \n" + PARSER.prepareComment( comments[0] ) ) ;
                         }
                         print( "\n" + queryXml ) ;
                         if( comments[1] != null ) {
                             print( "trailer comment: \n" + PARSER.prepareComment( comments[1] ) ) ;
                         }
                     }             
                 }                	
             }
             catch( ExitException eof ) {
                 print( "Goodbye!" ) ;
                 System.exit(0) ;
             }
             catch( ModeSwitchException mse ) {
                 String mseMode = mse.getMode() ;
                 if( mseMode.equalsIgnoreCase( FULL_MODE ) ) {
                     print( "Switching to full queries..." ) ;
                     bFullMode = true ;
                 }
                 else if( mseMode.equalsIgnoreCase( FRAGMENT_MODE ) ) {
                     print( "Switching to fragments..." ) ;
                     bFullMode = false ;
                 }
             }
             catch( AdqlException adqlException ) {
                 print( "Parse failed:" ) ;
                 String[] messages = adqlException.getErrorMessages() ;
                 for( int i=0; i<messages.length; i++ ) {
                     print( messages[i] ) ;
                 }
                 if( adqlException.isWarningMessage() ) {
                     messages = adqlException.getWarningMessages() ;
                     for( int i=0; i<messages.length; i++ ) {
                         print( messages[i] ) ;
                     }
                 }
             }
             catch( IOException iox ) {
                 print( iox.getMessage() ) ; 
             }
             catch( Exception ex ) {
                 print( "\nPossible error within parser:", ex ) ;
             }
         }				
	 }

     
     private static void initQueryBuffer() {
         if( queryBuffer == null ) {
             queryBuffer = new StringBuffer( 1024 ) ;
         }
         else if( queryBuffer.length() > 0 ) {
             queryBuffer.delete( 0, queryBuffer.length() ) ;
         }            
     }
     
     
     private static StringReader getQuerySource() 
                    throws ModeSwitchException
                         , ExitException
                         , IOException {
         StringBuffer lineBuffer = new StringBuffer() ;
         fragmentName = null ;
         //
         // Force a query to begin with a new line....
         queryBuffer.append( '\n' ) ;       
         String line ;
         char c = (char)System.in.read() ;
         while( true ) {
             lineBuffer.append( c ) ;
             if( c == '\n' ) {
                 line = lineBuffer.toString().trim() ;
                 if( line.endsWith( ";" ) && !line.startsWith( "--" ) ) {
                     queryBuffer.append( line.substring( 0, line.length()-1 ) ) ;
                     break ;
                 }
                 else if( line.startsWith( FRAGMENT_MODE ) ) {
                     throw interactive.new ModeSwitchException( FRAGMENT_MODE ) ;
                 }
                 else if( line.startsWith( FULL_MODE ) ) {
                     throw interactive.new ModeSwitchException( FULL_MODE ) ;
                 }
                 else if( line.startsWith( BYE ) || line.startsWith( BYE.toLowerCase() ) ) {
                     throw interactive.new ExitException() ;
                 }
                 queryBuffer.append( lineBuffer ) ;
                 lineBuffer.delete( 0, lineBuffer.length() ) ;
             }
             else if( bFullMode == false && c == ':' ) {
                 fragmentName = lineBuffer.toString().substring(0,lineBuffer.length()-1).trim() ;
                 lineBuffer.delete( 0, lineBuffer.length() ) ;
             }
             c = (char)System.in.read() ;
         }
            
         print( "query source:" ) ;
         print( queryBuffer.toString() ) ;
         StringReader source = new StringReader( queryBuffer.toString() ) ;
         
         return source ;
     }
     
     private static AdqlParser getParser( StringReader source) {
         if( PARSER == null ) {
             PARSER = new AdqlParser( source );  
             PARSER.setSemanticProcessing( true ) ;
             PARSER.setSyntax( AdqlParser.V20_AGX ) ;
             if( metaDataFileName != null ) {
                 metadata = MetaDataLoader.getMetaData( metaDataFileName ) ;
                 PARSER.setMetadataQuery( interactive ) ;
             }
             else {
                 metadata = new Container() ;
             }
             PARSER.setUserDefinedFunctionPrefix( userDefinedPrefix ) ;
             PARSER.addReservedWordHints( RESERVED_WORDS ) ;
         }
         else {
             PARSER.ReInit( source ) ;
         }
         return PARSER ;
     }
	 
     private static void print( String message ) {
         if( log.isDebugEnabled() ) {
             log.debug( message ) ;
         }
         else {
             System.out.println( message ) ;
         }
     }
     
     private static void print( String message, Exception ex ) {
         if( log.isDebugEnabled() ) {
             log.debug( message, ex ) ;
         }
         else {
             System.out.println( message ) ;
             System.out.println( ex.getStackTrace() ) ;
         }
     }

    /* (non-Javadoc)
     * @see org.astrogrid.adql.metadata.MetadataQuery#isColumn(java.lang.String, java.lang.String)
     */
    public boolean isColumn(String tableName, String columnName) {
        return metadata.isColumn( columnName, new String[] { tableName } ) ;
    }

    /* (non-Javadoc)
     * @see org.astrogrid.adql.metadata.MetadataQuery#isTable(java.lang.String)
     */
    public boolean isTable(String tableName) {
        return metadata.isTable( tableName );
    }

    /* (non-Javadoc)
     * @see org.astrogrid.adql.metadata.MetadataQuery#getFunctionCardinality(java.lang.String)
     */
    public int[] getFunctionCardinality(String functionName) {
        return metadata.getFunction( functionName ).getCardinality() ;
    }

    /* (non-Javadoc)
     * @see org.astrogrid.adql.metadata.MetadataQuery#isFunction(java.lang.String)
     */
    public boolean isFunction(String functionName) {
        return metadata.isFunction( functionName );
    }
     
    private boolean retrieveArgs( String[] args ) {
        Interactive.userDefinedPrefix = null ;
        boolean retVal = true ;
        if( args != null && args.length >= 1 ) {
            for( int i=0; i<args.length; i++ ) {
                if( args[i].startsWith( "-m") ) {
                    if( args[i].length() > 3 ) {
                      metaDataFileName = args[i].substring(3) ;                       
                  }
                }
                else if( args[i].startsWith( "-u=" ) ) { 
                    if( args[i].length() > 3 ) {
                        Interactive.userDefinedPrefix = args[i].substring(3) ;
                    }
                    else {
                        //
                        // a null prefix means no checking for prefix
                        Interactive.userDefinedPrefix = null ;
                    }

                }
                else if( args[i].startsWith( "-s=" ) ) {
                    if( args[i].length() > 3 ) {
//                        print( System.getProperties().toString() ) ;
                        System.setProperty( "javax.xml.transform.TransformerFactory"
                                           ,"com.icl.saxon.TransformerFactoryImpl" ) ;
//                        print( "System property javax.xml.transform.TransformerFactory: " +
//                                System.getProperty( "javax.xml.transform.TransformerFactory" ) ) ;
                        TRANSFORMER = new AdqlTransformer( args[i].substring(3) ) ;                       
                    }
                }
                else {
                    retVal = false ;
                }
            } 
            if( retVal == false ) {
                print( "Invalid args:") ; 
                for( int i=0; i<args.length; i++ ) {
                    print( args[i] ) ;
                }
            }
        }       
        return retVal ;
    }
    
}
