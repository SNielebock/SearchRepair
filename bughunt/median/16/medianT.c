#include <stdio.h>
#include <math.h>

int main(void) 
{ 
  int a ;
  int b ;
  int c ;
  int printf_tmp0 ;

  {
  printf("Please enter 3 numbers separated by spaces  > ");
  scanf("%d%d%d", & a, & b, & c);
  if (a < c && a > b) {
    printf_tmp0 = a;
  } else
  if (a < b && a > c) {
    printf_tmp0 = a;
  }
  if (b < a && b > c) {
    printf_tmp0 = b;
  } else
  if (b > a && b < c) {
    printf_tmp0 = b;
  }
  if (c > a && c < b) {
    printf_tmp0 = c;
  } else
  if (c < a && c > b) {
    printf_tmp0 = c;
  }
  {
  printf("%d is the median\n", printf_tmp0);
  return (0);
  }
}
}

