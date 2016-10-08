EIoTServer
======

Authentication service for the clients that connect each Raspberry Pi ([PiMQ](https://github.com/mariniss/PiMQ)) to the EasyIoTConnect platform, main project [here](https://github.com/mariniss/EasyIoTConnect).
It implements the authentication logic necessary to make the access unique for the ActiveMQ queues. 

It is a java Vert.x application that provides a really simple REST interface and persists the data in a Mongo DB.
