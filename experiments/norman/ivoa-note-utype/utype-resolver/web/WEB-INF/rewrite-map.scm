;; Table of MIME-dependent URI mappings, for URIs beginning /test/...,
;; as handled by the test-redirector handler.
(("null")                               ;no mappings
 ("/testcases/simple1"
  ("text/rdf+n3" . "/testcases/simple1.n3"))         ;one mapping
 ("/testcases/simple2"                             ;multiple mappings
  ("text/rdf+n3" . "/testcases/simple2.n3")
  ("application/rdf+xml" . "/testcases/simple2.rdf")
  ;; following is bogus MIME type mapping (that's OK) to non-existent file
  ("text/undefined" . "/testcases/missing.html")     ;erroneous mapping
  ("text/html" . "/testcases/simple2.html"))
 ("/testcases/grddl1.html" "application/xhtml+xml")
 ("/testcases/grddl2.html" "application/xhtml+xml")
 ("/testcases/grddl3.html" "application/xhtml+xml")
 ("/testcases/grddl4.html" "application/xhtml+xml")
 ;; following is erroneous mapping -- content is not XML
 ("/testcases/grddl-malformed2.html" "application/xhtml+xml")
 )
