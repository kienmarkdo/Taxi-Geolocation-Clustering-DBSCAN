# Overview
Inspired by [Clustering Taxi Geolocation Data To Predict Location of Taxi Service Stations](https://medium.com/analytics-vidhya/clustering-taxi-geolocation-data-to-predict-location-of-taxi-service-stations-answering-important-82535ed9bf57).

Imagine we are managing a taxi fleet in NYC and we would like to identify the best waiting areas for our vehicles. To solve this problem, we have a large dataset of taxi trip records from 2009.

This project implements a data clustering algorithm named [DBSCAN - Density-Based Spatial Clustering of Applications with Noise](https://en.wikipedia.org/wiki/DBSCAN). Given a large set of data points in a space of arbitrary dimension and given a distance metric, this algorithm can discover clusters of different shapes and sizes, marking as outliers isolated points in low-density regions (i.e. points whose nearest neighbours are too far away).


## Clustering
Clustering is done with the [DBSCAN algorithm](https://en.wikipedia.org/wiki/DBSCAN#Algorithm) which detects spatiotemporal taxi trip hotspots. The clustered data can then be used in a variety of applications, such as a recommendation system that generates optimal GPS routes for taxis that maximizes time driving passengers while minimizing time wasted on the road without serving a passenger. This ensures that taxi drivers can drop off a passenger and quickly pick up a nearby passenger to save time, money, and fuel, while also making sure that passengers can have a taxi as soon as possible.

## Implementation
The project is implemented in four programming languages and paradigms where each implementation is specific to the language and paradigm's purpose: 
  - [Objected-oriented (Java)](Java)
    - The DBSCAN algorithm is fully implemented and clusters the dataset procedurally (dataset contains 21,232 points)
    - This OOP version gives a general overview of the project before proceeding to the next version
  - [Concurrent (Golang)](Golang)
    - Partition the dataset then run DBSCAN concurrently on each partition using the producer-consumer pattern and thread pools
    - This version splits the dataset into smaller partitions and clusters each partition concurrently
    - Utilizes the maximum CPU capacity in order to cluster large datasets faster (dataset contains 232,051 points)
  - [Logical (Prolog)](Prolog)
    - This version merges the clustered dataset from each partition (includes removing overlapping points) using the MapReduce pattern
    - Programmed declaratively and logically; mainly involves tail-recursion
<!--     - Performs the merging step of the MapReduce pattern on cluster partitions produced by the concurrent version -->
  - [Functional (Scheme)](Scheme)
    - This version merges the clustered dataset from each partition (includes removing overlapping points) using the MapReduce pattern
    - Programmed functionally; mainly involves tail-recursion
<!--     - Performs the merging step of the MapReduce pattern on cluster partitions produced by the concurrent version -->
<!-- Example of full path (https://github.com/kienmarkdo/Taxi-Geolocation-Clustering-DBSCAN/tree/main/Java) -->

<!-- NOTE: HTML Scroll Box doesn't work in GitHub markdown so <details> and <summary> are used instead. -->

<!-- ===================================  DETAIL SEPARATOR  =================================== -->
<details>
  <summary>
    Algorithm pseudocode
  </summary>
  
```
DBSCAN(DB, distFunc, eps, minPts) {
    C := 0                                                  /* Cluster counter */
    for each point P in database DB {
        if label(P) ≠ undefined then continue               /* Previously processed in inner loop */
        Neighbors N := RangeQuery(DB, distFunc, P, eps)     /* Find neighbors */
        if |N| < minPts then {                              /* Density check */
            label(P) := Noise                               /* Label as Noise */
            continue
        }
        C := C + 1                                          /* next cluster label */
        label(P) := C                                       /* Label initial point */
        SeedSet S := N \ {P}                                /* Neighbors to expand */
        for each point Q in S {                             /* Process every seed point Q */
            if label(Q) = Noise then label(Q) := C          /* Change Noise to border point */
            if label(Q) ≠ undefined then continue           /* Previously processed (e.g., border point) */
            label(Q) := C                                   /* Label neighbor */
            Neighbors N := RangeQuery(DB, distFunc, Q, eps) /* Find neighbors */
            if |N| ≥ minPts then {                          /* Density check (if Q is a core point) */
                S := S ∪ N                                  /* Add new neighbors to seed set */
            }
        }
    }
}

RangeQuery(DB, distFunc, Q, eps) {
    Neighbors N := empty list
    for each point P in database DB {                      /* Scan all points in the database */
        if distFunc(Q, P) ≤ eps then {                     /* Compute distance and check epsilon */
            N := N ∪ {P}                                   /* Add to result */
        }
    }
    return N
}

/* Reference: https://en.wikipedia.org/wiki/DBSCAN */
```
</details>

## Dataset
The dataset is taken from NYC's 2009 taxi database that recorded taxi trips spanning a certain timeframe on January 15, 2009. The dataset is contained in a CSV file, with each line corresponding to a trip record and the columns representing the relevant attributes of each trip. Since we want to identify the best waiting areas, we are interested in the starting points. As such, the dataset includes the GPS coordinates of the start and end point for each taxi trip.

The image below is an example of a visualization of the plotted GPS coordinates of the NYC taxi dataset from the [Java](Java) version of this project, spanning 1 hour on January 15, 2009, and containing **_21,232 points_**. Note that each of the four implementations of this project uses a different sized dataset (for example, the [Golang](Golang) version uses the same dataset, but spanning 12 hours and contains **_232,051 points_** instead).

_Disclaimer: Google Maps has a cap at 2,000 markers per layer. As such, not all points are plotted as the dataset contains 21,232 points._

<div><img src="https://user-images.githubusercontent.com/67518620/150623764-db94fc77-e238-46b7-abd5-0b0630aeabb8.png" width="100%"></div>

## Results
Since each of the four versions of this project uses a different dataset to match their respective paradigms' strengths, it is evident that they also produce different results.

To view the datasets and results for each programming paradigm, select one of the following options.
  - [objected-oriented programming (Java)](Java)
  - [concurrent programming (Golang)](Golang)
  - [logical programming (Prolog)](Prolog)
  - [functional programming (Scheme)](Scheme)
