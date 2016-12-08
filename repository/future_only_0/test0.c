int test(int b, int c, int a, int printf_tmp0){
  if ((a >= b && b >= c) || (c >= b && b >= a)) {
    printf_tmp0 = b;
  } else if ((b >= a && a >= c) || (c >= a && a >= b)) {
    printf_tmp0 = a;
  } else if ((a >= c && c >= b) || (b >= c && c >= a)) {
    printf_tmp0 = c;
  }
}
