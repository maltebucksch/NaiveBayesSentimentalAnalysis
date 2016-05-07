package classification;

import java.util.Random;

public final class ArrayHelper {
    // return true if the input equals (at least) one of the elements in the
    // list
    public static boolean contains(String[] list, String input) {
        for (int i = 0; i < list.length; i++)
            if (input.equals(list[i]))
                return true;
        return false;
    }

    public static void shuffleArray(String[] array) {
        Random rnd = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            String a = array[index];
            array[index] = array[i];
            array[i] = a;
        }
    }
}
