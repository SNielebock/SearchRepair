#include<stdio.h>

int main(void) 
{ 
  int num1 ;
  int num2 ;
  int num3 ;
  int median ;
  int big ;
  int small ;
  int printf_tmp0 ;

  {
  printf("Please enter 3 numbers separated by spaces > ");
  scanf("%d %d %d", & num1, & num2, & num3);
printf("inputStart:small:%d:int_VBC_big:%d:int_VBC_median:%d:int_VBC_num1:%d:int_VBC_printf_tmp0:%d:int_VBC_num3:%d:int_VBC_num2:%d:int_VBC_inputEnd", small, big, median, num1, printf_tmp0, num3, num2);
  if (num1 >= num2) {
    small = num2;
    big = num1;
  } else {
    big = num2;
    small = num2;
printf("outputStart:small:%d:int_VBC_big:%d:int_VBC_median:%d:int_VBC_num1:%d:int_VBC_printf_tmp0:%d:int_VBC_num3:%d:int_VBC_num2:%d:int_VBC__nextloop_", small, big, median, num1, printf_tmp0, num3, num2);
  }
  if (num3 >= big) {
    median = big;
  } else
  if (num3 <= small) {
    median = small;
  } else {
    median = num3;
  }
  printf("%d is the median\n", median);
  return (0);
}
}

