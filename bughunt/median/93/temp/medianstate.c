#include <stdio.h>

int main(void) 
{ 
  int n1 ;
  int n2 ;
  int n3 ;
  int printf_tmp0 ;

  {
  printf("Please eneter 3 numbers separated by spaces > ");
  scanf("%d%d%d", & n1, & n2, & n3);
printf("inputStart:n1:%d:int_VBC_n2:%d:int_VBC_n3:%d:int_VBC_printf_tmp0:%d:int_VBC_inputEnd", n1, n2, n3, printf_tmp0);
  if (((n1 == n2 || n1 == n3) || (n2 < n1 && n1 < n3)) || (n3 < n1 && n1 < n2)) {
    printf_tmp0 = n1;
  } else
  if ((n2 == n3 || (n1 < n2 && n2 < n3)) || (n3 < n2 && n2 < n1)) {
    printf_tmp0 = n2;
  } else
  if (n1 < n3 && n3 < n2) {
    printf_tmp0 = n3;
  }
printf("outputStart:n1:%d:int_VBC_n2:%d:int_VBC_n3:%d:int_VBC_printf_tmp0:%d:int_VBC__nextloop_", n1, n2, n3, printf_tmp0);
  {
  printf("%d is the median\n", printf_tmp0);
  return (0);
  }
}
}
