#include<stdio.h>
#include<math.h>

int main(void) 
{ 
  int a ;
  int b ;
  int c ;
  int temp1 ;
  int temp2 ;
  int printf_tmp0 ;

  {
  printf("Please enter 3 numbers separated by spaces > ");
  scanf("%d %d %d", & a, & b, & c);
  temp1 = a;
  temp2 = b;
printf("inputStart:a:%d:int_VBC_b:%d:int_VBC_c:%d:int_VBC_printf_tmp0:%d:int_VBC_temp2:%d:int_VBC_temp1:%d:int_VBC_inputEnd", a, b, c, printf_tmp0, temp2, temp1);
  if (a > c) {
    a = b;
    b = temp1;
printf("outputStart:a:%d:int_VBC_b:%d:int_VBC_c:%d:int_VBC_printf_tmp0:%d:int_VBC_temp2:%d:int_VBC_temp1:%d:int_VBC__nextloop_", a, b, c, printf_tmp0, temp2, temp1);
  }
  if (b > c) {
    b = c;
    c = temp2;
  }
  if (a > b) {
    a = b;
    b = temp1;
  }
  printf("%d is the median", b);
  return (0);
}
}

