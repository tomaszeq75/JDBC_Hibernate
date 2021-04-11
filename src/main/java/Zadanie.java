import java.util.List;

public class Zadanie {
    public static void main(String[] args) {
        List<Integer> list = List.of(3,4,1,70);
        System.out.println(getNext(list)); //71
        System.out.println(getNext2(list)); //2
    }

    private static int getNext(List<Integer> list) {
        list.sort(Integer::compareTo);
        for (int i = 0; i < list.size() -1; i++) {
            if (list.get(i) + 1 == list.get(i + 1)) {
                return list.get(i) + 1;
            }

        }
        return 0;
    }
    private static int getNext2(List<Integer> list) {
        return 0;
    }

    private int getNext3(int a) {
        //a = 37 -> binarne 0000100101000
        // największa liczba zer pomiędzy 1
        //return 2
        return 2;
    }
}
