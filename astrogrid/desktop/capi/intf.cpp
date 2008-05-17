/*
C interface to the AR
Paul Harrison paul.harrison@manchester.ac.uk
produced on 2008-01-04Z

DO NOT EDIT - this file is produced automatically by the AR build process

 * Copyright 2007 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
*/
   
   
#include "AR.h"
#include "arintf.h"
#include "intfclasses.h"

         
         /* begin class ivoa.adql
    Support for working with ADQL queries. */
	 
	
			
			
/* function ivoa_adql_s2x(s)convert an adq/s string to an adql/x document
		
		s - (JString)
		
	Returns XMLString - 
       */
XMLString ivoa_adql_s2x ( JString s)
   {
     XmlRpcValue _args, _result;
   XMLString retval;
   _args[0] = s;
   
     if (myAR->execute("ivoa.adql.s2x", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
      /* end class
      ivoa.adql
      */
   /* begin class userInterface.applicationLauncher
    Control the  Application Launcher GUI.
 
 <img src="doc-files/applauncher.png"/>See: 
				org.astrogrid.acr.astrogrid.Applications
			  */
	 
	
			
			
/* function userInterface_applicationLauncher_show()display a  application launcher UI
		
		
	Returns void - 
       */
void userInterface_applicationLauncher_show ( )
   {
     XmlRpcValue _args, _result;
   
     if (myAR->execute("userInterface.applicationLauncher.show", _args, _result))
     {
     
     }
    
   };
   
      /* end class
      userInterface.applicationLauncher
      */
   /* begin class astrogrid.applications
    Work with remote applications -  Compute and Data Query services.
 
 
 The Common Execution Architecture (CEA) provides a uniform way to describe and execute astronomical applications and data services on the VO.
 This interface provides methods to
 <ul>
 <li>Discover available applications</li>
 <li>Build invocation documents containing the correct parameters</li>
 <li>Submit invocation documents for execution on remote servers</li>
 <li>Monitor progress and retreive results of execution</li>
 </ul>
 
 Each new application invocation is assigned a new globally unique id.
  These id's  should be treated as opaque objects - the internal structure is still liable to change.See: 
				<a href="http://www.astrogrid.org/maven/docs/HEAD/applications/design/CEADesignIVOANote.html">Common Execution Architecture - IVOA Proposal</a>
 <br/>
			 
				<a href="http://www.astrogrid.org/maven/docs/HEAD/astrogrid-workflow-objects/schema/Workflow.html#element_tool">Tool Document Schema-Documentation</a>
			 
				<a href="http://www.astrogrid.org/maven/docs/HEAD/astrogrid-workflow-objects/schema/AGParameterDefinition.html#type_parameter">Value Parameter Element Schema-Documentation</a>
			 
				<a href="http://www.astrogrid.org/viewcvs/astrogrid/workflow-objects/schema/">XSD Schemas</a>
 <br/>
			 
				<a href="doc-files/run-app-demo.py">Calling CEA services - example python script</a>
			 
				<a href="doc-files/runAppDemo.groovy">Calling CEA services - example groovy script</a>
			 
				<a href="../dialogs/doc-files/example-tool.xml"> Example Tool Document</a>
 <br/>
			 
				org.astrogrid.acr.ui.ApplicationLauncher
			 
				org.astrogrid.acr.astrogrid.ApplicationInformation
			 
				org.astrogrid.acr.dialogs.ToolEditor
			 
				org.astrogrid.acr.astrogrid.ExecutionInformation
			  */
	 
	
			
			
/* function astrogrid_applications_list()list remote applications available in the registry.
		
		
	Returns ListOfIvornOrURI - a list of the registry identifiers of available applications
       */
ListOfIvornOrURI astrogrid_applications_list ( )
   {
     XmlRpcValue _args, _result;
   ListOfIvornOrURI retval;
   
     if (myAR->execute("astrogrid.applications.list", _args, _result))
     {
     ListOf<IvornOrURI> s = ListOf<IvornOrURI>(_result);

                retval.n = s.size();
                retval.list = copyArray<IvornOrURI, IvornOrURI>(s);

     }
    
     return retval;
    
   };
   
			
			
/* function astrogrid_applications_getRegistryAdqlQuery()helper method - returns the ADQL/s query that should be passed to the registry to
 list all available applications.
 
 can be used as a starting point to build up filters, etc.
		
		
	Returns JString - an adql query string.
       */
JString astrogrid_applications_getRegistryAdqlQuery ( )
   {
     XmlRpcValue _args, _result;
   JString retval;
   
     if (myAR->execute("astrogrid.applications.getRegistryAdqlQuery", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function astrogrid_applications_getRegistryXQuery()helper method - returns the Xquery that should be passed to the registry to
 list all available applications.
 
 can be used as a starting point to build up filters, etc.
		
		
	Returns JString - an xquery string.
       */
JString astrogrid_applications_getRegistryXQuery ( )
   {
     XmlRpcValue _args, _result;
   JString retval;
   
     if (myAR->execute("astrogrid.applications.getRegistryXQuery", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function astrogrid_applications_getCeaApplication(applicationName)get information for a specific application from the registry.
		
		applicationName - name of the application to hunt for(IvornOrURI)
		
	Returns struct CeaApplication - details of this application
       */
struct CeaApplication astrogrid_applications_getCeaApplication ( IvornOrURI applicationName)
   {
     XmlRpcValue _args, _result;
   struct CeaApplication retval;
   _args[0] = applicationName;
   
     if (myAR->execute("astrogrid.applications.getCeaApplication", _args, _result))
     {
     CeaApplication_* res = new CeaApplication_(_result);
        res->asStruct(&retval);
     }
    
     return retval;
    
   };
   
			
			
/* function astrogrid_applications_getDocumentation(applicationName)get formatted information about an application
		
		applicationName - (IvornOrURI)
		
	Returns JString - formatted, human-readable information about the application
       */
JString astrogrid_applications_getDocumentation ( IvornOrURI applicationName)
   {
     XmlRpcValue _args, _result;
   JString retval;
   _args[0] = applicationName;
   
     if (myAR->execute("astrogrid.applications.getDocumentation", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function astrogrid_applications_createTemplateDocument(applicationName, interfaceName)create a template invocation document for a particular application.
 
 Examines the registry entry for this application, and constructs a template document containing fields for the required input and output parameters.
		
		applicationName - the application to create the template for(IvornOrURI)
		interfaceName - interface of this application to create a template from.(JString)
		
	Returns XMLString - a tool document. (xml form)
       */
XMLString astrogrid_applications_createTemplateDocument ( IvornOrURI applicationName, JString interfaceName)
   {
     XmlRpcValue _args, _result;
   XMLString retval;
   _args[0] = applicationName;
   _args[1] = interfaceName;
   
     if (myAR->execute("astrogrid.applications.createTemplateDocument", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function astrogrid_applications_createTemplateStruct(applicationName, interfaceName)create a template invocation datastucture for a particular application.
 
 Examines the registry entry for this application, and constructs a template document containing fields for the required input and output parameters. 
 <br />
 The datastructure returned is equivalent to the document returned by {@link #createTemplateDocument(URI, String)} - 
 this is a convenience method for scripting languages with minimal
 xml abilities.
		
		applicationName - the application to create the template for(IvornOrURI)
		interfaceName - interface of this application to create a template from.(JString)
		
	Returns ACRKeyValueMap - a tool object (structure)
       */
ACRKeyValueMap astrogrid_applications_createTemplateStruct ( IvornOrURI applicationName, JString interfaceName)
   {
     XmlRpcValue _args, _result;
   ACRKeyValueMap retval;
   _args[0] = applicationName;
   _args[1] = interfaceName;
   
     if (myAR->execute("astrogrid.applications.createTemplateStruct", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function astrogrid_applications_convertDocumentToStruct(document)convert a invocation document to a invocation structure. 
 <br />
 Translates an invocation document between two equvalent forms - a datastructure and a document
		
		document - a tool document(XMLString)
		
	Returns ACRKeyValueMap - the equvalent tool structure
       */
ACRKeyValueMap astrogrid_applications_convertDocumentToStruct ( XMLString document)
   {
     XmlRpcValue _args, _result;
   ACRKeyValueMap retval;
   _args[0] = document;
   
     if (myAR->execute("astrogrid.applications.convertDocumentToStruct", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function astrogrid_applications_convertStructToDocument(structure)convert a invocation structure to the equivalent document.
		
		structure - a tool structure(ACRKeyValueMap)
		
	Returns XMLString - the equivalent tool document
       */
XMLString astrogrid_applications_convertStructToDocument ( ACRKeyValueMap structure)
   {
     XmlRpcValue _args, _result;
   XMLString retval;
   _args[0] = structure;
   
     if (myAR->execute("astrogrid.applications.convertStructToDocument", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function astrogrid_applications_validate(document)Validate an invocation document against the  application's description
 <br />
 Verifies that all required parameters are present.
		
		document - tool document to validate(XMLString)
		
	Returns void - 
       */
void astrogrid_applications_validate ( XMLString document)
   {
     XmlRpcValue _args, _result;
   _args[0] = document;
   
     if (myAR->execute("astrogrid.applications.validate", _args, _result))
     {
     
     }
    
   };
   
			
			
/* function astrogrid_applications_validateStored(documentLocation)Validate an invocation document (referenced by url) against 
                an application description
		
		documentLocation - location of a resource containing the tool document to validate(IvornOrURI)
		
	Returns void - 
       */
void astrogrid_applications_validateStored ( IvornOrURI documentLocation)
   {
     XmlRpcValue _args, _result;
   _args[0] = documentLocation;
   
     if (myAR->execute("astrogrid.applications.validateStored", _args, _result))
     {
     
     }
    
   };
   
			
			
/* function astrogrid_applications_listServersProviding(applicationId)list the remote servers that provides a particular application.
 
  (It's possible, for CEA especially, that an application may be provided by multiple servers)
		
		applicationId - registry identifier of the application to search servers for.(IvornOrURI)
		
	Returns ListOfService_Base - list of registry summaries of cea servers that support this application
       */
ListOfService_Base astrogrid_applications_listServersProviding ( IvornOrURI applicationId)
   {
     XmlRpcValue _args, _result;
   ListOfService_Base retval;
   _args[0] = applicationId;
   
     if (myAR->execute("astrogrid.applications.listServersProviding", _args, _result))
     {
     ListOfBase<Service_> s = ListOfBase<Service_>(_result);

                retval.n = s.size();
                retval.list = copyArrayAsBaseStruct<Service_, struct Service_Base>(s);

     }
    
     return retval;
    
   };
   
			
			
/* function astrogrid_applications_submit(document)submit an invocation document for execution..
 
 No particular remote server is specified - the system will select a suitable one.
		
		document - tool document to execute(XMLString)
		
	Returns IvornOrURI - a new unique execution id
       */
IvornOrURI astrogrid_applications_submit ( XMLString document)
   {
     XmlRpcValue _args, _result;
   IvornOrURI retval;
   _args[0] = document;
   
     if (myAR->execute("astrogrid.applications.submit", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function astrogrid_applications_submitTo(document, server)submit an invocation document for execution  on a named remote server.
		
		document - tool document to execute(XMLString)
		server - remote server to execute on(IvornOrURI)
		
	Returns IvornOrURI - a new unique execution id
       */
IvornOrURI astrogrid_applications_submitTo ( XMLString document, IvornOrURI server)
   {
     XmlRpcValue _args, _result;
   IvornOrURI retval;
   _args[0] = document;
   _args[1] = server;
   
     if (myAR->execute("astrogrid.applications.submitTo", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function astrogrid_applications_submitStored(documentLocation)a variant of {@link #submit} where invocation document is stored somewhere and referenced by URI.
		
		documentLocation - pointer to tool document - may be file:/, http://, ftp:// or ivo:// (myspace) protocols(IvornOrURI)
		
	Returns IvornOrURI - a new unique execution id
       */
IvornOrURI astrogrid_applications_submitStored ( IvornOrURI documentLocation)
   {
     XmlRpcValue _args, _result;
   IvornOrURI retval;
   _args[0] = documentLocation;
   
     if (myAR->execute("astrogrid.applications.submitStored", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function astrogrid_applications_submitStoredTo(documentLocation, server)variant of {@link #submitTo} where tool document is referenced by URI. 
      * @param documentLocation pointer to tool document - may be file:/, http://, ftp:// or ivo:// (myspace) protocols
		
		documentLocation - (IvornOrURI)
		server - remote server to execute on(IvornOrURI)
		
	Returns IvornOrURI - a new unique execution id
       */
IvornOrURI astrogrid_applications_submitStoredTo ( IvornOrURI documentLocation, IvornOrURI server)
   {
     XmlRpcValue _args, _result;
   IvornOrURI retval;
   _args[0] = documentLocation;
   _args[1] = server;
   
     if (myAR->execute("astrogrid.applications.submitStoredTo", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function astrogrid_applications_cancel(executionId)cancel execution of an application.
		
		executionId - id of execution to cancel(IvornOrURI)
		
	Returns void - 
       */
void astrogrid_applications_cancel ( IvornOrURI executionId)
   {
     XmlRpcValue _args, _result;
   _args[0] = executionId;
   
     if (myAR->execute("astrogrid.applications.cancel", _args, _result))
     {
     
     }
    
   };
   
			
			
/* function astrogrid_applications_getExecutionInformation(executionId)retrive  information about an application execution.
		
		executionId - id of application to query(IvornOrURI)
		
	Returns struct ExecutionInformation - summary of this execution
       */
struct ExecutionInformation astrogrid_applications_getExecutionInformation ( IvornOrURI executionId)
   {
     XmlRpcValue _args, _result;
   struct ExecutionInformation retval;
   _args[0] = executionId;
   
     if (myAR->execute("astrogrid.applications.getExecutionInformation", _args, _result))
     {
     ExecutionInformation_* res = new ExecutionInformation_(_result);
        res->asStruct(&retval);
     }
    
     return retval;
    
   };
   
			
			
/* function astrogrid_applications_getResults(executionid)retreive results of the application execution .
		
		executionid - id of application to query(IvornOrURI)
		
	Returns ACRKeyValueMap - results of this execution (name - value pairs). Note that this will only be the actual results for <b>direct</b> output parameters. For output parameters specified as <b>indirect</b>, the value returned  will be the URI pointing to the location where the results are stored.
       */
ACRKeyValueMap astrogrid_applications_getResults ( IvornOrURI executionid)
   {
     XmlRpcValue _args, _result;
   ACRKeyValueMap retval;
   _args[0] = executionid;
   
     if (myAR->execute("astrogrid.applications.getResults", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
      /* end class
      astrogrid.applications
      */
   /* begin class userInterface.astroscope
    Control  AstroScope.
 
 <img src="doc-files/astroscope.png"/> */
	 
	
			
			
/* function userInterface_astroscope_show()display a new instance of astroscope
		
		
	Returns void - 
       */
void userInterface_astroscope_show ( )
   {
     XmlRpcValue _args, _result;
   
     if (myAR->execute("userInterface.astroscope.show", _args, _result))
     {
     
     }
    
   };
   
      /* end class
      userInterface.astroscope
      */
   /* begin class ivoa.cache
    data cache. */
	 
	
			
			
/* function ivoa_cache_flush()flush all cached data - for example registry entries
		
		
	Returns void - 
       */
void ivoa_cache_flush ( )
   {
     XmlRpcValue _args, _result;
   
     if (myAR->execute("ivoa.cache.flush", _args, _result))
     {
     
     }
    
   };
   
      /* end class
      ivoa.cache
      */
   /* begin class astrogrid.community
    astogrid identity and authentication.
 
 At the moment provides login ability. Later will provide access to permissioning and quota information for the current user.
 <img src="doc-files/login.png"/> */
	 
	
			
			
/* function astrogrid_community_login(username, password, community)login to astrogrid - identify yourself
		
		username - user name (e.g. <tt>fredbloggs</tt>)(JString)
		password - password for this user(JString)
		community - community the user is registered with (e.g. <tt>uk.ac.astogrid</tt> )(JString)
		
	Returns void - 
       */
void astrogrid_community_login ( JString username, JString password, JString community)
   {
     XmlRpcValue _args, _result;
   _args[0] = username;
   _args[1] = password;
   _args[2] = community;
   
     if (myAR->execute("astrogrid.community.login", _args, _result))
     {
     
     }
    
   };
   
			
			
/* function astrogrid_community_getUserInformation()access information about the currently logged in user.
 
 <b>This method forces login if not already logged in.</b>
		
		
	Returns struct UserInformation - information about the current user.
       */
struct UserInformation astrogrid_community_getUserInformation ( )
   {
     XmlRpcValue _args, _result;
   struct UserInformation retval;
   
     if (myAR->execute("astrogrid.community.getUserInformation", _args, _result))
     {
     UserInformation_* res = new UserInformation_(_result);
        res->asStruct(&retval);
     }
    
     return retval;
    
   };
   
			
			
/* function astrogrid_community_logout()log current user out of astrogrid
		
		
	Returns void - 
       */
void astrogrid_community_logout ( )
   {
     XmlRpcValue _args, _result;
   
     if (myAR->execute("astrogrid.community.logout", _args, _result))
     {
     
     }
    
   };
   
			
			
/* function astrogrid_community_isLoggedIn()verify user is currently logged in.
		
		
	Returns BOOL - true if the user is logged in
       */
BOOL astrogrid_community_isLoggedIn ( )
   {
     XmlRpcValue _args, _result;
   BOOL retval;
   
     if (myAR->execute("astrogrid.community.isLoggedIn", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function astrogrid_community_guiLogin()display the login dialogue to prompt the user for input, and then log in
		
		
	Returns void - 
       */
void astrogrid_community_guiLogin ( )
   {
     XmlRpcValue _args, _result;
   
     if (myAR->execute("astrogrid.community.guiLogin", _args, _result))
     {
     
     }
    
   };
   
      /* end class
      astrogrid.community
      */
   /* begin class ivoa.cone
    Query catalogs using Cone-search services. */
	 
	
			
			
/* function ivoa_cone_addOption(query, optionName, optionValue)add an option to a previously constructed query
		
		query - the query url(URLString)
		optionName - name of the option to add(JString)
		optionValue - value for the new option(JString)
		
	Returns URLString - <tt>query</tt> with the option appended.
       */
URLString ivoa_cone_addOption ( URLString query, JString optionName, JString optionValue)
   {
     XmlRpcValue _args, _result;
   URLString retval;
   _args[0] = query;
   _args[1] = optionName;
   _args[2] = optionValue;
   
     if (myAR->execute("ivoa.cone.addOption", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function ivoa_cone_execute(query)execute a DAL query, returning a datastructure
		
		query - query url to execute(URLString)
		
	Returns ListOfACRKeyValueMap - A model the DAL query response as a list of  of rows. Each row is represented is a map between UCD keys or datamodel names   and values from the response
       */
ListOfACRKeyValueMap ivoa_cone_execute ( URLString query)
   {
     XmlRpcValue _args, _result;
   ListOfACRKeyValueMap retval;
   _args[0] = query;
   
     if (myAR->execute("ivoa.cone.execute", _args, _result))
     {
     ListOf<ACRKeyValueMap> s = ListOf<ACRKeyValueMap>(_result);

                retval.n = s.size();
                retval.list = copyArray<ACRKeyValueMap, ACRKeyValueMap>(s);

     }
    
     return retval;
    
   };
   
			
			
/* function ivoa_cone_executeVotable(query)execute a DAL query, returning a votable document.
 
 This is a convenience method  - just performs a 'GET' on the query url- many programming languages support this functionality themselves
		
		query - query url to execute(URLString)
		
	Returns XMLString - a votable document of results
       */
XMLString ivoa_cone_executeVotable ( URLString query)
   {
     XmlRpcValue _args, _result;
   XMLString retval;
   _args[0] = query;
   
     if (myAR->execute("ivoa.cone.executeVotable", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function ivoa_cone_executeAndSave(query, saveLocation)execute a DAL query and save the resulting document.
		
		query - query url to execute(URLString)
		saveLocation - location to save result document - may be file:/, ivo:// (myspace), ftp://(IvornOrURI)
		
	Returns void - 
       */
void ivoa_cone_executeAndSave ( URLString query, IvornOrURI saveLocation)
   {
     XmlRpcValue _args, _result;
   _args[0] = query;
   _args[1] = saveLocation;
   
     if (myAR->execute("ivoa.cone.executeAndSave", _args, _result))
     {
     
     }
    
   };
   
			
			
/* function ivoa_cone_saveDatasets(query, saveLocation)save the datasets pointed to by this DAL query response
		
		query - the DAL query(URLString)
		saveLocation - location of a directory in which to save the datasets. may be a file:/, ivo:// or ftp:// reference(IvornOrURI)
		
	Returns int - number of datasets saved.
       */
int ivoa_cone_saveDatasets ( URLString query, IvornOrURI saveLocation)
   {
     XmlRpcValue _args, _result;
   int retval;
   _args[0] = query;
   _args[1] = saveLocation;
   
     if (myAR->execute("ivoa.cone.saveDatasets", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function ivoa_cone_saveDatasetsSubset(query, saveLocation, rows)save a subset of the datasets point to by this DAL query response
		
		query - the DAL query(URLString)
		saveLocation - location of a directory in which to save the datasets. may be a file:/, ivo:// or ftp:// reference(IvornOrURI)
		rows - list of Integers - indexes of the rows in the query response for which to save the dataset.(ACRList)
		
	Returns int - number of datasets saved.
       */
int ivoa_cone_saveDatasetsSubset ( URLString query, IvornOrURI saveLocation, ACRList rows)
   {
     XmlRpcValue _args, _result;
   int retval;
   _args[0] = query;
   _args[1] = saveLocation;
   _args[2] = rows;
   
     if (myAR->execute("ivoa.cone.saveDatasetsSubset", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function ivoa_cone_getRegistryAdqlQuery()helper method - returns an ADQL/s query that should be passed to a registry to list all 
 available DAL services of this type. 
 <br/>
 can be used as a starting point for filters, etc.
		
		
	Returns JString - an adql query string
       */
JString ivoa_cone_getRegistryAdqlQuery ( )
   {
     XmlRpcValue _args, _result;
   JString retval;
   
     if (myAR->execute("ivoa.cone.getRegistryAdqlQuery", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function ivoa_cone_getRegistryXQuery()helper method - returns an Xquery that should be passed to a registry to list all 
 available DAL services of this type. 
 <br/>
 can be used as a starting point for filters, etc.
		
		
	Returns JString - an xquery string
       */
JString ivoa_cone_getRegistryXQuery ( )
   {
     XmlRpcValue _args, _result;
   JString retval;
   
     if (myAR->execute("ivoa.cone.getRegistryXQuery", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function ivoa_cone_constructQuery(service, ra, dec, sr)construct a query on RA, DEC, SR
		
		service - URL of the service endpoint, or ivorn of the service description(IvornOrURI)
		ra - right ascension(double)
		dec - declination(double)
		sr - search radius(double)
		
	Returns URLString - query URL that can be fetched using a HTTP GET to execute the query
       */
URLString ivoa_cone_constructQuery ( IvornOrURI service, double ra, double dec, double sr)
   {
     XmlRpcValue _args, _result;
   URLString retval;
   _args[0] = service;
   _args[1] = ra;
   _args[2] = dec;
   _args[3] = sr;
   
     if (myAR->execute("ivoa.cone.constructQuery", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
      /* end class
      ivoa.cone
      */
   /* begin class nvo.cone
    Deprecated: 
				use the ivoa.cone interface instead.
			Query  catalogs using Cone-search services */
	 
	
			
			
/* function nvo_cone_constructQuery(service, ra, dec, sr)construct a query on RA, DEC, SR
		
		service - URL of the service endpoint, or ivorn of the service description(IvornOrURI)
		ra - right ascension(double)
		dec - declination(double)
		sr - search radius(double)
		
	Returns URLString - query URL that can be fetched using a HTTP GET to execute the query
       */
URLString nvo_cone_constructQuery ( IvornOrURI service, double ra, double dec, double sr)
   {
     XmlRpcValue _args, _result;
   URLString retval;
   _args[0] = service;
   _args[1] = ra;
   _args[2] = dec;
   _args[3] = sr;
   
     if (myAR->execute("nvo.cone.constructQuery", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function nvo_cone_addOption(coneQuery, optionName, optionValue)Add an option to a previously constructed query
		
		coneQuery - the query url(URLString)
		optionName - the name of the option to add(JString)
		optionValue - value for the new option(JString)
		
	Returns URLString - <tt>query</tt> with the option appended.
       */
URLString nvo_cone_addOption ( URLString coneQuery, JString optionName, JString optionValue)
   {
     XmlRpcValue _args, _result;
   URLString retval;
   _args[0] = coneQuery;
   _args[1] = optionName;
   _args[2] = optionValue;
   
     if (myAR->execute("nvo.cone.addOption", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function nvo_cone_getResults(coneQuery)execute a cone query.
 
 Convenience method - just performs a 'GET' on the query URL - many programming languages
 support this themselves
		
		coneQuery - query url to execute(URLString)
		
	Returns XMLString - a votable of results
       */
XMLString nvo_cone_getResults ( URLString coneQuery)
   {
     XmlRpcValue _args, _result;
   XMLString retval;
   _args[0] = coneQuery;
   
     if (myAR->execute("nvo.cone.getResults", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function nvo_cone_saveResults(coneQuery, saveLocation)execute a cone query and save the results
		
		coneQuery - the query url to execute(URLString)
		saveLocation - location to save result document - may be file://, ivo:// (myspace), ftp://(IvornOrURI)
		
	Returns void - 
       */
void nvo_cone_saveResults ( URLString coneQuery, IvornOrURI saveLocation)
   {
     XmlRpcValue _args, _result;
   _args[0] = coneQuery;
   _args[1] = saveLocation;
   
     if (myAR->execute("nvo.cone.saveResults", _args, _result))
     {
     
     }
    
   };
   
			
			
/* function nvo_cone_getRegistryAdqlQuery()returns an ADQL/s query that should be passed to a registry to list all available cone services
		
		
	Returns JString - 
       */
JString nvo_cone_getRegistryAdqlQuery ( )
   {
     XmlRpcValue _args, _result;
   JString retval;
   
     if (myAR->execute("nvo.cone.getRegistryAdqlQuery", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function nvo_cone_getRegistryXQuery()returns an xquery that should be passed to a registry to list all available cone services
		
		
	Returns JString - 
       */
JString nvo_cone_getRegistryXQuery ( )
   {
     XmlRpcValue _args, _result;
   JString retval;
   
     if (myAR->execute("nvo.cone.getRegistryXQuery", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
      /* end class
      nvo.cone
      */
   /* begin class cds.coordinate
    Astronomical Coordinate Web Service, from CDSSee: 
				http://cdsweb.u-strasbg.fr/cdsws/astroCoo.gml
			  */
	 
	
			
			
/* function cds_coordinate_convert(x, y, z, precision)convert a coordinate
		
		x - (10.0)(double)
		y - (15.0)(double)
		z - (20.0)(double)
		precision - (0=NONE, 1=DEG, 3=ARCMIN, 5=ARCSEC, 8=MAS)(int)
		
	Returns JString - a String like 03 45 14.3838 +47 58 07.990 (J2000.0)
       */
JString cds_coordinate_convert ( double x, double y, double z, int precision)
   {
     XmlRpcValue _args, _result;
   JString retval;
   _args[0] = x;
   _args[1] = y;
   _args[2] = z;
   _args[3] = precision;
   
     if (myAR->execute("cds.coordinate.convert", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function cds_coordinate_convertL(lon, lat, precision)convert a longitude-lattitude coordinate
		
		lon - (12.0)(double)
		lat - (45.0)(double)
		precision - (0=NONE, 1=DEG, 3=ARCMIN, 5=ARCSEC, 8=MAS)(int)
		
	Returns JString - a String like 04 21 34. +53 32.5 (J2000.0)
       */
JString cds_coordinate_convertL ( double lon, double lat, int precision)
   {
     XmlRpcValue _args, _result;
   JString retval;
   _args[0] = lon;
   _args[1] = lat;
   _args[2] = precision;
   
     if (myAR->execute("cds.coordinate.convertL", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function cds_coordinate_convertE(frame1, frame2, x, y, z, precision, equinox1, equinox2)convert a coordinate, considering equinox
		
		frame1 - (1=FK4, 2=GAL, 3=SGAL, 4=ECL, 5=FK5, 6=ICRS)(int)
		frame2 - (1=FK4, 2=GAL, 3=SGAL, 4=ECL, 5=FK5, 6=ICRS)(int)
		x - (10.0)(double)
		y - (15.0)(double)
		z - (20.0)(double)
		precision - (0=NONE, 1=DEG, 3=ARCMIN, 5=ARCSEC, 8=MAS)(int)
		equinox1 - (Julian Years or Besselian, unused for GAL, SGAL, ICRS)(double)
		equinox2 - (Julian Years or Besselian, unused for GAL, SGAL, ICRS)(double)
		
	Returns JString - a String like 150.4806267 -05.3873952 (Gal)
       */
JString cds_coordinate_convertE ( int frame1, int frame2, double x, double y, double z, int precision, double equinox1, double equinox2)
   {
     XmlRpcValue _args, _result;
   JString retval;
   _args[0] = frame1;
   _args[1] = frame2;
   _args[2] = x;
   _args[3] = y;
   _args[4] = z;
   _args[5] = precision;
   _args[6] = equinox1;
   _args[7] = equinox2;
   
     if (myAR->execute("cds.coordinate.convertE", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function cds_coordinate_convertLE(frame1, frame2, lon, lat, precision, equinox1, equinox2)convert a longitude-latitude coordinate,  considering equinox
		
		frame1 - (1=FK4, 2=GAL, 3=SGAL, 4=ECL, 5=FK5, 6=ICRS)(int)
		frame2 - (1=FK4, 2=GAL, 3=SGAL, 4=ECL, 5=FK5, 6=ICRS)(int)
		lon - (12.0)(double)
		lat - (45.0)(double)
		precision - (0=NONE, 1=DEG, 3=ARCMIN, 5=ARCSEC, 8=MAS)(int)
		equinox1 - (Julian Years or Besselian, unused for GAL, SGAL, ICRS)(double)
		equinox2 - (Julian Years or Besselian, unused for GAL, SGAL, ICRS)(double)
		
	Returns JString - 
       */
JString cds_coordinate_convertLE ( int frame1, int frame2, double lon, double lat, int precision, double equinox1, double equinox2)
   {
     XmlRpcValue _args, _result;
   JString retval;
   _args[0] = frame1;
   _args[1] = frame2;
   _args[2] = lon;
   _args[3] = lat;
   _args[4] = precision;
   _args[5] = equinox1;
   _args[6] = equinox2;
   
     if (myAR->execute("cds.coordinate.convertLE", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
      /* end class
      cds.coordinate
      */
   /* begin class ivoa.externalRegistry
    Query an arbitrary registry service.
 
 This interface gives access to a range of querying functions - for querying using xQuery, 
 keyword, adlq/s and adql/x. The functions either return a raw XML document, or a series of
 datastructures that contain the parsed information of the registry entries.s
 
 The first parameter to each query method is the endpoint URL of the registry service to connect to.
 Such endpoints either need to be already known, or can be located using the RegistryOfRegistries.
 
 In future, these functions will also accept the IVORN name of a registry - which 
 will then be resolved using the RegistryOfRegistries before processing the query.
 However, the RegistryOfRegistries isn't available yet.

These functions are useful when you want to access records in a registry
 other than the 'system configured' registry,
 or if you wish to access the raw xml of the records.
 For other cases, we recommend using the simple 'ivoa.Registry' service.See: 
				<a href="http://www.ivoa.net/Documents/latest/IDs.html">IVOA Identifiers</a>
			 
				<a href="http://www.ivoa.net/twiki/bin/view/IVOA/ResourceMetadata">Resource Metadata</a>
			 
				<a href="http://www.ivoa.net/Documents/latest/RM.html">IVOA Resource Metadata for the VO</a>
			 
				<a href="http://www.ivoa.net/Documents/latest/ADQL.html">ADQL Query Language Specification</a>
			 
				<a href="http://www.ivoa.net/twiki/bin/view/IVOA/IvoaResReg">IVOA Registry Working Group</a>
			 
				<a href="http://www.w3schools.com/xquery/default.asp">XQuery Language Tutorial</a>
			 
				org.astrogrid.acr.ui.RegistryBrowser
			 
				org.astrogrid.acr.ivoa.Registry - queries the system-configured registry - suitable for most cases.
			  */
	 
	
			
			
/* function ivoa_externalRegistry_adqlxSearchXML(registry, adqlx, identifiersOnly)Perform an ADQL/x query
 
 Equivalent to  {@link #adqlsSearchXML} but expects the full xml form of ADQL - this is less
 error prone than the adql/s variant until someone defines adql/s properly and implements parsers for it.
		
		registry - (IvornOrURI)
		adqlx - (XMLString)
		identifiersOnly - (BOOL)
		
	Returns XMLString - 
       */
XMLString ivoa_externalRegistry_adqlxSearchXML ( IvornOrURI registry, XMLString adqlx, BOOL identifiersOnly)
   {
     XmlRpcValue _args, _result;
   XMLString retval;
   _args[0] = registry;
   _args[1] = adqlx;
   _args[2] = identifiersOnly;
   
     if (myAR->execute("ivoa.externalRegistry.adqlxSearchXML", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function ivoa_externalRegistry_adqlxSearch(registry, adqlx)Perform an ADQL/x query, returning an array of datastructures.
 
 Equivalent to  {@link #adqlsSearch} but expects the full xml form of ADQL - which is less
 error prone than the adql/s variant until someone defines adql/s properly and implements parsers for it.
		
		registry - (IvornOrURI)
		adqlx - (XMLString)
		
	Returns ListOfResource_Base - 
       */
ListOfResource_Base ivoa_externalRegistry_adqlxSearch ( IvornOrURI registry, XMLString adqlx)
   {
     XmlRpcValue _args, _result;
   ListOfResource_Base retval;
   _args[0] = registry;
   _args[1] = adqlx;
   
     if (myAR->execute("ivoa.externalRegistry.adqlxSearch", _args, _result))
     {
     ListOfBase<Resource_> s = ListOfBase<Resource_>(_result);

                retval.n = s.size();
                retval.list = copyArrayAsBaseStruct<Resource_, struct Resource_Base>(s);

     }
    
     return retval;
    
   };
   
			
			
/* function ivoa_externalRegistry_adqlsSearchXML(registry, adqls, identifiersOnly)Perform a ADQL/s query.
 Although convenient, prefer xquerySearch instead - as ADQL is less expressive and more poorly (especially adql/s) defined than xquery
		
		registry - identifier or endpoint of the registry to connect to(IvornOrURI)
		adqls - (JString)
		identifiersOnly - (BOOL)
		
	Returns XMLString - xml document of search results -  a series of matching registry records contained within an element  called <tt>VOResources</tt> in the namespace <tt>http://www.ivoa.net/wsdl/RegistrySearch/v1.0</tt>
       */
XMLString ivoa_externalRegistry_adqlsSearchXML ( IvornOrURI registry, JString adqls, BOOL identifiersOnly)
   {
     XmlRpcValue _args, _result;
   XMLString retval;
   _args[0] = registry;
   _args[1] = adqls;
   _args[2] = identifiersOnly;
   
     if (myAR->execute("ivoa.externalRegistry.adqlsSearchXML", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function ivoa_externalRegistry_adqlsSearch(registry, adqls)Perform an ADQL/s query, returning an array of datastructures.
 
 Equivalent to {@link #adqlsSearchXML} but returning results in form that can be more easily used.
		
		registry - (IvornOrURI)
		adqls - (JString)
		
	Returns ListOfResource_Base - 
       */
ListOfResource_Base ivoa_externalRegistry_adqlsSearch ( IvornOrURI registry, JString adqls)
   {
     XmlRpcValue _args, _result;
   ListOfResource_Base retval;
   _args[0] = registry;
   _args[1] = adqls;
   
     if (myAR->execute("ivoa.externalRegistry.adqlsSearch", _args, _result))
     {
     ListOfBase<Resource_> s = ListOfBase<Resource_>(_result);

                retval.n = s.size();
                retval.list = copyArrayAsBaseStruct<Resource_, struct Resource_Base>(s);

     }
    
     return retval;
    
   };
   
			
			
/* function ivoa_externalRegistry_keywordSearchXML(registry, keywords, orValues)perform a keyword search
		
		registry - identifier or endpoint of the registry to connect to(IvornOrURI)
		keywords - space separated list of keywords to search for(JString)
		orValues - - true to 'OR' together matches. false to 'AND' together matches(BOOL)
		
	Returns XMLString - xml document of search results, same format as result of {@link #adqlSearchXML}
       */
XMLString ivoa_externalRegistry_keywordSearchXML ( IvornOrURI registry, JString keywords, BOOL orValues)
   {
     XmlRpcValue _args, _result;
   XMLString retval;
   _args[0] = registry;
   _args[1] = keywords;
   _args[2] = orValues;
   
     if (myAR->execute("ivoa.externalRegistry.keywordSearchXML", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function ivoa_externalRegistry_keywordSearch(registry, keywords, orValues)Perform a keyword search and return a list of datastructures.
        A more convenient variant of {@link #keywordSearchXML}
		
		registry - (IvornOrURI)
		keywords - (JString)
		orValues - (BOOL)
		
	Returns ListOfResource_Base - 
       */
ListOfResource_Base ivoa_externalRegistry_keywordSearch ( IvornOrURI registry, JString keywords, BOOL orValues)
   {
     XmlRpcValue _args, _result;
   ListOfResource_Base retval;
   _args[0] = registry;
   _args[1] = keywords;
   _args[2] = orValues;
   
     if (myAR->execute("ivoa.externalRegistry.keywordSearch", _args, _result))
     {
     ListOfBase<Resource_> s = ListOfBase<Resource_>(_result);

                retval.n = s.size();
                retval.list = copyArrayAsBaseStruct<Resource_, struct Resource_Base>(s);

     }
    
     return retval;
    
   };
   
			
			
/* function ivoa_externalRegistry_getResourceXML(registry, id)Retreive a record document from the registry
		
		registry - identifier or endpoint of the registry to connect to(IvornOrURI)
		id - identifier of the registry entry to retrrieve(IvornOrURI)
		
	Returns XMLString - xml document of the registry entry - a <tt>Resource</tt> document   probably in the <tt>http://www.ivoa.net/xml/VOResource/v1.0</tt> namespace
       */
XMLString ivoa_externalRegistry_getResourceXML ( IvornOrURI registry, IvornOrURI id)
   {
     XmlRpcValue _args, _result;
   XMLString retval;
   _args[0] = registry;
   _args[1] = id;
   
     if (myAR->execute("ivoa.externalRegistry.getResourceXML", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function ivoa_externalRegistry_getResource(registry, id)Retrieve a record from the registry, returning it as a datastructure
 
 For most uses, it's better to use this method instead of {@link #getResourceXML} as the result is easier to work with.
		
		registry - identifier or endpoint of the registry to connect to(IvornOrURI)
		id - identifier of the registry entry to retrieve(IvornOrURI)
		
	Returns struct Resource_Base - a  datastructue representing the registry entry - will be a {@link Resource} or one of it's   subclasses depending on the registry entry type.
       */
struct Resource_Base ivoa_externalRegistry_getResource ( IvornOrURI registry, IvornOrURI id)
   {
     XmlRpcValue _args, _result;
   struct Resource_Base retval;
   _args[0] = registry;
   _args[1] = id;
   
     if (myAR->execute("ivoa.externalRegistry.getResource", _args, _result))
     {
     Resource_* res = Resource_::create(_result);
        res->asStruct(&retval);
     }
    
     return retval;
    
   };
   
			
			
/* function ivoa_externalRegistry_xquerySearchXML(registry, xquery)perform an XQuery
		
		registry - identifier or endpoint of the registry to connect to(IvornOrURI)
		xquery - the query to perform. Must return a well-formed xml document - i.e. starting with a single root element.(JString)
		
	Returns XMLString - the result of executing this xquery over the specified registry - a document of arbitrary structure.
       */
XMLString ivoa_externalRegistry_xquerySearchXML ( IvornOrURI registry, JString xquery)
   {
     XmlRpcValue _args, _result;
   XMLString retval;
   _args[0] = registry;
   _args[1] = xquery;
   
     if (myAR->execute("ivoa.externalRegistry.xquerySearchXML", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function ivoa_externalRegistry_xquerySearch(registry, xquery)Variant of xquerySearchXML that returns registry records as data structures
		
		registry - endpoint of registry service to connect to.(IvornOrURI)
		xquery - should return a document, or nodeset, containing &lt;vor:Resource&gt; elements.   Results are not required to be single-rooted, and resource elements may be embedded within other elements.(JString)
		
	Returns ListOfResource_Base - an array containing any registry records present in the query result.
       */
ListOfResource_Base ivoa_externalRegistry_xquerySearch ( IvornOrURI registry, JString xquery)
   {
     XmlRpcValue _args, _result;
   ListOfResource_Base retval;
   _args[0] = registry;
   _args[1] = xquery;
   
     if (myAR->execute("ivoa.externalRegistry.xquerySearch", _args, _result))
     {
     ListOfBase<Resource_> s = ListOfBase<Resource_>(_result);

                retval.n = s.size();
                retval.list = copyArrayAsBaseStruct<Resource_, struct Resource_Base>(s);

     }
    
     return retval;
    
   };
   
			
			
/* function ivoa_externalRegistry_getIdentityXML(registry)Retreive a a description of this registry, returning it asan xml document
		
		registry - identifier or endpoint of the registry to connect to(IvornOrURI)
		
	Returns XMLString - that registries own service description - a single Resource documnt
       */
XMLString ivoa_externalRegistry_getIdentityXML ( IvornOrURI registry)
   {
     XmlRpcValue _args, _result;
   XMLString retval;
   _args[0] = registry;
   
     if (myAR->execute("ivoa.externalRegistry.getIdentityXML", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function ivoa_externalRegistry_getIdentity(registry)Retreive a a description of this registry, returning it as a datastructure
		
		registry - identifier or endpoint of the registry to connect to(IvornOrURI)
		
	Returns struct RegistryService - that registries own service description - a single ResourceDocument
       */
struct RegistryService ivoa_externalRegistry_getIdentity ( IvornOrURI registry)
   {
     XmlRpcValue _args, _result;
   struct RegistryService retval;
   _args[0] = registry;
   
     if (myAR->execute("ivoa.externalRegistry.getIdentity", _args, _result))
     {
     RegistryService_* res = new RegistryService_(_result);
        res->asStruct(&retval);
     }
    
     return retval;
    
   };
   
			
			
/* function ivoa_externalRegistry_buildResources(doc)convenience function - build an array of resouce objects from an xml document
		
		doc - (XMLString)
		
	Returns ListOfResource_Base - 
       */
ListOfResource_Base ivoa_externalRegistry_buildResources ( XMLString doc)
   {
     XmlRpcValue _args, _result;
   ListOfResource_Base retval;
   _args[0] = doc;
   
     if (myAR->execute("ivoa.externalRegistry.buildResources", _args, _result))
     {
     ListOfBase<Resource_> s = ListOfBase<Resource_>(_result);

                retval.n = s.size();
                retval.list = copyArrayAsBaseStruct<Resource_, struct Resource_Base>(s);

     }
    
     return retval;
    
   };
   
			
			
/* function ivoa_externalRegistry_getRegistryOfRegistriesEndpoint()returns the service endpoint of the standard IVOA registry of registries
 this registry can be used to query for other registry services
		
		
	Returns IvornOrURI - 
       */
IvornOrURI ivoa_externalRegistry_getRegistryOfRegistriesEndpoint ( )
   {
     XmlRpcValue _args, _result;
   IvornOrURI retval;
   
     if (myAR->execute("ivoa.externalRegistry.getRegistryOfRegistriesEndpoint", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
      /* end class
      ivoa.externalRegistry
      */
   /* begin class userInterface.fileManager
     */
	 
	
			
			
/* function userInterface_fileManager_show()
		
		
	Returns void - 
       */
void userInterface_fileManager_show ( )
   {
     XmlRpcValue _args, _result;
   
     if (myAR->execute("userInterface.fileManager.show", _args, _result))
     {
     
     }
    
   };
   
      /* end class
      userInterface.fileManager
      */
   /* begin class cds.glu
    Deprecated: 
				the webservice interface at CDS this client calls doesn't seem to be maintained
			Webservice to resolve  GLU  (Generateur de Liens Uniformes).tags.See: 
				http://cdsweb.u-strasbg.fr/cdsws/glu_resolver.gml
			  */
	 
	
			
			
/* function cds_glu_getURLfromTag(id)Resolve a tag.
		
		id - a tag to resolve (example : VizieR.MetaCat)(JString)
		
	Returns JString - Result :  URL corresponding to the tag (example : http://vizier.u-strasbg.fr/cgi-bin/votable?-meta)
       */
JString cds_glu_getURLfromTag ( JString id)
   {
     XmlRpcValue _args, _result;
   JString retval;
   _args[0] = id;
   
     if (myAR->execute("cds.glu.getURLfromTag", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
      /* end class
      cds.glu
      */
   /* begin class userInterface.jobMonitor
    Deprecated: 
				replaced by {@link Lookout}
			Control the Job Monitor GUI.See: 
				org.astrogrid.acr.astrogrid.Jobs
			  */
	 
	
			
			
/* function userInterface_jobMonitor_show()show the job monitor window
		
		
	Returns void - 
       */
void userInterface_jobMonitor_show ( )
   {
     XmlRpcValue _args, _result;
   
     if (myAR->execute("userInterface.jobMonitor.show", _args, _result))
     {
     
     }
    
   };
   
			
			
/* function userInterface_jobMonitor_hide()hide the job monitor  window
		
		
	Returns void - 
       */
void userInterface_jobMonitor_hide ( )
   {
     XmlRpcValue _args, _result;
   
     if (myAR->execute("userInterface.jobMonitor.hide", _args, _result))
     {
     
     }
    
   };
   
			
			
/* function userInterface_jobMonitor_refresh()manually refresh the job list
		
		
	Returns void - 
       */
void userInterface_jobMonitor_refresh ( )
   {
     XmlRpcValue _args, _result;
   
     if (myAR->execute("userInterface.jobMonitor.refresh", _args, _result))
     {
     
     }
    
   };
   
			
			
/* function userInterface_jobMonitor_addApplication(name, executionId)Add a new application to the monitor
		
		name - user-friendly name of the app(JString)
		executionId - identifier of the application(IvornOrURI)
		
	Returns void - 
       */
void userInterface_jobMonitor_addApplication ( JString name, IvornOrURI executionId)
   {
     XmlRpcValue _args, _result;
   _args[0] = name;
   _args[1] = executionId;
   
     if (myAR->execute("userInterface.jobMonitor.addApplication", _args, _result))
     {
     
     }
    
   };
   
			
			
/* function userInterface_jobMonitor_displayApplicationTab()bring the application tab of the monitor uppermost
		
		
	Returns void - 
       */
void userInterface_jobMonitor_displayApplicationTab ( )
   {
     XmlRpcValue _args, _result;
   
     if (myAR->execute("userInterface.jobMonitor.displayApplicationTab", _args, _result))
     {
     
     }
    
   };
   
			
			
/* function userInterface_jobMonitor_displayJobTab()bring the jes tab of the monitor uppermost
		
		
	Returns void - 
       */
void userInterface_jobMonitor_displayJobTab ( )
   {
     XmlRpcValue _args, _result;
   
     if (myAR->execute("userInterface.jobMonitor.displayJobTab", _args, _result))
     {
     
     }
    
   };
   
      /* end class
      userInterface.jobMonitor
      */
   /* begin class astrogrid.jobs
    Deprecated: 
				JES is no longer supported.
			Execute and control workflows on remote job servers.


 For now, an interface to a single JES server  - which is configured in the system properties for the ACR.
 In future, JES servers should be registered, and a default server associated with a user in a community .
 It may also be necessary to be able to browse a selection of job servers, and maybe aggregate a user's jobs from a set of servers.
 <br />
 Each workflow submitted is assigned a globally-unique identifier. This takes the form of a URI, but should be treated as opaque - the structure is
 liable to change  once JES servers are registered.See: 
				<a href="http://www.astrogrid.org/maven/docs/HEAD/jes/userguide-architecture.html">Workflow Userguide</a>
			 
				<a href="http://wiki.astrogrid.org/bin/view/Astrogrid/JesScriptingFAQ">Workflow Scripting FAQ</a> 
 <br/>
			 
				<a href="http://www.astrogrid.org/maven/docs/HEAD/astrogrid-workflow-objects/schema/Workflow.html">Workflow Schema-Documentation</a>
			 
				<a href="http://www.astrogrid.org/maven/docs/HEAD/astrogrid-workflow-objects/schema/ExecutionRecord.html">Execution Record Schema-Document</a>
			 
				<a href="http://www.astrogrid.org/viewcvs/astrogrid/workflow-objects/schema/">XSD Schemas</a>
 <br/>
			 
				<a href="doc-files/example-workflow.xml">Example workflow</a>
			 
				<a href="doc-files/example-workflow-transcript.xml">Example execution transcript</a>
			 
				<a href="doc-files/example-workflow-transcript.html">Html-formatted execution transcript</a>
 <b/>
			 
				org.astrogrid.acr.astrogrid.Applications Information about executing single applications
			 
				org.astrogrid.acr.astrogrid.Myspace Information about distributed file storage
			 
				org.astrogrid.acr.astrogrid.RemoteProcessManager for more general process management - methods
 in this class are convenicne wrapper around operations in <tt>RemoteProcessManager</tt>
 <br/>
			 
				org.astrogrid.acr.ui.JobMonitor
			 
				org.astrogrid.acr.ui.WorkflowBuilder
			 
				org.astrogrid.acr.astrogrid.ExecutionInformation
			  */
	 
	
			
			
/* function astrogrid_jobs_list()list the jobs for the current user.
		
		
	Returns ListOfIvornOrURI - list of identifiers for the user's jobs (both current and completed jobs )
       */
ListOfIvornOrURI astrogrid_jobs_list ( )
   {
     XmlRpcValue _args, _result;
   ListOfIvornOrURI retval;
   
     if (myAR->execute("astrogrid.jobs.list", _args, _result))
     {
     ListOf<IvornOrURI> s = ListOf<IvornOrURI>(_result);

                retval.n = s.size();
                retval.list = copyArray<IvornOrURI, IvornOrURI>(s);

     }
    
     return retval;
    
   };
   
			
			
/* function astrogrid_jobs_listFully()list summaries of the jobs for the current user.
		
		
	Returns ListOfExecutionInformation - a beanful of information on each job
       */
ListOfExecutionInformation astrogrid_jobs_listFully ( )
   {
     XmlRpcValue _args, _result;
   ListOfExecutionInformation retval;
   
     if (myAR->execute("astrogrid.jobs.listFully", _args, _result))
     {
     ListOf<ExecutionInformation_> s = ListOf<ExecutionInformation_>(_result);

                retval.n = s.size();
                retval.list = copyArrayAsStruct<ExecutionInformation_, struct ExecutionInformation>(s);

     }
    
     return retval;
    
   };
   
			
			
/* function astrogrid_jobs_createJob()create a new initialized workflow document, suitable as a starting point for building workflows.
		
		
	Returns XMLString - a workflow document - a <tt>workflow</tt> document in the the <tt>http://www.astrogrid.org/schema/AGWorkflow/v1</tt> namespace
       */
XMLString astrogrid_jobs_createJob ( )
   {
     XmlRpcValue _args, _result;
   XMLString retval;
   
     if (myAR->execute("astrogrid.jobs.createJob", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function astrogrid_jobs_wrapTask(taskDocument)wrap a remote application invocation document, to create a single-step workflow.
		
		taskDocument - - a task document in the <tt> http://www.astrogrid.org/schema/AGWorkflow/v1</tt> namespace(XMLString)
		
	Returns XMLString - a workflow document with a single step that executes the parameter task - a <tt>workflow</tt> document in the the <tt>http://www.astrogrid.org/schema/AGWorkflow/v1</tt> namespace
       */
XMLString astrogrid_jobs_wrapTask ( XMLString taskDocument)
   {
     XmlRpcValue _args, _result;
   XMLString retval;
   _args[0] = taskDocument;
   
     if (myAR->execute("astrogrid.jobs.wrapTask", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function astrogrid_jobs_getJobTranscript(jobURN)retrieve the execution transcript for a job.
		
		jobURN - the identifier of the job to retrieve(IvornOrURI)
		
	Returns XMLString - a workflow transcript  document - A <tt>workflow</tt> document in   the <tt>http://www.astrogrid.org/schema/AGWorkflow/v1</tt> namespace, annotated with execution information from the   <tt>http://www.astrogrid.org/schema/ExecutionRecord/v1</tt> namespace.
       */
XMLString astrogrid_jobs_getJobTranscript ( IvornOrURI jobURN)
   {
     XmlRpcValue _args, _result;
   XMLString retval;
   _args[0] = jobURN;
   
     if (myAR->execute("astrogrid.jobs.getJobTranscript", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function astrogrid_jobs_getJobInformation(jobURN)retrive summary for a job.
		
		jobURN - the identifier of the job to summarize(IvornOrURI)
		
	Returns struct ExecutionInformation - information about this job.
       */
struct ExecutionInformation astrogrid_jobs_getJobInformation ( IvornOrURI jobURN)
   {
     XmlRpcValue _args, _result;
   struct ExecutionInformation retval;
   _args[0] = jobURN;
   
     if (myAR->execute("astrogrid.jobs.getJobInformation", _args, _result))
     {
     ExecutionInformation_* res = new ExecutionInformation_(_result);
        res->asStruct(&retval);
     }
    
     return retval;
    
   };
   
			
			
/* function astrogrid_jobs_cancelJob(jobURN)cancel the exeuciton of a running job.
		
		jobURN - identifier of the job to cancel.(IvornOrURI)
		
	Returns void - 
       */
void astrogrid_jobs_cancelJob ( IvornOrURI jobURN)
   {
     XmlRpcValue _args, _result;
   _args[0] = jobURN;
   
     if (myAR->execute("astrogrid.jobs.cancelJob", _args, _result))
     {
     
     }
    
   };
   
			
			
/* function astrogrid_jobs_deleteJob(jobURN)delete all record of a job from the job server.
		
		jobURN - identifier of the job to delete(IvornOrURI)
		
	Returns void - 
       */
void astrogrid_jobs_deleteJob ( IvornOrURI jobURN)
   {
     XmlRpcValue _args, _result;
   _args[0] = jobURN;
   
     if (myAR->execute("astrogrid.jobs.deleteJob", _args, _result))
     {
     
     }
    
   };
   
			
			
/* function astrogrid_jobs_submitJob(workflow)submit a workflow for execution.
		
		workflow - workflow document to submit(XMLString)
		
	Returns IvornOrURI - a unique identifier for this new job
       */
IvornOrURI astrogrid_jobs_submitJob ( XMLString workflow)
   {
     XmlRpcValue _args, _result;
   IvornOrURI retval;
   _args[0] = workflow;
   
     if (myAR->execute("astrogrid.jobs.submitJob", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function astrogrid_jobs_submitStoredJob(workflowReference)submit a workflow (stored in a file) for execution.
		
		workflowReference - url  refernce to the workflow document to submit (may be file://, http://, ftp:// or ivo:// - a myspace reference.)(IvornOrURI)
		
	Returns IvornOrURI - a unique identifier for this new job.
       */
IvornOrURI astrogrid_jobs_submitStoredJob ( IvornOrURI workflowReference)
   {
     XmlRpcValue _args, _result;
   IvornOrURI retval;
   _args[0] = workflowReference;
   
     if (myAR->execute("astrogrid.jobs.submitStoredJob", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
      /* end class
      astrogrid.jobs
      */
   /* begin class userInterface.lookout
    Deprecated: Control the Lookout UI.
 
 <img src="doc-files/lookout.png"/> */
	 
	
			
			
/* function userInterface_lookout_show()
		
		
	Returns void - 
       */
void userInterface_lookout_show ( )
   {
     XmlRpcValue _args, _result;
   
     if (myAR->execute("userInterface.lookout.show", _args, _result))
     {
     
     }
    
   };
   
			
			
/* function userInterface_lookout_hide()
		
		
	Returns void - 
       */
void userInterface_lookout_hide ( )
   {
     XmlRpcValue _args, _result;
   
     if (myAR->execute("userInterface.lookout.hide", _args, _result))
     {
     
     }
    
   };
   
			
			
/* function userInterface_lookout_refresh()
		
		
	Returns void - 
       */
void userInterface_lookout_refresh ( )
   {
     XmlRpcValue _args, _result;
   
     if (myAR->execute("userInterface.lookout.refresh", _args, _result))
     {
     
     }
    
   };
   
      /* end class
      userInterface.lookout
      */
   /* begin class astrogrid.myspace
    Work with  Myspace - a distributed storage system, AstroGrid's implementation of VOSpace.
 
 All resources in myspace are uniquely identified by a myspace resource identifier - which is an URI of form
  * <tt>ivo://<i>Community-Id</i>/<i>User-Id</i>#<i>File-Path</i></tt>. However, for convenience methods in this interface also accept an
 abridged form of reference - <tt>#<i>File-Path</i></tt> - this is resolved relative to the currently logged-in user. The abridged
 form is more concise, and means hard-coded file references can be avoided if needed.   
 <br/>
 <b>NB</b>: At present this interface doesn't contain suficient functionality to work with myspace in a truly efficient manner. Expect a cleaner, more efficient interface
 to myspace to be added later. However this interface and it's current methods will remain available, and won't be deprecated.See: 
				<a href="http://www.ivoa.net/twiki/bin/view/IVOA/IvoaGridAndWebServices#VO_Store_Proposal">IVOA VOStore</a>
			 
				org.astrogrid.acr.ui.MyspaceBrowser
			 
				org.astrogrid.acr.dialogs.ResourceChooser
			 
				org.astrogrid.acr.astrogrid.NodeInformation
			  */
	 
	
			
			
/* function astrogrid_myspace_getHome()retreive the identifier of the current user's home folder in myspace. 
 
 Each user has a single root folder. this method returns the name of it.
		
		
	Returns IvornOrURI - uri of the home folder - typically has form  <tt>ivo://<i>Community-Id</i>/<i>User-Id</i>#</tt>
       */
IvornOrURI astrogrid_myspace_getHome ( )
   {
     XmlRpcValue _args, _result;
   IvornOrURI retval;
   
     if (myAR->execute("astrogrid.myspace.getHome", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function astrogrid_myspace_exists(ivorn)test whether a myspace resource exists.
		
		ivorn - uri to check (full or abridged form)(IvornOrURI)
		
	Returns BOOL - true if the resource exists
       */
BOOL astrogrid_myspace_exists ( IvornOrURI ivorn)
   {
     XmlRpcValue _args, _result;
   BOOL retval;
   _args[0] = ivorn;
   
     if (myAR->execute("astrogrid.myspace.exists", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function astrogrid_myspace_getNodeInformation(ivorn)access metadata about a myspace resource.
 <b>NB: </b> At the moment, this is a costly operation.
		
		ivorn - resource to investigate(IvornOrURI)
		
	Returns struct NodeInformation - a beanful of information
       */
struct NodeInformation astrogrid_myspace_getNodeInformation ( IvornOrURI ivorn)
   {
     XmlRpcValue _args, _result;
   struct NodeInformation retval;
   _args[0] = ivorn;
   
     if (myAR->execute("astrogrid.myspace.getNodeInformation", _args, _result))
     {
     NodeInformation_* res = new NodeInformation_(_result);
        res->asStruct(&retval);
     }
    
     return retval;
    
   };
   
			
			
/* function astrogrid_myspace_createFile(ivorn)create a new myspace file. 
 
 Any parent folders that are missing will be created too.
		
		ivorn - the resource to create.(IvornOrURI)
		
	Returns void - 
       */
void astrogrid_myspace_createFile ( IvornOrURI ivorn)
   {
     XmlRpcValue _args, _result;
   _args[0] = ivorn;
   
     if (myAR->execute("astrogrid.myspace.createFile", _args, _result))
     {
     
     }
    
   };
   
			
			
/* function astrogrid_myspace_createFolder(ivorn)create a new myspace folder.
 
 Any parent folders that are missing will be created too.
		
		ivorn - the resource to create.(IvornOrURI)
		
	Returns void - 
       */
void astrogrid_myspace_createFolder ( IvornOrURI ivorn)
   {
     XmlRpcValue _args, _result;
   _args[0] = ivorn;
   
     if (myAR->execute("astrogrid.myspace.createFolder", _args, _result))
     {
     
     }
    
   };
   
			
			
/* function astrogrid_myspace_createChildFolder(parentIvorn, name)create a child folder of  the specified resource.
		
		parentIvorn - parent of the new resource (must be a folder)(IvornOrURI)
		name - name of the new folder(JString)
		
	Returns IvornOrURI - the ivorn of the new folder
       */
IvornOrURI astrogrid_myspace_createChildFolder ( IvornOrURI parentIvorn, JString name)
   {
     XmlRpcValue _args, _result;
   IvornOrURI retval;
   _args[0] = parentIvorn;
   _args[1] = name;
   
     if (myAR->execute("astrogrid.myspace.createChildFolder", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function astrogrid_myspace_createChildFile(parentIvorn, name)create a child file of the specified resource.
		
		parentIvorn - parent of the new resource (must be a folder)(IvornOrURI)
		name - name of the new file(JString)
		
	Returns IvornOrURI - the ivorn of the new file
       */
IvornOrURI astrogrid_myspace_createChildFile ( IvornOrURI parentIvorn, JString name)
   {
     XmlRpcValue _args, _result;
   IvornOrURI retval;
   _args[0] = parentIvorn;
   _args[1] = name;
   
     if (myAR->execute("astrogrid.myspace.createChildFile", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function astrogrid_myspace_getParent(ivorn)retrieve the ID  of the parent of a myspace resource
		
		ivorn - uri of the resource to find parent for(IvornOrURI)
		
	Returns IvornOrURI - uri of the parent
       */
IvornOrURI astrogrid_myspace_getParent ( IvornOrURI ivorn)
   {
     XmlRpcValue _args, _result;
   IvornOrURI retval;
   _args[0] = ivorn;
   
     if (myAR->execute("astrogrid.myspace.getParent", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function astrogrid_myspace_list(ivorn)list the names of the children (files and folders) of a myspace folder
		
		ivorn - uri of the folder to inspect(IvornOrURI)
		
	Returns ListOfJString - an array of the names of the contents.
       */
ListOfJString astrogrid_myspace_list ( IvornOrURI ivorn)
   {
     XmlRpcValue _args, _result;
   ListOfJString retval;
   _args[0] = ivorn;
   
     if (myAR->execute("astrogrid.myspace.list", _args, _result))
     {
     ListOf<JString> s = ListOf<JString>(_result);

                retval.n = s.size();
                retval.list = copyArray<JString, JString>(s);

     }
    
     return retval;
    
   };
   
			
			
/* function astrogrid_myspace_listIvorns(ivorn)list the identifiers of the children ( files and folders)  of a myspace folder
		
		ivorn - uri of the folder to inspect(IvornOrURI)
		
	Returns ListOfIvornOrURI - an array of the ivorns of the contents.
       */
ListOfIvornOrURI astrogrid_myspace_listIvorns ( IvornOrURI ivorn)
   {
     XmlRpcValue _args, _result;
   ListOfIvornOrURI retval;
   _args[0] = ivorn;
   
     if (myAR->execute("astrogrid.myspace.listIvorns", _args, _result))
     {
     ListOf<IvornOrURI> s = ListOf<IvornOrURI>(_result);

                retval.n = s.size();
                retval.list = copyArray<IvornOrURI, IvornOrURI>(s);

     }
    
     return retval;
    
   };
   
			
			
/* function astrogrid_myspace_listNodeInformation(ivorn)list the node information objects for the children ( files and folders)  of a myspace folder   
 
 <b>NB: </b> Expensive operation at present.
		
		ivorn - uri of the folder to inspect(IvornOrURI)
		
	Returns ListOfNodeInformation - an array of the node information objects.
       */
ListOfNodeInformation astrogrid_myspace_listNodeInformation ( IvornOrURI ivorn)
   {
     XmlRpcValue _args, _result;
   ListOfNodeInformation retval;
   _args[0] = ivorn;
   
     if (myAR->execute("astrogrid.myspace.listNodeInformation", _args, _result))
     {
     ListOf<NodeInformation_> s = ListOf<NodeInformation_>(_result);

                retval.n = s.size();
                retval.list = copyArrayAsStruct<NodeInformation_, struct NodeInformation>(s);

     }
    
     return retval;
    
   };
   
			
			
/* function astrogrid_myspace_refresh(ivorn)refresh the metadata held about  a myspace resource with the server.
 <br/>
 For performance, metadata about myspace resources is used in a LRU cache. This method forces the ACR to re-query the myspace server
 about this resource.
		
		ivorn - resource to refresh(IvornOrURI)
		
	Returns void - 
       */
void astrogrid_myspace_refresh ( IvornOrURI ivorn)
   {
     XmlRpcValue _args, _result;
   _args[0] = ivorn;
   
     if (myAR->execute("astrogrid.myspace.refresh", _args, _result))
     {
     
     }
    
   };
   
			
			
/* function astrogrid_myspace_delete(ivorn)delete a myspace resource
		
		ivorn - uri of the resource to delete(IvornOrURI)
		
	Returns void - 
       */
void astrogrid_myspace_delete ( IvornOrURI ivorn)
   {
     XmlRpcValue _args, _result;
   _args[0] = ivorn;
   
     if (myAR->execute("astrogrid.myspace.delete", _args, _result))
     {
     
     }
    
   };
   
			
			
/* function astrogrid_myspace_rename(srcIvorn, newName)rename a myspace resource
		
		srcIvorn - uri of the resource to renam(IvornOrURI)
		newName - new name for this resource(JString)
		
	Returns IvornOrURI - uri pointing to the renamed resource (original uri may now be invalid)
       */
IvornOrURI astrogrid_myspace_rename ( IvornOrURI srcIvorn, JString newName)
   {
     XmlRpcValue _args, _result;
   IvornOrURI retval;
   _args[0] = srcIvorn;
   _args[1] = newName;
   
     if (myAR->execute("astrogrid.myspace.rename", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function astrogrid_myspace_move(srcIvorn, newParentIvorn, newName)move a myspace resource
		
		srcIvorn - ivorn of the resource to move(IvornOrURI)
		newParentIvorn - ivorn of the new parent(IvornOrURI)
		newName - new name for this resource.(JString)
		
	Returns IvornOrURI - uri pointing to the moved resouce (original uri will now be invalid)
       */
IvornOrURI astrogrid_myspace_move ( IvornOrURI srcIvorn, IvornOrURI newParentIvorn, JString newName)
   {
     XmlRpcValue _args, _result;
   IvornOrURI retval;
   _args[0] = srcIvorn;
   _args[1] = newParentIvorn;
   _args[2] = newName;
   
     if (myAR->execute("astrogrid.myspace.move", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function astrogrid_myspace_changeStore(srcIvorn, storeIvorn)relocate a myspace resource to a different store.
 
 The relocated file remains in the same position in the user's myspace filetree. However, this method moves the data associated with the 
 file from one filestore to another.
		
		srcIvorn - uri of the resource to relocate(IvornOrURI)
		storeIvorn - uri of the store server to relocat to.(IvornOrURI)
		
	Returns void - 
       */
void astrogrid_myspace_changeStore ( IvornOrURI srcIvorn, IvornOrURI storeIvorn)
   {
     XmlRpcValue _args, _result;
   _args[0] = srcIvorn;
   _args[1] = storeIvorn;
   
     if (myAR->execute("astrogrid.myspace.changeStore", _args, _result))
     {
     
     }
    
   };
   
			
			
/* function astrogrid_myspace_copy(srcIvorn, newParentIvorn, newName)make a copy of a resource
		
		srcIvorn - uri of the resource to copy(IvornOrURI)
		newParentIvorn - uri of the folder to copy to(IvornOrURI)
		newName - name to copy to(JString)
		
	Returns IvornOrURI - uri pointing to the resource copy
       */
IvornOrURI astrogrid_myspace_copy ( IvornOrURI srcIvorn, IvornOrURI newParentIvorn, JString newName)
   {
     XmlRpcValue _args, _result;
   IvornOrURI retval;
   _args[0] = srcIvorn;
   _args[1] = newParentIvorn;
   _args[2] = newName;
   
     if (myAR->execute("astrogrid.myspace.copy", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function astrogrid_myspace_read(ivorn)read the content of a myspace resource directly.
 
 <b>NB:</b> not a good idea for large files. in this case use {@link #copyContentToURL(URI, URL) } or {@link #getReadContentURL(URI) }
		
		ivorn - resource to read(IvornOrURI)
		
	Returns JString - content of this resource (as a string)
       */
JString astrogrid_myspace_read ( IvornOrURI ivorn)
   {
     XmlRpcValue _args, _result;
   JString retval;
   _args[0] = ivorn;
   
     if (myAR->execute("astrogrid.myspace.read", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function astrogrid_myspace_write(ivorn, content)Write data to a myspace resource.
 
 <b>NB: </b> not a good idea for large files. In this case use {@link #copyURLToContent(URL, URI) }
		
		ivorn - resource to write to(IvornOrURI)
		content - the data to write(JString)
		
	Returns void - 
       */
void astrogrid_myspace_write ( IvornOrURI ivorn, JString content)
   {
     XmlRpcValue _args, _result;
   _args[0] = ivorn;
   _args[1] = content;
   
     if (myAR->execute("astrogrid.myspace.write", _args, _result))
     {
     
     }
    
   };
   
			
			
/* function astrogrid_myspace_readBinary(ivorn)read the binary content of a myspace resource directly.
 
 <b>NB: </b> not a good idea for large files. in this case use {@link #copyContentToURL(URI, URL) } or {@link #getReadContentURL(URI) }
		
		ivorn - resource to read(IvornOrURI)
		
	Returns ListOfchar - byte array of the contents of this resource
       */
ListOfchar astrogrid_myspace_readBinary ( IvornOrURI ivorn)
   {
     XmlRpcValue _args, _result;
   ListOfchar retval;
   _args[0] = ivorn;
   
     if (myAR->execute("astrogrid.myspace.readBinary", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function astrogrid_myspace_writeBinary(ivorn, content)Write binary data to a myspace resource.

<b>NB: </b> not a good idea for large files. In this case use {@link #copyURLToContent(URL, URI) }
		
		ivorn - resource to write to(IvornOrURI)
		content - the data to write(ListOfchar)
		
	Returns void - 
       */
void astrogrid_myspace_writeBinary ( IvornOrURI ivorn, ListOfchar content)
   {
     XmlRpcValue _args, _result;
   _args[0] = ivorn;
   _args[1] = content;
   
     if (myAR->execute("astrogrid.myspace.writeBinary", _args, _result))
     {
     
     }
    
   };
   
			
			
/* function astrogrid_myspace_getReadContentURL(ivorn)compute a URL which can then be read to access the contents (data) of a myspace resource.
		
		ivorn - resource to read(IvornOrURI)
		
	Returns URLString - a url from which the contents of the resource can be read
       */
URLString astrogrid_myspace_getReadContentURL ( IvornOrURI ivorn)
   {
     XmlRpcValue _args, _result;
   URLString retval;
   _args[0] = ivorn;
   
     if (myAR->execute("astrogrid.myspace.getReadContentURL", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function astrogrid_myspace_getWriteContentURL(ivorn)compute  a URL which can then be written to set the contents (i.e. data) of a myspace resource.
		
		ivorn - resource to write to(IvornOrURI)
		
	Returns URLString - a url to  which the contents of the resource can be written
       */
URLString astrogrid_myspace_getWriteContentURL ( IvornOrURI ivorn)
   {
     XmlRpcValue _args, _result;
   URLString retval;
   _args[0] = ivorn;
   
     if (myAR->execute("astrogrid.myspace.getWriteContentURL", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function astrogrid_myspace_transferCompleted(ivorn)Notify the filemanager  server that the data for a filestore node has been changed. 
 
 This method must be called after storing data in a myspace file via the URL returned by {@link #getWriteContentURL}.
 There's no need to call this method when storing data using any other method
		
		ivorn - the myspace resource just written to(IvornOrURI)
		
	Returns void - 
       */
void astrogrid_myspace_transferCompleted ( IvornOrURI ivorn)
   {
     XmlRpcValue _args, _result;
   _args[0] = ivorn;
   
     if (myAR->execute("astrogrid.myspace.transferCompleted", _args, _result))
     {
     
     }
    
   };
   
			
			
/* function astrogrid_myspace_copyContentToURL(ivorn, destination)Copy the contents (data) of a resource out of myspace into a URL location.
		
		ivorn - the myspace resource to write out(IvornOrURI)
		destination - a writable URL - file:/, http:/ or ftp:/ protocol(URLString)
		
	Returns void - 
       */
void astrogrid_myspace_copyContentToURL ( IvornOrURI ivorn, URLString destination)
   {
     XmlRpcValue _args, _result;
   _args[0] = ivorn;
   _args[1] = destination;
   
     if (myAR->execute("astrogrid.myspace.copyContentToURL", _args, _result))
     {
     
     }
    
   };
   
			
			
/* function astrogrid_myspace_copyURLToContent(src, ivorn)Copy the contents (data) of a URL location into a myspace resource
		
		src - url to read data from - file:/, http:/ or ftp:/ protocol.(URLString)
		ivorn - the myspace resource to store the data in.(IvornOrURI)
		
	Returns void - 
       */
void astrogrid_myspace_copyURLToContent ( URLString src, IvornOrURI ivorn)
   {
     XmlRpcValue _args, _result;
   _args[0] = src;
   _args[1] = ivorn;
   
     if (myAR->execute("astrogrid.myspace.copyURLToContent", _args, _result))
     {
     
     }
    
   };
   
			
			
/* function astrogrid_myspace_listStores()List the available filestores
		
		
	Returns ListOfService_Base - an array of service descriptions.
       */
ListOfService_Base astrogrid_myspace_listStores ( )
   {
     XmlRpcValue _args, _result;
   ListOfService_Base retval;
   
     if (myAR->execute("astrogrid.myspace.listStores", _args, _result))
     {
     ListOfBase<Service_> s = ListOfBase<Service_>(_result);

                retval.n = s.size();
                retval.list = copyArrayAsBaseStruct<Service_, struct Service_Base>(s);

     }
    
     return retval;
    
   };
   
      /* end class
      astrogrid.myspace
      */
   /* begin class userInterface.myspaceBrowser
    Deprecated: 
				prefer filemanager
			Control the  Myspace Browser UI.
 
 <img src="doc-files/filemanager.png"/>See: 
				org.astrogrid.acr.astrogrid.Myspace
			  */
	 
	
			
			
/* function userInterface_myspaceBrowser_show()show the explorer gui
		
		
	Returns void - 
       */
void userInterface_myspaceBrowser_show ( )
   {
     XmlRpcValue _args, _result;
   
     if (myAR->execute("userInterface.myspaceBrowser.show", _args, _result))
     {
     
     }
    
   };
   
			
			
/* function userInterface_myspaceBrowser_hide()hide the explorer gui
		
		
	Returns void - 
       */
void userInterface_myspaceBrowser_hide ( )
   {
     XmlRpcValue _args, _result;
   
     if (myAR->execute("userInterface.myspaceBrowser.hide", _args, _result))
     {
     
     }
    
   };
   
      /* end class
      userInterface.myspaceBrowser
      */
   /* begin class astrogrid.publish
    Publish to a registry. */
	 
	
			
			
/* function astrogrid_publish_register(registry, entry)Publish a resource in a registry.
		
		registry - the IVOA identifier of the registry.(IvornOrURI)
		entry - the resource to be published.(XMLString)
		
	Returns void - 
       */
void astrogrid_publish_register ( IvornOrURI registry, XMLString entry)
   {
     XmlRpcValue _args, _result;
   _args[0] = registry;
   _args[1] = entry;
   
     if (myAR->execute("astrogrid.publish.register", _args, _result))
     {
     
     }
    
   };
   
      /* end class
      astrogrid.publish
      */
   /* begin class userInterface.queryBuilder
     */
	 
	
			
			
/* function userInterface_queryBuilder_show()
		
		
	Returns void - 
       */
void userInterface_queryBuilder_show ( )
   {
     XmlRpcValue _args, _result;
   
     if (myAR->execute("userInterface.queryBuilder.show", _args, _result))
     {
     
     }
    
   };
   
      /* end class
      userInterface.queryBuilder
      */
   /* begin class ivoa.registry
    Access  the system-configured  registry service.
 
 ACR uses an IVOA-compliant registry to retreive details of available resources
  - servers, applications, catalogues, etc.
  
  The endpoint of this registry service can be inspected by calling {@link #getSystemRegistryEndpoint()}.
  In cases where this service is unavailable, registry queries will automatically fall-back to the
  backup registry service, whose endpoint is defined by {@link #getFallbackSystemRegistryEndpoint()}
 
 The query functions in this interface are the equivalent to their counterparts in the 
 {@link ExternalRegistry} interface, but against the System and Fallback registries.See: 
				<a href="http://www.ivoa.net/Documents/latest/IDs.html">IVOA Identifiers</a>
			 
				<a href="http://www.ivoa.net/twiki/bin/view/IVOA/ResourceMetadata">Resource Metadata</a>
			 
				<a href="http://www.ivoa.net/Documents/latest/RM.html">IVOA Resource Metadata for the VO</a>
			 
				<a href="http://www.ivoa.net/Documents/latest/ADQL.html">ADQL Query Language Specification</a>
			 
				<a href="http://www.ivoa.net/twiki/bin/view/IVOA/IvoaResReg">IVOA Registry Working Group</a>
			 
				<a href="http://www.w3schools.com/xquery/default.asp">XQuery Language Tutorial</a>
			 
				org.astrogrid.acr.ui.RegistryBrowser
			 
				org.astrogrid.acr.ivoa.ExternalRegistry - to query other IVOA registries, and the Registry of Registries.
			  */
	 
	
			
			
/* function ivoa_registry_adqlxSearch(adqlx)Perform an ADQL/x registry search, return a list of matching resources
		
		adqlx - (XMLString)
		
	Returns ListOfResource_Base - 
       */
ListOfResource_Base ivoa_registry_adqlxSearch ( XMLString adqlx)
   {
     XmlRpcValue _args, _result;
   ListOfResource_Base retval;
   _args[0] = adqlx;
   
     if (myAR->execute("ivoa.registry.adqlxSearch", _args, _result))
     {
     ListOfBase<Resource_> s = ListOfBase<Resource_>(_result);

                retval.n = s.size();
                retval.list = copyArrayAsBaseStruct<Resource_, struct Resource_Base>(s);

     }
    
     return retval;
    
   };
   
			
			
/* function ivoa_registry_adqlsSearch(adqls)Perform an ADQL/s registry search, return a list of matching resources
		
		adqls - (JString)
		
	Returns ListOfResource_Base - 
       */
ListOfResource_Base ivoa_registry_adqlsSearch ( JString adqls)
   {
     XmlRpcValue _args, _result;
   ListOfResource_Base retval;
   _args[0] = adqls;
   
     if (myAR->execute("ivoa.registry.adqlsSearch", _args, _result))
     {
     ListOfBase<Resource_> s = ListOfBase<Resource_>(_result);

                retval.n = s.size();
                retval.list = copyArrayAsBaseStruct<Resource_, struct Resource_Base>(s);

     }
    
     return retval;
    
   };
   
			
			
/* function ivoa_registry_keywordSearch(keywords, orValues)Perform a keyword registry search, return a list of matching resources
		
		keywords - (JString)
		orValues - (BOOL)
		
	Returns ListOfResource_Base - 
       */
ListOfResource_Base ivoa_registry_keywordSearch ( JString keywords, BOOL orValues)
   {
     XmlRpcValue _args, _result;
   ListOfResource_Base retval;
   _args[0] = keywords;
   _args[1] = orValues;
   
     if (myAR->execute("ivoa.registry.keywordSearch", _args, _result))
     {
     ListOfBase<Resource_> s = ListOfBase<Resource_>(_result);

                retval.n = s.size();
                retval.list = copyArrayAsBaseStruct<Resource_, struct Resource_Base>(s);

     }
    
     return retval;
    
   };
   
			
			
/* function ivoa_registry_getResource(id)Retrieve a resource by identifier
		
		id - (IvornOrURI)
		
	Returns struct Resource_Base - 
       */
struct Resource_Base ivoa_registry_getResource ( IvornOrURI id)
   {
     XmlRpcValue _args, _result;
   struct Resource_Base retval;
   _args[0] = id;
   
     if (myAR->execute("ivoa.registry.getResource", _args, _result))
     {
     Resource_* res = Resource_::create(_result);
        res->asStruct(&retval);
     }
    
     return retval;
    
   };
   
			
			
/* function ivoa_registry_xquerySearch(xquery)Perform an xquery registry search, return a list of matching resources
		
		xquery - (JString)
		
	Returns ListOfResource_Base - 
       */
ListOfResource_Base ivoa_registry_xquerySearch ( JString xquery)
   {
     XmlRpcValue _args, _result;
   ListOfResource_Base retval;
   _args[0] = xquery;
   
     if (myAR->execute("ivoa.registry.xquerySearch", _args, _result))
     {
     ListOfBase<Resource_> s = ListOfBase<Resource_>(_result);

                retval.n = s.size();
                retval.list = copyArrayAsBaseStruct<Resource_, struct Resource_Base>(s);

     }
    
     return retval;
    
   };
   
			
			
/* function ivoa_registry_xquerySearchXML(xquery)Perform an xquery registry search, return a document
		
		xquery - (JString)
		
	Returns XMLString - 
       */
XMLString ivoa_registry_xquerySearchXML ( JString xquery)
   {
     XmlRpcValue _args, _result;
   XMLString retval;
   _args[0] = xquery;
   
     if (myAR->execute("ivoa.registry.xquerySearchXML", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function ivoa_registry_getIdentity()Access the registry entry describing the system registry itself
		
		
	Returns struct RegistryService - 
       */
struct RegistryService ivoa_registry_getIdentity ( )
   {
     XmlRpcValue _args, _result;
   struct RegistryService retval;
   
     if (myAR->execute("ivoa.registry.getIdentity", _args, _result))
     {
     RegistryService_* res = new RegistryService_(_result);
        res->asStruct(&retval);
     }
    
     return retval;
    
   };
   
			
			
/* function ivoa_registry_getSystemRegistryEndpoint()gives the endpoint of the system registry
		
		
	Returns IvornOrURI - 
       */
IvornOrURI ivoa_registry_getSystemRegistryEndpoint ( )
   {
     XmlRpcValue _args, _result;
   IvornOrURI retval;
   
     if (myAR->execute("ivoa.registry.getSystemRegistryEndpoint", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function ivoa_registry_getFallbackSystemRegistryEndpoint()gives the endpoint of the fallback system registry
		
		
	Returns IvornOrURI - 
       */
IvornOrURI ivoa_registry_getFallbackSystemRegistryEndpoint ( )
   {
     XmlRpcValue _args, _result;
   IvornOrURI retval;
   
     if (myAR->execute("ivoa.registry.getFallbackSystemRegistryEndpoint", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
      /* end class
      ivoa.registry
      */
   /* begin class ivoa.registryAdqlBuilder
    Builds ADQL queries for the registry */
	 
	
			
			
/* function ivoa_registryAdqlBuilder_allRecords()query that returns all active records in the registry
		
		
	Returns JString - 
       */
JString ivoa_registryAdqlBuilder_allRecords ( )
   {
     XmlRpcValue _args, _result;
   JString retval;
   
     if (myAR->execute("ivoa.registryAdqlBuilder.allRecords", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function ivoa_registryAdqlBuilder_fullTextSearch(recordSet, searchTerm)build a full text search
		
		recordSet - (JString)
		searchTerm - (JString)
		
	Returns JString - 
       */
JString ivoa_registryAdqlBuilder_fullTextSearch ( JString recordSet, JString searchTerm)
   {
     XmlRpcValue _args, _result;
   JString retval;
   _args[0] = recordSet;
   _args[1] = searchTerm;
   
     if (myAR->execute("ivoa.registryAdqlBuilder.fullTextSearch", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function ivoa_registryAdqlBuilder_summaryTextSearch(recordSet, searchTerm)build a summary text search 
 
 searches on identifier, shortName, title and content/description
		
		recordSet - (JString)
		searchTerm - (JString)
		
	Returns JString - 
       */
JString ivoa_registryAdqlBuilder_summaryTextSearch ( JString recordSet, JString searchTerm)
   {
     XmlRpcValue _args, _result;
   JString retval;
   _args[0] = recordSet;
   _args[1] = searchTerm;
   
     if (myAR->execute("ivoa.registryAdqlBuilder.summaryTextSearch", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function ivoa_registryAdqlBuilder_identifierSearch(recordSet, searchTerm)build an identifier search
		
		recordSet - (JString)
		searchTerm - (JString)
		
	Returns JString - 
       */
JString ivoa_registryAdqlBuilder_identifierSearch ( JString recordSet, JString searchTerm)
   {
     XmlRpcValue _args, _result;
   JString retval;
   _args[0] = recordSet;
   _args[1] = searchTerm;
   
     if (myAR->execute("ivoa.registryAdqlBuilder.identifierSearch", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function ivoa_registryAdqlBuilder_shortNameSearch(recordSet, searchTerm)build a short-name search
		
		recordSet - (JString)
		searchTerm - (JString)
		
	Returns JString - 
       */
JString ivoa_registryAdqlBuilder_shortNameSearch ( JString recordSet, JString searchTerm)
   {
     XmlRpcValue _args, _result;
   JString retval;
   _args[0] = recordSet;
   _args[1] = searchTerm;
   
     if (myAR->execute("ivoa.registryAdqlBuilder.shortNameSearch", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function ivoa_registryAdqlBuilder_titleSearch(recordSet, searchTerm)build a search on title
		
		recordSet - (JString)
		searchTerm - (JString)
		
	Returns JString - 
       */
JString ivoa_registryAdqlBuilder_titleSearch ( JString recordSet, JString searchTerm)
   {
     XmlRpcValue _args, _result;
   JString retval;
   _args[0] = recordSet;
   _args[1] = searchTerm;
   
     if (myAR->execute("ivoa.registryAdqlBuilder.titleSearch", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function ivoa_registryAdqlBuilder_descriptionSearch(recordSet, searchTerm)build a search on description
		
		recordSet - (JString)
		searchTerm - (JString)
		
	Returns JString - 
       */
JString ivoa_registryAdqlBuilder_descriptionSearch ( JString recordSet, JString searchTerm)
   {
     XmlRpcValue _args, _result;
   JString retval;
   _args[0] = recordSet;
   _args[1] = searchTerm;
   
     if (myAR->execute("ivoa.registryAdqlBuilder.descriptionSearch", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
      /* end class
      ivoa.registryAdqlBuilder
      */
   /* begin class userInterface.registryBrowser
    Control the registry browser UI.
 
 <img src="doc-files/registry.png"/>See: 
				org.astrogrid.acr.astrogrid.Registry
			  */
	 
	
			
			
/* function userInterface_registryBrowser_show()show a new instance of registry browser ui
		
		
	Returns void - 
       */
void userInterface_registryBrowser_show ( )
   {
     XmlRpcValue _args, _result;
   
     if (myAR->execute("userInterface.registryBrowser.show", _args, _result))
     {
     
     }
    
   };
   
			
			
/* function userInterface_registryBrowser_hide()hide the registry browser ui
		
		
	Returns void - 
       */
void userInterface_registryBrowser_hide ( )
   {
     XmlRpcValue _args, _result;
   
     if (myAR->execute("userInterface.registryBrowser.hide", _args, _result))
     {
     
     }
    
   };
   
			
			
/* function userInterface_registryBrowser_search(s)show an new instance of the registry browser, and perform the requiested search (keywords)
		
		s - (JString)
		
	Returns void - 
       */
void userInterface_registryBrowser_search ( JString s)
   {
     XmlRpcValue _args, _result;
   _args[0] = s;
   
     if (myAR->execute("userInterface.registryBrowser.search", _args, _result))
     {
     
     }
    
   };
   
			
			
/* function userInterface_registryBrowser_open(uri)display a particular record in a new instacne of the browser
		
		uri - (IvornOrURI)
		
	Returns void - 
       */
void userInterface_registryBrowser_open ( IvornOrURI uri)
   {
     XmlRpcValue _args, _result;
   _args[0] = uri;
   
     if (myAR->execute("userInterface.registryBrowser.open", _args, _result))
     {
     
     }
    
   };
   
      /* end class
      userInterface.registryBrowser
      */
   /* begin class dialogs.registryGoogle
    prompt the user to select a registry resource by displaying  a more advanced registry chooser dialogue. */
	 
	
			
			
/* function dialogs_registryGoogle_selectResources(prompt, multiple)display the resource chooser dialogue.
		
		prompt - message to prompt user for input.(JString)
		multiple - if true, allow multiple selections.(BOOL)
		
	Returns ListOfResource_Base - 0 or more selected resources. never null.
       */
ListOfResource_Base dialogs_registryGoogle_selectResources ( JString prompt, BOOL multiple)
   {
     XmlRpcValue _args, _result;
   ListOfResource_Base retval;
   _args[0] = prompt;
   _args[1] = multiple;
   
     if (myAR->execute("dialogs.registryGoogle.selectResources", _args, _result))
     {
     ListOfBase<Resource_> s = ListOfBase<Resource_>(_result);

                retval.n = s.size();
                retval.list = copyArrayAsBaseStruct<Resource_, struct Resource_Base>(s);

     }
    
     return retval;
    
   };
   
			
			
/* function dialogs_registryGoogle_selectResourcesAdqlFilter(prompt, multiple, adqlFilter)display the resource chooser dialogue, enabling only resources which match a filter.
		
		prompt - message to prompt user for input.(JString)
		multiple - if true, allow multiple selections.(BOOL)
		adqlFilter - adql-like 'where' clause.(JString)
		
	Returns ListOfResource_Base - 0 or more selected resources. never null.
       */
ListOfResource_Base dialogs_registryGoogle_selectResourcesAdqlFilter ( JString prompt, BOOL multiple, JString adqlFilter)
   {
     XmlRpcValue _args, _result;
   ListOfResource_Base retval;
   _args[0] = prompt;
   _args[1] = multiple;
   _args[2] = adqlFilter;
   
     if (myAR->execute("dialogs.registryGoogle.selectResourcesAdqlFilter", _args, _result))
     {
     ListOfBase<Resource_> s = ListOfBase<Resource_>(_result);

                retval.n = s.size();
                retval.list = copyArrayAsBaseStruct<Resource_, struct Resource_Base>(s);

     }
    
     return retval;
    
   };
   
			
			
/* function dialogs_registryGoogle_selectResourcesXQueryFilter(prompt, multiple, xqueryFilter)display the resource chooser dialogue, enabling only resources which match a filter
		
		prompt - message to prompt user for input.(JString)
		multiple - if true, allow multiple selections.(BOOL)
		xqueryFilter - xpath-like condition(JString)
		
	Returns ListOfResource_Base - 0 or more selected resources. never null.
       */
ListOfResource_Base dialogs_registryGoogle_selectResourcesXQueryFilter ( JString prompt, BOOL multiple, JString xqueryFilter)
   {
     XmlRpcValue _args, _result;
   ListOfResource_Base retval;
   _args[0] = prompt;
   _args[1] = multiple;
   _args[2] = xqueryFilter;
   
     if (myAR->execute("dialogs.registryGoogle.selectResourcesXQueryFilter", _args, _result))
     {
     ListOfBase<Resource_> s = ListOfBase<Resource_>(_result);

                retval.n = s.size();
                retval.list = copyArrayAsBaseStruct<Resource_, struct Resource_Base>(s);

     }
    
     return retval;
    
   };
   
      /* end class
      dialogs.registryGoogle
      */
   /* begin class ivoa.registryXQueryBuilder
    Builds XQueries for the registry */
	 
	
			
			
/* function ivoa_registryXQueryBuilder_allRecords()query that returns all active records in the registry
		
		
	Returns JString - 
       */
JString ivoa_registryXQueryBuilder_allRecords ( )
   {
     XmlRpcValue _args, _result;
   JString retval;
   
     if (myAR->execute("ivoa.registryXQueryBuilder.allRecords", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function ivoa_registryXQueryBuilder_fullTextSearch(recordSet, searchTerm)build a full text search
		
		recordSet - (JString)
		searchTerm - (JString)
		
	Returns JString - 
       */
JString ivoa_registryXQueryBuilder_fullTextSearch ( JString recordSet, JString searchTerm)
   {
     XmlRpcValue _args, _result;
   JString retval;
   _args[0] = recordSet;
   _args[1] = searchTerm;
   
     if (myAR->execute("ivoa.registryXQueryBuilder.fullTextSearch", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function ivoa_registryXQueryBuilder_summaryTextSearch(recordSet, searchTerm)build a summary text search 
 
 searches on identifier, shortName, title and content/description
		
		recordSet - (JString)
		searchTerm - (JString)
		
	Returns JString - 
       */
JString ivoa_registryXQueryBuilder_summaryTextSearch ( JString recordSet, JString searchTerm)
   {
     XmlRpcValue _args, _result;
   JString retval;
   _args[0] = recordSet;
   _args[1] = searchTerm;
   
     if (myAR->execute("ivoa.registryXQueryBuilder.summaryTextSearch", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function ivoa_registryXQueryBuilder_identifierSearch(recordSet, searchTerm)build an identifier search
		
		recordSet - (JString)
		searchTerm - (JString)
		
	Returns JString - 
       */
JString ivoa_registryXQueryBuilder_identifierSearch ( JString recordSet, JString searchTerm)
   {
     XmlRpcValue _args, _result;
   JString retval;
   _args[0] = recordSet;
   _args[1] = searchTerm;
   
     if (myAR->execute("ivoa.registryXQueryBuilder.identifierSearch", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function ivoa_registryXQueryBuilder_shortNameSearch(recordSet, searchTerm)build a short-name search
		
		recordSet - (JString)
		searchTerm - (JString)
		
	Returns JString - 
       */
JString ivoa_registryXQueryBuilder_shortNameSearch ( JString recordSet, JString searchTerm)
   {
     XmlRpcValue _args, _result;
   JString retval;
   _args[0] = recordSet;
   _args[1] = searchTerm;
   
     if (myAR->execute("ivoa.registryXQueryBuilder.shortNameSearch", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function ivoa_registryXQueryBuilder_titleSearch(recordSet, searchTerm)build a search on title
		
		recordSet - (JString)
		searchTerm - (JString)
		
	Returns JString - 
       */
JString ivoa_registryXQueryBuilder_titleSearch ( JString recordSet, JString searchTerm)
   {
     XmlRpcValue _args, _result;
   JString retval;
   _args[0] = recordSet;
   _args[1] = searchTerm;
   
     if (myAR->execute("ivoa.registryXQueryBuilder.titleSearch", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function ivoa_registryXQueryBuilder_descriptionSearch(recordSet, searchTerm)build a search on description
		
		recordSet - (JString)
		searchTerm - (JString)
		
	Returns JString - 
       */
JString ivoa_registryXQueryBuilder_descriptionSearch ( JString recordSet, JString searchTerm)
   {
     XmlRpcValue _args, _result;
   JString retval;
   _args[0] = recordSet;
   _args[1] = searchTerm;
   
     if (myAR->execute("ivoa.registryXQueryBuilder.descriptionSearch", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
      /* end class
      ivoa.registryXQueryBuilder
      */
   /* begin class astrogrid.processManager
    a general manager for the execution , monitoring, and control of all remote processes.

 RemoteProcessManager unifies the  functionality in {@link org.astrogrid.acr.astrogrid.Jobs} and {@link org.astrogrid.acr.astrogrid.Applications}
 and provides additional features - notably ability to register callbacks for progress notifications. It is still valid to use the <tt>Jobs</tt> or <tt>Applications</tt>, 
 however, this interface also knows how to invoke other kinds of service, and provides a uniform interface to cea, cone, siap, ssap services. */
	 
	
			
			
/* function astrogrid_processManager_list()list current remote processes  belonging to the current user
		
		
	Returns ListOfIvornOrURI - list of identifiers for the user's remote processes that are currently being managed by AR
       */
ListOfIvornOrURI astrogrid_processManager_list ( )
   {
     XmlRpcValue _args, _result;
   ListOfIvornOrURI retval;
   
     if (myAR->execute("astrogrid.processManager.list", _args, _result))
     {
     ListOf<IvornOrURI> s = ListOf<IvornOrURI>(_result);

                retval.n = s.size();
                retval.list = copyArray<IvornOrURI, IvornOrURI>(s);

     }
    
     return retval;
    
   };
   
			
			
/* function astrogrid_processManager_submit(document)Submit a document for execution.
 
 No particular  server is specified - the system will choose a suitable server.
		
		document - the document to execute - a program / script / workflow / tool document(XMLString)
		
	Returns IvornOrURI - a new unique execution id
       */
IvornOrURI astrogrid_processManager_submit ( XMLString document)
   {
     XmlRpcValue _args, _result;
   IvornOrURI retval;
   _args[0] = document;
   
     if (myAR->execute("astrogrid.processManager.submit", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function astrogrid_processManager_submitTo(document, server)Submit a document for execution  on a named server
		
		document - the document to execute - a workflow, cea task, etc(XMLString)
		server - server to execute on(IvornOrURI)
		
	Returns IvornOrURI - a new unique execution id
       */
IvornOrURI astrogrid_processManager_submitTo ( XMLString document, IvornOrURI server)
   {
     XmlRpcValue _args, _result;
   IvornOrURI retval;
   _args[0] = document;
   _args[1] = server;
   
     if (myAR->execute("astrogrid.processManager.submitTo", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function astrogrid_processManager_submitStored(documentLocation)variant of {@link #submit} where document is stored somewhere and referenced by URI
		
		documentLocation - pointer to document - may be file:/, http://, ftp:// or ivo:// (myspace) protocols(IvornOrURI)
		
	Returns IvornOrURI - a new unique execution id
       */
IvornOrURI astrogrid_processManager_submitStored ( IvornOrURI documentLocation)
   {
     XmlRpcValue _args, _result;
   IvornOrURI retval;
   _args[0] = documentLocation;
   
     if (myAR->execute("astrogrid.processManager.submitStored", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function astrogrid_processManager_submitStoredTo(documentLocation, server)variant of {@link #submitTo} where document is referenced by URL 
      * @param documentLocation pointer to tool document - may be file:/, http://, ftp:// or ivo:// (myspace) protocols
		
		documentLocation - (IvornOrURI)
		server - to execute on(IvornOrURI)
		
	Returns IvornOrURI - a new unique execution id
       */
IvornOrURI astrogrid_processManager_submitStoredTo ( IvornOrURI documentLocation, IvornOrURI server)
   {
     XmlRpcValue _args, _result;
   IvornOrURI retval;
   _args[0] = documentLocation;
   _args[1] = server;
   
     if (myAR->execute("astrogrid.processManager.submitStoredTo", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function astrogrid_processManager_halt(executionId)halt execution of a process
		
		executionId - id of execution to cancel(IvornOrURI)
		
	Returns void - 
       */
void astrogrid_processManager_halt ( IvornOrURI executionId)
   {
     XmlRpcValue _args, _result;
   _args[0] = executionId;
   
     if (myAR->execute("astrogrid.processManager.halt", _args, _result))
     {
     
     }
    
   };
   
			
			
/* function astrogrid_processManager_delete(executionId)delete all record of a process
		
		executionId - (IvornOrURI)
		
	Returns void - 
       */
void astrogrid_processManager_delete ( IvornOrURI executionId)
   {
     XmlRpcValue _args, _result;
   _args[0] = executionId;
   
     if (myAR->execute("astrogrid.processManager.delete", _args, _result))
     {
     
     }
    
   };
   
			
			
/* function astrogrid_processManager_getExecutionInformation(executionId)get  information about an process execution
		
		executionId - id of application to query(IvornOrURI)
		
	Returns struct ExecutionInformation - summary of this execution
       */
struct ExecutionInformation astrogrid_processManager_getExecutionInformation ( IvornOrURI executionId)
   {
     XmlRpcValue _args, _result;
   struct ExecutionInformation retval;
   _args[0] = executionId;
   
     if (myAR->execute("astrogrid.processManager.getExecutionInformation", _args, _result))
     {
     ExecutionInformation_* res = new ExecutionInformation_(_result);
        res->asStruct(&retval);
     }
    
     return retval;
    
   };
   
			
			
/* function astrogrid_processManager_getMessages(executionId)return the messages received from a remote process
		
		executionId - id of process to query/(IvornOrURI)
		
	Returns ListOfExecutionMessage - an array of all messages received from process
       */
ListOfExecutionMessage astrogrid_processManager_getMessages ( IvornOrURI executionId)
   {
     XmlRpcValue _args, _result;
   ListOfExecutionMessage retval;
   _args[0] = executionId;
   
     if (myAR->execute("astrogrid.processManager.getMessages", _args, _result))
     {
     ListOf<ExecutionMessage_> s = ListOf<ExecutionMessage_>(_result);

                retval.n = s.size();
                retval.list = copyArrayAsStruct<ExecutionMessage_, struct ExecutionMessage>(s);

     }
    
     return retval;
    
   };
   
			
			
/* function astrogrid_processManager_getResults(executionid)Retreive results of the application execution
		
		executionid - id of application to query(IvornOrURI)
		
	Returns ACRKeyValueMap - results of this execution (name - value pairs).   Note that this will only be the actual results for <b>direct</b> output parameters. For output parameters specified as <b>indirect</b>, the value returned  will be the URI pointing to the location where the results are stored.
       */
ACRKeyValueMap astrogrid_processManager_getResults ( IvornOrURI executionid)
   {
     XmlRpcValue _args, _result;
   ACRKeyValueMap retval;
   _args[0] = executionid;
   
     if (myAR->execute("astrogrid.processManager.getResults", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function astrogrid_processManager_getSingleResult(executionId, resultName)convenience method to retreive a single result from an application executioin.
 
 equaivalent to <tt>getResults(execId).get(resultName)</tt>, but may be more convenient from some scripting languages, or cases
 where there's only a single result returned.
		
		executionId - id of the application to query(IvornOrURI)
		resultName - name of the result to return. If the name is null or unrecognized AND this application only returns one result, that is returned.(JString)
		
	Returns JString - a result. Never null.
       */
JString astrogrid_processManager_getSingleResult ( IvornOrURI executionId, JString resultName)
   {
     XmlRpcValue _args, _result;
   JString retval;
   _args[0] = executionId;
   _args[1] = resultName;
   
     if (myAR->execute("astrogrid.processManager.getSingleResult", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
      /* end class
      astrogrid.processManager
      */
   /* begin class dialogs.resourceChooser
    Prompt the user to select a local file / myspace resource / url by displaying a resource chooser dialogue.
  
 This is a  generalisation of the 'open/save file' browser that also allows local and  remote ( myspace / vospace / URL) resources to be selected.See: 
				org.astrogrid.acr.astrogrid.Myspace
			  */
	 
	
			
			
/* function dialogs_resourceChooser_chooseResource(title, enableRemote)show the resource chooser, and block until user selects a file
		
		title - title for the dialogue - e.g.'choose file to open'(JString)
		enableRemote - - if true,allow selection of a remote resource (myspace / vospace / URL). Selection of local resources is enabled always.(BOOL)
		
	Returns IvornOrURI - URI of the selected resource, or null if the user cancelled.
       */
IvornOrURI dialogs_resourceChooser_chooseResource ( JString title, BOOL enableRemote)
   {
     XmlRpcValue _args, _result;
   IvornOrURI retval;
   _args[0] = title;
   _args[1] = enableRemote;
   
     if (myAR->execute("dialogs.resourceChooser.chooseResource", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function dialogs_resourceChooser_chooseFolder(title, enableRemote)show the resource chooser, and block untiil user selects a folder.
		
		title - title for the dialogue - e.g.'choose file to open'(JString)
		enableRemote - - if true,allow selection of a remote resource (myspace / vospace / URL). Selection of local resources is enabled always.(BOOL)
		
	Returns IvornOrURI - URI of the selected folder, or null if the user cancelled.
       */
IvornOrURI dialogs_resourceChooser_chooseFolder ( JString title, BOOL enableRemote)
   {
     XmlRpcValue _args, _result;
   IvornOrURI retval;
   _args[0] = title;
   _args[1] = enableRemote;
   
     if (myAR->execute("dialogs.resourceChooser.chooseFolder", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function dialogs_resourceChooser_fullChooseResource(title, enableVospace, enableLocal, enableURL)fully-configurable resource chooser - a generalization of {@link #chooseResource}
		
		title - title for the dialogue(JString)
		enableVospace - if true,allow selection of a remote myspace / vospace resource.(BOOL)
		enableLocal - if true, allow selection of local files(BOOL)
		enableURL - if true, enable the 'enter a URL' tab, so an arbitrary URL can be entered.(BOOL)
		
	Returns IvornOrURI - the URI of the selected resource, or null if the user cancelled
       */
IvornOrURI dialogs_resourceChooser_fullChooseResource ( JString title, BOOL enableVospace, BOOL enableLocal, BOOL enableURL)
   {
     XmlRpcValue _args, _result;
   IvornOrURI retval;
   _args[0] = title;
   _args[1] = enableVospace;
   _args[2] = enableLocal;
   _args[3] = enableURL;
   
     if (myAR->execute("dialogs.resourceChooser.fullChooseResource", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function dialogs_resourceChooser_fullChooseFolder(title, enableVospace, enableLocal, enableURL)fully-configurable resource chooser - a generalization of {@link #chooseFolder}
		
		title - title for the dialogue(JString)
		enableVospace - if true,allow selection of a remote myspace / vospace folder(BOOL)
		enableLocal - if true, allow selection of local folders(BOOL)
		enableURL - if true, enable the 'enter a URL' tab, so an arbitrary URL can be entered. No verification that this _is_ a folder in some sense is performed.(BOOL)
		
	Returns IvornOrURI - the URI of the selected folder, or null if the user cancelled
       */
IvornOrURI dialogs_resourceChooser_fullChooseFolder ( JString title, BOOL enableVospace, BOOL enableLocal, BOOL enableURL)
   {
     XmlRpcValue _args, _result;
   IvornOrURI retval;
   _args[0] = title;
   _args[1] = enableVospace;
   _args[2] = enableLocal;
   _args[3] = enableURL;
   
     if (myAR->execute("dialogs.resourceChooser.fullChooseFolder", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
      /* end class
      dialogs.resourceChooser
      */
   /* begin class cds.sesame
    Resolve object  names to position by querying  Simbad and/or NED and/or VizieR.
 
 {@link #resolve} resolves an object name to a datastructure containing position, error, aliases, etc.
 <br />
 {@link #sesame} and {@link #sesameChooseService(String, String, boolean, String)}
 provide low-level access to the raw webservice.See: 
				http://cdsweb.u-strasbg.fr/cdsws/name_resolver.gml
			 
				http://vizier.u-strasbg.fr/xml/sesame_1.dtd
			 
				http://vizier.u-strasbg.fr/xml/sesame_1.xsd
			  */
	 
	
			
			
/* function cds_sesame_resolve(name)Resolve an object name to a position using Sesame
		
		name - the object name to resolve(JString)
		
	Returns struct SesamePositionBean - a datastructure of positional information about the object.
       */
struct SesamePositionBean cds_sesame_resolve ( JString name)
   {
     XmlRpcValue _args, _result;
   struct SesamePositionBean retval;
   _args[0] = name;
   
     if (myAR->execute("cds.sesame.resolve", _args, _result))
     {
     SesamePositionBean_* res = new SesamePositionBean_(_result);
        res->asStruct(&retval);
     }
    
     return retval;
    
   };
   
			
			
/* function cds_sesame_sesame(name, resultType)resolve a name to position
		
		name - the name to resolve(JString)
		resultType - <pre>            u = usual (corresponding to the deprecated Sesame(String name) output)                 H = html                  x = XML (XSD at http://vizier.u-strasbg.fr/xml/sesame_1.xsd, DTD at http://vizier.u-strasbg.fr/xml/sesame_1.dtd)                  p (for plain (text/plain)) and i (for all identifiers) options can be added to H or x                 </pre>(JString)
		
	Returns JString - format depending on the resultTtype parameter
       */
JString cds_sesame_sesame ( JString name, JString resultType)
   {
     XmlRpcValue _args, _result;
   JString retval;
   _args[0] = name;
   _args[1] = resultType;
   
     if (myAR->execute("cds.sesame.sesame", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function cds_sesame_sesameChooseService(name, resultType, all, service)resolve a name, selecing which services to use.
		
		name - the name to resolve(JString)
		resultType - * <pre>            u = usual (corresponding to the deprecated Sesame(String name) output)                 H = html                x = XML (XSD at http://vizier.u-strasbg.fr/xml/sesame_1.xsd, DTD at http://vizier.u-strasbg.fr/xml/sesame_1.dtd)                 p (for plain (text/plain)) and i (for all identifiers) options can be added to H or x                 </pre>(JString)
		all - true if all identifiers wanted(BOOL)
		service - <pre>    S=Simbad         N=NED          V=VizieR         A=all         </pre>         (examples : S to query in Simbad, NS to query in Ned if not found in Simbad,         NS to query in Ned and Simbad, A to query in Ned, Simbad and VizieR, ...)(JString)
		
	Returns JString - format depending on the resultTtype parameter
       */
JString cds_sesame_sesameChooseService ( JString name, JString resultType, BOOL all, JString service)
   {
     XmlRpcValue _args, _result;
   JString retval;
   _args[0] = name;
   _args[1] = resultType;
   _args[2] = all;
   _args[3] = service;
   
     if (myAR->execute("cds.sesame.sesameChooseService", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
      /* end class
      cds.sesame
      */
   /* begin class ivoa.siap
    Query for Images from Simple Image Access Protocol (SIAP) servicesSee: 
				http://www.ivoa.net/Documents/latest/SIA.html
			  */
	 
	
			
			
/* function ivoa_siap_constructQuery(service, ra, dec, size)construct query on RA, DEC, SIZE
		
		service - URL of the service endpoint, or ivorn of the service description(IvornOrURI)
		ra - right ascension (as described in siap spec)(double)
		dec - declination (as described in siap spec)(double)
		size - radius of cone ( as described in siap spec)(double)
		
	Returns URLString - query URL that can be fetched using a HTTP GET to execute query
       */
URLString ivoa_siap_constructQuery ( IvornOrURI service, double ra, double dec, double size)
   {
     XmlRpcValue _args, _result;
   URLString retval;
   _args[0] = service;
   _args[1] = ra;
   _args[2] = dec;
   _args[3] = size;
   
     if (myAR->execute("ivoa.siap.constructQuery", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function ivoa_siap_constructQueryF(service, ra, dec, size, format)construct query on RA, DEC, SIZE, FORMAT
		
		service - URL of the service endpoint, or ivorn of the service description(IvornOrURI)
		ra - right ascension (as described in siap spec)(double)
		dec - declination (as described in siap spec)(double)
		size - radius of cone ( as described in siap spec)(double)
		format - format of images (as described in siap spec)(JString)
		
	Returns URLString - query URL that can be fetched using a HTTP GET to execute query
       */
URLString ivoa_siap_constructQueryF ( IvornOrURI service, double ra, double dec, double size, JString format)
   {
     XmlRpcValue _args, _result;
   URLString retval;
   _args[0] = service;
   _args[1] = ra;
   _args[2] = dec;
   _args[3] = size;
   _args[4] = format;
   
     if (myAR->execute("ivoa.siap.constructQueryF", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function ivoa_siap_constructQueryS(service, ra, dec, ra_size, dec_size)construct query on RA, DEC, RA_SIZE, DEC_SIZE
		
		service - URL of the service endpoint, or ivorn of the service description(IvornOrURI)
		ra - right ascension (as described in siap spec)(double)
		dec - declination (as described in siap spec)(double)
		ra_size - size of ra ( as described in siap spec)(double)
		dec_size - size of dec (as described in siap spec)(double)
		
	Returns URLString - query URL that can be fetched using a HTTP GET to execute query
       */
URLString ivoa_siap_constructQueryS ( IvornOrURI service, double ra, double dec, double ra_size, double dec_size)
   {
     XmlRpcValue _args, _result;
   URLString retval;
   _args[0] = service;
   _args[1] = ra;
   _args[2] = dec;
   _args[3] = ra_size;
   _args[4] = dec_size;
   
     if (myAR->execute("ivoa.siap.constructQueryS", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function ivoa_siap_constructQuerySF(service, ra, dec, ra_size, dec_size, format)construct query on RA, DEC, RA_SIZE, DEC_SIZE, FORMAT
		
		service - URL of the service endpoint, or ivorn of the service description(IvornOrURI)
		ra - right ascension (as described in siap spec)(double)
		dec - declination (as described in siap spec)(double)
		ra_size - size of ra ( as described in siap spec)(double)
		dec_size - size of dec (as described in siap spec)(double)
		format - format of images (as described in siap spec)(JString)
		
	Returns URLString - query URL that can be fetched using a HTTP GET to execute query
       */
URLString ivoa_siap_constructQuerySF ( IvornOrURI service, double ra, double dec, double ra_size, double dec_size, JString format)
   {
     XmlRpcValue _args, _result;
   URLString retval;
   _args[0] = service;
   _args[1] = ra;
   _args[2] = dec;
   _args[3] = ra_size;
   _args[4] = dec_size;
   _args[5] = format;
   
     if (myAR->execute("ivoa.siap.constructQuerySF", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function ivoa_siap_addOption(query, optionName, optionValue)add an option to a previously constructed query
		
		query - the query url(URLString)
		optionName - name of the option to add(JString)
		optionValue - value for the new option(JString)
		
	Returns URLString - <tt>query</tt> with the option appended.
       */
URLString ivoa_siap_addOption ( URLString query, JString optionName, JString optionValue)
   {
     XmlRpcValue _args, _result;
   URLString retval;
   _args[0] = query;
   _args[1] = optionName;
   _args[2] = optionValue;
   
     if (myAR->execute("ivoa.siap.addOption", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function ivoa_siap_execute(query)execute a DAL query, returning a datastructure
		
		query - query url to execute(URLString)
		
	Returns ListOfACRKeyValueMap - A model the DAL query response as a list of  of rows. Each row is represented is a map between UCD keys or datamodel names   and values from the response
       */
ListOfACRKeyValueMap ivoa_siap_execute ( URLString query)
   {
     XmlRpcValue _args, _result;
   ListOfACRKeyValueMap retval;
   _args[0] = query;
   
     if (myAR->execute("ivoa.siap.execute", _args, _result))
     {
     ListOf<ACRKeyValueMap> s = ListOf<ACRKeyValueMap>(_result);

                retval.n = s.size();
                retval.list = copyArray<ACRKeyValueMap, ACRKeyValueMap>(s);

     }
    
     return retval;
    
   };
   
			
			
/* function ivoa_siap_executeVotable(query)execute a DAL query, returning a votable document.
 
 This is a convenience method  - just performs a 'GET' on the query url- many programming languages support this functionality themselves
		
		query - query url to execute(URLString)
		
	Returns XMLString - a votable document of results
       */
XMLString ivoa_siap_executeVotable ( URLString query)
   {
     XmlRpcValue _args, _result;
   XMLString retval;
   _args[0] = query;
   
     if (myAR->execute("ivoa.siap.executeVotable", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function ivoa_siap_executeAndSave(query, saveLocation)execute a DAL query and save the resulting document.
		
		query - query url to execute(URLString)
		saveLocation - location to save result document - may be file:/, ivo:// (myspace), ftp://(IvornOrURI)
		
	Returns void - 
       */
void ivoa_siap_executeAndSave ( URLString query, IvornOrURI saveLocation)
   {
     XmlRpcValue _args, _result;
   _args[0] = query;
   _args[1] = saveLocation;
   
     if (myAR->execute("ivoa.siap.executeAndSave", _args, _result))
     {
     
     }
    
   };
   
			
			
/* function ivoa_siap_saveDatasets(query, saveLocation)save the datasets pointed to by this DAL query response
		
		query - the DAL query(URLString)
		saveLocation - location of a directory in which to save the datasets. may be a file:/, ivo:// or ftp:// reference(IvornOrURI)
		
	Returns int - number of datasets saved.
       */
int ivoa_siap_saveDatasets ( URLString query, IvornOrURI saveLocation)
   {
     XmlRpcValue _args, _result;
   int retval;
   _args[0] = query;
   _args[1] = saveLocation;
   
     if (myAR->execute("ivoa.siap.saveDatasets", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function ivoa_siap_saveDatasetsSubset(query, saveLocation, rows)save a subset of the datasets point to by this DAL query response
		
		query - the DAL query(URLString)
		saveLocation - location of a directory in which to save the datasets. may be a file:/, ivo:// or ftp:// reference(IvornOrURI)
		rows - list of Integers - indexes of the rows in the query response for which to save the dataset.(ACRList)
		
	Returns int - number of datasets saved.
       */
int ivoa_siap_saveDatasetsSubset ( URLString query, IvornOrURI saveLocation, ACRList rows)
   {
     XmlRpcValue _args, _result;
   int retval;
   _args[0] = query;
   _args[1] = saveLocation;
   _args[2] = rows;
   
     if (myAR->execute("ivoa.siap.saveDatasetsSubset", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function ivoa_siap_getRegistryAdqlQuery()helper method - returns an ADQL/s query that should be passed to a registry to list all 
 available DAL services of this type. 
 <br/>
 can be used as a starting point for filters, etc.
		
		
	Returns JString - an adql query string
       */
JString ivoa_siap_getRegistryAdqlQuery ( )
   {
     XmlRpcValue _args, _result;
   JString retval;
   
     if (myAR->execute("ivoa.siap.getRegistryAdqlQuery", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function ivoa_siap_getRegistryXQuery()helper method - returns an Xquery that should be passed to a registry to list all 
 available DAL services of this type. 
 <br/>
 can be used as a starting point for filters, etc.
		
		
	Returns JString - an xquery string
       */
JString ivoa_siap_getRegistryXQuery ( )
   {
     XmlRpcValue _args, _result;
   JString retval;
   
     if (myAR->execute("ivoa.siap.getRegistryXQuery", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
      /* end class
      ivoa.siap
      */
   /* begin class ivoa.skyNode
    Query for data from SkyNode servicesSee: 
				http://www.ivoa.net/Documents/latest/SkyNodeInterface.html
			  */
	 
	
			
			
/* function ivoa_skyNode_getRegistryAdqlQuery()helper method - returns an adql query that should be passed to a registry to list all known skynode services
		
		
	Returns JString - an adql query string
       */
JString ivoa_skyNode_getRegistryAdqlQuery ( )
   {
     XmlRpcValue _args, _result;
   JString retval;
   
     if (myAR->execute("ivoa.skyNode.getRegistryAdqlQuery", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function ivoa_skyNode_getRegistryXQuery()returns an xquery that should be passed to a registry to list all known skynode services
		
		
	Returns JString - an xquery string
       */
JString ivoa_skyNode_getRegistryXQuery ( )
   {
     XmlRpcValue _args, _result;
   JString retval;
   
     if (myAR->execute("ivoa.skyNode.getRegistryXQuery", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function ivoa_skyNode_getMetadata(service)interrogate skynode for  complete metadata about it's database
		
		service - identifier of the service to retrieve metadata for(IvornOrURI)
		
	Returns ListOfSkyNodeTableBean - a list of will use the tablebeans - actually, {@link SkyNodeTableBean} and   {@link SkyNodeColumnBean}  subclasses that present additional metadata
       */
ListOfSkyNodeTableBean ivoa_skyNode_getMetadata ( IvornOrURI service)
   {
     XmlRpcValue _args, _result;
   ListOfSkyNodeTableBean retval;
   _args[0] = service;
   
     if (myAR->execute("ivoa.skyNode.getMetadata", _args, _result))
     {
     ListOf<SkyNodeTableBean_> s = ListOf<SkyNodeTableBean_>(_result);

                retval.n = s.size();
                retval.list = copyArrayAsStruct<SkyNodeTableBean_, struct SkyNodeTableBean>(s);

     }
    
     return retval;
    
   };
   
			
			
/* function ivoa_skyNode_getFormats(service)interrogate skynode for supported output formats
		
		service - identifier of the service to retrieve metadata for(IvornOrURI)
		
	Returns ListOfJString - 
       */
ListOfJString ivoa_skyNode_getFormats ( IvornOrURI service)
   {
     XmlRpcValue _args, _result;
   ListOfJString retval;
   _args[0] = service;
   
     if (myAR->execute("ivoa.skyNode.getFormats", _args, _result))
     {
     ListOf<JString> s = ListOf<JString>(_result);

                retval.n = s.size();
                retval.list = copyArray<JString, JString>(s);

     }
    
     return retval;
    
   };
   
			
			
/* function ivoa_skyNode_getFunctions(service)interrogate skynode for the functions it supports
		
		service - identifier of the service to retrieve metadata for(IvornOrURI)
		
	Returns ListOfFunctionBean - 
       */
ListOfFunctionBean ivoa_skyNode_getFunctions ( IvornOrURI service)
   {
     XmlRpcValue _args, _result;
   ListOfFunctionBean retval;
   _args[0] = service;
   
     if (myAR->execute("ivoa.skyNode.getFunctions", _args, _result))
     {
     ListOf<FunctionBean_> s = ListOf<FunctionBean_>(_result);

                retval.n = s.size();
                retval.list = copyArrayAsStruct<FunctionBean_, struct FunctionBean>(s);

     }
    
     return retval;
    
   };
   
			
			
/* function ivoa_skyNode_getResults(service, adqlx)execute an adql query
		
		service - identifier of the service to execute query on(IvornOrURI)
		adqlx - the query to execute(XMLString)
		
	Returns XMLString - a document containing a votable
       */
XMLString ivoa_skyNode_getResults ( IvornOrURI service, XMLString adqlx)
   {
     XmlRpcValue _args, _result;
   XMLString retval;
   _args[0] = service;
   _args[1] = adqlx;
   
     if (myAR->execute("ivoa.skyNode.getResults", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function ivoa_skyNode_saveResults(service, adqlx, saveLocation)execute an adql query, saving results to specified location
		
		service - identifier of the service to execute query on(IvornOrURI)
		adqlx - the query to execute(XMLString)
		saveLocation - location to save result document - may be file:/, ivo:// (myspace), ftp://(IvornOrURI)
		
	Returns void - 
       */
void ivoa_skyNode_saveResults ( IvornOrURI service, XMLString adqlx, IvornOrURI saveLocation)
   {
     XmlRpcValue _args, _result;
   _args[0] = service;
   _args[1] = adqlx;
   _args[2] = saveLocation;
   
     if (myAR->execute("ivoa.skyNode.saveResults", _args, _result))
     {
     
     }
    
   };
   
			
			
/* function ivoa_skyNode_getResultsF(service, adqlx, format)execute an adql query, specifying required output format
		
		service - identifier of the service to execute query on(IvornOrURI)
		adqlx - the query to execute(XMLString)
		format - required format for results (one of the results returned from {@link #getFormats()}(JString)
		
	Returns JString - a string of results @todo consider whether byte[] is a safer bet here.
       */
JString ivoa_skyNode_getResultsF ( IvornOrURI service, XMLString adqlx, JString format)
   {
     XmlRpcValue _args, _result;
   JString retval;
   _args[0] = service;
   _args[1] = adqlx;
   _args[2] = format;
   
     if (myAR->execute("ivoa.skyNode.getResultsF", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function ivoa_skyNode_saveResultsF(service, adqlx, saveLocation, format)execute an adql query, saving results to specified location, specifying required output format.
		
		service - identifier of the service to execute query on(IvornOrURI)
		adqlx - the query to execute(XMLString)
		saveLocation - location to save result document - may be file:/, ivo:// (myspace), ftp://(IvornOrURI)
		format - (JString)
		
	Returns void - 
       */
void ivoa_skyNode_saveResultsF ( IvornOrURI service, XMLString adqlx, IvornOrURI saveLocation, JString format)
   {
     XmlRpcValue _args, _result;
   _args[0] = service;
   _args[1] = adqlx;
   _args[2] = saveLocation;
   _args[3] = format;
   
     if (myAR->execute("ivoa.skyNode.saveResultsF", _args, _result))
     {
     
     }
    
   };
   
			
			
/* function ivoa_skyNode_getFootprint(service, region)query the server's footprint for this region (FULL Skynode only)
      * @param service identifier of the service to interrogate
      * @param region a STC document describing a region
		
		service - (IvornOrURI)
		region - (XMLString)
		
	Returns XMLString - another STC document describing the intersection between the parameter <tt>region</tt> and the holdings of this skynode
       */
XMLString ivoa_skyNode_getFootprint ( IvornOrURI service, XMLString region)
   {
     XmlRpcValue _args, _result;
   XMLString retval;
   _args[0] = service;
   _args[1] = region;
   
     if (myAR->execute("ivoa.skyNode.getFootprint", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function ivoa_skyNode_estimateQueryCost(planId, adql)interrogate the service to estimate the cost of a query (FULL Skynode only)
		
		planId - not known @todo(long)
		adql - query to estimate cost for.(XMLString)
		
	Returns double - estimation of query cost
       */
double ivoa_skyNode_estimateQueryCost ( long planId, XMLString adql)
   {
     XmlRpcValue _args, _result;
   double retval;
   _args[0] = planId;
   _args[1] = adql;
   
     if (myAR->execute("ivoa.skyNode.estimateQueryCost", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function ivoa_skyNode_getAvailability(service)interrogate server for system information
		
		service - identifier of the service to interrogate(IvornOrURI)
		
	Returns struct AvailabilityBean - availability information for this server
       */
struct AvailabilityBean ivoa_skyNode_getAvailability ( IvornOrURI service)
   {
     XmlRpcValue _args, _result;
   struct AvailabilityBean retval;
   _args[0] = service;
   
     if (myAR->execute("ivoa.skyNode.getAvailability", _args, _result))
     {
     AvailabilityBean_* res = new AvailabilityBean_(_result);
        res->asStruct(&retval);
     }
    
     return retval;
    
   };
   
      /* end class
      ivoa.skyNode
      */
   /* begin class ivoa.ssap
    Querying for Spectra from Simple Spectral Access Protool (SSAP) Services.
 <b>NB:</b> working, but based on unfinished IVOA specification - interface may need to change to follow specificaiton. */
	 
	
			
			
/* function ivoa_ssap_constructQuery(service, ra, dec, size)construct query on RA, DEC, SIZE
		
		service - URL of the service endpoint, or ivorn of the service description(IvornOrURI)
		ra - right ascension (as described in ssap spec)(double)
		dec - declination (as described in ssap spec)(double)
		size - radius of cone ( as described in ssap spec)(double)
		
	Returns URLString - query URL that can be fetched using a HTTP GET to execute query
       */
URLString ivoa_ssap_constructQuery ( IvornOrURI service, double ra, double dec, double size)
   {
     XmlRpcValue _args, _result;
   URLString retval;
   _args[0] = service;
   _args[1] = ra;
   _args[2] = dec;
   _args[3] = size;
   
     if (myAR->execute("ivoa.ssap.constructQuery", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function ivoa_ssap_constructQueryS(service, ra, dec, ra_size, dec_size)construct query on RA, DEC, RA_SIZE, DEC_SIZE
		
		service - URL of the service endpoint, or ivorn of the service description(IvornOrURI)
		ra - right ascension (as described in ssap spec)(double)
		dec - declination (as described in ssap spec)(double)
		ra_size - size of ra ( as described in ssap spec)(double)
		dec_size - size of dec (as described in ssap spec)(double)
		
	Returns URLString - query URL that can be fetched using a HTTP GET to execute query
       */
URLString ivoa_ssap_constructQueryS ( IvornOrURI service, double ra, double dec, double ra_size, double dec_size)
   {
     XmlRpcValue _args, _result;
   URLString retval;
   _args[0] = service;
   _args[1] = ra;
   _args[2] = dec;
   _args[3] = ra_size;
   _args[4] = dec_size;
   
     if (myAR->execute("ivoa.ssap.constructQueryS", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function ivoa_ssap_addOption(query, optionName, optionValue)add an option to a previously constructed query
		
		query - the query url(URLString)
		optionName - name of the option to add(JString)
		optionValue - value for the new option(JString)
		
	Returns URLString - <tt>query</tt> with the option appended.
       */
URLString ivoa_ssap_addOption ( URLString query, JString optionName, JString optionValue)
   {
     XmlRpcValue _args, _result;
   URLString retval;
   _args[0] = query;
   _args[1] = optionName;
   _args[2] = optionValue;
   
     if (myAR->execute("ivoa.ssap.addOption", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function ivoa_ssap_execute(query)execute a DAL query, returning a datastructure
		
		query - query url to execute(URLString)
		
	Returns ListOfACRKeyValueMap - A model the DAL query response as a list of  of rows. Each row is represented is a map between UCD keys or datamodel names   and values from the response
       */
ListOfACRKeyValueMap ivoa_ssap_execute ( URLString query)
   {
     XmlRpcValue _args, _result;
   ListOfACRKeyValueMap retval;
   _args[0] = query;
   
     if (myAR->execute("ivoa.ssap.execute", _args, _result))
     {
     ListOf<ACRKeyValueMap> s = ListOf<ACRKeyValueMap>(_result);

                retval.n = s.size();
                retval.list = copyArray<ACRKeyValueMap, ACRKeyValueMap>(s);

     }
    
     return retval;
    
   };
   
			
			
/* function ivoa_ssap_executeVotable(query)execute a DAL query, returning a votable document.
 
 This is a convenience method  - just performs a 'GET' on the query url- many programming languages support this functionality themselves
		
		query - query url to execute(URLString)
		
	Returns XMLString - a votable document of results
       */
XMLString ivoa_ssap_executeVotable ( URLString query)
   {
     XmlRpcValue _args, _result;
   XMLString retval;
   _args[0] = query;
   
     if (myAR->execute("ivoa.ssap.executeVotable", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function ivoa_ssap_executeAndSave(query, saveLocation)execute a DAL query and save the resulting document.
		
		query - query url to execute(URLString)
		saveLocation - location to save result document - may be file:/, ivo:// (myspace), ftp://(IvornOrURI)
		
	Returns void - 
       */
void ivoa_ssap_executeAndSave ( URLString query, IvornOrURI saveLocation)
   {
     XmlRpcValue _args, _result;
   _args[0] = query;
   _args[1] = saveLocation;
   
     if (myAR->execute("ivoa.ssap.executeAndSave", _args, _result))
     {
     
     }
    
   };
   
			
			
/* function ivoa_ssap_saveDatasets(query, saveLocation)save the datasets pointed to by this DAL query response
		
		query - the DAL query(URLString)
		saveLocation - location of a directory in which to save the datasets. may be a file:/, ivo:// or ftp:// reference(IvornOrURI)
		
	Returns int - number of datasets saved.
       */
int ivoa_ssap_saveDatasets ( URLString query, IvornOrURI saveLocation)
   {
     XmlRpcValue _args, _result;
   int retval;
   _args[0] = query;
   _args[1] = saveLocation;
   
     if (myAR->execute("ivoa.ssap.saveDatasets", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function ivoa_ssap_saveDatasetsSubset(query, saveLocation, rows)save a subset of the datasets point to by this DAL query response
		
		query - the DAL query(URLString)
		saveLocation - location of a directory in which to save the datasets. may be a file:/, ivo:// or ftp:// reference(IvornOrURI)
		rows - list of Integers - indexes of the rows in the query response for which to save the dataset.(ACRList)
		
	Returns int - number of datasets saved.
       */
int ivoa_ssap_saveDatasetsSubset ( URLString query, IvornOrURI saveLocation, ACRList rows)
   {
     XmlRpcValue _args, _result;
   int retval;
   _args[0] = query;
   _args[1] = saveLocation;
   _args[2] = rows;
   
     if (myAR->execute("ivoa.ssap.saveDatasetsSubset", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function ivoa_ssap_getRegistryAdqlQuery()helper method - returns an ADQL/s query that should be passed to a registry to list all 
 available DAL services of this type. 
 <br/>
 can be used as a starting point for filters, etc.
		
		
	Returns JString - an adql query string
       */
JString ivoa_ssap_getRegistryAdqlQuery ( )
   {
     XmlRpcValue _args, _result;
   JString retval;
   
     if (myAR->execute("ivoa.ssap.getRegistryAdqlQuery", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function ivoa_ssap_getRegistryXQuery()helper method - returns an Xquery that should be passed to a registry to list all 
 available DAL services of this type. 
 <br/>
 can be used as a starting point for filters, etc.
		
		
	Returns JString - an xquery string
       */
JString ivoa_ssap_getRegistryXQuery ( )
   {
     XmlRpcValue _args, _result;
   JString retval;
   
     if (myAR->execute("ivoa.ssap.getRegistryXQuery", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
      /* end class
      ivoa.ssap
      */
   /* begin class astrogrid.stap
    Query for Images from Simple Time Access Protocol (STAP) servicesSee: 
				http://software.astrogrid.org/schema/vo-resource-types/Stap/v0.1/Stap.xsd
			  */
	 
	
			
			
/* function astrogrid_stap_constructQuery(service, start, end)construct query on time -  START, END
		
		service - URL of the service endpoint, or ivorn of the service description(IvornOrURI)
		start - start date and time(ACRDate)
		end - end date and time(ACRDate)
		
	Returns URLString - query URL that can be fetched using a HTTP GET to execute query
       */
URLString astrogrid_stap_constructQuery ( IvornOrURI service, ACRDate start, ACRDate end)
   {
     XmlRpcValue _args, _result;
   URLString retval;
   _args[0] = service;
   _args[1] = start;
   _args[2] = end;
   
     if (myAR->execute("astrogrid.stap.constructQuery", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function astrogrid_stap_constructQueryF(service, start, end, format)construct query on time and format -  START, DATE, FORMAT
		
		service - URL of the service endpoint, or ivorn of the service description(IvornOrURI)
		start - start date and time(ACRDate)
		end - end date and time(ACRDate)
		format - format of images or time series data (as described in stap spec)     *(JString)
		
	Returns URLString - query URL that can be fetched using a HTTP GET to execute query
       */
URLString astrogrid_stap_constructQueryF ( IvornOrURI service, ACRDate start, ACRDate end, JString format)
   {
     XmlRpcValue _args, _result;
   URLString retval;
   _args[0] = service;
   _args[1] = start;
   _args[2] = end;
   _args[3] = format;
   
     if (myAR->execute("astrogrid.stap.constructQueryF", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function astrogrid_stap_constructQueryP(service, start, end, ra, dec, size)construct query on time and position - START, END RA, DEC, SIZE
		
		service - URL of the service endpoint, or ivorn of the service description(IvornOrURI)
		start - start date and time(ACRDate)
		end - end date and time(ACRDate)
		ra - right ascension (as described in siap spec)(double)
		dec - declination (as described in siap spec)(double)
		size - radius of cone ( as described in siap spec)(double)
		
	Returns URLString - query URL that can be fetched using a HTTP GET to execute query
       */
URLString astrogrid_stap_constructQueryP ( IvornOrURI service, ACRDate start, ACRDate end, double ra, double dec, double size)
   {
     XmlRpcValue _args, _result;
   URLString retval;
   _args[0] = service;
   _args[1] = start;
   _args[2] = end;
   _args[3] = ra;
   _args[4] = dec;
   _args[5] = size;
   
     if (myAR->execute("astrogrid.stap.constructQueryP", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function astrogrid_stap_constructQueryPF(service, start, end, ra, dec, size, format)construct query on time, position and format - START, END, RA, DEC, SIZE, FORMAT
		
		service - URL of the service endpoint, or ivorn of the service description(IvornOrURI)
		start - start date and time(ACRDate)
		end - end date and time(ACRDate)
		ra - right ascension (as described in siap spec)(double)
		dec - declination (as described in siap spec)(double)
		size - radius of cone ( as described in siap spec)(double)
		format - format of images or time series data (as described in stap spec)     *(JString)
		
	Returns URLString - query URL that can be fetched using a HTTP GET to execute query
       */
URLString astrogrid_stap_constructQueryPF ( IvornOrURI service, ACRDate start, ACRDate end, double ra, double dec, double size, JString format)
   {
     XmlRpcValue _args, _result;
   URLString retval;
   _args[0] = service;
   _args[1] = start;
   _args[2] = end;
   _args[3] = ra;
   _args[4] = dec;
   _args[5] = size;
   _args[6] = format;
   
     if (myAR->execute("astrogrid.stap.constructQueryPF", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function astrogrid_stap_constructQueryS(service, start, end, ra, dec, ra_size, dec_size)construct query on time and full position START, END, RA, DEC, RA_SIZE, DEC_SIZE
		
		service - URL of the service endpoint, or ivorn of the service description(IvornOrURI)
		start - start date and time(ACRDate)
		end - end date and time(ACRDate)
		ra - right ascension (as described in siap spec)(double)
		dec - declination (as described in siap spec)(double)
		ra_size - size of ra ( as described in siap spec)(double)
		dec_size - size of dec (as described in siap spec)(double)
		
	Returns URLString - query URL that can be fetched using a HTTP GET to execute query
       */
URLString astrogrid_stap_constructQueryS ( IvornOrURI service, ACRDate start, ACRDate end, double ra, double dec, double ra_size, double dec_size)
   {
     XmlRpcValue _args, _result;
   URLString retval;
   _args[0] = service;
   _args[1] = start;
   _args[2] = end;
   _args[3] = ra;
   _args[4] = dec;
   _args[5] = ra_size;
   _args[6] = dec_size;
   
     if (myAR->execute("astrogrid.stap.constructQueryS", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function astrogrid_stap_constructQuerySF(service, start, end, ra, dec, ra_size, dec_size, format)construct query on time, full position and format START, END, RA, DEC, RA_SIZE, DEC_SIZE, FORMAT
		
		service - URL of the service endpoint, or ivorn of the service description(IvornOrURI)
		start - start date and time(ACRDate)
		end - end date and time(ACRDate)
		ra - right ascension (as described in siap spec)(double)
		dec - declination (as described in siap spec)(double)
		ra_size - size of ra ( as described in siap spec)(double)
		dec_size - size of dec (as described in siap spec)(double)
		format - format of images or time series data (as described in stap spec)     *(JString)
		
	Returns URLString - query URL that can be fetched using a HTTP GET to execute query
       */
URLString astrogrid_stap_constructQuerySF ( IvornOrURI service, ACRDate start, ACRDate end, double ra, double dec, double ra_size, double dec_size, JString format)
   {
     XmlRpcValue _args, _result;
   URLString retval;
   _args[0] = service;
   _args[1] = start;
   _args[2] = end;
   _args[3] = ra;
   _args[4] = dec;
   _args[5] = ra_size;
   _args[6] = dec_size;
   _args[7] = format;
   
     if (myAR->execute("astrogrid.stap.constructQuerySF", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function astrogrid_stap_addOption(query, optionName, optionValue)add an option to a previously constructed query
		
		query - the query url(URLString)
		optionName - name of the option to add(JString)
		optionValue - value for the new option(JString)
		
	Returns URLString - <tt>query</tt> with the option appended.
       */
URLString astrogrid_stap_addOption ( URLString query, JString optionName, JString optionValue)
   {
     XmlRpcValue _args, _result;
   URLString retval;
   _args[0] = query;
   _args[1] = optionName;
   _args[2] = optionValue;
   
     if (myAR->execute("astrogrid.stap.addOption", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function astrogrid_stap_execute(query)execute a DAL query, returning a datastructure
		
		query - query url to execute(URLString)
		
	Returns ListOfACRKeyValueMap - A model the DAL query response as a list of  of rows. Each row is represented is a map between UCD keys or datamodel names   and values from the response
       */
ListOfACRKeyValueMap astrogrid_stap_execute ( URLString query)
   {
     XmlRpcValue _args, _result;
   ListOfACRKeyValueMap retval;
   _args[0] = query;
   
     if (myAR->execute("astrogrid.stap.execute", _args, _result))
     {
     ListOf<ACRKeyValueMap> s = ListOf<ACRKeyValueMap>(_result);

                retval.n = s.size();
                retval.list = copyArray<ACRKeyValueMap, ACRKeyValueMap>(s);

     }
    
     return retval;
    
   };
   
			
			
/* function astrogrid_stap_executeVotable(query)execute a DAL query, returning a votable document.
 
 This is a convenience method  - just performs a 'GET' on the query url- many programming languages support this functionality themselves
		
		query - query url to execute(URLString)
		
	Returns XMLString - a votable document of results
       */
XMLString astrogrid_stap_executeVotable ( URLString query)
   {
     XmlRpcValue _args, _result;
   XMLString retval;
   _args[0] = query;
   
     if (myAR->execute("astrogrid.stap.executeVotable", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function astrogrid_stap_executeAndSave(query, saveLocation)execute a DAL query and save the resulting document.
		
		query - query url to execute(URLString)
		saveLocation - location to save result document - may be file:/, ivo:// (myspace), ftp://(IvornOrURI)
		
	Returns void - 
       */
void astrogrid_stap_executeAndSave ( URLString query, IvornOrURI saveLocation)
   {
     XmlRpcValue _args, _result;
   _args[0] = query;
   _args[1] = saveLocation;
   
     if (myAR->execute("astrogrid.stap.executeAndSave", _args, _result))
     {
     
     }
    
   };
   
			
			
/* function astrogrid_stap_saveDatasets(query, saveLocation)save the datasets pointed to by this DAL query response
		
		query - the DAL query(URLString)
		saveLocation - location of a directory in which to save the datasets. may be a file:/, ivo:// or ftp:// reference(IvornOrURI)
		
	Returns int - number of datasets saved.
       */
int astrogrid_stap_saveDatasets ( URLString query, IvornOrURI saveLocation)
   {
     XmlRpcValue _args, _result;
   int retval;
   _args[0] = query;
   _args[1] = saveLocation;
   
     if (myAR->execute("astrogrid.stap.saveDatasets", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function astrogrid_stap_saveDatasetsSubset(query, saveLocation, rows)save a subset of the datasets point to by this DAL query response
		
		query - the DAL query(URLString)
		saveLocation - location of a directory in which to save the datasets. may be a file:/, ivo:// or ftp:// reference(IvornOrURI)
		rows - list of Integers - indexes of the rows in the query response for which to save the dataset.(ACRList)
		
	Returns int - number of datasets saved.
       */
int astrogrid_stap_saveDatasetsSubset ( URLString query, IvornOrURI saveLocation, ACRList rows)
   {
     XmlRpcValue _args, _result;
   int retval;
   _args[0] = query;
   _args[1] = saveLocation;
   _args[2] = rows;
   
     if (myAR->execute("astrogrid.stap.saveDatasetsSubset", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function astrogrid_stap_getRegistryAdqlQuery()helper method - returns an ADQL/s query that should be passed to a registry to list all 
 available DAL services of this type. 
 <br/>
 can be used as a starting point for filters, etc.
		
		
	Returns JString - an adql query string
       */
JString astrogrid_stap_getRegistryAdqlQuery ( )
   {
     XmlRpcValue _args, _result;
   JString retval;
   
     if (myAR->execute("astrogrid.stap.getRegistryAdqlQuery", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function astrogrid_stap_getRegistryXQuery()helper method - returns an Xquery that should be passed to a registry to list all 
 available DAL services of this type. 
 <br/>
 can be used as a starting point for filters, etc.
		
		
	Returns JString - an xquery string
       */
JString astrogrid_stap_getRegistryXQuery ( )
   {
     XmlRpcValue _args, _result;
   JString retval;
   
     if (myAR->execute("astrogrid.stap.getRegistryXQuery", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
      /* end class
      astrogrid.stap
      */
   /* begin class util.tables
    Utility functions for working with tables.
 Exposes some of the functionality of STILSee: 
				http://www.star.bris.ac.uk/~mbt/stil/
			  */
	 
	
			
			
/* function util_tables_convertFiles(inLocation, inFormat, outLocation, outFormat)Converts a table in a file between supported formats.
		
		inLocation - input location: may be a http://, file://, ivo:// , ftp://                      compressed using unix compress, gzip or bzip2(IvornOrURI)
		inFormat - input handler name: generally one of                        fits, votable, ascii, csv, ipac, wdc or null(JString)
		outLocation - output location: file://, ivo://, ftp://(IvornOrURI)
		outFormat - output format: generally one of                       fits, fits-plus,                       votable, votable-tabledata, votable-binary-inline,                       votable-binary-href, votable-fits-inline,                        votable-fits-href,                       text, ascii, csv, html, html-element, latex,                       latex-document or null(JString)
		
	Returns void - 
       */
void util_tables_convertFiles ( IvornOrURI inLocation, JString inFormat, IvornOrURI outLocation, JString outFormat)
   {
     XmlRpcValue _args, _result;
   _args[0] = inLocation;
   _args[1] = inFormat;
   _args[2] = outLocation;
   _args[3] = outFormat;
   
     if (myAR->execute("util.tables.convertFiles", _args, _result))
     {
     
     }
    
   };
   
			
			
/* function util_tables_convertToFile(input, inFormat, outLocation, outFormat)Writes an in-memory table to a table in a file, converting between supported formats.
		
		input - the input table(JString)
		inFormat - input handler name: generally one of                        fits, votable, ascii, csv, ipac, wdc or null(JString)
		outLocation - output location: file://, ivo://, ftp://(IvornOrURI)
		outFormat - output format: generally one of                       fits, fits-plus,                       votable, votable-tabledata, votable-binary-inline,                       votable-binary-href, votable-fits-inline,                        votable-fits-href,                       text, ascii, csv, html, html-element, latex,                       latex-document or null(JString)
		
	Returns void - 
       */
void util_tables_convertToFile ( JString input, JString inFormat, IvornOrURI outLocation, JString outFormat)
   {
     XmlRpcValue _args, _result;
   _args[0] = input;
   _args[1] = inFormat;
   _args[2] = outLocation;
   _args[3] = outFormat;
   
     if (myAR->execute("util.tables.convertToFile", _args, _result))
     {
     
     }
    
   };
   
			
			
/* function util_tables_convertFromFile(inLocation, inFormat, outFormat)Reads a table in a file into an in-memory table, converting between supported formats
 Will only give good results for text-based table formats.
		
		inLocation - input location: may be a http://, file://, ivo:// , ftp://                      compressed using unix compress, gzip or bzip2(IvornOrURI)
		inFormat - input handler name: generally one of                        fits, votable, ascii, csv, ipac, wdc or null(JString)
		outFormat - output format: generally one of                       fits, fits-plus,                       votable, votable-tabledata, votable-binary-inline,                       votable-binary-href, votable-fits-inline,                        votable-fits-href,                       text, ascii, csv, html, html-element, latex,                       latex-document or null(JString)
		
	Returns JString - the converted representation of the table.
       */
JString util_tables_convertFromFile ( IvornOrURI inLocation, JString inFormat, JString outFormat)
   {
     XmlRpcValue _args, _result;
   JString retval;
   _args[0] = inLocation;
   _args[1] = inFormat;
   _args[2] = outFormat;
   
     if (myAR->execute("util.tables.convertFromFile", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function util_tables_convert(input, inFormat, outFormat)Converts an in-memory table between supported formats. 
 Will only give good results for text-based table formats.
		
		input - the input table.(JString)
		inFormat - input handler name: generally one of                        fits, votable, ascii, csv, ipac, wdc or null(JString)
		outFormat - output format: generally one of                       fits, fits-plus,                       votable, votable-tabledata, votable-binary-inline,                       votable-binary-href, votable-fits-inline,                        votable-fits-href,                       text, ascii, csv, html, html-element, latex,                       latex-document or null(JString)
		
	Returns JString - a table in the requested format.
       */
JString util_tables_convert ( JString input, JString inFormat, JString outFormat)
   {
     XmlRpcValue _args, _result;
   JString retval;
   _args[0] = input;
   _args[1] = inFormat;
   _args[2] = outFormat;
   
     if (myAR->execute("util.tables.convert", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function util_tables_listOutputFormats()list the names of the table formats this module can write out as
		
		
	Returns ListOfJString - 
       */
ListOfJString util_tables_listOutputFormats ( )
   {
     XmlRpcValue _args, _result;
   ListOfJString retval;
   
     if (myAR->execute("util.tables.listOutputFormats", _args, _result))
     {
     ListOf<JString> s = ListOf<JString>(_result);

                retval.n = s.size();
                retval.list = copyArray<JString, JString>(s);

     }
    
     return retval;
    
   };
   
			
			
/* function util_tables_listInputFormats()list the names of the table formats this module can read in from
		
		
	Returns ListOfJString - 
       */
ListOfJString util_tables_listInputFormats ( )
   {
     XmlRpcValue _args, _result;
   ListOfJString retval;
   
     if (myAR->execute("util.tables.listInputFormats", _args, _result))
     {
     ListOf<JString> s = ListOf<JString>(_result);

                retval.n = s.size();
                retval.list = copyArray<JString, JString>(s);

     }
    
     return retval;
    
   };
   
      /* end class
      util.tables
      */
   /* begin class dialogs.toolEditor
    Display the remote invocation document editor as a dialogue.
 
 Can be used to construct calls to Remote applications, such as data processor and catalogues.

 <br />
 Displays the content of a invocation document, augmented with data about this application loaded from
 the registry. Enables user to edit input ad output parameters (including using the {@link org.astrogrid.acr.dialogs.ResourceChooser}
 dialogue to select indirect parameters.
 <br />
 <img src="doc-files/pw-params.png"/>See: 
				<a href="http://www.astrogrid.org/maven/docs/HEAD/astrogrid-workflow-objects/schema/Workflow.html#element_tool">Tool Document Schema-Documentation</a>
			 
				<a href="http://www.astrogrid.org/maven/docs/HEAD/astrogrid-workflow-objects/schema/AGParameterDefinition.html#type_parameter">Value Parameter Element Schema-Documentation</a>
			 
				<a href="http://www.astrogrid.org/viewcvs/astrogrid/workflow-objects/schema/">XSD Schemas</a>
			 
				<a href="doc-files/example-tool.xml"> Example Tool Document</a>
			 
				org.astrogrid.acr.astrogrid.Applications
			  */
	 
	
			
			
/* function dialogs_toolEditor_edit(t)Prompt the user to edit a tool document
		
		t - document conforming to Tool schema(XMLString)
		
	Returns XMLString - an edited copy of this document
       */
XMLString dialogs_toolEditor_edit ( XMLString t)
   {
     XmlRpcValue _args, _result;
   XMLString retval;
   _args[0] = t;
   
     if (myAR->execute("dialogs.toolEditor.edit", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function dialogs_toolEditor_editStored(documentLocation)prompt the user to edit a tool document stored elsewhere
		
		documentLocation - location the tool document is stored at (http://, ftp://, ivo://)(IvornOrURI)
		
	Returns XMLString - edited copy of this document
       */
XMLString dialogs_toolEditor_editStored ( IvornOrURI documentLocation)
   {
     XmlRpcValue _args, _result;
   XMLString retval;
   _args[0] = documentLocation;
   
     if (myAR->execute("dialogs.toolEditor.editStored", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function dialogs_toolEditor_selectAndBuild()prompt the user to select a VO service (application, datacenter, or something else) and construct a query against it.
		
		
	Returns XMLString - a new tool document
       */
XMLString dialogs_toolEditor_selectAndBuild ( )
   {
     XmlRpcValue _args, _result;
   XMLString retval;
   
     if (myAR->execute("dialogs.toolEditor.selectAndBuild", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
      /* end class
      dialogs.toolEditor
      */
   /* begin class cds.ucd
    Web Service for manipulating 
Unified Content Descriptors (UCD).See: 
				http://cdsweb.u-strasbg.fr/cdsws/ucdClient.gml
			  */
	 
	
			
			
/* function cds_ucd_UCDList()list of UCD1
		
		
	Returns JString - html document containing all ucd1
       */
JString cds_ucd_UCDList ( )
   {
     XmlRpcValue _args, _result;
   JString retval;
   
     if (myAR->execute("cds.ucd.UCDList", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function cds_ucd_resolveUCD(ucd)resolve a UCD1 (wont work with UCD1+)
		
		ucd - ucd  the UCD1 to resolve (example : PHOT_JHN_V)(JString)
		
	Returns JString - sentence corresponding to the UCD1 (example : Johnson magnitude V (JHN))
       */
JString cds_ucd_resolveUCD ( JString ucd)
   {
     XmlRpcValue _args, _result;
   JString retval;
   _args[0] = ucd;
   
     if (myAR->execute("cds.ucd.resolveUCD", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function cds_ucd_UCDofCatalog(catalog_designation)
		
		catalog_designation - designes the catalog (example : I/239)(JString)
		
	Returns JString - list of UCD1 (in raw text) contained in a given catalog
       */
JString cds_ucd_UCDofCatalog ( JString catalog_designation)
   {
     XmlRpcValue _args, _result;
   JString retval;
   _args[0] = catalog_designation;
   
     if (myAR->execute("cds.ucd.UCDofCatalog", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function cds_ucd_translate(ucd)makes the translation of old-style UCD1 into the newer UCD1+ easier:
		
		ucd - The argument is a UCD1 (not UCD1+ !).(JString)
		
	Returns JString - String ucd. This function returns the default UCD1+ corresponding to an old-style UCD1.
       */
JString cds_ucd_translate ( JString ucd)
   {
     XmlRpcValue _args, _result;
   JString retval;
   _args[0] = ucd;
   
     if (myAR->execute("cds.ucd.translate", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function cds_ucd_upgrade(ucd)upgrade a ucd
		
		ucd - a deprecated UCD1+ (word or combination).                     Useful when the 'validate' method returns with code 2.(JString)
		
	Returns JString - String ucd. This function returns a valid UCD1+ corresponding to a deprecated word.                       It is useful when some reference words of the UCD1+ vocabulary are changed,                       and ensures backward compatibility.
       */
JString cds_ucd_upgrade ( JString ucd)
   {
     XmlRpcValue _args, _result;
   JString retval;
   _args[0] = ucd;
   
     if (myAR->execute("cds.ucd.upgrade", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function cds_ucd_validate(ucd)validate a ucd
		
		ucd - (e.g. ivoa:phot.mag;em.opt.B)(JString)
		
	Returns JString - String, this function checks that a UCD is well-formed <pre> The first word of the string is an error code, possibly followed by an explanation of the error.  A return value of 0 indicates no error (valid UCD).  The error-code results from the combination (logical OR) of the following values:   1: warning indicating use of non-standard namespace (not ivoa:)  2: use of deprecated word  4: use of non-existing word  8: syntax error (extra space or unallowed character)  </pre>
       */
JString cds_ucd_validate ( JString ucd)
   {
     XmlRpcValue _args, _result;
   JString retval;
   _args[0] = ucd;
   
     if (myAR->execute("cds.ucd.validate", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function cds_ucd_explain(ucd)returns description of a ucd
		
		ucd - (e.g. ivoa:phot.mag;em.opt.B)(JString)
		
	Returns JString - String, this function gives a human-readable explanation for a UCD composed of one or several words
       */
JString cds_ucd_explain ( JString ucd)
   {
     XmlRpcValue _args, _result;
   JString retval;
   _args[0] = ucd;
   
     if (myAR->execute("cds.ucd.explain", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function cds_ucd_assign(descr)Find the UCD associated with a description
		
		descr - Plain text description of a parameter to be described(JString)
		
	Returns JString - String ucd. This function returns the UCD1+ corresponding to the description
       */
JString cds_ucd_assign ( JString descr)
   {
     XmlRpcValue _args, _result;
   JString retval;
   _args[0] = descr;
   
     if (myAR->execute("cds.ucd.assign", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
      /* end class
      cds.ucd
      */
   /* begin class cds.vizier
    Access VizieR catalogues from CDSSee: 
				http://cdsweb.u-strasbg.fr/cdsws/vizierAccess.gml
			  */
	 
	
			
			
/* function cds_vizier_cataloguesMetaData(target, radius, unit, text)get metadata about catalogues.
		
		target - (example : M31)(JString)
		radius - (example : 1.0)(double)
		unit - (example : arcmin)(JString)
		text - (author, ..., example : Ochsenbein)(JString)
		
	Returns XMLString - metadata about catalogues depending on the given parameters (VOTable format)
       */
XMLString cds_vizier_cataloguesMetaData ( JString target, double radius, JString unit, JString text)
   {
     XmlRpcValue _args, _result;
   XMLString retval;
   _args[0] = target;
   _args[1] = radius;
   _args[2] = unit;
   _args[3] = text;
   
     if (myAR->execute("cds.vizier.cataloguesMetaData", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function cds_vizier_cataloguesMetaDataWavelength(target, radius, unit, text, wavelength)get metadata about catalogues
		
		target - (example : M31)(JString)
		radius - (example : 1.0)(double)
		unit - (example : arcmin)(JString)
		text - (author, ..., example : Ochsenbein)(JString)
		wavelength - (example : optical, Radio, like in the VizieR Web interface)(JString)
		
	Returns XMLString - metadata about catalogues depending on the given parameters (VOTable format)
       */
XMLString cds_vizier_cataloguesMetaDataWavelength ( JString target, double radius, JString unit, JString text, JString wavelength)
   {
     XmlRpcValue _args, _result;
   XMLString retval;
   _args[0] = target;
   _args[1] = radius;
   _args[2] = unit;
   _args[3] = text;
   _args[4] = wavelength;
   
     if (myAR->execute("cds.vizier.cataloguesMetaDataWavelength", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function cds_vizier_cataloguesData(target, radius, unit, text)get catalogue data
		
		target - (example : M31)(JString)
		radius - (example : 1.0)(double)
		unit - (example : arcmin)(JString)
		text - (author, ..., example : Ochsenbein)(JString)
		
	Returns XMLString - data about catalogues depending on the given parameters (VOTable format)
       */
XMLString cds_vizier_cataloguesData ( JString target, double radius, JString unit, JString text)
   {
     XmlRpcValue _args, _result;
   XMLString retval;
   _args[0] = target;
   _args[1] = radius;
   _args[2] = unit;
   _args[3] = text;
   
     if (myAR->execute("cds.vizier.cataloguesData", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function cds_vizier_cataloguesDataWavelength(target, radius, unit, text, wavelength)get catalogue data for a wavelength
		
		target - (example : M31)(JString)
		radius - (example : 1.0)(double)
		unit - (example : arcmin)(JString)
		text - (author, ..., example : Ochsenbein)(JString)
		wavelength - (example : optical, Radio, like in the VizieR Web interface)(JString)
		
	Returns XMLString - data about catalogues depending on the given parameters (VOTable format)
       */
XMLString cds_vizier_cataloguesDataWavelength ( JString target, double radius, JString unit, JString text, JString wavelength)
   {
     XmlRpcValue _args, _result;
   XMLString retval;
   _args[0] = target;
   _args[1] = radius;
   _args[2] = unit;
   _args[3] = text;
   _args[4] = wavelength;
   
     if (myAR->execute("cds.vizier.cataloguesDataWavelength", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
			
			
/* function cds_vizier_metaAll()get metadata for all catalogues
		
		
	Returns XMLString - all metadata about catalogues in VizieR (VOTable format)
       */
XMLString cds_vizier_metaAll ( )
   {
     XmlRpcValue _args, _result;
   XMLString retval;
   
     if (myAR->execute("cds.vizier.metaAll", _args, _result))
     {
         retval = _result;
     }
    
     return retval;
    
   };
   
      /* end class
      cds.vizier
      */
   /* begin class votech.vomon
    Monitor service availability using the VoMon serviceSee: 
				vomon.sourceforge.net
			  */
	 
	
			
			
/* function votech_vomon_reload()forces the status information to be reloaded from the vomon server 
 - potentially expensive operation
		
		
	Returns void - 
       */
void votech_vomon_reload ( )
   {
     XmlRpcValue _args, _result;
   
     if (myAR->execute("votech.vomon.reload", _args, _result))
     {
     
     }
    
   };
   
			
			
/* function votech_vomon_checkAvailability(id)check the availability of a service
		
		id - registry id of the service(IvornOrURI)
		
	Returns struct VoMonBean - a monitor bean describing this service's availability, or null if this  service is not known
       */
struct VoMonBean votech_vomon_checkAvailability ( IvornOrURI id)
   {
     XmlRpcValue _args, _result;
   struct VoMonBean retval;
   _args[0] = id;
   
     if (myAR->execute("votech.vomon.checkAvailability", _args, _result))
     {
     VoMonBean_* res = new VoMonBean_(_result);
        res->asStruct(&retval);
     }
    
     return retval;
    
   };
   
			
			
/* function votech_vomon_checkCeaAvailability(id)check the availability of a cea application
		
		id - registry id of the application(IvornOrURI)
		
	Returns ListOfVoMonBean - an array of monitoring beans, one for each server that  states it provides this application. May be null if the application is unknown,   i.e if no servers provide this application.
       */
ListOfVoMonBean votech_vomon_checkCeaAvailability ( IvornOrURI id)
   {
     XmlRpcValue _args, _result;
   ListOfVoMonBean retval;
   _args[0] = id;
   
     if (myAR->execute("votech.vomon.checkCeaAvailability", _args, _result))
     {
     ListOf<VoMonBean_> s = ListOf<VoMonBean_>(_result);

                retval.n = s.size();
                retval.list = copyArrayAsStruct<VoMonBean_, struct VoMonBean>(s);

     }
    
     return retval;
    
   };
   
      /* end class
      votech.vomon
      */
   