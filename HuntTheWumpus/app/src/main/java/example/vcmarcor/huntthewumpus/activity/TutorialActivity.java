package example.vcmarcor.huntthewumpus.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import example.vcmarcor.huntthewumpus.R;

/**
 * Created by victor on 9/11/17.
 *
 * This is the tutorial activity to show the user the basics of the game.
 */
public class TutorialActivity extends Activity implements View.OnClickListener {
    
    /**
     * Contains all tutorial slides with text and image.
     */
    private final Slide[] slides = {
            new Slide(R.string.tutorial_text1, android.R.color.transparent),
            new Slide(R.string.tutorial_text2, R.drawable.slide1),
            new Slide(R.string.tutorial_text3, R.drawable.slide2),
            new Slide(R.string.tutorial_text4, R.drawable.slide3),
            new Slide(R.string.tutorial_text5, R.drawable.slide4),
            new Slide(R.string.tutorial_text6, R.drawable.slide5),
            new Slide(R.string.tutorial_text7, R.drawable.slide6),
            new Slide(R.string.tutorial_text8, R.drawable.slide7),
            new Slide(R.string.tutorial_text9, R.drawable.slide8),
            new Slide(R.string.tutorial_text10, R.drawable.slide9),
            new Slide(R.string.tutorial_text11, R.drawable.slide10),
            new Slide(R.string.tutorial_text12, R.drawable.slide11),
            new Slide(R.string.tutorial_text13, R.drawable.slide12),
            new Slide(R.string.tutorial_text14, R.drawable.slide13),
            new Slide(R.string.tutorial_text15, R.drawable.slide14),
            new Slide(R.string.tutorial_text16, R.drawable.slide15),
            new Slide(R.string.tutorial_text17, R.drawable.slide16)
            
    };
    
    /**
     * This is the current slide number.
     */
    private int currentSlide;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
    
        // Setup the controls
        Button buttonNextSlide = findViewById(R.id.buttonNextSlide);
        buttonNextSlide.setOnClickListener(this);
        Button buttonPreviousSlide = findViewById(R.id.buttonPreviousSlide);
        buttonPreviousSlide.setOnClickListener(this);
        
        // Set the first slide
        currentSlide = 0;
        
        // Update the slide
        updateSlide();
    }
    
    /**
     * Updates the slide depending on the value of the current slide.
     */
    private void updateSlide() {
        // Set the text
        TextView tutorialMessage = findViewById(R.id.tutorialMessage);
        tutorialMessage.setText(slides[currentSlide].textResId);
    
        // Set the image
        ImageView tutorialImage = findViewById(R.id.tutorialImage);
        tutorialImage.setImageResource(slides[currentSlide].imageResId);
        
        Button buttonPreviousSlide = findViewById(R.id.buttonPreviousSlide);
        if(currentSlide <= 0) {
            // If the current slide is the first, the user can't go back, hide the button
            buttonPreviousSlide.setVisibility(View.INVISIBLE);
        }
        else {
            // Reset it to be visible in case it is not
            buttonPreviousSlide.setVisibility(View.VISIBLE);
        }
        
        Button buttonNextSlide = findViewById(R.id.buttonNextSlide);
        if(currentSlide >= slides.length - 1) {
            // If the current slide is the last, the user can't go any further, hide the button
            buttonNextSlide.setVisibility(View.INVISIBLE);
        }
        else {
            // Reset it to be visible in case it is not
            buttonNextSlide.setVisibility(View.VISIBLE);
        }
    }
    
    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.buttonNextSlide: {
                // Next slide button
                if(currentSlide < slides.length - 1) {
                    currentSlide++;
                }
                updateSlide();
                break;
            }
            
            case R.id.buttonPreviousSlide: {
                // Back slide button
                if(currentSlide > 0) {
                    currentSlide--;
                }
                updateSlide();
                break;
            }
        }
    }
    
    /**
     * This object represents a slide that contains an image and a text.
     */
    private class Slide {
    
        /**
         * The text resource Id.
         */
        private int textResId;
    
        /**
         * The image resource Id.
         */
        private int imageResId;
    
        /**
         * Constructor for the class.
         * @param textResId  text resource Id.
         * @param imageResId The image resource Id.
         */
        public Slide(final int textResId, final int imageResId) {
            this.textResId = textResId;
            this.imageResId = imageResId;
        }
    }
}
