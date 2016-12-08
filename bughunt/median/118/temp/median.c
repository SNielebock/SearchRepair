#include <stdio.h>
#include<math.h>

int main(void) 
{ 
  int a ;
  int b ;
  int c ;
  int printf_tmp0 ;

  {
  printf("Please enter 3 numbers separated by spaces > ");
  scanf("%d%d%d", & a, & b, & c);
  if ((a > b && a < c) || (a < b && a > c)) {
    printf_tmp0 = a;
  } else
  if ((b > a && b < c) || (b < a && b > c)) {
    printf_tmp0 = b;
  } else {
    printf_tmp0 = c;
  }
  {
  printf("%d is the median\n", printf_tmp0);
  return (0);
  }
}
}

