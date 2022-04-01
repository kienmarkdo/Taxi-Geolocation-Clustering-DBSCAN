#|
Nom / Name             : Kien Do
Cours / Course         : CSI2520 - Paradigmes de programmation / Programming Paradigms
Professeur / Professor : Robert Laganière, uottawa.ca
Session / Semester     : Hiver 2022 / Winter 2022
Projet / Project       : Merging DBSCAN-Clustered Taxi Geolocation Partitions
|#

#lang scheme

; ==================  Read partition files START  ==================
; Partition format: (PARTITION_ID, POINT_ID, X, Y, CLUSTER_ID).
(define (readlist filename)
 (call-with-input-file filename
  (lambda (in)
    (read in))))
(define (import)
  (let ((p65 (readlist "partition65.scm"))
        (p74 (readlist "partition74.scm")) 
        (p75 (readlist "partition75.scm")))
    (append p65 p74 p75)))
; ===================  Read partition files END  ===================


; =====================  Merge clusters START  =====================

;(define clusterList '())              ; list of clusters to be produced by mergeClusters function

; Merge clusters merge the partitions extracted in the previous section
;(define (mergeClusters (KB))  ; KB is a variable that stands for knowledge base
  
;)


; Helper function: extracts partition IDs from the import knowledge base
(define (extractPartitions KB)
  (cond
    ((equal? KB '()) '())                                                             ; if knowledge base is empty
    ((not(equal? KB '())) (append (list (car(car KB))) (extractPartitions (cdr KB)))) ; if KB not empty, append the current partition ID to the list of remaining partition IDs
  )
)

; Helper function: extracts cluster IDs from a given partition ID
;(define (extractClusters P)
;  (cond
;    ((equal? P))
;)

; ======================  Merge clusters END  ======================


; ======================  Save results START  ======================
; save the resulting list into a text file

; =======================  Save results END  =======================