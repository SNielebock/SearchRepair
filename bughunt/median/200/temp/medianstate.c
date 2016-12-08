#include <stdio.h>

int main(void) 
{ 
  int A ;
  int B ;
  int C ;
  int Small ;
  int Large ;
  int Median ;
  int printf_tmp0 ;

  {
  printf("Please enter 3 numbers separated by spaces > ");
  scanf("%d%d%d", & A, & B, & C);
printf("inputStart:A:%d:int_VBC_B:%d:int_VBC_C:%d:int_VBC_Small:%d:int_VBC_Large:%d:int_VBC_printf_tmp0:%d:int_VBC_Median:%d:int_VBC_inputEnd", A, B, C, Small, Large, printf_tmp0, Median);
  if (A == B && A == C) {
    Median = A;
  } else
  if (A > B) {
    Large = A;
    Small = B;
  } else {
    Large = B;
    Small = A;
  }
printf("outputStart:A:%d:int_VBC_B:%d:int_VBC_C:%d:int_VBC_Small:%d:int_VBC_Large:%d:int_VBC_printf_tmp0:%d:int_VBC_Median:%d:int_VBC__nextloop_", A, B, C, Small, Large, printf_tmp0, Median);
  if (C > Large) {
    Median = Large;
  } else
  if (C < Small) {
    Median = Small;
  } else {
    Median = C;
  }
  printf("%d is the median\n", Median);
  return (0);
}
}

