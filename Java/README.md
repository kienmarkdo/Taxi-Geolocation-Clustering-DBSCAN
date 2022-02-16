# Objected-oriented programming (Java)
UML class diagrams and detailed documentation are also included in the development of this project.

All source code is implemented from scratch.

## Dataset
The dataset is taken from NYC's 2009 taxi database that recorded taxi trips in the span of 1 hour. The dataset is contained in a CSV file, with each line corresponding to a trip record and the columns representing the relevant attributes of each trip. Since we want to identify the best waiting areas, we are interested in the starting points. As such, the dataset includes the GPS coordinates of the start and end point for each taxi trip. 

_Disclaimer: Google Maps has a cap at 2,000 markers per layer. As such, not all points are plotted as the dataset contains 21,232 points._

<div><img src="https://user-images.githubusercontent.com/67518620/150623764-db94fc77-e238-46b7-abd5-0b0630aeabb8.png" width="100%"></div>

## Results
### Version 1: Single output (eps = 0.0003, min_pts = 5)
The Java application `TaxiClusters.java` produced as output the list of clusters where each cluster's position is the average value of the GPS coordinates of its point set.
<div><img src="https://user-images.githubusercontent.com/67518620/151718993-d012fd6c-64e8-4e44-b4fc-80951ad30d1b.png" width = 100%></div>

### Version 2: Multiple output (eps = 0.0003, min_pts = 5)
Instead of producing one single CSV file containing all clusters, this version produces multiple CSV files where each CSV file represents ONE cluster. This version produced 705 clusters (consequently, 705 CSV files). The 10 largest clusters/files are then plotted onto Google Maps, each cluster is marked in a different colour.

To view the output image below on Google Maps, [click here](https://www.google.com/maps/d/edit?mid=1NQyUCyEju_fRNNSgHPOevQFdtw9hk-C6&usp=sharing).
<div><img src="https://user-images.githubusercontent.com/67518620/151680554-3bfaa9ec-5a7e-435e-8e2f-383af0e66cac.png" width="100%"></div>

<!-- ![plottedLocationsGPS](https://user-images.githubusercontent.com/67518620/150623764-db94fc77-e238-46b7-abd5-0b0630aeabb8.png)
![sample_clusters_robert_laganiere](https://user-images.githubusercontent.com/67518620/151680554-3bfaa9ec-5a7e-435e-8e2f-383af0e66cac.png) -->
<!-- ![image](https://user-images.githubusercontent.com/67518620/151718993-d012fd6c-64e8-4e44-b4fc-80951ad30d1b.png) middle image -->

<!-- 
<div>
  <div><img src="https://user-images.githubusercontent.com/67518620/150623764-db94fc77-e238-46b7-abd5-0b0630aeabb8.png" width="100%"></div>
  <div><img src="https://user-images.githubusercontent.com/67518620/151718993-d012fd6c-64e8-4e44-b4fc-80951ad30d1b.png" width = 100%></div>
  <div><img src="https://user-images.githubusercontent.com/67518620/151680554-3bfaa9ec-5a7e-435e-8e2f-383af0e66cac.png" width="100%"></div>
</div> -->

<!-- <p float="left">
  <img src="https://user-images.githubusercontent.com/67518620/150623764-db94fc77-e238-46b7-abd5-0b0630aeabb8.png" width="49%" height="100%" />
  <img src="https://user-images.githubusercontent.com/67518620/151680554-3bfaa9ec-5a7e-435e-8e2f-383af0e66cac.png" width="49%" height="100%" /> 
</p> -->
<!-- trying to make two images sit side by side, but one image's height is more than the other... -->

