# VIRTUAL PARKING SYSTEM #

## Overview ##

This application should be a working prototype of a system that aims to manage a smart parking area. The system is *virtual* in the sense that the components (sensors, cars, etc) are not real but are software components that simulate a plausible behavior.

## Components ##

The main elements that compose the system are:

* an **environment**: it is where the park area is placed and everything takes place. It is, for simplicity, a grid-based world, that is an (n x m) matrix where each cell could be a street block or a park place.

* a **sensors layer**: each car parking place is equipped with a parking sensor able to determine if the place is free or busy. Each sensor send this information to a central server. It also provide a (simulated) local area network that could be used by a client application to retrieve information in case the server is not available.

* a **server application**: it knows everything about the parking area but it may suddenly break down. The server receives information from sensors and uses them to produce a dynamic park map (for eg. a graph). The server interacts also with drivers (client applications) sending the current map data that will be used to locate the nearest parking place. 

* some **client applications**: that is drivers that would like to park their car or to locate a parked one. The client application interacts with the server, if available, to retrieve an updated park map and find the nearest parking place available. If the server is offline, it tries to interact with the nearest sensors to retrieve some useful information.

## System functionalities ##

The final system should provide the following functionalities:

* find the nearest free park place

* locate the parked car

* ...

The above functionalities should be supported in a client-server interaction schema if the server is available or using local interactions with near sensors otherwise. 

### How do I get set up? ###

1. Download the [source](https://bitbucket.org/acco93/virtual-parking-system/src)
 
2. Import the project in Eclipse

3. Run gradle eclipset o set up dependencies

## Technologies ##

At the moment components interactions are implemented using RabbitMQ.