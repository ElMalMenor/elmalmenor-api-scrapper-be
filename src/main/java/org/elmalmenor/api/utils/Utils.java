package org.elmalmenor.api.utils;

import org.htmlunit.WebClient;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Utils {

    public static List<String> toListWithDivider(String string) {

        List<String> words;

        switch (string) {
            case String s when (s.contains("y")) -> words = Arrays.asList(string.split("y"));
            case String s when (s.contains("-")) -> words = Arrays.asList(string.split("-"));
            case String s when (s.contains("/")) ->  {
                words = new LinkedList<>(Arrays.asList(string.split("/")));

                if (words.getLast().equals("a"))
                    words.removeLast();
                else if (words.getLast().equals("o"))
                    words.removeLast();
            }
            default -> words = List.of(string);
        }

        return words.stream().map(Utils::rawTitleCase).collect(Collectors.toList());
    }

    public static String toTitleCase(String string) {
        if (Objects.isNull(string) || string.trim().isEmpty()) {
            return "No Definido";
        }

        List<String> list = List.of(string.split(" "));

        return IntStream.range(0 , list.size())
                .mapToObj(i -> {
                    String word = stripAccents(list.get(i).toLowerCase());

                    if (i != 0 && checkBannedWords(word)) {
                        return word;
                    }

                    return word.substring(0, 1).toUpperCase() + word.substring(1);
                })
                .collect(Collectors.joining(" "));

    }

    private static boolean checkBannedWords(String string) {
        return List.of("por", "y", "de", "del", "la", "las", "los", "lo", "en", "e").contains(string);
    }

    private static String rawTitleCase(String string) {
        String word = stripAccents(string);
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }

    private static String stripAccents(String s) {
        s = s.replace('ñ', '\001');
        s = s.replace('Ñ', '\002');

        s = Normalizer.normalize(s.trim(), Normalizer.Form.NFD);
        s = s.replaceAll("\\p{InCombiningDiacriticalMarks}", "");

        s = s.replace('\001', 'ñ');
        s = s.replace('\002', 'Ñ');

        return s;
    }

    public static WebClient webClient() {
        WebClient client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);
        client.getOptions().setTimeout(180000);

        return client;
    }

}
