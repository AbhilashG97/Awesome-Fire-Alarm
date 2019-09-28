var options = {};
var temperatureData = {
    labels: [],
    datasets: [
        {
            label: "Temperature",
            data: [],
            backgroundColor: "blue",
            borderColor: "lightblue",
            fill: false,
            lineTension: 0,
            radius: 5
        },{
            label:"Abnormality",
            data:[],
            backgroundColor:"red",
            borderColor:"lightred",
            fill:false,
            lineTension:0,
            radius:5
        }
        
    ]
};

var chart = document.getElementById('tempChart');
var tempChart = new Chart(chart,{
    type:'line',
    data:temperatureData
});

var alerts = [];
let time = 0;
var connected = false;
var pubnub = new PubNub({
    publishKey:'pub-c-957647d4-c417-4a35-969f-95d00a04a33f',
    subscribeKey:'sub-c-0bbe0cb0-e2b6-11e8-a575-5ee09a206989'
});

pubnub.addListener({
    status: function(statusEvent) {
        if (statusEvent.category === "PNConnectedCategory") {
            connected = true;            
        }
    },
    message: handleMessage,
});

pubnub.subscribe({
    channels:['temperature','alarm','user_settings',"threshold_temperature"]
});


function handleMessage(msg){
    if(msg.channel == 'alarm'){
        alerts.push({message:msg.message.message,temperature:parseFloat(msg.message.temperature)});
        tempChart.data.labels.push(time);
        tempChart.data.datasets[1].data.push(parseFloat(msg.message.temperature));
        tempChart.data.datasets[0].data.push(null);
        console.log('Alert data: '+msg.message.temperature);
        var cardTile = '<div class="card-panel red"><span class="white-text"> '+msg.message.message+':'+msg.message.temperature+'</span></div>'
        var template = document.createElement('template');
        cardTile = cardTile.trim();
        template.innerHTML = cardTile;
        document.getElementById('alert').appendChild(template.content.firstChild);
    }
    else if(msg.channel == 'temperature'){
        
        tempChart.data.labels.push(time);
        tempChart.data.datasets[0].data.push(parseFloat(msg.message.message));
        tempChart.data.datasets[1].data.push(null);
        console.log('new temperature data: ' + msg.message.message);
    }
    else if(msg.channel == 'user_settings'){
        let message = msg.message.message;

        if(message == 'Success-Threshold-Changed'){
            alert('Threshold changed successfully');
        }
        if(message == 'ALARM-OFF-Success'){
            alert('ALARM Successfully switched off');
        }
        
    }
    else if(msg.channel == 'threshold_temperature'){
        alert('Current Threshold ' + msg.message.message );
    }
    time += 1
    tempChart.update();
}

function changeThreshold(){
    let thresholdValue = document.getElementById('threshold').value;
    pubnub.publish({
        message:{
            threshold:thresholdValue
        },
        channel:['user_settings']
    });
}

function showThreshold(){
    pubnub.publish({
        message:{
            message:"SHOW THRESHOLD"
        },
        channel:['user_settings']
    });
}

function alarmOff(){
    pubnub.publish({
        message:{
            message:"ALARM OFF"
        },
        channel:["user_settings"]
    })
}





