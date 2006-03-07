(import s2j)

(define nfails 0)
(define-syntax expect
  (syntax-rules ()
    ((_ id expected body ...)
     (let ((test ((lambda ()
                    body ...))))
       (if (not (equal? expected test))
           (begin (format #t "Test ~a~%    produced ~s~%    expected ~s~%"
                          (quote id) test expected)
                  (set! nfails (+ nfails 1))))))))

(define files-to-test '("utils.scm"))

(define (main . args)
  (define-generic-java-method
    exit)
  (define-java-class <java.lang.system>)
  (for-each run-test-file
            files-to-test)
  (if (> nfails 0)
      (format #t "Number of fails=~a~%" nfails))
  (exit (java-null <java.lang.system>) (->jint nfails)))

(define (run-test-file file-name)
  (let ((url (find-resource file-name)))
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


