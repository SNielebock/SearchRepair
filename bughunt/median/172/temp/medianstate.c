#include <stdio.h>

int main(void) 
{ 
  int i1 ;
  int i2 ;
  int i3 ;
  int printf_tmp0 ;

  {
  printf("Please enter 3 numbers separated by spaces > ");
  scanf("%d%d%d", & i1, & i2, & i3);
printf("inputStart:i1:%d:int_VBC_printf_tmp0:%d:int_VBC_i2:%d:int_VBC_i3:%d:int_VBC_inputEnd", i1, printf_tmp0, i2, i3);
  if (((i1 >= i2 && i1 <= i3) || (i1 == i2 && i1 == i3)) || (i1 > i2 && i1 < i3)) {
    printf_tmp0 = i1;
  } else
  if (((i2 >= i1 && i2 <= i3) || (i2 == i1 && i2 == i3)) || (i2 > i1 && i2 < i3)) {
    printf_tmp0 = i2;
  } else
  if (((i3 >= i2 && i3 <= i1) || (i3 == i2 && i3 == i1)) || (i3 > i2 && i3 < i1)) {
    printf_tmp0 = i3;
  }
printf("outputStart:i1:%d:int_VBC_printf_tmp0:%d:int_VBC_i2:%d:int_VBC_i3:%d:int_VBC__nextloop_", i1, printf_tmp0, i2, i3);
  {
  printf("%d is the median\n", printf_tmp0);
  return (0);
  }
}
}

