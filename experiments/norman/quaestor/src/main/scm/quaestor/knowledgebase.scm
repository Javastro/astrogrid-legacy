;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; Support for the `knowledgebase' for Quaestor

(import s2j)

(import quaestor-support)

(require-library 'quaestor/jena)
(require-library 'quaestor/utils)
(require-library 'util/lambda-contract)

(require-library 'sisc/libs/srfi/srfi-1)
(import* srfi-1
         filter)

(module knowledgebase
  (kb:new
   kb:get
   kb:discard
   kb:get-names
   kb:knowledgebase?)

  (import jena)
  (import* utils
           is-java-type?
           collection->list)

  (define (jena-model? x)
    (is-java-type? x '|com.hp.hpl.jena.rdf.model.Model|))
  (define (alist? x)
    (and (list? x)
         (let loop ((l x))
           (if (null? l)
               #t
               (and (pair? (car l))
                    (loop (cdr l)))))))
  (define (jstring? x)
    (is-java-type? x '|java.lang.String|))

  ;; A `knowledgebase' is a named model, consisting of a number of named
  ;; `submodels'.  When any of these are updated, the new submodel is
  ;; merged with the main model.
  ;;
  ;; The knowledgebase object that is returned is a function which takes
  ;; a number of subcommands.

  ;; The model list is a list of pairs (name-symbol . knowledgebase-proc)
  (define _model-list '())

  ;; We'd like to synchronize, below, on _model-list, but we can't
  ;; because it's not a Java object, so create a dummy Java object as
  ;; a lock.
  (define _model-list-lock-object (->jstring ""))

  ;; KB:GET : string -> knowledgebase or false
  ;; Retrieve the knowledgebase with the given name, which is
  ;; a symbol or string.  Return #f if there is no KB with this name.
  (define/contract (kb:get (kb-name string?)
                           -> (lambda (x)
                                (or (not x)
                                    (kb:knowledgebase? x))))
    (or kb-name
        (error 'kb:get "bad call to kb:get with object ~s" kb-name))
    (cond ((assoc kb-name _model-list)
           => cdr)
          (else
           #f)))

  ;; KB:NEW string : -> knowledgebase
  ;;
  ;; Create a new knowledgebase from scratch, registering it with the given
  ;; KB-NAME (a string).  It must not already exist.
  ;; Return the new knowledgebase.  Either succeeds or throws an error.
  (define/contract (kb:new (kb-name string?) -> kb:knowledgebase?)
    (or kb-name
        (error 'kb:new "bad call to kb:new with object ~s" kb-name))
    (if (kb:get kb-name)
        (error 'kb:new
               "bad call to kb:new: knowledgebase ~a already exists"
               kb-name))
    (let ((kb (make-kb kb-name)))
      (java-synchronized _model-list-lock-object
        (lambda ()
          (set! _model-list
                (cons (cons kb-name kb)
                      _model-list))))
      kb))

  ;; KB:GET-NAMES : -> list
  ;;
  ;; Returns a list of available knowledgebases.  Returns the names
  ;; as strings, or a null list if there are none.
  (define (kb:get-names)
    (map car _model-list))

  ;; KB:DISCARD : string -> knowledgebase/#f
  ;;
  ;; Remove a given knowledgebase from the list.  Returns it, or
  ;; returns #f if no such knowledgebase existed
  (define/contract (kb:discard (kb-name string?)
                               -> (lambda (x)
                                    (or (not x)
                                        (kb:knowledgebase? x))))
    (let ((ret #f))
      (java-synchronized _model-list-lock-object
        (lambda ()
          (let loop ((new-list '())
                     (l _model-list))
            (cond ((null? l)
                   (set! _model-list new-list))
                  ((string=? kb-name (caar l)) ;found it
                   (set! ret (cdar l))
                   (loop new-list (cdr l)))
                  (else
                   (loop (cons (car l) new-list)
                         (cdr l))))
            ret)))))

  ;; add-submodel : list symbol jena-model boolean -> list
  ;; Add a new submodel to the model.  Returns the original or an updated
  ;; submodel list, or #f on any errors (there's nothing which triggers #f
  ;; at present, but it's documented to do this just in case)
  (define/contract (add-submodel (submodel-list      list?)
                                 (new-submodel-name  string?)
                                 (new-submodel-model jena-model?)
                                 (tbox?              boolean?)
                                 -> list?)
    (java-synchronized _model-list-lock-object
      (lambda ()
        (cond ((assoc new-submodel-name submodel-list)
               => (lambda (sm-list)     ;already exists
                    (set-cdr! sm-list
                              (cons tbox? new-submodel-model))
                    submodel-list))
              (else                     ;create a new one by consing
                                        ;an entry to the front
               (cons `(,new-submodel-name ,tbox? . ,new-submodel-model)
                     submodel-list))))))
  

  ;; make-kb symbol -> knowledgebase
  ;;
  ;; Create a new knowledgebase.  The knowledgebase is a procedure.
  ;; The model and submodels may be retrieved, and new ones added, by
  ;; calling the procedure with a command as the second argument.
  ;;
  ;;    (kb 'add-abox/tbox SUBMODEL-NAME SUBMODEL)
  ;;        Add or update a abox/tbox with the given name.
  ;;        SUBMODEL-NAME may be a string or a symbol.  Return #t on success
  ;;    (kb 'get-inferencing-model)
  ;;        As with GET-MODEL, except that it is an inferencing model.
  ;;        Return #f on error.
  ;;    (kb 'get-model)
  ;;        Return the model, or #f if none exists.
  ;;    (kb 'get-model SUBMODEL-NAME)
  ;;        Return the named submodel, or #f if none exists.
  ;;        SUBMODEL-NAME may be a symbol or a string.
  ;;    (kb 'get-model-abox/tbox)
  ;;        Return the merged abox/tbox submodels, or #f if there are none.
  ;;    (kb 'get-metadata)
  ;;        Return the model's metadata, as a Jena Model.
  ;;    (kb 'get-name)
  ;;        Return the knowledgebase's name, as a symbol
  ;;    (kb 'has-model [SUBMODEL-NAME])
  ;;        Return true if the named (sub)model exists, that is, if
  ;;        (kb 'get-model [SUBMODEL-NAME]) would succeed.  Return #f otherwise.
  ;;    (kb 'set-metadata jstream base-uri content-type)
  ;;        Set the metadata to the arbitrary string INFO.
  ;;    (kb 'info)
  ;;        Return alist with info
  (define (make-kb kb-name)
    (let ((submodels '()) ;a list of (name tbox? . submodel) pseudo-lists
          (myname kb-name)
          (myuri #f)
          (metadata #f)                 ;a Model
          (merged-model #f)
          (merged-tbox #f)
          (merged-abox #f)
          (inferencing-model #f)
          (sync-object (->jstring "")))

      (define-syntax kb-synchronized
        (syntax-rules ()
          ((_ form . forms)
           (java-synchronized sync-object
             (lambda ()
               form . forms)))))

      (define (clear-memos)
        (set! merged-model #f)
        (set! merged-abox #f)
        (set! merged-tbox #f)
        (set! inferencing-model #f))

      ;; Return the abox or tbox: BOXNAME is either 'tbox or 'abox.
      ;; Caches result in merged-abox/tbox.
      (define (get-box boxname)
        (kb-synchronized
         (let* ((tbox? (eq? boxname 'tbox))
                (models (filter (if tbox?
                                    (lambda (x) (cadr x))
                                    (lambda (x) (not (cadr x))))
                                submodels)))
           (cond ((null? models)
                  #f)
                 (tbox?
                  (or merged-tbox
                      (set! merged-tbox
                            (rdf:merge-models (map cddr models))))
                  merged-tbox)
                 (else
                  (or merged-abox
                      (set! merged-abox
                            (rdf:merge-models (map cddr models))))
                  merged-abox)))))

      ;; Add the given submodel to the model.  BOXNAME is either 'tbox or 'abox.
      (define (add-box name submodel boxname)
        (kb-synchronized
         (set! submodels
               (add-submodel submodels
                             name
                             submodel
                             (eq? boxname 'tbox)))
         (clear-memos)))

      ;; Return a new inferencing model, using the settings in METADATA.
      ;; Return false on errors
      (define (create-inferencing-model)
        (let ((tbox (get-box 'tbox))
              (abox (get-box 'abox)))
          (define-java-classes
            (<factory> |com.hp.hpl.jena.rdf.model.ModelFactory|))
          (define-generic-java-methods
            create-inf-model to-string)
          (let* ((levelres
                  (and metadata
                       myuri
                       (rdf:select-statements
                        metadata
                        myuri
                        "http://ns.nxg.me.uk/quaestor#requiredReasoner"
                        #f)))
                 (levelp (and levelres
                              (not (null? levelres))
                              (rdf:get-property-on-resource
                               (car levelres)
                               "http://ns.nxg.me.uk/quaestor#level")))
                 (level (and levelp
                             (->string (to-string levelp)))))
            (chatter "Level for ~s is ~s" myuri level)
            (cond ((and tbox abox)
                   (create-inf-model (java-null <factory>)
                                     (rdf:get-reasoner level)
                                     tbox
                                     abox))
                  (tbox
                   (create-inf-model (java-null <factory>)
                                     (rdf:get-reasoner level)
                                     tbox))
                  (else
                   #f)))))

      (lambda (cmd . args)
        (case cmd

          ;;;;;;;;;;
          ;; Commands which explicitly mutate the knowledgebase
          
          ((add-abox add-tbox)
           ;; (kb 'add-abox/tbox SUBMODEL-NAME SUBMODEL)
           (if (not (and (= (length args) 2)
                         (string? (car args))))
               (error 'make-kb
                      "Bad call to add-abox/tbox: wrong number or type of args in ~s"
                      args))
           (add-box (car args)
                    (cadr args)
                    (if (eq? cmd 'add-tbox)
                        'tbox
                        'abox))
           #t)

          ((set-metadata)
           ;; (kb 'set-metadata jinput-stream base-uri content-type-string)
           ;; (kb 'set-metadata jstring base-uri <anything>)
           ;; Set model metadata to the Model read from the given reader.
           ;; If the first argument is a string, then create a model with
           ;; this as a dc:description.
           (if (not (= (length args) 3))
               (error 'make-kb
                      "bad call to set-metadata: wrong number of args in ~s"
                      args))
           (let ((input (car args))
                 (base-uri (cadr args))
                 (content-type (caddr args)))
             (let ((m (get-metadata-from-source input base-uri content-type)))
               (kb-synchronized
                (set! myuri base-uri)
                (set! metadata m)))))

          ;;;;;;;;;;
          ;; Commands which implicitly mutate the knowledgebase, via memoisation

          ((get-model)
           ;; (kb 'get-model [SUBMODEL-NAME])
           ;; Return newly-merged model or #f if no models exist
           (case (length args)
             ((0)
              (kb-synchronized
               (cond ((null? submodels)
                      #f)
                     (merged-model)      ;already merged/memoized
                     (else
                      (set! merged-model
                            (rdf:merge-models (map cddr submodels)))
                      merged-model))))
             ((1)
              (let ((sm (assoc (car args) submodels)))
                (and sm (cddr sm))))
             (else
              (error 'make-kb
                     "Bad call to get: wrong number of args in ~a"
                     args))))

          ((get-inferencing-model)
           ;; return #f on error
           (kb-synchronized
            (if (not inferencing-model)
                (set! inferencing-model
                      (create-inferencing-model))))
           inferencing-model)

          ((get-model-tbox get-model-abox)
           ;; (kb 'get-model-tbox/abox)
           ;; return merged models or #f if none
           (or (null? args)
               (error 'make-kb
                      "Bad call to get-model-tbox/abox: wrong no args ~s" args))
           (get-box (if (eq? cmd 'get-model-tbox)
                        'tbox
                        'abox)))

          ;;;;;;;;;;
          ;; Commands which do not mutate the knowledgebase

          ((has-model)
           ;; (kb 'has-model [SUBMODEL-NAME])
           ;; With an argument, this is redundant with
           ;; (kb 'get-model SUBMODEL-NAME); but without an argument,
           ;; it saves the redundant merging of the models,
           ;; as well as being more intelligible.
           (cond ((= (length args) 0)
                  (not (null? submodels)))
                 ((= (length args) 1)
                  (assoc (car args) submodels))
                 (else
                  (error 'make-kb
                         "Bad call to has-model: wrong no. args ~s" args))))

          ((get-metadata)
           metadata)

          ((get-name)
           myname)

          ((info)
           (list (cons 'submodels
                       (map (lambda (sm)
                              `((name . ,(car sm))
                                (tbox . ,(cadr sm))
                                (namespaces
                                 . ,(get-model-namespaces (cddr sm)))))
                            submodels))))

          (else
           (error 'make-kb "impossible command for knowledgebase: ~s" cmd))))))

  ;; KB:KNOWLEDGEBASE? : object -> boolean
  ;;
  ;; Returns true if the object is a knowledgebase
  (define (kb:knowledgebase? object)
    (and (procedure? object)
         (with/fc
             (lambda (m e) #f)
           (lambda ()
             (string? (object 'get-name))))))

  ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
  ;;
  ;; Helpers used above

  ;; get-metadata-from-source jinput-stream base-uri content-type
  ;; get-metadata-from-source jstring base-uri <ignored>
  ;;
  ;; Given an input stream, read a model from it; given a string, make
  ;; a model which has this as a dc:description.
  (define/contract (get-metadata-from-source
                    (source (or (jstring? source)
                                (is-java-type? source '|java.io.InputStream|)))
                    (base-uri string?)
                    content-type
                    -> jena-model?)
    (if (jstring? source)
        (let ((new-model (rdf:new-empty-model))) ;content is string
          (define-generic-java-methods
            create-resource create-statement
            create-literal add)
          (add new-model
               (create-statement new-model
                                 (create-resource new-model
                                                  (->jstring base-uri))
                                 (java-retrieve-static-object
                                  '|com.hp.hpl.jena.vocabulary.DC.description|)
                                 (create-literal new-model
                                                 source
                                                 (->jstring "en")))))
        (rdf:ingest-from-stream/language source ;content is model
                                         base-uri
                                         content-type)))

  ;; Return an alist consisting of (prefix . namespace) mappings for
  ;; the given model.
  (define/contract (get-model-namespaces (model jena-model?)
                                         -> alist?)
    (define-generic-java-methods
      get-ns-prefix-map
      entry-set
      get-key
      get-value)
    (map (lambda (mapentry)
           (cons (->string (get-key mapentry))
                 (->string (get-value mapentry))))
         (collection->list (entry-set (get-ns-prefix-map model)))))

  )
