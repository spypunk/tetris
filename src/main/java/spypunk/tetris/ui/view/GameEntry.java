package spypunk.tetris.ui.view;

public class GameEntry{
    private String name;
    private int score;
    public GameEntry(String n, int s){
        name = n;
        score = s;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setScore(int score){
        this.score = score;
    }

    public String getName(){
        return name;
    }
    public int getScore(){
        return score;
    }

    @Override
    public String toString() {
        return name + "-" + score + "\n";
    }
}





