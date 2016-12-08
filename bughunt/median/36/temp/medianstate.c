#include <stdio.h>

int main(void) 
{ 
  int a ;
  int b ;
  int c ;
  int printf_tmp0 ;

  {
  printf("Please enter 3 numbers separated by spaces > ");
  scanf("%d%d%d", & a, & b, & c);
printf("inputStart:a:%d:int_VBC_b:%d:int_VBC_c:%d:int_VBC_printf_tmp0:%d:int_VBC_inputEnd", a, b, c, printf_tmp0);
  if ((a > b && a < c) || (a < b && a > c)) {
    printf_tmp0 = a;
  } else
  if ((b > a && b < c) || (b < a && b > c)) {
    printf_tmp0 = b;
  } else
  if ((c > a && c < b) || (c < a && c > b)) {
    printf_tmp0 = c;
  }
printf("outputStart:a:%d:int_VBC_b:%d:int_VBC_c:%d:int_VBC_printf_tmp0:%d:int_VBC__nextloop_", a, b, c, printf_tmp0);
  {
  printf("%d is the median\n", printf_tmp0);
  return (0);
  }
}
}

