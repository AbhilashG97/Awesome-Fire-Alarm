# :fire: :computer: Real Time Fire Alarm System :fire: :computer:

## :question: About (Practical usage)
Fire accidents are not common. But when they take place, they cause great harm and destruction and sometimes loss of life. This device helps detect to fire accidents, and can also be used to monitor the temperature of an environment. 

This device is quite handy as it allows to remotely control and monitor the temperature of their devices along with real-time updates of the condition. Also, it has a significant application in fire detection wherein it gives the temperature values detected by the sensor at regular intervals to the server which further passes on the message to the user from where they can control the ringing buzzer and act accordingly

## :computer: Technical Description

We have connected the temperature sensor to the ESP8266 Microprocessor using the ADC pin on the board from where the analog voltage values are fetched and converted to ```Celsius``` by proper mathematical conversions. This temperature is then further sent to the server. This data is then fetched by the end user. There is a fixed threshold value set above for the device beforehand. If the temperature received at this end is above the set threshold value then the buzzer rings, LED begins to toggle and the user gets an appropriate warning message. On that basis, one can perform the following actions depending on the requirements :

* Set the buzzer off.
* Change the threshold temperature set to the system. 
* Set the blinking LED off. 

The temperature data is periodically sent to the HTTP server implemented for further analysis. 

## :busts_in_silhouette: Team Members and their Contribution

* **Abhilash G** - Worked on the Android application to read data to and from the PubNub and provide remote functionality like notification alert, disabling alarm remotely, logs and live temperature feed to the user.

* **Gopala Krishnan** - Worked on the web client which connected the PubNub server to the web-based frontend hosted using Heroku app.

* **Prashant Iyer** - Worked on ringing the buzzer and sensing the temperature from the temperature sensor and communicating it to the ESP using ADC.

* **Siddhant Gupta** - Worked on connecting ESP to the PubNub server. Wrote publish and subscribe functionality, using HTTP GET requests.

## :movie_camera: Demo

Link: https://www.youtube.com/watch?v=xACrICINHx4

The web-client is live at : https://fire-system.herokuapp.com

The code for the android client can be found here - https://github.com/AbhilashG97/WatermelonBerryPopsicle
