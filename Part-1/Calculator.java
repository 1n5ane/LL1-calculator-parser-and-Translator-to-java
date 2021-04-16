import java.io.InputStream;
import java.io.IOException;

class Calculator {
    private static int pow(int base, int exponent) {
      if (exponent < 0)
        return 0;
      if (exponent == 0)
        return 1;
      if (exponent == 1)
        return base;

      if (exponent % 2 == 0) //even exp -> b ^ exp = (b^2)^(exp/2)
        return pow(base * base, exponent/2);
      else                   //odd exp -> b ^ exp = b * (b^2)^(exp/2)
        return base * pow(base * base, exponent/2);
    }
    private final InputStream in;

    private int lookahead;

    public Calculator(InputStream in) throws IOException {
        this.in = in;
        lookahead = in.read();
    }

    private void consume(int symbol) throws IOException, ParseError {
        if (lookahead == symbol)
            lookahead = in.read();
        else
            throw new ParseError();
    }

    private boolean isDigit(int c) {
        return '0' <= c && c <= '9';
    }

    private boolean isDigit1(int c) {
        return '1' <= c && c <= '9';
    }

    private int evalDigit(int c) {
        return c - '0';
    }

    public int eval() throws IOException, ParseError {
        int value = Exp();
        if (lookahead != -1 && lookahead != '\n')
            throw new ParseError();

        return value;
    }

    private int Exp() throws IOException, ParseError {
        if(isDigit(lookahead) || lookahead=='(')
          return Exp1(Term());

        throw new ParseError();
    }

    private int Exp1(int x) throws IOException, ParseError {
      switch(lookahead){
        case -1:
        case '\n':
        case ')':
          return x;
        case '+':
          consume(lookahead);
          return x+Exp1(Term());
        case '-':
          int dig;
          consume(lookahead);
          return Exp1(x-Term());
        default:
          throw new ParseError();
      }
    }

    private int Term() throws IOException, ParseError {
      if(isDigit(lookahead))
        return Term1(Num());
      else if (lookahead=='('){
        consume(lookahead);
        int val;
        val=Exp();
        consume(')');
        return Term1(val);
      }
      throw new ParseError();

    }

    private int Term1(int val) throws IOException, ParseError {
      switch(lookahead){
        case '+':
        case '-':
        case '\n':
        case -1:
        case ')':
          return val;
        case '*':
          // consume one more * from exponention
          consume(lookahead);
          consume('*');
          int power=Term();
          return (int)Math.pow((double)val,(double)power);
        default:
          throw new ParseError();
      }
    }

    private int Num() throws IOException, ParseError {
      if(lookahead<='9' && lookahead>='0'){
        if(lookahead=='0'){
          return Digit();
        }

        return Num2(Digit1());
      }else{
        throw new ParseError();
      }
    }

    private int Num2(int x) throws IOException, ParseError {
      switch(lookahead){
        case '+':
        case '-':
        case -1:
        case '\n':
        case '*':
        case ')':
          // do nothing and dont consume
          return x;
        default:
          if(isDigit(lookahead))
            return Num2(10*x+Digit());

          throw new ParseError();
      }
    }

    private int Digit() throws IOException, ParseError {
      if(isDigit(lookahead)){
        int dig=lookahead;
        consume(lookahead);
        return evalDigit(dig);
      }
      throw new ParseError();
    }

    private int Digit1() throws IOException, ParseError {
      if(isDigit1(lookahead)){
        int dig=lookahead;
        consume(lookahead);
        return evalDigit(dig);
      }
      throw new ParseError();
    }
}
