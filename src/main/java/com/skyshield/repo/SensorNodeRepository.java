package com.skyshield.repo;
import com.skyshield.model.SensorNode;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface SensorNodeRepository extends JpaRepository<SensorNode, Long> { Optional<SensorNode> findByNodeCode(String nodeCode); }
