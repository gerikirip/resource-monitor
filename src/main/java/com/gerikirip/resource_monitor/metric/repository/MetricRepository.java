package com.gerikirip.resource_monitor.metric.repository;

import com.gerikirip.resource_monitor.metric.entity.Metric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MetricRepository extends JpaRepository<Metric, Long> {
    List<Metric> findByServerIdOrderByRecordedAtDesc(Long serverId);

    Optional<Metric> findTopByServerIdOrderByRecordedAtDesc(Long serverId);
}
