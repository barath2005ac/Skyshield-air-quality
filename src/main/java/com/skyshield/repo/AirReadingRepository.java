package com.skyshield.repo;
import com.skyshield.model.AirReading;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface AirReadingRepository extends JpaRepository<AirReading, Long> { List<AirReading> findTop20ByNodeNodeCodeOrderByRecordedAtDesc(String nodeCode); List<AirReading> findTop1ByNodeNodeCodeOrderByRecordedAtDesc(String nodeCode); }
