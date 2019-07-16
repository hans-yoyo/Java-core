package com.hyman.advance.jdk8;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Description:
 * @author: Hyman
 * @date: 2019/07/16 20:44
 * @version： 1.0.0
 */
public class StreamTest {

    public static void main(String[] args) {
        // Stream的生成方式
        // 1.使用Collections和Arrays
        String[] strArr = new String[]{"a", "b", "c"};
        Stream<String> stream = Arrays.stream(strArr);
        List<String> list = Arrays.asList(strArr);
        Stream<String> stream1 = list.stream();

        // 2.使用Stream中提供的静态方法
        Stream<String> stream2 = Stream.of(strArr);
        Stream<Double> stream3 = Stream.generate(Math::random);
        Stream<Object> stream4 = Stream.empty();
        Stream.iterate(1, i -> i++);


        List<String> streamList = new ArrayList<>();
        streamList.add("ddd2");
        streamList.add("aaa2");
        streamList.add("bbb1");
        streamList.add("aaa1");
        streamList.add("bbb3");
        streamList.add("ccc");
        streamList.add("bbb2");
        streamList.add("ddd1");

        // filter
//        streamList.stream().filter(s -> s.startsWith("a")).forEach(System.out::println);
//        System.out.println("======================");
//        streamList.forEach(System.out::println);

//        streamList
//                .stream()
//                .sorted()
//                .forEach(System.out::println);
//        System.out.println("======================");
//        streamList.forEach(System.out::println);

        streamList.stream()
                .map(String::toUpperCase)
                .map(a -> a + "_ZZZ")
                .sorted((a, b) -> b.compareTo(a))
                .forEach(System.out::println);

        Stream<Integer> stream5 = Stream.of(2, 3, 1, 5, 3, 2);
//        stream5.sorted(Integer::compareTo).forEach(System.out::println);
//        stream5.skip(2).forEach(System.out::println);
//        stream5.distinct().forEach(System.out::println);
//        stream5.limit(3).forEach(System.out::println);
//        stream5.forEach(System.out::println);
//        Integer maxNum = stream5.max(Integer::compareTo).get();
//        System.out.println(maxNum);


        Stream.concat(Stream.of(1,4,5), Stream.of(2,3,6,4))
                .forEach(System.out::print);

        Stream.of("one", "two", "three", "four")
                .filter(e -> e.length() > 3)
                .peek(e -> System.out.println("Filtered value: " + e))
                .map(String::toUpperCase)
                .peek(e -> System.out.println("Mapped value: " + e))
                .collect(Collectors.toList());

        Optional

    }

}
