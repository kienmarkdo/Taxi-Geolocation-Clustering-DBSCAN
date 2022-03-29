/*
Nom / Name             : Kien Do
Cours / Course         : CSI2520 - Paradigmes de programmation / Programming Paradigms
Professeur / Professor : Robert Laganière, uottawa.ca
Session / Semester     : Hiver 2022 / Winter 2022
Projet / Project       : Merging DBSCAN-Clustered Taxi Geolocation Partitions
*/

/* ======================  Import partition files  ===================== */

% import/1
% creates a fact base of all partitions extracted from the partition##.csv files
% the fact based created by the import predicate can be viewed by using the query: 
%      listing(partition) 
% where the partition format is: 
%      partition(PARTITION_ID, POINT_ID, X, Y, CLUSTER_ID)
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

% mergeClusters/1
% merges the clusters in partition; includes removing all duplicate/overlapping clusters
mergeClusters(L) :-
    findall([P,X,Y,C], partition(_,P,X,Y,C), LL),
    populateClusterList(LL, [], LLL),
    reverseList(LLL, L)
.

% check to see if we should be relabelling or adding the current point to the cluster list
% populateClusterList(Partitions, Condition, Auxiliary list, Final cluster list)

populateClusterList([], A, A) :- !.

populateClusterList([H|T], A, ClusterList) :-
    isOverlap(H, A, OldClusterID, NewClusterID),
    relabel(OldClusterID, NewClusterID, A, AA), % relabel every overlapping cluster in cluster list to the new cluster
    % this is slower than simply relabelling the ID of H to the IDs in A, but it is easier to implement
    populateClusterList(T, AA, ClusterList),
    !
.
populateClusterList([H|T], A, ClusterList) :-
    insert(H, A, AA),
    populateClusterList(T, AA, ClusterList)
.

% add cluster to cluster list OR relabel the cluster IDs
printList([]) :- !.
printList([H|T]) :- writeln(H), printList(T).

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
% relabel END

/* ===================  Helper/auxiliary predicates  =================== */

% insert/2
% insert item in front of a list
% insert(NewElement, List, NewList)
insert(A, L, [A|L]).

% isOverlap/2
% returns true if a given point already exists in a given list
% that is, if PointID of the element is the same as any of the pointIDs of
%    the elements in the given list, return true
% change the cluster ID in the cluster list to the cluster ID of the given partition row
% isOverlap(Element, List)
isOverlap([PointID, _, _, NewClusterID], [PointID, _, _, OldClusterID|_], OldClusterID, NewClusterID) :- !.
% isOverlap([PointID, _, _, SameClusterID], [PointID, _, _, SameClusterID|T], OldClusterID, NewClusterID) :-
%    isOverlap([PointID, _, _, SameClusterID], T, OldClusterID, NewClusterID)
%.
isOverlap(X, [_|L], OldClusterID, NewClusterID) :- isOverlap(X, L, OldClusterID, NewClusterID).


renverser([],L,L):-!.
renverser([H|T],L,R):- renverser(T,[H|L],R).

reverseList(L,R) :- renverser(L,[],R).


