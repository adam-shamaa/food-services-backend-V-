# Food Services Aggregator Backend (V2)

## Table of Contents
1. [Getting Started](#Getting-Started)
2. [API](#API)
3. [Architecture](#Architecture)

## Getting Started
- More details to come. Working out some issues with unauthorized read authentication of github maven packages.

## Web API
All endpoints exposed through this server conform to an API contract hosted in a [seperate repository](https://github.com/adam-shamaa/food-services-aggregator-spec). The repository automatically publishes an artifact containing autogenerated stubs and classes for the web APIs. See the repository for the published open api specs and more information.

## Architecture
### System Centric View
<img width="1125" alt="V3 - Systems Context Diagram - C4 Model" src="https://user-images.githubusercontent.com/61364811/172213692-9a417bc5-1b7c-4327-91db-45546d6b8918.png">

### Container Centric View
<img width="1125" alt="V3 - Container Diagram - C4 Model" src="https://user-images.githubusercontent.com/61364811/172213731-c144abd8-3939-442f-a640-a51888bcfa76.png">

### Component Centric View
<img width="1244" alt="V3 - Component Diagram - C4 Model" src="https://user-images.githubusercontent.com/61364811/172213750-dfb3e1c9-6bc5-46dc-a511-762b576dde5e.png">

## Additional Resources
I've put together a [API client library](https://pastebin.com/UfCc5qrc) in [Insomnia](https://insomnia.rest/). It features API requests to currently delivery providers - SkipTheDishes, Ubereats and DoorDash in addiiton to API requests for the backend, including environments for development and prod servers. 

Feel free to use it to play around with the various delivery service API's or the production server directly.
