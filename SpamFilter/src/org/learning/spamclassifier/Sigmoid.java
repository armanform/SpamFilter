/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.learning.spamclassifier;

/**
 *
 * @author arman_000
 */
public class Sigmoid {
    public static float sigmoid(double val)
    {
        double sig = 1/(1 + Math.exp(-val));
        float out = (float)sig;
        return out;
    }
}
