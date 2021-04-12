import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Zadanie {
    public static void main(String[] args) {
        List<Integer> list = List.of(3, 4, 1, 70);
        List<Integer> list2 = List.of(3, 4, 1, 5, 2, 7, 6);
        System.out.println(getNext(list)); //71

        System.out.println("\nloop");
        System.out.println(getNext2(list)); //2
        System.out.println(getNext2(list2)); //8

        System.out.println("\nstream");
        System.out.println(getNext2_1(list)); //2
        System.out.println(getNext2_1(list2)); //2

        System.out.println("\nnext 3");
        System.out.println(getNext3(37)); //2
        System.out.println(getNext3(128)); //2
        System.out.println(getNext3(145)); //2
    }

    private static int getNext(List<Integer> list) {
        return list.stream().max(Integer::compareTo).get() + 1;
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

    private static int getNext2_1(List<Integer> list) {
        int min = list.stream().min(Integer::compareTo).get();
        int[] currentValue = {min};
        list.stream().sorted()
                .forEach(x -> {
                    if (x == currentValue[0] + 1) currentValue[0]++;
                });
        return ++currentValue[0];
    }

    private static int getNext3(int a) {
        //a = 37 -> binarne 0000100101000
        // największa liczba zer pomiędzy 1
        //return 2
        String binary = Integer.toBinaryString(a);
        System.out.println(binary);
        String[] zeroes = binary.split("1");
        System.out.println(Arrays.stream(zeroes).collect(Collectors.toList()));
        int result = 0;
        for (String z : zeroes) {
            if (z.length() > result) result = z.length();
        }
        return result;
    }
}
