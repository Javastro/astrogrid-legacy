package org.astrogrid.community.common.ivorn ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import java.net.URI ;
import java.net.URISyntaxException ;

import java.util.regex.Matcher ;
import java.util.regex.Pattern ;

import org.astrogrid.store.Ivorn ;
import org.astrogrid.common.ivorn.MockIvorn ;

import org.astrogrid.config.Config ;
import org.astrogrid.config.SimpleConfig ;
import org.astrogrid.config.PropertyNotFoundException ;

import org.astrogrid.community.common.exception.CommunityServiceException ;
import org.astrogrid.community.common.exception.CommunityIdentifierException ;

/**
 * A parser for an IVO identifier, a.k.a. "IVORN".
 *
 * The parser handles standard IVORNs and also some special forms used by
 * AstroGrid in respect of user accounts and MySpace locations.
 *
 * In the first form, the IVORN identifies a user account in a specific
 * community, e.g. ivo://user@ivo-authority/resource-key. In this form,
 * ivo://ivo-authority/resource-key must be the IVORN for the community
 * service where the user is registered and must be resolvable to that
 * service in the IVO registry. This is a new usage, introduced in the 2008.0
 * release of AstroGrid software. It serves to identify users in a registry
 * based on VOResource v1.0.
 *
 * In the second form, the IVORN again identifies a user account, with the
 * encoding ivo://ivo-authority/user. In this form, the IVORN for the
 * associated community-service is assumed to be ivo://ivo-authority/community
 * where "community" is a literal; i.e. the resource-key for the service's
 * registration has a conventional value and there can be at most one
 * community per IVO authority. This form is deprecated, but is supported
 * as a fall-back option.
 *
 * In either of the two forms above, a URI-fragment is interpreted as a
 * file-path within MySpace. E.g. ivo://fred.hoyle@uk.ac.cam.ast/IoA#/foo/bar/baz
 * identifies the file /foo/bar/baz in the account "fred.hoyle" at the community
 * ivo://uk.ac.cam.ast/IoA.
 *
 */
public class CommunityIvornParser
    {
    /**
     * Our debug logger.
     *
     */
	private static Log log = LogFactory.getLog(CommunityIvornParser.class);

    /**
     * Our AstroGrid configuration.
     *
     */
    protected static Config config = SimpleConfig.getSingleton() ;

    /**
     * Public constructor for a specific Ivorn.
     * @param ivorn A vaild Ivorn identifier.
     * @throws CommunityIdentifierException If the identifier is not valid.
     *
     */
    public CommunityIvornParser(Ivorn ivorn)
        throws CommunityIdentifierException
        {
        this.setIvorn(ivorn) ;
        }

    /**
     * Public constructor for a specific identifier.
     * @param ident A vaild identifier.
     * @throws CommunityIdentifierException If the identifier is not valid.
     */
    public CommunityIvornParser(String ident) 
        throws CommunityIdentifierException {
      this.setIvorn(parse(ident));
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
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("CommunityIvornParser.setIvorn()") ;
        log.debug("  Ivorn : " + ivorn) ;
        //
        // Check for null param.
        if (null == ivorn)
            {
            throw new CommunityIdentifierException(
                "Null identifier"
                ) ;
            }
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
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("CommunityIvornParser.parseCommunityIdent()") ;
        log.debug("  Ivorn     : " + ivorn) ;
        if (null != uri) {
          
          // Assumes special form one, described in class comment.
          System.out.println("User = " + uri.getUserInfo());
          if (uri.getUserInfo() == null) {
            this.community = uri.getHost() + "/community";
          }
          
          // Assumes special form two, described in class comment.
          else {
            this.community = uri.getHost() + uri.getPath();
          }
        }
        log.debug("  Community : " + this.community) ;
        }

    /**
     * Get the Community name as a string.
     * @return The Community name, or null if no match was found.
     *
     */
    public String getCommunityName()
        {
        return this.community ;
        }

    /**
     * Get the Community ident as a string.
     * @return The Community ident, or null if no match was found.
     *
     */
    public String getCommunityIdent()
        {
        return new StringBuffer()
            .append(Ivorn.SCHEME)
            .append("://")
            .append(this.community)
            .toString() ;
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
                this.getCommunityName(),
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
            log.debug("ERROR ----") ;
            log.debug("CommunityIvornParser RegExp pattern is not valid.") ;
            log.debug(ouch) ;
            log.debug("----------") ;
            }
        }

    /**
     * Parse the Account ident.
     *
     */
    protected void parseAccountIdent()
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("CommunityIvornParser.parseAccountIdent()") ;
        log.debug("  Ivorn : " + ivorn) ;
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
        log.debug(" Account   : " + this.account) ;
        log.debug(" Path      : " + this.path) ;
        log.debug(" Query     : " + this.query) ;
        log.debug(" Fragment  : " + this.fragment) ;
        }

    /**
     * Parse the URI path for the Account ident.
     *
     */
    protected void parseAccountPath()
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("CommunityIvornParser.parseAccountPath()") ;
        log.debug("  Ivorn : " + ivorn) ;
        log.debug("  Path  : " + uri.getPath()) ;
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
                //
                // If we found an account.
                if (matcher.groupCount() > 0)
                    {
                    //
                    // Use the first match as our account.
                    this.account = matcher.group(1) ;
                    }
                //
                // If we found anything else.
                if (matcher.groupCount() > 1)
                    {
                    //
                    // Strip spaces from the remainder.
                    String temp = matcher.group(2).trim() ;
                    if (temp.length() > 0)
                        {
                        //
                        // Save anything left over as the path.
                        this.path = temp ;
                        System.out.println("path = '" + this.path + "'");
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
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("CommunityIvornParser.parseAccountFragment()") ;
        log.debug("  Ivorn    : " + ivorn) ;
        log.debug("  Fragment : " + uri.getFragment()) ;
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
                //
                // If we found an account.
                if (matcher.groupCount() > 0)
                    {
                    //
                    // Use the first match as our account.
                    this.account = matcher.group(1) ;
                    }
                //
                // If we found anything else.
                if (matcher.groupCount() > 1)
                    {
                    //
                    // Save the rest as the fragment.
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
     * This will contain just the account name, without the community identifier.
     * @return The Account name, or null if no match was found.
     *
     */
    public String getAccountName()
        {
        return this.account ;
        }

  /**
   * Get the Account ident as a string.
   * This will contain both the community ident and account name:
   * account@community
   * 
   * @return The Account ident, or null if no match was found.
   * @todo Should this add 'ivo://' to the ident ?
   *
   */
  public String getAccountIdent() {
    if ((null != this.getCommunityIdent()) && (null != this.getAccountName())) {
      return this.getAccountName() + "@" + this.getCommunityName();
    }
    else {
      return null;
    }
  }
  
  /**
   * Get the Account Ivorn.
   * This will contain both the community ident and account name:
   * ivo://account@community
   * 
   * @return A new Ivorn, or null if the Community or Account ident are null.
   *
   */
  public Ivorn getAccountIvorn() throws CommunityIdentifierException {
    String accountName = getAccountIdent();
    if (accountName == null) {
      return null;
    }
    else {
      try {
        return new Ivorn("ivo://" + accountName);
      } catch (URISyntaxException ex) {
        throw new CommunityIdentifierException("Failed to form an account IVORN", ex);
      }
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
          System.out.println("appending '" + this.path + "'");
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
        System.out.println("length = " + buffer.length());
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

    /**
     * Converts a string identifier into an Ivorn.
     * Normally, the identifier is just the string form of the IVORN,
     * e.g. ivo://frog@org.astrogrid.etc/community. Occassionally, the
     * scheme is omitted from the URI, e.g.
     * frog@org.astrogrid.etc/community. This latter form is supported but
     * deprecated.
     *
     * @param ident The identifier.
     * @return a new Ivorn containing the identifier.
     * @throws CommunityIdentifierException If the identifier is invalid.
     */
    protected static Ivorn parse(String ident)
        throws CommunityIdentifierException {
      System.out.println("static parse() was called");
      if (null == ident) {
        throw new CommunityIdentifierException("Null identifier");
      }
      System.out.println(ident);
      try {
        if (ident.startsWith("ivo://")) {
          return new Ivorn(ident) ;
        }
        else {
          // Add the scheme prefix since the Ivorn class fails if it is missing.
          return new Ivorn("ivo://" + ident);
        }
      }
      catch (URISyntaxException ouch) {
        throw new CommunityIdentifierException(ouch);
      }
    }

    /**
     * The property name for our local Community identifier.
     *
     */
    public static final String LOCAL_COMMUNITY_PROPERTY = "org.astrogrid.community.ident" ;

    /**
     * Access to the local Community identifier.
     * @throws CommunityServiceException If the local Community identifier is not set.
     *
     */
    public static String getLocalIdent()
        throws CommunityServiceException
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("CommunityIvornParser.getLocalIdent()") ;
        try {
            //
            // Get the local identifier from our configuration.
            String local = (String) config.getProperty(LOCAL_COMMUNITY_PROPERTY) ;
            log.debug("  Local : " + local) ;
            //
            // If we found the local ident.
            if (null != local)
                {
                return local ;
                }
            //
            // If we didn't find the local ident.
            else {
                throw new CommunityServiceException(
                    "Local Community identifier not configured"
                    ) ;
                }
            }
        catch (PropertyNotFoundException ouch)
            {
            throw new CommunityServiceException(
                "Local Community identifier not configured"
                ) ;
            }
        }

    /**
     * Check if the Community ident is local.
     * @return true If the Ivorn Community matches the local Community identifier.
     * @throws CommunityServiceException If the local Community identifier is not set.
     *
     */
    public boolean isLocal()
        throws CommunityServiceException
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("CommunityIvornParser.isLocal()") ;
        log.debug("  Ivorn : " + ivorn) ;
        //
        // Compare the ident.
        return getLocalIdent().equals(
            this.getCommunityName()
            ) ;
        }

  /**
   * Reveals the file-path inside MySpace.
   * This is worked out from the fragment of the IVORN.
   */
  public String getMySpacePath() {
    return getFragment();
  }
  
    /**
     * Convert the parser to a String.
     *
     */
    public String toString()
        {
        return "CommunityIvornParser : " + ((null != ivorn) ? ivorn.toString() : null) ;
        }

    }
