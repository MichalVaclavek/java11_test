package cz.vaclavek.java11;

import cz.michalv.openings.DayOfWeek;
import cz.michalv.openings.OpeningTimeInterval;
import cz.michalv.openings.OpeningWeekDays;
import cz.vaclavek.java11.pizza.Calzone;
import cz.vaclavek.java11.pizza.NyPizza;
import cz.vaclavek.java11.pizza.Pizza;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.DoubleBinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import lombok.NonNull;

import static java.math.BigInteger.TWO;


public class Main {

    public static void main(String[] args) {
        System.out.println("Java 11".isBlank());

        var koza = "Koza";

        System.out.println(koza);

        String[] num = { "1201", "12018", "1201800","12018000" };
        String prefix = "120180000175135";

        for (int i=num.length-1; i >= 0; i--) {
            if (prefix.startsWith(num[i])) {
                System.out.println("Longest match is: " + num[i]);
                break;
            }
        }

        //String accountAlias = call.getAccountAlias();
        Optional<String> match = Arrays.asList(num).stream()
                .sorted(Comparator.comparingInt(String::length).reversed())
                .filter(prefix::startsWith)
                .findFirst();
        System.out.println("Longest match is: " + match.orElse("room_unknown"));

        Object[] oa = new Object[100];
        Collection<Object> co = new ArrayList<Object>();

        // T inferred to be Object
        fromArrayToCollection(oa, co);

        String[] sa = new String[100];
        Collection<String> cs = new ArrayList<String>();

        // T inferred to be String
        fromArrayToCollection(sa, cs);

        // T inferred to be Object
        fromArrayToCollection(sa, co);

        Integer[] ia = new Integer[100];
        Float[] fa = new Float[100];
//        Number[] na = new Number[100];
        Number[] na = {5, 6, 10, 45.4, 100};

        Collection<Number> cn = new ArrayList<Number>();
        //Collection<Number> cn = List.of(5, 6, 10, 45.4, 100); // immutable collection

        // T inferred to be Number
        fromArrayToCollection(ia, cn);

        // T inferred to be Number
        fromArrayToCollection(fa, cn);

        // T inferred to be Number
        fromArrayToCollection(na, cn);

        // T inferred to be Object
        fromArrayToCollection(na, co);

        Sink<Number> s = System.out::println;

        Collection<String> strings;
        Number str = writeAll(cn, s);


        System.out.println(Operation.PLUS.apply(4,5));
        System.out.println(Operation.MINUS.apply(4,5));
        System.out.println(Operation.TIMES.apply(4,5));
        System.out.println(Operation.DIVIDE.apply(4,5));

//        primes().map(p -> TWO.pow(p.intValueExact()).subtract(ONE))
//                .filter(mersenne -> mersenne.isProbablePrime(50))
//                .limit(20)
//                .forEach(System.out::println);
//
//        randomMethod();


        ObservableSet<Integer> set = new ObservableSet<>(new HashSet<>());
        //set.addObserver((ss, e) -> System.out.println(e));

        set.addObserver(new SetObserver<>() {
            public void added(ObservableSet<Integer> s, Integer e) {
                System.out.println(e);
                if (e == 23) {
                    ExecutorService exec = Executors.newSingleThreadExecutor();
                    try {
                        Boolean result = exec.submit(() -> s.removeObserver(this)).get();
                    } catch (ExecutionException | InterruptedException ex) {
                        throw new AssertionError(ex);
                    } finally {
                        exec.shutdown();
                    }
                }
            }
        });

        for (int i = 0; i < 100; i++) {
            set.add(i);
        }

        Runnable task = Main::randomMethod;

        try {
            System.out.println("Exec time: " + time(Executors.newFixedThreadPool(10),  5, task)/1_000_000_000f + "s");
        } catch (InterruptedException ex) {
            throw new AssertionError(ex);
        }

        NyPizza myPizza1 = new NyPizza.Builder(NyPizza.Size.LARGE).addTopping(Pizza.Topping.HAM).build();
        NyPizza myPizza2 = new NyPizza.Builder(NyPizza.Size.MEDIUM).addTopping(Pizza.Topping.SAUSAGE).addTopping(Pizza.Topping.ONION).build();
        Calzone calzone1 = new Calzone.Builder().sauceInside().addTopping(Pizza.Topping.MUSHROOM).build();

        Set<Pizza> pizzas = Collections.unmodifiableSet(Set.of(myPizza1, myPizza2, calzone1));

        pizzas.forEach(System.out::println);


        OpeningWeekDays otviracka = new OpeningWeekDays.Builder()
                                                       .addOpeningDays(DayOfWeek.Po, DayOfWeek.Ut, DayOfWeek.Str, DayOfWeek.Ct)
                                                                .addOpeningTimeInterval(OpeningTimeInterval.of("07:00", "11:30"))
                                                                .addOpeningTimeInterval(OpeningTimeInterval.of("12:30", "18:30"))
                                                       .and()
                                                       .addOpeningDays(DayOfWeek.Pa)
                                                                .addOpeningTimeInterval(OpeningTimeInterval.of("07:00", "12:30"))
                                                                .addOpeningTimeInterval(OpeningTimeInterval.of("13:00", "17:30"))
                                                       .and()
                                                       .addOpeningDays(DayOfWeek.So)
                                                                .addOpeningTimeInterval(OpeningTimeInterval.of("08:00", "12:00"))
                                                                .addOpeningTimeInterval(OpeningTimeInterval.of("12:00", "17:30"))
                                                      .and()
                                                      .addOpeningDays(DayOfWeek.Ne).setClosed()
                                                      .and()
                                                      .build();

        System.out.println(otviracka);

        List<String> sampleList = Arrays.asList("Java", "Kotlin");
        String[] sampleArray = sampleList.toArray(String[]::new);
        printArray(sampleArray);
        //assertThat(sampleArray).containsExactly("Java", "Kotlin");

        List<Discounter> discounters = List.of(Discounter.christmas(), Discounter.easter(), Discounter.newYear());

        Discounter combinedDiscounter = discounters
                .stream()
                .reduce(v -> v, Discounter::combine);

        discounters.forEach(d-> System.out.println(d.apply(new BigDecimal(20))));

        System.out.println(combinedDiscounter.apply(new BigDecimal(20)));

        Map<Long, Pizza> chessTournamentDtoMap = new LinkedHashMap<>();

        ExecutorService executor = Executors.newFixedThreadPool(10);

        try {
            System.out.println("Secs: " + (double) time(executor, 10, Main::randomMethod) / 1_000_000_000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ForkJoinPool forkJoinPool = new ForkJoinPool();

        long startNanos = System.nanoTime();
//
        FactorialSquareCalculator calculator = new FactorialSquareCalculator(999);

        forkJoinPool.execute(calculator);

        try {
            System.out.println(calculator.get());
            System.out.println((double) (System.nanoTime() - startNanos)/ 1_000_000_000);
        } catch (InterruptedException | ExecutionException ex) {
            Thread.currentThread().interrupt();
            System.err.println(ex);
        }

        startNanos = System.nanoTime();
        System.out.println(IntStream.rangeClosed(1, 999).map(x -> x*x).reduce(0, Integer::sum));
        System.out.println((double) (System.nanoTime() - startNanos)/ 1_000_000_000);

        startNanos = System.nanoTime();
        System.out.println(IntStream.rangeClosed(1, 999).parallel().map(x -> x*x).reduce(0, Integer::sum));
        System.out.println((double) (System.nanoTime() - startNanos)/ 1_000_000_000);

        UnaryOperator<String> addA = (startString) -> startString + "a";
        UnaryOperator<String> addB = (startString) -> startString + "b";
        UnaryOperator<String> addc = (startString) -> startString + "c";
        List<UnaryOperator<String>> operators = List.of(addA, addB, addc);
        String concatenatedString =
                operators
                        .stream()
                        .reduce(
                                "", // identity
                                (value, op) -> op.apply(value), // accumulator
                                String::concat // combiner
                        );
        System.out.println(concatenatedString); // prints "abc" as expected.

        UnaryOperator<String> combine = operators.stream().reduce((l, r) -> (string) -> l.andThen(r).apply(string))
                .orElseGet(UnaryOperator::identity);

        System.out.println(combine.apply(""));

        UnaryOperator<String> combineF = operators.stream()
                                    .reduce(UnaryOperator.identity(), (l, r) -> (string) -> r.compose(l).apply(string));

        System.out.println(combineF.apply(""));



        // JAVA 11 Features

        // 1. String methods - isBlank, lines, strip, stripLeading, stripTrailing, and repeat.
        String multilineString = "Baeldung helps \n \n developers \n explore Java.";
        List<String> lines = multilineString.lines()
                .filter(line -> !line.isBlank())
                .map(String::strip)
                .collect(Collectors.toList());

        Assertions.assertLinesMatch(List.of("Baeldung helps", "developers", "explore Java."), lines );

        // 2. New File Methods
        // Additionally, it's now easier to read and write Strings from files.
        // We can use the new readString and writeString static methods from the Files class:
        String tempDirPath = "/home/michalv/tempdir"; // directory must be created in the system before
        Path tempDir = Paths.get(tempDirPath);
        String fileContent = "";
        Path filePath;

        try {
            Path tempFile = Files.createTempFile(tempDir, "demo", ".txt");
            filePath = Files.writeString(tempFile, "Sample text");
            fileContent = Files.readString(filePath);
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        Assertions.assertEquals("Sample text", fileContent);


        // 3. Collection to an Array

        // The java.util.Collection interface contains a new default toArray method which takes an IntFunction argument.
        // This makes it easier to create an array of the right type from a collection:

        String[] testArray = new String[] {"Java", "Kotlin"};

        List<String> sampleList2 = Arrays.asList("Java", "Kotlin");
        String[] sampleArray2 = sampleList2.toArray(String[]::new);
        Assertions.assertArrayEquals(testArray, sampleArray2);

        // 4. The Not Predicate Method

        // A static not() method has been added to the Predicate interface. We can use it to negate an existing predicate, much like the negate method:

        List<String> sampleList3 = Arrays.asList("Java", "\n \n", "Kotlin", " ");
        List<String> withoutBlanks = sampleList3.stream()
          .filter(Predicate.not(String::isBlank))
          .collect(Collectors.toList());
        Assertions.assertArrayEquals(testArray, withoutBlanks.toArray());

        // While not(isBlank) reads more naturally than isBlank.negate(), the big advantage is that we can also use not with method references, like not(String:isBlank).

        // 5. Local-Variable Syntax for Lambda

        // Support for using the local variable syntax (var keyword) in lambda parameters was added in Java 11.
        // We can make use of this feature to apply modifiers to our local variables, like defining a type annotation:

        List<String> sampleList4 = Arrays.asList("Java", "Kotlin");
        String resultString = sampleList4.stream()
                                        .map((@NonNull var x) -> x.toUpperCase())
                                        .collect(Collectors.joining(", "));
        Assertions.assertEquals(resultString, "JAVA, KOTLIN");

        // 6. HTTP Client

        // The new HTTP client from the java.net.http package was introduced in Java 9. It has now become
        // a standard feature in Java 11.
        // The new HTTP API improves overall performance and provides support for both HTTP/1.1 and HTTP/2:

        int port = 8080;

        HttpClient httpClient = HttpClient.newBuilder()
                                          .version(HttpClient.Version.HTTP_2)
                                          .connectTimeout(Duration.ofSeconds(20))
                                          .build();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                                              .GET()
                                              .uri(URI.create("http://localhost:" + port))
                                              .build();
        HttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException ex) {

        }
        //Assertions.assertEquals("Hello from the server!", httpResponse.body());

        System.exit(0);
    } // main




    static Stream<BigInteger> primes() {
        return Stream.iterate(TWO, BigInteger::nextProbablePrime);
    }

    public static void randomMethod() {
        int n = 2 * (Integer.MAX_VALUE / 3);
        int low = 0;
        for (int i = 0; i < 500000; i++)
            if (random(n) < n/2)
                low++;
        System.out.println(low);
    }

    // Common but deeply flawed!
    static Random rnd = new Random();

    static int random(int n) {
        return Math.abs(rnd.nextInt()) % n;
    }


    static <T> void fromArrayToCollection(T[] a, Collection<T> c) {
        for (T o : a) {
            c.add(o); // Correct
        }
    }

    public static <T> T writeAll(Collection<? extends T> coll, Sink<? super T> snk) {
        T last = null;
        for (T t : coll) {
            last = t;
            if (last != null) {
                snk.flush(last);
            }
            
        }
        return last;
    }

    public enum Operation {
        PLUS ("+", Double::sum),
        MINUS ("-", (x, y) -> x - y),
        TIMES ("*", (x, y) -> x * y),
        DIVIDE("/", (x, y) -> x / y);

        private final String symbol;
        private final DoubleBinaryOperator op;

        Operation(String symbol, DoubleBinaryOperator op) {
            this.symbol = symbol;
            this.op = op;
        }
        @Override public String toString() { return symbol; }

        public double apply(double x, double y) {
            return op.applyAsDouble(x, y);
        }
    }

    static class ThreadPerTaskExecutor implements Executor {
        public void execute(Runnable r) {
            new Thread(r).start();
        }
    }

    // Simple framework for timing concurrent execution
    public static long time(Executor executor, int concurrency, Runnable action) throws InterruptedException {
        CountDownLatch ready = new CountDownLatch(concurrency);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(concurrency);

        for (int i = 0; i < concurrency; i++) {
            int finalI = i;
            executor.execute(() -> {
                ready.countDown(); // Tell timer we're ready

                try {
                    start.await(); // Wait till peers are ready
                    System.out.println("Thread n. " + finalI + " started." );
                    action.run();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    done.countDown(); // Tell timer we're done
                    System.out.println("Done count: " + done.getCount());

                }
            });
        }
        ready.await();
        System.out.println("All threads ready");
        // Wait for all workers to be ready
        long startNanos = System.nanoTime();
        start.countDown(); // And they're off!
        System.out.println("All threads started");
        done.await();
        // Wait for all workers to finish
        System.out.println("Execution finished");
        return System.nanoTime() - startNanos;
    }

    private static <T> void printArray(T[] array) {
        Collection<Object> co = new ArrayList<Object>();
        fromArrayToCollection(array, co);
        co.forEach(e -> System.out.println(e.toString()));
    }

}
