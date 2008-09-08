/*$Id: AdqlStoXTest.java,v 1.2 2008/09/08 15:37:12 jl99 Exp $
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.ListIterator;

import org.astrogrid.adql.metadata.*;
import org.astrogrid.adql.AdqlParser.SyntaxOption ;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.astrogrid.adql.beans.SelectDocument;
import org.astrogrid.xml.DomHelper;
import org.custommonkey.xmlunit.XMLTestCase;
import org.w3c.dom.Document;
import org.apache.xmlbeans.XmlCursor;

import org.apache.log4j.Logger ;
import org.apache.log4j.Level ;
import org.apache.log4j.spi.LoggerRepository;
import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

/**
 * AdqlStoXTest
 *
 *
 * @author Jeff Lusted jl99@star.le.ac.uk
 * Sep 26, 2006
 */
public class AdqlStoXTest extends XMLTestCase {
    
    private static Log log = LogFactory.getLog( AdqlStoXTest.class ) ;
	
	final static String README = "README_FOR_QUERIES" ;
	final static String BAD_INIT_MESSAGE = "AdqlStoXTest. Initialization failed: " ;
	final static String BAD_SETUP_MESSAGE = "AdqlStoXTest. Method setUp() failed: " ;
    
    final static String BAD_SOURCE = "SELECT las.sourceID, las.ra, las.\"dec\", fsc.seqNo, fsc.ra, fsc.\"dec\"" +
                                     "FROM lasSource AS las, ROSAT..rosat_fsc AS fsc, lasSourceXrosat_fsc AS x" +
                                     "WHERE x.masterObjID=las.sourceID AND x.slaveObjID=fsc.seqNo AND" + 
                                     "x.distanceMins<0.1 AND;" ;
    final static String GOOD_SOURCE = "Select * From catalogue as a Where (a.POS_EQ_RA<100) And (( a.POS_EQ_RA>100 )" +   
                                      "And (ACOS((( SIN( a.POS_EQ_DEC ) * SIN( 100 ) ) + (COS(a.POS_EQ_DEC) * " +  
                                      "( COS(100)  * COS( (a.POS_EQ_RA - 100) ) )))) <= 100 ));" ;

    private static AdqlParser sParser = null ;
//    private static Container metadata ; 
	private static boolean sInitialized = false ; 
	private static boolean sBadInitializedStatus = false ;
	private static File sDirectoryOfREADME = null ;
    
    public static HashMap sImplicitNamespaces = new HashMap() ;
    static {
        sImplicitNamespaces.put( "adql", "http://www.ivoa.net/xml/v2.0/adql" ) ;    
        sImplicitNamespaces.put( "xs", "http://www.w3.org/2001/XMLSchema" ) ; 
        sImplicitNamespaces.put( "stc", "http://www.ivoa.net/xml/v1.0/stc" ) ; 
        sImplicitNamespaces.put( "xsi", "http://www.w3.org/2001/XMLSchema-instance" ) ; 
    }
	
	private File currentSFile ; 
	private File currentXFile ;
    
    private String fragmentContext ;
    
    private ConvertADQL convertor ;
    
    private static long accumulatedTime ;
    private static int testMethodCount ;
    private long startTime ;
    private long endTime ;
    
    public AdqlStoXTest() {
        super() ;
        System.setProperty( "javax.xml.transform.TransformerFactory"
                           ,"com.icl.saxon.TransformerFactoryImpl" ) ;
    }

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
    
    public void testOfAcquiringSQLReservedWords() {
        String[] rws = AdqlParser.getSQLReservedWords() ;
        System.out.println( "Number of SQL reserved words: " + rws.length ) ;
        System.out.println( "First five: " 
                          + rws[0] + " "  + rws[1] + " " + rws[2] + " " + rws[3] + " " + rws[4] ) ;       
        assertTrue( ("\"" + rws[0] + "\"" ).equalsIgnoreCase( AdqlStoXConstants.tokenImage[AdqlStoXConstants.ABSOLUTE] ) ) ;
        assertTrue( ("\"" + rws[ rws.length-1 ] + "\"" ).equalsIgnoreCase( AdqlStoXConstants.tokenImage[AdqlStoXConstants.ZONE] ) ) ;
    }
    
    public void testOfAcquiringADQLReservedWords() {
        String[] rws = AdqlParser.getADQLReservedWords() ;
        System.out.println( "Number of ADQL reserved words: " + rws.length ) ;
        System.out.println( "First five: " 
                          + rws[0] + " "  + rws[1] + " " + rws[2] + " " + rws[3] + " " + rws[4] ) ;       
        assertTrue( ("\"" + rws[0] + "\"" ).equalsIgnoreCase( AdqlStoXConstants.tokenImage[AdqlStoXConstants.AREA] ) ) ;
        assertTrue( ("\"" + rws[ rws.length-1 ] + "\"" ).equalsIgnoreCase( AdqlStoXConstants.tokenImage[AdqlStoXConstants.TRUNCATE] ) ) ;
    }
    
    public void testOf_v20_area01() throws Exception { execTest() ; }
    public void testOf_v10_BADdelimitedIdentifier() throws Exception { execTest() ; }
    public void testOf_v10_BADduplicatedAliases() throws Exception { execTest() ; }
	public void testOf_v10_BADemptyFrom() throws Exception { execTest() ; }
	public void testOf_v10_BADemptyWhere() throws Exception { execTest() ; }
    public void testOf_v10_BADextremelyComplexQuery() throws Exception { execTest() ; }
    
    public void testOf_v10_BADinnerJoinWithJoinCondition() throws Exception { 
        setReferenceImplementationSupport() ;
        execTest() ; 
        setDefaultSupport() ; 
    }
    
    public void testOf_v10_BADNestedBracketWithinConeSearch01() throws Exception { execTest() ; }
    public void testOf_v10_BADregion() throws Exception { execTest() ; }
    public void testOf_v10_BADregularIdentifier() throws Exception { execTest() ; }
    public void testOf_v10_BADreservedWordAsTableName() throws Exception { execTest() ; }
	public void testOf_v10_BADselectEmptyAlias() throws Exception { execTest() ; }
    public void testOf_v10_BADMAselectList() throws Exception { execTest() ; }
	public void testOf_v10_BADselectOrderByDirOnly() throws Exception { execTest() ; }
    
    public void testOf_v10_BADthreeWayJoinAcrossArchives() throws Exception { 
        setReferenceImplementationSupport() ;
        execTest() ; 
        setDefaultSupport() ; 
    }
    
    public void testOf_v10_BADtop() throws Exception { execTest() ; }
    public void testOf_v10_ceilingFunction() throws Exception { execTest() ; }
    public void testOf_v20_centroidInCircle() throws Exception { execTest() ; }
    public void testOf_v10_comments01() throws Exception { execTest() ; }
    public void testOf_v10_comments02() throws Exception { execTest() ; }
    public void testOf_v10weeds_comments03() throws Exception { execTest() ; }
    public void testOf_v10_commentsMultipleLines04() throws Exception { execTest() ; }
    public void testOf_v20_comparisonPredicateOnCircle() throws Exception { execTest() ; }
    public void testOf_v10weeds_complexSelect01() throws Exception { execTest() ; }
    public void testOf_v10_coneSearch01() throws Exception { execTest() ; }
    public void testOf_v10_coneSearch02() throws Exception { execTest() ; }
    public void testOf_v20_coordsysRegion() throws Exception { execTest() ; }
    public void testOf_v20_distanceFromPoint() throws Exception { execTest() ; }
    public void testOf_v10_delimitedIdentifier() throws Exception { execTest() ; }
    public void testOf_v20_existsPredWithJoinOn() throws Exception { execTest() ; }
    public void testOf_v20_existsPredWithJoinUsing() throws Exception { execTest() ; }
    public void testOf_v20_existsPredWithSelect() throws Exception { execTest() ; }
    public void testOf_v20_fromwithderivedtable() throws Exception { execTest() ; }
    //
    // testOf_v20_fromwithderivedtableWithNoCorrelation:
    // Error message could do with improving
    public void testOf_v20_fromwithderivedtableWithNoCorrelation() throws Exception { execTest() ; }
    public void testOf_v20_fromwithtablesjoin() throws Exception { execTest() ; }
    public void testOf_v10_fullOuterJoinWithJoinCondition() throws Exception { execTest() ; }
    public void testOf_v10_groupByOneColumn() throws Exception { execTest() ; }
    public void testOf_v10_innerJoinWithJoinCondition() throws Exception { execTest() ; }
    public void testOf_v20_inPredicateListComplex() throws Exception { execTest() ; }
    public void testOf_v10_inPredicateWithConstantStringList() throws Exception { execTest() ; }
    public void testOf_v10_inPredicateWithSubQuery() throws Exception { execTest() ; }
    public void testOf_v20_joinTableDefaultInner() throws Exception { execTest() ; }
    public void testOf_v20_joinTableNaturalDefaultInner() throws Exception { execTest() ; }
    public void testOf_v20_joinTableUsingColumnList() throws Exception { execTest() ; }
    public void testOf_v20_joinTableWithAlias() throws Exception { execTest() ; }
    public void testOf_v10_leftOuterJoinWithJoinCondition() throws Exception { execTest() ; }
    public void testOf_v10_likeWithoutBracket() throws Exception { execTest() ; }
    public void testOf_v20_likeWithoutBracketComplex() throws Exception { execTest() ; }
    public void testOf_v10_likeWithBracket() throws Exception { execTest() ; }
    public void testOf_v10_MAFullOuterJoinWithJoinCondition() throws Exception { execTest() ; }
    public void testOf_v10_MAInnerJoinWithJoinCondition() throws Exception { execTest() ; }
    public void testOf_v10_MALeftOuterJoinWithJoinCondition() throws Exception { execTest() ; }
    public void testOf_v10_MAThreeWayJoin() throws Exception { execTest() ; }
    public void testOf_v10_MAThreeWayJoinNonAliased() throws Exception { execTest() ; }
    public void testOf_v20_miscellaneousGeometry01() throws Exception { execTest() ; }
    public void testOf_v20_miscellaneousGeometry02() throws Exception { execTest() ; }
    public void testOf_v20_miscellaneousGeometry03() throws Exception { execTest() ; }
    public void testOf_v20_miscellaneousGeometry04() throws Exception { execTest() ; }
    public void testOf_v20_miscellaneousGeometry05() throws Exception { execTest() ; }
    public void testOf_v20_miscellaneousGeometry06() throws Exception { execTest() ; }
    public void testOf_v20_miscellaneousGeometry07() throws Exception { execTest() ; }
    public void testOf_v20_miscellaneousGeometry08() throws Exception { execTest() ; }
    public void testOf_v10_naiveConeSearch() throws Exception { execTest() ; }
    public void testOf_v20_notExistsPredWithComplexSubQuery() throws Exception { execTest() ; }
    public void testOf_v20_notExistsPredWithJoinOn() throws Exception { execTest() ; }
    public void testOf_v10_notLikeWithBracket() throws Exception { execTest() ; }
    public void testOf_v10_notLikeWithoutBracket() throws Exception { execTest() ; }
    public void testOf_v10_notInPredicateWithConstantStringList() throws Exception { execTest() ; }
    public void testOf_v20_notInPredicateListJoin() throws Exception { execTest() ; }
    public void testOf_v10_notInPredicateWithSubQuery() throws Exception { execTest() ; }
    public void testOf_v20_nullAndNotNullPredicate() throws Exception { execTest() ; }
    public void testOf_v20_regionCircleJ2000() throws Exception { execTest() ; }
    public void testOf_v20_regionCircleJ2000WithColRefs() throws Exception { execTest() ; }
    public void testOf_v20_regionNotInBoxJ2000() throws Exception { execTest() ; }
    public void testOf_v20_regionPolyJ2000WithColRefs() throws Exception { execTest() ; }
    public void testOf_v20_regionWithSTCStringPlusConcatenation() throws Exception { execTest() ; }
    public void testOf_v10_regularIdentifier() throws Exception { execTest() ; }
    public void testOf_v10_SDSSBasicSelectFromWhere() throws Exception { execTest() ; }
    public void testOf_v10_SDSSGalaxiesWithTwoCriteria() throws Exception { execTest() ; }
    public void testOf_v10_SDSSGalaxiesWithMultipleCriteria() throws Exception { execTest() ; }
    public void testOf_v10_SDSSSpatialUnitVectors() throws Exception { execTest() ; }
    public void testOf_v10_SDSSCataclysmicVariablesUsingColors() throws Exception { execTest() ; }
    
    // Has bit processing...
    public void testOf_v10_SDSSDataSubSample() throws Exception { 
        setReferenceImplementationSupport() ;
        execTest() ; 
        setDefaultSupport() ; 
    }
    
    public void testOf_v10_SDSSLowzQSOsUsingColors() throws Exception { execTest() ; }
    public void testOf_v10_SDSSObjectVelocitiesAndErrors() throws Exception { execTest() ; }
    public void testOf_v10_SDSSUsingBetween() throws Exception { execTest() ; }
    public void testOf_v10_SDSSMovingAsteroids() throws Exception { execTest() ; }
    public void testOf_v10_SDSSQuasarsInImaging() throws Exception { execTest() ; }
    public void testOf_v10_SDSSQSOsInSpectroscopy() throws Exception { execTest() ; }  
    // Has bit processing and use of str function...
    public void testOf_v10_SDSSSelectedNeighborsInRun() throws Exception { execTest() ; }
    // Contains a case...
    public void testOf_v10_SDSSObjectCountingAndLogic() throws Exception { execTest() ; }
    public void testOf_v10_SDSSGalaxiesBlendedWithStars() throws Exception { execTest() ; }
    public void testOf_v10_SDSSStarsInSpecificFields() throws Exception { execTest() ; }
    
    // Uses bit processing...
    public void testOf_v10_SDSSUsingThreeTables() throws Exception { 
        setReferenceImplementationSupport() ;
        execTest() ; 
        setDefaultSupport() ; 
    }
    
    public void testOf_v10_SDSSObjectsClosePairs() throws Exception { execTest() ; }
    
    // Uses bit processing and user defined function...
    public void testOf_v10_SDSSErrorsUsingFlags() throws Exception { 
        setReferenceImplementationSupport() ;
        execTest() ; 
        setDefaultSupport() ; 
    }
    
    // Uses bit processing and user defined function...
    public void testOf_v10_SDSSEllipticalGalaxiesBasedOnModelFits() throws Exception { 
        setReferenceImplementationSupport() ;
        execTest() ; 
        setDefaultSupport() ; 
    }
       
    public void testOf_v10_SDSSUnclassifiedSpectra() throws Exception { execTest() ; }
	public void testOf_v10_selectAggregateFuncs() throws Exception { execTest() ; }
	public void testOf_v10_selectAliasExpr() throws Exception { execTest() ; } 
    public void testOf_v10_selectAll() throws Exception { execTest() ; }
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
    public void testOf_v10weeds_selectOrderByCol() throws Exception { execTest() ; }
    public void testOf_v10weeds_BADselectOrderByComplex() throws Exception { execTest() ; }
    public void testOf_v20_selectOrderByComplex() throws Exception { execTest() ; }
    public void testOf_v20_selectCentroid() throws Exception { execTest() ; }
    public void testOf_v20_selectListWithAsteriskQualification() throws Exception { execTest() ; }
    public void testOf_v10_selectSome() throws Exception { execTest() ; }
    public void testOf_v10_selectTrigFuncsDeg() throws Exception { execTest() ; }
    public void testOf_v10_selectTrigFuncsRad() throws Exception { execTest() ; }
    public void testOf_v10_selectTwoTablesFourCols() throws Exception { execTest() ; }
    public void testOf_v10_selectTwoTablesFourColsNoAlias() throws Exception { execTest() ; }
    public void testOf_v10_selectUnaries() throws Exception { execTest() ; }
    public void testOf_v10_selectValueTweakMathsFuncs() throws Exception { execTest() ; }
    public void testOf_v20_selectWithStringConcatenation01() throws Exception { execTest() ; }
    public void testOf_v10_spectralLines() throws Exception { execTest() ; }
    public void testOf_v10_squaringTheCircle() throws Exception { execTest() ; }
    public void testOf_v10_threeWayJoin() throws Exception { execTest() ; }
    public void testOf_v20_udfLikeWithoutBrackets() throws Exception { execTest() ; }
    public void testOf_v20_useOfCOORDSYSfunction() throws Exception { execTest() ; }
    public void testOf_v10_whereWithBinaryOpsAndUnaryOps() throws Exception { execTest() ; }
    public void testOf_v10_whereWithNegativeUnaryLiteral() throws Exception { execTest() ; }
    public void testOf_v10_whereWithPositiveUnaryLiteral() throws Exception { execTest() ; }
    public void testOf_v20_whereWithStringConcatenation01() throws Exception { execTest() ; }
    public void testOf_v10_whereWithUnsignedNumericLiteral() throws Exception { execTest() ; reportTail() ; }
    
    //
    // Extensions beyond the ADQL version 2.0 standard... 
    public void testOf_v20extensions_SDSSDataSubSample() throws Exception { execTest() ; }
    public void testOf_v20extensions_SDSSDiameterLimitedSample() throws Exception { execTest() ; }
    public void testOf_v20extensions_SDSSEllipticalGalaxiesBasedOnModelFits() throws Exception { execTest() ; }
    public void testOf_v20extensions_SDSSErrorsUsingFlags() throws Exception { execTest() ; }
    public void testOf_v20extensions_SDSSExtremelyRedGalaxies() throws Exception { execTest() ; }
    public void testOf_v20extensions_SDSSGalaxiesWithBlueCenters() throws Exception { execTest() ; }
    public void testOf_v20extensions_SDSSLRGSample() throws Exception { execTest() ; }
    public void testOf_v20extensions_SDSSQSOsInSpectroscopy() throws Exception { execTest() ; }
    public void testOf_v20extensions_SDSSUnclassifiedSpectra() throws Exception { execTest() ; }
    public void testOf_v20extensions_SDSSUsingThreeTables() throws Exception { execTest() ; }
    public void testOf_v20extensions_UKIDSSComplexWithCast() throws Exception { execTest() ; }
    public void testOf_v20extensions_UKIDSSDerivedTableAndLongInList() throws Exception { execTest() ; }
    
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
    public void testOf_v10weeds_FragmentLikePattern() throws Exception { execFragment() ; }
    public void testOf_v10_FragmentMATablesArrayOfFromTableType() throws Exception { execFragment() ; }
    public void testOf_v10_FragmentNaiveConeSearch() throws Exception { execFragment() ; }
    public void testOf_v10weeds_FragmentOrderBy() throws Exception { execFragment() ; }
    public void testOf_v10weeds_FragmentOrderByItem() throws Exception { execFragment() ; }
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
    public void testOf_v10weeds_FragmentSetItem() throws Exception { execFragment() ; }
    public void testOf_v10weeds_FragmentSubsetSpectralLines() throws Exception { execFragment() ; }
    public void testOf_v10weeds_FragmentTableCondition() throws Exception { execFragment() ; }
    public void testOf_v10_FragmentTablesArrayOfFromTableType() throws Exception { execFragment() ; }
    public void testOf_v10_FragmentTableTableType() throws Exception { execFragment() ; }
    public void testOf_v10_FragmentTop() throws Exception { execFragment() ; }
    public void testOf_v10_FragmentWhereCondition() throws Exception { execFragment() ; } // Uncertain of result.
    public void testOf_v10_FragmentWithHeadAndTrailComments() throws Exception { execFragmentHeadersAndTrailers() ; }

    public void testOptionSettingsReference() throws Exception {
        sParser.setStrictSyntax( AdqlParser.V20 ) ;
        assertTrue( sParser.isStrictSyntax( AdqlParser.V20 ) ) ;
        assertFalse( sParser.isStrictSyntax( AdqlParser.V20_AGX ) ) ;
        assertTrue( sParser.isSyntaxSet( AdqlParser.V20 ) ) ;
    }
    
    public void testOptionSettingsAstrogrid() throws Exception {
        sParser.setStrictSyntax( AdqlParser.V20_AGX ) ;
        assertTrue( sParser.isStrictSyntax( AdqlParser.V20_AGX ) ) ;
        assertFalse( sParser.isStrictSyntax( AdqlParser.V20 ) ) ;
        assertTrue( sParser.isSyntaxSet( AdqlParser.V20_AGX ) ) ;
    }
    
    public void testOptionSettingsVarious() throws Exception {
        sParser.resetSyntax() ;
        sParser.setSyntax( SyntaxOption.BITWISE_MANIPULATION ) ;
        assertTrue( sParser.isSyntaxSet( SyntaxOption.BITWISE_MANIPULATION ) ) ;
        assertFalse( sParser.isSyntaxSet( SyntaxOption.BOX ) ) ;
        assertFalse( sParser.isSyntaxSet( new SyntaxOption[] { SyntaxOption.AREA, SyntaxOption.LAX_SCHEMA } ) ) ;
    }
    
    // JL note:
    // need to try the next two tests for memory on separate threading basis as well.
    public void testMemoryUsageWithReinit() throws OutOfMemoryError {
        // NB: (JL) I've tried this with the setting set to 1000000 and it worked.
        //
        // Turn off all logging.
        // Precaution in case the log level is set inappropriately,
        // in which case we would be testing the ability of the logging
        // system to survive rather than the parser.
        // A single parse at trace level can produce many hundreds of lines of print.
        Level savedLevel ;
        Logger logger = Logger.getRootLogger();
        LoggerRepository repository = logger.getLoggerRepository() ;
        savedLevel = repository.getThreshold() ;
        repository.setThreshold( Level.OFF ) ;

        //
        // Set options to the widest syntax...
        sParser.setSyntax( AdqlParser.V20_X ) ;
        final int total = 10000 ;
        System.out.println( "testMemoryUsageWithReinit() underway: " + total + " queries." ) ;
        // This one continues to use the parser instance used so far in all the above tests.   
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
                    getParser( source ).parseToXmlBeans();
                    goodCount++ ;
                } 
                catch ( AdqlException aex ) {               
                    if( i%2 != 0 ) {
                        System.out.println( "testMemoryUsageWithReinit produced: " + aex.getClass().getName() ) ;
                        String[] ms = aex.getErrorMessages() ;
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
            System.out.println( "testMemoryUsageWithReinit() finished." ) ;
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
        // system to survive rather than the parser.
        // A single parse at trace level can produce many hundreds of lines of print.
        Level savedLevel ;
        Logger logger = Logger.getRootLogger();
        LoggerRepository repository = logger.getLoggerRepository() ;
        savedLevel = repository.getThreshold() ;
        repository.setThreshold( Level.OFF ) ;

        final int total = 10000 ;
        System.out.println( "testMemoryUsageWithoutReinit() underway: " + total + " queries." ) ;
        // This gets a new instance of the parser for every query.      
        int goodCount = 0, badCount = 0 ;

        try {
            for( int i=0; i<total; i++ ) {

                AdqlParser parser = null ;
                StringReader source = null ;
                if( i%2 == 0 ) {
                    source = new StringReader( BAD_SOURCE ) ;
                }
                else {
                    source = new StringReader( GOOD_SOURCE ) ;
                }
                try {        
                    parser = new AdqlParser( source ) ;
                    parser.parseToXmlBeans();
                    goodCount++ ;
                } 
                catch ( AdqlException aex ) {        
                    if( i%2 != 0 ) {
                        System.out.println( "testMemoryUsageWithoutReinit produced: " + aex.getClass().getName() ) ;
                        String[] ms = aex.getErrorMessages() ;
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
            System.out.println( "testMemoryUsageWithoutReinit() finished." ) ;     
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
        printFragmentParseFile( currentSFile ) ;
        AdqlParser parser = null ;
        String[] comments = new String[2] ;
        try {
            parser = getParser( currentSFile ) ;
            //
            // NB: This test currently applies only to a top level select...
            SelectDocument selectDoc ;
            SelectDocument.Select select = (SelectDocument.Select)parser.execFragment( fragmentContext, comments ) ;   
            XmlCursor cursor = select.newCursor() ;
            cursor.toParent() ;
            selectDoc = (SelectDocument)cursor.getObject() ;
            selectDoc.setSelect( select ) ;
            parser.writeHeaderAndTrailerComments( selectDoc, selectDoc.getSelect(), comments ) ;
            printParse( selectDoc ) ;          
            assertTrue( currentSFile.getName() + ": Parse succeeded when not expected.", currentXFile != null ) ; 
            compareParsings( selectDoc, currentXFile ) ;                  
        }
        catch( Exception ex ) {
            if( currentXFile != null ) {
                System.out.println( currentSFile.getName() + ": Parse failed when not expected..." ) ;
                ex.printStackTrace() ;
            }     
            assertTrue( currentSFile.getName() + ": Parse failed when not expected.", currentXFile == null ) ;
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
        System.out.println( "Average parse time: " + accumulatedTime / testMethodCount ) ;
    }
    
	private void execTest() throws Exception {
		SelectDocument sd = null ;
		printHelpfulStuff( currentSFile.getName() ) ;
		System.out.println( "== From: ===>" ) ;
		printFullParseFile( currentSFile ) ;
		try {
            execTop() ;
			sd = getParser( currentSFile ).exec() ;	
            execTail() ;
			System.out.println( "\nParse suceeded..." ) ;			
			System.out.println( "==== To: ===>" ) ;
			printParse( sd ) ;			
			assertTrue( currentSFile.getName() + ": Parse succeeded when not expected.", currentXFile != null ) ;	
			if( log.isDebugEnabled() ) {
                if( currentXFile != null ) {
                    log.debug( "currentXFile: " + currentXFile.getName() ) ;
                }
            }
            compareParsings( sd, currentXFile ) ;		
		}
		catch( Exception ex ) {
            execTail() ;
            if( ex instanceof AdqlException == false  ) {
                System.out.println( "Unexpected exception thrown in parser: " ) ;
                ex.printStackTrace() ;
                assertTrue( false ) ;
            }
            else if( currentXFile != null ) {
                System.out.println( currentSFile.getName() + ": Parse failed when not expected..." ) ;
                assertTrue( currentSFile.getName() + ": Parse failed when not expected.", currentXFile == null ) ;
                ex.printStackTrace() ;
            }    		
		}	
	}
    
    private void execFragment() throws Exception {
        XmlObject xmlObject = null ;
        printHelpfulStuff( currentSFile.getName() ) ;
        System.out.println( "== From: ===>" ) ;
        printFragmentParseFile( currentSFile ) ;
        try {
            xmlObject = getParser( currentSFile ).execFragment( fragmentContext ) ;   
            System.out.println( "\nParse suceeded..." ) ;         
            System.out.println( "==== To: ===>" ) ;
            printParse( xmlObject ) ;            
            assertTrue( currentSFile.getName() + ": Parse succeeded when not expected.", currentXFile != null ) ; 
            compareParsings( xmlObject, currentXFile ) ;       
        }
        catch( Exception ex ) {
            if( currentXFile != null ) {
                System.out.println( currentSFile.getName() + ": Parse failed when not expected..." ) ;
                ex.printStackTrace() ;
            }     
            assertTrue( currentSFile.getName() + ": Parse failed when not expected.", currentXFile == null ) ;
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
	
	private void compareParsings( XmlObject xo, File xmlFile ) throws Exception {
        //
        // This rather extended rigmarole is what I've had to do (partly!)
        // to control namespace occurances in an instance, 
        // and also text and white space...
        XmlOptions opts = getCompareOptions() ;
	    String parsedVersion = xo.xmlText( opts ) ;
                
        String fileContents = retrieveFile( xmlFile ) ;
//        if( log.isDebugEnabled() ) {
//            log.debug( "fileContents: " + fileContents ) ;
//        }
        String namespace = ConvertADQL.getCovertibleNameSpace( fileContents ) ;
        String controlledVersion ;
        String convertedXml ;
        if( namespace != null ) {
            System.out.println( "Xml file requires converting. Namespace: " + namespace ) ;
            convertedXml = getConvertor().convertV10ToV20( new StringReader( fileContents ) ) ;
            controlledVersion = XmlObject.Factory.parse( convertedXml ).xmlText( opts ) ;
            System.out.println( "Converts to: " ) ;
            System.out.println( controlledVersion + "\n==") ;
        }
        else {
            System.out.println( "Xml file does not require conversion." ) ;
            controlledVersion = XmlObject.Factory.parse( fileContents ).xmlText( opts ) ;
        }

        Document compiledDom = DomHelper.newDocument( parsedVersion ) ;
        Document fileDom = DomHelper.newDocument( controlledVersion ) ;

        // Normalize just to be sure 
        compiledDom.normalize();
        fileDom.normalize();
        
        // Using xmlunit to compare documents
        assertXMLEqual("Adql/s does not parse to what is expected!", compiledDom, fileDom) ;

	}
    
    private XmlOptions getCompareOptions() {
        //return sParser.getSaveOptions( true ) ;
        XmlOptions opts = new XmlOptions();
        opts.setSavePrettyPrint();
        opts.setSavePrettyPrintIndent(4);
        return opts ;
    }
       
    private void setReferenceImplementationSupport() {
        sParser.setStrictSyntax( AdqlParser.V20 ) ;
    }
    
    private void setDefaultSupport() {
        sParser.setSyntax( AdqlParser.V20_X ) ;        
    }
    
    private AdqlParser getParser( File file ) throws FileNotFoundException {
        if( sParser == null ) {
            sParser = new AdqlParser( new FileReader( file ) ) ;
            sParser.setSemanticProcessing( true ) ;
        }
        else {
            sParser.ReInit( new FileReader( file ) ) ;
        }
        if( currentSFile.getName().indexOf( "SDSS" ) != -1 ) {
            sParser.setUserDefinedFunctionPrefix( "f" ) ;
        }
        else {
            sParser.resetUserDefinedFunctionPrefix() ;
        }
        return sParser ;
    }
    
    private AdqlParser getParser( StringReader source ) {
        if (sParser == null) {
            sParser = new AdqlParser(source);
        }
        else {
            sParser.ReInit(source);
        }
        return sParser ;
    }
    
    private ConvertADQL getConvertor() {
        if ( this.convertor == null ) {
            this.convertor = new ConvertADQL() ;
        }
        return this.convertor ;
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
//			readme = this.getClass().getResource( README ) ;
            readme = this.getClass().getClassLoader().getResource( README ) ;
			sDirectoryOfREADME = new File( new URI( readme.toString() ) ).getParentFile() ;
			
			//
			// Count all of the files with an .adqls file extension...
            if( log.isDebugEnabled() ) {
                log.debug( "directories.size() :" + directories.size() ) ;
            }
                       
			Enumeration en = directories.elements() ;
			while( en.hasMoreElements() ) {
				String directory = (String)en.nextElement() ;
                String path = sDirectoryOfREADME.getAbsolutePath() + File.separator + directory ;
				if( log.isDebugEnabled() ) {
				    log.debug( "path: " + path ) ;
                }
                fileArray = new File( path ).listFiles( new AdqlsFilter() ) ;
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
                
                //
                // Attempt to find any files without methods
                // (Any others will show up in the unit tests)...
                HashSet ms = new HashSet() ;
                for( int i=0; i<methodArray.length; i++ ) {
                    ms.add( methodArray[i].getName() ) ;
                }
                
                ListIterator it = fileList.listIterator() ;
                while( it.hasNext() ) {
                    File file = (File)it.next() ;
                    String mf = file.getName().split( "\\." )[0] ;
                    String md = file.getParentFile().getName() ;
                    String methodName = "testOf_" + md + '_' + mf ;
                    if( !ms.contains( methodName ) ) {
                        System.out.println( "Method missing for: " + file.getPath() ) ;
                    }
                    
                }
                    
			}			
			sBadInitializedStatus = false ;
			sInitialized = true ;
		}
		catch( Exception ex ) {
            ex.printStackTrace() ;
			throw new InitializationException( BAD_INIT_MESSAGE + ex.getLocalizedMessage() ) ;
		}
	
	}
	
	protected void printHelpfulStuff(String filename) {
		System.out.println("------------------------------------------------");
		System.out.println("Parsing " + filename);
		System.out.println("------------------------------------------------");
	}
	
	private void printParse( XmlObject xo ) {
		XmlOptions opts = new XmlOptions();
		opts.setSavePrettyPrint();
		opts.setSavePrettyPrintIndent(4);
		System.out.println(xo.xmlText(opts));
	}
	
	private void printFullParseFile( File file ) {
        String fileContent = retrieveFile( file ) ;
        System.out.print( fileContent ) ;
        System.out.print( "\n" ) ;
	}
    
    private String retrieveFile( File file ) {
        FileReader reader = null ;
        StringBuffer buffer = new StringBuffer( 1024 ) ;
        try {
            reader = new FileReader( file ) ;
            int ch = reader.read() ;
            while( ch != -1 ) {
                buffer.append( (char)ch ) ;
                ch = reader.read() ;
            }
        }
        catch( Exception iox ) {
            ;
        }
        finally {
            try{ reader.close() ; } catch( Exception ex ) { ; }
        }
        return buffer.toString() ;
    }
    
    private void printFragmentParseFile( File file ) {
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

//    /* (non-Javadoc)
//     * @see org.astrogrid.adql.metadata.MetadataQuery#isColumn(java.lang.String, java.lang.String)
//     */
//    public boolean isColumn(String tableName, String columnName) {
//        return metadata.isColumn( columnName, new String[] { tableName } ) ;
//    }
//
//    /* (non-Javadoc)
//     * @see org.astrogrid.adql.metadata.MetadataQuery#isTable(java.lang.String)
//     */
//    public boolean isTable(String tableName) {
//        return metadata.isTable( tableName );
//    }
//    
//    /* (non-Javadoc)
//     * @see org.astrogrid.adql.metadata.MetadataQuery#getFunctionCardinality(java.lang.String)
//     */
//    public int[] getFunctionCardinality(String functionName) {
//        return metadata.getFunction( functionName ).getCardinality() ;
//    }
//
//    /* (non-Javadoc)
//     * @see org.astrogrid.adql.metadata.MetadataQuery#isFunction(java.lang.String)
//     */
//    public boolean isFunction(String functionName) {
//        return metadata.isFunction( functionName );
//    }
}


/* $Log: AdqlStoXTest.java,v $
 * Revision 1.2  2008/09/08 15:37:12  jl99
 * Merge of branch adql_jl_2575_mark2 into HEAD
 *
/* Revision 1.1.2.5  2008/09/08 11:10:28  jl99
/* Change to where test queries are held within project.
/*
/* Revision 1.1.2.4  2008/09/06 09:52:10  jl99
/* Added more tests, one with cast.
/*
/* Revision 1.1.2.3  2008/09/05 07:23:25  jl99
/* Documentation
/*
/* Revision 1.1.2.2  2008/08/31 10:11:49  jl99
/* Renaming option settings and default now the reference version
/*
/* Revision 1.1.2.1  2008/08/29 14:49:07  jl99
/* First mass commit for the new project adql2
/*
/* Revision 1.15.2.24  2008/08/01 18:47:35  jl99
/* Complete reorg for purposes of setting up a maven build
/*
/* Revision 1.15.2.23  2008/07/25 18:15:45  jl99
/* Tidy.
/*
/* Revision 1.15.2.22  2008/07/21 09:48:08  jl99
/* Refinement of option settings plus unit tests
/*
/* Revision 1.15.2.21  2008/07/18 17:12:05  jl99
/* Refinement of option settings plus unit tests
/*
/* Revision 1.15.2.20  2008/07/17 10:34:03  jl99
/* Correction to processing table.* type syntax
/*
/* Revision 1.15.2.19  2008/07/15 08:49:04  jl99
/* Parser renamed from AdqlCompiler to AdqlParser
/*
/* Revision 1.15.2.18  2008/07/14 12:15:47  jl99
/* Rejigged options to use type safe enums and bet settings.
/*
/* Revision 1.15.2.17  2008/07/12 21:49:19  jl99
/* Added two unit tests for retrieval of SQL reserved words and ADQL reserved words.
/*
/* Revision 1.15.2.16  2008/07/08 16:24:30  jl99
/* Added SDSS tests for extensions beyond v2.0 standard
/*
/* Revision 1.15.2.15  2008/07/08 10:57:51  jl99
/* Delving into bit processing and bit/hex literals: tidy.
/* Note. There are twists that makes this more complex than I thought:
/* (1) Operator precedence
/* (2) Mixing expressions (bit string literals and numerics?).
/*
/* Revision 1.15.2.14  2008/07/05 11:21:52  jl99
/* Delving into bit processing and bit/hex literals
/*
/* Revision 1.15.2.13  2008/07/04 09:14:19  jl99
/* Starting to implement beyond ADQL standard, protected by option settings
/*
/* Revision 1.15.2.12  2008/06/27 15:26:53  jl99
/* Added notInPredicateListJoin test
/*
/* Revision 1.15.2.11  2008/06/26 20:37:00  jl99
/* Rationalized Function Types + more unit tests
/*
/* Revision 1.15.2.10  2008/06/26 11:34:41  jl99
/* Exists, Not Exists and some geometry function tests added
/*
/* Revision 1.15.2.9  2008/06/19 09:14:05  jl99
/* More unit tests added
/*
/* Revision 1.15.2.8  2008/06/12 19:06:20  jl99
/* Intermediate results.
/*
/* Revision 1.15.2.7  2008/06/11 11:06:38  jl99
/* Fixed multiple tables in from where one is a join
/*
/* Revision 1.15.2.6  2008/06/04 17:53:18  jl99
/* (i) some type safety for function return values in region/geometry
/* (ii) removed the possibility of using comparison and between predicates on geometries.
/*
/* Revision 1.15.2.5  2008/06/03 10:04:57  jl99
/* Code tidy
/*
/* Revision 1.15.2.4  2008/05/27 09:05:47  jl99
/* Additional unit tests covering (i) joins (ii) region
/*
/* Revision 1.15.2.3  2008/05/26 20:15:06  jl99
/* Update to join in unit tests
/*
/* Revision 1.15.2.2  2008/05/08 16:14:12  jl99
/* Improvements to semantic and metadata checks
/*
/* Revision 1.15.2.1  2008/02/27 12:40:28  jl99
/* Changes to Region
/*
/* Revision 1.15  2007/08/21 09:28:38  jl99
/* Simplification of Region: Polygon
/*
/* Revision 1.14  2007/08/20 17:30:57  jl99
/* Simplification of Region: Box
/*
/* Revision 1.13  2007/08/20 16:34:05  jl99
/* Simplification of Region.
/*
/* Revision 1.12  2007/08/16 22:30:15  jl99
/* Refinements to the use of Query Expression
/*
/* Revision 1.11  2007/08/10 11:59:52  jl99
/* Correction to LIKE predicate when used with a user defined function.
/*
/* Revision 1.10  2007/08/07 17:37:05  jl99
/* Initial multi-threaded test environment for AdqlCompilerSV
/*
/* Revision 1.9  2007/08/06 16:12:06  jl99
/* Some test file name changes
/*
/* Revision 1.8  2007/08/06 15:10:42  jl99
/* Converting fragments.
/*
/* Revision 1.7  2007/08/06 12:36:36  jl99
/* Converting fragments.
/*
/* Revision 1.6  2007/08/06 11:26:02  jl99
/* First attempts at converting fragments.
/*
/* Revision 1.5  2007/08/02 17:07:33  jl99
/* Partial reorg of test directories between versions.
/*
/* Revision 1.4  2007/07/30 14:38:38  jl99
/* Attempting to compare newer compilations against control output files from previous version.
/*
/* Revision 1.3  2007/07/30 10:57:30  jl99
/* Attempting conversion of expected results to account for change in version number and namespace
/*
/* Revision 1.2  2007/07/12 13:45:07  jl99
/* Accommodating top Select element for fragmet processing.
/* Explicit Select element required for version number.
/*
/* Revision 1.1  2007/06/28 09:07:49  jl99
/* Creation of temporary project adql2 to explore complexities of moving
/* ADQL to conform to the draft spec of April 2007.
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