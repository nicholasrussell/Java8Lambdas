import java.util.function.Function;

public class Pair {
    public Function<Object, Function<Object, Object>> TRUE = x -> y -> x;
    public Function<Object, Function<Object, Object>> FALSE = x -> y -> y;
    public Function<Object, Function<Object, Function<Object, Object>>> NIL = x -> TRUE;
    public Function<Object, Function<Object, Function<Function<Object, Function<Object, Object>>, Object>>> CONS = x -> y -> f -> f.apply(x).apply(y);
    public Function<Function<Function<Object, Function<Object, Object>>, Object>, Object> FIRST = p -> p.apply(TRUE);
    public Function<Function<Function<Object, Function<Object, Object>>, Object>, Object> SECOND = p -> p.apply(FALSE);

    public static void main(String[] args) {
        Pair c = new Pair();
        System.out.println(c.TRUE.apply(true).apply(false));
        System.out.println(c.FALSE.apply(true).apply(false));
        System.out.println(c.NIL);
        System.out.println(c.CONS.apply(1).apply(2));
        System.out.println(c.CONS.apply(1).apply(2).apply(c.TRUE));
        System.out.println(c.CONS.apply(1).apply(2).apply(c.FALSE));
        System.out.println(c.FIRST.apply(c.CONS.apply(1).apply(2)));
        System.out.println(c.SECOND.apply(c.CONS.apply(1).apply(2)));
        System.out.println(c.CONS.apply(1).apply(c.CONS.apply(2).apply(3)));
        System.out.println(c.FIRST.apply(c.CONS.apply(1).apply(c.CONS.apply(2).apply(3))));
        System.out.println(c.SECOND.apply(c.CONS.apply(1).apply(c.CONS.apply(2).apply(3))));
        System.out.println(c.FIRST.apply(c.CONS.apply(1).apply(c.NIL)));
        System.out.println(c.SECOND.apply(c.CONS.apply(1).apply(c.NIL)));
        System.out.println(c.FIRST.apply(c.CONS.apply(1).apply(c.CONS.apply(2).apply(c.NIL))));
        System.out.println(c.SECOND.apply(c.CONS.apply(1).apply(c.CONS.apply(2).apply(c.NIL))));
    }
}
