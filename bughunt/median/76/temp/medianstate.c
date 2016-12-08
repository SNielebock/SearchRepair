#include <stdio.h>
#include <math.h>

int main(void) 
{ 
  int int1 ;
  int int2 ;
  int int3 ;
  int printf_tmp0 ;

  {
  printf("Please enter 3 numbers separated by spaces > ");
  scanf("%d %d %d", & int1, & int2, & int3);
printf("inputStart:int2:%d:int_VBC_int1:%d:int_VBC_int3:%d:int_VBC_printf_tmp0:%d:int_VBC_inputEnd", int2, int1, int3, printf_tmp0);
  if ((int1 < int2 && int1 > int3) || (int1 < int2 && int1 > int3)) {
    printf_tmp0 = int1;
  } else
  if ((int2 < int1 && int2 > int3) || (int2 < int3 && int2 > int1)) {
    printf_tmp0 = int2;
  } else
  if ((int3 < int1 && int3 > int2) || (int3 < int2 && int3 > int1)) {
    printf_tmp0 = int3;
  }
printf("outputStart:int2:%d:int_VBC_int1:%d:int_VBC_int3:%d:int_VBC_printf_tmp0:%d:int_VBC__nextloop_", int2, int1, int3, printf_tmp0);
  {
  printf("%d is the median\n", printf_tmp0);
  return (0);
  }
}
}

