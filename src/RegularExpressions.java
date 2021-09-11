
public class RegularExpressions {

	String swapLastFirst(String in) {
		String result = in.replaceAll("^\\s*(\\w*)\\s*,\\s*(\\w*)\\s*","$2 $1");
		return result;
	}

	String padTokensWithSpaces(String in) {
		String result = in.replaceAll("([\\+\\-\\*\\/\\(\\)])"," $1 ");
		result = result.replaceFirst("^\\s*", "");
		return result;
	}
	
	String[] identifyTokenType(String in) {
		in = padTokensWithSpaces(in);
		String[] tokens = in.split("\\s+");
		for (int i = 0; i < tokens.length; i++) {
			if (tokens[i].matches("[\\+\\-\\*\\/\\(\\)]")) {
				tokens[i] = "Operation: "+tokens[i];
			} else if (tokens[i].matches("\\d+")) {
				tokens[i] = "  Integer: "+tokens[i];
			} else if (tokens[i].matches("\\d+\\.\\d+")) {
				tokens[i] = "   Double: "+tokens[i];
			} else {
				tokens[i] = "    Error: "+tokens[i];
			}
		}
		return tokens;
	} 
	
	void printTokens(String[] tokens) {
		for (int i = 0; i < tokens.length; i++ ) {
			System.out.println( tokens[i]+",");
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		RegularExpressions regex = new RegularExpressions();
		String test = "Duck,Daffy";   // ==> Should go to Daffy Duck
		System.out.println(test+" becomes ["+regex.swapLastFirst(test)+"]");
		test = "   Duck , Daisy";     // ==> Should become Daisy Duck
		System.out.println(test+" becomes ["+regex.swapLastFirst(test)+"]");
		test = "   Duck ,Donald";     // ==> Should become Donald Duck
		System.out.println(test+" becomes ["+regex.swapLastFirst(test)+"]");
		test = "100+59*(17/4)";       // ==> [100 + 59 *  ( 17 / 4 ) ]
		System.out.println(test+" becomes ["+regex.padTokensWithSpaces(test)+"]");
		test = "-57+217*(17/-4) ";    // ==> [- 57 + 217 *  ( 17 /  - 4 )  ]
		System.out.println(test+" becomes ["+regex.padTokensWithSpaces(test)+"]");
		String[] tokens = regex.identifyTokenType(test);
		System.out.println("Identifying token types in ["+test+"]:");
		regex.printTokens(tokens);
		/*
		 *  Operation: -
		 *    Integer: 57
		 *  Operation: +
		 *    Integer: 217
		 *  Operation: *
		 *  Operation: (
		 *    Integer: 17
		 *  Operation: /
		 *  Operation: -
		 *    Integer: 4
		 *  Operation: ) 
		 */
		test = "-5.7+0.217*(107/-4.0)*17";
		System.out.println(test+" becomes ["+regex.padTokensWithSpaces(test)+"]");
		tokens = regex.identifyTokenType(test);
		System.out.println("Identifying token types in ["+test+"]:");
		regex.printTokens(tokens);
		/*
		 *Operation: -
		 *   Double: 5.7
		 *Operation: +
		 *   Double: 0.217
		 *Operation: *
		 *Operation: (
		 *  Integer: 107
		 *Operation: /
		 *Operation: -
		 *   Double: 4.0
		 *Operation: )
		 *Operation: *
		 * Integer: 17
		 */
	}

}
