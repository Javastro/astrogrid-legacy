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
;; Get one of our resources (only one level -- no /x/y) and return a
;; (stream . string) pair containing the stream connected to it, and
;; its mime type.  If there's no such resource, return #f.
(define (our-resource-as-stream name)
  (define-generic-java-methods get-resource-as-stream get-mime-type)
  (let ((jname (->jstring name)))
    (cond ((non-null (get-resource-as-stream (servlet 'context) jname))
           => (lambda (stream)
                (servlet 'log "resource ~s/~s produced stream ~s and mime-type ~s"
                         name jname stream
                         (->string-if-non-null
                          (get-mime-type (servlet 'context) jname)))
                (cons stream
                      (->string-if-non-null
                       (get-mime-type (servlet 'context) jname)))))
          (else
           #f))))

;; rewrite-resource : string list-of-strings -> string-or-false
;; rewrite-resource : #f object -> #f
;; Given a RESOURCE and a list of mime types, return a string resource which
;; is what that should map to as controlled by the
;; /WEB-INF/rewrite-map.scm file.
(define rewrite-resource
  (let ((rewrite-map #f))  ;map of ((name (mime file) (mime file))...)
    (define (match-name-and-mime name acceptable-mimes)
      (chatter "match-name-and-mime: name=~s  acceptable-mimes=~s"
               name acceptable-mimes)
      ;; find an entry in rewrite-map which has the given name,
      ;; and a mime in acceptable-mimes.  Or #f if none found
      (cond ((assoc name rewrite-map)
             => (lambda (p)
                  (let ((mime-file-mappings (cdr p)))
                    (and (not (null? mime-file-mappings))
                         (let loop ((ok-mimes acceptable-mimes))
                           (cond ((null? ok-mimes)
                                  #f)
                                 ((string=? (car ok-mimes) "*/*")
                                  (cadar mime-file-mappings))
                                 ((assoc (car ok-mimes) mime-file-mappings)
                                  => cadr)
                                 (else
                                  (loop (cdr ok-mimes)))))))))
            (else
             #f)))
    (lambda (resource mime-type-list)
      (define-java-classes <java.io.input-stream-reader>)
      (if (not rewrite-map)
          (cond ((our-resource-as-stream "/WEB-INF/rewrite-map.scm")
                 => (lambda (p)
                      (set! rewrite-map
                            (read (->character-input-port
                                   (java-new <java.io.input-stream-reader>
                                             (car p)))))
                      (chatter "rewrite-map: map=~s" rewrite-map)))
                (else
                 (error 'test-redirector "Can't find rewrite-map"))))
      (let ((ans (cond ((not resource)  ;short-circuit special case
                        #f)
                       ((match-name-and-mime resource mime-type-list))
                       (else
                        #f))))
        (servlet 'log
                 "rewrite-resource: resource=~s, mime-list=~s => ~s"
                 resource mime-type-list ans)
        ans)
      ;;         (cond ((not resource)           ;short-circuit special case
      ;;                #f)
      ;;               ((match-name-and-mime resource mime-type-list))
      ;;               (else
      ;;                #f))
      )))

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
    (set-response-status! response '|SC_OK| "text/html")
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
                          (mime (cdr p)))
                      (set-response-status! response '|SC_OK| mime)
                      (send-stream-to-response response stream))))

              ((rewrite-resource res acceptable)
               ;; the resource has been successfully rewritten
               ;; (ie, we know about it)
               => (lambda (newres)
                    (define-java-classes <java.io.file>)
                    (define-generic-java-methods exists)
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
                             (set-response-status! response '|SC_NOT_FOUND|)
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
