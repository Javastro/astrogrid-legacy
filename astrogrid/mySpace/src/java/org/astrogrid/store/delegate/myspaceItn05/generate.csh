#! /bin/csh -f

# generate.csh
#
# Complete procedure to generate the files for the MySpace delegate.
# Both the java classes and the WSDL are generated.
#
# Author:
#   A C Davenhall (ACD) acd@roe.ac.uk
#
# History:
#   10/3/04 (ACD) Original version.
#   16/3/04 (ACD) First stable version.
#

#
# Delete all the class files and the files which are automatically generated.

echo "Delete the automatically generated files..."
rm *.class

rm AstrogridMyspaceSoapBindingSkeleton.java
rm AstrogridMyspaceSoapBindingStub.java
rm ManagerService.java
rm ManagerServiceLocator.java
rm Manager.java
rm KernelResults.java
rm EntryResults.java
rm StatusResults.java

#
# Preserve a copy of AstrogridMyspaceSoapBindingImpl.java.

echo "Preserve a copy of AstrogridMyspaceSoapBindingImpl.java..."
mv AstrogridMyspaceSoapBindingImpl.java \
      AstrogridMyspaceSoapBindingImpl.java_old

#
# Copy and compile the template files.

echo "Copy and compile the template files..."
cp Manager.template Manager.java
cp KernelResults.template KernelResults.java
cp EntryResults.template EntryResults.java
cp StatusResults.template StatusResults.java

javac Manager.java

#
# Generate the WSDL.

echo "Generate the WSDL..."
source buildwsdl.csh

#
# Generate the delegate Java classes.

echo "Generate the delegate Java classes from the WSDL..."
source buildjava.csh

#
# Replace the generated AstrogridMyspaceSoapBindingImpl skeleton
# with the actual class.

echo "Substitute the real AstrogridMyspaceSoapBindingImpl..."
rm AstrogridMyspaceSoapBindingImpl.java
cp AstrogridMyspaceSoapBindingImpl.template \
      AstrogridMyspaceSoapBindingImpl.java

#
# Compile everything.

echo "Compile everything..."
javac *.java

#
# Move the WSDL and WSDD files to their final resting place.

echo "Move the WSDL and WSDD files to their final resting place..."
cp ./deploy.wsdd  ../../../../../../../wsdd/.
cp ./undeploy.wsdd  ../../../../../../../wsdd/.

cp ../../../../../wp.wsdl  ../../../../../../../wsdl/.
