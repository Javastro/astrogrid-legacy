/**
 * 
 */
package org.astrogrid.desktop.modules.system.messaging;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.astrogrid.samp.Message;
import org.astrogrid.samp.client.HubConnection;
import org.astrogrid.samp.client.SampException;

/** 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 16, 20091:21:30 PM
 */
public final  class ResourceSetMessageType extends MessageType<ResourceSetMessageSender> {

    private final static URI VORESOURCE_LOADSET = URI.create("ivo://votech.org/voresource/loadList");
    private final static String SAMP_MTYPE = "voresource.loadlist";
    public static final ResourceSetMessageType instance = new ResourceSetMessageType();
    
    @Override
    protected URI getPlasticMessageType() {
        return VORESOURCE_LOADSET;
    }

    @Override
    protected ResourceSetMessageSender createPlasticSender(
            final PlasticApplicationDescription plas) {
        return new PlasticSenderImpl(plas);
    }
    
    private  class PlasticSenderImpl extends AbstractMessageSender implements ResourceSetMessageSender {

        /**
         * @param target
         */
        public PlasticSenderImpl(final PlasticApplicationDescription target) {
            super(target);
        }

        public void sendResourceSet(final List<URI> resources,  final String setName) {
            final List l = new ArrayList();
            // marshall the args..
            final List<String> us = new Vector(resources.size());
            l.add(us);
            for (final URI u : resources) {
                us.add(u.toString());
            }
            final PlasticApplicationDescription plasTarget = (PlasticApplicationDescription)getTarget();
            
            plasTarget.getTupperware().singleTargetFireAndForgetMessage(
                    getPlasticMessageType()
                    ,l
                    ,plasTarget.id);
            //@todo handle response.
        }
    }
    
    private  class SampSender extends AbstractMessageSender implements ResourceSetMessageSender {

        /**
         * @param somethingSamp
         */
        public SampSender(final SampMessageTarget t) {
            super(t);
        }

        public void sendResourceSet(final List<URI> resourceList,  final String setName) {
            final SampMessageTarget t = (SampMessageTarget)getTarget();            
            try {
                final HubConnection connection = t.getHubConnector().getConnection();
                final Map<String,Object> params = new HashMap<String,Object>();
                final Map<String,String> ids = new HashMap<String,String>();
                params.put("ids",ids);
                for(final URI r: resourceList) {
                    final String url = t.samp.webserverRoot + "resource?id=" + r; // a bit of magic here.
                    ids.put(r.toString(),url);
                }
            
                if (setName != null) {
                    params.put("name",setName);
                }
                final Message msg = new Message(SAMP_MTYPE,params);
                connection.notify(t.getId(),msg);
            } catch (final SampException x) {
                throw new RuntimeException(x);
            }              
        }
    }
    


    @Override
    protected MessageUnmarshaller<ResourceSetMessageSender> createPlasticUnmarshaller() {
        return new MessageUnmarshaller<ResourceSetMessageSender>() {

            public Object handle(final ExternalMessageTarget source, 
                    final List args,
                    final ResourceSetMessageSender handler)
                throws Exception {
                final List<URI> resList = new ArrayList<URI>();
                final Object o = args.get(0);
                if (o == null) {                    
                    return Boolean.FALSE;
                }
                if (o instanceof Collection) {
                    final Collection c= (Collection)o;
                    for (final Iterator i = c.iterator(); i.hasNext();) {
                        final Object e =  i.next();
                        if (e != null) {
                            resList.add(new URI(e.toString()));
                        }
                    }
                } else if (o.getClass().isArray()) {
                    final Object[] arr = (Object[])o;
                    for (int i = 0; i < arr.length; i++) {
                        if (arr[i] != null) {
                            resList.add(new URI(arr[i].toString()));
                        }
                    }
                } else { // treat it as a single string
                    final URI resourceId = new URI(o.toString());
                    resList.add(resourceId);
                }
                handler.setSource(source);
                handler.sendResourceSet(resList,null);
                //@todo work out how to get return value out from handler
                return Boolean.TRUE;   
            }
        };
    }

    @Override
    protected ResourceSetMessageSender createSampSender(final SampMessageTarget somethingSamp) {
        return new SampSender(somethingSamp);
    }

    @Override
    protected MessageUnmarshaller<ResourceSetMessageSender> createSampUnmarshaller() {
        return new MessageUnmarshaller<ResourceSetMessageSender> () {

            public Object handle(final ExternalMessageTarget source, final List rawInputs,
                    final ResourceSetMessageSender handler) throws Exception {
                final List<URI> ids = new ArrayList(); // mandatory
                String name = null; // optional
                for (final Map.Entry<?,?> e : (List<Map.Entry<?,?>>)rawInputs) {
                    if ("name".equals(e.getKey())) {
                        name = e.getValue().toString();
                    } else if ("ids".equals(e.getKey())) {
                        // expect value to be a map of URI -> URL (both as strings). Only care about the uri.
                        final Object o = e.getValue();
                        if (! (o instanceof Map)) {
                            throw new IllegalArgumentException("The value of the 'ids' parameter must be a Map");
                        }
                        final Map<String,String> idMap = (Map<String,String>)o;
                        for (final String s :idMap.keySet()) {
                            ids.add(new URI(s));
                        }
                    }
                }
                if (ids.size() == 0) {
                    throw new IllegalArgumentException("No resource identifiers provided: expected a parameter names 'ids' containing a map of <identifier, optional-downloadURL>");
                }
                handler.setSource(source);
                handler.sendResourceSet(ids,name);
                return null;                
            }
        };
    }

    @Override
    protected String getSampMType() {
        return SAMP_MTYPE;
    }

}
