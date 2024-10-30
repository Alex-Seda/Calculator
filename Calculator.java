import javax.swing.*;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.*;
import java.math.BigDecimal;
import java.math.MathContext;


public class Calculator{
    public static void main(String[] args){
        new CalcFrame();
    }
}

class CalcModel {
    private double disp;

    private double total;
    private String operation;
    private boolean clear;
    private double prev;

    private double c;
    private boolean decimal;
    private int decimalPlace;

    CalcModel(){ total=0; operation=""; clear=false; prev=0; disp=0; c=0; decimal=false; decimalPlace=0; }

    public void Add(double d){ total+=d; }
    public void Sub(double d){ total-=d; }
    public void Mult(double d){ total*=d; }
    public void Div(double d){ total/=d; }
    public void power(double c, double d){ total = Math.pow(c,d); }
    public void Sqrt(double d){ total = Math.sqrt(d); }

    public void pressed(boolean isOp, double value, String text){
        if(isOp){
            if(text=="C"){
                decimal=false;
                decimalPlace=0;
                if(clear){
                    total=0;
                    c=0;
                    prev=0;
                    disp=0;
                    clear=false;
                }
                else{
                    clear=true;
                    c=0;
                    disp=prev;
                }
            }
            else if(text=="√"){
                clear=true;
                decimal=false;
                decimalPlace=0;
                if(c==0){
                    Sqrt(total);
                }
                else{
                    Sqrt(c);
                }
                operation="";
                round();
                prev=total;
                disp=total;
                c=0;
            }
            else if(text=="^2"){
                clear=true;
                decimal=false;
                decimalPlace=0;
                if(c==0){
                    power(total,2);
                }
                else{
                    power(c,2);
                }
                operation="";
                prev=total;
                disp=total;
                c=0;
            }
            else if(text=="."){
                decimal=true;
                decimalPlace=0;
            }
            else if(text=="="){
                clear=true;
                decimal=false;
                decimalPlace=0;
                if(operation=="+"){ Add(c);}
                else if(operation=="-"){ Sub(c); }
                else if(operation=="*"){ Mult(c); }
                else if(operation=="/"){ Div(c); }
                else if(operation=="^"){ power(total, c); }
                operation="";
                round();
                prev=total;
                disp=total;
                c=0;
            }
            else{
                operation = text;
                if(c==0){}
                else{
                    total=c;
                    c=0;
                }
                decimalPlace=0;
                decimal=false;
                clear=false;
            }
            
        }
        else{
            prev=total;
            clear=false;
            if(decimal){
                decimalPlace++;
                double temp = value/Math.pow(10, decimalPlace);
                c+=temp;
                disp=c;
            }
            else{
                c*=10;
                c+=value;
                disp=c;
            }
            
        }
    }

    public double getDisp(){
        return disp;
    }

    public void round(){
        BigDecimal bd = new BigDecimal(total);
        bd = bd.round(new MathContext(3));
        total = bd.doubleValue();
    }
}

class CalcFrame extends JFrame{
    double display;
    DisplayPanel dispPan;
    CalcPanel panel;

    public CalcFrame(){
        setSize(300,400);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        BorderLayout layout = new BorderLayout();
        setLayout(layout);
        dispPan = new DisplayPanel();
        panel = new CalcPanel();
        setTitle("Calculator");
        add(dispPan, BorderLayout.PAGE_START);
        add(panel, BorderLayout.CENTER);
        dispPan.window.setText(Double.toString(display));
        setVisible(true);
    }


    class CalcPanel extends JPanel{
        CalcModel model1;

        public CalcPanel(){
            model1 = new CalcModel();
            GridLayout layout = new GridLayout(5,4);
            setLayout(layout);
            add(new CalcButton("^"));
            add(new CalcButton("^2"));
            add(new CalcButton("√"));
            add(new CalcButton("C"));
            add(new CalcButton(1));
            add(new CalcButton(2));
            add(new CalcButton(3));
            add(new CalcButton("+"));
            add(new CalcButton(4));
            add(new CalcButton(5));
            add(new CalcButton(6));
            add(new CalcButton("-"));
            add(new CalcButton(7));
            add(new CalcButton(8));
            add(new CalcButton(9));
            add(new CalcButton("*"));
            add(new CalcButton("."));
            add(new CalcButton(0));
            add(new CalcButton("="));
            add(new CalcButton("/"));
        }


        class CalcButton extends JButton{
            String text;
            int value;
            boolean isOperation;

            CalcButton(String s){
                text = s;
                value = -1;
                isOperation = true;
                MouseHandler m = new MouseHandler();
                addMouseListener(m);
                setText(text);
            }

            CalcButton(int n){
                value = n;
                text = ((Integer)value).toString();
                isOperation = false;
                MouseHandler m = new MouseHandler();
                addMouseListener(m);
                setText(text);
            }
            
            class MouseHandler extends MouseAdapter{
                public void mousePressed(MouseEvent e) {
                    model1.pressed(isOperation, value, text);
                    display=model1.getDisp();
                    dispPan.window.setText(Double.toString(display));
                    repaint();
                }
            }
        }
    }

    class DisplayPanel extends JPanel{
        JTextField window;
        public DisplayPanel(){
            BorderLayout layout2 = new BorderLayout();
            setLayout(layout2);
            window = new JTextField(Integer.toString(0));
            window.setHorizontalAlignment(JTextField.RIGHT);
            window.setEditable(false);
            Font bigFont = window.getFont().deriveFont(Font.PLAIN, 65f);
            window.setFont(bigFont);
            add(window, BorderLayout.PAGE_START); 
        }  
    }
}
