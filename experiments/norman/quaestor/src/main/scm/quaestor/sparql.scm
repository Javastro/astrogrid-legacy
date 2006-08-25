;; SISC library module for Quaestor
;; Support for SPARQL queries

(import s2j)

(require-library 'quaestor/utils)
(require-library 'quaestor/knowledgebase)
(require-library 'quaestor/jena)

(module sparql
(;sparql:perform-query
 sparql:make-query-runner)

(import* utils
         report-exception
         chatter
         jlist->list
         is-java-type?)
(import* knowledgebase
         kb:knowledgebase?)
(import* jena
         rdf:mime-type-list
         rdf:mime-type->language)


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

;; sparql:make-query-runner knowledgebase string-or-jstring list-of-strings -> procedure
;;
;; Given a knowledgebase KB, a SPARQL QUERY, and a MIME-TYPE-LIST
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
(define (sparql:make-query-runner kb
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

  (let ((infmodel (kb 'get-inferencing-model))
        (query-jstring (cond ((and (is-java-type? query '|java.lang.String|)
                                   (not (java-null? query)))
                              query)
                             ((string? query)
                              (->jstring query))
                             (else
                              (error 'sparql:make-query-runner
                                     "bad call: got query ~s, not string"
                                     query)))))
    (check infmodel
           "failed to get inferencing model from knowledgebase ~a"
           (kb 'get-name))

    (let ((query (with/fc
                     (lambda (m e)
                       ;; (print-exception (make-exception m e)) seems not to
                       ;; work here.  No matter: (error-message m) is the
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
                                      infmodel))
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
     (let ((handlers `(("application/xml" .
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
     (let ((handlers `(("application/xml" .
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

  (let ((result-vars (jlist->list (get-result-vars result)))
        (pw (java-new <java.io.print-writer>
                      (java-new <java.io.output-stream-writer>
                                stream
                                (->jstring "UTF-8")))))
    (formatter result-vars pw)
    (let loop ()
      (if (->boolean (has-next result))
          (let ((qs (next-solution result)))
            (formatter (map (lambda (var)
                              (to-string (get qs var)))
                            result-vars)
                       pw)
            (loop))))
    (flush pw))) ; flush is necessary

;; find-query-executor java-string -> java-method
;;
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
