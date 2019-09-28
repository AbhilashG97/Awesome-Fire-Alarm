const express = require('express');
const app = require('express')();
const path = require('path');
const http = require('http').Server(app);
const bodyParser = require('body-parser');
const io = require('socket.io')(http);
const publishKey = process.env.publishKey || 'pub-c-957647d4-c417-4a35-969f-95d00a04a33f';
const subscribeKey = process.env.subscribeKey || 'sub-c-0bbe0cb0-e2b6-11e8-a575-5ee09a206989'; 
const PORT = process.env.PORT || 3000;
const _ = require('underscore');

app.use(bodyParser.urlencoded({'extended':'true'})); 
app.use(bodyParser.json());  
app.use("/public", express.static(path.join(__dirname, 'public')));


app.get('/',function(req,res){
    res.sendfile('./public/index.html');
});






http.listen(PORT,function(){
    console.log('Server Started at PORT: '+PORT);
});