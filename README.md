# Clustering-Taxi-Geolocation-Data-To-Predict-Location-of-Taxi-Service-Stations
Taxi Movement Detection using the DBSCAN clustering algorithm.

# Clustering Algorithm
Clustering is done with the DBSCAN algorithm which detects spatiotemporal taxi trip hotspots. The clustered data is then used in a recommendation system that generates optimal GPS routes for taxis that maximizes time driving passengers while minimizing time wasted on the road without serving a passenger. This ensures that taxi drivers can drop off a passenger and quickly pick up a nearby passenger to save time, money, and fuel, while also making sure that passengers can have a taxi as soon as possible.

# Implementation
The project is implemented in four different programming paradigms: objected-oriented, concurrent, logical, and functional programming in Java, Golang, Prolog, and Scheme, respectively. UML diagrams and detailed documentation are also included in the development of this project.

# Dataset
The data is contained in a CSV file, with each line corresponding to a trip record and the columns representing the relevant attributes of each trip. Since we want to identify the best waiting areas, we are interested in the starting points. As such, the dataset includes the GPS coordinates of the start and end point for each taxi trip.
