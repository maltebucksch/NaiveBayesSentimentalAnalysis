package analyzer;

class LoadingInfo {
	static boolean printInfo(int curCnt, int maxCnt, int parts,
							 String label) {
		int step = maxCnt / parts;
		if (step > 0) {
			if (curCnt % step == 0) {
				int fin = (int) (((float) curCnt) / ((float) maxCnt) * 100f);
				System.out.println(label + " - Finished already: " + fin);
				return true;
			} else {
				return false;
			}
		}else return false;
	}
}
