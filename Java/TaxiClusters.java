/**
 * Kien Do 300163370
 * CSI2520 - Paradigmes de programmation - Hiver 2022
 */

public class TaxiClusters {

    // eps is a distance perimeter, like a radius
    // minPts is the min points within eps such that this radius counts as a cluster

    /**
     * Thinking of making a key:value data structure.
     * GPS is the unique key, representing the starting point, ~~# of points within the radius is the value~~.
     *  the coordinates of the other points within the radius is the value
     */

//    DBSCAN(DB, distFunc, eps, minPts) {
//        C := 0 /* Cluster counter */
//        for each point P in database DB {
//            if label(P) ≠ undefined then continue /* Previously processed in inner loop */
//                    Neighbors N := RangeQuery(DB, distFunc, P, eps) /* Find neighbors */
//            if |N| < minPts then { /* Density check */
//                label(P) := Noise /* Label as Noise */
//                continue
//            }
//            C := C + 1 /* next cluster label */
//            label(P) := C /* Label initial point */
//            SeedSet S := N \ {P} /* Neighbors to expand */
//            for each point Q in S { /* Process every seed point Q */
//                if label(Q) = Noise then label(Q) := C /* Change Noise to border point */
//                if label(Q) ≠ undefined then continue /* Previously processed */
//                        label(Q) := C /* Label neighbor */
//                Neighbors N := RangeQuery(DB, distFunc, Q, eps) /* Find neighbors */
//                if |N| ≥ minPts then { /* Density check (if Q is a core point) */
//                    S := S ∪ N /* Add new neighbors to seed set */
//                }
//            }
//        }
//    }
//    RangeQuery(DB, distFunc, Q, eps) {
//        Neighbors N := empty list
//        for each point P in database DB { /* Scan all points in the database */
//            if distFunc(Q, P) ≤ eps then { /* Compute distance and check epsilon */
//                N := N ∪ {P} /* Add to result */
//            }
//        }
//        return N
//    }
//    /* Reference: https://en.wikipedia.org/wiki/DBSCAN */



}
