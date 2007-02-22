;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; TEST-REDIRECTOR handler

;; Define TEST-REDIRECTOR, which supports the sort of 303-based HTTP redirection
;; required to test the resolving logic above.
;;
;; That is, the following functionality is for the test harness and for
;; demos, and not (really) the final exposed functionality.
(require-library 'quaestor/utils)

(module redirector
    (test-redirector)

(import utils)

(define redirector-context
  (let ((ctx #f))
    (lambda (request)
      (if (not ctx)
          (set! ctx (webapp-base-from-request request #t)))
      ctx)))

;; our-resource-as-stream : string -> pair-or-false
;; Get one of our resources and return a (stream . string) pair containing
;; the stream connected to it, and its mime type.
;; If there's no such resource, return #f.
(define (our-resource-as-stream name)
  (define-generic-java-methods get-resource-as-stream get-mime-type)
  (let ((jname (->jstring name)))
    (chatter "our-resource-as-stream: name=~s" name)
    (cond ((non-null (get-resource-as-stream (servlet 'context) jname))
           => (lambda (stream)
                (chatter "our-resource-as-stream: ~s => stream with mime-type ~s"
                         name 
                         (->string-if-non-null
                          (get-mime-type (servlet 'context) jname)))
                (cons stream
                      (->string-if-non-null
                       (get-mime-type (servlet 'context) jname)))))
          (else
           #f))))

;; ACCEPTABLE-MIME : string list-of-strings -> string-or-#f
;;
;; Test whether a given MIME type is acceptable.  Given a MIME type,
;; return a MIME type which is present in the MIME-LIST (which may
;; include "*/*"), or #f if there is none such.  The return value will
;; typically be the same as the input MIME argument, unless this is
;; not acceptable. A non-#f return will be different from the input if
;; the input includes a wildcard as above.
(define (acceptable-mime mime mime-list)
  (chatter "acceptable-mime: mime=~s  mime-list=~s" mime mime-list)
  (cond ((null? mime-list)
         #f)
        ((member "*/*" mime-list)
         mime)
        ((member mime mime-list)
         mime)
        (else
         #f)))

;; LOOKUP-REWRITES : string list-of-strings-or-#f -> various
;;
;; Given a NAME and list of strings containing acceptable MIME types,
;; return:
;;    (mime . url) representing a new replacement URL and its
;;        MIME type; or 
;;    (mime . #f) representing the MIME type which the input resource
;;        should be served as; or
;;   'unacceptable if a list of MIME types was given and there is
;;        no resource which satisfies it; or
;;   #f if we know nothing about this resource.
;;
;; If the ACCEPTABLE-MIMES argument is #f, this is taken as equivalent to
;; 'Accept: */*' (though we are probably just expecting to receive a
;; new MIME type back.
(define lookup-rewrites
  (let ((rewrite-map #f))
    (lambda (name acceptable-mimes)
      (define-java-classes <java.io.input-stream-reader>)
      (if (not rewrite-map)
          (cond ((our-resource-as-stream "/WEB-INF/rewrite-map.scm")
                 => (lambda (p)
                      (set! rewrite-map
                            (read (->character-input-port
                                   (java-new <java.io.input-stream-reader>
                                             (car p)))))
                      (chatter "lookup-rewrites: map=~s" rewrite-map)))
                (else
                 (error 'test-redirector "Can't find rewrite-map"))))
      (chatter "lookup-rewrites: name=~s  mimes=~s" name acceptable-mimes)
      (cond ((assoc name rewrite-map)
             => (lambda (p)
                  (let ((mime-file-mappings (cdr p)))
                    (cond ((null? mime-file-mappings)
                           #f)
                          ((string? (car mime-file-mappings))
                           (cons (car mime-file-mappings) #f))
                          (acceptable-mimes
                           (let loop ((ok-mimes acceptable-mimes))
                             (cond ((null? ok-mimes)
                                    'unacceptable)
                                   ((string=? (car ok-mimes) "*/*")
                                    (car mime-file-mappings))
                                   ((assoc (car ok-mimes) mime-file-mappings))
                                   (else
                                    (loop (cdr ok-mimes))))))
                          (else         ;we know (pair? (car mime-file-mappings))
                           (car mime-file-mappings))))))
            (else
             #f)))))

;; rewrite-resource : string list-of-strings -> (string . string) or #f or 'unacceptable
;; rewrite-resource : #f object -> #f
;; Given a RESOURCE and a list of mime types, return a string resource which
;; is what that should map to as controlled by the /WEB-INF/rewrite-map.scm file.
;;
;; If we know nothing of the resource, return #f.
;;
;; If we know about the resource, but there are no acceptable MIME types,
;; return 'unacceptable
(define (rewrite-resource resource mime-type-list)
  (let ((ans (cond ((not resource)      ;short-circuit special case
                    #f)
                   ((lookup-rewrites resource mime-type-list))
                   (else
                    #f))))
    (servlet 'log
             "rewrite-resource: resource=~s, mime-list=~s => ~s"
             resource mime-type-list ans)
    ans))

;; send-stream-to-response : response java-input-stream -> #f
;; Send the contents of the input stream to the response
(define (send-stream-to-response response istream)
  (define-java-classes
    (<q-utils> |org.eurovotech.quaestor.helpers.Util|))
  (define-generic-java-methods
    get-output-stream
    flush
    close
    copy)
  (let ((ostream (get-output-stream response)))
    (copy (java-null <q-utils>) istream ostream)
    (flush ostream)
    (close istream)
    (close ostream)
    #f))

;; test-redirector : request response -> string
;; If the request refers to an actual resource, return it as a stream
;; (thus returning #f from this procedure).  If the request instead
;; refers to something which REWRITE-RESOURCE knows about, then set
;; a 303 response and return something pointing to that.  Otherwise
;; set the response to be 404.
(define (test-redirector request response)
  (with/fc
      (make-fc request response '|SC_INTERNAL_SERVER_ERROR|)
    (lambda ()
      (define-generic-java-methods get-path-info)
      (let ((res (or (->string-if-non-null (get-path-info request))
                     "index.html"))
            (acceptable (request->accept-mime-types request)))
        (define-generic-java-methods
          set-header
          to-string)
        (chatter "test-redirector: res=~s request MIME-types=~s"
                 res acceptable)

        (cond ((our-resource-as-stream res)
               ;; this resource exists
               => (lambda (p)
                    (let ((stream (car p))
                          (mime   (cdr p)))
                      (cond ((acceptable-mime mime acceptable)
                             (set-response-status!
                              response
                              '|SC_OK|
                              (cond ((lookup-rewrites res #f)
                                     => car)
                                    (else mime)))
                             (send-stream-to-response response stream))
                            (else
                             (set-response-status! response
                                                   '|SC_NOT_ACCEPTABLE|
                                                   "text/html")
                             (response-page request response
                                            "No acceptable representations"
                                            `((p ,(format #f "No representations of the resource ~s could be found, in one of the requested representations ~s"
                                                          res acceptable)))))))))

              ((rewrite-resource res acceptable)
               ;; the resource has been successfully rewritten
               ;; (ie, we know about it)
               => (lambda (mime-and-resource)
                    (define-java-classes <java.io.file>)
                    (define-generic-java-methods exists)
                    (cond ((and (symbol? mime-and-resource)
                                (eq? mime-and-resource 'unacceptable))
                           (set-response-status! response
                                                 '|SC_NOT_ACCEPTABLE|
                                                 "text/html")
                           (response-page request response
                                          "No acceptable representations"
                                          `((p ,(format #f "No representations of the resource ~s could be found, in one of the requested representations ~s"
                                                        res acceptable)))))

                          ((pair? mime-and-resource)
                           (let ((mime-type (car mime-and-resource))
                                 (newres    (cdr mime-and-resource)))
                             (let ((real-path (servlet 'real-path newres))
                                   (new-url (string-append (redirector-context request)
                                                           newres)))
                               (cond ((and real-path
                                           (->boolean (exists (java-new <java.io.file>
                                                                        real-path))))
                                      ;; ...and it maps to an actual file
                                      ;; (normal case)
                                      (set-response-status! response
                                                            '|SC_SEE_OTHER|
                                                            "text/html")
                                      (set-header response
                                                  (->jstring "Location")
                                                  (->jstring new-url))
                                      (response-page request response "See elsewhere"
                                                     `((p ,(format #f "The resource you are looking for can be found at ~a" new-url)))))

                                     (real-path
                                      ;; ...but it maps to a non-existent file
                                      (set-response-status! response '|SC_INTERNAL_SERVER_ERROR|)
                                      (servlet 'log "Resource ~a mapped to ~a, but real path ~a not found"
                                               res newres (->string real-path))
                                      (response-page request response "Not found"
                                                     `((p ,(format #f "Resource ~a not found" res)))))

                                     (else
                                      ;; does this case ever happen?
                                      (set-response-status! response
                                                            '|SC_NOT_FOUND|
                                                            "text/html")
                                      (response-page request response "Not found"
                                                     `((p ,(format #f "Resource ~a not found" res)))))))))
                          (else
                           (error 'test-redirector
                                  (format #f "Unexpected response ~s from rewriting" newres))))))

              (else
               ;; resource couldn't be rewritten
               ;; (ie, we don't know anything about it)
               (set-response-status! response
                                     '|SC_NOT_FOUND|
                                     "text/html")
               (response-page request response "Unknown resource"
                              `((p ,(format #f "I don't know anything about ~a"
                                            res))
                                (p ,(format #f "You asked for MIME types: ~s"
                                            acceptable))))
               ;;                  (error (format #f "Nothing found for res=~a: MIME types=~s"
               ;;                                 res acceptable))
               ))))))

) ; end of module
