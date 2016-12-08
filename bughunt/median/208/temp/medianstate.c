/**/

#include <stdio.h>

int main(){

	int n1, n2, n3, small;

	printf("Please enter 3 numbers separated by spaces > ");
	scanf("%d%d%d", &n1, &n2, &n3);
	if (n1 <= n2){
		small = n1;
		if (small > n3)
			printf("%d is the median\n", n1);
		
		else if (n3 > n2)
			printf("%d is the median\n", n2);

		else
			printf("%d is the median\n", n3);
}
	else{
		small = n2;
printf("inputStart:small:%d:int_VBC_n1:%d:int_VBC_n2:%d:int_VBC_n3:%d:int_VBC_inputEnd", small, n1, n2, n3);
		if (small > n3)
			printf("%d is the median\n", n2);

		else if (n3 > n1)
                        printf("%d is the median\n", n1);

                else
                        printf("%d IS the median\n", n3);
}
printf("outputStart:small:%d:int_VBC_n1:%d:int_VBC_n2:%d:int_VBC_n3:%d:int_VBC__nextloop_", small, n1, n2, n3);
//**/
	return 0;
}
