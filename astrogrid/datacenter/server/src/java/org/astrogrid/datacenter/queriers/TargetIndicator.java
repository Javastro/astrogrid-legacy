/*
 * $Id: TargetIndicator.java,v 1.2 2004/03/14 16:55:48 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import org.astrogrid.community.Account;
import org.astrogrid.store.Agsl;
import org.astrogrid.store.Ivorn;
import org.astrogrid.store.delegate.VoSpaceResolver;

/**
 * Used to indicate the target where the results are to be sent.  May be an AGSL, or an email address, or
 * some IVO based thingamy that is still to be resolved
 *
 */

public class TargetIndicator  {

   protected String email = null;
   protected Agsl agsl = null;
   protected Ivorn ivorn = null;
   protected Writer out = null;

   public TargetIndicator(String targetEmail) {
      this.email = targetEmail;
   }

   public TargetIndicator(Agsl targetAgsl) {
      this.agsl = targetAgsl;
   }

   public TargetIndicator(Ivorn targetIvorn) {
      this.ivorn = targetIvorn;
   }
   
   public TargetIndicator(Writer targetOut) {
      this.out = targetOut;
   }

   public String getEmail() {
      return email;
   }
   
   public Writer getWriter() {
      return out;
   }
   
   public Agsl resolveAgsl() throws IOException {
      if (agsl != null) {
         return agsl;
      }
      else if (ivorn != null) {
         return new VoSpaceResolver().resolveAgsl(ivorn);
      }
      else {
         return null;
      }
   }

   public Writer resolveWriter(Account user) throws IOException {
      if (out != null) {
         return out;
      }
      
      Agsl target = resolveAgsl();
      if (target != null) {
         return new OutputStreamWriter(target.openOutputStream(user.toUser()));
      }
      else {
         return null;
      }
   }

   
   public String toString() {
      if (email != null) {
         return "Email TargetIndicator "+email;
      }
      else if (agsl != null) {
         return "Agsl TargetIndicator "+agsl;
      }
      else if (ivorn != null) {
         return "Ivorn TargetIndicator "+ivorn;
      }
      else if (out != null) {
         return "Writer TargetIndicator ";
      }
      else {
         return super.toString();
      }
   }
}
/*
 $Log: TargetIndicator.java,v $
 Revision 1.2  2004/03/14 16:55:48  mch
 Added XSLT ADQL->SQL support

 Revision 1.1  2004/03/14 04:13:04  mch
 Wrapped output target in TargetIndicator

 */



