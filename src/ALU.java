import java.math.BigInteger;

import org.xml.sax.ext.LexicalHandler;

//author:     FrankYao

public class ALU {
	public enum Operation {
		ADDITION, 
		SUBTRACTION, 
		MULTIPLICATION, 
		DIVISION
	};
	public enum Type {
		INTEGER, 
		FLOAT, 
		DECIMAL
	};
	
	public String LeftShift(String operand,int length){
				
		operand=operand.substring(length,operand.length());
		for(int i=0;i<length;i++)
			operand+="0";
		
		return operand;
	}
	
	public String RightShift(String operand,int length) {
		char[] c=operand.toCharArray();
		if(operand.length()>=length){
		operand=operand.substring(0, operand.length()-length);
		//System.out.println(operand);
		if(c[0]=='0'){
		for(int i=0;i<length;i++){
			
		   operand="0"+operand;
		   
		 	}
		}
	    if(c[0]=='1'){
			for(int i=0;i<length;i++){
				   operand="1"+operand;
				 }		
			}
		}
		else {
			System.out.println("Error");
			return null;
		}
			
		return operand;
	}
	
	public String Complement (String number, int length){
		int integer = Integer.parseInt(number);
		String result = "";
		int j=1;
		for (int i = length-1; i >= 0; i--) {
			if ((j & integer)!=0) {
				result = "1"+result;
			}else {
				result = "0"+result;
			}
			
			j*=2;
		}
		
		
		return result;
	}
	public String TrueValue (String operand,Type type,int[] length){
		if(type==Type.INTEGER ){
			char[] c=operand.toCharArray();
			int TrueValue=0;
			int j=1;
			if(c[0]=='0'){
				for(int i=operand.length()-1;i>=0;i--){
					if(c[i]=='1'){
						TrueValue+=j;}
					j*=2;
				}
			}
			else {
				operand=Negation(operand);
				c=operand.toCharArray();
				for(int i=operand.length()-1;i>=0;i--){
					if(c[i]=='1'){
						TrueValue+=j;}
					j*=2;
				}
				TrueValue+=1;
				TrueValue=-TrueValue;
			}
			return ""+TrueValue;
		}
		else {
			double TrueValue=0.0;
			String exponentString="0"+operand.substring(1,length[1]+1);
			int exponent=Integer.parseInt(TrueValue(exponentString, Type.INTEGER, length));
			String sString=operand.substring(length[1]+1);
			if(exponent==0){
				exponent=(int) -Math.pow(2, length[1])/2+2-length[0];
				int s=Integer.parseInt(TrueValue("0"+sString,Type.INTEGER,length));
				TrueValue=s*Math.pow(2, exponent);	
			}
			else if(exponent==(int)Math.pow(2, length[1])-1){
				exponent=exponent-(int) -Math.pow(2, length[1])/2+1-length[0];
				int s=Integer.parseInt(TrueValue("01"+sString,Type.INTEGER,length));
				if (s==Math.pow(2, length[0]+1)-1) {
					if(operand.charAt(0)=='0')
						return "������";
					else {
						return "������";
					}
				}
				else {
				     TrueValue=s*Math.pow(2, exponent);
				}
			}
			else {
				exponent=exponent-(int) Math.pow(2, length[1])/2+1-length[0];
				int s=Integer.parseInt(TrueValue("01"+sString,Type.INTEGER,length));
				TrueValue=s*Math.pow(2, exponent);
			}
			if (operand.charAt(0)=='1') {
				TrueValue=-TrueValue;
			}
			
			return ""+TrueValue;
		}
		
	}

	public String Negation (String operand){
		char[] c=operand.toCharArray();
		
		operand="";
		
		for(char c1:c){
			if(c1=='1')
				c1='0';
			else {
				c1='1';
			}
			operand+=c1;
		}
		

		return operand;
	}
	

	public String FloatRepresentation(String number,int sLength,int eLength){
		
		String result="";
		int maxExponent=(int) Math.pow(2, eLength);   //ָ�����ֵ
		double operand=Double.parseDouble(number);
		String symbolString="";  //����λ
		int exponent=-1;
		
		//��numberת��Ϊ��������ȷ������λ
		if(operand>0){
			symbolString="0";
		}

		else {
			symbolString="1";
			operand=-operand;
		}
		
		int operand1=(int)operand;
		double operand2=operand-operand1;
		
		//ȷ��С�����ֵĶ����Ʊ�ʾ
		String decimal="";
		for(int i=0;i<maxExponent/2+sLength-2;i++){
			operand2=operand2*2;
			if(operand2>=1){
				decimal+="1";
				operand2-=1;
			}
			else {
				decimal+="0";
			}
				
		}
		
		int copyOfoperand1=operand1;  //operand1�ĸ����������ų�operand1Ϊ0�����
		
		String integerString="";	
		
		do{
			integerString=""+operand1%2+integerString;
			operand1=operand1/2;
			exponent++;		
		}while(operand1!=0);
		
		if(copyOfoperand1==0){
			exponent--;
			exponent=exponent+(maxExponent/2)-1;
			while(decimal.charAt(0)!='1'&&exponent>0){
				exponent--;			
				decimal=decimal.substring(1);
			}
			
			
			if(exponent>0){
				String exponentString=Complement(""+exponent, eLength);
				result=symbolString+exponentString+decimal.substring(1,sLength+1);
			}
			else{
				String exponentString=Complement(""+exponent, eLength);
				result=symbolString+exponentString+decimal.substring(0,sLength);
			}
			
		}
		else {
			integerString+=decimal;
			String exponentString="";
			String sString="";
			exponent=exponent+(maxExponent/2)-1;
			if(exponent>maxExponent-1){
				for(int i=0;i<eLength;i++){
					exponentString+="1";
				}
				for(int i=0;i<sLength;i++){
					sString+="1";
				}
				result=symbolString+exponentString+sString;
			}
			else {
				exponentString=Complement(""+exponent, eLength);
				sString=integerString.substring(1,sLength+1);
				result=symbolString+exponentString+sString;
			}
		}
			
		//System.out.println(result.length());
		return result;
	}
	
	public String IEEE754(String number,int length){
		String resultString="";
		if(length==32){
			resultString=FloatRepresentation(number, 8, 23);
		}
		else {
			resultString=FloatRepresentation(number, 11, 52);
		}
		
		return resultString;
	}
	
	
	

	public String FullAdder (char x, char y, char c){
		String result= "";
		result += Integer.parseInt(x+"")^Integer.parseInt(y+"")^Integer.parseInt(c+"");
		result += (Integer.parseInt(x+"")&Integer.parseInt(y+""))|(Integer.parseInt(y+"")&Integer.parseInt(c+""))|(Integer.parseInt(x+"")&Integer.parseInt(c+""));
		return result;
	}
	
	public String CLAAdder (String operand1, String operand2, char c, int length){
		char[] x=operand1.toCharArray();
		char[] y=operand2.toCharArray();
		while(operand1.length()<length){
			operand1=x[0]+operand1;
		}
		x=operand1.toCharArray();
		
		while(operand2.length()<length){
			operand2=y[0]+operand2;
		}
	    y=operand2.toCharArray();
		
		int[] P=new int[length];
		int[] G=new int[length];
		int[] C=new int[length+1];
		//char[] cChar=new char[length+1];
		
		C[0]=Integer.parseInt(c+"");
		//cChar[0]=(char)C[0];
				
		for(int i=length-1;i>=0;i--){      
			String xString=""+x[i];
			String yString=""+y[i];
			P[length-1-i]=Integer.parseInt(xString)|Integer.parseInt(yString);
			G[length-1-i]=Integer.parseInt(xString)&Integer.parseInt(yString);
			
		}
		
		switch (length) {
		case 8:
			C[8] = (char)((G[7])|(P[7]&G[6])|(P[7]&P[6]&G[5])|(P[7]&P[6]&P[5]&G[4])|(P[7]&P[6]&P[5]&P[4]&G[3])|(P[7]&P[6]&P[5]&P[4]&P[3]&G[2])|(P[7]&P[6]&P[5]&P[4]&P[3]&P[2]&G[1])|(P[7]&P[6]&P[5]&P[4]&P[3]&P[2]&P[1]&G[0])|(P[7]&P[6]&P[5]&P[4]&P[3]&P[2]&P[1]&P[0]&C[0]));
			
        case 7:
        	C[7] = (char)((G[6])|(P[6]&G[5])|(P[6]&P[5]&G[4])|(P[6]&P[5]&P[4]&G[3])|(P[6]&P[5]&P[4]&P[3]&G[2])|(P[6]&P[5]&P[4]&P[3]&P[2]&G[1])|(P[6]&P[5]&P[4]&P[3]&P[2]&P[1]&G[0])|(P[6]&P[5]&P[4]&P[3]&P[2]&P[1]&P[0]&C[0]));
			
        case 6:
        	C[6] = (char)((G[5])|(P[5]&G[4])|(P[5]&P[4]&G[3])|(P[5]&P[4]&P[3]&G[2])|(P[5]&P[4]&P[3]&P[2]&G[1])|(P[5]&P[4]&P[3]&P[2]&P[1]&G[0])|(P[5]&P[4]&P[3]&P[2]&P[1]&P[0]&C[0]));
			
        case 5:
        	C[5] = (char)((G[4])|(P[4]&G[3])|(P[4]&P[3]&G[2])|(P[4]&P[3]&P[2]&G[1])|(P[4]&P[3]&P[2]&P[1]&G[0])|(P[4]&P[3]&P[2]&P[1]&P[0]&C[0]));
			
        case 4:
			
        	C[4] = (char)((G[3])|(P[3]&G[2])|(P[3]&P[2]&G[1])|(P[3]&P[2]&P[1]&G[0])|(P[3]&P[2]&P[1]&P[0]&C[0]));
        case 3:
			
        	C[3] = (char)(G[2]|(P[2]&G[1])|(P[2]&P[1]&G[0])|(P[2]&P[1]&P[0]&C[0]));
        case 2:
			
        	C[2] = (char)(G[1]|(P[1]&G[0])|(P[1]&P[0]&C[0]));
        case 1:
        	
        	C[1] = (char) ((P[0]&C[0])|G[0]);
			break;

		default:		
			System.out.println("Error!");
			return null;
		}
		
		String result="";
		char[] s= new char[length];
		for (int i = 0; i < length; i++) {
			s[i] = FullAdder(x[length-i-1], y[length-i-1], (char)(C[i]+'0')).charAt(0);
		}
		for (int i = 1; i < length+1; i++) {
			result += "" + s[length-i];
		}
		
		result+=""+C[length];
		return result;
	}
	
	public String Addition (String operand1, String operand2, char c, int length){
		char[] x=operand1.toCharArray();
		char[] y=operand2.toCharArray();
		while(operand1.length()<length){
			operand1=x[0]+operand1;
		}
		x=operand1.toCharArray();
		
		while(operand2.length()<length){
			operand2=y[0]+operand2;
		}
		y=operand2.toCharArray();
		
		int i=length;
		String result="";
		while(i>=8){
			result=CLAAdder(operand1.substring(i-8,i), operand2.substring(i-8,i), c, 8).substring(0,8)+result;
			c=CLAAdder(operand1.substring(i-8,i), operand2.substring(i-8,i), c, 8).charAt(8);
			i-=8;
		}
		if(i<8&&i!=0){
			result=CLAAdder(operand1.substring(0,i), operand2.substring(0,i), c, i).substring(0,i)+result;
		}
		
		if(x[0]=='1'&&y[0]=='1'&&result.charAt(0)=='0'){
			result+="1";
			System.out.println("Out of range!Error!");
		}	
		else if(x[0]=='0'&&y[0]=='0'&&result.charAt(0)=='1'){
			System.out.println("Out of range!Error!");
			result+="1";
		}
		else {
			result+="0";
		}
		
		
		return result;
	}
	
	public String AdditionF(String operand1,String operand2,int sLength,int eLength,int gLength){
		int[] length=new int[3];
		String exponentString1="0"+operand1.substring(1,eLength+1);
		int exponent1=Integer.parseInt(TrueValue(exponentString1, Type.INTEGER, length));
		String sString1=operand1.substring(eLength+1);
		String exponentString2="0"+operand2.substring(1,eLength+1);
		int exponent2=Integer.parseInt(TrueValue(exponentString2, Type.INTEGER, length));
		String sString2=operand2.substring(eLength+1);
		for(int i=0;i<gLength;i++){
			sString1+="0";
			sString2+="0";
		}
		
		String result="";
		String bigSString="";
		int bigExponent=0;
		int smallExponent=0;
		String smallSString="";
		String outRange="0";
		char symbol=operand1.charAt(0);
		if(exponent1!=exponent2){
			if(exponent1>exponent2){
				bigExponent=exponent1;
				bigSString=sString1;
				smallExponent=exponent2;
				smallSString=sString2;
				symbol=operand1.charAt(0);
					}
			else {
				bigExponent=exponent2;
				bigSString=sString2;
				smallExponent=exponent1;
				smallSString=sString1;
				symbol=operand2.charAt(0);
			}
				if(smallExponent==0){
					smallSString=RightShift("0"+smallSString, bigExponent-smallExponent-1);
					 if(operand1.charAt(0)==operand2.charAt(0)){
						 result=Addition("01"+bigSString, smallSString, '0', sLength+gLength+2);
						  result=result.substring(2);
						 if(result.charAt(result.length()-1)=='1'){
							 result.substring(0,result.length()-1);
							 result=RightShift(result, 1);
							 bigExponent++;
						 }
						 if(result.charAt(sLength+1)=='1'){
							  result=Addition("0"+result.substring(0, sLength), "01", '0', sLength+1);
							  result=result.substring(1);
								 if(result.charAt(result.length()-1)=='1'){
									// result.substring(0,result.length()-1);
									 result=RightShift(result, 1);
									 bigExponent++;
								 }
						 }
						 result=result.substring(0,sLength);
						 if(bigExponent>Math.pow(2, eLength)-1){
							 outRange="1";
						 }
						 exponentString1=Complement(""+bigExponent, eLength+1).substring(1);
						 result=symbol+exponentString1+result+outRange;
					 }
					 else {
						result=Subtraction("01"+bigSString, smallSString, sLength+gLength+2);
						if(result.charAt(1)=='1'){
							result=result.substring(2);
							 if(result.charAt(sLength+1)=='1'){
								  result=Addition("0"+result.substring(0, sLength), "01", '0', sLength+1).substring(1,sLength+1);}
							 else{
								 result=result.substring(0,sLength);
							 }
						}
						else {
							result=result.substring(1,result.length()-1);
							while(bigExponent>=0&&(result.charAt(0)=='0')){
								bigExponent--;
								result=RightShift(result, 1);
							}
							result=result.substring(1);
							 if(result.charAt(sLength+1)=='1'){
								  result=Addition("0"+result.substring(0, sLength), "01", '0', sLength+1).substring(1,sLength+1);}
							 else {
								result=result.substring(0,sLength);
							}
						}
						exponentString1=Complement(""+bigExponent, eLength+1).substring(1);
						result=operand1.charAt(0)+exponentString1+result+outRange;
					}
				}
			else {
				smallSString=RightShift("01"+smallSString, bigExponent-smallExponent);
				 if(operand1.charAt(0)==operand2.charAt(0)){
					 result=Addition("01"+bigSString, smallSString, '0', sLength+gLength+2);
					  result=result.substring(2);
					 if(result.charAt(result.length()-1)=='1'){
						 result=RightShift(result, 1);
						 bigExponent++;
					 }
					 if(result.charAt(sLength+1)=='1'){
						  result=Addition("0"+result.substring(0, sLength), "01", '0', sLength+1);
						  result=result.substring(1);
							 if(result.charAt(result.length()-1)=='1'){
								 //result.substring(0,result.length()-1);
								 result=RightShift(result, 1);
								 bigExponent++;
							 }
					 }
					 result=result.substring(0,sLength);
					 if(bigExponent>Math.pow(2, eLength)-1){
						 outRange="1";
					 }
					 exponentString1=Complement(""+bigExponent, eLength+1).substring(1);
					 result=symbol+exponentString1+result+outRange;
				 }
				 else {
					result=Subtraction("01"+bigSString, smallSString, sLength+gLength+2);
					if(result.charAt(1)=='1'){
						result=result.substring(2);
						 if(result.charAt(sLength+1)=='1'){
							  result=Addition("0"+result.substring(0, sLength), "01", '0', sLength+1);
							  result=result.substring(1);
								 if(result.charAt(result.length()-1)=='1'){
									 //result.substring(0,result.length()-1);
									 result=RightShift(result, 1);
									 bigExponent++;
								 }
						 }
					}
					else {
						result=result.substring(1,result.length()-1);
						while(bigExponent>=0&&(result.charAt(0)=='0')){
							bigExponent--;
							result=LeftShift(result, 1);
						}
						result=result.substring(1);
						 if(result.charAt(sLength+1)=='1'){
							  result=Addition("0"+result.substring(0, sLength), "01", '0', sLength+1);
							  result=result.substring(1);
								 if(result.charAt(result.length()-1)=='1'){
									 //result.substring(0,result.length()-1);
									 result=RightShift(result, 1);
									 bigExponent++;
								 }
						 }
					}
					
					result=result.substring(0,sLength);
					
					exponentString1=Complement(""+bigExponent, eLength+1).substring(1);
					result=symbol+exponentString1+result+outRange;
				}	
				}
		}
		else {
			if (exponent1==0) {
				if(operand1.charAt(0)==operand2.charAt(0)){
					result=Addition("0"+sString1, "0"+sString2, '0', sLength+gLength+1);
					if (result.charAt(result.length()-1)=='1') {
						exponent1++;
					}
					result=result.substring(1);
					 if(result.charAt(sLength+1)=='1'){
						  result=Addition("0"+result.substring(0, sLength), "01", '0', sLength+1);
						  result=result.substring(1);
							 if(result.charAt(result.length()-1)=='1'){
								 //result.substring(0,result.length()-1);
								 result=RightShift(result, 1);
								 exponent1++;
							 }
					 }
					 result=result.substring(0,sLength);
					exponentString1=Complement(""+exponent1, eLength+1).substring(1);
					result=symbol+exponentString1+result+outRange;
				}
				else {
					result=Subtraction("0"+sString1,"0"+sString2 , sLength+gLength+1);
					if(result.charAt(0)=='1'){
						result=result.substring(0,result.length()-1);
						result=Negation(result);
						result=Addition(result, "01", '0', result.length()).substring(0,result.length());
						symbol=operand2.charAt(0);
					}
					result=result.substring(1);
					if(result.charAt(sLength+1)=='1'){
						  result=Addition("0"+result.substring(0, sLength), "01", '0', sLength+1);
						  result=result.substring(1);
							 if(result.charAt(result.length()-1)=='1'){
								 //result.substring(0,result.length()-1);
								 result=RightShift(result, 1);
								 exponent1++;
							 }
					 }
					result=result.substring(0,sLength);
					exponentString1=Complement(""+exponent1, eLength+1).substring(1);
					result=symbol+exponentString1+result;
				}
				
			}
			else {
				if(operand1.charAt(0)==operand2.charAt(0)){
					result=Addition("01"+sString1, "01"+sString2, '0', sLength+gLength+2);
					if(exponent1==Math.pow(2, eLength)-1){
						for(int i=0;i<sLength+gLength;i++){
							result+="1";
						}
						outRange="1";
						result=symbol+result+outRange;
						return result;
					}
					exponent1++;
					result.substring(1);
					if(result.charAt(sLength+1)=='1'){
						  result=Addition("0"+result.substring(0, sLength), "01", '0', sLength+1);
						  result=result.substring(1);
							 if(result.charAt(result.length()-1)=='1'){
								 //result.substring(0,result.length()-1);
								 result=RightShift(result, 1);
								exponent1++;
							 }
					 }
					result=result.substring(0,sLength);
					exponentString1=Complement(""+exponent1, eLength+1).substring(1);
					result=symbol+exponentString1+result;
				
				}
				else {
					result=Subtraction("01"+sString1, "01"+sString2, sLength+gLength+2);
					if(result.charAt(0)=='1'){
						result=result.substring(0,result.length()-1);
						result=Negation(result);
						result=Addition(result, "01", '0', result.length()).substring(0,result.length());
						symbol=operand2.charAt(0);
					}
					result=result.substring(1);
					while(exponent1>=0&&(result.charAt(0)=='0')){
						exponent1--;
						result=LeftShift(result, 1);
					}
					result=result.substring(1);
					 if(result.charAt(sLength+1)=='1'){
						  result=Addition("0"+result.substring(0, sLength), "01", '0', sLength+1);
						  result=result.substring(1);
							 if(result.charAt(result.length()-1)=='1'){
								 //result.substring(0,result.length()-1);
								 result=RightShift(result, 1);
								 exponent1++;
							 }
					 }
				    result=result.substring(0,sLength);
					exponentString1=Complement(""+exponent1, eLength+1).substring(1);
					result=symbol+exponentString1+result+outRange;
				}

			}
			

		}
		
		return result;
		
	}
	
	public String Subtraction (String operand1, String operand2, int length){
	

		char[] x=operand1.toCharArray();
		char[] y=operand2.toCharArray();
		while(operand1.length()<length){
			operand1=x[0]+operand1;
		}
		x=operand1.toCharArray();
		
		while(operand2.length()<length){
			operand2=y[0]+operand2;
		}
		y=operand2.toCharArray();
		
		operand2=Negation(operand2);
		operand2=Addition(operand2, "01", '0', length).substring(0, length);
		
		String result=""+Addition(operand1, operand2, '0', length);
		
		return result;
	}
	
	public String SubtractionF(String operand1,String operand2,int sLength,int eLength,int gLength){
		if(operand2.charAt(0)=='0'){
			operand2=operand2.substring(1);
			operand2="1"+operand2;
		}
		else {
			operand2=operand2.substring(1);
			operand2="0"+operand2;
		}
		
		return AdditionF(operand1, operand2, sLength, eLength, gLength);
	}
	
	public String Multiplication (String operand1, String operand2, int length){
		char[] x=operand1.toCharArray();	
		char[] y=operand2.toCharArray();
		while(operand1.length()<length){
			operand1=x[0]+operand1;
		}
				
				
		while(operand2.length()<length){
			operand2=y[0]+operand2;
		}
		
		String result="";

		for(int i=0;i<length;i++){
			result+="0";

		}
		

		result+=operand2;
		result+="0";

		
		for(int i=0;i<length;i++){	
						
			char[] operand=result.toCharArray();
			if((operand[2*length]-operand[2*length-1]==0)){
				result=RightShift(result, 1);
			}
			else if((operand[2*length]-operand[2*length-1]==1)){
				String tempString=result.substring(length,result.length());
				result=Addition(operand1, result.substring(0, length), '0', length).substring(0,length)+tempString;				
				result=RightShift(result, 1);
			}
			else {
				String tempString=result.substring(length,result.length());
				result=Subtraction(result.substring(0, length),operand1, length).substring(0,length)+tempString;
				result=RightShift(result, 1);
			}
			
		
			
		}
		return result.substring(0,2*length);
	}
	
	public String MultiplicationF(String operand1,String operand2,int sLength,int eLength){
		String result="";
		int[] length=new int[3];
		String exponentString="";
		String offsetString="0";
		String exponentString1="0"+operand1.substring(1,eLength+1);
		String sString1=operand1.substring(eLength+1);
		String exponentString2="0"+operand2.substring(1,eLength+1);
		String sString2=operand2.substring(eLength+1);
		char symbol='0';
		String zero="0";
		String infinite="";
		if(operand1.charAt(0)!=operand2.charAt(0))
			symbol='1';
		for(int i=0;i<sLength+eLength;i++){
			zero+="0";
			infinite+="1";
		}
		infinite=symbol+infinite;
		for(int i=0;i<eLength-1;i++){
			offsetString+="1";
		}
		if(operand1.substring(1).equals(zero.substring(1))||operand2.substring(1).equals(zero.substring(1)))
			return zero;
		else {
			exponentString=Addition("0"+exponentString1, "0"+exponentString2, '0', eLength+2).substring(0, eLength+2);
			exponentString=Subtraction(exponentString, offsetString, eLength+2).substring(0,eLength+2);
			if(exponentString.charAt(0)=='1'){
				return zero;
			}
			else if(exponentString.charAt(1)=='1'){
				return infinite;
			}
						
			else {
				int exponent=Integer.parseInt(TrueValue(exponentString, Type.INTEGER, length));
				if(operand1.substring(1,eLength+1).equals(zero.substring(0,eLength))&&!operand2.substring(1,eLength+1).equals(zero.substring(0,eLength))){
					sString1="0"+sString1+"0";
					sString2="01"+sString2;
					while(sString1.charAt(1)!='1'){
						sString1=LeftShift(sString1, 1);
						exponent--;
					}
					
					result=Multiplication(sString1, sString2, sLength+2);
					if(result.charAt(2)=='1'){
						exponent++;
						if(exponent>0){
							result=result.substring(3);
							if(result.charAt(sLength+1)=='1'){
								  result=Addition("0"+result.substring(0, sLength), "01", '0', sLength+1);
								  result=result.substring(1);
									 if(result.charAt(result.length()-1)=='1'){
										
										 result=RightShift(result, 1);
										 exponent++;
									 }
							 }
						    result=result.substring(0,sLength);
							exponentString=Complement("0"+exponent, eLength+1).substring(1);
							result=symbol+exponentString+result;
							return result;
						}
						else {
							result=RightShift(result, 1);
							
						}
					}
					result=result.substring(2);
					while(exponent<0){
						exponent++;
						result=RightShift(result, 1);
					}
					result=result.substring(1);
					if(result.charAt(sLength+1)=='1'){
						  result=Addition("0"+result.substring(0, sLength), "01", '0', sLength+1);
						   result=result.substring(1);
							 if(result.charAt(result.length()-1)=='1'){
								
								 result=RightShift(result, 1);
								 exponent++;
							 }
					 }
				    result=result.substring(0,sLength);
					exponentString=Complement("0"+exponent, eLength+1).substring(1);
					result=symbol+exponentString+result;
					
				}
				else if(!operand1.substring(1,eLength+1).equals(zero.substring(0,eLength))&&operand2.substring(1,eLength+1).equals(zero.substring(0,eLength))){
					sString2="0"+sString2+"0";
					sString1="01"+sString1;
					while(sString2.charAt(1)!='1'){
						sString2=LeftShift(sString2, 1);
						exponent--;
					}
					
					result=Multiplication(sString1, sString2, sLength+2);
					if(result.charAt(2)=='1'){
						exponent++;
						if(exponent>0){
							result=result.substring(3);
							if(result.charAt(sLength+1)=='1'){
								  result=Addition("0"+result.substring(0, sLength), "01", '0', sLength+1);
								  result=result.substring(1);
									 if(result.charAt(result.length()-1)=='1'){									
										 result=RightShift(result, 1);
										 exponent++;
									 }
							 }
						    result=result.substring(0,sLength);
							exponentString=Complement("0"+exponent, eLength+1).substring(1);
							result=symbol+exponentString+result;
							return result;
						}
						else {
							result=RightShift(result, 1);
							
						}
					}
					result=result.substring(2);
					while(exponent<0){
						exponent++;
						result=RightShift(result, 1);
					}
					result=result.substring(1);
					if(result.charAt(sLength+1)=='1'){
						  result=Addition("0"+result.substring(0, sLength), "01", '0', sLength+1);
						  result=result.substring(1);
							 if(result.charAt(result.length()-1)=='1'){									
								 result=RightShift(result, 1);
								 exponent++;
							 }
					 }
				    result=result.substring(0,sLength);
					exponentString=Complement("0"+exponent, eLength+1).substring(1);
					result=symbol+exponentString+result;
					
				}
				else{
					sString1="01"+sString1;
					sString2="01"+sString2;
					result=Multiplication(sString1, sString2, sLength+2);
					if(result.charAt(2)=='1'){
						exponent++;
						result=result.substring(3);
						if(result.charAt(sLength+1)=='1'){
							  result=Addition("0"+result.substring(0, sLength), "01", '0', sLength+1);
							  result=result.substring(1);
								 if(result.charAt(result.length()-1)=='1'){									
									 result=RightShift(result, 1);
									 exponent++;
								 }
						 }
					    result=result.substring(0,sLength);
						
					}
					else {
						result=result.substring(4);
						if(result.charAt(sLength+1)=='1'){
							  result=Addition("0"+result.substring(0, sLength), "01", '0', sLength+1);
							  result=result.substring(1);
								 if(result.charAt(result.length()-1)=='1'){									
									 result=RightShift(result, 1);
									 exponent++;
								 }
						 }
					    result=result.substring(0,sLength);
						
					}
					if(exponent>Math.pow(2, eLength)-1){
						return infinite;
					}
					exponentString=Complement("0"+exponent, eLength+1).substring(1);
					result=symbol+exponentString+result;
						
				}
				
			}
		}
		
		
		return result;
	}
	
	public String Division (String operand1, String operand2, int length){
		char[] x=operand1.toCharArray();	
		char[] y=operand2.toCharArray();
		String zero="";
		for(int i=0;i<length;i++){
			zero+="0";
		}
		String remainder="";
		if(operand2.equals(zero)){
			return "NaN";
		}
		else if (operand1.equals(zero)) {
			return zero+operand1;
		}
		else {
	
		
		while(operand1.length()<length){
			operand1=x[0]+operand1;
		}
		while(operand2.length()<length){
			operand2=y[0]+operand2;
		}
		String  dividenedString=operand1;
		x=operand1.toCharArray();
		y=operand2.toCharArray();
	
			for(int i=0;i<length;i++){
				remainder+=x[0];
			}
			
		String result=remainder+operand1;
		y=operand2.toCharArray();
		
		for(int i=0;i<length;i++){
			
			
			if(remainder.charAt(0)==y[0]){
				remainder=Subtraction(remainder, operand2, length).substring(0,length);
			}
			else{
				remainder=Addition(remainder, operand2, '0', length).substring(0,length);
			}
			result=remainder+operand1;
			result=LeftShift(result, 1);
			if(remainder.charAt(0)==y[0]){		
				result=Addition(result, "01", '0', 2*length).substring(0,2*length);
	
			}
			
                remainder=result.substring(0,length);
				operand1=result.substring(length,2*length);
		}
		
		if(remainder.charAt(0)==y[0]){
			remainder=Subtraction(remainder, operand2, length).substring(0,length);
		}
		else{
			remainder=Addition(remainder, operand2, '0', length).substring(0,length);
		}
		
		result=remainder+operand1;
		operand1=result.substring(length,2*length);	
		
		String qn="";
		if(remainder.charAt(0)==operand2.charAt(0))
			qn ="01";
		else {
			qn="0";
		}
		if(dividenedString.charAt(0)!=remainder.charAt(0)){
			if(dividenedString.charAt(0)!=operand2.charAt(0)){
					remainder=Subtraction(remainder, operand2, length).substring(0,length);
			}
			else{
					remainder=Addition(remainder, operand2, '0', length).substring(0,length);
			}
		}	


		
		operand1=LeftShift(operand1, 1);
		operand1=Addition(operand1, qn, '0', length).substring(0,length);
		
		if(dividenedString.charAt(0)!=operand2.charAt(0))
			operand1=Addition(operand1, "01", '0', length).substring(0,length);
		
		
		}
		
		
		return remainder+operand1;
	}
	
	public String DivisionF(String operand1,String operand2,int sLength,int eLength){
		String result="";
		int[] length=new int[3];
		String exponentString="";
		String offsetString="0";
		String exponentString1="0"+operand1.substring(1,eLength+1);
		String sString1=operand1.substring(eLength+1);
		String exponentString2="0"+operand2.substring(1,eLength+1);
		String sString2=operand2.substring(eLength+1);
		char symbol='0';
		String zero="0";
		String infinite="";
		if(operand1.charAt(0)!=operand2.charAt(0))
			symbol='1';
		for(int i=0;i<sLength+eLength;i++){
			zero+="0";
			infinite+="1";
		}
		for(int i=0;i<eLength-1;i++){
			offsetString+="1";
		}
		infinite=symbol+infinite;
		if(operand1.substring(1).equals(zero.substring(1)))
			return zero;
		else if(operand2.substring(1).equals(zero.substring(1)))
			return "NaN";
		else {
			exponentString=Subtraction("0"+exponentString1, "0"+exponentString2,  eLength+2).substring(0, eLength+2);
			exponentString=Addition(exponentString, offsetString, '0',eLength+2).substring(0,eLength+2);
			if(exponentString.charAt(0)=='1'){
				return zero;
			}
			else if(exponentString.charAt(1)=='1'){
				return infinite;
			}
			else {
				int exponent=Integer.parseInt(TrueValue(exponentString, Type.INTEGER, length));
				if(operand1.substring(1,eLength+1).equals(zero.substring(0,eLength))&&operand2.substring(1,eLength+1).equals(zero.substring(0,eLength))){
					sString1="0"+sString1+"0"+zero.substring(0,sLength);
					sString2="0"+sString2+"0";
					result=Division(sString1, sString2, 2*sLength+2).substring(2*sLength+2,4*sLength+4);
					int i=0;
					while (result.charAt(0)=='0') {
					 result=LeftShift(result, 1);
						i++;
					}
					exponent=exponent-(i-sLength-1);
					
					result=result.substring(1);
					if(result.charAt(sLength+1)=='1'){
						  result=Addition("0"+result.substring(0, sLength), "01", '0', sLength+1);
						  result=result.substring(1);
							 if(result.charAt(result.length()-1)=='1'){									
								 result=RightShift(result, 1);
								 exponent++;
							 }
					 }
				    result=result.substring(0,sLength);
					exponentString=Complement("0"+exponent, eLength+1).substring(1);
					result=symbol+exponentString+result;
						
				}
				else if (!operand1.substring(1,eLength+1).equals(zero.substring(0,eLength))&&operand2.substring(1,eLength+1).equals(zero.substring(0,eLength))) {
					sString1="01"+sString1+zero.substring(0,sLength);
					sString2="0"+sString2+"0";
					result=Division(sString1, sString2, 2*sLength+2).substring(2*sLength+2,4*sLength+4);
					int i=0;
					while (result.charAt(0)=='0') {
					 result=LeftShift(result, 1);
						i++;
					}
					exponent=exponent-(i-sLength-1);
					
					result=result.substring(1);
					if(result.charAt(sLength+1)=='1'){
						  result=Addition("0"+result.substring(0, sLength), "01", '0', sLength+1);
						  result=result.substring(1);
							 if(result.charAt(result.length()-1)=='1'){									
								 result=RightShift(result, 1);
								 exponent++;
							 }
					 }
					if(exponent>Math.pow(2, eLength)-1){
						return infinite;
					}
				    result=result.substring(0,sLength);
					exponentString=Complement("0"+exponent, eLength+1).substring(1);
					result=symbol+exponentString+result;
						
				}
				else if (operand1.substring(1,eLength+1).equals(zero.substring(0,eLength))&&!operand2.substring(1,eLength+1).equals(zero.substring(0,eLength))) {
					sString1="0"+sString1+"0"+zero.substring(0,sLength);
					sString2="01"+sString2;
					result=Division(sString1, sString2, 2*sLength+2).substring(2*sLength+2,4*sLength+4);
					int i=0;
					while (result.charAt(0)=='0'&&exponent-(i-sLength-1)!=0) {
					 result=LeftShift(result, 1);
						i++;
					}
					exponent=exponent-(i-sLength-1);
					if(exponent>0){
						result=result.substring(1);
						if(result.charAt(sLength+1)=='1'){
							  result=Addition("0"+result.substring(0, sLength), "01", '0', sLength+1);
							  result=result.substring(1);
								 if(result.charAt(result.length()-1)=='1'){									
									 result=RightShift(result, 1);
									 exponent++;
								 }
						 }
					    //result=result.substring(0,sLength);
					}
					
					result=result.substring(0,sLength);
					exponentString=Complement("0"+exponent, eLength+1).substring(1);
					result=symbol+exponentString+result;
						
				}
				else {
					sString1="01"+sString1+zero.substring(0,sLength);
					sString2="01"+sString2;
					result=Division(sString1, sString2, 2*sLength+2).substring(2*sLength+2,4*sLength+4);
					int i=0;
					while (result.charAt(0)=='0'&&exponent-(i-sLength-1)!=0) {
					 result=LeftShift(result, 1);
						i++;
					}
					exponent=exponent-(i-sLength-1);
					if(exponent>0){
						result=result.substring(1);
						if(result.charAt(sLength+1)=='1'){
							  result=Addition("0"+result.substring(0, sLength), "01", '0', sLength+1);
							  result=result.substring(1);
								 if(result.charAt(result.length()-1)=='1'){									
									 result=RightShift(result, 1);
									 exponent++;
								 }
						 }
					    //result=result.substring(0,sLength);
					}
					if(exponent>Math.pow(2, eLength)-1){
						return infinite;
					}
					result=result.substring(0,sLength);
					exponentString=Complement("0"+exponent, eLength+1).substring(1);
					result=symbol+exponentString+result;
				}
			}
		}
		return result;
	}
	
	
	public String Calculation (String number1, String number2,Type type, Operation operation, int[] length){
		if(type==Type.INTEGER){
		String operand1=Complement(number1, length[0]);
		String operand2=Complement(number2, length[0]);
		String result="";
		if(operation==Operation.ADDITION){
			result=(Addition(operand1, operand2, '0', length[0]));
			if(result.charAt(length[0])=='1'){
				return "���";
			}
			else {			
				return TrueValue(result.substring(0,length[0]),Type.INTEGER,length);
			}
		}
		else if (operation==Operation.SUBTRACTION) {
			result=Subtraction(operand1, operand2, length[0]);
			if(result.charAt(length[0])=='1'){
				return "���";
			}
			else {
				return TrueValue(result.substring(0,length[0]),type.INTEGER,length);
			}
		}
		else if (operation==Operation.MULTIPLICATION) {

			return TrueValue(Multiplication(operand1, operand2, length[0]),Type.INTEGER,length);
		}
		else if(operation==Operation.DIVISION){
			result=Division(operand1, operand2, length[0]);
			if (result.charAt(0)=='N') {
				return result;
			}
			return TrueValue(Division(operand1, operand2, length[0]).substring(length[0], 2*length[0]),type.INTEGER,length);
		}
		
		}
		else{
			String operand1=FloatRepresentation(number1, length[0],length[1]);
			String operand2=FloatRepresentation(number2, length[0], length[1]);
			String result="";
			if(operation==Operation.ADDITION){
				result=AdditionF(operand1, operand2, length[0], length[1], length[2]);
				if(result.charAt(result.length()-1)=='1'){
					return "���";
				}
				else {
					return  TrueValue(result.substring(0,result.length()-1), Type.FLOAT, length);
				}
			}
			else if (operation==Operation.SUBTRACTION) {
				result=SubtractionF(operand1, operand2, length[0], length[1], length[2]);
				if(result.charAt(result.length()-1)=='1'){
					return "���";
				}
				else {
					return TrueValue(result.substring(0,result.length()-1), Type.FLOAT, length);
				}
			}
			else if (operation==Operation.MULTIPLICATION) {
				return TrueValue(MultiplicationF(operand1, operand2, length[0], length[1]), Type.FLOAT, length);
			}
			else {
				result=DivisionF(operand1, operand2, length[0], length[1]);
				if(result.charAt(0)=='N'){
					return result;
				}
				else {
					return TrueValue(result, Type.FLOAT, length);
				}
			}
			
			
		}
		
		return "I can't caculate it now.";
	}
	
	public static void main(String[] args) {
		ALU alu=new ALU();
		int[] length=new int[]{23,8,0};
		//System.out.println(alu.CLAAdder("11001", "0100", '0', 8));
		double test=0.475;
	
//		System.out.println(alu.Calculation("4", "2", Type.INTEGER, Operation.DIVISION, length));
//		System.out.println(alu.Division("0100", "0010", 8));
		System.out.println(alu.AdditionF("00111111000000000000000000000000",
				"10111110111000000000000000000000", 23, 8, 20));
		System.out.println(alu.MultiplicationF("00111110111000000", "00111111000000000", 8, 8));
		System.out.println(alu.DivisionF("00111110111000000", "00111111000000000", 8, 8));
	}

}