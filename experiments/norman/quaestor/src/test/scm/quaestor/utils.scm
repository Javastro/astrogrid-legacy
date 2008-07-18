;; A list of test cases for the utils package.

(require-library 'quaestor/utils)
(import utils)


;; (expect sexp->xml-1
;;         "<p>hello <em>there</em></p>\n"
;;         (sexp->xml '(p "hello " (em there))))

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
                     (error jobject->list-1 "Ooops: not jarray: ~s" ja))
                 (map (lambda (ji) (string-append "x" (->string ji)))
                      (jobject->LIST
                       (iterator
                        (as-list (java-null <java.util.arrays>) ja))))))
             '(()
               ("one")
               ("one" "two" "three"))))

(expect java-type
        '(#t #t #t #t #f #f #f)
        (define-java-classes <java.util.hash-set> <java.util.linked-hash-set>)
        (let ((hash (java-new <java.util.hash-set>))
              (linked-hash (java-new <java.util.linked-hash-set>)))
          (map (lambda (p) (apply is-java-type? p))
             `((,hash ,<java.util.hash-set>)
               (,hash |java.util.HashSet|)
               (,linked-hash ,<java.util.linked-hash-set>)
               (,linked-hash ,<java.util.hash-set>) ;hash-set subclass of linked-hash-set
               ;; and not...
               (,(java-null <java.util.hash-set>) ,<java.util.hash-set>)
               ("hello" ,<java.util.hash-set>)
               (#f ,<java.util.hash-set>)))))

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

(expect parse-http-accept-header
        '(("text/plain")
          ("*/*")
          ;("text/plain" "application/xml")
          ("text/plain" "text/xml")
          ("text/plain" "text/xml" "*/*")
          ("application/xml" "text/plain")
          ("a/a" "b/b" "c/c")
          ("text/x-c" "text/html" "text/x-dvi" "text/plain")
          ("text/html" "text/html" "text/*" "*/*"))
        (map (lambda (ss)
               (PARSE-HTTP-ACCEPT-HEADER (->jstring ss)))
             '("text/plain"
               "*/*"
               "text/plain, text/xml; q=0.2"            ;simple
               "text/plain, text/xml;q=.5, */*;q=.2"    ;no leading 0 on numbers
               "text/plain; q=0.5 , , application/xml," ; empty elements
               "c/c;q=0.2,b/b;q=0.5,a/a,,,"             ;list is reversed
               "text/plain;q=0.5,text/html;q=0.9,text/x-dvi;q=0.8,,text/x-c"
               ;; check ordering, and that non-q parameters are stripped
               "text/*, text/html, text/html;level=1, */*"
               )))

(expect acceptable-mime-1
        "text/html"
        (acceptable-mime "text/html" '("text/plain" "text/html")))
(expect acceptable-mime-2
        #f
        (acceptable-mime "text/html" '("text/plain" "text/nothing")))
(expect acceptable-mime-3
        "text/html"
        (acceptable-mime "text/html" '("text/plain" "text/nothing" "*/*")))
;; (expect acceptable-mime-4 ; doesn't work yet
;;         "text/html"
;;         (acceptable-mime "text/html" '("text/plain" "text/nothing" "text/*")))
(expect acceptable-mime-5
        "text/html"
        (acceptable-mime '("text/plain" "text/html")
                         '("text/html" "text/plain")))
(expect acceptable-mime-6
        "text/nothing1"
        (acceptable-mime '("text/nothing1" "text/nothing2")
                         '("text/html" "text/plain" "*/*")))
(expect acceptable-mime-7
        #f
        (acceptable-mime '("text/nothing1" "text/nothing2")
                         '("text/html" "text/plain")))
;; (expect acceptable-mime-8               ;doesn't work yet
;;         "text/nothing1"
;;         (acceptable-mime '("text/nothing1" "text/nothing2")
;;                          '("text/html" "text/plain" "text/*")))

(expect acceptable-mime1
        '()
        (map ACCEPTABLE-MIME
             '()
             '()))

(expect parse-query-string
        '(() () () ()
          ((query . ""))
          ((query . ""))
          ((query . "value"))
          ((query . "value=another"))
          ((query . "value") (q2 . "v2"))
          ((query . "value") (q2 . "v2"))
          ((query . "value"))
          ((query . "value") (q2 . ""))
          ((query . "value") (q2 . "")))
        (map PARSE-QUERY-STRING
             '(#f
               ""
               "="
               "=value"
               "query"
               "query="
               "query=value"
               "query=value=another"
               "query=value&q2=v2"
               "&query=value&&q2=v2&"
               "query=value&=v2"
               "query=value&q2"
               "query=value&&q2=&&&")))

(expect string-split-plain
        '(("one" "two")
          ("one" "two")
          ("one" "two")
          ("one")
          ("one")
          ())
        (map (lambda (s) (string-split s #\;))
             '("one;two"
               "one;;two"
               ";one;;two;;;"
               "one"
               ";one;"
               "")))
(expect string-split-start
        '(("one" "two")
          ("two")
          ("wo")
          ())
        (map (lambda (p) (string-split (car p) #\; (cdr p)))
             '(("one;two" . 0)
               ("one;two" . 4)
               ("one;two" . 5)
               ("one;two" . 99))))

(expect java-retrieve-static-object
        '(#t #t #t)
        (map ->boolean
             (list (java-retrieve-static-object '|java.lang.Boolean.TRUE|)
                   (java-retrieve-static-object "java.lang.Boolean.TRUE")
                   (java-retrieve-static-object '|java.lang.Boolean| '|TRUE|))))
