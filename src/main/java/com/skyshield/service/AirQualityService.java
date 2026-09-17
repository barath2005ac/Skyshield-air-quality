package com.skyshield.service;

import com.skyshield.api.ReadingRequest;
import com.skyshield.model.*;
import com.skyshield.repo.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;

@Service
public class AirQualityService {
  private final SensorNodeRepository nodes; private final AirReadingRepository readings; private final AlertRepository alerts;
  public AirQualityService(SensorNodeRepository nodes, AirReadingRepository readings, AlertRepository alerts){this.nodes=nodes;this.readings=readings;this.alerts=alerts;}
  public AirLevel classify(double pm25, double pm10, double voc, double oxygen, double co2, double co, double no2) {
    if (pm25 > 250 || pm10 > 430 || voc > 300 || oxygen < 19.5 || co2 > 5000 || co > 35 || no2 > 1.0) return AirLevel.HAZARDOUS;
    if (pm25 > 125 || pm10 > 250 || voc > 200 || co2 > 2000 || co > 9 || no2 > .2) return AirLevel.UNHEALTHY;
    if (pm25 > 55 || pm10 > 150 || voc > 120 || co2 > 1200 || co > 4 || no2 > .1) return AirLevel.UNHEALTHY_SENSITIVE;
    if (pm25 > 12 || pm10 > 54 || voc > 70 || co2 > 800 || co > 2 || no2 > .05) return AirLevel.MODERATE;
    return AirLevel.GOOD;
  }
  @Transactional public AirReading addReading(ReadingRequest r) {
    // A remote Wokwi node may begin posting after a free host has restarted.
    // Create it automatically so the simulator does not need a separate registration call.
    SensorNode node=nodes.findByNodeCode(r.nodeCode()).orElseGet(() -> nodes.save(new SensorNode(r.nodeCode(), "IoT Sensor " + r.nodeCode(), "Remote Wokwi Simulation")));
    if (!node.isActive()) throw new IllegalStateException("Node is inactive");
    AirLevel level=classify(r.pm25(),r.pm10(),r.gasPpm(),r.oxygenPercent(),r.co2Ppm(),r.coPpm(),r.no2Ppm()); boolean danger=level==AirLevel.UNHEALTHY || level==AirLevel.HAZARDOUS;
    AirReading reading=readings.save(new AirReading(node, r.recordedAt()==null?Instant.now():r.recordedAt(),r.pm25(),r.pm10(),r.gasPpm(),r.oxygenPercent(),r.co2Ppm(),r.coPpm(),r.no2Ppm(),r.temperature(),r.humidity(),level,danger));
    if(danger) alerts.save(new Alert(node,level,advice(level)+" at "+node.getLocation()));
    return reading;
  }
  public String advice(AirLevel level) { return switch(level) { case GOOD -> "Air is good: outdoor activity is safe."; case MODERATE -> "Moderate pollution: sensitive people should limit exposure."; case UNHEALTHY_SENSITIVE -> "Unhealthy for sensitive groups: wear a mask outdoors."; case UNHEALTHY -> "Unhealthy air: avoid outdoor exercise and wear an N95 mask."; case HAZARDOUS -> "Hazardous air: stay indoors and keep ventilation filtered.";}; }
}
