# Overview
Inspired by [Clustering Taxi Geolocation Data To Predict Location of Taxi Service Stations](https://medium.com/analytics-vidhya/clustering-taxi-geolocation-data-to-predict-location-of-taxi-service-stations-answering-important-82535ed9bf57).

Imagine we are managing a taxi fleet in NYC and we would like to identify the best waiting areas for our vehicles. To solve this problem, we have a large dataset of taxi trip records from 2009.

This project implements a data clustering algorithm named [DBSCAN - Density-Based Spatial Clustering of Applications with Noise](https://en.wikipedia.org/wiki/DBSCAN). Given a large set of data points in a space of arbitrary dimension and given a distance metric, this algorithm can discover clusters of different shapes and sizes, marking as outliers isolated points in low-density regions (i.e. points whose nearest neighbors are too far away).


## Clustering
Clustering is done with the [DBSCAN](https://en.wikipedia.org/wiki/DBSCAN) algorithm which detects spatiotemporal taxi trip hotspots. The clustered data is then used in a recommendation system that generates optimal GPS routes for taxis that maximizes time driving passengers while minimizing time wasted on the road without serving a passenger. This ensures that taxi drivers can drop off a passenger and quickly pick up a nearby passenger to save time, money, and fuel, while also making sure that passengers can have a taxi as soon as possible.

## Implementation
The project is implemented in four programming languages and paradigms: 
  - objected-oriented programming (Java)
  - concurrent programming (Golang)
  - logical programming (Prolog)
  - functional programming (Scheme)

## Dataset and Results
Each of the four versions of this project uses a different dataset and produces different results based on each paradigms' strengths.

To view the datasets and results for each paradigms, select one of the following options.
  - [objected-oriented programming (Java)](/Java)
  - concurrent programming (Golang)
  - logical programming (Prolog)
  - functional programming (Scheme)
