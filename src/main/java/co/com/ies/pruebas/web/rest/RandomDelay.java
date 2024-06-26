package co.com.ies.pruebas.web.rest;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public final class RandomDelay {

    private RandomDelay() {}

    public static void randomDelay() {
        int delay = 0;
        try {
            delay = SecureRandom.getInstanceStrong().nextInt(5000);
        } catch (NoSuchAlgorithmException e) {
            delay = (int) (Math.random() * 5000);
        }

        try {
            Thread.sleep(delay);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }

    public static void randomDelay(boolean error) throws Exception {
        int delay = 0;
        try {
            delay = SecureRandom.getInstanceStrong().nextInt(5000);
        } catch (NoSuchAlgorithmException e) {
            delay = (int) (Math.random() * 5000);
        }

        try {
            Thread.sleep(delay);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
        if (error) {
            double hasError = Math.random() * 4;
            if (hasError <= 1) {
                throw new Exception();
            }
        }
    }
}
