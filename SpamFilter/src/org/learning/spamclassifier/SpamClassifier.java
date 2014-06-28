/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.learning.spamclassifier;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 *
 * @author Arman
 */
public class SpamClassifier {
    private static ArrayList<Double> JValues = new ArrayList<Double>();
    
    private static ArrayList<Double> sigmoid(ArrayList<Double> a)
    {
        ArrayList<Double> sigmoids = new ArrayList<Double>();
        
        for(int i = 0; i < a.size(); i++)
        {
            double en = -a.get(i);
            double sigm = 1/(1 + Math.exp(en));
            sigmoids.add(sigm);
        }
        
        return sigmoids;
    }
    
    private static ArrayList<Double> costFunction(ArrayList<ArrayList<Integer>>  X, int[] y, double[] theta, float lambda)
    {
        ArrayList<Double> sigmoids = new ArrayList<Double>();
        
        int m = y.length;
        int n = 1899;
        //return values
        double J = 0.0;
        ArrayList<Double> grad = new ArrayList<Double>();
        //----------------------------------------------
        
        ArrayList<Double> sig = new ArrayList<Double>();
        double sumsig = 0.0;
        for(int i = 0; i < X.size(); i++)
        {
            //X.get(i).add(1);
            for(int j = 0; j < X.get(i).size(); j++)
            {
                sumsig += X.get(i).get(j)*theta[j];
            }
            sig.add(sumsig);
            sumsig = 0;
        }
        
        sig = sigmoid(sig);
        
        double J0 = 0.0;
        double J1 = 0.0;
        for(int i = 0; i < m; i++)
        {
            J0 += -y[i]*Math.log(sig.get(i)) - (1-y[i])*Math.log(1 - sig.get(i));
        }
        
        for(int i = 1; i  < theta.length; i++)
        {
            J1 += theta[i]*theta[i]; 
        }
        
        J = J0/m + (J1*lambda)/(2*m);
        JValues.add(J);
        
        double gradTemp = 0.0;
        for(int i = 0; i <= n; i++)
        {
            gradTemp = 0.0;
            for(int j = 0; j < m; j++){
                gradTemp += (sig.get(j) - y[j]) * X.get(j).get(i);
                //gradTemp = gradTemp/m;
            }
            gradTemp = gradTemp/m;
            if(i != 0)
                gradTemp += (lambda/m)*theta[i];
            
            grad.add(gradTemp);
            System.out.println(i);
        }
        
        return grad;
    }
    
    public static void main(String[] args) throws IOException
    {
        ArrayList<ArrayList<Integer>>  X = new ArrayList<ArrayList<Integer>>();
        int[] y = new int[1000];
        double[] theta = new double[1900];
        float lambda = 0.1f;
        
        BufferedReader br = new BufferedReader(new FileReader((new File(".")).getAbsolutePath() + "\\TrainData\\xTrain.txt"));
        String line;
        int i = 0, i3 = 0, i4 = 0, i5 = 0;
        while ((line = br.readLine()) != null) {
            StringTokenizer token = new StringTokenizer(line);
            ArrayList<Integer> XItem = new ArrayList<Integer>();
            XItem.add(1);
            while(token.hasMoreTokens())
            {
                try{
                    XItem.add(Integer.parseInt(token.nextToken()));
                    //System.out.println(i);
                }catch(Exception e)
                {
                }
            }
            //System.out.println(i);
            if(XItem.size() < 1899) i5++;
            else if(XItem.size() == 1899) i3++;
            else if(XItem.size() > 1899) i4++;
            X.add(XItem);
            i++;
        }
        br.close();
        
        BufferedReader br1 = new BufferedReader(new FileReader((new File(".")).getAbsolutePath() + "\\TrainData\\yTrain.txt"));
        String line1;
        int i1 = 0;
        while ((line1 = br1.readLine()) != null) {
            try{
                y[i1] = Integer.parseInt(line1);
            }catch(Exception e)
            {
            }
            i1++;
        }
        br1.close();
        
        for(int j = 0; j < 700; j++)
        {
            ArrayList<Double> grad = costFunction(X,y, theta, lambda);
            for(int k = 0; k < 1900; k++)
            {
                theta[k] = theta[k] - grad.get(k);
            }
            System.out.println("Iteration: " + j);
        }
        
        
        PrintWriter pw = new PrintWriter((new File(".")).getAbsolutePath() + "\\TrainData\\weights.txt");
        
        for(int it = 0; it < theta.length; it++)
        {
            pw.println(theta[it]);
        }
        pw.flush();
        pw.close();
    }
    
}
