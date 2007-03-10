LXCP=/Volumes/Lagrange/checkouts/me/code/lx/build/classes

TRANSFORMERS=rdf-RegistryInterface-0.1.xslt rdf-RegistryInterface-1.0.xslt \
	rdf-VOResource-0.10.xslt rdf-VOResource-1.0.xslt \
	rdf-VORegistry-1.0.xslt \
	rdf-VODataService-v0.5.xslt rdf-VODataService-1.0.xslt \
	rdf-SIA-0.7.xslt rdf-SIA-1.0.xslt \
	rdf-TabularDB-v0.3.xslt

# %.rdf: %.xml VOResource2rdf-0.10.lx
# 	java -cp $(LXCP) uk.me.nxg.lx.helpers.Transformer \
# 	  $< VOResource2rdf-0.10.lx $@

# %.rdf: %.xml VOResource2rdf-0.10.xslt
# 	rm -f $@
# 	xsltproc VOResource2rdf-0.10.xslt $< >$@

%.xslt: %.lx
	java -cp $(LXCP) uk.me.nxg.lx.helpers.Transformer \
	  http://ns.nxg.me.uk/lx/generated-prefix=x --indent $< >$@

all: $(TRANSFORMERS) ExtractNamespaces.jar

ExtractNamespaces.jar: ExtractNamespaces.java
	javac $<
	jar cf $@ ExtractNamespaces*.class
	rm ExtractNamespaces*.class

clean:
	rm -f $(TRANSFORMERS) *.class
