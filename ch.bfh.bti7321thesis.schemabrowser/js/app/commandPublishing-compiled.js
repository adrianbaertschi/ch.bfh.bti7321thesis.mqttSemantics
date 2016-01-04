"use strict";

var _createClass = (function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; })();

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

var Publisher = (function () {
    function Publisher() {
        _classCallCheck(this, Publisher);
    }

    _createClass(Publisher, [{
        key: "publish",
        value: function publish(value, topic) {

            var client = new Paho.MQTT.Client(host, port, "cmdPub_" + parseInt(Math.random() * 100, 10));

            // set callback handlers
            client.onConnectionLost = onConnectionLost;
            //client.onMessageArrived = onMessageArrived;

            // connect the client
            client.connect({ onSuccess: onConnect });

            // called when the client connects
            function onConnect() {
                // Once a connection has been made, make a subscription and send a message.
                console.log("onConnect");

                var buffer = new ArrayBuffer(16);
                var int32View = new Int32Array(buffer);
                int32View[0] = 5200;

                //var message = new Paho.MQTT.Message(buffer);// 170 /  byte
                var message = new Paho.MQTT.Message(String(value)); // 170 / 98byte
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
    }]);

    return Publisher;
})();

//# sourceMappingURL=commandPublishing-compiled.js.map