package com.example.monitor;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class MachineMovedEventListener {
    private final MeterRegistry meterRegistry;
    private final Map<String, ExpirableItem<Gauge>> idToExpirableGaugeMap;

    public MachineMovedEventListener(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        this.idToExpirableGaugeMap = new HashMap<>();
    }

    @EventListener
    public void onMachineMoved(MachineMoved e) {
        Machine machine = e.machine();
        idToExpirableGaugeMap.computeIfAbsent(machine.getId(), key -> {
            Gauge gauge = Gauge.builder("machine.x", machine::getX)
                    .tag("id", key)
                    .tag("zone", machine.getZone())
                    .register(meterRegistry);
            return new ExpirableItem<>(gauge);
        }).touch();
    }

    @Scheduled(fixedDelay = 2000)
    void clearExpiredGauges() {
        LocalDateTime criteriaDateTime = LocalDateTime.now().minusSeconds(30);
        idToExpirableGaugeMap.values().removeIf(i -> {
            boolean expired = i.getTouchedAt().isBefore(criteriaDateTime);
            if (expired) {
                meterRegistry.remove(i.getItem());
            }
            return expired;
        });
        log.debug("gauges: {}", idToExpirableGaugeMap.values().size());
    }

    @Getter
    private static class ExpirableItem<T> {
        private final T item;
        private LocalDateTime touchedAt;

        private ExpirableItem(T item) {
            this.item = item;
            this.touch();
        }

        void touch() {
            this.touchedAt = LocalDateTime.now();
        }
    }

}
