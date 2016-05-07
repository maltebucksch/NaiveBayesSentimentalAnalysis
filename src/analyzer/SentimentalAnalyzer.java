package analyzer;

import classification.BayesMachine;
import io.XMLInput;
import io.XMLOutput;
import rating_model.Review;

import java.util.ArrayList;

public class SentimentalAnalyzer {
	private BayesMachine machine;
	private final double THRESHOLD=100d;
	private final String TAG="weibo";
	
	
	public static void main(String[] args) {
		new SentimentalAnalyzer().start();
	}
	public void start(){
		trainBayes();
		analyzeFinal();
	}
	public void trainBayes(){
			String[] reviewsPos = XMLInput.readReviewData(
					"sample.positive.txt", "review");
			String[] reviewsNeg = XMLInput.readReviewData(
					"sample.negative.txt", "review");

			machine = new BayesMachine();
			machine.trainInput(reviewsPos, true);
			machine.trainInput(reviewsNeg, false);
	}

	public void analyzeFinal() {	
		ArrayList<Review> reviews=XMLInput.readReviewDataAsList("training.xml", TAG);

        int reviewsAnalyzedCount = 0;
		for (Review review: reviews) {
			double[] res = machine.guessIfPositive(review.getReviewContent());
            double positiveTextProbability = res[0];
            double negativeTextProbability = res[1];

            if (positiveTextProbability > negativeTextProbability * THRESHOLD) {
				review.setPolarity(1);
			} else if (negativeTextProbability > positiveTextProbability * THRESHOLD) {
				review.setPolarity(-1);
			} else {
                review.setPolarity(getPolarityWhenUnsure(positiveTextProbability,negativeTextProbability));
			}

			LoadingInfo.printInfo(++reviewsAnalyzedCount, reviews.size(), 10,
					"Analysis ");
		}

		XMLOutput.output(reviews, "output.xml",TAG);
	}

    private int getPolarityWhenUnsure(double negativeTextProbability, double positiveTextProbability){
        return positiveTextProbability >= negativeTextProbability ? 1 : -1;
    }
}
