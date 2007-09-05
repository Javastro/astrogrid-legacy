;; test cases for the sparql package
;;
;; The following tests work, in that they do currently fail as expected,
;; but for these to be meaningful, I'll have to pass them a real knowledgebase
;; to show that they can succeed -- how else do I check that they're
;; failing in the correct way, or for the correct reasons?

(require-library 'quaestor/sparql)
(import sparql)

(require-library 'quaestor/jena)
(require-library 'quaestor/knowledgebase)

(import s2j)

(define select-query (->jstring "select ?i where { ?i a <urn:example#c1> }"))
(define ask-query (->jstring "ask { <urn:example#i1> a <urn:example#c1> }"))
(define bad-query  (->jstring "ask { <urn:example#i1 a <urn:example#c1> }"))

(define-java-classes
  <java.lang.string>
  <java.io.string-reader>
  (<uri> |java.net.URI|))

(define test-kb
  (let ()
    (import* jena rdf:ingest-from-stream/language)
    (import knowledgebase)
    
    (let ((model (rdf:ingest-from-stream/language
                  (java-new <java.io.string-reader>
                            (->jstring "<urn:example#i1> a <urn:example#c1>."))
                  ""
                  "text/rdf+n3"))
          (kb (kb:new (java-new <uri> (->jstring "urn:sparql-test-kb")) (rdf:new-empty-model))))
      (kb 'add-abox "abox" model)       ;just abox
      kb)))

;; (format #t "test-kb=~s  procedure?=~s  kb?=~s~%"
;;         test-kb (procedure? test-kb) (kb:knowledgebase? test-kb))

(expect-failure sparql-error-just-abox
                (sparql:make-query-runner test-kb
                                          ask-query
                                          '("text/plain")))

(test-kb 'add-tbox
         "tbox"
         (let ()
           (import* jena rdf:ingest-from-stream/language)
           (rdf:ingest-from-stream/language
            (java-new <java.io.string-reader>
                      (->jstring "<urn:example#c1> a <http://www.w3.org/2002/07/owl#Class>."))
            ""
            "application/n3")))

(expect sparql-good-query-select-1
        #t
        (procedure? (sparql:make-query-runner test-kb
                                              select-query
                                              '("text/plain"))))
(expect sparql-good-query-select-2
        #t
        (procedure? (sparql:make-query-runner test-kb
                                              select-query
                                              '("wibble/woot"
                                                "application/xml"))))
(expect sparql-good-query-select-3
        #t
        (procedure? (sparql:make-query-runner test-kb
                                              select-query
                                              '("fandango" "*/*"))))
(expect sparql-good-query-select-4
        #t
        (procedure? (sparql:make-query-runner test-kb
                                              select-query
                                              '("fandango" "x" "text/csv"))))
(expect sparql-good-query-ask-1
        #t
        (procedure? (sparql:make-query-runner test-kb
                                              ask-query
                                              '("text/plain"))))
(expect sparql-good-query-ask-2
        #t
        (procedure? (sparql:make-query-runner test-kb
                                              ask-query
                                              '("wibble/woot"
                                                "application/xml"))))
(expect sparql-good-query-ask-3
        #t
        (procedure? (sparql:make-query-runner test-kb
                                              ask-query
                                              '("fandango" "*/*"))))

(expect-failure sparql-error-null-kb
                (sparql:make-query-runner #f ;boolean argument
                                          ask-query
                                          '("text/plain")))
(expect-failure sparql-error-null-kb
                (sparql:make-query-runner "hello" ;string argument
                                          ask-query
                                          '("text/plain")))
(expect-failure sparql-error-null-query
                (sparql:make-query-runner test-kb
                                          (java-null <java.lang.string>);null
                                          '("text/plain")))
(expect-failure sparql-error-null-mime-types
                (sparql:make-query-runner test-kb
                                          ask-query
                                          '())) ;no MIME types
(expect-failure sparql-error-bad-mime-types
                (sparql:make-query-runner test-kb
                                          ask-query
                                          '("wibble" ;bad
                                            "text/csv" ;good only for SELECT
                                            )))
(expect-failure sparql-error-bad-query
                (sparql:make-query-runner test-kb
                                          bad-query ;malformed query
                                          '("text/plain")))
