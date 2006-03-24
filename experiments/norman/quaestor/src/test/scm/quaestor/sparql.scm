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

(define good-query (->jstring "ask { <urn:example#i1> a <urn:example#c1> }"))
(define bad-query  (->jstring "ask { <urn:example#i1 a <urn:example#c1> }"))

(define-java-classes
  <java.lang.string>
  <java.io.string-reader>)

(define test-kb
  (let ()
    (import* jena rdf:ingest-from-stream)
    (import knowledgebase)
    
    (let ((model (rdf:ingest-from-stream
                  (java-new <java.io.string-reader>
                            (->jstring "<urn:example#i1> a <urn:example#c1>."))
                  "application/n3"))
          (kb (kb:new "sparql-test-kb")))
      (kb 'add-abox "abox" model)       ;just abox
      kb)))

;; (format #t "test-kb=~s  procedure?=~s  kb?=~s~%"
;;         test-kb (procedure? test-kb) (kb:knowledgebase? test-kb))

(expect-failure sparql-error-just-abox
                (sparql:make-query-runner test-kb
                                          good-query
                                          '("text/plain")))

(test-kb 'add-tbox
         "tbox"
         (let ()
           (import* jena rdf:ingest-from-stream)
           (rdf:ingest-from-stream
            (java-new <java.io.string-reader>
                      (->jstring "<urn:example#c1> a <http://www.w3.org/2002/07/owl#Class>."))
            "application/n3")))

(expect sparql-good-query
        #t
        (procedure? (sparql:make-query-runner test-kb
                                              good-query
                                              '("text/plain"))))

(expect-failure sparql-error-null-kb
                (sparql:make-query-runner #f ;boolean argument
                                          good-query
                                          '("text/plain")))
(expect-failure sparql-error-null-kb
                (sparql:make-query-runner "hello" ;string argument
                                          good-query
                                          '("text/plain")))
(expect-failure sparql-error-null-query
                (sparql:make-query-runner test-kb
                                          (java-null <java.lang.string>);null
                                          '("text/plain")))
(expect-failure sparql-error-null-mime-types
                (sparql:make-query-runner test-kb
                                          good-query
                                          '())) ;no MIME types
(expect-failure sparql-error-bad-query
                (sparql:make-query-runner test-kb
                                          bad-query ;malformed query
                                          '("text/plain")))
