/**
 * 
 */
package org.astrogrid.desktop.modules.plastic;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.votech.plastic.CommonMessageConstants;
import org.votech.plastic.incoming.handlers.AbstractMessageHandler;

/**
 * A handler that simply caches all messages, and passes them on to the next handler.
 * TODO move this to the plastic lib
 * @author jdt
 *
 */
public class CachingMessageHandler extends AbstractMessageHandler {

    /**
     * Simply a struct holding message parameters
     */
    public static class Message {
        private URI sender;
        private URI message;
        private List args;

        /**
         * @return Returns the args.
         */
        public List getArgs() {
            return args;
        }

        /**
         * @return Returns the message.
         */
        public URI getMessage() {
            return message;
        }

        /**
         * @return Returns the sender.
         */
        public URI getSender() {
            return sender;
        }

        public Message(URI sender, URI message, List args) {
            this.sender = sender;
            this.message = message;
            this.args=args;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#equals(java.lang.Object)
         */
        public boolean equals(Object obj) {
            if (obj==null) return false;
            if (! (obj instanceof Message)) return false;
            Message castedObj = (Message) obj;
            return castedObj.message.equals(message) && castedObj.sender.equals(sender) && castedObj.args.equals(args);
        }

        /* (non-Javadoc)
         * @see java.lang.Object#hashCode()
         */
        public int hashCode() {
            return sender.hashCode()+message.hashCode()+args.hashCode();
        }
        
        
    }
    /**
     * Indicate that we want all messages.
     */
    protected List getLocalMessages() {
        return CommonMessageConstants.EMPTY;
    }

    public Object perform(URI sender, URI message, List args) {
        Message messageStruct = new Message(sender, message, args);
        cachedMessages.add(messageStruct);
        return nextHandler.perform(sender, message, args);
    }
    
    private List cachedMessages = new ArrayList();
    public Collection getMessages() {
        return cachedMessages;
    }
    public void clearMessages() {
        cachedMessages.clear();
    }
}
