	.data
	.align 2
.x:	.space 4
	.data
	.align 2
.y:	.space 4
	.text
f:
	sw    $ra, 0($sp)	#PUSH
	subu  $sp, $sp, 4
	sw    $fp, 0($sp)	#PUSH
	subu  $sp, $sp, 4
	addu  $fp, $sp, 13
	subu  $sp, $sp, 4
	j     .L0
.L0:	
	lw    $ra, -5($fp)
	move  $t0, $fp
	lw    $fp, -9($fp)
	move  $sp, $t0
	jr    $ra
	.data
	.align 2
.z:	.space 4
	.text
	.globl main
_main:
__start:
	sw    $ra, 0($sp)	#PUSH
	subu  $sp, $sp, 4
	sw    $fp, 0($sp)	#PUSH
	subu  $sp, $sp, 4
	addu  $fp, $sp, 8
	subu  $sp, $sp, 4
	lw    $a0, 4($sp)	#POP
	addu  $sp, $sp, 4
	li    $v0, 1
	syscall
	j     .L1
.L1:	
	lw    $ra, -0($fp)
	move  $t0, $fp
	lw    $fp, -4($fp)
	move  $sp, $t0
	li    $v0, 10
	syscall
