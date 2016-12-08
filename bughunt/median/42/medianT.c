#include <stdio.h>

int main(void) 
{ 
  int A ;
  int B ;
  int C ;
  int printf_tmp0 ;

  {
  printf("Please enter 3 numbers separated by spaces > ");
  scanf("%d%d%d", & A, & B, & C);
  if (A == B && A == C) {
    printf_tmp0 = A;
  } else
  if (A > B && A < C) {
    printf_tmp0 = A;
  } else
  if (A > C && A < B) {
    printf_tmp0 = A;
  } else
  if (B > C && B < A) {
    printf_tmp0 = B;
  } else
  if (B > A && B < C) {
    printf_tmp0 = B;
  } else
  if (C > A && C < B) {
    printf_tmp0 = C;
  } else
  if (C > B && C < A) {
    printf_tmp0 = C;
  } else
  if (A == B) {
    printf_tmp0 = B;
  } else
  if (A == C) {
    printf_tmp0 = C;
  } else
  if (C == B) {
    printf_tmp0 = C;
  }
  {
  printf("%d is the median\n", printf_tmp0);
  return (0);
  }
}
}

