#include <stdio.h>
#include <math.h>

int main(void) 
{ 
  double first ;
  double second ;
  double third ;
  double median ;
  double comp_fir ;
  double comp_sec ;
  double comp_thi ;
  int tmp ;
  int tmp___0 ;
  int tmp___1 ;
  int printf_tmp0 ;

  {
  printf("Please enter 3 numbers separated by spaces > ");
  scanf("%lf%lf%lf", & first, & second, & third);
  median = ((first + second) + third) / (double )3;
  tmp = fabs(first - median);
  comp_fir = tmp;
  tmp___0 = fabs(second - median);
  comp_sec = tmp___0;
  tmp___1 = fabs(third - median);
  comp_thi = tmp___1;
  if (comp_fir < comp_sec && comp_fir < comp_thi) {
    printf_tmp0 = first;
  } else
  if (comp_sec < comp_fir && comp_sec < comp_thi) {
    printf_tmp0 = second;
  } else
  if (comp_thi < comp_fir && comp_thi < comp_sec) {
    printf_tmp0 = third;
  }
  {
  printf("%.0f is the median\n", printf_tmp0);
  return (0);
  }
}
}

