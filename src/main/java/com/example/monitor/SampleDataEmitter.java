package com.example.monitor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Tags
 * - id:
 */
@Slf4j
@Component
public class SampleDataEmitter {
    private final ApplicationEventPublisher publisher;
    private final List<Machine> machineList;
    private final Random randomizer;
    private final AtomicInteger idCounter;

    public SampleDataEmitter(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
        this.machineList = new ArrayList<>(10);
        this.randomizer = new Random();
        this.idCounter = new AtomicInteger(0);
    }

    @PostConstruct
    void init() {
        int size = 5;
        for (int i = 0; i < size; i++) {
            machineList.add(createMachine());
        }
    }

    @Scheduled(fixedDelay = 1000)
    public void work() {
        for (Machine machine : machineList) {
            machine.move();
            publisher.publishEvent(new MachineMoved(machine));
        }
    }

    @Scheduled(fixedDelay = 20 * 1000)
    public void replaceMachine() {
        int idx = randomizer.nextInt(machineList.size());
        machineList.remove(idx);
        machineList.add(createMachine());
    }

    Machine createMachine() {
        Machine machine = new Machine();
        machine.setId("id-" + idCounter.getAndIncrement());
        machine.setX(randomizer.nextDouble(100.0));
        machine.setSpeed(randomizer.nextDouble());
        machine.setDest(randomizer.nextDouble(100.0));
        machine.setZone(randomizer.nextBoolean() ? "A" : "B");
        return machine;
    }
}
