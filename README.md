# SkyShield – IoT Air Pollution Monitor API

A Spring Boot backend for ESP32-based air-quality monitoring. It receives readings from MQ135, PMS5003 and DHT22 sensors, classifies pollution levels, stores history, raises alerts, and provides dashboard data for multiple locations.

## Run

Install Java 21 and Maven, then run:

```powershell
mvn spring-boot:run
```

The application starts at `http://localhost:8080`; the H2 database console is at `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:skyshield`).

## Core endpoints

| Purpose | Method and endpoint |
|---|---|
| Register an IoT node | `POST /api/nodes` |
| Send sensor reading | `POST /api/readings` |
| Latest reading per location | `GET /api/dashboard` |
| Last 20 readings at a node | `GET /api/nodes/{nodeCode}/readings` |
| View unacknowledged alerts | `GET /api/alerts/open` |
| Mark alert handled | `PATCH /api/alerts/{id}/acknowledge` |
| Enable/disable faulty node | `PATCH /api/nodes/{id}/active?value=false` |

## Create a node

```json
POST /api/nodes
{"nodeCode":"NODE-01","name":"College Gate","location":"Main Road"}
```

## ESP32 sensor payload

```json
POST /api/readings
{
  "nodeCode":"NODE-01",
  "pm25":68.4,
  "pm10":132.7,
  "gasPpm":91.2,
  "oxygenPercent":20.9,
  "co2Ppm":650,
  "coPpm":0.4,
  "no2Ppm":0.02,
  "temperature":31.6,
  "humidity":62.0
}
```

The backend assigns one of these levels: `GOOD`, `MODERATE`, `UNHEALTHY_SENSITIVE`, `UNHEALTHY`, or `HAZARDOUS`. It raises an alert for `UNHEALTHY` and `HAZARDOUS` readings and supplies practical health advice.

## Simple ESP32 request flow

After connecting to Wi-Fi, read the PMS5003, MQ135 and DHT22 sensors, form the JSON body above, then send it every 1–5 minutes using an HTTP `POST` request to:

`http://<computer-ip-address>:8080/api/readings`

For a production deployment, replace H2 with MySQL/PostgreSQL and add authentication (for example, an API key per sensor node).

## Separate gas sensors

MQ135 provides a general VOC/gas trend only; it cannot accurately identify each gas separately. For separate readings, add an **NDIR CO2 sensor** (such as MH-Z19B/SCD40), an **electrochemical O2 sensor**, a **CO sensor** (MQ-7 or electrochemical), and an **NO2 sensor** (electrochemical module). Wire each module to the ESP32 and include its calibrated reading in the corresponding API field.
