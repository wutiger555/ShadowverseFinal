package idv.ron.oogame_poke.controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import idv.ron.oogame_poke.R;
import idv.ron.oogame_poke.model.Pokemon;

// 精靈球捕捉頁面
public class BallHuntActivity extends AppCompatActivity {
    private static final String TAG = "BallHuntActivity";
    private Pokemon fieldPokemon;
    private ImageView ivFieldPokemon, ivBall;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ball_hunt);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            fieldPokemon = (Pokemon) bundle.getSerializable("fieldPokemon");
        }
        String title = String.format(
                getString(R.string.textPokemonHunt),
                getString(R.string.textBallHunt), fieldPokemon.getName());
        setTitle(title);
        handleViews();
    }

    private void handleViews() {
        ivFieldPokemon = findViewById(R.id.ivFieldPokemon);
        ivFieldPokemon.setImageResource(fieldPokemon.getImage());

        ivBall = findViewById(R.id.ivBall);
        ivBall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 點擊精靈球後依照亂數與捕捉率來決定是否抓到寶可夢
                boolean catchSuccess = catchSuccess(fieldPokemon.getCatchChance());
                // 捕捉成功將精靈球設為disable，避免user繼續點擊攻擊
                if (catchSuccess) {
                    ivBall.setEnabled(false);
                }
                startCatchAnimation(catchSuccess);
            }
        });
    }

    private void startCatchAnimation(boolean catchSuccess) {
        // 設定精靈球動畫
        Animation ballAnimation = getBallTranslateAnimation(catchSuccess);
        ivBall.startAnimation(ballAnimation);

        if (catchSuccess) {
            // 抓到寶可夢，先存入我的百寶箱內
            Pokemon.getMyPokemons().add(fieldPokemon);
            // 晃動寶可夢的ImageView代表被有效攻擊
            Animation shakeAnimation = getShakeAnimation();
            shakeAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    // 當晃動完畢後寶可夢變成透明（代表被抓而消失了）
                    Animation alphaAnimation = getAlphaAnimation();
                    alphaAnimation.setFillAfter(true);
                    alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        // 當ImageView變透明後，Toast「抓到xxx」並關閉此頁回到前頁
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            if (toast != null) {
                                toast.cancel();
                            }
                            String text = String.format(
                                    getString(R.string.textPokemonCaught), fieldPokemon.getName());
                            toast = Toast.makeText(
                                    BallHuntActivity.this, text, Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, -200, 0);
                            toast.show();
                            finish();
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    ivFieldPokemon.startAnimation(alphaAnimation);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            ivFieldPokemon.startAnimation(shakeAnimation);
        } else {
            // 讓寶可夢暫時消失，代表沒抓到
            Animation alphaAnimation = getAlphaAnimation();
            alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                // 消失後Toast「逃跑了」
                @Override
                public void onAnimationEnd(Animation animation) {
                    // 之前已經有Toast，立即cancel後再重新產生
                    if (toast != null) {
                        toast.cancel();
                    }
                    String text = String.format(getString(R.string.textPokemonDodge), fieldPokemon.getName());
                    toast = Toast.makeText(BallHuntActivity.this, text, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 200, 0);
                    toast.show();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            ivFieldPokemon.startAnimation(alphaAnimation);
        }

    }

    private boolean catchSuccess(int catchChance) {
        // 產生1~100亂數
        int chance = (int) (Math.random() * 100) + 1;
        Log.d(TAG, "chance = " + chance + "; catchSuccess = " + catchChance);
        // 產生的亂數小於等於捕捉率即代表捕捉到
        return chance <= catchChance;
    }

    private TranslateAnimation getBallTranslateAnimation(boolean catchSuccess) {
        // 取得畫面高度
        View parentView = (View) ivBall.getParent();
        // 球要向上移動至畫面上方，所以y為負值，需要乘以-1
        float distance = -1 * (parentView.getHeight() - parentView.getPaddingBottom() -
                parentView.getPaddingTop() - ivBall.getHeight());
        long duration = 500;
        TranslateAnimation translateAnimation;
        // 如果抓寶可夢不成功，球移動至畫面上方；抓寶可夢成功，球停在寶可夢下方1/2處，動畫時間也僅為1/2
        if (catchSuccess) {
            distance = distance / 2 + (ivFieldPokemon.getHeight() / 2);
            duration /= 2;
            translateAnimation = new TranslateAnimation(0, 0, 0, distance);
            translateAnimation.setDuration(duration);
            translateAnimation.setFillAfter(true);
        } else {
            translateAnimation = new TranslateAnimation(0, 0, 0, distance);
            translateAnimation.setDuration(duration);
            translateAnimation.setRepeatMode(Animation.RESTART);
        }
        return translateAnimation;
    }

    private TranslateAnimation getShakeAnimation() {
        TranslateAnimation shakeAnimation = new TranslateAnimation(0, 10, 0, 0);
        shakeAnimation.setStartOffset(200);
        shakeAnimation.setDuration(500);
        CycleInterpolator cycleInterpolator = new CycleInterpolator(5);
        shakeAnimation.setInterpolator(cycleInterpolator);
        return shakeAnimation;
    }

    private AlphaAnimation getAlphaAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        alphaAnimation.setDuration(300);
        return alphaAnimation;
    }
}