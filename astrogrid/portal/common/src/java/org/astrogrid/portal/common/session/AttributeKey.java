/*
 * Created on 26-Jan-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.astrogrid.portal.common.session;

/**
 * @author jl99
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public final class AttributeKey {
     
    public static final AttributeKey 
    	USER = new AttributeKey( "user" ),
    	COMMUNITY_NAME = new AttributeKey( "community_name" ),
    	COMMUNITY_AUTHORITY = new AttributeKey( "community_authority" ),
    	CREDENTIAL = new AttributeKey( "credential" ),
    	COMMUNITY_ACCOUNT = new AttributeKey( "community_account" ),
       	ACOUNTSPACE_IVORN = new AttributeKey( "ivorn" ),
       	USER_IVORN = new AttributeKey( "AGO_USER_IVORN" ),
       	FILE_MANAGER_CLIENT = new AttributeKey( "AGO_FILEMANAGERCLIENT" ),
       	MYSPACE_TREE = new AttributeKey( "AGO_MYSPACETREE" ),
        MYSPACE_TREE_OPEN_BRANCHES = new AttributeKey( "AGO_MYSPACETREE_OPENBRANCHES" ),
        CLIENT_SCREEN = new AttributeKey( "AGO_CLIENT_SCREEN" ),
    	QUERY_EDITOR_LAST_MICROBROWSER_VIEW = new AttributeKey( "AGO_QUERY_EDITOR_MB_VIEW" ),
    	WORKFLOW_EDITOR_LAST_MICROBROWSER_VIEW = new AttributeKey( "AGO_WORKFLOW_EDITOR_MB_VIEW" ),
    	PARAMETER_SELECTOR_LAST_MICROBROWSER_VIEW = new AttributeKey( "AGO_PARAMETER_SELECTOR_MB_VIEW" ),
    	MYSPACE_LAST_VIEW = new AttributeKey( "AGO_MYSPACE_LAST_VIEW" ),
    	MYSPACE_TOGGLE_TREE = new AttributeKey( "AGO_MYSPACE_TOGGLE_TREE" );
    
    // Section from query editor...
    public static final AttributeKey 
        ADQL_AS_STRING = new AttributeKey( "adqlQuery" ),
        RESOURCE_ID = new AttributeKey( "uniqueID" ),
        ADQL_ERROR = new AttributeKey( "query-builder-adql-error" ),
        TABLENAME = new AttributeKey( "tableID" ),
        RESULT_SINGLE_CATALOG = new AttributeKey( "resultSingleCatalog" ) ;
    
    private String myName ;
    
    private AttributeKey( String name ){
        this.myName = name ;
    }
    
    public String toString() {
        return this.myName ;
    }

}
