package com.codepath.iClaim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;

import java.io.IOException;
import java.util.List;

public class FirebaseVisionActivity extends AppCompatActivity {

    private Context context;
    private TextView detectedTextView;

    public FirebaseVisionActivity(Context context, TextView detectedTextView ) {
        this.context = context;
        this.detectedTextView = detectedTextView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    public void firebaseVisionImage(Uri imageUri){
        InputImage image;
        try {
            image = InputImage.fromFilePath(context, imageUri);
            recognizeText(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void recognizeText(InputImage image){
        TextRecognizer recognizer = TextRecognition.getClient();

        Task<Text> result =
                recognizer.process(image)
                        .addOnSuccessListener(new OnSuccessListener<Text>() {
                            @Override
                            public void onSuccess(Text visionText) {
                                processText(visionText);

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                e.printStackTrace();
                                Toast.makeText(context, "Oops.. Error detecting total amount, please try again", Toast.LENGTH_SHORT).show();
                            }
                        });
    }

    private void processText(Text visionText) {

        String resultText = visionText.getText();
       // List<Text.TextBlock> blocks = visionText.getTextBlocks();

        if(visionText.getTextBlocks().isEmpty()){
            Toast.makeText(context, "No text found or Text may not be clear", Toast.LENGTH_LONG).show();
        }
        else {
            String blockText = "";
            //String lineText = "";
            for (Text.TextBlock block : visionText.getTextBlocks()){
                Point[] blockCornerPoints = block.getCornerPoints();
                Rect blockFrame = block.getBoundingBox();
                if(block.getText().contains("Total")) {
                    blockText = block.getText() + "\n";
                }
                //code commented for now, might use in future
                /*for (Text.Line line : block.getLines()) {
                    //lineText = lineText + line.getText() + "\n";
                    if(line.getText().contains("Total")) {
                        blockText = block.getText() + "\n";
                    }
                    Point[] lineCornerPoints = line.getCornerPoints();
                    Rect lineFrame = line.getBoundingBox();
                    for (Text.Element element : line.getElements()) {
                        String elementText = element.getText();
                        Point[] elementCornerPoints = element.getCornerPoints();
                        Rect elementFrame = element.getBoundingBox();
                    }
                }*/
            }
            detectedTextView.setText(blockText);

        }
    }
}