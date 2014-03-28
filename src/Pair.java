import java.util.function.Function;

public class Pair {

    public Function<Object, Function<Object, Object>> TRUE = x -> y -> x;
    public Function<Object, Function<Object, Object>> FALSE = x -> y -> y;
    public Function<Object, Function<Object, Function<Object, Object>>> NIL = x -> TRUE;
    // public Function<Object, Function<Object, Function<Object, Function<Object, Object>>>> CONS = x -> y -> f -> f.apply(x).apply(y);

    public static void main(String[] args) {
        Pair c = new Pair();
        System.out.println(c.TRUE.apply(true).apply(false));
        System.out.println(c.FALSE.apply(true).apply(false));
    }
}
