LXCP=/Volumes/Lagrange/checkouts/me/code/lx/build/classes

# %.rdf: %.xml VOResource2rdf-0.10.lx
# 	java -cp $(LXCP) uk.me.nxg.lx.helpers.Transformer \
# 	  $< VOResource2rdf-0.10.lx $@

%.rdf: %.xml VOResource2rdf-0.10.xslt
	rm -f $@
	xsltproc VOResource2rdf-0.10.xslt $< >$@

%.xslt: %.lx
	java -cp $(LXCP) uk.me.nxg.lx.helpers.Transformer $< >$@

.PRECIOUS: VOResource2rdf-0.10.xslt 
