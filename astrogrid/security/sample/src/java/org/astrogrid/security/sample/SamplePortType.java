package org.astrogrid.security.sample;

import java.rmi.Remote;

public interface SamplePortType extends Remote {

  public String whoAmI ();

}