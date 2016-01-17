
var schemas = {};
var subscriptions = {};

var client = new Paho.MQTT.Client(host, port, '/', 'descBrowser');
client.onMessageArrived = onMessage;
client.onconnectionlost = onDisconnect;
function onConnect() {
    client.subscribe('+/+/+/+/+/schema/' + descFormat, onMessage);
    console.log("MQTT connected");
}

client.connect({onSuccess: onConnect});

function onMessage(message) {
//        console.log(message.destinationName + " - " + message.payloadString);
    var topics = message.destinationName.split("/");

    var fulltopic = message.destinationName;

    var baseInfo = topics[0] + ' - ' + topics[1] + ' - ' + topics[2] + ' - ';
    console.log(baseInfo);
    var deviceType = topics[3];
    var devId = topics[4];


    if(fulltopic in schemas) {
        schemas[fulltopic] = message.payloadString;
        return;
    }
    schemas[fulltopic] = message.payloadString;

    var a = '<a href="#" onclick="deviceClick(\'' + fulltopic + '\')">'+ topics[4] + '</a>';


    var row = '<tr>' +
        '<td>'+ topics[0] + '</td>'+
        '<td>'+ topics[1] + '</td>'+
        '<td>'+ topics[2] + '</td>'+
        '<td>'+ topics[3] + '</td>'+
        '<td>' + a + '</td></tr>';

    $('#deviceTable > tbody:last-child').append(row);



}

function deviceClick(schemaTopic) {
    console.log('table click');
    console.log(schemaTopic);
    var devId = schemaTopic.split('/')[4];


    var deviceTopic = schemaTopic.split('/').slice(0, 5).join('/');
    console.log(deviceTopic);

    $('#txt').val(schemas[schemaTopic]);

    var json;
    if(descFormat === 'YAML') {
        json = jsyaml.load(schemas[schemaTopic]);
    } else if(descFormat === 'JSON') {
        json = JSON.parse(schemas[schemaTopic]);
    }

    $('#doc').text('');

    // TODO: meta infos (version , etc).
    //$('#commands').text(json.id).append(' - ').append(json.version);

    var infoCnt = '';

    $('#doc').append(createSection('Info', renderInfo(json)));

    var stateCnt = '';
    json.stateDescription.states.forEach(function (state) {
        stateCnt += renderState(state, deviceTopic);
    });
    $('#doc').append(createSection('State', stateCnt));

    var eventCnt = '';
    json.eventDescription.events.forEach(function (event) {
        eventCnt += renderEvent(event, deviceTopic);
    });
    $('#doc').append(createSection('Events', eventCnt));

    var cmdCnt = '';
    json.commandDescription.commands.forEach(function (cmd) {
        cmdCnt += renderCmd(cmd, deviceTopic);
    });
    $('#doc').append(createSection('Commands', cmdCnt));

    var typeCnt = '';
    json.complexTypes.forEach(function (type) {
        typeCnt += renderType(type, deviceTopic);
    });
    $('#doc').append(createSection('Complex_Types', typeCnt));


    evtHandler();

}

function createSection(title, content) {
    var res = '';
    res += '<div class="panel panel-default" id="panel1">';

    res += '  <div class="panel-heading">';
    res += '    <h2 class="panel-title">';
    res += '      <a data-toggle="collapse" data-target="#collapse'+title+'" href="#collapse'+ title + '">';
    res +=          title;
    res += '      </a>';
    res += '    </h2>';
    res += '  </div>';

    res += '  <div id="collapse'+title+'" class="panel-collapse collapse in">';
    res += '    <div class="panel-body">';
    res +=          content;
    res += '    </div>';

    res += '</div>';

    return res;
}

function renderInfo(deviceDesc) {
    var res = '';
    res += '<strong>Id: </strong>' + deviceDesc.id + '<br>';
    res += '<strong>Version: </strong>' + deviceDesc.version + '<br>';
    res += '<strong>Description: </strong>' + deviceDesc.description + '<br>';

    return res;
}

function renderState(state, deviceTopic) {
    var res = '<strong>' + state.name + '</strong> <br>';

    if(state.description) {
        res += state.description + '<br>';
    }


//        console.log(state.range);
    if (state.range && state.range.hasOwnProperty('type')) {
        res += renderRange(state.range, 'Value:');
    } else if (state.options && state.options.hasOwnProperty('values')) {
        res += renderPresets(state.options);
    } else if(state.complexTypeRef !== null) {
        // Ref to Complextype
        res += renderComplexTypeLink(state.complexTypeRef) + '<br>';
    } else {
        console.log('ERROR ' + state);
    }

    // Topic
    var stateTopic =  deviceTopic + '/state/' + state.name;
    res += renderTopic(stateTopic);

    return res + '<br>';
}

function renderComplexTypeLink(typeRef) {
    var res = '';

    res += 'Type: ';
    res += '<a href="#type_' + typeRef + '">' + typeRef + '</a> <br>';

    return res;
}


function renderCmd(cmd, deviceTopic) {
    var cmdTopic = deviceTopic + '/commands/' + cmd.name;

    var res = '<strong>' + cmd.name + '</strong> <br>';

    res += 'Linked state: ' + cmd.linkedState + '<br>';

//        console.log(cmd.parameter);


    $.each(cmd.parameter, function (key, value) {
        //console.log(value);
        res += 'Parameter: ' + key + '<br>';
        console.log(value);
        console.log(cmd.parameters);

        //console.log(value);
        if (value.hasOwnProperty('min')) {
            res += renderRange(value, 'Expects:');
        } else if (value.hasOwnProperty('values')) {
            res += renderPresets(value);
        } else if(typeof value === 'string') {
            // Ref to Complextype
            res += renderComplexTypeLink(value) + '<br>';
        } else {
            console.log('ERROR ' + value);
        }

        res += renderTopic(cmdTopic);

        var cmdId = $.md5(cmdTopic);

        res += '<form class="form-inline">';
        res += '    <div class="form-group">';
        res += '        <textarea id="' + cmdId + '" rows="2" cols="20" style="font-family:monospace;" class="form-control" ></textarea>';
        res += '        <button data-id="' + cmdTopic + '" data-topic="' + cmdTopic + '" class="btn btn-default cmd" type="button">Send</button>';
        res += '    </div>';
        res += '</form>';
    });

    return res + '<br>';
}

function renderEvent(event, deviceTopic) {
    var eventName = event.name;
    var eventTopic = deviceTopic + '/events/' + event.name;
    var evtId = $.md5(eventTopic);

    var res = '<strong>' + eventName + '</strong> <br>';
    res += event.description + '<br>';


//        res += renderRange(event.range, 'Value:');
    if (event.range && event.range.hasOwnProperty('type')) {
        res += renderRange(event.range, 'Value:');
    } else if (event.options && event.options.hasOwnProperty('values')) {
        res += renderPresets(event.options);
    } else if(event.complexTypeRef !== null) {
        // Ref to Complextype
        res += renderComplexTypeLink(event.complexTypeRef) + '<br>';
    } else {
        console.log('ERROR ' + event);
    }

    res += renderTopic(eventTopic)+ '<br>';
    res += '<form class="form-inline">';
    res += '    <div class="form-group">';
    res += '        <button data-topic="' + eventTopic + '" class="btn btn-default evt" type="button">Subscribe</button>';
    res += '        <input id="' + evtId + '"type="text" class="form-control">';
    res += '    </div>';
    res += '</form>';

    return res +'<br>';
}

function renderType(type, deviceTopic) {
    var res = '<strong> <p id="type_'+ type.name +'">' + type.name + '</p></strong>';

    res += 'Properties:' + '<br>';
    res += '<ul>';
    $.each(type.properties, function (index, value) {
        res += '<li>' + value.name + ': ' + value.type  + '</li>';
        res += '</li>'

    });
    res += '</ul>';

    return res;
}

function renderTopic(topic) {
    var res = ' <div class="input-group">';
    res += '         <span class="input-group-addon" id="basic-addon3">Topic</span> ';
    res += '        <input class="form-control" id="disabledInput" type="text" value="'+topic+'" readonly>';
    res += '    </div>';
    return res;
}

function renderPresets(presets) {
    var res = '';
    $.each(presets, function (index, value) {
        res += value + ' ';
    });

    return res + '<br>';
}

function renderRange(range, label) {

    if (range == null) {
        return '';
    }
    var res = label;
    res += '<ul>';
    res += '<li>Type: ' + range.type + '</li>';
//        console.log(range.min);
    if(range.min !== '' && !isNaN(range.min)) {
        res += '<li>Min: ' + range.min + '</li>';
    }
    if(range.max !== '' && !isNaN(range.max)) {
        res += '<li>Max: ' + range.max + '</li>';
    }

    res += '</ul>';
    return res;
}

function onDisconnect(reason) {
    console.log("disconnected - " + reason);
    alert("disconnected - " + reason);
}

function evtHandler() {
    $('.evt').click(function () {
        //var eventId = $(this).data('id');
        var eventTopic = $(this).data('topic');

        var sub;
        if (!(eventTopic in subscriptions)) {
            sub = new Subscription();
            subscriptions[eventTopic] = sub;
        } else {
            sub = subscriptions[eventTopic];
        }

        // TODO: topic übergeben


        //console.log($(this).text());
        $(this).toggleClass("active");
        if ($(this).text() === 'Subscribe') {
            sub.subscribe(eventTopic);
            $(this).text('Unsubscribe');
        } else {
            $(this).text('Subscribe');
            sub.unsubscribe(eventTopic);
        }


    });

    $('.cmd').click(function () {

        var cmdTopic = $(this).data('topic');

        var value = $('#'+ $.md5(cmdTopic)).val();
        console.log(value);
        console.log(cmdTopic);

        var pub = new Publisher();
        pub.publish(value, cmdTopic);
    });

//        $('#collapseComplex_Types').addClass('in');
}
