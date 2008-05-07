package org.astrogrid.security.community ;

import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.store.Ivorn;
import org.astrogrid.config.Config ;
import org.astrogrid.config.SimpleConfig ;
import org.astrogrid.config.PropertyNotFoundException ;
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
    public CommunityIvornParser(Ivorn ivorn) throws Exception
        {
        this.setIvorn(ivorn) ;
        }

    /**
     * Public constructor for a specific identifier.
     * @param ident A vaild identifier.
     * @throws CommunityIdentifierException If the identifier is not valid.
     */
    public CommunityIvornParser(String ident) throws Exception {
      this.setIvorn(parse(ident));
    }

  /**
   * The given IVORN.
   */
  private Ivorn ivorn ;
    
  /**
   * The publishing authority.
   */
  private String authority;
  
  /**
   * The IVORN of the community service associated with the given IVORN.
   */
  private Ivorn community;

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
   * The query string from the URI form of the IVORN.
   */
  private String query ;

  /**
   * The query string from the URI form of the IVORN.
   */
  private String fragment ;

  /**
   * Reveals the IVORN.
   */
  public Ivorn getIvorn() {
    return this.ivorn ;
  }

  /**
   * Records and parses the IVORN.
   *
   * @param ivorn A valid Ivorn identifier.
   * @throws CommunityIdentifierException If the identifier is not valid.
   */
  protected void setIvorn(Ivorn ivorn) throws Exception {
    assert ivorn != null;
    this.ivorn = ivorn;
    parseIvornText(ivorn.toString());
  }




  /**
   * Reveals the community name.
   * The name is the string form of the IVORN, stripped of the ivo:// prefix.
   */
  public String getCommunityName() {
    // This method Ivorn.getPath() doesn't return the path of the IVORN
    // when represented as a URI. It returns the authority + path. 
    return this.community.getPath();
  }
    
  /**
   * Reveals the IVORN for the community service.
   */
  public String getCommunityIdent() {
    return this.community.toString();
  }
    
    
  /**
   * Reveals the IVORN for the community service.
   */
  public Ivorn getCommunityIvorn() { 
    return this.community;
  }

  /**
   * Get the Account name as a string.
   * This will contain just the account name, without the community identifier.
   * @return The Account name, or null if no match was found.
   */
  public String getAccountName() {
     return this.account;
  }

  /**
   * Get the Account ident as a string.
   * This will contain both the community ident and account name:
   * account@community. No ivo:// prefix is returned.
   *
   * @return The Account ident, or null if no match was found.
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
   */
  public Ivorn getAccountIvorn() throws Exception {
    String accountName = getAccountIdent();
    if (accountName == null) {
      return null;
    }
    else {
      try {
        return new Ivorn("ivo://" + accountName);
      } catch (URISyntaxException ex) {
        throw new Exception("Failed to form an account IVORN", ex);
      }
    }
  }

  /**
   * Gets the rest of the path, after the Account ident has been removed.
   * @return The remaining URI path, or null if there is nothing left. 
   */
  protected String getPath() {
    return path;
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
     *
     * @return The remaining URI fragment, or null if there is nothing left. 
     */
    protected String getFragment()
        {
        return fragment ;
        }

    /**
     * Get the remaining URI string, including the path, query and fragment,
     * after the account has been removed
     *
     * @return The remaining URI string, or null if there is nothing left. 
     */
    public String getRemainder()
        {
        StringBuffer buffer = new StringBuffer() ;
        //
        // If we have a path.
        if (null != this.path)
            {
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
    /*
    public boolean isMock()
        {
        return (
            MockIvorn.isMock(this.ivorn)
            ) ;
        }
     */

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
    protected static Ivorn parse(String ident) throws Exception {
      assert ident != null;
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
        throw new Exception(ouch);
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
    public static String getLocalIdent() throws Exception
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
                throw new Exception(
                    "Local Community identifier not configured"
                    ) ;
                }
            }
        catch (PropertyNotFoundException ouch)
            {
            throw new Exception(
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
    public boolean isLocal() throws Exception
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

  /**
   * Parses a given IVORN.
   *
   * @throws Exception If the given string is not a URI.
   * @throws Exception If the given string is a URI but not an IVORN.
   * @throws Exception If the given string is an IVORN but not for a user account.
   */
  private void parseIvornText(String given) throws Exception {
    
    // First, parse the given string as a URI.
    // This pattern is copied verbatim from appendix A of RFC.
    // The captured elements are as follows:
    // scheme    = $2
    // authority = $4
    // path      = $5
    // query     = $7
    // fragment  = $9
    Pattern p = 
        Pattern.compile("^(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\\?([^#]*))?(#(.*))?");
    Matcher m = p.matcher(given);
    if (!m.matches()) {
      throw new Exception(given + " is not an IVORN (not a valid URI)");
    }
    this.fragment    = m.group(9);
    this.query       = m.group(7);
    String path      = m.group(5);
    String authority = m.group(4);
    String scheme    = m.group(2);
    
    // It's a URI. Is it an IVORN?
    if (!"ivo".equals(scheme)) {
      throw new Exception(given + " is not an IVORN (wrong URI scheme)");
    }
    if (authority == null) {
      throw new Exception(given + " is not an IVORN (no authority part)");
    }
    
    // Is it an account URI? It could be in either of the two forms noted in
    // class comments.
    Pattern authorityPattern = Pattern.compile("([^@]+)@(.+)");
    Matcher authorityMatcher = authorityPattern.matcher(authority);
    if (authorityMatcher.matches()) {
      this.account   = authorityMatcher.group(1);
      this.authority = authorityMatcher.group(2);
      this.community = new Ivorn(this.authority, 
                                 (path == null || path.length() < 2)? "community" : path.substring(1), 
                                 null);
      this.path      = path;
    }
    else if (path == null) {
      throw new Exception(
                    given + 
                    " is not an IVORN for a community account (no user name)"
                );
    }
    else {
      Pattern pathPattern = Pattern.compile("/([^/]+)(.*)");
      Matcher pathMatcher = pathPattern.matcher(path);
      if (pathMatcher.matches()) {
        this.account   = pathMatcher.group(1);
        this.authority = authority;
        this.path      = pathMatcher.group(2);
        this.community = new Ivorn(this.authority, "community", null);
      }
      else {
        throw new Exception(
                    given + 
                    " is not an IVORN for a community account (no user name)"
                );
      }
    }
      
  }
}
