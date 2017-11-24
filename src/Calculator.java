import java.math.BigDecimal;
import java.util.EmptyStackException;
import java.util.Scanner;
import java.util.Stack;

public class Calculator {

    private static Stack<Object> num = new Stack<>();
    private static Stack<Object> operator = new Stack<>();
    private static int[] operatPriority = new int[]{0, 3, 2, 1, -1, 1, 0, 2};//运用运算符ASCII码-40做索引的运算符优先级

    public static void main(String[] args) {
        String exam = "5+12*(3+5)/7";
        System.out.println("例子：计算" + exam);
        PopDown(exam);
        System.out.println("后缀表达式：" + num.toString());
        double examResult = Calculate(Reverse(num));
        System.out.println("最终答案：" + examResult);
        System.out.println();
        System.out.print("请输入算式：");
        Scanner in = new Scanner(System.in);
        String equation = in.nextLine();
        PopDown(equation);
        System.out.println("后缀表达式：" + num.toString());
        double result = Calculate(Reverse(num));
        System.out.println("最终答案：" + result);
    }

    //压栈
    public static void PopDown(String equation) {
        char[] chars = new char[equation.length()];
        for (int i = 0; i < equation.length(); i++) {
            chars[i] = equation.charAt(i);
        }
        int count = 0, curIndex = 0;
        for (int i = 0; i < chars.length; i++) {
            if (isCaoZuoFu(chars[i])) {
                if (count > 0) {
                    num.push(new String(chars, curIndex, count));
                    count = 0;
                }
                curIndex = i + 1;
                if (chars[i] == ')') {
                    while ((char) operator.peek() != '(') {
                        num.add(operator.pop());
                    }
                    operator.pop();

                } else {
                    while (!operator.isEmpty() && chars[i] != '(' && compare(chars[i], (char) operator.peek())) {
                        num.push(operator.pop());
                    }
                    operator.push(chars[i]);
                }
            } else {
                count++;
            }
        }
        num.push(new String(chars, curIndex, count));
        while (!operator.isEmpty()) {
            num.add(operator.pop());
        }
    }

    public static double Calculate(Stack<Object> stack) {
        Stack<Object> temp = new Stack<>();//存放后缀表达式的操作数的栈
        while (!stack.isEmpty()) {
            String tmp = stack.peek().toString();
            if (!isCaoZuoFu(tmp.charAt(0))) {
                temp.add(stack.pop());
                System.out.println(temp);
            } else {
                BigDecimal o1 = new BigDecimal(temp.pop().toString());
                BigDecimal o2 = new BigDecimal(temp.pop().toString());
                try {
                    if (!stack.isEmpty()) {
                        if ((char) stack.peek() == '+') {
                            temp.add(o2.add(o1).doubleValue());
                        }
                        if ((char) stack.peek() == '-') {
                            temp.add(o2.subtract(o1).doubleValue());
                        }
                        if ((char) stack.peek() == '*') {
                            temp.add(o2.multiply(o1).doubleValue());
                        }
                        if ((char) stack.peek() == '/') {
                            //除不尽保留10位小数
                            temp.add(o2.divide(o1, 10, java.math.BigDecimal.ROUND_HALF_UP).doubleValue());
                        }
                        stack.pop();
                    }
                } catch (ArithmeticException e) {
                    System.out.println("除数不能为0！");
                    break;
                }
            }
        }
        return (double) temp.pop();
    }

    public static Stack<Object> Reverse(Stack<Object> shu) {
        Stack<Object> reverseStack = new Stack<>();
        while (!shu.isEmpty()) {
            reverseStack.add(shu.pop());
        }
        System.out.println("反序：" + reverseStack);
        return reverseStack;
    }

    //此方法用于判断对应字符是不是操作符，是则返回true。
    public static boolean isCaoZuoFu(char chars) {
        return chars == 40 || chars == 41 || chars == 42 || chars == 43 || chars == 45 || chars == 47;
    }

    // 如果是peek优先级高于cur，返回true，默认都是peek优先级要低
    public static boolean compare(char cur, char peek) {
        boolean result = false;
        if (operatPriority[(peek) - 40] >= operatPriority[(cur) - 40]) {
            result = true;
        }
        return result;
    }
}