#LXCP=$(CT)/code/lx/build/classes
LXCP=$(CT)/code/lx/lx.jar

# DISTDIR is where 'install' puts things -- should be overridden by parent makefile
DISTDIR=XXX

# TRANSFORMERS=rdf-RegistryInterface-0.1.xslt rdf-RegistryInterface-1.0.xslt \
# 	rdf-VOResource-0.10.xslt rdf-VOResource-1.0.xslt \
# 	rdf-VORegistry-1.0.xslt \
# 	rdf-VODataService-v0.5.xslt rdf-VODataService-1.0.xslt \
# 	rdf-SIA-0.7.xslt rdf-SIA-1.0.xslt \
# 	rdf-TabularDB-v0.3.xslt

# The following are converters which are hand-written, rather than
# generated from the corresponding .xsd files.
TRANSFORMERS=rdf-VOResource-1.0.xslt rdf-RegistryInterface-1.0.xslt

SCHEMAS=../xschemas
# List of schemas which have been verified to convert sanely with
# xschema2xslt.lx.  This will be a superset of the corresponding list
# in ../xschemas/Makefile, including a few such as VORegistry-v0.3 which
# aren't targets of a conversion, but which we do need to be
# able to convert _from_.
AUTOCONVERTIBLE=SIA-v1.0 VODataService-v1.0 \
	VORegistry-v0.3 VORegistry-v1.0 \
	TabularDB-v0.3 ConeSearch-v1.0 stc-v1.30 SSA-v0.4 \
	CEA-v1.0 CEABase-v1.1 CEAImplementation-v2.0 CEATypes-v1.1 \
	VOApplication-v1.0rc1 \
	test-schema

PRODUCTS=ExtractNamespaces.jar convert-root.xslt

# %.rdf: %.xml VOResource2rdf-0.10.lx
# 	java -cp $(LXCP) uk.me.nxg.lx.helpers.Transformer \
# 	  $< VOResource2rdf-0.10.lx $@

# %.rdf: %.xml VOResource2rdf-0.10.xslt
# 	rm -f $@
# 	xsltproc VOResource2rdf-0.10.xslt $< >$@

%.xslt: %.lx
	java -cp $(LXCP) uk.me.nxg.lx.helpers.Transformer \
	  http://ns.nxg.me.uk/lx/generated-prefix=x --indent $< >$@.tmp && mv $@.tmp $@

%.xslt: $(SCHEMAS)/%.xsd xschema2xslt.lx namespace-nodes.xml
	java -cp $(LXCP) uk.me.nxg.lx.helpers.Transformer \
	  $< xschema2xslt.lx $@

%.class: %.java
	CLASSPATH=. javac $<

all: STAMP

STAMP: $(TRANSFORMERS) $(AUTOCONVERTIBLE:=.xslt) $(PRODUCTS)
	date >STAMP

ExtractNamespaces.jar: ExtractNamespaces.class XMLBuilder.class
	echo "Main-Class: ExtractNamespaces" >temp-manifest
	jar cfm $@ temp-manifest *.class
	rm -f temp-manifest

ExtractNamespaces.java: ExtractNamespaces.java.in ExtractNamespaces.content
	rm -f $@
	echo "// GENERATED from ExtractNamespaces.java.in -- DO NOT EDIT" >$@
	sed -e '/@ExtractNamespaces.content@/r ExtractNamespaces.content' ExtractNamespaces.java.in >>$@

ExtractNamespaces.content: handler-registry.xml
	rm -f $@
	xsltproc make-ExtractNamespaces-content.xslt handler-registry.xml >$@.tmp \
	  && mv $@.tmp $@

namespace-nodes.xml: handler-registry.xml make-namespace-nodes.xslt
	rm -f $@
	xsltproc make-namespace-nodes.xslt handler-registry.xml | sed 's/xxxxx-/xmlns:/g' >$@.tmp && mv $@.tmp $@

convert-root.xslt: handler-registry.xml namespace-nodes.xml make-convert-root.lx
	rm -f $@
	java -cp $(LXCP) uk.me.nxg.lx.helpers.Transformer \
	  handler-registry.xml make-convert-root.lx $@
#	xsltproc make-convert-root.xslt handler-registry.xml >$@.tmp \
#	  && mv $@.tmp $@

install: all
	test -d $(DISTDIR)
	cp $(TRANSFORMERS) convert-root.xslt fallback.xslt $(DISTDIR)
	cp $(AUTOCONVERTIBLE:=.xslt) $(DISTDIR)

clean:
	rm -f $(TRANSFORMERS) $(AUTOCONVERTIBLE:=.xslt) *.class STAMP
	rm -f $(PRODUCTS) namespace-nodes.xml
	rm -f ExtractNamespaces.content ExtractNamespaces.java ExtractNamespaces.jar
