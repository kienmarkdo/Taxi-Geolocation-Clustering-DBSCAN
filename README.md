# Clustering-Taxi-Geolocation-Data-To-Predict-Location-of-Taxi-Service-Stations
Imagine we are managing a taxi fleet in NYC and we would like to identify the best waiting areas for our vehicles. To solve this problem, we have a large dataset of taxi trip records from 2009.

This project implements a data clustering algorithm named DBSCAN - Density-Based Spatial Clustering of Applications with Noise. Given a large set of data points in a space
of arbitrary dimension and given a distance metric, this algorithm can discover clusters of different shapes and sizes, marking as outliers isolated points in low-density regions (i.e. points whose nearest neighbors are too far away).


# Clustering and Trajectory Recommendation
Clustering is done with the DBSCAN algorithm which detects spatiotemporal taxi trip hotspots. The clustered data is then used in a recommendation system that generates optimal GPS routes for taxis that maximizes time driving passengers while minimizing time wasted on the road without serving a passenger. This ensures that taxi drivers can drop off a passenger and quickly pick up a nearby passenger to save time, money, and fuel, while also making sure that passengers can have a taxi as soon as possible.

![plottedLocationsGPS](https://user-images.githubusercontent.com/67518620/150623764-db94fc77-e238-46b7-abd5-0b0630aeabb8.png)


# Implementation
The project is implemented in four different programming paradigms: objected-oriented, concurrent, logical, and functional programming in Java, Golang, Prolog, and Scheme, respectively. UML class diagrams and detailed documentation are also included in the development of this project.

# Dataset
The dataset is taken from NYC's 2009 taxi database that recorded taxi trips in the span of 1 hour. The dataset is contained in a CSV file, with each line corresponding to a trip record and the columns representing the relevant attributes of each trip. Since we want to identify the best waiting areas, we are interested in the starting points. As such, the dataset includes the GPS coordinates of the start and end point for each taxi trip.
