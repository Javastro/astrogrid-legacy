;; SISC library module for Quaestor

(import s2j)
(require-library 'sisc/libs/srfi/srfi-13)


(module utils
 (sexp->xml
  iterator->list
  ;jlist->list
  enumeration->list
  java-retrieve-static-object
  is-java-type?
  url-decode-to-jstring
  error-with-status
  )

 (import* srfi-13
          string-index-right)


 ;; Given a list of SExps, SEXP-LIST, return this translated into a string
 ;; Takes two optional arguments: the first specifies a list of
 ;; elements which are to be formatted (ie, have linebreaks inserted)
 ;; as `block' elements (like <div> in HTML), and the second a list
 ;; which should be formatted as `para' elements (like HTML <p>).
 ;; Either may be given as 'ALL to format all like this.
 ;;
 ;; If the second element of the list is of the form (@ LIST ...), then the
 ;; LIST is a two-element list of (ATTRIBUTE VALUE)
 (define (sexp->xml s . opts)
   (let ((block-elems (and (> (length opts) 0)
                           (car opts)))
         (para-elems  (and (> (length opts) 1)
                           (cadr opts))))
     (cond
      ((string? s)
       s)

      ((symbol? s)
       (symbol->string s))

      ((number? s)
       (format #f "~a" s))

      ((list? s)
       (if (and (> (length s) 1)
                (list? (cadr s))
                (eq? (caadr s) '@))
           (sexp->xml-write* (car s)
                             (cdadr s)
                             (cddr s)
                             block-elems
                             para-elems)
           (sexp->xml-write* (car s)
                             #f
                             (cdr s)
                             block-elems
                             para-elems)))
      (else
       (error (format #f
                      "Unrecognised type of object (~s) in sexp->str"
                      s))))))

 ;; Write out an element with attributes, and formatting depending on the
 ;; element `type'.
 ;; GI: a symbol containing the name of the element to be written
 ;; ATTLIST: a list of two-element lists, each containing (attribute value),
 ;;     as either symbols or strings
 ;; CONTENT: a sexp representing the element content
 ;; BLOCK-ELEMENT-LIST and PARA-ELEMENT-LIST: either a list of symbols
 ;;     or the symbol 'ALL.  If the GI is found in one of the lists, or the
 ;;     relevant variable has the value 'ALL, then the element is formatted
 ;;     as a block element, a paragraph element, or an inline element if it
 ;;     is in neither list.
 ;; Internal function
 (define (sexp->xml-write* gi attlist content
                           block-element-list para-element-list)
   (define block-elements
     (or block-element-list
         '(html head body div ul ol)))
   (define para-style
     (or para-element-list
         '(p title link h1 h2 h3 h4 h5 h6 li)))
   (if (null? content)
       (format
        #f
        (cond
         ((or (eq? block-elements 'ALL)
              (memq gi block-elements)
              (eq? para-style 'ALL)
              (memq gi para-style))
          "<~a~a/>~%")
         (else
          "<~a~a/>"))
        gi
        (if attlist
            (apply string-append
                   (map (lambda (p)
                          (format #f " ~a='~a'" (car p) (cadr p)))
                        attlist))
            ""))
       (format
        #f
        (cond
         ((or (eq? block-elements 'ALL)
              (memq gi block-elements))
          "<~a~a>~%~a</~a>~%~%")
         ((or (eq? para-style 'ALL)
              (memq gi para-style))
          "<~a~a>~a</~a>~%")
         (else
          "<~a~a>~a</~a>"))
        gi
        (if attlist
            (apply string-append
                   (map (lambda (p)
                          (format #f " ~a='~a'" (car p) (cadr p)))
                        attlist))
            "")
        (apply string-append
               (map (lambda (x)
                      (sexp->xml x block-elements para-style))
                    content))
        gi)))

 ;; Given a Java iterator, JITER, extract each of its contents into a list
 (define (iterator->list jiter)
   (define-generic-java-methods
     (has-next? |hasNext|)
     next)
   (let loop ((l '()))
     (if (->boolean (has-next? jiter))
         (loop (cons (next jiter) l))
         (reverse l))))

 ;; The same, but taking a Java List as input
 ;; No, just use built-in ->list
;;  (define (jlist->list jlist)
;;    (define-generic-java-method iterator)
;;    (iterator->list (iterator jlist)))

 ;; The same, but taking a Java Enumeration as input
 (define (enumeration->list enum)
   (define-generic-java-methods
     has-more-elements
     next-element)
   (if (->boolean (has-more-elements enum))
       (let ((n (next-element enum)))
         (cons n
               (enumeration->list enum)))
       '()))

 ;; Retrieve a Java static object by its full Java name, which may be a symbol
 ;; such as '|java.lang.System.out|, or a string
 (define (java-retrieve-static-object javaobj)
   (let ((javaobjname (if (string? javaobj)
                          javaobj
                          (symbol->string javaobj))))
     (let ((dot (string-index-right javaobjname #\.)))
       (and dot
            (let ((classname (substring javaobjname 0 dot))
                  (objname (substring javaobjname
                                      (+ 1 dot)
                                      (string-length javaobjname))))
              ((generic-java-field-accessor (string->symbol objname))
               (java-null (java-class (string->symbol classname)))))))))


 ;; Verifies that a JOBJECT is an instance of a Java class CLASS (which
 ;; can be a class, a string, or a symbol).  Returns true if so, #f it
 ;; it's not, or if it's not a Java object at all.
 (define (is-java-type? jobject class)
   (if (java-object? jobject)
       (->boolean ((generic-java-method '|isInstance|)
                   (if (java-class? class)
                       class
                       (java-class (if (string? class)
                                       (string->symbol class)
                                       class)))
                   jobject))
       #f))

 ;; Given a string S, return the URL-decoded string as a jstring
 (define (url-decode-to-jstring s)
   (define-java-classes
     (<url-decoder> |java.net.URLDecoder|))
   (define-generic-java-method
     decode)
   (decode (java-null <url-decoder>)
           (->jstring s)
           (->jstring "UTF-8")))

 ;; Variant of ERROR, which can be called in a region handled by the failure
 ;; continuation created by MAKE-FC.  Throw an error, in the given LOCATION,
 ;; with a message formatted with the given FMT and ARGS.  However instead
 ;; of exiting with the status code defaulted when the fc was created
 ;; by MAKE-FC, use the given NEW-STATUS.
 (define (error-with-status location new-status fmt . args)
   (let ((msg (apply format `(#f ,fmt ,@args))))
     (error location (cons new-status msg))))

)
