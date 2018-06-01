package idv.ron.oogame_poke.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

import idv.ron.oogame_poke.R;
import idv.ron.oogame_poke.model.Pokemon;

// 首頁
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handleViews();
        changeCharacterImage();
    }

    private void handleViews() {
        // 取得野生寶可夢後將圖片顯示在ImageView上
        List<Pokemon> fieldPokemons = Pokemon.getFieldPokemons();
        List<ImageView> imageViews = new ArrayList<>();
        ImageView imageView1 = findViewById(R.id.imageView1);
        ImageView imageView2 = findViewById(R.id.imageView2);
        ImageView imageView3 = findViewById(R.id.imageView3);
        ImageView imageView4 = findViewById(R.id.imageView4);
        imageViews.add(imageView1);
        imageViews.add(imageView2);
        imageViews.add(imageView3);
        imageViews.add(imageView4);
        for (int i = 0; i < imageViews.size(); i++) {
            final Pokemon fieldPokemon = fieldPokemons.get(i);
            imageViews.get(i).setImageResource(fieldPokemon.getImage());
            // 點擊野生寶可夢後跳出選單讓user選擇捕捉方式
            imageViews.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPopupMenu(fieldPokemon, view);
                }
            });
        }
    }

    private void showPopupMenu(final Pokemon fieldPokemon, View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.hunt_menu);
        // 如果沒有寶可夢，移除「使用我的寶可夢」選項，只能用精靈球來獵捕野生寶可夢
        if (Pokemon.getMyPokemons().size() == 0) {
            popupMenu.getMenu().removeItem(R.id.myPokemonHunt);
        }
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent();
                switch (item.getItemId()) {
                    // 選擇「使用我的寶可夢」會開啟百寶箱頁面供user挑選作戰用的寶可夢
                    case R.id.myPokemonHunt:
                        intent.setClass(MainActivity.this, PokemonActivity.class);
                        break;
                    // 預設是使用精靈球捕捉，會開啟精靈球捕捉頁面
                    default:
                        intent.setClass(MainActivity.this, BallHuntActivity.class);
                        break;
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable("fieldPokemon", fieldPokemon);
                intent.putExtras(bundle);
                startActivity(intent);
                return true;
            }
        });
        popupMenu.show();
    }


    // 點擊主角會開啟百寶箱
    public void onKetchumClick(View view) {
        Intent intent = new Intent(this, PokemonActivity.class);
        startActivity(intent);
    }



    //切換主角圖片
    private void changeCharacterImage(){
        RadioGroup rp_character = findViewById(R.id.rp_character);
        final ImageView imageCharacter = findViewById(R.id.imageView0);
        final int []photos = {R.drawable.lucinda,R.drawable.serena};

        rp_character.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = radioGroup.findViewById(i);
                int index = radioGroup.indexOfChild(radioButton);
                imageCharacter.setImageResource(photos[index]);
            }
        });
    }
}
