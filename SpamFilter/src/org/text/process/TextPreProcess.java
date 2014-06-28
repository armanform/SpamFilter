/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.text.process;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 *
 * @author
 */
public class TextPreProcess {
    public static int[] processEmail(String email_contents) throws FileNotFoundException, IOException
    {
        String output = email_contents.toLowerCase();
        output = output.replaceAll("<[^<>]+>", " ");
        output = output.replaceAll("[0-9]+", "number");
        output = output.replaceAll("(http|https)://[^\\s]*", "httpaddr");
        output = output.replaceAll("[^\\s]+@[^\\s]+", "emailaddr");
        output = output.replaceAll("[$]+", "dollar");
        output = output.replaceAll("[^a-zA-Z0-9\\s]", "");
        
        StringTokenizer st = new StringTokenizer(output);
        
        //String finishedProc = "";
        ArrayList<String> strCollection = new ArrayList<String>();
        ArrayList<Integer> features = new ArrayList<Integer>();
        
        BufferedReader br = new BufferedReader(new FileReader((new File(".")).getAbsolutePath() + "\\TrainData\\vocab.txt"));
        String line;
        while ((line = br.readLine()) != null) {
            strCollection.add(line);
        }
        br.close();
        
        while(st.hasMoreTokens())
        {
            String token = st.nextToken();
            Stemmer stemPorter = new Stemmer();
            try{
                stemPorter.add(token.toCharArray(), token.length());
                stemPorter.stem();
            }catch(Exception e)
            {
            }
            String stemmedStr = stemPorter.toString();
            //finishedProc += stemmedStr + " ";
            
            for(int i = 0; i < strCollection.size(); i++)
            {
                if(strCollection.get(i).equals(stemmedStr))
                {
                    features.add(i);
                    break;
                }
            }
            
        }
        int[] featuresOfEmail = emailFeatures(features);
        return featuresOfEmail;
    }
    
    private static int[] emailFeatures(ArrayList<Integer> emailWords)
    {
        int[] featuresExt = new int[1899];
        for(int i = 0; i < emailWords.size(); i++)
        {
            featuresExt[emailWords.get(i)] = 1;
        }
        
        return featuresExt;
    }
}
