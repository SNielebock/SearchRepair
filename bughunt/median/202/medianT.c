#include<stdio.h>

int main(void) 
{ 
  int x ;
  int y ;
  int z ;
  int printf_tmp0 ;

  {
  printf("Please enter 3 numbers separated by space  > ");
  scanf("%d%d%d", & x, & y, & z);
  if (x >= y && x <= z) {
    printf_tmp0 = x;
  } else
  if (y >= x && y <= z) {
    printf_tmp0 = y;
  } else {
    printf_tmp0 = z;
  }
  {
  printf("%d is the median\n", printf_tmp0);
  return (0);
  }
}
}

