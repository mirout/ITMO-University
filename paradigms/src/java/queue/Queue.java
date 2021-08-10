package queue;

/*
    Model:
        [a1, a2, ..., an]
        n -- queue size

    Inv:
        n >= 0
        forall i = 1..n: a[i] != null
*/

public interface Queue {

    // Pred: obj != null
    // Post: n = n' + 1 &&  a[n - 1] == obj && forall i = 1..n': a[i] == a'[i]
    void enqueue(Object element);

    // Pred: n > 0
    // Post: R == a[1] && n == n' && forall i = 1..n: a[i] == a'[i]
    Object element();

    // Pred: n > 0
    // Post: R = a'[1] && n = n' - 1 && forall i = 1..n: a[i] == a'[i + 1]
    Object dequeue();

    // Pred: true
    // Post: R == n && forall i = 1..n: a[i] == a'[i]
    int size();

    // Pred: true
    // Post: n' == 0 && forall i = 1..n: a[i] == a'[i]
    void clear();

    // Pred: true
    // Post: R == (n == 0) && n == n' && forall i = 1..n: a[i] == a'[i]
    boolean isEmpty();

    // Pred: true
    // Post: Res == (exists i : a[i] == element) && n == n' && forall j = j..n: a[j] == a'[j]
    boolean contains(Object element);

    // Pred: true
    // Post: R == (j <= n') where
    //      j = min({i : a[i] == element} U {n' + 1})
    //      && min(Set) == k : (k in Set) && (forall j in Set: k <= j)
    //      && forall i = 1..(j - 1): a[i] == a'[i]
    //      && forall i = (j + 1)..n': a[i - 1] == a'[i]
    //      && n = max(n', j) - 1
    boolean removeFirstOccurrence(Object element);
}
