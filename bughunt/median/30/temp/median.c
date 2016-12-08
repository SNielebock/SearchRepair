#include <math.h>
#include <stdio.h>

int main(void) 
{ 
  int a ;
  int b ;
  int c ;
  int printf_tmp0 ;

  {
  printf("Please enter 3 numbers separated by spaces > ");
  scanf("%d%d%d", & a, & b, & c);
  if ((a <= b && b <= c) || (c <= b && b <= a)) {
    printf_tmp0 = b;
  }
  if ((b <= a && a <= c) || (c <= a && a <= b)) {
    printf_tmp0 = a;
  } else {
    printf_tmp0 = c;
  }
  {
  printf("%d is the median\n", printf_tmp0);
  return (0);
  }
}
}

