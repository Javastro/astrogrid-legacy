/*
 * $Id: plastic.h,v 1.1 2007/10/23 14:06:52 pah Exp $
 * 
 * Created on 16 Oct 2007 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2007 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 
#ifndef PLASTIC_H_
#define PLASTIC_H_
#include "acrtypes.h"

/* begin package plastic */


#ifdef __cplusplus
 extern "C" {
#endif

/* begin class plastic.hub
    The interface that a Plastic Hub should support. For information on what a Plastic Hub is, and why you'd want one,
 please see the URL below.See: 
				<a href="http://plastic.sourceforge.net/">http://plastic.sourceforge.net</a>
			  */
	 
	
			
			
/* function plastic_hub_getRegisteredIds()Get all the IDs of the currently registered applications.
		
		
	Returns ACRList - see above
       */
ACRList plastic_hub_getRegisteredIds ( );
			
			
/* function plastic_hub_getHubId()Get this hub's ID. The hub "registers with itself", and this method will give you its own Id.
		
		
	Returns IvornOrURI - see above
       */
IvornOrURI plastic_hub_getHubId ( );
			
			
/* function plastic_hub_getName(plid)Get the name of this application, as used at registration.
		
		plid - the plastic ID returned at registration(IvornOrURI)
		
	Returns JString - The user friendly name
       */
JString plastic_hub_getName ( IvornOrURI);
			
			
/* function plastic_hub_getUnderstoodMessages(plid)Get the messages understood by this application.    Note that just because an application declares itself
 to be interested in a message, it's no guarantee it will act on it.
		
		plid - the plastic ID returned at registration(IvornOrURI)
		
	Returns ACRList - A ACRList of message URIs
       */
ACRList plastic_hub_getUnderstoodMessages ( IvornOrURI);
			
			
/* function plastic_hub_getMessageRegisteredIds(message)Get all the applications that support a particular message
		
		message - the messageId you're interested in(IvornOrURI)
		
	Returns ACRList - a ACRList of plastic ids.
       */
ACRList plastic_hub_getMessageRegisteredIds ( IvornOrURI);
			
			
/* function plastic_hub_registerXMLRPC(name, supportedMessages, callBackURL)Register an application with the hub. Each application that wishes to use the hub should register with it - the
 hub may not forward messages from applications whose ID it doesn't recognise. There are different register
 methods depending on how (and whether) the application wishes to receive messages back from the hub.
		
		name - An optional string with a short name describing the application. This may be added to the hub             assigned ID, making it more human friendly.(JString)
		supportedMessages - an array of messages (as URIs) the application is interested in.(ACRList)
		callBackURL - the application's internal xmlrpc server URL. Used by the hub to send messages to the             application.(URLString)
		
	Returns IvornOrURI - a hub-assigned ID
       */
IvornOrURI plastic_hub_registerXMLRPC ( JString, ACRList, URLString);
			
			
/* function plastic_hub_registerRMI(name, supportedMessages, caller)A java-rmi version of {@link #registerXMLRPC(String, ACRList, URL) registerXMLRPC}
		
		name - see {@link #registerRMI(String, ACRList, PlasticACRListener) registerRMI}(JString)
		supportedMessages - (ACRList)
		caller - the PlasticACRListener that wishes to register(struct PlasticACRListener* )
		
	Returns IvornOrURI - 
       */
IvornOrURI plastic_hub_registerRMI ( JString, ACRList, struct PlasticACRListener* );
			
			
/* function plastic_hub_registerNoCallBack(name)Register this application with the hub, but don't send it any messages in return. This is to allow uncallable
 applications like scripting environments to register. 
 spec.
		
		name - (JString)
		
	Returns IvornOrURI - 
       */
IvornOrURI plastic_hub_registerNoCallBack ( JString);
			
			
/* function plastic_hub_registerPolling(name, supportedMessages)Register this application with the hub, but store messages for later recovery by polling.
 Note that this message is experimental and not part of the Plastic Spec.
		
		name - (JString)
		supportedMessages - (ACRList)
		
	Returns IvornOrURI - 
       */
IvornOrURI plastic_hub_registerPolling ( JString, ACRList);
			
			
/* function plastic_hub_pollForMessages(id)Poll for messages.  Returns a ACRList of messages.  Each ACRList is another
 ACRList containing (sender, message, args).
 Note that this message is experimental and not part of the Plasti Spec.
		
		id - (IvornOrURI)
		
	Returns ACRList - 
       */
ACRList plastic_hub_pollForMessages ( IvornOrURI);
			
			
/* function plastic_hub_unregister(id)Unregister the application from the hub.
		
		id - the application to unregister(IvornOrURI)
		
	Returns void - 
       */
void plastic_hub_unregister ( IvornOrURI);
			
			
/* function plastic_hub_request(sender, message, args)Send a message to all registered Plastic applications.
		
		sender - the id of the originating tool - provided by the hub on   registration.  Note that the hub is at liberty to refused to forward requests that             don't come from an ID that it has registered.(IvornOrURI)
		message - the message to send.(IvornOrURI)
		args - any arguments to pass with the message(ACRList)
		
	Returns KeyValueMap - a Map of application ids to responses
       */
ACRKeyValueMap plastic_hub_request ( IvornOrURI, IvornOrURI, ACRList);
			
			
/* function plastic_hub_requestToSubset(sender, message, args, recipientIds)Send a request to ACRListed registered Plastic apps. See {@link #request(URI, URI, ACRList) request} for
 details of the other parameters.
		
		sender - (IvornOrURI)
		message - (IvornOrURI)
		args - (ACRList)
		recipientIds - a ACRList of target application ids (as URIs)(ACRList)
		
	Returns ACRKeyValueMap - 
       */
ACRKeyValueMap plastic_hub_requestToSubset ( IvornOrURI, IvornOrURI, ACRList, ACRList);
			
			
/* function plastic_hub_requestToSubsetAsynch(sender, message, args, recipientIds)Send a request to ACRListed registered Plastic apps, but don't wait for a response.
		
		sender - (IvornOrURI)
		message - (IvornOrURI)
		args - (ACRList)
		recipientIds - a ACRList of target application ids (as URIs). See {@link #request(URI, URI, ACRList) request} for             details of the other parameters.(ACRList)
		
	Returns void - 
       */
void plastic_hub_requestToSubsetAsynch ( IvornOrURI, IvornOrURI, ACRList, ACRList);
			
			
/* function plastic_hub_requestAsynch(sender, message, args)Send a request to all registered Plastic apps, but don't wait for a response. See
 {@link #request(URI, URI, ACRList) request} for details of parameters.
		
		sender - (IvornOrURI)
		message - (IvornOrURI)
		args - (ACRList)
		
	Returns void - 
       */
void plastic_hub_requestAsynch ( IvornOrURI, IvornOrURI, ACRList);
      /* end class
      plastic.hub
      */
   
/* end package plastic */

#ifdef __cplusplus
 }
#endif
#endif /*PLASTIC_H_*/
