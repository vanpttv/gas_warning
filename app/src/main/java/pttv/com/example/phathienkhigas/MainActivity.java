package pttv.com.example.phathienkhigas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;

import static pttv.com.example.phathienkhigas.R.raw.sunlight_custom;

public class MainActivity extends AppCompatActivity {
    Button btn_off;
    TextView edt1, edt2, edt3, text_canhbao;
    ImageView img1, img2, img3;
    DatabaseReference myData;
    int so_phong;
    int t_so = 11;
    int val_1=0;
    int val_2=0;
    int val_3=0;
    int nguong=820;

    private static final String CONTENT_TITLE="Thông báo có rò rỉ khí gas";
    private static final String CONTENT_TEXT="Hệ thống nhận được thông báo có rò rỉ khí gas tại phòng ";
    public MediaPlayer mediaPlayer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img1 = (ImageView) findViewById(R.id.img1);
        img2 = (ImageView) findViewById(R.id.img2);
        img3 = (ImageView) findViewById(R.id.img3);
        btn_off = (Button) findViewById(R.id.btn_off);
        edt1 = (TextView) findViewById(R.id.edt1);
        edt2 = (TextView) findViewById(R.id.edt2);
        edt3 = (TextView) findViewById(R.id.edt3);
        text_canhbao = (TextView) findViewById(R.id.canhbao);

        myData = FirebaseDatabase.getInstance().getReference();

        myData.child("MQ2_Phong1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String val_sensor1 = snapshot.getValue().toString();
                val_1 = Integer.parseInt(val_sensor1.replaceAll("[\\D]", ""));
                int per_val1=(val_1*100)/4096;
                edt1.setText(per_val1+"%");
                so_phong = 1;
                t_so = 11;
                if (val_1 >= nguong) {
                    gui_thong_bao();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        myData.child("MQ2_Phong2").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String val_sensor2=snapshot.getValue().toString();
                val_2 = Integer.parseInt(val_sensor2.replaceAll("[\\D]", ""));
                int per_val2=(val_2*100)/4096;
                edt2.setText(per_val2+"%");
                so_phong = 2;
                t_so=21;
                if (val_2 >= nguong) {
                    gui_thong_bao();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        myData.child("MQ2_Phong3").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String val_sensor3=snapshot.getValue().toString();
                val_3= Integer.parseInt(val_sensor3.replaceAll("[\\D]", ""));
                int per_val3=(val_3*100)/4096;
                edt3.setText(per_val3+"%");
                so_phong= 3;
                t_so=31;
                if(val_3>=nguong) {
                    gui_thong_bao();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        myData.child("TT_CHUONG_1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String tt_chuong=snapshot.getValue().toString();
                if (tt_chuong.equals("ON")) {
                    so_phong=1;
                    t_so=11;
                    gui_canh_bao();
                    img1.setVisibility(View.VISIBLE);
                    xu_ly_chuong_canh_bao();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        myData.child("TT_CHUONG_2").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String tt_chuong=snapshot.getValue().toString();
                if(tt_chuong.equals("ON")){
                    so_phong=2;
                    t_so=22;
                    gui_canh_bao();
                    img2.setVisibility(View.VISIBLE);
                    xu_ly_chuong_canh_bao();;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        myData.child("TT_CHUONG_3").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String tt_chuong=snapshot.getValue().toString();
                if(tt_chuong.equals("ON")){
                    so_phong=3;
                    t_so=33;
                    gui_canh_bao();
                    img3.setVisibility(View.VISIBLE);
                    xu_ly_chuong_canh_bao();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void gui_thong_bao() {
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification thong_bao=new NotificationCompat.Builder(this, MyNotification.CHANNEL_ID)
                .setContentTitle(CONTENT_TITLE)
                .setContentText(CONTENT_TEXT+ so_phong)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(CONTENT_TEXT + so_phong))
                .setSmallIcon(R.drawable.icon_bao_dong)
                .setSound(uri)
                .setLargeIcon(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.icon_canh_bao), 128, 128, false))
                .build();

        NotificationManager notificationManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if(notificationManager!=null) notificationManager.notify(t_so, thong_bao);
    }

    public void gui_canh_bao() {
        Notification thong_bao_2=new NotificationCompat.Builder(this, MyNotification.CHANNEL_ID_2)
                .setContentTitle("Cảnh báo rò rỉ khí gas")
                .setContentText("Rò rỉ khí gas tại phòng "+ so_phong)
                .setSmallIcon(R.drawable.icon_bao_dong)
                .setLargeIcon(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.nguy_hiem_icon), 128, 128, false))
                .setSound(null)
                .build();

        NotificationManager notificationManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if(notificationManager!=null) notificationManager.notify(t_so, thong_bao_2);
    }

    public void start_notification(){
        if(mediaPlayer==null){
            mediaPlayer=MediaPlayer.create(this, sunlight_custom);
        }
        mediaPlayer.start();
    }

    public void stop_notification(){
        if(mediaPlayer!=null){
            mediaPlayer.release();
            mediaPlayer=null;
            Toast.makeText(this, "ĐÃ TẮT CHUÔNG CẢNH BÁO", Toast.LENGTH_SHORT).show();
        }
    }

    public void cancelNotification() {
        NotificationManager notificationManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    public void xu_ly_chuong_canh_bao(){
        start_notification();
        btn_off.setVisibility(View.VISIBLE);
        text_canhbao.setVisibility(View.VISIBLE);
        btn_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_off.setVisibility(View.INVISIBLE);
                img1.setVisibility(View.INVISIBLE);
                img2.setVisibility(View.INVISIBLE);
                img3.setVisibility(View.INVISIBLE);
                text_canhbao.setVisibility(View.INVISIBLE);
                stop_notification();
                cancelNotification();
            }
        });
    }
}