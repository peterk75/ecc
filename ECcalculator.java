//package com.eccalculator;


import org.bouncycastle.math.ec.ECAlgorithms;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;

import java.awt.*;
import java.awt.event.*;
import java.math.BigInteger;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;

public class ECcalculator extends JPanel {
    private JLabel jcomp1;
    private JLabel jcomp2;
    private JLabel jcomp3;
    private JLabel jcomp4;
    private JTextField modp;
    private JLabel jcomp6;
    private JLabel jcomp7;
    private JTextField A;
    private JLabel jcomp9;
    private JLabel jcomp10;
    private JTextField B;
    private JLabel jcomp12;
    private JLabel jcomp13;
    private JTextField Px;
    private JLabel jcomp15;
    private JLabel jcomp16;
    private JTextField Py;
    private JLabel jcomp18;
    private JLabel jcomp19;
    private JTextField Qx;
    private JLabel jcomp21;
    private JLabel jcomp22;
    private JTextField Qy;
    private JLabel jcomp24;
    private JLabel jcomp25;
    private JTextField n;
    private JLabel jcomp27;
    private JLabel jcomp28;
    private JButton nP;
    private JLabel jcomp30;
    private JLabel jcomp31;
    private JButton PQ;
    private JLabel jcomp33;
    private JLabel jcomp34;
    private JTextField X;
    private JLabel jcomp36;
    private JLabel jcomp37;
    private JTextField Y;
    private JLabel jcomp39;
    private JLabel jcomp40;
    private JLabel jcomp41;
    private JLabel jcomp42;


    public ECcalculator() {
        //construct components
        jcomp1 = new JLabel ("");
        jcomp2 = new JLabel ("");
        jcomp3 = new JLabel ("");
        jcomp4 = new JLabel ("mod p: ",SwingConstants.RIGHT);
        modp = new JTextField (0);
        jcomp6 = new JLabel (" (be sure its a prime, just fermat prime test here, so avoid carmichael numbers)");
        jcomp6.setForeground(Color.RED);
        jcomp7 = new JLabel ("A: ",SwingConstants.RIGHT);
        A = new JTextField (5);
        jcomp9 = new JLabel ("");
        jcomp10 = new JLabel ("B: ",SwingConstants.RIGHT);
        B = new JTextField (5);
        jcomp12 = new JLabel ("");
        jcomp13 = new JLabel ("Px: ",SwingConstants.RIGHT);
        Px = new JTextField (5);
        jcomp15 = new JLabel ("");
        jcomp16 = new JLabel ("Py: ",SwingConstants.RIGHT);
        Py = new JTextField (5);
        jcomp18 = new JLabel ("");
        jcomp19 = new JLabel ("Qx: ",SwingConstants.RIGHT);
        Qx = new JTextField (5);
        jcomp21 = new JLabel (" type in coordinate Qx  type in coordinate Qy");
        jcomp22 = new JLabel ("Qy: ",SwingConstants.RIGHT);
        Qy = new JTextField (5);
        jcomp24 = new JLabel (" it's your own responsibility to ensure that Q is on curve ");
        jcomp25 = new JLabel ("number n: ",SwingConstants.RIGHT);
        n = new JTextField (5);
        jcomp27 = new JLabel ("");
        jcomp28 = new JLabel ("");
        nP = new JButton ("Calculate nP");
        jcomp30 = new JLabel ("");
        jcomp31 = new JLabel ("");
        PQ = new JButton ("Calculate P + Q");
        jcomp33 = new JLabel ("");
        jcomp34 = new JLabel ("Result X: ",SwingConstants.RIGHT);
        X = new JTextField (5);
        jcomp36 = new JLabel ("");
        jcomp37 = new JLabel ("Result Y: ",SwingConstants.RIGHT);
        Y = new JTextField (5);
        jcomp39 = new JLabel ("");
        jcomp40 = new JLabel ("");
        jcomp41 = new JLabel ("");
        jcomp42 = new JLabel ("");

        //set components properties
        jcomp1.setEnabled (false);
        jcomp2.setEnabled (false);
        jcomp3.setEnabled (false);
        jcomp6.setEnabled (false);
        jcomp9.setEnabled (false);
        jcomp12.setEnabled (false);
        jcomp15.setEnabled (false);
        jcomp18.setEnabled (false);
        jcomp21.setEnabled (false);
        jcomp24.setEnabled (false);
        jcomp27.setEnabled (false);
        jcomp30.setEnabled (false);
        jcomp33.setEnabled (false);
        jcomp36.setEnabled (false);
        jcomp39.setEnabled (false);
        jcomp40.setEnabled (false);
        jcomp41.setEnabled (false);
        jcomp42.setEnabled (false);

        //adjust size and set layout
        setPreferredSize (new Dimension (944, 642));
        GridLayout layout = new GridLayout(14, 3, 0, 18);
        setLayout (layout);

        String title = "Elliptic Curve Calculator for elliptic curve E(Fp): Y^2 =X^3+AX+B , p prime";
        TitledBorder border = BorderFactory.createTitledBorder(title);
        border.setTitleFont(new Font("Arial", Font.ITALIC, 20));
        border.setTitlePosition(TitledBorder.CENTER);
        border.setTitleColor(Color.BLUE);
        this.setBorder(border);


        //add components
        add(jcomp1);
        add(jcomp2);
        add(jcomp3);
        add (jcomp4);
        add (modp);
        add (jcomp6);
        add (jcomp7);
        add (A);
        add (jcomp9);
        add (jcomp10);
        add (B);
        add (jcomp12);
        add (jcomp13);
        add (Px);
        add (jcomp15);
        add (jcomp16);
        add (Py);
        add (jcomp18);
        add (jcomp19);
        add (Qx);
        add (jcomp21);
        add (jcomp22);
        add (Qy);
        add (jcomp24);
        add (jcomp25);
        add (n);
        add (jcomp27);
        add (jcomp28);
        add (nP);
        add (jcomp30);
        add (jcomp31);
        add (PQ);
        add (jcomp33);
        add (jcomp34);
        add (X);
        add (jcomp36);
        add (jcomp37);
        add (Y);
        add (jcomp39);
        add(jcomp40);
        add(jcomp41);
        add(jcomp42);

        nP.addActionListener((e) -> {
            if (modp.getText().equals("")) {
                jcomp6.setText("p is empty!!!");
                return;
            }

           if (A.getText().equals("")) {
               jcomp9.setText("A is empty!!!");
               return;
           }
            if (B.getText().equals("")) {
                jcomp12.setText("B is empty!!!");
                return;
            }

            if (Px.getText().equals("")) {
                jcomp15.setText("Px is empty!!!");
                return;
            }

            if (Py.getText().equals("")) {
                jcomp18.setText("Py is empty!!!");
                return;
            }

            if (n.getText().equals("")) {
                jcomp27.setText("n is empty!!!");
                return;
            }

            BigInteger lA;
            BigInteger lPy;
            BigInteger lPx;
            BigInteger lmodp;
            BigInteger ln;
            BigInteger lB;

            try {
                lmodp = new BigInteger(modp.getText());
            } catch (Exception ex) {
                jcomp6.setText("wrong number");
                return;
            }

            try {
                lA = new BigInteger(A.getText());
            } catch (Exception ex) {
                jcomp9.setText("wrong number");
                return;
            }

            try {
                lB = new BigInteger(B.getText());
            } catch (Exception ex) {
                jcomp12.setText("wrong number");
                return;
            }

            try {
                lPx = new BigInteger(Px.getText());
            } catch (Exception ex) {
                jcomp15.setText("wrong number");
                return;
            }

            try {
               lPy = new BigInteger(Py.getText());
            } catch (Exception ex) {
                jcomp18.setText("wrong number");
                return;
            }

            try {
                ln = new BigInteger(n.getText());
            } catch (Exception ex) {
                jcomp27.setText("wrong number");
                return;
            }

            ECCurve curve = new ECCurve.Fp(lmodp,lA,lB);
            ECPoint pointP = curve.createPoint(lPx,lPy);

            ECPoint Result = ECAlgorithms.referenceMultiply(pointP, ln).normalize();
            if (Result.isInfinity()) {
                jcomp36.setText("Infinity is not a valid value");
                return;
            }
            X.setText(Result.getAffineXCoord().toBigInteger().toString());
            jcomp36.setText("");
            Y.setText(Result.getAffineYCoord().toBigInteger().toString());

        });

        PQ.addActionListener((e) -> {
            if (modp.getText().equals("")) {
                jcomp6.setText("p is empty!!!");
                return;
            }

            if (A.getText().equals("")) {
                jcomp9.setText("A is empty!!!");
                return;
            }
            if (B.getText().equals("")) {
                jcomp12.setText("B is empty!!!");
                return;
            }

            if (Px.getText().equals("")) {
                jcomp15.setText("Px is empty!!!");
                return;
            }

            if (Py.getText().equals("")) {
                jcomp18.setText("Py is empty!!!");
                return;
            }

            if (Qx.getText().equals("")) {
                jcomp21.setText("Qx is empty!!!");
                return;
            }

            if (Qy.getText().equals("")) {
                jcomp24.setText("Qy is empty!!!");
                return;
            }



            BigInteger lA;
            BigInteger lPy;
            BigInteger lPx;
            BigInteger lmodp;
            BigInteger lQx;
            BigInteger lB;
            BigInteger lQy;

            try {
                lmodp = new BigInteger(modp.getText());
            } catch (Exception ex) {
                jcomp6.setText("wrong number");
                return;
            }

            try {
                lA = new BigInteger(A.getText());
            } catch (Exception ex) {
                jcomp9.setText("wrong number");
                return;
            }

            try {
                lB = new BigInteger(B.getText());
            } catch (Exception ex) {
                jcomp12.setText("wrong number");
                return;
            }

            try {
                lPx = new BigInteger(Px.getText());
            } catch (Exception ex) {
                jcomp15.setText("wrong number");
                return;
            }

            try {
                lPy = new BigInteger(Py.getText());
            } catch (Exception ex) {
                jcomp18.setText("wrong number");
                return;
            }

            try {
                lQx = new BigInteger(n.getText());
            } catch (Exception ex) {
                jcomp21.setText("wrong number");
                return;
            }

            try {
                lQy = new BigInteger(n.getText());
            } catch (Exception ex) {
                jcomp24.setText("wrong number");
                return;
            }


            ECCurve curve = new ECCurve.Fp(lmodp,lA,lB);
            ECPoint pointP = curve.createPoint(lPx,lPy);
            ECPoint pointQ = curve.createPoint(lQx, lQy);

            ECPoint Result = pointP.add(pointQ).normalize();
            if (Result.isInfinity()) {
                jcomp36.setText("Infinity is not a valid value");
                return;
            }
            X.setText(Result.getAffineXCoord().toBigInteger().toString());
            jcomp36.setText("");
            Y.setText(Result.getAffineYCoord().toBigInteger().toString());

        });

        A.addCaretListener ((e) -> {
            jcomp9.setText("");
        });

        B.addCaretListener ((e) -> {
            jcomp12.setText("");
        });

        Px.addCaretListener ((e) -> {
            jcomp15.setText("");
        });

        Py.addCaretListener ((e) -> {
            jcomp18.setText("");
        });

        n.addCaretListener ((e) -> {
            jcomp27.setText("");
        });

        modp.addCaretListener ((e) -> {
            jcomp6.setText(" (be sure its a prime, just fermat prime test here, so avoid carmichael numbers)");
        });

        Qx.addCaretListener ((e) -> {
            jcomp21.setText(" type in coordinate Qx  type in coordinate Qy");
        });

        Qy.addCaretListener ((e) -> {
            jcomp24.setText(" it's your own responsibility to ensure that Q is on curve ");
        });
    }

    public static void main(String[] args) {
        try {
            JFrame frame = new JFrame("ECcalculator");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(new ECcalculator());
            frame.pack();
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}