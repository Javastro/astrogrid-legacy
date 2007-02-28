;; SISC program to create a Scheme server
;;
;; See http://www.ietf.org/rfc/rfc2616.txt

(import s2j)
(import debugging)                      ;for print-stack-trace
;(emit-annotations #t)                   ;should be default, yes?
;(emit-debugging-symbols #t)

(import string-io)                      ;for string ports

(require-library 'quaestor/utils)
(import utils)
(require-library 'quaestor/knowledgebase)
(import knowledgebase)
(require-library 'quaestor/jena)
(import jena)
(require-library 'quaestor/sparql)
(import sparql)
(require-library 'util/xmlrpc)
(import xmlrpc)
(require-library 'util/sexp-xml)
(import* sexp-xml
         sexp-xml:sexp->xml
         sexp-xml:xml->sexp/reader
         sexp-xml:escape-string-for-xml)
(require-library 'util/lambda-contract)


(define (ident)
  (define-java-class <sisc.util.version>)
  (define-generic-java-field-accessor :version |VERSION|)
  `((quaestor.version . "@VERSION@")
    (sisc.version . ,(->string (:version (java-null <sisc.util.version>))))
    (string
     . "quaestor.scm @VERSION@ ($Revision: 1.41 $ $Date: 2007/02/28 14:58:17 $)")))

;; Predicates for contracts
(define-java-classes
  <java.lang.string>)
(define (request? x)
  (define-java-class <javax.servlet.servlet-request>)
  (is-java-type? x <javax.servlet.servlet-request>))
(define (response? x)
  (define-java-class <javax.servlet.servlet-response>)
  (is-java-type? x <javax.servlet.servlet-response>))
(define (string-or-true? x)
  (or (string? x)
      (and (boolean? x) x)))
(define (string-or-false? x)
  (or (not x)
      (string? x)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; Implementation

;; APPLY-WITH-TOP-FC procedure args... -> object
;; This is intended to be a main entry point for the functions here.
;; Call the given procedure with the given arguments, in the context
;; of a suitable failure-continuation.
;;
;; Since the functions which are called via this typically have their
;; own error handlers, this failure-continuation will generally only
;; be called if something quite bad has gone wrong; therefore the
;; failure-continuation shows debugging information and chatter, and
;; returns a Java excaption.
;; (define (apply-with-top-fc proc . args)
;;   (with/fc
;;       (lambda (m kontinuation)
;;         (define-java-class <javax.servlet.servlet-exception>)
;;         (java-new
;;          <javax.servlet.servlet-exception>
;;          (->jstring
;;           (format #f "Top-level error: ~a~%~a~%~%Stack trace: ~a~%"
;;                   (format-error-record m)
;;                   (let ((c (chatter)))
;;                     (cond ((list? c)    ;normal case
;;                            (apply string-append
;;                                   (map (lambda (x)
;;                                          (format #f "[chatter: ~a]~%" x))
;;                                        c)))
;;                           ((not c)      ;no chatter
;;                            "")
;;                           (else         ;can't happen!
;;                            (format #f
;;                                    "[Can't happen: (chatter) produced ~s]" c))))
;;                   (with-output-to-string
;;                     (lambda () (print-stack-trace kontinuation)))))))
;;     (lambda ()
;;       (chatter "Applying proc ~s..." proc)
;;       (parameterize ((suppressed-stack-trace-source-kinds '()))
;;                     (apply proc args)))))

(define (initialise-quaestor scheme-servlet)
  (define-generic-java-methods
    register-handler
    get-init-parameter)
  (let ((kb (get-init-parameter scheme-servlet (->jstring "kb-context")))
        (xmlrpc (get-init-parameter scheme-servlet (->jstring "xmlrpc-context")))
        (pickup (get-init-parameter scheme-servlet (->jstring "pickup-context"))))
    (define (reg method context proc)
      (register-handler scheme-servlet
                        (->jstring method)
                        context
                        (java-wrap proc)))
    (reg "GET" kb http-get)
    (reg "HEAD" kb http-head)
    (reg "PUT" kb http-put)
    (reg "POST" kb http-post)
    (reg "DELETE" kb http-delete)
    (reg "POST" xmlrpc xmlrpc-handler)
    (reg "GET" pickup pickup-dispatcher)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; GET support

;; Handle a single GET request.  This examines the path-list from the
;; request, and works through a list of handlers.  It may return a
;; string, or #t on success; if it returns a string, it is to be
;; returned as the response by the caller.
(define/contract (http-get (request  request?)
                           (response response?)
                           -> string-or-true?)
  (with-failure-continuation
       (make-fc request response '|SC_INTERNAL_SERVER_ERROR|)
    (lambda ()
      (let ((path-list (request->path-list request))
            (query-string (request->query-string request)))
        (set-response-status! response '|SC_OK|) ;default response
        (let loop ((h get-handlers))
          (if (null? h)
              (error 'get "No applicable GET handlers found!")
              (or ((car h) path-list query-string request response)
                  (loop (cdr h)))))))))

;; GET-HANDLERS is a list of procedures which take four arguments:
;;     a list of strings representing the path-info
;;     a query-string
;;     a HTTP Request
;;     a HTTP Response
;; Each procedure should decide whether it can handle this query, and if it
;; does, it should do so and return either a string or #t; otherwise
;; it should do nothing and return #f.  If the procedure returns a
;; string, then that should be printed out as the response.

;; Fallback get handler.  Matches everything, and returns a string.
(define (get-fallback path-info-list query-string request response)
  (define-generic-java-method
    set-content-type)
  (set-content-type response (->jstring "text/html"))
  (response-page request response
                 "Quaestor"
                 `((p "I don't recognise that URL.")
                   (p "The details of the request follow:")
                   ,@(tabulate-request-information request))))

;; Display an HTML page reporting on the knowledgebases available
(define (get-knowledgebase-list path-info-list query-string request response)
  (define (display-kb-info kb-name)
    (define-generic-java-methods
      (get-request-uri |getRequestURI|))
    (define (model-to-string model)
      (define-generic-java-methods write to-string)
      (define-java-class <java.io.string-writer>)
      (let ((sw (java-new <java.io.string-writer>)))
        (write model sw (->jstring "N3"))
        (->string (to-string sw))))

    (let* ((kb (kb:get kb-name))
           (info (kb 'info))
           (base-uri (webapp-base-from-request request #t))
           (submodel-pair (assq 'submodels info))) ;cdr is list of alists
      `(li "Knowledgebase "
           (a (@ (href ,(format #f "~a/~a" base-uri kb-name)))
              (strong ,kb-name))
           ", submodels:"
           (ul ,@(map (lambda (sm-alist)
                        (let ((name-pair (assq 'name sm-alist))
                              (tbox-pair (assq 'tbox sm-alist))
                              (namespaces (assq 'namespaces sm-alist)))
                          `(li
                            (p
                             (a (@ (href ,(format #f
                                                  "~a/~a/~a"
                                                  base-uri
                                                  kb-name
                                                  (cdr name-pair))))
                                ,(cdr name-pair))
                             ,(format #f " (~a)"
                                      (cond ((not tbox-pair)
                                             "???")
                                            ((cdr tbox-pair)
                                             "tbox")
                                            (else
                                             "abox"))))
                            (p "Namespaces")
                            (table
                             (tr (th "prefix") (th "namespace"))
                             ,@(map (lambda (ns)
                                      `(tr (td (@ (style "text-align: right"))
                                               ,(string-append (car ns) ":"))
                                           (td ,(cdr ns))))
                                   (cdr namespaces))))))
                      (cdr submodel-pair)))
           (p "Knowledgebase metadata...")
           (pre
            ,(sexp-xml:escape-string-for-xml
              (model-to-string (kb 'get-metadata))))
           ;; Is this the best thing?  Perhaps better would be to iterate
           ;; through the predicates on the model and list them in some
           ;; semi-readable fashion.
           )))
  (if (= (length path-info-list) 0)     ;we handle this one
      (let ((namelist (kb:get-names)))
        (set-response-status! response '|SC_OK| "text/html")
        (response-page request response
                       "Quaestor: list of knowledgebases"
                       (if (null? namelist)
                           `((p "No knowledgebases available"))

                           `((p "Knowledgebases available:")
                             (ul ,@(map (lambda (kb-name)
                                          (display-kb-info kb-name))
                                        (kb:get-names)))))))
      #f))

;; If path-info-list has one element, and the query-string starts with "sparql",
;; then the query is a URL-encoded SPARQL query to make of the model
;; named in (car path-info-list).
(define (get-model-query path-info-list query-string request response)
  (define (sparql-encoded-query model-name encoded-query)
    (with/fc
        (make-fc request response '|SC_BAD_REQUEST|)
      (lambda ()
        (define-generic-java-methods
          set-content-type
          get-output-stream)
        (let ((model (kb:get model-name)))
          (or model
              (error 'get-model-query
                     "unknown knowledgebase ~a" model-name))
          (let ((runner
                 (sparql:make-query-runner
                  model
                  (url-decode-to-jstring encoded-query)
                  (request->accept-mime-types request))))
            (runner
             (response->lazy-output-stream response)
             (lambda (mimetype)
               (set-content-type response
                                 (->jstring mimetype))))
            #t)))))
  (let ((qp (parse-query-string query-string)))
    (and (= (length path-info-list) 1)
         (car qp)
         (string=? (car qp) "sparql")
         ;; the world is calling...
         (if (cdr qp)
             (sparql-encoded-query (car path-info-list)
                                   (cdr qp))
             (no-can-do request response
                        '|SC_BAD_REQUEST|
                        "found empty SPARQL query in GET request")))))

;; Retrieve the submodel named by the two-element path-info-list, and
;; write it to the response.  Return #t if successful, or set a
;; suitable response code and return a string representing an HTTP
;; error document, if there are any problems.
(define (get-model path-info-list query-string request response)
  (define-generic-java-methods
    write
    get-output-stream
    set-status
    get-writer
    println)
  (define-java-classes
    <java.lang.string>)

  ;; Examine any Accept headers in the request.  Return #f if there were
  ;; Accept headers and we can't satisfy them.
  ;; Return (mime-string . rdf-language) if we can satisfy a request.
  ;; Return a default pair (the language which MIME type */* maps to)
  ;; if there were no Accept headers.
  (define (find-ok-language rq)
    (let ((lang-mime-list (request->accept-mime-types rq)))
      (chatter (format #f "lang-mime-list=~s" lang-mime-list))
      (if (null? lang-mime-list)
          (let ((deflang (rdf:mime-type->language #f)))
            (cons (rdf:language->mime-type deflang)
                  deflang))             ;use the default language
          (let loop ((ml lang-mime-list))
            (if (null? ml)
                #f                      ;ooops
                (let ((lang-string (rdf:mime-type->language (car ml))))
                  (chatter "rdf:mime-type->language: ~s -> ~s"
                           (car ml) lang-string)
                  (if lang-string
                      ;; convert lang back to mime-type: don't use (car ml)
                      ;; in case it was */*; also this uses a canonical language
                      (cons (rdf:language->mime-type lang-string)
                            lang-string)
                      (loop (cdr ml)))))))))

  (case (length path-info-list)
    ((1 2)
     (let ((kb-name (car path-info-list))
           (submodel-name (if (null? (cdr path-info-list))
                               #f       ;no submodel
                               (cadr path-info-list))))
       (let ((kb (kb:get kb-name))
             (mime-and-lang (find-ok-language request))
             (query (string->symbol (or query-string "model"))))
         (cond ((and kb
                     (eq? query 'model)
                     mime-and-lang) ;normal case
                (set-response-status! response '|SC_OK| (car mime-and-lang))
                (let ((m (if submodel-name
                             (kb 'get-model submodel-name)
                             (kb 'get-model))))
                  (if m
                      (write m
                             (get-output-stream response)
                             (->jstring (cdr mime-and-lang)))
                      (no-can-do request response
                                 '|SC_NOT_FOUND|
                                 "There is no model to return")))
                #t)

               ((and kb
                     (eq? query 'model))
                (no-can-do request response
                           '|SC_NOT_ACCEPTABLE|
                           "Cannot generate requested content-type:~%~a"
                           (chatter)))

               ((and kb
                     (eq? query 'metadata)
                     mime-and-lang)
                (set-response-status! response '|SC_OK| (car mime-and-lang))
                ((generic-java-method '|write|)
                 (kb 'get-metadata)
                 (get-writer response)
                 (->jstring (cdr mime-and-lang)))
;;                 (for-each (lambda (c) ; XXX
;;                             ((generic-java-method '|println|)
;;                              (get-writer response)
;;                              (->jstring c)))
;;                           (chatter))
                #t)

               ((and kb (eq? query 'metadata))
                (no-can-do
                 request response
                 '|SC_NOT_ACCEPTABLE|
                 "Can't return metadata as acceptable type (requested ~a)"
                 (request->accept-mime-types request)))

               (kb
                (no-can-do request response
                           '|SC_BAD_REQUEST|
                           "bad query: ~a" query-string))

               (else
                (no-can-do request response
                           '|SC_NOT_FOUND|
                           "No such knowledgebase: ~a" kb-name))))))

        (else
         #f)))

(define get-handlers (list get-knowledgebase-list
                           get-model-query
                           get-model
                           get-fallback))

(define/contract (http-head (request  request?)
                            (response response?)
                            -> string-or-true?)
  (with/fc
      (make-fc request response '|SC_INTERNAL_SERVER_ERROR|)
    (lambda ()
      (let ((path-list (request->path-list request)))
        (set-response-status! response '|SC_OK|) ;default response
        (case (length path-list)
          ((0)
           #t)
          ((1)                          ;URL referring to a knowledgebase
           (if (kb:get (car path-list))
               #t
               (set-response-status! response '|SC_NOT_FOUND|)))
          ((2)                          ;URL referring to a submodel
           (let ((status (let ((kb (kb:get (car path-list))))
                           (cond ((not kb)
                                  '|SC_NOT_FOUND|)
                                 ((kb 'get-model (cadr path-list))
                                  '|SC_OK|)
                                 (else
                                  '|SC_NOT_FOUND|)))))
             (set-response-status! response status)))
          (else
           (set-response-status! response '|SC_NOT_FOUND|)))))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; PUT support

;; Handle a single PUT request.  Examines the path-list from the
;; request, and either creates a new knowledgebase (if there is only
;; one element in the path), or creates or updates a submodel (if there
;; are two elements in the path).  It is an error to create a
;; knowledgebase where one of that name exists already.
(define/contract (http-put (request  request?)
                           (response response?)
                           -> string-or-true?)

  ;; Create a new KB, or manage an existing one.  The knowledgebase is
  ;; called kb-name (a symbol), and the content of the request is read
  ;; from the given reader.
  (define (manage-knowledgebase kb-name query)
     (define-generic-java-methods
       get-reader
       get-input-stream)
    (let ((kb (kb:get kb-name))
          (content-type (request->content-type request)))
      (cond ((and kb
                  query
                  (string=? query "metadata")) ;update metadata
             (if (not content-type)
                 (report-exception 'http-put
                                   '|SC_BAD_REQUEST|
                                   "Can't post metadata without a content-type"))
             (let ((kb-uri (string-append (webapp-base-from-request request #t)
                                          "/" kb-name)))
               (if (string=? content-type "text/plain")
                   (kb 'set-metadata
                       (reader->jstring (get-reader request))
                       kb-uri      
                       #f)
                   (kb 'set-metadata
                       (get-input-stream request)
                       kb-uri
                       content-type)))
             (set-response-status! response '|SC_NO_CONTENT|))

            ((and kb query)             ;unrecognised query
             (no-can-do request response '|SC_BAD_REQUEST|
                        "Unrecognised query ~a?~a"
                        kb-name query))

            (kb                                ;already exists
             (no-can-do request response
                        '|SC_FORBIDDEN| ;correct? or SC_CONFLICT?
                        "Knowledgebase ~a already exists"
                        kb-name))

            (query             ;no knowledgebase, but there is a query
             (no-can-do request response '|SC_BAD_REQUEST|
                        "Knowledgebase ~a does not exist, so query ~a is not allowed"
                        kb-name query))

             ((not content-type)
              (no-can-do request response
                         '|SC_BAD_REQUEST|
                         "Can't post metadata without a content-type"))

            (else                         ;normal case
             (let ((nkb (kb:new kb-name))
                   (kb-uri (string-append (webapp-base-from-request request #t)
                                          "/" kb-name)))
               (if (string=? content-type "text/plain")
                   (nkb 'set-metadata
                        (reader->jstring (get-reader request))
                        kb-uri
                        #f)
                   (nkb 'set-metadata
                        (get-input-stream request)
                        kb-uri
                        content-type))
               (set-response-status! response '|SC_NO_CONTENT|))))))

  ;; Given a knowledgebase called KB-NAME, upload a RDF/XML submodel called
  ;; KB-NAME which is available from the given STREAM.  The RDF-MIME is
  ;; the MIME type of the incoming stream.
  (define (update-submodel kb-name
                           submodel-name
                           tbox?
                           rdf-mime
                           stream
                           response)
    (let ((kb (kb:get kb-name))
          (ok-headers '(type length)))
      (if kb
          (with/fc
              (make-fc request response '|SC_BAD_REQUEST|)
            (lambda ()
              (chatter "update-submodel: about to read ~s" submodel-name)
              (let ((m (rdf:ingest-from-stream stream rdf-mime)))
                (chatter "update-submodel: kb=~s submodel=~s"
                         kb submodel-name)
                (if (kb (if tbox? 'add-tbox 'add-abox)
                        submodel-name
                        m)
                    (set-response-status! response '|SC_NO_CONTENT|)
                    (no-can-do request response
                               '|SC_INTERNAL_SERVER_ERROR| ;correct?
                               "Unable to update model!")))))
          (no-can-do request response
                     '|SC_BAD_REQUEST|
                     (format #f "No such knowledgebase ~a"
                             kb-name)))))

  (with/fc
      (make-fc request response '|SC_INTERNAL_SERVER_ERROR|)
   (lambda ()
     (define-generic-java-methods
       get-input-stream)
     (let ((path-list (request->path-list request))
           (query-string (request->query-string request)))
       (cond ((not (content-headers-ok? request))
              (no-can-do
               request response
               '|SC_NOT_IMPLEMENTED|
               "Found unexpected content-* header; allowed ones are ~a"
               (content-headers-ok?)))

             ((= (length path-list) 1)
              (manage-knowledgebase (car path-list)
                                    query-string))

             ((= (length path-list) 2)
              (update-submodel (car path-list)
                               (cadr path-list)
                               (or (not query-string)
                                   (string=? query-string "tbox"))
                               (request->content-type request)
                               (get-input-stream request)
                               response))

             (else                      ;ooops
              (let ()
                (define-generic-java-method get-path-info)
                (no-can-do request response '|SC_BAD_REQUEST|
                           "The request path ~a has the wrong number of elements (1 or 2)"
                           (->string (get-path-info request))))))))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; POST support

;; Handle POST requests.  Return #t on success, or a string response
(define/contract (http-post (request  request?)
                            (response response?)
                            -> string-or-true?)
  (with/fc
      (make-fc request response '|SC_INTERNAL_SERVER_ERROR|)
    (lambda ()
      (define-generic-java-methods
        get-reader
        set-content-type)
      (let ((path-list (request->path-list request))
            (query-string (request->query-string request))
            (content-type (request->content-type request)))
        ;; First, insist that there's just one element in the path-list.
        ;; If there is a content type on the incoming query, we check
        ;; that it is application/sparql-query
        ;; (see <http://www.w3.org/TR/rdf-sparql-query/>)
        (if (and (= (length path-list) 1)
                 (not query-string)
                 (or (not content-type)
                     (string=? content-type "application/sparql-query")))
            (let ((kb (kb:get (car path-list))))
              (or kb
                  (report-exception
                   'http-post
                   '|SC_BAD_REQUEST|
                   "don't know about knowledgebase ~a" (car path-list))
                  ;; (error 'http-post
;;                          "don't know about knowledgebase ~a" (car path-list))
                  )
              (let ((runner
                     (sparql:make-query-runner
                      kb
                      (reader->jstring (get-reader request))
                      (request->accept-mime-types request))))
                (runner (response->lazy-output-stream response)
                        (lambda (mimetype)
                          (set-content-type response
                                            (->jstring mimetype))))
                #t))
            (no-can-do request response
                       '|SC_BAD_REQUEST|
                       "POST SPARQL request must have one path element, no query, and content-type application/sparql-query~%(path=~s, query=~a, content-type=~a)"
                       path-list query-string content-type))
        ))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; DELETE support

;; Handle DELETE requests.  Return #t on success, or a string response.
(define/contract (http-delete (request  request?)
                              (response response?)
                              -> string-or-true?)
  (with/fc
      (make-fc request response '|SC_INTERNAL_SERVER_ERROR|)
    (lambda ()
      (let ((path-list (request->path-list request)))
        (cond ((= (length path-list) 1)
               (if (kb:discard (car path-list))
                   (set-response-status! response '|SC_NO_CONTENT|)
                   (no-can-do request response
                              '|SC_NOT_FOUND| ;correct?
                              "There was no knowledgebase ~a to delete"
                              (car path-list))))
              ((= (length path-list) 2)
               (let ((kb-name (car path-list))
                     (submodel (cadr path-list)))
                 (cond ((kb:get kb-name)
                        => (lambda (kb)
                             (if (kb 'drop-submodel submodel)
                                 (set-response-status! response '|SC_NO_CONTENT|)
                                   (no-can-do request response
                                              '|SC_BAD_REQUEST|
                                              "No such submodel ~a"
                                              submodel))))
                       (else
                        (no-can-do request response
                                   '|SC_BAD_REQUEST|
                                   "No such knowledgebase ~a" kb-name)))))
              (else
               (no-can-do request response
                          '|SC_BAD_REQUEST|
                          "The request path has too many elements ~s"
                          path-list)))))))

;; Small module to wrap a hashtable, which stores functions
;; (and possibly, later, continuations) in exchange for a token
(module f-store
    (f->ftoken ftoken->f)

  (import hashtable)
  (define fmap #f)
  (define fmap-sync-object (->jstring ""))

  (define (f->ftoken f)
    (define-java-class <java.lang.system>)
    (define-generic-java-methods
      current-time-millis)
    (or fmap (set! fmap (make-hashtable)))
    (java-synchronized fmap-sync-object
      (lambda ()
        (let ((tok (format #f "f~a"
                           (->number (current-time-millis
                                      (java-null <java.lang.system>))))))
          (hashtable/put! fmap tok f)
          tok))))

  (define (ftoken->f ftoken)
    (if fmap
        (let ((f (hashtable/get fmap ftoken)))
          (java-synchronized fmap-sync-object
            (lambda ()
              (and f (hashtable/remove! fmap ftoken))))
          f)
        #f)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; XML-RPC support

(define xmlrpc-handler #f)
;; (module xmlrpc-support
;;     (xmlrpc-handler)
(let ()

  (import f-store)

  ;; Wrapper for xmlrpc:create-fault.
  ;; Given a fault code as a symbol, turn it into an integer using the
  ;; contained alist, and produce an error message using the given format
  ;; and arguments.
  (define (fault code fmt . args)
    (let ((fault-list '((protocol-error 0)
                        (unrecognised-method 1)
                        (malformed-request 2)
                        (unknown-object 10)
                        (unparseable-query 20))))
      (let ((l (assq code fault-list)))
        (if l
            (xmlrpc:create-fault
             (cadr l)
             (apply format
                    `(#f
                      ,(string-append "~a:" fmt)
                      ,code
                      ,@args)))
            (error 'fault "Ooops: unrecognised fault code ~s" code)))))

  ;; make-fc-xmlrpc symbol string -> procedure
  ;;
  ;; Make a failure-continuation suitable for XML-RPC errors.  Given a
  ;; MESSAGE-CODE symbol acceptable to FAULT, and a MESSAGE-TEXT string,
  ;; return a procedure suitable for use as a failure-continuation.
  (define (make-fc-xmlrpc message-code message-text)
    (lambda (m e)
      ;; don't print the stack trace except when debugging:
      ;; angle brackets result in malformed XML
      (if (debugging?)
          (fault message-code
                 "~a (~a)~%~a"
                 message-text
                 (error-message m)
                 (with-output-to-string
                   (lambda () (print-stack-trace e))))
          (fault message-code
                 "~a (~a)"
                 message-text
                 (error-message m)))))

  ;; Get a single model.  The response contains a URL which points
  ;; to one of the HTTP GET methods.
  (define (xmlrpc-get-model quaestor-url . args)
    (cond ((and (= (length args) 1)
                (string? (car args)))
           (let ((kb (kb:get (car args))))
             (if kb
                 (xmlrpc:create-response "~a/kb/~a"
                                         quaestor-url (car args))
                 (fault 'unknown-object
                        "no such knowledgebase ~a" (car args)))))
          ((and (= (length args) 2)
                (string? (car args))
                (string? (cadr args)))
           (let ((kb (kb:get (car args))))
             (if (and kb (kb 'has-model (cadr args)))
                 (xmlrpc:create-response "~a/kb/~a/~a"
                                         quaestor-url
                                         (car args)
                                         (cadr args))
                 (fault 'unknown-object
                        "no such model ~a/~a"
                        (car args) (cadr args)))))
          (else
           (fault 'malformed-request
                  "get-model requires 1 or 2 string args, got ~a ~s"
                  (length args) args))))

  ;; xmlrpc-query-model string string string -> sexp
  ;;
  ;; Do a query against the given model.
  (define (xmlrpc-query-model quaestor-url model-name query-string . mime-type)
    (let ((kb (kb:get model-name)))
      (if kb
          (with/fc
              (make-fc-xmlrpc 'unparseable-query "can't process SPARQL query")
            (lambda ()
              (let ((runner
                     (sparql:make-query-runner kb
                                               query-string
                                               (if (null? mime-type)
                                                   '("*/*")
                                                   `(,(car mime-type))))))
                (xmlrpc:create-response "~a/pickup/~a"
                                        quaestor-url
                                        (f->ftoken runner)))))
          (fault 'unknown-object
                 "don't know about knowledgebase ~a" model-name))))

  ;; method-name->handler symbol -> integer, procedure
  ;;
  ;; Map method names to procedures.
  ;;    (method-name->handler 'NAME) => NARGS, HANDLER
  ;; Given a method NAME as a symbol, return a pair of values giving
  ;; the number of arguments that method expects (or #f if it can
  ;; handle a variable number), and a procedure which will do the
  ;; work.  The handler must match the signature
  ;;    (handler BASE-URI ARG ...) => XML-SEXP
  ;; The handler is passed the BASE-URI of this service and the
  ;; requisite number of arguments extracted from the XML-RPC call,
  ;; and returns a sexp which is to be converted to XML as a
  ;; response.  This may most conveniently be generated by
  ;; XMLRPC:CREATE-RESPONSE or XMLRPC:CREATE-FAULT (wrapped in FAULT).
  (define method-name->handler
    (let ((mappings #f))
      (lambda (name)
        (or mappings
            (set! mappings
                  `((get-model #f ,xmlrpc-get-model)
                    (query-model #f ,xmlrpc-query-model))))
        (cond ((not name)
               (values #f #f))
              ((assq name mappings)
               => (lambda (l)
                    (apply values (cdr l))))
              (else
               (values #f #f))))))

  ;; Do the actual XML-RPC call, given a READER from which to read the 
  ;; XML, and a URL, which is the base URI of the service.  Return a sexp
  ;; which is to be turned into XML.
  (define (do-xmlrpc-call my-url reader)
    (let ((call (xmlrpc:new-call (sexp-xml:xml->sexp/reader reader))))
      (call-with-values (lambda ()
                          (method-name->handler (xmlrpc:method-name call)))
        (lambda (nargs h)
          (cond ((not h)
                 (fault 'unrecognised-method
                        "unrecognised method: ~a" (xmlrpc:method-name call)))
                ((not (procedure? h))
                 (error 'do-xmlrpc-call
                        "handler was not procedure! ~a -> ~s"
                        (xmlrpc:method-name call) h))
                (nargs
                 (if (= nargs (xmlrpc:number-of-params call))
                     (apply h         ;normal case 1 -- fixed no. args
                            (cons my-url (xmlrpc:method-param-list call)))
                     (fault 'malformed-request
                            "method ~a expected ~a params, got ~a"
                            (xmlrpc:method-name call)
                            nargs
                            (xmlrpc:number-of-params call))))
                (else              ;normal case 2 -- variable no. args
                 (apply h
                        (cons my-url (xmlrpc:method-param-list call)))))))))

  ;; Handle a single XML-RPC request.  Return a response as a string
  ;; containing XML, success or failure.
  (define (_xmlrpc-handler request response)
    (define-generic-java-methods
      get-reader
      get-local-name
      get-local-port
      get-context-path)

    ;; pre-emptively set the response status and content-type
    ;; (error handlers may change this)
    (set-response-status! response '|SC_OK| "text/xml")
    (sexp-xml:sexp->xml
     (with/fc 
         (make-fc-xmlrpc 'protocol-error "malformed request")
       (lambda ()
         (cond  ((not (content-headers-ok? request))
                 ;; Unexpected Content-* header found
                 (no-can-do
                  request response
                  '|SC_NOT_IMPLEMENTED|
                  "Found unexpected content-* header; allowed ones are ~a"
                  (content-headers-ok?)))

                ((let ((type (request->content-type request)))
                   (and type (or (string=? type "text/xml")
                                 ;; http://www.xmlrpc.com/spec
                                 ;; says text/xml only
                                 ;; but allow application/xml, too
                                 (string=? type "application/xml"))))
                 ;; Good -- normal case
                 ;; Now do the actual work of reading the method
                 ;; call from the input reader.
                 (do-xmlrpc-call
                  (webapp-base-from-request request)
                  (get-reader request)))

                (else
                 ;; bad Content-Type
                 (fault 'protocol-error
                        "Request content-type must be text/xml, not ~a"
                        (or (request->content-type request)
                            "<null>"))))))
     '(|methodResponse| fault params struct) ;make it look pretty
     '(param member)))
  (set! xmlrpc-handler _xmlrpc-handler)
)                                     ;end of module xmlrpc-support

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; Pickup
;;
;; We get tokens which were created by f->ftoken, turn them back into
;; functions, and call them.  There's only one type of function signature
;; at present, namely those created by xmlrpc-query-runner above, which takes
;; an output stream and a function to set a response MIME type.  More
;; are clearly possible in future.

;; pickup-dispatcher java-request java-response -> object
;; side-effect: dereference the ftoken in the request, and call the function,
;;   which will send output to the response output stream.
;;
;; Handle the pickup functions.  Return whatever the stored function returns.
(define/contract (pickup-dispatcher (request  request?)
                                    (response response?)
                                    -> string-or-true?)
  (import f-store)
  (define-generic-java-method
    set-content-type)
  (with/fc
      ;; Since all the error-handling was supposed to be done before
      ;; the callback functions were stored, any errors other than
      ;; malformed calls are our fault.
      (make-fc request response '|SC_INTERNAL_SERVER_ERROR|)
    (lambda ()
      (let ((path-list (request->path-list request)))
        (if (= (length path-list) 1)
            (let ((callback (ftoken->f (car path-list))))
              (if callback
                  (callback (response->lazy-output-stream response)
                            (lambda (mimetype)
                              (set-content-type response
                                                (->jstring mimetype))))
                  (no-can-do request response
                             '|SC_BAD_REQUEST|
                             "can't find callback for token ~a (have you called this more than once?)" (car path-list))))
            (no-can-do request response
                       '|SC_BAD_REQUEST|
                       "found multiple path elements in pickup URL: ~s"
                       path-list))))))


;; Return true if debugging is on; false otherwise.
;; If given an argument, set the debugging state to that value and return the
;; previous one
(define debugging?
  (let ((flag #f))
    (lambda args
      (if (null? args)
          flag
          (let ((prev flag))
            (set! flag (car args))
            prev)))))
