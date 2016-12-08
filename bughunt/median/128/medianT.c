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
  int tmp___2 ;
  int tmp___3 ;
  int tmp___4 ;
  int printf_tmp0 ;

  {
  printf("Please enter 3 numbers separated by spaces > ");
  scanf("%lf%lf%lf", & first, & second, & third);
  tmp = fabs(first);
  tmp___0 = fabs(second);
  tmp___1 = fabs(third);
  median = ((tmp + tmp___0) + tmp___1) / 3;
  tmp___2 = fabs(first - median);
  comp_fir = tmp___2;
  tmp___3 = fabs(second - median);
  comp_sec = tmp___3;
  tmp___4 = fabs(third - median);
  comp_thi = tmp___4;
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

