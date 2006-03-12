;; Test cases for the util/sisc-xml.scm package

(require-library 'sisc/libs/srfi/srfi-39) ;parameter objects: form PARAMETERIZE

(require-library 'util/sisc-xml)
(import sisc-xml)

(expect-failure xml->sexp-1
        (sisc-xml:xml->sexp/string ""))

(expect xml->sexp-basic
        '(*TOP* (doc (p "hello") (p)))
        (sisc-xml:xml->sexp/string "<doc><p>hello</p><p/></doc>"))

(expect xml->sexp-basic-with-spaces
        '(*TOP* (doc (p "hello  ") (p)))
        (sisc-xml:xml->sexp/string "<doc>  <p>hello  </p>  <p>   </p> </doc>"))

(expect xml->sexp-basic-plus-whitespace
        '(*TOP* (doc "  " (p "hello  ") "  " (p "   ") " "))
        (parameterize ((sisc-xml:skip-whitespace? #f))
          (sisc-xml:xml->sexp/string
           "<doc>  <p>hello  </p>  <p>   </p> </doc>")))

;; The order of attributes is preserved by xml->sexp, though it's not
;; thus documented.
(expect xml->sexp-attributes
        '(*TOP* (doc (p (@ (class "simple"))
                        "content1")
                     (p (@ (class "complicated")
                           (att1  "value1")))
                     (p "content2")))
        (sisc-xml:xml->sexp/string "<doc><p class='simple'  >content1</p>  <p class='complicated' att1=\"value1\"/> <p>content2</p></doc>"))

(expect xml->sexp-namespacing
        '(*TOP* (doc (p (@ ((urn:example . a1) "v1")
                           (a2 "v2"))
                        "c1")
                     ((urn:example . q)
                      "c2")
                     ((urn:example2 . r)
                      "c3")))
        (sisc-xml:xml->sexp/string
         "<doc xmlns:x='urn:example'><p x:a1='v1' a2='v2'>c1</p><x:q>c2</x:q><r xmlns='urn:example2'>c3</r></doc>"))
