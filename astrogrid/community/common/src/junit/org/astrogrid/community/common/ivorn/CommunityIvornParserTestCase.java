package org.astrogrid.community.common.ivorn ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import java.net.URL ;
import java.net.URI ;
import java.net.URISyntaxException ;
import java.net.MalformedURLException ;

import junit.framework.TestCase ;

import org.astrogrid.config.SimpleConfig;
import org.astrogrid.store.Ivorn ;

/**
 * A test case to verify the CommunityIvornParser.
 *
 */
public class CommunityIvornParserTestCase
    extends TestCase
    {
    /**
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(CommunityIvornParserTestCase.class);

    /**
     * Array of test data containing ivorn values and expected results.
     * { ivron, community-ident, account-ident, path, fragment, remainder, community-ivorn, account-ivorn,}
     *
     */
    private String data[][] = 
        {
            {
            "ivo://org.astrogrid.test/frog",
            "org.astrogrid.test/community",
            "frog",
            "org.astrogrid.test/frog",
            null,
            null,
            null,
            "ivo://org.astrogrid.test/community",
            "ivo://frog@org.astrogrid.test/community"
            },
            {
            "ivo://org.astrogrid.test/frog/extra",
            "org.astrogrid.test/community",
            "frog",
            "org.astrogrid.test/frog",
            "extra",
            null,
            "/extra",
            "ivo://org.astrogrid.test/community",
            "ivo://frog@org.astrogrid.test/community"
            },
            {
            "ivo://org.astrogrid.test/frog#toad",
            "org.astrogrid.test/community",
            "frog",
            "org.astrogrid.test/frog",
            null,
            "toad",
            "#toad",
            "ivo://org.astrogrid.test/community",
            "ivo://frog@org.astrogrid.test/community"
            },
            {
            "ivo://org.astrogrid.test/frog/extra#toad",
            "org.astrogrid.test/community",
            "frog",
            "org.astrogrid.test/frog",
            "extra",
            "toad",
            "/extra#toad",
            "ivo://org.astrogrid.test/community",
            "ivo://frog@org.astrogrid.test/community"
            },
            {
            "ivo://org.astrogrid.test/frog/extra#toad/extra",
            "org.astrogrid.test/community",
            "frog",
            "org.astrogrid.test/frog",
            "extra",
            "toad/extra",
            "/extra#toad/extra",
            "ivo://org.astrogrid.test/community",
            "ivo://frog@org.astrogrid.test/community"
            },

            {
            "ivo://org.astrogrid.test#frog",
            "org.astrogrid.test/community",
            "frog",
            "org.astrogrid.test/frog",
            null,
            null,
            null,
            "ivo://org.astrogrid.test/community",
            "ivo://frog@org.astrogrid.test/community"
            },
            {
            "ivo://org.astrogrid.test#frog/extra",
            "org.astrogrid.test/community",
            "frog",
            "org.astrogrid.test/frog",
            null,
            "extra",
            "#extra",
            "ivo://org.astrogrid.test/community",
            "ivo://frog@org.astrogrid.test/community"
            },

            {
            "ivo://frog@org.astrogrid.test",
            "org.astrogrid.test",
            "frog",
            "org.astrogrid.test/frog",
            null,
            null,
            null,
            "ivo://org.astrogrid.test",
            "ivo://frog@org.astrogrid.test"
            },
            {
            "ivo://frog@org.astrogrid.test/toad",
            "org.astrogrid.test/toad",
            "frog",
            "org.astrogrid.test/frog",
            "toad",
            null,
            "/toad",
            "ivo://org.astrogrid.test/toad",
            "ivo://frog@org.astrogrid.test/toad"
            },
            {
            "ivo://frog@org.astrogrid.test/toad/extra",
            "org.astrogrid.test/toad/extra",
            "frog",
            "org.astrogrid.test/frog",
            "toad/extra",
            null,
            "/toad/extra",
            "ivo://org.astrogrid.test/toad/extra",
            "ivo://frog@org.astrogrid.test/toad/extra"
            },
            {
            "ivo://frog@org.astrogrid.test/toad/extra#newt",
            "org.astrogrid.test/toad/extra",
            "frog",
            "org.astrogrid.test/frog",
            "toad/extra",
            "newt",
            "/toad/extra#newt",
            "ivo://org.astrogrid.test/toad/extra",
            "ivo://frog@org.astrogrid.test/toad/extra"
            },
            {
            "ivo://frog@org.astrogrid.test/toad/extra#newt/extra",
            "org.astrogrid.test/toad/extra",
            "frog",
            "org.astrogrid.test/frog",
            "toad/extra",
            "newt/extra",
            "/toad/extra#newt/extra",
            "ivo://org.astrogrid.test/toad/extra",
            "ivo://frog@org.astrogrid.test/toad/extra"
            }
        } ;

    /**
     * Test that we can handle a null Ivorn.
     *
    public void testNullIvorn()
        throws Exception
        {
        System.out.println("") ;
        System.out.println("----\"----") ;
        System.out.println("CommunityIvornParserTestCase.testNullIvorn()") ;
        //
        // Create an IvornResolver.
        CommunityIvornParser parser = new CommunityIvornParser() ;
        //
        // Check the community ident.
        assertEquals(
            "Community part not null",
            null,
            parser.getCommunityIdent()
            ) ;
        //
        // Check the account ident.
        assertEquals(
            "Account part not null",
            null,
            parser.getAccountIdent()
            ) ;
        }
     */

    /**
     * Test that we can resolve our test Community Ivorn(s).
     *
     */
    public void testCommunityIvorn()
        throws Exception
        {
        System.out.println("") ;
        System.out.println("----\"----") ;
        System.out.println("CommunityIvornParserTestCase.testCommunityIvorn()") ;
        for (int i = 0 ; i < data.length ; i++)
            { 
            testCommunityIvorn(data[i]) ;
            }
        }

    /**
     * Test that we can resolve a Community Ivorn.
     *
     */
    public void testCommunityIvorn(String data[])
        throws Exception
        {
        System.out.println("") ;
        System.out.println("----\"----") ;
        System.out.println("CommunityIvornParserTestCase.testCommunityIvorn()") ;
        System.out.println("  Ivorn : " + data[0]) ;
        //
        // Create an Ivorn and IvornResolver.
        CommunityIvornParser parser = new CommunityIvornParser(
            new Ivorn(data[0])
            ) ;
        //
        // Check the community ident.
        assertEquals(
            "Community name not equal",
            data[1],
            parser.getCommunityName()
            ) ;
        //
        // Check the account ident.
        assertEquals(
            "Account name not equal",
            data[2],
            parser.getAccountName()
            ) ;
/*
 *
        //
        // Check the account ident.
        assertEquals(
            "Account ident not equal",
            data[3],
            parser.getAccountIdent()
            ) ;
 *
 */
        //
        // Check the ivorn path.
        assertEquals(
            "Path not equal",
            data[4],
            parser.getPath()
            ) ;
        //
        // Check the ivorn fragment.
        assertEquals(
            "Fragment not equal",
            data[5],
            parser.getFragment()
            ) ;
        //
        // Check the ivorn remainder.
        assertEquals(
            "Remainder not equal",
            data[6],
            parser.getRemainder()
            ) ;

        //
        // Check the community ident.
        assertEquals(
            "Community ident not equal",
            data[7],
            parser.getCommunityIdent()
            ) ;
        //
        // Check the community ivorn.
/*
 *
        assertEquals(
            "Community ivorn not equal",
            data[7],
            ((null != parser.getCommunityIvorn()) ? parser.getCommunityIvorn().toString() : null)
            ) ;
 *
 */
        
 /*
        //
        // Check the account ident.
        assertEquals(
            "Account ident not equal",
            data[8],
            parser.getAccountIdent()
            ) ;
  */
        
        //
        // Check the account ivorn.
        assertEquals(
            "Account ivorn not equal",
            data[8],
            ((null != parser.getAccountIvorn()) ? parser.getAccountIvorn().toString() : null)
            ) ;
        
        // Check the user name (the community service depends on this
        // being right in order to find accounts in its DB).
        assertEquals(
            "User name not equal",
            data[2],
            parser.getAccountName()
            );
        }

    /**
     * Tests an account IVORN with the local-commmunity configuration.
     */
    public void testLocalAccount() throws Exception {
      SimpleConfig.getSingleton().setProperty(CommunityIvornParser.LOCAL_COMMUNITY_PROPERTY,
                                              "org.astrogrid.local.community/community");
        
      Ivorn ivorn = new Ivorn("ivo://frog@org.astrogrid.local.community/community#/fu/bar/baz");
      CommunityIvornParser sut = new CommunityIvornParser(ivorn);
      assertEquals("frog", sut.getAccountName());
      assertEquals("org.astrogrid.local.community/community", sut.getCommunityName());
      assertEquals("/fu/bar/baz", sut.getFragment());
    }
    
    /**
     * Test a local Ivorn.
     *
     */
    public void testLocalCommunity()
        throws Exception
        {
        System.out.println("") ;
        System.out.println("----\"----") ;
        System.out.println("testLocalCommunity");
        
        SimpleConfig.getSingleton().setProperty(CommunityIvornParser.LOCAL_COMMUNITY_PROPERTY,
                                                "org.astrogrid.local.community/community");
        
        
        //
        // Check a local Ivorn
        assertTrue(
            "Didn't recognise local ident",
            new CommunityIvornParser(
                new Ivorn(
                    "ivo://org.astrogrid.local.community/frog"
                    )
                ).isLocal()
            ) ;
        }

  /**
   * Tests the parsing when the input is an IVORN string without the ivo://
   * prefix. Sadly, this form seems to be normal input to the PolicyManager
   * web-service.
   */
  public void testHeadless() throws Exception {
    System.out.println("testHeadless()");
    SimpleConfig.getSingleton().setProperty(CommunityIvornParser.LOCAL_COMMUNITY_PROPERTY,
                                            "org.astrogrid.local.community/community");
        
    String ident = "frog@org.astrogrid.local.community/community#/fu/bar/baz";
    CommunityIvornParser sut = new CommunityIvornParser(ident);
    assertEquals("frog", sut.getAccountName());
    assertEquals("org.astrogrid.local.community/community", sut.getCommunityName());
    assertEquals("/fu/bar/baz", sut.getFragment());
  }

}

