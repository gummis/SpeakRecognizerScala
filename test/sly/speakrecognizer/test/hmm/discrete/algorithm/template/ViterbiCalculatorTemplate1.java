package sly.speakrecognizer.test.hmm.discrete.algorithm.template;

/**
 * Fancy Viterbi HMM Forward/Backward Processor
 * 
 * @author sagiemao
 * @author gittitda
 *
 */
import java.util.ArrayList;
import java.util.Hashtable;

public class ViterbiCalculatorTemplate1 {
        
        /**
         * Returns argmax of an array of Doubles,
         * argmax being the index of the maximum value.
         * @param arr An array of Double values
         * @return index of maximum value
         */
        private static Integer argmax(Double[] arr) {
                Double max = arr[0];
                Integer argmax = 0;
            for (int i=1; i<arr.length; i++) {
                if (arr[i] > max) {
                    max = arr[i];
                    argmax = i;
                }
            }
            return argmax;
        }
        
        private static Double[][] delta;
        private static Integer[][] psi;
        private static Double[] prob;


        public static double[][] getDelta() {
        	double[][] res = new double[delta[0].length][delta.length-2];
        	for(int y = 0 ; y < res.length ; y++)
            	for(int x = 0 ; x < res[0].length ; x++){
            		res[y][x] = delta[x+1][y];
            	}
			return res;
		}

		public static int[][] getPsi() {
        	int[][] res = new int[psi[0].length][psi.length-2];
        	for(int y = 0 ; y < res.length ; y++)
            	for(int x = 0 ; x < res[0].length ; x++){
            		int ps = psi[x+1][y];
            		res[y][x] = y == 0 ? ps : ps-1;
            	}
			return res;
		}

		public static double[] getProb() {
        	double[] res = new double[prob.length];
           	for(int x = 0 ; x < res.length ; x++){
            		res[x] = prob[x];
            }
			return res;
		}

		/**
         * Returns the most probable analyzing path for a word, given an HMM
         * @param word                        Observation (word to analyze)
         * @param transitions        Hash table describing probability of each
         *                                                 HMM state to transition to the other states
         * @param outputs                Hash table describing probability of an output
         *                                                 character in each state 
         * @return An array of state indexes passed in the most probably analyzing
         */
        public static ArrayList<Integer> viterbiPath(String word,
                        Hashtable<Integer, Hashtable<Integer, Double>> transitions,
                        Hashtable<Integer, Hashtable<Character, Double>> outputs) {
                
                int T = word.length(); // word length
                int N = outputs.size(); // outputting states count (without q0, qF)
                int F = N + 1; // final state index
                
                // create a path probability table v : (N+2)xT
                Double[][] v = new Double[N+2][T];
                Integer[][] bp = new Integer[N+2][T];
                
                // calculate first transition (first column in table)
                for (int s=1; s<=N; s++) {
                	Double transition = transitions.get(0).get(s);
                	Double probability = outputs.get(s).get(word.charAt(0));
                	v[s][0] = transition * probability;
                    bp[s][0] = 0;
                }
                 
                // populate Viterbi table with max. probabilities
                for (int t=1; t<T; t++) {
                        for (int s=1; s<=N; s++) {
                                Double[] probs = new Double[N];
                                for (int i=1; i<=N; i++) {
                                		Double tab = v[i][t-1];
                                        if (tab != null) {
                                        	Double transition = transitions.get(i).get(s);
                                        	Double probability = outputs.get(s).get(word.charAt(t));
                                            probs[i-1] = tab * transition * probability;
                                        }
                                        else {
                                                probs[i-1] = 0.0;
                                        }
                                }
                                int argmax = argmax(probs);
                                bp[s][t] = argmax+1;
                                v[s][t] = probs[argmax];
                        }
                }
                
                // calculate transition to final state
                Double[] probs = new Double[N];
                for (int i=1; i<=N; i++) {
                        if (v[i][T-1] != null) {
                                probs[i-1] = v[i][T-1];// * transitions.get(i).get(F);
                        }
                        else {
                                probs[i-1] = 0.0;
                        }
                }
                int argmax = argmax(probs);
                ArrayList<Integer> path = new ArrayList<Integer>();

                path.add(0,argmax+1);
        		for (int t2 = T - 2; t2 >= 0; t2--){
        			argmax = bp[path.get(0)][t2+1];
                    path.add(0,argmax);
        		}
        		
                delta = v;
                psi = bp;
                prob = probs;
                return path;
        }

        /**
         * Returns the total probability of an observation, given an HMM
         * @param word                        Observation (word to analyze)
         * @param transitions        Hash table describing probability of each
         *                                                 HMM state to transition to the other states
         * @param outputs                Hash table describing probability of an output
         *                                                 character in each state 
         * @return Total probability value of an observation
         */
        public static Double viterbiForward(String word,
                        Hashtable<Integer, Hashtable<Integer, Double>> transitions,
                        Hashtable<Integer, Hashtable<Character, Double>> outputs) {
                
                int T = word.length(); // word length
                int N = outputs.size(); // outputting states count (without q0, qF)
                int F = N + 1; // final state index
                
                // create a path probability table v : (N+2)xT
                Double[][] alpha = new Double[N+2][T];
                
                // calculate first transition (first column in table)
                for (int s=1; s<=N; s++) {
                        alpha[s][0] = transitions.get(0).get(s) * outputs.get(s).get(word.charAt(0));
                }
                
                // populate Viterbi table with total probabilities
                for (int t=1; t<T; t++) {
                        for (int s=1; s<=N; s++) {
                                alpha[s][t] = 0.0;
                                for (int i=1; i<=N; i++) {
                                        if (alpha[i][t-1] != null) {
                                                alpha[s][t]+= alpha[i][t-1] *
                                                        transitions.get(i).get(s) *
                                                        outputs.get(s).get(word.charAt(t));
                                        }
                                }
                        }
                }
                
                // calculate transition to final state
                alpha[F][T-1] = 0.0;
                for (int i=1; i<=N; i++) {
                        if (alpha[i][T-1] != null) {
                                alpha[F][T-1]+= alpha[i][T-1] * transitions.get(i).get(F);
                        }
                }
                
                // return final probability
                return alpha[F][T-1];
        }

        /**
         * Defines the HMM we saw in class and prints the analyzing path
         * calculated by viterbiPath() for a given word.
         * @param args        Observations as a string (a word to analyze)
         */
        public static void main(String[] args) {
        		args = new String[]{"uuuvvvuvuvvvuv"};
                // prepare transition probabilities table
                Hashtable<Integer, Hashtable<Integer, Double>> transitions =
                        new Hashtable<Integer, Hashtable<Integer, Double>>();
                
                // final state
                int F = 3;
                
                Hashtable<Integer, Double> q0 = new Hashtable<Integer, Double>();
                q0.put(1, 0.7);
                q0.put(2, 0.3);
                Hashtable<Integer, Double> q1 = new Hashtable<Integer, Double>();
                q1.put(1, 0.5);
                q1.put(2, 0.3);
                q1.put(F, 0.2);
                Hashtable<Integer, Double> q2 = new Hashtable<Integer, Double>();
                q2.put(1, 0.4);
                q2.put(2, 0.5);
                q2.put(F, 0.1);
                transitions.put(0, q0);
                transitions.put(1, q1);
                transitions.put(2, q2);
                
                // prepare outputs probabilities table
                Hashtable<Integer, Hashtable<Character, Double>> outputs =
                        new Hashtable<Integer, Hashtable<Character, Double>>();
                Hashtable<Character, Double> b1 = new Hashtable<Character, Double>();
                b1.put('u', 0.5);
                b1.put('v', 0.5);
                Hashtable<Character, Double> b2 = new Hashtable<Character, Double>();
                b2.put('u', 0.8);
                b2.put('v', 0.2);
                outputs.put(1, b1);
                outputs.put(2, b2);
                
                // validate input
                if (args.length < 1) {
                        System.err.println("Error! Please pass a word to analyze as an argument.");
                }
                else if (args[0].matches("[uv]*[^uv]+[uv]*")) {
                        System.err.println("Error! The input word consists of characters not recognized by my HMM. Please use only: [u,v].");
                }
                else {
                        // calculate requested values
                        ArrayList<Integer> path = viterbiPath(args[0], transitions, outputs);
                        Double forward = viterbiForward(args[0], transitions, outputs);
                        
                        System.out.println("Input for Viterbi analysis:");
                        System.out.println(args[0]);
                        System.out.println("\nViterbi optimal path:");
                        System.out.println(path);
                        System.out.println("\nViterbi forward probability:");
                        System.out.format("%.12f%n", forward);
                }
        }

}