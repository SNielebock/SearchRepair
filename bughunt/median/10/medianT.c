#include <stdio.h>
#include <math.h>

int main(void) 
{ 
  int x ;
  int y ;
  int z ;
  int temp ;
  int printf_tmp0 ;

  {
  printf("Please enter 3 numbers separated by spaces > ");
  scanf("%d %d %d", & x, & y, & z);
  if (x >= y) {
    temp = x;
    x = y;
    y = temp;
  }
  if (x >= z) {
    temp = x;
    x = z;
    z = temp;
  }
  if (y >= z) {
    x = z;
  } else {
    x = y;
  }
  printf("%d is the median\n", x);
  return (0);
}
}

