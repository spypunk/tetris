package spypunk.tetris.model;

import spypunk.tetris.ui.view.TetrisAchievementsView;

import java.util.Map;

public class TetrisAchievementChecker {
    private TetrisAchievement ta;

    public TetrisAchievementChecker(TetrisAchievement ta) {
        this.ta = ta;
    }

    public void checkAchievement() {
        if (ta.getShapeType() == null) { // that means this achievement is related to score or row
            if (ta.getAchievementType().equals("SCORE")) {
                ta.getTetris().setNForScore(ta.getThreshold());
                // controls can be done in my previous methods.
            }
            if (ta.getAchievementType().equals("ROW")) {
                ta.getTetris().setNForRow(ta.getThreshold());
            }
        }
        else {
            for (Map.Entry<ShapeType, Integer> entry: ta.getTetris().getStatistics().entrySet()) {
                if (entry.getKey().equals(ta.getShapeType()) && entry.getValue() >= ta.getThreshold()) {
                    if (!ta.isAchievementUnlocked()) {
                        ta.setAchievementUnlocked(true);
                        ta.getTetris().addAchievement(ta);
                    }
                }
            }
        }
    }
}
