;; SISC library module for Quaestor
;; Support for SPARQL queries

(import s2j)

(require-library 'quaestor/utils)

(module sparql
(sparql:perform-query)

(import* utils error-with-status)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; SPARQL stuff

;; Perform the SPARQL query in QUERY-JSTRING on the knowledgebase KB.
;; Send the XML results to the output stream returned by procedure
;; GET-OUTPUT-STREAM (which should not be called before we know we can
;; write to it), and return #t on success.  MIME-TYPE-LIST is a list of 
;; acceptable MIME types as strings.  SET-RESPONSE-CONTENT-TYPE is a
;; function which should be called with the content type (as a scheme string)
;; which is about to be written to the output stream.
;;
;; The procedure is called in a context such that it may call ERROR
;; at any point prior to sending stuff to the output stream.
(define (sparql:perform-query kb
                              query-jstring
                              get-output-stream
                              mime-type-list
                              set-response-content-type)
  (define-java-classes
    (<factory> |com.hp.hpl.jena.rdf.model.ModelFactory|))
  (define-generic-java-methods
    create-inf-model)

  (let ((handler (make-result-set-handler mime-type-list
                                          'perform-sparql-query
                                          set-response-content-type
                                          get-output-stream)))

    (or kb                              ;check we've found a KB
        (error 'perform-sparql-query
               "Request to perform-sparql-query on null knowledgebase"))
    (or handler                         ;check we can handle requested MIME type
        (error-with-status 'perform-sparql-query
                           '|SC_NOT_ACCEPTABLE|
                           "can't handle any of the MIME types ~a"
                           mime-type-list))

    (let ((tbox (kb 'get-model-tbox))
          (abox (kb 'get-model-abox)))
      (or tbox
          (error 'perform-sparql-query
                 "Model ~a has no TBOX -- can't query" (kb 'get-name)))
      (run-sparql-query query-jstring
                        (if abox
                            (create-inf-model (java-null <factory>)
                                              (get-reasoner)
                                              tbox
                                              abox)
                            (create-inf-model (java-null <factory>)
                                              (get-reasoner)
                                              tbox))
                        handler))))

;; Return a procedure which will handle output of results.
;; The returned procedure takes two arguments, a QUERY-RESULT
;; (ResultSet or other) and a QUERY-TYPE symbol, which is one of those
;; returned by DETERMINE-QUERY-TYPE.  The results should be written to the
;; stream returned by the procedure GET-OUTPUT-STREAM, which should not be
;; called before it is required.
;;
;; Results should have the MIME type application/xml.
;; See <http://www.w3.org/2001/sw/DataAccess/prot26>, which refers to schema
;; spec at <http://www.w3.org/TR/rdf-sparql-XMLres/>
(define (make-result-set-handler mime-types
                                 caller-name
                                 set-response-content-type
                                 get-output-stream)
  (define-generic-java-methods
    out
    (output-as-xml |outputAsXML|)
    write
    println)
  (define-java-classes
    <java.io.print-stream>
    (<result-set-formatter> |com.hp.hpl.jena.query.ResultSetFormatter|))
  (lambda (query-result query-type)
    ;; Returns the first string in POSSIBILITIES which is one of the strings
    ;; in WANT, or #f if there are none.  If one of the entries in POSSIBILITIES
    ;; is */*, return the first element of FIND-IN-LIST.
    (define (find-in-list want possibilities)
      (cond ((null? possibilities)
             #f)
            ((string=? (car possibilities) "*/*")
             (car want))
            ((member (car possibilities) want)
             (car possibilities))       ;success!
            (else
             (find-in-list want (cdr possibilities)))))

    (case query-type
      ((select)
       (let ((best-type (find-in-list '("application/xml" "text/plain")
                                      mime-types)))
         (if best-type
             (set-response-content-type best-type))
         (cond ((string=? best-type "application/xml")
                (output-as-xml (java-null <result-set-formatter>)
                               (get-output-stream)
                               query-result))
               ((string=? best-type "text/plain")
                (out (java-null <result-set-formatter>)
                     (get-output-stream)
                     query-result))
               (else
                (error-with-status caller-name
                                   '|SC_NOT_ACCEPTABLE|
                                   "can't handle any of the MIME types ~a"
                                   mime-types)))))

      ((ask)
       (let ((best-type (find-in-list '("application/xml" "text/plain")
                                      mime-types)))
         (if best-type
             (set-response-content-type best-type))
         (cond ((string=? best-type "application/xml")
                (output-as-xml (java-null <result-set-formatter>)
                               (get-output-stream)
                               query-result))
               ((string=? best-type "text/plain")
                (println (java-new <java.io.print-stream> (get-output-stream))
                         (->jstring (if (->boolean query-result) "yes" "no"))))
               (else
                (error-with-status caller-name
                                   '|SC_NOT_ACCEPTABLE|
                                   "can't handle any of the MIME types ~a"
                                   mime-types)))))

      ((construct describe)
       ;; QUERY-RESULT is a Model 
       (let ((best-type (find-in-list (rdf:mime-type-list) mime-types)))
         (if best-type
             (let ((lang (rdf:mime-type->language best-type)))
               (set-response-content-type best-type)
               (write query-result
                      (get-output-stream)
                      (->jstring lang)))
             (error-with-status caller-name
                                '|SC_NOT_ACCEPTABLE|
                                "can't handle any of the MIME types ~a"
                                mime-types))))

      (else
       (error caller-name "Unrecognised query type ~a" query-type)))))

;; Given a SPARQL query as a jstring, and an inferencing model, run
;; the query and pass the results to procedure RESULT-SET-HANDLER.
(define (run-sparql-query query-jstring
                          infmodel
                          result-set-handler)
  (define-java-classes
    (<query-factory> |com.hp.hpl.jena.query.QueryFactory|)
    (<query-execution-factory> |com.hp.hpl.jena.query.QueryExecutionFactory|))
  (define-generic-java-methods
    create
    close)
  (if (or (java-null? query-jstring)    ;these shouldn't happen
          (java-null? infmodel)
          (java-null? result-set-handler))
      (error 'run-sparql-query "received null arguments!"))

  (let ((query (create (java-null <query-factory>) query-jstring)))
    (if (java-null? query)              ;ooops
        (error 'run-sparql-query "created query is null"))
    (let ((qexec (create (java-null <query-execution-factory>)
                         query
                         infmodel))
          (exec-it (find-query-executor query)))
      (if (java-null? qexec)
          (error 'run-sparql-query "Ooops: couldn't create executable query"))
      (if exec-it
          (let ((query-result (exec-it qexec)))
            (result-set-handler query-result (determine-query-type query))
            (close qexec))
          (begin (or (java-null? qexec)
                     (close qexec))
                 (error 'run-sparql-query ;is this message describing the only cause?
                        "Query <~a>... unrecognised query type: query-jstring=~a query=~a  qexec=~a"
                        (->string query-jstring)
                        query-jstring query qexec)
                 )))))

;; Return a new Reasoner object
(define (get-reasoner)

  (define (get-dig-reasoner)
    (define-java-classes
      ;;(<registry> |com.hp.hpl.jena.reasoner.ReasonerRegistry|)
      <com.hp.hpl.jena.reasoner.reasoner-registry>
      <com.hp.hpl.jena.rdf.model.model-factory>
      ;;(<factory> |com.hp.hpl.jena.reasoner.dig.DIGReasonerFactory|)
      ;;<com.hp.hpl.jena.rdf.model.resource>
      )
    (define-generic-java-methods
      the-registry
      (create-with-owl-axioms |createWithOWLAxioms|)
      get-factory
      create-resource
      create-default-model
      add-property)

    (let* ((config-model (create-default-model
                          (java-null <com.hp.hpl.jena.rdf.model.model-factory>)))
           (conf (create-resource config-model)))
      (add-property conf
                    (java-retrieve-static-object
                     '|com.hp.hpl.jena.vocabulary.ReasonerVocabulary.EXT_REASONER_URL|)
                    (create-resource config-model
                                     (->jstring (dig-uri))))
      ;(chatter "Connecting to DIG reasoner at ~a" (dig-uri))
      (create-with-owl-axioms
       (get-factory
        (the-registry (java-null <com.hp.hpl.jena.reasoner.reasoner-registry>))
        (java-retrieve-static-object
         '|com.hp.hpl.jena.reasoner.dig.DIGReasonerFactory.URI|))
       conf)))

  (define (get-owl-reasoner)
    (define-java-classes
      (<registry> |com.hp.hpl.jena.reasoner.ReasonerRegistry|))
    (define-generic-java-methods
      (get-owl-reasoner |getOWLReasoner|))
    ;(chatter "Creating OWL reasoner")
    (get-owl-reasoner (java-null <registry>)))

  (define (get-rdfs-reasoner)
    (define-java-class
      <com.hp.hpl.jena.reasoner.reasoner-registry>)
    (define-generic-java-methods
      (get-rdfs-reasoner |getRDFSReasoner|))
    ;(chatter "Creating RDFS reasoner")
    (get-rdfs-reasoner (java-null <com.hp.hpl.jena.reasoner.reasoner-registry>)))

  (get-owl-reasoner))

;; Given a QUERY, return one of the set QueryExecution.execSelect,
;; QueryExecution.execAsk, ..., based on the type of query returned by
;; Query.getQueryType.  If none of them match for some reason, return #f.
(define find-query-executor
  (let ((alist #f))
    (lambda (query)
      (if (not alist)
          (let ()
            (define-generic-java-methods
              exec-ask
              exec-construct
              exec-describe
              exec-select)
            (set! alist
                  `((select    . ,exec-select)
                    (ask       . ,exec-ask)
                    (construct . ,exec-construct)
                    (describe  . ,exec-describe)))))
      (let ((result-pair (assq (determine-query-type query) alist)))
        (and result-pair (cdr result-pair))))))

;; Given a query, return one of the symbols 'ask, 'select, 'construct or
;; 'describe depending on what type of query it is.
;; Return #f if none match (which shouldn't happen).
(define determine-query-type
  (let ((alist #f))
    (lambda (query)
      (define-generic-java-method get-query-type)
      (if (not alist)
          (let ()
            (define-generic-java-field-accessors
              (query-ask |QueryTypeAsk|)
              (query-construct |QueryTypeConstruct|)
              (query-describe |QueryTypeDescribe|)
              (query-select |QueryTypeSelect|))
            (set! alist
                  `((,(->number (query-select    query)) . select)
                    (,(->number (query-ask       query)) . ask)
                    (,(->number (query-construct query)) . construct)
                    (,(->number (query-describe  query)) . describe)))))
      (let ((result-pair (assv (->number (get-query-type query))
                               alist)))
        (and result-pair (cdr result-pair))))))


;; End SPARQL stuff
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
)
