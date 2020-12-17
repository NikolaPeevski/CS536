.text
.globl main
func:
	
	#Prologue
	sw $ra, 0($sp) #Push
	subu $sp, $sp, 4
	sw $fp, 0($sp) #Push
	subu $sp, $sp, 4
	addu $fp, $sp, 8
	move    $t0,$a1
	move    $t1,$a2
	
	#Function body
	## WhileStmt
	
	
	.L0:
	## WhileCondition
	## b > a
	bgt $t1, $t0, .L2
	
	j .L1

	.L2:
	##Assign
	addu $t0, $t0, $t1
	j .L0

	.L1: #Lool Successor

	#Return

	j _func_Exit

	_func_Exit:
	#Epilogue
	lw $ra, 0($fp)
	move $t0, $fp #save control link
	lw $fp, -4($fp) #restore FP
	jr $ra
