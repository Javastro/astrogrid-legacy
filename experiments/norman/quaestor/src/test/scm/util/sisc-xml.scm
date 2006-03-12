;; Test cases for the util/siscsax.scm package

(require-library 'util/sisc-xml)
(import sisc-xml)

(expect-failure xml->sexp-1
        (xml->sexp/string ""))

(expect xml->sexp-2
        '(*TOP* (doc (p "hello") (p)))
        (xml->sexp/string "<doc><p>hello</p><p/></doc>"))

(expect xml->sexp-3
        '(*TOP* (doc (p "hello  ") (p)))
        (xml->sexp/string "<doc>  <p>hello  </p>  <p>   </p> </doc>"))

(expect xml->sexp-4
        '(*TOP* (doc (p (@ (class "simple"))
                        "content1")
                     (p (@ (att1  "value1") ; order of these two not significant
                           (class "complicated")))
                     (p "content2")))
        (xml->sexp/string "<doc><p class='simple'  >content1</p>  <p class='complicated' att1=\"value1\"/> <p>content2</p></doc>"))
