package edu.brown.cs.student.main.acsData;

public class StateCodePair {

    private String state;

    private int code;

    /**
     * Constructor for a state code pairing
     * @param state string
     * @param code int
     */
    public StateCodePair(String state, int code) {
        this.state = state;
        this.code = code;
    }

    /**
     * Getter for code
     * @return int code
     */
    public int getCode() {
        return code;
    }

    /**
     * Getter for state
     * @return string state
     */
    public String getState() {
        return state;
    }
}
