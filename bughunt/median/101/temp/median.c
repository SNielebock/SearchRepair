#include<stdio.h>
#include<math.h>

int main(void) 
{ 
  int a ;
  int b ;
  int c ;
  int temp1 ;
  int temp2 ;
  int printf_tmp0 ;

  {
  printf("Please enter 3 numbers separated by spaces > ");
  scanf("%d %d %d", & a, & b, & c);
  temp1 = a;
  temp2 = b;
  if (a > c) {
    a = b;
    b = temp1;
  }
  if (b > c) {
    b = c;
    c = temp2;
  }
  if (a > b) {
    a = b;
    b = temp1;
  }
  printf("%d is the median", b);
  return (0);
}
}

