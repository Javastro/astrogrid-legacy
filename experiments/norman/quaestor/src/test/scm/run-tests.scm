;; Test harness.
;; The first argument to MAIN should be the full path to this script.

(import s2j)

;; We don't actually import this library here, but the tested modules
;; do import quaestor-support
(require-library 'org/eurovotech/quaestor/scheme-wrapper-support)


(define failures-in-block?
  (let ((failures? #f))
    (lambda arg
      (cond ((null? arg)
             (let ((t failures?))
               (set! failures? #f)
               t))
            ((boolean? (car arg))
             (set! failures? (car arg)))
            (else
             (error "Bad call to FAILURES-IN-BLOCK?"))))))
(define failures
  (let ((n 0))
    (lambda arg
      (cond ((null? arg)
             n)
            ((number? (car arg))
             (set! n (+ n (car arg)))
             (failures-in-block? #t))
            (else
             (error "Bad call to FAILURES"))))))

(define (test-fc id expected)
  (lambda (m e)
    (format #t "Test ~a~%    produced error~a: ~a~%    expected ~s~%"
            id
            (if (error-location m)
                (format #f " in ~a" (error-location m))
                "")
            (or (error-message m)
                (error-message (error-parent-error m))
                (error-message
                 (error-parent-error
                  (error-parent-error m)))
                m)
            expected)
    (failures 1)))

;; Expect the body to evaluate to the value EXPECTED
(define-syntax expect
  (syntax-rules ()
    ((_ id expected body ...)
     (with/fc (test-fc (quote id) expected)
       (lambda ()
         (let ((test ((lambda ()
                        body ...))))
           (if (not (equal? expected test))
               (begin (format #t "Test ~a~%    produced ~s~%    expected ~s~%"
                              (quote id) test expected)
                      (failures 1)))))))))

;; Expect the body to throw an error
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
            (failures 1)))))))

;; Expect the body to evaluate to some non-#f result
(define-syntax expect-true
  (syntax-rules ()
    ((_ id body ...)
     (with/fc (test-fc (quote id) #t)
       (lambda ()
         (or ((lambda () body ...))
             (begin (format #t "Test ~a: expected true, was false~%"
                            (quote id))
                    (failures 1))))))))

(define files-to-test '("quaestor/utils.scm"
                        "quaestor/knowledgebase.scm"
                        "quaestor/jena.scm"
                        "quaestor/sparql.scm"
                        "util/sexp-xml.scm"
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
  (if (> (failures) 0)
      (format #t "Number of fails=~a~%" (failures)))
  (exit (java-null <java.lang.system>) (->jint (failures))))

(define (run-test-file file-name)
  (let ((url (normalize-url (current-url) file-name)))
    (or url
        (error (format #f "Can't find resource ~a" file-name)))
    (chatter "Running tests in ~a..." file-name)
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


