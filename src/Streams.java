import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Streams {
    // just for practice; should never be used for many reasons
    public static class StreamImpl<T, R> {
        Stream<T> istream;

        public StreamImpl(Stream<T> stream) {
            this.istream = stream;
        }
        public Stream<T> myFilter(Function<T, Boolean> func) {
            List<T> s = new ArrayList<T>();
            istream.forEach(element -> {
                if (func.apply(element)) {
                    s.add(element);
                }
            });
            return s.stream();
        }

        public Stream<R> myMap(Function<T, R> func) {
            List<R> s = new ArrayList<R>();
            istream.forEach(element -> {
                s.add(func.apply(element));
            });
            return s.stream();
        }

        // a somewhat weird reduce
        public R myReduce(R initial, BiFunction<T, R, R> func) {
            return helpReduce(istream.collect(Collectors.toList()).listIterator(), initial, func);
        }

        protected R helpReduce(ListIterator<T> it, R total, BiFunction<T, R, R> func) {
            if (it.hasNext()) {
                return helpReduce(it, func.apply(it.next(), total), func);
            } else {
                return total;
            }
        }
    }

    public static String toTime(Long l) {
        return String.valueOf(TimeUnit.NANOSECONDS.toMillis(l));
    }

    public static void main(String[] args) {
        List<String> outputs = Arrays.asList("This", "is", "a", "test");
        String output = outputs.stream()
                .map(x -> x.toUpperCase())
                .reduce((x, y) -> x + " " + y)
                .get();
        System.out.println("Array: " + output);
        System.out.println("Array: " + outputs.stream().map(x -> x.toUpperCase()).collect(Collectors.joining(" ")));

        System.out.println("==========");
        List<Integer> ints = Arrays.asList(1,2,3,4,5,6,7,8,9,0);
        List<String> strs = ints.parallelStream().map(x -> String.valueOf(x)).collect(Collectors.toList());
        strs.forEach(x -> System.out.println(x));
        ints = ints.stream().filter(x -> x > 4).collect(Collectors.toList());
        ints.forEach(x -> System.out.println(x));
        System.out.println("==========");

        List<List<Integer>> subInts = Arrays.asList(Arrays.asList(1, 2, 3), Arrays.asList(4, 5, 6), Arrays.asList(7, 8 ,9));
        subInts.stream().map((List<Integer> x) -> x.stream().mapToInt(Integer::valueOf).sum()).forEach(x -> {
            System.out.println("Sum: " + x);
        });
        subInts.stream().forEach(x -> {
            System.out.println("Sum: " + x.stream().reduce(0, (a, b) -> a + b));
        });

        System.out.println("==========");

        List<Integer> myInts = Arrays.asList(0,1,2,3,4,5,6,7,8,9);
        StreamImpl<Integer, Integer> intsImpl = new StreamImpl<Integer, Integer>(myInts.stream());
        Stream<Integer> intsStream = intsImpl.myFilter((Integer x) -> {
            if (x % 2 == 0) {
                return true;
            }
            return false;
        });
        System.out.println("MyFilter: " + intsStream.map(x -> String.valueOf(x)).reduce((x, y) -> x + " " + y).get());
        StreamImpl<Integer, Integer> intsMapImpl = new StreamImpl<Integer, Integer>(myInts.stream());
        Stream<Integer> intsMapStream = intsMapImpl.myMap((Integer x) -> {
            return x * x;
        });
        System.out.println("MyMap: " + intsMapStream.map(x -> String.valueOf(x)).reduce((x, y) -> x + " " + y).get());
        StreamImpl<Integer, String> isImpl = new StreamImpl<>(myInts.stream());
        String out = isImpl.myReduce("", (x, y) -> {
            return y + " " + String.valueOf(x);
        });
        System.out.println("MyReduce: " + out);
        StreamImpl<Integer, Integer> iiImpl = new StreamImpl<>(myInts.stream());
        Integer outI = iiImpl.myReduce(0, (x, y) -> {
            return x + y;
        });
        System.out.println("MyReduce: " + outI);
        System.out.println("==========");

        int numRuns = 1;
        int numElements = 1000000;
        List<Long> nTimes = new ArrayList<>(numRuns);
        List<Long> sTimes = new ArrayList<>(numRuns);
        List<Long> pTimes = new ArrayList<>(numRuns);

        for (int i = 0; i < numRuns; i++) {
            List<Integer> unsortedn = new ArrayList<Integer>(numElements);
            List<Integer> unsorteds = new ArrayList<Integer>(numElements);
            List<Integer> unsortedp = new ArrayList<Integer>(numElements);

            Random rand = new Random();
            for (int j = 0; j < numElements; j++) {
                int r = rand.nextInt(1000);
                unsortedn.add(r);
                unsorteds.add(r);
                unsortedp.add(r);
            }

            long startTime;
            long endTime;

            // there is a small performance hit for going first
            // because of the way I am measuring these

            // parallel stream
            startTime = System.nanoTime();
            unsortedp.parallelStream().sorted().filter(x -> x > 100).collect(Collectors.toList());
            endTime = System.nanoTime();
            pTimes.add(endTime - startTime);

            // normal
            startTime = System.nanoTime();
            Collections.sort(unsortedn);
            ListIterator<Integer> iter = unsortedn.listIterator();
            while(iter.hasNext()) {
                if (iter.next() <= 100) {
                    iter.remove();
                }
            }
            endTime = System.nanoTime();
            nTimes.add(endTime - startTime);

            // stream
            startTime = System.nanoTime();
            // unsorteds.stream().sorted().filter(x -> x > 100).collect(Collectors.toList());
            endTime = System.nanoTime();
            sTimes.add(endTime - startTime);
        }

        System.out.println("Took: " + Streams.toTime(nTimes.stream().reduce((x, y) -> x + y).get() / numRuns));
        //System.out.println("Stream took: " + Streams.toTime(sTimes.stream().reduce((x, y) -> x + y).get() / numRuns));
        System.out.println("Parallel stream took: " + Streams.toTime(pTimes.stream().reduce((x, y) -> x + y).get() / numRuns));
        //System.out.println("Took: " + Streams.toTime(nTimes.stream().mapToLong(Long::valueOf).sum() / numRuns));
        //System.out.println("Stream took: " + Streams.toTime(sTimes.stream().mapToLong(Long::valueOf).sum() / numRuns));
        //System.out.println("Parallel stream took: " + Streams.toTime(pTimes.stream().mapToLong(Long::valueOf).sum() / numRuns));
        System.out.println("Took: " + nTimes.stream().mapToLong(Long::valueOf).average());
        //System.out.println("Stream took: " + sTimes.stream().mapToLong(Long::valueOf).average());
        System.out.println("Parallel stream took: " + pTimes.stream().mapToLong(Long::valueOf).average());

    }
}
