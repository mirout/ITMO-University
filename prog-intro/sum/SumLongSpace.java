public class SumLongSpace {

    public static boolean isSpaceSeparator(char c) {
        return Character.SPACE_SEPARATOR == Character.getType(c);
    }

    public static int findWhitespace(String s, int start, boolean isNum) { 

        int i = start;

        while (i < s.length() && (isNum ^ isSpaceSeparator(s.charAt(i)))) {
            i++;
        }

        return i;
    }

    public static long sumNumbersAtString(String s) {

        int start = 0;
        int end = 0;
        long sum = 0;

        while (start < s.length()) {

            start = findWhitespace(s, start, false);
            end = findWhitespace(s, start, true);

            if (start < end) {
                sum += Long.parseLong(s.substring(start, end));
            }
            start = end;

        }

        return sum;
    }

    public static void main(String[] args) {

        long sum = 0;

        for (int i = 0; i < args.length; i++) {
            sum += sumNumbersAtString(args[i]);
        }

        System.out.println(sum);
    }
}