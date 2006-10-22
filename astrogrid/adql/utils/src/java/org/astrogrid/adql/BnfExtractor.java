/*$Id: BnfExtractor.java,v 1.1 2006/10/22 22:03:55 jl99 Exp $
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
/**
 * BnfExtractor
 *
 *
 * @author Jeff Lusted jl99@star.le.ac.uk
 * Oct 22, 2006
 */
public class BnfExtractor {
    
private static Log log = LogFactory.getLog( BnfExtractor.class ) ;
    
    private static final boolean TRACE_ENABLED = true ;
    private static final boolean DEBUG_ENABLED = true ;
    private static StringBuffer logIndent = new StringBuffer() ;
    
    File inputFile ;
    File outputFile ;
    String format ;
    
    private FileReader reader = null ;
    private FileWriter writer = null ;
    private StringBuffer lineBuffer = new StringBuffer( 100 ) ;
    private StringBuffer statementBuffer = new StringBuffer( 256 ) ;
    private boolean inputEOF = false ;
    private ArrayList list = new ArrayList( 256 ) ;
    private BnfStatementFactory statementFactory = new BnfStatementFactory() ;

    /**
     * @param args
     */
    public static void main( String[] args ) {
        if( TRACE_ENABLED ) enterTrace ( "BnfExtractor.main" ) ;
        
        String option ;
        String inFilePath ;
        String outFilePath ;
               
        // options:
        //      -t for text
        //      -h for html
        //      -o followed by output file spec
        //
        // then path to annotated input .jjt file
        if( args == null || args.length < 2 || args.length > 3) {
            System.out.println( "incorrect parameters" ) ;
            System.exit(0) ;
        }
        
        option = args[0] ;
        outFilePath = args[1] ;
        inFilePath = args[2] ;
        
        File inputFile = new File( inFilePath ) ;
        File outputFile = new File( outFilePath ) ;
        
        BnfExtractor extractor = new BnfExtractor( inputFile, outputFile, option ) ;
        
        extractor.exec() ;
        
        if( TRACE_ENABLED ) exitTrace ( "BnfExtractor.main" ) ;
    }
    
    public BnfExtractor( File inputFile, File outputFile, String format ) {
        if( TRACE_ENABLED ) enterTrace ( "BnfExtractor(File, File, String)" ) ;
        this.inputFile = inputFile ;
        this.outputFile = outputFile ;
        this.format = format ;
        if( TRACE_ENABLED ) exitTrace ( "BnfExtractor(File, File, String)" ) ;
    }
   
    public void exec() {
        if( TRACE_ENABLED ) enterTrace ( "BnfExtractor.exec()" ) ;
        openFiles() ;
        consumeInputFile() ; 
        if( TRACE_ENABLED ) exitTrace ( "BnfExtractor.exec()" ) ;
    }
    
    private void openFiles() {
        if( TRACE_ENABLED ) enterTrace ( "BnfExtractor.openFiles()" ) ;
        try {
            reader = new FileReader( inputFile ) ;
            writer = new FileWriter( outputFile ) ;
        }
        catch( Exception iox ) {
            System.out.println( iox.getLocalizedMessage() ) ;
        }
        finally {
            if( TRACE_ENABLED ) exitTrace ( "BnfExtractor.openFiles()" ) ;
        }
    }
    
    private void consumeInputFile() {
        if( TRACE_ENABLED ) enterTrace ( "BnfExtractor.consumeInputFile()" ) ;
        while( isInputEOF() == false ) { 
            processOneStatement() ;
        }
//        if( DEBUG_ENABLED ) {
//            ListIterator it = list.listIterator() ;
//            while( it.hasNext() ) {
//                BnfStatement s = (BnfStatement)it.next() ;
//                System.out.println( s.toString() ) ;
//            }
//        }
        if( TRACE_ENABLED ) exitTrace ( "BnfExtractor.consumeInputFile()" ) ;
    }
    
    private void processOneStatement() {
        if( TRACE_ENABLED ) enterTrace ( "BnfExtractor.processOneStatement()" ) ;
        String line = readAheadToStatement() ;
        try {
            if( line.startsWith( " * bnf-single" ) ) {
                list.add( statementFactory.newInstance( line.substring(13) ) ) ;
            }
            else if( line.startsWith( " * bnf-start" ) ) {
                ArrayList lineArray= new ArrayList() ;               
                line = readLine() ;
                while( !isInputEOF() && !line.startsWith( " * bnf-end" ) ) {
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
            if( TRACE_ENABLED ) exitTrace ( "BnfExtractor.processOneStatement()" ) ;
        }
    }
    
    private String readAheadToStatement() {
        if( TRACE_ENABLED ) enterTrace ( "BnfExtractor.readAheadToStatement()" ) ;
        String line = readLine() ;
        while( !isInputEOF() && !( line.startsWith( " * bnf-" ) ) ) {
            line = readLine() ;
        }
        if( TRACE_ENABLED ) exitTrace ( "BnfExtractor.readAheadToStatement()" ) ;
        return line ;
    }
    
    private String readLine() {
//        if( TRACE_ENABLED ) enterTrace ( "BnfExtractor.readLine()" ) ;
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
//            if( TRACE_ENABLED ) exitTrace ( "BnfExtractor.readLine()" ) ;
        }
        return lineBuffer.toString() ;
    }
    
    private void emptyLineBuffer() {
        if( lineBuffer.length() > 0 ) {
            lineBuffer.delete( 0, lineBuffer.length() ) ;
        }
    }
    
    private void emptyStatementBuffer() {
        if( statementBuffer.length() > 0 ) {
            statementBuffer.delete( 0, statementBuffer.length()-1 ) ;
        }
    }
    
    private void setInputEOF() {
        this.inputEOF = true ;
    }
    
    private boolean isInputEOF() {
        return this.inputEOF ;
    }
    
    class BnfStatement {
         
        String[] statements ;
        String key ;
        
        BnfStatement( String[] statements, String key ) {
            this.statements = statements ;
            this.key = key ;
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
        
        public String toString() {
            StringBuffer buffer = new StringBuffer() ;
            for( int i=0; i<statements.length; i++ ) {
                buffer.append( statements[i] ) ;
            }
            return buffer.toString() ;
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
                throw new InvalidBnfStatementException() ;
            }
            
            return firstLine.substring( indexFrom+1, indexTo ) ;
        }
        
    }
    
    class InvalidBnfStatementException extends Exception {
        
    }
    
    private class EOF extends Exception {
        
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
Revision 1.1  2006/10/22 22:03:55  jl99
First commit of utility to extract bnf statements from annotated .jjt files.

*/