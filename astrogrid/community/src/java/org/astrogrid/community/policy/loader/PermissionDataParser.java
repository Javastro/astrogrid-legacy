/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/policy/loader/Attic/PermissionDataParser.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2003/11/06 15:35:26 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PermissionDataParser.java,v $
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

import org.astrogrid.community.policy.data.PolicyPermission ;

import org.astrogrid.community.policy.server.PolicyManager ;

/**
 * An XML parser for handling Permission data.
 *
 */
public class PermissionDataParser
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
    public PermissionDataParser(PolicyDataParser parent)
        {
        this(parent, "permission") ;
        }

    /**
     * Public constructor.
     *
     */
    public PermissionDataParser(PolicyDataParser parent, String name)
        {
        super(name) ;
        this.parent = parent ;
        }

    /**
     * Our group ident.
     *
     */
    protected String group ;

    /**
     * Our resource ident.
     *
     */
    protected String resource ;

    /**
     * Our action.
     *
     */
    protected String action ;

    /**
     * Our status.
     *
     */
    protected String status ;

    /**
     * Initialise our parser tree.
     *
     */
    public void init()
        {
        //
        // Initialise our PermissionIdentParser.
        super.init() ;
        //
        // Add our attribute handlers.
        addAttributeHandler(
            new SAXAttributeHandler("group")
                {
                public void parseAttribute(String value)
                    {
                    group = value ;
                    }
                }
            ) ;
        addAttributeHandler(
            new SAXAttributeHandler("resource")
                {
                public void parseAttribute(String value)
                    {
                    resource = value ;
                    }
                }
            ) ;
        addAttributeHandler(
            new SAXAttributeHandler("action")
                {
                public void parseAttribute(String value)
                    {
                    action = value ;
                    }
                }
            ) ;
        //
        // Add an character handler for our status.
        addElementHandler(
            new SAXElementHandler("status")
                {
                public void init()
                    {
                    setCharacterHandler(
                        new SAXCharacterHandler()
                            {
                            public void parseText(String value)
                                throws SAXException
                                {
                                if (null != permission)
                                    {
                                    permission.setStatus(value) ;
                                    }
                                }
                            }
                        ) ;
                    }
                }
            ) ;
        //
        // Add an character handler for our reason.
        addElementHandler(
            new SAXElementHandler("reason")
                {
                public void init()
                    {
                    setCharacterHandler(
                        new SAXCharacterHandler()
                            {
                            public void parseText(String value)
                                throws SAXException
                                {
                                if (null != permission)
                                    {
                                    permission.setReason(value) ;
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
     * Our current Permission data.
     *
     */
    protected PolicyPermission permission ;

    /**
     * Handle the start of our <permission> element.
     * Re-sets our PermissionData.
     *
     */
    protected void startElement()
        throws SAXException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("PermissionDataParser.startElement()") ;

        //
        // Reset our permission data.
        permission = null ;

        if (DEBUG_FLAG) System.out.println("----\"----") ;
        }


    /**
     * Handle the end of our attributes.
     *
     */
    protected void doneAttributes()
        throws SAXException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("PermissionDataParser.doneAttributes()") ;
        //
        // If we have a Group, Resource and action.
        if ((null != group) && (null != resource) && (null != action))
            {
            if (DEBUG_FLAG) System.out.println("  PASS : Got group, resource and action") ;
            //
            // Try updating the database
            if (null != getManager())
                {
                if (DEBUG_FLAG) System.out.println("  PASS : Got manager") ;
                //
                // See if we can load the PolicyPermission.
                if (null == permission)
                    {
                    if (DEBUG_FLAG) System.out.println("  Loading permission ....") ;
                    try {
                        permission = getManager().getPermission(resource, group, action) ;
                        }
                    catch (Exception ouch)
                        {
                        if (DEBUG_FLAG) System.out.println("  FAIL : Exception while loading permission") ;
                        ouch.printStackTrace() ;
                        }
                    }
                //
                // See if we can create the PolicyPermission.
                if (null == permission)
                    {
                    if (DEBUG_FLAG) System.out.println("  Creating permission ....") ;
                    try {
                        permission = getManager().addPermission(resource, group, action) ;
                        }
                    catch (Exception ouch)
                        {
                        if (DEBUG_FLAG) System.out.println("  FAIL : Exception while creating permission") ;
                        ouch.printStackTrace() ;
                        }
                    }
                if (null != permission)
                    {
                    if (DEBUG_FLAG) System.out.println("  PASS : Got permission") ;
                    }
                }
            }
        //
        // If we don't have a Group, Resource and action.
        else {
            if (DEBUG_FLAG) System.out.println("  FAIL : Null group, resource or action.") ;
            }
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        }

    /**
     * Handle the end of our <permission> element.
     * Calls our PolicyManager to update the server with our PermissionData.
     *
     */
    protected void closeElement()
        throws SAXException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("PermissionDataParser.closeElement()") ;
        //
        // If we have an Permission.
        if (null != permission)
            {
            if (DEBUG_FLAG) System.out.println("  PASS : Got permission") ;
            //
            // Try updating the database
            if (null != getManager())
                {
                if (DEBUG_FLAG) System.out.println("  PASS : Got manager") ;
                try {
                    permission = getManager().setPermission(permission) ;
                    }
                catch (Exception ouch)
                    {
                    if (DEBUG_FLAG) System.out.println("  FAIL : Exception while creating Permission") ;
                    ouch.printStackTrace() ;
                    }
                if (null != permission)
                    {
                    if (DEBUG_FLAG) System.out.println("  PASS : Updated permission") ;
                    }
                }
            }
        //
        // If we don't have an Permission.
        else {
            if (DEBUG_FLAG) System.out.println("  FAIL : Null permission") ;
            }

        if (DEBUG_FLAG) System.out.println("----\"----") ;
        }

    }

