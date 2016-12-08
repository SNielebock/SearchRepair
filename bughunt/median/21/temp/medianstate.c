#include <stdio.h>

int main(void) 
{ 
  int a ;
  int b ;
  int c ;
  int m ;
  int printf_tmp0 ;

  {
  printf("Please enter 3 numbers separated by spaces > ");
  scanf("%d %d %d", & a, & b, & c);
printf("inputStart:a:%d:int_VBC_b:%d:int_VBC_c:%d:int_VBC_printf_tmp0:%d:int_VBC_m:%d:int_VBC_inputEnd", a, b, c, printf_tmp0, m);
  if ((a >= b && a <= c) || (a >= c && a <= b)) {
    m = b;
  } else
  if ((b >= a && b <= c) || (b >= c && b <= a)) {
    m = b;
  } else
  if ((c >= a && c <= b) || (c >= b && c <= a)) {
    m = c;
  }
printf("outputStart:a:%d:int_VBC_b:%d:int_VBC_c:%d:int_VBC_printf_tmp0:%d:int_VBC_m:%d:int_VBC__nextloop_", a, b, c, printf_tmp0, m);
  printf("%d is the median\n", m);
  return (0);
}
}

