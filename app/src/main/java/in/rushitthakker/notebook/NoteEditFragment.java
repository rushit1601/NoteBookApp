package in.rushitthakker.notebook;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class NoteEditFragment extends Fragment {
    private ImageButton noteCatButton;
    private EditText title,message;
    private Note.Category savedButtonCategory;
    private AlertDialog categoryDialogObject,confirmDialogObject;
    private static final String MODIFIED_CATEGORY = "Modified category";
    private boolean newNote = false;
    private long noteID = 0;

    public NoteEditFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        if(bundle!=null){
            newNote = bundle.getBoolean(NoteDetailActivity.NEW_NOTE_EXTRA,false);
        }

        //Taking out the values from the previous state
        if(savedInstanceState!=null){
            savedButtonCategory = (Note.Category) savedInstanceState.get(MODIFIED_CATEGORY);
        }


        //Get references to each view and assign them to variables
        View fragmentLayout = inflater.inflate(R.layout.fragment_note_edit,container,false);
        title =(EditText) fragmentLayout.findViewById(R.id.editNoteTitle);
        message =(EditText) fragmentLayout.findViewById(R.id.editMessageNote);
        noteCatButton =(ImageButton) fragmentLayout.findViewById(R.id.noteEditButton);
        Button saveButton = (Button) fragmentLayout.findViewById(R.id.saveNote);

        //Populate widgets with note data
        Intent intent = getActivity().getIntent();
        title.setText(intent.getExtras().getString(MainActivity.NOTE_TITLE_EXTRA,""));
        message.setText(intent.getExtras().getString(MainActivity.NOTE_MESSAGE_EXTRA,""));
        noteID = intent.getExtras().getLong(MainActivity.NOTE_ID_EXTRA);


        if(savedButtonCategory!=null){
            noteCatButton.setImageResource(Note.categoryToDrawable(savedButtonCategory));
        }else if (!newNote) {
            Note.Category noteCat = (Note.Category) intent.getSerializableExtra(MainActivity.NOTE_CATEGORY_EXTRA);
            savedButtonCategory = noteCat;
            noteCatButton.setImageResource(Note.categoryToDrawable(noteCat));
        }



        buildCategoryDialog();

        noteCatButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                categoryDialogObject.show();
            }
        });

        buildConfirmDialog();
        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                confirmDialogObject.show();
            }
        });


        return fragmentLayout;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putSerializable(MODIFIED_CATEGORY,savedButtonCategory);
    }

    public void buildCategoryDialog(){
        final String [] categories = new String[] {"Personal","Technical","Quote","Finance"};
        AlertDialog.Builder categoryBuilder = new AlertDialog.Builder(getActivity());
        categoryBuilder.setTitle("Choose Note Type");

        categoryBuilder.setSingleChoiceItems(categories, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                categoryDialogObject.cancel();
                switch (item){
                    case 0:
                        savedButtonCategory = Note.Category.PERSONAL;
                        noteCatButton.setImageResource(R.drawable.p);
                        break;
                    case 1:
                        savedButtonCategory = Note.Category.TECHNICAL;
                        noteCatButton.setImageResource(R.drawable.t);
                        break;
                    case 2:
                        savedButtonCategory = Note.Category.QUOTE;
                        noteCatButton.setImageResource(R.drawable.q);
                        break;
                    case 3:
                        savedButtonCategory = Note.Category.FINANCE;
                        noteCatButton.setImageResource(R.drawable.f);
                        break;

                }
            }
        });

        categoryDialogObject = categoryBuilder.create();
    }

    private void buildConfirmDialog(){
        AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(getActivity());
        confirmBuilder.setTitle("Save changes");
        confirmBuilder.setMessage("Are you sure you want to save the changes to the note?");

        confirmBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("Save Note"," Note title: "+title.getText()+" Note Message: "+message.getText() + " Note Category: " + savedButtonCategory);


                NoteBookDbAdapter dbAdapter = new NoteBookDbAdapter(getActivity().getBaseContext());
                dbAdapter.open();
                //If it's a new note create
                if(newNote){
                    dbAdapter.createNote( title.getText() + "", message.getText() + "", (savedButtonCategory != null)?savedButtonCategory : Note.Category.PERSONAL );
                }else{
                    //else
                    dbAdapter.updateNote(noteID,title.getText() + "",message.getText() + "",savedButtonCategory);
                }

                dbAdapter.close();
                Intent intent = new Intent(getActivity(),MainActivity.class);
                startActivity(intent);
            }
        });

        confirmBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //No thing to do
            }
        });

        confirmDialogObject = confirmBuilder.create();
    }

}
