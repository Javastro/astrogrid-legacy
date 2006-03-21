;; A list of test cases for the utils package.

(require-library 'quaestor/utils)
(import utils)


(expect sexp->xml-1
        "<p>hello <em>there</em></p>\n"
        (sexp->xml '(p "hello " (em there))))

(expect iterator->list-1
        '(()
          ("xone")
          ("xone" "xtwo" "xthree"))
        (define-java-classes
          <java.lang.string>
          <java.util.arrays>)
        (define-generic-java-methods
          as-list
          iterator)
        (map (lambda (sl)
               (let ((ja (->jarray (map ->jstring sl) <java.lang.string>)))
                 (or (java-array? ja)
                     (error iterator->list-1 "Ooops: not jarray: ~s" ja))
                 (map (lambda (ji) (string-append "x" (->string ji)))
                      (ITERATOR->LIST
                       (iterator
                        (as-list (java-null <java.util.arrays>) ja))))))
             '(()
               ("one")
               ("one" "two" "three"))))

(expect url-decode-to-jstring-1
        '(""
          "hello there"
          "one$two"
          "%20")
        (map (lambda (s)
               (->string (URL-DECODE-TO-JSTRING s)))
             '(""
               "hello+there"
               "one%24two"
               "%2520")))

(expect error-with-status-1
        '((t1 1 "hello")
          (t2 "x" "hello there"))
        (map (lambda (l)
               (with/fc
                (lambda (m e)
                  (list (error-location m)
                        (car (error-message m))
                        (cdr (error-message m))))
                (lambda ()
                  (apply ERROR-WITH-STATUS l))))
             '((t1 1 "hello")
               (t2 "x" "hello ~a" "there"))))

(expect parse-http-accept-header
        '(("text/plain")
          ("*/*")
          ;("text/plain" "application/xml")
          ("text/plain" "text/xml")
          ("text/plain" "text/xml" "*/*")
          ("application/xml" "text/plain")
          ("a/a" "b/b" "c/c"))
        (map (lambda (ss)
               (parse-http-accept-header (->jstring ss)))
             '("text/plain"
               "*/*"
               ;"text/plain, application/xml" ;omit this -- sort isn't stable
               "text/plain, text/xml; q=0.2"            ;simple
               "text/plain, text/xml;q=.5, */*;q=.2"    ;no leading 0 on numbers
               "text/plain; q=0.5 , , application/xml," ; empty elements
               "c/c;q=0.2,b/b;q=0.5,a/a,,,"             ;list is reversed
               )))
