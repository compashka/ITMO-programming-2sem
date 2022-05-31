erat_table(1).
init(N) :- do_sieve(2, N + 1).

do_sieve(I, N) :- prime(I), X is (I * I), fill_table(X, N, I).
do_sieve(I, N) :- I < N, I1 is (I + 1), do_sieve(I1, N).
fill_table(I, N, K) :- I < N, assert(erat_table(I)), M is (I + K), fill_table(M, N, K).

prime(X) :- \+(erat_table(X)).
composite(X) :- erat_table(X).

divisor(N, A, A) :- K is div(N, A), N is (K * A), prime(A), !.
divisor(N, A, D) :- D =< N, D1 is D + 1, divisor(N, A, D1), !.

find_divisors(1, [], _).
find_divisors(N, [N], _) :- prime(N), !.
find_divisors(N, [L | R], P) :- divisor(N, L, P), N1 is N / L, find_divisors(N1, R, L), !.

check_order([]).
check_order([_]).
check_order([A, B | C]) :- A =< B , check_order([B | C]), !.
check_copies(1, []).
check_copies(N, [A | R]) :- check_copies(T, R), N is A * T, !.

prime_divisors(N, D) :- is_list(D), check_order(D), check_copies(N, D), !.
prime_divisors(N, D) :- \+(is_list(D)), find_divisors(N, D, 2), !.

to_numb_syst(0, [], _) :- !.
to_numb_syst(N, [L | R], K) :- N1 is div(N, K), L is mod(N, K), to_numb_syst(N1, R, K), !.

prime_palindrome(N, K) :- prime(N), to_numb_syst(N, P, K), reverse(P, PR), P = PR.