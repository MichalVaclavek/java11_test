package com.example.reactive_demo;

import com.example.reactive_demo.repository.Customer;
import com.example.reactive_demo.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@SpringBootApplication
@EnableScheduling
public class ReactiveDemoApplication {

    private static final Logger log = LoggerFactory.getLogger(ReactiveDemoApplication.class);

    public static void main(String[] args) {

        List<Customer> customers = new ArrayList<>();
        customers.add(new Customer(1, "Lojza"));
        customers.add(new Customer(2, "Pepa_1"));
        customers.add(new Customer(3, "Stana"));
        customers.add(new Customer(4, "Pepa_2"));
        customers.add(new Customer(5, "Ferda"));

        Set<String> names = customers.stream().map(c -> c.getName().length() > 2 ? c.getName().substring(0, c.getName().length() -2) : c.getName())
                .collect(Collectors.toSet());

        List<Customer> customers2 = customers.stream().filter(distinctByKey(c -> c.getName().substring(0, c.getName().length() -2)))
                .collect(Collectors.toList());

        names.stream().forEach(System.out::println);

        customers2.stream().map(Customer::getName).forEach(System.out::println);

        SpringApplication.run(ReactiveDemoApplication.class, args);
    }

    @Autowired
    private CustomerRepository repository;

    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }


    @Bean
    public CommandLineRunner demo() {

        return args -> {
            // fetch customers by last name
            printDB();
        };
    }

    @Scheduled(fixedDelay = 2000)
    private void printDB() {
        repository.findAll().doOnNext(bauer -> {
            log.info(bauer.toString());
        }).blockLast(Duration.ofSeconds(1));;
        log.info("");
    }
}
