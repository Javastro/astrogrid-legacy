/**
 * 
 */
package org.astrogrid.desktop.modules.system.messaging;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/** 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 16, 20091:21:30 PM
 */
public final  class ResourceSetMessageType extends MessageType<ResourceSetMessageSender> {

    private final static URI VORESOURCE_LOADSET = URI.create("ivo://votech.org/voresource/loadList");
    
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
    
    private class PlasticSenderImpl extends AbstractMessageSender implements ResourceSetMessageSender {

        /**
         * @param target
         */
        public PlasticSenderImpl(final PlasticApplicationDescription target) {
            super(target);
        }

        public void sendResourceSet(final List<URI> resources, final String setId,
                final String setName) {
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
                //@todo check items passed in plastic message - is there a setname?
                handler.sendResourceSet(resList,null,null);
                //@todo work out how to get return value out from handler
                return Boolean.TRUE;   
            }
        };
    }

    @Override
    protected ResourceSetMessageSender createSampSender(final Object somethingSamp) {
        return null; //@implement
    }

    @Override
    protected MessageUnmarshaller<ResourceSetMessageSender> createSampUnmarshaller() {
        return null; //@implement
    }

    @Override
    protected String getSampMType() {
        return null; //@implement
    }

}
