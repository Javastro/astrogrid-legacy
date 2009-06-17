/*$Id: BnfExtractor.java,v 1.2 2009/06/17 10:13:32 jl99 Exp $
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.adql;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;
import java.io.* ;
import java.util.*;
import java.text.MessageFormat;
/** 
 * BnfExtractor
 * 
 * Extracts bnf statements from an annotated javacc file.
 *
 * Usage: BnfExtractor {Options} input=file-path output=file-path        
 * Options:
 *    -t  text output
 *    -h  html output
 * Notes: (1) The output file-path parameter is optional. If omitted, output
 *            is directed to standard out.
 *        (2) If no options are specified, text output is assumed.
 *        (3) Ouput files must not already exist. If you wish to overwrite,
 *            omit the output file parameter and pipe standard out.
 *
 * @author Jeff Lusted jl99@star.le.ac.uk
 * Oct 22, 2006
 */
public class BnfExtractor {
    
private static Log log = LogFactory.getLog( BnfExtractor.class ) ;
    
    private static StringBuffer logIndent = new StringBuffer() ;
    
    private static final String USAGE =
        "Usage: BnfExtractor {Options} input=file-path output=file-path \n" +       
        "Options:\n" +
        "   -t  text output\n" +
        "   -h  html output\n" +
        "Notes: (1) The output file-path parameter is optional. If omitted, output\n" +
        "           is directed to standard out.\n" +
        "       (2) If no options are specified, text output is assumed." +
        "       (3) Ouput files must not already exist. If you wish to overwrite," +
        "           omit the output file parameter and pipe standard out." ;
    
    private static final String TEXT_HEADINGS =
        "+===============================================================+\n" +
        "|                ADQL BNF definition file.                      |\n" +
        "+===============================================================+\n\n" +
        "This file contains a BNF definition of ADQL, the Astronomical\n" +
        "Data Query Language. The elements are arranged in alphabetical\n" +
        "order. ADQL is based upon a subset of SQL92 with extensions for\n" +
        "astronomical usage.\n" +
        "\n" +
        "The definition represents the equivalent ADQL/s version of ADQL/x v1.0a2\n" +
        "as supported by Astrogrid. This is an evolving document.\n\n" +
        "The select statement is found under <query_specification>\n\n" ;
     
    private static final String TEXT_FOOTINGS =
        "" ;
    
    private static final String HTML_HEADINGS =
        "<html>" +
        "<head>" +
        "<title>BNF for ADQL v1.0a2</title>" +
        "</head>" +
        "<body>" +
        "<pre>" +
        "This file contains a BNF definition of ADQL, the Astronomical\n" +
        "Data Query Language. The elements are arranged in alphabetical\n" +
        "order. ADQL is based upon a subset of SQL92 with extensions for\n" +
        "astronomical usage.\n" +
        "\n" +
        "The definition represents the equivalent ADQL/s version of ADQL/x v1.0a2\n" +
        "as supported by Astrogrid. This is an evolving document.\n\n" +
        "The select statement is found at <a href=\"#query_specification\">&lt;query_specification&gt;</a>\n" +
        "See <a href=\"http://savage.net.au/SQL/sql-92.bnf.html\">SQL92</a> for a similar page describing SQL92 in full.\n\n" ;
  
    private static final String HTML_FOOTINGS =
        "\n" +
        "</pre>" +
        "</body>" +  
        "</html>" ;
    
    private static final String HTML_KEY_TEMPLATE = 
        "<a name=\"{0}\">&lt;{0}&gt;</a>" ;
    
    private static final String HTML_DETAIL_TEMPLATE = 
        "&lt;<a href=\"#{0}\">{0}</a>&gt;" ;

    private static final String BNF_SINGLE = " * bnf-single" ;
    private static final String BNF_START = " * bnf-start" ;
    private static final String BNF_END = " * bnf-end" ;
    private static final String BNF_TRIGGER = "bnf-" ;
    
    private static String option = null ;
    private static String inFilePath = null ;
    private static String outFilePath = null ;
    
    private String iOption = null ;
    private File inputFile ;
    private File outputFile ;   
    private FileReader reader = null ;
    private MessageFormat keyFormat = new MessageFormat( HTML_KEY_TEMPLATE )  ;
    private MessageFormat detailFormat = new MessageFormat( HTML_DETAIL_TEMPLATE )  ;
    private OutputStream outputStream = null ;
    
    private StringBuffer lineBuffer = new StringBuffer( 100 ) ;
    private boolean inputEOF = false ;
    private ArrayList list = new ArrayList( 256 ) ;
    private BnfStatementFactory statementFactory = new BnfStatementFactory() ;

    /**
     * @param args
     */
    public static void main( String[] args ) {
        if( log.isTraceEnabled() ) enterTrace ( "BnfExtractor.main" ) ;
        
        if( retrieveArgs( args ) == false ) {
            System.out.println() ;
            System.out.print( USAGE ) ;
            return ;
        }
        
        File inputFile = new File( inFilePath ) ;
        File outputFile = null ;
        
        if( inputFile.exists() == false ) {
            System.out.println( "Input file does not exist:\n" +
                                "    " + inFilePath
                              ) ;
            return ;
        }
          
        if( outFilePath != null ) {
            outputFile = new File( outFilePath ) ;
            if( outputFile.exists() == true ) {
                System.out.println( "Output file already exists:\n" +
                                    "    " + outFilePath
                                  ) ;
                return ;
            }
        }
        
        if( BnfExtractor.option == null ) {
            BnfExtractor.option = "t" ;
        }
               
        BnfExtractor extractor = new BnfExtractor( inputFile, outputFile, option ) ;
        
        extractor.exec() ;
        System.out.println( "BNF extraction complete." ) ;
        if( log.isTraceEnabled() ) exitTrace ( "BnfExtractor.main" ) ;
    }
    
    private static boolean retrieveArgs( String[] args ) {
        boolean retVal = false ;
        if( args != null && args.length > 0 ) {
            // Usage: BnfExtractor {Options} input=file-path output=file-path 
            for( int i=0; i<args.length; i++ ) {
                if( args[i].startsWith( "-t" ) ) {
                   BnfExtractor.option = "t" ;
                }
                else if( args[i].startsWith( "-h" ) ) {
                    BnfExtractor.option = "h" ; 
                }
                else if( args[i].startsWith( "input=" )  
                         |
                         args[i].startsWith( "i=" ) ) { 
                    BnfExtractor.inFilePath = args[i].substring(6) ;
                }
                else if( args[i].startsWith( "output=" ) 
                         |
                         args[i].startsWith( "o=" ) ) { 
                    BnfExtractor.outFilePath = args[i].substring(7) ;
                }
                else {
                    break ;
                }
            }
            if( BnfExtractor.inFilePath != null )
               retVal = true ;
        }       
        return retVal ;
    }
    
    public BnfExtractor( File inputFile, File outputFile, String format ) {
        if( log.isTraceEnabled() ) enterTrace ( "BnfExtractor(File, File, String)" ) ;
        this.inputFile = inputFile ;
        this.outputFile = outputFile ;
        if( this.outputFile != null ) {
            try {
                this.outputStream = new FileOutputStream( outputFile ) ;
            }
            catch( FileNotFoundException fnfex ) {
                fnfex.printStackTrace() ;
            }
        }
        else {
            this.outputStream = System.out ;
        }   
        this.iOption = format ;
        if( log.isTraceEnabled() ) exitTrace ( "BnfExtractor(File, File, String)" ) ;
    }
    
    public BnfExtractor( File inputFile, String format ) {
        this( inputFile, null, format ) ;
    }
   
    public void exec() {
        if( log.isTraceEnabled() ) enterTrace ( "BnfExtractor.exec()" ) ;
        openFiles() ;
        consumeInputFile() ; 
        produceOutput() ;
        if( log.isTraceEnabled() ) exitTrace ( "BnfExtractor.exec()" ) ;
    }
    
    private void openFiles() {
        if( log.isTraceEnabled() ) enterTrace ( "BnfExtractor.openFiles()" ) ;
        try {
            reader = new FileReader( inputFile ) ;
        }
        catch( Exception iox ) {
            System.out.println( iox.getLocalizedMessage() ) ;
        }
        finally {
            if( log.isTraceEnabled() ) exitTrace ( "BnfExtractor.openFiles()" ) ;
        }
    }
    
    private void consumeInputFile() {
        if( log.isTraceEnabled() ) enterTrace ( "BnfExtractor.consumeInputFile()" ) ;
        while( isInputEOF() == false ) { 
            processOneStatement() ;
        }
        validateInputFile() ;
//        BnfStatement[] sArray = getSortedStatementArray() ;       
//        if( DEBUG_ENABLED ) {
//            for( int i=0; i<sArray.length; i++ ) {
//                System.out.println( sArray[i].toString() ) ;
//            }
//        }
        if( log.isTraceEnabled() ) exitTrace ( "BnfExtractor.consumeInputFile()" ) ;
    }
    
    private void validateInputFile() {
        HashMap map = new HashMap() ;
        HashSet errors = new HashSet() ;
        ListIterator it = list.listIterator() ;
        while( it.hasNext() ) {
            BnfStatement bnf = (BnfStatement)it.next() ;
            map.put( bnf.key, bnf ) ;
        }
        it = list.listIterator() ;
        while( it.hasNext() ) {
            BnfStatement bnf = (BnfStatement)it.next() ;
            checkStatement( bnf, map, errors ) ;
        }
        if( errors.size() > 0 ) {
            String[] errArray = new String[ errors.size() ] ;
            errArray = (String[])errors.toArray( errArray ) ;
            Arrays.sort( errArray ) ;
            for( int i=0; i<errArray.length; i++ ) {
                log.error( errArray[i] ) ;
            }
        }
    }
    
    private void checkStatement( BnfStatement bnf, HashMap map, HashSet errors ) {
        String[] rKeys = bnf.getKeysOfReferencedElements() ;
        for( int i=0; i<rKeys.length; i++ ) {
            if( !map.containsKey( rKeys[i] ) ) {
                errors.add( "BNF Statement missing from input: <" + rKeys[i] + ">" ) ;
            }
        }
    }
    
    private void produceOutput() {
        try {
            if( iOption.equals( "t" ) ) {
                produceText() ;
            }
            else {
                produceHtml() ;
            }
        }
        catch( IOException iox ) {
            iox.printStackTrace() ;
        }
       
    }
    
    private void processOneStatement() {
        if( log.isTraceEnabled() ) enterTrace ( "BnfExtractor.processOneStatement()" ) ;
        String line = readAheadToStatement() ;
        try {
            if( isBnfSingle( line ) ) {
                list.add( statementFactory.newInstance( line.substring( BNF_SINGLE.length() ) ) ) ;
            }
            else if( isBnfStart( line ) ) {
                ArrayList lineArray= new ArrayList() ;               
                line = readLine() ;
                while( !isInputEOF() && !isBnfEnd( line ) ) {
                    lineArray.add( line.substring(2) ) ;
                    line = readLine() ;
                }
                if( lineArray.size() > 0 ) {
                    list.add( statementFactory.newInstance( (String[])lineArray.toArray( new String[ lineArray.size() ] ) ) ) ;
                }
            }
        }
        catch( InvalidBnfStatementException ibsex ) {
            System.out.println( ibsex.getLocalizedMessage() ) ;
        }
        finally {
            if( log.isTraceEnabled() ) exitTrace ( "BnfExtractor.processOneStatement()" ) ;
        }
    }
    
    private String readAheadToStatement() {
        if( log.isTraceEnabled() ) enterTrace ( "BnfExtractor.readAheadToStatement()" ) ;
        String line = readLine() ;
        while( !isInputEOF() && ( line.indexOf( BNF_TRIGGER ) == -1 ) ) {
            line = readLine() ;
        }
        if( log.isTraceEnabled() ) exitTrace ( "BnfExtractor.readAheadToStatement()" ) ;
        return line ;
    }
    
    private String readLine() {
//        if( log.isTraceEnabled() ) enterTrace ( "BnfExtractor.readLine()" ) ;
        emptyLineBuffer() ;
        try {
            int ch = reader.read() ;
            readLoop : {
                while( ch != -1 ) {
                    lineBuffer.append( (char)ch ) ;
                    if( ch == '\n' ) {
                        break readLoop ;
                    }             
                    ch = reader.read() ;
                }
                setInputEOF() ;
            } // end of readLoop
        }
        catch( IOException iox ) {
            System.out.println( iox.getLocalizedMessage() ) ;
        }
        finally {
//            if( DEBUG_ENABLED ) {
//                if( lineBuffer.length() > 0 ) {
//                    System.out.print( lineBuffer.toString() ) ;
//                }
//            }
//            if( log.isTraceEnabled() ) exitTrace ( "BnfExtractor.readLine()" ) ;
        }
        return lineBuffer.toString() ;
    }
    
    private void produceText() throws IOException {
       outputStream.write( TEXT_HEADINGS.getBytes() ) ;
       BnfStatement[] sArray = getSortedStatementArray() ;  
       for( int i=0; i<sArray.length; i++ ) {
           outputStream.write( sArray[i].toText().getBytes() ) ;
           outputStream.write( '\n' ) ;
       }
       outputStream.write( TEXT_FOOTINGS.getBytes() ) ;
    }
    
    private void produceHtml() throws IOException {
        outputStream.write( HTML_HEADINGS.getBytes() ) ;
        BnfStatement[] sArray = getSortedStatementArray() ;  
        for( int i=0; i<sArray.length; i++ ) {
            outputStream.write( sArray[i].toHtml().getBytes() ) ;
            outputStream.write( '\n' ) ;
        }
        outputStream.write( HTML_FOOTINGS.getBytes() ) ;
     }
    
    private void emptyLineBuffer() {
        if( lineBuffer.length() > 0 ) {
            lineBuffer.delete( 0, lineBuffer.length() ) ;
        }
    }
    
    private void setInputEOF() {
        this.inputEOF = true ;
    }
    
    private boolean isInputEOF() {
        return this.inputEOF ;
    }
    
    private boolean isBnfSingle( String line ) {
        if( line.indexOf( BNF_SINGLE ) != -1 )
            return true ;
        return false ;
    }
    
    private boolean isBnfStart( String line ) {
        if( line.indexOf( BNF_START ) != -1 )
            return true ;
        return false ;
    }
    
    private boolean isBnfEnd( String line ) {
        if( line.indexOf( BNF_END ) != -1 )
            return true ;
        return false ;
    }
    
    private BnfStatement[] getSortedStatementArray() {
        BnfStatement[] arrayStatement = new BnfStatement[ list.size() ] ;
        arrayStatement = (BnfStatement[])list.toArray( arrayStatement ) ;
        Arrays.sort( arrayStatement 
                   , new Comparator() {
                       public int compare( Object o1, Object o2 ) {
                           return ((BnfStatement)o1).compare((BnfStatement)o2) ;
                       }       
                   } ) ;
        return arrayStatement ;
    }
    
    class BnfStatement {
         
        String statementsAsString ;
        String key ;
        
        BnfStatement( String[] statements, String key ) {
            if( log.isDebugEnabled() ) {
                log.debug( "BnfStatement.key: " + key ) ;
            }
            this.key = key ;
            StringBuffer buffer = new StringBuffer() ;
            for( int i=0; i<statements.length; i++ ) {
                buffer.append( statements[i] ) ;
            }
            this.statementsAsString = buffer.toString() ;
        }
        
        private BnfStatement() {}
        
        /* (non-Javadoc)
         * @see java.lang.Object#equals(java.lang.Object)
         */
        public boolean equals( Object that ) {
            if( that instanceof BnfStatement ) {
                BnfStatement arg = (BnfStatement)that ;
                if( this.key.equals( arg.key ) ) 
                    return true ;
            }        
            return false ;
        }
        
        public int compare( BnfStatement s ) {
            return this.key.compareTo( s.key ) ;          
        }
        
        public String toString() {
            return statementsAsString ;
        }
        
        public String toText() {
            return toString() ;
        }
        
        public String[] getKeysOfReferencedElements() {
            ArrayList elements = new ArrayList() ;
            ArrayList psList = getMatchedPairsAndSingletons() ;
            ListIterator it = psList.listIterator() ;
            //
            // Read past my own key...
            it.next() ;
            while( it.hasNext() ) {
                Object o = it.next() ;
                if( o instanceof Pair ) {
                    Pair p = (Pair)o ;
                    String elementName = statementsAsString.substring( p.x+1, p.y ) ;
                    elements.add( elementName ) ;
                } 
            }
            String[] elementArray = new String[ elements.size() ] ;
            elementArray = (String[])elements.toArray( elementArray ) ;
            return elementArray ;
        }
        
        public String toHtml() {  
            String s = statementsAsString ;
            StringBuffer buffer = new StringBuffer() ;
            ArrayList psList = getMatchedPairsAndSingletons() ;
            ListIterator it = psList.listIterator() ;
            //
            // There has to be a first one and it has to be a Pair...
            // It is the element or key!
            Pair p = (Pair)it.next() ;
            //
            // First, buffer up the space before the element name...
            buffer.append( s.substring( 0, p.x ) ) ;
            //
            // Format the element/key...      
            String elementName = s.substring( p.x+1, p.y ) ;
            String  htmlizedElement = 
              keyFormat.format( new String[]{elementName}, new StringBuffer(), null ).toString() ;
            buffer.append( htmlizedElement ) ;
            //
            // OK, now for the details...
            // But first save the current position.
            int current = p.y + 1 ;
            while( it.hasNext() ) {
                Object o = it.next() ;
                if( o instanceof Pair ) {
                    p = (Pair)o ;
                    // Buffer up the intermediate characters...
                    buffer.append( s.substring( current, p.x ) ) ;
                    // Then format one detail element...
                    elementName = s.substring( p.x+1, p.y ) ;
                    htmlizedElement = 
                        detailFormat.format( new String[]{elementName}, new StringBuffer(), null ).toString() ;
                    buffer.append( htmlizedElement ) ;            
                    current = p.y + 1 ; // remember to hold position
                }
                else { // It must be a singleton...
                    int i = ((Integer)o).intValue() ;
                    // Buffer up the intermediate characters...
                    if( current < i ) {
                        buffer.append( s.substring( current, i ) ) ;
                        current = i ;
                    }                   
                    if( s.charAt(i) == '>' ) {
                        buffer.append( "&gt;" ) ;
                    }
                    else {
                        buffer.append( "&lt;" ) ;
                    }
                    current++ ; // remember to hold position
                }
            }
            //
            // Pick up the remainder...
            // which may include comments with < and > embedded in them...
            if( current != s.length() ) {
                String remainder = s.substring( current ) ;
                if( log.isDebugEnabled() ) {
                    log.debug( "Remainder: " + remainder ) ;
                }
                remainder = remainder.replaceAll( "<", "&lt;" ) ;
                remainder = remainder.replaceAll( ">", "&gt;" ) ;
                buffer.append( remainder ) ;
            }  
            return buffer.toString() ;
        }
        
        private ArrayList getMatchedPairsAndSingletons() {
            ArrayList psList = new ArrayList() ;
            ArrayList indices = getIndicesOfLtGt() ;
            Integer[] x = new Integer[ indices.size() ] ;
            String s = statementsAsString ;
            x = (Integer[])indices.toArray( x ) ;
            Integer candidateLt = null ;
            Integer candidateGt = null ;
            for( int i=0; i<x.length; i++ ) {
                if( s.charAt(x[i].intValue() ) == '<' ) {
                    if( candidateLt == null ) {
                        candidateLt = x[i] ;
                    }
                    else {
                        psList.add( candidateLt ) ;
                        candidateLt = x[i] ;
                    }
                }
                else if( s.charAt(x[i].intValue() ) == '>' ){
                    if( candidateGt == null ) {
                        candidateGt = x[i] ;
                    }
                    else {
                        psList.add( x[i] ) ;
                    }
                }
                if( candidateLt != null && candidateGt != null ) {
                    if( candidateLt.intValue()+1 == candidateGt.intValue() ) {
                        psList.add( candidateLt ) ;
                        psList.add( candidateGt ) ;
                    }
                    else {
                        psList.add( new Pair( candidateLt, candidateGt ) ) ;
                    }
                    candidateLt = candidateGt = null ;
                }
            }
            return psList ;
        }
        
        private ArrayList getIndicesOfLtGt() {
            ArrayList listLtGt = new ArrayList() ;
            for( int i=0; i<statementsAsString.length(); i++ ) {
                char c = statementsAsString.charAt(i) ;
                if( c == '!' && statementsAsString.charAt(i+1) == '!' ) {
                    break ;
                }
                if( c == '<' || c == '>' )
                    listLtGt.add( new Integer(i) ) ;
            }
            return listLtGt ;
        }
        
    }
    
    class BnfStatementFactory {
        
        BnfStatement newInstance( String statement ) throws InvalidBnfStatementException {
            return new BnfStatement( new String[] { statement }, extractKey( statement ) ) ;
        }
        
        BnfStatement newInstance( String[] statementArray ) throws InvalidBnfStatementException {
            return new BnfStatement( statementArray, extractKey( statementArray[0] ) ) ;
        }
        
        private String extractKey( String firstLine ) throws InvalidBnfStatementException {
            int indexFrom = firstLine.indexOf( '<' ) ;
            int indexTo = firstLine.indexOf(  '>', indexFrom ) ;
            int indexOfDefinitionOp = firstLine.indexOf( "::=", indexTo ) ;
            
            if( indexFrom == -1 || indexTo == -1 || indexOfDefinitionOp == -1 ) {
                String message = 
                    "InvalidBnfStatementException:\n" +
                    "   " + firstLine ;
                throw new InvalidBnfStatementException( message ) ;
            }
            
            return firstLine.substring( indexFrom+1, indexTo ) ;
        }
        
    }
    
    class InvalidBnfStatementException extends Exception {
        public InvalidBnfStatementException(String message) {
            super(message);
        }       
    }
    
    class Pair {
        public int x ;
        public int y ;
        
        Pair( Integer X, Integer Y ) {
            x = X.intValue() ;
            y = Y.intValue() ;
        }
    }
  
    private static void enterTrace( String entry ) {
        log.debug( logIndent.toString() + "enter: " + entry ) ;
        indentPlus() ;
    }

    private static void exitTrace( String entry ) {
        indentMinus() ;
        log.debug( logIndent.toString() + "exit : " + entry ) ;
    }
    
    private static void indentPlus() {
        logIndent.append( ' ' ) ;
    }
    
    private static void indentMinus() {
        logIndent.deleteCharAt( logIndent.length()-1 ) ;
    }

}


/*
$Log: BnfExtractor.java,v $
Revision 1.2  2009/06/17 10:13:32  jl99
Merge of adql v1 parser with maven 2 build.
Due to some necessary restructuring, the maven 1 build process has been removed.

Revision 1.1.2.1  2009/06/16 07:49:41  jl99
First commit of maven 2 build

Revision 1.11  2008/04/16 11:04:23  jl99
Merge of branch adql-jl-2731

Revision 1.10.6.1  2008/04/15 14:09:57  jl99
(1) ROUND and TRUNCATE functions have changed cardinality.
(2) Some improvements to comment processing.

Revision 1.10  2008/02/04 17:47:30  jl99
Merge of branch adql-jl-2504

Revision 1.9.2.1  2008/01/29 15:32:57  jl99
Updated header comments.

Revision 1.9  2007/06/06 18:20:19  jl99
Merge of branch adql-jl-2135

Revision 1.8.8.1  2007/06/06 10:53:59  jl99
Code tidy just prior to merge of branch adql-jl-2135

Revision 1.8  2006/11/12 20:06:44  jl99
Slight change to heading.

Revision 1.7  2006/11/06 23:08:38  jl99
Removed footnote references.

Revision 1.6  2006/10/28 22:10:19  jl99
Adjustments in the area of aliased expressions.
At present ADQL/x is broken in this area.

Revision 1.5  2006/10/25 11:47:16  jl99
Corrections to processing not-equals-operator in html format (ie: <>)

Revision 1.4  2006/10/23 22:11:53  jl99
Validates the input file for missing elements.

Revision 1.3  2006/10/23 21:13:46  jl99
Working correctly to file and standard out.
Produces text and html versions.

Revision 1.2  2006/10/23 09:43:55  jl99
Working correctly to standard out.

Revision 1.1  2006/10/22 22:03:55  jl99
First commit of utility to extract bnf statements from annotated .jjt files.

*/