package com.andrei.slash.java8fun.streams;

import com.andrei.slash.java8fun.model.Trader;
import com.andrei.slash.java8fun.model.Transaction;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.*;

/**
 * @author andrei
 */
public class TransactionsStreamTest {

    private static List<Transaction> transactions;

    @BeforeClass
    public static void setup() {
        Trader andrei = new Trader("Andrei", "London");
        Trader john = new Trader("John", "New York");
        Trader jimmmy = new Trader("Jimmy", "London");
        Trader franz = new Trader("Franz", "Berlin");

        transactions = Arrays.asList(new Transaction(andrei, 2011, 3000),
                new Transaction(john, 2013, 100),
                new Transaction(john, 2011, 2000),
                new Transaction(jimmmy, 2014, 800),
                new Transaction(franz, 2013, 900),
                new Transaction(franz, 2014, 1000));
    }

    @Test
    public void transactions_in_2013_sorted_asc_by_value() {
        List<Transaction> sortedTransactions = transactions.stream()
                .filter((transaction) -> transaction.getYear() == 2013)
                .sorted(comparing((transaction) -> transaction.getValue()))
                .collect(toList());

        assertTrue(sortedTransactions.size() == 2);
        assertTrue(sortedTransactions.get(0).getValue() == 100);
        assertTrue(sortedTransactions.get(1).getValue() == 900);
    }

    @Test
    public void get_unique_traders_cities() {
        List<String> cities = transactions.stream()
                .map((transaction) -> transaction.getTrader().getCity())
                .distinct()
                .collect(toList());

        assertTrue(cities.size() == 3);

        Set<String> citiesSet = transactions.stream()
                .map((transaction) -> transaction.getTrader().getCity())
                .collect(toSet());
        assertTrue(citiesSet.size() == 3);
    }

    @Test
    public void tranders_from_london_sorted_by_name() {
        List<Trader> tradersFromLondon = transactions.stream()
                .map((transaction) -> transaction.getTrader())
                .filter((trader) -> trader.getCity().equals("London"))
                .sorted(comparing((trader) -> trader.getName()))
                .collect(toList());

        assertTrue(tradersFromLondon.size() == 2);
        assertEquals("Andrei", tradersFromLondon.get(0).getName());
        assertEquals("Jimmy", tradersFromLondon.get(1).getName());
    }

    @Test
    public void tranders_name_as_string() {
        String mergedTradersNames = transactions.stream()
                .map((transaction) -> transaction.getTrader().getName())
                .distinct()
                .sorted()
                .reduce("", (t1, t2) -> t1 + " " + t2);

        assertFalse(mergedTradersNames.isEmpty());
        assertEquals("Andrei Franz Jimmy John", mergedTradersNames.trim());


        Optional<String> mergedTraders = transactions.stream()
                .map((transaction) -> transaction.getTrader().getName())
                .distinct()
                .sorted()
                .reduce((t1, t2) -> t1 + " " + t2);

        assertTrue(mergedTraders.isPresent());
        assertEquals("Andrei Franz Jimmy John", mergedTraders.get());
    }

    @Test
    public void are_any_traders_in_new_york() {
        boolean areTradersInNewYork = transactions.stream()
                .map((transaction) -> transaction.getTrader().getCity())
                .anyMatch((city) -> city.equals("New York"));

        assertTrue(areTradersInNewYork);
    }

    @Test
    public void get_transaction_values_from_london() {
        List<Integer> transactionsInLondon = transactions.stream()
                .filter((transaction) -> transaction.getTrader().getCity().equals("London"))
                .map((transaction) -> transaction.getValue())
                .collect(toList());

        assertTrue(transactionsInLondon.size() == 2);
        assertTrue(transactionsInLondon.get(0) == 3000);
        assertTrue(transactionsInLondon.get(1) == 800);

        transactions.stream()
                .filter((transaction) -> transaction.getTrader().getCity().equals("London"))
//                .forEach((transaction) -> System.out.println(transaction.getValue()));
                .map((transaction) -> transaction.getValue())
                .forEach(System.out::println);
    }

    @Test
    public void highest_transaction_value() {
        Optional<Integer> maxTransactionValue = transactions.stream()
                .map((transaction) -> transaction.getValue())
                .reduce((v1, v2) -> v1 > v2 ? v1 : v2);

        assertTrue(maxTransactionValue.get() == 3000);

        maxTransactionValue = transactions.stream()
                .map((transaction) -> transaction.getValue())
                .reduce(Integer::max);

        assertTrue(maxTransactionValue.get() == 3000);
    }

    @Test
    public void min_transaction_value() {
        Optional<Transaction> minTransaction = transactions.stream()
                .min(comparing(Transaction::getValue));
        assertTrue(minTransaction.get().getValue() == 100);

        minTransaction = transactions.stream()
                .reduce((t1, t2) -> t1.getValue() < t2.getValue() ? t1 : t2);
        assertTrue(minTransaction.get().getValue() == 100);
    }

    @Test
    public void max_transaction_value_without_boxing() {
        OptionalInt maxTransactionValue = transactions.stream()
                .mapToInt(Transaction::getValue)
                .max();

        assertTrue(maxTransactionValue.getAsInt() == 3000);
    }

}
