
/*$Id: AdqlStoXTest.java,v 1.2 2009/06/17 10:13:32 jl99 Exp $
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.adql;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader; 
import java.io.StringReader;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.astrogrid.adql.v1_0.beans.SelectDocument;
import org.astrogrid.adql.v1_0.beans.SelectType;
import org.astrogrid.xml.DomHelper;
import org.custommonkey.xmlunit.XMLTestCase;
import org.w3c.dom.Document;

import org.apache.log4j.Logger ;
import org.apache.log4j.Level ;
import org.apache.log4j.spi.LoggerRepository;

/**
 * AdqlStoXTest
 *
 *
 * @author Jeff Lusted jl99@star.le.ac.uk
 * Sep 26, 2006
 */
public class AdqlStoXTest extends XMLTestCase {
	
	final static String README = "README" ;
	final static String BAD_INIT_MESSAGE = "AdqlStoXTest. Initialization failed: " ;
	final static String BAD_SETUP_MESSAGE = "AdqlStoXTest. Method setUp() failed: " ;
    
    final static String BAD_SOURCE = "SELECT las.sourceID, las.ra, las.\"dec\", fsc.seqNo, fsc.ra, fsc.\"dec\"" +
                                     "FROM lasSource AS las, ROSAT..rosat_fsc AS fsc, lasSourceXrosat_fsc AS x" +
                                     "WHERE x.masterObjID=las.sourceID AND x.slaveObjID=fsc.seqNo AND" + 
                                     "x.distanceMins<0.1;" ;
    final static String GOOD_SOURCE = "Select * From catalogue as a Where (a.POS_EQ_RA<100) And (( a.POS_EQ_RA>100 )" +   
                                      "And (ACOS((( SIN( a.POS_EQ_DEC ) * SIN( 100 ) ) + (COS(a.POS_EQ_DEC) * " +  
                                      "( COS(100)  * COS( (a.POS_EQ_RA - 100) ) )))) <= 100 ));" ;

    private static AdqlCompiler sCompiler = null ;
	private static boolean sInitialized = false ; 
	private static boolean sBadInitializedStatus = false ;
	private static File sDirectoryOfREADME = null ;
	
	private File currentSFile ; 
	private File currentXFile ;
    
    private String fragmentContext ;
    
    private static long accumulatedTime ;
    private static int testMethodCount ;
    private long startTime ;
    private long endTime ;

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		if( sBadInitializedStatus == true ) {
			String message = BAD_INIT_MESSAGE + "see message from earlier initialization failure" ;
			throw new InitializationException( message ) ;
		}
		if( sInitialized == false ) {
			init() ;
		}
		locateFilesForTest() ;
	}
    public void testOf_v10_6dfExplicitThreeWayJoin() throws Exception { execTest() ; }
    public void testOf_v10_6dfExplicitThreeWayJoinWithBrackets() throws Exception { execTest() ; }
    public void testOf_v10_BADdelimitedIdentifier() throws Exception { execTest() ; }
    public void testOf_v10_BADduplicatedAliases() throws Exception { execTest() ; }
	public void testOf_v10_BADemptyFrom() throws Exception { execTest() ; }
	public void testOf_v10_BADemptyWhere() throws Exception { execTest() ; }
    public void testOf_v10_BADextremelyComplexQuery() throws Exception { execTest() ; }
    public void testOf_v10_BADinnerJoinWithJoinCondition() throws Exception { execTest() ; }
    public void testOf_v10_BADNestedBracketWithinConeSearch01() throws Exception { execTest() ; }
    public void testOf_v10_BADregularIdentifier() throws Exception { execTest() ; }
    public void testOf_v10_BADreservedWordAsTableName() throws Exception { execTest() ; }
	public void testOf_v10_BADselectEmptyAlias() throws Exception { execTest() ; }
    public void testOf_v10_BADMAselectList() throws Exception { execTest() ; }
	public void testOf_v10_BADselectOrderByDirOnly() throws Exception { execTest() ; }
    public void testOf_v10_BADthreeWayJoinAcrossArchives() throws Exception { execTest() ; }
    public void testOf_v10_BADtop() throws Exception { execTest() ; }
    public void testOf_v10_ceilingFunction() throws Exception { execTest() ; }
    public void testOf_v10_comments01Cstyle() throws Exception { execTest() ; }
    public void testOf_v10_comments01CPPstyle() throws Exception { execTest() ; }
    public void testOf_v10_comments01() throws Exception { execTest() ; }
    public void testOf_v10_comments02() throws Exception { execTest() ; }
    public void testOf_v10_comments03() throws Exception { execTest() ; }
    public void testOf_v10_commentsMultipleLines04() throws Exception { execTest() ; }
    public void testOf_v10_complexSelect01() throws Exception { execTest() ; }
    public void testOf_v10_coneSearch01() throws Exception { execTest() ; }
    public void testOf_v10_coneSearch02() throws Exception { execTest() ; }
    public void testOf_v10_decAsNonReservedWord() throws Exception { execTest() ; }
    public void testOf_v10_delimitedIdentifier() throws Exception { execTest() ; }
// JBL. 30/01/2008. Full outer joins pulled for the moment. Cannot be supported easily in MySQL
//    public void testOf_v10_fullOuterJoinWithJoinCondition() throws Exception { execTest() ; }
    public void testOf_v10_groupByOneColumn() throws Exception { execTest() ; }
    public void testOf_v10_innerJoinWithJoinCondition() throws Exception { execTest() ; }
    public void testOf_v10_inPredicateWithConstantStringList() throws Exception { execTest() ; }
    public void testOf_v10_inPredicateWithSubQuery() throws Exception { execTest() ; }
    public void testOf_v10_leftOuterJoinWithJoinCondition() throws Exception { execTest() ; }
    public void testOf_v10_likeWithoutBracket() throws Exception { execTest() ; }
    public void testOf_v10_likeWithBracket() throws Exception { execTest() ; }
//  JBL. 30/01/2008. Full outer joins pulled for the moment. Cannot be supported easily in MySQL
//    public void testOf_v10_MAFullOuterJoinWithJoinCondition() throws Exception { execTest() ; }
    public void testOf_v10_MAInnerJoinWithJoinCondition() throws Exception { execTest() ; }
    public void testOf_v10_MALeftOuterJoinWithJoinCondition() throws Exception { execTest() ; }
    public void testOf_v10_MAThreeWayJoin() throws Exception { execTest() ; }
    public void testOf_v10_MAThreeWayJoinNonAliased() throws Exception { execTest() ; }
    public void testOf_v10_naiveConeSearch() throws Exception { execTest() ; }
    public void testOf_v10_notLikeWithBracket() throws Exception { execTest() ; }
    public void testOf_v10_notLikeWithoutBracket() throws Exception { execTest() ; }
    public void testOf_v10_notInPredicateWithConstantStringList() throws Exception { execTest() ; }
    public void testOf_v10_notInPredicateWithSubQuery() throws Exception { execTest() ; }
    public void testOf_v10_regularIdentifier() throws Exception { execTest() ; }
    public void testOf_v10_SDSSBasicSelectFromWhere() throws Exception { execTest() ; }
    public void testOf_v10_SDSSGalaxiesWithTwoCriteria() throws Exception { execTest() ; }
    // Has a user defined function which will fail at present.
    // Now requires renaming as user defined functions catered for...
    public void _testOf_v10_SDSSUnclassifiedSpectraWILLFAIL() throws Exception { execTest() ; }
    public void testOf_v10_SDSSGalaxiesWithMultipleCriteria() throws Exception { execTest() ; }
    public void testOf_v10_SDSSSpatialUnitVectors() throws Exception { execTest() ; }
    public void testOf_v10_SDSSCataclysmicVariablesUsingColors_C_style() throws Exception { execTest() ; }
    public void testOf_v10_SDSSCataclysmicVariablesUsingColors() throws Exception { execTest() ; }
    // Has bit processing which will fail...
    public void testOf_v10_SDSSDataSubSampleWILLFAIL() throws Exception { execTest() ; }
    public void testOf_v10_SDSSLowzQSOsUsingColors() throws Exception { execTest() ; }
    public void testOf_v10_SDSSObjectVelocitiesAndErrors() throws Exception { execTest() ; }
    public void testOf_v10_SDSSUsingBetween() throws Exception { execTest() ; }
    public void testOf_v10_SDSSMovingAsteroids() throws Exception { execTest() ; }
    public void testOf_v10_SDSSQuasarsInImaging() throws Exception { execTest() ; }
    // Has bit processing and use of str function...
    public void testOf_v10_SDSSSelectedNeighborsInRunWILLFAIL() throws Exception { execTest() ; }
    // Contains a case...
    public void testOf_v10_SDSSObjectCountingAndLogicWILLFAIL() throws Exception { execTest() ; }
    public void testOf_v10_SDSSGalaxiesBlendedWithStars() throws Exception { execTest() ; }
    public void testOf_v10_SDSSStarsInSpecificFields() throws Exception { execTest() ; }
    // Uses bit processing...
    public void testOf_v10_SDSSUsingThreeTablesWILLFAIL() throws Exception { execTest() ; }
    // User defined function...
    // Now requires renaming as user defined functions catered for...
    public void _testOf_v10_SDSSQSOsInSpectroscopyWILLFAIL() throws Exception { execTest() ; }
    public void testOf_v10_SDSSObjectsClosePairs() throws Exception { execTest() ; }
    // Uses bit processing and user defined function...
    public void testOf_v10_SDSSErrorsUsingFlagsWILLFAIL() throws Exception { execTest() ; }
    // Uses bit processing and user defined function...
    public void testOf_v10_SDSSEllipticalGalaxiesBasedOnModelFitsWILLFAIL() throws Exception { execTest() ; }
	public void testOf_v10_selectAggregateFuncs() throws Exception { execTest() ; }
	public void testOf_v10_selectAliasExpr() throws Exception { execTest() ; } 
	public void testOf_v10_selectAllAllow() throws Exception { execTest() ; } 
	public void testOf_v10_selectAllLimit() throws Exception { execTest() ; } 
	public void testOf_v10_selectBetweenOps() throws Exception { execTest() ; } 
	public void testOf_v10_selectBinaries() throws Exception { execTest() ; }
	public void testOf_v10_selectBoolOps() throws Exception { execTest() ; }
    public void testOf_v10_selectComparisonOps() throws Exception { execTest() ; }
    public void testOf_v10_selectDistinct() throws Exception { execTest() ; }
    public void testOf_v10_selectExpr1() throws Exception { execTest() ; }
    public void testOf_v10_selectExpr2() throws Exception { execTest() ; }
    public void testOf_v10_selectExprMixed1() throws Exception { execTest() ; }
    public void testOf_v10_selectExprMultiAlias() throws Exception { execTest() ; }
    public void testOf_v10_selectExprSum() throws Exception { execTest() ; }
    public void testOf_v10_selectExprUnary() throws Exception { execTest() ; }
    public void testOf_v10_selectFromNoAlias() throws Exception { execTest() ; }
    public void testOf_v10_selectGroupBy() throws Exception { execTest() ; }
    public void testOf_v10_selectLogPowMathsFuncs() throws Exception { execTest() ; }
    public void testOf_v10_selectMultiTabMultiAlias1() throws Exception { execTest() ; }
    public void testOf_v10_selectOneTableTwoCols() throws Exception { execTest() ; }
    public void testOf_v10_selectOrderByCol() throws Exception { execTest() ; }
    public void testOf_v10_selectOrderByComplex() throws Exception { execTest() ; }
    public void testOf_v10_selectSome() throws Exception { execTest() ; }
    public void testOf_v10_selectTrigFuncsDeg() throws Exception { execTest() ; }
    public void testOf_v10_selectTrigFuncsRad() throws Exception { execTest() ; }
    public void testOf_v10_selectTwoTablesFourCols() throws Exception { execTest() ; }
    public void testOf_v10_selectTwoTablesFourColsNoAlias() throws Exception { execTest() ; }
    public void testOf_v10_selectUnaries() throws Exception { execTest() ; }
    public void testOf_v10_selectValueTweakMathsFuncs() throws Exception { execTest() ; }
    public void testOf_v10_spectralLines() throws Exception { execTest() ; }
    public void testOf_v10_squaringTheCircle() throws Exception { execTest() ; }
    public void testOf_v10_threeWayJoin() throws Exception { execTest() ; }
    public void testOf_v10_whereWithBinaryOpsAndUnaryOps() throws Exception { execTest() ; }
    public void testOf_v10_whereWithNegativeUnaryLiteral() throws Exception { execTest() ; }
    public void testOf_v10_whereWithPositiveUnaryLiteral() throws Exception { execTest() ; }
    public void testOf_v10_whereWithUnsignedNumericLiteral() throws Exception { execTest() ; reportTail() ; }
    
    // Place all fragment compilations at the end please...
    public void testOf_v10_FragmentBADContextName() throws Exception { execFragment() ; }
    public void testOf_v10_FragmentColumn() throws Exception { execFragment() ; }
    public void testOf_v10_FragmentCountArgument() throws Exception { execFragment() ; }  // Keep an eye on this
    public void testOf_v10_FragmentCountArgument02() throws Exception { execFragment() ; } 
    public void testOf_v10_FragmentConeSearchWhere() throws Exception { execFragment() ; }
    public void testOf_v10_FragmentFrom01() throws Exception { execFragment() ; }
    public void testOf_v10_FragmentFromNoAlias() throws Exception { execFragment() ; }
    public void testOf_v10_FragmentFromTableTypeTableType() throws Exception { execFragment() ; }
    public void testOf_v10_FragmentGroupBy() throws Exception { execFragment() ; }
    public void testOf_v10_FragmentHaving() throws Exception { execFragment() ; }
    public void testOf_v10_FragmentItemArgument() throws Exception { execFragment() ; }
    public void testOf_v10_FragmentLikePattern() throws Exception { execFragment() ; }
    public void testOf_v10_FragmentMATablesArrayOfFromTableType() throws Exception { execFragment() ; }
    public void testOf_v10_FragmentNaiveConeSearch() throws Exception { execFragment() ; }
    public void testOf_v10_FragmentOrderBy() throws Exception { execFragment() ; }
    public void testOf_v10_FragmentOrderByItem() throws Exception { execFragment() ; }
    public void testOf_v10_FragmentOrderByItemExpression() throws Exception { execFragment() ; }
    public void testOf_v10_FragmentOrderByItemOrder() throws Exception { execFragment() ; }
    public void testOf_v10_FragmentOrderByItemOrder02() throws Exception { execFragment() ; }
    public void testOf_v10_FragmentSelectAllAllow() throws Exception { execFragment() ; }
    public void testOf_v10_FragmentSelectFromTable() throws Exception { execFragment() ; }
    public void testOf_v10_FragmentSelectWhereConditionNotInExpression() throws Exception { execFragment() ; } 
    public void testOf_v10_FragmentSelectWhereConditionInExpression() throws Exception { execFragment() ; } 
    public void testOf_v10_FragmentSelectionList01() throws Exception { execFragment() ; }
    public void testOf_v10_FragmentSelectionListAllItems() throws Exception { execFragment() ; }
    public void testOf_v10_FragmentSelectionListCeilingFunction() throws Exception { execFragment() ; }
    public void testOf_v10_FragmentSelectionListItemExpression() throws Exception { execFragment() ; }
    public void testOf_v10_FragmentSetItem() throws Exception { execFragment() ; }
    public void testOf_v10_FragmentSubsetSpectralLines() throws Exception { execFragment() ; }
    public void testOf_v10_FragmentTableCondition() throws Exception { execFragment() ; }
    public void testOf_v10_FragmentTablesArrayOfFromTableType() throws Exception { execFragment() ; }
    public void testOf_v10_FragmentTableTableType() throws Exception { execFragment() ; }
    public void testOf_v10_FragmentTop() throws Exception { execFragment() ; }
    public void testOf_v10_FragmentWhereCondition() throws Exception { execFragment() ; } // Uncertain of result.
    public void testOf_v10_FragmentWithHeadAndTrailComments() throws Exception { execFragmentHeadersAndTrailers() ; }
    
    // JL note:
    // need to try the next two tests for memory on separate threading basis as well.
    public void testMemoryUsageWithReinit() throws OutOfMemoryError {
        // NB: (JL) I've tried this with the setting set to 1000000 and it worked.
        //
        // Turn off all logging.
        // Precaution in case the log level is set inappropriately,
        // in which case we would be testing the ability of the logging
        // system to survive rather than the compiler.
        // A single compile at trace level can produce many hundreds of lines of print.
        Level savedLevel ;
        Logger logger = Logger.getRootLogger();
        LoggerRepository repository = logger.getLoggerRepository() ;
        savedLevel = repository.getThreshold() ;
        repository.setThreshold( Level.OFF ) ;

        final int total = 10000 ;
        System.out.println( "testMemoryUsageWithReinit() underway: " + total + " compilations." ) ;
        // This one continues to use the compiler instance used so far in all the above tests.   
        int goodCount = 0, badCount = 0 ;

        try {
            for( int i=0; i<total; i++ ) {

                StringReader source = null ;
                if( i%2 == 0 ) {
                    source = new StringReader( BAD_SOURCE ) ;
                }
                else {
                    source = new StringReader( GOOD_SOURCE ) ;
                }
                try {           
                    getCompiler( source ).compileToXmlBeans();
                    goodCount++ ;
                } 
                catch ( AdqlException aex ) {                
                    if( i%2 != 0 ) {
                        System.out.println( "testMemoryUsageWithReinit produced: " + aex.getClass().getName() ) ;
                        String[] ms = aex.getMessages() ;
                        for( int j=0; j<ms.length; j++ ) {
                            System.out.println( ms[j] ) ;                      
                        }
                        assertTrue( "Unexpected exception: " + aex.getClass().getName(), false ) ;
                        break ;
                    }
                    else {
                        badCount++ ;
                    }
                }
                catch ( Throwable th ) {
                    System.out.println( "testMemoryUsageWithReinit produced: " + th.getClass().getName() ) ;
                    if( th instanceof OutOfMemoryError ) {
                        throw (OutOfMemoryError)th;
                    }
                    assertTrue( "Unexpected exception: " + th.getClass().getName(), false ) ;
                    break ;
                }
                if( i != 0 && i%1000 == 0 ) {
                    System.out.println( i + " finished" ) ;
                }
            }
            System.out.println( "testMemoryUsageWithReinit() compilations finished." ) ;
            System.out.println( "good count: " + goodCount ) ;
            System.out.println( "bad count: " + badCount ) ;
            assertTrue( goodCount == total/2  && badCount == total/2 ) ;
        }
        finally {
            repository.setThreshold( savedLevel ) ;
        }
               
    }
    
    public void testMemoryUsageWithoutReinit() throws OutOfMemoryError {
        //
        // Turn off all logging.
        // Precaution in case the log level is set inappropriately,
        // in which case we would be testing the ability of the logging
        // system to survive rather than the compiler.
        // A single compile at trace level can produce many hundreds of lines of print.
        Level savedLevel ;
        Logger logger = Logger.getRootLogger();
        LoggerRepository repository = logger.getLoggerRepository() ;
        savedLevel = repository.getThreshold() ;
        repository.setThreshold( Level.OFF ) ;

        final int total = 10000 ;
        System.out.println( "testMemoryUsageWithoutReinit() underway: " + total + " compilations." ) ;
        // This gets a new instance of the compiler for every compilation.      
        int goodCount = 0, badCount = 0 ;

        try {
            for( int i=0; i<total; i++ ) {

                AdqlCompiler compiler = null ;
                StringReader source = null ;
                if( i%2 == 0 ) {
                    source = new StringReader( BAD_SOURCE ) ;
                }
                else {
                    source = new StringReader( GOOD_SOURCE ) ;
                }
                try {        
                    compiler = new AdqlCompiler( source ) ;
                    compiler.compileToXmlBeans();
                    goodCount++ ;
                } 
                catch ( AdqlException aex ) {        
                    if( i%2 != 0 ) {
                        System.out.println( "testMemoryUsageWithoutReinit produced: " + aex.getClass().getName() ) ;
                        String[] ms = aex.getMessages() ;
                        for( int j=0; j<ms.length; j++ ) {
                            System.out.println( ms[j] ) ;
                        }
                        assertTrue( "Unexpected exception: " + aex.getClass().getName(), false ) ;
                        break ;
                    }
                    else {
                        badCount++ ;
                    }
                }
                catch ( Throwable th ) {
                    System.out.println( "testMemoryUsageWithoutReinit produced: " + th.getClass().getName() ) ;
                    if( th instanceof OutOfMemoryError ) {
                        throw (OutOfMemoryError)th;
                    }
                    assertTrue( "Unexpected exception: " + th.getClass().getName(), false ) ;
                    break ;
                }
                if( i != 0 && i%1000 == 0 ) {
                    System.out.println( i + " finished" ) ;
                }
            }
            System.out.println( "testMemoryUsageWithoutReinit() compilations finished." ) ;     
            System.out.println( "good count: " + goodCount ) ;
            System.out.println( "bad count: " + badCount ) ;
            assertTrue( goodCount == total/2  && badCount == total/2 ) ;
        }
        finally {
            repository.setThreshold( savedLevel ) ;
        }
    }
    
    public void execFragmentHeadersAndTrailers() throws Exception {
        printHelpfulStuff( currentSFile.getName() ) ;
        System.out.println( "== From: ===>" ) ;
        printFragmentCompilationFile( currentSFile ) ;
        AdqlCompiler compiler = null ;
        String[] comments = new String[2] ;
        try {
            compiler = getCompiler( currentSFile ) ;
            SelectType select = (SelectType)compiler.execFragment( fragmentContext, comments ) ;   
            SelectDocument selectDoc = SelectDocument.Factory.newInstance() ;
            selectDoc.setSelect( select ) ;
            compiler.writeHeaderAndTrailerComments( selectDoc, selectDoc.getSelect(), comments ) ;
            printCompilation( selectDoc ) ;          
            assertTrue( currentSFile.getName() + ": Compilation succeeded when not expected.", currentXFile != null ) ; 
            compareCompilations( selectDoc, currentXFile ) ;       
        }
        catch( Exception ex ) {
            if( currentXFile != null ) {
                System.out.println( currentSFile.getName() + ": Compilation failed when not expected..." ) ;
                ex.printStackTrace() ;
            }     
            assertTrue( currentSFile.getName() + ": Compilation failed when not expected.", currentXFile == null ) ;
        }       
    }

    public void execTop() throws Exception {
       startTime = System.currentTimeMillis() ; 
    }
    
    public void execTail() throws Exception {
       endTime = System.currentTimeMillis() ;
       accumulatedTime += (endTime-startTime) ;        
    }
    
    public void reportTail() throws Exception {
        System.out.println( "Total elapse time in milliseconds: " + accumulatedTime ) ;
        System.out.println( "Average compilation time: " + accumulatedTime / testMethodCount ) ;
    }
    
	private void execTest() throws Exception {
		SelectDocument sd = null ;
		printHelpfulStuff( currentSFile.getName() ) ;
		System.out.println( "== From: ===>" ) ;
		printFullCompilationFile( currentSFile ) ;
		try {
            execTop() ;
			sd = getCompiler( currentSFile ).exec() ;	
            execTail() ;
			System.out.println( "\nCompilation suceeded..." ) ;			
			System.out.println( "==== To: ===>" ) ;
			printCompilation( sd ) ;			
			assertTrue( currentSFile.getName() + ": Compilation succeeded when not expected.", currentXFile != null ) ;	
			compareCompilations( sd, currentXFile ) ;		
		}
		catch( Exception ex ) {
            execTail() ;
            if( ex instanceof AdqlException == false  ) {
                System.out.println( "Unexpected exception thrown in compiler: " ) ;
                ex.printStackTrace() ;
                assertTrue( false ) ;
            }
            else if( currentXFile != null ) {
                System.out.println( currentSFile.getName() + ": Compilation failed when not expected..." ) ;
                assertTrue( currentSFile.getName() + ": Compilation failed when not expected.", currentXFile == null ) ;
                ex.printStackTrace() ;
            }    		
		}	
	}
    
    private void execFragment() throws Exception {
        XmlObject xmlObject = null ;
        printHelpfulStuff( currentSFile.getName() ) ;
        System.out.println( "== From: ===>" ) ;
        printFragmentCompilationFile( currentSFile ) ;
        try {
            xmlObject = getCompiler( currentSFile ).execFragment( fragmentContext ) ;   
            System.out.println( "\nCompilation suceeded..." ) ;         
            System.out.println( "==== To: ===>" ) ;
            printCompilation( xmlObject ) ;            
            assertTrue( currentSFile.getName() + ": Compilation succeeded when not expected.", currentXFile != null ) ; 
            compareCompilations( xmlObject, currentXFile ) ;       
        }
        catch( Exception ex ) {
            if( currentXFile != null ) {
                System.out.println( currentSFile.getName() + ": Compilation failed when not expected..." ) ;
                ex.printStackTrace() ;
            }     
            assertTrue( currentSFile.getName() + ": Compilation failed when not expected.", currentXFile == null ) ;
        }   
    }
	
	private void locateFilesForTest() throws InitializationException {
		currentSFile = null ;
		currentXFile = null ;
		String testMethodName = this.getName() ;
        // if not a file based test, return ...
        if( testMethodName.startsWith( "testOf_" ) == false ) {
            return ;
        }
		String[] parts = testMethodName.split("_") ;
		String path = sDirectoryOfREADME.getAbsolutePath() 
		            + File.separator 
		            + parts[1]
		            + File.separator
		            + parts[2] 
		            + ".adqls" ;
		File file = new File( path ) ;
		if( file.exists() ) {
			currentSFile = file ;
		}
		else {
			throw new InitializationException( BAD_SETUP_MESSAGE 
					                         + "Method "
					                         + testMethodName
					                         + ": corresponding .adqls file not found" ) ;
		}
		path = sDirectoryOfREADME.getAbsolutePath() 
             + File.separator 
             + parts[1]
             + File.separator
             + parts[2] 
             + ".adqlx" ;
		file = new File( path ) ;
		if( file.exists() ) {
			currentXFile = file ;
		}
	}
	
	private void compareCompilations( XmlObject xo, File xmlFile ) throws Exception {

		Document compiledDom = null ;
		Document fileDom = null ;
		
		compiledDom = DomHelper.newDocument( xo.toString() ) ;
        fileDom = DomHelper.newDocument( xmlFile ) ;
		// Normalize just to be sure 
		compiledDom.normalize();
		fileDom.normalize();

		// Using xmlunit to compare documents
		setIgnoreWhitespace(true);
		assertXMLEqual("Adql/s does not compile to what is expected!",compiledDom, fileDom);
	}
	
    private AdqlCompiler getCompiler( File file ) throws FileNotFoundException {
        if( sCompiler == null ) {
            sCompiler = new AdqlCompiler( new FileReader( file ) ) ;
            sCompiler.setSemanticProcessing( true ) ;
        }
        else {
            sCompiler.ReInit( new FileReader( file ) ) ;
        }
        return sCompiler ;
    }
    
    private AdqlCompiler getCompiler( StringReader source ) {
        if (sCompiler == null) {
            sCompiler = new AdqlCompiler(source);
            sCompiler.setSemanticProcessing( true ) ;
        }
        else {
            sCompiler.ReInit(source);
        }
        return sCompiler ;
    }
	
	private void init() throws InitializationException {
		// Assume the worst...
		sBadInitializedStatus = true ;
		
		try {
			//
			// Work out the number of tests...	
			// And also attempt to extract the directories involved...
			Method[] methodArray = this.getClass().getMethods() ;
			testMethodCount = 0 ;
			Hashtable directories = new Hashtable() ;
			for( int i=0; i<methodArray.length; i++ ) {
				String methodName = methodArray[i].getName() ;
				if( methodName.startsWith( "testOf_" ) ) {
					testMethodCount++ ;
					String[] paths = methodName.split( "_" ) ;
					directories.put( paths[1], paths[1] ) ;
				}
			}
			
			ArrayList fileList = new ArrayList() ;
			File[] fileArray = null ;
			URL readme = null ;
			
			//
			// Locate the directory that holds the README file...			
			readme = this.getClass().getResource( README ) ;
			sDirectoryOfREADME = new File( new URI( readme.toString() ) ).getParentFile() ;
			
			//
			// Count all of the files with an .adqls file extension...
			Enumeration en = directories.elements() ;
			while( en.hasMoreElements() ) {
				String directory = (String)en.nextElement() ;
				fileArray = new File( sDirectoryOfREADME.getAbsolutePath() 
						            + File.separator 
						            + directory ).listFiles( new AdqlsFilter() ) ;
				for( int j=0; j<fileArray.length; j++ ) {
					fileList.add( fileArray[j] ) ;
				}
			}
			
			//
			// If the number of methods doesn't match the number of files,
			// Then issue a warning message...
			if( testMethodCount != fileList.size() ) {
				System.out.println( "\nAdqlStoXTest. WARNING. Number of test methods does not match available test files." ) ;
				System.out.println( "   Methods count: " + testMethodCount ) ;
				System.out.println( "   File count: " + fileList.size() ) ;
				System.out.println( 
						"   You may encounter failures within a test or complete absence of some tests.\n"
					+   "   Please inspect AdqlStoXTest for test methods and your test files.\n"					
				) ;
			}			
			sBadInitializedStatus = false ;
			sInitialized = true ;
		}
		catch( Exception ex ) {
			throw new InitializationException( BAD_INIT_MESSAGE + ex.getLocalizedMessage() ) ;
		}
	
	}
	
	protected void printHelpfulStuff(String filename) {
		System.out.println("------------------------------------------------");
		System.out.println("Compiling " + filename);
		System.out.println("------------------------------------------------");
	}
	
	private void printCompilation( XmlObject xo ) {
		XmlOptions opts = new XmlOptions();
		opts.setSavePrettyPrint();
		opts.setSavePrettyPrintIndent(4);
		System.out.println(xo.xmlText(opts));
	}
	
	private void printFullCompilationFile( File file ) {
		FileReader reader = null ;
		try {
			reader = new FileReader( file ) ;
			int ch = reader.read() ;
			while( ch != -1 ) {
				System.out.print( (char)ch ) ;
				ch = reader.read() ;
			}
		    System.out.print( "\n" ) ;
		}
		catch( Exception iox ) {
			;
		}
		finally {
			try{ reader.close() ; } catch( Exception ex ) { ; }
		}
	}
    
    private void printFragmentCompilationFile( File file ) {
        FileReader reader = null ;
        StringBuffer buffer = new StringBuffer() ;
        try {
            reader = new FileReader( file ) ;
            int ch = reader.read() ;
            while( ch != -1 ) {
                System.out.print( (char)ch ) ;
                if( buffer != null ) {
                    if( ((char)ch) != '\n' ) {
                        buffer.append( (char)ch ) ;                   
                    } 
                    else {
                        fragmentContext = buffer.substring(2).trim() ;
                        buffer = null ;
                    }
                }
                ch = reader.read() ;
            }
            System.out.print( "\n" ) ;
        }
        catch( Exception iox ) {
            ;
        }
        finally {
            try{ reader.close() ; } catch( Exception ex ) { ; }
        }
    }
	
	public class InitializationException extends Exception {

		/**
		 * @param message
		 * @param cause
		 */
		public InitializationException(String message, Throwable cause) {
			super(message, cause);
		}

		/**
		 * @param message
		 */
		public InitializationException(String message) {
			super(message);
		}
		
	}
	
	private class AdqlsFilter implements FileFilter {
	
		public boolean accept( File file ) {
			if( file.getName().endsWith( "adqls" ) ) {
				return true ;
			}
			return false ;
		}
	}
	
}


/* $Log: AdqlStoXTest.java,v $
 * Revision 1.2  2009/06/17 10:13:32  jl99
 * Merge of adql v1 parser with maven 2 build.
 * Due to some necessary restructuring, the maven 1 build process has been removed.
 *
/* Revision 1.1.2.1  2009/06/16 07:49:41  jl99
/* First commit of maven 2 build
/*
/* Revision 1.17  2008/04/16 11:04:23  jl99
/* Merge of branch adql-jl-2731
/*
/* Revision 1.16.4.1  2008/04/15 14:09:56  jl99
/* (1) ROUND and TRUNCATE functions have changed cardinality.
/* (2) Some improvements to comment processing.
/*
/* Revision 1.16.2.2  2008/04/10 14:58:36  jl99
/* Removed redundant templates.
/* Updated maths and trig function templates.
/*
/* Revision 1.16.2.1  2008/04/08 11:40:01  jl99
/* Changed names of some comment tests
/*
/* Revision 1.16  2008/03/19 20:53:21  jl99
/* Merge of branch adql-jl2650
/*
/* Revision 1.15.2.1  2008/03/19 19:28:31  jl99
/* Improved comment processing.
/*
/* Revision 1.15  2008/02/04 17:47:30  jl99
/* Merge of branch adql-jl-2504
/*
/* Revision 1.14.2.3  2008/02/01 09:56:52  jl99
/* Removed 2 tests for full outer join syntax.
/*
/* Revision 1.14.2.2  2008/01/29 15:32:29  jl99
/* Added two tests to make sure we errored n-way explicit joins with n > 2
/*
/* Revision 1.14.2.1  2008/01/25 16:31:46  jl99
/* Strictly for testing and/or DSA, allowing what semantic testing exists within the parser to be turned off or on.
/*
/* Revision 1.14  2007/06/06 18:21:05  jl99
/* Merge of branch adql-jl-2135
/*
/* Revision 1.13.2.36  2007/06/06 15:03:18  jl99
/* Commented out two errant unit tests
/* These are in the area of UserDefinedFunction.
/*
/* Revision 1.13.2.35  2007/06/06 10:55:17  jl99
/* Tidy just prior to merge of branch adql-jl-2135
/*
/* Revision 1.13.2.34  2007/05/22 10:38:42  jl99
/* Refinement to user defined functions.
/*
/* Revision 1.13.2.33  2007/05/21 17:38:16  jl99
/* Unit tests added. Derived from SDSS site.
/*
/* Revision 1.13.2.32  2007/05/20 20:20:40  jl99
/* Unit tests added. Derived from SDSS site.
/*
/* Revision 1.13.2.31  2007/05/20 18:54:12  jl99
/* Unit tests added. Derived from SDSS site.
/*
/* Revision 1.13.2.30  2007/05/18 14:09:20  jl99
/* Unit tests added. Derived from SDSS site.
/*
/* Revision 1.13.2.29  2007/05/18 13:58:55  jl99
/* Unit tests added. Derived from SDSS site.
/*
/* Revision 1.13.2.28  2007/05/18 12:44:36  jl99
/* Unit tests added. Derived from SDSS site.
/*
/* Revision 1.13.2.27  2007/05/17 13:19:23  jl99
/* Minor correction.
/*
/* Revision 1.13.2.26  2007/05/17 13:16:35  jl99
/* Memory tests further enhanced.
/* Should now stop if an inappropriate/unexpected exception is thrown by any compilation.
/*
/* Revision 1.13.2.25  2007/05/17 11:28:21  jl99
/* Memory tests enhanced. Now each does 10,000 compilations.
/* Logging turned off and restored after each test.
/*
/* Revision 1.13.2.24  2007/05/11 13:59:06  jl99
/* Better cross validation position processing plus unit tests
/*
/* Revision 1.13.2.23  2007/05/10 18:57:56  jl99
/* Reorg to improve AST_ classes and memory usage: fragment processing
/*
/* Revision 1.13.2.22  2007/05/09 13:58:10  jl99
/* Reorg to improve AST_ classes and memory usage: fragment processing
/*
/* Revision 1.13.2.21  2007/05/01 08:34:25  jl99
/* Updating unit test in line with improvement in comment processing.
/*
/* Revision 1.13.2.20  2007/04/30 17:23:24  jl99
/* Better comment processing.
/*
/* Revision 1.13.2.19  2007/04/30 10:22:23  jl99
/* Better comment processing.
/*
/* Revision 1.13.2.18  2007/04/26 14:50:34  jl99
/* Strengthening unit tests
/*
/* Revision 1.13.2.17  2007/04/25 09:04:52  jl99
/* Unit tests for multiple archives
/*
/* Revision 1.13.2.16  2007/04/25 07:47:43  jl99
/* Unit tests for multiple archives
/*
/* Revision 1.13.2.15  2007/04/23 12:32:06  jl99
/* Introduction of module AdqlCompiler to contain majority of java code previously in AdqlStoX.
/* AdqlStoX is a non-java source module which must be pre-processed to produce java.
/* AdqlCompiler makes for ease of maintenance and clarity.
/*
/* Revision 1.13.2.14  2007/04/20 21:50:29  jl99
/* More fragment unit tests
/*
/* Revision 1.13.2.13  2007/04/20 20:58:50  jl99
/* More fragment unit tests
/*
/* Revision 1.13.2.12  2007/04/20 20:26:46  jl99
/* More fragment unit tests
/*
/* Revision 1.13.2.11  2007/04/20 09:42:14  jl99
/* More fragment unit tests
/*
/* Revision 1.13.2.10  2007/04/19 16:14:28  jl99
/* More fragment unit tests
/*
/* Revision 1.13.2.9  2007/04/19 12:12:33  jl99
/* More fragment unit tests
/*
/* Revision 1.13.2.8  2007/04/19 11:51:30  jl99
/* More fragment unit tests
/*
/* Revision 1.13.2.7  2007/04/17 15:45:59  jl99
/* Rationalizing multiple error reporting
/*
/* Revision 1.13.2.6  2007/04/04 20:08:37  jl99
/* More unit tests to do with compiling a fragment.
/*
/* Revision 1.13.2.5  2007/04/04 17:34:04  jl99
/* First unit test to do with compiling a fragment.
/*
/* Revision 1.13.2.4  2007/03/14 12:57:14  jl99
/* Two memory tests added: 1000 compilations each.
/*
/* Revision 1.13.2.3  2007/03/09 09:19:50  jl99
/* Added unit test for difficult unmatched brackets on a cone search.
/*
/* Revision 1.13.2.2  2007/03/08 16:28:03  jl99
/* Position Tracking
/*
/* Revision 1.13.2.1  2007/03/03 00:22:20  jl99
/* Tracking comments: principles established.
/*
/* Revision 1.13  2007/01/26 09:45:57  jl99
/* Merge of adql-jl-2031-a into HEAD
/*
/* Revision 1.12.6.2  2007/01/25 14:09:00  jl99
/* More unit tests plus instrumentation for measuring compile times.
/*
/* Revision 1.12.6.1  2007/01/19 08:34:32  jl99
/* New unit test material folded in (cone searches etc provided by Kona)
/*
/* Revision 1.12  2006/10/28 22:12:50  jl99
/* Reinstated aliased expressions tests.
/*
/* Revision 1.11  2006/10/27 14:32:20  jl99
/* Made alliases for tables compulsary.
/* Changed unit tests accordingly.
/*
/* Revision 1.10  2006/10/25 18:38:11  jl99
/* Unit tests added for reserved words used within complex queries,
/* some requiring the use of delimited ids.
/*
/* Revision 1.9  2006/10/14 16:15:56  jl99
/* like_predicate unit tests added
/*
/* Revision 1.8  2006/10/14 13:10:27  jl99
/* in_predicate unit tests added
/*
/* Revision 1.7  2006/10/13 21:00:24  jl99
/* More unit tests added.
/*
/* Revision 1.6  2006/10/13 13:24:37  jl99
/* More unit tests added.
/*
/* Revision 1.5  2006/10/11 20:36:25  jl99
/* (1) Change of signature to exec method. Now throws a ParserException.
/*
/* Revision 1.4  2006/10/02 09:05:31  jl99
/* First attempt at mavenizing project
/*
/* Revision 1.3  2006/09/28 15:08:08  jl99
/* New unit tests added.
/*
/* Revision 1.2  2006/09/28 13:35:15  jl99
/* Unit test harness established.
/*
/* Revision 1.1  2006/09/26 19:53:03  jl99
/* Initial unit test framework for AdqlStoX
/* 
 * 
*/