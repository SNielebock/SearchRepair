#include<stdio.h>
#include<math.h>

int main(void) 
{ 
  int a ;
  int b ;
  int c ;
  int printf_tmp0 ;

  {
  printf("Please enter 3 numbers separated by spaces > ");
  scanf("%d %d %d", & a, & b, & c);
  if ((b > a && b < c) || (b < a && b > c)) {
    printf_tmp0 = b;
  }
  if ((c > a && c < b) || (c < a && c > b)) {
    printf_tmp0 = c;
  }
  if ((a > b && a < c) || (a < b && a > c)) {
    printf_tmp0 = a;
  }
  {
  printf("%d is the median", printf_tmp0);
  return (0);
  }
}
}

