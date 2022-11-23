package com.example.monitor;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;

@Slf4j
@Getter
@Setter
@ToString
class Machine {
    private String id;
    private Double x;
    private Double speed;
    private Double dest;
    private String zone;

    /**
     * speed 만큼 목적지(dest)로 이동한다. 도착하면 다음 목적지와 속도가 새로 정해진다.
     */
    void move() {
        int direction = x > dest ? -1 : 1;
        log.debug("next x: {}, {}", x + (speed * direction), this);

        double diff = Math.abs(x - dest);
        if (diff <= speed) {
            Random randomizer = new Random();
            dest = randomizer.nextDouble(100.0);
            speed = randomizer.nextDouble();
            return;
        }
        x = x + (speed * direction);
    }

    public Double getX() {
        return x;
    }
}
