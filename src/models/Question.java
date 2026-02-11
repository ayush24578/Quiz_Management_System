package models;

/**
 * Represents a quiz question with all its properties.
 * This model class stores question data including text, options,
 * correct answer and difficulty level.
 * 
 */
public class Question {
    private int id;
    private String questionText;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String correctOption;
    private String difficulty;
    
    /**
     * Default constructor.
     * Creates an empty Question object.
     */
    public Question() {}
    /**
     * Constructor with all question properties.
     * 
     * @param questionText The text of the question
     * @param optionA Text for option A
     * @param optionB Text for option B
     * @param optionC Text for option C
     * @param optionD Text for option D
     * @param correctOption The correct option (A, B, C or D)
     * @param difficulty Difficulty level (Beginner, Intermediate, Advanced)
     */
    public Question(String questionText, String optionA, String optionB, 
                   String optionC, String optionD, String correctOption, String difficulty) {
        this.questionText = questionText;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctOption = correctOption;
        this.difficulty = difficulty;
    }
    
    /**
     * Gets the question ID.
     * 
     * @return The question ID
     */
    public int getId() {
    	return id; 
    }
    /**
     * Sets the question ID.
     * 
     * @param id The question ID to set
     */
    public void setId(int id) { 
    	this.id = id; 
    }
    /**
     * Gets the question text.
     * 
     * @param questionText The question Text
     */    
    public String getQuestionText() {
    	return questionText;
    }
    /**
     * Sets the question text.
     * 
     * @param questionText The question Text to set
     */  
    public void setQuestionText(String questionText) {
    	this.questionText = questionText;
    }
    /**
     * Gets the option A.
     * 
     * @param optionA The option A
     */  
    public String getOptionA() {
    	return optionA; 
    }
    /**
     * Sets the optionA.
     * 
     * @param optionA The optionA to set
     */  
    public void setOptionA(String optionA) {
    	this.optionA = optionA;
    }
    /**
     * Gets the optionB.
     * 
     * @param optionB The optionB
     */  
    public String getOptionB() {
    	return optionB; 
    }
    /**
     * Sets the optionB.
     * 
     * @param optionB The optionB to set
     */
    public void setOptionB(String optionB) {
    	this.optionB = optionB; 
    }
    /**
     * Gets the optionC.
     * 
     * @param optionC The optionC
     */
    public String getOptionC() {
    	return optionC; 
    }
    /**
     * Sets the optionC.
     * 
     * @param optionC The optionC to set
     */
    public void setOptionC(String optionC) { 
    	this.optionC = optionC; 
    }
    /**
     * Gets the optionD.
     * 
     * @param optionD The optionD
     */
    public String getOptionD() { 
    	return optionD; 
    }
    /**
     * Sets the optionD.
     * 
     * @param optionD The optionD to set
     */
    public void setOptionD(String optionD) {
    	this.optionD = optionD; 
    }
    /**
     * Gets the correctOption
     * 
     * @param correctOption The correctOption
     */
    public String getCorrectOption() { 
    	return correctOption; 
    }
    /**
     * Sets the correctOption
     * 
     * @param correctOption The correctOption to set
     */
    public void setCorrectOption(String correctOption) { 
    	this.correctOption = correctOption; 
    }
    /**
     * Gets the difficulty.
     * 
     * @param difficulty The difficulty
     */
    public String getDifficulty() { 
    	return difficulty; 
    }
    /**
     * Sets the difficulty.
     * 
     * @param difficulty The difficulty to set
     */
    public void setDifficulty(String difficulty) {
    	this.difficulty = difficulty; 
    }
}