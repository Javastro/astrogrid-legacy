;; Test harness.
;; The first argument to MAIN should be the full path to this script.

(import s2j)

(define nfails 0)
(define-syntax expect
  (syntax-rules ()
    ((_ id expected body ...)
     (with/fc (lambda (m e)
                (format #t "Test ~a~%    produced error~a: ~a~%    expected ~s~%"
                          (quote id)
                          (if (error-location m)
                              (format #f " in ~a" (error-location m))
                              "")
                          (error-message m)
                          expected)
                (set! nfails (+ nfails 1)))
       (lambda ()
         (let ((test ((lambda ()
                        body ...))))
           (if (not (equal? expected test))
               (begin (format #t "Test ~a~%    produced ~s~%    expected ~s~%"
                              (quote id) test expected)
                      (set! nfails (+ nfails 1))))))))))
(define-syntax expect-failure
  (syntax-rules ()
    ((_ id body ...)
     (with/fc (lambda (m e)
                ;;failed -- OK
                #t)
        (lambda ()
          (let ((test ((lambda () body ...))))
            (format #t "Test ~a~%    produced ~s~%    expected ERROR~%"
                    (quote id) test)
            (set! nfails (+ nfails 1))))))))

(define files-to-test '("quaestor/utils.scm"
                        "quaestor/knowledgebase.scm"
                        "quaestor/jena.scm"
                        "util/sisc-xml.scm"
                        "util/xmlrpc.scm"
                        ))

(define (main . args)
  (define-generic-java-method
    exit)
  (define-java-class <java.lang.system>)
  (with-current-url (car args)
     (lambda ()
       (for-each run-test-file
                 files-to-test)))
  (if (> nfails 0)
      (format #t "Number of fails=~a~%" nfails))
  (exit (java-null <java.lang.system>) (->jint nfails)))

(define (run-test-file file-name)
  (let ((url (normalize-url (current-url) file-name)))
    (or url
        (error (format #f "Can't find resource ~a" file-name)))
    (chatter "Running tests in ~a..." url)
    (load url)))

(define chatter
  (let ((chatter? #t))
    (lambda (fmt . args)
      (cond ((boolean? fmt)
             (set! chatter? fmt))
            (chatter?
             (apply format `(#t ,(string-append fmt "~%") ,@args)))
            (else
             #f)))))


