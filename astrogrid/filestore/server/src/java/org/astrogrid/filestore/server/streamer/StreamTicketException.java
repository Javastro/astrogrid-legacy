package org.astrogrid.filestore.server.streamer;

/**
 * An exception specific to the use of {@link StreamTicket}.
 * This class of exception has no special behaviour.
 *
 * @author  Guy Rixon
 */
public class StreamTicketException extends Exception {

   /** Creates a new instance of StreamTicketException */
   public StreamTicketException() {
     super();
   }

   /** Creates a new instance of StreamTicketException */
   public StreamTicketException(String message) {
     super(message);
   }
}
