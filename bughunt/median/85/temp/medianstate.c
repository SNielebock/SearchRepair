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
printf("inputStart:a:%d:int_VBC_b:%d:int_VBC_c:%d:int_VBC_median:%d:int_VBC_printf_tmp0:%d:int_VBC_inputEnd", a, b, c, median, printf_tmp0);
  if (a > b && a < c) {
    median = a;
  } else
  if (b > a && b < c) {
    median = b;
  } else
  if (c > a && c < b) {
    median = c;
  }
printf("outputStart:a:%d:int_VBC_b:%d:int_VBC_c:%d:int_VBC_median:%d:int_VBC_printf_tmp0:%d:int_VBC__nextloop_", a, b, c, median, printf_tmp0);
  printf("%d is the median\n", median);
  return (0);
}
}

