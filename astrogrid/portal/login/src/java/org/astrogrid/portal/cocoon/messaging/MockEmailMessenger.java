/**
 * Created on Mar 22, 2004 by jdt
 * Auto generated using mockmaker
* Copyright (C) AstroGrid. All rights reserved.
*
* This software is published under the terms of the AstroGrid 
* Software License version 1.2, a copy of which has been included 
* with this distribution in the LICENSE.txt file.
* */
package org.astrogrid.portal.cocoon.messaging;
import javax.mail.Session;
import mockmaker.ReturnValues;
import mockmaker.VoidReturnValues;
import mockmaker.ExceptionalReturnValue;
import com.mockobjects.*;
import org.astrogrid.portal.cocoon.messaging.Messenger;
import org.astrogrid.portal.cocoon.messaging.MessengerException;
public class MockEmailMessenger implements Messenger {
    private ExpectationCounter mySendMessageCalls =
        new ExpectationCounter("org.astrogrid.portal.cocoon.messaging.EmailMessenger SendMessageCalls");
    private ReturnValues myActualSendMessageReturnValues =
        new VoidReturnValues(false);
    private ExpectationList mySendMessageParameter0Values =
        new ExpectationList("org.astrogrid.portal.cocoon.messaging.EmailMessenger java.lang.String");
    private ExpectationList mySendMessageParameter1Values =
        new ExpectationList("org.astrogrid.portal.cocoon.messaging.EmailMessenger java.lang.String");
    public void setExpectedSendMessageCalls(int calls) {
        mySendMessageCalls.setExpected(calls);
    }
    public void addExpectedSendMessageValues(String arg0, String arg1) {
        mySendMessageParameter0Values.addExpected(arg0);
        mySendMessageParameter1Values.addExpected(arg1);
    }
    public void sendMessage(String arg0, String arg1)
        throws MessengerException {
        mySendMessageCalls.inc();
        mySendMessageParameter0Values.addActual(arg0);
        mySendMessageParameter1Values.addActual(arg1);
        Object nextReturnValue = myActualSendMessageReturnValues.getNext();
        if (nextReturnValue instanceof ExceptionalReturnValue
            && ((ExceptionalReturnValue) nextReturnValue).getException()
                instanceof MessengerException)
            throw (MessengerException)
                ((ExceptionalReturnValue) nextReturnValue)
                .getException();
        if (nextReturnValue instanceof ExceptionalReturnValue
            && ((ExceptionalReturnValue) nextReturnValue).getException()
                instanceof RuntimeException)
            throw (RuntimeException) ((ExceptionalReturnValue) nextReturnValue)
                .getException();
    }
    public void setupExceptionSendMessage(Throwable arg) {
        myActualSendMessageReturnValues.add(new ExceptionalReturnValue(arg));
    }
    public void verify() {
        mySendMessageCalls.verify();
        mySendMessageParameter0Values.verify();
        mySendMessageParameter1Values.verify();
    }
    private String recipient;
    private Session session;
    public MockEmailMessenger(final Session session, final String recipient) {
        this.recipient = recipient;
        this.session = session;
    }
    /**
     * getter
     * @return Returns the recipient.
     */
    public String getRecipient() {
        return recipient;
    }

    /**
     * getter
     * @return Returns the session.
     */
    public Session getSession() {
        return session;
    }

}
