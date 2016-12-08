#include <stdio.h>
#include <stdlib.h>

int main(void) 
{ 
  int a ;
  int b ;
  int c ;
  int printf_tmp0 ;

  {
  printf("Please enter 3 numbers separated by spaces > ");
  scanf("%d%d%d", & a, & b, & c);
  if (a > b && a < c) {
    printf_tmp0 = a;
  }
  if (a > c && a < b) {
    printf_tmp0 = a;
  }
  if (b > a && b < c) {
    printf_tmp0 = b;
  }
  if (b > c && b < a) {
    printf_tmp0 = b;
  }
  if (c > b && c < a) {
    printf_tmp0 = c;
  }
  if (c > a && c < b) {
    printf_tmp0 = c;
  }
  {
  printf("%d is the median\n", printf_tmp0);
  return (0);
  }
}
}

