# HOTEL BOOKING SYSTEM

## Motivation
This project aims to get to used making REST/SOAP API by using Java EE 8.

## About
##### NOTE: This project was created for educational purposes and do not intended to be used at the production level.
The project is a simple example of a "Hotel Booking System."
We have Hotels and Customers; every customer can book a hotel room. 
There are no strict checks on requests and responses. The main point of the project is to showcase the way that we can organize our backend using Java EE 8 and TomEE 8.0.6
The project exposes the REST and SOAP endpoints.

## Requirements
To be able to build and deploy the application we need to have
* Aerospike database up and running on **localhost:3000**. 
  In case if you are running DB on different port/host you can configure it in `application.properties`.
* TomEE 8.0.6 server
* Minimum Java 8

# RESOURCE MANAGEMENT SERVICE

## Motivation
The essence of the project is to train concurrency, the project is named a server pool in which it will allocate some computation power or memory on the cloud via a cloud provider.

## Requirements
* Docker
* Java 8 or higher

## Instalation
Before running the project please up the `docker-compose.yml` by the following command.  
```
                                 docker-compose -f docker-compose.yml up -d
```
It does several things, first of all it will download the `Aerospike DB`,`Kafka` and `zookeeper`(the Kafka server) then creates the containers and run them.  
`-d` will run the commands on background. The project can be build by `./gradlew build` command.  
