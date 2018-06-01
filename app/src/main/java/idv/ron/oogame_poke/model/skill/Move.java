package idv.ron.oogame_poke.model.skill;

import java.io.Serializable;

/**
 * 攻擊技能
 */
public class Move implements Serializable {
    private String name; // 技能名稱
    private int power; // 技能傷害

    public Move(String name, int power) {
        this.name = name;
        this.power = power;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }
}
