package co.com.ies.pruebas.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SlotTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Slot getSlotSample1() {
        return new Slot().id(1L).idCasino(1).serial("serial1").nuc("nuc1");
    }

    public static Slot getSlotSample2() {
        return new Slot().id(2L).idCasino(2).serial("serial2").nuc("nuc2");
    }

    public static Slot getSlotRandomSampleGenerator() {
        return new Slot()
            .id(longCount.incrementAndGet())
            .idCasino(intCount.incrementAndGet())
            .serial(UUID.randomUUID().toString())
            .nuc(UUID.randomUUID().toString());
    }
}
