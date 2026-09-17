package com.skyshield.model;

import jakarta.persistence.*;

@Entity
public class SensorNode {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
  @Column(unique = true, nullable = false) private String nodeCode;
  private String name;
  private String location;
  private boolean active = true;
  protected SensorNode() {}
  public SensorNode(String nodeCode, String name, String location) { this.nodeCode=nodeCode; this.name=name; this.location=location; }
  public Long getId(){return id;} public String getNodeCode(){return nodeCode;} public String getName(){return name;} public String getLocation(){return location;} public boolean isActive(){return active;}
  public void setName(String name){this.name=name;} public void setLocation(String location){this.location=location;} public void setActive(boolean active){this.active=active;}
}
