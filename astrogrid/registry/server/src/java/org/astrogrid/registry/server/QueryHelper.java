package org.astrogrid.registry.server;

import org.astrogrid.config.Config;

public class QueryHelper {
    
    /**
     * conf - Config variable to access the configuration for the server normally
     * jndi to a config file.
     * @see org.astrogrid.config.Config
     */      
    public static Config conf = null;
    

    
    private static String findRegistryQuery = 
        " for $x in //<rootnode> where @xsi:type &= '*Registry*' return $x";
    
    private static String findRegistryQueryWithOutAuthority = 
        " for $x in //<rootnode> where @xsi:type = 'vg:RegistryType' and vr:identifier &= '*<id>*' return $x";

    private static String findRegistryQueryWithAuthority = 
        " for $x in //<rootnode> where  @xsi:type='vg:RegistryType' and vr:Identifier/vr:AuthorityID = '<id>' return $x";
    
    private static String findAllRegistryQuery =
        " //*[@xsi:type &= '*Registry*']";

    
    private static String startQuery  = 
        " for $x in //<rootnode> where ";
    
    
    /**
     * final variable for the default AuthorityID associated to this registry.
     */
    private static final String AUTHORITYID_PROPERTY =
                                           "org.astrogrid.registry.authorityid";    
        
    /**
     * Static to be used on the initiatian of this class for the config
     */   
    static {
       if(conf == null) {        
          conf = org.astrogrid.config.SimpleConfig.getSingleton();    
       }      
    }
    
    private static String getXQLDeclarations(String versionNumber) {
        versionNumber = versionNumber.replace('.','_');
        return conf.getString("declare.namespace." + versionNumber,"");
    }    
    
    public static String queryForRegistries(String versionNumber) {
        return getXQLDeclarations(versionNumber) + 
               findRegistryQuery.replaceAll("<rootnode>",
               RegistryServerHelper.getRootNodeName(versionNumber));
    }
    
    public static String queryForMainRegistry(String versionNumber) {
        boolean hasAuthorityID = conf.getBoolean(
                "identifier.path.hasauthorityid." + versionNumber,true);
        String mainQuery = null;
        String authorityID = conf.getString(AUTHORITYID_PROPERTY);
        if(hasAuthorityID) {
            mainQuery = findRegistryQueryWithAuthority.replaceAll("<id>",authorityID);
        } else {
            mainQuery = findRegistryQueryWithOutAuthority.replaceAll("<id>",authorityID);
        }
        return getXQLDeclarations(versionNumber) + 
               mainQuery.replaceAll("<rootnode>",
               RegistryServerHelper.getRootNodeName(versionNumber));
    }
    

    public static String getStartQuery(String versionNumber) {
        return getXQLDeclarations(versionNumber) + 
               startQuery.replaceAll("<rootnode>",
               RegistryServerHelper.getRootNodeName(versionNumber));
    }
    
    public static String getAllRegistryQuery() {
        return findAllRegistryQuery; 
    }

    
    
}