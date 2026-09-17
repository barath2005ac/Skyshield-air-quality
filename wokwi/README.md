# Connect the Wokwi virtual ESP32 to SkyShield

## Virtual parts in this simulation

| Wokwi part | Dashboard value | How to change it |
|---|---|---|
| DHT22 | Temperature and humidity | Click the DHT22 while running and use its sliders. |
| MQ2 gas sensor | VOC/general gas (`gasPpm`) | Click it and change ppm. |
| Potentiometer `pm25` | PM2.5 | Turn its knob. |
| Potentiometer `pm10` | PM10 | Turn its knob. |
| Potentiometer `oxygen` | O2 percentage | Turn its knob. |
| Potentiometer `co2` | CO2 ppm | Turn its knob. |
| Potentiometer `co` | CO ppm | Turn its knob. |
| Potentiometer `no2` | NO2 ppm | Turn its knob. |

The potentiometers are **virtual substitutes** for gas and particle sensors; they let you demonstrate separate readings in Wokwi even when an exact sensor model is unavailable.

## One-time Spring Boot setup

1. Start the backend with `mvn spring-boot:run`.
2. Register the Wokwi node once in a second terminal:

```powershell
Invoke-RestMethod -Method Post -Uri "http://localhost:8080/api/nodes" -ContentType "application/json" -Body '{"nodeCode":"WOKWI-01","name":"Wokwi Virtual Node","location":"Simulation Lab"}'
```

## Wokwi setup

1. Create a new **ESP32** project at Wokwi.
2. Copy `sketch.ino` and `diagram.json` from this folder into the Wokwi editor.
3. Add the **DHT sensor library for ESPx** when Wokwi asks to install `DHTesp`.
4. Install and run the **Wokwi Private IoT Gateway** on your computer, then enable it from Wokwi with `F1` → `Enable Private Wokwi IoT Gateway`.
5. Start the Wokwi simulation. Its Serial Monitor must show `POST status: 201` every 15 seconds.
6. Open `http://localhost:8080` and adjust a sensor/knob. The dashboard refreshes automatically.

`host.wokwi.internal:8080` in `sketch.ino` means your Spring Boot server running on the same computer. It requires the Wokwi Private IoT Gateway; Wokwi's public gateway cannot access a local `localhost` server.
