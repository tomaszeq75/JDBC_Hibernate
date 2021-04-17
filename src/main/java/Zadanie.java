import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Zadanie {
    public static void main(String[] args) {
        List<Integer> list = List.of(3, 4, 1, 70);
        List<Integer> list2 = List.of(3, 4, 1, 5, 2, 7, 6);
        System.out.println(getNext(list)); //71

        System.out.println("\nloop");
        System.out.println(getNext2(list));
        System.out.println(getNext2(list2));

        System.out.println("\nstream (2.1)");
        System.out.println(getNext2_1(list));
        System.out.println(getNext2_1(list2));

        System.out.println("\nstream (2.2)");
        System.out.println(getNext2_2(list));
        System.out.println(getNext2_2(list2));

        System.out.println("\nnext 3 - loop");
        System.out.println(getNext3(37));
        System.out.println(getNext3(128));
        System.out.println(getNext3(145));

        System.out.println("\nnext 3_1 - regex");
        System.out.println(getNext3_1(37));
        System.out.println(getNext3_1(128));
        System.out.println(getNext3_1(145));
    }

    private static int getNext(List<Integer> list) {
        return Collections.max(list) + 1;
//        return list.stream().max(Integer::compareTo).get() + 1;
    }

    private static int getNext2(List<Integer> list) {
        List<Integer> temp = list.stream().sorted().collect(Collectors.toList()); // list jest immutable
        int i;
        for (i = 0; i < temp.size() - 1; i++) {
            if (temp.get(i) + 1 != temp.get(i + 1)) {
                return temp.get(i) + 1;
            }
        }
        return i + 2;
    }

    // przetwarza całą listę
    private static int getNext2_1(List<Integer> list) {
        int[] currentValue = {Collections.min(list)};
        list.stream().sorted()
                .forEach(x -> {
                    if (x == currentValue[0] + 1) currentValue[0]++;
                });
        return ++currentValue[0];
    }

    // java 9 - nie przetwarza całej listy
    private static int getNext2_2(List<Integer> list) {
        int[] currentValue = {Collections.min(list)};
        list.stream().sorted().skip(1)
                .takeWhile(x -> x == currentValue[0] + 1)
                .forEach(x -> currentValue[0] = x);
        return ++currentValue[0];
    }

    // regex
    private static int getNext3_1(int a) {
        //a = 37 -> binarne 0000100101000
        // największa liczba zer pomiędzy 1

        String binary = Integer.toBinaryString(a);
        System.out.println(binary);
        int result = 0;

        Pattern pattern = Pattern.compile("(?=1(0+)1)");
        Matcher matcher = pattern.matcher(binary);
        while (matcher.find()) {
            if (result < matcher.group(1).length()) {
                result = matcher.group(1).length();
            }
        }

        return result;
    }

    // loop
    private static int getNext3(int a) {
        String binary = Integer.toBinaryString(a);
        System.out.println(binary);
        int result = 0;
        int lastOcurrenceIndex = 0;

        for (int i = 0; i < binary.length(); i++) {
            if (binary.charAt(i) == '1') {
                if (result < i - lastOcurrenceIndex) {
                    result = i - lastOcurrenceIndex - 1;
                }
                lastOcurrenceIndex = i;
            }
        }
        return result;
    }
}
