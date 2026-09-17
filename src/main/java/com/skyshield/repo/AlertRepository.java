package com.skyshield.repo;
import com.skyshield.model.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface AlertRepository extends JpaRepository<Alert, Long> { List<Alert> findByAcknowledgedFalseOrderByCreatedAtDesc(); }
