#include <WiFi.h>                                                
#include <IOXhop_FirebaseESP32.h>  

#define FIREBASE_HOST "gas-warning-default-rtdb.firebaseio.com/" //dia chi firebase
#define FIREBASE_AUTH "9aLdbaRd9R5zFfYqK8mRUAzmoufOUWgxvd9lTIVc" //ma xac nhan
#define ssid       "DucThieu"             // Ten WiFi SSID
#define password   "0898683656"             // Mat khau wifi  

int led1=22, led2=3, led3=19;
int tt_dem_1=0, tt_dem_2=0, tt_dem_3=0;
const int mq2_1=36;
const int mq2_2=34;
const int mq2_3=32;

void setup() { 
  pinMode(led1,OUTPUT); 
  pinMode(led2,OUTPUT);
  pinMode(led3,OUTPUT);
  pinMode(mq2_1,INPUT);
  pinMode(mq2_2,INPUT);
  pinMode(mq2_3,INPUT);
  Serial.begin(115200); 
  WiFi.begin (ssid, password);
  Serial.print("Connecting");
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(500);
  }
  Serial.print("Connected");
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);  
}

void loop() {
  int sensor_data_1=analogRead(mq2_1);
  Firebase.setInt("MQ2_Phong1", sensor_data_1);
  if(Firebase.failed()) 
  {
    Serial.println(Firebase.error());
    return;
  }
  
  if(sensor_data_1<2048)
  {
    digitalWrite(led1, LOW);
    Firebase.setString("TT_CHUONG_1","OFF");
    tt_dem_1=0;
  }
  else if (sensor_data_1>=2048)
  {
    digitalWrite(led1, HIGH);
    tt_dem_1++;
    if(tt_dem_1==5)
    {
      Firebase.setString("TT_CHUONG_1","ON"); 
      tt_dem_1=0;
    }
  }

  int sensor_data_2=analogRead(mq2_2);
  Firebase.setInt("MQ2_Phong2", sensor_data_2);
  if(Firebase.failed()) 
  {
    Serial.println(Firebase.error());
    return;
  }
  
  if(sensor_data_2<2048)
  {
    digitalWrite(led2, LOW);
    Firebase.setString("TT_CHUONG_2","OFF");
    tt_dem_2=0;
  }
  else if (sensor_data_2>=2048)
  {
    digitalWrite(led2, HIGH);
    tt_dem_2++;
    if(tt_dem_2==5)
    { 
      Firebase.setString("TT_CHUONG_2","ON"); 
      tt_dem_2=0;
    }
  }

  int sensor_data_3=analogRead(mq2_3);
  Firebase.setInt("MQ2_Phong3", sensor_data_3);
  if(Firebase.failed()) 
  {
    Serial.println(Firebase.error());
    return;
  }
  
  if(sensor_data_3<2048)
  {
    digitalWrite(led3, LOW);
    Firebase.setString("TT_CHUONG_3","OFF");
    tt_dem_3=0;
  }
  else if (sensor_data_3>=2048)
  {
    digitalWrite(led3, HIGH);
    tt_dem_3++;
    if(tt_dem_3==5)
    {
      Firebase.setString("TT_CHUONG_3","ON"); 
      tt_dem_3=0;
    }
  }
  sensor_data_1=0; sensor_data_2=0; sensor_data_3=0;
  delay(2000);  
}
