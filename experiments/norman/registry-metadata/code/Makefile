LXCP=/Volumes/Lagrange/checkouts/me/code/lx/build/classes

TRANSFORMERS=rdf-RegistryInterface-0.1.xslt rdf-VOResource-0.10.xslt

# %.rdf: %.xml VOResource2rdf-0.10.lx
# 	java -cp $(LXCP) uk.me.nxg.lx.helpers.Transformer \
# 	  $< VOResource2rdf-0.10.lx $@

# %.rdf: %.xml VOResource2rdf-0.10.xslt
# 	rm -f $@
# 	xsltproc VOResource2rdf-0.10.xslt $< >$@

%.xslt: %.lx
	java -cp $(LXCP) uk.me.nxg.lx.helpers.Transformer $< >$@

all: $(TRANSFORMERS) ExtractNamespaces.class

ExtractNamespaces.class: ExtractNamespaces.java
	javac $<
