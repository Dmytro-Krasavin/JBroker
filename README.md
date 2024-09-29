# Custom MQTT Broker Implementation

This repository contains a custom implementation of an MQTT broker, designed to give a deep understanding of the MQTT protocol and how it handles message transmission, client connections, and more. The project is built from scratch without using major libraries, except for Lombok and SLF4J for simplifying code and logging.

## Features

- **MQTT v3.1.1 support** (with basic features like CONNECT, PUBLISH, and DISCONNECT)
- **Socket-level connection handling** with a focus on learning the underlying transport protocol (TCP)
- **Custom Packet Handling**: Encoding and decoding of MQTT packets from raw bytes
- **Message Queue Processing**: Processing messages with multi-threading using `ExecutorService`
- **Connection Management**: A simple client connection manager for handling multiple clients

### In Development

Please note that some features, such as **Will Message**, **Retain**, and **Clean Session**, **QoS 1**, **QoS 2** are not currently supported but are in development for future releases.

## Table of Contents

1. [Installation](#installation)
2. [Usage](#usage)
3. [Project Structure](#project-structure)
4. [License](#license)

## Installation

### Prerequisites

- **Java 21**
- **Maven** for dependency management and building the project

### Clone the repository

```bash
git clone https://github.com/your-username/mqtt-broker.git
cd mqtt-broker
```
### Build the project

```bash
mvn clean install
```

### Configuration

The broker is configurable via a properties file. The default properties file is `application.properties`, which is located in the project's root directory.

Example `application.properties`:

```properties
broker.port=1883
```

### Running the Broker

Once the project is built, you can run the broker using the following command:

```bash
java -jar target/mqtt-broker.jar
```

The broker will start listening on the port defined in the `application.properties` file (default: 1883).

## Usage

This project serves primarily as a learning tool, demonstrating how to build an MQTT broker from scratch. It is not recommended for production use but can serve as a foundation for further development or a deep-dive into the MQTT protocol.

### Client Connections

The broker supports client connections via TCP. Once a client connects to the broker, it must send a valid `CONNECT` packet to establish an MQTT session.

### Message Publishing

The broker handles PUBLISH packets and routes messages to the appropriate subscribers. Currently, the broker only supports QoS 0.

## Project Structure

The project is structured into various packages, each responsible for different functionalities of the MQTT protocol and broker operation.

- `com.jbroker.connection`: Handles the socket-level connections and the high-level MQTT connection management.
- `com.jbroker.packet`: Contains the classes responsible for encoding and decoding MQTT packets.
- `com.jbroker.command`: Manages command dispatching and executing based on received packets.
- `com.jbroker.message.queue`: Implements a simple message queue processor using `ExecutorService` for asynchronous message publishing.
- `com.jbroker.subscription`: This package contains the `SubscriptionRegistry`, which is responsible for managing client subscriptions to MQTT topics. It provides an interface for handling subscription requests and maintains a registry of active subscriptions, allowing for efficient message delivery to subscribed clients. The current implementation, `InMemorySubscriptionRegistry`, stores subscriptions in memory and supports basic subscription functionalities.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
