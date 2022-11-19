package com.example.monitor;

import io.micrometer.core.instrument.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@RestController
public class HelloController {

    private static int c;
    private Counter counter;
    private Timer timer;

    @PostConstruct
    void init() {
        /*
        # HELP hello_controller_total
        # TYPE hello_controller_total counter
        hello_controller_total 15.0
         */
        counter = Metrics.counter("hello_controller");

        /*
        # HELP g1
        # TYPE g1 gauge
        g1{tag1="value1",} 5.0
         */
        Gauge.builder("g1", () -> c)
                .tag("tag1", "value1")
                .register(Metrics.globalRegistry);


        /*
        # HELP test_timer_seconds_max
        # TYPE test_timer_seconds_max gauge
        test_timer_seconds_max 5.0
        # HELP test_timer_seconds
        # TYPE test_timer_seconds histogram
        test_timer_seconds{quantile="0.3",} 1.979711488
        test_timer_seconds{quantile="0.5",} 3.053453312
        test_timer_seconds{quantile="0.95",} 5.066719232
        test_timer_seconds_bucket{le="0.001",} 0.0
        test_timer_seconds_bucket{le="0.001048576",} 0.0
        test_timer_seconds_bucket{le="0.001398101",} 0.0
        test_timer_seconds_bucket{le="0.001747626",} 0.0
        test_timer_seconds_bucket{le="0.002097151",} 0.0
        test_timer_seconds_bucket{le="0.002446676",} 0.0
        test_timer_seconds_bucket{le="0.002796201",} 0.0
        test_timer_seconds_bucket{le="0.003145726",} 0.0
        test_timer_seconds_bucket{le="0.003495251",} 0.0
        test_timer_seconds_bucket{le="0.003844776",} 0.0
        test_timer_seconds_bucket{le="0.004194304",} 0.0
        test_timer_seconds_bucket{le="0.005592405",} 0.0
        test_timer_seconds_bucket{le="0.006990506",} 0.0
        test_timer_seconds_bucket{le="0.008388607",} 0.0
        test_timer_seconds_bucket{le="0.009786708",} 0.0
        test_timer_seconds_bucket{le="0.011184809",} 0.0
        test_timer_seconds_bucket{le="0.01258291",} 0.0
        test_timer_seconds_bucket{le="0.013981011",} 0.0
        test_timer_seconds_bucket{le="0.015379112",} 0.0
        test_timer_seconds_bucket{le="0.016777216",} 0.0
        test_timer_seconds_bucket{le="0.022369621",} 0.0
        test_timer_seconds_bucket{le="0.027962026",} 0.0
        test_timer_seconds_bucket{le="0.033554431",} 0.0
        test_timer_seconds_bucket{le="0.039146836",} 0.0
        test_timer_seconds_bucket{le="0.044739241",} 0.0
        test_timer_seconds_bucket{le="0.050331646",} 0.0
        test_timer_seconds_bucket{le="0.055924051",} 0.0
        test_timer_seconds_bucket{le="0.061516456",} 0.0
        test_timer_seconds_bucket{le="0.067108864",} 0.0
        test_timer_seconds_bucket{le="0.089478485",} 0.0
        test_timer_seconds_bucket{le="0.111848106",} 0.0
        test_timer_seconds_bucket{le="0.134217727",} 0.0
        test_timer_seconds_bucket{le="0.156587348",} 0.0
        test_timer_seconds_bucket{le="0.178956969",} 0.0
        test_timer_seconds_bucket{le="0.20132659",} 0.0
        test_timer_seconds_bucket{le="0.223696211",} 0.0
        test_timer_seconds_bucket{le="0.246065832",} 0.0
        test_timer_seconds_bucket{le="0.268435456",} 0.0
        test_timer_seconds_bucket{le="0.357913941",} 0.0
        test_timer_seconds_bucket{le="0.447392426",} 0.0
        test_timer_seconds_bucket{le="0.536870911",} 0.0
        test_timer_seconds_bucket{le="0.626349396",} 0.0
        test_timer_seconds_bucket{le="0.715827881",} 0.0
        test_timer_seconds_bucket{le="0.805306366",} 0.0
        test_timer_seconds_bucket{le="0.894784851",} 0.0
        test_timer_seconds_bucket{le="0.984263336",} 0.0
        test_timer_seconds_bucket{le="1.073741824",} 1.0
        test_timer_seconds_bucket{le="1.431655765",} 1.0
        test_timer_seconds_bucket{le="1.789569706",} 1.0
        test_timer_seconds_bucket{le="2.147483647",} 2.0
        test_timer_seconds_bucket{le="2.505397588",} 2.0
        test_timer_seconds_bucket{le="2.863311529",} 2.0
        test_timer_seconds_bucket{le="3.22122547",} 3.0
        test_timer_seconds_bucket{le="3.579139411",} 3.0
        test_timer_seconds_bucket{le="3.937053352",} 3.0
        test_timer_seconds_bucket{le="4.294967296",} 4.0
        test_timer_seconds_bucket{le="5.726623061",} 5.0
        test_timer_seconds_bucket{le="7.158278826",} 5.0
        test_timer_seconds_bucket{le="8.589934591",} 5.0
        test_timer_seconds_bucket{le="10.021590356",} 5.0
        test_timer_seconds_bucket{le="11.453246121",} 5.0
        test_timer_seconds_bucket{le="12.884901886",} 5.0
        test_timer_seconds_bucket{le="14.316557651",} 5.0
        test_timer_seconds_bucket{le="15.748213416",} 5.0
        test_timer_seconds_bucket{le="17.179869184",} 5.0
        test_timer_seconds_bucket{le="22.906492245",} 5.0
        test_timer_seconds_bucket{le="28.633115306",} 5.0
        test_timer_seconds_bucket{le="30.0",} 5.0
        test_timer_seconds_bucket{le="+Inf",} 5.0
        test_timer_seconds_count 5.0
        test_timer_seconds_sum 15.0
         */
        timer = Timer
                .builder("test.timer")
                .publishPercentiles(0.3, 0.5, 0.95)
                .publishPercentileHistogram()
                .register(Metrics.globalRegistry);
    }

    @GetMapping("/hello")
    public String sayHello() {
        /*
        # HELP hello_summary
        # TYPE hello_summary summary
        hello_summary_count{tag1="value1",} 15.0
        hello_summary_sum{tag1="value1",} 105.0
        # HELP hello_summary_max
        # TYPE hello_summary_max gauge
        hello_summary_max{tag1="value1",} 14.0
         */
        DistributionSummary summary = Metrics.summary("hello_summary", "tag1", "value1");
        summary.record(c++);
        counter.increment();

        timer.record(c, TimeUnit.SECONDS);

        return "hello";
    }
}
