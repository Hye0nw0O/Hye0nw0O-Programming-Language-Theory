import java.io.*;

class Calc {
    int token; int value; int ch;
    private PushbackInputStream input;
    final int NUMBER=256;

    Calc(PushbackInputStream is) {
        input = is;
    }

    int getToken( )  { /* tokens are characters */
        while(true) {
            try  {
	            ch = input.read();
                if (ch == ' ' || ch == '\t' || ch == '\r') ;
                else 
                    if (Character.isDigit(ch)) {
                        value = number( );
	               input.unread(ch);
		     return NUMBER;
	          }
	          else return ch;
	  } catch (IOException e) {
                System.err.println(e);
            }
        }
    }

    private int number( )  {
    /* number -> digit { digit } */
        int result = ch - '0';
        try  {
            ch = input.read();
            while (Character.isDigit(ch)) {
                result = 10 * result + ch -'0';
                ch = input.read(); 
            }
        } catch (IOException e) {
            System.err.println(e);
        }
        return result;
    }

    void error( ) {
        System.out.printf("parse error : %d\n", ch);
        //System.exit(1);
    }

    void match(int c) { 
        if (token == c) 
	    token = getToken();
        else error();
    }

    void command( ) {
    /* command -> expr '\n' */
//        int result = aexp();		// TODO: [Remove this line!!]
        Object result = expr();  // TODO: [Use this line for solution]
        if (token == '\n') /* end the parse and print the result */
	    System.out.println(result);
        else error();
    }
    
    Object expr() {
    /* <expr> -> <bexp> {& <bexp> | '|'<bexp>} | !<expr> | true | false */
    	Object result;
//    	result = ""; // TODO: [Remove this line!!]
    	if (token == '!'){
    		// !<expr>
    		match('!');
    		result = !(boolean) expr();
    	}
    	else if (token == 't'){
    		// true
    		match('t');
    		result = (boolean)true;
    	}
    	else if (token == 'f'){
    		// false
    		// TODO: [Fill in your code here]
            match('f');
            result = (boolean)false;
    	}
    	else {
    		/* <bexp> {& <bexp> | '|'<bexp>} */
    		result = bexp();
    		while (token == '&' || token == '|') {
    			if (token == '&'){
    				// TODO: [Fill in your code here]
                    match('&');
                    result = (boolean)bexp() && (boolean)result;
    			} else if (token == '|'){
    				// TODO: [Fill in your code here]
                    match('|');
                    result = (boolean)bexp() || (boolean)result;
                }
    		}
    	}
    	return result;
	}

    Object bexp( ) {
    /* <bexp> -> <aexp> [<relop> <aexp>] */
    	Object result = null;
//    	result = ""; // TODO: [Remove this line!!]
    	int aexp1 = aexp();
    	if (token == '<' || token == '>' || token == '=' || token == '!'){ // <relop>
    		/* Check each string using relop(): "<", "<=", ">", ">=", "==", "!=" */
    		// TODO: [Fill in your code here]
            Object relop = relop();
            int aexp2 = aexp();
//            System.out.println(aexp1 + " " + relop + " " + aexp2);
            if (relop.equals("<")) {
                result = aexp1 < aexp2;
                return result;
            }
            if (relop.equals("<=")) {
                result = aexp1 <= aexp2;
                return result;
            }
            if (relop.equals(">")) {
                result = aexp1 > aexp2;
                return result;
            }
            if (relop.equals(">=")) {
                result = aexp1 >= aexp2;
                return result;
            }
            if (relop.equals("==")) {
                result = aexp1 == aexp2;
                return result;
            }
            if (relop.equals("!=")) {
                result = aexp1 != aexp2;
                return result;
            }
    	}
		else {
			result = aexp1;
		}
    	return result;		
	}

    String relop() {    	
    /* <relop> -> ( < | <= | > | >= | == | != ) */    	
    	String result = "";
    	// TODO: [Fill in your code here]
        if (token == '<') {
            match('<');
            if (token == '=') {
                match('=');
                result = "<=";
            } else {
                result = "<";
            }
        }
        if (token == '>') {
            match('>');
            if (token == '=') {
                match('=');
                result = ">=";
            } else {
                result = ">";
            }
        }
        if (token == '=') {
            match('=');
            if (token == '=') {
                match('=');
                result = "==";
            }
        }
        if (token == '!') {
            match('!');
            if (token == '=') {
                match('=');
                result = "!=";
            }
        }
    	return result;
	}
    
    // TODO: [Modify code of aexp() for <aexp> -> <term> { + <term> | - <term> }]
    int aexp() {
    /* expr -> term { '+' term } */
        int result = term();
        while (token == '+' || token == '-') {
            if (token == '+') {
                match('+');
                result += term();
            } else if (token == '-') {
                match('-');
                result -= term();
            }
        }
        return result;
    }

 // TODO: [Modify code of term() for <term> -> <factor> { * <factor> | / <factor>}]
    int term( ) {
    /* term -> factor { '*' factor } */
       int result = factor();
       while (token == '*' || token == '/') {
           if (token == '*') {
               match('*');
               result *= factor();
           } else if (token == '/') {
               match('/');
               result /= factor();
           }
       }
       return result;
    }

    int factor() {
    /* factor -> '(' expr ')' | number */
        int result = 0;
        if (token == '(') {
            match('(');
            result = aexp();
            match(')');
        }
        else if (token == NUMBER) {
            result = value;
	        match(NUMBER); //token = getToken();
        }
        return result;
    }

    void parse( ) {
        token = getToken(); // get the first token
        command();          // call the parsing command
    }

    public static void main(String args[]) { 
        Calc calc = new Calc(new PushbackInputStream(System.in));
        while(true) {
            System.out.print(">> ");
            calc.parse();
        }
    }
}