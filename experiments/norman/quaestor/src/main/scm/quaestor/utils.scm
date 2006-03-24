;; SISC library module for Quaestor

(import s2j)
(require-library 'sisc/libs/srfi/srfi-1)
(require-library 'sisc/libs/srfi/srfi-13)


(module utils
 (sexp->xml
  iterator->list
  jlist->list
  enumeration->list
  java-retrieve-static-object
  is-java-type?
  url-decode-to-jstring
  report-exception
  chatter
  parse-http-accept-header
  parse-query-string
  )

 (import* srfi-1
          remove)
 (import* srfi-13
          string-index
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
 (define (jlist->list jlist)
   (define-generic-java-method iterator)
   (iterator->list (iterator jlist)))

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
 ;; by MAKE-FC, use the given NEW-STATUS.  When the MAKE-FC handler
 ;; processes this, it will _not_ output debugging information.  That is,
 ;; this is for throwing `normal' errors.
 (define (report-exception location new-status fmt . args)
   (let ((msg (apply format `(#f ,fmt ,@args))))
     (error location (cons new-status msg))))


 ;; Mostly for debugging.
 ;; Accumulate remarks, to supply later.
 ;;     (chatter fmt . args)  ; Format a message.
 ;;     (chatter)             ; return list of messages, or #f if none,
 ;;                           ; and clear list
 (define chatter
   (let ((l '()))
     (lambda msg
       (cond ((and (null? msg)
                   (null? l))
              #f)
             ((null? msg)
              (let ((r (reverse l)))
                (set! l '())
                (apply string-append r)))
             (else
              (set! l
                    (cons (apply format `(#f
                                          ,(string-append (car msg) "~%")
                                          ,@(cdr msg)))
                          l)))))))


 ;; Parse an HTTP Accept header into a list of acceptable MIME types.
 ;; The JHEADER is a Java string in the format required by RFC 2616, that is,
 ;; as a comma-separated list of media-type tokens:
 ;;   media-type     = type "/" subtype *( ";" parameter )
 ;;   parameter      = attribute "=" value
 ;;   attribute      = token
 ;;   value          = token | quoted-string
 ;; There's a good discussion, albeit in a Python context, at
 ;; <http://www.xml.com/pub/a/2005/06/08/restful.html>
 ;; The result is sorted in order of descending weight parameter, though
 ;; the sort isn't stable, so order of equally-weighted types isn't preserved.
 (define parse-http-accept-header
   (let ((commas #f)
         (parameters #f))
     (define-generic-java-methods
       split
       compile
       matcher
       matches
       group
       parse-float)
     (define-java-classes
       <java.util.regex.pattern>
       <java.lang.float>)
     (lambda (jheader)
       (if (not commas)
           (begin (set! commas
                        (compile (java-null <java.util.regex.pattern>)
                                 (->jstring " *, *")))
                  (set! parameters
                        (compile (java-null <java.util.regex.pattern>)
                                 (->jstring "\([^;]*\); *q=\([0-9.]+\).*")))))
       (let ((pairs (map (lambda (js)
                           (let ((js-matcher (matcher parameters js)))
                             (if (->boolean (matches js-matcher))
                                 (cons (->string
                                        (group js-matcher (->jint 1)))
                                       (string->number
                                        (->string
                                         (group js-matcher (->jint 2)))))
                                 (cons (->string js) 1))))
                         (->list (split commas jheader)))))
         (remove (lambda (s) (= (string-length s) 0))
                 (map car
                      (sort-list pairs
                                 (lambda (a b)
                                   (> (cdr a) (cdr b))))))))))

 ;; Simple implementation of heapsort (?).  Probably not massively efficient
 ;; but there isn't a SRFI for sorting yet.  Sort the given list L with
 ;; respect to the given sorting function, <=.
 ;;
 ;; NOTE: this isn't a stable sort.
 (define (sort-list l <=)
   (define (merge-lists ina inb)
     (let loop ((res '())
                (a ina)
                (b inb))
       (cond
        ((null? a)
         (append (reverse res) b))
        ((null? b)
         (append (reverse res) a))
        (else
         (if (<= (car a) (car b))
             (loop (cons (car a) res)
                   (cdr a)
                   b)
             (loop (cons (car b) res)
                   a
                   (cdr b)))))))

   (define (partition-list pe inl)
     (let loop ((pa '())
                (pb '())
                (l inl))
       (if (null? l)
           (values pa pb)
           (if (<= (car l) pe)
               (loop (cons (car l) pa) pb (cdr l))
               (loop pa (cons (car l) pb) (cdr l))))))

   (case (length l)
     ((0 1) l)
     ((2) (if (<= (car l) (cadr l))
              l
              (list (cadr l) (car l))))
     (else
      (let ((pe (car l)))
        (call-with-values
            (lambda () (partition-list pe (cdr l)))
          (lambda (left right)
            (merge-lists (sort-list left <=)
                         (cons pe (sort-list right <=)))))))))

;; parse-query-string string -> pair-of-strings
;;
;; Given a query string "keyword=value", parse it into a pair of strings
;; representing the text before and after the equals sign.  If either
;; is missing, replace it with #f.
;;
;; Very simple-minded.  Query strings can be more generic than this,
;; but they're not, in this application.
(define (parse-query-string qs)
  (cond ((not qs)
         '(#f . #f))
        ((string-index qs #\=)
         => (lambda (index)
              (let ((k (substring qs 0 index))
                    (v (substring qs (+ index 1) (string-length qs))))
                (cons (if (> (string-length k) 0)
                          k
                          #f)
                      (if (> (string-length v) 0)
                          v
                          #f)))))
        ((= (string-length qs) 0)
         '(#f . #f))
        (else
         (cons qs #f))))


)
