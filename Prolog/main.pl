/*
Nom / Name             : Kien Do
Cours / Course         : CSI2520 - Paradigmes de programmation / Programming Paradigms
Professeur / Professor : Robert Laganière, uottawa.ca
Session / Semester     : Hiver 2022 / Winter 2022
Projet / Project       : Merging DBSCAN-Clustered Taxi Geolocation Partitions
*/

/* ======================  Import partition files  ===================== */

% import/1
% creates a knowledge base of all partitions extracted from the partition##.csv files
%     using the predicate partition
% the fact base created by the import predicate can be viewed by using the query:
%     listing(partition)
% where the partition format is:
%     partition(PARTITION_ID, POINT_ID, X, Y, CLUSTER_ID)
import :-
    csv_read_file('partition65.csv', Data65, [functor(partition)]), maplist(assert, Data65),
    csv_read_file('partition74.csv', Data74, [functor(partition)]), maplist(assert, Data74),
    csv_read_file('partition75.csv', Data75, [functor(partition)]), maplist(assert, Data75),
    csv_read_file('partition76.csv', Data76, [functor(partition)]), maplist(assert, Data76),
    csv_read_file('partition84.csv', Data84, [functor(partition)]), maplist(assert, Data84),
    csv_read_file('partition85.csv', Data85, [functor(partition)]), maplist(assert, Data85),
    csv_read_file('partition86.csv', Data86, [functor(partition)]), maplist(assert, Data86)
    % listing(partition) % partition(PARTITION_ID, POINT_ID, X, Y, CLUSTER_ID)
.

/* =====================  Merge partition clusters  ==================== */

% mergeClusters/1
% merge the clusters from partition and exclude overlapping clusters
% returns list L containing all the marged partitions
mergeClusters(L) :-
    findall([P,X,Y,C], partition(_,P,X,Y,C), LL),
    populateClusterList(LL, [], LLL),
    reverseList(LLL, L)
.


% populateClusterList/3
% populateClusterList(Partitions list, Auxiliary list, Final cluster list)
% populates the cluster list by adding non-overlapping clusters or relabelling overlapping clusters

% base case: the initial partition list has been iterated through completely; return the final cluster list
populateClusterList([], A, A) :- !.

% both populateClusterList predicates below uses the if/then/else design pattern using the cut
% if current partition row overlaps with the cluster list (aka the auxiliary list, A)
%     relabel all overlapping points in cluster list to the current partition row's cluster ID
% else if the current partition row does not overlap with the points in cluster list
%     add/insert the point into cluster list     
populateClusterList([H|T], A, ClusterList) :-
    isOverlap(H, A, OldClusterID, NewClusterID),
    relabel(OldClusterID, NewClusterID, A, AA), % relabel all overlapping clusters in cluster list
    populateClusterList(T, AA, ClusterList),
    !
.

populateClusterList([H|T], A, ClusterList) :-
    insert(H, A, AA),
    populateClusterList(T, AA, ClusterList)
.
% populateClusterList/3 ENDS


% relabel/4
% relabels the points of cluster OldClusterID with label NewClusterID
% relabel(OldClusterID, NewClusterID, ClusterListIn, ClusterListOut)
relabel(_, _, [], []) :- !.
relabel(OldClusterID, NewClusterID, [[P, X, Y, OldClusterID]|T], Result) :-
    relabel(OldClusterID, NewClusterID, T, RR),
    insert([P, X, Y, NewClusterID], RR, Result),
    !
.

relabel(OldClusterID, NewClusterID, [[P, X, Y, NoChange]|T], Result) :-
    relabel(OldClusterID, NewClusterID, T, RR),
    insert([P, X, Y, NoChange], RR, Result),
    !
.
% relabel/4 ENDS

/* ===================  Helper/auxiliary predicates  =================== */

% insert/2
% insert(NewElement, List, NewList)
% insert an item NewElement in front of a list List; returns list NewList
insert(A, L, [A|L]).

% isOverlap/2
% isOverlap(Element, List)
% returns true AND the OldClusterID, NewClusterID if a given GPS point exists in the given cluster list
% that is, if PointID of the given row X is the same as any of the pointIDs of
%    the rows in the given list [_|L], return true
% Note that OldClusterID is the ID in ClusterList and NewClusterID is the ID of the current row
isOverlap([PointID, _, _, NewClusterID], [[PointID, _, _, OldClusterID]|_], OldClusterID, NewClusterID) :- !.
isOverlap(X, [_|L], OldClusterID, NewClusterID) :- isOverlap(X, L, OldClusterID, NewClusterID).

% reverse/3
% helper predicate for reverseList that reverses a list recursively using an auxiliary list
reverse([], L, L) :- !.
reverse([H|T], L, R) :- reverse(T, [H|L], R).

% reverseList/2
% reverses a given list using the helper predicate reverse
reverseList(L,R) :- reverse(L,[],R).

% printList/1
% prints the contents of a list by traversing through it from start to end recursively
printList([]) :- !.
printList([H|T]) :- writeln(H), printList(T).

/* =========================  Test predicates  ========================= */

% tests the relabel predicate with different values/test cases
