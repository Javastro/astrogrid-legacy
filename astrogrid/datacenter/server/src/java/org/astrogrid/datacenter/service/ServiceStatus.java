/*
 * $Id: ServiceStatus.java,v 1.1 2004/03/07 00:33:50 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.service;


/**
 * Represents current service status.  Just a place holder for now; should
 * include things like number of queries running, when started, etc
 *
 * @author M Hill
 */

public class ServiceStatus
{
   String state = "UNKNOWN";
   
   public ServiceStatus(String givenState) {
      this.state = givenState;
   }

   public String toString() {
      return "Service Status ="+state;
   }
   
}

/*
$Log: ServiceStatus.java,v $
Revision 1.1  2004/03/07 00:33:50  mch
Started to separate It4.1 interface from general server services

 */

