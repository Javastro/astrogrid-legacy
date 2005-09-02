package org.astrogrid.security;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import org.apache.ws.security.WSPasswordCallback;

/**
 * A callback handler that supplies a password to WSS4J.
 * This handler supplies a fixed password the value of which is
 * set at construction of the handler.
 *
 * @author Guy Rixon
 */
public class PasswordCallback implements CallbackHandler {

  /**
   * The password to be given to callbacks.
   */
  String password;

  /**
   * Constructs a PasswordCallback with a default password.
   */
  public PasswordCallback() {
    this.password="unknown";
  }

  /**
   * Constructs a PasswordCallback with a given password.
   *
   * @param password The given password.
   */
  public PasswordCallback(String password) {
    this.password = password;
  }

  /**
   * Handles a list of callbacks, supplying the given (or default) password.
   * Only callbacks of type WSPasswordCallback should be passed in.
   *
   * @param callbacks The list of callbacks.
   * @throws UnsupportedCallbackException If a callback is not of type WSPasswordCallback.
   */
  public void handle(Callback[] callbacks) throws UnsupportedCallbackException {
    for (int i = 0; i < callbacks.length; i++) {
      if (callbacks[i] instanceof WSPasswordCallback) {
        WSPasswordCallback pc = (WSPasswordCallback)callbacks[i];
          pc.setPassword(this.password);
      }
      else {
        throw new UnsupportedCallbackException(callbacks[i], "Unrecognized Callback");
      }
    }
  }

}