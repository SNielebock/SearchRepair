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
  if (C > large) {
    median = large;
  } else
  if (C < small) {
    median = small;
  } else {
    median = C;
  }
  printf("%d is the median\n", median);
  return (0);
}
}

