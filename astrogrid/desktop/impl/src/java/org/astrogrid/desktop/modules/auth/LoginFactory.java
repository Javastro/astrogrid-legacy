package org.astrogrid.desktop.modules.auth;

import java.net.URI;
import java.net.URISyntaxException;

import org.astrogrid.acr.astrogrid.UserInformation;
import org.astrogrid.community.common.exception.CommunityIdentifierException;
import org.astrogrid.community.common.exception.CommunitySecurityException;
import org.astrogrid.community.common.exception.CommunityServiceException;
import org.astrogrid.community.resolver.exception.CommunityResolverException;
import org.astrogrid.registry.RegistryException;

/** Class that constructs a scripting environment, from results of logging in.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 20-Dec-2004
 */
public class LoginFactory {

    private static String trustedCertificatesDirectoryName;

    /**
     * Identifies the directory containing trusted certificates.
     * This should be called before the first call to login().
     * The certificates are those used when authenticating replies
     * from services, typically when doing TLS.
     *
     * @param directoryName Fully-qualified name of the directory holding the certificates. 
     */
    public static void declareTrustedCertificates(String directoryName) {
        LoginFactory.trustedCertificatesDirectoryName = directoryName;
    }
    /** Construct a new LoginFactory
     * 
     */
    private LoginFactory() {
        super();
    }

    /** create a script environment by logging into astrogird 
     * @deprecated favour the more oo login(UserInformation)
     * @param username user identity
     * @param community community the user belongs to
     * @param password password for this user
     * @return an initialized scripting environment
     * @throws RegistryException
     * @throws CommunityIdentifierException
     * @throws CommunitySecurityException
     * @throws CommunityServiceException
     * @throws CommunityResolverException
     */
    public static ScriptEnvironment login(String username,
            String community,
            String password) 
    throws CommunityResolverException, 
    CommunityServiceException, 
    CommunitySecurityException, 
    CommunityIdentifierException, 
    RegistryException {
        try {
            return login(mkUserInfo(community,username,password));
        } catch (URISyntaxException x) {
            throw new CommunityResolverException("Failed to create user identifier",x);
        }
    }

    public static ScriptEnvironment login(UserInformation info) 
    throws CommunityResolverException, 
    CommunityServiceException, 
    CommunitySecurityException, 
    CommunityIdentifierException, 
    RegistryException {
        return new LoginScriptEnvironment(info, 
                LoginFactory.trustedCertificatesDirectoryName);
    }




    public static UserInformation mkUserInfo(String community, String username, String password) throws URISyntaxException {
        return new UserInformation(
            new URI(new UserIvorn(community,username,"").toString())
                //@todo should I use the new form of user here - unsure what else this affects yet.
             //   new URI("ivo://" + username + "@" + community)
                ,username
                ,password
                ,community
        );
    }

}

