#include <WiFi.h>
#include <HTTPClient.h>
#include <DHTesp.h>

// Wokwi Virtual Sensor Node for the SkyShield Spring Boot API
const char* WIFI_SSID = "Wokwi-GUEST";
const char* API_URL = "https://YOUR-RENDER-SERVICE.onrender.com/api/readings";
const char* NODE_CODE = "WOKWI-01";

const int DHT_PIN = 4;
const int MQ2_PIN = 35;
const int PM25_PIN = 34;
const int PM10_PIN = 32;
const int OXYGEN_PIN = 33;
const int CO2_PIN = 25;
const int CO_PIN = 26;
const int NO2_PIN = 27;
DHTesp dht;

float mapFloat(int raw, float low, float high) {
  return low + (raw / 4095.0f) * (high - low);
}

void connectWifi() {
  WiFi.begin(WIFI_SSID, "", 6);
  while (WiFi.status() != WL_CONNECTED) { delay(300); Serial.print('.'); }
  Serial.println("\nWiFi connected");
}

void postReading() {
  TempAndHumidity climate = dht.getTempAndHumidity();
  // MQ2 is the Wokwi gas sensor. Other values are adjustable virtual potentiometers.
  float voc = mapFloat(analogRead(MQ2_PIN), 0, 400);
  float pm25 = mapFloat(analogRead(PM25_PIN), 0, 300);
  float pm10 = mapFloat(analogRead(PM10_PIN), 0, 500);
  float oxygen = mapFloat(analogRead(OXYGEN_PIN), 18.0, 23.5);
  float co2 = mapFloat(analogRead(CO2_PIN), 400, 6000);
  float co = mapFloat(analogRead(CO_PIN), 0, 50);
  float no2 = mapFloat(analogRead(NO2_PIN), 0, 1.2);

  String body = "{\"nodeCode\":\"" + String(NODE_CODE) + "\",\"pm25\":" + String(pm25, 1) +
    ",\"pm10\":" + String(pm10, 1) + ",\"gasPpm\":" + String(voc, 1) +
    ",\"oxygenPercent\":" + String(oxygen, 2) + ",\"co2Ppm\":" + String(co2, 0) +
    ",\"coPpm\":" + String(co, 2) + ",\"no2Ppm\":" + String(no2, 3) +
    ",\"temperature\":" + String(climate.temperature, 1) + ",\"humidity\":" + String(climate.humidity, 1) + "}";
  HTTPClient http;
  http.begin(API_URL);
  http.addHeader("Content-Type", "application/json");
  int status = http.POST(body);
  Serial.printf("POST status: %d\n%s\n", status, body.c_str());
  http.end();
}

void setup() {
  Serial.begin(115200);
  dht.setup(DHT_PIN, DHTesp::DHT22);
  analogReadResolution(12);
  connectWifi();
}

void loop() {
  if (WiFi.status() != WL_CONNECTED) connectWifi();
  postReading();
  delay(15000); // Upload a simulated reading every 15 seconds
}
