package org.astrogrid.security;

import java.security.Principal;

/**
 * The name of an account in a community.  This
 * is essentially a user name but the "user" may be
 * a groups or a software agent rather than a human
 * individual.
 *
 * No constraints are placed on the name, so this
 * class just aggregates a String. The purpose of
 * this wrapper is to provide a class that
 * implements java.security.Principal so that it
 * can be used with JAAS.
 *
 * @author Guy Rixon
 */
 public class AccountName implements Principal {

   /**
    * The name.
    */
   private String name = "anonymous";


   /**
    * Constructs an AccountName with a given value.
    */
   public AccountName (String name) {
     this.name = name;
   }

   /**
    * Serializes the object as a String.
    */
   public String getName() {
     return this.name;
   }

   /**
    * Serializes the object as a String.
    */
   public String toString() {
     return this.name;
   }

   /**
    * Tests equality with another object.
    *
    * @return true if the other object is the same or
    * if the other object is a Principal with the same name
    */
   public boolean equals (Object other) {
     if (other instanceof Principal) {
       return this.name.equals(((Principal)other).getName());
     }
     else {
       return false;
     }
   }

 }