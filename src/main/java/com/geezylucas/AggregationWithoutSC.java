package com.geezylucas;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class AggregationWithoutSC {

    // Caso 1: La tarea 1 falla, la segunda operacion es inalcanzable y no se puede
    // cancelar
    // se termina ejecutando la tarea 2 cuando pudieramos cancelarla.
    // lleva codigo adicional del programador manejar rollbacks, etc
    // Si la tarea 2 es costosa seguira ejecutandose y no la necesitamos
    public static void sellProduct() throws ExecutionException, InterruptedException {

        try (var ex = new ScheduledThreadPoolExecutor(8)) {
            Future<Integer> inv = ex.submit(() -> updateInventory()); // falla
            Future<Integer> ord = ex.submit(() -> updateOrder()); // continua

            Integer invResult = inv.get(); // falla
            Integer ordResult = ord.get(); // inalcanzable

            System.out.println("Result is inv = " + invResult + " ord = " + ordResult);
        }
    }

    // Caso 2: La tarea 1 es costosa y la tarea 2 falla,
    // nos enteramos del fallo luego de que innecesariamente esperamos por 1
    public static void sellProduct2() throws InterruptedException, ExecutionException {

        try (var ex = new ScheduledThreadPoolExecutor(8)) {
            Future<Integer> inv = ex.submit(() -> updateInventory()); // tarea costosa
            Future<Integer> ord = ex.submit(() -> updateOrder()); // falla

            Integer invResult = inv.get(); // bloqueada
            Integer ordResult = ord.get(); // falla cuando termina anterior

            System.out.println("Result is inv = " + invResult + " ord = " + ordResult);
        }
    }

    public static Integer updateInventory() {
        // return 1;
        throw new RuntimeException();
    }

    public static Integer updateOrder() {
        return 2;
    }

    public static void execute() throws ExecutionException, InterruptedException {
        sellProduct();
    }
}