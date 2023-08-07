package com.blekione.fsb;

import com.blekione.fsb.test.TestUtils;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.time.Clock;

@TestConfiguration
public class FixedClockConfiguration {

    @Bean
    public Clock clock() {
        return TestUtils.getFixedClock();
    }
}
