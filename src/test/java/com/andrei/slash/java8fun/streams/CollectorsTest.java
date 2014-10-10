package com.andrei.slash.java8fun.streams;

import com.andrei.slash.java8fun.model.Trader;
import com.andrei.slash.java8fun.model.Transaction;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;

import static java.util.stream.Collectors.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author andrei
 */
public class CollectorsTest {

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
    public void multi_level_group_by_trader_city_and_transaction_value() {
        Map<String, Map<Integer, List<Transaction>>> transactionsByCitiesAndValues = transactions.stream()
                .collect(groupingBy(t -> t.getTrader().getCity(), groupingBy(Transaction::getValue)));

        assertThat("Transactions are happening in 3 cities", transactionsByCitiesAndValues.size(), is(3));
        assertThat(transactionsByCitiesAndValues.get("London").get(3000).get(0).getTrader().getName(), is("Andrei"));
        assertThat(transactionsByCitiesAndValues.get("London").get(800).get(0).getTrader().getName(), is("Jimmy"));
    }

    @Test
    public void group_max_transactions_per_year() {
        Map<Integer, Transaction> maxTransationPerYear = transactions.stream()
                .collect(groupingBy(Transaction::getYear,
                        collectingAndThen(maxBy(Comparator.comparingInt(Transaction::getValue)),
                                Optional::get)));

        assertThat("Max transaction in 2011 was 3000", maxTransationPerYear.get(2011).getValue(), is(3000));
        assertThat("Max transaction in 2013 was 900", maxTransationPerYear.get(2013).getValue(), is(900));
        assertThat("Max transaction in 2014 was 1000", maxTransationPerYear.get(2014).getValue(), is(1000));
    }

    @Test
    public void partition_transactions_by_greater_than_1000() {
        Map<Boolean, List<Transaction>> partitionedTransactionsByValue = transactions.stream().collect(partitioningBy(t -> t.getValue() > 1000));

        assertThat(partitionedTransactionsByValue.get(true).size(), is(2));
        assertThat(partitionedTransactionsByValue.get(false).size(), is(4));
    }
}
