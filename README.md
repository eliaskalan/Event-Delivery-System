# ****Event Delivery System and Android Chat Application (Kafka)****

This project involves the development of a multimedia streaming framework called the Event Delivery System, which supports the real-time streaming of video, photos, and text. The second phase of the project includes creating an Android application that uses the interfaces from the first phase to enable users to transfer information in a group chat and playback multimedia content on mobile devices.

## **Event Delivery System**

The Event Delivery System is a programming framework that allows real-time sending and forwarding of data through "push" and "pull" functions. The system is based on an intermediate node called the broker, which receives data from publishers and transfers them to subscribers. The "push" function forwards key/value pairs to the broker, and the intermediate structure stores the information in a queue to efficiently transfer data. The framework streams videos and photos in small, equally sized pieces for communication efficiency. The push function can be executed in parallel, allowing different users to see information communicated by the same user, and the system must be able to handle the load of multiple users at the same time.

The "pull" function retrieves information associated with a specific key from an intermediate node and forwards it to the requester. It generates a list of values corresponding to the key, which is sent to the intermediate node and then to all interested listeners. The multimedia streaming framework consists of three components: the publisher (user profile), the broker nodes, and the consumer (subscriber function), all of which must be implemented in the project.

![project (1)](https://github.com/eliaskalan/distributed-systems/assets/57637832/106c8956-815a-448c-9ed0-aaa642043ee3)


### **Publisher Component**

The Publisher component is responsible for storing and exporting media files that a user wants to send. It notifies intermediate nodes when new material is available and sends it to them. It can serve multiple requests from different intermediate nodes, and the content is transferred in small chunks. The basic function of the Publisher is to push data to the brokers responsible for serving specific keys. It needs to know which broker to send the data to, and there needs to be synchronization and shared structures among brokers to know which publisher is subscribed to which keys.

### **Broker Nodes Component**

The Broker Nodes component is responsible for a range of keys or topics, which in this case are the names of conversations. The system needs to have an equal distribution of conversations among brokers, and this is achieved using a hashing function. Brokers inform Publishers which keys they are responsible for and establish communication with them to receive information and forward it to appropriate consumers. Brokers also inform new consumers about other brokers and the topics they are responsible for. To ensure real-time multicasting to subscribers, multithreading principles are used. The important aspect of brokers is that the information they send to consumers must be sent to everyone at the same time.

### **Consumer Component**

The Consumer component is responsible for receiving the required information from the system and playing multimedia content. It will be part of the application in the user's mobile device. The Consumer receives tuples of the form <ProfileName, ObjectName, ObjectFileInfo> from the Broker Nodes. When the Consumer first communicates with the Event Delivery System, it queries the first random broker about the available brokers responsible for the various topic-conversations and receives an object of the form Info {ListOfBrokers {IP,Port}, <BrokerId, ProfileName, {Topics-Channels}}}. Based on this object, the Consumer chooses the appropriate broker to return the corresponding information for a user profile.

![DS_1](https://github.com/eliaskalan/distributed-systems/assets/57637832/d4afc779-9f1c-4ea2-b036-468f3ed0aac6)


## **Design Pattern**

We are using the MVC model in this project. The components are:

- Controller (Accepts input and converts it to commands for the model or view)
- Model (The central component of the pattern. It directly manages the data, logic, and rules of the application)
- View (In this project, an Android app)

## **Codebase Organization**

- **`src/controller`**: Contains the Controller component of the MVC model.
- **`src/model`**: Contains the Model component of the MVC model, including the multimedia streaming framework (Event Delivery System).
- **`src/view`**: Contains the View component of the MVC model, which is the Android app.
- **`src/utils`**: Contains our custom tools to help us, such as the Config class for setting up brokers.

## **Configuring the Project**

To run the project, follow these steps:

1. Go to **`src/utils/Config.java`**.
2. Set how many brokers you want to have with variable **`NUMBER_OF_BROKERS`**.
3. For how many brokers you set, create static variables of type **`Address`**. For example, **`public static Address BROKER_1`**.
4. Create a class for each broker, where you initialize the brokers as Broker1 and give the constant variables from the previous step.
5. Run the project.

Note: This example has 6 channels.

## **Collaborators**

- Ioannis Apostolou - Ιωάννης Αποστόλου - 3190013
- Elias Kalantzis - Ηλίας Καλαντζης - 3190068
- Konstantinos Katsamis - Κωνσταντίνος Κατσάμης - 3190237
