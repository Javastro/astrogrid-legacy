##################################################################
# CUSTOMISATION SECTION
#
# Customise this to match your installation details.
##################################################################

JAVAC=/data/cass111a/gtr/j2sdk1.4.0/bin/javac
JAVA=/data/cass111a/gtr/j2sdk1.4.0/bin/java

# Customise this to match your top-level source directory
# (the directory where the ace source dir lives).
#
TOPLEVEL=/home/kea/ksrc
BUILD=/data/cass123a/kea/kbuild


# Shouldn't need to customise these.
#
PARENT=org.astrogrid
PARENTDIR=org/astrogrid
PACKAGE=xmlutils

PACKAGEDIR=${TOPLEVEL}/${PARENTDIR}/${PACKAGE}
PACKAGENAME=${PARENT}.${PACKAGE}

DOCDIR=${TOPLEVEL}/doc/${PARENTDIR}/${PACKAGE}

##################################################################
# THE TARGETS
##################################################################

classes: ${PACKAGEDIR}/Validate.class 

${PACKAGEDIR}/Validate.class : Validate.java ${PACKAGEDIR}/XmlValidatorXercesImpl.class ${PACKAGEDIR}/XmlValidatorFactoryImpl.class
	${JAVAC} -sourcepath . -d ${BUILD} Validate.java 

${PACKAGEDIR}/XmlValidatorFactoryImpl.class : XmlValidatorFactoryImpl.java ${PACKAGEDIR}/XmlValidatorIfc.class
	${JAVAC} -sourcepath . -d ${BUILD} XmlValidatorFactoryImpl.java 

${PACKAGEDIR}/XmlValidatorXercesImpl.class : XmlValidatorXercesImpl.java ${PACKAGEDIR}/XmlValidatorIfc.class
	${JAVAC} -sourcepath . -d ${BUILD} XmlValidatorXercesImpl.java 

${PACKAGEDIR}/XmlValidatorIfc.class : XmlValidatorIfc.java 
	${JAVAC} -sourcepath . -d ${BUILD} XmlValidatorIfc.java 

test: classes
	java org.astrogrid.xmlutils.Validate testdata.xml

javadoc:
	javadoc -d ${BUILD}/javadoc/${PACKAGENAME} *.java

clean:
	rm ${PACKAGEDIR}/*.class
