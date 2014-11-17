AndroidStateSaver
=================

Library for automatic state saving on Android

Simplifies the saving Activitys and Fragments states by annotating the fields that should be saved.
The annotated field should be either Parcelable (preferred Android way) or Serializable.
throws RuntimeException when trying to save final fields.
 
Example usage:

    public class MainActivity extends Activity {

        @SaveState
        SimpleObject simpleObject;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.main_activity);
            new Saver().restore(this, savedInstanceState);
            final TextView title = (TextView) findViewById(R.id.title);
            title.setText(text);
            final EditText input = (EditText) findViewById(R.id.input);
            findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    text = input.getText().toString();
                    title.setText(text);
                    }
                });
        }

        @Override
        protected void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            new Saver().save(this, outState);
        }
    }
    
    
 Possible improvements:
 1. {@link android.os.Bundle} size restriction according to OS params
 2. code generation during compile phase
 3. complex saving and restoring using use defined methods
 4. gradle deployment script
 5. Porting to v4 support library
 
 All the contributors are welcome!
