/**
 * 
 */
package org.astrogrid.desktop.modules.system.messaging;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.astrogrid.samp.ErrInfo;
import org.astrogrid.samp.Message;
import org.astrogrid.samp.Response;
import org.astrogrid.samp.client.SampException;
import org.votech.plastic.CommonMessageConstants;

/** Message Type for exchanging a votable.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 16, 20094:08:34 PM
 */
public final class VotableMessageType extends MessageType<VotableMessageSender> {

    /**
     * 
     */
    private static final String SAMP_MTYPE = "table.load.votable";
    public static final VotableMessageType instance = new VotableMessageType();

    @Override
    protected VotableMessageSender createPlasticSender(
            final PlasticApplicationDescription plas) {
        return new PlasticSender(plas);
    }

    private class PlasticSender extends AbstractMessageSender implements VotableMessageSender {

        /**
         * @param target
         */
        public PlasticSender(final PlasticApplicationDescription target) {
            super(target);
        }

        public Response sendVotable(final URL votableURL, final String tableID, final String tableName) {
            final PlasticApplicationDescription plasTarget = (PlasticApplicationDescription)getTarget();
            try {
                final List<Object> l = new ArrayList<Object>();
                l.add(maybeRewriteForAccessability(votableURL,plasTarget.getTupperware().getWebserverRoot()).toString());            
                l.add(tableName);  
                plasTarget.getTupperware().singleTargetFireAndForgetMessage(
                        getPlasticMessageType()
                        ,l
                        ,plasTarget.id);
                //mock up a response.
                return new Response(Response.OK_STATUS,null,null);
            } catch (final MalformedURLException x) {
                final ErrInfo err = new ErrInfo(x);
                err.setErrortxt("Failed to rewrite source URL");
                return new Response(Response.ERROR_STATUS,null,err);                
            }            
        }
    }


    @Override
    protected MessageUnmarshaller<VotableMessageSender> createPlasticUnmarshaller() {
        return new MessageUnmarshaller<VotableMessageSender>() {

            public Object handle(final ExternalMessageTarget source, final List args,
                    final VotableMessageSender handler) throws Exception {
                final Object o = args.get(0);
                if (o == null) {
                    return Boolean.FALSE;
                } else {
                    final String urlString = o.toString();
                    try {
                        final URL u = new URL(urlString);
                        handler.setSource(source);
                        handler.sendVotable(u,null,null);
                        return Boolean.TRUE;
                    } catch (final MalformedURLException e) {
                        return Boolean.FALSE;
                    }
                }
            }
        };
    }

    @Override
    protected VotableMessageSender createSampSender(final SampMessageTarget somethingSamp) {
        return new SampSender(somethingSamp);
    }

    private class SampSender extends AbstractMessageSender implements VotableMessageSender {

        /**
         * @param somethingSamp
         */
        public SampSender(final SampMessageTarget target) {
            super(target);
        }

        public Response sendVotable(final URL votableURL, final String tableID, final String tableName) {
            final SampMessageTarget t = (SampMessageTarget)getTarget();

            try {
                final Map params = new HashMap();
                params.put("url",maybeRewriteForAccessability(votableURL,t.getSampImpl().getWebserverRoot()).toString());
                if (tableID != null) {
                    params.put("table-id",tableID);
                }
                if (tableName != null) {
                    params.put("name",tableName);
                }
                final Message msg = new Message(SAMP_MTYPE,params);
                return t.getHubConnector().callAndWait(t.getId(),msg,DEFAULT_TIMEOUT);
            } catch (final SampException x) {
                final ErrInfo err = new ErrInfo(x);
                err.setErrortxt("Failed to send message");
                return new Response(Response.ERROR_STATUS,null,err);               
            } catch (final MalformedURLException x) {
                final ErrInfo err = new ErrInfo(x);
                err.setErrortxt("Failed to rewrite source URL");
                return new Response(Response.ERROR_STATUS,null,err);
            }

        }
    }



    @Override
    protected MessageUnmarshaller<VotableMessageSender> createSampUnmarshaller() {
        return new MessageUnmarshaller<VotableMessageSender>() {

            public Object handle(final ExternalMessageTarget source, final List args,
                    final VotableMessageSender handler) throws Exception {
                URL u = null;
                String tableId = null;
                String name = null;
                for (final Map.Entry<?,?> e : (List<Map.Entry<?,?>>)args) {
                    if ("url".equals(e.getKey())) {
                        u = new URL(e.getValue().toString());
                    } else if ("table-id".equals(e.getKey())) {
                        tableId= e.getValue().toString();
                    } else if ("name".equals(e.getKey())) {
                        name = e.getValue().toString();
                    }
                }
                if (u == null) {
                    throw new IllegalArgumentException("No 'url' parameter provided");
                }

                handler.setSource(source);
                handler.sendVotable(u,tableId,name);
                return null;
            }
        };
    }

    @Override
    protected URI getPlasticMessageType() {
        return CommonMessageConstants.VOTABLE_LOAD_FROM_URL;
    }

    @Override
    protected String getSampMType() {
        return SAMP_MTYPE;
    }

}
