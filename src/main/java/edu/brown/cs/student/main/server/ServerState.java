package edu.brown.cs.student.main.server;

import java.util.ArrayList;
import java.util.List;

public class ServerState {

    private List<List<String>> csvData;
    private String filepath;

    /**
     * Constructor for state does nothing. Will set values in their own methods
     */
    public ServerState() {}

    /**
     * Takes an input filepath and sets it to the filepath field of the state
     * @param filepath String filepath
     */
    public void setFilepath(String filepath){
        this.filepath = filepath;
    }

    /**
     * Takes input parsed csvData and assigns it to the state
     * @param csvData list<list<string>> parsed data
     */
    public void setCsvData(List<List<String>> csvData){
        this.csvData = csvData;
    }

    /**
     * Getter for filepath string
     * @return string filepath
     */
    public String getFilepath(){
        return this.filepath;
    }

    /**
     * Returns csv data
     * @return csv data field
     */
    public List<List<String>> getCsvData(){
        return this.csvData;
    }

    /**
     * Clears state data and sets both values to null.
     */
    public void clearData(){
        this.filepath = null;
        this.csvData = null;
    }

}
