#|
Nom / Name             : Kien Do
Cours / Course         : CSI2520 - Paradigmes de programmation / Programming Paradigms
Professeur / Professor : Robert Laganière, uottawa.ca
Session / Semester     : Hiver 2022 / Winter 2022
Projet / Project       : Merging DBSCAN-Clustered Taxi Geolocation Partitions
|#

#lang scheme

; =====================================  Import partition files  ===================================== ;

; reads a file given the filename as input
(define (readlist filename)
 (call-with-input-file filename
  (lambda (in)
    (read in))))

; returns a list of partitions from "partition##.scm" files where the partition format is
; (PARTITION_ID, POINT_ID, X, Y, CLUSTER_ID).
(define (import)
  (let ((p65 (readlist "partition65.scm"))
        (p74 (readlist "partition74.scm")) 
        (p75 (readlist "partition75.scm")))
    (append p65 p74 p75)))

; ====================================  Merge partition clusters  ==================================== ;

(define (mergeClusters partitions)
  (remove-partition-id partitions)
)

; returns a list (POINT_ID, X, Y, CLUSTER_ID) given (PARTITION_ID, POINT_ID, X, Y, CLUSTER_ID)
; removes PARTITION_ID parameter / returns a list without the PARTITION_ID parameter
(define (remove-partition-id L)
  (cond
    ((null? L) '())
    (else (cons (cdr (car L)) (remove-partition-id (cdr L)))
    )
  )
)

(import)
(mergeClusters (import))


; ==================================  Helper/auxiliary predicates  =================================== ;


; ==================================  Output results to text file  =================================== ;