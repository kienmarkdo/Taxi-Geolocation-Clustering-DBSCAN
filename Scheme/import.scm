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
        (p75 (readlist "partition75.scm"))
        (p76 (readlist "partition76.scm"))
        (p84 (readlist "partition84.scm")) 
        (p85 (readlist "partition85.scm"))
        (p86 (readlist "partition86.scm"))
        )
    (append p65 p74 p75 p76 p84 p85 p86)))

; ====================================  Merge partition clusters  ==================================== ;

(define (mergeClusters partitions)
  (define L (remove-partition-id partitions))
  (populate-cluster-list L '())
  ;(populate-cluster-list (remove-partition-id partitions) '())
)

; populates an initially-empty cluster list by adding clusters to or relabelling clusters in cluster list
; returns a list of non-overlapping clusters using tail-recursion
(define (populate-cluster-list L AccL)
  (cond
    ((equal? L '()) (reverse AccL)) ; end case of tail-recursion; return reverse of AccL due to cons
    ((equal? (overlap (car L) AccL) '()) (populate-cluster-list (cdr L) (cons (car L) AccL))) ; if no overlap
    (else (populate-cluster-list (cdr L) (relabel (car (overlap (car L) AccL)) (last (car L)) AccL))) ; if overlap
  )
)



; relabels the points of current list of clusters having OldClusterID with NewClusterID
(define (relabel old-id new-id L)
  (cond
    ((null? L) '())
    ((= old-id (last (car L))) (cons (replace-last (car L) new-id) (relabel old-id new-id (cdr L))))
    (else (cons (car L) (relabel old-id new-id (cdr L))))
  )
)
; (relabel 33 77 '((1 2.2 3.1 33) (2 2.1 3.1 22) (3 2.5 3.1 33) (4 2.1 4.1 33) (5 4.1 3.1 30))) ; test case


; returns NewClusterID if a given GPS point exists in the given cluster list
; else, return null if the given point does not overlap with any points in cluster list
; NOTE: Two points overlap if their PointIDs are the same; Assume that X and Y will also be the same
(define (overlap X L)
  (cond
    ((null? L) '())
    ((= (car X) (car (car L))) (list (last (car L)))) ; return (OLD_ID)
    (else (overlap X (cdr L)))
  )
)
;(overlap '(2 2.1 3.1 99) '((1 2.2 3.1 33) (2 2.1 3.1 22) (3 2.5 3.1 33) (4 2.1 4.1 33) (5 4.1 3.1 30))) ; 99
;(overlap '(99 99 99 99) '((1 2.2 3.1 33) (2 2.1 3.1 22) (3 2.5 3.1 33) (4 2.1 4.1 33) (5 4.1 3.1 30))) ; false

; returns a list where the last element is replaced with a given element
; given (POINT_ID, X, Y, CLUSTER_ID) and NEW_ID, return (POINT_ID, X, Y, NEW_ID)
; helper procedure for relabel procedure
(define (replace-last L New-id)
  (if (null? (cdr L))
      (list New-id)
      (cons (car L) (replace-last (cdr L) New-id)))) ; alternate: (append (list(car L)) (replace-last (cdr L) New-id))))



; given a list of (PARTITION_ID, POINT_ID, X, Y, CLUSTER_ID), return a list of (POINT_ID, X, Y, CLUSTER_ID)
; removes PARTITION_ID parameter / returns a list without the PARTITION_ID parameter
(define (remove-partition-id L)
  (cond
    ((null? L) '())
    (else (cons (cdr (car L)) (remove-partition-id (cdr L)))
    )
  )
)

;(import)
"===================================================="
;(mergeClusters (import))
;(define list '((1 2.2 3.1 33) (2 2.1 3.1 22) (3 2.5 3.1 33) (4 2.1 4.1 33) (5 4.1 3.1 30) (1 2.2 3.1 100) (2 2.1 3.1 101) (3 2.5 3.1 102) (4 2.1 4.1 103)))
;(mergeClusters list)
;(overlap (car list) '((1 2.2 3.1 33) (2 2.1 3.1 22)))


; ==================================  Helper/auxiliary predicates  =================================== ;


; ==================================  Output results to text file  =================================== ;
(define (saveList filename L)
  (call-with-output-file filename
    (lambda (out)
      (write L out))))
(saveList "kienClusters.txt" (mergeClusters (import)))