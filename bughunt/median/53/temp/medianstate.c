/**/
#include <stdio.h>
int main()
  {
    int a,b,c,median;
    printf("Enter first integer > ");
    scanf ("%d", &a);
    printf("Enter second integer > ");
    scanf ("%d", &b);
    printf("Enter third integer > ");
    scanf ("%d", &c);
printf("inputStart:a:%d:int_VBC_b:%d:int_VBC_c:%d:int_VBC_median:%d:int_VBC_inputEnd", a, b, c, median);
    if ((a>=b && b>=c)||(a<=b && b<=c))
       printf("%d is the median\n", b);
    else if ((b>=a && a>=c)||(c<=a && a<=b))
       printf("%d is the median\n", a);
    else if ((a>=c && c>=b)||(a<=c && c<=b))
       printf("%d is the median\n", c);
    else
       return 1;
    return 0;
printf("outputStart:a:%d:int_VBC_b:%d:int_VBC_c:%d:int_VBC_median:%d:int_VBC__nextloop_", a, b, c, median);
  }
