public class BoundCounterWithRaceConditionCheck {

	private static final int RACE_CONDITION_POINTS = 25;
	private static final String RACE_CONDITION_MESSAGE = "Race condition occured";

	private int value;
	private Integer maxAllowed;
	private Integer minAllowed;
	private int maxErrorPoints;
	private int minErrorPoints;
	private String maxErrorMessage;
	private String minErrorMessage;

	public static int raceConditionDefaultTime = 3;

	private int max;


	public BoundCounterWithRaceConditionCheck(int value) {
		super();
		this.value = value;
		this.max = value;
	}

	
	public BoundCounterWithRaceConditionCheck(int value, Integer maxAllowed,
			int maxErrorPoints, String maxErrorMessage, Integer minAllowed,
			int minErrorPoints, String minErrorMessage) {
		super();
		this.value = value;
		this.max = value;
		this.maxAllowed = maxAllowed;
		this.minAllowed = minAllowed;
		this.maxErrorPoints = maxErrorPoints;
		this.minErrorPoints = minErrorPoints;
		this.maxErrorMessage = maxErrorMessage;
		this.minErrorMessage = minErrorMessage;
	}


	public int getMax() {
		return max;
	}


	public synchronized int getValue() {
		return value;
	}

	public synchronized void setValue(int value) {
		this.value = value;
	}

	
	public synchronized PointsException assertEquals(int val, int points,
			String errorMessage) {
		if (this.value != val) {
			PointsException e = new PointsException(points, errorMessage);
			return e;
		} else {
			return null;
		}
	}

	public synchronized PointsException assertNotEquals(int val, int points,
			String errorMessage) {
		if (this.value == val) {
			PointsException e = new PointsException(points, errorMessage);
			return e;
		} else {
			return null;
		}
	}


	public PointsException checkRaceCondition() {
		return checkRaceCondition(raceConditionDefaultTime,
				RACE_CONDITION_MESSAGE);
	}


	public PointsException checkRaceCondition(int time, String message) {
		int val;

		synchronized (this) {
			val = value;
		}
		Switcher.forceSwitch(time);
		if (val != value) {
			PointsException e = new PointsException(RACE_CONDITION_POINTS,
					message);
			return e;
		}
		return null;

	}

	public PointsException incrementWithMax() {
		return incrementWithMax(true);
	}

	public PointsException incrementWithMax(boolean checkRaceCondition) {
		if (checkRaceCondition) {
			PointsException raceCondition = checkRaceCondition();
			if (raceCondition != null) {
				return raceCondition;
			}
		}
		synchronized (this) {
			value++;

			if (value > max) {
				max = value;
			}
			if (maxAllowed != null) {
				if (value > maxAllowed) {
					PointsException e = new PointsException(maxErrorPoints,
							maxErrorMessage);
					return e;
				}
			}
		}

		return null;
	}

	public PointsException decrementWithMin() {
		return decrementWithMin(true);
	}

	public PointsException decrementWithMin(boolean checkRaceCondition) {
		if (checkRaceCondition) {
			PointsException raceCondition = checkRaceCondition();
			if (raceCondition != null) {
				return raceCondition;
			}
		}

		synchronized (this) {
			value--;
			if (minAllowed != null) {
				if (value < minAllowed) {
					PointsException e = new PointsException(minErrorPoints,
							minErrorMessage);
					return e;
				}
			}
		}
		return null;
	}

}
