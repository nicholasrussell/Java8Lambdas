import java.math.BigInteger;
import java.util.function.Function;

// Lots of thanks for http://mail.openjdk.java.net/pipermail/lambda-dev/2012-October/006309.html
public class YCombinator {
    interface SelfApplicable<T> {
        T apply(SelfApplicable<T> a);
    }

    public static final Function<Function<Function<Object, Object>, Function<Object, Object>>, Function<Object, Object>> Y =
        ((SelfApplicable<Function<Function<Function<Object, Object>, Function<Object, Object>>, Function<Object, Object>>>)
            ((SelfApplicable<Function<Function<Function<Object, Object>, Function<Object, Object>>, Function<Object, Object>>> y) ->
                (Function<Function<Object, Object>, Function<Object, Object>> f) ->
                    (Object x) ->
                        f.apply(y.apply(y).apply(f)).apply(x))).apply(
            (SelfApplicable<Function<Function<Function<Object, Object>, Function<Object, Object>>, Function<Object, Object>>> y) ->
                (Function<Function<Object, Object>, Function<Object, Object>> f) ->
                    (Object x) ->
                        f.apply(y.apply(y).apply(f)).apply(x));

    public static BigInteger toBigInt(Object o) {
        if (o instanceof BigInteger) {
            return (BigInteger) o;
        } else if (o instanceof Long) {
            return BigInteger.valueOf((long) o);
        } else {
            return new BigInteger(o.toString());
        }
    }

    public static void main(String[] args) {
        // Anonymous
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

        System.out.println("==========");
        System.out.println(factorial5);
        System.out.println("==========");


        // Named
        Function<Function<Object, Object>, Function<Object, Object>> factorial = ((Function<Object, Object> f) ->
            (Object x) -> {
                if ((Integer) x == 0) {
                    return 1;
                } else {
                    return ((Integer) x) * ((Integer) f.apply((Integer) x - 1));
                }
            });

        System.out.println(Y.apply(factorial).apply(5));
        System.out.println("==========");

        for (int i = 0; i <= 10; i++) {
            System.out.println(Y.apply(factorial).apply(i));
        }
        System.out.println("==========");

        Function<Function<Object, Object>, Function<Object, Object>> bigFactorial = ((Function<Object, Object> f) ->
                (Object x) -> {
                    if (toBigInt(x) == BigInteger.ZERO) {
                        return BigInteger.ONE;
                    } else {
                        return toBigInt(x).multiply(toBigInt(f.apply(toBigInt(x).subtract(BigInteger.ONE))));
                    }
                });

        System.out.println(Y.apply(bigFactorial).apply(BigInteger.valueOf((long) 1000)));
        System.out.println("==========");

        Function<Function<Object, Object>, Function<Object, Object>> fibonacci = ((Function<Object, Object> f) ->
            (Object x) -> {
                if ((Integer) x == 0) {
                    return 0;
                } else if ((Integer) x == 1) {
                    return 1;
                } else {
                    return ((Integer) f.apply((Integer) x - 1)) + ((Integer) f.apply((Integer) x - 2));
                }
            });

        for (int i = 0; i <= 30; i++) {
            System.out.println(Y.apply(fibonacci).apply(i));
        }
        System.out.println("==========");
    }
}
