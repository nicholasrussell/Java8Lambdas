import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class Lambda {
    public static void main(String[] args) {
        List<String> outputs = Arrays.asList("This", "is", "a", "test");
        String output = outputs.stream()
                .map(x -> x.toUpperCase())
                .reduce((x, y) -> x + " " + y)
                .get();
        System.out.println("Array: " +output);

        ((Function<Object, Object>) o -> {
            System.out.println(o);
            return new Object();
        }).apply("Anonymous");

        String testString = "test";
        Runnable r = () -> {
            System.out.println("Runnable: " + testString);
        };
        r.run();

        List<Integer> nums = Arrays.asList(1,2,3,0,2,10,4,5);
        Collections.sort(nums, Integer::compare);
        System.out.print("Sorted: ");
        System.out.println(nums);
        Integer max = nums.stream().reduce((x, y) -> Math.max(x, y)).get();
        System.out.println("Max: " + max);
    }
}
