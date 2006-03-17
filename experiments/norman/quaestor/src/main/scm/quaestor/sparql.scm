;; SISC library module for Quaestor
;; Support for SPARQL queries

(import s2j)

(require-library 'quaestor/utils)

(module sparql
(;sparql:perform-query
 sparql:make-query-runner)

(import* utils error-with-status)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; SPARQL stuff

(define-syntax fail-if-procedure-name
  (syntax-rules ()
    ((_ procname)
     (define :fail-if-procedure-name (quote procname)))))
(define-syntax fail-if
  (syntax-rules ()
    ((_ test message ...)
     (if test
         (error :fail-if-procedure-name message ...)))))

;; Given a knowledgebase KB, a SPARQL query QUERY-JSTRING, and a list
;; of acceptable MIME types, return a procedure which has the signature
;;
;;     (query-runner output-stream content-type-setter)
;;
;; where CONTENT-TYPE-SETTER is a procedure which takes a mime-type and
;; sets that as the content-type of the given OUTPUT-STREAM.
;; All parsing and verification should be done before the procedure is returned,
;; so that when the procedure is finally called, it should run
;; successfully, barring unforseen changes in the environment.
;;
;; The procedure will either succeed or throw an error.
(define (sparql:make-query-runner kb
                                  query-jstring
                                  mime-type-list)
  (define-java-classes
    (<query-factory> |com.hp.hpl.jena.query.QueryFactory|)
    (<query-execution-factory> |com.hp.hpl.jena.query.QueryExecutionFactory|))
  (define-generic-java-methods
    create
    close)

  (fail-if-procedure-name sparql:make-query-runner)

  (fail-if (not kb)
           "cannot query null knowledgebase")
  (fail-if (java-null? query-jstring)
           "received null query")
  (fail-if (null? mime-type-list)
           "received null mime-type-list")

  (let ((infmodel (kb 'get-inferencing-model)))
    (fail-if (not infmodel)
             "failed to get inferencing model from knowledgebase ~a"
             (kb 'get-name))

    (let ((query (create (java-null <query-factory>) query-jstring)))
      (fail-if (java-null? query) "can't parse query")
      (let ((executable-query (create (java-null <query-execution-factory>)
                                      query
                                      infmodel))
            (query-executor (find-query-executor query))
            (handler (make-result-set-handler 'sparql:make-query-runner
                                              mime-type-list
                                              (determine-query-type query))))
        (fail-if (java-null? executable-query)
                 "can't make executable query from ~a" (->string query-jstring))
        (if (not (and query-executor executable-query))
            (begin (or (java-null? executable-query)
                       (close executable-query))
                   (error 'sparql:make-query-runner
                          "can't find a way of executing query ~a"
                          (->string query-jstring))))

        ;; success!
        (lambda (output-stream content-type-setter)
          (let ((query-result (query-executor executable-query)))
            (handler query-result output-stream content-type-setter)
            (close executable-query)))))))

;; Return a procedure which will handle output of query results.
;; Given: CALLER-NAME: a symbol giving the location errors should be reported as,
;;        MIME-TYPES: a list of acceptable MIME-types
;;        QUERY-TYPE: one of the four symbols returned by DETERMINE-QUERY-TYPE,
;; return a procedure.
;;
;; The returned procedure has the following signature:
;;
;;     (result-set-handler query-result output-stream content-type-setter)
;;
;; The procedure should write the query-result to the given output stream,
;; after first calling the CONTENT-TYPE-SETTER procedure with the appropriate
;; MIME type.
;;
;; When outputting XML, results should have the MIME type application/xml.
;; See <http://www.w3.org/2001/sw/DataAccess/prot26>, which refers to schema
;; spec at <http://www.w3.org/TR/rdf-sparql-XMLres/>
(define (make-result-set-handler caller-name
                                 mime-types
                                 query-type)
  (define-generic-java-methods
    out
    (output-as-xml |outputAsXML|)
    write
    println)
  (define-java-classes
    <java.io.print-stream>
    (<result-set-formatter> |com.hp.hpl.jena.query.ResultSetFormatter|))

  ;; Returns the first string in POSSIBILITIES which is one of the strings
  ;; in WANT, or #f if there are none.  If one of the entries in POSSIBILITIES
  ;; is */*, return the first element of WANT.
  (define (find-in-list want possibilities)
    (cond ((null? possibilities)
           #f)
          ((string=? (car possibilities) "*/*")
           (car want))
          ((member (car possibilities) want)
           (car possibilities))         ;success!
          (else
           (find-in-list want (cdr possibilities)))))

  (case query-type
    ((select)
     (let ((best-type (find-in-list '("application/xml" "text/plain")
                                    mime-types)))

       (cond ((string=? best-type "application/xml")
              (lambda (result stream set-type)
                (set-type "application/xml")
                (output-as-xml (java-null <result-set-formatter>)
                               stream
                               result)))
             ((string=? best-type "text/plain")
              (lambda (result stream set-type)
                (set-type "text/plain")
                (out (java-null <result-set-formatter>) stream result)))
             (else
              (error-with-status caller-name
                                 '|SC_NOT_ACCEPTABLE|
                                 "can't handle any of the MIME types ~a"
                                 mime-types)))))

      ((ask)
       (let ((best-type (find-in-list '("application/xml" "text/plain")
                                      mime-types)))

         (cond ((string=? best-type "application/xml")
                (lambda (result stream set-type)
                  (set-type "application/xml")
                  (output-as-xml (java-null <result-set-formatter>)
                                 stream
                                 result)))
               ((string=? best-type "text/plain")
                (lambda (result stream set-type)
                  (println (java-new <java.io.print-stream> stream)
                           (->jstring (if (->boolean result) "yes" "no")))))
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
               (lambda (result stream set-type)
                 (set-type best-type)
                 (write result
                        stream
                        (->jstring lang))))
             (error-with-status caller-name
                                '|SC_NOT_ACCEPTABLE|
                                "can't handle any of the MIME types ~a"
                                mime-types))))

      (else
       (error caller-name "Unrecognised query type ~a" query-type))))

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
;; (define (xx-sparql:perform-query kb
;;                               query-jstring
;;                               get-output-stream
;;                               mime-type-list
;;                               set-response-content-type)
;;   (define-java-classes
;;     (<factory> |com.hp.hpl.jena.rdf.model.ModelFactory|))
;;   (define-generic-java-methods
;;     create-inf-model)

;;   (let ((handler (make-result-set-handler mime-type-list
;;                                           'perform-sparql-query
;;                                           set-response-content-type
;;                                           get-output-stream)))

;;     (or kb                              ;check we've found a KB
;;         (error 'perform-sparql-query
;;                "Request to perform-sparql-query on null knowledgebase"))
;;     (or handler                         ;check we can handle requested MIME type
;;         (error-with-status 'perform-sparql-query
;;                            '|SC_NOT_ACCEPTABLE|
;;                            "can't handle any of the MIME types ~a"
;;                            mime-type-list))

;;     (let ((tbox (kb 'get-model-tbox))
;;           (abox (kb 'get-model-abox)))
;;       (or tbox
;;           (error 'perform-sparql-query
;;                  "Model ~a has no TBOX -- can't query" (kb 'get-name)))
;;       (run-sparql-query query-jstring
;;                         (if abox
;;                             (create-inf-model (java-null <factory>)
;;                                               (jena:get-reasoner)
;;                                               tbox
;;                                               abox)
;;                             (create-inf-model (java-null <factory>)
;;                                               (jena:get-reasoner)
;;                                               tbox))
;;                         handler))))

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
(define (xx-make-result-set-handler mime-types
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
(define (xx-run-sparql-query query-jstring
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
