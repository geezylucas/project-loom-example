package com.geezylucas;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;

public class AggregationWithSC {

    // =============== StructuredTaskScope.ShutdownOnFailure
    // ===========================

    // StructuredTaskScope.ShutdownOnFailure
    // Caso 1: Si falla una tarea se propaga la cancelacion a la otra.
    // Se evitan los thread leaks y se usa la propagacion de la cancelacion
    public static void sellProduct() throws ExecutionException, InterruptedException {

        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            var inv = scope.fork(() -> updateInventory()); // falla
            var ord = scope.fork(() -> updateOrder()); // se cancela

            scope.join(); // unir los forks
            scope.throwIfFailed(); // si falla alguno propagar el error

            System.out.println("Result is inv = " + inv.get() + " ord = " + ord.get());
        }
    }

    public static Integer updateInventory() throws InterruptedException {
        //return 1;
        throw new RuntimeException();
    }

    public static Integer updateOrder() {
        return 2;
    }

    // =============== StructuredTaskScope.ShutdownOnSuccess
    // ===========================

    // StructuredTaskScope.ShutdownOnSuccess
    // Cerramos el scope con al menos una respuesta valida.
    // Evitamos seguir llamando otras operaciones paralelas
    // el propio scope realiza la cancelacion de los futuros en curso
    // si una tarea falla no es tomada en cuenta
    // si todas las tareas fallan se toma como causa raiz la excepcion de la primera
    public static Integer sell() throws ExecutionException, InterruptedException {

        try (var scope = new StructuredTaskScope.ShutdownOnSuccess<Integer>()) {
            var t1 = scope.fork(() -> tienda1());
            var t2 = scope.fork(() -> tienda2());
            var t3 = scope.fork(() -> tienda3());

            scope.join();

            System.out.println("Result state t1=" + t1.state() + "t2=" + t2.state() + "t3=" + t3.state());

            return scope.result();
        }
    }

    public static Integer tienda1() throws InterruptedException {
        Thread.sleep(1500);
        throw new RuntimeException();
        // return 1;
    }

    public static Integer tienda2() throws InterruptedException {
        Thread.sleep(2000);
        // throw new RuntimeException();
        return 2;
    }

    public static Integer tienda3() throws InterruptedException {
        Thread.sleep(40000);
        return 3;
    }

    public static void execute() throws ExecutionException, InterruptedException {
        sellProduct();
        //Integer r = sell();
        //System.out.println("StructuredTaskScope.ShutdownOnSuccess result is " + r);
    }
}