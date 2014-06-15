package com.foo;

import com.google.common.cache.CacheBuilder;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * @author ding.lid
 */
public class JavaGcPlayGroundTest {
    private static final Logger logger = LoggerFactory.getLogger(JavaGcPlayGroundTest.class);

    @Test
    public void test_weakRefAndGc() throws Exception {
        Thread thread = new Thread(new NewTask());
        thread.start();

        final ConcurrentMap<Object, byte[]> holder = CacheBuilder.newBuilder()
                .weakValues()
                .<Object, byte[]>build()
                .asMap();
        for (int i = 0; ; ++i) {
            if (i % 1000000 == 0)
                logger.info("input count: {}m, total new byte array size: {}g", i / 1000000, i / 1000000);
            holder.put(i, new byte[1024]);
        }
    }

    static class NewTask implements Runnable {
        @Override
        public void run() {
            Random random = new Random();
            while (true) {
                byte[] bytes = new byte[1024 * 1024];
                logger.error("locate 1m byte array: {}", bytes.toString());
                try {
                    TimeUnit.SECONDS.sleep(random.nextInt(5));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
