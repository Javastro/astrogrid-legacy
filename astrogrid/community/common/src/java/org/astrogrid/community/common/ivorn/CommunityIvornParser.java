/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/ivorn/CommunityIvornParser.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/19 14:43:14 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityIvornParser.java,v $
 *   Revision 1.4  2004/03/19 14:43:14  dave
 *   Merged development branch, dave-dev-200403151155, into HEAD
 *
 *   Revision 1.3.2.4  2004/03/18 13:41:19  dave
 *   Added Exception handling to AccountManager
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.ivorn ;

import java.net.URI ;
import java.net.URISyntaxException ;

import junit.framework.TestCase ;

import java.util.regex.Matcher ;
import java.util.regex.Pattern ;

import org.astrogrid.store.Ivorn ;
import org.astrogrid.common.ivorn.MockIvorn ;

import org.astrogrid.community.common.exception.CommunityIdentifierException ;

/**
 * A parser for handling Ivorn identifiers.
 *
 */
public class CommunityIvornParser
    {
    /**
     * Switch for our debug statements.
     *
     */
    private static boolean DEBUG_FLAG = true ;

    /**
     * Public constructor.
     *
     */
    public CommunityIvornParser()
        {
        }

    /**
     * Public constructor, for a specific Ivorn.
     * @param A vaild Ivorn identifier.
     * @throws CommunityIdentifierException If the identifier is not valid.
     *
     */
    public CommunityIvornParser(Ivorn ivorn)
		throws CommunityIdentifierException
        {
        this.setIvorn(ivorn) ;
        }

    /**
     * Our target Ivorn.
     *
     */
    private Ivorn ivorn ;

    /**
     * Our corresponding URI.
     *
     */
    private URI uri ;

    /**
     * The community ident part.
     *
     */
    private String community ;

    /**
     * The account ident part.
     *
     */
    private String account ;

    /**
     * The remaining path, after the account ident has been removed.
     *
     */
    private String path ;

    /**
     * The URI query string.
     *
     */
    private String query ;

    /**
     * The remaining fragment, after the account ident has been removed.
     *
     */
    private String fragment ;

    /**
     * Get our target Ivorn.
     *
     */
    public Ivorn getIvorn()
        {
        return this.ivorn ;
        }

    /**
     * Set our target Ivorn.
     * @param A vaild Ivorn identifier.
     * @throws CommunityIdentifierException If the identifier is not valid.
     *
     */
    protected void setIvorn(Ivorn ivorn)
		throws CommunityIdentifierException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityIvornParser.setIvorn()") ;
        if (DEBUG_FLAG) System.out.println("  Ivorn : " + ivorn) ;
		//
		// Check for null param.
        if (null == ivorn) { throw new CommunityIdentifierException("Null identifier") ; }
        //
        // Save the ivorn.
        this.ivorn     = ivorn ;
        //
        // Reset everything else.
        this.uri       = null  ;
        this.account   = null ;
        this.community = null ;
        this.path      = null ;
        this.fragment  = null ;
        //
        // Try convert it into a URI.
        try {
            this.uri = new URI(ivorn.toString()) ;
	        //
	        // Parse the Community ident.
	        this.parseCommunityIdent() ;
	        //
	        // Parse Account ident.
	        this.parseAccountIdent() ;
            }
        //
        // All Ivorn should be valid URI.
        catch (URISyntaxException ouch)
            {
			throw new CommunityIdentifierException(
				ouch
				) ;
            }
        }

    /**
     * Parse the Community ident.
     *
     */
    protected void parseCommunityIdent()
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityIvornParser.parseCommunityIdent()") ;
        if (DEBUG_FLAG) System.out.println("  Ivorn     : " + ivorn) ;
        if (null != uri)
            {
            this.community = uri.getHost() ;
            }
        if (DEBUG_FLAG) System.out.println("  Community : " + this.community) ;
        }

    /**
     * Get the Community ident as a string.
     * @return The Community ident, or null if no match was found.
     *
     */
    public String getCommunityIdent()
        {
        return this.community ;
        }

    /**
     * Get the Community ident as a new Ivorn.
     * @return A new Ivorn, or null if the Community ident is null.
     *
     */
    public Ivorn getCommunityIvorn()
        {
		//
		// If we have a community ident.
        if (null != this.getCommunityIdent())
            {
			//
			// Create a new Ivorn.
            return new Ivorn(
                this.getCommunityIdent(),
                null
                ) ;
            }
		//
		// If we don't have a community ident.
        else {
            return null ;
            }
        }

    /**
     * Our regex pattern.
     *
     */
    private static Pattern pattern ;
    static {
        try {
            pattern = Pattern.compile("^/?([^/]+)/?(.*)") ;
            }
        catch (Exception ouch)
            {
            //
            // Log this as a fatal error.
            if (DEBUG_FLAG) System.out.println("ERROR ----") ;
            if (DEBUG_FLAG) System.out.println("CommunityIvornParser RegExp pattern is not valid.") ;
            if (DEBUG_FLAG) System.out.println(ouch) ;
            if (DEBUG_FLAG) System.out.println("----------") ;
            }
        }

    /**
     * Parse the Account ident.
     *
     */
    protected void parseAccountIdent()
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityIvornParser.parseAccountIdent()") ;
        if (DEBUG_FLAG) System.out.println("  Ivorn : " + ivorn) ;
        //
        // Set all of the parts to null.
        this.account   = null ;
        this.path      = null ;
        this.query     = null ;
        this.fragment  = null ;
        //
        // If we have a valid URI.
        if (null != uri)
            {
            //
            // Try the user part.
            if (DEBUG_FLAG) System.out.println(" Trying user ....") ;
            this.account  = uri.getUserInfo() ;
            //
            // If we got the account from the URI user.
            if (null != this.account)
                {
                //
                // The path and fragment are left unchanged.
                if (null != uri.getPath())
                    {
                    //
                    // If there is anything in the path.
                    if (uri.getPath().length() > 0)
                        {
                        //
                        // Check for a leading '/'
                        if (uri.getPath().startsWith("/"))
                            {
                            //
                            // If there is anything else.
                            if (uri.getPath().length() > 1)
                                {
                                //
                                // Use anything after the '/'
                                this.path = uri.getPath().substring(1) ;
                                }
                            }
                        else {
                            //
                            // Use the path as-is.
                            this.path = uri.getPath() ;
                            }
                        }
                    }
                //
                // If there is anything in the fragment.
                if (null != uri.getFragment())
                    {
                    if (uri.getFragment().length() > 0)
                        {
                        //
                        // Use the fragment as-is.
                        this.fragment = uri.getFragment() ;
                        }
                    }
                }
            //
            // If we didn't get the account from the URI user.
            else {
                //
                // If we have a path.
                if (null != uri.getPath())
                    {
                    //
                    // Try parse the path.
                    this.parseAccountPath() ;
                    }
                //
                // If we don't have a path.
                else {
                    //
                    // Try parse the fragment.
                    this.parseAccountFragment() ;
                    }
                }
            //
            // The query string is untouched.
            if (null != uri.getQuery())
                {
                if (uri.getQuery().length() > 0)
                    {
                    this.query = uri.getQuery() ;
                    }
                }
            }
        if (DEBUG_FLAG) System.out.println(" Account   : " + this.account) ;
        if (DEBUG_FLAG) System.out.println(" Path      : " + this.path) ;
        if (DEBUG_FLAG) System.out.println(" Query     : " + this.query) ;
        if (DEBUG_FLAG) System.out.println(" Fragment  : " + this.fragment) ;
        }

    /**
     * Parse the URI path for the Account ident.
     *
     */
    protected void parseAccountPath()
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityIvornParser.parseAccountPath()") ;
        if (DEBUG_FLAG) System.out.println("  Ivorn : " + ivorn) ;
        if (DEBUG_FLAG) System.out.println("  Path  : " + uri.getPath()) ;
        //
        // If we have a path.
        if (null != uri.getPath())
            {
            //
            // Try match the path.
            Matcher matcher = pattern.matcher(uri.getPath()) ;
            //
            // If we got a match.
            if (matcher.matches())
                {
                if (DEBUG_FLAG) System.out.println(" PASS : matched the regexp") ;
                if (DEBUG_FLAG) System.out.println(" Count    : " + matcher.groupCount()) ;
                if (DEBUG_FLAG) System.out.println(" Group[0] : " + matcher.group(0)) ;
                //
                // If we found an account.
                if (matcher.groupCount() > 0)
                    {
                    //
                    // Use the first match as our account.
                    this.account = matcher.group(1) ;
                    if (DEBUG_FLAG) System.out.println(" Group[1] : " + matcher.group(1)) ;
                    }
                //
                // If we found anything else.
                if (matcher.groupCount() > 1)
                    {
                    if (DEBUG_FLAG) System.out.println(" Group[2] : " + matcher.group(2)) ;
                    //
                    // Strip spaces from the remainder.
                    String temp = matcher.group(2).trim() ;
                    if (temp.length() > 0)
                        {
                        //
                        // Save anything left over as the path.
                        this.path = temp ;
                        }
                    }
                //
                // The fragment remains unchanged.
                this.fragment = uri.getFragment() ;
                }
            //
            // If we didn't get a match.
            else {
                //
                // Try parse the fragment.
                this.parseAccountFragment() ;
                }
            }
        }

    /**
     * Parse the URI fragment for the Account ident.
     *
     */
    protected void parseAccountFragment()
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityIvornParser.parseAccountFragment()") ;
        if (DEBUG_FLAG) System.out.println("  Ivorn    : " + ivorn) ;
        if (DEBUG_FLAG) System.out.println("  Fragment : " + uri.getFragment()) ;
        //
        // If we have a fragment.
        if (null != uri.getFragment())
            {
            //
            // Try match the fragment.
            Matcher matcher = pattern.matcher(uri.getFragment()) ;
            //
            // If we got a match.
            if (matcher.matches())
                {
                if (DEBUG_FLAG) System.out.println(" PASS : matched the regexp") ;
                if (DEBUG_FLAG) System.out.println(" Count    : " + matcher.groupCount()) ;
                if (DEBUG_FLAG) System.out.println(" Group[0] : " + matcher.group(0)) ;
                //
                // If we found an account.
                if (matcher.groupCount() > 0)
                    {
                    //
                    // Use the first match as our account.
                    this.account = matcher.group(1) ;
                    if (DEBUG_FLAG) System.out.println(" Group[1] : " + matcher.group(1)) ;
                    }
                //
                // If we found anything else.
                if (matcher.groupCount() > 1)
                    {
                    //
                    // Save the rest as the fragment.
                    if (DEBUG_FLAG) System.out.println(" Group[2] : " + matcher.group(2)) ;
                    //
                    // Strip spaces from the remainder.
                    String temp = matcher.group(2).trim() ;
                    if (temp.length() > 0)
                        {
                        //
                        // Save anything left over as the fragment.
                        this.fragment = temp ;
                        }
                    }
                }
            }
        }

    /**
     * Get the Account name as a string.
     * @return The Account name, or null if no match was found.
     *
     */
    protected String getAccountName()
        {
		return this.account ;
        }

    /**
     * Get the Account ident as a string.
     * @return The Account ident, or null if no match was found.
     *
     */
    public String getAccountIdent()
        {
        if ((null != this.getCommunityIdent()) && (null != this.getAccountName()))
            {
			return new StringBuffer()
				.append(this.getCommunityIdent())
				.append("/")
				.append(this.getAccountName())
				.toString() ;
			}
		else {
			return null ;
			}
        }

    /**
     * Get the Account Ivorn.
     * @return A new Ivorn, or null if the Community or Account ident are null.
     *
     */
    public Ivorn getAccountIvorn()
		throws CommunityIdentifierException
        {
        if ((null != this.getCommunityIdent()) && (null != this.getAccountName()))
            {
            return CommunityAccountIvornFactory.createIvorn(
                this.getCommunityIdent(),
                this.getAccountName()
                ) ;
            }
        else {
            return null ;
            }
        }

    /**
     * Get the rest of the path, after the Account ident has been removed.
     * @return The remaining URI path, or null if there is nothing left. 
     *
     */
    protected String getPath()
        {
        return path ;
        }

    /**
     * Get the query, after the Account ident has been removed.
     * @return The remaining URI query, or null if there is nothing left. 
     *
     */
    protected String getQuery()
        {
        return query ;
        }

    /**
     * Get the rest of fragment, after the Account ident has been removed.
     * @return The remaining URI fragment, or null if there is nothing left. 
     *
     */
    protected String getFragment()
        {
        return fragment ;
        }

    /**
     * Get the remaining URI string, including the path, query and fragment,
     * after the account has been removed
     * @return The remaining URI string, or null if there is nothing left. 
     *
     */
    public String getRemainder()
        {
        StringBuffer buffer = new StringBuffer() ;
        //
        // If we have a path.
        if (null != this.path)
            {
            buffer.append("/") ;
            buffer.append(this.path) ;
            }
        //
        // If we have a query.
        if (null != this.query)
            {
            buffer.append("?") ;
            buffer.append(this.query) ;
            }
        //
        // If we have a fragment.
        if (null != this.fragment)
            {
            buffer.append("#") ;
            buffer.append(this.fragment) ;
            }
        //
        // Return the string, or null if it is empty.
        return (buffer.length() > 0) ? buffer.toString() : null ;
        }

    /**
     * Checks if the Ivorn is a mock identifier.
     * @return true if the Ivorn is a mock identifier.
     *
     */
    public boolean isMock()
        {
        return (
            MockIvorn.isMock(this.ivorn)
            ) ;
        }
    }
