package calcolatrice;

public class MathExpressionParser {

    /**
     * Parses and evaluates a mathematical expression in simple formats:
     * - number operator number
     * - function number
     * - function number number
     * - number! (factorial)
     *
     * @param expressionString The expression to evaluate
     * @return The calculated numerical result
     * @throws IllegalArgumentException if the expression is invalid
     */
    public double parseAndEvaluateMathematicalExpression(String expressionString) {
        if (expressionString == null || expressionString.trim().isEmpty()) {
            throw new IllegalArgumentException("Expression cannot be null or empty");
        }

        String trimmedExpression = expressionString.trim();
        
        // Check for factorial first: number!
        if (trimmedExpression.endsWith("!")) {
            String numberStr = trimmedExpression.substring(0, trimmedExpression.length() - 1).trim();
            double number = parseNumber(numberStr);
            return calculateFactorial(number);
        }
        
        // Parse the expression: number operator number
        String[] parts = trimmedExpression.split("\\s+");
        
        if (parts.length == 2) {
            // Format: function number
            String function = parts[0].toLowerCase();
            double number = parseNumber(parts[1]);
            return evaluateFunction(function, number);
            
        } else if (parts.length == 3) {
            // Format: number operator number OR function number number
            if (isFunction(parts[0])) {
                // Format: function number number (like "root 2 4")
                String function = parts[0].toLowerCase();
                double num1 = parseNumber(parts[1]);
                double num2 = parseNumber(parts[2]);
                return evaluateTwoArgumentFunction(function, num1, num2);
                
            } else {
                // Format: number operator number
                double num1 = parseNumber(parts[0]);
                String operator = parts[1];
                double num2 = parseNumber(parts[2]);
                return evaluateOperator(num1, operator, num2);
            }
            
        } else {
            throw new IllegalArgumentException("Invalid format. Use: \"1 + 1\"");
        }
    }

    private double parseNumber(String numberStr) {
        try {
            return Double.parseDouble(numberStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number: " + numberStr);
        }
    }

    private boolean isFunction(String str) {
        String lower = str.toLowerCase();
        return lower.equals("sin") || lower.equals("cos") || lower.equals("tan") || 
               lower.equals("sec") || lower.equals("inv") || lower.equals("root");
    }

    private double evaluateFunction(String function, double number) {
        switch (function) {
            case "sin":
                return Math.sin(number);
            case "cos":
                return Math.cos(number);
            case "tan":
                return Math.tan(number);
            case "sec":
                double cos = Math.cos(number);
                if (cos == 0) throw new ArithmeticException("Division by zero in sec");
                return 1.0 / cos;
            case "inv":
                if (number == 0) throw new ArithmeticException("Division by zero in inv");
                return 1.0 / number;
            default:
                throw new IllegalArgumentException("Unknown function: " + function);
        }
    }

    private double evaluateTwoArgumentFunction(String function, double num1, double num2) {
        if (!function.equals("root")) {
            throw new IllegalArgumentException("Unknown two-argument function: " + function);
        }
        
        if (num1 == 0) {
            throw new ArithmeticException("Root degree cannot be zero");
        }
        if (num2 < 0 && num1 % 2 == 0) {
            throw new ArithmeticException("Cannot calculate even root of negative number");
        }
        
        if (num2 < 0) {
            return -Math.pow(-num2, 1.0 / num1);
        }
        return Math.pow(num2, 1.0 / num1);
    }

    private double evaluateOperator(double num1, String operator, double num2) {
        switch (operator) {
            case "+":
                return num1 + num2;
            case "-":
                return num1 - num2;
            case "*":
                return num1 * num2;
            case "/":
                if (num2 == 0) throw new ArithmeticException("Division by zero");
                return num1 / num2;
            case "^":
                return Math.pow(num1, num2);
            default:
                throw new IllegalArgumentException("Unknown operator: " + operator);
        }
    }

    private double calculateFactorial(double number) {
        long n = (long) number;
        
        if (number != n) {
            throw new IllegalArgumentException("Factorial requires integer input");
        }
        if (n < 0) {
            throw new ArithmeticException("Factorial is not defined for negative numbers");
        }
       
        double result = 1;
        for (long i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }
}