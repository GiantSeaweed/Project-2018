; 161220039 Shiwei Feng
; case 1: 1^mx1^n=1^{mn}

;the set of states
#Q = {beforeX,afterX,after=,goStart,del1m,gotoX,find1,gotoEnd,del1mn,goBackX,clearM,startOver,fCheck1,fCheck2,fCheck3,accept,accept1,accept2,accept3,halt_accept,error,errP,errP1,errP2,errP3,errP4,halt_error}

;the set of input symbols
#S = {1,x,=}

;the set of tape symbols
#T = {0,1,_,T,r,u,e,F,a,l,s,x,=}

;the initial state
#q0 = beforeX

;the blank symbol
#B = _

;the set of final states
#F = {halt_accept}

;the list of transition functions
;curState curSymbol nextSymbol nextDirection nextState

;check whether the input is in the format of "1^mx1^n=1^{mn}"
beforeX 1 1 r beforeX
beforeX x x r afterX
beforeX = = r error
beforeX _ _ r error

afterX 1 1 r afterX
afterX = = r after=
afterX x x r error
afterX _ _ r error

after= 1 1 r after=
after= _ _ l goStart
after= = = r error
after= x x r error

goStart * * l goStart
goStart _ _ r del1m

;whenever I delete a '1' in 1^m, I will delete 1^n in 1^{mn}
del1m 1 _ r gotoX
del1m x x * fCheck1
del1m * * r error

gotoX 1 1 r gotoX
gotoX x x r find1
gotoX * * r error

;I use 'a' to mark how many '1's has been deleted in 1^{mn}
;if the 1^n has been changed to a^n, it indicates that I have deleted 1^n in 1^{mn}
find1 1 a r gotoEnd
find1 a a r find1
find1 = = l clearM
find1 * * r error

gotoEnd 1 1 r gotoEnd
gotoEnd = = r gotoEnd
gotoEnd _ _ l del1mn
gotoEnd * * r error

del1mn 1 _ l goBackX
del1mn * * r error

goBackX 1 1 l goBackX
goBackX = = l goBackX
goBackX a a l goBackX
goBackX x x r find1

;after deleted 1^n in 1^{mn}, I go back to the start and delete a '1' in 1^m. And repeat this procedure...
clearM a 1 l clearM
clearM x x l startOver
clearM * * r error

startOver 1 1 l startOver
startOver _ _ r del1m
startOver * * r error

fCheck1 x x r fCheck2
fCheck2 1 1 r fCheck2
fCheck2 = = r fCheck3
fCheck3 _ _ l accept

fCheck1 * * r error
fCheck2 * * r error
fCheck3 * * r error

;finally I move rightmost and leftmost to clear the tape and print the result
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