composite(1).
prime(N) :- not(composite(N)).

init(N) :- sieve(2, N), !.

sieve(I, N) :-
	POW2 is I * I,
	POW2 > N, !.
sieve(I, N) :-
	prime(I),
	POW2 is I * I,
	fill_table(N, POW2, I).
sieve(I, N) :- NEXT is I + 1, sieve(NEXT, N).

fill_table(N, I, J) :- I =< N, assert(composite(I)), NEXT is I + J, fill_table(N, NEXT, J).

sorted([]).
sorted([A]).
sorted([A, B | XS]) :- A =< B.

prime_divisors(1, []) :- !.
prime_divisors(X, [H | T]) :-
	not(number(X)),
	prime(H),
	sorted([H | T]),
	prime_divisors(R, T),
	X is R * H.
prime_divisors(X, [H | T]) :-
	number(X),
	min_divisors(X, H),
	K is X / H,
	prime_divisors(K, T).

min_divisors(X, R) :-
	prime(X),
	R is X, !.
min_divisors(X, R) :-
	not(prime(X)),
	loop(X, 2, R), !.

loop(X, DIVISOR, R) :-
	prime(DIVISOR),
	0 is mod(X, DIVISOR),
	R is DIVISOR, !.
loop(X, DIVISOR, R) :-
	not(prime),
	NEXT is DIVISOR + 1,
	NEXT =< X,
	loop(X, NEXT, R), !.
loop(X, DIVIVSOR, R) :-
	not(0 is mod(X, DIVISOR)),
	NEXT is DIVISOR + 1,
	NEXT =< X,
	loop(X, NEXT, R), !.

inter([], _, []).
inter(_, [], []).
inter([H1|T1], [H1|T2], [H1|R]) :-
	inter(T1, T2, R).
inter([H1|T1], [H2|T2], R) :-
	H1 > H2,
	inter([H1|T1], T2, R).
inter([H1|T1], [H2|T2], R) :-
	H1 < H2,
  inter(T1, [H2|T2], R).

gcd(A, B, R) :-
	prime_divisors(A, AD),
	prime_divisors(B, BD),
	inter(AD, BD, S),
	prime_divisors(R, S), !.
