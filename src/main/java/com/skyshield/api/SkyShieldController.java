package com.skyshield.api;

import com.skyshield.model.*;
import com.skyshield.repo.*;
import com.skyshield.service.AirQualityService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api")
public class SkyShieldController {
  private final SensorNodeRepository nodes; private final AirReadingRepository readings; private final AlertRepository alerts; private final AirQualityService air;
  public SkyShieldController(SensorNodeRepository nodes, AirReadingRepository readings, AlertRepository alerts, AirQualityService air){this.nodes=nodes;this.readings=readings;this.alerts=alerts;this.air=air;}

  @GetMapping("/nodes") public List<SensorNode> nodes(){return nodes.findAll();}
  @PostMapping("/nodes") @ResponseStatus(HttpStatus.CREATED) public SensorNode createNode(@Valid @RequestBody NodeRequest request){
    if(nodes.findByNodeCode(request.nodeCode()).isPresent()) throw new IllegalArgumentException("Node code already exists");
    return nodes.save(new SensorNode(request.nodeCode(),request.name(),request.location()));
  }
  @PatchMapping("/nodes/{id}/active") public SensorNode setActive(@PathVariable Long id, @RequestParam boolean value){SensorNode node=nodes.findById(id).orElseThrow();node.setActive(value);return nodes.save(node);}

  /** Endpoint called by the ESP32 every 1–5 minutes. */
  @PostMapping("/readings") @ResponseStatus(HttpStatus.CREATED) public AirReading addReading(@Valid @RequestBody ReadingRequest request){return air.addReading(request);}
  @GetMapping("/nodes/{code}/readings") public List<AirReading> history(@PathVariable String code){return readings.findTop20ByNodeNodeCodeOrderByRecordedAtDesc(code);}
  @GetMapping("/dashboard") public List<Map<String,Object>> dashboard(){
    return nodes.findAll().stream().map(n -> { Map<String,Object> row=new LinkedHashMap<>(); row.put("nodeCode",n.getNodeCode());row.put("name",n.getName());row.put("location",n.getLocation());row.put("latest",readings.findTop1ByNodeNodeCodeOrderByRecordedAtDesc(n.getNodeCode()).stream().findFirst().orElse(null));return row; }).toList();
  }
  @GetMapping("/alerts/open") public List<Alert> openAlerts(){return alerts.findByAcknowledgedFalseOrderByCreatedAtDesc();}
  @PatchMapping("/alerts/{id}/acknowledge") public Alert acknowledge(@PathVariable Long id){Alert a=alerts.findById(id).orElseThrow();a.acknowledge();return alerts.save(a);}
  @GetMapping("/levels/advice/{level}") public Map<String,String> advice(@PathVariable AirLevel level){return Map.of("level",level.name(),"advice",air.advice(level));}
}
