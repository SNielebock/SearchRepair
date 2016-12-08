#include <stdio.h>

int main(void) 
{ 
  int A ;
  int B ;
  int C ;
  int small ;
  int large ;
  int median ;
  int printf_tmp0 ;

  {
  printf("Please enter 3 numberss separated by spaces > ");
  scanf("%d%d%d", & A, & B, & C);
  if (A > B) {
    small = B;
    large = A;
  } else
  if (A < B) {
    small = A;
    large = B;
  }
printf("inputStart:small:%d:int_VBC_A:%d:int_VBC_B:%d:int_VBC_C:%d:int_VBC_large:%d:int_VBC_median:%d:int_VBC_printf_tmp0:%d:int_VBC_inputEnd", small, A, B, C, large, median, printf_tmp0);
  if (C > large) {
    median = large;
  } else
  if (C < small) {
    median = small;
  } else {
    median = C;
  }
printf("outputStart:small:%d:int_VBC_A:%d:int_VBC_B:%d:int_VBC_C:%d:int_VBC_large:%d:int_VBC_median:%d:int_VBC_printf_tmp0:%d:int_VBC__nextloop_", small, A, B, C, large, median, printf_tmp0);
  printf("%d is the median\n", median);
  return (0);
}
}

