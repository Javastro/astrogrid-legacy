;; SISC library module for Quaestor
;; Support for SPARQL queries

(import s2j)

(require-library 'quaestor/utils)
(require-library 'quaestor/knowledgebase)
(require-library 'quaestor/jena)

(module sparql
(sparql:make-query-runner)              ;the sole export from this module

(import* utils
         jobject->list
         is-java-type?)
(import* knowledgebase
         kb:knowledgebase?
         kb:get)
(import* jena
         rdf:mime-type-list
         rdf:mime-type->language
         rdf:select-statements)

;; a few predicates for contracts
(define (jstring? x)
  (define-java-class <java.lang.string>)
  (is-java-type? x <java.lang.string>))
(define (jena-model? x)
  (define-java-class <com.hp.hpl.jena.rdf.model.model>)
  (is-java-type? x <com.hp.hpl.jena.rdf.model.model>))
(define (java-query? x)
  (define-java-class <com.hp.hpl.jena.query.query>)
  (is-java-type? x <com.hp.hpl.jena.query.query>))
(define (andf l)                        ; (and...) as a function
  (cond ((null? l))
        ((car l)
         (andf (cdr l)))
        (else #f)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; SPARQL stuff

;; Following doesn't work, for reasons that I don't completely follow.  See 
;; http://sourceforge.net/mailarchive/forum.php?forum_id=7422&max_rows=25&style=nested&viewmonth=200412
;; for discussion and a workaround I definitely don't follow.
;; (define-syntax check-set-name!
;;   (syntax-rules ()
;;     ((_ procname)
;;      (define :check-procedure-name (quote procname)))))
(define-syntax check
  (syntax-rules ()
    ((_ test message ...)
     (if (not test)
         (error 'sparql:make-query-runner message ...)))))

;; Create an implementation of the ResultSet interface, which takes
;; a list of ResultSet instances, and returns results from each of
;; them in turn until all are exhausted.
;;
;; As well, this can be invoked with a null list of result-sets,
;; in which case it functions correctly as a (dummy?) result-set
;; with no results.
(define-java-classes <com.hp.hpl.jena.query.result-set>)

(define-java-proxy (multi-result-set result-sets)
  (<com.hp.hpl.jena.query.result-set>)
  (define (get-result-vars obj)
    (define-generic-java-method get-result-vars)
    (define-java-class <java.lang.string>)
    (if (null? result-sets)
        (java-array-new <java.lang.string> 0)
        (get-result-vars (car result-sets))))
  (define (get-row-number obj)
    ;; This doesn't respect multiplicity of result sets
    ;; ie, it resets the row number when starting on a new resultset
    (define-generic-java-method get-row-number)
    (if (null? result-sets)
        (->jint 0)
        (get-row-number (car result-sets))))
  (define (has-next obj)
    ;; return true if the hasNext method of the car of the result-sets returns true;
    ;; if it doesn't, then try the succeeding elements of the list until one does,
    ;; or we run out of list
    (define-generic-java-method has-next)
    (if (null? result-sets)
        (->jboolean #f)
        (let loop ((current-has-next (has-next (car result-sets))))
          (if (->boolean current-has-next) ;it does
              current-has-next
              (let ((new-result-sets (cdr result-sets)))
                (set! result-sets new-result-sets)
                (if (null? new-result-sets) ; no more results available from anywhere
                    (->jboolean #f)
                    (loop (has-next (car new-result-sets)))))))))
  (define (distinct? obj)
    ;; All the results should be generated in response to the same query, so all should have
    ;; the same return for this method.
    (define-generic-java-method distinct?)
    (if (null? result-sets)
        (->jboolean #t)
        (distinct? (car result-sets))))
  (define (ordered? obj)
    ;; as with DISTINCT?
    (define-generic-java-method ordered?)
    (if (null? result-sets)
        (->jboolean #t)
        (ordered? (car result-sets))))
  (define (next obj)
    (define-generic-java-method next)
    (define-java-class <java.util.no-such-element-exception>)
    (if (null? result-sets)
        (error (java-new <java.util.no-such-element-exception>))
        (next (car result-sets))))
  (define (next-solution obj)
    (define-generic-java-method next-solution)
    (define-java-class <java.util.no-such-element-exception>)
    (if (null? result-sets)
        (error (java-new <java.util.no-such-element-exception>))
        (next-solution (car result-sets))))
  (define (remove obj)
    (define-java-class <java.util.unsupported-operation-exception>)
    (error (java-new <java.util.unsupported-operation-exception>
                     (->jstring "multi-result-set: Method 'remove' called unexpectedly")))))

;; PARSE-SPARQL-QUERY : string-or-jstring -> <Query>
(define (parse-sparql-query query-string)
  (define-java-classes
    (<query-factory> |com.hp.hpl.jena.query.QueryFactory|))
  (define-generic-java-methods
    create get-message)
  (let ((query-jstring (cond ((and (is-java-type? query-string '|java.lang.String|)
                                   (not (java-null? query-string)))
                              query-string)
                             ((string? query-string)
                              (->jstring query-string))
                             (else
                              (error 'sparql:make-query-runner
                                     "bad call: got query ~s, not string"
                                     query-string)))))
    (with/fc
     (lambda (m e)
       ;; (error-message m) is the exception.
       (report-exception 'sparql:make-query-runner
                         '|SC_BAD_REQUEST|
                         "SPARQL parse error: ~a" (->string (get-message (error-message m)))))
     (lambda ()
       (let ((q (create (java-null <query-factory>) query-jstring)))
         (check q "can't happen: null query in sparql:make-query-runner")
         q)))))

;; FIND-DELEGATES : knowledgebase -> listof knowledgebase-or-URIstring
;; Return the KB given as argument, at the head of a list of the KBs it delegates to
;; (ie, without delegation, this returns simply (k).
;; The return values in the list are knowledgebases if the knowledgebase is in this quaestor,
;; or a jstring containing a URI, if the delegated-to knowledgebase is at a different SPARQL endpoint
(define/contract (find-delegates (k kb:knowledgebase?) -> list?)
  (define-generic-java-methods
    (get-uri |getURI|))
  (define (literal->string literal)
    (define-generic-java-methods to-string)
    (->string (to-string literal)))
  (chatter "finding delegates of KB ~a" (if (procedure? k) (k 'get-name-as-uri) k))
  (cons k
        (let ((kb-uris (map get-uri (rdf:select-statements (k 'get-metadata)
                                                           (k 'get-name-as-resource)
                                                           "http://ns.eurovotech.org/quaestor#delegatesTo"
                                                           #f)))
              (external? (member "externaldelegation"
                                (map literal->string
                                     (rdf:select-statements (k 'get-metadata)
                                                            (k 'get-name-as-resource)
                                                            "http://ns.eurovotech.org/quaestor#debug"
                                                            #f)))))
          (chatter "...KB ~s apparently delegates to ~s~a" (if (procedure? k) (k 'get-name-as-uri) k) kb-uris (if external? " (EXTERNALDELEGATION)" ""))
          (apply append
                 (map (lambda (kb-uri)
                        (cond (external? ;forced external for debugging/unit-testing
                               (list kb-uri))
                              ((kb:get (java-new <uri> kb-uri))
                               => ;; this resource names a KB in this quaestor, so query that model directly
                               find-delegates)
                              (else ;; remote KB?, so evaluate to the string URI
                               (list kb-uri))))
                      kb-uris)))))

;; MAKE-EXECUTABLE-QUERY : model-or-url <com.hp.hpl.jena.query.Query> -> <...QueryExecution>
(define (make-executable-query x query)
  (define-java-classes
    (<query-execution-factory> |com.hp.hpl.jena.query.QueryExecutionFactory|))
  (define-generic-java-methods create sparql-service)
  (let ((qef (java-null <query-execution-factory>)))
    (cond ((jena-model? x)
           (create qef query x))
;;           ((kb:knowledgebase? kb)
;;            (create qef query (kb 'get-query-model)))
          ((jstring? x)
           ;; otherwise assume it names a SPARQL endpoint
           ;; (FIXME: the following might throw MalformedURL exceptions,
           ;; either because it is indeed malformed, or because the protocol is
           ;; unknown (for example, 'urn:'); we should trap these, and also make
           ;; ourselves more robust against the URL being simply wrong/dead, though
           ;; we won't find that out until the query is actually performed, later).
           ;;
           ;; The returned object is a
           ;; com.hp.hpl.jena.query.engineHTTP.QueryEngineHTTP object.  It does not
           ;; appear possible to control the type of query being made, but it seems
           ;; from the ARQ source, and its behaviour, that it constructs a
           ;; 'GET foo?query=<query>' interaction, and that it switches to a POST
           ;; of the query if the GET URL would be too long.
           (sparql-service qef x query))
          (else
           (error 'sparql-make-query-runner
                  "can't happen: expected model or jstring, but found ~s" x)))))

;; sparql:make-query-runner knowledgebase string-or-jstring list-of-strings -> procedure
;;
;; Given a knowledgebase KB, a SPARQL QUERY, and a (non-null) MIME-TYPE-LIST
;; of acceptable MIME types, return a procedure which has the signature
;;
;;     (query-runner output-stream content-type-setter)
;;
;; where CONTENT-TYPE-SETTER is a procedure which takes a mime-type and
;; sets that as the content-type of the given OUTPUT-STREAM.  This returned
;; procedure is all side-effect, and returns #f.
;;
;; All parsing and verification should be done before the procedure is returned,
;; so that when the procedure is finally called, it should run
;; successfully, barring unforseen changes in the environment.
;;
;; The procedure will either succeed or throw an error.
(define/contract (sparql:make-query-runner (kb (or (kb:knowledgebase? kb) (jena-model? kb)))
                                           (query (or (string? query)
                                                      (jstring? query)))
                                           (mime-type-list list?)
                                           -> procedure?)
  (with/fc (lambda (m error-continuation)
             (if (pair? (error-message m))
                 (throw m) ;it came from report-exception; just pass it on
                 (report-exception 'sparql:make-query-runner
                                   '|SC_INTERNAL_SERVER_ERROR|
                                   "Something bad happened in sparql:make-query-runner: ~a"
                                   (format-error-record m))))
    (lambda ()
      (cond ((null? mime-type-list)
             (error "sparql:make-query-runner called with no MIME types"))
            ((kb:knowledgebase? kb)
             (let ((query-model-list (map (lambda (one-kb)
                                            (cond ((jstring? one-kb)    one-kb)
                                                  ((procedure? one-kb) (one-kb 'get-query-model))
                                                  (else (error "one-kb=~s (this shouldn't happen)" one-kb))))
                                          (find-delegates kb))))
               (if (andf query-model-list)
                   (sparql:make-query-runner* query-model-list query mime-type-list)
                   (report-exception 'sparql:make-query-runner '|SC_BAD_REQUEST| "I wasn't able to find a "))))
            (else
             (sparql:make-query-runner* (list kb) query mime-type-list))))))

;; worker procedure for sparql:make-query-runner
;; The elements of the QUERY-MODEL-LIST list are models if the model is held in this quaestor,
;; or a jstring if it is at a remote SPARQL endpoint.
;; The QUERY is the SPARQL query.
;; The MIME-TYPE-LIST is a non-null list of MIME types from the Accept header.
(define/contract (sparql:make-query-runner* (query-model-list (and (list? query-model-list)
                                                                   (andf (map (lambda (x)
                                                                                      (or (jena-model? x)
                                                                                          (jstring? x))) ;url
                                                                                    query-model-list))))
                                            (query (or (string? query) (jstring? query)))
                                            (mime-type-list (and (list? mime-type-list)
                                                                 (not (null? mime-type-list)))))
  (define-generic-java-methods close to-string)

  (let ((parsed-query (parse-sparql-query query)))

    ;; For each of the query models extracted above (in the simplest case, there will be only one,
    ;; but there will be multiple ones if there is delegation), create an executable query
    (let ((executable-queries        ;a list of QueryExecution objects
           (map (lambda (one-model)
                  (make-executable-query one-model parsed-query))
                query-model-list))
          (query-executor (find-query-executor parsed-query))
          (handler (make-result-set-handler 'sparql:make-query-runner
                                            mime-type-list
                                            (determine-query-type parsed-query))))

      ;; slightly paranoid: check everything looks as it should
      (check (and (not (null? executable-queries))
                  (let loop ((l executable-queries))
                    (if (null? l)
                        #t
                        (and (not (java-null? (car l)))
                             (loop (cdr l))))))
             "error creating ~a executable queries from query ~a"
             (length executable-queries) (->string query-jstring))

      ;; check we have a query-executor and a non-null list of queries
      (if (not (and query-executor (not (null? executable-queries))))
          (begin (map close executable-queries)
                 (error 'sparql:make-query-runner
                        "can't find a way of executing query ~a"
                        (->string query-jstring))))
      ;; OK, we're fairly sure everything's OK

      (chatter "Produced list of queries: ~s" executable-queries)

      ;; Return the function which will run all of these executable queries (in principle in parallel)
      ;; and print out a result set consisting of the union of the result-sets returned.
      ;; This doesn't attempt to deal with the case where the same result appears from multiple
      ;; queries -- perhaps it should.
      (lambda (output-stream content-type-setter)
        (if (= (length executable-queries) 1)
            (handler (query-executor (car executable-queries))
                     output-stream
                     content-type-setter)
            (handler (multi-result-set (map query-executor executable-queries))
                     output-stream
                     content-type-setter))
        (map close executable-queries)
        #f))))

;; The following is the simple case which doesn't handle delegation.
;; Perhaps it's useful as documentation...?
(define (sparql:make-query-runner-NO-DELEGATION kb
                                  query
                                  mime-type-list)
  (define-java-classes
    (<query-factory> |com.hp.hpl.jena.query.QueryFactory|)
    (<query-execution-factory> |com.hp.hpl.jena.query.QueryExecutionFactory|))
  (define-generic-java-methods
    create
    close)

  (check kb
         "cannot query null knowledgebase")
  (check (kb:knowledgebase? kb)
         "kb argument is not a knowledgebase!")
  (check (not (null? mime-type-list))
         "received null mime-type-list")

  (let ((query-model (kb 'get-query-model))
        (query-jstring (cond ((and (is-java-type? query '|java.lang.String|)
                                   (not (java-null? query)))
                              query)
                             ((string? query)
                              (->jstring query))
                             (else
                              (error 'sparql:make-query-runner
                                     "bad call: got query ~s, not string"
                                     query)))))
    (check query-model
           "failed to get query model from knowledgebase ~a"
           (kb 'get-name-as-uri))

    (let ((query (with/fc
                     (lambda (m e)
                       ;; (print-exception (make-exception m e)) seems not to
                       ;; work here.  Don't know why.  No matter: (error-message m) is the
                       ;; exception.
                       (define-generic-java-method get-message)
                       (report-exception 'sparql:make-query-runner
                                          '|SC_BAD_REQUEST|
                                          "SPARQL parse error: ~a"
                                          (->string
                                           (get-message (error-message m)))))
                   (lambda ()
                     (create (java-null <query-factory>) query-jstring)))))
      (or query
          (error "can't happen: null query in sparql:make-query-runner"))
      (let ((executable-query (create (java-null <query-execution-factory>)
                                      query
                                      query-model))
            (query-executor (find-query-executor query))
            (handler (make-result-set-handler 'sparql:make-query-runner
                                              mime-type-list
                                              (determine-query-type query))))
        (check (not (java-null? executable-query))
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
            (close executable-query)
            #f))))))

;; make-result-set-handler symbol list symbol -> procedure
;; error: if none of the listed MIME types can be handled
;;
;; Return a procedure which will handle output of query results.
;; Given:  CALLER-NAME: a symbol giving the location errors should
;              be reported as coming from,
;;         MIME-TYPES: a list of acceptable MIME-types,
;;         QUERY-TYPE: one of the four symbols returned by DETERMINE-QUERY-TYPE,
;; Return: a procedure.
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
    (chatter "find-in-list: want=~s poss=~s" want possibilities)
    (cond ((null? possibilities)
           #f)
          ((string=? (car possibilities) "*/*")
           (car want))
          ((member (car possibilities) want)
           (car possibilities))         ;success!
          (else
           (find-in-list want (cdr possibilities)))))

  (chatter "make-result-set-handler: caller-name=~s mime-types=~s query-type=~s"
           caller-name mime-types query-type)

  (case query-type
    ((select)
     (let ((handlers `(("application/sparql-results+xml" . ;see http://www.w3.org/TR/rdf-sparql-XMLres/#mime
                        ,(lambda (result stream set-type)
                           (set-type "application/sparql-results+xml")
                           (output-as-xml (java-null <result-set-formatter>)
                                          stream
                                          result)))
                       ("application/xml" .
                        ,(lambda (result stream set-type)
                           (set-type "application/xml")
                           (output-as-xml (java-null <result-set-formatter>)
                                          stream
                                          result)))
                       ("text/plain" .
                        ,(lambda (result stream set-type)
                           (set-type "text/plain")
                           (out (java-null <result-set-formatter>)
                                stream result)))
                       ("text/csv" .
                        ,(lambda (result stream set-type)
                           (set-type "text/csv;header=present")
                           (output-as-csv stream result)))
                       ("text/tab-separated-values" .
                        ,(lambda (result stream set-type)
                           ;; we include the line of headers, but TSV
                           ;; doesn't specify the header=present parameter
                           (set-type "text/tab-separated-values")
                           (output-as-tab-separated-values stream result))))))
       (let ((best-type (find-in-list (map car handlers)
                                      mime-types)))
         (cond ((not best-type)
                (report-exception caller-name
                                  '|SC_NOT_ACCEPTABLE|
                                  "can't handle any of the MIME types ~a for SELECT queries (only ~a)"
                                  mime-types (map car handlers)))
               ((assoc best-type handlers)
                => (lambda (p)
                     (cdr p)))
               (else
                ;; this shouldn't happen
                (report-exception caller-name
                                  '|SC_INTERNAL_SERVER_ERROR|
                                  "this can't happen: mime-types ~s -> unrecognised best-type=~s"
                                  mime-types best-type))))))

    ((ask)
     (let ((handlers `(("application/sparql-results+xml" . ;see http://www.w3.org/TR/rdf-sparql-XMLres/#mime
                        ,(lambda (result stream set-type)
                           (set-type "application/sparql-results+xml")
                           (output-as-xml (java-null <result-set-formatter>)
                                          stream
                                          result)))
                       ("application/xml" .
                        ,(lambda (result stream set-type)
                           (set-type "application/xml")
                           (output-as-xml (java-null <result-set-formatter>)
                                          stream
                                          result)))
                       ("text/plain" .
                        ,(lambda (result stream set-type)
                           (set-type "text/plain")
                           (println (java-new <java.io.print-stream> stream)
                                    (->jstring (if (->boolean result)
                                                   "yes" "no"))))))))
       (let ((best-type (find-in-list (map car handlers)
                                      mime-types)))
         (cond ((not best-type)
                (report-exception caller-name
                                  '|SC_NOT_ACCEPTABLE|
                                  "can't handle any of the MIME types ~a for ASK queries (only ~a)"
                                  mime-types (map car handlers)))
               ((assoc best-type handlers)
                => (lambda (p)
                     (cdr p)))
               (else
                ;; this shouldn't happen
                (report-exception caller-name
                                  '|SC_INTERNAL_SERVER_ERROR|
                                  "this can't happen: mime-types ~s -> unrecognised best-type=~s"
                                  mime-types best-type))))))

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
             (report-exception caller-name
                                '|SC_NOT_ACCEPTABLE|
                                "can't handle any of the MIME types ~a for ~a queries"
                                mime-types query-type))))

      (else
       (error caller-name "Unrecognised query type ~a" query-type))))

;; output-as-csv java-stream java-resultset -> void
;; side-effect: write resultset to stream
;;
;; Given a ResultSet and an output stream, write the result as CSV, following
;; the spec in RFC 4180.  Include a header.
(define (output-as-csv stream result)
  (define (write-list-with-commas print-list pw)
    (define-generic-java-methods print)
    (let ((jcomma (->jstring ","))
          (jcrlf (->jstring (list->string (list (integer->char 13)
                                                (integer->char 10))))))
      (if (null? print-list)
          (error 'output-as-csv "asked to output null list"))
      (let loop ((l print-list))
        (print pw (car l))
        (let ((next (cdr l)))
          (if (null? next)
              (print pw jcrlf)
              (begin (print pw jcomma)
                     (loop next)))))))
  (output-with-formatter stream result write-list-with-commas))

;; The format of TSV files is specified rather informally at
;; <http://www.iana.org/assignments/media-types/text/tab-separated-values>.
;; The MIME type is "text/tab-separated-values".
(define (output-as-tab-separated-values stream result)
  (define (tsv-output print-list pw)
    (define-generic-java-methods print println)
    (let ((jtab (->jstring (list->string (list (integer->char 9))))))
      (if (null? print-list)
          (error 'output-as-tab-separated-values "can't output null list"))
      (let loop ((l print-list))
        (print pw (car l))
        (let ((next (cdr l)))
          (if (null? next)
              (println pw)
              (begin (print pw jtab)
                     (loop next)))))))
  (output-with-formatter stream result tsv-output))

;; OUTPUT-WITH-FORMATTER java-stream java-result-set procedure -> void
;; side-effect: write the given result-set to the stream
;;
;; The formatter is a procedure with the prototype
;;     (formatter output-list printwriter)
;; The output-list is a list of Java objects which are to be sent to the
;; given PrintStream.
(define (output-with-formatter stream result formatter)
  (define-generic-java-methods
    get-result-vars
    has-next
    next-solution
    get
    to-string
    flush)
  (define-java-classes
    <java.io.print-writer>
    <java.io.output-stream-writer>)

  (let ((result-vars (jobject->list (get-result-vars result)))
        (pw (java-new <java.io.print-writer>
                      (java-new <java.io.output-stream-writer>
                                stream
                                (->jstring "UTF-8"))))
        (empty-string (->jstring "")))
    (formatter result-vars pw)
    (let loop ()
      (if (->boolean (has-next result))
          (let ((qs (next-solution result)))
            (formatter (map (lambda (var)
                              (let ((res (get qs var)))
                                (if (java-null? res)
                                    empty-string
                                    (to-string res))))
                            result-vars)
                       pw)
            (loop))))
    (flush pw))) ; flush is necessary

;; FIND-QUERY-EXECUTOR : <Query> -> procedure-or-false
;;
;; Given a PARSED-QUERY, return one of the set QueryExecution.execSelect,
;; QueryExecution.execAsk, ..., based on the type of query returned by
;; Query.getQueryType.  If none of them match for some reason, return #f.
(define find-query-executor
  (let ((alist #f))
    (define-generic-java-methods exec-ask exec-construct exec-describe exec-select)
    (set! alist
          `((select    . ,exec-select)
            (ask       . ,exec-ask)
            (construct . ,exec-construct)
            (describe  . ,exec-describe)))
    (lambda/contract ((parsed-query java-query?)
                      -> (lambda (x) (or (not x) (procedure? x))))
      (let ((result-pair (assq (determine-query-type parsed-query) alist)))
        ;; If we've found a suitable function to execute the query (in the cdr of this pair),
        ;; then return it, wrapped in a FC which means we don't fall over if the (remote?)
        ;; query fails.
        (and result-pair
             (lambda (executable-query)
               (define-generic-java-method to-string)
               (with/fc (lambda (m e)
                          ;; There's been some error executing the query, possibly because a remote
                          ;; service is down, or something like that.  It's not completely clear what's
                          ;; the best thing to do, here, but for now, let's just note the error
                          ;; and return an empty result set.
                          (chatter "query execution failed: ~a" (->string (to-string (error-message m))))
                          (multi-result-set '()))
                 (lambda ()
                   ((cdr result-pair) executable-query)))))))))

;; determine-query-type java-string -> symbol
;;
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


