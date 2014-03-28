import java.util.function.Function;

// Lots of thanks for http://mail.openjdk.java.net/pipermail/lambda-dev/2012-October/006309.html
public class YCombinator {
    interface SelfApplicable<T> {
        T apply(SelfApplicable<T> a);
    }

    public static void main(String[] args) {
        Object factorial5 =
            ((SelfApplicable<Function<Function<Function<Object, Object>, Function<Object, Object>>, Function<Object, Object>>>)
                ((SelfApplicable<Function<Function<Function<Object, Object>, Function<Object, Object>>, Function<Object, Object>>> y) ->
                    (Function<Function<Object, Object>, Function<Object, Object>> f) ->
                        (Object x) ->
                                f.apply(y.apply(y).apply(f)).apply(x))).apply(
            (SelfApplicable<Function<Function<Function<Object, Object>, Function<Object, Object>>, Function<Object, Object>>> y) ->
                (Function<Function<Object, Object>, Function<Object, Object>> f) ->
                    (Object x) ->
                        f.apply(y.apply(y).apply(f)).apply(x)).apply(
            ((Function<Object, Object> f) ->
                (Object x) -> {
                    if ((Integer) x <= 0) {
                        return 1;
                    } else {
                        return ((Integer) x) * ((Integer) f.apply((Integer) x - 1));
                    }
                })).apply(5);

        System.out.println(factorial5);
    }
}
