max(A, B, B) :- A < B, !.
max(A, B, A).

get_height(nil, 0).
get_height(tree(_, _, _, _, R), R).

get_balance(nil, 0).
get_balance(tree(_, _, L, R, _), Result) :-
    get_height(L, H1),
    get_height(R, H2),
    Result is H2 - H1.

fix_height(tree(K, V, L, R, H), tree(K, V, L, R, NH)) :-
    get_height(L, H1),
    get_height(R, H2),
    max(H1, H2, MAX),
    NH is MAX + 1.

right_rotate(tree(K, V, tree(KL, VL, L, M, HL), R, H), Result) :-
    fix_height(tree(K, V, M, R, H), NR),
    fix_height(tree(KL, VL, L, NR, HL), Result).

left_rotate(tree(K, V, L, tree(KR, VR, M, R, HR), H), Result) :-
    fix_height(tree(K, V, L, M, H), NL),
    fix_height(tree(KR, VR, NL, R, HR), Result).

balance(A, Result) :-
    fix_height(A, tree(K, V, L, R, H)),
    get_balance(A, 2),
    get_balance(R, BR),
    (BR < 0 -> right_rotate(R, NR); NR = R),
    left_rotate(tree(K, V, L, NR, H), Result).

balance(A, Result) :-
    fix_height(A, tree(K, V, L, R, H)),
    get_balance(A, -2),
    get_balance(L, BL),
    (BL > 0 -> left_rotate(L, NL); NL = L),
    right_rotate(tree(K, V, NL, R, H), Result).

balance(A, Result) :-
    fix_height(A, Result),
    get_balance(Result, Balance),
    Balance \= 2,
    Balance \= -2.

map_put(nil, K, V, tree(K, V, nil, nil, 1)).
map_put(tree(K, V, L, R, H), K, NV, tree(K, NV, L, R, H)).
map_put(tree(K, V, L, R, H), NK, NV, Result) :-
    NK < K,
    map_put(L, NK, NV, NL),
    balance(tree(K, V, NL, R, H), Result).
map_put(tree(K, V, L, R, H), NK, NV, Result) :-
    NK > K,
    map_put(R, NK, NV, NR),
    balance(tree(K, V, L, NR, H), Result).

find_min(tree(K, V, nil, R, H), tree(K, V, nil, R, H)) :- !.
find_min(tree(K, V, L, R, H), Result) :-
    find_min(L, Result).

remove_min(tree(K, V, nil, R, H), R) :- !.
remove_min(tree(K, V, L, R, H), Res) :-
    remove_min(L, NL),
    balance(tree(K, V, NL, R, H), Res).

map_remove(nil, K, nil).
map_remove(tree(K, V, L, R, H), NK, Res) :-
    NK < K,
    map_remove(L, NK, NL),
    balance(tree(K, V, NL, R, H), Res).
map_remove(tree(K, V, L, R, H), NK, Res) :-
    NK > K,
    map_remove(R, NK, NR),
    balance(tree(K, V, L, NR, H), Res).
map_remove(tree(K, V, L, nil, H), K, L) :- !.
map_remove(tree(K, V, L, R, H), K, Res) :-
    find_min(R, Min),
    Min = tree(MK, MV, ML, MR, MH),
    remove_min(R, NR),
    balance(tree(MK, MV, L, NR, MH), Res).

map_get(tree(K, V, _, _, _), K, V).
map_get(tree(K, _, L, _, _), NK, Res) :-
    NK < K,
    map_get(L, NK, Res).
map_get(tree(K, _, _, R, _), NK, Res) :-
    NK > K,
    map_get(R, NK, Res).
    
map_build([], nil).
map_build([(K, V) | T], Res) :-
    map_build(T, Tree),
    map_put(Tree, K, V, Res).

map_getLast(tree(K, V, L, nil, H), (K, V)) :- !.
map_getLast(tree(K, V, L, R, H), Result) :- map_getLast(R, Result).

map_removeLast(nil, nil).
map_removeLast(tree(K, V, L, nil, H), L) :- !.
map_removeLast(tree(K, V, L, R, H), Res) :-
    map_removeLast(R, NR),
    balance(tree(K, V, L, NR, H), Res).