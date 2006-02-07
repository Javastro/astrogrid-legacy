package org.astrogrid.registry.server.query;

import org.astrogrid.config.Config;
import org.astrogrid.registry.server.ConfigExtractor;

import java.util.Map;
import java.util.HashMap;

/**
 * Class: QueryConfigExtractor
 * Description: QueryConfigExtractor is class that reads properties of various queries that are used internally
 * here in the registry.  The Registry has to use some queries internally for harvesting and other various methods.
 * Because we allow for the customization of working with other xml databases, these queries are in a properties file
 * in case they desire to change the queries to work with there xml databases better.
 * @author Kevin Benson
 *
 */
public class QueryConfigExtractor extends ConfigExtractor {
    

    /**
     * Small HashMap that has the queries.
     */
    private static Map queries = new HashMap();
        
    
    /**
     * final variable for the default AuthorityID associated to this registry.
     */
    private static final String AUTHORITYID_PROPERTY =
                                           "reg.amend.authorityid";    
        
    /**
     * Static to be used on the initiatian of this class for the config
     */   
    static {
        if(queries.size() == 0) {
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
    
    public static String getDefaultContractVersion() {
        return conf.getString("reg.custom.query.defaultContractVersion","0.1");
    }
    
    /**
     * Method: getXQLDeclarations
     * Description: get the declare namespaces for a particular version.
     * @param versionNumber - versionNumber of the registry (from vr namespace).
     * @return the namespaces.
     */
    public static String getXQLDeclarations(String versionNumber) {
        return conf.getString("reg.custom.declareNS." + versionNumber,"");
    }    
        

    /**
     * Method: queryForRegistries
     * Description: Query for Registry types in the registry.
     * @param versionNumber - versionNumber of the registry (from vr namespace).
     * @return String of the query.
     */
    public static String queryForRegistries(String versionNumber) {
        return ((String)queries.get("findRegistryQuery")).replaceAll("<rootnode>",
               getRootNodeName(versionNumber));
    }

    /**
     * Method: queryForResource
     * Description: Query for a particular in the resource based on the identifier.
     * @param identifier - A identifier string.
     * @param versionNumber - versionNumber of the registry (from vr namespace).
     * @return String of the query.
     */
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
        getRootNodeName(versionNumber));
    }
    
    /**
     * Method: queryForAllResource
     * Description: Query for Resources based on any part of the identifier string.
     * @param identifier - A identifier String.
     * @param versionNumber - versionNumber of the registry (from vr namespace).
     * @return String of the query.
     */
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
        getRootNodeName(versionNumber));
    }

    /**
     * Method: queryForMainRegistry
     * Description: Query for the main registry type.
     * 
     * @param versionNumber - versionNumber of the registry (from vr namespace).
     * @return String of the query.
     */
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
               getRootNodeName(versionNumber));
    }
    

    /**
     * Method: getStartQuery
     * Description: This is the start part of most queries.  Used mainly for building queries up. 
     * @param versionNumber - versionNumber of the registry (from vr namespace).
     * @return String of the query.
     */
    public static String getStartQuery(String versionNumber) {
        return ((String)queries.get("startQuery")).replaceAll("<rootnode>",
               getRootNodeName(versionNumber));
    }
    
    /**
     * Method: getAllQuery
     * Description: Query for all Resources.
     * @param versionNumber - versionNumber of the registry (from vr namespace).
     * @return String of the query.
     */
    public static String getAllQuery(String versionNumber) {
        return ((String)queries.get("findAll")).replaceAll("<rootnode>",
        getRootNodeName(versionNumber));        
    }
    
    /**
     * Method: getAllRegistryQuery
     * Description: Query String for all Registry types.
     * @return String of the query.
     */
    public static String getAllRegistryQuery() {
        return ((String)queries.get("findAllRegistryQuery")); 
    }
}