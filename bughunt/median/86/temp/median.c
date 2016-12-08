# include <stdio.h>
# include <math.h>

int main(void) 
{ 
  int a ;
  int b ;
  int c ;
  int printf_tmp0 ;

  {
  printf("Please enter 3 numbers separated by spaces > ");
  scanf("%d%d%d", & a, & b, & c);
  if (a > b && b > c) {
    printf_tmp0 = b;
  }
  if (a > b && a > c) {
    if (c > b) {
      printf_tmp0 = c;
    }
  }
  if (b > a && a > c) {
    printf_tmp0 = a;
  }
  if (b > a && b > c) {
    if (c > a) {
      printf_tmp0 = c;
    }
  }
  if (c > a && a > b) {
    printf_tmp0 = a;
  }
  if (c > a && c > b) {
    if (b > a) {
      printf_tmp0 = b;
    }
  }
  {
  printf("%d is the median\n", printf_tmp0);
  return (0);
  }
}
}

