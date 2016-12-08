#include <stdio.h>
#include <math.h>

int main(void) 
{ 
  int n1 ;
  int n2 ;
  int n3 ;
  int temp ;
  int printf_tmp0 ;

  {
  printf("Please enter 3 numbers separated by spaces > ");
  scanf("%d%d%d", & n1, & n2, & n3);
  if (n2 < n1) {
    temp = n2;
    n2 = n1;
    n1 = temp;
  }
  if (n3 < n2 && n3 >= n1) {
    temp = n2;
    n2 = n3;
    n3 = temp;
  }
  printf("%d is the median\n", n2);
  return (0);
}
}

