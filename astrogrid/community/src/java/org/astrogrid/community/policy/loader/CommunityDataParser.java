/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/policy/loader/Attic/CommunityDataParser.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2003/11/06 15:35:26 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityDataParser.java,v $
 *   Revision 1.2  2003/11/06 15:35:26  dave
 *   Replaced tabs with spaces
 *
 *   Revision 1.1  2003/09/24 15:47:38  dave
 *   Added policy database loader tools.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.policy.loader ;

import org.xml.sax.SAXException ;

import org.astrogrid.community.common.util.xml.sax.SAXElementHandler ;
import org.astrogrid.community.common.util.xml.sax.SAXDocumentHandler ;
import org.astrogrid.community.common.util.xml.sax.SAXAttributeHandler ;
import org.astrogrid.community.common.util.xml.sax.SAXCharacterHandler ;

import org.astrogrid.community.policy.data.CommunityData ;

import org.astrogrid.community.policy.server.PolicyManager ;

/**
 * An XML parser for handling Community data.
 *
 */
public class CommunityDataParser
    extends SAXElementHandler
    {
    /**
     * Switch for our debug statements.
     *
     */
    protected static final boolean DEBUG_FLAG = true ;

    /**
     * A reference to our parent parser.
     *
     */
    private PolicyDataParser parent ;

    /**
     * Access to our parent parser.
     *
     */
    public PolicyDataParser getParent()
        {
        return this.parent ;
        }

    /**
     * Public constructor.
     *
     */
    public CommunityDataParser(PolicyDataParser parent)
        {
        this(parent, "community") ;
        }

    /**
     * Public constructor.
     *
     */
    public CommunityDataParser(PolicyDataParser parent, String name)
        {
        super(name) ;
        this.parent = parent ;
        }

    /**
     * Initialise our parser tree.
     *
     */
    public void init()
        {
        //
        // Initialise our CommunityIdentParser.
        super.init() ;
        //
        // Add an attrib handler for our ident.
        addAttributeHandler(
            new SAXAttributeHandler("ident")
                {
                public void parseAttribute(String value)
                    {
                    setIdent(value) ;
                    }
                }
            ) ;
        //
        // Add an character handler for our description.
        addElementHandler(
            new SAXElementHandler("description")
                {
                public void init()
                    {
                    setCharacterHandler(
                        new SAXCharacterHandler()
                            {
                            public void parseText(String value)
                                throws SAXException
                                {
                                if (null != community)
                                    {
                                    if (DEBUG_FLAG) System.out.println("  PASS : Got community") ;
                                    community.setDescription(value) ;
                                    }
                                else {
                                    if (DEBUG_FLAG) System.out.println("  FAIL : Null community") ;
                                    }
                                }
                            }
                        ) ;
                    }
                }
            ) ;

        //
        // Add an character handler for our service URL.
        addElementHandler(
            new SAXElementHandler("service")
                {
                public void init()
                    {
                    setCharacterHandler(
                        new SAXCharacterHandler()
                            {
                            public void parseText(String value)
                                throws SAXException
                                {
                                if (null != community)
                                    {
                                    if (DEBUG_FLAG) System.out.println("  PASS : Got community") ;
                                    community.setServiceUrl(value) ;
                                    }
                                else {
                                    if (DEBUG_FLAG) System.out.println("  FAIL : Null community") ;
                                    }
                                }
                            }
                        ) ;
                    }
                }
            ) ;

        //
        // Add an character handler for our manager URL.
        addElementHandler(
            new SAXElementHandler("manager")
                {
                public void init()
                    {
                    setCharacterHandler(
                        new SAXCharacterHandler()
                            {
                            public void parseText(String value)
                                throws SAXException
                                {
                                if (null != community)
                                    {
                                    if (DEBUG_FLAG) System.out.println("  PASS : Got community") ;
                                    community.setManagerUrl(value) ;
                                    }
                                else {
                                    if (DEBUG_FLAG) System.out.println("  FAIL : Null community") ;
                                    }
                                }
                            }
                        ) ;
                    }
                }
            ) ;

        //
        // Add an character handler for our authentication URL.
        addElementHandler(
            new SAXElementHandler("authentication")
                {
                public void init()
                    {
                    setCharacterHandler(
                        new SAXCharacterHandler()
                            {
                            public void parseText(String value)
                                throws SAXException
                                {
                                if (null != community)
                                    {
                                    if (DEBUG_FLAG) System.out.println("  PASS : Got community") ;
                                    community.setAuthenticationUrl(value) ;
                                    }
                                else {
                                    if (DEBUG_FLAG) System.out.println("  FAIL : Null community") ;
                                    }
                                }
                            }
                        ) ;
                    }
                }
            ) ;
        }

    /**
     * Reference to our PolicyManager.
     *
     */
    private PolicyManager manager ;

    /**
     * Access to our PolicyManager.
     *
     */
    public PolicyManager getManager()
        {
        if (null == manager)
            {
            if (null != parent)
                {
                //
                // Get the PolicyManager from our parent.
                manager = parent.getManager() ;
                }
            }
        return this.manager ;
        }

    /**
     * Our current Community data.
     *
     */
    protected CommunityData community ;

    /**
     * Handle the start of our <community> element.
     * Re-sets our CommunityData.
     *
     */
    protected void startElement()
        throws SAXException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityDataParser.startElement()") ;

        //
        // Reset our community data.
        community = null ;

        if (DEBUG_FLAG) System.out.println("----\"----") ;
        }

    /**
     * Handle the end of our <community> element.
     * Calls our PolicyManager to update the server with our CommunityData.
     *
     */
    protected void closeElement()
        throws SAXException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityDataParser.closeElement()") ;

        //
        // If we have an Community.
        if (null != community)
            {
            if (DEBUG_FLAG) System.out.println("  PASS : Got community") ;
            //
            // Try updating the database
            if (null != getManager())
                {
                if (DEBUG_FLAG) System.out.println("  PASS : Got manager") ;
                try {
                    community = getManager().setCommunity(community) ;
                    }
                catch (Exception ouch)
                    {
                    if (DEBUG_FLAG) System.out.println("  FAIL : Exception while creating Community") ;
                    ouch.printStackTrace() ;
                    }
                if (null != community)
                    {
                    if (DEBUG_FLAG) System.out.println("  PASS : Updated community") ;
                    }
                }
            }
        //
        // If we don't have an Community.
        else {
            if (DEBUG_FLAG) System.out.println("  FAIL : Null community") ;
            }

        if (DEBUG_FLAG) System.out.println("----\"----") ;
        }

    /**
     * Handle our Community ident.
     * Calls our PolicyManager to load or create our CommunityData.
     * Creates a new Community if it does not exist.
     *
     */
    protected void setIdent(String ident)
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityDataParser.setIdent()") ;
        if (DEBUG_FLAG) System.out.println("  Ident : " + ident) ;

        if (null != getManager())
            {
            //
            // See if we can load the Community.
            if (null == community)
                {
                if (DEBUG_FLAG) System.out.println("  Loading community ....") ;
                try {
                    community = getManager().getCommunity(ident) ;
                    }
                catch (Exception ouch)
                    {
                    if (DEBUG_FLAG) System.out.println("  FAIL : Exception while loading Community") ;
                    ouch.printStackTrace() ;
                    }
                }

            //
            // See if we can create the Community.
            if (null == community)
                {
                if (DEBUG_FLAG) System.out.println("  Creating community ....") ;
                try {
                    community = getManager().addCommunity(ident) ;
                    }
                catch (Exception ouch)
                    {
                    if (DEBUG_FLAG) System.out.println("  FAIL : Exception while creating Community") ;
                    ouch.printStackTrace() ;
                    }
                }

            if (null != community)
                {
                if (DEBUG_FLAG) System.out.println("  PASS : Got community") ;
                }
            }

        if (DEBUG_FLAG) System.out.println("----\"----") ;
        }

    /**
     * Handle our Community description.
     *
     */
    protected void setDescription(String value)
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityDataParser.setDescription()") ;
        if (DEBUG_FLAG) System.out.println("  Description : " + value) ;


        if (DEBUG_FLAG) System.out.println("----\"----") ;
        }
    }

