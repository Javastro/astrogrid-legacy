/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/install/src/java/org/astrogrid/community/install/loader/CommunityLoaderData.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/30 01:40:03 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityLoaderData.java,v $
 *   Revision 1.3  2004/03/30 01:40:03  dave
 *   Merged development branch, dave-dev-200403242058, into HEAD
 *
 *   Revision 1.2.4.2  2004/03/28 09:11:43  dave
 *   Convert tabs to spaces
 *
 *   Revision 1.2.4.1  2004/03/28 02:00:55  dave
 *   Added database management tasks.
 *
 *   Revision 1.2  2004/03/23 16:34:08  dave
 *   Merged development branch, dave-dev-200403191458, into HEAD
 *
 *   Revision 1.1.2.1  2004/03/20 06:54:11  dave
 *   Added addAccount(AccountData) to PolicyManager et al.
 *   Added XML loader for AccountData.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.install.loader ;

import java.util.HashMap ;
import java.util.ArrayList ;
import java.util.Collection ;

/**
 * A data object Castor to load Community data into.
 *
 */
public class CommunityLoaderData
    {
    /**
     * Public constructor.
     *
     */
    public CommunityLoaderData()
        {
        }

    /**
     * Our identifier.
     *
     */
    private String ident ;

    /**
     * Access to our identifier.
     * @return Our Community identifier.
     *
     */
    public String getIdent()
        {
        return this.ident ;
        }

    /**
     * Access to our identifier.
     * @param ident the Community identifier.
     *
     */
    public void setIdent(String ident)
        {
        this.ident = ident ;
        }

    /**
     * Our collection of Accounts.
     *
     */
    private Collection accounts = new ArrayList() ;

    /**
     * Access to our Accoounts.
     *
     */
    public Collection getAccounts()
        {
        return this.accounts ;
        }

    /**
     * Our collection of Groups.
     *
     */
    private Collection groups = new ArrayList() ;

    /**
     * Access to our Groups.
     *
     */
    public Collection getGroups()
        {
        return this.groups ;
        }

    /**
     * Our collection of passwords.
     *
     */
    private HashMap passwords = new HashMap() ;

    /**
     * Access to our passwords.
     *
     */
    public HashMap getPasswords()
        {
        return this.passwords ;
        }

    }

