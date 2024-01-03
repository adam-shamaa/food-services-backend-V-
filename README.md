# Food Services Aggregator Backend (V2)

**\*\*As of January 2024, this project will no longer be maintained. Demo videos of the working application are available at the https://github.com/adam-shamaa/food-services-frontend-V2**

## Table of Contents
1. [Technologies](#Technologies)
2. [Overview](#Overview)
   1. [Architecture](#Architecture)
      1. [System Centric View](#System-Centric-View)
      2. [Container Centric View](#Container-Centric-View)
      3. [Component Centric View](#Component-Centric-View)
3. [Getting Started](#Getting-Started)
   1. [Development](#Development)
      1. [Docker](#Using-Docker)
      2. [Maven](#Using-Maven)
4. [Additional Resources](#Additional-Resources)

## Technologies
- Java
  - Spring
  - Maven
  - jsonschema2pojo-maven-plugin
  - mapstruct-annotation-processor
- Docker 
- Github Actions

## Overview
This repository houses the API for the food-services-aggregator application.

The main objective is to expose an API which enables information aggregation from delivery services (i.e. SkipTheDishes, UberEats, ...)  such as available restaurants and their delivery prices, service fees & menu items.

### Architecture  

#### System Centric View
<img width="1125" alt="V3 - Systems Context Diagram - C4 Model" src="https://user-images.githubusercontent.com/61364811/172213692-9a417bc5-1b7c-4327-91db-45546d6b8918.png">

#### Container Centric View
<img width="1125" alt="V3 - Container Diagram - C4 Model" src="https://user-images.githubusercontent.com/61364811/172213731-c144abd8-3939-442f-a640-a51888bcfa76.png">

#### Component Centric View
<img width="1244" alt="V3 - Component Diagram - C4 Model" src="https://user-images.githubusercontent.com/61364811/172213750-dfb3e1c9-6bc5-46dc-a511-762b576dde5e.png">

## Getting Started

### Development

#### Using Docker
1. Ensure Docker is running
2. Run `docker-compose up`

#### Using Maven
1. Ensure maven 3.0.0 is installed
2. Run `mvn spring-boot:run`


## Additional Resources
- [API client library](https://pastebin.com/UfCc5qrc) in [Insomnia](https://insomnia.rest/), contains: 
  - API requests for UberEats and SkipTheDishes
  - API requests for backend server w/ environments for local dev and prod servers

