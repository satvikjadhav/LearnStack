# Broker service

The Broker service routes queries in a distributed cluster setup. It interprets the metadata published to ZooKeeper about segment distribution across services and routes queries accordingly. Additionally, the Broker service consolidates result sets from individual services.

