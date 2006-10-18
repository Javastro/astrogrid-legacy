README.patch: Tuesday 10th October 2006.

The plugin.jelly file was altered in this patched version to use the property maven.gen.src.
The default was too unfriendly and the documentation states that maven.gen.src should be used.
If maven.gen.src is set, then it is used, otherwise the old default is used.

Also took the time to change the initialization properties in plugin.properties. Some of the
properties were incorrectly named.

NB: The version number has been altered to 1.1-patch and the dependency on javacc.jar was raised to 4.0

Jeff Lusted. 
Astrogrid. 
jl99@star.le.ac.uk
