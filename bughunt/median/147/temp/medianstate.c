#include<stdio.h>

int main(void) 
{ 
  int num1 ;
  int num2 ;
  int num3 ;
  int median ;
  int printf_tmp0 ;

  {
  printf("Please enter 3 numbers seperated by spaces > ");
  scanf("%d%d%d", & num1, & num2, & num3);
printf("inputStart:median:%d:int_VBC_num1:%d:int_VBC_printf_tmp0:%d:int_VBC_num3:%d:int_VBC_num2:%d:int_VBC_inputEnd", median, num1, printf_tmp0, num3, num2);
  if ((num1 > num2 && num1 < num3) || (num1 < num2 && num1 > num3)) {
    median = num1;
  } else
  if ((num2 > num1 && num2 < num3) || (num2 < num1 && num2 > num3)) {
    median = num2;
  } else
  if ((num3 > num1 && num3 < num2) || (num3 < num1 && num3 > num2)) {
    median = num3;
  }
printf("outputStart:median:%d:int_VBC_num1:%d:int_VBC_printf_tmp0:%d:int_VBC_num3:%d:int_VBC_num2:%d:int_VBC__nextloop_", median, num1, printf_tmp0, num3, num2);
  printf("%d is the median\n", median);
  return (0);
}
}

