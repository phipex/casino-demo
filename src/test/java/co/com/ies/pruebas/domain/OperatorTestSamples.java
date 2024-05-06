package co.com.ies.pruebas.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class OperatorTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Operator getOperatorSample1() {
        return new Operator().id(1L).name("name1").nit("nit1").contract("contract1");
    }

    public static Operator getOperatorSample2() {
        return new Operator().id(2L).name("name2").nit("nit2").contract("contract2");
    }

    public static Operator getOperatorRandomSampleGenerator() {
        return new Operator()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .nit(UUID.randomUUID().toString())
            .contract(UUID.randomUUID().toString());
    }
}
