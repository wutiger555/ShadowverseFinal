package idv.ron.oogame_poke.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import idv.ron.oogame_poke.R;
import idv.ron.oogame_poke.model.action.Fight;
import idv.ron.oogame_poke.model.skill.Move;

/**
 * 寶可夢精靈，參看寶可夢Go全圖鑑（http://www.otaku-hk.com/pkmgo/en/pokedex）
 */
public class Pokemon implements Fight, Serializable {
    private static List<Pokemon> myPokemons = new ArrayList<>();

    // 圖片
    private int image;
    // 名稱
    private String name;
    // 等級
    private int level;
    // 耐力
    private int stamina;
    // 血
    private int hp;
    // 攻擊力
    private int attack;
    // 防禦力
    private int defense;
    // 捕捉率
    private int catchChance;
    // 基本技能
    private Move fastMove;
    // 特別技能
    private Move chargeMove;

    /**
     * 建立寶可夢精靈
     *
     * @param image       圖片
     * @param name        名稱
     * @param level       等級
     * @param stamina     耐力
     * @param attack      攻擊力
     * @param defense     防禦力
     * @param catchChance 捕捉率
     * @param fastMove    基本技能
     * @param chargeMove  特別技能
     */
    public Pokemon(int image, String name, int level, int stamina, int attack, int defense, int catchChance, Move fastMove, Move chargeMove) {
        this.image = image;
        this.name = name;
        this.level = level;
        this.stamina = stamina;
        // 血 = 等級 x 耐力
        this.hp = getFullHp();
        this.attack = attack;
        this.defense = defense;
        this.catchChance = catchChance;
        this.fastMove = fastMove;
        this.chargeMove = chargeMove;
    }

    @Override
    public int attack(Pokemon enemy, Move move) {
        // 總傷害 = 攻擊力 + 技能傷害
        int totalDamage = getAttack() + move.getPower();
        // 敵人結果傷害 = 總傷害 - 敵人防禦，結果傷害為負值則改為0
        int resultDamage = totalDamage - enemy.getDefense();
        resultDamage = resultDamage >= 0 ? resultDamage : 0;

        // 敵人依照結果傷害計算損失的HP，HP為負值則改為0
        int hp = enemy.getHp() - resultDamage;
        enemy.setHp(hp > 0 ? hp : 0);
        return resultDamage;
    }

    @Override
    public String attackResult(Pokemon enemy, Move move) {
        double resultDamage = this.attack(enemy, move);
        String text = String.format(
                "[%s][%s]攻擊[%s]造成[%s]傷害, [%3$s]HP剩下[%s]",
                this.getName(), move.getName(), enemy.getName(), resultDamage, enemy.getHp());
        return text;
    }

    /**
     * 取得野生寶可夢
     * @return 回傳野生寶可夢
     */
    public static List<Pokemon> getFieldPokemons() {
        List<Pokemon> pokemons = new ArrayList<>();
        Pokemon zubat = new Pokemon(R.drawable.zubat,
                "超音蝠",
                1,
                80,
                83,
                76,
                40,
                new Move("咬住", 6),
                new Move("劇毒牙", 35)
        );
        Pokemon squirtle = new Pokemon(R.drawable.squirtle,
                "傑尼龜",
                1,
                88,
                94,
                112,
                16,
                new Move("撞擊", 10),
                new Move("水壓噴射", 45)
        );
        Pokemon charmander = new Pokemon(R.drawable.charmander,
                "小火龍",
                1,
                78,
                116,
                96,
                16,
                new Move("抓", 6),
                new Move("烈焰濺射", 70)
        );
        Pokemon farfetch_d = new Pokemon(R.drawable.farfetch_d,
                "大蔥鴨",
                1,
                104,
                124,
                118,
                24,
                new Move("空氣斬", 14),
                new Move("空氣利刃", 60)
        );

        pokemons.add(zubat);
        pokemons.add(squirtle);
        pokemons.add(charmander);
        pokemons.add(farfetch_d);
        return pokemons;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getStamina() {
        return stamina;
    }

    public void setStamina(int stamina) {
        this.stamina = stamina;
    }

    public int getHp() {
        return hp;
    }

    public int getFullHp() {
        return level * stamina;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getCatchChance() {
        return catchChance;
    }

    public void setCatchChance(int catchChance) {
        this.catchChance = catchChance;
    }

    public Move getFastMove() {
        return fastMove;
    }

    public void setFastMove(Move fastMove) {
        this.fastMove = fastMove;
    }

    public Move getChargeMove() {
        return chargeMove;
    }

    public void setChargeMove(Move chargeMove) {
        this.chargeMove = chargeMove;
    }


    public static List<Pokemon> getMyPokemons() {
        return myPokemons;
    }


    public static void addPokemon(Pokemon pokemon) {
        myPokemons.add(pokemon);
    }

}
