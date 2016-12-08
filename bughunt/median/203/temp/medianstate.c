#include <stdio.h>

int main(void) 
{ 
  int n1 ;
  int n2 ;
  int n3 ;
  int small ;
  int printf_tmp0 ;

  {
  printf("Please enter 3 numbers separated by spaces > ");
  scanf("%d%d%d", & n1, & n2, & n3);
  if (n1 < n2) {
    small = n1;
    if (small > n3) {
      printf_tmp0 = n1;
    } else
    if (n3 > n2) {
      printf_tmp0 = n2;
    } else {
      printf_tmp0 = n3;
    }
  } else {
    small = n2;
printf("inputStart:small:%d:int_VBC_n1:%d:int_VBC_n2:%d:int_VBC_n3:%d:int_VBC_printf_tmp0:%d:int_VBC_inputEnd", small, n1, n2, n3, printf_tmp0);
    if (small > n3) {
      printf_tmp0 = n2;
    } else
    if (n3 > n1) {
      printf_tmp0 = n3;
    } else {
      printf_tmp0 = n1;
    }
printf("outputStart:small:%d:int_VBC_n1:%d:int_VBC_n2:%d:int_VBC_n3:%d:int_VBC_printf_tmp0:%d:int_VBC__nextloop_", small, n1, n2, n3, printf_tmp0);
  }
  {
  printf("%d is the median\n", printf_tmp0);
  return (0);
  }
}
}

