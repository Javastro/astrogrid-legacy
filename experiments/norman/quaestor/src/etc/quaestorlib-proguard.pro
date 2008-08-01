# This doesn't work, and I've little idea how far it is from being
# made to work.  The command-line is
# java -Xmx128m -jar $T/proguard4.2/lib/proguard.jar @quaestorlib.pro
# ...and it produces a lot of output

# location of built quaestor classes
-injars /tmp/quaestor-build/war/WEB-INF/classes

-injars /Data/tools/sisc-1.16.6/sisc.jar

# Jena and SDB overlap quite a lot, and do some reflection
# The following seems to produce fewer errors than naming jars
# individually, but they seem more intractable, and it looks as if the
# only way to go is to try and ferret out the dependencies by hand.  I
# think there may be a few inconsistencies in these jar files, with
# methods potentially, but not actually, calling non-existent methods.
#-injars /Data/tools/Jena-2.5.5/lib(*.jar;)
#-injars /Data/tools/SDB-1.0/lib(*.jar;)

# Jena jars
#-injars /Data/tools/Jena-2.5.5/lib(*.jar;)
-injars /Data/tools/Jena-2.5.5/lib/antlr-2.7.5.jar
#-injars /Data/tools/Jena-2.5.5/lib/arq-extra.jar
-injars /Data/tools/Jena-2.5.5/lib/arq.jar
-injars /Data/tools/Jena-2.5.5/lib/commons-logging-1.1.1.jar
-injars /Data/tools/Jena-2.5.5/lib/concurrent.jar
#-injars /Data/tools/Jena-2.5.5/lib/icu4j_3_4.jar
-injars /Data/tools/Jena-2.5.5/lib/iri.jar
-injars /Data/tools/Jena-2.5.5/lib/jena.jar
-injars /Data/tools/Jena-2.5.5/lib/jenatest.jar
#-injars /Data/tools/Jena-2.5.5/lib/json.jar
#-injars /Data/tools/Jena-2.5.5/lib/junit.jar
-injars /Data/tools/Jena-2.5.5/lib/log4j-1.2.12.jar
#-injars /Data/tools/Jena-2.5.5/lib/lucene-core-2.2.0.jar
#-injars /Data/tools/Jena-2.5.5/lib/stax-api-1.0.jar
#-injars /Data/tools/Jena-2.5.5/lib/wstx-asl-3.0.0.jar
-injars /Data/tools/Jena-2.5.5/lib/xercesImpl.jar
-injars /Data/tools/Jena-2.5.5/lib/xml-apis.jar

# SDB jars -- quite a lot of overlap with the contents of Jena jars
#-injars /Data/tools/SDB-1.0/lib(*.jar;)
#-injars /Data/tools/SDB-1.0/lib/antlr-2.7.5.jar
#-injars /Data/tools/SDB-1.0/lib/arq-extra.jar
#-injars /Data/tools/SDB-1.0/lib/arq.jar
#-injars /Data/tools/SDB-1.0/lib/commons-logging-1.1.jar
#-injars /Data/tools/SDB-1.0/lib/concurrent.jar
#-injars /Data/tools/SDB-1.0/lib/icu4j_3_4.jar
#-injars /Data/tools/SDB-1.0/lib/iri.jar
#-injars /Data/tools/SDB-1.0/lib/jena.jar
#-injars /Data/tools/SDB-1.0/lib/jenatest.jar
#-injars /Data/tools/SDB-1.0/lib/json.jar
#-injars /Data/tools/SDB-1.0/lib/junit-4.4.jar
#-injars /Data/tools/SDB-1.0/lib/log4j-1.2.12.jar
#-injars /Data/tools/SDB-1.0/lib/lucene-core-2.2.0.jar
-injars /Data/tools/SDB-1.0/lib/sdb.jar
#-injars /Data/tools/SDB-1.0/lib/stax-api-1.0.jar
#-injars /Data/tools/SDB-1.0/lib/wstx-asl-3.0.0.jar
#-injars /Data/tools/SDB-1.0/lib/xercesImpl.jar
#-injars /Data/tools/SDB-1.0/lib/xml-apis.jar

-injars /Data/tools/jetty-6.1.7/lib(*.jar;)
-libraryjars /Data/tools/junit3.8.1/junit.jar

# Where we want the output to go
-outjars quaestor-lib-pg-0.4.6.jar

# Java implementation -- this is the appropriate value on my machine
-libraryjars /System/Library/Frameworks/JavaVM.framework/Versions/1.5/Classes/classes.jar

-keep public class org.eurovotech.*
-keep public class sisc.*

-keep interface sun.misc.SignalHandler { sun.misc.SignalHandler SIG_DFL; }# referenced in SISC
-keep public class com.hp.hpl.jena.rdf.model.*
-keep public class com.hp.hpl.jena.reasoner.ReasonerRegistry
-keep public class com.hp.hpl.jena.reasoner.rulesys.RDFSRuleReasonerFactory
-keep public class com.hp.hpl.jena.vocabulary.ReasonerVocabulary
-keep public class com.hp.hpl.jena.query.*
-keep public class javax.servlet.ServletRequest
-keep public class javax.servlet.ServletResponse
-keep public class com.hp.hpl.jena.sdb.SDB
-keep public class com.hp.hpl.jena.sdb.SDBFactory
-keep public class com.hp.hpl.jena.sdb.StoreDesc
-keep public class com.hp.hpl.jena.sdb.store.StoreFactory
-dontoptimize
-dontobfuscate
