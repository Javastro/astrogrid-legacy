/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/policy/loader/Attic/GroupDataParser.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2003/11/06 15:35:26 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: GroupDataParser.java,v $
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

import org.astrogrid.community.policy.data.GroupData ;

import org.astrogrid.community.policy.server.PolicyManager ;

/**
 * An XML parser for handling Group data.
 *
 */
public class GroupDataParser
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
    public GroupDataParser(PolicyDataParser parent)
        {
        this(parent, "group") ;
        }

    /**
     * Public constructor.
     *
     */
    public GroupDataParser(PolicyDataParser parent, String name)
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
        // Initialise our GroupIdentParser.
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
                                if (null != group)
                                    {
                                    group.setDescription(value) ;
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
     * Our current Group data.
     *
     */
    protected GroupData group ;

    /**
     * Handle the start of our <group> element.
     * Re-sets our GroupData.
     *
     */
    protected void startElement()
        throws SAXException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("GroupDataParser.startElement()") ;

        //
        // Reset our group data.
        group = null ;

        if (DEBUG_FLAG) System.out.println("----\"----") ;
        }

    /**
     * Handle the end of our <group> element.
     * Calls our PolicyManager to update the server with our GroupData.
     *
     */
    protected void closeElement()
        throws SAXException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("GroupDataParser.closeElement()") ;

        //
        // If we have an Group.
        if (null != group)
            {
            if (DEBUG_FLAG) System.out.println("  PASS : Got group") ;
            //
            // Try updating the database
            if (null != getManager())
                {
                if (DEBUG_FLAG) System.out.println("  PASS : Got manager") ;
                try {
                    group = getManager().setGroup(group) ;
                    }
                catch (Exception ouch)
                    {
                    if (DEBUG_FLAG) System.out.println("  FAIL : Exception while creating Group") ;
                    ouch.printStackTrace() ;
                    }
                if (null != group)
                    {
                    if (DEBUG_FLAG) System.out.println("  PASS : Updated group") ;
                    }
                }
            }
        //
        // If we don't have an Group.
        else {
            if (DEBUG_FLAG) System.out.println("  FAIL : Null group") ;
            }

        if (DEBUG_FLAG) System.out.println("----\"----") ;
        }

    /**
     * Handle our Group ident.
     * Calls our PolicyManager to load or create our GroupData.
     * Creates a new Group if it does not exist.
     *
     */
    protected void setIdent(String ident)
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("GroupDataParser.setIdent()") ;
        if (DEBUG_FLAG) System.out.println("  Ident : " + ident) ;

        if (null != getManager())
            {
            //
            // See if we can load the Group.
            if (null == group)
                {
                if (DEBUG_FLAG) System.out.println("  Loading group ....") ;
                try {
                    group = getManager().getGroup(ident) ;
                    }
                catch (Exception ouch)
                    {
                    if (DEBUG_FLAG) System.out.println("  FAIL : Exception while loading Group") ;
                    ouch.printStackTrace() ;
                    }
                }
            //
            // See if we can create the Group.
            if (null == group)
                {
                if (DEBUG_FLAG) System.out.println("  Creating group ....") ;
                try {
                    group = getManager().addGroup(ident) ;
                    }
                catch (Exception ouch)
                    {
                    if (DEBUG_FLAG) System.out.println("  FAIL : Exception while creating Group") ;
                    ouch.printStackTrace() ;
                    }
                }

            if (null != group)
                {
                if (DEBUG_FLAG) System.out.println("  PASS : Got group") ;
                }
            }

        if (DEBUG_FLAG) System.out.println("----\"----") ;
        }

    /**
     * Handle our Group description.
     *
     */
    protected void setDescription(String value)
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("GroupDataParser.setDescription()") ;
        if (DEBUG_FLAG) System.out.println("  Description : " + value) ;

        //
        // Update the Group description.
        if (null != group)
            {
            if (DEBUG_FLAG) System.out.println("  PASS : Got group") ;
            group.setDescription(value) ;
            }
        //
        // If we don't have an Group.
        else {
            if (DEBUG_FLAG) System.out.println("  FAIL : Null group") ;
            }

        if (DEBUG_FLAG) System.out.println("----\"----") ;
        }
    }

