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
  <java.io.byte-array-output-stream>
  (<uri> |java.net.URI|))
(define-generic-java-methods
  to-string write)

;; Set up two knowledgebases with test data
(define test-kb
  ;; A simple KB with only a single triple in it.
  (let ()
    (import* jena rdf:ingest-from-stream/language rdf:new-empty-model)
    (import knowledgebase)
    
    (let ((model (rdf:ingest-from-stream/language
                  (java-new <java.io.string-reader>
                            (->jstring "<urn:example#i1> a <urn:example#c1>."))
                  ""
                  "text/rdf+n3"))
          (kb (kb:new (java-new <uri> (->jstring "urn:sparql-test-kb"))
                      (rdf:new-empty-model)))) ;no metadata
      (kb 'add-abox "abox" model)       ;just abox
      kb)))

(define test-kb-empty
  ;; A simple KB with no triples in it.
  (let ()
    (import* jena rdf:ingest-from-stream/language rdf:new-empty-model)
    (import knowledgebase)

    (kb:new (java-new <uri> (->jstring "urn:sparql-test-kb-empty"))
            (rdf:new-empty-model))))    ;no metadata

(define test-kb-delegating
  ;; This KB also has only a single triple in it, but it delegates to the other one.
  (let ()
    (import* jena rdf:ingest-from-stream/language)
    (import knowledgebase)
    
    (let* ((model (rdf:ingest-from-stream/language
                   (java-new <java.io.string-reader>
                             (->jstring "<urn:example#i2> a <urn:example#c1>."))
                   ""
                   "text/rdf+n3"))
           (metadata (rdf:ingest-from-stream/language
                      (java-new <java.io.string-reader>
                                (->jstring "<urn:sparql-test-kb-delegating> <http://ns.eurovotech.org/quaestor#delegatesTo> <urn:sparql-test-kb>."))
                      ""
                      "text/rdf+n3"))
           (kb (kb:new (java-new <uri> (->jstring "urn:sparql-test-kb-delegating"))
                       metadata)))
      (kb 'add-abox "abox" model)       ;just abox
      kb)))

;; it is no longer a failure to run a query against a model with no TBox
;; (expect-failure sparql-error-just-abox
;;                 (sparql:make-query-runner test-kb
;;                                           ask-query
;;                                           '("text/plain")))

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

(define (run-runner-on-kb kb)
  (let ((runner (sparql:make-query-runner kb
                                          select-query
                                          '("text/csv")))
        (string-stream (java-new <java.io.byte-array-output-stream>)))
    (runner string-stream
            (lambda (mime-type)
              #f ; discard argument at present (the following doesn't work -- dunno why)
;;               (define-generic-java-method get-bytes)
;;               (define-generic-java-field-accessor :length)
;;               (let ((mime-type-bytes (get-bytes (->jstring mime-type))))
;;                 (write string-stream
;;                        mime-type-bytes
;;                        (->jint 0)
;;                        (:length mime-type-bytes)))
              ))
    (->string (to-string string-stream))))


(expect sparql-query-empty
        "i\r\n"
        (run-runner-on-kb test-kb-empty))

(expect sparql-make-select-simple
        (string->list "i\r\nurn:example#i1\r\n")
        (string->list (run-runner-on-kb test-kb)))

(expect sparql-make-select-delegating
        (string->list "i\r\nurn:example#i2\r\nurn:example#i1\r\n")
        (string->list (run-runner-on-kb test-kb-delegating)))

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
