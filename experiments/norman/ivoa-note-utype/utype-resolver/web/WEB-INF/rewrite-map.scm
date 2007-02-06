;; table of MIME-dependent URI mappings, for URIs beginning /test/...,
;; as handled by the test-redirector handler.
(("null")                               ;no mappings
 ("/testcases/simple1"
  ("text/rdf+n3" "/testcases/simple1.n3"))         ;one mapping
 ("/testcases/simple2"                             ;multiple mappings
  ("text/rdf+n3" "/testcases/simple2.n3")
  ("application/rdf+xml" "/testcases/simple2.rdf")
  ("text/undefined" "/testcases/missing.html")     ;erroneous mapping
  ("text/html" "/testcases/simple2.html")))
