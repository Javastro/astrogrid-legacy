/*
 * $Id: plastic.cpp,v 1.1 2007/10/23 14:06:52 pah Exp $
 * 
 * Created on 16 Oct 2007 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2007 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 
#include "plastic.h"
#include "AR.h"

			
/* function plastic_hub_getRegisteredIds()Get all the IDs of the currently registered applications.
		
		
	Returns List - see above
       */
ACRList plastic_hub_getRegisteredIds ( )
   {
     XmlRpcValue _args, _result;
   ACRList retval;
   
     if (myAR->execute("plastic.hub.getRegisteredIds", _args, _result))
     {
     }
    
     return retval;
    
   };
   
			
			
/* function plastic_hub_getHubId()Get this hub's ID. The hub "registers with itself", and this method will give you its own Id.
		
		
	Returns IvornOrURI - see above
       */
IvornOrURI plastic_hub_getHubId ( )
   {
     XmlRpcValue _args, _result;
   IvornOrURI retval;
   
     if (myAR->execute("plastic.hub.getHubId", _args, _result))
     {
     }
    
     return retval;
    
   };
   
			
			
/* function plastic_hub_getName(plid)Get the name of this application, as used at registration.
		
		plid - the plastic ID returned at registration(IvornOrURI)
		
	Returns JString - The user friendly name
       */
JString plastic_hub_getName ( IvornOrURI plid)
   {
     XmlRpcValue _args, _result;
   JString retval;
   _args[0] = plid;
   
     if (myAR->execute("plastic.hub.getName", _args, _result))
     {
     }
    
     return retval;
    
   };
   
			
			
/* function plastic_hub_getUnderstoodMessages(plid)Get the messages understood by this application.    Note that just because an application declares itself
 to be interested in a message, it's no guarantee it will act on it.
		
		plid - the plastic ID returned at registration(IvornOrURI)
		
	Returns ACRList - A ACRList of message URIs
       */
ACRList plastic_hub_getUnderstoodMessages ( IvornOrURI plid)
   {
     XmlRpcValue _args, _result;
   ACRList retval;
   _args[0] = plid;
   
     if (myAR->execute("plastic.hub.getUnderstoodMessages", _args, _result))
     {
     }
    
     return retval;
    
   };
   
			
			
/* function plastic_hub_getMessageRegisteredIds(message)Get all the applications that support a particular message
		
		message - the messageId you're interested in(IvornOrURI)
		
	Returns ACRList - a ACRList of plastic ids.
       */
ACRList plastic_hub_getMessageRegisteredIds ( IvornOrURI message)
   {
     XmlRpcValue _args, _result;
   ACRList retval;
   _args[0] = message;
   
     if (myAR->execute("plastic.hub.getMessageRegisteredIds", _args, _result))
     {
     }
    
     return retval;
    
   };
   
			
			
/* function plastic_hub_registerXMLRPC(name, supportedMessages, callBackURL)Register an application with the hub. Each application that wishes to use the hub should register with it - the
 hub may not forward messages from applications whose ID it doesn't recognise. There are different register
 methods depending on how (and whether) the application wishes to receive messages back from the hub.
		
		name - An optional string with a short name describing the application. This may be added to the hub             assigned ID, making it more human friendly.(JString)
		supportedMessages - an array of messages (as URIs) the application is interested in.(ACRList)
		callBackURL - the application's internal xmlrpc server URL. Used by the hub to send messages to the             application.(URLString)
		
	Returns IvornOrURI - a hub-assigned ID
       */
IvornOrURI plastic_hub_registerXMLRPC ( JString name, ACRList supportedMessages, URLString callBackURL)
   {
     XmlRpcValue _args, _result;
   IvornOrURI retval;
   _args[0] = name;
   //FIXME   _args[1] = supportedMessages;
   _args[2] = callBackURL;
   
     if (myAR->execute("plastic.hub.registerXMLRPC", _args, _result))
     {
     }
    
     return retval;
    
   };
   
			
			
/* function plastic_hub_registerRMI(name, supportedMessages, caller)A java-rmi version of {@link #registerXMLRPC(String, ACRList, URL) registerXMLRPC}
		
		name - see {@link #registerRMI(String, ACRList, PlasticACRListener) registerRMI}(JString)
		supportedMessages - (ACRList)
		caller - the PlasticACRListener that wishes to register(struct PlasticACRListener* )
		
	Returns IvornOrURI - 
       */
IvornOrURI plastic_hub_registerRMI ( JString name, ACRList supportedMessages, struct PlasticACRListener*  caller)
   {
     XmlRpcValue _args, _result;
   IvornOrURI retval;
   _args[0] = name;
   //FIXME   _args[1] = supportedMessages;
   _args[2] = caller;
   
     if (myAR->execute("plastic.hub.registerRMI", _args, _result))
     {
     }
    
     return retval;
    
   };
   
			
			
/* function plastic_hub_registerNoCallBack(name)Register this application with the hub, but don't send it any messages in return. This is to allow uncallable
 applications like scripting environments to register. 
 spec.
		
		name - (JString)
		
	Returns IvornOrURI - 
       */
IvornOrURI plastic_hub_registerNoCallBack ( JString name)
   {
     XmlRpcValue _args, _result;
   IvornOrURI retval;
   _args[0] = name;
   
     if (myAR->execute("plastic.hub.registerNoCallBack", _args, _result))
     {
     }
    
     return retval;
    
   };
   
			
			
/* function plastic_hub_registerPolling(name, supportedMessages)Register this application with the hub, but store messages for later recovery by polling.
 Note that this message is experimental and not part of the Plastic Spec.
		
		name - (JString)
		supportedMessages - (ACRList)
		
	Returns IvornOrURI - 
       */
IvornOrURI plastic_hub_registerPolling ( JString name, ACRList supportedMessages)
   {
     XmlRpcValue _args, _result;
   IvornOrURI retval;
   _args[0] = name;
   //FIXME   _args[1] = supportedMessages;
   
     if (myAR->execute("plastic.hub.registerPolling", _args, _result))
     {
     }
    
     return retval;
    
   };
   
			
			
/* function plastic_hub_pollForMessages(id)Poll for messages.  Returns a ACRList of messages.  Each ACRList is another
 ACRList containing (sender, message, args).
 Note that this message is experimental and not part of the Plasti Spec.
		
		id - (IvornOrURI)
		
	Returns ACRList - 
       */
ACRList plastic_hub_pollForMessages ( IvornOrURI id)
   {
     XmlRpcValue _args, _result;
   ACRList retval;
   _args[0] = id;
   
     if (myAR->execute("plastic.hub.pollForMessages", _args, _result))
     {
     }
    
     return retval;
    
   };
   
			
			
/* function plastic_hub_unregister(id)Unregister the application from the hub.
		
		id - the application to unregister(IvornOrURI)
		
	Returns void - 
       */
void plastic_hub_unregister ( IvornOrURI id)
   {
     XmlRpcValue _args, _result;
   _args[0] = id;
   
     if (myAR->execute("plastic.hub.unregister", _args, _result))
     {
     }
    
   };
   
			
			
/* function plastic_hub_request(sender, message, args)Send a message to all registered Plastic applications.
		
		sender - the id of the originating tool - provided by the hub on   registration.  Note that the hub is at liberty to refused to forward requests that             don't come from an ID that it has registered.(IvornOrURI)
		message - the message to send.(IvornOrURI)
		args - any arguments to pass with the message(ACRList)
		
	Returns KeyValueMap - a Map of application ids to responses
       */
ACRKeyValueMap plastic_hub_request ( IvornOrURI sender, IvornOrURI message, ACRList args)
   {
     XmlRpcValue _args, _result;
   ACRKeyValueMap retval;
   _args[0] = sender;
   _args[1] = message;
   _args[2] = args;
   
     if (myAR->execute("plastic.hub.request", _args, _result))
     {
     }
    
     return retval;
    
   };
   
			
			
/* function plastic_hub_requestToSubset(sender, message, args, recipientIds)Send a request to ACRListed registered Plastic apps. See {@link #request(URI, URI, ACRList) request} for
 details of the other parameters.
		
		sender - (IvornOrURI)
		message - (IvornOrURI)
		args - (ACRList)
		recipientIds - a ACRList of target application ids (as URIs)(ACRList)
		
	Returns KeyValueMap - 
       */
ACRKeyValueMap plastic_hub_requestToSubset ( IvornOrURI sender, IvornOrURI message, ACRList args, ACRList recipientIds)
   {
     XmlRpcValue _args, _result;
   ACRKeyValueMap retval;
   _args[0] = sender;
   _args[1] = message;
//FIXME   _args[2] = args;
   //FIXME   _args[3] = recipientIds;
   
     if (myAR->execute("plastic.hub.requestToSubset", _args, _result))
     {
     }
    
     return retval;
    
   };
   
			
			
/* function plastic_hub_requestToSubsetAsynch(sender, message, args, recipientIds)Send a request to ACRListed registered Plastic apps, but don't wait for a response.
		
		sender - (IvornOrURI)
		message - (IvornOrURI)
		args - (ACRList)
		recipientIds - a ACRList of target application ids (as URIs). See {@link #request(URI, URI, ACRList) request} for             details of the other parameters.(ACRList)
		
	Returns void - 
       */
void plastic_hub_requestToSubsetAsynch ( IvornOrURI sender, IvornOrURI message, ACRList args, ACRList recipientIds)
   {
     XmlRpcValue _args, _result;
   _args[0] = sender;
   _args[1] = message;
   //FIXME   _args[2] = args;
   //FIXME  _args[3] = recipientIds;
   
     if (myAR->execute("plastic.hub.requestToSubsetAsynch", _args, _result))
     {
     }
    
   };
   
			
			
/* function plastic_hub_requestAsynch(sender, message, args)Send a request to all registered Plastic apps, but don't wait for a response. See
 {@link #request(URI, URI, ACRList) request} for details of parameters.
		
		sender - (IvornOrURI)
		message - (IvornOrURI)
		args - (ACRList)
		
	Returns void - 
       */
void plastic_hub_requestAsynch ( IvornOrURI sender, IvornOrURI message, ACRList args)
   {
     XmlRpcValue _args, _result;
   _args[0] = sender;
   _args[1] = message;
   //FIXME   _args[2] = args;
   
     if (myAR->execute("plastic.hub.requestAsynch", _args, _result))
     {
     }
    
   };
   
      /* end class
      plastic.hub
      */
 