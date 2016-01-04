class Subscription {

    constructor() {
        this.client2 = new Paho.MQTT.Client(host, port, "eventSubscr_" + parseInt(Math.random() * 100, 10));
    }
    subscribe(eventTopic) {
        var evtId =  $.md5(eventTopic);
        var self = this;
        var options = {
            timeout: 3,
            onSuccess: function (message) {
                console.log("mqtt subscribe to event " + eventTopic);
                self.client2.subscribe(eventTopic);
            },
            onFailure: function (message) {
                console.log("Connection failed: " + message.errorMessage);
            }
        };

        this.client2.onConnectionLost = function (responseObject) {
            console.log("connection lost: " + responseObject.errorMessage);
        };
        this.client2.onMessageArrived = function (message) {
            console.log(message.destinationName, ' -- ', message.payloadString);
            ($('#' +evtId)).val(message.payloadString);
        };

        console.log('mqtt connecting ...');
        this.client2.connect(options);
    }

    unsubscribe(eventTopic) {
        this.client2.unsubscribe(eventTopic);
        this.client2.disconnect();
    }
}