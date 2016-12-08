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
printf("inputStart:x:%d:int_VBC_printf_tmp0:%d:int_VBC_y:%d:int_VBC_z:%d:int_VBC_inputEnd", x, printf_tmp0, y, z);
  if (x >= y && x <= z) {
    printf_tmp0 = x;
  } else
  if (y >= x && y <= z) {
    printf_tmp0 = y;
  } else {
    printf_tmp0 = z;
  }
printf("outputStart:x:%d:int_VBC_printf_tmp0:%d:int_VBC_y:%d:int_VBC_z:%d:int_VBC__nextloop_", x, printf_tmp0, y, z);
  {
  printf("%d is the median\n", printf_tmp0);
  return (0);
  }
}
}

