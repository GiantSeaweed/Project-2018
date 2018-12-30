; 161220039 Shiwei Feng
; case2: ww

;the set of states
#Q = {q1,q2,q3,q4,q5,q6,q7,q8,q9,q10,q11,accept,accept1,accept2,accept3,halt_accept,error,errP,errP1,errP2,errP3,errP4,halt_error}

;the set of input symbols
#S = {a,b}

;the set of tape symbols
#T = {a,b,X,Y,Z,_,T,r,u,e,F,a,l,s}

;the initial state
#q0 = q1

;the blank symbol
#B = _

;the set of final states
#F = {halt_accept}

;the list of transition functions
;curState curSymbol nextSymbol nextDirection nextState
q1 a X r q2
q1 b Y r q2
q1 X X l q5
q1 Y Y l q8
q1 _ _ r accept
q1 * * r error

q2 a a r q2
q2 b b r q2
q2 _ _ l q3
q2 X a l q3
q2 Y b l q3
q2 * * r error

q3 a X l q4
q3 b Y l q4
q3 * * r error

q4 a a l q4
q4 b b l q4
q4 X a r q1
q4 Y b r q1
q4 * * r error

q5 a a l q5
q5 b b l q5
q5 _ _ r q6
q5 Z Z r q6
q5 * * r error

q6 a Z r q7
q6 * * r error

q7 a a r q7
q7 b b r q7
q7 X a r q11
q7 * * r error

q8 a a l q8
q8 b b l q8
q8 _ _ r q9
q8 Z Z r q9
q8 * * r error

q9 b Z r q10
q9 * * r error

q10 a a r q10
q10 b b r q10
q10 Y b r q11
q10 * * r error

q11 a X l q5
q11 b Y l q8
q11 _ _ l accept
q11 * * r error

accept * _ l accept
accept _ T r accept1
accept1 _ r r accept2
accept2 _ u r accept3
accept3 _ e r halt_accept

error * * r error
error _ _ l errP
errP * _ l errP
errP _ F r errP1
errP1 _ a r errP2
errP2 _ l r errP3
errP3 _ s r errP4
errP4 _ e r halt_error

