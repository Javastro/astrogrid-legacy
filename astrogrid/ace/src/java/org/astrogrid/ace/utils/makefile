##################################################################
# VARIABLES SECTION
#
##################################################################

# Customise these to match your installation.
#
JAVAC=/data/cass111a/gtr/j2sdk1.4.0/bin/javac
JAVA=/data/cass111a/gtr/j2sdk1.4.0/bin/java

# Customise this to match your top-level source directory
# (the directory where the ace source dir lives).
#
TOPLEVEL=/home/kea/ksrc
DESTDIR=/data/cass123a/kea/kbuild

# Shouldn't need to customise these.
#
PARENT=org.astrogrid.ace
PARENTDIR=org/astrogrid/ace
PACKAGE=utils

PACKAGEDIR=${DESTDIR}/${PARENTDIR}/${PACKAGE}
PACKAGENAME=${PARENT}.${PACKAGE}

DOCDIR=${TOPLEVEL}/doc/${PARENTDIR}/${PACKAGE}

##################################################################
# THE TARGETS
##################################################################
help:
	# make classes: Compiles the source 
	# make test: Runs the tests

classes: ${PACKAGEDIR}/Translator.class ${PACKAGEDIR}/Tester.class 

${PACKAGEDIR}/Tester.class : Tester.java ${PACKAGEDIR}/Translator.class
	${JAVAC} -sourcepath . -d ${DESTDIR} Tester.java 

${PACKAGEDIR}/Translator.class : Translator.java ${PACKAGEDIR}/XmlToNativeDomImpl.class ${PACKAGEDIR}/NativeToXmlImpl.class ${PACKAGEDIR}/XmlToNativeXsltImpl.class
	${JAVAC} -sourcepath . -d ${DESTDIR} Translator.java 

${PACKAGEDIR}/XmlToNativeXsltImpl.class : XmlToNativeXsltImpl.java ${PACKAGEDIR}/XmlToNativeIfc.class
	${JAVAC} -sourcepath . -d ${DESTDIR} XmlToNativeXsltImpl.java 

${PACKAGEDIR}/XmlToNativeDomImpl.class : XmlToNativeDomImpl.java ${PACKAGEDIR}/XmlToNativeIfc.class
	${JAVAC} -sourcepath . -d ${DESTDIR} XmlToNativeDomImpl.java 

${PACKAGEDIR}/XmlToNativeIfc.class : XmlToNativeIfc.java 
	${JAVAC} -sourcepath . -d ${DESTDIR} XmlToNativeIfc.java 

${PACKAGEDIR}/NativeToXmlImpl.class : NativeToXmlImpl.java ${PACKAGEDIR}/NativeToXmlIfc.class 
	${JAVAC} -sourcepath . -d ${DESTDIR} NativeToXmlImpl.java 

${PACKAGEDIR}/NativeToXmlIfc.class : NativeToXmlIfc.java 
	${JAVAC} -sourcepath . -d ${DESTDIR} NativeToXmlIfc.java 

test:  ${PACKAGEDIR}/Tester.class
	# Testing xml2native:
	mkdir -p tmp
	rm -f tmp/out.*
	${JAVA} ${PACKAGENAME}.Tester xml2native testdata-1_0.xml tmp/out.sex tmp/out.param 
	# Testing native2xml:
	${JAVA} ${PACKAGENAME}.Tester native2xml tmp/out.sex tmp/out.param tmp/newdata.xml

test1:  ${PACKAGEDIR}/Tester.class
	# Testing xml2native:
	mkdir -p tmp
	rm -f tmp/out.*
	${JAVA} ${PACKAGENAME}.Tester xml2native testdata-1_0.xml tmp/out.sex tmp/out.param 

test2:  ${PACKAGEDIR}/Tester.class
	# Testing native2xml:
	${JAVA} ${PACKAGENAME}.Tester native2xml tmp/out.sex tmp/out.param tmp/newdata.xml

javadoc:
	javadoc -d ${TOPLEVEL}/javadoc/${PACKAGENAME} *.java

clean:
	rm ${PACKAGEDIR}/*.class

cleandoc:
	rm ${DOCDIR}/*.html
