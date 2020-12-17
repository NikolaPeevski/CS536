.text
main:
  lw $t0, a
  lw $t1, b
  subu $t2, $t0, $t1
  sw $t2, ($sp)
  subu $sp, $sp, 4
  addu  $t2, $t0, $t1 
  sw $t2, ($sp)
  subu $sp, $sp, 4
  lw $t0 4($sp)
  addu $sp, $sp, 4 
  lw $t1 4($sp)
  addu $sp, $sp, 4 
  blt $t0, $t1, L1
  div $t1, $t0
  j L2
L1:
  div $t0, $t1
L2:
  mflo $t0
  sw $t0, c
