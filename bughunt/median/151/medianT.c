#include <stdio.h>
#include <math.h>

int main(void) 
{ 
  int a ;
  int b ;
  int c ;
  int median ;
  int printf_tmp0 ;

  {
  printf("Please enter 3 numbers separated by spaces > ");
  scanf("%d%d%d", & a, & b, & c);
  if ((a <= b && a >= c) || (a >= b && a <= c)) {
    median = a;
  } else
  if ((b <= a && b >= c) || (b >= a && b <= c)) {
    median = b;
  } else
  if ((c <= b && a >= c) || (c >= b && a <= c)) {
    median = c;
  }
  printf("%d is the median\n", median);
  return (0);
}
}
