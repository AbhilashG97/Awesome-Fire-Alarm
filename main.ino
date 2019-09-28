#include <ESP8266WiFi.h>

//needed for library
#include <DNSServer.h>
#include <ESP8266WebServer.h>
#include <ESP8266HTTPClient.h>
#include "WiFiManager.h"



String pub_key = "pub-c-957647d4-c417-4a35-969f-95d00a04a33f";
String sub_key = "sub-c-0bbe0cb0-e2b6-11e8-a575-5ee09a206989";
double saved_threshold = 60;


// Structure for channel metadata
struct chann
{
  String name;
  String timestamp;
};

//struct chann user_settings;
struct chann alarm = {"alarm", "0"};
struct chann temperature = {"temperature", "0"};
struct chann user_settings = {"user_settings", "0"};

String urlencode(String str)
{
    String encodedString="";
    char c;
    char code0;
    char code1;
    char code2;
    for (int i =0; i < str.length(); i++){
      c=str.charAt(i);
      if (c == ' '){
        encodedString+= '+';
      } else if (isalnum(c)){
        encodedString+=c;
      } else{
        code1=(c & 0xf)+'0';
        if ((c & 0xf) >9){
            code1=(c & 0xf) - 10 + 'A';
        }
        c=(c>>4)&0xf;
        code0=c+'0';
        if (c > 9){
            code0=c - 10 + 'A';
        }
        code2='\0';
        encodedString+='%';
        encodedString+=code0;
        encodedString+=code1;
        //encodedString+=code2;
      }
      yield();
    }
    return encodedString;
    
}



void configModeCallback (WiFiManager *myWiFiManager) {
  Serial.println("Entered config mode");
  Serial.println(WiFi.softAPIP());
  //if you used auto generated SSID, print it
  Serial.println(myWiFiManager->getConfigPortalSSID());
}

void setup() {
  
  Serial.begin(115200);
  
  //WiFiManager
  WiFiManager wifiManager;
  
  pinMode(16,OUTPUT);
  //reset settings - for testing
  wifiManager.setAPCallback(configModeCallback);

  if(!wifiManager.autoConnect()) {
    Serial.println("failed to connect and hit timeout");
    
    ESP.reset();
    delay(1000);
  } 

  Serial.println("Connected to WiFi");

  pinMode(LED_BUILTIN, OUTPUT);
  digitalWrite(LED_BUILTIN, HIGH);
 
}


String publish_request (String channel, String msg, String signature, String callback)
{
  HTTPClient http;
  
  //publishing events
  String Link = "http://pubsub.pubnub.com/publish/"; //pub-c-957647d4-c417-4a35-969f-95d00a04a33f/sub-c-0bbe0cb0-e2b6-11e8-a575-5ee09a206989/0/test/0/%22Hello%20World%22";
  Link += pub_key + "/" + sub_key;                   // {publish_key}/{subscribe_key}
  Link += "/" + signature + "/";                     // {signature}
  Link += channel + "/";                             // {channname}
  Link += callback + "/";                            // {callback_function}
  Link += msg;                                       // {message}
  
  http.begin(Link);                                  //Specify request destination
  
  int httpCode = http.GET();                         //Send the request
  String payload = http.getString();                 //Get the response payload

  http.end();                                        //Close connection

  return payload;
}

String subscribe_request (chann* channel, String callback)
{
  
  HTTPClient http;
  
  // Subscribing to events
  String Link = "http://pubsub.pubnub.com/subscribe/";  //sub-c-0bbe0cb0-e2b6-11e8-a575-5ee09a206989/threshold/0/" + timestamp;
  Link += sub_key + "/";
  Link += channel->name;
  Link += "/" + callback + "/";
  Link += channel->timestamp;
  
  Serial.println(channel->timestamp);
  http.begin(Link);
  
  int httpCode = http.GET();
  String payload = http.getString();
  Serial.println(payload);

  Serial.println(httpCode);

  int i;
  String ret = "";
  Serial.println(payload);
  if (httpCode == 200)
  {

    ret = parse_(payload);    
    
    channel->timestamp = "";
    for (i=0;payload[i]!=']';i++);

    i += 3;

    for (; payload[i]!='"'; i++)
    {
      channel->timestamp += payload[i];
    }
    
  }

  http.end();
  Serial.println(channel->timestamp);
  
  return ret;

}

String parse_ (String input)
{
  if (input[2] == ']')
  {
    return "";
  }
  int i;
  for (i=0; input[i]!=':'; i++);
  i += 2;

  String ret;
  for (;input[i]!='"';i++)
  {
    ret += input[i];
  }

  return ret;
}


String command (chann* channel, String callback)
{
  String instruction = subscribe_request(channel, callback);
  Serial.println(instruction);
  if (instruction == "LED ON")
  {    
    
    digitalWrite(LED_BUILTIN, LOW);
    Serial.println("Instruction: LED ON");  // turn the LED off by making the voltage LOW
    delay(1000);
    
    publish_request (channel->name, urlencode("{\"message\":\"Done\"}"), "0", "0");
  }
  else if (instruction == "LED OFF")
  {
    
    digitalWrite(LED_BUILTIN, HIGH);
    Serial.println("Instruction: LED OFF"); // turn the LED off by making the voltage LOW
    delay(1000);
    
    publish_request (channel->name, urlencode("{\"message\":\"Done\"}"), "0", "0");
  }
  else if (instruction == "ALARM OFF")
  {
    digitalWrite(LED_BUILTIN, HIGH);
    tone(16, 0);
    Serial.println("Instruction: ALARM OFF");

    publish_request (channel->name, urlencode("{\"message\":\"Done\"}"), "0", "0");
  }
  else
  {
      String num = instruction;
      Serial.println("User settings timestamp: " + String(user_settings.timestamp));
      Serial.print("Threshold recieved: "+num);
      if (num != "")
      {
          double n = (double)num.toInt();
          if ( n > 20 )
          {
            saved_threshold = n;
          }
      }
  }

  return instruction;
}

void raise_alarm ()
{
    digitalWrite(LED_BUILTIN,LOW);
    tone(16, 500);
    delay(500);
    tone(16, 1000);
    digitalWrite(LED_BUILTIN,HIGH);
    
}

double get_temp ()
{
  double analogValue = analogRead(A0);
  Serial.print("Sensor Value: ");
  Serial.print(analogValue);
  
  delay(500);
  double analogVolts = (analogValue * 3.3)/1024;
  double temp = (analogVolts - 0.5) * 100;
  Serial.print(" Voltage: ");
  Serial.print(analogVolts);
  Serial.print(" Temperature: ");
  Serial.println(temp);
  return temp;
  
}

double set_temp ()
{
  String num = subscribe_request (&user_settings, "0");
  Serial.println("User settings timestamp: " + String(user_settings.timestamp));
  Serial.print("Threshold recieved: "+num);
  if (num == "")
  {
    return 0;
  }
  double n = (double)num.toInt();
  return n;
}


void send_temp (double temp, String channel)
{
  String t = String(temp);
  publish_request(channel, urlencode("{\"message\":\""+t+"\"}"), "0", "0");
}


int counter = 0;

void loop ()
{ 
  command(&user_settings, "0");
  double currentTemp = get_temp();

  send_temp(currentTemp, temperature.name);
  Serial.println(saved_threshold);
  if ( currentTemp > saved_threshold )
  {

    publish_request(alarm.name, urlencode("{\"message\":\"House-is-on-fire\",\"temperature\":\"" + String(currentTemp) + "\"}"), "0", "0");
    
    while (1)
    {
      raise_alarm();
    
      String cmd = command(&user_settings, "0");
      if (cmd == "ALARM OFF")
      {
        break;
      }
    }
  }
  
  delay(1000);  // 1 second interval between each new round

}
