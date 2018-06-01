package idv.ron.oogame_poke.model.action;

import idv.ron.oogame_poke.model.Pokemon;
import idv.ron.oogame_poke.model.skill.Move;

/**
 * 對戰
 */
public interface Fight {
    /**
     * 攻擊敵人
     *
     * @param enemy 敵人
     * @param move  技能攻擊
     * @return 攻擊結果造成的傷害
     */
    int attack(Pokemon enemy, Move move);

    /**
     * 攻擊敵人的結果
     *
     * @param enemy 敵人
     * @param move  技能攻擊
     * @return 攻擊結果
     */
    String attackResult(Pokemon enemy, Move move);
}
