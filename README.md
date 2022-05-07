# distributed-systems

## Organize codebase
we will try a **MVC** model
- **Controller** (Accepts input and converts it to commands for the model or view)
- **Model** (The central component of the pattern. It is the application's dynamic data structure, independent of the user interface. It directly manages the data, logic and rules of the application.
  View)
- **View** (In the future will be an android app)
- **Utils** (Our custom tools to help us)

## Run Project
Go to `src` -> `utils` -> `Config`

Set how many brokers you want to have with variable `NUMBER_OF_BROKERS`.
Then for how many `NUMBER_OF_BROKERS` create static variables of type `Address`

As in example `public static Address BROKER_1`.

Then create for each broker class where you initialize the brokers as `Broker1`
and give the constant variables from the previous step.

This example has 6 channels!

## Collaborators
- [Elias Kalantzis](https://github.com/L0TH)
- [Konstantinos Katsamis](https://github.com/konstantinosKatsamis)