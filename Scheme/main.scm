; Nom / Name             : Kien Do
; Cours / Course         : CSI2520 - Paradigmes de programmation / Programming Paradigms
; Professeur / Professor : Robert Laganière, uottawa.ca
; Session / Semester     : Hiver 2022 / Winter 2022
; Projet / Project       : Merging DBSCAN-Clustered Taxi Geolocation Partitions

#lang scheme

; =====================================  Import partition files  ===================================== ;

; reads a file given the filename as input
(define (readlist filename)
 (call-with-input-file filename
  (lambda (in)
    (read in))))

; returns a list of partitions from imported "partition##.scm" files where the partition format is
; (PARTITION_ID, POINT_ID, X, Y, CLUSTER_ID)
(define (import)
  (let ((p65 (readlist "partition65.scm"))
        (p74 (readlist "partition74.scm")) 
        (p75 (readlist "partition75.scm"))
        (p76 (readlist "partition76.scm"))
        (p84 (readlist "partition84.scm")) 
        (p85 (readlist "partition85.scm"))
        (p86 (readlist "partition86.scm")))
    (append p65 p74 p75 p76 p84 p85 p86)))

; ====================================  Merge partition clusters  ==================================== ;

; merges the clusters from the partitions extracted from (import) and excludes overlapping points
; returns list L containing all the merged partitions
(define (mergeClusters partitions)
  (define L (remove-partition-id partitions))
  (populate-cluster-list L '())
  ;(populate-cluster-list (remove-partition-id partitions) '()) ; alternate function
)

; populates a cluster list using tail-recursion where the accumulator list starts as '()
; non-overlapping clusters are inserted into cluster list; overlapping clusters are used for relabelling
; returns a list of merged, non-overlapping clusters from all the extracted partitions
(define (populate-cluster-list L AccL)
  (cond
    ((equal? L '()) (reverse AccL)) ; end case; returns (reverse AccL) due to front-insertion of (cons x y)
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

; ==================================  Helper/auxiliary predicates  =================================== ;

; returns OldClusterID (Cluster ID to be changed) as a list if a GPS point exists in a cluster list
; else, return '() if the given point does not overlap with any points in cluster list
; NOTE: Two points overlap if their PointIDs are the same
; Assume that two points having the same PointIDs implies that the X and Y coordinates are also the same
(define (overlap X L)
  (cond
    ((null? L) '())
    ((= (car X) (car (car L))) (list (last (car L)))) ; return (OLD_CLUSTER_ID) - aka ID to-be-changed
    (else (overlap X (cdr L)))
  )
)

; takes a list and a number; returns a new list where the last number is replaced with the given number
; in other words, given '(POINT_ID, X, Y, CLUSTER_ID) and NEW_ID, return '(POINT_ID, X, Y, NEW_ID)
; acts as a helper procedure for relabel procedure
(define (replace-last L New-id)
  (if (null? (cdr L))
      (list New-id)
      (cons (car L) (replace-last (cdr L) New-id)) ; alternate: (append (list(car L)) (replace-last (cdr L) New-id))))
  )
)

; given a list of '(PARTITION_ID, POINT_ID, X, Y, CLUSTER_ID), return a list of '(POINT_ID, X, Y, CLUSTER_ID)
; that is, remove PARTITION_ID parameter / return the same list without the PARTITION_ID parameter
(define (remove-partition-id L)
  (cond
    ((null? L) '())
    (else (cons (cdr (car L)) (remove-partition-id (cdr L)))
    )
  )
)

; ==================================  Output results to text file  =================================== ;

; runs the program and saves the output to a textfile that contains all of the partition-merged clusters
; produced by the procedure (mergeClusters (import))
(define (saveList filename L)
  (call-with-output-file filename
    (lambda (out)
      (write L out))))
(saveList "clusters.txt" (mergeClusters (import)))
