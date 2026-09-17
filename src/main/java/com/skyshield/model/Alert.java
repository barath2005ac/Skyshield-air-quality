package com.skyshield.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
public class Alert {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
  @ManyToOne(optional = false) private SensorNode node;
  private Instant createdAt = Instant.now();
  @Enumerated(EnumType.STRING) private AirLevel level;
  private String message;
  private boolean acknowledged;
  protected Alert() {}
  public Alert(SensorNode node, AirLevel level, String message){this.node=node;this.level=level;this.message=message;}
  public Long getId(){return id;} public SensorNode getNode(){return node;} public Instant getCreatedAt(){return createdAt;} public AirLevel getLevel(){return level;} public String getMessage(){return message;} public boolean isAcknowledged(){return acknowledged;} public void acknowledge(){acknowledged=true;}
}
