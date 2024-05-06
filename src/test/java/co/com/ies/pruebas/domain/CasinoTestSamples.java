package co.com.ies.pruebas.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CasinoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Casino getCasinoSample1() {
        return new Casino().id(1L).nit("nit1").name("name1").direction("direction1");
    }

    public static Casino getCasinoSample2() {
        return new Casino().id(2L).nit("nit2").name("name2").direction("direction2");
    }

    public static Casino getCasinoRandomSampleGenerator() {
        return new Casino()
            .id(longCount.incrementAndGet())
            .nit(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .direction(UUID.randomUUID().toString());
    }
}
