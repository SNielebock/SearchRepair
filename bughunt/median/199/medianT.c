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
  if (A > B) {
    Large = A;
    Small = B;
  } else {
    Large = B;
    Small = A;
  }
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

