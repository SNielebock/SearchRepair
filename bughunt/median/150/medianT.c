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
  a = 0;
  b = 0;
  c = 0;
  median = 0;
  printf("Please enter 3 numbers separated by spaces > ");
  scanf("%d %d %d", & a, & b, & c);
  if ((a <= b && a >= c) || (a >= b && a <= c)) {
    median = a;
  } else
  if ((b <= a && b >= c) || (b >= a && b <= c)) {
    median = b;
  } else {
    median = c;
  }
  printf("%d is the median\n", median);
  return (0);
}
}

