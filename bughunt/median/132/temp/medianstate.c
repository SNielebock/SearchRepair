#include <stdio.h>

int main(void) 
{ 
  float data[3] ;
  float temp ;
  int printf_tmp0 ;

  {
  temp = 0;
  printf("Please enter three numbers separated by spaces > ");
  scanf("%f%f%f", & data[1], & data[2], & data[3]);
  while (! (data[1] <= data[2] && data[2] <= data[3])) {
    if (data[2] <= data[1]) {
      temp = data[2];
      data[2] = data[1];
      data[1] = temp;
    }
printf("inputStart:temp:%f:float_VBC_printf_tmp0:%d:int_VBC_inputEnd", temp, printf_tmp0);
    if (data[3] <= data[2]) {
      temp = data[3];
      data[3] = data[2];
printf("outputStart:temp:%f:float_VBC_printf_tmp0:%d:int_VBC__nextloop_", temp, printf_tmp0);
      data[2] = data[3];
    }
  }
  printf("%.0f is the median\n", data[2]);
  return (0);
}
}

