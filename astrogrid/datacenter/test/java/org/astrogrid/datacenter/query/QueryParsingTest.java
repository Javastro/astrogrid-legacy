package org.astrogrid.datacenter.query;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.axis.utils.XMLUtils;
import org.astrogrid.datacenter.impl.QueryFactoryImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
/** Unit / system testing for the query parser.
 * <p>
 * <B>NW</b> wondering if this test is specific to one particular implementation of the query factory / database flavour.
 * <p>
 * Fixed loading of xml files - rather than hard coding file location, or passing in directoy parameter in main (which rules out automated testing),
 * load from the classpath as resources instead.
 * <p>FUTURE - come back and look at later - not wanting to work at present.
 * @author unknown
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Aug-2003
 *
 */
public class QueryParsingTest extends TestCase {

    /** very specific to current implementation
     *  maybe we could generalize this, so it can exercise any implementation? 
     */
    public QueryFactoryImpl factory = new QueryFactoryImpl();

    /** package name of test resource files */
    public final static String RESOURCE_PREFIX="org/astrogrid/datacenter/query/inputs/";
    public Element setUpQueryElement(String fileName) throws IOException, SAXException, ParserConfigurationException {
        Element element = null;
            //DatasetAgent someDatasetAgent = new DatasetAgent();
            InputStream sourceStream = ClassLoader.getSystemResourceAsStream(RESOURCE_PREFIX + fileName);
            assertNotNull("input file is missing: " + fileName,sourceStream);
            Document document = XMLUtils.newDocument(sourceStream);

            NodeList nodeList =
                document.getDocumentElement().getElementsByTagName(
                    RunJobRequestDD.QUERY_ELEMENT);

            for (int i = 0; i < nodeList.getLength(); i++) {
 
                if (nodeList.item(i).getNodeType() != Node.ELEMENT_NODE)
                    continue;
                element = (Element) nodeList.item(i);
                if (element.getTagName().equals(RunJobRequestDD.QUERY_ELEMENT))
                    break;

            }

            if (element == null) {
                 fail("Failed to pick up a Query element");
            }
        return element;

    } // end setUpQueryElement()

    public void testQueryToString_CONE() throws Exception {

        // This is the pseudo-SQL... "SELECT URA, UDEC, PMPROB FROM USNOB WHERE CONE(234.56, -12.34, 0.01)"
        final String sqlString =
            "SELECT  URA, UDEC, PMPROB FROM USNOB..USNOB  WHERE "
                + "( ((2 * ASIN(SQRT(POW(SIN(-12.34-UDEC)/2),2) + COS(UDEC) + POW(SIN(234.56-URA)/2),2)) < 0.01 )",
            fileName = "query_CONE.xml";
        Element queryElement =
            this.setUpQueryElement( fileName);
        Query query = null;
        String resultString = null;

            query = new Query(queryElement, factory);
            resultString = query.toSQLString();
            //			logger.info( "testQueryToString_CONE: " + resultString ) ;
            assertEquals(resultString,sqlString);


    } // end of testQueryToString_CONE()

    public void testQueryToString_AND_with_EQUALS_and_NOT_EQUALS() throws Exception {

        // This is the pseudo-SQL... 
        // "SELECT "
        final String sqlString =
            "SELECT   DISTINCT COLUMN_ONE, COLUMN_TWO, COLUMN_THREE FROM USNOB..USNOB  "
                + "WHERE ( ( COLUMN_FOUR = 16 ) AND ( COLUMN_FIVE <> 11.5 ) )",
            fileName = "query_AND_EQUALS_NE.xml";
        Element queryElement =
            this.setUpQueryElement( fileName);
        Query query = null;
        String resultString = null;

            query = new Query(queryElement, factory);
            resultString = query.toSQLString();
            assertEquals(resultString,sqlString);
    } // end of testQueryToString_AND_with_EQUALS_and_NOT_EQUALS()

    public void testQueryToString_CONE_with_AND() throws Exception{


        // This is the pseudo-SQL... 
        // "SELECT URA, UDEC, PMPROB FROM USNOB WHERE CONE(234.56, -12.34, 0.01)"
        final String sqlString =
            "SELECT  URA, UDEC, PMPROB FROM USNOB..USNOB  "
                + "WHERE ( ( ((2 * ASIN(SQRT(POW(SIN(-12.34-UDEC)/2),2) "
                + "+ COS(UDEC) + POW(SIN(234.56-URA)/2),2)) < 0.01 ) AND ( PMPROB > 5 ) )",
            fileName = "query_AND_CONE_GT.xml";
        Element queryElement =
            this.setUpQueryElement( fileName);
        Query query = null;
        String resultString = null;

            query = new Query(queryElement, factory);
            resultString = query.toSQLString();
            //			logger.info( "testQueryToString_CONE_with_AND: " + resultString ) ;
            assertEquals(resultString,sqlString);


    } // end of testQueryToString_CONE_with_AND()

    public void testQueryToString_BETWEEN_BETWEEN_GT() throws Exception {

        // This is the pseudo-SQL... 
        // "SELECT URA, UDEC, PMPROB FROM USNOB WHERE CONE(234.56, -12.34, 0.01)"
        final String sqlString =
            "SELECT  URA, UDEC, NDETS FROM USNOB..USNOB  WHERE "
                + "( ( URA BETWEEN 234.56 AND 234.6 ) AND "
                + "( UDEC BETWEEN -12.34 AND -12.45 ) AND ( NDETS > 4 ) )",
            fileName = "query_AND_BETWEEN_BETWEEN_GT.xml";
        Element queryElement =
            this.setUpQueryElement( fileName);
        Query query = null;
        String resultString = null;

            query = new Query(queryElement, factory);
            resultString = query.toSQLString();
            //			logger.info( "testQueryToString_BETWEEN_BETWEEN_GT: " + resultString ) ;
            assertEquals(resultString.replaceAll(" ", ""),sqlString.replaceAll(" ", ""));
 
    } // end of testQueryToString_BETWEEN_BETWEEN_GT()

    public void testQueryToString_AND_GTE_LTE_OR_NE_GT_LT_NE() throws Exception {

        // This is the pseudo-SQL... 
        // ""
        final String sqlString =
            "SELECT  COLUMN_ONE, COLUMN_TWO FROM USNOB..USNOB  "
                + "WHERE ( ( COLUMN_6 >= 60 ) AND ( COLUMN_7 <= 70 ) AND "
                + "( ( COLUMN_11 = 110 ) OR ( COLUMN_12 <> COLUMN_13 ) ) AND "
                + "( COLUMN_8 > 80 ) AND ( COLUMN_9 < 90 ) AND ( COLUMN_10 <> 100 ) )",
            fileName = "query_AND_GTE_LTE_OR_NE_GT_LT_NE.xml";
        Element queryElement =
            this.setUpQueryElement( fileName);
        Query query = null;
        String resultString = null;

            query = new Query(queryElement, factory);
            resultString = query.toSQLString();
            //			logger.info( "testQueryToString_AND_GTE_LTE_OR_NE_GT_LT_NE: " + resultString ) ;
            assertTrue(resultString.equals(sqlString));

    } // end of testQueryToString_AND_GTE_LTE_OR_NE_GT_LT_NE()

    public void testQueryToString_AND_GT_NOT_NULL() throws Exception {

        // This is the pseudo-SQL... 
        // ""
        final String sqlString =
            "SELECT  * FROM USNOB..USNOB  WHERE ( ( TYCHO > 0 ) AND ( NMAG NOT NULL ) )",
            fileName = "query_AND_GT_NOT_NULL.xml";
        Element queryElement =
            this.setUpQueryElement( fileName);
        Query query = null;
        String resultString = null;

            query = new Query(queryElement, factory);
            resultString = query.toSQLString();
            //			logger.info( "testQueryToString_AND_GT_NOT_NULL: " + resultString ) ;
            assertTrue(resultString.equals(sqlString));


    } // end of testQueryToString_AND_GT_NOT_NULL()

    public void testQueryToString_AND_LTE_GT_LT_NE() throws Exception {
        // This is the pseudo-SQL... 
        // ""
        final String sqlString =
            "SELECT  COLUMN_ONE, COLUMN_TWO FROM USNOB..USNOB  "
                + "WHERE ( ( COLUMN_6 >= 60 ) AND ( COLUMN_7 <= 70 ) AND "
                + "( COLUMN_8 > 80 ) AND ( COLUMN_9 < 90 ) AND ( COLUMN_10 <> 100 ) )",
            fileName = "query_AND_LTE_GT_LT_NE.xml";
        Element queryElement =
            this.setUpQueryElement( fileName);
        Query query = null;
        String resultString = null;

            query = new Query(queryElement, factory);
            resultString = query.toSQLString();
            //			logger.info( "testQueryToString_AND_LTE_GT_LT_NE: " + resultString ) ;
            assertTrue(resultString.equals(sqlString));

    } // end of testQueryToString_AND_LTE_GT_LT_NE()

    public void testQueryToString_AND_OR_GT_LTE() throws Exception{
  

        // This is the pseudo-SQL... 
        // ""
        final String sqlString =
            "SELECT  COLUMN_ONE, COLUMN_TWO FROM USNOB..USNOB  WHERE "
                + "( ( ( COLUMN_SIX > 16.09 ) OR ( COLUMN_SEVEN <= 11.5 ) ) AND "
                + "( NOT ( ( COLUMN_8 > 16.09 ) OR ( COLUMN_9 <= 11.5 ) ) ) )",
            fileName = "query_AND_OR_GT_LTE.xml";
        Element queryElement =
            this.setUpQueryElement( fileName);
        Query query = null;
        String resultString = null;

            query = new Query(queryElement, factory);
            resultString = query.toSQLString();
            //			logger.info( "testQueryToString_AND_OR_GT_LTE: " + resultString ) ;
            assertTrue(resultString.equals(sqlString));

    } // end of testQueryToString_AND_OR_GT_LTE()

    public void testQueryToString_COUNT_with_no_criteria() throws Exception {

        // This is the pseudo-SQL... 
        // ""
        final String sqlString = "SELECT  COUNT(*) FROM USNOB..USNOB",
            fileName = "query_COUNT_with_no_criteria.xml";
        Element queryElement =
            this.setUpQueryElement( fileName);
        Query query = null;
        String resultString = null;

            query = new Query(queryElement, factory);
            resultString = query.toSQLString().trim();
            //			logger.info( "testQueryToString_COUNT_with_no_criteria: " + resultString ) ;
            assertTrue(resultString.equals(sqlString));


    } // end of testQueryToString_COUNT_with_no_criteria()

    public void testQueryToString_IN_with_list() throws Exception {

        // This is the pseudo-SQL... 
        // ""
        final String sqlString =
            "SELECT  COLUMN_1, COLUMN_2 FROM USNOB..USNOB  WHERE "
                + "( COLUMN_3 IN ( COLUMN_4, COLUMN_5, COLUMN_6, COLUMN_7, "
                + "COLUMN_8, COLUMN_9, COLUMN_10, COLUMN_11, COLUMN_12, COLUMN_13 ) )",
            fileName = "query_IN_with_list.xml";
        Element queryElement =
            this.setUpQueryElement( fileName);
        Query query = null;
        String resultString = null;

            query = new Query(queryElement, factory);
            resultString = query.toSQLString().trim();
            //			logger.info( "testQueryToString_IN_with_list: " + resultString ) ;
            assertTrue(resultString.equals(sqlString));
    } // end of testQueryToString_IN_with_list()

    public void testQueryToString_MIN_MAX_with_no_criteria() throws Exception{

        // This is the pseudo-SQL... 
        // ""
        final String sqlString =
            "SELECT  MIN(URA), MAX(URA), MIN(UDEC), MAX(UDEC) FROM USNOB..USNOB",
            fileName = "query_MIN_MAX_with_no_criteria.xml";
        Element queryElement =
            this.setUpQueryElement( fileName);
        Query query = null;
        String resultString = null;

            query = new Query(queryElement, factory);
            resultString = query.toSQLString().trim();
            //			logger.info( "testQueryToString_MIN_MAX_with_no_criteria: " + resultString ) ;
            assertTrue(resultString.equals(sqlString));
    } // end of testQueryToString_MIN_MAX_with_no_criteria()

    public void testQueryToString_NOT_AND_GT_LTE() throws Exception {

        // This is the pseudo-SQL... 
        // ""
        final String sqlString =
            "SELECT  COLUMN_ONE, COLUMN_TWO FROM USNOB..USNOB  WHERE "
                + "( NOT ( ( COLUMN_SIX > 16.09 ) AND ( COLUMN_SEVEN <= 11.5 ) ) )",
            fileName = "query_NOT_AND_GT_LTE.xml";
        Element queryElement =
            this.setUpQueryElement( fileName);
        Query query = null;
        String resultString = null;

            query = new Query(queryElement, factory);
            resultString = query.toSQLString().trim();
            //			logger.info( "testQueryToString_NOT_AND_GT_LTE: " + resultString ) ;
            assertTrue(resultString.equals(sqlString));

    } // end of testQueryToString_NOT_AND_GT_LTE()

    public void testQueryToString_OR_GTE_LTE_GT_LT() throws Exception{
        // This is the pseudo-SQL... 
        // ""
        final String sqlString =
            "SELECT  COLUMN_ONE, COLUMN_TWO FROM USNOB..USNOB  WHERE "
                + "( ( COLUMN_6 >= 60 ) OR ( COLUMN_7 <= 70 ) OR ( COLUMN_8 > 80 ) OR ( COLUMN_9 < 90 ) )",
            fileName = "query_OR_GTE_LTE_GT_LT.xml";
        Element queryElement =
            this.setUpQueryElement( fileName);
        Query query = null;
         String resultString = null;
            query = new Query(queryElement, factory);
            resultString = query.toSQLString().trim();
            //			logger.info( "testQueryToString_OR_GTE_LTE_GT_LT: " + resultString ) ;
            assertTrue(resultString.equals(sqlString));

    } // end of testQueryToString_OR_GTE_LTE_GT_LT()

    public void testQueryToString_OR_GT_AND_GT_LTE() throws Exception{

        // This is the pseudo-SQL... 
        // ""
        final String sqlString =
            "SELECT  COLUMN_ONE, COLUMN_TWO, COLUMN_THREE FROM USNOB..USNOB  WHERE "
                + "( ( COLUMN_FOUR > 16 ) OR ( ( COLUMN_SIX > 16.09 ) AND ( COLUMN_SEVEN <= 11.5 ) ) )",
            fileName = "query_OR_GT_AND_GT_LTE.xml";
        Element queryElement =
            this.setUpQueryElement( fileName);
        Query query = null;
        String resultString = null;

            query = new Query(queryElement, factory);
            resultString = query.toSQLString().trim();
            //			logger.info( "testQueryToString_OR_GT_AND_GT_LTE: " + resultString ) ;
            assertTrue(resultString.equals(sqlString));
 
    } // end of testQueryToString_OR_GTE_LTE_GT_LT()

    public void testQueryToString_LIKE() throws Exception {
        // This is the pseudo-SQL... 
        // ""
        final String sqlString =
            "SELECT  COLUMN_ONE, COLUMN_TWO, COLUMN_THREE FROM USNOB..USNOB  WHERE "
                + "( COLUMN_FOUR LIKE '%star' )",
            fileName = "query_LIKE.xml";
        Element queryElement =
            this.setUpQueryElement( fileName);
        Query query = null;
        String resultString = null;

            query = new Query(queryElement, factory);
            resultString = query.toSQLString().trim();
            // logger.info( "testQueryToString_LIKE: " + resultString ) ;
            assertTrue(resultString.equals(sqlString));

    } // end of testQueryToString_LIKE()	

    public void testQueryToString_LIKE_AND_LIKE() throws Exception {

        // This is the pseudo-SQL... 
        // ""
        final String sqlString =
            "SELECT  COLUMN_ONE, COLUMN_TWO, COLUMN_THREE FROM USNOB..USNOB  WHERE "
                + "( ( COLUMN_FOUR LIKE '%star' ) AND ( COLUMN_FIVE LIKE 'nebula%' ) )",
            fileName = "query_LIKE_AND_LIKE.xml";
        Element queryElement =
            this.setUpQueryElement( fileName);
        Query query = null;
        String resultString = null;

            query = new Query(queryElement, factory);
            resultString = query.toSQLString().trim();
            // logger.info( "testQueryToString_LIKE_AND_LIKE: " + resultString ) ;
            assertTrue(resultString.equals(sqlString));

    } // end of testQueryToString_LIKE_AND_LIKE()	

    public void testQueryToString_NOT_LIKE() throws Exception{
        // This is the pseudo-SQL... 
        // ""
        final String sqlString =
            "SELECT  COLUMN_ONE, COLUMN_TWO, COLUMN_THREE FROM USNOB..USNOB  WHERE "
                + "( NOT ( COLUMN_FOUR LIKE '%star' ) )",
            fileName = "query_NOT_LIKE.xml";
        Element queryElement =
            this.setUpQueryElement( fileName);
        Query query = null;
        String resultString = null;

            query = new Query(queryElement, factory);
            resultString = query.toSQLString().trim();
            // logger.info( "testQueryToString_NOT_LIKE: " + resultString ) ;
            assertTrue(resultString.equals(sqlString));
    } // end of testQueryToString_NOT_LIKE()	

    public void testQueryToString_PLUS() throws Exception {

        // This is the pseudo-SQL... 
        // ""
        final String sqlString =
            "SELECT  COLUMN_ONE, COLUMN_TWO FROM USNOB..USNOB  WHERE "
                + "( COLUMN_THREE + COLUMN_FOUR )",
            fileName = "query_PLUS.xml";
        Element queryElement =
            this.setUpQueryElement( fileName);
        Query query = null;
        String resultString = null;

            query = new Query(queryElement, factory);
            resultString = query.toSQLString().trim();
            // logger.info( "testQueryToString_PLUS: " + resultString ) ;
            assertTrue(resultString.equals(sqlString));

    } // end of testQueryToString_PLUS()

    public void testQueryToString_MINUS() throws Exception {
  
        // This is the pseudo-SQL... 
        // ""
        final String sqlString =
            "SELECT  COLUMN_ONE, COLUMN_TWO FROM USNOB..USNOB  WHERE "
                + "( COLUMN_THREE - COLUMN_FOUR - COLUMN_FIVE )",
            fileName = "query_MINUS.xml";
        Element queryElement =
            this.setUpQueryElement( fileName);
        Query query = null;
        String resultString = null;

            query = new Query(queryElement, factory);
            resultString = query.toSQLString().trim();
            // logger.info( "testQueryToString_MINUS: " + resultString ) ;
            assertTrue(resultString.equals(sqlString));

    } // end of testQueryToString_MINUS()	

    public void testQueryToString_ARITHMETIC_PASSTHROUGH() throws Exception {

        // This is the pseudo-SQL... 
        // ""
        final String sqlString =
            "SELECT  COLUMN_ONE, COLUMN_TWO, COLUMN_THREE FROM USNOB..USNOB  WHERE "
                + "( COLUMN_FOUR + COLUMN_FIVE > COLUMN_SIX )",
            fileName = "query_COLUMN_ARITHMETIC_PASSTHROUGH.xml";
        Element queryElement =
            this.setUpQueryElement( fileName);
        Query query = null;
        String resultString = null;

            query = new Query(queryElement, factory);
            resultString = query.toSQLString().trim();

            assertTrue(resultString.equals(sqlString));
    } // end of testQueryToString_ARITHMETIC_PASSTHROUGH()	

    public void testQueryToString_ORDER_BY() throws Exception{
        // This is the pseudo-SQL... 
        // ""
        final String sqlString =
            "SELECT  COLUMN_ONE, COLUMN_TWO, COLUMN_THREE FROM USNOB..USNOB  WHERE "
                + "( COLUMN_FOUR > COLUMN_FIVE ) ORDER BY   COLUMN_ONE DESC  ,  COLUMN_TWO DESC  ,  COLUMN_THREE ASC",
            fileName = "query_ORDER_BY_DESC.xml";
        Element queryElement =
            this.setUpQueryElement( fileName);
        Query query = null;
        String resultString = null;

            query = new Query(queryElement, factory);
            resultString = query.toSQLString().trim();
            // logger.info( "testQueryToString_ORDER_BY: " + resultString ) ;
            assertTrue(resultString.equals(sqlString));

    } // end of testQueryToString_ORDER_BY()

    public void testQueryToString_GROUP_BY() throws Exception {
        // This is the pseudo-SQL... 
        // ""
        final String sqlString =
            "SELECT  COLUMN_ONE, COLUMN_TWO, COLUMN_THREE FROM USNOB..USNOB  WHERE "
                + "( COLUMN_FOUR > COLUMN_FIVE ) GROUP BY  COLUMN_ONE, COLUMN_TWO",
            fileName = "query_GROUP_BY.xml";
        Element queryElement =
            this.setUpQueryElement( fileName);
        Query query = null;
        String resultString = null;

            query = new Query(queryElement, factory);
            resultString = query.toSQLString().trim();
            // logger.info( "testQueryToString_GROUP_BY: " + resultString ) ;
            assertTrue(resultString.equals(sqlString));


    } // end of testQueryToString_GROUP_BY()

    public void testQueryToString_IN_WITH_SUBQUERY() throws Exception {

        // This is the pseudo-SQL... 
        // ""
        final String sqlString =
            "SELECT  COLUMN_ONE, COLUMN_TWO FROM USNOB..USNOB  WHERE "
                + "( COLUMN_THREE IN  (  SELECT  COLUMN_FOUR FROM USNOB..USNOB "
                + " WHERE ( ( COLUMN_FIVE = COLUMN_SIX ) AND ( COLUMN_SEVEN > 1000 ) ) )  )",
            fileName = "query_IN_with_SUBQUERY.xml";
        Element queryElement =
            this.setUpQueryElement( fileName);
        Query query = null;
        String resultString = null;

            query = new Query(queryElement, factory);
            resultString = query.toSQLString().trim();
            // logger.info( "testQueryToString_IN_WITH_SUBQUERY: " + resultString ) ;
            assertTrue(resultString.equals(sqlString));

    } // end of testQueryToString_IN_WITH_SUBQUERY()

    public void testQueryToString_EXISTS_WITH_SUBQUERY() throws Exception{

        // This is the pseudo-SQL... 
        // ""
        final String sqlString =
            "SELECT  COLUMN_ONE, COLUMN_TWO FROM USNOB..USNOB  WHERE "
                + "( COLUMN_THREE EXISTS  (  SELECT  COLUMN_FOUR FROM USNOB..USNOB "
                + " WHERE ( ( COLUMN_FIVE = COLUMN_SIX ) AND ( COLUMN_SEVEN > 1000 ) ) )  )",
            fileName = "query_EXISTS_with_SUBQUERY.xml";
        Element queryElement =
            this.setUpQueryElement( fileName);
        Query query = null;
        String resultString = null;

            query = new Query(queryElement, factory);
            resultString = query.toSQLString().trim();
            // logger.info( "testQueryToString_EXISTS_WITH_SUBQUERY: " + resultString ) ;
            assertTrue(resultString.equals(sqlString));


    } // end of testQueryToString_EXISTS_WITH_SUBQUERY()    

    public void testQueryToString_COLUMN_ARITMETIC_PLUS_GT_MINUS() throws Exception {
        // This is the pseudo-SQL... 
        // ""
        final String sqlString =
            "SELECT  * FROM USNOB..USNOB  WHERE "
                + "( ( ( COLUMN_ONE + COLUMN_TWO ) ) > ( ( COLUMN_THREE - COLUMN_FOUR - COLUMN_FIVE ) ) )",
            fileName = "query_COLUMN_ARITHMETIC_PLUS_GT_MINUS.xml";
        Element queryElement =
            this.setUpQueryElement( fileName);
        Query query = null;
        String resultString = null;

            query = new Query(queryElement, factory);
            resultString = query.toSQLString().trim();
            // logger.info( "testQueryToString_COLUMN_ARITMETIC_PLUS_GT_MINUS: " + resultString ) ;
            assertTrue(resultString.equals(sqlString));

    } // end of testQueryToString_COLUMN_ARITMETIC_PLUS_GT_MINUS()   

    public void testQueryToString_COLUMN_ARITMETIC_MULTIPLY() throws Exception{
  
        // This is the pseudo-SQL... 
        // ""
        final String sqlString =
            "SELECT  * FROM USNOB..USNOB  WHERE "
                + "( ( ( COLUMN_ONE * COLUMN_TWO ) ) > ( ( COLUMN_THREE * COLUMN_FOUR * 1000 ) ) )",
            fileName = "query_COLUMN_ARITHMETIC_MULTIPLY.xml";
        Element queryElement =
            this.setUpQueryElement( fileName);
        Query query = null;
        String resultString = null;

            query = new Query(queryElement, factory);
            resultString = query.toSQLString().trim();
            // logger.info( "testQueryToString_COLUMN_ARITMETIC_MULTIPLY: " + resultString ) ;
            assertTrue(resultString.equals(sqlString));


    } // end of testQueryToString_COLUMN_ARITMETIC_MULTIPLY()  	

    public void testQueryToString_COLUMN_ARITMETIC_DIVIDE() throws Exception{

        // This is the pseudo-SQL... 
        // ""
        final String sqlString =
            "SELECT  * FROM USNOB..USNOB  WHERE "
                + "( ( ( COLUMN_ONE / COLUMN_TWO )  / 10  ) = ( ( COLUMN_THREE / COLUMN_FOUR / 1000 ) ) )",
            fileName = "query_COLUMN_ARITHMETIC_DIVIDE.xml";
        Element queryElement =
            this.setUpQueryElement( fileName);
        Query query = null;
        String resultString = null;

            query = new Query(queryElement, factory);
            resultString = query.toSQLString().trim();
            // logger.info( "testQueryToString_COLUMN_ARITMETIC_DIVIDE: " + resultString ) ;
            assertTrue(resultString.equals(sqlString));

    } // end of testQueryToString_COLUMN_ARITMETIC_DIVIDE()	

    public void testQueryToString_MIN_MAX_AVG() throws Exception {
        // This is the pseudo-SQL... 
        // ""
        final String sqlString =
            "SELECT  COLUMN_ONE FROM USNOB..USNOB  WHERE "
                + "( ( COLUMN_TWO >  AVG(COLUMN_THREE)  ) AND (  MAX(COLUMN_FOUR)  <  MIN(COLUMN_FIVE)  ) )",
            fileName = "query_MIN_MAX_AVG.xml";
        Element queryElement =
            this.setUpQueryElement( fileName);
        Query query = null;
        String resultString = null;

            query = new Query(queryElement, factory);
            resultString = query.toSQLString().trim();
            // logger.info( "testQueryToString_COLUMN_ARITMETIC_DIVIDE: " + resultString ) ;
            assertTrue(resultString.equals(sqlString));
 

    } // end of testQueryToString_MIN_MAX_AVG()	

    /**
     * Assembles and returns a test suite for
     * all the test methods of this test case.
     *
     * @return A non-null test suite.
     */
    public static Test suite() {
        // Reflection is used here to add all the testXXX() methods to the suite.
        return new TestSuite(QueryParsingTest.class);
    }

    /**
     * Runs the test case.
     */
    public static void main(String args[]) {

            junit.textui.TestRunner.run(suite());
    }

} // end of class QueryTestSuite