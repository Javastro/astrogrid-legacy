package org.astrogrid.registry.server;

import org.astrogrid.config.Config;
import java.util.Map;
import java.util.HashMap;


public class QueryHelper {
    
    /**
     * conf - Config variable to access the configuration for the server normally
     * jndi to a config file.
     * @see org.astrogrid.config.Config
     */      
    public static Config conf = null;
    
    private static Map queries = new HashMap();
    
    /*    
    private static String findResourceWithoutAuthority = 
        " for $x in //<rootnode> where $x/vr:identifier = '<id>' return $x";
    
    private static String findResourceQueryByAuthority = 
        " for $x in //<rootnode> where $x/vr:identifier &= '*<id>*' return $x";

    private static String findAllResourceWithoutAuthority = 
        " for $x in //<rootnode> where $x/vr:identifier &= '*<id>*' return $x";

    
    private static String findResourceWithAuthority = 
        " for $x in //<rootnode> where $x/vr:Identifier/vr:AuthorityID = '<id>' return $x";

    private static String findResourceWithAuthorityAndResourceKey = 
        " for $x in //<rootnode> where $x/vr:Identifier/vr:AuthorityID = '<id>' and $x/vr:Identifier/vr:ResourceKey = '<reskey>' return $x";

    private static String findResourcesWithAuthorityAndResourceKey = 
        " for $x in //<rootnode> where $x/vr:Identifier/vr:AuthorityID &= '*<id>*' or $x/vr:Identifier/vr:ResourceKey &= '*<reskey>*' return $x";    
    
    private static String findRegistryQuery = 
        " for $x in //<rootnode> where $x/@xsi:type &= '*Registry*' return $x";
    
    private static String findRegistryQueryWithOutAuthority = 
        " for $x in //<rootnode> where $x/@xsi:type = 'vg:Registry' and $x/vr:identifier &= '*<id>*' return $x";

    private static String findRegistryQueryWithAuthority = 
        " for $x in //<rootnode> where  $x/@xsi:type='vg:RegistryType' and $x/vr:Identifier/vr:AuthorityID = '<id>' return $x";
    
    private static String findAllRegistryQuery =
        " //*[@xsi:type &= '*Registry*']";
    
    private static String findAll =
        " for $x in //<rootnode> return $x";

    private static String startQuery  = 
        " for $x in //<rootnode> where ";
    */
    
    
    /**
     * final variable for the default AuthorityID associated to this registry.
     */
    private static final String AUTHORITYID_PROPERTY =
                                           "reg.amend.authorityid";    
        
    /**
     * Static to be used on the initiatian of this class for the config
     */   
    static {
       if(conf == null) {        
          conf = org.astrogrid.config.SimpleConfig.getSingleton();
          queries.put("findResourceWithoutAuthority",conf.getString("reg.custom.query.one"));
          queries.put("findAllResourceWithoutAuthority",conf.getString("reg.custom.query.two"));
          queries.put("findResourceWithAuthority",conf.getString("reg.custom.query.three"));
          queries.put("findResourceWithAuthorityAndResourceKey",conf.getString("reg.custom.query.four"));
          queries.put("findResourcesWithAuthorityAndResourceKey",conf.getString("reg.custom.query.five"));
          queries.put("findRegistryQuery",conf.getString("reg.custom.query.six"));
          queries.put("findRegistryQueryWithOutAuthority",conf.getString("reg.custom.query.seven"));
          queries.put("findRegistryQueryWithAuthority",conf.getString("reg.custom.query.eight"));
          queries.put("findAllRegistryQuery",conf.getString("reg.custom.query.nine"));
          queries.put("findAll",conf.getString("reg.custom.query.ten"));
          queries.put("startQuery",conf.getString("reg.custom.query.eleven"));          
       }      
    }
    
    public static String getXQLDeclarations(String versionNumber) {
        return conf.getString("reg.custom.declareNS." + versionNumber,"");
    }    
    
    public static String queryForRegistries(String versionNumber) {
        return ((String)queries.get("findRegistryQuery")).replaceAll("<rootnode>",
               RegistryServerHelper.getRootNodeName(versionNumber));
    }
    
    public static String queryForResource(String identifier, String versionNumber) {
        boolean hasAuthorityID = conf.getBoolean(
                "reg.custom.identifier.hasauthorityid." + versionNumber,false);
        String mainQuery = null;        
        
        if(hasAuthorityID) {
            int index = identifier.indexOf("/",7);
            if(index != -1 && index < (identifier.trim().length()-1)) {
                mainQuery = ((String)queries.get("findResourceWithAuthorityAndResourceKey"))
                            .replaceAll("<id>",identifier.substring(6,index));
                mainQuery = mainQuery
                            .replaceAll("<reskey>",identifier.substring((index+1)));
            } else {
                mainQuery = ((String)queries.get("findResourceWithAuthority"))
                .replaceAll("<id>",identifier.substring(6));
            }
        } else {
            mainQuery = ((String)queries.get("findResourceWithoutAuthority")).replaceAll("<id>",identifier);
        }
        return mainQuery.replaceAll("<rootnode>",
        RegistryServerHelper.getRootNodeName(versionNumber));
    }
    
    public static String queryForAllResource(String identifier, String versionNumber) {
        boolean hasAuthorityID = conf.getBoolean(
                "reg.custom.identifier.hasauthorityid." + versionNumber,false);
        String mainQuery = null;        
        
        if(hasAuthorityID) {
            int index = identifier.indexOf("/");
                mainQuery = ((String)queries.get("findResourcesWithAuthorityAndResourceKey"))
                            .replaceAll("<id>",identifier);
                mainQuery = mainQuery
                            .replaceAll("<reskey>",identifier);
        } else {
            mainQuery = ((String)queries.get("findAllResourceWithoutAuthority")).replaceAll("<id>",identifier);
        }
        return mainQuery.replaceAll("<rootnode>",
        RegistryServerHelper.getRootNodeName(versionNumber));
    }

    public static String queryForMainRegistry(String versionNumber) {
        boolean hasAuthorityID = conf.getBoolean(
                "reg.custom.identifier.hasauthorityid." + versionNumber,false);
        String mainQuery = null;
        String authorityID = conf.getString(AUTHORITYID_PROPERTY);
        if(hasAuthorityID) {
            mainQuery = ((String)queries.get("findRegistryQueryWithAuthority")).replaceAll("<id>",authorityID);
        } else {
            mainQuery = ((String)queries.get("findRegistryQueryWithOutAuthority")).replaceAll("<id>",authorityID);
        }
        return mainQuery.replaceAll("<rootnode>",
               RegistryServerHelper.getRootNodeName(versionNumber));
    }
    

    public static String getStartQuery(String versionNumber) {
        return ((String)queries.get("startQuery")).replaceAll("<rootnode>",
               RegistryServerHelper.getRootNodeName(versionNumber));
    }
    
    public static String getAllQuery(String versionNumber) {
        return ((String)queries.get("findAll")).replaceAll("<rootnode>",
        RegistryServerHelper.getRootNodeName(versionNumber));        
    }
    
    public static String getAllRegistryQuery() {
        return ((String)queries.get("findAllRegistryQuery")); 
    }
    
}