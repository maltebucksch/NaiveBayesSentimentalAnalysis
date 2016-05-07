package classification;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

public class BayesMachine {
	private HashMap<String, Integer> posTrainingData = new HashMap<String, Integer>();
	private HashMap<String, Integer> negTrainingData = new HashMap<String, Integer>();
	private int positiveReviewsTrained = 0;
	private int negativeReviewsTrained = 0;

	private final static String[] NEGATING_TOKENS = { "never", "no", "nothing",
			"nowhere", "noone", "none", "not", "havent", "hasnt", "hadnt",
			"cant", "couldnt", "shouldnt", "wont", "wouldnt", "dont", "doesnt",
			"didnt", "isnt", "arent", "aint" };
	private final static boolean STEMMING_ON = true;

	private Stemmer stemmer;


	public BayesMachine() {
		stemmer = new Stemmer();
	}

	// trains the machine give an array of reviews and the information that they
	// are postive or negative
	public void trainInput(String[] input, boolean isPositive) {
		for (int i = 0; i < input.length; i++) {
			String[] simple = simplify(input[i]);
			trainElement(simple, isPositive);
		}
	}

	// trains the machine only based on one simplified review and the
	// information that it is postive or negative
	private void trainElement(String[] simplifiedList, boolean isPositive) {
		HashMap<String, Integer> trainingData = null;
		if (isPositive)
			trainingData = posTrainingData;
		else
			trainingData = negTrainingData;

		for (int i = 0; i < simplifiedList.length; i++) {
			String token = simplifiedList[i];
			if (trainingData.containsKey(token)) {
				trainingData.put(token, trainingData.get(token) + 1);
			} else {
				trainingData.put(token, 1);
			}
		}
		if (isPositive)
			positiveReviewsTrained++;
		else
			negativeReviewsTrained++;
	}


	// simplifies the input String and returns and array of all the words in the
	// String
	private String[] simplify(String input) {
		// remove all special characters
		String simple = input.toLowerCase().replaceAll("[^a-z ]+", "");
		// remove double whitespaces
		simple = simple.replaceAll("\\s+", " ");
		// convert into array of all words
		String[] wl = simple.trim().split(" ");

		// mark words around the negatating words as a negated word by adding a
		// "-"
		for (int i = 0; i < wl.length; i++) {
			if (ArrayHelper.contains(NEGATING_TOKENS,wl[i])) {
				if (i < wl.length - 1) {
					if(STEMMING_ON)wl[i+1] = getStemmed(wl[i+1]);
					wl[i + 1] += "-";
					i++;
				}
				if (i > 0)
					wl[i - 1] += "-";
			} else {
				if (STEMMING_ON) {
					wl[i] = getStemmed(wl[i]);
				}
			}
		}
		// remove duplicates
		Set<String> set = new LinkedHashSet<String>(Arrays.asList(wl));
		String[] unique = set.toArray(new String[set.size()]);

		return unique;
	}
	private String getStemmed(String s){
		stemmer.add(s.toCharArray(), s.length());
		stemmer.stem();
		return stemmer.toString();
	}

	// returns how often this token was found during training phase by comparing
	// it to either the positive or negative training data
	private int countInTrainingData(String token, boolean isPositive) {
		Integer cnt = null;
		if (isPositive)
			cnt = posTrainingData.get(token);
		else
			cnt = negTrainingData.get(token);

		if (cnt == null)
			return 0;
		else
			return cnt;
	}

	// the main purpose of the training is applied here
	// returns if the input-Review is more likely to
	// be a positive review or a negative review based on the previous training
	// of this Machine
	public double[] guessIfPositive(String input) {
		String[] tokens = simplify(input);

		double positiveTextProbability = 1;
		double negativeTextProbability = 1;
		for (int i = 0; i < tokens.length; i++) {
			double posCnt = countInTrainingData(tokens[i], true);
			double negCnt = countInTrainingData(tokens[i], false);

			if (negCnt + posCnt > 0) {
				// probability that this token shows up in positive review
				double posProb = posCnt / positiveReviewsTrained;
				// probability that this token shows up in negative review
				double negProb = negCnt / negativeReviewsTrained;

				// probability that this review is positive under the assumption
				// that this token appears
				double condProb = posProb / (posProb + negProb);
				double condProbNeg = negProb / (posProb + negProb);

				condProb = ((3 * 0.5f) + ((negCnt + posCnt) * condProb))
						/ (3 + negCnt + posCnt);

				positiveTextProbability *= condProb;
				negativeTextProbability *= condProbNeg;
			}
		}

		return new double[]{positiveTextProbability,negativeTextProbability};
	}

	public boolean isPositive(String input) {
		double[]res=this.guessIfPositive(input);
		return res[0]>res[1];
	}
}
