#include <stdio.h>
#include <math.h>

int main(void) 
{ 
  int x ;
  int y ;
  int z ;
  int printf_tmp0 ;

  {
  printf("Please enter 3 numbers separated by spaces > ");
  scanf("%d %d %d", & x, & y, & z);
  if (x == y) {
    printf_tmp0 = x;
  }
  if (x == z) {
    printf_tmp0 = x;
  }
  if (y == z) {
    printf_tmp0 = y;
  }
  {
  printf("%d is the median\n", printf_tmp0);
  return (0);
  }
}
}

