#include <stdio.h>
#include <math.h>

int main(void) 
{ 
  int frst ;
  int scnd ;
  int thrd ;
  int cmp1 ;
  int cmp2 ;
  int med ;
  int printf_tmp0 ;

  {
  printf("Please enter 3 numbers separated by spaces > ");
  scanf("%d %d %d", & frst, & scnd, & thrd);
printf("inputStart:thrd:%d:int_VBC_cmp2:%d:int_VBC_scnd:%d:int_VBC_cmp1:%d:int_VBC_printf_tmp0:%d:int_VBC_frst:%d:int_VBC_med:%d:int_VBC_inputEnd", thrd, cmp2, scnd, cmp1, printf_tmp0, frst, med);
  if (frst <= scnd) {
    cmp1 = frst;
  } else {
    cmp1 = scnd;
  }
printf("outputStart:thrd:%d:int_VBC_cmp2:%d:int_VBC_scnd:%d:int_VBC_cmp1:%d:int_VBC_printf_tmp0:%d:int_VBC_frst:%d:int_VBC_med:%d:int_VBC__nextloop_", thrd, cmp2, scnd, cmp1, printf_tmp0, frst, med);
  if (scnd <= thrd) {
    cmp2 = scnd;
  } else {
    cmp2 = thrd;
  }
  if (cmp1 >= cmp2) {
    med = cmp1;
  } else {
    med = cmp2;
  }
  printf("%d is lesser of first 2\n%d is lesser of second 2\n", cmp1, cmp2);
  printf("%d is the median\n", med);
  return (0);
}
}

