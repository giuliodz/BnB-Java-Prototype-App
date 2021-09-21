# BRIEF
This is a smiple GUI-based Application written in Java for exploring properties available for rental in London. It gives
users an immersive experience for finding a rental property in London, matching their
desired criteria over a variety of categories including price range, location and size. It
consists of 4 panels forming the logical journey of how the user can decide to select a
property. The first panel includes a welcome message with instructions of how to use the
application, where the user can select their preferred price range. Upon selecting the price
range they are prompted to move to the next panel where the user is presented with a
borough map of London, and the user can select their preferred borough location. The next
panel shows some useful statistics about all the properties that can be useful in further
narrowing down the search. The fourth panel is a google maps application that also has
further functionality when selecting a particular property, which will be further explained later.


## Welcome Panel - Panel 1
Users are greeted by a welcome message detailing instructions of how to use the
application. The user is prompted to select a price range before clicking the directional
button to move to the next panel and select a desired borough location.

## Borough Map - Panel 2
After the user has selected their desired price range and moved to the map panel, a borough
map of London will appear on the application window. The user can then click on a desired
borough and the application will subsequently list all the properties in the particular borough
location matching the user’s selected price range, as well as a digital map of the borough in
the adjacent section of the screen.
Once the list of properties has loaded up, the user can select a specific property, from which
the fourth panel additional functionality becomes available. A digital map will load up pointing
out the specific location the property, with information and a full, detailed description of the
property. The user can also then see a street view of the property and surrounding areas. As
well as this, there is a drop-down menu in the window that allows the user to find the nearest
points of interest such as tube stations, restaurants and other nightlife spots, just to name a
few. Once finished taking a detailed look at the property the user can simply close the
window and proceed to the remaining panels.

## Statistics - Panel 3
The next panel contains several useful statistics about all the property listings (these will be
further explained shortly). The user can either see these statistics for all properties, or they
can filter the results by price range and neighbourhood.

## Panel 4
Although the final panel has a lot of its useful implementation through each specific property
selected with points of interest and street view for example, as it is easily accessed from the
second panel (see panel 2 above), the user can also use the final panel to specifically find
particular locations and properties. It is uses Google Maps functionality such that the user
can zoom in and find any location, as well as obtain directions to and from any locations they
desire.
### Added statistics
Four additional statistics have been created:
1. A statistic that shows given a selected neighbourhood how many properties
close to a tube station there are.
It has been achieved using the google api for searching places. The google api URL had
been set so that it can return an array of results containing all the tube stations within
a range of 500 meters away from the property. So it passes in the coordinates for
every property in a selected neighbourhood. If the array of results from that property
is not empty then we know that that property is close to a tube station. Finally it is just
returned the number of properties whose array of results is not empty (the ones
which are close to a tube station).
2. A statistic representing the number of properties which match the filters sat by
the user (price range and area).
This has been achieved by creating a method in the Statistic class which takes as
parameters the filters applied by the user. The implementation of the method
basically goes through all the properties in the Abnb dataset one by one and checks
their attributes. If they match the user filters the yare added into an ArrayList. Finally
the size of the ArrayList (which will contain all the properties which matched the
user’s filters) is returned.
3. A statistic that shows the average price por property given a selected
neighbourhood.
It is done by a method in the Statistics class which collects all the properties of a
specified (by the user) neighbourhood in a List. Afterwards the price of each property
is retrieved, multiplied by the minimum nights of staying and stored in a variable
which will add all the resulting values. Then that value is simply divided by the size of
the list of the properties in that neighbourhood and returned.
4. A statistic that states how expensive a neighbourhood is showing its position
in a rank of all the neighbourhoods of London based on the neighbourhood’s
property costs (taking into account of the minimum nights of staying).
The main method for that is always in the Statistics class. In its implementation there is
an HashMap containing all the neighbourhood names as keys. Their related values
are the sum of all the property’s prices (multiplied by the minimum nights of staying)
in that neighbourhood.
Once done that, thank to a sorting algorithm, the names of the neighbourhoods are
passed into an ArrayList in a sorted and ascending way. Finally the index of the queried
neighbourhood is incremented by one and returned.

# Known Bugs
- As of now there is only one known bug. In the Map functionality class there are two
methods one loads up a static image of street view and the other loads up a dynamic
user-friendly interface for street view. The dynamic one is often glitchy and the
source of the issue has not been found. We are assuming that it has to do with the
Google API rather than our program. In the meantime we just load a static image of
the street view.
- The API also has limits to how many times it can be requested each day and even
though this is not a bug it is worth mentioning.
- The method to find nearby tube stations (isNearbyStations) (stations within 500
meters of the property) takes too long to load, this is suspected to be an issue with
the API. We tried to add another column to the dataset to get around this issue, to
store the data of whether there are stations within 500 meters, however, due to the
size of the data set with over 50,000 properties, the API limit (10,000 requests per
day) would not allow this.