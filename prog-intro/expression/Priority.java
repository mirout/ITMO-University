package expression;

public class Priority {

    private final PriorityEnum value;
    private final boolean isReverse;

    public Priority(PriorityEnum value, boolean isReverse) {
        this.value = value;
        this.isReverse = isReverse;
    }

    public boolean isBracketsNeed(Priority pr, boolean isLeft) {
        return isLeft ? isLeftBracketsNeed(pr) : isRightBracketsNeed(pr);
    }

    public boolean isLeftBracketsNeed(Priority pr) {
        return pr.value.compareTo(value) < 0;
    }

    public boolean isRightBracketsNeed(Priority pr) {
        return pr.value.compareTo(value) < 0 ||
                pr.value == value && isReverse ||
                pr.value == value && value == PriorityEnum.MULTIPLY && pr.isReverse;
    }
}
