package com.geezylucas;

import java.util.concurrent.ExecutionException;

/*
         long startTime = System.currentTimeMillis();
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            IntStream.range(0, 30000).forEach(i -> executor.submit(() -> {
                Thread.sleep(Duration.ofSeconds(1));
                System.out.println(i);
                return i;
            }));
        }
        long endTime = System.currentTimeMillis();
        long total = endTime - startTime;
        System.out.println("Total time (virtual thread): " + total);

                long startTime = System.currentTimeMillis();
        try (var executor = Executors.newThreadPerTaskExecutor(Executors.defaultThreadFactory())) {
            IntStream.range(0, 30000).forEach(i -> executor.submit(() -> {
                Thread.sleep(Duration.ofSeconds(1));
                System.out.println(i);
                return i;
            }));
        }

        long endTime = System.currentTimeMillis();
        long total = endTime - startTime;
        System.out.println("Total time: (thread per task)" + total);

 */

public class Main {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Thread.startVirtualThread(() -> {
            System.out.println("Hello, Project Loom!");
        });

        AggregationWithoutSC.execute();
        //AggregationWithSC.execute();
        
        Thread.sleep(1000);
    }
}