#include <stdio.h>
#include <math.h>

int main(void) 
{ 
  int a ;
  int b ;
  int c ;
  int printf_tmp0 ;

  {
  printf("Please enter 3 numbers seperated by spaces > ");
  scanf("%d %d %d", & a, & b, & c);
  if ((a < b && b < c) || (b < a && c < b)) {
    printf_tmp0 = b;
  }
  if ((b < a && a < c) || (a < b && c < a)) {
    printf_tmp0 = a;
  }
  if ((a < c && c < b) || (b < c && c < a)) {
    printf_tmp0 = c;
  }
  {
  printf("%d is the median\n", printf_tmp0);
  return (0);
  }
}
}

