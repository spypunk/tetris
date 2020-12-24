package spypunk.tetris.model;

import java.util.Map;

public class TetrisAchievement {
    private Tetris tetris;
    private String name;
    private String message;
    String achievementType;
    private ShapeType shapeType;
    private int threshold;
    private boolean achievementUnlocked;

    public ShapeType getShapeType() {
        return shapeType;
    }

    public String getName() {
        return name;
    }

    public Tetris getTetris() {
        return tetris;
    }

    public String getAchievementType() {
        return achievementType;
    }

    public String getMessage() {
        return message;
    }

    public int getThreshold() {
        return threshold;
    }

    public boolean isAchievementUnlocked() {
        return achievementUnlocked;
    }

    public void setAchievementUnlocked(boolean exp) {
        this.achievementUnlocked = exp;
    }

    public TetrisAchievement(String name, String message, String achievementType, int threshold) {
        shapeType = null;
        achievementUnlocked = false;
        this.name = name;
        this.message = message;
        this.threshold = threshold;
    }

    public TetrisAchievement(String name, String message, ShapeType shapeType, int threshold) {
        /*
            In this type of achievement, programmer is able to count
            how many times of using any shape type. Any type and any threshold value
            can unlock such an achievement.
         */
        achievementUnlocked = false;
        this.name = name;
        this.message = message;
        this.threshold = threshold;
    }

}
