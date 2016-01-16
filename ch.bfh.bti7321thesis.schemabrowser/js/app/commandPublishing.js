class Publisher {
    constructor() {

    }

    publish(value, topic) {

        var client = new Paho.MQTT.Client(host, port, "cmdPub_" + parseInt(Math.random() * 100, 10));

        // set callback handlers
        client.onConnectionLost = onConnectionLost;
        //client.onMessageArrived = onMessageArrived;

        // connect the client
        client.connect({onSuccess: onConnect});


        // called when the client connects
        function onConnect() {
            // Once a connection has been made, make a subscription and send a message.
            console.log("onConnect");


            var message = new Paho.MQTT.Message(String(value));
            message.destinationName = topic;
            client.send(message);
            console.log("sent");
            client.disconnect();
        }

        // called when the client loses its connection
        function onConnectionLost(responseObject) {
            if (responseObject.errorCode !== 0) {
                console.log("onConnectionLost:" + responseObject.errorMessage);
            }
        }


    }
}