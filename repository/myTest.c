void test(int x, int y){
	int i = 0;
	if (x > y){
		if(x > 5) {
			return x+2;
		}
		else{
			i = 5;
		}
	}else{
		if(y < 10){
			i = (i + x) - y;
		}else{ 
			return y;
		}
	}
	return i;
}

