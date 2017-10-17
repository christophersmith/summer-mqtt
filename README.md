# Summer MQTT

Summer MQTT is a MQTT Client wrapper service for Spring Framework, and is intended as a flexible alternate to using Spring Integration MQTT. The first iteration provides an implementation that uses the asynchronous Paho MQTT Client.

## Why Summer MQTT?

Summer MQTT came as a product of need in environments that had poor latency and connectivity. I found that while Spring Integration MQTT provided a clean pattern, I needed more functionality than available without modifying (or heavily customizing) Spring Integration.

So what's the difference?

- Smaller distribution size - both the Core and Paho wrappers weigh in at 33 KB combined. This was essential for mass distribution to thousands of nodes with limited bandwidth
- In-bound and Out-bound connections aren't required to be separated. You can have one MQTT Client connection that handles both Subscribing and Publishing. This was crucial in high latency and poor connectivity environments where I found that synchronizing the the up-state of the In-bound and Out-bound channel adapters to be problematic.
- Better control of the state of the Client - you can start and stop the service at any time without having to re-create the Bean.
- Includes an optional Connection State Message Publisher that will publish State messages when the MQTT Client connects or disconnects (gracefully).

## Project Status

The Summer MQTT project is functional for use with the Asynchronous Paho MQTT Client. At this point, I'm finishing up Unit Tests and will begin writing up usage documentation soon.