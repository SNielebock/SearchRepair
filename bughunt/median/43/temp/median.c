#include <stdio.h>

int main(void) 
{ 
  int A ;
  int B ;
  int C ;
  int printf_tmp0 ;

  {
  printf("Pleaes enter 3 numbers separated by spaces > ");
  scanf("%d%d%d", & A, & B, & C);
  if (A > B && A < C) {
    printf_tmp0 = A;
  }
  if (A > C && A < B) {
    printf_tmp0 = A;
  }
  if (B > C && B < A) {
    printf_tmp0 = B;
  }
  if (B > A && B < C) {
    printf_tmp0 = B;
  }
  if (C > A && C < B) {
    printf_tmp0 = C;
  }
  if (C > B && C < A) {
    printf_tmp0 = C;
  }
  {
  printf("%d is the median\n", printf_tmp0);
  return (0);
  }
}
}
