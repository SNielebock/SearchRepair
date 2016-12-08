#include <stdio.h>
#include <math.h>

int main(void) 
{ 
  int a ;
  int b ;
  int c ;
  int z ;
  int printf_tmp0 ;

  {
  printf("Please enter 3 numbers separated by spaces > ");
  scanf("%d%d%d", & a, & b, & c);
printf("inputStart:a:%d:int_VBC_b:%d:int_VBC_c:%d:int_VBC_printf_tmp0:%d:int_VBC_z:%d:int_VBC_inputEnd", a, b, c, printf_tmp0, z);
  if ((b < a && a < c) || (c < a && a < b)) {
    z = a;
  } else
  if ((a < b && b < c) || (c < b && b < a)) {
    z = b;
  } else
  if ((a < c && c < b) || (b < c && c < a)) {
    z = c;
  }
printf("outputStart:a:%d:int_VBC_b:%d:int_VBC_c:%d:int_VBC_printf_tmp0:%d:int_VBC_z:%d:int_VBC__nextloop_", a, b, c, printf_tmp0, z);
  printf("%d is the median\n", z);
  return (0);
}
}

