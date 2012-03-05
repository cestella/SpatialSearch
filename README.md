If you came here from Kent State's ACM, you might want to see the slide deck [here](https://github.com/cestella/SpatialSearch/raw/master/presentations/Kent_ACM_3_8_2012/presentation.pdf)

#Introduction
The purpose of this library is to assist in the creation and use of [Locality Sensitive Hashes](http://en.wikipedia.org/wiki/Locality-sensitive_hashing) to solve Near Neighbor Searches under certain approximation criteria.  This works with large amounts of data and for very high dimensions.

In particular:

* fixed radius
* fixed metric
* approximately accurate

If you want to know more about this technique, please see the slide deck referenced [here](https://github.com/cestella/SpatialSearch/raw/master/presentations/Kent_ACM_3_8_2012/presentation.pdf)

#Usage
##Prerequisites
To execute this, you must have:

* A JDK installed
* Maven 2 or higher installed

From the command line, in the spatial\_search directory, execute

     mvn package dependency:copy-dependencies

In order to execute the Stable Distribution parameter approximation tool, you must have a file with data points (in text).  
You can use the stable\_distribution\_parameter\_estimator.sh script to execute the utility

#Contact

If anyone has comments, concerns or criticisms, please let me hear about it at cestella@gmail.com

