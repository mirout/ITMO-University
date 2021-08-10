public class PairOfIntList implements Comparable<PairOfIntList>{
    private final IntList firstChild = new IntList();
    private final IntList secondChild = new IntList();

    public void add(int first, int second) {
        firstChild.add(first);
        secondChild.add(second);
    }

    public int size() {
        return firstChild.size();
    }

    @Override
    public int compareTo(PairOfIntList o) {
        if (size() > o.size()) {
            return 1;
        } else if (size() < o.size()) {
            return -1;
        } else if (firstChild.get(0) > o.firstChild.get(0)) {
            return 1;
        } else if (firstChild.get(0) < o.firstChild.get(0)) {
            return -1;
        } else if (secondChild.get(0) > o.secondChild.get(0)) {
            return 1;
        } else if (secondChild.get(0) < o.secondChild.get(0)) {
            return -1;
        }
        return 0;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < size(); i++) {
            sb.append(String.format("%d:%d ", firstChild.get(i), secondChild.get(i)));
        }

        sb.setLength(sb.length() - 1);

        return sb.toString();
    }
}
