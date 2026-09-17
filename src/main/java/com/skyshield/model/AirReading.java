package com.skyshield.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
public class AirReading {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
  @ManyToOne(optional = false) private SensorNode node;
  private Instant recordedAt;
  private double pm25, pm10, gasPpm, oxygenPercent, co2Ppm, coPpm, no2Ppm, temperature, humidity;
  @Enumerated(EnumType.STRING) private AirLevel level;
  private boolean alertRaised;
  protected AirReading() {}
  public AirReading(SensorNode node, Instant recordedAt, double pm25, double pm10, double gasPpm, double oxygenPercent, double co2Ppm, double coPpm, double no2Ppm, double temperature, double humidity, AirLevel level, boolean alertRaised) { this.node=node; this.recordedAt=recordedAt; this.pm25=pm25; this.pm10=pm10; this.gasPpm=gasPpm; this.oxygenPercent=oxygenPercent; this.co2Ppm=co2Ppm; this.coPpm=coPpm; this.no2Ppm=no2Ppm; this.temperature=temperature; this.humidity=humidity; this.level=level; this.alertRaised=alertRaised; }
  public Long getId(){return id;} public SensorNode getNode(){return node;} public Instant getRecordedAt(){return recordedAt;} public double getPm25(){return pm25;} public double getPm10(){return pm10;} public double getGasPpm(){return gasPpm;} public double getOxygenPercent(){return oxygenPercent;} public double getCo2Ppm(){return co2Ppm;} public double getCoPpm(){return coPpm;} public double getNo2Ppm(){return no2Ppm;} public double getTemperature(){return temperature;} public double getHumidity(){return humidity;} public AirLevel getLevel(){return level;} public boolean isAlertRaised(){return alertRaised;}
}
