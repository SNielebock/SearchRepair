#include <stdio.h>

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
  if ((a >= b && a <= c) || (a >= c && a <= b)) {
    median = a;
  }
printf("outputStart:a:%d:int_VBC_b:%d:int_VBC_c:%d:int_VBC_median:%d:int_VBC_printf_tmp0:%d:int_VBC__nextloop_", a, b, c, median, printf_tmp0);
  if ((b >= a && b <= c) || (b >= c && b <= a)) {
    median = b;
  } else {
    median = c;
  }
  printf("%d is the median\n", median);
  return (0);
}
}

