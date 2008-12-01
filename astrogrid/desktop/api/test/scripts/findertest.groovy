#!/usr/bin/env groovy
import org.astrogrid.acr.Finder

/* 
requires acr-interface.jar to be on groovy classpath

*/

/*
permutations to test:
vodesktop running 
    - this script should complete normally.
vodesktop not running, but vodesktop.jar on classpath 
    - ar should be started
    -this script should complete normally.
    (FAIL: at moment it prompts user to start manually, but 
    clicking ok causes a startup).
vodesktop not running, vodesktop.jar not on classpath
    - user should be prompted to start AR
    a) if user starts, script should complete normally.
    b) if user doesn't start, script should fail.
*/

// connect
f = new Finder()
acr = f.find()
assert acr != null
// try accessing a function
conf = acr.getService('system.configuration')
println conf.getKey('system.rmi.port')
// closedown.
System.exit(0)