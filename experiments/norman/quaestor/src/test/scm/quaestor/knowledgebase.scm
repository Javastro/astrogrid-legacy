;; test cases for the knowledgebase module

(require-library 'quaestor/knowledgebase)
(import knowledgebase)

(require-library 'quaestor/jena)
(import* jena rdf:new-empty-model)

(expect kb-create-1
        #t
        (not (not (kb:new "try1"))))
(expect kb-create-2
        #t
        (not (not (kb:new "try2"))))
(expect-failure kb-create-duplicate     ;can't create duplicates
                (kb:new "try1"))

(expect kb-predicate
        '(#t #f #f #f)
        (map kb:knowledgebase?
             `(,(kb:get "try1")
               "hello"
               ,(lambda () "hello")
               ,(lambda (arg) "hello"))))

(expect kb-names
        '(try2 try1)                    ;order isn't significant
        (kb:get-names))

(expect kb-get-1
        #t
        (not (not (kb:get "try1"))))
(expect kb-get-2
        #t
        (not (not (kb:get "try2"))))
(expect kb-get-3
        #f
        (kb:get "try3"))

(expect kb-discard
        #t
        (kb:knowledgebase? (kb:discard "try2")))
(expect kb-reget-1
        #t
        (not (not (kb:get "try1"))))
(expect kb-reget-2
        #f
        (kb:get "try2"))

(define m (kb:get "try1"))
(expect kb-retrieve-empty
        #f
        (m 'get-model))
(expect kb-has-empty
        #f
        (m 'has-model))
(m 'add-abox 's1 (rdf:new-empty-model))
(expect kb-has-non-empty
        #t
        (not (not (m 'has-model))))
(expect kb-retrieve-non-empty
        #t
        (not (not ((kb:get "try1") 'get-model))))

(expect kb-has-model-string
        #t
        (not (not (m 'has-model "s1"))))
(expect kb-has-model-symbol
        #t
        (not (not (m 'has-model 's1))))
(expect kb-has-no-submodel
        #f
        (m 'has-model 's2))
