operation(op_add, A, B, R) :- R is A + B.
operation(op_subtract, A, B, R) :- R is A - B.
operation(op_multiply, A, B, R) :- R is A * B.
operation(op_divide, A, B, R) :- R is A / B.
operation(op_negate, A, R) :- R is -A.
operation(op_sinh, A, R) :- R is (exp(A) - exp(-A)) / 2.
operation(op_cosh, A, R) :- R is (exp(A) + exp(-A)) / 2.

lookup(K, [(K, V) | _], V).
lookup(K, [_ | T], V) :- lookup(K, T, V).

evaluate(const(V), Vars, V).
evaluate(variable(C), Vars, R) :- atom_chars(C, [H | _]), lookup(H, Vars, R).
evaluate(operation(Op, A), Vars, R) :-
    evaluate(A, Vars, AV),
    operation(Op, AV, R).
evaluate(operation(Op, A, B), Vars, R) :- 
    evaluate(A, Vars, AV), 
    evaluate(B, Vars, BV),
    operation(Op, AV, BV, R).

:- load_library('alice.tuprolog.lib.DCGLibrary').

nonvar(V, _) :- var(V).
nonvar(V, T) :- nonvar(V), call(T).

expr_ws_p(A) --> {(var(A) -> R = true; R = false)},
    ws(R),
    expr_p(A),
    ws(R).

expr_p(variable(Name)) --> 
    {nonvar(Name, atom_chars(Name, Chars))},
    variables(Chars),
    { Chars = [_ | _], atom_chars(Name, Chars)}.

variables([]) --> [].
variables([H | T]) --> 
    [H], variables(T), { member(H, ['X', 'Y', 'Z', x, y, z]) }.

expr_p(const(Value)) --> 
    {nonvar(Value, number_chars(Value, Chars))},
    digits_p(Chars),
    { Chars = [_, _ | _], number_chars(Value, Chars)}.

digits_p([]) --> [].
digits_p([H | T]) --> 
    { member(H, ['-', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.'])},
    [H], 
    digits_p(T).

ws(_) --> [].
ws(true) --> [' '], ws(true).

op_p(op_add)      --> ['+'].
op_p(op_subtract) --> ['-'].
op_p(op_multiply) --> ['*'].
op_p(op_divide)   --> ['/'].
op_p(op_negate)   --> ['n', 'e', 'g', 'a', 't', 'e'].
op_p(op_sinh)     --> ['s', 'i', 'n', 'h'].
op_p(op_cosh)     --> ['c', 'o', 's', 'h'].

expr_p(operation(Op, A)) --> {var(Op) -> R = true; R = false},
    op_p(Op), ws(R), ['('], expr_ws_p(A), [')'].

expr_p(operation(Op, A, B)) --> ['('], expr_ws_p(A), [' '], op_p(Op), [' '], expr_ws_p(B), [')'].

infix_str(E, A) :- ground(E), phrase(expr_ws_p(E), C), atom_chars(A, C).
infix_str(E, A) :-   atom(A), atom_chars(A, C), phrase(expr_ws_p(E), C).