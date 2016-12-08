#include<stdio.h>

int main(void) 
{ 
  int a ;
  int b ;
  int c ;
  int median ;
  int temp ;
  int printf_tmp0 ;

  {
  median = 0;
  temp = 0;
  printf("Please enter 3 numbers separated by spaces > ");
  scanf("%d%d%d", & a, & b, & c);
  if (a >= b) {
    temp = b;
    b = a;
    a = temp;
  }
printf("inputStart:a:%d:int_VBC_b:%d:int_VBC_temp:%d:int_VBC_c:%d:int_VBC_median:%d:int_VBC_printf_tmp0:%d:int_VBC_inputEnd", a, b, temp, c, median, printf_tmp0);
  if (a < c) {
    median = b;
  } else
  if (b > c) {
    median = a;
  } else {
    median = c;
  }
printf("outputStart:a:%d:int_VBC_b:%d:int_VBC_temp:%d:int_VBC_c:%d:int_VBC_median:%d:int_VBC_printf_tmp0:%d:int_VBC__nextloop_", a, b, temp, c, median, printf_tmp0);
  printf("%d is the median\n", median);
  return (0);
}
}

